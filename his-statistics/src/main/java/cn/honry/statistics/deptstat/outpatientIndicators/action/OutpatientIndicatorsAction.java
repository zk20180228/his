package cn.honry.statistics.deptstat.outpatientIndicators.action;

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
@Namespace(value="/statistics/outpatientIndicators")
public class OutpatientIndicatorsAction extends ActionSupport {
	private String startTime;
	private String endTime;
	
	@Action(value = "toView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/outpatientIndicators/outpatientIndicators.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView(){
		return "list";
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
}
