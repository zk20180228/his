package cn.honry.statistics.bi.bistac.sevenConfirmed.action;

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
@Namespace(value = "/statistics/sevenConfirmed")
public class SevenConfirmedAction extends ActionSupport {
	/**
	 * 七日确诊数据分析
	 * @return
	 */
	@Action(value = "sevenConfirmedlist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/bistac/sevenConfirmed.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String sevenConfirmedlist() {
		
		return "list";
	}
}

