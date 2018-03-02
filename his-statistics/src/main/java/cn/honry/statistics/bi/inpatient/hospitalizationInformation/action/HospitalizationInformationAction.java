package cn.honry.statistics.bi.inpatient.hospitalizationInformation.action;

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

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.service.HospitalizationInformationService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 住院人次统计分析
 * @author GengH
 * @createDate：2016/7/14
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/biHospitalizationInformation")
@SuppressWarnings({ "all" })
public class HospitalizationInformationAction extends ActionSupport{
	/**
	 * service注入
	 */
	private HospitalizationInformationService hospitalizationInformationService;
	@Autowired
    @Qualifier(value = "hospitalizationInformationService")
	public void setHospitalizationInformationService(HospitalizationInformationService hospitalizationInformationService) {
		this.hospitalizationInformationService = hospitalizationInformationService;
	}
	// 2016年8月19日15:05:33 new 
	private String dateString;
	private String dimensionString;
	private int dateType;
	//private String[] dimStringArray;
	private String dimensionValue;
	private int[] dateArray;
	private HttpServletRequest request= ServletActionContext.getRequest();
	
	
	public String getDimensionString() {
		return dimensionString;
	}
	public void setDimensionString(String dimensionString) {
		this.dimensionString = dimensionString;
	}
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
	 *  跳转住院人次统计分析页面
	 * @author GengH
	 * @createDate：2016/7/14
	 * @version 1.0
	 */
	@Action(value = "hospitalizationInformationList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/inpatient/hospitalizationInformation/hospitalizationInformationList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listNurseInfo() throws Exception {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		dateString = ""+year+","+year+",0,0,0,0,0,0";
		List<BiBaseOrganization> list = hospitalizationInformationService.queryDeptForBiPublic("I");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("dept_code");
		for(BiBaseOrganization info:list){
			deptValue.append(",");
			deptValue.append(info.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString = "dept_code,科室";
		return "list";
		
		
	}
	/**
	 * 查询所有科室
	 * @author GengH
	 * @createDate：2016年7月22日11:00:45
	 * @version 1.0
	 */
	@Action(value="queryDeptWorkload")
	public void queryDeptWorkload(){
			List<BiBaseOrganization> biorglist=hospitalizationInformationService.queryAllDept();
			Map<String,String> map=new HashMap<String,String>();
			for(BiBaseOrganization biorg:biorglist){
				map.put(biorg.getOrgCode(), biorg.getOrgName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
	
	/**
	 * 获取地域 MAP
	 * by GH
	 */
	@Action(value = "homeMap", results = { @Result(name = "json", type = "json") })
	public void homeMap() {
		List<BIBaseDistrict> inSourseList = hospitalizationInformationService.getHome();
		Map<String,String> map=new HashMap<String,String>();
		for(BIBaseDistrict biorg:inSourseList){
			map.put(biorg.getCityCode(), biorg.getCityName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询列表数据
	 * @author GengH
	 * @createDate：2016年7月22日17:22:58
	 * @version 1.0 
	 */
	@Action(value="querytDatagrid")
	public void querytDatagrid(){
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
		String resultData=hospitalizationInformationService.querytDatagrid(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}
	
//	/**
//	 * 获取统计图数据
//	 * @author GengH
//	 */
//	@Action(value="queryStatDate")
//	public void queryStatDate(){
//		String json=JSONUtils.toJson(hospitalizationInformationService.queryStatDate(timeString,nameString));
//		json=json.replace("null", "0");
//		WebUtils.webSendJSON(json);
//	}
//	/**
//	 * 获取科室年龄峰值
//	 * @author GengH
//	 */
//	@Action(value="getAgePeakValue")
//	public void getAgePeakValue(){
//		String json=JSONUtils.toJson(hospitalizationInformationService.getAgePeakValue());
//		WebUtils.webSendJSON(json);
//	}
	
}
