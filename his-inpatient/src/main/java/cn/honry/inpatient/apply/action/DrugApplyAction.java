package cn.honry.inpatient.apply.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.apply.Service.DrugApplyService;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * 退费申请
 * @ClassName: DrugApplyAction 
 * @Description: 
 * @author wfj
 * @date 2016年5月12日 上午11:28:28 
 * 该模块位于  住院护士站---退费申请
 * 此处包括退药申请和退费（非药品）申请两部分。
 * 可以对已经发生的住院费用发起退费申请，也可对退费申请进行取消操作
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/applydrug")
public class DrugApplyAction extends ActionSupport {

	private Logger logger=Logger.getLogger(DrugApplyAction.class);
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private static final long serialVersionUID = 1L;
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	//病历号
	private String medicalrecordId; 
	//住院流水号
	private String inpatientNo;


	//记账时间
	private String firstDate;  
	//直到今天
	private String endDate;   
	
	private List<ApplyVo> applyVoList;
	/*申请区药品json*/
	private String drugJson;
	/*申请区非药品json*/
	private String notDrugJson;
	/*id集合*/
	private String ids[];
	/*项目名称*/
	private String objName;
	//患者名称
	private String name; 
	//json文件
	private String cancelitemJson;  
	//退的金额
	private String  money;
	//病床使用表id
	private String bedinfoId;

