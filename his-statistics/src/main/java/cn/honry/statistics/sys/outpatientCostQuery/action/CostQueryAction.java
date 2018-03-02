package cn.honry.statistics.sys.outpatientCostQuery.action;

import java.util.ArrayList;
import java.util.HashMap;
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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.frequency.service.FrequencyInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.statistics.sys.outpatientCostQuery.service.CostQueryService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 
 * @Description 门诊收费查询action
 * @author  lyy
 * @createDate： 2016年7月13日 下午7:17:31 
 * @modifier lyy
 * @modifyDate：2016年7月13日 下午7:17:31
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/statistics/outpatientCostQuery")
@SuppressWarnings({"all"})
public class CostQueryAction extends ActionSupport{

	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	/**
	 *开始时间 
	 */
	private String beginTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 发票号
	 */
	private String invoiceNo;
	
	/**
	 *当前页 
	 */
	private String page;
	
	/**
	 * 每页显示的行数
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

	// 记录异常日志
	private Logger logger = Logger.getLogger(CostQueryAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	@Autowired
	@Qualifier(value = "costQueryService")
	private CostQueryService costQueryService;
	public void setCostQueryService(CostQueryService costQueryService) {
		this.costQueryService = costQueryService;
	}
	
	/**
	 *  员工业务接口的service
	 */
	@Resource
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	
	/**
	 * 内部接口：科室接口 
	 */
	@Resource
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	@Resource
	private PatinentInnerService patinentInnerService;
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	
	@Resource
	private FrequencyInInterService frequencyInInterService;
	public void setFrequencyInInterService(FrequencyInInterService frequencyInInterService) {
		this.frequencyInInterService = frequencyInInterService;
	}
	
	/**
	 * 编码查询接口Service
	 */
	private CodeInInterService codeInInterService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setCodeInInterService(CodeInInterService codeInInterService) {
		this.codeInInterService = codeInInterService;
	}
	
