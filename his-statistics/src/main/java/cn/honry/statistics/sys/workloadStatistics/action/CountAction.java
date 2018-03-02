package cn.honry.statistics.sys.workloadStatistics.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

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
import cn.honry.base.bean.model.Registration;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.statistics.deptWorkCount.vo.DeptWorkCountVo;
import cn.honry.statistics.sys.workloadStatistics.service.CountService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description 科室工作量统计
 * @author  lyy
 * @createDate： 2016年7月14日 上午10:46:43 
 * @modifier lyy
 * @modifyDate：2016年7月14日 上午10:46:43
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/workloadStatistics")
@SuppressWarnings({"all"})
public class CountAction extends ActionSupport implements ModelDriven<Registration>{
	
	private static final long serialVersionUID = 1L;
	
	private Registration registration=new Registration();
	private String  STime;//开始时间
	private String  ETime;//结束时间
	private String  dept;//部门编号
	private String  page;//当前页
	private String  rows;//每页显示的记录数
	
	private CountService countService;
	@Autowired
    @Qualifier(value = "countService")
	public void setCountService(CountService countService) {
		this.countService = countService;
	}
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(CountAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Override
	public Registration getModel() {
		return registration;
	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	public Registration getRegistration() {
		return registration;
	}
	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
	public String getSTime() {
		return STime;
	}
	public void setSTime(String sTime) {
		STime = sTime;
	}
	public String getETime() {
		return ETime;
	}
	public void setETime(String eTime) {
		ETime = eTime;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
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
	
	/**  
	 *  
	 * @Description：  科室工作量统计列表
	 * @Author：wujiao
	 * @CreateDate：2015-8-25 下午03:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"KSGZLTJ:function:view"}) 
	@Action(value = "listCount", results = { @Result(name = "list", location = "/WEB-INF/pages/register/workloadStatistics/countList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listCount() {
		Date date = new Date();
		ETime = DateUtils.formatDateY_M_D(date);
		STime = DateUtils.formatDateYM(date)+"-01";
		
		return "list";
	}
	
	/**
	 * 
	 * <p>课时工作量统计列表 </p>
	 * @Author: 
	 * @CreateDate: 2017年7月5日 上午10:19:52 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月5日 上午10:19:52 
	 * @ModifyRmk:添加过时标记，因为目前数据是从mongodb中获取的 ，而此方法的数据源是oracle(spark实现 ),因此强烈不提倡使用，前台也没有此方法的url了，如果用，你只需要改前台jsp 
	 * @version: V1.0
	 * @throws:
	 *
	 */
	//@RequiresPermissions(value={"a:function:view","b:function:view"},logical=Logical.OR) 多栏目同权限查询
	@RequiresPermissions(value={"KSGZLTJ:function:query"}) 
	@Action(value = "listCountQuery", results = { @Result(name = "json", type = "json") })
	@Deprecated
	public void listCountQuery() {
		
		try {
			Map<String,Object> countVoMap = countService.getInfoNow(dept,STime,ETime,page,rows,menuAlias);
			String json =JSONUtils.toJson(countVoMap);
			
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("TJFXGL_KSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_KSGZLTJ", "门诊统计分析_科室工作量统计", "2","0"), e);
			
		}
	}
	

	/**
	 * @Description:根据开始时间，结束时间，科室编号，查询科室的工作量
	 * @param dept 科室编号
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param page 查询的页码
	 * @param rows 每页显示的记录数
	 * @param menuAlias 栏目的别名
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月28日 上午9:35:12
	 */
	@RequiresPermissions(value="KSGZLTJ:function:query")
	@Action(value="listDeptWorkCountByMongo",results={@Result(name="json",type="json")})
	public void listDeptWorkCountByMongo(){
		
		Map<String, Object> map=null;
		try {
			
			//对结束时间减一天，前台的时间多算了一天，以前数据从sql查询书造成的
			ETime=DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(ETime), -1));
			map = countService.listDeptWorkCountByMongo(dept,STime,ETime,page,rows,menuAlias);
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("TJFXGL_KSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_KSGZLTJ", "门诊统计分析_科室工作量统计", "2","0"), e);
		}
		if(map==null){
			map.put("total", 0);
			map.put("rows", new ArrayList<DeptWorkCountVo>());
		}
		String json = JSONUtils.toJson(map);
		
		WebUtils.webSendJSON(json);
	}
	

}
