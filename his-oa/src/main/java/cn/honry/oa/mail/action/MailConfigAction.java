package cn.honry.oa.mail.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.MailConfig;
import cn.honry.oa.mail.service.MailConfigService;
import cn.honry.oa.mail.service.MailMessageService;
import cn.honry.oa.mail.vo.SenderVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.sun.mail.util.MailConnectException;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/oa/mail")
public class MailConfigAction {

    @Resource(name = "mailConfigService")
    private MailConfigService mailConfigService;
    public void setMailConfigService(MailConfigService mailConfigService) {
        this.mailConfigService = mailConfigService;
    }
    
    @Resource(name = "mailMessageService")
    private MailMessageService mailMessageService;
    public void setMailMessageService(MailMessageService mailMessageService) {
        this.mailMessageService = mailMessageService;
    }

    private MailConfig mailConfig;
    public MailConfig getMailConfig() {
        return mailConfig;
    }
    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }
    private String mid;
    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    private Integer page;
    private Integer rows;
    public void setPage(Integer page) {
        this.page = page;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Action("addOrUpdateConfig")
    public void addOrUpdateConfig() {
        Map<String, Object> map = new HashMap<>();
        try {
            this.mailMessageService.createStore(mailConfig);//验证收件配置
            this.mailMessageService.createTransport(mailConfig);//验证发件配置
            this.mailConfigService.addOrUpdate(mailConfig);
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
        } catch (MessagingException e) {
            if(e.getMessage().contains("NO EXAMINE Unsafe Login. Please contact kefu@188.com for help")){
                map.put("resCode", "163");
                map.put("resMsg", "网易邮箱限制imap收信，是否前往官网解除限制？");
                map.put("href", "http://config.mail.163.com/settings/imap/login.jsp?uid="+mailConfig.getEmail());
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
     * 删除邮箱配置，同时删除邮件和附件
     */
    @Action("deleteMailConfig")
    public void deleteMailConfig() {
        Map<String, Object> map = new HashMap<>();
        try {
            this.mailConfigService.delete(mailConfig);
            this.mailMessageService.deleteByCid(mailConfig.getId());
            map.put("resCode", "success");
        } catch (Exception e) {
            map.put("resCode", "error");
            map.put("resMsg", "删除失败！");
            e.printStackTrace();
        } finally {
            String json = JSONUtils.toJson(map);
            WebUtils.webSendJSON(json);
        }
    }
    
    /**
     * 根据员工号查询邮箱配置
     */
    @Action("queryByEmpCode")
    public void queryByEmpCode() {
        try {
            String empCode = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getCode();
            List<MailConfig> list = this.mailConfigService.queryByEmpCode(empCode, page, rows);
            for(MailConfig config : list){
                config.setPwd(null);
            }
            String json = JSONUtils.toJson(list);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Action("querySenderByEmpCode")
    public void querySenderByEmpCode() {
        try {
            String empCode = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getCode();
            List<SenderVo> list = this.mailConfigService.querySenderByEmpCode(empCode, mid);
            String json = JSONUtils.toJson(list);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 从邮箱配置表中查询邮箱
     */
    @Action("queryMailFromConf")
    public void queryComMail() {
        try {
            String empCode = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getCode();
            MailConfig config = this.mailConfigService.queryMailFromConf(empCode);
            if (config != null && config.getEmail() != null) {
                WebUtils.webSendString(config.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 从员工表查询email账号
     */
    @Action("queryMailFromEmp")
    public void queryMailFromEmp() {
        try {
            String empCode = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getCode();
            String email = this.mailConfigService.queryMailFromEmp(empCode);
            if (email != null) {
                WebUtils.webSendString(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取邮箱各文件夹
     */
    @Action("mailTree")
    public void mailTree() {
        try {
            String empCode = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getCode();
            List<TreeJson> tree = this.mailConfigService.mailTree(empCode);
            String json = JSONUtils.toJson(tree);
            WebUtils.webSendJSON(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取定时时间
     */
    @Action("queryReceiveTiming")
    public void queryReceiveTiming() {
        Map<String, Object> map = new HashMap<>();
        Integer timing = 0;
        try {
            MailConfig config = this.mailConfigService.queryById(this.mailConfig.getId());
            if(config == null){
                map.put("resCode", "error");
                map.put("resMsg", "邮箱不存在！");
                return;
            }
            timing = config.getTiming();
            if(timing == null || timing == 0){
                timing = 15;
            }
            map.put("resCode", "success");
            map.put("data", timing);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resCode", "error");
            map.put("resMsg", "服务异常！");
        } finally {
            String s = JSONUtils.toJson(map);
            WebUtils.webSendJSON(s);
        }
    }
}
