package cn.honry.statistics.classification.referralDown.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @className：ReferralDownAction
 * @Description：向下转诊申请
 * @Author：gaotiantian
 * @CreateDate：2017-6-2 上午9:44:17
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
@Namespace(value = "/statistics/classification")
public class ReferralDownAction extends ActionSupport{
	@Action(value = "referralDown", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/classification/referralDown/referralDown.jsp")},interceptorRefs = {@InterceptorRef(value = "manageInterceptor")})
	public String showReferralDown(){
		return "list";
	}

}

