package cn.honry.inpatient.info.action;

import java.io.IOException;
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

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientPrepayin;
import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.inpatient.consultation.service.ConsultationInInterService;
import cn.honry.inner.inpatient.info.service.InpatientInfoInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inpatient.account.service.InpatientAccountService;
import cn.honry.inpatient.info.service.InpatientBedInfoService;
import cn.honry.inpatient.info.service.InpatientInfoService;
import cn.honry.inpatient.info.vo.InfoVo;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ClassName: InpatientInfoAction 
 * @Description: 住院登记表action
 * @author zl
 * @date 2015-12-3
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/info")
@SuppressWarnings({"all"})
public class InpatientInfoAction extends ActionSupport {
	
	private Logger logger=Logger.getLogger(InpatientInfoAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	//就诊卡号或身份证号
	private String idcardOrRe;
	public String getIdcardOrRe() {
		return idcardOrRe;
	}
	public void setIdcardOrRe(String idcardOrRe) {
		this.idcardOrRe = idcardOrRe;
	}
	private String now;
	public String getNow() {
		return now;
	}
	public void setNow(String now) {
		this.now = now;
	}

	InpatientInfoNow inpatientInfo = new InpatientInfoNow();
	
	public InpatientInfoNow getInpatientInfo() {
		return inpatientInfo;
	}
	public void setInpatientInfo(InpatientInfoNow inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}

	InpatientCancelitem inpatientCancelitem=new InpatientCancelitem();
	
	
	private String menuAlias;
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	private List<InpatientInfoNow> inpatientInfoList;
	private HttpServletRequest request = ServletActionContext.getRequest();
	/**
	 * 起始页数
	 */
	private String page;
	
	/**
	 * 数据列数
	 */
	private String rows;
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}


	/**
	 * 预交金实体类
	 */
	private InpatientInPrepayNow inpatientInPrepay;
	/**
	 * 担保金实体类
	 */
	private InpatientSurety inpatientSurety;
	/**
	 * 当前登录员工
	 * @return
	 */
	private String empJobno;
	/**
	 * 病历号
	 */
	public String medinfoId;
	/**
	 * 身份证号
	 */
	/**
	 * 判断是否领取发票号
	 */
	private String InvoiceNoflag;
	/**
	 * 发票号
	 */
	private String invoiceUserNo;
	private String cerno;
	private String idcardNo;
	private String bedName;
	private String bedLevel;
	private String bedState;
	
