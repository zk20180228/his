package cn.honry.outpatient.newChangeDeptLog.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.outpatient.newChangeDeptLog.service.ChangeDeptService;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outpatient/newChangeDeptLog")
public class ChangeDeptAction extends ActionSupport{

	private Logger logger=Logger.getLogger(ChangeDeptAction.class);
	
	/**
	 * 新挂号换科
	 */
	private static final long serialVersionUID = 1L;
	
	private ChangeDeptService changeDeptService;
	@Autowired
	@Qualifier(value = "changeDeptService")
	public void setChangeDeptService(ChangeDeptService changeDeptService) {
		this.changeDeptService = changeDeptService;
	}
	
	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
		this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService  innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	private Registration ationInfo;
	
	public Registration getAtionInfo() {
		return ationInfo;
	}

	public void setAtionInfo(Registration ationInfo) {
		this.ationInfo = ationInfo;
	}
	
	private RegisterChangeDeptLog changeDeptLog;
	
	public RegisterChangeDeptLog getChangeDeptLog() {
		return changeDeptLog;
	}

	public void setChangeDeptLog(RegisterChangeDeptLog changeDeptLog) {
		this.changeDeptLog = changeDeptLog;
	}
	
	private String middayOld;
	
	public String getMiddayOld() {
		return middayOld;
	}

	public void setMiddayOld(String middayOld) {
		this.middayOld = middayOld;
	}

	@Resource
	private RedisUtil redis;
	
