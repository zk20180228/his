package cn.honry.statistics.bi.inpatient.inpatientDepartment.action;

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

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.statistics.bi.inpatient.inpatientDepartment.service.InpatientDepartmentService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 住院科室工作量统计
 * @author donghe
 * @createDate：2018/1/27
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/inpatientDepartment")
@SuppressWarnings({ "all" })
public class InpatientDepartmentAction extends ActionSupport{
	@Autowired
	@Qualifier(value="inpatientDepartmentService")
	private InpatientDepartmentService inpatientDepartmentService;
	
	public void setInpatientDepartmentService(
			InpatientDepartmentService inpatientDepartmentService) {
		this.inpatientDepartmentService = inpatientDepartmentService;
	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	public String sTime;//开始时间
	public String eTime;//结束时间
	private String dept;
	private Integer rows;
	private Integer page;
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	/**
	 *  跳转住院科室工作量统计
	 * @author donghe
	 * @createDate：2018/1/27
	 * @version 1.0
	 */                                                                                  
	@Action(value = "inpatientDepartmentList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/inpatientDepartment/inpatientDepartment.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientDepartmentList() throws Exception {
		Date date = new Date();
		eTime = DateUtils.formatDateY_M_D(date);
		sTime = DateUtils.formatDateY_M(date)+"-01";
		return "list";
	}
	/**
	 * 
	 * 
	 * <p> pc端住院科室工作量统计</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月31日 下午4:36:04 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月31日 下午4:36:04 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value="inpatientDeptList")
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
		Map<String,Object> map=inpatientDepartmentService.queryInpatientDept(depts, sTime, eTime, rows, page);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
	
}
