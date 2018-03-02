package cn.honry.oa.mail.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;

import cn.honry.utils.DateUtils;

import com.sun.mail.util.BASE64DecoderStream;


public class MailUtils {
    public static Logger log = Logger.getLogger(MailUtils.class);
    public static final String POP3 = "pop3";
    public static final String IMAP = "imap";
    public static final String SMTP = "smtp";
    private MimeMessage msg;
    private String headerEncoding;
    private String bodyEncoding = "gbk";;
    private Boolean isContainAttch = false;
    public static String saveAttchPath;
    private StringBuffer bodyText = new StringBuffer();
    private StringBuffer bodyHtml = new StringBuffer();
    private Map<String, InputStream> imapAttachMap = new HashMap<>();
    private Map<String, Object> attachMap = new HashMap<>();
    private Map<String, File> inlineMap = new HashMap<>();

    public MailUtils(MimeMessage msg) throws FolderClosedException {
        try {
            MimeMessage m = new MimeMessage(msg);
            String[] encodingHeader = m.getHeader("Content-Transfer-Encoding");
            if (encodingHeader != null && encodingHeader.length > 0) {
                this.headerEncoding = encodingHeader[0];
                if ("16bit".equalsIgnoreCase(encodingHeader[0])) {
                    m.removeHeader("Content-Transfer-Encoding");
                }
            }
            String contentType = m.getContentType().toLowerCase();
            if(contentType.contains("charset=")){
                bodyEncoding = contentType.substring(contentType.indexOf("charset=")+8).replace("\"","");
            }
            this.msg = m;
        } catch (FolderClosedException e) {
            e.printStackTrace();
            throw e;
        }catch (MessagingException e) {
            e.printStackTrace();
            this.msg = msg;
        }
    }

    /**
     * 设置保存附件地址
     * @param saveAttchPath
     */
    public void setSaveAttchPath(String saveAttchPath) {
        MailUtils.saveAttchPath = saveAttchPath;
    }
    /**
     * 获取邮件纯文本内容
     * @return
     */
    public String getBodyText(){
        return bodyText.toString();
    }

    /**
     * 获取邮件html内容
     * @return
     */
    public String getBodyHtml(){
        return bodyHtml.toString();
    }

    /**
     * 是否包含附件
     * @return
     */
    public Boolean getIsContainAttch() {
        return isContainAttch;
    }

    /**
     * 获取Imap邮件附件名称
     * @return
     */
    public Map<String, InputStream> getImapAttachMap() {
        return imapAttachMap;
    }

    /**
     * 获取Pop3邮件附件流
     * @return
     */
    public Map<String, Object> getAttachMap() {
        return attachMap;
    }

    /**
     * 获取邮件内嵌文件
     * @return
     */
    public Map<String, File> getInlineMap() {
        return inlineMap;
    }

    public Integer getPriority() throws MessagingException {
        String[] header = msg.getHeader("X-Priority");
        if (header != null && header.length>0) {
            return Integer.valueOf(header[0].substring(0,1));
        } else {
            return 3;
        }
    }
    public Boolean isNeedReceipt() throws MessagingException {
        String[] header = msg.getHeader("Disposition-Notification-To");
        if (header != null && header.length>0) {
            return true;
        } else {
            return false;
        }
    }
    public Boolean isAnswered() throws MessagingException {
        return msg.isSet(Flags.Flag.ANSWERED);
    }

