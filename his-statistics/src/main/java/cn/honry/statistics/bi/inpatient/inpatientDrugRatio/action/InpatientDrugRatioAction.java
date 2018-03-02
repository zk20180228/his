package cn.honry.statistics.bi.inpatient.inpatientDrugRatio.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.statistics.bi.inpatient.inpatientDrugRatio.service.InpatientDrugRatioService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 住院药品比例分析Action
 * @author hanzurong
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/bi/inpatientDrugRatio")
public class InpatientDrugRatioAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	@Autowired			
	@Qualifier(value = "inpatientDrugRatioService")
	private InpatientDrugRatioService inpatientDrugRatioService;			
	public void setInpatientDrugRatioService(InpatientDrugRatioService inpatientDrugRatioService) {
		this.inpatientDrugRatioService = inpatientDrugRatioService;
	}
	@Autowired			
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;				
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private int dateType;
//	private String[] dimStringArray;
	private String dimensionValue;
	private String dateString;
	private HttpServletRequest request= ServletActionContext.getRequest();
	private String dimensionString;
	
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
	/**
	 * @Description:获取页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-8-11
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYYPBLFX:function:view"})
	@Action(value="toViewInpatientDrugRatio",results={@Result(name="list",location = "/WEB-INF/pages/stat/bi/inpatient/inpatientDrugRatio/inpatientDrugRatio.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewInpatientDrugRatio()throws Exception{
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
		List<BiBaseOrganization> biorglist=inpatientDrugRatioService.queryDeptForBiPublic("I,C");
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
	 * @author yeguanqun
	 * @createDate：2016/8/12
	 * @version 1.0 
	 */
	@Action(value="queryInpatientDrugRatio")
	public void queryInpatientDrugRatio(){
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
/*		String[] array = dateArray.split(",");
		if(array.length==1){
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
		}
		if(array.length==2){
			datevo.setYear2("".equals(array[1])?0:Integer.valueOf(array[1]));
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
		}
		if(array.length==3){
			datevo.setYear2("".equals(array[1])?0:Integer.valueOf(array[1]));
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
			datevo.setQuarter1("".equals(array[2])?0:Integer.valueOf(array[2]));
		}
		if(array.length==4){
			datevo.setYear2("".equals(array[1])?0:Integer.valueOf(array[1]));
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
			datevo.setQuarter2("".equals(array[3])?0:Integer.valueOf(array[3]));
			datevo.setQuarter1("".equals(array[2])?0:Integer.valueOf(array[2]));
		}
		if(array.length==5){
			datevo.setYear2("".equals(array[1])?0:Integer.valueOf(array[1]));
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
			datevo.setQuarter2("".equals(array[3])?0:Integer.valueOf(array[3]));
			datevo.setQuarter1("".equals(array[2])?0:Integer.valueOf(array[2]));
			datevo.setMonth1("".equals(array[4])?0:Integer.valueOf(array[4]));
		}
		if(array.length==6){
			datevo.setYear2("".equals(array[1])?0:Integer.valueOf(array[1]));
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
			datevo.setQuarter2("".equals(array[3])?0:Integer.valueOf(array[3]));
			datevo.setQuarter1("".equals(array[2])?0:Integer.valueOf(array[2]));
			datevo.setMonth2("".equals(array[5])?0:Integer.valueOf(array[5]));
			datevo.setMonth1("".equals(array[4])?0:Integer.valueOf(array[4]));
		}
		if(array.length==7){
			datevo.setYear2("".equals(array[1])?0:Integer.valueOf(array[1]));
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
			datevo.setQuarter2("".equals(array[3])?0:Integer.valueOf(array[3]));
			datevo.setQuarter1("".equals(array[2])?0:Integer.valueOf(array[2]));
			datevo.setMonth2("".equals(array[5])?0:Integer.valueOf(array[5]));
			datevo.setMonth1("".equals(array[4])?0:Integer.valueOf(array[4]));
			datevo.setDay1("".equals(array[6])?0:Integer.valueOf(array[6]));
		}
		if(array.length==8){
			datevo.setYear2("".equals(array[1])?0:Integer.valueOf(array[1]));
			datevo.setYear1("".equals(array[0])?0:Integer.valueOf(array[0]));
			datevo.setQuarter2("".equals(array[3])?0:Integer.valueOf(array[3]));
			datevo.setQuarter1("".equals(array[2])?0:Integer.valueOf(array[2]));
			datevo.setMonth2("".equals(array[5])?0:Integer.valueOf(array[5]));
			datevo.setMonth1("".equals(array[4])?0:Integer.valueOf(array[4]));
			datevo.setDay2("".equals(array[7])?0:Integer.valueOf(array[7]));
			datevo.setDay1("".equals(array[6])?0:Integer.valueOf(array[6]));
		}		*/
		String resultData =inpatientDrugRatioService.queryInpatientDrugRatio(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
	}
	/**
	 * 查询列表数据
	 * @author yeguanqun
	 * @createDate：2016/8/17
	 * @version 1.0 
	 */
	@Action(value="queryDeptCodeAndNameMap")
	public Map<String, String> queryDeptCodeAndNameMap() {
		Map<String, String> map = deptInInterService.querydeptCodeAndNameMap();		
		return map;
	}
}
