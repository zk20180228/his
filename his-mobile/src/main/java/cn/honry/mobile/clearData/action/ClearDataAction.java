package cn.honry.mobile.clearData.action;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.clearData.service.ClearDataService;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace("/mosys/clearData")
public class ClearDataAction extends ActionSupport{
	private Logger logger=Logger.getLogger(ClearDataAction.class);
	
	@Autowired
	@Qualifier(value = "exceptionLogService")
	private ExceptionLogService exceptionLogService;
	public void setExceptionLogService(ExceptionLogService exceptionLogService) {
		this.exceptionLogService = exceptionLogService;
	}
	@Autowired
	@Qualifier(value = "clearDataService")
	private ClearDataService clearDataService;
	public void setClearDataService(ClearDataService clearDataService) {
		this.clearDataService = clearDataService;
	}
	private String flag;
	
	@RequiresPermissions(value={"YWSJCSH:function:view"})
	@Action(value = "toVeiw", results = { @Result(name = "list", location = "/WEB-INF/pages/mobile/clearData/clearData.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toVeiw(){
		return "list";
	}
	
	@RequiresPermissions(value={"YWSJCSH:function:view"})
	@Action(value = "clear")
	public void clear(){
		String res = "success";
		try {
			clearDataService.clear(flag);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("YWYWSJCSH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YWYWSJCSH","业务数据初始化","2","1"), e);
			res = "error";
		}
		WebUtils.webSendString(res);
	}
	
	@RequiresPermissions(value={"YWSJCSH:function:view"})
	@Action(value = "sendMes")
	public void sendMes(){
		String res = "success";
		try {
			clearDataService.sendMes();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("YWSJCSH", e);
			exceptionLogService.saveExceptionInfoToMongo(new RecordToMobileException("YWSJCSH","业务数据初始化推送通知","2","1"), e);
			res = "error";
		}
		WebUtils.webSendString(res);
	}
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
