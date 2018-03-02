package cn.honry.statistics.bi.outpatient.outpatientWorkload.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 门急诊工作量统计分析
 * @author tuchuanjiang
 * @createDate：2016/7/14
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/biOutpatientWorkload")
@SuppressWarnings({ "all" })
public class OutPatientWorkloadAction extends ActionSupport{
	/**
	 * service注入
	 */
	private OutpatientWorkloadService outpatientWorkloadService;
	@Autowired
    @Qualifier(value = "outpatientWorkloadService")
	public void setOutpatientWorkloadService(
			OutpatientWorkloadService outpatientWorkloadService) {
		this.outpatientWorkloadService = outpatientWorkloadService;
	}
	private int dateType;
//	private String[] dimStringArray;
	private String dimensionValue;
	private String dateString;
	private HttpServletRequest request= ServletActionContext.getRequest();
	private String dimensionString;
	
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
	public String getDimensionValue() {
		return dimensionValue;
	}
	public void setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
	}
	public int getDateType() {
		return dateType;
	}
	public void setDateType(int dateType) {
		this.dateType = dateType;
	}
	/**
	 *  跳转门急诊工作量统计分析页面
	 * @author tuchuanjiang
	 * @createDate：2016/7/14
	 * @version 1.0
	 */
	@Action(value = "outpatientWordloadList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientWorkLoad/outpatientWorkLoadList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listNurseInfo() throws Exception {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
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
	 * 查询列表数据
	 * @author tuchuanjiang
	 * @createDate：2016/7/15
	 * @version 1.0 
	 */
	@Action(value="querytWordloadDatagrid")
	public void querytWordloadDatagrid(){
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
		String resultData =outpatientWorkloadService.querytWordloadDatagrid(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}
	/**
	 * 查询科室下来code、name
	 * @author tcj
	 */
	@Action(value="queryDeptForBiPublic")
	public void queryDeptForBiPublic(){
		List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic(null);
		Map<String,String> map=new HashMap<String,String>();
		for(BiBaseOrganization biorg:biorglist){
			map.put(biorg.getOrgCode(), biorg.getOrgName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
