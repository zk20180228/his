package cn.honry.statistics.bi.outpatientcost.action;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.bi.outpatientcost.service.OutpatientcostService;
import cn.honry.statistics.bi.outpatientcost.vo.OutpatientcostVo;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 门诊费用分析
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/Outpatientcost")
public class OutpatientcostAction extends ActionSupport {
	
	private OutpatientWorkloadService outpatientWorkloadService;
	@Autowired
    @Qualifier(value = "outpatientWorkloadService")
	public void setOutpatientWorkloadService(
			OutpatientWorkloadService outpatientWorkloadService) {
		this.outpatientWorkloadService = outpatientWorkloadService;
	}
	@Autowired
	@Qualifier(value="outpatientcostService")
	private OutpatientcostService outpatientcostservice;
	private String dept;//科室
	/**
	 * 接口加载科室
	 */
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	private HttpServletRequest request=ServletActionContext.getRequest();
	private int dateType;
	private String dimensionValue;
	private String dateString;
	private String dimensionString;
	private int[] dateArray;
	//开始时间
	private String log;
	public int[] getDateArray() {
		return dateArray;
	}

	public void setDateArray(int[] dateArray) {
		this.dateArray = dateArray;
	}
	//结束时间
	private String end;
	//页面Vo
	private List<OutpatientcostVo> outlist;
	//name(维度)
	private String name;
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getDimensionString() {
		return dimensionString;
	}

	public void setDimensionString(String dimensionString) {
		this.dimensionString = dimensionString;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public String getDimensionValue() {
		return dimensionValue;
	}

	public void setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
	}

	public DeptInInterService getDeptInInterService() {
		return deptInInterService;
	}

	public OutpatientcostService getOutpatientcostservice() {
		return outpatientcostservice;
	}

	public void setOutpatientcostservice(OutpatientcostService outpatientcostservice) {
		this.outpatientcostservice = outpatientcostservice;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<OutpatientcostVo> getOutlist() {
		return outlist;
	}

	public void setOutlist(List<OutpatientcostVo> outlist) {
		this.outlist = outlist;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	//加载当前页面
	@Action(value = "Outpatientcostlist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientcost/outpatientcostlist.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String Outpatientcostlist() {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic("I,C");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("dept_code");
		for(BiBaseOrganization bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="dept_code,科室";
		return "list";
	}
	
	/**
	 * 加载科室
	 */
	@Action(value="getdept")
	public void getdept(){
		List<SysDepartment> list=deptInInterService.getDept();
		String json=JSONUtils.toExposeJson(list, false, null, "deptCode","deptName");
		WebUtils.webSendJSON(json);
	}
	/**
	 * @Description:页面加载时加载table(1)
	 * @Author: zhangjin
	 * @CreateDate: 2016年8月4日
	 * @param:date：当前时间
	 * @return 
	 * @Modifier:zhangjin
	 * @ModifyDate:2016-8-20
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value="getOutpatientcostlist")
	public void getOutpatientcostlist(){
		String[] dimStringArray =request.getParameterValues("dimStringArray[]"); 
		String[] dateArray =request.getParameterValues("dateArray[]");
		DateVo datevo =new DateVo();
		datevo.setYear2(Integer.valueOf(dateArray[1]));
		datevo.setYear1(Integer.valueOf(dateArray[0]));
		datevo.setQuarter2(Integer.valueOf(dateArray[3]));
		datevo.setQuarter1(Integer.valueOf(dateArray[2]));
		datevo.setMonth2(Integer.valueOf(dateArray[5]));
		datevo.setMonth1(Integer.valueOf(dateArray[4]));
		datevo.setDay2(Integer.valueOf(dateArray[7]));
		datevo.setDay1(Integer.valueOf(dateArray[6]));
		String resultData =outpatientcostservice.getOutpatientcostlist(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}
	
	//加载当前页面
		@Action(value = "Outpatientcostlist2", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientcost/outpatientcostbi.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
		public String Outpatientcostlist2() {
			Date date=new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int year = c.get(Calendar.YEAR);//获取年份
			dateString=""+year+","+year+",0,0,0,0,0,0";
			List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic("I,C");
			StringBuilder deptValue=new StringBuilder();
			deptValue.append("dept_code");
			for(BiBaseOrganization bo:biorglist){
				deptValue.append(",");
				deptValue.append(bo.getOrgCode());
			}
			dimensionValue=deptValue.toString();
			dimensionString="dept_code,科室";
			return "list";
		}
	/**
	 * @Description:页面加载时加载table(2)
	 * @Author: zhangjin
	 * @CreateDate: 2016年8月22日
	 * @param:date：当前时间
	 * @return ${dateString}
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Action(value="getOutpatientfeelist")
	public void getOutpatientfeelist(){
		String[] dimStringArray =request.getParameterValues("dimStringArray[]"); 
		String[] dateArray =request.getParameterValues("dateArray[]");
		DateVo datevo =new DateVo();
		datevo.setYear2(Integer.valueOf(dateArray[1]));
		datevo.setYear1(Integer.valueOf(dateArray[0]));
		datevo.setQuarter2(Integer.valueOf(dateArray[3]));
		datevo.setQuarter1(Integer.valueOf(dateArray[2]));
		datevo.setMonth2(Integer.valueOf(dateArray[5]));
		datevo.setMonth1(Integer.valueOf(dateArray[4]));
		datevo.setDay2(Integer.valueOf(dateArray[7]));
		datevo.setDay1(Integer.valueOf(dateArray[6]));
		String resultData =outpatientcostservice.getOutpatientfeelist(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendJSON(resultData);
	}

	
	

}
