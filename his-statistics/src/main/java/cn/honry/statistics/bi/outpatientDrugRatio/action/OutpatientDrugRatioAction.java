package cn.honry.statistics.bi.outpatientDrugRatio.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import cn.honry.statistics.bi.outpatientDrugRatio.service.OutpatientDrugRatioService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/bi/outpatientDrugRatio")
public class OutpatientDrugRatioAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	@Autowired			
	@Qualifier(value = "outpatientDrugRatioService")
	private OutpatientDrugRatioService	outpatientDrugRatioService;			
	public void setOutpatientDrugRatioService(OutpatientDrugRatioService outpatientDrugRatioService) {
		this.outpatientDrugRatioService = outpatientDrugRatioService;
	}
	
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private int dateType;
	private String dimensionValue;
	private String dateString;
	private HttpServletRequest request= ServletActionContext.getRequest();
	private String dimensionString;
	
	/**
	 * @Description:获取页面
	 * @Author：  yeguanqun
	 * @CreateDate： 2016-7-27
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"MZYPBLFX:function:view"})
	@Action(value="toViewOutpatientDrugRatio",results={@Result(name="list",location = "/WEB-INF/pages/stat/bi/outpatient/outpatientDrugRatio/outpatientDrugRatio.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewOutpatientDrugRatio()throws Exception{
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
		List<BiBaseOrganization> biorglist=outpatientDrugRatioService.queryDeptForBiPublic("C");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("EXEC_DPCD");
		for(BiBaseOrganization bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="EXEC_DPCD,科室";
		return "list";
	}
	
	/**
	 * 查询列表数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/15
	 * @version 1.0 
	 */
	@Action(value="queryOutpatientDrugRatio")
	public void queryOutpatientDrugRatio(){
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
		String resultData =outpatientDrugRatioService.queryOutpatientDrugRatio(datevo,dimStringArray,dateType,dimensionValue);
		
		
		WebUtils.webSendString(resultData);
	}
	
	
	/**
	 * 查询科室下来code、name
	 * @author tcj
	 */
	@Action(value="queryDeptForBiPublic")
	public void queryDeptForBiPublic(){
		List<BiBaseOrganization> biorglist=outpatientDrugRatioService.queryDeptForBiPublic(null);
		Map<String,String> map=new HashMap<String,String>();
		for(BiBaseOrganization biorg:biorglist){
			map.put(biorg.getOrgCode(), biorg.getOrgName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
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
	
}
