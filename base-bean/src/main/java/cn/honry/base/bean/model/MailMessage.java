package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

/**
 * oa邮件
 * 
 * @author zhuzhenkun
 *
 */
public class MailMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键 **/
    private String id;
    /** 邮箱配置表主键 **/
    private String cid;
    /** 收件类型 **/
    private String receiveType;
    /** 标识邮箱账户的每一封邮件 **/
    private String uidd;
    /** 邮件主题 **/
    private String subject;
    /** 邮件正文 **/
    private String content;
    /** 发送人 **/
    private String sendEmail;
    /** 发送人昵称 **/
    private String sendName;
    /** 发件时间 **/
    private Date sendDate;
    /** 收件人 **/
    private String receiveEmail;
    /** 收件人昵称 **/
    private String receiveName;
    /** 收件时间 **/
    private Date receiveDate;
    /** 抄送人 **/
    private String cc;
    /** 秘密抄送人 **/
    private String bcc;
    /** 回执人 **/
    private String replyTo;
    /** 附件标记，0：没有 1：有 **/
    private Integer attachFlg;
    /** 星标标记，0：没有 1：有 **/
    private Integer starFlg;
    /** 新邮件文件夹类型，1、收件，2、草稿，3、已发送，4、已删除，5、垃圾 **/
    private Integer folder;
    /** 原邮件所在文件夹 **/
    private String oldFolder;
    /** 收件是否未读，0：未读 1：已读 **/
    private Integer unreadFlg;
    /** 邮件大小 **/
    private Integer emailSize;
    /** 优先级1、最高 2、高、3、正常 4、低 5、最低 **/
    private Integer priority;
    /** 是否需要回执，0、不需要 1需要 **/
    private Integer receipt;
    /** 是否已经回复 0、没有 1、已经回复 **/
    private Integer answered;
    /** 创建时间 **/
    private Date createTime;
    /** 更新时间，修改草稿邮件时更新该字段 **/
    private Date updateTime;
    // 冗余字段
    /** 附件信息 **/
    private String attachInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getUidd() {
        return uidd;
    }

    public void setUidd(String uidd) {
        this.uidd = uidd;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Integer getAttachFlg() {
        return attachFlg;
    }

    public void setAttachFlg(Integer attachFlg) {
        this.attachFlg = attachFlg;
    }

    public Integer getStarFlg() {
        return starFlg;
    }

    public void setStarFlg(Integer starFlg) {
        this.starFlg = starFlg;
    }

    public Integer getFolder() {
        return folder;
    }

    public void setFolder(Integer folder) {
        this.folder = folder;
    }

    public String getOldFolder() {
        return oldFolder;
    }

    public void setOldFolder(String oldFolder) {
        this.oldFolder = oldFolder;
    }

    public Integer getUnreadFlg() {
        return unreadFlg;
    }

    public void setUnreadFlg(Integer unreadFlg) {
        this.unreadFlg = unreadFlg;
    }

    public Integer getEmailSize() {
        return emailSize;
    }

    public void setEmailSize(Integer emailSize) {
        this.emailSize = emailSize;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getReceipt() {
        return receipt;
    }

    public void setReceipt(Integer receipt) {
        this.receipt = receipt;
    }

    public Integer getAnswered() {
        return answered;
    }

    public void setAnswered(Integer answered) {
        this.answered = answered;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(String attachInfo) {
        this.attachInfo = attachInfo;
    }
}
