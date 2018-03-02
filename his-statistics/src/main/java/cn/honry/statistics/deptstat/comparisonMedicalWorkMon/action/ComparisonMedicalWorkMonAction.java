package cn.honry.statistics.deptstat.comparisonMedicalWorkMon.action;

import java.util.Date;

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
@Namespace(value = "/statistics/comparisonMedicalWorkMon")
public class ComparisonMedicalWorkMonAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String STime;
	private String menuAlias;
	public String getSTime() {
		return STime;
	}

	public void setSTime(String sTime) {
		STime = sTime;
	}
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 跳转到医技工作时期对比页面
	 * @return
	 */
	@Action(value = "comparisonMedicalWorkMon", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/comparisonMedicalWorkMon/comparisonMedicalWorkMon.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String comparisonMedicalWork() {
		Date date = new Date();
		STime = DateUtils.formatDateYM(date);
		return "list";
	}
}
