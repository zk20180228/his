package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

/**
 * oa邮箱配置
 * @author zhuzhenkun
 *
 */
public class MailConfig implements Serializable{

    private static final long serialVersionUID = 1L;

    /**主键**/
    private String id;
    /**邮箱类型**/
    private String type;
    /**员工id**/
    private String employeeCode;
    /**邮箱账号**/
    private String email;
    /**邮箱密码**/
    private String pwd;
    /**账户昵称**/
    private String nickName;
    /**收件类型**/
    private String receiveType;
    /**收件服务器**/
    private String receiveHost;
    /**收件服务器端口**/
    private Integer receivePort;
    /**收件安全连接协议**/
    private String receiveSecurity;
    /**发件服务器**/
    private String sendHost;
    /**发件服务器端口**/
    private Integer sendPort;
    /**发件安全连接协议**/
    private String sendSecurity;
    /**定时收取时间，单位分钟**/
    private Integer timing;
    /**是否是个人资料中的邮箱**/
    private Integer companyFlg;
    /**建立时间**/
    private Date createTime;
    /**修改时间**/
    private Date updateTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getEmployeeCode() {
        return employeeCode;
    }
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getReceiveType() {
        return receiveType;
    }
    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }
    public String getReceiveHost() {
        return receiveHost;
    }
    public void setReceiveHost(String receiveHost) {
        this.receiveHost = receiveHost;
    }
    public Integer getReceivePort() {
        return receivePort;
    }
    public void setReceivePort(Integer receivePort) {
        this.receivePort = receivePort;
    }
    public String getReceiveSecurity() {
        return receiveSecurity;
    }
    public void setReceiveSecurity(String receiveSecurity) {
        this.receiveSecurity = receiveSecurity;
    }
    public String getSendHost() {
        return sendHost;
    }
    public void setSendHost(String sendHost) {
        this.sendHost = sendHost;
    }
    public Integer getSendPort() {
        return sendPort;
    }
    public void setSendPort(Integer sendPort) {
        this.sendPort = sendPort;
    }
    public String getSendSecurity() {
        return sendSecurity;
    }
    public void setSendSecurity(String sendSecurity) {
        this.sendSecurity = sendSecurity;
    }
    public Integer getTiming() {
        return timing;
    }
    public void setTiming(Integer timing) {
        this.timing = timing;
    }
    public Integer getCompanyFlg() {
        return companyFlg;
    }
    public void setCompanyFlg(Integer companyFlg) {
        this.companyFlg = companyFlg;
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

}
