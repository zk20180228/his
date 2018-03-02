package cn.honry.statistics.bi.outpatient.outpatientDepartment.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.statistics.bi.outpatient.outpatientDepartment.service.OutpatientDepartmentService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 门诊科室工作量统计
 * @author donghe
 * @createDate：2018/1/27
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/outpatientDepartment")
@SuppressWarnings({ "all" })
public class OutpatientDepartmentAction extends ActionSupport{
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	@Autowired
	@Qualifier(value="outpatientDepartmentService")
	private OutpatientDepartmentService outpatientDepartmentService;
	
	public void setOutpatientDepartmentService(
			OutpatientDepartmentService outpatientDepartmentService) {
		this.outpatientDepartmentService = outpatientDepartmentService;
	}
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private Integer page;
	private Integer rows;
	private String dept;//科室条件
	
	private String expxrt;//医生条件
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	private String  STime;//开始时间
	private String  ETime;//结束时间
	
	public String getETime() {
		return ETime;
	}
	public void setETime(String eTime) {
		ETime = eTime;
	}
	public void setSTime(String sTime) {
		STime = sTime;
	}
	public String getSTime() {
		return STime;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}
	/**
	 *  跳转门诊科室工作量统计
	 * @author donghe
	 * @createDate：2018/1/27
	 * @version 1.0
	 */                                                                                  
	@Action(value = "outpatientDepartmentList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientDepartment/outpatientDepartment.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String outpatientDepartmentList() throws Exception {
		Date date = new Date();
		 ETime= DateUtils.formatDateY_M_D(date);
		 STime = DateUtils.formatDateY_M(date)+"-01";
		return "list";
	}
	
	
	@Action(value="outDeptList")
	public void outpatientList() {
		String[] depts=null;
		String[] doctors=null;
		if(StringUtils.isBlank(dept)){
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
			if(deptList!=null&&deptList.size()>0){
				depts=new String[deptList.size()];
				for(int i=0,len=deptList.size();i<len;i++){
					depts[i]=deptList.get(i).getDeptCode();
				}
			}else{
				depts=new String[0];
			}
		}else{
			depts=dept.split(",");
		}
		Map<String,Object> map=outpatientDepartmentService.queryOutPatientDept(depts, STime, ETime, rows, page);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
}
