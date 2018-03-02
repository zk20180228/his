package cn.honry.outpatient.changeDeptLog.action;

import java.io.PrintWriter;
import java.util.ArrayList;
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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.outpatient.blacklist.action.BlacklistAction;
import cn.honry.outpatient.changeDeptLog.service.RegisterChangeDeptLogService;
import cn.honry.outpatient.info.service.RegisterInfoService;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.info.vo.InfoVo;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *  
 * @Description：  挂号换科
 * @Author：liudelin
 * @CreateDate：2015-6-25 下午05:20:16  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
//@Namespace(value = "/register")
@Namespace(value = "/outpatient/changeDeptLog")
public class RegisterChangeDeptLogAction extends ActionSupport implements ModelDriven<RegisterChangeDeptLog>{

	/**
	 * id
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public RegisterChangeDeptLog getModel() {

		return registerChangeDeptLog;
	}
	private RegisterChangeDeptLog registerChangeDeptLog = new RegisterChangeDeptLog();
	
	RegisterInfo  registerInfo  = new RegisterInfo();
	
	
	private RegisterChangeDeptLogService registerChangeDeptLogService;
	@Autowired
	@Qualifier(value = "registerChangeDeptLogService")
	public void setRegisterChangeDeptLogService(
			RegisterChangeDeptLogService registerChangeDeptLogService) {
		this.registerChangeDeptLogService = registerChangeDeptLogService;
	}
	@Autowired
	@Qualifier(value = "registerInfoService")
	private RegisterInfoService registerInfoService;
	
	public RegisterInfoService getRegisterInfoService() {
		return registerInfoService;
	}
	public void setRegisterInfoService(RegisterInfoService registerInfoService) {
		this.registerInfoService = registerInfoService;
	}
	
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
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
	// 记录异常日志
	private Logger logger = Logger.getLogger(RegisterChangeDeptLogAction.class);
	// 存储异常
	@Resource
	private HIASExceptionService hiasExceptionService;
	
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	
	/**  
	 *  
	 * @Description：  挂号换科列表
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHHK:function:view"}) 
	@Action(value = "listRegisterChangeDeptLog", results = { @Result(name = "list", location = "/WEB-INF/pages/register/changeDeptLog/registerChangeDeptLogList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRegisterChangeDeptLog() {
		return "list";
	}
	/**  
	 *  
	 * @Description：  卡号查询挂号记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHHK:function:query"}) 
	@Action(value = "queryRegisterChangeDeptLog", results = { @Result(name = "json", type = "json") })
	public void queryRegisterChangeDeptLog(){
		try {
			String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
			InfoPatient infoPatient = registerInfoService.queryRegisterInfo(idcardNo);
			List<RegisterInfo> infoList = null;
			if(StringUtils.isNotBlank(infoPatient.getIdCardNo())){
				String no = ServletActionContext.getRequest().getParameter("no");
				String state = ServletActionContext.getRequest().getParameter("state");
				infoList = registerChangeDeptLogService.findInfoList(infoPatient.getIdCardNo(),no,state);
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(infoList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
			
		}
	}
	
	/**  
	 * @Description：  卡号查询换科记录
	 * @Author：liudelin
	 * @ModifyDate：2015-11-11上午9:31:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHHK:function:query"}) 
	@Action(value = "queryChangeDeptLogList", results = { @Result(name = "json", type = "json") })
	public void queryChangeDeptLogList(){
		try {
			List<RegisterChangeDeptLog> changeDeptLogList = new  ArrayList<RegisterChangeDeptLog>();
			String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
			PatientIdcard patientIdcard = registerChangeDeptLogService.queryPatientIdcardByidcardNo(idcardNo);//根据就诊卡号查询就诊卡ID
			List<RegisterInfo> info = registerChangeDeptLogService.findInfos(patientIdcard.getId());//根据就诊卡ID查询挂号信息
			String ids = "";//挂号记录ID
			if(info.size()>0){
				for(RegisterInfo modl:info){
					if(ids!=""){
						ids = ids + "','";
					}
					ids = ids + modl.getId();
				}
			}
			if(!"".equals(ids)){
				changeDeptLogList = registerChangeDeptLogService.queryChangeDeptLogList(ids);
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(changeDeptLogList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  更换科室
	 * @param:id(挂号信息表id)
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHHK:function:edit"}) 
	@Action(value = "eidtRgisterChangeDeptLog", results = { @Result(name = "list", location = "/WEB-INF/pages/register/changeDeptLog/eidtRegisterChangeDeptLog.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String eidtRgisterChangeDeptLog() {
		String id = ServletActionContext.getRequest().getParameter("id");
		RegisterInfo info = registerInfoService.get(id);
		RegisterChangeDeptLog registerChangeDeptLog = new RegisterChangeDeptLog();
		registerChangeDeptLog.setOldDept(info.getDept());
		registerChangeDeptLog.setOldDoc(info.getExpxrt());
		String deptId = registerChangeDeptLog.getOldDept();
		SysDepartment sysDepartmentList = registerChangeDeptLogService.findSysDepartment(deptId);
		String expxrtId = registerChangeDeptLog.getOldDoc();
		SysEmployee  sysEmployeeList = registerChangeDeptLogService.findSysEmployee(expxrtId);
		registerChangeDeptLog.setOldDeptName(sysDepartmentList.getDeptName());
		registerChangeDeptLog.setOldDocName(sysEmployeeList.getName());
		registerChangeDeptLog.setGradeX(info.getGrade());
		registerChangeDeptLog.setInfoId(id);
		registerChangeDeptLog.setRigisterId(id);
		ServletActionContext.getRequest().setAttribute("registerChangeDeptLog", registerChangeDeptLog);
		return "list";
	}
	/**  
	 *  
	 * @Description：  卡号查询挂号记录
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHHK:function:query"})
	@Action(value = "queryOldChangeDeptLog", results = { @Result(name = "json", type = "json") })
	public void queryOldChangeDeptLog(){
		try {
			//根据id查询原来的医生和科室
			String id = ServletActionContext.getRequest().getParameter("id");
			RegisterInfo changeDeptLogList =registerChangeDeptLogService.findInfoId(id);
			//根据医生Id查询到医生姓名
			String expxrtId = changeDeptLogList.getExpxrt();
			SysEmployee  sysEmployeeList = registerChangeDeptLogService.findSysEmployee(expxrtId);
			// 根据科室Id查询到科室名
			String deptId = changeDeptLogList.getDept();
			SysDepartment sysDepartmentList = registerChangeDeptLogService.findSysDepartment(deptId);
			changeDeptLogList.setExpxrtName(sysEmployeeList.getName());
	 		changeDeptLogList.setDeptName(sysDepartmentList.getDeptName());
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.setDateFormat("yyyy-MM-dd") 
			.create();
			String json = gson.toJson(changeDeptLogList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  下拉框科室
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHHK:function:query"})
	@Action(value = "changeDepartmentCombobox", results = { @Result(name = "json", type = "json") })
	public void changeDepartmentCombobox() {
		try {
			List<SysDepartment>	sysDepartmentList = registerChangeDeptLogService.getCombobox();
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(sysDepartmentList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  下拉框医生
	 * @param:departmentId(科室Id)，grade（级别id）
	 * @Author：liudelin
	 * @ModifyDate：2015-6-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHHK:function:query"})
	@Action(value = "changeEmployeeCombobox", results = { @Result(name = "json", type = "json") })
	public void changeEmployeeCombobox() {
		try {
			String departmentId = ServletActionContext.getRequest().getParameter("departmentId");
			String grade = ServletActionContext.getRequest().getParameter("grade");
			List<InfoVo>sysEmployeeList = registerChangeDeptLogService.EgetCombobox(departmentId,grade);
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(sysEmployeeList);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	/**
	 * 添加&修改
	 * @author  liudelin
	 * @date 2015-06-26
	 * @version 1.0
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"GHHK:function:edit"}) 
	@Action(value = "registerChangeSave", results = { @Result(name = "edit", location = "/WEB-INF/pages/register/changeDeptLog/registerChangeDeptLogList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String registerChangeSave() throws Exception {
		PrintWriter out = WebUtils.getResponse().getWriter();
		try{
			String rigisterIds = ServletActionContext.getRequest().getParameter("rigisterIds");
			String remark = "";
			if(StringUtils.isNotEmpty(registerChangeDeptLog.getRemark())){
				remark = registerChangeDeptLog.getRemark().replaceAll("\\r\\n", "</br>");
				remark = remark.replaceAll("\\n", "</br>");
			}
			registerChangeDeptLog.setRemark(remark);
			registerChangeDeptLog.setRigisterId(rigisterIds);
			registerChangeDeptLogService.saveChange(registerChangeDeptLog);
			out.write("success");
		}catch(Exception e){
			out.write("error");
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
		return "edit";
	}
	
	/**  
	 *  
	 * @Description：  患者信息
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-5 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"GHHK:function:query"})
	@Action(value = "findPatientList", results = { @Result(name = "json", type = "json") })
	public void findPatientList() {
		try {
			String idcardNo = ServletActionContext.getRequest().getParameter("idcardNo");
			String no = ServletActionContext.getRequest().getParameter("no");
			RegisterInfo infoPatientList = new RegisterInfo();
			Map<String,Object> map=new HashMap<String,Object>();
			InfoPatient infoPatient = registerInfoService.queryRegisterInfo(idcardNo);
			if(StringUtils.isBlank(infoPatient.getIdCardNo())){
				map.put("resMsg", "error");
				map.put("resCode", "卡号错误，请重新输入");
			}else{
				infoPatientList = registerChangeDeptLogService.findPatientList(infoPatient.getIdCardNo(),no);
				if(StringUtils.isBlank(infoPatientList.getId())){
					map.put("resMsg", "error");
					map.put("resCode", "改患者在有效时间内没有挂号记录");
				}else{
					map.put("resMsg", "success");
					map.put("infoPatientList",infoPatientList);
				}
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY).setDateFormat("yyyy-MM-dd")
			.create();
			String json = gson.toJson(map);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	/**  
	 *  
	 * @Description：  渲染部门
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querydeptComboboxs", results = { @Result(name = "json", type = "json") })
	public void querydeptComboboxs() {
		List<SysDepartment> deptList = registerChangeDeptLogService.querydeptCombobox();
		Map<String,String> deptMap = new HashMap<String, String>();
		for(SysDepartment dept : deptList){
			deptMap.put(dept.getId(), dept.getDeptName());
		}
		String json=JSONUtils.toJson(deptMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  渲染级别
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querygradeComboboxs", results = { @Result(name = "json", type = "json") })
	public void querygradeComboboxs() {
		try {
			List<RegisterGrade> gradeList = registerChangeDeptLogService.querygradeCombobox();
			Map<String,String> gradeMap = new HashMap<String, String>();
			for(RegisterGrade grade : gradeList){
				gradeMap.put(grade.getId(), grade.getName());
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(gradeMap);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  渲染人员
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryempComboboxs", results = { @Result(name = "json", type = "json") })
	public void queryempComboboxs() {
		List<SysEmployee> empList = registerChangeDeptLogService.queryempCombobox();
		Map<String,String> empMap = new HashMap<String, String>();
		for(SysEmployee emp : empList){
			//2016-7-11，用account代替id
			empMap.put(emp.getJobNo(), emp.getName());
		}
		String json=JSONUtils.toJson(empMap);
		WebUtils.webSendJSON(json);
	}
	
	/**  
	 *  
	 * @Description：  渲染合同单文
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querycontComboboxs", results = { @Result(name = "json", type = "json") })
	public void querycontComboboxs() {
		try {
			List<BusinessContractunit> contList = registerChangeDeptLogService.querycontCombobox();
			Map<String,String> contMap = new HashMap<String, String>();
			for(BusinessContractunit cont : contList){
				contMap.put(cont.getId(), cont.getName());
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(contMap);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  渲染挂号类别
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-10 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "querytypeComboboxs", results = { @Result(name = "json", type = "json") })
	public void querytypeComboboxs() {
		try {
			List<BusinessDictionary> typeList = innerCodeService.getDictionary("registerType");
			Map<String,String> typeMap = new HashMap<String, String>();
			for(BusinessDictionary type : typeList){
				typeMap.put(type.getEncode(), type.getName());
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(typeMap);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	
	/**  
	 *  
	 * @Description：  渲染证件类别
	 * @param:
	 * @Author：liudelin
	 * @ModifyDate：2015-11-26 下午09:06:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "likeCertificate", results = { @Result(name = "json", type = "json") })
	public void likeCertificate() {
		try {
			List<BusinessDictionary> certificateList = innerCodeService.getDictionary("certificate");
			Map<String,String> certificateMap = new HashMap<String, String>();
			for(BusinessDictionary type : certificateList){
				certificateMap.put(type.getEncode(), type.getName());
			}
			Gson gson = new GsonBuilder()
			.registerTypeAdapterFactory(HibernateCascade.FACTORY)
			.create();
			String json = gson.toJson(certificateMap);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
}
