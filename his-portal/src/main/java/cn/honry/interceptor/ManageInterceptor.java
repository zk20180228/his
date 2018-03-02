package cn.honry.interceptor;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

@SuppressWarnings("serial")
public class ManageInterceptor extends MethodFilterInterceptor {
	Logger logger=Logger.getLogger(ManageInterceptor.class);
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request=ServletActionContext.getRequest();
		String requestType=request.getHeader("X-Requested-With");
		String requestURI=request.getRequestURI();
		if (!SecurityUtils.getSubject().isAuthenticated()&&(requestURI.length()==(requestURI.replaceAll("/AppNurseStation/|/outBalanceMobileAction/|/appReturnPremium/","").length()))) {
			logger.info("manageInterceptor 拦截器：未登录，跳转到登录页面！");
			if(requestType!=null&&"XMLHttpRequest".equalsIgnoreCase(requestType)){
				PrintWriter printWriter = ServletActionContext.getResponse().getWriter();
				printWriter.print("ajaxSessionTimeOut");
				printWriter.flush(); 
				printWriter.close();  
				return null; 
			}else{
				return Action.LOGIN;
			}
		} 
		return invocation.invoke();
		
	}

}
