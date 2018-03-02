package cn.honry.statistics.sys.decorate.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**  
 * 类说明   
 *  
 * @author qh 
 * @date 2017年4月26日  
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/sys/decorate")
@SuppressWarnings({"all"})
public class DecorateAction extends ActionSupport{
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	 private HttpServletRequest request;
	 
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public EmployeeInInterService getEmployeeInInterService() {
		return employeeInInterService;
	}
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}

	@Action(value = "queryEmpCodeAndNameMap")
	public void queryEmpCodeAndNameMap(){
		request = ServletActionContext.getRequest();
		Map<String, String> map = employeeInInterService.queryEmpCodeAndNameMap();
		request.setAttribute("map", map);
		String mapJosn = JSONUtils.toJson(map);
		WebUtils.webSendJSON(mapJosn);
	}
	
}
