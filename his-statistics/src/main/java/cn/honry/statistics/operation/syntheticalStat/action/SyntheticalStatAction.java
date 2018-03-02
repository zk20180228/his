package cn.honry.statistics.operation.syntheticalStat.action;

import java.util.ArrayList;
import java.util.Date;
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
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.statistics.operation.syntheticalStat.service.SyntheticalStatService;
import cn.honry.statistics.operation.syntheticalStat.vo.InvoiceInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.MedicalInfoVo;
import cn.honry.statistics.operation.syntheticalStat.vo.PatientInfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 *  
 * @className：SyntheticalStatAction
 * @Description：  门诊综合查询Action 
 * @Author：aizhonghua
 * @CreateDate：2016-6-22 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-22 下午04:41:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/statistics/syntheticalStat")
@SuppressWarnings({"all"})
public class SyntheticalStatAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	private SyntheticalStatService syntheticalStatService;
	
	@Autowired
	@Qualifier(value = "syntheticalStatService")
	public void setSyntheticalStatService(SyntheticalStatService syntheticalStatService) {
		this.syntheticalStatService = syntheticalStatService;
	}
	
	// 记录异常日志
	private Logger logger = Logger.getLogger(SyntheticalStatAction.class);
	
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 类型0全部1挂号科室2挂号医生3挂号级别4合同单位
	 */
	private String type;

	/**
	 * 查询参数
	 */
	private String para;
	
	/**
	 * 是否模糊查询
	 */
	private String vague;
	
	/**
	 * 页数
	 */
	private String page;
	
	/**
	 * 条数
	 */
	private String rows;
	
	/**
	 * 患者id
	 */
	private String patientId;
	
	/**
	 * 门诊号
	 */
	private String registerNo;
	
	/**
	 * 病历号
	 */
	private String recordNo;
	
	/**
	 * 表
	 */
	private String tab;
	
	/**
	 * getters and setters
	 */
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
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
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public String getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
	public String getVague() {
		return vague;
	}
	public void setVague(String vague) {
		this.vague = vague;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	
	/**  
	 *  
	 * 跳转list页面
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZZHCX:function:view"}) 
	@Action(value = "listSyntheticalStat", results = { @Result(name = "list", location = "/WEB-INF/pages/stat/outpatientSyntheticalStat/syntheticalStatList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listSyntheticalStat() {

		endTime=DateUtils.formatDateY_M_D(new Date());
		startTime=DateUtils.formatDateY_M_D(DateUtils.addMonth(new Date(),-1));
		
		return "list";
	}
	
	/**  
	 *  
	 * 查询挂号患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryRegisterInfo")
	public void queryRegisterInfo(){
		Map<String,Object> map =null;
		try {
			if(StringUtils.isBlank(startTime)){
				startTime=DateUtils.formatDateY_M_D_H_M_S(DateUtils.addMonth(new Date(),-1));
			}
			if(StringUtils.isBlank(endTime)){
				endTime=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}

			map = syntheticalStatService.getRegisterInfo(page,rows,startTime,endTime,type,para,vague);
		} catch (Exception e) {
			map= new HashMap<String, Object>();
			map.put("total", 0);
			map.put("rows", new ArrayList());
			
			e.printStackTrace();
			logger.error("MZCX_MZZHCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZZHCX", "门诊查询_门诊综合查询", "2","0"), e);
		}
		String retJson = JSONUtils.toJson(map);

		WebUtils.webSendJSON(retJson);
	}
	
	/**  
	 *  
	 * 查询患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryPatientInfo")
	public void queryPatientInfo(){
		PatientInfoVo vo =null;
		try {
			vo = syntheticalStatService.queryPatientInfo(patientId);
		} catch (Exception e) {
				
			vo=new PatientInfoVo();
			e.printStackTrace();
			logger.error("MZCX_MZZHCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZZHCX", "门诊查询_门诊综合查询", "2","0"), e);
		}
		String retJson = JSONUtils.toJson(vo);
		
		WebUtils.webSendJSON(retJson);
	}
	
	/**  
	 *  
	 * 查询患者发票信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryInvoiceInfo")
	public void queryInvoiceInfo(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			List<InvoiceInfoVo> voList =  syntheticalStatService.queryInvoiceInfo(registerNo,tab);
			if(voList!=null && voList.size()>0){
				retMap.put("resMsg", "success");
				retMap.put("resCode", voList);
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode", "该患者暂无发票信息！");
			}
		} catch (Exception e) {
			
			retMap.put("resMsg", "error");
			retMap.put("resCode", "该患者暂无发票信息！");
			
			e.printStackTrace();
			logger.error("MZCX_MZZHCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZZHCX", "门诊查询_门诊综合查询", "2","0"), e);
		}
		
		String retJson = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(retJson);
	}
	
	/**  
	 *  
	 * 查询患者历史医嘱树
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryMedicalTree")
	public void queryMedicalTree(){
		
		List<TreeJson> treeList = null;
		try {
			treeList =  syntheticalStatService.queryMedicalTree(recordNo);
		} catch (Exception e) {

			treeList=new ArrayList<TreeJson>();
			e.printStackTrace();
			logger.error("MZCX_MZZHCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZZHCX", "门诊查询_门诊综合查询", "2","0"), e);
		}
		
		String treeJson = JSONUtils.toJson(treeList);
		
		WebUtils.webSendJSON(treeJson);
	}
	
	/**  
	 *  
	 * 查询患者历史医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="queryMedicalInfo")
	public void queryMedicalInfo(){
		Map<String,Object> retMap = new HashMap<String, Object>();
		try {
			List<MedicalInfoVo> voList =  syntheticalStatService.queryMedicalInfo(registerNo,tab);
			if(voList!=null && voList.size()>0){
				retMap.put("resMsg", "success");
				retMap.put("resCode", voList);
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode", "该看诊号无医嘱信息！");
			}
		} catch (Exception e) {
			
			retMap.put("resMsg", "error");
			retMap.put("resCode", "该看诊号无医嘱信息！");
			e.printStackTrace();
			logger.error("MZCX_MZZHCX", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZCX_MZZHCX", "门诊查询_门诊综合查询", "2","0"), e);
		}
		String treeJson = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(treeJson);
	}
	
}
