package cn.honry.statistics.classification.referralApproval.action;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@Namespace(value = "/classification/ReferralApproval")
public class ReferralApprovalAction extends ActionSupport {

	@RequiresPermissions(value = { "ZZSP:function:view" })
	@Action(value = "listReferralApproval", results = {
			@Result(name = "list", location = "/WEB-INF/pages/stat/classification/referralApproval/referralApproval.jsp") }, interceptorRefs = {
					@InterceptorRef(value = "manageInterceptor") })
	public String listRegisDocScheInfo() {
		return "list";

	}

}
