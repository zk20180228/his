package cn.honry.oa.mail.action;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.MailAttachment;
import cn.honry.base.bean.model.MailConfig;
import cn.honry.base.bean.model.MailMessage;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.oa.mail.dao.MailAttachmentDao;
import cn.honry.oa.mail.service.MailConfigService;
import cn.honry.oa.mail.service.MailMessageService;
import cn.honry.oa.mail.utils.MailUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.mail.util.MailConnectException;

/**
 * 邮件管理
 * 
 * @author zhuzhenkun
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/mail")
public class MailMessageAction extends ActionSupport{
    private static final long serialVersionUID = 1L;

    @Resource(name = "uploadFileService")
    private UploadFileService uploadFileService;
    public void setUploadFileService(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }
    @Resource(name = "mailMessageService")
    private MailMessageService mailMessageService;
    public void setMailMessageService(MailMessageService mailMessageService) {
        this.mailMessageService = mailMessageService;
    }
    @Resource(name = "mailConfigService")
    private MailConfigService mailConfigService;
    public void setMailConfigService(MailConfigService mailConfigService) {
        this.mailConfigService = mailConfigService;
    }
    @Resource(name = "mailAttachmentDao")
    private MailAttachmentDao mailAttachmentDao;

    private MailMessage mailMessage;
    public MailMessage getMailMessage() {
        return mailMessage;
    }
    public void setMailMessage(MailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }
    private MailConfig mailConfig;
    public MailConfig getMailConfig() {
        return mailConfig;
    }
    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }
    private MailAttachment mailAttachment;
    public MailAttachment getMailAttachment() {
        return mailAttachment;
    }
    public void setMailAttachment(MailAttachment mailAttachment) {
        this.mailAttachment = mailAttachment;
    }
    private String cid;
    public void setCid(String cid) {
        this.cid = cid;
    }
    private Integer folder;
    public void setFolder(Integer folder) {
        this.folder = folder;
    }
    private Integer page;
    private Integer rows;
    public void setPage(Integer page) {
        this.page = page;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    private File uploadFile;
    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }
    private InputStream inputStream;
    public InputStream getInputStream() {
        return inputStream;
    }
    private String fileName;
    public String getFileName() {
        return fileName;
    }
    private Integer fileSize;
    public Integer getFileSize() {
        return fileSize;
    }
    private Integer markType;
    public Integer getMarkType() {
        return markType;
    }
    public void setMarkType(Integer markType) {
        this.markType = markType;
    }
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 测试发信
     */
    @Action("testSend")
    public void testSend() {
        Map<String, Object> map = new HashMap<>();
        try {
            this.mailMessageService.createStore(mailConfig);
            this.mailMessageService.testSend(mailConfig);
            map.put("resCode", "success");
        } catch (MailConnectException e) {
            map.put("resCode", "error");
            map.put("resMsg", "连接邮箱服务器失败！");
            e.printStackTrace();
        } catch (AuthenticationFailedException e) {
            if (e.getMessage().contains("A secure connection is requiered(such as ssl)")) {
                map.put("resCode", "needSsl");
                map.put("resMsg", "必须使用SSL加密！");
            } else {
                map.put("resCode", "error");
                map.put("resMsg", "账号或密码错误！");
            }
            e.printStackTrace();
        } catch (EmailException e) {
            Throwable cause = e.getCause();
            if (cause != null){
                cause.printStackTrace();
                if (cause instanceof MailConnectException) {
                    map.put("resCode", "error");
                    map.put("resMsg", "连接邮箱服务器失败！");
                } else if (cause instanceof AuthenticationFailedException) {
                    if (e.getMessage().contains("A secure connection is requiered(such as ssl)")) {
                        map.put("resCode", "needSsl");
                        map.put("resMsg", "必须使用SSL加密！");
                    } else {
                        map.put("resCode", "error");
                        map.put("resMsg", "账号或密码错误！");
                    }
                } else {
                    map.put("resCode", "error");
                    map.put("resMsg", "配置错误！");
                }
            } else {
                e.printStackTrace();
                map.put("resCode", "error");
                map.put("resMsg", "配置错误！");
            }
        } catch (MessagingException e) {
            if (e.getMessage().contains("NO EXAMINE Unsafe Login. Please contact kefu@188.com for help")) {
                map.put("resCode", "163");
                map.put("resMsg", "网易邮箱限制imap收信，是否前往官网解除限制？");
                map.put("href", "http://config.mail.163.com/settings/imap/login.jsp?uid=" + mailConfig.getEmail());
            }
            e.printStackTrace();
        } catch (Throwable e) {
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请检查！");
            e.printStackTrace();
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }
    /**
     * 发信
     */
    @Action("send")
    public void send() {
        Map<String, Object> map = new HashMap<>();
        try {
            MailConfig config = this.mailConfigService.queryById(this.mailMessage.getCid());
            if (config != null) {
                this.mailMessageService.send(config, this.mailMessage);
                map.put("resCode", "success");
            } else {
                map.put("resCode", "error");
                map.put("resMsg", "邮箱不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请检查！");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }
    /**
     * 存为草稿
     */
    @Action("saveToDraft")
    public void saveToDraft() {
        Map<String, Object> map = new HashMap<>();
        try {
            MailConfig config = this.mailConfigService.queryById(this.mailMessage.getCid());
            if (config != null) {
                this.mailMessageService.saveToDraft(config, this.mailMessage);
                map.put("resCode", "success");
            } else {
                map.put("resCode", "error");
                map.put("resMsg", "邮箱不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请检查！");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }
    /**
     * 收信
     */
    @Action("receive")
    public void receive() {
        Map<String, Object> map = new HashMap<>();
        try {
            MailConfig config = this.mailConfigService.queryById(cid);
            if (config != null) {
                this.mailMessageService.receive(config);
                map.put("resCode", "success");
            } else {
                map.put("resCode", "error");
                map.put("resMsg", "邮箱不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请检查！");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }

    /**
     * 搜索邮件
     */
    @Action("searchMail")
    public void searchMail(){
        Map<String, Object> map = new HashMap<>();
        try {
            List<MailMessage> list = this.mailMessageService.searchMail(content, page, rows);
            Integer total = this.mailMessageService.searchMailCount(content);
            map.put("total", total);
            map.put("rows", list);
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请检查！");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }

    /**
     * 查询该邮箱服务器中的邮件数
     */
    @Action("queryCountByCidFromServer")
    public void queryCountByCidFromServer() {
        Map<String, Object> map = new HashMap<>();
        try {
            MailConfig config = this.mailConfigService.queryById(cid);
            if (config != null) {
                this.mailMessageService.receive(config);
                Integer count = this.mailMessageService.queryCountByCidFromServer(config);
                map.put("resCode", "success");
                map.put("data", count);
            } else {
                map.put("resCode", "error");
                map.put("resMsg", "邮箱不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请检查！");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }
    /**
     * 查询数据库中该邮箱的邮件数
     */
    @Action("queryCountByCid")
    public void queryCountByCid() {
        try {
            Integer count = this.mailMessageService.queryCountByCid(cid);
            WebUtils.webSendString(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 查看未读邮件
     */
    @Action("queryUnreadMail")
    public void queryUnreadMail() {
        try {
            List<MailMessage> list = this.mailMessageService.queryUnreadMail(cid, page, rows);
            Integer total = this.mailMessageService.queryCountByUnreadMail(cid);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("rows", list);
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查看星标邮件
     */
    @Action("queryStarBoxMail")
    public void queryStarBoxMail() {
        try {
            List<MailMessage> list = this.mailMessageService.queryStarBoxMail(cid, page, rows);
            Integer total = this.mailMessageService.queryCountByStarBoxMail(cid);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("rows", list);
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查看收件箱、草稿邮件、已发送邮件、已删除邮件、垃圾邮件
     */
    @Action("queryBox")
    public void queryBox() {
        try {
            List<MailMessage> list = this.mailMessageService.queryByFolder(cid, folder, page, rows);
            Integer total = this.mailMessageService.queryCountByFolder(cid, folder);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("rows", list);
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传附件
     */
    @Action("uploadFile")
    public void uploadFile() {
        String fileUploadUrl = HisParameters.getfileURI(ServletActionContext.getRequest());
        Map<String, Object> map = new HashMap<>();
        try{
            if (uploadFile.exists() && uploadFile.isFile()) {
                long size = uploadFile.length();//单位B字节
                if (size < 1024) {
                    map.put("size", "1K");
                } else if (size < 1024 * 1024) {//转为K
                    map.put("size", String.valueOf(size / 1024) + "K");
                } else if (size <= 20 * 1024 * 1024) {//转为M
                    map.put("size", String.valueOf(NumberUtil.init().format(size * 1.0 / (1024 * 1024), 2)) + "M");
                } else {
                    map.put("resCode", "error");
                    map.put("resMsg", "附件不能超过20M！");
                    return;
                }
                User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
                String url = this.uploadFileService.fileUpload(uploadFile, mailMessage.getAttachInfo(), "1", user.getAccount());
                map.put("resCode", "success");
                map.put("url", fileUploadUrl + url);
            } else {
                map.put("resCode", "error");
                map.put("resMsg", "附件不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常，请检查！");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }

    /**
     * 下载附件
     */
    @Action(value = "downAttach",
            results = {@Result(name = "download", type = "stream",
                    params = {
                            "contentType", "application/octet-stream;utf-8",
                            "inputName", "inputStream",
                            "contentDisposition", "attachment;filename=\"${fileName}\"",
                            "contentLength", "${fileSize}",
                            "bufferSize", "4096"
                    })})
    public String downAttach() {
        try {
            MailAttachment attach = mailAttachmentDao.get(mailAttachment.getId());
            this.fileSize = attach.getFileSize();
            MailMessage mailMessage = this.mailMessageService.queryByAid(mailAttachment.getId());
            MailConfig mailConfig = this.mailConfigService.queryById(mailMessage.getCid());
            if (MailUtils.POP3.equals(mailConfig.getReceiveType())) {//pop3收信时附件已经下载到本地服务器上
                String attachUrl = HisParameters.getfileURI(ServletActionContext.getRequest()) + attach.getFileUrl();
                URL url = new URL(attachUrl);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.connect();
                this.inputStream = huc.getInputStream();
            } else if (MailUtils.IMAP.equals(mailConfig.getReceiveType())) {
                this.inputStream = this.mailMessageService.downImapAttachByAid(mailConfig, mailMessage, attach.getFileName());
            }
            //设置文件名的编码
            if (ServletActionContext.getRequest().getHeader("user-agent").toLowerCase().contains("msie")) {
                this.fileName = URLEncoder.encode(attach.getFileName(), "UTF-8");//将不安全的文件名改为UTF-8格式
            } else {
                this.fileName = new String(attach.getFileName().getBytes("UTF-8"), "iso-8859-1");//火狐浏览器
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "download";
    }

    /**
     * 邮件标记为已读、未读、星标、等
     */
    @Action("markMail")
    public void markMail(){
        Map<String, Object> map = new HashMap<>();
        try {
            if(StringUtils.isBlank(mailMessage.getId())){
                map.put("resCode", "error");
                map.put("resMsg", "id不能为空！");
                return;
            }
            this.mailMessageService.markMail(mailMessage.getId(), markType);
            map.put("resCode", "success");
        }catch (Exception e){
            e.printStackTrace();
            map.put("resCode", "error");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }
    /**
     * 邮件移动到其他文件夹
     */
    @Action("moveFolder")
    public void moveFolder(){
        Map<String, Object> map = new HashMap<>();
        try {
            if(StringUtils.isBlank(mailMessage.getId())){
                map.put("resCode", "error");
                map.put("resMsg", "id不能为空！");
                return;
            }
            this.mailMessageService.moveFolder(mailMessage.getId(), markType);
            map.put("resCode", "success");
        }catch (Exception e){
            e.printStackTrace();
            map.put("resCode", "error");
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }

    /**
     * 查找上一封邮件
     */
    @Action("getLastMailById")
    public void getLastMailById(){
        Map<String, Object> map = new HashMap<>();
        try {
            MailMessage m;
            this.mailMessage = this.mailMessageService.queryById(this.mailMessage.getId());
            if(this.mailMessage == null){
                map.put("resCode", "error");
                map.put("data", "邮件不存在");
                return;
            }
            List<MailMessage> list = this.mailMessageService.queryByFolder(this.mailMessage.getCid(), this.mailMessage.getFolder());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(mailMessage.getId())) {
                    if (i == 0) {
                        m = list.get(0);
                        map.put("resCode", "firstOne");
                        map.put("data", m);
                    } else {
                        m = list.get(i-1);
                        map.put("resCode", "success");
                        map.put("data", m);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }

    /**
     * 查找下一封邮件
     */
    @Action("getNextMailById")
    public void getNextMailById(){
        Map<String, Object> map = new HashMap<>();
        try {
            MailMessage m;
            this.mailMessage = this.mailMessageService.queryById(this.mailMessage.getId());
            if(this.mailMessage == null){
                map.put("resCode", "error");
                map.put("data", "邮件不存在");
                return;
            }
            List<MailMessage> list = this.mailMessageService.queryByFolder(this.mailMessage.getCid(), this.mailMessage.getFolder());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(mailMessage.getId())) {
                    if (i < list.size()-1) {
                        m = list.get(i + 1);
                        map.put("resCode", "success");
                        map.put("data", m);
                    } else {
                        m = list.get(i);
                        map.put("resCode", "lastOne");
                        map.put("data", m);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }
}
