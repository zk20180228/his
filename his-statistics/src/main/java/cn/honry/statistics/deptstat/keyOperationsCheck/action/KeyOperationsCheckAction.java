package cn.honry.statistics.deptstat.keyOperationsCheck.action;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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

@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/keyOperationsCheck")
public class KeyOperationsCheckAction {
	 
	/** 
     * 开始时间
     */
	private String start;
	
	 /** 
     * 结束时间
     */
	private String end;

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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}


	/**  
	 *  
	 * @Description：  获取list页面
	 * @Author：ZXL
	 * @CreateDate：2016年6月22日 上午9:47:41 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("deprecation")
	@RequiresPermissions(value={"ZYZDSSJCHZ:function:view"})
	@Action(value = "listkeyOperationsCheck", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/deptstat/operation/keyOperationsCheck.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRegisDocScheInfo() {
		Date date = new Date();
		start = DateUtils.formatDateYM(date)+"-01";
		end = DateUtils.formatDateY_M_D(date);
		ServletActionContext.getRequest().setAttribute("start", start);
		ServletActionContext.getRequest().setAttribute("end", end);
		return "list";
		
	}
}
