package cn.honry.outpatient.transfuse.action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.OutpatientMixLiquid;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.outpatient.transfuse.service.TransfuseService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**  
 * @className：TransfuseAction
 * @Description：  门诊配液Action 
 * @Author：tuchuanjiang
 * @CreateDate：2016-06-20
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/transfuse")
public class TransfuseAction extends ActionSupport{
	// 记录异常日志
	private Logger logger = Logger.getLogger(TransfuseAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier(value = "transfuseService")
	private TransfuseService transfuseService;
	public void setTransfuseService(TransfuseService transfuseService) {
		this.transfuseService = transfuseService;
	}
	/**
	 * 注入patinentInnerService公共接口
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	/**
	 * 注入inpatientInfoInInterService公共接口
	 */
	private InpatientInfoInInterService inpatientInfoInInterService;
	
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	//注入code编码service
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	//员工公共接口
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	
	/**
	 * 门诊号
	 */
	private String clinicCode;
//	private String rowdata;
	
//	public String getRowdata() {
//		return rowdata;
//	}
//	public void setRowdata(String rowdata) {
//		this.rowdata = rowdata;
//	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	@RequiresPermissions(value={"MZPYGL:function:view"})
	@Action(value = "listtransfuse", results = { @Result(name = "listtransfuse", location = "/WEB-INF/pages/outpatient/transfuse/transfuseList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listtransfuse() {
		return "listtransfuse";
	}
	/**
	 * 通过病历号查询患者的处方信息
	 * @author tuchuanjiang
	 * @CreateDate： 2016-06-21
	 * @version 1.0
	 */
	@Action(value = "queryPatientYZInfo")
	public void queryPatientYZInfo() {
		
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			//通过接口查询就诊卡号对应的病历号
			String medicalrecordNo = patinentInnerService.getMedicalrecordId(clinicCode);
			if(StringUtils.isNotBlank(medicalrecordNo)){
				List<OutpatientRecipedetailNow> oprl =transfuseService.queryPatientYZInfo(medicalrecordNo);
				List<Patient> list = transfuseService.queryPatientInfo(clinicCode);
				if(oprl!=null&&oprl.size()>0 && list.size()>0){
					map.put("value1", oprl);
					map.put("value2", list.get(0));
				}else{
					map.put("key", "none");
					map.put("value", "您查询的患者没有待院注信息！");
				}
			}else{
				map.put("key", "none");
				map.put("value", "您输入的就诊卡有误,请核对后重新输入！");
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
		      	e.printStackTrace();
				logger.error("MZHSZ_MZPY", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  根据门诊号查询待院注信息
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryRecipedetail")
	public void queryRecipedetail() {
		//通过接口查询就诊卡号对应的病历号
		String medicalrecordNo = patinentInnerService.getMedicalrecordId(clinicCode);
		List<OutpatientMixLiquid> oprl =transfuseService.queryRecipedetail(medicalrecordNo);
		String json=JSONUtils.toJson(oprl);
		WebUtils.webSendJSON(json);
	}
	/***
	 *  根据门诊号查询已院注信息
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryMixliquid")
	public void queryMixliquid() {
		try {
			//通过接口查询就诊卡号对应的病历号
			String medicalrecordId = patinentInnerService.getMedicalrecordId(clinicCode);
			//根据病历号获取已院注信息
			List<OutpatientMixLiquid> oprl =transfuseService.queryMixliquid(medicalrecordId);
			String json=JSONUtils.toJson(oprl);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  查询员工map
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryDoctrans")
	public void queryDoctrans() {
		try {
			Map<String, String> map = employeeInInterService.queryEmpCodeAndNameMap();
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  查询科室map
	 *  (此方法已不再使用,查询科室map改为从baseInfo/department/中查询;2017-02-16 lys)
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryDeptTrans")
	public void queryDeptTrans() {
		try {
			List<SysDepartment> deptl =inpatientInfoInInterService.queryDeptMapPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(SysDepartment dept:deptl){
				map.put(dept.getDeptCode(), dept.getDeptName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  查询频次map
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryFrequencyTrans")
	public void queryFrequencyTrans() {
		try {
			List<BusinessFrequency> freql =inpatientInfoInInterService.queryFrequencyListPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessFrequency freq:freql){
				map.put(freq.getEncode(), freq.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  查询药品性质map
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryDrugpropertiesTrans")
	public void queryDrugpropertiesTrans() {
		try {
			List<BusinessDictionary> cdpl =innerCodeService.getDictionary("drugProperties");
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary cdp:cdpl){
				map.put(cdp.getEncode(), cdp.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  保存院注信息
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "saveform")
	public void saveform() {
		try {
			String rowdata=ServletActionContext.getRequest().getParameter("rowdata");
			String clinicCode=ServletActionContext.getRequest().getParameter("clinicCode");
			String result=transfuseService.saveform(rowdata,clinicCode);
			WebUtils.webSendString(result);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	} 
	/***
	 *  查询包装单位map
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryOnceUnti")
	public void queryOnceUnti() {
		try {
			String type ="doseUnit";
			String encode=null;
			List<BusinessDictionary> cdl =inpatientInfoInInterService.queryDictionaryListForcomboboxPublic(type, encode);
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary cd:cdl){
				map.put(cd.getEncode(), cd.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  查询usermap
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryUsertrans")
	public void queryUsertrans() {
		try {
			List<User> cdl =inpatientInfoInInterService.queryUserListPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(User cd:cdl){
				map.put(cd.getAccount(), cd.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	/***
	 *  查询员工list
	 * @author  tuchuanjiang
	 * @createDate ：2016-6-22
	 * @version 1.0
	 */
	@Action(value = "queryEmptrans")
	public void queryEmptrans() {
		try {
			List<SysEmployee> empl =employeeInInterService.getEmpByType(null);
			String json=JSONUtils.toExposeJson(empl, false, null, "jobNo","name");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
	      	e.printStackTrace();
			logger.error("MZHSZ_MZPY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZHSZ_MZPY", "门诊护士站_门诊配液", "2", "0"), e);
		}
	}
	
}
