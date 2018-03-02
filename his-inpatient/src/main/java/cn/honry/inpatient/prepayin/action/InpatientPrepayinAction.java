package cn.honry.inpatient.prepayin.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
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

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.BusinessIcd;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.bean.model.VIdcardPatient;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.prepayin.service.InpatientPrepayinService;
import cn.honry.inpatient.prepayin.vo.PrepayinVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/prepayin")
@SuppressWarnings({"all"})
public class InpatientPrepayinAction extends ActionSupport implements ModelDriven<InpatientPrepayin>{
	
	private Logger logger=Logger.getLogger(InpatientPrepayinAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	
		this.hiasExceptionService = hiasExceptionService;

	}

	/**
	 * shiro session中当前user对象对应的属性名
	 */
	public static final String SESSIONUSER="sessionUser";
	
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
	InpatientPrepayin inpatientPrepayin = new InpatientPrepayin();
	@Override
	public InpatientPrepayin getModel() {
		return inpatientPrepayin;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private List<InpatientPrepayin> inpatientPrepayinList;
	
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	
	/**
	 * 门诊号
	 */
	private String no;
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	} 
	
	/**
	 * 病历号
	 */
	private String medId;
	/**
	 * 护士站代码
	 */
	private String nurId;
	
	/**
	 * 病床号
	 */
	private String bedId;
	/**
	 * 门诊号
	 */
	private String idcardNo;
	
	
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	private InpatientPrepayinService inpatientPrepayinService;
	@Autowired
	public void setInpatientPrepayinService(InpatientPrepayinService inpatientPrepayinService) {
		this.inpatientPrepayinService = inpatientPrepayinService;
	}
	
	
	public String getMedId() {
		return medId;
	}
	public void setMedId(String medId) {
		this.medId = medId;
	}
	
	public String getNurId() {
		return nurId;
	}
	public void setNurId(String nurId) {
		this.nurId = nurId;
	}
	
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	
	/**
	 * @Description:获取list页面
	 * @Author：  ldl
	 * @CreateDate： 2015-8-11
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYYY:function:view"}) 
	@Action(value="inpatientPrepayinList",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/prepayin/prepayinList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientPrepayinList()throws Exception{
		User user =(User)SecurityUtils.getSubject().getSession().getAttribute(SESSIONUSER);
		String userName = user.getName();
		request.setAttribute("userName", userName);
		return "list";
	}
	
	/**
	 * 已预约管理列表
	 * @author  tangfeishuai
	 * @date 2016-6-21 14:00
	 * @version 1.0
	 */
	@Action(value = "queryPrepayin")
	public void queryPrepayin() {
		
		try {
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId=ServletActionContext.getRequest().getParameter("medicalrecordId");
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}
			
			//通过接口查询就诊卡号对应的病历号
			if(StringUtils.isNotBlank(medicalrecordId)){
				inpatientPrepayin.setMedicalrecordId(medicalrecordId);
			}
			int total = inpatientPrepayinService.getTotal(inpatientPrepayin);
			inpatientPrepayinList = inpatientPrepayinService.prepayinList(request.getParameter("page"),request.getParameter("rows"),inpatientPrepayin);
			Map<String,Object> retMap = new HashMap<String,Object>();
			retMap.put("total", total);
			retMap.put("rows", inpatientPrepayinList);
			String json = JSONUtils.toJson(retMap);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
		
	}
	
