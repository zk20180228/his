package cn.honry.statistics.bi.outpatient.outpatientDoctor.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.JsonUtils;
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
import cn.honry.statistics.bi.outpatient.outpatientDoctor.service.OutPatientDoctorService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 门诊医生工作量统计
 * @author donghe
 * @createDate：2018/1/27
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/outpatientDoctor")
@SuppressWarnings({ "all" })
public class OutpatientDoctorAction extends ActionSupport{
	@Autowired
	@Qualifier(value="outPatientDoctorService")
	private OutPatientDoctorService outPatientDoctorService;
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	public void setOutPatientDoctorService(
			OutPatientDoctorService outPatientDoctorService) {
		this.outPatientDoctorService = outPatientDoctorService;
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
	 * 医生工作量统计开始时间
	 */
	private String sTime;
	/**
	 * 医生工作量统计结束时间
	 */
	private String eTime;
	
	private String dept;//科室条件
	
	private String expxrt;//医生条件
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
	 *  跳转门诊医生工作量统计
	 * @author donghe
	 * @createDate：2018/1/27
	 * @version 1.0
	 */                                                                                  
	@Action(value = "outpatientDoctorList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientDoctor/outpatientDoctor.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String outpatientDoctorList() throws Exception {
		Date now = new Date();
		sTime=DateUtils.formatDateY_M(now)+"-01";
		eTime=DateUtils.formatDateY_M_D(now);
		return "list";
	}
	
	@Action(value="outpatientList")
	public void outpatientList() {
		String[] depts=null;
		String[] doctors=null;
		if(StringUtils.isBlank(expxrt)){
			doctors=new String[0];
		}else{
			doctors=expxrt.split(",");
		}
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
		Map<String,Object> map=outPatientDoctorService.queryOutPatientDoc(depts, doctors, sTime, eTime, rows, page);
		WebUtils.webSendJSON(JSONUtils.toJson(map));
	}
}
