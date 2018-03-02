package cn.honry.oa.mail.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DataSourceResolver;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceCompositeResolver;
import org.apache.commons.mail.resolver.DataSourceFileResolver;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MailAttachment;
import cn.honry.base.bean.model.MailConfig;
import cn.honry.base.bean.model.MailMessage;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.mail.dao.MailAttachmentDao;
import cn.honry.oa.mail.dao.MailConfigDao;
import cn.honry.oa.mail.dao.MailMessageDao;
import cn.honry.oa.mail.service.MailMessageService;
import cn.honry.oa.mail.utils.MailAuthenticator;
import cn.honry.oa.mail.utils.MailUtils;
import cn.honry.utils.HisParameters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

@Service("mailMessageService")
public class MailMessageServiceImpl implements MailMessageService{

    private static Logger logger = LoggerFactory.getLogger(MailMessageServiceImpl.class);
    @Resource(name = "uploadFileService")
    private UploadFileService uploadFileService;
    @Resource(name = "mailMessageDao")
    private MailMessageDao mailMessageDao;
    @Resource(name = "mailConfigDao")
    private MailConfigDao mailConfigDao;
    @Resource(name = "mailAttachmentDao")
    private MailAttachmentDao mailAttachmentDao;

    @Override
    public void receive(MailConfig config) throws MessagingException {
        Store store = this.createStore(config);
        Folder folder = null;
        if(MailUtils.POP3.equalsIgnoreCase(config.getReceiveType())){
            folder = store.getFolder("INBOX");//pop3只有INBOX
        } else if (MailUtils.IMAP.equalsIgnoreCase(config.getReceiveType())) {
            folder = store.getDefaultFolder();
        }
        this.receiveByFolder(folder, config);
        store.close();
    }

