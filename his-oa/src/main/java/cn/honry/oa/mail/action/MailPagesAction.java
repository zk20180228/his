package cn.honry.oa.mail.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
import cn.honry.oa.mail.service.MailConfigService;
import cn.honry.oa.mail.service.MailMessageService;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

/**
 * 邮件管理跳转页面action
 * @author zhuzhenkun
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/mail/page")
public class MailPagesAction {

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

    private MailConfig mailConfig;
    public MailConfig getMailConfig() {
        return mailConfig;
    }
    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    private MailMessage mailMessage;
    public MailMessage getMailMessage() {
        return mailMessage;
    }
    public void setMailMessage(MailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }
    private List<MailAttachment> mailAttachmentList;
    public List<MailAttachment> getMailAttachmentList() {
        return mailAttachmentList;
    }
    public void setMailAttachmentList(List<MailAttachment> mailAttachmentList) {
        this.mailAttachmentList = mailAttachmentList;
    }

    private String fileServersURL;
    public String getFileServersURL() {
        return fileServersURL;
    }
    public void setFileServersURL(String fileServersURL) {
        this.fileServersURL = fileServersURL;
    }
    
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    private String emailDiv;
    public String getEmailDiv() {
        return emailDiv;
    }
    public void setEmailDiv(String emailDiv) {
        this.emailDiv = emailDiv;
    }

    private List<MailConfig> mailConfigList;
    public List<MailConfig> getMailConfigList() {
        return mailConfigList;
    }
    public void setMailConfigList(List<MailConfig> mailConfigList) {
        this.mailConfigList = mailConfigList;
    }

    /**
     * 邮件管理主页
     */
    @Action(value = "index", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/index.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String index() {
        try {
            String empCode = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getCode();
            this.mailConfigList = this.mailConfigService.queryByEmpCode(empCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "list";
    }

    /**
     * 收件箱页面
     */
    @Action(value = "inBox", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/inBox.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String inBox() {
        return "list";
    }

    /**
     * 未读邮件页面
     */
    @Action(value = "unreadBox", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/unreadBox.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String unreadBox() {
        return "list";
    }

    /**
     * 已发送页面
     */
    @Action(value = "sendBox", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/sendBox.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String sendBox() {
        return "list";
    }

    /**
     * 星标邮件页面
     */
    @Action(value = "starBox", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/starBox.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String starBox() {
        return "list";
    }

    /**
     * 草稿箱页面
     */
    @Action(value = "draftBox", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/draftBox.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String draftBox() {
        return "list";
    }

    /**
     * 已删除页面
     */
    @Action(value = "deleteBox", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/deleteBox.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String deleteBox() {
        return "list";
    }

    /**
     * 垃圾箱页面
     */
    @Action(value = "spamBox", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/spamBox.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String spamBox() {
        return "list";
    }

    /**
     * 写信页面
     */
    @Action(value = "editMail", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/editMail.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String editMail() {
        this.fileServersURL = HisParameters.getfileURI(ServletActionContext.getRequest());
        return "list";
    }

    /**
     * 配置邮箱页面
     */
    @Action(value = "config", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/config.jsp") },
            interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String config(){
        return "list";
    }

    /**
     * 新建或修改邮箱页面
     */
    @Action(value = "editConfig", results = { @Result(name = "list", location = "/WEB-INF/pages/oa/mail/editConfig.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
    public String editConfig() {
        if (mailConfig != null && StringUtils.isNotBlank(mailConfig.getId())) {
            this.mailConfig = this.mailConfigService.queryById(mailConfig.getId());
            if (this.mailConfig == null) {
                this.mailConfig = new MailConfig();
            } else {
                this.mailConfig.setPwd(null);
            }
        }

        return "list";
    }

    @Action(value = "contentView", results = { @Result(name = { "list" }, location = "/WEB-INF/pages/oa/mail/mailView.jsp") }, interceptorRefs = { @InterceptorRef("manageInterceptor") })
    public String contentView() {
        try {
            MailMessage mailMessage = this.mailMessageService.queryById(id);
            if (mailMessage == null) {
                WebUtils.webSendString("邮件不存在，请刷新邮件列表");
                return null;
            }
            String fileUploadUrl = HisParameters.getfileURI(ServletActionContext.getRequest());
            if(StringUtils.isNoneBlank(mailMessage.getContent())){
                mailMessage.setContent(mailMessage.getContent().replaceAll("(src=(\"|\')){1}/upload/", "src=\""+fileUploadUrl + "/upload/"));
            }
            mailMessage.setSendEmail(mailMessage.getSendEmail().replace("<", "&lt").replace(">", "&gt"));
            mailMessage.setReceiveEmail(mailMessage.getReceiveEmail().replace("<", "&lt").replace(">", "&gt"));
            this.mailMessage = mailMessage;
//            List<MailAttachment> mailAttachment = this.mailMessageService.queryAttachById(id);
//            for(MailAttachment attach : mailAttachment){
//                attach.setFileUrl(attach.getFileUrl().replaceAll("/upload/", fileUploadUrl + "/upload/"));
//            }
            this.mailAttachmentList = this.mailMessageService.queryAttachById(id);
        } catch (Exception e) {
            e.printStackTrace();
//            this.logger.error("XTGL_TZGL", e);
//            this.hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("XTGL_TZGL",
//                    "系统管理_通知管理", "2", "0"), e);
        }
        return "list";
    }
    /**
     * 修改草稿邮件
     */
    @Action(value = "editDraft", results = { @Result(name = { "list" }, location = "/WEB-INF/pages/oa/mail/editMail.jsp") }, interceptorRefs = { @InterceptorRef("manageInterceptor") })
    public String editDraft() {
        MailMessage message = this.mailMessageService.queryById(id);
        if (message == null) {
            WebUtils.webSendString("邮件不存在，请刷新邮件列表");
            return null;
        }
        String fileUploadUrl = HisParameters.getfileURI(ServletActionContext.getRequest());
        message.setId(id);
        message.setSubject(message.getSubject()==null ? "" : message.getSubject());
        message.setContent(message.getContent().replaceAll("(src=(\"|\')){1}/upload/", "src=\""+fileUploadUrl + "/upload/"));
        this.mailMessage = message;
        this.mailAttachmentList = this.mailMessageService.queryAttachById(id);
        return "list";
    }
    /**
     * 转发邮件
     */
    @Action(value = "forwarding", results = { @Result(name = { "list" }, location = "/WEB-INF/pages/oa/mail/editMail.jsp") }, interceptorRefs = { @InterceptorRef("manageInterceptor") })
    public String forwarding() {
        MailMessage message = this.mailMessageService.queryById(id);
        if (message == null) {
            WebUtils.webSendString("邮件不存在，请刷新邮件列表");
            return null;
        }
        this.mailMessage = new MailMessage();
        String fileUploadUrl = HisParameters.getfileURI(ServletActionContext.getRequest());
        this.mailMessage.setId(id);
        this.mailMessage.setSubject("转发：" + (message.getSubject()==null ? "" : message.getSubject()));
        this.mailMessage.setContent(message.getContent().replaceAll("(src=(\"|\')){1}/upload/", "src=\""+fileUploadUrl + "/upload/"));
        this.mailAttachmentList = this.mailMessageService.queryAttachById(id);
        return "list";
    }
    /**
     * 回复邮件
     */
    @Action(value = "reply", results = { @Result(name = { "list" }, location = "/WEB-INF/pages/oa/mail/editMail.jsp") }, interceptorRefs = { @InterceptorRef("manageInterceptor") })
    public String reply() {
        MailMessage message = this.mailMessageService.queryById(id);
        if (message == null) {
            WebUtils.webSendString("邮件不存在，请刷新邮件列表");
            return null;
        }
        this.mailMessage = new MailMessage();
        this.mailMessage.setId(id);
        this.mailMessage.setReceiveEmail(message.getSendEmail());
        this.mailMessage.setSubject("回复：" + (message.getSubject()==null ? "" : message.getSubject()));
        return "list";
    }

    /**
     * 搜索邮件
     */
    @Action(value = "searchMailList", results = { @Result(name = { "list" }, location = "/WEB-INF/pages/oa/mail/searchMail.jsp") }, interceptorRefs = { @InterceptorRef("manageInterceptor") })
    public String searchMailList() {
        return "list";
    }

}
