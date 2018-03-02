package cn.honry.statistics.deptstat.comparisonMedicalWork.action;

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

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/comparisonMedicalWork")
public class ComparisonMedicalWorkAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String STime;
	private String ETime;
	private String menuAlias;
	
	public String getSTime() {
		return STime;
	}

	public void setSTime(String sTime) {
		STime = sTime;
	}

	public String getETime() {
		return ETime;
	}

	public void setETime(String eTime) {
		ETime = eTime;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 跳转到医技工作时间段同期对比页面
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "comparisonMedicalWork", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/comparisonMedical/comparisonMedical.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toComparisonMedicalWork() {
		Date date = new Date();
		STime = DateUtils.formatDateYM(date)+"-01";
		ETime = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("STime", STime);
		ServletActionContext.getRequest().setAttribute("ETime", ETime);
		return "list";
	}

}
