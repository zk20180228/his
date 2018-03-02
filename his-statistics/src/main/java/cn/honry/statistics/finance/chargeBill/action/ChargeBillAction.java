package cn.honry.statistics.finance.chargeBill.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.math.NumberUtils;
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
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.baseinfo.hospital.service.HospitalInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.report.service.IReportService;
import cn.honry.statistics.finance.chargeBill.service.ChargeBillService;
import cn.honry.statistics.finance.chargeBill.vo.ChargeBillVo;
import cn.honry.statistics.finance.chargeBill.vo.InPatient_Info_no;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**   
*  
* @className：ChargeBill
* @description：住院患者费用清单action
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value="/statistics/ChargeBill")
public class ChargeBillAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(ChargeBillAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	/**
	 * 注入chargeBillService
	 */
	private ChargeBillService chargeBillService;
	@Autowired
	@Qualifier(value = "chargeBillService")
	public void setChargeBillService(ChargeBillService chargeBillService) {
		this.chargeBillService = chargeBillService;
	}
	/**
	 * 注入chargeBillService
	 */
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "innerCodeService")
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	/**
	 * 注入inpatientInfoInInterService公共接口
	 */
	private InpatientInfoInInterService inpatientInfoInInterService;
	@Autowired
	@Qualifier(value = "inpatientInfo InInterService")
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	/**
	 * 注入patinentInnerService公共接口
	 * 
	 */
	private PatinentInnerService patinentInnerService;
	@Autowired
	@Qualifier(value = "patinentInnerService")
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	/**
	 * @see注入报表借口口
	 */
	@Autowired
	@Qualifier(value = "iReportService")
	private IReportService iReportService;
	public void setiReportService(IReportService iReportService) {
		this.iReportService = iReportService;
	}
	/**
	 * @see 渲染科室
	 */
	@Autowired
	@Qualifier(value="deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	/**
	 * @see 渲染人
	 */
	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(
			EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	/**获取医院名称**/
	@Autowired
	@Qualifier(value="hospitalInInterService")
	private HospitalInInterService hospitalInInterService;
	public void setHospitalInInterService(
			HospitalInInterService hospitalInInterService) {
		this.hospitalInInterService = hospitalInInterService;
	}
	/**
	 * 栏目别名,在主界面中点击栏目时传到action的参数
	 */
	private String menuAlias;
	/**
	 *病历号 
	 */
	private String medId;
	/**
	 *住院流水号 
	 */
	private String inpatientNo;
	/**
	 * 起始时间
	 * @return
	 */
	private String startTime;
	/**
	 * 截止时间
	 * @return
	 */
	private String endTime;
	/**
	 * 发放状态
	 * @return
	 */
	private String sendFlag;
	/**
	 * 6位病历号
	 * @return
	 */
	private String medicalrecordId;
	
	private HttpServletRequest request = null;
	
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getMedId() {
		return medId;
	}
	public void setMedId(String medId) {
		this.medId = medId;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	/**
	 * @Description:获取住院患者费用清单list页面
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYHZFYQD:function:view"})
	@Action(value="chargebillist",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/chargeBill/chargeBillist.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String chargebillist() {
		Date date = new Date();
		endTime = DateUtils.formatDateY_M_D(date);
		startTime = DateUtils.formatDateYM(date)+"-01";
		return "list";
	}
	/**
	 * @Description:通过病历号住院主表的患者信息
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	**/
	@Action(value = "queryInpatientInfobill")
	public void queryInpatientInfobill(){
		try{
			List<InpatientInfoNow> info=chargeBillService.queryInpatientInfo(medicalrecordId);
			String json=JSONUtils.toJson(info);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJFX_ZYHZFYQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYHZFYQD", "住院统计分析_住院患者费用清单", "2", "0"), e);   
		}
	}
	/**
	 * @Description:通过住院流水号查询患者的费用信息（住院药品明细表、住院费药品明细表）
	 * @Author：  tcj
	 * @CreateDate： 2016-4-9
	 * @version 1.0
	**/
	@Action(value = "queryDatagridinfo")
	public void queryDatagridinfo(){
		try{
			if(StringUtils.isBlank(startTime)){
				Date date = new Date();
				startTime = DateUtils.formatDateYM(date)+"-01";
			}
			if(StringUtils.isBlank(endTime)){
				Date date = new Date();
				endTime = DateUtils.formatDateY_M_D(date);
			}
			
			List<ChargeBillVo> cbl=chargeBillService.queryDatagridinfo(inpatientNo,startTime,endTime,sendFlag);
			for(ChargeBillVo v:cbl){
				v.setTotCost(Double.valueOf(NumberUtil.init().format(v.getQty()*v.getUnitPrice(),2)));
			}
			String json=JSONUtils.toJson(cbl,"yyyy-MM-dd");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJFX_ZYHZFYQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYHZFYQD", "住院统计分析_住院患者费用清单", "2", "0"), e);   
		}
	}
	
	/**
	 * @see通过住院流水号查询患者的费用信息提供打印功能
	 * @CreateDate： 2017-2-24
	 * @author xcl
	 */
	@Action(value = "queryDatagridinfoPDF")
	public void queryDatagridinfoPDF(){
		if(startTime==null){
			Date date = new Date();
			startTime = DateUtils.formatDateYM(date)+"-01";
		}
		if(endTime==null){
			Date date = new Date();
			endTime = DateUtils.formatDateY_M_D(date);
		}
		
		
		ArrayList<InPatient_Info_no> inPatientArray=new ArrayList<InPatient_Info_no>();
		InPatient_Info_no inPatient=new InPatient_Info_no();
		try {
			inPatient.setChargeBillVo(chargeBillService.queryDatagridinfo(inpatientNo,startTime,endTime,sendFlag));
		} catch (Exception e1) {
			logger.error("ZYTJFX_ZYHZFYQD", e1);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJFX_ZYHZFYQD", "住院统计分析_住院患者费用清单", "2", "0"), e1);   
			e1.printStackTrace();
		}
		//查询科室用作渲染
		Map<String,String> deptCodeAndName=deptInInterService.querydeptCodeAndNameMap();
		//渲染人
		Map<String,String>  empCodeAndName=employeeInInterService.queryEmpCodeAndNameMap();
		//渲染单位
		List<BusinessDictionary> cstl=innerCodeService.getDictionary("nonmedicineencoding");
		Map<String,String> map=new HashMap<String,String>();
		for(BusinessDictionary cst:cstl){
			map.put(cst.getEncode(), cst.getName());
		}
		for(ChargeBillVo vo:inPatient.getChargeBillVo()){
			//渲染科室
			vo.setExecuteDeptcode(deptCodeAndName.get(vo.getExecuteDeptcode()));
			//渲染单位
			if(map.get(vo.getCurrentUnit()) != null){
				vo.setCurrentUnit(map.get(vo.getCurrentUnit()));
			}
			//渲染人
			vo.setChargeOpercode(empCodeAndName.get(vo.getChargeOpercode()));
			if("y".equals(vo.getState())){
				vo.setState("药品");
			}else{
				vo.setState("非药品");
			}
		}
		deptCodeAndName=null;
		empCodeAndName=null;
		request=ServletActionContext.getRequest();
		 String root_path = request.getSession().getServletContext().getRealPath("/");
		 String reportFilePath = root_path + "WEB-INF/reportFormat/jasper/"+medId+".jasper";
		 HashMap<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("SUBREPORT_DIR", root_path + "WEB-INF/reportFormat/jasper/");
		 parameters.put("hospitalName", hospitalInInterService.getPresentHospital().getName());
		 parameters.put("serviceHopital", "住院患者费用清单");
		inPatientArray.add(inPatient);
		try {
			iReportService.doReportToJavaBean(request, WebUtils.getResponse(), reportFilePath, parameters, new JRBeanCollectionDataSource(inPatientArray));
		} catch (Exception e) {
			logger.error("ZYTJ_ZYHZFYQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZFYQD", "住院统计_住院患者费用清单_打印", "2", "0"), e);
			e.printStackTrace();
		}
	}
	/**
	 * @Description:查询住院科室用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	@Action(value = "queryZYDept")
	public void queryZYDept(){
		try{
			List<SysDepartment> sysdeptl=inpatientInfoInInterService.queryDeptMapPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(SysDepartment sd:sysdeptl){
				map.put(sd.getDeptCode(), sd.getDeptName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ZYTJ_ZYHZFYQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZFYQD", "住院统计_住院患者费用清单_打印", "2", "0"), e);
		}
	} 
	/**
	 * @Description:查询结算类别用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	@Action(value = "queryPaykindbill")
	public void queryPaykindbill(){
		try{
			List<BusinessDictionary> csl=innerCodeService.getDictionary("settlement");
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary cs:csl){
				map.put(cs.getEncode(), cs.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_ZYHZFYQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZFYQD", "住院统计_住院患者费用清单_打印", "2", "0"), e);
		}
	}
	/**
	 * @Description:查询用户用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	@Action(value = "queryEmpbill")
	public void queryEmpbill(){
		try{
			List<User> sel=inpatientInfoInInterService.queryUserListPublic();
			Map<String,String> map=new HashMap<String,String>();
			for(User se:sel){
				map.put(se.getAccount(), se.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_ZYHZFYQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZFYQD", "住院统计_住院患者费用清单_打印", "2", "0"), e);
			
		}
	}
	/**
	 * @Description:查询单位用作渲染
	 * @Author：  tcj
	 * @CreateDate： 2016-4-11
	 * @version 1.0
	**/
	@Action(value = "queryCodeSysType")
	public void queryCodeSysType(){
		try{
			List<BusinessDictionary> cstl=innerCodeService.getDictionary("nonmedicineencoding");
			Map<String,String> map=new HashMap<String,String>();
			for(BusinessDictionary cst:cstl){
				map.put(cst.getEncode(), cst.getName());
			}
			String json=JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYTJ_ZYHZFYQD", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYTJ_ZYHZFYQD", "住院统计_住院患者费用清单_打印", "2", "0"), e);
		}
	}
	
}