	/***
	 * 注入本类Service
	 */
	@Autowired
	@Qualifier(value="drugApplyService")
	private DrugApplyService drugApplyService;
	public void setDrugApplyService(DrugApplyService drugApplyService) {
		this.drugApplyService = drugApplyService;
	}
	/***
	 * 注入员工Service
	 */
	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}

	/***
	 * 公共编码资料service实现层
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
    public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
    /**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public PatinentInnerService getPatinentInnerService() {
		return patinentInnerService;
	}
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	
	/***
	 * 护士站退费申请链接
	 * @Title: listDrugApply 
	 * @author  WFJ
	 * @createDate ：2016年4月20日
	 * @return String
	 * @version 1.0
	 */
	@RequiresPermissions(value={"TFSQ:function:view"})
	@Action(value="listDrugApply",results={@Result(name="list",location="/WEB-INF/pages/drug/apply/applyList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public  String listDrugApply(){
		SysDepartment sys= ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		if(sys!=null){
			 ServletActionContext.getRequest().setAttribute("deptId",sys.getDeptCode());//获取当前病区
			 ServletActionContext.getRequest().setAttribute("deptName",sys.getDeptName());//获取当前病区
		}
		return "list";
	}
	
	/***
	 * 跳转直接退费链接
	 * @Title: listDirect 
	 * @author  WFJ
	 * @createDate ：2016年5月16日
	 * @return String
	 * @version 1.0
	 */
	@RequiresPermissions(value={"TFSQ:function:view"})
	@Action(value="listDirect",results={@Result(name="direct",location="/WEB-INF/pages/drug/apply/directList.jsp")},interceptorRefs={@InterceptorRef(value="manageInterceptor")})
	public  String listDirect(){
		SysDepartment sys= ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		if(sys!=null){
			 ServletActionContext.getRequest().setAttribute("deptId",sys.getDeptCode());//获取当前病区
			 ServletActionContext.getRequest().setAttribute("deptName",sys.getDeptName());//获取当前病区
		}
		return "direct";
	}
	
	/**
	 * 患者信息树
	 * @author  lyy
	 * @createDate： 2016年1月9日 下午2:34:04 
	 * @modifier ： WFJ
	 * @modifyDate：2016年4月18日 下午4:13:07
	 * @modifyRmk：  获取登录科室下的患者
	 *  若登录科室为病区类型，则查询病区下的患者
	 *  若登录科室为科室类型，则查询部门科室关系，找到科室相关病区下的患者
	 * @version 2.0
	 */
	@Action(value="treeDrugApply")
	public void treeDrugApply(){
		try{
			String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			List<TreeJson> treeDrug= drugApplyService.treePatient(deptId);
			String json = JSONUtils.toJson(treeDrug);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	/***
	 * 根据病历号查询患者住院信息
	 * @Title: patientBasicData 
	 * @author  WFJ
	 * @createDate ：2016年4月20日
	 * @return void
	 * @version 1.0
	 */
	@Action(value="patientBasicData")
	public void patientBasicData(){
		try{
			InpatientInfoNow inpatientInfo = drugApplyService.patientBasicData(inpatientNo);
			String json = JSONUtils.toExposeJson(inpatientInfo, true, null, 
					"inpatientNo","medicalrecordId","idcardNo","patientName",
					"pactCode","deptCode","deptName","bedId","bedName");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	/***
	 * 药品列表信息
	 * @Title: queryDrugApply 
	 * @author  WFJ
	 * @createDate ：2016年4月20日
	 * @throws Exception
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "queryDrugApply")
	public void queryDrugApply(){
		try{
			//   金额信息目前是显示的是费用金额，因暂时没有与医保进行对接
			if(StringUtils.isBlank(medicalrecordId) || StringUtils.isBlank(inpatientNo)){
				applyVoList = new ArrayList<ApplyVo>();
				String json = JSONUtils.toJson(applyVoList,false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(json);
			}else{
				ApplyVo applySerch = new ApplyVo();
				applySerch.setMedicalrecordId(medicalrecordId);
				applySerch.setInpatientNo(inpatientNo);
				if(StringUtils.isNotBlank(objName)){
					applySerch.setObjName(objName);
				}
				if(StringUtils.isNotBlank(firstDate)){
					applySerch.setFirstDate(firstDate);
				}
				if(StringUtils.isNotBlank(endDate)){
					applySerch.setEndDate(endDate);
				}
				applyVoList = drugApplyService.getPage(applySerch);
				DecimalFormat df = new DecimalFormat("#.00");
				if(applyVoList.size()>0){
					for (ApplyVo applyVo : applyVoList) {
						String moneySum = df.format(applyVo.getMoneySum());
						applyVo.setMoneySum(Double.valueOf(moneySum));
					}
				}
				String json = JSONUtils.toJson(applyVoList,false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(json);
			}
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	/**
	 * 已处于申请状态的药品信息
	 * @author  lyy
	 * @modifier wfj
	 * @modifyDate：2016年1月23日 上午10:48:29  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDrugBack")
	public void queryDrugBack(){
		try{
			if(StringUtils.isBlank(medicalrecordId) || StringUtils.isBlank(inpatientNo)){
				applyVoList = new ArrayList<ApplyVo>();
				String json = JSONUtils.toJson(applyVoList,false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(json);
			}else{
				ApplyVo applySerch = new ApplyVo();
				applySerch.setInpatientNo(inpatientNo);
				applySerch.setMedicalrecordId(medicalrecordId);
				// 条件查询
				if(StringUtils.isNotBlank(objName)){
					applySerch.setObjName(objName);
				}
				if(StringUtils.isNotBlank(firstDate)){
					applySerch.setFirstDate(firstDate);
				}
				if(StringUtils.isNotBlank(endDate)){
					applySerch.setEndDate(endDate);
				}
				applyVoList = drugApplyService.getPageBack(applySerch);
				String json = JSONUtils.toJson(applyVoList);
				WebUtils.webSendJSON(json);
			}
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	/***
	 * 患者非药品信息查询
	 * @Title: queryNotDrugApply 
	 * @author  WFJ
	 * @createDate ：2016年4月25日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "queryNotDrugApply")
	public void queryNotDrugApply(){
		try{
			if(StringUtils.isBlank(medicalrecordId) || StringUtils.isBlank(inpatientNo)){
				applyVoList = new ArrayList<ApplyVo>();
				String json = JSONUtils.toJson(applyVoList,false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(json);
			}else{
				ApplyVo applySerch = new ApplyVo();
				applySerch.setInpatientNo(inpatientNo);
				applySerch.setMedicalrecordId(medicalrecordId);
				// 条件查询
				if(StringUtils.isNotBlank(objName)){
					applySerch.setObjName(objName);
				}
				if(StringUtils.isNotBlank(firstDate)){
					applySerch.setFirstDate(firstDate);
				}
				if(StringUtils.isNotBlank(endDate)){
					applySerch.setEndDate(endDate);
				}
				applyVoList = drugApplyService.getPageNotDrug(applySerch);
				String json = JSONUtils.toJson(applyVoList,false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(json);
			}
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	/***
	 * 已处于申请状态的非药品列表信息
	 * @Title: queryNotDrugBack 
	 * @author  WFJ
	 * @createDate ：2016年4月25日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "queryNotDrugBack")
	public void queryNotDrugBack(){
		try{
			if(StringUtils.isBlank(medicalrecordId) || StringUtils.isBlank(inpatientNo)){
				applyVoList = new ArrayList<ApplyVo>();
				String json = JSONUtils.toJson(applyVoList,false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(json);
			}else{
				ApplyVo applySerch = new ApplyVo();
				applySerch.setInpatientNo(inpatientNo);
				applySerch.setMedicalrecordId(medicalrecordId);
				// 条件查询
				if(StringUtils.isNotBlank(objName)){
					applySerch.setObjName(objName);
				}
				if(StringUtils.isNotBlank(firstDate)){
					applySerch.setFirstDate(firstDate);
				}
				if(StringUtils.isNotBlank(endDate)){
					applySerch.setEndDate(endDate);
				}
				applyVoList = drugApplyService.getPageDrugBack(applySerch);
				String json = JSONUtils.toJson(applyVoList,false, DateUtils.DATE_FORMAT, false);
				WebUtils.webSendJSON(json);
			}
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	/**
	 * 退费申请保存
	 * @author  lyy
	 * @createDate： 2016年1月12日 上午10:47:51 
	 * @modifier wfj
	 * @modifyDate：2016年1月12日 上午10:47:51  
	 * @modifyRmk：  
	 * @version 2.0
	 */
	@Action(value = "saveConsultation")
	public void saveConsultation(){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = drugApplyService.saveAdd(medicalrecordId,drugJson,notDrugJson);
		} catch (Exception e) {
			map.put("resCode","-1");
			map.put("resMsg","未知错误，请联系系统管理员！");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("ZYSF_TFSQ", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_TFSQ", "住院收费_退费申请", "2", "0"), e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	/**
	 * 取消退费申请
	 * @author  lyy
	 * @createDate： 2016年1月29日 下午4:34:04 
	 * @modifier lyy
	 * @modifyDate：2016年1月29日 下午4:34:04  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "delDrugOrNotDrugApply")
	public void delDrugOrNotDrugApply(){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			if(ids!=null){
				map = drugApplyService.delDrugOrNotDrugApply(ids);
			}else{
				map.put("resCode","1");
				map.put("resMes","success");
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
			e.printStackTrace();
		}
	}
	
	/***
	 * 直接退费
	 * @Title: directSave 
	 * @author  WFJ
	 * @createDate ：2016年5月16日
	 * @return void
	 * @version 1.0
	 */
	@Action(value="directSave")
	public void directSave(){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			List<ApplyVo> drugList = JSONUtils.fromJson(drugJson, new TypeToken<List<ApplyVo>>(){});
			List<ApplyVo> notDrugList = JSONUtils.fromJson(notDrugJson, new TypeToken<List<ApplyVo>>(){});
			map = drugApplyService.directSavePD(medicalrecordId, drugList, notDrugList);
			if("0".equals(map.get("resCode"))){
				map = drugApplyService.directSave(medicalrecordId, drugList, notDrugList);
			}
		} catch (Exception e) {
			map.put("resCode",-1);
			map.put("resMsg","未知错误，请联系系统管理员！");
			e.printStackTrace();
			//hedong 20170407 异常信息输出至日志文件
			logger.error("ZYSF_ZJTF", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZJTF", "住院收费_直接退费", "2", "0"), e);
		}finally {
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}
		

	}
	
/*--------------------		弹框		-------------------
	/**
	 * 根据病历号查询患者信息
	 * @author  lyy
	 * @createDate： 2016年1月28日 下午6:44:53 
	 * @modifier lyy
	 * @modifyDate：2016年1月28日 下午6:44:53  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "searchPatinent")
	public void searchPatinent(){
		try{
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}
			//通过接口查询就诊卡号对应的病历号
			String mId = medicalrecordId;
			List<ApplyVo> apply=drugApplyService.queryInpatientInfo(mId);
			
			String json = JSONUtils.toJson(apply,false, DateUtils.DATE_FORMAT, false);
			WebUtils.webSendJSON(json);
		}catch(Exception e ){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	
/*-----------------------------------------------------	渲染信息  --------------------------------------------------------------------------------------*/	
	/**
	 * 合同单位
	 * @createDate： 2016年1月11日 下午8:05:31 
	 * @modifier lyy
	 * @modifyDate：2016年1月11日 下午8:05:31  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryBussCon")
	public void queryBussCon(){
		try{
			List<BusinessContractunit> contractunitList = drugApplyService.likeContractunit();
			Map<String,String> contMap = new HashMap<String, String>();
			for (BusinessContractunit businessContractunit : contractunitList) {
				contMap.put(businessContractunit.getEncode(), businessContractunit.getName());
			}
			String json = JSONUtils.toJson(contMap);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	
	/***
	 * 病床渲染
	 * @Title: bedMap 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "queryBedName")
	public void queryBedName(){
		try{
			InpatientBedinfoNow bedinfo = drugApplyService.getBedinfo(bedinfoId);
			String bedId =bedinfo.getBedId();
			if(bedId!=null&&!"".equals(bedId)){
				BusinessHospitalbed bed = drugApplyService.getBed(bedId);
				String bedwardName = bed.getBusinessBedward().getBedwardName();
				String bedName = bed.getBedName();
				WebUtils.webSendString(bedwardName+"-"+bedName+"床");
			}else{
				WebUtils.webSendString("null");
			}
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
		
	}
	
	/***
	 * 渲染包装单位
	 * @Title: listPackUnit 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @return void
	 * @version 1.0
	 */
	@Action(value="listPackUnit")
	public void listPackUnit(){
		try{
			//药品的包装单位
			List<BusinessDictionary> codeDrugpackagingunitList = innerCodeService.getDictionary("packunit");
			//非药品的包装单位
			List<BusinessDictionary> nonmedicineencodingList = innerCodeService.getDictionary("nonmedicineencoding");
			Map<String,String> unitMap = new HashMap<String, String>();
			for(BusinessDictionary unit : codeDrugpackagingunitList){
				unitMap.put(unit.getEncode(), unit.getName());
			}
			for(BusinessDictionary packNot : nonmedicineencodingList){
				unitMap.put(packNot.getEncode(), packNot.getName());
			}
			String joString = JSONUtils.toJson(unitMap);
			WebUtils.webSendJSON(joString);
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	/***
	 * 渲染最小费用代码
	 * @Title: miniCostMap 
	 * @author  WFJ
	 * @createDate ：2016年5月12日
	 * @return void
	 * @version 1.0
	 */
	@Action(value="miniCostMap")
	public void miniCostMap(){
		try{
			List<BusinessDictionary> list = innerCodeService.getDictionary("drugMinimumcost");
			Map<String,String> unitMap = new HashMap<String, String>();
			for(BusinessDictionary model : list ){
				unitMap.put(model.getEncode(), model.getName());
			}
			String joString = JSONUtils.toJson(unitMap);
			WebUtils.webSendJSON(joString);
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	
	
	
/*-----------------------------------------------------		以下是未知领域  		--------------------------------------------------------------------------------------*/	
	
	/**
	 * 住院科室下拉框
	 * @author  lyy
	 * @createDate： 2016年1月12日 上午10:12:25 
	 * @modifier lyy
	 * @modifyDate：2016年1月12日 上午10:12:25  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryDept", results = { @Result(name = "json", type = "json") })
	public void queryDept() throws Exception{
		try {
			List<SysDepartment> deptList = drugApplyService.likeDept();
			String json=JSONUtils.toJson(deptList,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json); 
		} catch (Exception e) {
			logger.error("ZYSF_TFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_TFSQ", "住院收费_退费申请", "2", "0"), e);
		}
	}
	
	@Action(value = "editMediceList", results = { @Result(name = "update", location = "/WEB-INF/pages/drug/apply/applyList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editMediceList() throws Exception {
		String amount=ServletActionContext.getRequest().getParameter("amount");
		String ids=ServletActionContext.getRequest().getParameter("id");
		try{
			String result = drugApplyService.editUpdate(amount, ids,cancelitemJson);
			if("ok".equals(result)){
				WebUtils.webSendString("success");
			}else{
				WebUtils.webSendString("error");
			}
		}catch(Exception e){
			logger.error("ZYSF_TFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_TFSQ", "住院收费_退费申请", "2", "0"), e);
			WebUtils.webSendString("error");
		}
		return "update";
	}
	
	@Action(value = "editDirectList", results = { @Result(name = "update", location = "/WEB-INF/pages/drug/apply/applyList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editDirectList() throws Exception {
		String amount=ServletActionContext.getRequest().getParameter("amount");
		String ids=ServletActionContext.getRequest().getParameter("id");
		try{
			String result = drugApplyService.directUpdate(amount, ids, cancelitemJson, money);
			if("ok".equals(result)){
				WebUtils.webSendString("success");
			}else{
				WebUtils.webSendString("error");
			}
		}catch(Exception e){
			logger.error("ZYSF_ZJTF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_ZJTF", "住院收费_直接退费", "2", "0"), e);
			WebUtils.webSendString("error");
		}
		return "update";
	}
	
	
	/**
	 * 根据病历号查询患者信息
	 * @author  lyy
	 * @createDate： 2016年1月28日 下午6:44:53 
	 * @modifier lyy
	 * @modifyDate：2016年1月28日 下午6:44:53  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getInfoList", results = { @Result(name = "json", type = "json") })
	public void getInfoList(){
		try {
			String inpatientNo = ServletActionContext.getRequest().getParameter("no");
			InpatientInfoNow info=new InpatientInfoNow();
			if(StringUtils.isNotBlank(inpatientNo)){
				info.setInpatientNo(inpatientNo);
			}
			List<InpatientInfoNow> apply=drugApplyService.getInfoList(info);
			String json = JSONUtils.toJson(apply,DateUtils.DATETIME_FORMAT);
		
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYSF_TFSQ", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_TFSQ", "住院收费_退费申请", "2", "0"), ex);
			
		}
	}
	/**
	 * 渲染员工
	 * @author  lyy
	 * @createDate： 2016年12月3日 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getEmpList", results = { @Result(name = "json", type = "json") })
	public void getEmpList(){
		try{
			Map<String, String> map = employeeInInterService.queryEmpCodeAndNameMap();
			String joString = JSONUtils.toJson(map);
			WebUtils.webSendJSON(joString);
		}catch(Exception e){
			logger.error("ZYSF_QXTFSQ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_QXTFSQ", "住院收费_取消退费申请", "2", "0"), e);
		}
	}
	/**
	 * 根据病历号查询患者信息
	 * @author  lyy
	 * @createDate： 2016年1月28日 下午6:44:53 
	 * @modifier lyy
	 * @modifyDate：2016年1月28日 下午6:44:53  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getBedName", results = { @Result(name = "json", type = "json") })
	public void getBedName(){
		try {
			String inpatientNo = ServletActionContext.getRequest().getParameter("no");
			BusinessHospitalbed apply=drugApplyService.getBedName(inpatientNo);
			String json = JSONUtils.toJson(apply,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYSF_TFSQ", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_TFSQ", "住院收费_退费申请", "2", "0"), ex);
		}
	}

	
	
	/*------------------------------------------get    set    方法---------------------------------------------------------------------------*/

	public String getMoney() {
		return money;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCancelitemJson() {
		return cancelitemJson;
	}
	public void setCancelitemJson(String cancelitemJson) {
		this.cancelitemJson = cancelitemJson;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDrugJson() {
		return drugJson;
	}
	public void setDrugJson(String drugJson) {
		this.drugJson = drugJson;
	}
	public String getNotDrugJson() {
		return notDrugJson;
	}
	public void setNotDrugJson(String notDrugJson) {
		this.notDrugJson = notDrugJson;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	public String getBedinfoId() {
		return bedinfoId;
	}
	public void setBedinfoId(String bedinfoId) {
		this.bedinfoId = bedinfoId;
	}
}