	/**  
	 * @Description：  跳转门诊费用查询
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value="toView",results={@Result(name="list",location = "/WEB-INF/pages/stat/outpatientCostQuery/outpatientCostQuery.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String toViewFeeStatList()throws Exception{

		return "list";
	}
	
	/**  
	 * @Description：  根据发票号，开始时间，结束时间查询发票汇总
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findInvoiceNoSummary")
	public void findInvoiceNoSummary(){
		
		Map<String, Object> map=null;
		try {
			map = costQueryService.findInvoiceNoSummary(invoiceNo,beginTime,endTime,page,rows);
		} catch (Exception e) {
			
			//当发生异常时，返回空内容
			map.put("rows", new ArrayList());
			map.put("total", 0);
			
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}

		String mapJosn = JSONUtils.toJson(map);
		WebUtils.webSendJSON(mapJosn);
	}
	
	/**  
	 * @Description：  根据发票号，开始时间，结束时间查询发票明细
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findInvoiceDetailed")
	public void findInvoiceDetailed(){
		
		Map<String, Object> map=null;
		try {
			map = costQueryService.findInvoiceDetailed(invoiceNo,beginTime,endTime,page,rows);
		} catch (Exception e) {
			
			//当发生异常时，返回空内容
			map.put("rows", new ArrayList());
			map.put("total", 0);
			
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
		
		String mapJosn = JSONUtils.toJson(map);

		WebUtils.webSendJSON(mapJosn);
	}
	
	
	/**  
	 * @Description：   根据发票号，开始时间，结束时间查询费用明细
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "findCostDetailed")
	public void findCostDetailed(){
		
		Map<String, Object> map=null;
		try {
			map = costQueryService.findCostDetailed(invoiceNo,beginTime,endTime,page,rows);
		} catch (Exception e) {
			
			//当发生异常时，返回空内容
			map.put("rows", new ArrayList());
			map.put("total", 0);
			
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
		
		String mapJosn = JSONUtils.toJson(map);
		WebUtils.webSendJSON(mapJosn);
	}
	
	
	/**  
	 * @Description： 渲染用户
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "userFunction")
	public void userFunction(){
		
		try {
			List<SysEmployee> employeeList =  employeeInInterService.queryDiangDoctor(null);
			Map<String,String> empMap = new HashMap<String, String>();
			for(SysEmployee emp : employeeList){
				if(emp.getUserId()!=null){
					empMap.put(emp.getUserId().getId(), emp.getName());
				}
			}
			String mapJosn = JSONUtils.toJson(empMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 渲染合同单位
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "countFunction")
	public void countFunction(){
		
		try {
			List<BusinessContractunit> countList =  patinentInnerService.queryUnitCombobox();
			Map<String,String> countMap = new HashMap<String, String>();
			for(BusinessContractunit count : countList){
				countMap.put(count.getId(), count.getName());
			}
			String mapJosn = JSONUtils.toJson(countMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 渲染用户
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "empFunction")
	public void empFunction(){
		
		try {
			List<SysEmployee> employeeList =  employeeInInterService.queryDiangDoctor(null);
			Map<String,String> empMap = new HashMap<String, String>();
			for(SysEmployee emp : employeeList){
				empMap.put(emp.getId(), emp.getName());
			}
			String mapJosn = JSONUtils.toJson(empMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 渲染科室
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "deptFunction")
	public void deptFunction(){
		
		try {
			List<SysDepartment> departmentList =  deptInInterService.getDept();
			Map<String,String> deptMap = new HashMap<String, String>();
			for(SysDepartment dept : departmentList){
				deptMap.put(dept.getId(), dept.getDeptName());
			}
			String mapJosn = JSONUtils.toJson(deptMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 渲染系统类别
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "sysFunction")
	public void sysFunction(){
		
		try {
			List<BusinessDictionary> systemtypeList = codeInInterService.getDictionary("systemType");
			Map<String,String> sysMap = new HashMap<String, String>();
			for(BusinessDictionary sys : systemtypeList){
				sysMap.put(sys.getId(), sys.getName());
			}
			String mapJosn = JSONUtils.toJson(sysMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 渲染最小费用
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "feeFunction")
	public void feeFunction(){
		
		try {
			List<BusinessDictionary> codeRegistertypeList = codeInInterService.getDictionary("drugMinimumcost");
			Map<String,String> feeMap = new HashMap<String, String>();
			for(BusinessDictionary fee : codeRegistertypeList){
				feeMap.put(fee.getId(), fee.getName());
			}
			String mapJosn = JSONUtils.toJson(feeMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	
	}
	
	/**  
	 * @Description： 渲染药品性质
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "drugFunction")
	public void drugFunction(){
		
		try {
			List<BusinessDictionary> codeRegistertypeList = codeInInterService.getDictionary("drugProperties");
			Map<String,String> drugMap = new HashMap<String, String>();
			for(BusinessDictionary drug : codeRegistertypeList){
				drugMap.put(drug.getId(), drug.getName());
			}
			String mapJosn = JSONUtils.toJson(drugMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 渲染剂型
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "doseFunction")
	public void doseFunction(){
		
		try {
			List<BusinessDictionary> codeRegistertypeList = codeInInterService.getDictionary("dosageForm");
			Map<String,String> doseMap = new HashMap<String, String>();
			for(BusinessDictionary dose : codeRegistertypeList){
				doseMap.put(dose.getId(), dose.getName());
			}
			String mapJosn = JSONUtils.toJson(doseMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	/**  
	 * @Description： 渲染频次
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "frequencyFunction")
	public void frequencyFunction(){
		
		try {
			List<BusinessFrequency> frequencyList =  frequencyInInterService.queryFrequencyList();
			Map<String,String> frequencyMap = new HashMap<String, String>();
			for(BusinessFrequency frequency : frequencyList){
				frequencyMap.put(frequency.getId(), frequency.getName());
			}
			String mapJosn = JSONUtils.toJson(frequencyMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
	}
	
	
	/**  
	 * @Description： 渲染统计大类
	 * @Author：ldl
	 * @CreateDate：2016-06-22
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "itemFunction")
	public void itemFunction(){
		
		try {
			List<MinfeeStatCode> minfeeStatCodeList =  costQueryService.itemFunction();
			Map<String,String> itemMap = new HashMap<String, String>();
			for(MinfeeStatCode item : minfeeStatCodeList){
				itemMap.put(item.getFeeStatCode(), item.getFeeStatName());
			}
			String mapJosn = JSONUtils.toJson(itemMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZCX_MZSFCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZSFCX", "门诊查询_门诊收费查询", "2","0"), e);
		}
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
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	
	
}
