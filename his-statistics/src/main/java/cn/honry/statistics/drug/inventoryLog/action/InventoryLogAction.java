package cn.honry.statistics.drug.inventoryLog.action;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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

import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.statistics.drug.inventoryLog.service.InventoryLogService;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.DownloadUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/***
 * 盘点日志查询(统计)
 * @Description:
 * @author: zpty
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/InventoryLog")
public class InventoryLogAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(InventoryLogAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	@Autowired
    @Qualifier(value = "inventoryLogService")
 	private InventoryLogService inventoryLogService;
	public void setInventoryLogService(InventoryLogService inventoryLogService) {
		this.inventoryLogService = inventoryLogService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	private HttpServletResponse response = ServletActionContext.getResponse();
	
	/**
	 * 当前页数，用于分页查询
	 */
	private String page;
	
	/**
	 * 分页条数，用于分页查询
	 */
	private String rows;

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
	 * 查询科室
	 */
	private String dept;
	/**
	 * 开始时间
	 */
	private String Stime;
	/**
	 * 结束时间
	 */
	private String Etime;
	/**
	 * 药品
	 */
	private String drug;
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getStime() {
		return Stime;
	}
	public void setStime(String stime) {
		Stime = stime;
	}
	public String getEtime() {
		return Etime;
	}
	public void setEtime(String etime) {
		Etime = etime;
	}
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	/**  
	 *  
	 * @Description： 盘点日志查询(统计)页面
	 * @Author：zpty
	 * @CreateDate：2016-6-22 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("deprecation")
	@RequiresPermissions(value={"PDRZCX:function:view"}) 
	@Action(value = "listInventoryLog", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/inventoryLog/inventoryLogList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listInventoryLog() {
		Date date = new Date();
		Etime = DateUtils.formatDateY_M_D(date);
		Stime = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  盘点日志查询(统计)列表
	 * @Author：zpty
	 * @CreateDate：2015-6-22 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("deprecation")
	@Action(value = "queryInventoryLog", results = { @Result(name = "json", type = "json") })
	public void queryInventoryLog() {
		try{
			if(StringUtils.isBlank(Stime)){
				Date date = new Date();
				Stime = DateUtils.formatDateYM(date)+"-01 00:00:00";
			}
			if(StringUtils.isBlank(Etime)){
				Date date = new Date();
				Etime = DateUtils.formatDateY_M_D(date)+" 23:59:59";
			}
			Map<String,Object> retMap = new HashMap<String,Object>();
			List<InventoryLogVo> list = inventoryLogService.queryInventoryLog(Stime, Etime, dept, page, rows,drug);
			int total = inventoryLogService.queryInventoryLogTotle(Stime, Etime, dept,drug);
			retMap.put("total", total);
			retMap.put("rows", list);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_PDRZCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_PDRZCX", "住院统计_盘点日志查询", "2", "0"), e);
		}
	}

	/**  
	 *  
	 * @Description：  科室下拉框(挂号科室)
	 * @Author：zpty
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "departmentComboboxRegister", results = { @Result(name = "json", type = "json") })
	public void departmentComboboxRegister() {
		try{
			List<SysDepartment> deptList = new ArrayList<SysDepartment>();
			deptList = inventoryLogService.getComboboxdept();
			String json = JSONUtils.toJson(deptList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_PDRZCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_PDRZCX", "住院统计_盘点日志查询", "2", "0"), e);
		}
	}

	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2015-12-25 下午05:11:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "drugNameComboboxRegister", results = { @Result(name = "json", type = "json") })
	public void drugNameComboboxRegister() {
		try{
			List<DrugInfo> drugList = new ArrayList<DrugInfo>();
			drugList = inventoryLogService.getComboboxdrug();
			String json = JSONUtils.toExposeJson(drugList, false, null, "name","code","drugNamepinyin","drugCommonname","drugBasicwb");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_PDRZCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_PDRZCX", "住院统计_盘点日志查询", "2", "0"), e);
		}
	}
	
	/**
	 * @Description:导出 
	 * @Author： lt @CreateDate： 2015-9-10
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 **/
	@Action(value = "expInvLogList", results = { @Result(name = "json", type = "json") })
	public void expInvLogList() {
		PrintWriter out =null;
		try {
			List<InventoryLogVo> list = inventoryLogService.queryInvLogExp(Stime, Etime,dept, drug);
			System.out.println(list.isEmpty());
			Map<String, String> map1 = employeeInInterService.queryEmpCodeAndNameMap();
			Map<String, String> map = deptInInterService.querydeptCodeAndNameMap();
			for (InventoryLogVo inventoryLogVo : list) {
				inventoryLogVo.setDeptName(map.get(inventoryLogVo.getDeptName()));
				inventoryLogVo.setUserName(map1.get(inventoryLogVo.getUserName()));
			}
			if (list == null || list.isEmpty()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write("根据您选择的下载条件，不存在具备您要求的记录！");
			}
			String head = "";
	
			String name = "盘点日志查询";
			String[] headMessage = { "盘点科室", "盘点药品", "批号","规格", "零售价","包装单位", "包装数量","盘存数量", "单位","货位号","操作时间","操作人" };
	
			for (String message : headMessage) {
				head += "," + message;
			}
			head = head.substring(1);
			FileUtil fUtil = new FileUtil();
			String fileName = name + DateUtils.formatDateY_M_D_H_M(new Date()) + ".csv";
			String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF") + "/downLoad/" + fileName;
			fUtil.setFilePath(filePath);
			fUtil.write(head);
			
//			out = WebUtils.getResponse().getWriter();
			
				fUtil = inventoryLogService.export(list, fUtil);
				fUtil.close();
				DownloadUtils.download(request, response, filePath, HisParameters.PREFIXFILENAME + fileName);
//				out.write("success");
		} catch (Exception e) {
//			out.write("error");
			logger.error("ZYTJ_PDRZCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_PDRZCX", "住院统计_盘点日志查询", "2", "0"), e);
		}finally {
//			out.flush();
//			out.close();
		}
	}
}
