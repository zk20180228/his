package cn.honry.inpatient.recall.action;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBabyInfoNow;
import cn.honry.base.bean.model.InpatientDayreportDetail;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.hospitalbed.service.HospitalbedInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.inpatient.inpatientBedInfoNow.service.InpatientBedInfoNowInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.inpatient.recall.dao.InpatientReportDetailDAO;
import cn.honry.inpatient.recall.service.RecallService;
import cn.honry.inpatient.recall.vo.Vhinfo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


/**
 * 住院召回
 * @author  ldl
 * @date 2015-8-31 
 * @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/inpatient/recall")
public class RecallAction extends ActionSupport implements ModelDriven<InpatientInfoNow>{

	private Logger logger=Logger.getLogger(RecallAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpServletRequest request = ServletActionContext.getRequest();
	
	private InpatientInfoInInterService inpatientInfoInInterService;
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	public InpatientInfoInInterService getInpatientInfoInInterService() {
		return inpatientInfoInInterService;
	}
	
	private HospitalbedInInterService hospitalbedInInterService;
	@Autowired
	@Qualifier(value = "hospitalbedInInterService")
	public void setHospitalbedInInterService(HospitalbedInInterService hospitalbedInInterService) {
		this.hospitalbedInInterService = hospitalbedInInterService;
	}
	@Autowired
	@Qualifier(value = "inpatientReportDetailDAO")
    private InpatientReportDetailDAO inpatientReportDetailDAO;

	public void setInpatientReportDetailDAO(
			InpatientReportDetailDAO inpatientReportDetailDAO) {
		this.inpatientReportDetailDAO = inpatientReportDetailDAO;
	}

	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public EmployeeInInterService getEmployeeInInterService() {
		return employeeInInterService;
	}

	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}

	@Override
	public InpatientInfoNow getModel() {
		return inpatientInfo;
	}
	
	InpatientInfoNow inpatientInfo = new InpatientInfoNow();

	public InpatientInfoNow getInpatientInfo() {
		return inpatientInfo;
	}

	public void setInpatientInfo(InpatientInfoNow inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	
	private String menuAlias;//栏目别名,在主界面中点击栏目时传到action的参数
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	} 
	@Autowired
	@Qualifier(value = "inpatientBedInfoNowInInterService")
	private InpatientBedInfoNowInInterService inpatientBedInfoNowInInterService;
	public void setInpatientBedInfoNowInInterService(
			InpatientBedInfoNowInInterService inpatientBedInfoNowInInterService) {
		this.inpatientBedInfoNowInInterService = inpatientBedInfoNowInInterService;
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
	@Autowired
	@Qualifier(value="inprePayService")
	private InprePayService inprePayService;
	
	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
	}
	/**
	 *患者病历号 
	 *@author lyy
	 */
	private String medicalrecordId;
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	@Autowired
	@Qualifier(value = "recallService")
	private RecallService recallService;
	public void setRecallService(RecallService recallService) {
		this.recallService = recallService;
	}
	/**
	 * 住院主表的床号
	 */
	private String bedID;
	public String getBedID() {
		return bedID;
	}

	public void setBedID(String bedID) {
		this.bedID = bedID;
	}
	private String deptCode;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**  
	 *  
	 * @Description：  访问
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CYZH:function:view"})
	@Action(value = "listRecall", results = { @Result(name = "list", location = "/WEB-INF/pages/business/recall/recallList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listRecall() {
		return "list";
	}
	
	
	
	/**
	 * 根据病历号查询到患者的住院信息
	 * zhenglin  2016-01-15
	 * @throws Exception  getBedByName  queryInfoListBYmotherName   getcombobox
	 */
	@Action(value = "queryInfoByMId",results={@Result(name="json",type="json")})
	public void queryInfoByMId(){
		//前台传来的是就诊卡号，因牵扯较多，未另建变量
		String medicalNo = request.getParameter("MIdd");
		if(StringUtils.isNotBlank(medicalNo)){
			medicalNo=medicalNo.trim();
		}
		//通过接口查询就诊卡号对应的病历号
		String mid = medicalNo;
		try {
		List<InpatientInfoNow> fo= recallService.queryInfoList(mid);
			if(fo.size()==0){
				List<InpatientInfo> foo=recallService.queryOldInfoList(medicalNo);
				PrintWriter out = WebUtils.getResponse().getWriter();
				String json = JSONUtils.toJson(foo);
				out.write(json);
			}else{
				PrintWriter out = WebUtils.getResponse().getWriter();
				String json = JSONUtils.toJson(fo);
				out.write(json);
			}
		} catch (Exception e) {
			logger.error("ZYSF_CYZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 查询病床根据病床号
	 * @throws Exception
	 */
	@Action(value = "selectBedBYBedID",results={@Result(name="json",type="json")})
	public void selectBedBYBedID(){
		    BusinessHospitalbed hospitalbed=null;
		    PrintWriter out=null;
		try {
			String bediddd = request.getParameter("bediddd");
			 hospitalbed=recallService.getBedByName(bediddd);
			if(hospitalbed==null){
				hospitalbed=new BusinessHospitalbed();
			}
				out = WebUtils.getResponse().getWriter();
		} catch (Exception e) {
			logger.error("ZYSF_CYZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(hospitalbed);
		out.write(json);
	}
	
	
	
	
	/**
	 * 查询病区下的病床下拉
	 * @throws Exception
	 */
	@Action(value = "getcomboboxBystate",results={@Result(name="json",type="json")})
	public void getcomboboxBystate() {
		String deptId = "";
		try{
			String deptCode = request.getParameter("deptBqId");
			String json =null;
			if(StringUtils.isBlank(deptCode)){
				SysDepartment dept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				if(dept!=null){
					List<BusinessHospitalbed> list=recallService.getcombobox(deptId);
					json= JSONUtils.toJson(list);
				}else{
					Map<String,String> map=new HashMap<String,String>();
					map.put("resCode", "error");
					map.put("resMsg", "请选择登录科室");
					json=JSONUtils.toJson(map);
				}
			}else{
				deptId = deptCode;
				List<BusinessHospitalbed> list=recallService.getcombobox(deptId);
				json= JSONUtils.toJson(list);
			}
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYSF_CYZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
		}
	}
	
	/**
	 * 查询婴儿表
	 * @throws Exception
	 */
	@Action(value = "getBaByInfo",results={@Result(name="json",type="json")})
	public void getBaByInfo(){
		PrintWriter out=null;;
		InpatientBabyInfoNow info=null;
		try {
		String inpatientNo=request.getParameter("babyInpatinetNo");
		info=recallService.babyInfoByInpatientNo(inpatientNo);
			out = WebUtils.getResponse().getWriter();
		} catch (Exception e) {
			logger.error("ZYSF_CYZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(info);
		out.write(json);
	}
	
	/**
	 * 查询住院主表
	 * @throws Exception
	 */
	@Action(value = "getInfoMByInpatientNo",results={@Result(name="json",type="json")})
	public void getInfoMByInpatientNo(){
		String inpatientNo=request.getParameter("no");
		PrintWriter out=null;
		InpatientInfoNow info=null;
		try {
		    info=recallService.getInfoByMotherid(inpatientNo);
			out = WebUtils.getResponse().getWriter();
		} catch (Exception e) {
			logger.error("ZYSF_CYZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json = "";
		if(info!=null){
			json = JSONUtils.toJson(info);
		}
		out.write(json);
	}
	
	/**
	 * 保存住院召回
	 */
	@Action(value = "saveInpatientRecall")
	public void saveInpatientRecall() throws Exception {
		SysDepartment deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(deptCode.equals(null)){
			WebUtils.webSendString("error");
		}
		//住院主表的主键Id
		String id=request.getParameter("infoid");
		//妈妈的主键Id
		String motherId=request.getParameter("motherbedId");
		//病床号
		String bedid=request.getParameter("bebdbebbd");
		//住院医生
		String houseDocCode=request.getParameter("houseDocCode");
		//责任医生
		String chargeDocCode=request.getParameter("chargeDocCode");
		//主任医生
		String chiefDocCode=request.getParameter("chiefDocCode");
		//责任护士
		String dutyNurseCode=request.getParameter("dutyNurseCode");
		
		BusinessHospitalbed hospitalbed=hospitalbedInInterService.getBedByid(bedid);
		
		InpatientDayreportDetail detail=new InpatientDayreportDetail();
		//InpatientBedinfoNow bedinfo=new InpatientBedinfoNow();
		InpatientInfoNow info =recallService.get(id);
		if(StringUtils.isNotBlank(houseDocCode)){
			SysEmployee  SysEmployee1=employeeInInterService.getEmpByJobNo(houseDocCode);
			 info.setHouseDocName(SysEmployee1.getName());
		}
		if(StringUtils.isNotBlank(chargeDocCode)){
			SysEmployee  SysEmployee2=employeeInInterService.getEmpByJobNo(chargeDocCode);
			info.setChargeDocName(SysEmployee2.getName());
		}
		if(StringUtils.isNotBlank(chiefDocCode)){
			SysEmployee  SysEmployee3=employeeInInterService.getEmpByJobNo(chiefDocCode);
			info.setChiefDocName(SysEmployee3.getName());
		}
		if(StringUtils.isNotBlank(dutyNurseCode)){
			SysEmployee  SysEmployee4=employeeInInterService.getEmpByJobNo(dutyNurseCode);
			 info.setDutyNurseName(SysEmployee4.getName());
		}
		Patient patient=recallService.getIdByMedId(info.getMedicalrecordId());
		try {
			if(hospitalbed.getId()==null){
				hospitalbed=new BusinessHospitalbed();
			}
			else{
				hospitalbed.setBedState("4");
				hospitalbed.setPatientId(patient.getId());
				hospitalbed.setPatientName(patient.getPatientName());
			}
			//更新住院状态
			info.setInState("I");
		
			//更新病床号
			if(StringUtils.isNotBlank(motherId)){
				InpatientInfoNow info2=recallService.get(motherId);
				info.setBedId(info2.getBedId());
				info.setBedId(info2.getBedName());
			}
			else{
				info.setBedNo(bedid);
				info.setBedName(hospitalbed.getBedName());
			}
			
			//出院日期
			info.setOutDate(null);
			//入院来源
			info.setInSource("1");
			//开账操作
			info.setStopAcount(0);
			//住院医师
			info.setHouseDocCode(houseDocCode);
			
			//主治医师
			info.setChargeDocCode(chargeDocCode);
			
			//主任医师
			info.setChiefDocCode(chiefDocCode);
			
			//责任护士
			info.setDutyNurseCode(dutyNurseCode);
			
			detail.setStateDate(new Date());
			if(info.getDeptCode()!=null){
				detail.setDeptCode(info.getDeptCode());
			}
			else{
				detail.setDeptCode("未知");
			}
			if(info.getNurseCellCode()!=null){
				detail.setNurseCellCode(info.getNurseCellCode());
			}
			else{
				detail.setNurseCellCode("未知");
			}
			if(info.getInpatientNo()!=null){
				detail.setInpatientNo(info.getInpatientNo());
			}
			else{
				detail.setInpatientNo("未知");
			}
			detail.setBedNo(bedid);
			detail.setStatType("未知");
			detail.setMark("转归");
			detail.setValidState(0);
			detail.setExtend(null);
			detail.setUpflag(0);
			String userid=ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			detail.setCreateUser(userid);
			detail.setCreateTime(new Date());
			detail.setUpdateTime(new Date());
			detail.setCancelOperCode(null);
			detail.setCancelOperDate(null);
			detail.setHospitalId(HisParameters.CURRENTHOSPITALID);
			detail.setAreaCode(inprePayService.getDeptArea(deptCode.getDeptCode()));
			inpatientReportDetailDAO.save(detail);
			recallService.saveOrUpdate(info);
			hospitalbedInInterService.saveOrUpdate(hospitalbed);
		} catch (Exception e) {
			WebUtils.webSendString("error");
			//hedong 20170407 异常信息输出至日志文件
			logger.error("ZYSF_CYZH", e);
			//hedong 20170407 异常信息保存至mongodb
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
		}
		WebUtils.webSendString("success");
	}
	
	
	/**
	 * 根据患者的患者号来查询患者信息！！！！！！
	 * @throws Exception
	 */
	@Action(value = "queryInfoByName",results={@Result(name="json",type="json")})
	public void queryInfoByName(){
		String name = request.getParameter("name");
		Patient patient=null;
		PrintWriter out=null;
		try {
		patient= recallService.getInfoBYName(name);
			out = WebUtils.getResponse().getWriter();
		} catch (Exception e) {
			logger.error("ZYSF_CYZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(patient);
		out.write(json);
	}
	
	
	/**
	 * 根据员工科室和员工类型来查询医生,护士
	 * @throws Exception
	 */
	@Action(value = "getEmpComboobxOfDoctor",results={@Result(name="json",type="json")})
	public void getEmpComboobxOfDoctor(){
		String type=request.getParameter("type");
		List<SysEmployee> list=null;
		PrintWriter out=null;
		try {
		    list=recallService.getEmpCombobox(type);
			out = WebUtils.getResponse().getWriter();
		} catch (Exception e) {
			logger.error("ZYSF_CYZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json = JSONUtils.toJson(list);
		out.write(json);
	}
	/**  
	 *  
	 * @Description：  查询（住院号&&医保号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CYZH:function:query"}) 
	@Action(value = "queryRecallList", results = { @Result(name = "json", type = "json") })
	public void queryRecallList() {
		String inpatientNo = ServletActionContext.getRequest().getParameter("inpatientNo");
		String mcardNo = ServletActionContext.getRequest().getParameter("mcardNo");
		String bedId = ServletActionContext.getRequest().getParameter("bedId");
		String patientName = ServletActionContext.getRequest().getParameter("patientName");
		try {
			VidBedInfo vidBedInfo = recallService.queryRecallList(mcardNo,inpatientNo,bedId,patientName);
			String json = JSONUtils.toJson(vidBedInfo);
			PrintWriter out = WebUtils.getResponse().getWriter();
			out.write(json);
		}
		catch (Exception ex) {
			logger.error("ZYSF_CYZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), ex);
		}
	}
	/**  
	 *  
	 * @Description：  业务判断查询（住院号&&医保号）
	 * @Author：liudelin
	 * @CreateDate：2015-8-31上午9:12
	 * @ModifyRmk：
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CYZH:function:add"}) 
	@Action(value = "editVibPatient", results = { @Result(name = "json", type = "json") })
	public void editVibPatient() {
		try {
			String inpatientNos = ServletActionContext.getRequest().getParameter("inpatientNos");
			String bedIds = ServletActionContext.getRequest().getParameter("bedIds");
			String houseDocCode = ServletActionContext.getRequest().getParameter("houseDocCode");
			String chargeDocCode = ServletActionContext.getRequest().getParameter("chargeDocCode");
			String dutyNurseCode = ServletActionContext.getRequest().getParameter("dutyNurseCode");
			String chiefDocCode = ServletActionContext.getRequest().getParameter("chiefDocCode");
			VidBedInfo vidBedInfoId = recallService.RecallByInpatientNo(inpatientNos);
			String json = "";
			//判断是住院状态是否发生改变
			if(vidBedInfoId!=null&&!"B".equals(vidBedInfoId.getInState())){
				//判断住院状态是否是出院结算
				if("O".equals(vidBedInfoId.getInState())){
					json = JSONUtils.toJson(2);
				}else{
					json = JSONUtils.toJson(1);
				}
			//判断在出院状态时
			}else if(vidBedInfoId!=null&&"B".equals(vidBedInfoId.getInState())){
				//判断是否是婴儿
				if(vidBedInfoId.getBabyFlag()==null&&vidBedInfoId.getBabyFlag()==1){
					String s = inpatientNos.substring(4,2);
					VidBedInfo vidBedInfoMom = recallService.RecallByInpatientNo(inpatientNos.replace(s, "00"));
					//判断她母亲是不是登记
					if(StringUtils.isNotBlank(vidBedInfoMom.getId())){
						//判断他母亲是不是在院状态
						if(!"R".equals(vidBedInfoMom.getInState())||!"I".equals(vidBedInfoMom.getInState())){
							json = JSONUtils.toJson(4);
						}else{
							recallService.updateBedid(bedIds,inpatientNos,chiefDocCode,dutyNurseCode,chargeDocCode,houseDocCode);
						}
					}else{
						recallService.updateBedid(bedIds,inpatientNos,chiefDocCode,dutyNurseCode,chargeDocCode,houseDocCode);
					}
				}else{
					//当不是婴儿的情况下
					//判断病床的使用状态
					VidBedInfo VidBedName = recallService.RecallBedByName(bedIds);
					if(!"402880984dd22fe5014dd2312e8a0002".equals(VidBedName.getBedState())){
						json = JSONUtils.toJson(5);
					}else{
						recallService.updateBedid(bedIds,inpatientNos,chiefDocCode,dutyNurseCode,chargeDocCode,houseDocCode);
					}
				}
			}
			
				PrintWriter out = WebUtils.getResponse().getWriter();
				out.write(json);
			}
		catch (Exception ex) {
			logger.error("ZYSF_CYZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), ex);
		}
	}
	
	/**
	 * 根据患者床位ID查询病床信息
	 * @author  donghe
	 * @createDate： 
	 * @modifyDate：2016年5月12日 下午6:35:16
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getbusinesstion")
	public void getbusinesstion() {
		try{
			List<Vhinfo> info=recallService.getBedidByBusinessHospitalbed(bedID);
			String json =JSONUtils.toJson(info);
			WebUtils.webSendJSON(json);
		}catch(Exception ex){
			logger.error("ZYSF_CYZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_CYZH", "住院收费_出院召回", "2", "0"), ex);
		}
	}
}
