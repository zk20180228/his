package cn.honry.inpatient.amobileApply.action;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;

import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inpatient.apply.Service.ConfirmService;
import cn.honry.inpatient.apply.Service.DrugApplyService;
import cn.honry.inpatient.apply.vo.ApplyVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/appReturnPremium")
public class ReturnPremium extends ActionSupport{

	private Logger logger=Logger.getLogger(ReturnPremium.class);
	
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/***
	 * 退费申请  退费取消  直接退费
	 */
	@Autowired
	@Qualifier(value="drugApplyService")
	private DrugApplyService drugApplyService;
	
	/***
	 * 确认退费
	 */
	@Autowired
	@Qualifier(value="confirmService")
	private ConfirmService confirmService;
	
	//病历号
	private String medicalrecordId;
	/*申请区药品json*/
	private String drugJson;
	/*申请区非药品json*/
	private String notDrugJson;
	/*id集合*/
	private String ids;
	/*登录科室*/
	private String deptCode;
	/*登录人员*/
	private String empJobNo;
	
	/**
	 * 退费申请保存
	 * @author  CXW
	 * @createDate： 2017年12月7日 上午10:47:51 
	 * @modifier wfj
	 * @modifyDate：2017年12月7日 上午10:47:51 
	 * @modifyRmk：  
	 * @version 2.0
	 */
	@Action(value = "saveConsultation")
	public void saveConsultation(){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = drugApplyService.saveAdd(medicalrecordId,drugJson,notDrugJson,deptCode,empJobNo);
		} catch (Exception e) {
			map.put("resCode","-1");
			map.put("resMsg","未知错误，请联系系统管理员！");
			logger.error("ZYSF_TFSQ", e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
	}
	
	
	/**
	 * 取消退费申请
	 * @author  CXW
	 * @createDate：2017年12月7日 上午10:47:51  
	 * @modifier lyy
	 * @modifyDate：2017年12月7日 上午10:47:51 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "delDrugOrNotDrugApply")
	public void delDrugOrNotDrugApply(){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			if(ids!=null){
				//String ids=java.net.URLDecoder.decode(ids, "UTF-8");
				String[] idsss=ids.split(",");
				map = drugApplyService.delDrugOrNotDrugApply(idsss,deptCode,empJobNo);
			}else{
				map.put("resCode","success");
				map.put("resMsg","请选中要退费栏目");
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYSF_QXTFSQ", e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 确认退费操作
	 * @Title: confirm 
	 * @author  CXW
	 * @createDate ：2017年12月7日 上午10:47:51 
	 * @return void
	 * @version 1.0
	 */
	@Action(value = "applyConfirm")
	public void applyConfirm(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resCode", "success");
		try {
			//String idss=java.net.URLDecoder.decode(ids, "UTF-8");
			String[] idsss=ids.split(",");
			confirmService.applyConfirm(idsss,null,deptCode,empJobNo);
		} catch (Exception e) {
			map.put("resCode", "error");
			logger.error("ZYSF_QRTF", e);
			e.printStackTrace();
		}
		String joString = JSONUtils.toJson(map);
		WebUtils.webSendJSON(joString);
	}
	
	/***
	 * 直接退费
	 * @Title: directSave 
	 * @author  CXW
	 * @createDate ：2017年12月7日 上午10:47:51 
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
				map = drugApplyService.directSave(medicalrecordId, drugList, notDrugList,deptCode,empJobNo);
			}
		} catch (Exception e) {
			map.put("resCode",-1);
			map.put("resMsg","未知错误，请联系系统管理员！");
			e.printStackTrace();
			logger.error("ZYSF_ZJTF", e);
		}
		String json = JSONUtils.toJson(map);
		WebUtils.webSendJSON(json);
		

	}
	
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
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
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getEmpJobNo() {
		return empJobNo;
	}
	public void setEmpJobNo(String empJobNo) {
		this.empJobNo = empJobNo;
	}
	
	
	
	
}