	/**
	 * 取消预约
	 * @author  ldl
	 * @date 2015-8-11 14:00
	 * @version 1.0
	 */
	@RequiresPermissions(value={"ZYYY:function:delete"}) 
	@Action(value = "delPrepayin", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/prepayin/prepayinList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delPrepayin() throws Exception {
		String ids = ServletActionContext.getRequest().getParameter("ids");
		Map<String,String> resMap=new HashMap<String,String>();
		try{
			inpatientPrepayinService.del(ids);
			resMap.put("resCode", "success");
			resMap.put("resMsg", "删除成功!");
		}catch(Exception e){
			resMap.put("resCode", "error");
			resMap.put("resMsg", "删除失败!");
		}
		String json=JSONUtils.toJson(resMap);
		WebUtils.webSendJSON(json);
		return "list";
	}
	
	/**  
	 *  
	 * @Description：查询并赋值
	 * @Author：ldl
	 * @CreateDate：2015-8-11 下午16:29:35 
	 * @param：medicalrecordId（病例号） 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYYY:function:query"}) 
	@Action(value = "queryPrepayinInpatient", results = { @Result(name = "json", type = "json") })
	public void queryPrepayinInpatient() {
		try {
			String medicalrecordId = ServletActionContext.getRequest().getParameter("medicalrecordId");
			InpatientInfoNow inpatientInfoList = inpatientPrepayinService.queryPrepayinInpatient(medicalrecordId);
			VIdcardPatient vIdcardPatientList = null;
			if(inpatientInfoList.getId()==null){
				vIdcardPatientList = new VIdcardPatient();
				vIdcardPatientList = inpatientPrepayinService.queryVIdcardPatient(medicalrecordId);
			}
			String json = "";
			if(inpatientInfoList.getId()!=null){
				json = JSONUtils.toJson(inpatientInfoList);
			}else if(vIdcardPatientList.getIdcardId()!=null){
				json = JSONUtils.toJson(vIdcardPatientList);
			}else{
				json = "";
			}
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
		
	}
	
	/**  
	 *  
	 * @Description：  保存
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"ZYYY:function:add"}) 
	@Action(value = "savePrepayin", results = { @Result(name = "json", type = "json")})
	public void savePrepayin() throws Exception {
			String reselt="";
			try {
				inpatientPrepayinService.savePrepayin(inpatientPrepayin);
				reselt="ok";
				WebUtils.webSendString(reselt);
			}
			catch (Exception e) {
				reselt="error";
				WebUtils.webSendJSON("error");
				logger.error("ZYGL_ZYYY", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
			}
	}
	
	/**  
	 *  
	 * @Description：  下拉诊断
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryPrepayinIcd", results = { @Result(name = "json", type = "json") })
	public void queryPrepayinIcd() {
		try {
			List<BusinessIcd> icdList = inpatientPrepayinService.queryPrepayinIcd();
			String json = JSONUtils.toJson(icdList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	
	/**  
	 *  
	 * @Description：  下拉病床
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryPrepayinBed", results = { @Result(name = "json", type = "json") })
	public void queryPrepayinBed() {
		try {
			List<BusinessHospitalbed> hospitalbedList = inpatientPrepayinService.queryPrepayinBed();
			String json = JSONUtils.toJson(hospitalbedList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	
	/**  
	 *  
	 * @Description：  下拉医生
	 * @Author：liudelin
	 * @ModifyDate：2015-8-12 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryPrepayinPredoct", results = { @Result(name = "json", type = "json") })
	public void queryPrepayinPredoct() {
		try {
			List<SysEmployee> employeeList = inpatientPrepayinService.queryPrepayinPredoct();
			String json = JSONUtils.toJson(employeeList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	
	/**  
	 *  
	 * @Description：  查询病人及登记信息
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryPrepayinVo")
	public void queryPrepayinVo() {
		try {
			InpatientInfoNow inpatientInfoList = inpatientPrepayinService.queryInpatientInfo(medId);
			InpatientProof inpatientPrroof = inpatientPrepayinService.queryInpatientProof(medId,no);
			InpatientPrepayin inpatientPrepayin = inpatientPrepayinService.inpPrepayin(medId);
			PrepayinVo p=new PrepayinVo();
			if(StringUtils.isNotBlank(inpatientInfoList.getId())){
				p.setMedicalrecordId("1");
			}else if(StringUtils.isBlank(inpatientPrroof.getId())){
				p.setMedicalrecordId("2");
			}else if(StringUtils.isNotBlank(inpatientPrepayin.getId())){
				p.setMedicalrecordId("3");
			}else{
				p = inpatientPrepayinService.queryPrepayVo(medId,no);
			}
			String json=JSONUtils.toJson(p);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	
	@Action(value = "queryProofData")
	public void queryProofData() {
		try {
			InpatientProof inpatientPrroof = inpatientPrepayinService.getProof(medId,idcardNo);
			String json=JSONUtils.toJson(inpatientPrroof);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	
	/**  
	 *  
	 * @Description： 模糊查询病历号
	 * @ModifyDate：2016-8-24 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value="queryPrepayinVoList")
	public void queryPrepayinVoList(){
		try {
			List<PrepayinVo> prepayinVoList = inpatientPrepayinService.queryPrepayVoList(medId,no);
			String json=JSONUtils.toJson(prepayinVoList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	@Action(value="queryProofList")
	public void queryProofList(){
		if(StringUtils.isNotBlank(medId)){
			medId=medId.trim();
		}
		//通过接口查询就诊卡号对应的病历号
		List<InpatientProof> queryProofList = null;
		try {
			queryProofList = inpatientPrepayinService.queryProofList(medId);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
		String json=JSONUtils.toJson(queryProofList);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  根据病区id查询床号
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryBedByNurse")
	public void queryBedByNurse() {
		try {
			List<BusinessHospitalbed> list = inpatientPrepayinService.queryBedByNurse(nurId);
			String json=JSONUtils.toExposeJson(list, false, null, "id","bedName");
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	
	/**  
	 *  
	 * @Description：  合同单位渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryContractunit")
	public void queryContractunit() {
		try {
			Map<String, String> map=  inpatientPrepayinService.queryContractunit();
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}	
	
	/**  
	 *  
	 * @Description：  就诊卡号渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryIdCard")
	public void queryIdCard() {
		try {
			Map<String, String> map=  inpatientPrepayinService.queryIdCard();
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}	
	/**  
	 *  
	 * @Description： 城市渲染
	 * @Author：tangfeishuai
	 * @ModifyDate：2016-6-20 上午09：54 
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Action(value = "queryCity")
	public void queryCity() {
		try {
			Map<String, String> map=  inpatientPrepayinService.queryDistinct();
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}
	
	/**  
	 *  
	 * @Description：根据病床id查询维护该病床的医生
	 * @Author：tangfeishuai
	 * @CreateDate：2016-6-24下午16:29:35 
	 * @param：
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryInpatientBedinfo")
	public void queryInpatientBedinfo() {
		try {
			List<SysEmployee> map=  inpatientPrepayinService.queryInpatientBedinfo(bedId);
			List<SysEmployee> employeeList = inpatientPrepayinService.queryPrepayinPredoct();
			String json="";
			if(map.size()>0&&map!=null){
				json=JSONUtils.toJson(map);
			}else{
				json=JSONUtils.toJson(employeeList);
				
			}
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}	
	@Action(value = "queryNurInfo")
	public void queryNurInfo() {
		try {
			String dept  = ServletActionContext.getRequest().getParameter("reportDept");
			List<DepartmentContact> deptCon = inpatientPrepayinService.queryNurInfo(dept);
			String json="";
			if(deptCon.size()>0&&deptCon!=null){
				json=JSONUtils.toJson(deptCon);
			}else{
				json=JSONUtils.toJson(deptCon);
				
			}
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}	
	
	/**
	 * 
	 * 
	 * <p>查询病房信息 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 上午11:25:43 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 上午11:25:43 
	 * @ModifyRmk:  
	 * @version: V1.0:
	 *
	 */
	@Action(value = "queryBedWardInfo")
	public void queryBedWardInfo() {
		try {
			String bedId  = ServletActionContext.getRequest().getParameter("bedId");
			List<BusinessBedward> deptCon = inpatientPrepayinService.queryBedWardInfo(bedId);
			String json=""; 
			if(deptCon.size()>0&&deptCon!=null){
				json=JSONUtils.toJson(deptCon);
			}else{
				json=JSONUtils.toJson(deptCon);
				
			}
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
	}	
	
}
