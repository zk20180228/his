package cn.honry.inpatient.amobileApply.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.system.parameter.service.ParameterInnerService;
import cn.honry.inpatient.amobileApply.Service.DocManageServiceApp;
import cn.honry.inpatient.amobileApply.Service.OutBalanceMobileService;
import cn.honry.inpatient.apply.Service.DrugApplyService;
import cn.honry.inpatient.inprePay.service.InprePayService;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.WebUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@ParentPackage("global") 
@InterceptorRefs(value = { @InterceptorRef(value = "manageInterceptor") })
@Namespace(value = "/outBalanceMobileAction")
public class OutBalanceMobileAction extends ActionSupport{
	private static final long serialVersionUID = 8983900270459236957L;
	
	private Logger logger=Logger.getLogger(ReturnPremium.class);
	private HttpServletRequest request = ServletActionContext.getRequest();
	@Autowired
	@Qualifier(value="outBalanceMobileService")
	private OutBalanceMobileService outBalanceMobileService;
	
	public OutBalanceMobileService getOutBalanceMobileService() {
		return outBalanceMobileService;
	}
	@Qualifier(value = "hiasExceptionService")
	private HIASExceptionService hiasExceptionService;
	public void setHiasExceptionService(HIASExceptionService hiasExceptionService) {
	this.hiasExceptionService = hiasExceptionService;
	}
	@Autowired
	@Qualifier(value = "inprePayService")
	private InprePayService inprePayService;
	public void setInprePayService(InprePayService inprePayService) {
		this.inprePayService = inprePayService;
	}
	@Autowired
	@Qualifier(value="drugApplyService")
	private DrugApplyService drugApplyService;
	public void setDrugApplyService(DrugApplyService drugApplyService) {
		this.drugApplyService = drugApplyService;
	}
	/***
	 * 注入系统参数的service
	 * 调用接口
	 */
	@Autowired
	@Qualifier(value = "parameterInnerService")
	private ParameterInnerService parameterInnerService;
	public void setParameterInnerService(ParameterInnerService parameterInnerService) {
		this.parameterInnerService = parameterInnerService;
	}
	@Autowired
	@Qualifier(value = "docManageServiceApp")
	private DocManageServiceApp docManageServiceApp;	
	public void setDocManageServiceApp(DocManageServiceApp docManageServiceApp) {
		this.docManageServiceApp = docManageServiceApp;
	}
	@Autowired
	@Qualifier(value="businessStockInfoInInterService")
	private BusinessStockInfoInInterService  businessStockInfoInInterService;
	public void setBusinessStockInfoInInterService(
			BusinessStockInfoInInterService businessStockInfoInInterService) {
		this.businessStockInfoInInterService = businessStockInfoInInterService;
	}
	//病历号
		private String medicalrecordId;
		/*申请区药品json*/
		private String drugJson;
		/*申请区非药品json*/
		private String notDrugJson;
		/*id集合*/
		private String ids;
		/*登录科室*/
		private String deptCode;
		/*登录人员*/
		private String empJobNo;
		private String empJobName;
		private String deptName;
		
		/**
		 * 发票号
		 */
		private String invoiceNo;
		/**
		 * 病历号
		 */
		private String medicalrecordIdSearch;
		/**
		 * 现在时间
		 */
		private String now;
		/**
		 * 住院流水号
		 */
		private String inpatientNo; 
		/**
		 * 入院时间
		 */
		private String inDate;
		/**
		 * 结算时间
		 */
		private String outDate;
		/**
		 * 预交金
		 */
		private String yj;
		/**
		 * 汇总信息的自费金额合计 
		 */
		private String zfMoney;
		/**
		 * 结算序号
		 */
		private String balanceNo;
		/**
		 * 减免金额
		 */
		private String jmMoney;
		/**
		 * 应收
		 */
		private String sh;
		/**
		 * 补收
		 */
		private String sh1;
		/**
		 * 住院实付Json
		 */
		private String zfJson;
		/**
		 * 预交金列表ids
		 */
		private String prepayIds;
		/**
		 * 费用列表Json
		 */
		private String costJson;

