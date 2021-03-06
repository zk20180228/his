package cn.honry.statistics.bi.inpatient.expensesAnaly.action;

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
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.statistics.bi.inpatient.expensesAnaly.service.ExpensesAnalyService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 住院费用分析
 * @author donghe
 * @createDate：2016/8/8
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/expensesAnaly")
@SuppressWarnings({ "all" })
public class InpatientExpensesAnalyAction extends ActionSupport{
	@Autowired
    @Qualifier(value = "expensesAnalyService")
	private ExpensesAnalyService expensesAnalyService;
	public void setExpensesAnalyService(ExpensesAnalyService expensesAnalyService) {
		this.expensesAnalyService = expensesAnalyService;
	}
	@Autowired			
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	private OutpatientWorkloadService outpatientWorkloadService;
	@Autowired
    @Qualifier(value = "outpatientWorkloadService")
	public void setOutpatientWorkloadService(
			OutpatientWorkloadService outpatientWorkloadService) {
		this.outpatientWorkloadService = outpatientWorkloadService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
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
	private int[] dateArray;
	private String dateString;
	
/************************************************************************************/
	
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public int[] getDateArray() {
		return dateArray;
	}
	public void setDateArray(int[] dateArray) {
		this.dateArray = dateArray;
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
	public DateVo getDatevo() {
		return datevo;
	}
	public void setDatevo(DateVo datevo) {
		this.datevo = datevo;
	}
	public String getDimensionValue() {
		return dimensionValue;
	}
	public void setDimensionValue(String dimensionValue) {
		this.dimensionValue = dimensionValue;
	}
/************************************************************************************/
	/**
	 *  跳转住院费用分析
	 * @author donghe
	 * @createDate：2016/8/8
	 * @version 1.0
	 */
	@Action(value = "inpatientExpensesAnalylist", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/inpatient/expensesAnaly/expensesAnaly.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientExpensesAnalylist() throws Exception {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
		List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic("I,C");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("inhos_deptcode");
		for(BiBaseOrganization bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="inhos_deptcode,科室";
		return "list";
	}
	
	/**
	 * 查询列表数据
	 * @author donghe
	 * @createDate：2016/8/11
	 * @version 1.0 
	 */
	@Action(value="inpatientExpensesAnalyDatagrid")
	public void inpatientExpensesAnalyDatagrid(){
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
		String resultData =expensesAnalyService.querytExpensesAnalyDatagrid(datevo,dimensionString,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}
	/**
	 * 统计大类map渲染
	 */
	@Action(value = "casminfeeComXr", results = { @Result(name = "json", type = "json") })
	public void casminfeeComXr() {
		List<BusinessDictionary> businessDictionaries = innerCodeService.getDictionary("casminfee");
		Map<String,String> map=new HashMap<String,String>();
		for(BusinessDictionary biorg:businessDictionaries){
			map.put(biorg.getEncode(), biorg.getName());
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