    /**
     * 获取发送邮件者信息
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String getFrom() throws MessagingException, UnsupportedEncodingException{
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            return from;
        }
        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person);
            if(needTranscoding(person)){
                getHeaderEncoding(msg.getHeader("From")[0]);
                person = new String(person.getBytes("iso8859-1"),headerEncoding);
            }
            from = person + "<" + address.getAddress() + ">";
        } else {
            from = address.getAddress();
        }
        return from;
    }

    /**
     * 获取发送邮件者昵称
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String getSendName() throws MessagingException, UnsupportedEncodingException{
        String person = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            return person;
        }
        InternetAddress address = (InternetAddress) froms[0];
        person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person);
            if(needTranscoding(person)){
                getHeaderEncoding(msg.getHeader("From")[0]);
                person = new String(person.getBytes("iso8859-1"),headerEncoding);
            }
        } else {
            person = address.getAddress().substring(0, address.getAddress().lastIndexOf("@"));
        }
        return person;
    }
    /**
     * 获取接收邮件者昵称
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String getReceiveName() throws MessagingException, UnsupportedEncodingException {
        String mailaddr = "";
        InternetAddress[] address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.TO);
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                String person = address[i].getPersonal();
                if (person != null) {
                    person = MimeUtility.decodeText(person);
                    if (needTranscoding(person)) {
                        getHeaderEncoding(msg.getHeader("To")[0]);
                        person = new String(person.getBytes("iso8859-1"),bodyEncoding);
                    }
                    mailaddr += person + ";";
                }
            }
            if (mailaddr.endsWith(";")) {
                mailaddr = mailaddr.substring(0, mailaddr.lastIndexOf(";"));
            }
        } else {
            throw new RuntimeException("Error email Type!");
        }
        return mailaddr;
    }
    /**
     * 获取邮件收件人，抄送，密送的地址和信息。根据所传递的参数不同 "to"-->收件人,"cc"-->抄送人地址,"bcc"-->密送地址
     * @param type TO,CC,BCC
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String getMailAddress(String type) throws MessagingException, UnsupportedEncodingException{
        String mailaddr = "";
        String addrType = type.toUpperCase();
        InternetAddress[] address;
        if(addrType.equals("TO")){
            address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.TO);
        } else if(addrType.equals("CC")){
            address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.CC);
        } else if(addrType.equals("BCC")){
            address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.BCC);
        } else {
            throw new RuntimeException("Error email Type!");
        }
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                String mail = address[i].getAddress();
                if (StringUtils.isNotBlank(mail)) {
                    mail = MimeUtility.decodeText(mail);
                }
                String personal = address[i].getPersonal();
                if (StringUtils.isNotBlank(personal)) {
                    personal = MimeUtility.decodeText(personal);
                    if (needTranscoding(personal)) {
                        personal = new String(personal.getBytes("iso8859-1"),bodyEncoding);
                    }
                    mailaddr += personal + "<" + mail + ">" + ";";
                } else {
                    mailaddr += mail + ";";
                }
            }
            if (mailaddr.endsWith(";")) {
                mailaddr = mailaddr.substring(0, mailaddr.lastIndexOf(";"));
            }
        }
        return mailaddr;
    }

    /**
     * 获取邮件主题
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public String getSubject() throws UnsupportedEncodingException, MessagingException{
        String subject = msg.getSubject();
        if(subject == null){
            return "";
        }
        if(needTranscoding(subject)){
            getHeaderEncoding(msg.getHeader("Subject")[0]);
            subject = new String(subject.getBytes("iso8859-1"),bodyEncoding);
        }
        if("quoted-printable".equalsIgnoreCase(headerEncoding) && subject.contains("Q?") && subject.contains("?=")){
            subject = subject.substring(subject.indexOf("Q?")+2, subject.lastIndexOf("?="));
            subject = qpDecoding(subject);
        }
        return subject;
    }


    /**
     * 解析邮件，将得到的邮件内容保存到一个stringBuffer对象中，解析邮件 主要根据MimeType的不同执行不同的操作，一步一步的解析
     * @param part
     * @throws MessagingException
     * @throws IOException
     */
    public void getMailContent(Part part) throws MessagingException, IOException{
        String contentType = "";
        try {
            contentType = part.getContentType();
            int nameindex = contentType.indexOf("name");
            boolean conname = false;
            if(nameindex != -1){
                conname = true;
            }
            log.debug("CONTENTTYPE:"+contentType);
            if(part.isMimeType("text/plain")&&!conname){
                bodyText.append((String)part.getContent());
            }else if(part.isMimeType("text/html")&&!conname){
                bodyText.append((String)part.getContent());
            }else if(part.isMimeType("multipart/*")){
                Multipart multipart = (Multipart) part.getContent();
                int count = multipart.getCount();
                for(int i=0;i<count;i++){
                    getMailContent(multipart.getBodyPart(i));
                }
            }else if(part.isMimeType("message/rfc822")){
                getMailContent((Part) part.getContent());
            }
        } catch (MessagingException e) {
            // handling the bug
            if (part instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(e.getMessage())) {
                part = new MimeMessage((MimeMessage) part);
                getMailContent(part);
            } else {
                bodyText.append("无法获得该邮件正文！");
            }
        } catch (IOException e) {
            bodyText.append("无法获得该邮件正文！");
        }
    }

