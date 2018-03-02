package cn.honry.outpatient.medicalRecordModel.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.BusinessMedicalRecord;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.outpatient.medicalRecordModel.service.MedicalRecordModelService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**  
 *  
 * @className：MedicalRecordAction 
 * @Description：  电子病历模版表
 * @Author：ldl
 * @CreateDate：2015-7-13   
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/business/medicalrecord")
@Namespace(value = "/outpatient/medicalRecordModel")
public class MedicalRecordModelAction extends ActionSupport implements ModelDriven<BusinessMedicalRecord>{

	
	// 记录异常日志
	private Logger logger = Logger.getLogger(MedicalRecordModelAction.class);
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

	@Override
	public BusinessMedicalRecord getModel() {
		return medicalRecord;
	}
	@Autowired
	private MedicalRecordModelService medicalRecordModelService;
	
	
	
	
	public void setMedicalRecordModelService(MedicalRecordModelService medicalRecordModelService) {
		this.medicalRecordModelService = medicalRecordModelService;
	}
	BusinessMedicalRecord medicalRecord = new BusinessMedicalRecord();

	public BusinessMedicalRecord getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(BusinessMedicalRecord medicalRecord) {
		this.medicalRecord = medicalRecord;
	}

	/**  
	 *  
	 * @Description：  电子病历模版树(个别)
	 * @Author：liudelin
	 * @ModifyDate：2015-7-15 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "medicalRecordOtherTree", results = { @Result(name = "json", type = "json") })
	public void medicalRecordOtherTree() {
		try {
			String type = ServletActionContext.getRequest().getParameter("type");
		    List<TreeJson> treeJsonList = medicalRecordModelService.medicalRecordOtherTree(type);
		    List<TreeJson> treeDepar =  TreeJson.formatTree(treeJsonList);
			String json = JSONUtils.toJson(treeDepar);
		
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
		    e.printStackTrace();
			logger.error("DZBLMBB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("DZBLMBB", "电子病历模板表", "2", "0"), e);
		}
		
	}
	/**  
	 *  
	 * @Description：  电子病历模板列表
	 * @Author：liudelin  
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk：                                                                                                                                                                                                                                                                                                                                               
	 * @version 1.0
	 *
	 */
	@Action(value = "medicalRecordList", results = { @Result(name = "json", type = "json") })
	public void medicalRecordList() {
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			String recordType = ServletActionContext.getRequest().getParameter("recordType");
			List<BusinessMedicalRecord> medicalRecordList = medicalRecordModelService.queryMedicalRecordList(id,recordType);
			String json = JSONUtils.toJson(medicalRecordList);
		
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
			 e.printStackTrace();
			logger.error("DZBLMBB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("DZBLMBB", "电子病历模板表", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：  电子病历模版树()
	 * @Author：liudelin
	 * @ModifyDate：2015-7-14 上午10：13
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "medicalRecordTree", results = { @Result(name = "json", type = "json") })
	public void medicalRecordTree() {
		try {
			List<TreeJson> treeJsonList = medicalRecordModelService.medicalRecordTree();
		    List<TreeJson> treeDepar =  TreeJson.formatTree(treeJsonList);
			String json = JSONUtils.toJson(treeDepar);
		
			WebUtils.webSendString(json);
		}
		catch (Exception e) {
			 e.printStackTrace();
			logger.error("DZBLMBB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("DZBLMBB", "电子病历模板表", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  保存电子病历模版表
	 * @Author：liudelin
	 * @ModifyDate：2015-7-13 下午05：43
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "saveMedicalRecord", results = { @Result(name = "json", type = "json")})
	public void saveMedicalRecord()  {
		Map<String,String> retMap = new HashMap<String, String>();
		try {
			medicalRecordModelService.saveOrUpdateRecord(medicalRecord);
			retMap.put("resMsg", "success");
			retMap.put("resCode", "病历模板保存成功！");
		}catch (Exception e) {
			retMap.put("resMsg", "error");
			retMap.put("resCode", "病历模板保存失败！");
			e.printStackTrace();
			logger.error("DZBLMBB", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("DZBLMBB", "电子病历模板表", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
}
