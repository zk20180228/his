package cn.honry.statistics.bi.inpatient.peopleInHospital.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.dischargePerson.service.DischargePersonService;
import cn.honry.statistics.bi.inpatient.peopleInHospital.service.PeopleInHospitalService;
import cn.honry.statistics.bi.inpatient.peopleInHospital.vo.PeopleInHospitalVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**  
 * @Description： 在院人次统计分析
 * @Author：hanzurong
 * @CreateDate：2016年8月8日
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/statistics/peopleInHospital")
@SuppressWarnings({"all"})
public class PeopleInHospitalAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier(value = "peopleInHospitalService")
	private PeopleInHospitalService peopleInHospitalService;
	@Autowired
	@Qualifier(value = "dischargePersonService")
	private DischargePersonService dischargePersonService;
	
	private OutpatientWorkloadService outpatientWorkloadService;
	@Autowired
    @Qualifier(value = "outpatientWorkloadService")
	public void setOutpatientWorkloadService(
			OutpatientWorkloadService outpatientWorkloadService) {
		this.outpatientWorkloadService = outpatientWorkloadService;
	}
	private int dateType;
	private String dimensionValue;
	private int[] dateArray;
	private HttpServletRequest request = ServletActionContext.getRequest();

	private String dateString;
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

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setPeopleInHospitalService(PeopleInHospitalService peopleInHospitalService) {
		this.peopleInHospitalService = peopleInHospitalService;
	}

	public void setDischargePersonService(DischargePersonService dischargePersonService) {
		this.dischargePersonService = dischargePersonService;
	}

	/**
	 *  跳转在院人次
	 * @author hanzurong
	 * @createDate：2016/8/8
	 * @version 1.0
	 */
	@RequiresPermissions(value={"ZYRCTJFX:function:view"})
	@Action(value = "listpeopleInHospital" , results = { @Result(name = "list" , location = "/WEB-INF/pages/stat/bi/inpatient/peopleInHospital/peopleInHospitalList.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor")})
	public String listpeopleInHospital() throws Exception {
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
	 *  查询列表数据
	 * @author hanzurong
	 * @createDate：2016/7/28
	 * @version 1.0
	 */
	@Action(value="queryPeopleInHospital")
	public void queryPeopleInHospital(){
		String[] dimStringArray = null;
		String[] dateArray = null;
		try{
			dimStringArray=request.getParameterValues("dimStringArray[]");
			dateArray=request.getParameterValues("dateArray[]");
		}catch(Exception e){
			e.printStackTrace();
		}
		DateVo datevo=new DateVo();
		datevo.setYear2(Integer.valueOf(dateArray[1]));
		datevo.setYear1(Integer.valueOf(dateArray[0]));
		datevo.setQuarter2(Integer.valueOf(dateArray[3]));
		datevo.setQuarter1(Integer.valueOf(dateArray[2]));
		datevo.setMonth2(Integer.valueOf(dateArray[5]));
		datevo.setMonth1(Integer.valueOf(dateArray[4]));
		datevo.setDay2(Integer.valueOf(dateArray[7]));
		datevo.setDay1(Integer.valueOf(dateArray[6]));
		String registerLIst=peopleInHospitalService.queryregisterid(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendJSON(registerLIst);
	}
}
