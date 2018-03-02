package cn.honry.statistics.deptstat.operationcontrast.action;

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
@Namespace(value="/statistics/operationContrast")
public class OperationContrastAction extends ActionSupport{
	private static final long serialVersionUID = 7750760091748132275L;
	
	/** 
     * 栏目别名,在主界面中点击栏目时传到action的参数
     */
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/**
	 * 手术及操作时期对比表
	 * @return
	 */
	@RequiresPermissions(value={"SSJCZSQDBB:function:view"})
	@Action(value = "listOperationContrast", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/operation/operationContrast.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationContrast() {
		
		return "list";
	}

}