	public String getInvoiceUserNo() {
		return invoiceUserNo;
	}
	public void setInvoiceUserNo(String invoiceUserNo) {
		this.invoiceUserNo = invoiceUserNo;
	}
	public String getInvoiceNoflag() {
		return InvoiceNoflag;
	}
	public void setInvoiceNoflag(String invoiceNoflag) {
		InvoiceNoflag = invoiceNoflag;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getBedLevel() {
		return bedLevel;
	}
	public void setBedLevel(String bedLevel) {
		this.bedLevel = bedLevel;
	}
	public String getBedState() {
		return bedState;
	}
	public void setBedState(String bedState) {
		this.bedState = bedState;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getCerno() {
		return cerno;
	}
	public void setCerno(String cerno) {
		this.cerno = cerno;
	}
	public String getMedinfoId() {
		return medinfoId;
	}
	public void setMedinfoId(String medinfoId) {
		this.medinfoId = medinfoId;
	}
	public String getEmpJobno() {
		return empJobno;
	}
	public void setEmpJobno(String empJobno) {
		this.empJobno = empJobno;
	}
	public InpatientSurety getInpatientSurety() {
		return inpatientSurety;
	}
	public void setInpatientSurety(InpatientSurety inpatientSurety) {
		this.inpatientSurety = inpatientSurety;
	}
	public InpatientInPrepayNow getInpatientInPrepay() {
		return inpatientInPrepay;
	}
	public void setInpatientInPrepay(InpatientInPrepayNow inpatientInPrepay) {
		this.inpatientInPrepay = inpatientInPrepay;
	}
	/**
	 * 注入住院预交金Service
	 */
	@Autowired 
	@Qualifier(value = "inprePayService")
	private InprePayService inprePayService;
	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
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
    
    private PatinentInnerService patinentInnerService;
	@Autowired 
	@Qualifier(value = "patinentInnerService")
	public void setPatinentInnerService(PatinentInnerService patinentInnerService) {
		this.patinentInnerService = patinentInnerService;
	}
	
	private InpatientInfoService inpatientInfoService;
	@Autowired 
	@Qualifier(value = "inpatientInfoService")
	public void setInpatientInfoService(InpatientInfoService inpatientInfoService) {
		this.inpatientInfoService = inpatientInfoService;
	}
	/**
	 * 注入会诊service
	 */
	private ConsultationInInterService consultationInInterService;
	@Autowired
	@Qualifier(value = "consultationInInterService")
	public void setConsultationInInterService(ConsultationInInterService consultationInInterService) {
		this.consultationInInterService = consultationInInterService;
	}
	private InpatientBedInfoService inpatientBedInfoService;
	@Autowired
	@Qualifier(value = "inpatientBedInfoService")
	public void setInpatientBedInfoService(
			InpatientBedInfoService inpatientBedInfoService) {
		this.inpatientBedInfoService = inpatientBedInfoService;
	}

	private InpatientAccountService inpatientAccountService;
	@Autowired
	@Qualifier(value = "inpatientAccountService")
	public void setInpatientAccountService(InpatientAccountService inpatientAccountService) {
		this.inpatientAccountService = inpatientAccountService;
	}
	private ParameterInnerService parameterInnerService;
	@Autowired
	@Qualifier(value = "parameterInnerService")
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	private InpatientInfoInInterService inpatientInfoInInterService;
	@Autowired
	@Qualifier(value = "inpatientInfoInInterService")
	public void setInpatientInfoInInterService(
			InpatientInfoInInterService inpatientInfoInInterService) {
		this.inpatientInfoInInterService = inpatientInfoInInterService;
	}
	private String bedid;
	
	
	public String getBedid() {
		return bedid;
	}
	public void setBedid(String bedid) {
		this.bedid = bedid;
	}
	/**
	 * @Description:获取list页面
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYDJXXGL:function:view"}) 
	@Action(value="inpatientInfoList",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/info/infoList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String inpatientInfoList(){
		Date date = new Date();
		now = DateUtils.formatDateY_M_D_H_M_S(date);
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysEmployee sysemployee=consultationInInterService.queryById(user.getId());
		empJobno=sysemployee.getJobNo();
		Map<String,String> map=new HashMap<String,String>();
		
		//查询发票（根据发票类型，和领取人（获得的员工Id））
		map = inprePayService.queryFinanceInvoiceNo(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),"04");
		invoiceUserNo=map.get("resCode");
		InvoiceNoflag=map.get("resMsg");
		return "list";
	}
	
	/**  
	 * @Description：跳转至床位窗口页面
	 * @Author：tcj
	 * @CreateDate：2015-12-30下午14：:08
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "bedInfoList", results = { @Result(name = "bedInfoList", location = "/WEB-INF/pages/inpatient/info/bedList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String bedInfoList() {
		String noId=ServletActionContext.getRequest().getParameter("reportBedwardId");
		ServletActionContext.getRequest().setAttribute("noId", noId);
		return "bedInfoList";
	}
	
	/**
	 * @Description:查询列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYDJXXGL:function:query"}) 
	@Action(value = "queryInpatientInfo", results = { @Result(name = "json", type = "json") })
	public void queryInpatientInfo(){
		try {
			String inpatientNo=request.getParameter("inpatientNo");
			
			//条件查询
			InpatientInfoNow inpatientInfoSerch = new InpatientInfoNow();
			if(StringUtils.isNotBlank(inpatientNo)){
				inpatientInfoSerch.setMedicalrecordId(inpatientNo);
			}
			inpatientInfoList = inpatientInfoService.getPage(page,rows,inpatientInfoSerch);
			int total = inpatientInfoService.getTotal(inpatientInfoSerch);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", inpatientInfoList);
			String json=JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	
	/**
	 * 根据患者余额查询患者警戒线
	 * @throws Exception
	 */
	@Action(value = "queryInpatientInfos", results = { @Result(name = "json", type = "json") })
	public void queryInpatientInfos(){
		try {
			InpatientInfoNow info = new InpatientInfoNow();
			inpatientInfoList = inpatientInfoService.getInfoByMoney(info, rows, page);
			int total = inpatientInfoService.getTotals(info);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", inpatientInfoList);
			String json=JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	
	
	/**
	 * @Description:添加&修改
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	**/
	@Action(value = "editInpatientInfo",interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public void editInpatientInfo() throws Exception {
		try{
			inpatientInfoService.editInpatientInfo(inpatientInfo,inpatientInPrepay,inpatientSurety);			
		}catch(Exception e){
			WebUtils.webSendJSON("error");
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
		WebUtils.webSendString("success");
	}
	
	
	/**
	 * @Description:删除
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYDJXXGL:function:delete"}) 
	@Action(value = "delInpatientInfo", results = { @Result(name = "list", location = "/WEB-INF/pages/inpatient/info/infoList.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String delInpatientInfo() throws Exception {
		try{
			inpatientInfoService.removeUnused(inpatientInfo.getId());
			WebUtils.webSendString("success");
		}catch(Exception e){
			WebUtils.webSendString("error");
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
		return "list";
	}
	
	/**
	 * @Description:浏览信息
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @return String  
	 * @version 1.0
	 **/
	@Action(value = "viewInpatientInfo", results = { @Result(name = "view", location = "/WEB-INF/pages/inpatient/info/infoView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewInpatientInfo()throws Exception {
		request.setAttribute("inpatientInfo", inpatientInfoService.get(inpatientInfo.getId()));
		return "view";
	}
	
	/**
	 * @Description:添加页面
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @param @throws Exception   
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYDJXXGL:function:add"}) 
	@Action(value = "addInpatientInfo", results = { @Result(name = "add", location = "/WEB-INF/pages/inpatient/info/infoEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String addInpatientInfo() throws Exception {
		request.setAttribute("inpatientInfo", new InpatientInfo());
		return "add";
	}
	
	/**
	 * @Description:编辑页面
	 * @Author：  lt
	 * @CreateDate： 2015-6-24
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"ZYDJXXGL:function:edit"}) 
	@Action(value = "editInitInpatientInfo", results = { @Result(name = "edit", location = "/WEB-INF/pages/inpatient/info/infoEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInitInpatientInfo()throws Exception {
		request.setAttribute("inpatientInfo",inpatientInfoService.get(inpatientInfo.getId()));
		return "edit";
	}
	
	/**
	 * @Description:根据病历号获得住院登记信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "getInPatientInfoByMedical",results={@Result(name="json",type="json")})
	public void getInPatientInfoByMedical(){
		try {
			String medicalNo = ServletActionContext.getRequest().getParameter("medicalNo");
			InpatientInfoNow inpatientInfo = inpatientInfoService.queryByMedical(medicalNo);
			String json = JSONUtils.toJson(inpatientInfo);
			WebUtils.webSendJSON(json);
		}catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}	
	
	/**  
	 * @Description：  病区（下拉框）
	 * @Author：tcj
	 * @CreateDate：2015-12-30 上午9:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Action(value = "querydeptCombobox",results={@Result(name="json",type="json")})
	public void querydeptCombobox(){
		try {
			String id=ServletActionContext.getRequest().getParameter("deptId");
			List<SysDepartment> departmentList = inpatientInfoService.querydeptCombobox(id);
			String json = JSONUtils.toJson(departmentList);
			WebUtils.webSendJSON(json);
		}catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);			
		}
	}
	
	/**
	 * @Description:获取床号 做显示列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-29
	 * @return String  
	 * @version 1.0
	**/
	@Action(value = "likeBedinfo",results={@Result(name="json",type="json")})
	public void likeBedinfo(){
		try {
			List<InpatientBedinfoNow> bedInfoList = inpatientBedInfoService.list();
			String json=JSONUtils.toJson(bedInfoList);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	
	@Action(value = "likeBedinfos",results={@Result(name="json",type="json")})
	public void likeBedinfos() {
		try {
			List<BusinessHospitalbed> bedInfoLists = inpatientBedInfoService.bedlist();
			String json = JSONUtils.toJson(bedInfoLists);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	@Action(value = "likeDepinfo",results={@Result(name="json",type="json")})
	public void likeDepinfo() {
		try {
			List<DepartmentContact> depInfoLists = inpatientBedInfoService.deplist();
			String json = JSONUtils.toJson(depInfoLists);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	@Action(value = "likeRegcon",results={@Result(name="json",type="json")})
	public void likeRegcon() {
		try {
			List<BusinessContractunit> regconLists = inpatientBedInfoService.reglist();
			String json = JSONUtils.toJson(regconLists);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	@Action(value = "likeHousedoc",results={@Result(name="json",type="json")})
	public void likeHousedoc() {
		try {
			List<SysEmployee> housedocLists = inpatientBedInfoService.houseDoclist(bedid);
			String json = JSONUtils.toJson(housedocLists);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	/**
	 * @Description:获取list页面
	 * @Author：  wujiao
	 * @CreateDate： 2015-8-20
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"JJX:function:view"}) 
	@Action(value="listalertAll",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/info/alert.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String listalertAll()throws Exception{
		return "list";
	}
	
	
	/**
	 * @Description:查询列表
	 * @Author：  wujiao
	 * @CreateDate： 2015-8-20
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "queryAllInpatientInfoAlert", results = { @Result(name = "json", type = "json") })
	public void queryAllInpatientInfoAlert(){
		try {
			inpatientInfoList = inpatientInfoService.getPage(request.getParameter("page"),request.getParameter("rows"),inpatientInfo);
			int total = inpatientInfoService.getTotal(inpatientInfo);
			for (InpatientInfoNow info : inpatientInfoList) {
				if(info.getPrepayCost()>=info.getMoneyAlert()){
					info.setRemarkalert(1);
				}
			}
			String json = JSONUtils.toJson(inpatientInfoList,DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	
	/**
	 * @Description:编辑页面
	 * @Author：  wujiao
	 * @CreateDate： 2015-8-21
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"JJX:function:edit"}) 
	@Action(value = "editInitInpatientAlert", results = { @Result(name = "editInitInpatientAlert", location = "/WEB-INF/pages/inpatient/info/alertEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String editInitInpatientAlert(){
		request.setAttribute("inpatientInfo", inpatientInfoService.get(inpatientInfo.getId()));
		//获得住院状态
		request.setAttribute("inpatientInfo", inpatientInfoService.get(inpatientInfo.getInState()));
		//获得开院证明状态
		return "editInitInpatientAlert";
	}
	/**
	 * @Description:浏览信息
	 * @Author：  wujiao
	 * @CreateDate： 2015-8-21
	 * @return String  
	 * @version 1.0
	**/
	@RequiresPermissions(value={"JJX:function:view"}) 
	@Action(value = "viewInpatientAlert", results = { @Result(name = "viewInpatientAlert", location = "/WEB-INF/pages/inpatient/info/alertView.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String viewInpatientAlert() {
		request.setAttribute("inpatientInfo", inpatientInfoService.get(inpatientInfo.getId()));
		return "viewInpatientAlert";
	}
	
	
	/**  
	 *  
	 * @Description：  查询患者树
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryPatientTree", results = { @Result(name = "json", type = "json") })
	public void queryPatientTree() {
		try {
			String treeJsonList = inpatientInfoService.queryPatientTree();
			WebUtils.webSendString(treeJsonList);
		}catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	
	/**  
	 *  
	 * @Description：  
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-25 下午05:40:46  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-25 下午05:40:46  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value="leaveHospitalList",results={@Result(name="list",location = "/WEB-INF/pages/inpatient/info/leaveHospitaList.jsp")}, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String leaveHospitalList (){
		Date newdate=new Date();
		String endTime = DateUtils.formatDateY_M_D_H_M_S(newdate);
		ServletActionContext.getRequest().setAttribute("newDate", endTime);
		return "list";
	}
	
	/**  
	 *  
	 * @Description：  出院登记
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-18 下午05:37:12  
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-8-18 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	@RequiresPermissions(value={"CYDJ:function:view"})
	@Action(value = "queryInpinfo")
	public void queryInpinfo() throws IOException {
		try{
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String type = request.getParameter("type");
			if(StringUtils.isNotBlank(type)){
				type=type.trim();
			}
			//通过接口查询就诊卡号对应的病历号
			type = patinentInnerService.getMedicalrecordId(type);
			String no = request.getParameter("no");
			if(StringUtils.isNotBlank(no)){
				no=no.trim();
			}
			
			List<InpatientInfoNow> info = inpatientInfoService.queryInpinfo(type,no);
			String json = JSONUtils.toJson(info, "yyyy-MM-dd");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**  
	 *  
	 * @Description：  出院登记
	 * @Author：donghe
	 * @CreateDate：2017-3-24  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	@RequiresPermissions(value={"CYDJ:function:view"})
	@Action(value = "queryInpinfoM")
	public void queryInpinfoM() throws IOException {
		//前台传来的是就诊卡号，因牵扯较多，未另建变量
		String type = request.getParameter("type");
		if(StringUtils.isNotBlank(type)){
			type=type.trim();
			}
		String no = request.getParameter("no");
		List<InpatientInfoNow> info = null;
		try {
		if(StringUtils.isNotBlank(no)){
			no=no.trim();
			}
		 info = inpatientInfoService.queryInpinfo(type,no);
		} catch (Exception e) {
			WebUtils.webSendJSON("error");
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);		
		}
		String json = JSONUtils.toJson(info, "yyyy-MM-dd");
		WebUtils.webSendJSON(json);
	}
	/**  
	 *  
	 * @Description：  查看患者退费申请表
	 * @Author：zhangjin
	 * @CreateDate：2015-8-25 下午05:40:46  
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-04 下午07:40:46  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "ajaxCanceliem")
	public void ajaxCanceliem(){
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			List<InpatientCancelitemNow> info = inpatientInfoService.ajaxCanceliem(inpatientInfo.getInpatientNo());
			if(info==null){
				map.put("resMsg", "1");
			}else{
				String val = parameterInnerService.getparameter("cztfsqsnfjxcy");//患者存在未确认的退费申请时，是否可以继续出院
				if("".equals(val)){
					val = "0";
				}
				if("0".equals(val)){//不可以出院0
					map.put("resMsg", "0");
				}else{//可以出院1
					map.put("resMsg", "2");
					map.put("info", info);
				}
			}
			String json = JSONUtils.toJson(map);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**  
	 *  
	 * @Description：   获取患者未摆药记录
	 * @Author：donghe
	 * @CreateDate：2017-3-3
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryDrugApplyoutNowList")
	public void queryDrugApplyoutNowList(){
		try{
			List<DrugApplyoutNow> info = inpatientInfoService.getDrugApplyoutNowList(inpatientInfo.getInpatientNo());
			String json = JSONUtils.toJson(info);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**  
	 *  
	 * @Description：   获取患者未执行未作废的医嘱
	 * @Author：donghe
	 * @CreateDate：2017-3-3
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "queryInpatientOrderNowList")
	public void queryInpatientOrderNowList(){
		try{
			List<InpatientOrderNow> orderList = inpatientInfoService.getInpatientOrderNowList(inpatientInfo.getInpatientNo());
			String json = JSONUtils.toJson(orderList);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**  
	 *  
	 * @Description：  保存出院登记
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-25 下午05:40:46  
	 * @Modifier：zhangjin
	 * @ModifyDate：2015-12-04 下午07:40:46  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Action(value = "saveInpinfoOut")
	public void saveInpinfoOut(){
		String result = "";
		try {
			inpatientInfoService.inpatientIdGet(inpatientInfo);
			result = "success";
		} catch (Exception e) {
			result = "error";
			WebUtils.webSendJSON("error");
			logger.error("ZYGL_CYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_CYDJ", "住院管理_出院登记", "2", "0"), e);
		}
		WebUtils.webSendString(result);
	}
	
	/**  
	 *  
	 * @Description：  查询结算类别
	 * @Author：
	 * @CreateDate：2015-12——7 
	 * @ModifyRmk：
	 * @param: 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CYDJ:function:view"})
	@Action(value = "ajaxPaykindCode", results = { @Result(name = "json", type = "json") })
	public void ajaxPaykindCode() {
		String id = ServletActionContext.getRequest().getParameter("id");
	}
	
	/**  
	 *  
	 * @Description：  查询床号        
	 * @Author：
	 * @CreateDate：2015-12—7 
	 * @ModifyRmk：
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午7:21:45  
	 * @param: 
	 * @version 1.0
	 * @throws IOException 
	 */
	@Action(value = "ajaxBedId", results = { @Result(name = "json", type = "json") })
	public void ajaxBedId() throws IOException {
		try{
			String id = ServletActionContext.getRequest().getParameter("id");
			InpatientBedinfoNow bedinfo=inpatientInfoService.queryBedId(id);   //病床使用记录表
			String bedId=bedinfo.getBedId();      //拿到病床使用记录表中的主键
			InpatientInfoNow  info = inpatientInfoService.ajaxBedId(bedId);   //查询病床编码表
			String json = JSONUtils.toJson(info);
			WebUtils.webSendString(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
		
	}
	/**  
	 * @Description：  根据患者编号查询RegisterInfo信息
	 * @Author：zhenglin
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@RequiresPermissions(value={"CYDJ:function:save"})
	@Action(value = "getIdByPatient", results = { @Result(name = "json", type = "json") })
	public void getIdByPatient(){
		try {
			String id=ServletActionContext.getRequest().getParameter("paNo");
			RegisterInfo registerInfo=inpatientInfoService.getInfoByePatientNO(id);
			String json = JSONUtils.toJson(registerInfo);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	
	/**
	 * 
	 * @Description： 查询病人信息
	 * @Author：zhenglin
	 * @CreateDate：2015-12-09 上午10:56:35
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-09 上午10:56:35
	 * @ModifyRmk：修改：通过病历号进行查询
	 * @version 1.0
	 */
	@Action(value = "queryProofList", results = { @Result(name = "json", type = "json") })
	public void queryProofList() {
		try{
			String id = request.getParameter("c_no");
			InfoVo infoVo = inpatientInfoService.getProof(id,idcardNo);
			String json=JSONUtils.toJson(infoVo,"yyyy-MM-dd");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	
	/**  
	 *  
	 * @Description：  根据患者的住院状态查询.
	 * @Author：zhenglin
	 * @CreateDate：2015-12-15 下午05:37:12  
	 * @Modifier：zhenglin
	 * @ModifyDate：2015-12-15 下午05:37:12  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@RequiresPermissions(value={"CYDJ:function:save"})
	@Action(value = "queryPatientByInState", results = { @Result(name = "json", type = "json") })
	public void queryPatientByInState(){
		try {
			String state=request.getParameter("pstate");
			List<InpatientInfoNow> info=inpatientInfoService.queryPatientByInState(state);
			String json=JSONUtils.toJson(info);
			WebUtils.webSendString(json);
		} catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	} 
	
	/**  
	 * @Description：  查询病房树
	 * @Author：tcj
	 * @CreateDate：2015-12-30  上午15:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPatientRoom", results = { @Result(name = "json", type = "json") })
	public void queryPatientRoom(){
		try {
			String noId=ServletActionContext.getRequest().getParameter("noId");
			List<TreeJson> patientRoomTree =inpatientInfoService.queryPatientRoom(noId);
			String json =JSONUtils.toJson(patientRoomTree);
			WebUtils.webSendString(json);
		}catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	/**  
	 * @Description：  点击病房树查询病床
	 * @Author：tcj
	 * @CreateDate：2015-12-30  上午18:06
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryPatientRoomBed", results = { @Result(name = "json", type = "json") })
	public void queryPatientRoomBed(){
		try {
			String noId=ServletActionContext.getRequest().getParameter("roomId");
			String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
			BusinessHospitalbed bedEntity=new BusinessHospitalbed();
			bedEntity.setBedName(bedName);
			bedEntity.setBedLevel(bedLevel);
			List<BusinessHospitalbed> businessHospitalbedList =inpatientInfoService.queryPatientRoomBed(noId,page,rows,bedEntity);
			int total = inpatientInfoService.getTotal(bedEntity,noId);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", businessHospitalbedList);
			String json =JSONUtils.toJson(map);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYGL_ZYYY", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), ex);	
		}
	}
	/**  
	 * @Description：  查询患者的住院信息（登记、接诊，以及相应的时间）
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午16:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryMedicalrecordIdDate", results = { @Result(name = "json", type = "json") })
	public void queryMedicalrecordIdDate(){
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			String medicalrecordId =ServletActionContext.getRequest().getParameter("medicalrecordId");
			String result =inpatientInfoService.queryMedicalrecordIdDateByMid(medicalrecordId);
			if("R".equals(result)){
				map.put("resMsg", "R");
				map.put("resCode", "该患者已经办理住院登记，不得重复办理");
			}else if("I".equals(result)){
				map.put("resMsg", "I");
				map.put("resCode", "患者正在住院，无需办理住院登记");
			}else if("B".equals(result)){
				map.put("resMsg", "B");
				map.put("resCode", "患者已办理出院登记，无法办理住院登记");
			}else if("P".equals(result)){
				map.put("resMsg", "P");
				map.put("resCode", "患者已预约出院，无法办理住院登记");
			}else if("none".equals(result)){
				//查询患者是否有预约住院信息
				List<InpatientPrepayin> inpyl=inpatientInfoService.queryPrepayinInfo(medicalrecordId);
				if(inpyl.size()>0){
					map.put("resMsg", "have");
					Integer a=patinentInnerService.querySumByMedicalreId(medicalrecordId);
					int num=1;
					if(a!=null){
						num = a.intValue()+1;
					}
					String number=null;
					if(num<10){
						number="0"+num;
					}else{
						number=num+"";
					}
					map.put("innum", number);
					map.put("resCode", inpyl.get(0));
					List<Patient> pl =inpatientInfoService.getIdcardInfo(medicalrecordId);
					if(pl!=null&&pl.size()>0){
						map.put("patient", pl.get(0));
					}
					
				}else{
					//患者既没有住院信息，又没有预约信息
					map.put("resMsg", "none");
				}
			}
				String json = JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
				WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYGL_ZYYY", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), ex);	
		}
	}
	/**  
	 * @Description：  查询住院总次数
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryCount")
	public void queryCount(){
		String medicalrecordId=request.getParameter("medicalrecordId");
		Integer a = patinentInnerService.querySumByMedicalreId(medicalrecordId);
		int num = 1;
		if(a!=null){
			num =a.intValue()+1;
		}
		String json=JSONUtils.toJson(num);
		WebUtils.webSendJSON(json);
	}
	/**  
	 * @Description：  查询是否有 有效的住院证明
	 * @Author：tcj
	 * @CreateDate：2016-1-5  下午18:40
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "queryProofInfo", results = { @Result(name = "json", type = "json") })
	public void queryProofInfo(){
		try {
			String certificatesNo =ServletActionContext.getRequest().getParameter("certificatesNo");
			String result= inpatientInfoService.queryProofInfo(certificatesNo);
			Map<String,String> map=new HashMap<String,String>();
			if("yes".equals(result)){
				map.put("resMsg", "yes");
				map.put("resCode", "yes");
			}else if("no".equals(result)){
				map.put("resMsg", "no");
				map.put("resCode", "请先办理住院证明");
			}
			PrintWriter out = WebUtils.getResponse().getWriter();
			String json = JSONUtils.toJson(map,DateUtils.DATE_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception e) {
			logger.error("ZYGL_ZYDJ", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYDJ", "住院管理_住院登记", "2", "0"), e);
		}
	}
	/**
	 * 根据病历号查出多条住院信息数据
	 * @author  lyy
	 * @createDate： 2016年3月21日 下午1:01:16 
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午1:01:16  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getMedById")
	public void getMedById() {
		try{
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId = request.getParameter("medicalrecordId");
			//通过接口查询就诊卡号对应的病历号
			medicalrecordId = patinentInnerService.getMedicalrecordId(medicalrecordId);
			List<InpatientInfoNow> infoVo = inpatientInfoService.getQueryInfo(medicalrecordId);
			for (InpatientInfoNow inpatientInfo : infoVo) {
				inpatientInfo.setRemark(inpatientInfo.getReportAge()+inpatientInfo.getReportAgeunit());
			}
			String json=JSONUtils.toJson(infoVo);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 根据病历号查出多条住院信息数据
	 * @author  lyy
	 * @createDate： 2016年3月21日 下午1:01:16 
	 * @modifier lyy
	 * @modifyDate：2016年3月21日 下午1:01:16  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "getdengjiInfo")
	public void getdengjiInfo() {
		try{
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId = request.getParameter("medicalrecordId");
			if(StringUtils.isNotBlank(medicalrecordId)){
				medicalrecordId=medicalrecordId.trim();
			}
			//通过接口查询就诊卡号对应的病历号
			List<InpatientProof> infoVo = inpatientInfoService.getdengjiInfo(medicalrecordId);
			for (InpatientProof inpatientProof : infoVo) {
				if(StringUtils.isBlank(inpatientProof.getReportAgeunit())){
					inpatientProof.setReportRemark(inpatientProof.getReportAge().toString());
				}else{
					inpatientProof.setReportRemark(inpatientProof.getReportAge()+inpatientProof.getReportAgeunit());
				}
			}
			String json=JSONUtils.toJson(infoVo);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 根据就诊卡号查患者信息（住院来源为给门诊和急诊）
	 */
	@Action(value = "getPatientInfo")
	public void getPatientInfo() {
		try{
			//前台传来的是就诊卡号，因牵扯较多，未另建变量
			String medicalrecordId = request.getParameter("medicalrecordId");
			List<Patient> infoVo = inpatientInfoService.getPatientInfo(medicalrecordId);
			String json=JSONUtils.toJson(infoVo);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 查询支付方式
	 */
	@Action(value = "getPaykind")
	public void getPaykind() {
		try{
			String unit = request.getParameter("unit");
			String paykind = inpatientInfoService.getPaykind(unit);
			String json=JSONUtils.toJson(paykind);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 查询住院科室下拉框
	 * @author  tcj
	 * @version 1.0
	 */
	@Action(value = "zyDeptCombobox", results = { @Result(name = "json", type = "json") })
	public void zyDeptCombobox() {
		try {
			List<SysDepartment> sysdeptl = inpatientInfoService.zyDeptCombobox();
			String json = JSONUtils.toJson(sysdeptl);
			WebUtils.webSendString(json);
		} catch (Exception ex) {
			logger.error("ZYGL_ZYYY", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), ex);	
		}
	}
	/**
	 * 查询开据医生下拉框
	 * @author  tcj
	 * @version 1.0
	 */
	@Action(value = "querykaijudocDj")
	public void querykaijudocDj() {
		try{
			List<SysEmployee> sysdeptl = inpatientInfoService.querykaijudocDj();
			String json=JSONUtils.toExposeJson(sysdeptl, false, null, "id","name");
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
		
	}
	/**
	 * 通过病历号查询患者信息
	 * @author  tcj
	 * @version 1.0
	 */
	@Action(value = "getIdcardInfo")
	public void getIdcardInfo() {
		try{
			List<Patient> patient = inpatientInfoService.getIdcardInfo(medinfoId);
			String json=JSONUtils.toJson(patient.get(0));
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 通过病历号查询患者信息
	 * @author  tcj
	 * @version 1.0
	 */
	@Action(value = "getPatientInfoByCerNo")
	public void getPatientInfoByCerNo() {
		try{
			List<Patient> patient = inpatientInfoService.getPatientInfoByCerNo(cerno);
			String json=JSONUtils.toJson(patient);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 入院途径
	 * @version 1.0
	 */
	@Action(value = "getbrlydqo")
	public void getbrlydqo() {
		try{
			List<BusinessDictionary> list = innerCodeService.getDictionary("brlydq");
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 入院来源
	 * @version 1.0
	 */
	@Action(value = "getsource")
	public void getsource() {
		try{
			List<BusinessDictionary> list = innerCodeService.getDictionary("source");
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
	/**
	 * 根据就诊卡号或者身份证号查询
	 * @version 1.0
	 */
	@Action(value = "queryMedicalrecordId")
	public void queryMedicalrecordId() {
		try{
			String medicalrecordId = inpatientInfoInInterService.queryMedicalrecordId(idcardOrRe);
			if(medicalrecordId==null){
				medicalrecordId="";
			}
			WebUtils.webSendString(medicalrecordId);
		}catch(Exception e){
			logger.error("ZYGL_ZYYY", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYGL_ZYYY", "住院管理_住院预约", "2", "0"), e);	
		}
	}
}
