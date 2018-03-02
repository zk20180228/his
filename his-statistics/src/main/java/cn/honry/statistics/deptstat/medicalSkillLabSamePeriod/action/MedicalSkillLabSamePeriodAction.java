package cn.honry.statistics.deptstat.medicalSkillLabSamePeriod.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.utils.DateUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @className：MedicalSkillLabSamePeriodAction
 * @Description：医技工作同期对比表(实验室)
 * @Author：gaotiantian
 * @CreateDate：2017-6-1 下午2:58:30
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/deptstat")
public class MedicalSkillLabSamePeriodAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menuAlias;
	private String Stime;
	
	public String getStime() {
		return Stime;
	}
	public void setStime(String stime) {
		Stime = stime;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@SuppressWarnings("deprecation")
	@Action(value = "medicalSkillLabSamePeriod", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/medicalSkillLabSamePeriod/medicalSkillLabSamePeriod.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMedicalSkillLabSamePeriod(){
		//获取时间
		Date date = new Date();
		Stime = DateUtils.formatDateYM(date)+"-01";
		ServletActionContext.getRequest().setAttribute("startTime", Stime);
		return "list";
	}
}