	/**  
	 * @Description：  新挂号换科
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "toChangeDeptView", results = { @Result(name = "list", location = "/WEB-INF/pages/register/newChangeDept/newChangeDept.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRegisterInfo() {
		return "list";
	}
	
	/**  
	 * @Description：  挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHHKX:function:query"})
	@Action(value = "queryRegisterMain")
	public void queryRegisterMain(){
		try {
			List<RegistrationNow> codeRegistertypeList = changeDeptService.queryRegisterMain(ationInfo.getCardNo().trim(),ationInfo.getClinicCode().trim());
			if(codeRegistertypeList!=null&&codeRegistertypeList.size()>0){
				BusinessDictionary codes = innerCodeService.getDictionaryByCode("certificate", codeRegistertypeList.get(0).getCardType());
				codeRegistertypeList.get(0).setCardType(codes.getName());
			}
			String mapJosn = JSONUtils.toJson(codeRegistertypeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  挂号换科记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@RequiresPermissions(value={"GHHKX:function:query"})
	@Action(value = "queryChangeDept")
	public void queryChangeDept(){
		try {
			String ids = "";
			List<RegistrationNow> registrationList = changeDeptService.queryChangeDept(ationInfo.getCardNo(),ationInfo.getClinicCode());
			if(registrationList!=null && registrationList.size()>0){
				for(RegistrationNow modls : registrationList){
					if(ids!=""){
						ids = ids + "','";
					}
					ids = ids + modls.getId();
				}
			}
			List<RegisterChangeDeptLog> changeDeptList = changeDeptService.queryChangeDeptList(ids);
			String mapJosn = JSONUtils.toJson(changeDeptList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：  患者信息
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPritient")
	public void queryPritient(){
		try {
			List<RegistrationNow> codeRegistertypeList = changeDeptService.queryRegisterMain(ationInfo.getCardNo(),ationInfo.getClinicCode());
			String mapJosn = JSONUtils.toJson(codeRegistertypeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description： 跳转页面
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "eidtRgisterChangeDeptLog", results = { @Result(name = "list", location = "/WEB-INF/pages/register/newChangeDept/newChangeDeptEidt.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String eidtRgisterChangeDeptLog() {
		try {
			RegistrationNow ation = changeDeptService.getById(ationInfo.getId());
			changeDeptLog = new RegisterChangeDeptLog();
			changeDeptLog.setOldDept(ation.getDeptCode());
			changeDeptLog.setOldDeptName(ation.getDeptName());
			changeDeptLog.setOldDoc(ation.getDoctCode());
			changeDeptLog.setOldDocName(ation.getDoctName());
			changeDeptLog.setGradeX(ation.getReglevlCode());
			changeDeptLog.setNewMidday(ation.getNoonCode());
			changeDeptLog.setRigisterId(ationInfo.getId());
			changeDeptLog.setNewDoc(ationInfo.getDoctCode());//用于存放已挂医生的所有code
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
		return "list";
	}
	
	/**  
	 * @Description： 挂号科室下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "changeDepartmentCombobox")
	public void changeDepartmentCombobox(){
		try {
			List<SysDepartment> codeRegistertypeList = deptInInterService.queryAllDept();//直接从接口中取
//			List<SysDepartment> codeRegistertypeList = changeDeptService.changeDepartmentCombobox();//从自己的service中取
			String mapJosn = JSONUtils.toJson(codeRegistertypeList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：换科医生
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "changeEmployeeCombobox")
	public void changeEmployeeCombobox(){
		try {
			List<InfoVo> codeRegistertypeList = changeDeptService.changeEmployeeCombobox(changeDeptLog.getGradeX(),changeDeptLog.getNewDept());
			
			List<HospitalParameter> middyParameter = parameterInnerService.getMiddyParameter(HisParameters.WORKTIME);
			int starMornTime = 0;
			int endMornTime = 0;
			int starAfterTime = 0;
			int endAfterTime = 0;
			int starEvenTime = 0;
			int endEvenTime = 0;
			for(HospitalParameter hp : middyParameter){
				String middyNum = hp.getParameterValue();
				int startTime = Integer.parseInt(hp.getParameterDownlimit().split(":")[0])*60+Integer.parseInt(hp.getParameterDownlimit().split(":")[1]);
				int endTime = Integer.parseInt(hp.getParameterUplimit().split(":")[0])*60+Integer.parseInt(hp.getParameterUplimit().split(":")[1]);
				switch (middyNum) {
				case "1":
					starMornTime = startTime;
					endMornTime =  endTime;
					break;
				case "2":
					starAfterTime = startTime;
					endAfterTime =  endTime;
					break;
				case "3":
					starEvenTime = startTime;
					endEvenTime =  endTime;
					break;
				default:
					break;
				}
			}
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY); 
			int minu = c.get(Calendar.MINUTE);
			int time = 60*hour+minu;//当前时间。精确到分
			int middyNum = 0;
			if(time >= starMornTime && time < endMornTime){
				middyNum = 1;
			}
			if(time >= starAfterTime && time < endAfterTime){
				middyNum = 2;
			}
			if(time >= starEvenTime && time < endEvenTime){
				middyNum = 3;
			}
			List<InfoVo> arrayList = new ArrayList<InfoVo>();
			for(InfoVo list : codeRegistertypeList){
				int midday = list.getMidday();
				if(midday>=middyNum){
					arrayList.add(list);
				}
			}
			
			String mapJosn = JSONUtils.toJson(arrayList);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
	/**  
	 * @Description：保存
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "registerChangeSave")
	public void registerChangeSave(){
		Map<String,String> map = new HashMap<>();
		String date=DateUtils.formatDateY_M_D(new Date());
		String key="MZGH";
		String middayNew = String.valueOf(changeDeptLog.getNewMidday());
		String field =date+"-"+changeDeptLog.getOldDept()+"-"+changeDeptLog.getOldDoc()+"-"+middayOld;//原医生的号源信息
		String field1=date+"-"+changeDeptLog.getNewDept()+"-"+changeDeptLog.getNewDoc()+"-"+middayNew;//新医生的号源信息
		try {
			Boolean hexists  = redis.hexists(key, field);
			Boolean hexists1 = redis.hexists(key, field1);
			if(hexists1){
				Long hincr = redis.hincr(key, field1, -1L);//新医生的号源数-1
				if(hincr<0){//已挂满
					redis.hincr(key, field1, 1L);//恢复号源
					map.put("resMsg", "error");
					map.put("resCode", "该医生号源已挂满！");
					String mapJosn = JSONUtils.toJson(map);
					WebUtils.webSendJSON(mapJosn);
					return;
				}
			}
			if(hexists){
				redis.hincr(key, field, 1L);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "1"), e);
		}
		try {
			map = changeDeptService.registerChangeSave(changeDeptLog);
		} catch (Exception e) {//保存过程中发生异常
			try {//恢复原来的号源信息
				redis.hincr(key, field, -1L);
				redis.hincr(key, field1, 1L);
				map.put("resMsg","error");
				map.put("resCode", "发生异常,请刷新后重试或联系管理员!");
				logger.error("GHGL_GHHK", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error("GHGL_GHHK", e1);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "1"), e1);
			}
		}
		
		String mapJosn = JSONUtils.toJson(map);
		WebUtils.webSendJSON(mapJosn);
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
	@Action(value = "querydeptComboboxs")
	public void querydeptComboboxs() {
		try {
			List<SysDepartment> deptList = deptInInterService.queryAllDept();//直接从接口中取
//			List<SysDepartment> deptList = changeDeptService.querydeptComboboxs();
			Map<String,String> deptMap = new HashMap<String, String>();
			for(SysDepartment dept : deptList){
				deptMap.put(dept.getDeptCode(), dept.getDeptName());
			}
			String mapJosn = JSONUtils.toJson(deptMap);
			WebUtils.webSendJSON(mapJosn);
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
	@Action(value = "queryempComboboxs")
	public void queryempComboboxs() {
		try {
			List<SysEmployee> empList = employeeInInterService.getEmpInfo();
			Map<String,String> empMap = new HashMap<String, String>();
			for(SysEmployee emp : empList){
				empMap.put(emp.getJobNo(), emp.getName());
			}
			String mapJosn = JSONUtils.toJson(empMap);
			WebUtils.webSendJSON(mapJosn);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GHGL_GHHK", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("GHGL_GHHK", "挂号管理_挂号换科", "2", "0"), e);
		}
	}
	
}
