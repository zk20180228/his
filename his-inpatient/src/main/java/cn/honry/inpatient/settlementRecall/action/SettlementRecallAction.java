package cn.honry.inpatient.settlementRecall.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.patient.patient.service.PatinentInnerService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inpatient.settlementRecall.service.SettlementRecallService;
import cn.honry.inpatient.settlementRecall.vo.VBalanceHead;
import cn.honry.inpatient.settlementRecall.vo.VSettlementRecall;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName: SettlementRecallAction
 * @Description: 结算召回action
 * @author hedong
 * @date 2015-08-18
 */
@Controller
@Scope("prototype")
@ParentPackage("global")
@InterceptorRefs(value={@InterceptorRef(value = "manageInterceptor")})
@Namespace(value = "/inpatient/settlementRecall")
@SuppressWarnings({"all"})
public class SettlementRecallAction extends ActionSupport implements ModelDriven<VSettlementRecall>{
	private Logger logger=Logger.getLogger(SettlementRecallAction.class);

	@Autowired
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;

	}
	private String menuAlias;
	
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	private VSettlementRecall vSettlementRecall = new VSettlementRecall();
	@Override
	public VSettlementRecall getModel() {
		return vSettlementRecall;
	}
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	private SettlementRecallService settlementRecallService;
	@Qualifier(value = "settlementRecallService")
	public void setSettlementRecallService(
			SettlementRecallService settlementRecallService) {
		this.settlementRecallService = settlementRecallService;
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
	/**
	 * 住院结算明细表
	 */
	private List<InpatientBalanceListNow> inpatientBalanceList;
	/**
	 * 预交金表
	 */
	private List<InpatientInPrepayNow> prepayDetailList;
	/**
	 * 住院主表
	 */
	private List<InpatientInfoNow> InpatientInfoList;
	/**
	 * 患者账户操作表
	 */
	private List<PatientAccountrepaydetail> patientAccountrepaydetailList;
	/**
	 * 住院床位使用记录表实体
	 */
	private List<InpatientBedinfoNow> InpatientBedinfoList;
	/**
	 * 病床信息表
	 */
	private List<BusinessHospitalbed> BusinessHospitalbedList;
	/**
	 * 病房信息表
	 */
	private List<BusinessBedward> BusinessBedwardList;
	/**
	 * 部门科室
	 */
	private List<SysDepartment> SysDepartmentList;
	/**
	 * 合同单位维护表实体类
	 */
	private List<BusinessContractunit> BusinessContractunitList;
	/**
	 * 医院员工表
	 */
	private List<SysEmployee> SysEmployeeList;
	/***
	 * 公共编码资料service实现层
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
    public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
    /***
     * 系统参数service实现层
     */
    @Autowired
    @Qualifier(value = "parameterInnerService")
    private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}

	/**
	 * @Description:进入查询编辑页面
	 * @Author：  hedong
	 * @CreateDate： 2015-08-11
	 * @param
	 * @return void
	 * @version 1.0
	**/
	@RequiresPermissions(value={"JSZH:function:view"})
	@Action(value = "querySettlementRecallInfo", results = { @Result(name = "edit", location = "/WEB-INF/pages/inpatient/settlementRecall/infoEdit.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String querySettlementRecallInfo() {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			String now = simpledateformat.format(calendar.getTime());
			SimpleDateFormat simpledateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now1 = simpledateformat1.format(calendar.getTime());
			ServletActionContext.getRequest().setAttribute("now", now);
			ServletActionContext.getRequest().setAttribute("now1", now1);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
		}
		return "edit";
	}
	/*************************************************2015-08-21begin************************************************************************************/
	/**
	 * 系统参数的判断
	 * @throws Exception
	 */
	@Action(value = "HospitalParameterListxiugai")
	public void HospitalParameterListxiugai() {
		try {
			String value1=parameterInnerService.getparameter("sfyxxgryrq");//是否允许修改入院日期
			if("".equals(value1)){
				value1 = "0";
			}
			String value2=parameterInnerService.getparameter("yjjfpsfsyxdfph");//预交金负票是否使用新的预交金发票号
			if("".equals(value2)){
				value2 = "0";
			}
			String value3=parameterInnerService.getparameter("yjjsfdychfp");//预交金是否打印冲红发票
			if("".equals(value3)){
				value3 = "0";
			}
			String value4=parameterInnerService.getparameter("zhsfdyyjj");//召回是否打印预交金票据
			if("".equals(value4)){
				value4 = "0";
			}
			String value5=parameterInnerService.getparameter("isNew");//冲账产生正交易记录使用的发票号及发票序号
			if("".equals(value5)){
				value5 = "1";
			}
			List list = new ArrayList();
			list.add(value1);
			list.add(value2);
			list.add(value3);
			list.add(value4);
			list.add(value5);
			String json=JSONUtils.toJson(list);
			WebUtils.webSendJSON(json);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
		}
	}
	/**
	 * 查询住院医师
	 * @throws Exception
	 */
	@Action(value = "SysEmployeeList")
	public void SysEmployeeList(){
		String benId = request.getParameter("benId");
		try {
			InpatientBedinfoList=settlementRecallService.getInpatientBedinfo(benId);
			String houseDocCode = InpatientBedinfoList.get(0).getHouseDocCode();
			SysEmployeeList = settlementRecallService.getSysEmployee(houseDocCode);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
		}
		String json=JSONUtils.toJson(SysEmployeeList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询合同单位
	 * @throws Exception
	 */
	@Action(value = "BusinessContractunitList")
	public void BusinessContractunitList() {
		String pactCode = request.getParameter("pactCode");
		try {
			BusinessContractunitList = settlementRecallService.getBusinessContractunit(pactCode);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json=JSONUtils.toJson(BusinessContractunitList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询病床号
	 * @throws Exception
	 */
	@Action(value = "InpatientInfoBusinessHospitalbedList")
	public void InpatientInfoBusinessHospitalbedList(){
		String benId = request.getParameter("benId");
		try {
			InpatientBedinfoList=settlementRecallService.getInpatientBedinfo(benId);
			String bedId = InpatientBedinfoList.get(0).getBedId();
			BusinessHospitalbedList = settlementRecallService.getBusinessHospitalbed(bedId);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json=JSONUtils.toJson(BusinessHospitalbedList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询病区
	 * @throws Exception
	 */
	@Action(value = "BusinessBedwardList")
	public void BusinessBedwardList(){
		String businessBedward = request.getParameter("businessBedward");
		try {
			BusinessBedwardList = settlementRecallService.getBusinessBedward(businessBedward);
			String nursestation = BusinessBedwardList.get(0).getNursestation();
			SysDepartmentList = settlementRecallService.getSysDepartment(nursestation);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
		}
		String json=JSONUtils.toJson(SysDepartmentList);
		WebUtils.webSendJSON(json);
	}
	/**
	 * 查询是否存在患者
	 * @throws Exception
	 */
	@Action(value = "InpatientInfoMedicalrecordIdList")
	public void InpatientInfoMedicalrecordIdList(){
		//前台传来的是就诊卡号，因牵扯较多，未另建变量
		String medicalrecordId=ServletActionContext.getRequest().getParameter("medicalrecordId");
		if(StringUtils.isNotBlank(medicalrecordId)){
			medicalrecordId=medicalrecordId.trim();
		}
		try {
			InpatientInfoList=settlementRecallService.queryInfomedicalrecordId(medicalrecordId);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
			e.printStackTrace();
		}
		String json=JSONUtils.toJson(InpatientInfoList);
		WebUtils.webSendJSON(json);
	}
	
	@Action(value = "initialBalanceList", results = { @Result(name = "json", type = "json") })
	public void initialBalanceList(){
		try {
			inpatientBalanceList = new ArrayList<InpatientBalanceListNow>();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", 0);
			map.put("rows", inpatientBalanceList);
			String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYSF_JSZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), ex);
		}
	}
	/**
	 * 根据住院流水号和结算序号查询预交金
	 * @Description：
	 * @author  dh
	 * @createDate： 2016年4月18日 下午3:45:20 
	 * @modifier dh
	 * @modifyDate：2016年4月18日 下午3:45:20
	 * @param：  
	 * @return：
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Action(value = "intialPrepayDetail", results = { @Result(name = "json", type = "json") })
	public void intialPrepayDetail() throws Exception{
		try{
			String inpatientNo ="";
			Integer balanceNo =0;
			prepayDetailList = new ArrayList<InpatientInPrepayNow>();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", 0);
			map.put("rows", prepayDetailList);
			String json = JSONUtils.toJson(map,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
				logger.error("ZYSF_JSZH", ex);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), ex);
			}
	}
	
	@Action(value = "InpatientBalanceHead")
	public void InpatientBalanceHead(){
		String inpatientNoSearch = request.getParameter("inpatientNoSearch");
		String balanceNoSearch = request.getParameter("balanceNoSearch");
		List<InpatientBalanceHeadNow> balanceHeadList=null;
		try {
			balanceHeadList = settlementRecallService.queryHeadByInpatientNoAndBalanceNo(inpatientNoSearch,balanceNoSearch);
		} catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
		}
		String json=JSONUtils.toJson(balanceHeadList);
		WebUtils.webSendJSON(json);
	}
	/**
	 *根据发票号查询获取第一条头表信息作为结算对象
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "queryHeadByInvoiceNo")
	public void queryHeadByInvoiceNo() throws Exception{
		String invoiceNoSearch = request.getParameter("invoiceNoSearch");
		VBalanceHead vBalanceHead = new VBalanceHead();
		if(StringUtils.isNotBlank(invoiceNoSearch)){
			InpatientBalanceHeadNow headInfo = null;
			String flg="INITIAL_FLG";//初始化标记
			try {
				  headInfo = settlementRecallService.queryHeadByInvoiceNo(invoiceNoSearch);
			} catch (Exception e) {
					logger.error("ZYSF_JSZH", e);
					hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
					   flg="EXCEPTION_FLG";//异常标记
					   System.out.println(e.getMessage());
			}
			List<VBalanceHead> VBalanceHeadList = new ArrayList<VBalanceHead>();//返回前台页面的list
			if(headInfo == null){
				flg = "NULL_FLG";//无数据
			}else{
				flg = "RECORD_FLG";//有记录
				VBalanceHead tmpVBalanceHead = new VBalanceHead();
				tmpVBalanceHead.setId(headInfo.getId());
				tmpVBalanceHead.setInvoiceNo(headInfo.getInvoiceNo());
				tmpVBalanceHead.setReturnCost(headInfo.getReturnCost());
				tmpVBalanceHead.setSupplyCost(headInfo.getSupplyCost());
				tmpVBalanceHead.setBalanceType(headInfo.getBalanceType());
				tmpVBalanceHead.setInpatientNo(headInfo.getInpatientNo());
				tmpVBalanceHead.setBalanceNo(headInfo.getBalanceNo());
				VBalanceHeadList.add(tmpVBalanceHead);
			}
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("flg", flg);
			outmap.put("rows", VBalanceHeadList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		}
	}
	@Action(value = "queryPatientInfoByInpatientNo")
	public void queryPatientInfoByInpatientNo() throws Exception{
		String inpatientNo = request.getParameter("inpatientNoSearch");
		if(StringUtils.isNotBlank(inpatientNo)){
			List<InpatientInfoNow> inpatientInfo = null;
			String flg="INITIAL_FLG";//初始化标记
			try {
				   inpatientInfo = settlementRecallService.queryInfo(inpatientNo);
			} catch (Exception e) {
				logger.error("ZYSF_JSZH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
				   flg="EXCEPTION_FLG";//异常标记
			}
			List<VSettlementRecall> VBalanceHeadList = new ArrayList<VSettlementRecall>();//返回前台页面的list
			if(inpatientInfo == null){
				flg = "NULL_FLG";//无数据
			}else{
				flg = "RECORD_FLG";//有记录
				VSettlementRecall vSettlementRecall = new VSettlementRecall();
				vSettlementRecall.setInPatientInfoId(inpatientInfo.get(0).getId());
				vSettlementRecall.setPatientName(inpatientInfo.get(0).getPatientName());
				vSettlementRecall.setPactCode(inpatientInfo.get(0).getPactCode());
				vSettlementRecall.setDeptCode(inpatientInfo.get(0).getDeptName());
				vSettlementRecall.setNurseCellCode(inpatientInfo.get(0).getNurseCellName());
				vSettlementRecall.setInDate(inpatientInfo.get(0).getInDate()==null?null:new SimpleDateFormat("yyyy-MM-dd").format(inpatientInfo.get(0).getInDate()));
				vSettlementRecall.setHouseDocCode(inpatientInfo.get(0).getHouseDocName());
				vSettlementRecall.setBedId(inpatientInfo.get(0).getBedName());
				vSettlementRecall.setReportBirthday(inpatientInfo.get(0).getReportBirthday()==null?null:new SimpleDateFormat("yyyy-MM-dd").format(inpatientInfo.get(0).getReportBirthday()));
				VBalanceHeadList.add(vSettlementRecall);
			}
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("flg", flg);
			outmap.put("rows", VBalanceHeadList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		}
	}
	/**
	 *通过住院号和结算序号 查询住院结算头表 取得结算头表发票组结算信息
	 */
	@Action(value = "queryHeadByInpatientNoAndBalanceNo")
	public void queryHeadByInpatientNoAndBalanceNo() throws Exception{
		String inpatientNoSearch = request.getParameter("inpatientNoSearch");
		String balanceNoSearch = request.getParameter("balanceNoSearch");
		VBalanceHead vBalanceHead = new VBalanceHead();
		if((StringUtils.isNotBlank(inpatientNoSearch))&&(StringUtils.isNotBlank(balanceNoSearch))){
			List<InpatientBalanceHeadNow> balanceHeadList = null;
			String flg = "";
			try {
				balanceHeadList = settlementRecallService.queryHeadByInpatientNoAndBalanceNo(inpatientNoSearch,balanceNoSearch);
			} catch (Exception e) {
				logger.error("ZYSF_JSZH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
				flg = "0";
			}
			List<VBalanceHead> VBalanceHeadList = new ArrayList<VBalanceHead>();//返回前台页面的list
			if(balanceHeadList==null){
				flg = "1";//List为空
			}else{
				if(balanceHeadList.size()==0){
					flg = "2";//0条记录
				}else if(balanceHeadList.size()==1){
					flg = "3";//1条记录
					InpatientBalanceHeadNow balanceHead = balanceHeadList.get(0);
					VBalanceHead tmpVBalanceHead = new VBalanceHead();
					tmpVBalanceHead.setId(balanceHead.getId());
					tmpVBalanceHead.setInvoiceNo(balanceHead.getInvoiceNo());
					tmpVBalanceHead.setReturnCost(balanceHead.getReturnCost());
					tmpVBalanceHead.setSupplyCost(balanceHead.getSupplyCost());
					tmpVBalanceHead.setBalanceType(balanceHead.getBalanceType());
					tmpVBalanceHead.setInpatientNo(balanceHead.getInpatientNo());
					tmpVBalanceHead.setBalanceNo(balanceHead.getBalanceNo());
					VBalanceHeadList.add(tmpVBalanceHead);
				}else if(balanceHeadList.size()>1){
					flg = "4";//多条记录
					for(int i=0;i<balanceHeadList.size();i++){
						InpatientBalanceHeadNow balanceHead = balanceHeadList.get(i);
						VBalanceHead tmpVBalanceHead = new VBalanceHead();
						tmpVBalanceHead.setId(balanceHead.getId());
						tmpVBalanceHead.setInvoiceNo(balanceHead.getInvoiceNo());
						tmpVBalanceHead.setReturnCost(balanceHead.getReturnCost());
						tmpVBalanceHead.setSupplyCost(balanceHead.getSupplyCost());
						tmpVBalanceHead.setBalanceType(balanceHead.getBalanceType());
						tmpVBalanceHead.setInpatientNo(balanceHead.getInpatientNo());
						tmpVBalanceHead.setBalanceNo(balanceHead.getBalanceNo());
						VBalanceHeadList.add(tmpVBalanceHead);
					}
				}
			}
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("flg", flg);
			outmap.put("rows", VBalanceHeadList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		}
	}
	/**
	 * 根据住院流水号及  结算序号，查询住院结算明细表，获取结算明细
	 * @throws Exception
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "queryBalanceListByInpatientNoAndBalanceNo")
	public void queryBalanceListByInpatientNoAndBalanceNo() throws Exception{
		String inpatientNoSearch = request.getParameter("inpatientNoSearch");
		String balanceNoSearch = request.getParameter("balanceNoSearch");
		if((StringUtils.isNotBlank(inpatientNoSearch))&&(StringUtils.isNotBlank(balanceNoSearch))){
			try {
				inpatientBalanceList = settlementRecallService.getBalanceListInpatientNoAndBalanceNo(inpatientNoSearch,balanceNoSearch);
			} catch (Exception e) {
				logger.error("ZYSF_JSZH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
			}
			String json=JSONUtils.toJson(inpatientBalanceList, "yyyy-MM-dd HH:mm");
			WebUtils.webSendJSON(json);
		}
	}
	/**
	 * 根据结算序号，住院流水号，查询预交金表，获取预交金信息
	 * @throws Exception
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "queryPrepayByInpatientNoAndBalanceNo")
	public void queryPrepayByInpatientNoAndBalanceNo() throws Exception{
		String inpatientNoSearch = request.getParameter("inpatientNoSearch");
		String balanceNoSearch = request.getParameter("balanceNoSearch");
		if((StringUtils.isNotBlank(inpatientNoSearch))&&(StringUtils.isNotBlank(balanceNoSearch))){
			try {
				prepayDetailList = settlementRecallService.getPrepayByInpatientNoAndBalanceNo(inpatientNoSearch,balanceNoSearch);
			} catch (Exception e) {
				logger.error("ZYSF_JSZH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
			}
			String json=JSONUtils.toJson(prepayDetailList, "yyyy-MM-dd HH:mm");
			WebUtils.webSendJSON(json);
		}
	}
	
	/**
	 * 根据结算序号，住院流水号，查询账户明细表，获取预交金信息
	 * @throws Exception
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "queryPrepayByIdBalanceNo", results = { @Result(name = "json", type = "json") })
	public void queryPrepayByIdBalanceNo() throws Exception{
		try {
			String id = request.getParameter("id");
			String balanceNoSearch = request.getParameter("balanceNoSearch");
			patientAccountrepaydetailList=settlementRecallService.getPatientAccountrepaydetail(id, balanceNoSearch);
			String json = JSONUtils.toJson(patientAccountrepaydetailList,DateUtils.DATETIME_FORMAT);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYSF_JSZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), ex);
		}
	}
	/**
	 * 根据员工id查询员工名称
	 * @throws Exception
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "querySysempName")
	public void querySysempName(){
		try{
			SysEmployeeList=settlementRecallService.getSysEmpName();
			String json=JSONUtils.toExposeJson(SysEmployeeList, false, null, "name","id");
			WebUtils.webSendJSON(json);
		}catch(Exception ex){
			logger.error("ZYSF_JSZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), ex);
		}
	}
	/**
	 * 根据userid查询员工名称
	 * @throws Exception
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "getUseridEmpName")
	public void getUseridEmpName() {
		try{
			List<User> userList = new ArrayList<User>();
			userList=settlementRecallService.getUseridEmpName();
			String json=JSONUtils.toExposeJson(userList, false, null, "name","id");
			WebUtils.webSendJSON(json);
		}catch(Exception ex){
			logger.error("ZYSF_JSZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), ex);
		}
	}
	/**
	 * 渲染统计大类
	 * @throws Exception
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "queryCodeCasminfee")
	public void queryCodeCasminfee() {
		try{
			List<BusinessDictionary> CodeCasminfeeList = innerCodeService.getDictionary("casminfee");
			String json=JSONUtils.toExposeJson(CodeCasminfeeList, false, null, "name","encode");
			WebUtils.webSendJSON(json);
		}catch(Exception ex){
			logger.error("ZYSF_JSZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), ex);
		}
	}
	/**
	 * 按住院号线查询  根据住院号查询结算投标 获取为作废的发票号  List 结算对象
	 * @throws Exception
	 */
	@RequiresPermissions(value={"JSZH:function:query"})
	@Action(value = "queryHeadByInpatientNo")
	public void queryHeadByInpatientNo(){
		String inpatientNo = request.getParameter("inpatientNoSearch");
		VBalanceHead vBalanceHead = new VBalanceHead();
		if(StringUtils.isNotBlank(inpatientNo)){
			List<InpatientBalanceHeadNow> balanceHeadList = null;
			String flg = "";
			try {
				balanceHeadList = settlementRecallService.queryBalanceHead(inpatientNo);//根据查询条件获取结算主表信息
			} catch (Exception e) {
				flg="1";//异常
				logger.error("ZYSF_JSZH", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
			}
			List<VBalanceHead> VBalanceHeadList = new ArrayList<VBalanceHead>();//返回前台页面的list
			if(balanceHeadList==null){
				flg="2";//空
			}else{
				if(balanceHeadList.size()==1){
					flg = "3";//1条记录
					InpatientBalanceHeadNow balanceHead = balanceHeadList.get(0);
					VBalanceHead tmpVBalanceHead = new VBalanceHead();
					tmpVBalanceHead.setId(balanceHead.getId());
					tmpVBalanceHead.setInvoiceNo(balanceHead.getInvoiceNo());
					tmpVBalanceHead.setReturnCost(balanceHead.getReturnCost());
					tmpVBalanceHead.setSupplyCost(balanceHead.getSupplyCost());
					tmpVBalanceHead.setBalanceType(balanceHead.getBalanceType());
					tmpVBalanceHead.setInpatientNo(balanceHead.getInpatientNo());
					tmpVBalanceHead.setBalanceNo(balanceHead.getBalanceNo());
					VBalanceHeadList.add(tmpVBalanceHead);
				}else if(balanceHeadList.size()>1){
					flg = "4";//多条记录
					for(int i=0;i<balanceHeadList.size();i++){
						InpatientBalanceHeadNow balanceHead = balanceHeadList.get(i);
						VBalanceHead tmpVBalanceHead = new VBalanceHead();
						tmpVBalanceHead.setId(balanceHead.getId());
						tmpVBalanceHead.setInvoiceNo(balanceHead.getInvoiceNo());
						tmpVBalanceHead.setReturnCost(balanceHead.getReturnCost());
						tmpVBalanceHead.setSupplyCost(balanceHead.getSupplyCost());
						tmpVBalanceHead.setBalanceType(balanceHead.getBalanceType());
						tmpVBalanceHead.setInpatientNo(balanceHead.getInpatientNo());
						tmpVBalanceHead.setBalanceNo(balanceHead.getBalanceNo());
						VBalanceHeadList.add(tmpVBalanceHead);
					}
				}else if(balanceHeadList.size()==0){
					flg = "5";//0条记录
				}
			}
			Map<String, Object> outmap=new HashMap<String, Object>();
			outmap.put("flg", flg);
			outmap.put("rows", VBalanceHeadList);
			String json=JSONUtils.toJson(outmap);
			WebUtils.webSendJSON(json);
		}
	}
	/**
	 * @Description:进入查询编辑页面
	 * @Author：  hedong
	 * @CreateDate： 2015-08-11
	 * @param    
	 * @return void  
	 * @version 1.0
	**/
	@Action(value = "invoiceNoWin", results = { @Result(name = "invoiceNoWin", location = "/WEB-INF/pages/inpatient/settlementRecall/invoiceNoWin.jsp") }, interceptorRefs = { @InterceptorRef(value = "manageInterceptor") })
	public String invoiceNoWin() {
		request.setAttribute("invoiceNos", request.getParameter("invoiceNos"));
		return "invoiceNoWin";
	}
	@Action(value = "queryInvoiceNo", results = { @Result(name = "json", type = "json") })
	public void queryInvoiceNo() {
		try {
			String invoiceNos = request.getParameter("invoiceNos");
			String[] invoiceNoArr = invoiceNos.split("_");
			List<InpatientBalanceHead> balanceHeadList = new ArrayList();
			for(int i=0;i<invoiceNoArr.length;i++){
				InpatientBalanceHead balanceHead = new InpatientBalanceHead();
				balanceHead.setId(String.valueOf(i));
				balanceHead.setInvoiceNo(invoiceNoArr[i]);
				balanceHeadList.add(balanceHead);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", 0);
			map.put("rows", balanceHeadList);
			String json = JSONUtils.toJson(map);
			WebUtils.webSendString(json);
		}catch (Exception ex) {
			logger.error("ZYSF_JSZH", ex);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), ex);
		}
	}
	/*************************************************2015-08-21end************************************************************************************/
	@Action(value = "balanceRecall", results = { @Result(name = "json", type = "json") })
	public void balanceRecall() throws Exception{
		try {
			String invoiceNo = request.getParameter("invoiceNo");//发票号
			String balanceNo = request.getParameter("balanceNo");//结算序号
			String inpatientNo = request.getParameter("inpatientNo");//结算序号
			String payObjId = request.getParameter("payObjId");//结算对象id
			String balanceHeadIds = request.getParameter("invoiceArrIds");//结算对象id
			String balanceListIds = request.getParameter("balanceListIds");//结算明细id
			String prePayIds = request.getParameter("prePayIds");//预交金表id
			String patientInfoId = request.getParameter("patientInfoId");//患者住院主表id
		
			String msg =settlementRecallService.balanceRecall(invoiceNo,balanceNo,inpatientNo,balanceHeadIds,prePayIds,payObjId,balanceListIds,patientInfoId);
			WebUtils.webSendString(msg);
		}catch (Exception e) {
			logger.error("ZYSF_JSZH", e);
			hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYSF_JSZH", "住院收费_结算召回", "2", "0"), e);
		}
	}
	
}
