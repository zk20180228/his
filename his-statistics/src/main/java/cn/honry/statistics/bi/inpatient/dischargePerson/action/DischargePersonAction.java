package cn.honry.statistics.bi.inpatient.dischargePerson.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.inpatient.dischargePerson.service.DischargePersonService;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 出院人次统计分析
 * @author zhuxiaolu
 * @createDate：2016/7/21
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/dischargePerson")
@SuppressWarnings({ "all" })
public class DischargePersonAction extends ActionSupport{
	/**
	 * service注入
	 */
	private DischargePersonService dischargePersonService;

	public DischargePersonService getDischargePersonService() {
		return dischargePersonService;
	}
	@Autowired
    @Qualifier(value = "dischargePersonService")
	public void setDischargePersonService(
			DischargePersonService dischargePersonService) {
		this.dischargePersonService = dischargePersonService;
	}
	
	private String deptCode;
	
	private String years;
	private String quarters;
	private String months;
	private String days;
	
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getQuarters() {
		return quarters;
	}
	public void setQuarters(String quarters) {
		this.quarters = quarters;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	/**
	 *  跳转出院人次
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	@Action(value = "dischargePersonList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/inpatient/dischargePerson/dischargePersonList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String dischargePersonList() throws Exception {
		return "list";
	}
	
	
	/**
	 * 查询所有科室
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	@Action(value="queryDeptWorkload")
	public void queryDeptWorkload(){
		List<SysDepartment> deptList=dischargePersonService.queryAllDept("1");
		String json=JSONUtils.toJson(deptList);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 条形统计图数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	@Action(value="dataAccessload")
	public void dataAccessload(){
		List<DischargePersonVo> dischargePersonList=null;
		List<DischargePersonVo> dischargePersonListYear=null;
		List<DischargePersonVo> dischargePersonListMonth=null;
		
		if(years!=null||quarters!=null||months!=null||days!=null||deptCode!=null){
			 dischargePersonList=dischargePersonService.queryDischargePersonList(deptCode, years,quarters,months,days);
				 dischargePersonListYear=dischargePersonService.loadBarPersonList("1",deptCode, years,quarters,months,days);
				 dischargePersonListMonth=dischargePersonService.loadBarPersonList("2",deptCode, years,quarters,months,days);
		}else{
			dischargePersonList=dischargePersonService.loadPersonList("0");
			dischargePersonListYear=dischargePersonService.loadPersonList("1");
			dischargePersonListMonth=dischargePersonService.loadPersonList("2");
		}
		
		String[] categories=new String[dischargePersonList.size()];
		Integer[] values =new Integer[dischargePersonList.size()];
		for(int i=0;i<dischargePersonList.size();i++){
			categories[i]=dischargePersonList.get(i).getDeptName();
			values[i]=dischargePersonList.get(i).getDischargePerson();
			
		}
		
		Integer[] years =new Integer[dischargePersonListYear.size()];
		for(int i=0;i<dischargePersonListYear.size();i++){
			years[i]=dischargePersonListYear.get(i).getDischargePerson();
		}
		
		Integer[] months =new Integer[dischargePersonListMonth.size()];
		for(int i=0;i<dischargePersonListMonth.size();i++){
			months[i]=dischargePersonListMonth.get(i).getDischargePerson();
		}
	    Map<String, Object> json1 = new HashMap<String, Object>();  
	    json1.put("categories", categories);  
	    json1.put("values", values); 
	    json1.put("years", years); 
	    json1.put("months", months); 
	    String json=JSONUtils.toJson(json1);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 饼状统计图数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	@Action(value="pieChartload")
	public void pieChartload(){
		List<DischargePersonVo> dischargePersonList=null;
		if(years!=null||quarters!=null||months!=null||days!=null||deptCode!=null){
			 dischargePersonList=dischargePersonService.queryDischargePersonList(deptCode, years,quarters,months,days);
		}else{
			dischargePersonList=dischargePersonService.loadPersonList("0");
		}
		String[] categories=null;
		Object [] values=null;
		if(dischargePersonList.size()==1){
			categories=new String[dischargePersonList.size()+1];
			values=new Object[dischargePersonList.size()+1];
		}else{
			categories=new String[dischargePersonList.size()];
			values=new Object[dischargePersonList.size()];
		}
		
		for(int i=0;i<dischargePersonList.size();i++){
			if(dischargePersonList.size()==1){
				categories[0]="出院人次";
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("value", dischargePersonList.get(i).getDischargePerson());
				map.put("name", "出院人次");
				values[0]=map;
				categories[1]="住院人次";
				Map<String,Object> map2 = new HashMap<String, Object>();
				map2.put("value", dischargePersonList.get(i).getHospitalizationTime()-dischargePersonList.get(i).getDischargePerson());
				map2.put("name", "住院人次");
				values[1]=map2;
			}else{
				categories[i]=dischargePersonList.get(i).getDeptName();
				Map<String,Object> map = new HashMap<String, Object>();
				//map.put("value", dischargePersonList.get(i).getDischargePerson());
				map.put("value", dischargePersonList.get(i).getDischargePerson());
				map.put("name", dischargePersonList.get(i).getDeptName());
				values[i]=map;
			}
			
		}
	    Map<String, Object> json1 = new HashMap<String, Object>();  
	    json1.put("categories", categories);  
	    json1.put("values", values); 
	    String json=JSONUtils.toJson(json1);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 折线统计图数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/21
	 * @version 1.0
	 */
	@Action(value="axisload")
	public void axisload(){
		List<DischargePersonVo> dischargePersonList=null;
		if(years!=null||quarters!=null||months!=null||days!=null||deptCode!=null){
			 dischargePersonList=dischargePersonService.queryDischargePersonList(deptCode, years,quarters,months,days);
		}else{
			dischargePersonList=dischargePersonService.loadPersonList("0");
		}
		String[] categories=new String[dischargePersonList.size()];
		Integer[] values =new Integer[dischargePersonList.size()];
		for(int i=0;i<dischargePersonList.size();i++){
			categories[i]=dischargePersonList.get(i).getDeptName();
			values[i]=dischargePersonList.get(i).getDischargePerson();
			//values[i]=i+10;
		}
	    Map<String, Object> json1 = new HashMap<String, Object>();  
	    json1.put("categories", categories);  
	    json1.put("values", values); 
	    String json=JSONUtils.toJson(json1);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 查询列表数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/15
	 * @version 1.0 
	 */
	@Action(value="queryDischargePersonList")
	public void queryDischargePersonList(){
		List<DischargePersonVo> dischargePersonList=dischargePersonService.queryDischargePersonList(deptCode, years,quarters,months,days);
		String json=JSONUtils.toJson(dischargePersonList);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 查询列表数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/15
	 * @version 1.0 
	 */
	@Action(value="loadPersonList")
	public void loadPersonList(){
		List<DischargePersonVo> dischargePersonList=dischargePersonService.loadPersonList("0");
		String json=JSONUtils.toJson(dischargePersonList);
		WebUtils.webSendJSON(json);
	}
	
}
