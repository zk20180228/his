package cn.honry.statistics.bi.register.action;


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

import com.opensymphony.xwork2.ActionSupport;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.bi.register.service.RegisterService;
import cn.honry.statistics.bi.register.vo.RegisterVo;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

/**
 * 挂号统计分析Action
 * @author hanzurong
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor")})
@Namespace(value="/statistics/register")
@SuppressWarnings({"all"})
public class RegisterAction extends ActionSupport{

	/**
	 * 
	 * service注入
	 */
	private RegisterService registerService;
	@Autowired
	@Qualifier(value = "registerService")
	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}
	
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
	public int getDateType() {
		return dateType;
	}
	public void setDateType(int dateType) {
		this.dateType = dateType;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	/**
	 * 跳转挂号统计分析分析页面
	 * @author hanzurong
	 * @createDate: 2016/7/28
	 */
	@RequiresPermissions(value={"GHFX:function:view"})
	@Action(value = "registerlist", results = {@Result(name = "list", location = "/WEB-INF/pages/stat/bi/register/registerlist.jsp")},interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String registerlist() throws Exception{
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
	 *  查询科室
	 * @author hanzurong
	 * @createDate：2016/7/28
	 * @version 1.0
	 */
	@Action(value="queryworkload")
	public void queryworkload(){
		List<SysDepartment> deptList = registerService.queryAllDept();
		String json = JSONUtils.toExposeJson(deptList, false, null, "deptCode","deptName");
		WebUtils.webSendJSON(json);
	}
	/**
	 *  查询医师级别
	 * @author hanzurong
	 * @createDate：2016/7/28
	 * @version 1.0
	 */
	@Action(value="querygradename")
	public void querygradename(){
	List<BiRegisterGrade> gradeList = registerService.queryAllGrade();
	String json = JSONUtils.toExposeJson(gradeList, false, null, "gradeCode","gradeName");
	WebUtils.webSendJSON(json);
	}
	/**
	 *  查询列表数据
	 * @author hanzurong
	 * @createDate：2016/7/28
	 * @version 1.0
	 */
	@Action(value="queryregisterlist")
	public void queryregisterlist(){
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
		String registerLIst=registerService.queryregisterid(datevo,dimStringArray,dateType,dimensionValue);
		WebUtils.webSendString(registerLIst);
	}
	/**
	 * 查询科室下来code、name
	 * @author hanzurong
	 */
	@Action(value="queryDeptForBiPublic")
	public void queryDeptForBiPublic(){
		List<BiBaseOrganization> biorglist=registerService.queryDeptForBiPublic();
		Map<String,String> map=new HashMap<String,String>();
		for(BiBaseOrganization biorg:biorglist){
			map.put(biorg.getOrgCode(), biorg.getOrgName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询挂号级别下来code、name
	 * @author hanzurong
	 */
	@Action(value="queryreglevlForBiPublic")
	public void queryreglevlForBiPublic(){
		List<BiBaseOrganization> biorglist=registerService.queryreglevlForBiPublic();
		Map<String,String> map=new HashMap<String,String>();
		for(BiBaseOrganization biorg:biorglist){
			map.put(biorg.getOrgCode(), biorg.getOrgName());
		}
		String json=JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
}
