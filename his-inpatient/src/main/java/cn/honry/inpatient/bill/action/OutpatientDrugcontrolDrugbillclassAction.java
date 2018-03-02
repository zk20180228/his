package cn.honry.inpatient.bill.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.inpatient.bill.service.OutpatientDrugcontrolDrugbillclassService;
import cn.honry.inpatient.bill.vo.OutpatientDrugcontrolDrugbillclass;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("all")
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef("manageInterceptor")})
@Namespace(value="/inpatient/billoutpatient")
public class OutpatientDrugcontrolDrugbillclassAction extends ActionSupport implements ModelDriven<OutpatientDrugcontrolDrugbillclass> {
	
	@Override
	public OutpatientDrugcontrolDrugbillclass getModel() {
		return outpatientDrugcontrolDrugbillclass;
	}
	/**
	 * 栏目传参
	 */
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Autowired
	@Qualifier(value = "outpatientDrugcontrolDrugbillclassService")
	private OutpatientDrugcontrolDrugbillclassService outpatientDrugcontrolDrugbillclassService;
	
	
	public OutpatientDrugcontrolDrugbillclassService getOutpatientDrugcontrolDrugbillclassService() {
		return outpatientDrugcontrolDrugbillclassService;
	}
	public void setOutpatientDrugcontrolDrugbillclassService(
			OutpatientDrugcontrolDrugbillclassService outpatientDrugcontrolDrugbillclassService) {
		this.outpatientDrugcontrolDrugbillclassService = outpatientDrugcontrolDrugbillclassService;
	}

	private List<OutpatientDrugcontrolDrugbillclass> OutpatientDrugcontrolDrugbillclassList;
	private HttpServletRequest request = ServletActionContext.getRequest();
	OutpatientDrugcontrolDrugbillclass outpatientDrugcontrolDrugbillclass=new OutpatientDrugcontrolDrugbillclass();
	
	private String page;//起始页数
	private String rows;//数据列数
	public OutpatientDrugcontrolDrugbillclass getOutpatientDrugcontrolDrugbillclass() {
		return outpatientDrugcontrolDrugbillclass;
	}
	public void setOutpatientDrugcontrolDrugbillclass(
			OutpatientDrugcontrolDrugbillclass outpatientDrugcontrolDrugbillclass) {
		this.outpatientDrugcontrolDrugbillclass = outpatientDrugcontrolDrugbillclass;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	/**
	 * @Description:查询列表
	 * @Author：dh
	 * @CreateDate： 2015-12-28
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"BYTSZ:function:query"})
	@Action(value = "queryOutpatientDrugcontrolDrugbillclass", results = { @Result(name = "json", type = "json") })
	public void queryOutpatientDrugcontrolDrugbillclass() throws Exception{
		OutpatientDrugcontrolDrugbillclass outpatientdrugcontrolDrugbillclass = new OutpatientDrugcontrolDrugbillclass();//条件查询
		String deptCode = request.getParameter("deptCode");
		String controlName = java.net.URLDecoder.decode(request.getParameter("controlName"), "UTF-8");
			if(StringUtils.isNotBlank(controlName) || StringUtils.isNotBlank(deptCode)){
				outpatientdrugcontrolDrugbillclass.setDeptCode(deptCode);
				outpatientdrugcontrolDrugbillclass.setControlName(controlName);
			}
		int total = outpatientDrugcontrolDrugbillclassService.getTotalCount(outpatientdrugcontrolDrugbillclass);
		OutpatientDrugcontrolDrugbillclassList = outpatientDrugcontrolDrugbillclassService.getWarnLine(outpatientdrugcontrolDrugbillclass,request.getParameter("page"),request.getParameter("rows"));
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
		String json = gson.toJson(OutpatientDrugcontrolDrugbillclassList);
		try {
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write("{\"total\":" + total + ",\"rows\":" + json + "}");
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