    private void receiveByFolder(Folder folder, MailConfig config) throws MessagingException {
        if ((Folder.HOLDS_FOLDERS & folder.getType()) != 0) {
            for (Folder childFolder : folder.list()) {
                try {
                    this.receiveByFolder(childFolder, config);
                } catch (Exception e) {
                    e.printStackTrace();
//                    continue;
                }
            }
        }
        if ((Folder.HOLDS_MESSAGES & folder.getType()) != 0) {
            // this.receiveMessageByFolder(folder, config);
            Integer folderType = getFolderType(config.getType(), folder.getFullName());
//            Integer lastReceivedUid = null;
            folder.open(Folder.READ_ONLY);
//            Message[] messages = folder.getMessages();
//            FetchProfile profile = new FetchProfile();
//            profile.add(UIDFolder.FetchProfileItem.UID);
//            profile.add(FetchProfile.Item.ENVELOPE);
//            folder.fetch(messages, profile);
            int count = folder.getMessageCount();
            //按时间由近到远遍历
            for (int i = count; i >= 1; i--) {
                if (!folder.isOpen()) {
                    folder.open(Folder.READ_ONLY);
                }
                MimeMessage mimeMessage = (MimeMessage) folder.getMessage(i);
                try{
                    String uid = "";
                    if (folder instanceof POP3Folder) {
                        uid = ((POP3Folder) folder).getUID(mimeMessage);
                    } else if (folder instanceof IMAPFolder) {
                        uid = Long.toString(((IMAPFolder) folder).getUID(mimeMessage));
//                        if (lastReceivedUid == null) {//只查一次
//                            lastReceivedUid = mailMessageDao.queryImapLastReceivedUid(config.getId(), folderType);
//                        }
//                        //数据库中的uid大于等于服务器中邮件的uid代表数据库已存有该邮件
//                        if (lastReceivedUid!=null && lastReceivedUid >= Integer.valueOf(uid)) {
//                            //数据库存在该邮件，比较数据库邮件数量与服务器邮件数量，大于等于则不再接收
//                            Integer localCount = this.queryCountByFolder(config.getId(), folderType);
//                            if (localCount >= count) {
//                                break;
//                            } else {
//                                continue;
//                            }
//                        }
                    }
                    MailMessage m = this.mailMessageDao.queryByUid(config.getId(), folderType, uid);
                    if (m != null) {
                        //数据库存在该邮件，比较数据库邮件数量与服务器邮件数量，大于等于则不再接收
                        Integer localCount = this.queryCountByFolder(config.getId(), folderType);
                        if (localCount >= count) {
                            break;
                        } else {
                            continue;
                        }
                    }
//                    this.receive(config.getId(), receiveType, folderType, uid, mimeMessage);
                    this.receive(config, folderType, uid, mimeMessage);
                }catch (FolderClosedException e) {
                    e.printStackTrace();
                    folder.open(Folder.READ_ONLY);
                    i++;
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        if (folder.isOpen()) {
            folder.close(false);
        }
    }

    private void receive(MailConfig mailConfig, Integer folderType, String uid, MimeMessage message) throws Exception {
        String content = null;
        MailUtils mailUtils = new MailUtils(message);
        String subject = mailUtils.getSubject();
        MailMessage mailMessage = new MailMessage();
        if (subject.length() > 330) {
            subject = subject.substring(0, 330);
        }
        mailMessage.setCid(mailConfig.getId());
        mailMessage.setReceiveType(mailConfig.getReceiveType());
        mailMessage.setUidd(uid);
        mailMessage.setSubject(subject);
        mailMessage.setSendEmail(mailUtils.getFrom());
        mailMessage.setSendName(mailUtils.getSendName());
        mailMessage.setReceiveEmail(mailUtils.getMailAddress("TO"));
        mailMessage.setReceiveName(mailUtils.getReceiveName());
        if (message.getSentDate() != null && message.getReceivedDate() != null) {
            mailMessage.setSendDate(message.getSentDate());
            mailMessage.setReceiveDate(message.getReceivedDate());
        } else if (message.getSentDate() != null && message.getReceivedDate() == null) {
            mailMessage.setSendDate(message.getSentDate());
            mailMessage.setReceiveDate(message.getSentDate());
        } else if (message.getSentDate() == null && message.getReceivedDate() != null) {
            mailMessage.setSendDate(message.getReceivedDate());
            mailMessage.setReceiveDate(message.getReceivedDate());
        } else {
            mailMessage.setSendDate(new Date());
            mailMessage.setReceiveDate(new Date());
        }
        mailMessage.setCc(mailUtils.getMailAddress("CC"));
        mailMessage.setBcc(mailUtils.getMailAddress("BCC"));
        mailMessage.setStarFlg(mailUtils.isStar()?1:0);
        mailMessage.setFolder(folderType);
        mailMessage.setOldFolder(message.getFolder().toString());
        mailMessage.setUnreadFlg(mailUtils.isNew()?0:1);
        mailMessage.setEmailSize(message.getSize());
        mailMessage.setPriority(mailUtils.getPriority());
        mailMessage.setReceipt(mailUtils.isNeedReceipt()?1:0);
        mailMessage.setAnswered(mailUtils.isAnswered()?1:0);
        mailMessage.setCreateTime(new Date());
        //解析邮件
        mailUtils.parseMail(mailConfig.getReceiveType());
        mailMessage.setAttachFlg(mailUtils.getIsContainAttch()?1:0);
        if(StringUtils.isNoneBlank(mailUtils.getBodyText()) && StringUtils.isNoneBlank(mailUtils.getBodyHtml())){
            content = mailUtils.getBodyHtml();
        }else if (StringUtils.isNoneBlank(mailUtils.getBodyText()) && StringUtils.isBlank(mailUtils.getBodyHtml())) {
            content = mailUtils.getBodyText();
        }else if (StringUtils.isBlank(mailUtils.getBodyText()) && StringUtils.isNoneBlank(mailUtils.getBodyHtml())) {
            content = mailUtils.getBodyHtml();
        }

        Map<String, File> inlineMap= mailUtils.getInlineMap();
        List<String> imgList = new ArrayList<>();
        for (Entry<String, File> entry : inlineMap.entrySet()) {
            String url = this.uploadFileService.fileUpload(entry.getValue(), entry.getValue().getName(), "1",
                    mailConfig.getEmployeeCode());
            if (imgList.size() == 0) {
                imgList = getImg(content);
            }
            for (String img : imgList) {
                if (img.contains("src=\"cid:"+entry.getKey()) || img.contains("src=\'cid:"+entry.getKey())) {
                    String newImg = img.replaceAll("(src=(\"|\')cid:){1}(\\S)*(\"|\')", "src=\"" + url + "\"");
                    newImg = newImg.replaceAll("(_src=(\"|\')){1}(\\S)*(\"|\')", "");
                    content = content.replace(img, newImg);
                }
            }
        }
        mailMessage.setContent(content);
        String mid = mailMessageDao.insert(mailMessage);
        Map<String, Object> attachMap = mailUtils.getAttachMap();
        for (Entry<String, Object> entry : attachMap.entrySet()) {
            MailAttachment mailAttach = new MailAttachment();
            mailAttach.setId(null);
            mailAttach.setCid(mailConfig.getId());
            mailAttach.setMid(mid);
            mailAttach.setFileName(entry.getKey());
            mailAttach.setCreateTime(new Date());
            if (MailUtils.POP3.equals(mailConfig.getReceiveType())) {
                mailAttach.setFileSize((int) ((File)entry.getValue()).length());
                String url = this.uploadFileService.fileUpload((File) entry.getValue(), entry.getKey(), "1",
                        mailConfig.getEmployeeCode());
                mailAttach.setFileUrl(url);
            } else if(MailUtils.IMAP.equals(mailConfig.getReceiveType())) {
                mailAttach.setFileSize((Integer) entry.getValue());
            }
            this.mailAttachmentDao.save(mailAttach);
        }
    }
    //    @Override
//    public void receiveAttach(MailAttachmentVo mailAttachmentVo) throws Exception {
//        Folder folder = mailAttachmentVo.getMessage().getFolder();
//        if(!folder.isOpen()){
//            folder.open(Folder.READ_ONLY);
//        }
//        File attach = MailUtils.saveFile(MimeUtility.decodeText(mailAttachmentVo.getPart().getFileName()),
//                mailAttachmentVo.getPart().getInputStream());
//        String url = this.uploadFileService.fileUpload(attach, attach.getName());
//        MailAttachment mailAttach = new MailAttachment();
//        mailAttach.setId(null);
//        mailAttach.setMid(mailAttachmentVo.getMid());
//        mailAttach.setFileName(attach.getName());
//        mailAttach.setFileUrl(url);
//        this.mailAttachmentDao.save(mailAttach);
//    }
    private List<String> getImg(String content){
        List<String> imgList = new ArrayList<>();
        int indexOf = content.indexOf("<img");
        if (indexOf != -1) {
            String substring = content.substring(indexOf, content.indexOf(">",indexOf)+1);
            imgList.add(substring);
            content = content.replace(substring, "");
            List<String> img = getImg(content);
            imgList.addAll(img);
        } else {
            return imgList;
        }
        return imgList;
    }
    //根据不同的邮箱的各文件夹名称转为自定义的文件夹类型
    private Integer getFolderType(String emailType, String folder) {
        ObjectMapper mapper = new ObjectMapper();
        try {
//            ClassPathResource classPathResource = new ClassPathResource("/mail.json");
//            JsonNode jsonNode = mapper.readTree(classPathResource.getInputStream());
            InputStream inputStream = this.getClass().getResourceAsStream("/mail.json");
            JsonNode jsonNode = mapper.readTree(inputStream);
            if (jsonNode.has(folder)) {
                return jsonNode.get(folder).asInt();
            } else if (jsonNode.has(emailType)) {
                jsonNode = jsonNode.get(emailType);
                if (jsonNode.has(folder)) {
                    return jsonNode.get(folder).asInt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
    //根据自定义的文件夹类型转为不同的邮箱的各文件夹名称
    private String getOldFolder(String emailType, Integer foldeType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ClassPathResource classPathResource = new ClassPathResource("mail.json");
            JsonNode jsonNode = mapper.readTree(classPathResource.getInputStream());
            Iterator<Entry<String, JsonNode>> iterator = jsonNode.fields();
            while (iterator.hasNext()) {
                Entry<String, JsonNode> entry = iterator.next();
                if(foldeType == entry.getValue().asInt()){
                    return entry.getKey();
                }
            }
            if (jsonNode.has(emailType)) {
                jsonNode = jsonNode.get(emailType);
                iterator = jsonNode.fields();
                while (iterator.hasNext()) {
                    Entry<String, JsonNode> entry = iterator.next();
                    if(foldeType == entry.getValue().asInt()){
                        return entry.getKey();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Store createStore(MailConfig config) throws MessagingException {
        Properties props = this.createReceiveProperties(config);
        Session session = Session.getInstance(props, new MailAuthenticator(config.getEmail(), config.getPwd()));
        session.setDebug(false);
        Store store = session.getStore(config.getReceiveType());
        store.connect(config.getEmail(), config.getPwd());
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);//163、126邮箱imap不能打开文件夹
        return store;
    }

    @Override
    public Transport createTransport(MailConfig config) throws MessagingException, EmailException {
        Properties props = this.createSmtpProperties(config);
        Session session = Session.getInstance(props, new MailAuthenticator(config.getEmail(), config.getPwd()));
        session.setDebug(false);
        Transport transport = session.getTransport(MailUtils.SMTP);
        transport.connect(config.getEmail(), config.getPwd());
        return transport;
    }
    private Properties createSmtpProperties(MailConfig config) {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", MailUtils.SMTP);
        props.setProperty("mail.smtp.host", config.getSendHost());
        props.setProperty("mail.smtp.port", config.getSendPort().toString());
        props.setProperty("mail.smtp.auth", "true");
        if ("ssl".equals(config.getSendSecurity())) {
//            props.setProperty("mail.smtp.ssl.enable", "true");
//            props.setProperty("mail.smtp.ssl.trust", config.getReceiveHost());
            props.setProperty("mail.smtp.socketFactory.port", config.getSendPort().toString());
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
        }
        return props;
    }
    private Properties createReceiveProperties(MailConfig config) {
        if(MailUtils.POP3.equalsIgnoreCase(config.getReceiveType())){
            return this.createPop3Properties(config);
        } else if (MailUtils.IMAP.equalsIgnoreCase(config.getReceiveType())) {
            return this.createImapProperties(config);
        }
        return new Properties();
    }
    private Properties createPop3Properties(MailConfig config) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", config.getReceiveType());
        props.setProperty("mail.pop3.host", config.getReceiveHost());
        props.setProperty("mail.pop3.port", config.getReceivePort().toString());
        props.setProperty("mail.pop3.auth", "true");
        if ("ssl".equals(config.getReceiveSecurity())) {
            props.setProperty("mail.pop3.ssl.enable", "true");
            props.setProperty("mail.pop3.ssl.trust", config.getReceiveHost());
        } else if ("ssl-all".equals(config.getReceiveSecurity())) {
            props.setProperty("mail.pop3.ssl.enable", "true");
            props.setProperty("mail.pop3.ssl.trust", "*");
        } else {
            logger.info("unsuppport : {}", config.getReceiveSecurity());
        }

        return props;
    }
    private Properties createImapProperties(MailConfig config) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", config.getReceiveType());
        props.setProperty("mail.imap.host", config.getReceiveHost());
        props.setProperty("mail.imap.port", config.getReceivePort().toString());
//        props.setProperty("mail.imap.auth", "true");
        if ("ssl".equals(config.getReceiveSecurity())) {
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.imap.ssl.trust", config.getReceiveHost());
            props.setProperty("mail.imap.ssl.checkserveridentity", "true");
        } else if ("ssl-all".equals(config.getReceiveSecurity())) {
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.imap.ssl.trust", "*");
        } else {
            logger.info("unsuppport : {}", config.getReceiveSecurity());
        }

        return props;
    }

    @Override
    public List<MailMessage> queryUnreadMail(String cid, Integer page, Integer rows) {
        return this.mailMessageDao.queryUnreadMail(cid, page, rows);
    }

    @Override
    public List<MailMessage> queryByFolder(String cid, Integer folder, Integer page, Integer rows) {
        return this.mailMessageDao.queryByFolder(cid, folder, page, rows);
    }

    @Override
    public List<MailMessage> queryByFolder(String cid, Integer folder) {
        return this.mailMessageDao.queryByFolder(cid, folder);
    }

    @Override
    public List<MailMessage> queryStarBoxMail(String cid, Integer page, Integer rows) {
        return this.mailMessageDao.queryStarBoxMail(cid, page, rows);
    }

    /**
     * 发信
     * @param mailConfig 邮箱配置对象
     * @param mailMessage 邮件信息对象
     * @throws Exception 异常
     */
    @Override
    public void send(MailConfig mailConfig, MailMessage mailMessage) throws Exception{
        HtmlEmail email = getEmail(mailConfig, mailMessage);
        email.send();
    }

    /**
     * 存为草稿
     * @param mailConfig 邮箱配置对象
     * @param mailMessage 邮件信息对象
     * @throws Exception 异常
     */
    @Override
    public void saveToDraft(MailConfig mailConfig, MailMessage mailMessage) throws Exception{
        MailMessage localMailMessage = null;
        if (StringUtils.isNotBlank(mailMessage.getId())) {
            localMailMessage = this.mailMessageDao.get(mailMessage.getId());
        }
        if (MailUtils.POP3.equalsIgnoreCase(mailConfig.getReceiveType())) {
            //pop3不能在服务器上添加邮件，只能本地数据库添加邮件
            mailMessage.setReceiveType(mailConfig.getReceiveType());
            mailMessage.setUidd(UUID.randomUUID().toString());
            mailMessage.setSendEmail(mailConfig.getEmail());
            mailMessage.setSendName(mailConfig.getNickName());
            String receiveEmail = mailMessage.getReceiveEmail();
            if (receiveEmail.contains("<")) {
                mailMessage.setReceiveName(receiveEmail.substring(0, receiveEmail.indexOf("<")));
            } else if (receiveEmail.contains("@")) {
                mailMessage.setReceiveName(receiveEmail.substring(0, receiveEmail.indexOf("@")));
            } else {
                mailMessage.setReceiveName(receiveEmail);
            }
            mailMessage.setSendDate(new Date());
            mailMessage.setStarFlg(0);
            mailMessage.setFolder(2);
            mailMessage.setUnreadFlg(1);
            if (localMailMessage == null) {//新邮件存为草稿
                mailMessage.setId(null);
                mailMessage.setCreateTime(new Date());
            } else {//已经存在的草稿邮件修改后再次存为草稿
                mailMessage.setCreateTime(localMailMessage.getCreateTime());
                mailMessage.setUpdateTime(new Date());
                this.mailAttachmentDao.deleteByMid(localMailMessage.getId());//删除修改前邮件的附件
            }
            String attachInfo = mailMessage.getAttachInfo();
            if (StringUtils.isNotBlank(attachInfo)) {
                mailMessage.setAttachFlg(0);
                this.mailMessageDao.save(mailMessage);//新增或修改
                String[] attachInfoArray = attachInfo.split("#");
                for (String attach : attachInfoArray) {
                    String[] att = attach.split(";");
                    MailAttachment mailAttach = new MailAttachment();
                    mailAttach.setId(null);
                    mailAttach.setCid(mailConfig.getId());
                    mailAttach.setMid(mailMessage.getId());
                    mailAttach.setFileName(att[0]);
                    mailAttach.setFileUrl(att[1]);
                    mailAttach.setFileSize(formateSize(att[2]));
                    mailAttach.setCreateTime(new Date());
                    this.mailAttachmentDao.save(mailAttach);
                }
            } else {
                mailMessage.setAttachFlg(1);
                this.mailMessageDao.save(mailMessage);//新增或修改
            }

        } else if (MailUtils.IMAP.equalsIgnoreCase(mailConfig.getReceiveType())) {
            HtmlEmail email = getEmail(mailConfig, mailMessage);
            email.buildMimeMessage();
            MimeMessage mimeMessage = email.getMimeMessage();
            String oldFolder = getOldFolder(mailConfig.getType(), 2);
            Store store = createStore(mailConfig);
            Folder folder = store.getFolder(oldFolder);
            folder.open(Folder.READ_WRITE);
            //草稿文件夹新增一封邮件
            folder.appendMessages(new Message[]{mimeMessage});
            if (localMailMessage != null) {//已经存在的草稿邮件修改后再次存为草稿,需删除本地和服务器中的修改前的草稿邮件
                this.deleteById(localMailMessage.getId());
                List<Message> messageList = new ArrayList<>();
                Message message = ((IMAPFolder) folder).getMessageByUID(Long.valueOf(localMailMessage.getUidd()));
                if (message != null) {
                    messageList.add(message);
                }
                Message[] msgs = messageList.toArray(new Message[messageList.size()]);
                folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
            }
            if (folder.isOpen()) {
                folder.close(true);
            }
            //调用收信，接收新增到服务器上的草稿邮件
            this.receive(mailConfig);
        }
    }

    private Integer formateSize(String s){
        if (s.endsWith("K")) {
            return Integer.valueOf(s.substring(0, s.lastIndexOf("K"))) * 1024;
        } else if (s.endsWith("M")) {
            return Integer.valueOf(s.substring(0, s.lastIndexOf("M"))) * 1024 * 1024;
        }
        return 0;
    }
    private HtmlEmail getEmail(MailConfig mailConfig, MailMessage mailMessage) throws Exception{
        String fileUploadUrl = HisParameters.getfileURI(ServletActionContext.getRequest());
        String to = mailMessage.getReceiveEmail();
        String cc = mailMessage.getCc();
        String bcc = mailMessage.getBcc();
        String subject = mailMessage.getSubject();
        String content = mailMessage.getContent();
        String attachInfo = mailMessage.getAttachInfo();
        Integer priority = mailMessage.getPriority();
        Integer receipt = mailMessage.getReceipt();
        HtmlEmail email;
        if (StringUtils.deleteWhitespace(content).toLowerCase().
                contains(StringUtils.deleteWhitespace("<img src=\"" + fileUploadUrl).toLowerCase())) {
            email = new ImageHtmlEmail();// ImageHtmlEmail 将本地服务器图片转化为内嵌图片
            DataSourceResolver[] dataSourceResolvers = new DataSourceResolver[] {
                    new DataSourceFileResolver(),// 添加DataSourceFileResolver用于解析本地图片
                    new DataSourceUrlResolver(new URL("http://")) };// 添加DataSourceUrlResolver用于解析网络图片，注意：new URL("http://")
            ((ImageHtmlEmail) email).setDataSourceResolver(new DataSourceCompositeResolver(dataSourceResolvers));
        } else {
            email = new HtmlEmail();// HtmlEmail 外部图片原样发送
        }
        if(StringUtils.isNotBlank(attachInfo)){
            String[] attachInfoArray = attachInfo.split("#");
            for (String attach : attachInfoArray) {
                String[] att = attach.split(";");
                EmailAttachment attachment = new EmailAttachment();
                attachment.setURL(new URL(att[1]));
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription(att[0]);
                attachment.setName(att[0]);
                email.attach(attachment);
            }
        }

        email.setDebug(false);// 可以看到执行过程的debug信息
        email.setCharset("UTF-8");// 防止乱码
        email.setSSLCheckServerIdentity(true);
        email.setSSLOnConnect("ssl".equalsIgnoreCase(mailConfig.getSendSecurity()));
        email.setHostName(mailConfig.getSendHost());
        if(email.isSSLOnConnect()){
            email.setSslSmtpPort(String.valueOf(mailConfig.getSendPort()));
        } else {
            email.setSmtpPort(mailConfig.getSendPort());
        }
        email.setFrom(mailConfig.getEmail(), mailConfig.getNickName());
        String[] toArray = to.split(";");
        for (String t : toArray){
            InternetAddress internetAddress = new InternetAddress(t);
            email.addTo(internetAddress.getAddress(), internetAddress.getPersonal());
        }
        if(StringUtils.isNotBlank(cc)){
            String[] ccArray = cc.split(";");
            for (String t : ccArray){
                InternetAddress internetAddress = new InternetAddress(t);
                email.addCc(internetAddress.getAddress(), internetAddress.getPersonal());
            }
        }
        if(StringUtils.isNotBlank(bcc)){
            String[] bccArray = bcc.split(";");
            for (String t : bccArray){
                InternetAddress internetAddress = new InternetAddress(t);
                email.addBcc(internetAddress.getAddress(), internetAddress.getPersonal());
            }
        }
        // 设置回复人(收件人回复此邮件时,默认收件人)
//        email.setReplyTo(InternetAddress.parse("\"" + MimeUtility.encodeText("田七") + "\" <chenxingxing745@163.com>"));
        if (priority != null) {
            // 设置优先级(1、最高 2、高、3、正常 4、低 5、最低)
            email.addHeader("X-Priority", String.valueOf(priority));
        }
        if (receipt != null && receipt == 1) {
            // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
            email.addHeader("Disposition-Notification-To", mailConfig.getEmail());
        }
        email.setAuthenticator(new DefaultAuthenticator(mailConfig.getEmail(), mailConfig.getPwd()));
        email.setSubject(subject);
        email.setHtmlMsg(content);
        email.setTextMsg("你的邮箱客户端不支持HTML格式邮件");
        return email;
    }
    @Override
    public Integer queryCountByCidFromServer(MailConfig config) throws MessagingException {
        Store store = this.createStore(config);
        Folder folder = store.getFolder("INBOX");
        logger.info("default folder : {}", folder);
        Integer count = folder.getMessageCount();
        store.close();
        return count;
    }

    @Override
    public Integer queryCountByCid(String cid) {
        return this.mailMessageDao.queryCountByCid(cid);
    }

    @Override
    public Integer queryCountByFolder(String cid, Integer folder) {
        return this.mailMessageDao.queryCountByFolder(cid, folder);
    }

    @Override
    public Integer queryCountByUnreadMail(String cid) {
        return this.mailMessageDao.queryCountByUnreadMail(cid);
    }

    @Override
    public Integer queryCountByStarBoxMail(String cid) {
        return this.mailMessageDao.queryCountByStarBoxMail(cid);
    }

    @Override
    public MailMessage queryById(String id) {
        return this.mailMessageDao.get(id);
    }

    @Override
    public void deleteByCid(String cid) {
        this.mailMessageDao.deleteByCid(cid);
        this.mailAttachmentDao.deleteByCid(cid);
    }

    @Override
    public void deleteById(String id) {
        this.mailMessageDao.deleteByid(id);
        this.mailAttachmentDao.deleteByMid(id);
    }

    @Override
    public List<MailAttachment> queryAttachById(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("mid", id);
        return mailAttachmentDao.findByObjectProperty(map);
    }

    @Override
    public MailMessage queryByAid(String aid) {
        return this.mailMessageDao.queryByAid(aid);
    }

    @Override
    public InputStream downImapAttachByAid(MailConfig mailConfig, MailMessage mailMessage, String fileName) throws Exception {
        Store store = this.createStore(mailConfig);
        Folder folder = store.getFolder(mailMessage.getOldFolder());
        folder.open(Folder.READ_ONLY);
        Message message = ((IMAPFolder) folder).getMessageByUID(Long.valueOf(mailMessage.getUidd()));
        MailUtils mailUtils = new MailUtils((MimeMessage) message);
        mailUtils.downAttchMent();
        Map<String, InputStream> attachMap = mailUtils.getImapAttachMap();
        for(Entry<String, InputStream> entry : attachMap.entrySet()){
            if(fileName.equals(entry.getKey())){
                return entry.getValue();
            }
        }
        store.close();
        return null;
    }

    @Override
    public void testSend(MailConfig mailConfig) throws EmailException, UnsupportedEncodingException {
        HtmlEmail email = new HtmlEmail();
        email.setDebug(false);
        email.setCharset("UTF-8");
        email.setSSLCheckServerIdentity(true);
        email.setSSLOnConnect("ssl".equalsIgnoreCase(mailConfig.getSendSecurity()));
        email.setHostName(mailConfig.getSendHost());
        if(email.isSSLOnConnect()){
            email.setSslSmtpPort(String.valueOf(mailConfig.getSendPort()));
        } else {
            email.setSmtpPort(mailConfig.getSendPort());
        }
        email.setFrom(mailConfig.getEmail(), mailConfig.getNickName());
        InternetAddress internetAddress = new InternetAddress(mailConfig.getEmail(), mailConfig.getNickName());
        email.addTo(internetAddress.getAddress(), internetAddress.getPersonal());
        email.setAuthenticator(new DefaultAuthenticator(mailConfig.getEmail(), mailConfig.getPwd()));
        email.setSubject("OA邮件系统测试");
        email.setHtmlMsg("OA邮件系统测试--发信成功！");
        email.send();
    }

    /**
     * imap邮件支持服务器标记，pop3邮件只支持彻底删除标记
     * @param ids 邮件id，多个id以英文逗号隔开
     * @param markType 标记类型，1、标记已读，2、标记未读，3、星标标记，4、取消星标，5、彻底删除
     * @throws Exception 异常
     */
    @Override
    public void markMail(String ids, Integer markType) throws Exception{
        List<MailMessage> mailList = new ArrayList<>();
        MailMessage mailMessage;
        MailConfig mailConfig = null;
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            mailMessage = this.mailMessageDao.get(id);
            if (mailConfig == null) {
                mailConfig = this.mailConfigDao.get(mailMessage.getCid());
            }
            if (MailUtils.IMAP.equalsIgnoreCase(mailMessage.getReceiveType())) {
                mailList.add(mailMessage);
            } else if (MailUtils.POP3.equalsIgnoreCase(mailMessage.getReceiveType())) {
                if (mailMessage.getFolder()!=2 && markType == 5) {//只有pop3的INBOX文件夹邮件可以从服务器中彻底删除
                    mailList.add(mailMessage);
                }
            }
            if (markType == 5) {//本地数据库删除
                this.deleteById(id);
            }
        }
        if (markType != 5) {//本地数据库标记
            this.markMailFromDB(ids, markType);
        }
        //TODO 开一个线程异步同步服务器数据，减少前台等待时间
        this.markMailFromServer(mailConfig, mailList, markType);
    }

    /**
     * imap邮件支持服务器移动文件夹，pop3邮件不支持服务器移动文件夹只在本地移动
     * @param ids 邮件id，多个id以英文逗号隔开
     * @param folderType 自定义文件夹类型，1、收件，2、草稿，3、已发送，4、已删除，5、垃圾
     * @throws Exception 异常
     */
    @Override
    public void moveFolder(String ids, Integer folderType) throws Exception {
        List<MailMessage> mailList = new ArrayList<>();
        String[] idArray = ids.split(",");
        MailMessage mailMessage = this.mailMessageDao.get(idArray[0]);
        MailConfig mailConfig = this.mailConfigDao.get(mailMessage.getCid());
        if (MailUtils.IMAP.equalsIgnoreCase(mailConfig.getReceiveType())) {
            for (String id : idArray) {
                mailMessage = this.mailMessageDao.get(id);
                mailList.add(mailMessage);
                this.deleteById(id);//imap收信移动邮件文件夹，先删除本地数据库的，再从服务器获取新的
            }
            //TODO 开一个线程异步同步服务器数据和本地数据库，减少前台等待时间
            this.moveFolderFromServer(mailConfig, mailList, folderType);
            this.receive(mailConfig);//调用收信方法，接收移动后的邮件
        } else if (MailUtils.POP3.equalsIgnoreCase(mailConfig.getReceiveType())) {
            this.moveFolderFromDB(ids, folderType);//移动本地数据库的邮件
        }
    }

    @Override
    public List<MailMessage> searchMail(String content, Integer page, Integer rows) {
        return this.mailMessageDao.searchMail(content, page, rows);
    }

    @Override
    public Integer searchMailCount(String content){
        return this.mailMessageDao.searchMailCount(content);
    }

    //邮箱服务器邮件移动文件夹
    private void moveFolderFromServer(MailConfig mailConfig, List<MailMessage> mailList, Integer folderType) {
        if (MailUtils.POP3.equalsIgnoreCase(mailConfig.getReceiveType())
                || mailList == null || mailList.size() == 0) {
            return;
        }
        List<Message> messageList = new ArrayList<>();
        try {
            Store store = this.createStore(mailConfig);
            Folder folder = store.getFolder(mailList.get(0).getOldFolder());
            folder.open(Folder.READ_WRITE);
            for (MailMessage mailMessage : mailList) {
                Message message = ((IMAPFolder) folder).getMessageByUID(Long.valueOf(mailMessage.getUidd()));
                if (message != null) {
                    messageList.add(message);
                }
                Message[] msgs = messageList.toArray(new Message[messageList.size()]);
                ((IMAPFolder)folder).moveMessages(msgs, store.getFolder(getOldFolder(mailConfig.getType(), folderType)));
            }
            if (folder.isOpen()) {
                folder.close(true);
            }
        } catch (MessagingException e) {//捕获异常，同步服务器邮件失败不影响本地流程
            e.printStackTrace();
        }
    }

    private void moveFolderFromDB(String ids, Integer folderType) {
        this.mailMessageDao.moveFolder(ids, folderType);
    }

    //邮箱服务器标记
    private void markMailFromServer(MailConfig mailConfig, List<MailMessage> mailList, Integer markType) {
        if(mailList == null || mailList.size() == 0){
            return;
        }
        List<Message> messageList = new ArrayList<>();
        try {
            Store store = this.createStore(mailConfig);
            Folder folder = store.getFolder(mailList.get(0).getOldFolder());
            folder.open(Folder.READ_WRITE);

            if (MailUtils.POP3.equalsIgnoreCase(mailConfig.getReceiveType())) {
                for (MailMessage mailMessage : mailList) {
                    int count = folder.getMessageCount();
                    for (int i = 1; i <= count; i++) {
                        Message m = folder.getMessage(i);
                        String uid = ((POP3Folder) folder).getUID(m);
                        if (mailMessage.getUidd().equals(uid)) {
                            messageList.add(m);
                            break;
                        }
                    }
                }
            } else if (MailUtils.IMAP.equalsIgnoreCase(mailConfig.getReceiveType())) {
                for (MailMessage mailMessage : mailList) {
                    Message message = ((IMAPFolder) folder).getMessageByUID(Long.valueOf(mailMessage.getUidd()));
                    if (message != null) {
                        messageList.add(message);
                    }
                }
            }
            Message[] msgs = messageList.toArray(new Message[messageList.size()]);
            Flags.Flag f = getFlag(markType);
            boolean set = true;
            if (markType == 2 || markType == 4) {
                set = false;
            }
            folder.setFlags(msgs, new Flags(f), set);
            if (folder.isOpen()) {
                folder.close(true);
            }
        } catch (MessagingException e) {//捕获异常，同步服务器邮件失败不影响本地流程
            e.printStackTrace();
        }
    }
    //本地数据库标记
    private void markMailFromDB(String ids, Integer markType) {
        this.mailMessageDao.markMail(ids, markType);
    }

    private Flags.Flag getFlag(Integer markType) {
        Flags.Flag f = null;
        switch (markType) {
            case 1://标记已读
                f = Flags.Flag.SEEN;
                break;
            case 2://标记未读
                f = Flags.Flag.SEEN;
                break;
            case 3://星标标记
                f = Flags.Flag.FLAGGED;
                break;
            case 4://取消星标
                f = Flags.Flag.FLAGGED;
                break;
            case 5://彻底删除
                f = Flags.Flag.DELETED;
                break;
            case 6:
                f = Flags.Flag.DRAFT;
                break;
        }
        return f;
    }

}
