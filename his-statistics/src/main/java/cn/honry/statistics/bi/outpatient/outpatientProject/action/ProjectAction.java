package cn.honry.statistics.bi.outpatient.outpatientProject.action;

import java.util.Calendar;
import java.util.Date;
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

import cn.honry.base.bean.model.BiBaseDictionary;
import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.bi.outpatient.outpatientProject.service.ProjectService;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.service.OutpatientWorkloadService;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/bi/project")
public class ProjectAction extends ActionSupport {

	/**
	 * BI门诊项目收费均次费用分析
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 维度Vo
	 */
	private DimensionVO dimensionVO;
	
	public DimensionVO getDimensionVO() {
		return dimensionVO;
	}

	public void setDimensionVO(DimensionVO dimensionVO) {
		this.dimensionVO = dimensionVO;
	}
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
	/**
	 * BI门诊项目收费均次费用分析Service
	 */
	@Autowired 
	@Qualifier(value = "projectService")
	private ProjectService projectService;
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	/**
	 * 维度Vo--横版
	 */
	private DateVo datevo;
	
	public DateVo getDatevo() {
		return datevo;
	}
	
	public void setDatevo(DateVo datevo) {
		this.datevo = datevo;
	}
	private String dateString;
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
	private String dimensionValue;
	

	/**  
	 * @Description： 门诊项目收费均次费用分析访问
	 * @Author：ldl
	 * @CreateDate：2016-08-08
	 * @ModifyRmk：  
	 * @version 1.0
	 */
//	@Action(value = "ProjectToView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientProject/outpatientProject.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
//	public String ProjectToView() {
//		return "list";
//	}
	
	/**  
	 * @Description： 门诊项目收费均次费用分析列表
	 * @Author：ldl
	 * @CreateDate：2016-08-08
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "toList")
	public void toList(){
		List<DimensionVO> dimensionVOList = projectService.findDimensionList(dimensionVO);
		String mapJosn = JSONUtils.toJson(dimensionVOList);
		WebUtils.webSendJSON(mapJosn);
	}
	
	/**  
	 * @Description： 系统类别下拉框
	 * @Author：ldl
	 * @CreateDate：2016-08-10
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "systemTypeCombobox")
	public void systemTypeCombobox(){
		List<BiBaseDictionary> findBiBaseDictionary = projectService.getDictionary("systemType");
		String mapJosn = JSONUtils.toJson(findBiBaseDictionary);
		WebUtils.webSendJSON(mapJosn);
	}
	
	/**  
	 * @Description：渲染系统类别
	 * @Author：ldl
	 * @CreateDate：2016-08-10
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "systemTypeFunction")
	public void systemTypeFunction(){
		List<BiBaseDictionary> findBiBaseDictionary = projectService.getDictionary("systemType");
		Map<String,String> casminfeeMap = new HashMap<String, String>();
		for(BiBaseDictionary casminfee : findBiBaseDictionary){
			casminfeeMap.put(casminfee.getCodeEncode(), casminfee.getCodeName());
		}
		String mapJosn = JSONUtils.toJson(casminfeeMap);
		WebUtils.webSendJSON(mapJosn);
	}
	
	/**  
	 * @Description：统计大类下拉框
	 * @Author：ldl
	 * @CreateDate：2016-08-10
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "casminfeeCombobox")
	public void casminfeeCombobox(){
		List<BiBaseDictionary> findBiBaseDictionary = projectService.getDictionary("casminfee");
		String mapJosn = JSONUtils.toJson(findBiBaseDictionary);
		WebUtils.webSendJSON(mapJosn);
	}
	
	/**  
	 * @Description：渲染统计大类
	 * @Author：ldl
	 * @CreateDate：2016-08-10
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "casminfeeFunction")
	public void casminfeeFunction(){
		List<BiBaseDictionary> findBiBaseDictionary = projectService.getDictionary("casminfee");
		Map<String,String> casminfeeMap = new HashMap<String, String>();
		for(BiBaseDictionary casminfee : findBiBaseDictionary){
			casminfeeMap.put(casminfee.getCodeEncode(), casminfee.getCodeName());
		}
		String mapJosn = JSONUtils.toJson(casminfeeMap);
		WebUtils.webSendJSON(mapJosn);
	}
	
	/**  
	 * @Description： 门诊项目收费均次费用分析---横版---访问
	 * @Author：ldl
	 * @CreateDate：2016-08-08
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "ProjectToView", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatient/outpatientProject/project.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String ProjectToView() {
		Date date=new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);//获取年份
		dateString=""+year+","+year+",0,0,0,0,0,0";
		//查询住院（I）和门诊（C）两个类型的科室
		List<BiBaseOrganization> biorglist=outpatientWorkloadService.queryDeptForBiPublic("I,C");
		StringBuilder deptValue=new StringBuilder();
		deptValue.append("reg_dpcd");
		for(BiBaseOrganization bo:biorglist){
			deptValue.append(",");
			deptValue.append(bo.getOrgCode());
		}
		dimensionValue=deptValue.toString();
		dimensionString="reg_dpcd,科室";
		return "list";
	}

	
	/**  
	 * @Description：门诊项目收费均次费用分析---填充json---横版
	 * @Author：ldl
	 * @CreateDate：2016-08-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="queryProjectoadDatagrid")
	public void queryProjectoadDatagrid(){
		String resultData =  projectService.queryProjectoadDatagrid(datevo,dimensionString,dateType,dimensionValue);
		WebUtils.webSendString(resultData);
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
	
}
