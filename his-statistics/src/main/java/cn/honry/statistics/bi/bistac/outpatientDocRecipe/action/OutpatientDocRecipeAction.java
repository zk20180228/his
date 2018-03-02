package cn.honry.statistics.bi.bistac.outpatientDocRecipe.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.handing.service.InnerHandingService;
import cn.honry.inner.statistics.outpatientDocRecipe.service.InnerOutpatientDocRecipeService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.service.OutpatientDocRecipeService;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.WebUtils;
import net.sf.json.JSONObject;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/OutpatientDocRecipe")
public class OutpatientDocRecipeAction {
	// 记录异常日志
	private Logger logger = Logger.getLogger(OutpatientDocRecipeAction.class);
	private String sTime;
	private String eTime;
	private String dept;
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	private String expxrt;
	public String emp;
	private String page;
	private String rows;
	/**
	 * 用于判断查询的模式
	 */
	private String selectStatus;
	/**
	 * 当前用户的职务
	 */
	private String post;
	/**
	 * 当前用户的jobno
	 */
	private String nowjobno;

	/**
	 * 当前用户的科室
	 */
	private String nowDept;
	
	
	public String getNowDept() {
		return nowDept;
	}
	public void setNowDept(String nowDept) {
		this.nowDept = nowDept;
	}
	public String getNowjobno() {
		return nowjobno;
	}
	public void setNowjobno(String nowjobno) {
		this.nowjobno = nowjobno;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getSelectStatus() {
		return selectStatus;
	}
	public void setSelectStatus(String selectStatus) {
		this.selectStatus = selectStatus;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getExpxrt() {
		return expxrt;
	}
	public void setExpxrt(String expxrt) {
		this.expxrt = expxrt;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value = "innerOutpatientDocRecipeService")
	private InnerOutpatientDocRecipeService innerOutpatientDocRecipeService;
	public void setInnerOutpatientDocRecipeService(InnerOutpatientDocRecipeService innerOutpatientDocRecipeService) {
		this.innerOutpatientDocRecipeService = innerOutpatientDocRecipeService;
	}
	@Autowired
	@Qualifier(value = "innerHandingService")
	private InnerHandingService innerHandingService;
	public void setInnerHandingService(InnerHandingService innerHandingService) {
		this.innerHandingService = innerHandingService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Resource
	private OutpatientDocRecipeService outpatientDocRecipeService;
	public void setOutpatientDocRecipeService(OutpatientDocRecipeService outpatientDocRecipeService) {
		this.outpatientDocRecipeService = outpatientDocRecipeService;
	}
	/** 
	* @Description: 门诊医生开单工作量统计页面
	* @return String    返回类型 
	* @author zx 
	* @date 2017年7月14日
	*/
	@Action(value = "outpatientDocRecipeList", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/bi/outpatientDocRecipe/outpatientDocRecipe.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String operationCostList(){
		return "list";
	}
	//测试
	//http://localhost:8080/his-portal/statistics/OutpatientDocRecipe/testInit.action?startTime=2015-12-01&endTime=2015-12-01
	@Action(value = "testInit")
	public void testInit(){
		try {
			innerHandingService.handing("MZYSKDGZL", "1", "2016-03-20");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Action(value = "checkZW", results = { @Result(name = "json", type = "json") })
	public void checkZW(){
		try {
			selectStatus = "0";
			String zw = parameterInnerService.getparameter("qygjzwcxtjsj");
			if("1".equals(zw)){
				post = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getPost();
				if(post==null){
					selectStatus = "1";
					nowjobno = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
					nowDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
				}else{
					if(!"1".equals(post) && !"2".equals(post) && !"27".equals(post) && !"28".equals(post)){
						nowjobno = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
						nowDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
						selectStatus = "1";
					}else{
						if("27".equals(post)||"28".equals(post)){
							nowjobno = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
							nowDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
							selectStatus = "2";
						}
					}
				}
			}
			
			JSONObject jsonO = new JSONObject();
			jsonO.accumulate("selectStatus", selectStatus);
			jsonO.accumulate("nowJobno", nowjobno);
			jsonO.accumulate("nowDept", nowDept);
			String json = jsonO.toString();
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			
			e.printStackTrace(); 
			logger.error("MZYSKDGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZYSKDGZLTJ", "门诊医生开单工作量统计", "2", "0"), e);
		}
	}
	/** 
	* @Description:门诊医生开单工作量统计
	* @return void    返回类型 
	* @throws 
	* @author zx 
	* @date 2017年7月26日
	*/
	@RequiresPermissions(value={"MZGXSRTJ:function:query"}) 
	@Action(value = "listStatisticsQuery", results = { @Result(name = "json", type = "json") })
	public void listStatisticsQuery()  {
		Map<String, Object> map=null;
		try {
			if(StringUtils.isBlank(dept)){
				List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
				if(deptList == null || deptList.size() == 0){
					expxrt = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
				}else{
					for(int i = 0;i<deptList.size();i++){
						dept += deptList.get(i).getDeptCode() + ",";
					}
					if(dept.endsWith(",")){
						dept = dept.substring(0, dept.lastIndexOf(","));
					}
				}
			}
			map = outpatientDocRecipeService.listStatisticsQueryByES(dept,expxrt,sTime,eTime,page,rows);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			//发生异常时返回空内容
			map =new HashMap<String, Object>();
			map.put("total",0);
			map.put("rows", new ArrayList<StatisticsVo>());
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
			e.printStackTrace();
			logger.error("TJFXGL_MZYSKDGZITJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_MZYSKDGZITJ", "门诊统计分析_门诊医生开单工作量统计", "2","0"), e);
		}
	}
}
