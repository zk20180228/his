package cn.honry.statistics.deptstat.medicalSkillLabPeriod.action;

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
 * @className：MedicalSkillLabPeriodAction
 * @Description：医技工作时期对比表(实验室)
 * @Author：gaotiantian
 * @CreateDate：2017-6-1 下午2:34:44 
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
public class MedicalSkillLabPeriodAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menuAlias;
	private String startTime;
	private String endTime;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	@SuppressWarnings("deprecation")
	@Action(value = "medicalSkillLabPeriod", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/medicalSkillLabPeriod/medicalSkillLabPeriod.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String showMedicalSkillLabPeriod(){
		//获取时间
		Date date = new Date();
		startTime = DateUtils.formatDateYM(date)+"-01";
		endTime = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("startTime", startTime);
		ServletActionContext.getRequest().setAttribute("endTime", endTime);
		return "list";
	}
}

