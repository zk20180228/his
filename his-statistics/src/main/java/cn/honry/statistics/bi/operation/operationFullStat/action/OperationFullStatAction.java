package cn.honry.statistics.bi.operation.operationFullStat.action;

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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.statistics.bi.operation.operationFullStat.service.OperationFullStatService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.WebUtils;

/**
 * 全院手术人次统计
 * @author tangfeishuai
 * @createDate：2016/8/11
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/biOperationFullStat")
@SuppressWarnings({ "all" })
public class OperationFullStatAction extends ActionSupport{
	
	/**
	 * service注入
	 */
	@Autowired
    @Qualifier(value = "operationFullStatService")
	private OperationFullStatService operationFullStatService;
	private int yearStart;
	private int yearEnd;
	private int quarterStart;
	private int quarterEnd;
	private int monthStart;
	private int monthEnd;
	private int dayStart;
	private int dayEnd;
	private int dateType;
	private String dimensionString;
	private DateVo datevo;
	private String dimensionValue;
	private String dateString;
	HttpServletRequest request=ServletActionContext.getRequest();
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
	public String getDimensionValue() {
		return dimensionValue;
	}
	public void setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
	}
	public DateVo getDatevo() {
		return datevo;
	}
	public void setDatevo(DateVo datevo) {
		this.datevo = datevo;
	}
	public int getYearStart() {
		return yearStart;
	}
	public void setYearStart(int yearStart) {
		this.yearStart = yearStart;
	}
	public int getYearEnd() {
		return yearEnd;
	}
	public void setYearEnd(int yearEnd) {
		this.yearEnd = yearEnd;
	}
	
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public int getQuarterStart() {
		return quarterStart;
	}
	public void setQuarterStart(int quarterStart) {
		this.quarterStart = quarterStart;
	}
	public int getQuarterEnd() {
		return quarterEnd;
	}
	public void setQuarterEnd(int quarterEnd) {
		this.quarterEnd = quarterEnd;
	}
	public int getMonthStart() {
		return monthStart;
	}
	public void setMonthStart(int monthStart) {
		this.monthStart = monthStart;
	}
	public int getMonthEnd() {
		return monthEnd;
	}
	public void setMonthEnd(int monthEnd) {
		this.monthEnd = monthEnd;
	}
	public int getDayStart() {
		return dayStart;
	}
	public void setDayStart(int dayStart) {
		this.dayStart = dayStart;
	}
	public int getDayEnd() {
		return dayEnd;
	}
	public void setDayEnd(int dayEnd) {
		this.dayEnd = dayEnd;
	}
	public int getDateType() {
		return dateType;
	}
	public void setDateType(int dateType) {
		this.dateType = dateType;
	}
	public String getDimensionString() {
		return dimensionString;
	}
	public void setDimensionString(String dimensionString) {
		this.dimensionString = dimensionString;
	}
	
	public void setOperationFullStatService(OperationFullStatService operationFullStatService) {
		this.operationFullStatService = operationFullStatService;
	}
	/**
	 *  跳转全院手术统计分析页面
	 * @author tangfeishuai
	 * @createDate：2016/8/11
	 * @version 1.0
	 */
	@Action(value = "listOperationFullStat", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/operationFullStat/operationFullStatList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOperationFullStat() throws Exception {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
		List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic("I,C");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("exec_dept");
		for(BiBaseOrganization bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="exec_dept,科室";
		return "list";
	}
	
	/**
	 * 查询列表数据
	 * @author tangfeishuai
	 * @createDate：2016/7/15
	 * @version 1.0 
	 */
	@Action(value="queryOperationFullStat")
	public void queryOperationFullStat(){
		

		String[] dimStringArray = null;
		String[] dateArray =null;
		try {
			dimStringArray = request.getParameterValues("dimStringArray[]"); 
			 dateArray=request.getParameterValues("dateArray[]");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		DateVo datevo =new DateVo();
		datevo.setYear2(Integer.valueOf(dateArray[1]));
		datevo.setYear1(Integer.valueOf(dateArray[0]));
		datevo.setQuarter2(Integer.valueOf(dateArray[3]));
		datevo.setQuarter1(Integer.valueOf(dateArray[2]));
		datevo.setMonth2(Integer.valueOf(dateArray[5]));
		datevo.setMonth1(Integer.valueOf(dateArray[4]));
		datevo.setDay2(Integer.valueOf(dateArray[7]));
		datevo.setDay1(Integer.valueOf(dateArray[6]));
		String resultData =operationFullStatService.querytWordloadDatagrid(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}

}
