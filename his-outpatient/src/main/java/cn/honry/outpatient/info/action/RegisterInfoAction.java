package cn.honry.outpatient.info.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
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
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.outpatient.changeDeptLog.action.RegisterChangeDeptLogAction;
import cn.honry.outpatient.info.service.RegisterInfoService;
import cn.honry.outpatient.info.vo.EmpInfoVo;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.info.vo.InfoStatistics;
import cn.honry.outpatient.info.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @Description：  门诊挂号
 * @Author：liudelin
 * @CreateDate：2015-6-5 下午05:12:16  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/info")
public class RegisterInfoAction extends ActionSupport implements ModelDriven<RegisterInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 记录异常日志
	private Logger logger = Logger.getLogger(RegisterInfoAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Override
	public RegisterInfo getModel() {
		return registerInfo;
	}
	private RegisterInfo registerInfo =  new RegisterInfo();
	
	
	private RegisterInfoService registerInfoService;
	@Autowired
	@Qualifier(value = "registerInfoService")
	public void setRegisterInfoService(RegisterInfoService registerInfoService) {
		this.registerInfoService = registerInfoService;
	}
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	
	/**
	 * 患者账户密码
	 */
	private String passwords;
	
	/**
	 * 患者病历号
	 */
	private String blhcs;
	//easyui-combobox 
	private String q;
	
	
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
	 *  
	 * @Description：  挂号信息列表
	 * @Author：liudelin
	 * @CreateDate：2015-6-6 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-6 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZGH:function:view"}) 
	@Action(value = "listRegisterInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/register/info/registerInfoList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRegisterInfo() {
		HospitalParameter parameter = registerInfoService.changePay();
		HospitalParameter parameters = registerInfoService.invocePemen();
		ServletActionContext.getRequest().setAttribute("medicalRecordBookPay", parameter.getParameterValue());
		ServletActionContext.getRequest().setAttribute("infoExenp", parameters.getParameterValue());
		return "list";
	}

	
	/**  
	 *  
	 * @Description：  门诊卡id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-25 下午11:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 下午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZGH:function:query"}) 
	@Action(value = "queryidcardId", results = { @Result(name = "json", type = "json") })
	public void queryidcardId() {
		try {
			String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
			
			if(StringUtils.isNotBlank(idcardNo)){
				registerInfo.setIdcardId(idcardNo);
			}
			registerInfo = registerInfoService.gethz(idcardNo);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(registerInfo); 
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  单击树查询id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-25 下午11:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 下午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "queryId", results = { @Result(name = "json", type = "json") })
	public void queryId() {
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			if(StringUtils.isNotBlank(id)){
				registerInfo.setId(id);
			}
			registerInfo = registerInfoService.gethzid(id);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(registerInfo); 
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  退号信息列表
	 * @Author：wujiao
	 * @CreateDate：2015-26-6 下午05:12:16  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-26-6 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"TH:function:view"}) 
	@Action(value = "listBack", results = { @Result(name = "list", location = "/WEB-INF/pages/register/backNO/backNoList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listBack() {
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  退号信息列表查询
	 * @Author：ldl
	 * @CreateDate：2015-11-25
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "queryBackNo", results = { @Result(name = "json", type = "json") })
	public void queryBackNo() {
		try {
			String idcardId = ServletActionContext.getRequest().getParameter("idcardId");
			InfoPatient infoPatient = registerInfoService.queryRegisterInfo(idcardId);
			List<RegisterInfo> infoList = registerInfoService.queryBackNo(infoPatient.getIdCardNo());
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(infoList); 
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  退号
	 * @Author：ldl
	 * @CreateDate：2015-11-25 
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@RequiresPermissions(value={"TH:function:delete"}) 
	@Action(value = "updateInfo")
	public void updateInfo(){
		try{
			String infoId = ServletActionContext.getRequest().getParameter("infoId");
			String quitreason = ServletActionContext.getRequest().getParameter("quitreason");
			String payType = ServletActionContext.getRequest().getParameter("payType");//1是现金2是院内账户
			RegisterInfo registerInfo = registerInfoService.get(infoId);
			Map<String,String> map = registerInfoService.saveOrUpdateInfoBack(registerInfo,quitreason,payType);
			String mapJosn = JSONUtils.toJson(map);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  挂号类别（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-7-9  上午09:40:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "typeCombobox", results = { @Result(name = "json", type = "json") })
	public void typeCombobox() {
		try {
			List<BusinessDictionary> codeRegistertypeList = innerCodeService.getDictionary("registerType");
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(codeRegistertypeList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "deptCombobox", results = { @Result(name = "json", type = "json") })
	public void deptCombobox() {
		try {
			List<SysDepartment> departmentList = registerInfoService.deptCombobox();
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(departmentList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);		
		}
	}
	
	/**  
	 * @Description：  挂号级别（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "gradeCombobox", results = { @Result(name = "json", type = "json") })
	public void gradeCombobox() {
		try {
			List<RegisterGrade> gradeList = registerInfoService.gradeCombobox(q);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(gradeList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  挂号专家（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "empCombobox", results = { @Result(name = "json", type = "json") })
	public void empCombobox() {
		try {
			List<EmpInfoVo> employeeList = registerInfoService.empCombobox(registerInfo.getDept(),registerInfo.getGrade(),registerInfo.getMidday());
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(employeeList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：  合同单位
	 * @parm:
	 * @Author：liudelin
	 * @CreateDate：2015-6-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "businessContractunitCombobox", results = { @Result(name = "json", type = "json") })
	public void businessContractunitCombobox() {
		try {
			List<BusinessContractunit> businessContractunit = registerInfoService.queryBusinessContractunit();
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(businessContractunit);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  显示挂号费
	 * @parm:id（合同单位ID）
	 * @Author：liudelin
	 * @CreateDate：2015-6-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "feeCombobox", results = { @Result(name = "json", type = "json") })
	public void feeCombobox() {
		try {
			String id = ServletActionContext.getRequest().getParameter("unitId");
			String gradeId = ServletActionContext.getRequest().getParameter("gradeId");
			RegisterFee registerFee = registerInfoService.queryRegisterFee(id,gradeId);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(registerFee);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  值班列表
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "findScheduleList", results = { @Result(name = "json", type = "json") })
	public void findScheduleList() {
		try {
			String deptId = ServletActionContext.getRequest().getParameter("deptId");
			String empId = ServletActionContext.getRequest().getParameter("empId");
			String gradeId = ServletActionContext.getRequest().getParameter("gradeId");
			String midday = ServletActionContext.getRequest().getParameter("midday");
			String newDate = DateUtils.getStringHour();
			int i=Integer.parseInt(newDate);
			if(StringUtils.isBlank(midday)){
				if(i>6&&i<=12){
					midday="1";
				}else if(i>12&&i<=20){
					midday="2";
				}else{
					midday="3";
				}
			}
			List<InfoVo> infoVoList = registerInfoService.findInfoList(deptId,empId,gradeId,midday);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy-MM-dd")
			.create();
			String json = gson.toJson(infoVoList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  根据职称转换
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "queryGradeTitle", results = { @Result(name = "json", type = "json") })
	public void queryGradeTitle() {
		try {
			String encode = ServletActionContext.getRequest().getParameter("encode");
			RegisterGrade gradeList = registerInfoService.queryGradeTitle(encode);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(gradeList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  查询信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "queryRegisterInfo")
	public void queryRegisterInfo() {
		try {
			String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
			Map<String, Object> map = new HashMap<String,Object>();
			InfoPatient infoPatient = registerInfoService.queryRegisterInfo(idcardNo);
			String patientLinkdoorno = infoPatient.getPatientCertificatesno();
			//判断是否在患者黑名单中
			PatientBlackList blackList = registerInfoService.queryBlackList(infoPatient.getInfoMedicalrecordId());
			if(StringUtils.isNotBlank(blackList.getId())){
				map.put("black", "error");
				map.put("infoPatient", 1);
			}else{
				//根据患者的身份证号 查询是否有预约   
				//有预约的返回null
				//没有预约的生成门诊号
				map.put("black", "success");
				RegisterPreregister preregister = registerInfoService.findPreregister(patientLinkdoorno);
				map.put("infoPatient", infoPatient);
				map.put("preregister", preregister.getPreregisterNo());
				map.put("stuts", preregister.getId());
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy-MM-dd")
			.create();
			String json = gson.toJson(map);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	


	
	/**  
	 * @Description：  统计
	 * @Author：ldl
	 * @CreateDate：2015-11-18 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "queryStatistics")
	public void queryStatistics() {
		try{
			String empId = ServletActionContext.getRequest().getParameter("empId");
			String midday = ServletActionContext.getRequest().getParameter("midday");
			//统计 挂号人数 	预约限额		  挂号总限额         预约已挂人数
			InfoStatistics infoStatistics = registerInfoService.queryStatistics(empId,midday);
			//统计 医生加号人数
			RegisterInfo registerInfo = registerInfoService.findInfoAdd(empId);
			infoStatistics.setLimitAdd(registerInfo.getInfoAdd());
			String mapJosn = JSONUtils.toJson(infoStatistics);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  查看历史信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "findInfoHisList", results = { @Result(name = "json", type = "json") })
	public void findInfoHisList() {
		try {
			String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
			InfoPatient infoPatient = registerInfoService.queryRegisterInfo(idcardNo);
			List<RegisterInfo> infoList = registerInfoService.findInfoHisList(infoPatient.getIdCardNo());
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(infoList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  验证
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "findInfoVo", results = { @Result(name = "json", type = "json") })
	public void findInfoVo() {
		try{
			Integer infoFlag = 0;//挂号状态 0当前没有挂当前选择医生科室的号1挂了
			String deptId = ServletActionContext.getRequest().getParameter("deptId");
			String empId = ServletActionContext.getRequest().getParameter("empId");
			String gradeId = ServletActionContext.getRequest().getParameter("gradeId");
			String midday = ServletActionContext.getRequest().getParameter("midday");
			RegisterInfo infoVoList = registerInfoService.findInfoVo(deptId,empId,gradeId,midday,blhcs);
			if(StringUtils.isNotBlank(infoVoList.getId())){
				infoFlag = 1;
			}
			String mapJosn = JSONUtils.toJson(infoFlag);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  查询预约列表
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "findPreregisterList", results = { @Result(name = "json", type = "json") })
	public void findPreregisterList() throws ParseException {
		try {
			String preregisterNo = ServletActionContext.getRequest().getParameter("preregisterNo");
			String preregisterName = ServletActionContext.getRequest().getParameter("preregisterName");
			String preregisterCertificatesno = ServletActionContext.getRequest().getParameter("preregisterCertificatesno");
			String preDate = ServletActionContext.getRequest().getParameter("preDate");
			String idcardId = ServletActionContext.getRequest().getParameter("idcardId");
			String phone = ServletActionContext.getRequest().getParameter("phone");
			InfoPatient infoPatient = new  InfoPatient();
			if(StringUtils.isNotBlank(idcardId)){
				 infoPatient = registerInfoService.queryRegisterInfo(idcardId);
				 preregisterCertificatesno = infoPatient.getPatientCertificatesno();
			}
			List<RegisterPreregister> preregisterList = registerInfoService.findPreregisterList(preregisterNo,preregisterCertificatesno,preregisterName,preDate,phone);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy年MM月dd日")
			.create();
			String json = gson.toJson(preregisterList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  修改&添加
	 * @Author：liudelin
	 * @CreateDate：2015-6-13 下午05:12:16  
	 * @Modifier：liudelin
	 * @ModifyDate：2015-6-29 上午10:20:16  
	 * @ModifyRmk：  添加发票
	 * @ModifyDate：2015-7-9 上午11:20:16  
	 * @ModifyRmk：  添加发票
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"MZGH:function:add"}) 
	@Action(value = "saveRegisterInfo")
	public void saveRegisterInfo() throws Exception {
		try{
			
			String id = "";//返回的ID
			Map<String,String> map=new HashMap<String,String>();
			Map<String,Object> mapinfo=new HashMap<String,Object>();
			OutpatientAccount account = new OutpatientAccount();
			map = registerInfoService.queryFinanceInvoiceNo();//查询发票
			if("error".equals(map.get("resMsg"))){
				mapinfo.put("resMsg", map.get("resMsg"));
				mapinfo.put("resCode", map.get("resCode"));
			}else{
				String no = map.get("resCode");//发票号
				if("4".equals(registerInfo.getPayType())){
					account = registerInfoService.getAccountByMedicalrecord(registerInfo.getMidicalrecordId());
					if(account.getId()==null){
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "该病历号无账户信息,请联系管理员!");
					}else if(account.getAccountState()==0){//停用
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "账户已停用,请联系管理员!");
					}else if(account.getAccountState()==2){//注销3结清4冻结
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "账户已注销!");
					}else if(account.getAccountState()==1&&Double.valueOf(registerInfo.getFee())>account.getAccountBalance()){//总金额大于剩余的门诊金额无法结账
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "账户剩余金额["+account.getAccountBalance()+"],请充值缴费后结算!");
					}else if(account.getAccountDaylimit()>0){//当当日消费额限不等于0时
						List<OutpatientAccountrecord> accountrecord = registerInfoService.queryAccountrecord(registerInfo.getMidicalrecordId());
						Double sum = 0.0;//已使用金额
						if(accountrecord.size()>0){
							for(OutpatientAccountrecord modls : accountrecord){//遍历符合条件的信息
								if(modls.getMoney()!=null){
									sum = sum + modls.getMoney();
								}
							}
							if(account.getAccountDaylimit()<-(sum-registerInfo.getFee())){
								mapinfo.put("resMsg", "error");
								mapinfo.put("resCode", "账户已经超当日额限!当日还可以缴费["+(account.getAccountDaylimit()+sum)+"]");
							}else{
								id = registerInfoService.saveOrUpdateInfo(registerInfo,account,no);
								mapinfo.put("resMsg", "success");
								mapinfo.put("resCode", id);
							}
						}else{
							id = registerInfoService.saveOrUpdateInfo(registerInfo,account,no);
							mapinfo.put("resMsg", "success");
							mapinfo.put("resCode", id);
						}
					}else{
						id = registerInfoService.saveOrUpdateInfo(registerInfo,account,no);
						mapinfo.put("resMsg", "success");
						mapinfo.put("resCode", id);
					}
				}else{
					id = registerInfoService.saveOrUpdateInfo(registerInfo,account,no);
					mapinfo.put("resMsg", "success");
					mapinfo.put("resCode", id);
				}
			}
			String mapJosn = JSONUtils.toJson(mapinfo);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  查询预约回显
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	//@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "queryPreregisterCertificatesno", results = { @Result(name = "json", type = "json") })
	public void queryPreregisterCertificatesno(){
		try{
			String idcardId = ServletActionContext.getRequest().getParameter("idcardId");
			Patient patientList = registerInfoService.queryPreregisterCertificatesno(idcardId);
			String json=JSONUtils.toJson(patientList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：  判断是否存在就诊卡号
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "judgeIdcrad", results = { @Result(name = "json", type = "json") })
	public void judgeIdcrad(){
		try {
			String preNo = ServletActionContext.getRequest().getParameter("preNo");
			InfoPatient preregister = registerInfoService.judgeIdcrad(preNo);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(preregister);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	
	/**  
	 * @Description：  特诊挂号费
	 * @Author：ldl
	 * @CreateDate：2015-12-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@RequiresPermissions(value={"MZGH:function:query"})
	@Action(value = "speciallimitInfo", results = { @Result(name = "json", type = "json") })
	public void speciallimitInfo(){
		try {
			String speciallimitInfo = ServletActionContext.getRequest().getParameter("speciallimitInfo");
			HospitalParameter parameter = registerInfoService.speciallimitInfo(speciallimitInfo);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(parameter);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	/**  
	 * @Description：修改打印发票标记
	 * @Author：ldl
	 * @CreateDate：2016-3-22
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Action(value = "iReportUpdate", results = { @Result(name = "json", type = "json") })
	public void iReportUpdate(){
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			RegisterInfo info = registerInfoService.get(id);
			registerInfoService.iReportUpdate(info);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write("ok");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：查询病历本费用
	 * @Author：ldl
	 * @CreateDate：2016-3-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Action(value = "changePay", results = { @Result(name = "json", type = "json") })
	public void changePay(){
		try {
			HospitalParameter parameter = registerInfoService.changePay();
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(parameter);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：银行下拉框
	 * @Author：ldl
	 * @CreateDate：2016-3-24
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Action(value = "bankCombobox", results = { @Result(name = "json", type = "json") })
	public void bankCombobox(){
		try {
			List<BusinessDictionary> bankList = innerCodeService.getDictionary("bank");
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(bankList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：验证密码是否正确
	 * @Author：ldl
	 * @CreateDate：2016-4-6
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Action(value = "veriPassWord")
	public void veriPassWord(){
		try{
			String state = "";//是否密码正确1正确2不正确
			OutpatientAccount accountIs = registerInfoService.getAccountByMedicalrecord(blhcs);
			if(StringUtils.isBlank(accountIs.getId())){
				state = "NOK";
			}else{
				OutpatientAccount account = registerInfoService.veriPassWord(DigestUtils.md5Hex(passwords),blhcs);
				if(StringUtils.isNotBlank(account.getId())){
					state = "OK";
				}else{
					state = "NO";
				}
			}
			String mapJosn = JSONUtils.toJson(state);
			WebUtils.webSendJSON(mapJosn);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("GHGL_MZGH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_MZGH", "挂号管理_门诊挂号", "2", "0"), e);
		}
	}
	
	public String getPasswords() {
		return passwords;
	}
	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}
	public String getBlhcs() {
		return blhcs;
	}
	public void setBlhcs(String blhcs) {
		this.blhcs = blhcs;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	
}