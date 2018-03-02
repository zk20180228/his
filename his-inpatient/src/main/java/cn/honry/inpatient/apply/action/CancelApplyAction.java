package cn.honry.inpatient.apply.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.hiasMongo.exception.service.HIASExceptionService;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 退费申请取消功能
 * @author  lyy
 * @createDate： 2016年2月17日 上午10:19:17 
 * @modifier lyy
 * @modifyDate：2016年2月17日 上午10:19:17  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/apply")
public class CancelApplyAction extends ActionSupport {
	private Logger logger=Logger.getLogger(CancelApplyAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	@RequiresPermissions(value={"TFSQQX:function:view"})
	@Action(value="listCancelApply",results={@Result(name="list",location="/WEB-INF/pages/drug/apply/cancelApplyList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public  String listDrugApply(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledte = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String first=simpledte.format(calendar.getTime());
        String end = simpledateformat.format(calendar.getTime());
        ServletActionContext.getRequest().setAttribute("first", first);
		ServletActionContext.getRequest().setAttribute("end",end);
		return "list";
	}
}
