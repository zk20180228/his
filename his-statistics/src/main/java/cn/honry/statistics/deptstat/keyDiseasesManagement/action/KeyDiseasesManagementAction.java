package cn.honry.statistics.deptstat.keyDiseasesManagement.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/keyDiseasesManagement")
public class KeyDiseasesManagementAction extends ActionSupport{
	private String startTime;
	private String endTime;
	private String menuAlias;
	
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

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

	/**
	 * 临床科室重点病种质控view
	 * @return
	 */
	@Action(value = "keyDiseasesManagementlist", results = { 
			@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/keyDiseasesManagement/keyDiseasesManagement.jsp") }
			,interceptorRefs = { @InterceptorRef(value = "manageInterceptor") }
			)
	public String radiotherapyFeelist() {
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		endTime=format.format(date);
		startTime=endTime.substring(0,7)+"-01";
		return "list";
	}
}
