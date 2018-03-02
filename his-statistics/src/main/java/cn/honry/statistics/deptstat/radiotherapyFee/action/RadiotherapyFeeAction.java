package cn.honry.statistics.deptstat.radiotherapyFee.action;

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
 * <p>放疗病人及费用统计</p>
 * @Author: yuke
 * @CreateDate: 2017年7月6日 下午3:09:58 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月6日 下午3:09:58 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/radiotherapyFee")
public class RadiotherapyFeeAction extends ActionSupport{
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/**
	 * 放疗病人及费用统计view
	 * @return
	 */
	@Action(value = "radiotherapyFeelist", results = { 
			@Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/radiotherapyFee/radiotherapyFee.jsp") }
			,interceptorRefs = { @InterceptorRef(value = "manageInterceptor") }
			)
	public String radiotherapyFeelist() {
		return "list";
	}
}
