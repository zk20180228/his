package cn.honry.statistics.classification.conjoined.action;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.statistics.deptstat.operationProportion.service.OperationProportionService;
import cn.honry.statistics.drug.inventoryLog.service.InventoryLogService;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 盘点日志查询(统计)
 * @Description:
 * @author: zpty
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/classification/conjoined")
public class ConjoinedAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * 查询科室
	 */
	private String dept;
	/**
	 * 开始时间
	 */
	private String Stime;
	/**
	 * 结束时间
	 */
	private String Etime;
	/**
	 * 药品
	 */
	private String drug;
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getStime() {
		return Stime;
	}
	public void setStime(String stime) {
		Stime = stime;
	}
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	@RequiresPermissions(value={"YLTGL:function:view"})
	@Action(value = "listConjoined", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/classification/conjoined/conjoinedList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listConjoined() {
		Date date = new Date();
		Etime = "2017-05";
		return "list";
	}
	
}