    /**
     * 判断邮件是否需要回执，如需回执返回true，否则返回false
     * @return
     * @throws MessagingException
     */
    public boolean getReplySign() throws MessagingException{
        boolean replySign = false;
        String needreply[] = msg.getHeader("Disposition-Notification-TO");
        if(needreply != null){
            replySign = true;
        }
        return replySign;
    }

    /**
     * 获取此邮件的message-id
     * @return
     * @throws MessagingException
     */
    public String getMessageId() throws MessagingException{
        return msg.getMessageID();
    }

    /**
     * 判断此邮件是否已读，如果未读则返回true，已读返回false
     * @return
     * @throws MessagingException
     */
    public boolean isNew() throws MessagingException{
        boolean isNew = true;
        Flags flags = msg.getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for(int i=0;i<flag.length;i++){
            if(flag[i]==Flags.Flag.SEEN){
                isNew = false;
                break;
            }
        }
        return isNew;
    }

    /**
     * 判断此邮件是星标，如果未读则返回true，已读返回false
     * @return
     * @throws MessagingException
     */
    public boolean isStar() throws MessagingException{
        return msg.isSet(Flags.Flag.FLAGGED);
    }

    /**
     * 判断是是否包含附件
     * @param part
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public boolean isContainAttch(Part part) throws MessagingException, IOException{
        boolean flag = false;
        try {
            String contentType = part.getContentType();
            if(part.isMimeType("multipart/*")){
                Multipart multipart = (Multipart) part.getContent();
                int count = multipart.getCount();
                for(int i=0;i<count;i++){
                    BodyPart bodypart = multipart.getBodyPart(i);
                    String dispostion = bodypart.getDisposition();
                    if((dispostion != null)&&(dispostion.equals(Part.ATTACHMENT)||dispostion.equals(Part.INLINE))){
                        flag = true;
                    }else if(bodypart.isMimeType("multipart/*")){
                        flag = isContainAttch(bodypart);
                    }else{
                        String conType = bodypart.getContentType();
                        if(conType.toLowerCase().contains("appliaction")){
                            flag = true;
                        }
                        if(conType.toLowerCase().contains("name")){
                            flag = true;
                        }
                    }
                }
            }else if(part.isMimeType("message/rfc822")){
                flag = isContainAttch((Part) part.getContent());
            }
        } catch (MessagingException e) {
            // handling the bug
            if (part instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(e.getMessage())) {
                part = new MimeMessage((MimeMessage) part);
                getMailContent(part);
            } else {
                bodyText.append("无法获得该邮件正文！");
            }
        } catch (IOException e) {
            bodyText.append("无法获得该邮件正文！");
        }
        return flag;
    }



    /**
     * 保存文件内容
     * @param fileName
     * @param inputStream
     * @throws IOException
     */
    public File saveFile(String fileName, InputStream inputStream) throws IOException {
        String time = DateUtils.formatDateYMDHMS(new Date());
        String osname = System.getProperty("os.name");
        String storedir = MailUtils.saveAttchPath;
        String sepatror = "";
        File storefile = null;
        if(osname == null){
            osname = "";
        }

        if(osname.toLowerCase().contains("win")){
            sepatror = "//";
            if(storedir==null||"".equals(storedir)){
                storedir = "c://temp";
            }
        }else{
            sepatror = "/";
            storedir = "/temp";
        }
        FileOutputStream out = null;
        try {
            storefile = new File(storedir + sepatror + time.substring(0, 8)+sepatror+time.substring(8, time.length())+sepatror+
                    fileName.replaceAll("\\?", "wenhao"));
            storefile.getParentFile().mkdirs();
            out = new FileOutputStream(storefile);
            int i;
            byte[] str_b = new byte[1024*8];
            while ((i = inputStream.read(str_b)) != -1) {
                System.out.println(i);
                out.write(str_b,0,i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null){
                    inputStream.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                inputStream = null;
                out = null;
            }
        }
        return storefile;

    }

    /**
     * 解析邮件
     *
     * @return
     * @throws Exception
     */
    public void parseMail(String receiveType) throws Exception {
        try{
            // 发件人信息
            Address[] froms = msg.getFrom();
            if (froms != null) {
                InternetAddress addr = (InternetAddress) froms[0];
                System.out.println("发件人地址:" + addr.getAddress());
                System.out.println("发件人显示名:" + addr.getPersonal());
            }
            System.out.println("邮件主题:" + getSubject());
            Object o = msg.getContent();
            if (o instanceof Multipart) {
                Multipart multipart = (Multipart) o;
                reMultipart(multipart, receiveType);
            } else if (o instanceof Part) {
                Part part = (Part) o;
                rePart(part, receiveType);
            } else {
                String contentType = MimeUtility.decodeText(msg.getContentType());
                if(contentType.contains("charset=")){
                    bodyEncoding = contentType.substring(contentType.indexOf("charset=")+8).replace("\"","");
                }
                if(contentType.toLowerCase().startsWith("text/plain")){
                    String text = MimeUtility.decodeText((String) o);
                    if(needTranscoding(text)){
                        text = new String(text.getBytes("iso8859-1"),bodyEncoding);
                    }
                    bodyText.append(text);
                }else if (contentType.toLowerCase().startsWith("text/html")) {
                    String html = MimeUtility.decodeText((String) o);
                    if(needTranscoding(html)){
                        html = new String(html.getBytes("iso8859-1"),bodyEncoding);
                    }
                    bodyHtml.append(html);
                }
            }
        } catch (MessagingException e) {
            if (!msg.getFolder().isOpen()) {
                msg.getFolder().open(Folder.READ_ONLY);
                parseMail(receiveType);
            } else if (msg instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(e.getMessage())) {
                msg = new MimeMessage(msg);
                parseMail(receiveType);
            } else {
                bodyText.append("无法获得该邮件正文！");
            }
        }
    }

    /**
     * @param part 解析内容
     * @throws Exception
     */
    public void rePart(Part part, String receiveType) throws Exception {
        Object content = part.getContent();
        String contentType = MimeUtility.decodeText(part.getContentType());
        if(contentType.contains("charset=")){
            bodyEncoding = contentType.substring(contentType.indexOf("charset=")+8).replace("\"","");
        }
        if (contentType.toLowerCase().startsWith("text/plain")) {
            String text = MimeUtility.decodeText((String) part.getContent());
            if(needTranscoding(text)){
                text = new String(text.getBytes("iso8859-1"),bodyEncoding);
            }
            bodyText.append(text);
            System.out.println("文本内容：" + text);
        } else if (contentType.toLowerCase().startsWith("text/html")) {
            String html = MimeUtility.decodeText((String) part.getContent());
            if(needTranscoding(html)){
                html = new String(html.getBytes("iso8859-1"),bodyEncoding);
            }
            bodyText.append(html);
            System.out.println("HTML内容：" + html);
        } else {
            if (Part.ATTACHMENT.equals(part.getDisposition())) {// 附件
                isContainAttch = true;
                String fileName = MimeUtility.decodeText(part.getFileName());
                System.out.println("发现附件: " + fileName);
                if (POP3.equals(receiveType)) {
                    InputStream in = part.getInputStream();// 打开附件的输入流
                    File file = this.saveFile(fileName, in);
                    attachMap.put(fileName, file);
                } else if (IMAP.equals(receiveType)) {
                    attachMap.put(fileName, part.getSize());
                }
            } else if (Part.INLINE.equals(part.getDisposition())) {// 内嵌资源
                String fileName = MimeUtility.decodeText(part.getFileName());
                System.out.println("发现内嵌资源: " + MimeUtility.decodeText(part.getFileName()));
                if(!fileName.contains(".")){
                    MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
                    MimeType type = allTypes.forName(contentType.split(";")[0]);
                    String ext = type.getExtension();
                    fileName += ext;
                }
                InputStream in = part.getInputStream();// 打开附件的输入流
                File file = this.saveFile(fileName, in);
                String contentID = ((MimeBodyPart) part).getContentID();
                if(contentID.contains("<") && contentID.contains(">")){
                    contentID = contentID.substring(contentID.indexOf("<")+1, contentID.indexOf(">"));
                }
                inlineMap.put(contentID, file);
            } else {//其他不规范的内嵌资源
                if (content instanceof BASE64DecoderStream) {
                    String contentID = ((MimeBodyPart) part).getContentID();
                    if(contentID.contains("<") && contentID.contains(">")){
                        contentID = contentID.substring(contentID.indexOf("<")+1, contentID.indexOf(">"));
                    }
                    String[] split = contentType.split(";");
                    if (split[0].toLowerCase().startsWith("image") && contentID != null) {
                        String fileName = part.getFileName();
                        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
                        MimeType type = allTypes.forName(contentType.split(";")[0]);
                        String ext = type.getExtension();
                        if (fileName == null || !fileName.contains(ext)) {
                            fileName = UUID.randomUUID().toString() + ext;
                        }
                        InputStream in = part.getInputStream();// 打开附件的输入流
                        File file = this.saveFile(fileName, in);
                        inlineMap.put(contentID, file);
                    }
                }
            }
        }
    }

    /**
     * @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)）
     * @throws Exception
     */
    private void reMultipart(Multipart multipart, String receiveType) throws Exception {
        // System.out.println("邮件共有" + multipart.getCount() + "部分组成");
        // 依次处理各个部分
        for (int j = 0, n = multipart.getCount(); j < n; j++) {
            // System.out.println("处理第" + j + "部分");
            Part part = multipart.getBodyPart(j);// 解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,
            // 也可能是另一个小包裹(MultipPart)
            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
            if (part.getContent() instanceof Multipart) {
                Multipart p = (Multipart) part.getContent();// 转成小包裹
                // 递归迭代
                reMultipart(p, receiveType);
            } else {
                rePart(part, receiveType);
            }
        }
    }
    public void downAttchMent() throws Exception {
        try{
            Object o = msg.getContent();
            if (o instanceof Multipart) {
                Multipart multipart = (Multipart) o;
                downAttchMent(multipart);
            } else if (o instanceof Part) {
                Part part = (Part) o;
                downAttchMent(part);
            }
        } catch (MessagingException e) {
            if (!msg.getFolder().isOpen()) {
                msg.getFolder().open(Folder.READ_ONLY);
                downAttchMent();
            } else if (msg instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(e.getMessage())) {
                msg = new MimeMessage((MimeMessage) msg);
                downAttchMent();
            }
        }
    }

    /**
     * 保存附件
     * @param part
     * @throws MessagingException
     * @throws IOException
     */
    private void downAttchMent(Part part) throws MessagingException, IOException {
        if (Part.ATTACHMENT.equals(part.getDisposition())) {// 附件
            String fileName = MimeUtility.decodeText(part.getFileName());
            System.out.println("发现附件: " + fileName);
            imapAttachMap.put(fileName, part.getInputStream());
        }
    }

    private void downAttchMent(Multipart multipart) throws Exception {
        for (int j = 0, n = multipart.getCount(); j < n; j++) {
            Part part = multipart.getBodyPart(j);// 解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,
            if (part.getContent() instanceof Multipart) {
                Multipart p = (Multipart) part.getContent();// 转成小包裹
                downAttchMent(p);
            } else {
                downAttchMent(part);
            }
        }
    }

    private void getHeaderEncoding(String str) throws MessagingException {
        if(str.startsWith("=?")){
            str = str.substring(2);
            headerEncoding = str.substring(0, str.indexOf("?"));
        }
    }
    //判断获得主题、发信收信人是否乱码
    private boolean needTranscoding(String str){
        boolean flag = false;
        byte b[];
        try {
            b = str.getBytes("ISO8859-1");
            for (int i  =  0;  i < b.length; i++) {
                byte b1 = b[i];
                if (b1  ==  63) {
                    break;//1
                } else if (b1 > 0) {
                    continue;//2
                } else if (b1 < 0) {//不可能为0，0为字符串结束符,小于0乱码
                    flag = true;
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            //  e.printStackTrace();
        }
        return flag;
    }

    //quoted-printable解码
    private String qpDecoding(String str) {
        if (str == null) {
            return "";
        }
        try {
            str = str.replaceAll("= ", "");
            byte[] bytes = str.getBytes("US-ASCII");
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                if (b != 95) {
                    bytes[i] = b;
                } else {
                    bytes[i] = 32;
                }
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i];
                if (b == '=') {
                    try {
                        int u = Character.digit((char) bytes[++i], 16);
                        int l = Character.digit((char) bytes[++i], 16);
                        if (u == -1 || l == -1) {
                            continue;
                        }
                        buffer.write((char) ((u << 4) + l));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                } else {
                    buffer.write(b);
                }
            }
            return new String(buffer.toByteArray(), bodyEncoding);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}