		public String getMedicalrecordId() {
			return medicalrecordId;
		}
		public void setMedicalrecordId(String medicalrecordId) {
			this.medicalrecordId = medicalrecordId;
		}
		public String getDrugJson() {
			return drugJson;
		}
		public void setDrugJson(String drugJson) {
			this.drugJson = drugJson;
		}
		public String getNotDrugJson() {
			return notDrugJson;
		}
		public void setNotDrugJson(String notDrugJson) {
			this.notDrugJson = notDrugJson;
		}
		public String getIds() {
			return ids;
		}
		public void setIds(String ids) {
			this.ids = ids;
		}
		public String getDeptCode() {
			return deptCode;
		}
		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}
		public String getEmpJobNo() {
			return empJobNo;
		}
		public void setEmpJobNo(String empJobNo) {
			this.empJobNo = empJobNo;
		}
		public String getEmpJobName() {
			return empJobName;
		}
		public void setEmpJobName(String empJobName) {
			this.empJobName = empJobName;
		}
		public String getDeptName() {
			return deptName;
		}
		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}
		public String getInvoiceNo() {
			return invoiceNo;
		}
		public void setInvoiceNo(String invoiceNo) {
			this.invoiceNo = invoiceNo;
		}
		public String getMedicalrecordIdSearch() {
			return medicalrecordIdSearch;
		}
		public void setMedicalrecordIdSearch(String medicalrecordIdSearch) {
			this.medicalrecordIdSearch = medicalrecordIdSearch;
		}
		public String getNow() {
			return now;
		}
		public void setNow(String now) {
			this.now = now;
		}
		public String getInpatientNo() {
			return inpatientNo;
		}
		public void setInpatientNo(String inpatientNo) {
			this.inpatientNo = inpatientNo;
		}
		public String getInDate() {
			return inDate;
		}
		public void setInDate(String inDate) {
			this.inDate = inDate;
		}
		public String getOutDate() {
			return outDate;
		}
		public void setOutDate(String outDate) {
			this.outDate = outDate;
		}
		public String getYj() {
			return yj;
		}
		public void setYj(String yj) {
			this.yj = yj;
		}
		public String getZfMoney() {
			return zfMoney;
		}
		public void setZfMoney(String zfMoney) {
			this.zfMoney = zfMoney;
		}
		public String getBalanceNo() {
			return balanceNo;
		}
		public void setBalanceNo(String balanceNo) {
			this.balanceNo = balanceNo;
		}
		public String getJmMoney() {
			return jmMoney;
		}
		public void setJmMoney(String jmMoney) {
			this.jmMoney = jmMoney;
		}
		public String getSh() {
			return sh;
		}
		public void setSh(String sh) {
			this.sh = sh;
		}
		public String getSh1() {
			return sh1;
		}
		public void setSh1(String sh1) {
			this.sh1 = sh1;
		}
		public String getZfJson() {
			return zfJson;
		}
		public void setZfJson(String zfJson) {
			this.zfJson = zfJson;
		}
		public String getPrepayIds() {
			return prepayIds;
		}
		public void setPrepayIds(String prepayIds) {
			this.prepayIds = prepayIds;
		}
		public String getCostJson() {
			return costJson;
		}
		public void setCostJson(String costJson) {
			this.costJson = costJson;
		}
		public void setOutBalanceMobileService(
				OutBalanceMobileService outBalanceMobileService) {
			this.outBalanceMobileService = outBalanceMobileService;
		}
		/**  
		 *  
		 * @Description：结算保存
		 * @Author：dh
		 * @ModifyRmk： 
		 * @version 1.0
		 *
		 */
		@Action(value = "saveBalance")
		public void saveBalance() throws Exception {
			Map<String,String> parameterMap=new HashMap<String,String>();
			parameterMap.put("yj", yj);//预交金
			parameterMap.put("zfMoney", zfMoney);//汇总信息的自费金额合计 
			parameterMap.put("invoiceNo", invoiceNo);//发票号
			parameterMap.put("medicalrecordId", medicalrecordIdSearch);//病历号
			parameterMap.put("inpatientNo", inpatientNo);//住院流水号
			parameterMap.put("balanceNo", balanceNo);//结算序号
			parameterMap.put("jmMoney", jmMoney);//减免金额
			parameterMap.put("sh", sh);//应收
			parameterMap.put("sh1", sh1);//补收
			parameterMap.put("outDate", outDate);//结算时间
			parameterMap.put("inDate", inDate);//住院时间
			parameterMap.put("prepayIds", prepayIds);//预交金列表ids
			String retVal="no";
			if("发票已用完请重新领取!".equals(invoiceNo)){
				retVal = "发票已用完请重新领取!";
				WebUtils.webSendString(retVal);
			}else{
				try {
					retVal = outBalanceMobileService.saveBalance(parameterMap,zfJson,costJson,empJobNo,deptCode,empJobName,deptName);
				} catch (Exception e) {
					retVal = e.getLocalizedMessage();
				}
				WebUtils.webSendString(retVal);
			}
		}
		
		/**  
		 *  
		 * @Description：中途结算保存
		 * @Author：dh
		 * @ModifyRmk： 
		 * @version 1.0
		 *
		 */
		@Action(value = "saveBalanceZhongtu")
		public void saveBalanceZhongtu() throws Exception {
			Map<String,String> parameterMap=new HashMap<String,String>();
			parameterMap.put("yj", yj);//预交金
			parameterMap.put("zfMoney", zfMoney);//汇总信息的自费金额合计 
			parameterMap.put("invoiceNo", invoiceNo);//发票号
			parameterMap.put("medicalrecordId", medicalrecordIdSearch);//病历号
			parameterMap.put("inpatientNo", inpatientNo);//住院流水号
			parameterMap.put("balanceNo", balanceNo);//结算序号
			parameterMap.put("jmMoney", jmMoney);//减免金额
			parameterMap.put("sh", sh);//应收
			parameterMap.put("sh1", sh1);//补收
			parameterMap.put("outDate", outDate);//结算时间
			parameterMap.put("inDate", inDate);//住院时间
			parameterMap.put("prepayIds", prepayIds);//预交金列表ids
			String retVal="no";
			if("发票已用完请重新领取!".equals(invoiceNo)){
				retVal = "发票已用完请重新领取!";
				WebUtils.webSendString(retVal);
			}else{
				try {
					retVal = outBalanceMobileService.saveBalanceZhongtu(parameterMap,zfJson,costJson,empJobNo,deptCode,empJobName,deptName);
				} catch (Exception e) {
					retVal = e.getLocalizedMessage();
				}
				WebUtils.webSendString(retVal);
			}
		}
		
		/**  
		 *  
		 * @Description： 保存医嘱列表新增行数据
		 * @Author：yeguanqun
		 * @CreateDate：2015-12-29 14:00 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		@Action(value = "saveInpatientOrder")
		public void saveInpatientOrder() throws Exception {
			Map<String,Object> map=new HashMap<String,Object>();
			InpatientOrderNow inpatientOrder = new InpatientOrderNow();
			PrintWriter out = WebUtils.getResponse().getWriter();
			request.setCharacterEncoding("gb2312");
			String str = request.getParameter("str");
			
			
			String inpatientNo = request.getParameter("inpatientNo");
			String patientNo = request.getParameter("patientNo");
			String deptCode = request.getParameter("deptCode");		
			String nurseCellCode = request.getParameter("nurseCellCode");
			String babyFlag = request.getParameter("babyFlag");
			String decmpsState = request.getParameter("decmpsState");
			int happenNo=0;
			if("1".equals(babyFlag)){
				happenNo=0;
			}
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateCascade.FACTORY).create();
			try{
				JSONArray jsonArray = JSONArray.fromObject(str);
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObj = JSONObject.fromObject(jsonArray.getString(i));
					String itemType = jsonObj.getString("itemType");
					if("1".equals(itemType)){
						String itemCode = jsonObj.getString("itemCode");
						String itemName = jsonObj.getString("itemName");
						String pharmacyCode = jsonObj.getString("pharmacyCode");//扣库科室
						String typeCode = jsonObj.getString("typeCode");//医嘱类型
						String usageCode = jsonObj.getString("usageCode");//用法
						String classCode = jsonObj.getString("classCode");
						String empJobNo = jsonObj.getString("empJobNo");
						if ("16".equals(classCode)||"17".equals(classCode)||"18".equals(classCode)) {
							usageCode = "10";
						}
						String result = docManageServiceApp.dispensingSf(itemCode,pharmacyCode,typeCode,usageCode);
						if("1".equals(result)){
							map.put("resMsg", "error");
							map.put("resCode", itemName+"已不存在!");
							break;
						}else if("success".equals(result)){
						}else{
							map.put("resMsg", "error");
							map.put("resCode", itemName+result);
							break;
						}
						double qtyTot = 0;
						if(StringUtils.isNotBlank(jsonObj.getString("qtyTot"))){
							qtyTot = Double.parseDouble(jsonObj.getString("qtyTot"));//总量
						}
						//判断药品状态是否可用及库存是否充足
						List<String> drugCodes= new ArrayList<>();//申请药品编码list
						drugCodes.add(itemCode);
						List<Double> applyNums= new ArrayList<>();//申请数量list
						applyNums.add(qtyTot);
						
						List<Integer> showFlags=new ArrayList<>();//申请数量list
						showFlags.add(1);
						Map<String, Object> valiuDrugMap = businessStockInfoInInterService.ynValiuDrug(pharmacyCode, drugCodes, applyNums, showFlags, false, true, true);
						String flag = valiuDrugMap.get("valiuFlag").toString();
						if("0".equals(flag)){//库存不足
							map.put("resMsg", "error");
							map.put("resCode", valiuDrugMap.get("failMesgs").toString());
						}
					}
				}
				if(map.size()>0 &&"error".equals(map.get("resMsg"))){				
					out.write(gson.toJson(map));
				}else{
					inpatientOrder.setInpatientNo(inpatientNo);//住院号
					inpatientOrder.setPatientNo(patientNo);//住院病历号
					inpatientOrder.setDeptCode(deptCode);
					inpatientOrder.setNurseCellCode(nurseCellCode);//住院护理站代码 	
					inpatientOrder.setBabyFlag(Integer.valueOf(babyFlag));//婴儿标记
					inpatientOrder.setHappenNo(happenNo);//婴儿序号
					inpatientOrder.setDecmpsState(Integer.parseInt(decmpsState));
					inpatientOrder.setHospitalId(HisParameters.CURRENTHOSPITALID);
					inpatientOrder.setAreaCode(inprePayService.getDeptArea(deptCode));
					map = docManageServiceApp.saveOrUpdateInpatientOrder(inpatientOrder,str,empJobNo,deptCode);
					out.write(gson.toJson(map));
					out.close();
				}
			}catch(Exception e){
				logger.error("ZYYSZ_YZGL", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYSZ_YZGL", "住院医生站_医嘱管理(新)", "2", "0"), e);		
			}
		}
		
		/**  
		 * @Description：摆药过程
		 * @Author：ldl
		 * @CreateDate：2016-05-04
		 * @ModifyRmk：  
		 * @version 1.0
		 */
		@Action(value = "approvalDrugSave")
		public void approvalDrugSave() {
			try{
				String applyNumbers=request.getParameter("applyNumbers");
				
				String parameterHz = parameterInnerService.getparameter("isNeedApproval");
				if("".equals(parameterHz)){
					parameterHz = "1";
				}
				Map<String,String> map = outBalanceMobileService.approvalDrugSave(applyNumbers,parameterHz,empJobNo,deptCode);
				String mapJosn = JSONUtils.toJson(map);
				WebUtils.webSendJSON(mapJosn);			
			}catch(Exception e){
				WebUtils.webSendJSON(JSONUtils.toJson(new HashMap<String,Object>(){{
					put("resCode","审批失败");
					put("resMeg","error");
				}}));
				logger.error("ZYYFGL_BYDHZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			}
		}
		
		
		@Action(value = "examineDrugSaveAndUpdate")
		public void examineDrugSaveAndUpdate() {
			try{
				String applyNumbers=request.getParameter("applyNumbers");
				Map<String,String> map = outBalanceMobileService.examineDrugSaveAndUpdate(applyNumbers,ids,empJobNo,deptCode);
				String mapJosn = JSONUtils.toJson(map);
				WebUtils.webSendJSON(mapJosn);			
			}catch(Exception e){
				WebUtils.webSendJSON(JSONUtils.toJson(new HashMap<String,Object>(){{
					put("resCode","核准失败");
					put("resMeg","error");
				}}));
				logger.error("ZYYFGL_BYDHZ", e);
				hiasExceptionService.saveExceptionInfoToMongo(new RecordToHIASException("ZYYFGL_BYDHZ", "住院药房管理_摆药单核准", "2", "0"), e);
			}
		}
}
