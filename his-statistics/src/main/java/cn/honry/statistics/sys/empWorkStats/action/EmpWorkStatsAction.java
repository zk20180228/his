package cn.honry.statistics.sys.empWorkStats.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
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
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.sys.empWorkStats.service.EmpWorkStatsService;
import cn.honry.statistics.sys.empWorkStats.vo.EmpWorkStatsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @Description 坐诊医生工作量统计
 * @author  lyy
 * @createDate： 2016年7月13日 下午7:53:33 
 * @modifier lyy
 * @modifyDate：2016年7月13日 下午7:53:33
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/statistics/empWorkStats")
@SuppressWarnings({"all"})
public class EmpWorkStatsAction extends ActionSupport{

	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(EmpWorkStatsAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private String dept;
	
	private String emp;
	
	private String beginTime;
	
	private String endTime;
	private String page;
	private String rows;
	
	@Autowired
	@Qualifier(value = "empWorkStatsService")
	private EmpWorkStatsService empWorkStatsService;
	public void setEmpWorkStatsService(EmpWorkStatsService empWorkStatsService) {
		this.empWorkStatsService = empWorkStatsService;
	}
	
	/**  
	 * @Description：  跳转坐诊医生工作量统计
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="toView",results={@Result(name="list",location = "/WEB-INF/pages/stat/empWorkStats/empWorkStats.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toView()throws Exception{
		
		Date date = new Date();
		endTime = DateUtils.formatDateY_M_D(date);
		beginTime = DateUtils.formatDateYM(date)+"-01";
		
		return "list";
	}
	

	/**
	 * 
	 * <p>门诊科室下拉框</p>
	 * @Author: ldl
	 * @CreateDate: 2016-06-23
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月5日 下午3:18:54 
	 * @ModifyRmk: 修改注释模板，添加过时标记，因为此方法已经不用了 
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Deprecated
	@Action(value = "deptCombobox")
	public void deptCombobox(){
		List<SysDepartment> list=null; 
		try {
			list= empWorkStatsService.deptCombobox();

		} catch (Exception e) {
			
			//发生异常时，返回空集合
			list = new ArrayList<SysDepartment>();
			
			e.printStackTrace();
			logger.error("TJFXGL_ZZYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_ZZYSGZLTJ", "门诊统计分析_坐诊医生工作量统计", "2","0"), e);
		}
		
		String mapJosn = JSONUtils.toJson(list);
		WebUtils.webSendJSON(mapJosn);
	}
	

	/**
	 * 
	 * <p> 坐诊医生下拉框</p>
	 * @Author: ldl
	 * @CreateDate: 2016-06-23
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月5日 下午3:18:54 
	 * @ModifyRmk: 修改注释模板，添加过时标记，因为此方法已经不用了 
	 * @version: V1.0
	 * @throws:
	 *
	 */
	@Action(value = "empCombobox")
	public void empCombobox(){
		List<SysEmployee> list =null;
		try {
			list = empWorkStatsService.empCombobox(emp);
		} catch (Exception e) {
			
			//发生异常时，返回空集合
			list = new ArrayList<SysEmployee>();
			
			e.printStackTrace();
			logger.error("TJFXGL_ZZYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_ZZYSGZLTJ", "门诊统计分析_坐诊医生工作量统计", "2","0"), e);
		}
		
		String mapJosn = JSONUtils.toJson(list);
		WebUtils.webSendJSON(mapJosn);
	}
	
	
	
	/**  
	 * @Description：查询列表
	 * @Author：ldl
	 * @CreateDate：2016-06-23
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryList")
	public void queryList(){
		Map<String,Object> map =null;
		try {
			map = empWorkStatsService.queryList(beginTime,endTime,dept,emp,menuAlias,rows,page);
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList<EmpWorkStatsVo>());
			//发生异常时，返回空集合
			logger.error("TJFXGL_ZZYSGZLTJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("TJFXGL_ZZYSGZLTJ", "门诊统计分析_坐诊医生工作量统计", "2","0"), e);
		}
		
		String mapJosn = JSONUtils.toJson(map);
		WebUtils.webSendJSON(mapJosn);
	}
	
	
	
	
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	
	
	
}
