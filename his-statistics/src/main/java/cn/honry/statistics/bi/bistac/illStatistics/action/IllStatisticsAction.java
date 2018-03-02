package cn.honry.statistics.bi.bistac.illStatistics.action;

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
@Namespace(value = "/statistics/illStatistics")
public class IllStatisticsAction extends ActionSupport {
	@Action(value = "illStatisticsList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/illStatistics.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String illStatistics() {

		return "list";
	}
}

