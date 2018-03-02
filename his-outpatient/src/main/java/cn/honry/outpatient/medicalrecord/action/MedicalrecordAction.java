package cn.honry.outpatient.medicalrecord.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.honry.base.bean.model.OutpatientMedicalrecord;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.outpatient.medicalrecord.service.MedicalrecordService;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;



/**  
 *  
 * @className：MedicalrecordAction 
 * @Description：  门诊病历Action 
 * @Author：aizhonghua
 * @CreateDate：2015-7-6 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-7-6 下午04:41:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/medicalrecord")
public class MedicalrecordAction extends ActionSupport implements ModelDriven<OutpatientMedicalrecord> {
	
	private static final long serialVersionUID = -8294964238407881285L;

	// 记录异常日志
	private Logger logger = Logger.getLogger(MedicalrecordAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	OutpatientMedicalrecord medicalrecord = new OutpatientMedicalrecord();
	@Override
	public OutpatientMedicalrecord getModel() {
		return medicalrecord;
	}
	@Autowired
	@Qualifier(value = "medicalrecordService")
	private MedicalrecordService medicalrecordService;
	public void setMedicalrecordService(MedicalrecordService medicalrecordService) {
		this.medicalrecordService = medicalrecordService;
	}
	
	/**  
	 *  
	 * @Description：门诊病历
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "medicalrecordSave")
	public void medicalrecordSave(){
		Map<String,String> retMap = new HashMap<String, String>();
		try {
			medicalrecordService.saveOrUpdatemedicalrecord(medicalrecord);
			retMap.put("resMsg", "success");
			retMap.put("resCode", "病历保存成功！");
		} catch (Exception e) {
			retMap.put("resMsg", "error");
			retMap.put("resCode", "病历保存失败！");
		    e.printStackTrace();
			logger.error("MZSF_MZSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MZSF", "门诊收费_门诊收费", "2", "0"), e);
		}
		String json = JSONUtils.toJson(retMap);
		WebUtils.webSendJSON(json);
	}
	/****
	 * 检验单
	 * @Description:
	 * @author  huangbiao
	 * @date 创建时间：2016年3月16日
	 * @Modifier：aizhonghua
	 * @version 1.0
	 */
	@Action(value = "getDeptId")
	public void getDeptId(){
		try {
			Map<String,Object> retMap = new HashMap<String, Object>();
			String patientNo = ServletActionContext.getRequest().getParameter("patientNo");
			String clinicCode = ServletActionContext.getRequest().getParameter("clinicCode");
			if(StringUtils.isNotBlank(patientNo)&&StringUtils.isNotBlank(clinicCode)){
				boolean isHave = medicalrecordService.findMedicalByNoAndCode(patientNo,clinicCode);
				if(isHave){
					List<String> list = medicalrecordService.getDeptId(clinicCode);
					if(list!=null&&list.size()>0){
						retMap.put("resMsg", "success");
						retMap.put("resCode", list);
					}else{
						retMap.put("resMsg", "error");
						retMap.put("resCode", "无检验单信息,无法打印检验单！");
					}
				}else{
					retMap.put("resMsg", "error");
					retMap.put("resCode", "该患者没有病历信息，请添加病历后打印检验单！");
				}
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode", "患者信息有误，请刷新后重试！");
			}
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZSF_MZSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MZSF", "门诊收费_门诊收费", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  分类组套树
	 * @Author：tangfeishuai
	 * @CreateDate：2016-3-23上午15:42:41  
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-3-23 上午15:42:41  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "getExecDpcdByclinicCode")
	public void getExecDpcdByclinicCode() {
		try {
			Map<String,Object> retMap = new HashMap<String, Object>();
			String patientNo = ServletActionContext.getRequest().getParameter("patientNo");
			String clinicCode = ServletActionContext.getRequest().getParameter("clinicCode");
			if(StringUtils.isNotBlank(patientNo)&&StringUtils.isNotBlank(clinicCode)){
				boolean isHave = medicalrecordService.findMedicalByNoAndCode(patientNo,clinicCode);
				if(isHave){
				List<String> sList = medicalrecordService.findExecDpcdByCodeAndPatino(clinicCode, patientNo);
					if(sList!=null&&sList.size()>0){
						retMap.put("resMsg", "success");
						retMap.put("resCode", sList);
					}else{
						retMap.put("resMsg", "error");
						retMap.put("resCode", "无检查单信息,无法打印检查单！");
					}
				}else{
					retMap.put("resMsg", "error");
					retMap.put("resCode", "该患者没有病历信息，请添加病历后打印检查单！");
				}
			}else{
				retMap.put("resMsg", "error");
				retMap.put("resCode", "患者信息有误，请刷新后重试！");
			}
			
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZSF_MZSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MZSF", "门诊收费_门诊收费", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：wanxing
	 * @CreateDate：2016-03-09 下午18:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "findRecipeNo")
	public void findRecipeNo(){
		try {
			List<String>  list= medicalrecordService.findRecipeNo(medicalrecord.getClinicCode(), medicalrecord.getPatientNo());
			String json=JSONUtils.toJson(list);
			WebUtils.webSendString(json);  
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("MZSF_MZSF", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("MZSF_MZSF", "门诊收费_门诊收费", "2", "0"), e);
		}
	}
	
}
