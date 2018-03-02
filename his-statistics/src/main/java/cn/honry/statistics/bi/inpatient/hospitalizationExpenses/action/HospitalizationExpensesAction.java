package cn.honry.statistics.bi.inpatient.hospitalizationExpenses.action;

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
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.service.HospitalizationExpensesService;
import cn.honry.statistics.bi.inpatient.hospitalizationExpenses.vo.HospitalizationExpensesVo;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.service.HospitalizationInformationService;
import cn.honry.statistics.bi.inpatient.hospitalizationInformation.service.impl.HospitalizationInformationServiceImpl;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 住院人次费用统计分析
 * @author GengH
 * @createDate：2016年8月4日16:23:49
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/biHospitalizationExpenses")
@SuppressWarnings({ "all" })
public class HospitalizationExpensesAction extends ActionSupport{
	/**
	 * service注入
	 */
	private HospitalizationExpensesService hospitalizationExpensesService;
	@Autowired
    @Qualifier(value = "hospitalizationExpensesService")
	public void setHospitalizationExpensesService(
			HospitalizationExpensesService hospitalizationExpensesService) {
		this.hospitalizationExpensesService = hospitalizationExpensesService;
	}
	private HospitalizationInformationService hospitalizationInformationService;
	@Autowired
    @Qualifier(value = "hospitalizationInformationService")
	public void setHospitalizationInformationService(
			HospitalizationInformationService hospitalizationInformationService) {
		this.hospitalizationInformationService = hospitalizationInformationService;
	}
	
	// 2016年8月19日15:05:33 new 
	private String dateString;
	private String dimensionString;
	private int dateType;
	//	private String[] dimStringArray;
	private String dimensionValue;
	private int[] dateArray;
	private HttpServletRequest request= ServletActionContext.getRequest();

	
	
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

	public int[] getDateArray() {
		return dateArray;
	}

	public void setDateArray(int[] dateArray) {
		this.dateArray = dateArray;
	}

	/**
	 *  跳转住院人次费用统计分析页面
	 * @author GengH
	 * @createDate：2016年8月5日09:36:30
	 * @version 1.0
	 */
	@Action(value = "hospitalizationExpensesList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/inpatient/hospitalizationExpenses/hospitalizationExpensesList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
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
		dimensionString = "dept_code,科室";//,totCost,总金额,drugs,药费,drugsPro,药费占比,noDrugs,非药费,noDrugsPro,非药费费占比,passengers,人次,passengersAvg,平均金额
		return "list";
	}
	
	/**
	 * 查询列表数据
	 * @author GengH
	 * @createDate：2016年8月5日09:36:20
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
		String resultData=hospitalizationExpensesService.querytDatagrid(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}
	
	/**  
	 * @Description： 住院人次分析维度弹窗
	 * @Author：donghe
	 * @CreateDate：2016-8-12 
	 * @version 1.0
	 */
	@Action(value = "window", results = { @Result(name = "window", location = "/WEB-INF/pages/stat/bi/wdWin/wdWinToHospitalizationExpenses.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String window() {
		return "window";
	}
}
