package cn.honry.statistics.deptstat.diagnosisOfDepartment.action;

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
@Namespace(value = "/statistics/diagnosisOfDepartment")
public class DiagnosisOfDepartmentAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private String menuAlias;
	private String Stime;
	private String Etime;

	public String getStime() {
		return Stime;
	}

	public void setStime(String stime) {
		Stime = stime;
	}

	public String getEtime() {
		return Etime;
	}

	public void setEtime(String etime) {
		Etime = etime;
	}
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/**
	 * 跳转疾病诊断科室排序查询页面
	 * @return
	 */
	@Action(value = "diagnosisOfDepartmentlist", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/diagnosisOfDepartment/diagnosisOfDepartment.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String diagnosisOfDepartmentlist() {
		Date date = new Date();
		Stime = DateUtils.formatDateYM(date)+"-01";
		Etime = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("Stime", Stime);
		ServletActionContext.getRequest().setAttribute("Etime", Etime);
		return "list";
	}
}
