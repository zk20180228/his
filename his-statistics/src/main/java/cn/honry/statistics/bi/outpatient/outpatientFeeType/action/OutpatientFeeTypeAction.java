package cn.honry.statistics.bi.outpatient.outpatientFeeType.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.district.service.DistrictInnerService;
import cn.honry.statistics.bi.inpatient.dischargePerson.service.DischargePersonService;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.service.OutpatientFeeTypeService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**  
 * @Description： 门诊收费类型分析
 * @Author：tangfeishuai
 * @CreateDate：2016年7月25日
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/outpatientFeeType")
public class OutpatientFeeTypeAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
    @Qualifier(value = "outpatientFeeTypeService")
	private OutpatientFeeTypeService outpatientFeeTypeService;
	
	@Autowired
	@Qualifier(value = "dischargePersonService")
	private DischargePersonService dischargePersonService;
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	
	@Autowired
	@Qualifier(value = "districtInnerService")
	private DistrictInnerService districtInnerService;
	
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
	private HttpServletRequest request= ServletActionContext.getRequest();
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
	
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
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
	public void setOutpatientFeeTypeService(OutpatientFeeTypeService outpatientFeeTypeService) {
		this.outpatientFeeTypeService = outpatientFeeTypeService;
	}

	
	public void setDischargePersonService(DischargePersonService dischargePersonService) {
		this.dischargePersonService = dischargePersonService;
	}


	/**  
	 * @Description： 门诊收费类型分析跳转页面
	 * @Author：tangfeishuai
	 * @CreateDate：2016年7月25日
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "listOutpatientFeeType", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientFeeType/outpatientFeeTypeList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listOutpatientFeeType() {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
		List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic("I,C");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("doct_dept");
		for(BiBaseOrganization bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="doct_dept,科室";
		return "list";
	}
	
	/**  
	 * @Description： 门诊收费类型分析
	 * @Author：tangfeishuai
	 * @CreateDate：2016年7月25日
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryOutpatientFeeType")
	public void queryOutpatientFeeType() {
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
		String resultData =outpatientFeeTypeService.queryOutpatientFeeType(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
		
	}
	
	/**
	 * 查询所有科室
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryDeptWorkload")
	public void queryDeptWorkload(){
		List<SysDepartment> deptList=dischargePersonService.queryAllDept("2");
		String json=JSONUtils.toExposeJson(deptList, false, null, "deptCode","deptName");
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询所有支付方式
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryPayWay")
	public void queryPayWay(){
		List<BusinessDictionary> deptList=innerCodeService.getDictionary("payway");
		String json=JSONUtils.toExposeJson(deptList, false, null, "encode","name");
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 查询所有文化程度
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryDegree")
	public void queryDegree(){
		List<BusinessDictionary> deptList=innerCodeService.getDictionary("degree");
		String json=JSONUtils.toExposeJson(deptList, false, null, "encode","name");
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询地域
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryAddress")
	public void queryAddress(){
		List<District> treeJsonList = districtInnerService.queryDistricttreeSJLD(1,"");
		String json=JSONUtils.toExposeJson(treeJsonList, false, null, "cityCode","cityName");
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询地域Map
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryAddressMap")
	public void queryAddressMap(){
		HashMap<String , String> addressMap=(HashMap<String, String>) outpatientFeeTypeService.addressMap();
		String json=JSONUtils.toJson(addressMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询年龄Map
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryAgeMap")
	public void queryAgeMap(){
		HashMap<String , String> addressMap=(HashMap<String, String>) outpatientFeeTypeService.ageMap();
		String json=JSONUtils.toJson(addressMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询文化程度Map
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryDegreeMap")
	public void queryDegreeMap(){
		HashMap<String , String> degreeMap=(HashMap<String, String>) outpatientFeeTypeService.degreeMap();
		String json=JSONUtils.toJson(degreeMap);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询支付方式Map
	 * @author tangfeishuai
	 * @createDate：2016/8/2
	 * @version 1.0
	 */
	@Action(value="queryPaywayMap")
	public void queryPaywayMap(){
		HashMap<String , String> paywayMap=(HashMap<String, String>) outpatientFeeTypeService.paywayMap();
		String json=JSONUtils.toJson(paywayMap);
		WebUtils.webSendJSON(json);
	}
}
