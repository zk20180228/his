package cn.honry.inner.outpatient.medicineList.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoRecipeNow;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.TecApply;
import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.drug.drugInfo.dao.DrugInfoInInerDAO;
import cn.honry.inner.drug.sendWicket.dao.SendWicketInInterDAO;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.outpatient.medicineList.NumberToChinese;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.outpatient.medicineList.service.MedicineListInInterService;
import cn.honry.inner.outpatient.medicineList.vo.ChargeVo;
import cn.honry.inner.outpatient.medicineList.vo.DrugOrUNDrugVo;
import cn.honry.inner.outpatient.medicineList.vo.ReportEntiry;
import cn.honry.inner.outpatient.medicineList.vo.SpeNalVo;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.inner.technical.tecapply.dao.TecApplyInInInterDAO;
import cn.honry.inner.technical.terminalApply.dao.TerminalApplyInInterDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.Gson;
@Service("medicineListInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class MedicineListServiceInInterImpl implements MedicineListInInterService{
	Logger logger = Logger.getLogger(MedicineListServiceInInterImpl.class);
	@Override
	public OutpatientFeedetail get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(OutpatientFeedetail arg0) {
	}
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	@Autowired
	@Qualifier(value = "matOutPutInInterService")
	private MatOutPutInInterService matOutPutInInterService;
	@Autowired
	@Qualifier(value = "businessStockInfoInInterService")
	private BusinessStockInfoInInterService businessStockInfoInInterService;
	@Autowired
	@Qualifier(value = "keyvalueInInterDAO")
	private KeyvalueInInterDAO keyvalueInInterDAO;//参数表
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value="terminalApplyInInterDAO")
	private TerminalApplyInInterDAO terminalApplyInInterDAO;
	@Autowired
	@Qualifier(value="tecApplyInInInterDAO")
	private TecApplyInInInterDAO tecApplyInInInterDAO;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "drugInfoInInerDAO")
	private DrugInfoInInerDAO drugInfoInInerDAO;
	@Autowired
	@Qualifier(value = "undrugInInterDAO")
	private UndrugInInterDAO undrugInInterDAO;
	@Autowired
	@Qualifier(value = "sendWicketInInterDAO")
	private SendWicketInInterDAO sendWicketInInterDAO;
	
	/**  
	 * 
	 * <p>门诊收费（新） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月3日 下午3:45:40 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月3日 下午3:45:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Map<String, String> chargeImplement(ChargeVo chargeVo,String feeWhenUnenougth) {
		//初始化map
		RegistrationNow registration = null;
		String receipeNOs = "";
		Map<String, String> map  = new HashMap<String, String>();
		User user2 = (User) SessionUtils.getCurrentUserFromShiroSession();
		SysDepartment department = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		
		Gson gson = new Gson();
		//挂号信息//患者信息
		RegistrationNow registerInfo = medicineListInInterDAO.queryInfoByNo(chargeVo.getClinicCode());
		//当前登陆科室名称
		SysDepartment dept = (SysDepartment)SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptName = dept.getDeptName();
		//当前登陆科室ID
		String deptId = dept.getDeptCode();
		//当前登陆用户
//			User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		String userId = user2.getAccount();
		//根据用户ID查询员工ID  
		SysEmployee employee = medicineListInInterDAO.queryEmployee(userId);
		
		//初始化物资所用map
		Map<String, Object> matMap  = new HashMap<String, Object>();
		//初始化患者账户
		OutpatientAccount account = new OutpatientAccount();
		//查询出所有的医技科室
		List<SysDepartment> departmentList = medicineListInInterDAO.chargeImplementDepartmentList();
		//发票序号 
		String invoiceSeq = ""; 
		//一次发票序号
		String invoiceComb = medicineListInInterDAO.getSeqByName("SEQ_INVOICE_COMB");
		//或得处方号
//			String recipeNo = medicineListInInterDAO.getSeqByName("SEQ_ADVICE_RECIPENO"); 
//			//医嘱流水号
//			String moOrder = medicineListInInterDAO.getSeqByName("SEQ_ADVICE_STREAMNO"); 
		//收费序列
		String recipeSeq = "";
		//查询已经存在的收费序列
//			OutpatientFeedetail patientFeedetail = medicineListInInterDAO.queryNOByclinicCode(chargeVo.getClinicCode());
//			if(patientFeedetail.getRecipeSeq()==null||"".equals(patientFeedetail.getRecipeSeq())){
//				recipeSeq = medicineListInInterDAO.getSeqByName("SEQ_ADVICE_CHARGESEQ"); 
//			}else{
//				recipeSeq = patientFeedetail.getRecipeSeq();
//			}
//			if(patientFeedetail.getInvoiceSeq()!=null&&!"".equals(patientFeedetail.getInvoiceSeq())){
//				invoiceSeq =patientFeedetail.getInvoiceSeq();
//			}else{
//				invoiceSeq = medicineListInInterDAO.getSeqByName("SEQ_INVOICE_SEQ");//查询序列得出序号
//			}
		
		List<OutpatientFeedetailNow> feedetailList = medicineListInInterDAO.findByIds(chargeVo.getDoctorId());
		//判断合通单位是否更改
		if(StringUtils.isNotBlank(chargeVo.getPink())&&StringUtils.isNotBlank(chargeVo.getNewconts())){
			if(!chargeVo.getPink().equals(chargeVo.getNewconts())){
				//进行合同单位换算
				feedetailList = this.getchangeByconts(feedetailList, null, chargeVo.getNewconts());
			}
		}
		//判断药品库存是否充足
		if(feedetailList.size()>0){
			List<String> drugCodes = new ArrayList<String>();
			List<Double> applyNums = new ArrayList<Double>();
			List<Integer> showFlags = new ArrayList<Integer>();
			String deptCode = feedetailList.get(0).getExecDpcd();
			for(OutpatientFeedetailNow drugList :feedetailList){
				if("1".equals(drugList.getDrugFlag())){
					drugCodes.add(drugList.getItemCode());
					applyNums.add(drugList.getQty());
					showFlags.add(drugList.getExtFlag3());
					Map<String, Object> valiuDrug = businessStockInfoInInterService.ynValiuDrug(drugList.getExecDpcd(), drugCodes, applyNums, showFlags, true, true, false);
					if(0==(Integer)valiuDrug.get("valiuFlag")){
						map.put("resMsg", "error");
						map.put("resCode",(String)map.get("resCode")+valiuDrug.get("failMesgs"));
					}
					drugCodes.clear();
					applyNums.clear();
					showFlags.clear();
				}
			}
		}
		//判断非药品库存是否充足、非药品是否停用、物资出库申请
		if(feedetailList.size()>0){
			for(OutpatientFeedetailNow undrugList :feedetailList){
				if(!"1".equals(undrugList.getDrugFlag())){
					//判断是否停用
					DrugUndrug undrug = undrugInInterDAO.getCode(undrugList.getItemCode());
					if(undrug==null||1==undrug.getStop_flg()){
						map.put("resMsg", "error");
						map.put("resCode", "对不起，["+undrug.getName()+"]已停用!");
						return map;
					}
					OutputInInterVO vo = new OutputInInterVO();
					vo.setStorageCode(undrugList.getExecDpcd());//执行科室
					vo.setInpatientNo(undrugList.getClinicCode());//门诊号
					vo.setUndrugItemCode(undrugList.getItemCode());//非药品ID
					vo.setRecipeNo(undrugList.getRecipeNo());//处方号
					vo.setSequenceNo(undrugList.getSequenceNo());//处方内流水号
					vo.setApplyNum(undrugList.getConfirmNum());//确认数量
					vo.setUseNum(0D);//执行数量
					vo.setTransType(1);//交易类型
					vo.setFlag("I");//门诊
					vo.setOutListCode(undrugList.getSampleId());//物资出库申请流水号取医嘱生成的申请流水号
					//物资出库申请
					//3-非物资; 2-未找到交易记录(反交易时才有2); 1-库存不足; 0-出库成功(出库时的返回值);0- 退库成功(反交易时的返回值) 出库时 2 -此物资出库其他条件不满足
					matMap = matOutPutInInterService.addRecord(vo);
					if("0".equals(matMap.get("resCode"))){//出库成功，更新出库申请流水号
						undrugList.setUpdateSequenceno(Long.valueOf(matMap.get("billNo").toString()));
					}
					if("1".equals(matMap.get("resCode").toString())){//库存不足
						map.put("resMsg", "error");
						map.put("resCode", "对不起，["+undrugList.getItemName()+"]不足，请查看库房");
						return map;
					}
					if("2".equals(matMap.get("resCode").toString())){
						matMap.put("resMsg", "error");
						matMap.put("resCode", "["+undrugList.getItemName()+"]出库时其他条件不满足！请核对后再进行收费");
						return map;
					}
				}
			}
		}
		//判断账户余额是否充足
		if(chargeVo.getHospitalAccount()!=null&&chargeVo.getHospitalAccount()!=0){
			account = medicineListInInterDAO.getAccountByMedicalrecord(registerInfo.getMidicalrecordId());
			if(account.getId()==null){
				map.put("resMsg", "error");
				map.put("resCode", "该病历号无账户信息,请联系管理员!");
				return map;
			}else if(account.getAccountState()==0){//停用
				map.put("resMsg", "error");
				map.put("resCode", "账户已停用,请联系管理员!");
				return map;
			}else if(account.getAccountState()==2){//注销3结清4冻结
				map.put("resMsg", "error");
				map.put("resCode", "账户已注销!");
				return map;
			}else if(account.getAccountState()==1&&chargeVo.getHospitalAccount()>account.getAccountBalance()){//总金额大于剩余的门诊金额无法结账
				map.put("resMsg", "error");
				map.put("resCode", "账户剩余金额["+account.getAccountBalance()+"],请充值缴费后结算!");
				return map;
			}else if(account.getAccountDaylimit()!=0){//当当日消费额限不等于0时
				List<OutpatientAccountrecord> accountrecord = medicineListInInterDAO.queryAccountrecord(registerInfo.getMidicalrecordId());
				Double sum = 0.0;//已使用金额
				if(accountrecord.size()>0){
					for(OutpatientAccountrecord modls : accountrecord){//遍历符合条件的信息
						if(modls.getMoney() != null){
							sum = sum + modls.getMoney();
						}
					}
					if(account.getAccountDaylimit()<chargeVo.getHospitalAccount()-sum){
						BigDecimal big = new BigDecimal(account.getAccountDaylimit()+sum);
						map.put("resMsg", "error");
						map.put("resCode", "账户已经超当日额限!当日还可以缴费["+(big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())+"]");
						return map;
					}
				}
			}
		}
		//根据门诊号进行分类		
		Map<String ,List<OutpatientFeedetailNow>>  clicnicCodeInvoiceTypeMap = new HashMap<String ,List<OutpatientFeedetailNow>>();
		//发票序号map key为cliniccode value为发票序号
		Map<String ,String>  invoiceSeqMap = new HashMap<String ,String>();
		//门诊号-发票号
//		Map<String ,String>  invoiceNoMap = new HashMap<String ,String>();
		String invoiceType = parameterInnerDAO.getParameterByCode("invoiceFno");//获取分发票的类型
		//根据处方号进行分类
		Map<String ,List<OutpatientFeedetailNow>>  recipeNoMap = new HashMap<String ,List<OutpatientFeedetailNow>>();
		//配药台Map
		Map<String,List<StoTerminal>> stoMap = new HashMap<String,List<StoTerminal>>();
		List<StoRecipeNow> stoRecipeList = new ArrayList<StoRecipeNow>();
		//Map<处方号，发药窗口>
		Map<String,String> recipeWinMap = new HashMap<String, String>();
		for (OutpatientFeedetailNow fee : feedetailList) {
			if("1".equals(invoiceType)){//统计大类分发票
				MinfeeStatCode feeStatCode = this.getFeeStatCode(fee);//获取统计大类code
				if(clicnicCodeInvoiceTypeMap.containsKey(fee.getClinicCode()+"_"+feeStatCode.getFeeStatCode())){//判断是否包含指定键的映射关系，包含为true
					List<OutpatientFeedetailNow> list2 = clicnicCodeInvoiceTypeMap.get(fee.getClinicCode()+"_"+feeStatCode.getFeeStatCode());
					list2.add(fee);
					clicnicCodeInvoiceTypeMap.put(fee.getClinicCode()+"_"+feeStatCode.getFeeStatCode(), list2);
				}else{
					List<OutpatientFeedetailNow> list = new ArrayList<OutpatientFeedetailNow>();
					list.add(fee);
					clicnicCodeInvoiceTypeMap.put(fee.getClinicCode()+"_"+feeStatCode.getFeeStatCode(), list);
				}
			}
			if("2".equals(invoiceType)){//执行科室分发票
				if(clicnicCodeInvoiceTypeMap.containsKey(fee.getClinicCode()+"_"+fee.getExecDpcd())){
					List<OutpatientFeedetailNow> list2 = clicnicCodeInvoiceTypeMap.get(fee.getClinicCode()+"_"+fee.getExecDpcd());
					list2.add(fee);
					clicnicCodeInvoiceTypeMap.put(fee.getClinicCode()+"_"+fee.getExecDpcd(), list2);
				}else{
					List<OutpatientFeedetailNow> list = new ArrayList<OutpatientFeedetailNow>();
					list.add(fee);
					clicnicCodeInvoiceTypeMap.put(fee.getClinicCode()+"_"+fee.getExecDpcd(), list);
				}
			}
			if("3".equals(invoiceType)){//处方号分发票
				if(clicnicCodeInvoiceTypeMap.containsKey(fee.getClinicCode()+"_"+fee.getRecipeNo())){
					List<OutpatientFeedetailNow> list2 = clicnicCodeInvoiceTypeMap.get(fee.getClinicCode()+"_"+fee.getRecipeNo());
					list2.add(fee);
					clicnicCodeInvoiceTypeMap.put(fee.getClinicCode()+"_"+fee.getRecipeNo(), list2);
				}else{
					List<OutpatientFeedetailNow> list = new ArrayList<OutpatientFeedetailNow>();
					list.add(fee);
					clicnicCodeInvoiceTypeMap.put(fee.getClinicCode()+"_"+fee.getRecipeNo(), list);
				}
			}
			
			if(!invoiceSeqMap.containsKey(fee.getClinicCode())){
				if(StringUtils.isNotBlank(fee.getInvoiceSeq())){
					invoiceSeq =fee.getInvoiceSeq();
				}else{
					invoiceSeq = medicineListInInterDAO.getSeqByName("SEQ_INVOICE_SEQ");//查询序列得出序号
				}
				invoiceSeqMap.put(fee.getClinicCode(), invoiceSeq);
			}
			if(recipeNoMap.containsKey(fee.getRecipeNo())){
				List<OutpatientFeedetailNow> list = recipeNoMap.get(fee.getRecipeNo());
				list.add(fee);
				recipeNoMap.put(fee.getRecipeNo(), list);
			}else{
				List<OutpatientFeedetailNow> list = new ArrayList<OutpatientFeedetailNow>();
				list.add(fee);
				recipeNoMap.put(fee.getRecipeNo(), list);
			}
		}
		//摆药单-调剂信息处理（生成处方调剂头表）
		if(recipeNoMap!=null&&recipeNoMap.size()>0){
			for(Map.Entry<String ,List<OutpatientFeedetailNow>> recipNoMap : recipeNoMap.entrySet()){
				String recipeN = recipNoMap.getKey();
				if(StringUtils.isNotBlank(receipeNOs)){
					receipeNOs += "','";
				}
				receipeNOs += recipeN;
				List<OutpatientFeedetailNow> feedetail = recipNoMap.getValue();
				registration = medicineListInInterDAO.queryInfoByNo(feedetail.get(0).getClinicCode());
				stoMap = this.stoRecipe(chargeVo.getPink(),feedetail, recipeN,feedetail.get(0).getClinicCode(),null, registration, employee, department, user2,stoMap);
				String wondowType = "";
				String execDeptCode = "";
				String doctDeptname = "";
				List<StoTerminal> list = new ArrayList<StoTerminal>();
				for (OutpatientFeedetailNow out : feedetail) {
					if("1".equals(out.getDrugFlag())){
						if (StringUtils.isNotBlank(out.getExecDpcd())) {
							execDeptCode = out.getExecDpcd();
							doctDeptname = out.getDoctDeptname();
							break;
						}
					}
				}
				if(feedetail.size()>0&&StringUtils.isNotBlank(execDeptCode)){
					Double recipeCost = 0.0;//处方金额(零售金额)
					Double recipeQty = 0.0;//处方中药品数量
					Double sumDays = 0.0;//
					StoRecipeNow stoRecipe = new StoRecipeNow();
					stoRecipe.setId(null);
					stoRecipe.setAreaCode(HisParameters.CURRENTHOSPITALCODE);
					stoRecipe.setHospitalID(HisParameters.CURRENTHOSPITALID);
					stoRecipe.setDrugDeptCode(execDeptCode);//执行科室/发放药房
					stoRecipe.setRecipeNo(recipeN);//处方号
					stoRecipe.setClassMeaningCode("1");//出库申请分类
					stoRecipe.setTransType(1);//交易类型
					stoRecipe.setRegDate(registration.getRegDate());//挂号日期
					stoRecipe.setRecipeState(0);//处方状态
					stoRecipe.setClinicCode(registration.getClinicCode());//门诊号
					stoRecipe.setCardNo(registration.getMidicalrecordId());//病例号
					stoRecipe.setPatientName(registration.getPatientName());//患者姓名
					stoRecipe.setSexCode(registration.getPatientSex());//性别
					stoRecipe.setBirthday(registration.getPatientBirthday());//出生日期
					stoRecipe.setPaykindCode(chargeVo.getPink());//结算类别
					stoRecipe.setDeptCode(registration.getDeptCode());//患者科室代码
					stoRecipe.setDoctCode(registration.getDoctCode());//开方医生
					stoRecipe.setDoctDept(registration.getDeptCode());//开方医生所在科室
					stoRecipe.setDoctName(registration.getDoctName());//开方医生姓名
					stoRecipe.setDoctDeptName(registration.getDeptName());//开方医生所在科室名称
					stoRecipe.setFeeOper(employee.getJobNo());//收费人
					stoRecipe.setFeeDate(DateUtils.getCurrentTime());//收费时间
					stoRecipe.setInvoiceNo(null);//发票号
					stoRecipe.setFeeOperName(user2.getName());//收费人姓名
					for(OutpatientFeedetailNow modlfeede : feedetail){
						if("1".equals(modlfeede.getDrugFlag())){
							recipeCost = recipeCost + modlfeede.getTotCost();
							recipeQty = recipeQty + modlfeede.getQty();
						}
						if("16".equals(modlfeede.getClassCode())&&modlfeede.getDays()!=null){//系统类别 草药
							sumDays = sumDays + modlfeede.getDays();
						}else{
							sumDays=1.0;
						}
					}
					BigDecimal bg = new BigDecimal(recipeCost);  
			        double recipeCost1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			        BigDecimal bg1 = new BigDecimal(recipeQty);  
			        double recipeQty1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			        BigDecimal bg2 = new BigDecimal(sumDays);  
			        double sumDays1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					stoRecipe.setRecipeCost(recipeCost1);//处方金额(零售金额)
					stoRecipe.setRecipeQty(recipeQty1);//处方中药品数量
					stoRecipe.setSumDays(sumDays1);//处方内药品剂数合计
					stoRecipe.setCreateUser(user2.getAccount());
					stoRecipe.setCreateDept(department.getDeptCode());
					stoRecipe.setCreateTime(DateUtils.getCurrentTime());
					stoRecipe.setValidState(1);
					stoRecipe.setModifyFlag(0);
					String key = "";
					//查询配药台，如果存在key相同的值，则使用原来的
					Map<String, List<StoTerminal>> stoTerminalMap = this.getStoTerminal(execDeptCode, registration, feedetail);
					for (Map.Entry<String, List<StoTerminal>> m : stoTerminalMap.entrySet()) {
						if(!stoMap.containsKey(m.getKey())){
							stoMap.put(m.getKey(), m.getValue());
							key = m.getKey();
						}
						key = m.getKey();
					}
					List<StoTerminal> list2 = stoMap.get(key);
					if(list2.size()==1&&list2.size()>0){
						StoTerminal stoTerminalNo = list2.get(0);//发药终端
						stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
						stoRecipe.setSendTerminalCode(stoTerminalNo.getCode());
						stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
						stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
						stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
						double stoQTY = 0.0;
						if(stoTerminalNo.getDrugQty()!=null){
							stoQTY = stoTerminalNo.getDrugQty();
						}
						stoTerminalNo.setDrugQty(stoQTY+recipeQty1);//更改待发药品数量
						medicineListInInterDAO.update(stoTerminalNo);
					}
					if(list2.size()==2){
						StoTerminal stoTerminalNo = list2.get(0);
						StoTerminal stoTerminal = list2.get(1);
						if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
							StoTerminal stoTerminal1 = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
							stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
							stoRecipe.setSendTerminalCode(stoTerminal.getCode());
							stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
							stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
							stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
						}else{
							stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
							stoRecipe.setSendTerminalCode(stoTerminalNo.getCode());
							stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
							stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
							stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
						}
						if(stoTerminalNo.getCode()!=null){
							stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()==null?0:stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
							medicineListInInterDAO.update(stoTerminalNo);
						}
					}
					
					String[] split = key.split("_");
					String tcode = split[split.length-1];
					stoRecipe.setDrugedTerminal(tcode);
					stoRecipeList.add(stoRecipe);
					BusinessDictionary dictionary = innerCodeDao.getDictionaryByCode("paykind", chargeVo.getPink());
					SysDepartment sysDepartment = deptInInterDAO.getDeptCode(execDeptCode);
					stoRecipe.setDrugDeptName(sysDepartment.getDeptName());//开方医生所在科室名称
					stoRecipe.setPayKindName(dictionary.getName());//结算名称
					stoRecipe.setDeptName(doctDeptname);//患者科室名称
					stoRecipe.setSexName(registration.getPatientSexName());
					medicineListInInterDAO.save(stoRecipe);
				}
				if(!stoMap.isEmpty()){
					for (Map.Entry<String, List<StoTerminal>> m : stoMap.entrySet()) {
						List<StoTerminal> stolist = m.getValue();
						if(stolist.size()>0&&stolist.size()==2){
							recipeWinMap.put(recipeN, stolist.get(1).getSendWindow());
						}
						if(stolist.size()==1){
							recipeWinMap.put(recipeN, stolist.get(0).getSendWindow());
						}
					}
				}
			}
		}
		//发票号Map具体key value类型看VO
		Map<String, String> invoiceMap = chargeVo.getInvoiceMap();
		
		//判断是map否为空，是因为医技有用到这个接口
		if(invoiceMap.isEmpty()){
			//获取多张发票号
			Map<String, String> queryFinanceInvoiceNoByNum = medicineListInInterDAO.queryFinanceInvoiceNoByNum(userId,HisParameters.OUTPATIENTINVOICETYPE, clicnicCodeInvoiceTypeMap.size());
			if("error".equals(queryFinanceInvoiceNoByNum.get("resMsg"))){
				throw new RuntimeException("INVOICE IS NOT ENOUGTH");
			}else{
				String[] invoiceNo = queryFinanceInvoiceNoByNum.get("resCode").split(",");
				int i = 0;
				for(Map.Entry<String ,List<OutpatientFeedetailNow>> invosMap : clicnicCodeInvoiceTypeMap.entrySet()){
					String clicnicCodeInvoiceType = invosMap.getKey();
					invoiceMap.put(clicnicCodeInvoiceType, invoiceNo[i]+"_0.00_"+queryFinanceInvoiceNoByNum.get(invoiceNo[i]));
					i+=1;
				}
			}
		}
		if(clicnicCodeInvoiceTypeMap!=null&&clicnicCodeInvoiceTypeMap.size()>0){
			for(Map.Entry<String ,List<OutpatientFeedetailNow>> cmap : clicnicCodeInvoiceTypeMap.entrySet()){
				//根据统计大类进行二次分组 
				Map<String ,List<OutpatientFeedetailNow>>  feeStatCodeMap = new HashMap<String ,List<OutpatientFeedetailNow>>();
				
				String[] sKey = cmap.getKey().split("_");
				String feeStatCode = sKey[1];
				String clicnicCode = sKey[0];
				//获取发票号
				String[] split = invoiceMap.get(cmap.getKey()).split("_");
				String invoiceNo = split[0];//发票号
				String invoiceNoID = split[2];//发票号所在发票组的ID
				registration = medicineListInInterDAO.queryInfoByNo(clicnicCode);
				int a = 0;
				for(OutpatientFeedetailNow fee : cmap.getValue()){
					a+=1;
					//门诊收费处理
					this.handleOutpatientFeedetail(fee, user2, department, invoiceNo, invoiceSeqMap.get(fee.getClinicCode()), feeStatCode, a);
					//根据统计大类进行二次分组
					MinfeeStatCode statCode = this.getFeeStatCode(fee);//获取统计大类code
					if(feeStatCodeMap.containsKey(statCode.getFeeStatCode())){
						List<OutpatientFeedetailNow> list = feeStatCodeMap.get(statCode.getFeeStatCode());
						list.add(fee);
						feeStatCodeMap.put(statCode.getFeeStatCode(), list);
					}else{
						List<OutpatientFeedetailNow> list = new ArrayList<OutpatientFeedetailNow>();
						list.add(fee);
						feeStatCodeMap.put(statCode.getFeeStatCode(), list);
					}
					//调剂信息发票号回填
					for (StoRecipeNow sto : stoRecipeList) {
						if(sto.getRecipeNo() != null && sto.getRecipeNo().equals(fee.getRecipeNo())){
							sto.setInvoiceNo(invoiceNo);
							medicineListInInterDAO.update(sto);
						}
					}
				}
				
				//修改发票和添加发票使用记录
				this.updateAndSaveInvoice(employee, invoiceNo, HisParameters.OUTPATIENTINVOICETYPE, department,invoiceNoID);
				//保存支付情况
				Double cost = this.getCostCurrentList(cmap.getValue());
				this.saveBusinessPay(cost, chargeVo, user2, department, invoiceNo, invoiceSeqMap.get(clicnicCode), invoiceComb);
				//添加发票明细
				int b = 0;
				if(feeStatCodeMap.size()>0){
					for(Map.Entry<String ,List<OutpatientFeedetailNow>> feeStatM : feeStatCodeMap.entrySet()){
						b+=1;
						String feeStatCode1 = feeStatM.getKey();
						List<OutpatientFeedetailNow> list = feeStatM.getValue();
						Double costCurrentList = this.getCostCurrentList(list);//计算同发票号同统计大类的费用
						if(list!=null&&list.size()>0){
							MinfeeStatCode statCode = this.getFeeStatCode(list.get(0));//获取统计大类code
							this.addInvoiceDetial(costCurrentList, statCode.getFeeStatCode(), statCode.getFeeStatName(), invoiceNo, deptId, deptName, userId, invoiceSeqMap.get(clicnicCode), b, user2, department);
						}
					}
				}
				
				String wondowType = "";
				for(Entry<String, String> reMap:recipeWinMap.entrySet()){
					String win = reMap.getValue();
					if(StringUtils.isBlank(wondowType)||!wondowType.contains(win)){
						if(StringUtils.isNotBlank(wondowType)){
							wondowType += ",";
						}
						wondowType += win;
					}
				}
				//结算信息   
				this.businessInvoiceInfo(cost, clicnicCode, invoiceNo, invoiceSeqMap.get(clicnicCode), invoiceComb, chargeVo, registration, wondowType, user2, department, userId);
				//出库申请单
				this.drugApplyout(cmap.getValue(), registration, department,user2);
			}
		}
		//门诊账户处理
		this.accountHandle(registration.getMidicalrecordId(),chargeVo, department, user2, HisParameters.OUTPATIENTINVOICETYPE);
	
		//门诊处方处理
		List<OutpatientRecipedetailNow> recipedetailList = medicineListInInterDAO.queryRecipedetailList(chargeVo.getRecipeNo());//处方表
		if(recipedetailList.size()>0){
			for(OutpatientRecipedetailNow RList :recipedetailList){
				RList.setId(RList.getId());
				RList.setStatus(1);
				RList.setUpdateUser(userId);
				RList.setUpdateTime(new Date());
				medicineListInInterDAO.update(RList);//保存门诊处方
			}
		}
		//预扣库存
		this.outFeedetial(feedetailList);
		//更新处方明细表的收费状态
		medicineListInInterDAO.updateRecipedetialCharge(receipeNOs);
//			//parameterMap 所有参数
//			if(chargeVo.getHospitalAccount()!=null&&chargeVo.getHospitalAccount()!=0){
//				//更新门诊账户表
//				account.setAccountBalance(account.getAccountBalance()-chargeVo.getHospitalAccount());
//				medicineListInInterDAO.save(account);
//				
//				//添加门诊账户明细表
//				OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
//				accountrecord.setId(null);//ID
//				accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
//				accountrecord.setAccountId(account.getId());//门诊账户编号
//				accountrecord.setOpertype(4);//操作类型
//				accountrecord.setMoney(-chargeVo.getHospitalAccount());//交易金额
//				accountrecord.setDeptCode(department.getDeptCode());//相关科室
//				accountrecord.setOperCode(user2.getAccount());//操作员 
//				accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
//				accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
//				accountrecord.setValid(0);//是否有效
//				accountrecord.setInvoiceType(invoiceType);//发票类型
//				accountrecord.setCreateUser(user2.getAccount());
//				accountrecord.setCreateDept(department.getDeptCode());
//				accountrecord.setCreateTime(DateUtils.getCurrentTime());
//				accountrecord.setUpdateUser(user2.getAccount());
//				accountrecord.setUpdateTime(DateUtils.getCurrentTime());
//				medicineListInInterDAO.save(accountrecord);
//			}
		
		//添加发票明细表
//			if(chargeVo.getInvoiceTypeCode()!=null&&chargeVo.getInvoiceTypeName()!=null&&chargeVo.getInvoiceTypeMoney()!=null&&!"".equals(chargeVo.getInvoiceTypeCode())&&!"".equals(chargeVo.getInvoiceTypeName())&&!"".equals(chargeVo.getInvoiceTypeMoney())){
//				String[] feeStr = chargeVo.getInvoiceTypeMoney().split(","); 
//				String[] TypeNameStr = chargeVo.getInvoiceTypeName().split(","); 
//				String[] TypeCodeStr = chargeVo.getInvoiceTypeCode().split(",");
//				for(int i1 = 0;i1<feeStr.length;i1++){
//					BusinessInvoicedetail invoicedetail = new BusinessInvoicedetail();
//					invoicedetail.setId(null);//ID
//					invoicedetail.setInvoiceNo(chargeVo.getInvoiceNo());//发票号
//					invoicedetail.setTransType(1);//正反类型1正2反
//					invoicedetail.setInvoCode(TypeCodeStr[i1]);//发票科目代码
//					invoicedetail.setInvoName(TypeNameStr[i1]);//发票科目名称
//					invoicedetail.setPubCost(0.00);//可报效金额
//					invoicedetail.setOwnCost(0.00);//不可报效金额
//					if(feeStr[i1]!=null&&!"".equals(feeStr[i1])){
//						Double fee = Double.valueOf(feeStr[i1]);
//						invoicedetail.setPayCost(fee);//自付金额
//					}
//					invoicedetail.setDeptCode(deptId);//执行科室ID
//					invoicedetail.setDeptName(deptName);//执行科室名称
//					invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
//					invoicedetail.setOperCode(userId);//操作人
//					invoicedetail.setBalanceFlag(0);//日结状态
//					invoicedetail.setBalanceNo(null);//日结标识号
//					invoicedetail.setBalanceOpcd(null);//日结人
//					invoicedetail.setBalanceDate(null);//日结时间
//					invoicedetail.setCancelFlag(1);//状态
//					invoicedetail.setInvoiceSeq(invoiceSeq);//发票序号
//					invoicedetail.setInvoSequence(i1+"");//发票内流水号
//					invoicedetail.setCreateUser(user2.getAccount());
//					invoicedetail.setCreateDept(department.getDeptCode());
//					invoicedetail.setCreateTime(DateUtils.getCurrentTime());
//					medicineListInInterDAO.save(invoicedetail);
//				}
//			}
//			
//			//门诊处方处理
//			List<OutpatientRecipedetail> recipedetailList = medicineListInInterDAO.queryRecipedetailList(chargeVo.getRecipeNo());//处方表
//			if(recipedetailList.size()>0){
//				for(OutpatientRecipedetail RList :recipedetailList){
//					RList.setId(RList.getId());
//					RList.setStatus(1);
//					RList.setCreateUser(userId);
//					RList.setCreateDept(deptId);
//					RList.setCreateTime(new Date());
//					medicineListInInterDAO.update(RList);//保存门诊处方
//				}
//			}
//			//门诊收费表处理
//			int a = 0;
//			if(feedetailList.size()>0){
//				for(OutpatientFeedetail FNlist :feedetailList){
//					a = a + 1;
//					FNlist.setId(FNlist.getId());//门诊收费表ID
//					FNlist.setPayFlag(1);//收费状态
//					if(FNlist.getConfirmNum()!=null||!"".equals(FNlist.getConfirmNum())){
//						FNlist.setNobackNum(FNlist.getConfirmNum());//可退数量
//					}
//					FNlist.setExecDpnm(department.getDeptCode());//操作科室
//					FNlist.setCreateUser(user2.getAccount());//操作人
//					FNlist.setCreateDept(department.getDeptCode());//缠
//					FNlist.setFeeDate(DateUtils.getCurrentTime());
//					FNlist.setFeeCpcd(user2.getAccount());
//					FNlist.setInvoSequence(a+"");
//					FNlist.setFeeDate(DateUtils.getCurrentTime());
//					FNlist.setCreateTime(DateUtils.getCurrentTime());
//					//根据最小费用代码ID查询到最小费用代码的encode
//					BusinessDictionary drugminimumcost =  innerCodeDao.getDictionaryByCode("drugMinimumcost",FNlist.getFeeCode());
//					//根据最小费用代码的encode查询统计大类中统计大类的encode和name
//					MinfeeStatCode minfeeStatCode = medicineListInInterDAO.minfeeStatCodeByEncode(drugminimumcost.getEncode());
//					FNlist.setInvoCode(minfeeStatCode.getFeeStatCode());
//					//根据统计大类的encode获取发票号
//					FNlist.setInvoiceNo(chargeVo.getInvoiceNo());//发票号
//					//根据发票号获取发票序号
//					FNlist.setInvoiceSeq(invoiceSeq);//发票序号
//					medicineListInInterDAO.update(FNlist);//保存门诊处方
//				}
//			}
		
		
//			//保存支付情况表
//			Double[] arr = new Double[]{chargeVo.getCash(),chargeVo.getBankCard(),chargeVo.getCheck(),chargeVo.getHospitalAccount()};
//			for(int z=0;z<arr.length;z++){
//				if(arr[z]!=null){
//					BusinessPayMode payMode = new BusinessPayMode();
//					//
//					payMode.setInvoiceNo(invoiceNos);//发票号合集
//					payMode.setTransType(1);//交易类型
//					payMode.setOperCode(user2.getAccount());//结算人
//					payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
//					if(chargeVo.getCash()!=null&&chargeVo.getCash()>0){//支付方式
//						payMode.setModeCode("1");
//						payMode.setTotCost(chargeVo.getCash());//应付金额
//						payMode.setRealCost(chargeVo.getCash());//实付金额
//					}
//					if(chargeVo.getBankCard()!=null&&chargeVo.getBankCard()>0){
//						payMode.setModeCode("2");
//						payMode.setTotCost(chargeVo.getBankCard());//应付金额
//						payMode.setRealCost(chargeVo.getBankCard());//实付金额
//					}
//					if(chargeVo.getCheck()!=null&&chargeVo.getCheck()>0){
//						payMode.setModeCode("3");
//						payMode.setTotCost(chargeVo.getCheck());//应付金额
//						payMode.setRealCost(chargeVo.getCheck());//实付金额
//						payMode.setBankName(chargeVo.getBankUniti());//开户银行名称
//						payMode.setAccount(chargeVo.getBanki());//开户账户
//						payMode.setCheckNo(chargeVo.getBankNo());//支票号
//						
//					}
//					if(chargeVo.getHospitalAccount()!=null&&chargeVo.getHospitalAccount()>0){
//						payMode.setModeCode("4");
//						payMode.setTotCost(chargeVo.getHospitalAccount());//应付金额
//						payMode.setRealCost(chargeVo.getHospitalAccount());//实付金额
//					}
//					payMode.setBalanceFlag(0);//是否结算
//					payMode.setCancelFlag(1);//状态1正常
//					payMode.setInvoiceSeq(invoiceSeq);//发票序号合集
//					payMode.setInvoiceComb(invoiceComb);//一次收费序号
//					payMode.setCreateUser(user2.getAccount());
//					payMode.setCreateDept(department.getDeptCode());
//					payMode.setCreateTime(DateUtils.getCurrentTime());
//					medicineListInInterDAO.save(payMode);
//				}
//			}
//			
//			String wondowType = "";
//			
//			//摆药单
//			String[] recipeNoArr = null;
//			String feedetailDrugIds = "";
//			if(feedetailList.size()>0){
//				for(OutpatientFeedetail fee : feedetailList){
//					if(fee.getDrugFlag().equals("1")){
//						if(!"".equals(feedetailDrugIds)){
//							feedetailDrugIds = feedetailDrugIds + "','";
//						}
//						feedetailDrugIds = feedetailDrugIds + fee.getId();
//					}
//				}
//			}
//			if(chargeVo.getRowsListNo()!=null&&!"".equals(chargeVo.getRowsListNo())){
//				recipeNoArr = chargeVo.getRowsListNo().split("','");
//				if(recipeNoArr.length>0){
//					for(int r=0;r<recipeNoArr.length;r++){
//						List<OutpatientFeedetail> feedetail = medicineListInInterDAO.queryFeedetailRecipeNo(recipeNoArr[r]);
//						if(feedetail.size()>0){
//							Double recipeCost = 0.0;//处方金额(零售金额)
//							Double recipeQty = 0.0;//处方中药品数量
//							Double sumDays = 0.0;//
//							StoRecipe stoRecipe = new StoRecipe();
//							stoRecipe.setId(null);
//							stoRecipe.setDrugDeptCode(feedetail.get(0).getExecDpcd());//执行科室/发放药房
//							stoRecipe.setRecipeNo(recipeNoArr[r]);//处方号
//							stoRecipe.setClassMeaningCode("1");//出库申请分类
//							stoRecipe.setTransType(1);//交易类型
//							stoRecipe.setRegDate(registerInfo.getRegDate());//挂号日期
//							stoRecipe.setRecipeState(0);//处方状态
//							stoRecipe.setClinicCode(feedetail.get(0).getClinicCode());//门诊号
//							stoRecipe.setCardNo(registerInfo.getMidicalrecordId());//病例号
//							stoRecipe.setPatientName(registerInfo.getPatientName());//患者姓名
//							stoRecipe.setSexCode(registerInfo.getPatientSex());//性别
//							stoRecipe.setBirthday(registerInfo.getPatientBirthday());//出生日期
//							stoRecipe.setPaykindCode(chargeVo.getPink());//结算类别
//							stoRecipe.setDeptCode(registerInfo.getDeptCode());//患者科室代码
//							stoRecipe.setDoctCode(registerInfo.getDoctCode());//开方医生
//							stoRecipe.setDoctDept(registerInfo.getDeptCode());//开方医生所在科室
//							stoRecipe.setFeeOper(employee.getJobNo());//收费人
//							stoRecipe.setFeeDate(DateUtils.getCurrentTime());//收费时间
//							//根据最小费用代码ID查询到最小费用代码的encode
//							BusinessDictionary drugminimumcost =  innerCodeDao.getDictionaryByCode("drugMinimumcost",feedetail.get(0).getFeeCode());
//							//根据最小费用代码的encode查询统计大类中统计大类的encode和name
//							MinfeeStatCode minfeeStatCode = medicineListInInterDAO.minfeeStatCodeByEncode(drugminimumcost.getEncode());
//							stoRecipe.setInvoiceNo(chargeVo.getInvoiceNo());//发票号      **************
//							for(OutpatientFeedetail modlfeede : feedetail){
//								if(modlfeede.getDrugFlag().equals("1")){
//									recipeCost = recipeCost + modlfeede.getTotCost();
//									recipeQty = recipeQty + modlfeede.getQty();
//								}
//								if(modlfeede.getClassCode().equals("16")&&modlfeede.getDays()!=null){//系统类别 草药
//									sumDays = sumDays + modlfeede.getDays();
//								}else{
//									sumDays=1.0;
//								}
//							}
//							BigDecimal bg = new BigDecimal(recipeCost);  
//					        double recipeCost1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//					        BigDecimal bg1 = new BigDecimal(recipeQty);  
//					        double recipeQty1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//					        BigDecimal bg2 = new BigDecimal(sumDays);  
//					        double sumDays1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//							stoRecipe.setRecipeCost(recipeCost1);//处方金额(零售金额)
//							stoRecipe.setRecipeQty(recipeQty1);//处方中药品数量
//							stoRecipe.setSumDays(sumDays1);//处方内药品剂数合计
//							stoRecipe.setCreateUser(user2.getAccount());
//							stoRecipe.setCreateDept(department.getDeptCode());
//							stoRecipe.setCreateTime(DateUtils.getCurrentTime());
//							stoRecipe.setValidState(1);
//							stoRecipe.setModifyFlag(0);
//							//按照特殊收费窗口查找配药台
//							Integer itemType = 4;//特殊收费窗口
//							SpeNalVo speNalVoList = medicineListInInterDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),((SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession()).getDeptCode(),itemType);
//							if(speNalVoList.gettCode()!=null){
//								stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
//								StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
//								if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
//									stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
//								}else{
//									stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
//								}
//							}else{
//								itemType = 2;//专科
//								speNalVoList = medicineListInInterDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),registerInfo.getDeptCode(),itemType);
//								if(speNalVoList.gettCode()!=null){
//									stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
//									StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
//									if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
//										stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
//									}else{
//										stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
//									}
//								}else{
//									itemType = 3;//结算类别
//									speNalVoList = medicineListInInterDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),registerInfo.getPaykindCode(),itemType);
//									if(speNalVoList.gettCode()!=null){
//										stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
//										StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
//										if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
//											stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
//										}else{
//											stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
//										}
//									}else{
//										itemType = 5;//挂号级别
//										speNalVoList = medicineListInInterDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),registerInfo.getReglevlCode(),itemType);
//										if(speNalVoList.gettCode()!=null){
//											stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
//											StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
//											if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
//												stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
//											}else{
//												stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
//											}
//										}else{
//											itemType = 1;//特殊药品
//											for(OutpatientFeedetail modlse :feedetail){
//												speNalVoList = medicineListInInterDAO.querySpeNalVoBy(modlse.getExecDpcd(),modlse.getItemCode(),itemType);
//												if(speNalVoList.gettCode()!=null){
//													stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
//													StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
//													if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
//														stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
//													}else{
//														stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
//													}
//													break;
//												}
//											}
//										}
//									}
//								}
//							}
//							if(speNalVoList.gettCode()==null){
//								StoTerminal stoTerminal = medicineListInInterDAO.queryStoTerminal(feedetail.get(0).getExecDpcd());//配药终端
//								StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(stoTerminal.getId());//发药终端
//								if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
//									stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
//								}else{
//									stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
//								}
//								stoRecipe.setDrugedTerminal(stoTerminal.getId());
//							}
//							if(!"".equals(wondowType)){
//								wondowType = wondowType + ",";
//							}
//							wondowType = wondowType + stoRecipe.getSendTerminal();
//							medicineListInInterDAO.save(stoRecipe);
//						}
//					}
//				}
//			}
//			
		
			//结算信息表
//				BusinessInvoiceInfo invoiceInfo = new BusinessInvoiceInfo();
//				invoiceInfo.setId(null);
//				invoiceInfo.setInvoiceNo(chargeVo.getInvoiceNo());//发票号
//				invoiceInfo.setTransType(1);//交易类型
//				invoiceInfo.setCardNo(registerInfo.getMidicalrecordId());//病历号
//				invoiceInfo.setPaykindCode(chargeVo.getPink());//结算类别
//				invoiceInfo.setRegDate(registerInfo.getRegDate());//挂号日期
//				invoiceInfo.setName(registerInfo.getPatientName());//患者姓名
//				invoiceInfo.setPactCode(registerInfo.getPactCode());//合同单位代码
//				invoiceInfo.setPactName(registerInfo.getPactName());//合同单位名称
//				invoiceInfo.setTotCost(chargeVo.getTotCost());//总金额
//				invoiceInfo.setPayCost(chargeVo.getTotCost());//自付金额
//				invoiceInfo.setRealCost(chargeVo.getTotCost());//实付金额
//				invoiceInfo.setOperCode(userId);//结算人
//				invoiceInfo.setClinicCode(chargeVo.getClinicCode());//门诊号
//				invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
//				invoiceInfo.setExamineFlag(0);//团体/个人
//				invoiceInfo.setCancelFlag(1);//有效
//				invoiceInfo.setDrugWindow(wondowType);//发药窗口
//				invoiceInfo.setCancelInvoice(null);//作废票据号
//				invoiceInfo.setCancelCode(null);//作废操作员
//				invoiceInfo.setCancelDate(null);//作废时间
//				invoiceInfo.setInvoiceSeq(invoiceSeq);//发票序号
//				invoiceInfo.setInvoiceComb(invoiceComb);//一次收费序号
//				invoiceInfo.setExtFlag(1);//拓展标示 1 自费
//				invoiceInfo.setCreateUser(user2.getAccount());
//				invoiceInfo.setCreateDept(department.getDeptCode());
//				invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
//				medicineListInInterDAO.save(invoiceInfo);
			
//				//修改发票
////				String invoiceType = "02";
//				medicineListInInterDAO.saveInvoiceFinance(employee.getJobNo(),chargeVo.getInvoiceNo(),invoiceType);
//				
//				//添加发票使用记录
//				InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
//				usageRecord.setId(null);
//				usageRecord.setUserId(employee.getId());
//				usageRecord.setUserCode(employee.getJobNo());
//				usageRecord.setUserType(2);
//				usageRecord.setInvoiceNo(chargeVo.getInvoiceNo());
//				usageRecord.setCreateUser(department.getDeptCode());
//				usageRecord.setCreateDept(department.getDeptCode());
//				usageRecord.setCreateTime(DateUtils.getCurrentTime());
//				usageRecord.setUserName(employee.getName());
//				medicineListInInterDAO.save(usageRecord);
//			
//			
//			//出库申请单
//			String[] feedetailIdsArr = null;
//			if(feedetailDrugIds!=null&&!"".equals(feedetailDrugIds)){
//				feedetailIdsArr = feedetailDrugIds.split("','");
//				if(feedetailIdsArr.length>0){
//					for(int f=0;f<feedetailIdsArr.length;f++){
//						OutpatientFeedetail  feedetailLists = medicineListInInterDAO.queryDrugInfoList(feedetailIdsArr[f]);
//						DrugInfo drugInfo = medicineListInInterDAO.queryDrugInfoById(feedetailLists.getItemCode());
//						int applyNumber = Integer.parseInt(medicineListInInterDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
//						String yearLast = new SimpleDateFormat("yyMM").format(new Date());
//						int value = keyvalueInInterDAO.getVal(deptId,"内部入库申请单号",yearLast);
//						String applyBillcode = "0" + yearLast + value;//组成内部入库申请单号
//						DrugApplyout drugApplyout = new DrugApplyout();
//						drugApplyout.setApplyNumber(applyNumber);//申请流水号
//						drugApplyout.setDeptCode(registerInfo.getDeptCode());//申请科室
//						drugApplyout.setDrugDeptCode(feedetailLists.getExecDpcd());//发药科室
//						drugApplyout.setOpType(1);//操作类型分类
//						drugApplyout.setGroupCode(null);//批次号,摆药生成出库记录时反写
//						drugApplyout.setDrugCode(feedetailLists.getItemCode());//药品id
//						drugApplyout.setTradeName(drugInfo.getName());//药品名
//						drugApplyout.setBatchNo(null);//批号 ,摆药生成出库记录时反写
//						drugApplyout.setDrugType(drugInfo.getDrugType());//药品类别
//						drugApplyout.setDrugQuality(drugInfo.getDrugNature());//药品性质
//						drugApplyout.setSpecs(drugInfo.getSpec());//规格
//						drugApplyout.setPackUnit(drugInfo.getDrugPackagingunit());//包装单位
//						drugApplyout.setPackQty(drugInfo.getPackagingnum());//包装数量
//						drugApplyout.setMinUnit(drugInfo.getUnit());//最小单位
//						drugApplyout.setShowFlag(null);
//						drugApplyout.setShowUnit(null);
//						drugApplyout.setValidState(1);//有效
//						drugApplyout.setRetailPrice(drugInfo.getDrugRetailprice());//零售价
//						drugApplyout.setWholesalePrice(drugInfo.getDrugWholesaleprice());//批发价
//						drugApplyout.setApplyBillcode(applyBillcode);//申请单号
//						drugApplyout.setApplyOpercode(registerInfo.getDoctCode());//申请人
//						drugApplyout.setApplyDate(DateUtils.getCurrentTime());//申请日期
//						drugApplyout.setApplyState(0);//申请状态
//						drugApplyout.setApplyNum(Double.parseDouble(feedetailLists.getConfirmNum()+""));//申请数量
//						drugApplyout.setBillclassCode("1");//查询发药药房对应的发药台对应的摆药单类型
//						drugApplyout.setShowFlag(feedetailLists.getExtFlag3());
//						drugApplyout.setDays(feedetailLists.getDays());//申请付数
//						drugApplyout.setPreoutFlag(1);//预扣库存，来源于预扣库存的参数，参数设置预扣库存是，此处为是，否则为否。预扣库存的情况下，采取更新仓库主表中的预扣库存数量。
//						drugApplyout.setChargeFlag(1);//已收费
//						drugApplyout.setPatientId(feedetailLists.getClinicCode());//患者门诊号
//						drugApplyout.setPatientDept(registerInfo.getDeptCode());//患者科室  ---待处理
//						if(feedetailLists.getDoseOnce()!=null&&!"".equals(feedetailLists.getDoseOnce())){
//							drugApplyout.setDoseOnce(feedetailLists.getDoseOnce().doubleValue());//每次剂量
//						}
//						drugApplyout.setDoseUnit(feedetailLists.getDoseUnit());//计量单位
//						drugApplyout.setUsageCode(feedetailLists.getUsageCode());//用法代码
//						drugApplyout.setUseName(feedetailLists.getUseName());//用法名称
//						drugApplyout.setDfqFreq(feedetailLists.getFrequencyCode());//频次代码
//						drugApplyout.setOrderType("1");//医嘱类别
//						drugApplyout.setMoOrder(feedetailLists.getMoOrder());//医嘱流水号
//						drugApplyout.setCombNo(feedetailLists.getCombNo());//组合号 
//						drugApplyout.setRecipeNo(feedetailLists.getRecipeNo());//处方号
//						int sequenceNo = 0;
//						if(feedetailLists.getSequenceNo()!=null&&!"".equals(feedetailLists.getSequenceNo())){
//							sequenceNo = Integer.parseInt(feedetailLists.getSequenceNo());
//						}
//						drugApplyout.setSequenceNo(sequenceNo);//处方内流水号
//						drugApplyout.setCreateUser(department.getDeptCode());
//						drugApplyout.setCreateDept(department.getDeptCode());
//						drugApplyout.setCreateTime(DateUtils.getCurrentTime());
//						medicineListInInterDAO.save(drugApplyout);
//					}
//				}
//			}
//			
//			
//			//预扣库存
//			if(!"".equals(feedetailDrugIds)){
//				String[] feedetailArrs = feedetailDrugIds.split("','");
//				if(feedetailArrs.length>0){
//					for(int h=0;h<feedetailArrs.length;h++){
//						OutpatientFeedetail outFeedetail = medicineListInInterDAO.queryOutFeedetail(feedetailArrs[h]);
//						businessStockInfoInInterService.withholdStock(outFeedetail.getExecDpcd(),outFeedetail.getItemCode(),outFeedetail.getNobackNum(),outFeedetail.getExtFlag3().toString());
//					}
//				}
//			}
		
//			//修改发票
//			String invoiceType = "02";
//			medicineListInInterDAO.saveInvoiceFinance(employee.getJobNo(),chargeVo.getInvoiceNo(),invoiceType);
//			
//			//添加发票使用记录
//			InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
//			usageRecord.setId(null);
//			usageRecord.setUserId(employee.getId());
//			usageRecord.setUserCode(employee.getJobNo());
//			usageRecord.setUserType(2);
//			usageRecord.setInvoiceNo(chargeVo.getInvoiceNo());
//			usageRecord.setCreateUser(department.getDeptCode());
//			usageRecord.setCreateDept(department.getDeptCode());
//			usageRecord.setCreateTime(DateUtils.getCurrentTime());
//			usageRecord.setUserName(employee.getName());
//			medicineListInInterDAO.save(usageRecord);
		//执行医技预约和医技确认
		OutputInInterVO out ;
		for(OutpatientFeedetailNow f : feedetailList){
			//1.获取药品或非药品
			DrugOrUNDrugVo vo = medicineListInInterDAO.getDrugOrUNDrugVo(f.getItemCode(),f.getDrugFlag());
			//2.判断是否需要医技预约
				if(vo.getIspreorder()==null||vo.getIspreorder()==0){//不需要预约
					if(vo.getIssubmit()!=null&&vo.getIssubmit()==1){//需要确认
						//3.医技终端确认表
						TecTerminalApply tec = new TecTerminalApply();
						tec.setClinicNo(f.getClinicCode());//病历号
						tec.setCardNo(f.getCardNo());//就诊卡号
						tec.setCreateDept(dept.getDeptCode());
						tec.setCreateTime(new Date());
						tec.setCreateUser(user2.getCode());
						tec.setCurrentUnit(f.getPriceUnit());//当前单位、即计价单位
						tec.setDeptCode(f.getDoctCode());//申请部门编码（即开方医生科室）
						tec.setDrugDeptCode(f.getExecDpcd());//发药科室编码       -------
						tec.setExecuteDeptcode(f.getExecDpcd());//终端科室编码，即执行科室编码
						tec.setExtFlag("1");//新旧标示
						tec.setExtFlag1("1");//是否有效
						tec.setInhosDeptcode(registration.getDeptCode());//------挂号科室
						tec.setIsphamacy(vo.getIsdrug().toString());//是否是药品
						tec.setItemCode(f.getItemCode());//项目编码
						tec.setItemName(f.getItemName());//项目名称
						tec.setMachineNo(vo.getEquipmentNO());//设备编号，（药品取药品的code，非药品去设备编码）
						tec.setMoExecSqn(f.getSequenceNo()+"");//医嘱执行单流水号，即处方内项目流水号
						tec.setMoOrder(f.getMoOrder());
						tec.setName(registration.getPatientName());//患者姓名
						tec.setOperCode(user2.getAccount());//操作完
						tec.setOperDate(new Date());
						tec.setOutpatientFeedetailId(f.getRecipeNo());//处方明细id 即处方号
						tec.setPackageCode(f.getPackageCode());//组套编码
						tec.setPackageName(f.getPackageName());//组套名称
						tec.setPactCode(registration.getPactCode());
						tec.setPatientsex(registration.getPatientSex().toString());
						tec.setPatienttype("1");//患者类别：‘1’ 门诊|‘2’ 住院|‘3’ 急诊|‘4’ 体检  5 集体体检
						tec.setPayFlag("1");
						tec.setQty(f.getQty());
						tec.setRecipeDoccode(f.getDoctCode());
						tec.setRecipeNo(f.getRecipeNo());
						tec.setSendFlag("0");//项目状态
						tec.setSequenceNo(f.getSequenceNo());//处方内项目流水号
						tec.setTotCost(f.getTotCost());
						tec.setUnitPrice(f.getUnitPrice());
						tec.setUpdateSequenceno(f.getUpdateSequenceno());
						if(StringUtils.isNotBlank(f.getSampleId())){
							tec.setApplyNumber(f.getSampleId());//申请流水号 
						}else{
							String seqByName = medicineListInInterDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER");
							tec.setApplyNumber(seqByName);
							f.setSampleId(seqByName);
							medicineListInInterDAO.update(f);
						}
						tec.setConfirmNum(0);
						tec.setId(null);
						tec.setCreateTime(new Date());
						tec.setCreateDept(deptId);
						tec.setCreateUser(userId);
						tec.setUpdateTime(new Date());
						tec.setUpdateUser(userId);
						terminalApplyInInterDAO.save(tec);
					}
				}
				if(vo.getIspreorder()!=null&&vo.getIspreorder()==1){//需要预约
					TecApply apply = new TecApply();
					apply.setId(null);
					apply.setAge(Integer.parseInt(registration.getPatientAge()));
//					apply.setBookDate(new Date());//预约时间
					if(StringUtils.isNotBlank(f.getSampleId())){
						apply.setBookId(f.getSampleId());//预约单号
					}else{
						String seqByName = medicineListInInterDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER");
						apply.setBookId(seqByName);//预约单号
						f.setSampleId(seqByName);
						medicineListInInterDAO.update(f);
					}
					apply.setCardNo(f.getCardNo());
					apply.setClinicCode(f.getClinicCode());
					apply.setCreateDept(f.getRegDpcd());
					apply.setCreateTime(f.getRegDate());
					apply.setCreateUser(f.getDoctCode());
					apply.setDeptCode(f.getExecDpcd());
					apply.setDeptName(f.getExecDpnm());
					apply.setExecDevice(vo.getEquipmentNO());//执行设备
					apply.setExecOper(f.getExeUser());//执行人
					apply.setExecuteLocate(f.getExecDpcd());//执行地点，暂时存放执行科室
//						apply.setHealthStatus("");//患者健康状态======
//						apply.setHurtFlag(hurtFlag);
					apply.setItemCode(f.getItemCode());
					apply.setItemName(f.getItemName());
					apply.setMoOrder(f.getMoOrder());
					apply.setName(registration.getPatientName());
//						apply.setNoonCode(noonCode);
					apply.setQty(f.getQty().intValue());
//						apply.setReasonableFlag(reasonableFlag);
					apply.setRectpeDeptCode(f.getDoctCode());//开单科室code
					apply.setRectpeNo(f.getRecipeNo());
//						apply.setReportDate(reportDate);//取报告时间
//						apply.setSampleKind(sampleKind);//标本或部位
//						apply.setSampleWay(sampleWay);//取样方式
					apply.setSequenceNo(f.getSequenceNo());
					apply.setStatus(0);//0 预约 1 生效 2 审核 3 作废
					apply.setTransType(1);//交易类型 1 正交易 ，2负交易
					apply.setCreateUser(userId);
					apply.setCreateTime(new Date());
					apply.setCreateDept(deptId);
					apply.setUpdateTime(new Date());
					apply.setUpdateUser(userId);
//						apply.setUnitFlag(unitFlag);//单位标识 1明细 2 组套
					tecApplyInInInterDAO.save(apply);
				}
		}
		String invoiceNos = "";
		for(Map.Entry<String, String> m : invoiceMap.entrySet()){
			String[] split = m.getValue().split("_");
			if(StringUtils.isNotBlank(invoiceNos)){
				invoiceNos += ",";
			}
			invoiceNos += split[0];
		}
		map.put("resMsg", "success");
		map.put("resCode", "缴费成功");
		map.put("invoiceNos",invoiceNos);//返回使用的发票号
		return map;
	}
	/**
	 * @Description 打印发票相关
	 * @author  marongbin
	 * @createDate： 2016年12月12日 下午4:43:28 
	 * @modifier 
	 * @modifyDate：
	 * @param feedetailList
	 * @return: ReportEntiry
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public ReportEntiry report(String invoiceNo,String payKind,RegistrationNow registerInfo,List<OutpatientFeedetailNow> feedetailList){
		ReportEntiry report = new ReportEntiry();
		Double payCost = 0.0;
		Double pubCost = 0.0;
		Double ownCost = 0.0;
		String chineseCost = "零";//金额大写
		if(feedetailList.size()>0){
			for (OutpatientFeedetailNow fee : feedetailList) {
				payCost += fee.getPayCost();
				pubCost += fee.getPubCost();
				ownCost += fee.getOwnCost();
			}
		}
		BigDecimal paybg = new BigDecimal(payCost);  
        payCost = paybg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal bg1 = new BigDecimal(pubCost);  
        pubCost = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal bg2 = new BigDecimal(ownCost);  
        ownCost = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        report.setOwnCost(ownCost);
        report.setPayCost(payCost);
        report.setPubCost(pubCost);
        String chinese = NumberToChinese.number2CNMontrayUnit(paybg);
        report.setChineseCost(chinese);
        report.setInvoiceNo(invoiceNo);
        //'01','自费','02','保险','03','公费在职','04','公费退休','05','公费干部'
        if("01".equals(payKind)){
        	report.setPayKind("自费");
        }
        if("02".equals(payKind)){
        	report.setPayKind("保险");
        }
        if("03".equals(payKind)){
        	report.setPayKind("公费在职");
        }
        if("04".equals(payKind)){
        	report.setPayKind("公费退休");
        }
        if("05".equals(payKind)){
        	report.setPayKind("公费干部");
        }
        return report;
	}
	/**
	 * @Description  计算费用
	 * @author  marongbin
	 * @createDate： 2016年11月10日 下午2:54:42 
	 * @modifier 
	 * @modifyDate：
	 * @param：  feedetailList 要计算费用的集合
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public Double getCostCurrentList(List<OutpatientFeedetailNow> feedetailList){
		Double cost1 = 0.0;
		if(feedetailList.size()>0){
			for (OutpatientFeedetailNow fee : feedetailList) {
				cost1 += fee.getTotCost();
			}
		}
		BigDecimal bg = new BigDecimal(cost1);  
        double cost = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return cost;
	}
	
	/**
	 * @Description  门诊账户处理
	 * @author  marongbin
	 * @createDate： 2016年11月10日 下午3:11:36 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void accountHandle(String idicalrecordId, ChargeVo chargeVo,SysDepartment department,User user,String invoiceType){
		//parameterMap 所有参数
		if(chargeVo.getHospitalAccount()!=null&&chargeVo.getHospitalAccount()!=0){
			//初始化患者账户
			OutpatientAccount account = medicineListInInterDAO.getAccountByMedicalrecord(idicalrecordId);
			//更新门诊账户表
			account.setAccountBalance(account.getAccountBalance()-chargeVo.getHospitalAccount());
			medicineListInInterDAO.save(account);
			
			//添加门诊账户明细表
			OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
			accountrecord.setId(null);//ID
			accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
			accountrecord.setAccountId(account.getId());//门诊账户编号
			accountrecord.setOpertype(4);//操作类型
			accountrecord.setMoney(-chargeVo.getHospitalAccount());//交易金额
			accountrecord.setDeptCode(department.getDeptCode());//相关科室
			accountrecord.setOperCode(user.getAccount());//操作员 
			accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
			accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
			accountrecord.setValid(0);//是否有效
			accountrecord.setInvoiceType(invoiceType);//发票类型
			accountrecord.setCreateUser(user.getAccount());
			accountrecord.setCreateDept(department.getDeptCode());
			accountrecord.setCreateTime(DateUtils.getCurrentTime());
			accountrecord.setUpdateUser(user.getAccount());
			accountrecord.setUpdateTime(DateUtils.getCurrentTime());
			medicineListInInterDAO.save(accountrecord);
		}
	}
	/**
	 * @Description 添加发票明细
	 * @author  marongbin
	 * @createDate： 2016年11月10日 下午3:41:18 
	 * @modifier 
	 * @modifyDate：
	 * @param：  minfeeStatCode 统计大类实体
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void addInvoiceDetial(Double cost,String feeStatCode,String feeStatName,String invoiceNo,String deptId,String deptName,String userId,String invoiceSeq,int a,User user2,SysDepartment department){
		FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
		invoicedetail.setId(null);//ID
		invoicedetail.setInvoiceNo(invoiceNo);//发票号
		invoicedetail.setTransType(1);//正反类型1正2反
		invoicedetail.setInvoCode(feeStatCode);//发票科目代码
		invoicedetail.setInvoName(feeStatName);//发票科目名称
		invoicedetail.setPubCost(0.00);//可报效金额
		invoicedetail.setOwnCost(cost);//不可报效金额(自费金额)
		invoicedetail.setPayCost(0.00);//自付金额
		invoicedetail.setDeptCode(deptId);//执行科室ID
		invoicedetail.setDeptName(deptName);//执行科室名称
		invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
		invoicedetail.setOperCode(userId);//操作人
		invoicedetail.setBalanceFlag(0);//日结状态
		invoicedetail.setBalanceNo(null);//日结标识号
		invoicedetail.setBalanceOpcd(null);//日结人
		invoicedetail.setBalanceDate(null);//日结时间
		invoicedetail.setCancelFlag(1);//状态
		invoicedetail.setInvoiceSeq(invoiceSeq);//发票序号
		invoicedetail.setInvoSequence(a+"");//发票内流水号
		invoicedetail.setCreateUser(user2.getAccount());
		invoicedetail.setCreateDept(department.getDeptCode());
		invoicedetail.setCreateTime(DateUtils.getCurrentTime());
		invoicedetail.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
		invoicedetail.setHospitalID(HisParameters.CURRENTHOSPITALID);
		invoicedetail.setAreaCode(HisParameters.CURRENTHOSPITALCODE);
		medicineListInInterDAO.save(invoicedetail);
	}
	
	/**
	 * @Description  结算信息
	 * @author  marongbin
	 * @createDate： 2016年11月10日 下午4:16:18 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void businessInvoiceInfo(Double cost,String clicnicCode, String invoiceNo,String invoiceSeq,String invoiceComb,ChargeVo chargeVo,RegistrationNow registerInfo,String wondowType,User user2,SysDepartment department,String userId){
		FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
		invoiceInfo.setId(null);
		invoiceInfo.setInvoiceNo(invoiceNo);//发票号
		invoiceInfo.setTransType(1);//交易类型
		invoiceInfo.setCardNo(registerInfo.getMidicalrecordId());//病历号
		invoiceInfo.setPaykindCode(chargeVo.getPink());//结算类别
		invoiceInfo.setRegDate(registerInfo.getRegDate());//挂号日期
		invoiceInfo.setName(registerInfo.getPatientName());//患者姓名
		invoiceInfo.setPactCode(registerInfo.getPactCode());//合同单位代码
		invoiceInfo.setPactName(registerInfo.getPactName());//合同单位名称
		invoiceInfo.setTotCost(cost);//总金额
		invoiceInfo.setPayCost(0.0);//自付金额
		invoiceInfo.setRealCost(cost);//实付金额
		invoiceInfo.setOwnCost(cost);
		invoiceInfo.setPubCost(cost);
		invoiceInfo.setOperCode(userId);//结算人
		invoiceInfo.setClinicCode(clicnicCode);//门诊号
		invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
		invoiceInfo.setExamineFlag(0);//团体/个人
		invoiceInfo.setCancelFlag(1);//有效
		invoiceInfo.setDrugWindow(wondowType);//发药窗口
		invoiceInfo.setCancelInvoice(null);//作废票据号
		invoiceInfo.setCancelCode(null);//作废操作员
		invoiceInfo.setCancelDate(null);//作废时间
		invoiceInfo.setInvoiceSeq(invoiceSeq);//发票序号
		invoiceInfo.setInvoiceComb(invoiceComb);//一次收费序号
		invoiceInfo.setExtFlag(1);//拓展标示 1 自费
		invoiceInfo.setCreateUser(user2.getAccount());
		invoiceInfo.setCreateDept(department.getDeptCode());
		invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
		invoiceInfo.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
		invoiceInfo.setHospitalId(HisParameters.CURRENTHOSPITALID);
		invoiceInfo.setAreaCode(HisParameters.CURRENTHOSPITALCODE);
		BusinessDictionary dictionary = innerCodeDao.getDictionaryByCode("paykind", chargeVo.getPink());
		if(dictionary!=null){
			invoiceInfo.setPaykindName(dictionary.getName());
		}
		medicineListInInterDAO.save(invoiceInfo);
	}
	/**
	 * @Description 门诊收费处理
	 * @author  marongbin
	 * @createDate： 2016年11月10日 下午4:23:35 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void handleOutpatientFeedetail(OutpatientFeedetailNow FNlist,User user2,SysDepartment department,String invoiceNo,String invoiceSeq,String feeStatCode,int a){
		FNlist.setId(FNlist.getId());//门诊收费表ID
		FNlist.setPayFlag(1);//收费状态
		if(FNlist.getQty()!=null){
			FNlist.setNobackNum(FNlist.getQty());//可退数量
		}
		FNlist.setCreateUser(user2.getAccount());//操作人
		FNlist.setCreateDept(department.getDeptCode());//缠
		FNlist.setFeeDate(DateUtils.getCurrentTime());
		FNlist.setFeeCpcd(user2.getAccount());
		FNlist.setFeeCpcdname(user2.getName());
		FNlist.setInvoSequence(a+"");
		FNlist.setFeeDate(DateUtils.getCurrentTime());
		FNlist.setCreateTime(DateUtils.getCurrentTime());
		FNlist.setInvoCode(feeStatCode);
		FNlist.setPayCost(0.00);//自付金额，和医保相关
		FNlist.setPubCost(0.00);//公费金额（可报销金额）
		FNlist.setOwnCost(FNlist.getTotCost());
		//根据统计大类的encode获取发票号
		FNlist.setInvoiceNo(invoiceNo);//发票号
		//根据发票号获取发票序号
		FNlist.setInvoiceSeq(invoiceSeq);//发票序号
		medicineListInInterDAO.update(FNlist);//保存门诊处方
	}
	/**
	 * @Description 保存支付情况表
	 * @author  marongbin
	 * @createDate： 2016年11月10日 下午4:58:23 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void saveBusinessPay(Double cost,ChargeVo chargeVo,User user2,SysDepartment department,String invoiceNo,String invoiceSeq,String invoiceComb){
		//保存支付情况表
		Double[] arr = new Double[]{chargeVo.getCash(),chargeVo.getBankCard(),chargeVo.getCheck(),chargeVo.getHospitalAccount()};
		for(int z=0;z<arr.length;z++){
			if(arr[z]!=null){
				BusinessPayModeNow payMode = new BusinessPayModeNow();
				payMode.setInvoiceNo(invoiceNo);//发票号
				payMode.setTransType(1);//交易类型
				payMode.setOperCode(user2.getAccount());//结算人
				payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
				payMode.setBalanceFlag(0);//是否结算
				payMode.setCancelFlag(1);//状态1正常
				payMode.setInvoiceSeq(invoiceSeq);//发票序号合集
				payMode.setInvoiceComb(invoiceComb);//一次收费序号
				payMode.setCreateUser(user2.getAccount());
				payMode.setCreateDept(department.getDeptCode());
				payMode.setCreateTime(DateUtils.getCurrentTime());
				payMode.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
				if(z==0){//支付方式
					payMode.setModeCode("CA");
					payMode.setTotCost(chargeVo.getCash());//应付金额
					payMode.setRealCost(chargeVo.getCash());//实付金额
					payMode.setModeName("现金");
				}
				if(z==1){
					payMode.setModeCode("DB");
					payMode.setTotCost(chargeVo.getBankCard());//应付金额
					payMode.setRealCost(chargeVo.getBankCard());//实付金额
					payMode.setModeName("银行卡");
				}
				if(z==2){
					payMode.setModeCode("CH");
					payMode.setTotCost(chargeVo.getCheck());//应付金额
					payMode.setRealCost(chargeVo.getCheck());//实付金额
					payMode.setBankName(chargeVo.getBankUniti());//开户银行名称
					payMode.setAccount(chargeVo.getBanki());//开户账户
					payMode.setCheckNo(chargeVo.getBankNo());//支票号
					payMode.setModeName("支票");
				}
				if(z==3){
					payMode.setModeCode("YS");
					payMode.setTotCost(chargeVo.getHospitalAccount());//应付金额
					payMode.setRealCost(chargeVo.getHospitalAccount());//实付金额
					payMode.setModeName("用户账户");
				}
				payMode.setHospitalID(HisParameters.CURRENTHOSPITALID);
				payMode.setAreaCode(HisParameters.CURRENTHOSPITALCODE);
				medicineListInInterDAO.save(payMode);
			}
		}
	}
	/**
	 * @Description 获取统计大类的code
	 * @author  marongbin
	 * @createDate： 2016年11月10日 下午6:02:48 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public MinfeeStatCode getFeeStatCode(OutpatientFeedetailNow fee){
		//根据最小费用代码ID查询到最小费用代码的encode
		BusinessDictionary drugminimumcost =  innerCodeDao.getDictionaryByCode("drugMinimumcost",fee.getFeeCode());
		//根据最小费用代码的encode查询统计大类中统计大类的encode和name
		MinfeeStatCode minfeeStatCode = medicineListInInterDAO.minfeeStatCodeByEncode(drugminimumcost.getEncode());
		return minfeeStatCode;
	}
	/**
	 * @Description  修改发票和添加发票使用记录
	 * @author  marongbin
	 * @createDate： 2016年11月11日 上午9:26:36 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void updateAndSaveInvoice(SysEmployee employee,String invoiceNo,String invoiceType,SysDepartment department,String invoiceNoId){
		//修改发票
		medicineListInInterDAO.saveInvoiceFinance(employee.getJobNo(),invoiceNo,invoiceType,invoiceNoId);
		//添加发票使用记录
		InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
		usageRecord.setId(null);
		usageRecord.setUserId(employee.getJobNo());
		usageRecord.setUserCode(employee.getCode());
		usageRecord.setUserType(2);
		usageRecord.setInvoiceNo(invoiceNo);
		usageRecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		usageRecord.setCreateDept(department.getDeptCode());
		usageRecord.setCreateTime(DateUtils.getCurrentTime());
		usageRecord.setUserName(employee.getName());
		usageRecord.setInvoiceUsestate(1);
		medicineListInInterDAO.save(usageRecord);
	}
	/**
	 * @Description 摆药单
	 * @author  marongbin
	 * @createDate： 2016年11月11日 上午11:40:37 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public Map<String,List<StoTerminal>> stoRecipe(String pink,List<OutpatientFeedetailNow> feedetail,String recipeNo,String clinicCode,String invoiceNo,RegistrationNow registerInfo,SysEmployee employee,SysDepartment department,User user2,Map<String,List<StoTerminal>> map){
		String wondowType = "";
		String execDeptCode = "";
		List<StoTerminal> list = new ArrayList<StoTerminal>();
		for (OutpatientFeedetailNow out : feedetail) {
			if("1".equals(out.getDrugFlag())){
				if (StringUtils.isNotBlank(out.getExecDpcd())) {
					execDeptCode = out.getExecDpcd();
					break;
				}
			}
		}
		if(feedetail.size()>0&&StringUtils.isNotBlank(execDeptCode)){
			Double recipeCost = 0.0;//处方金额(零售金额)
			Double recipeQty = 0.0;//处方中药品数量
			Double sumDays = 0.0;//
			StoRecipeNow stoRecipe = new StoRecipeNow();
			stoRecipe.setId(null);
			stoRecipe.setAreaCode(HisParameters.HOSPITALAREA);
			stoRecipe.setHospitalID(HisParameters.CURRENTHOSPITALID);
			stoRecipe.setDrugDeptCode(execDeptCode);//执行科室/发放药房
			stoRecipe.setRecipeNo(recipeNo);//处方号
			stoRecipe.setClassMeaningCode("1");//出库申请分类
			stoRecipe.setTransType(1);//交易类型
			stoRecipe.setRegDate(registerInfo.getRegDate());//挂号日期
			stoRecipe.setRecipeState(0);//处方状态
			stoRecipe.setClinicCode(registerInfo.getClinicCode());//门诊号
			stoRecipe.setCardNo(registerInfo.getMidicalrecordId());//病例号
			stoRecipe.setPatientName(registerInfo.getPatientName());//患者姓名
			stoRecipe.setSexCode(registerInfo.getPatientSex());//性别
			stoRecipe.setBirthday(registerInfo.getPatientBirthday());//出生日期
			stoRecipe.setPaykindCode(pink);//结算类别
			stoRecipe.setDeptCode(registerInfo.getDeptCode());//患者科室代码
			stoRecipe.setDoctCode(registerInfo.getDoctCode());//开方医生
			stoRecipe.setDoctDept(registerInfo.getDeptCode());//开方医生所在科室
			stoRecipe.setDoctName(feedetail.get(0).getDoctCodename());//开方医生姓名
			stoRecipe.setDoctDeptName(feedetail.get(0).getDoctDeptname());//开方医生所在科室名称
			stoRecipe.setFeeOper(employee.getJobNo());//收费人
			stoRecipe.setFeeDate(DateUtils.getCurrentTime());//收费时间
			stoRecipe.setInvoiceNo(invoiceNo);//发票号
			stoRecipe.setFeeOperName(user2.getName());//收费人姓名
			for(OutpatientFeedetailNow modlfeede : feedetail){
				if("1".equals(modlfeede.getDrugFlag())){
					recipeCost = recipeCost + modlfeede.getTotCost();
					recipeQty = recipeQty + modlfeede.getQty();
				}
				if("16".equals(modlfeede.getClassCode())&&modlfeede.getDays()!=null){//系统类别 草药
					sumDays = sumDays + modlfeede.getDays();
				}else{
					sumDays=1.0;
				}
			}
			BigDecimal bg = new BigDecimal(recipeCost);  
	        double recipeCost1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	        BigDecimal bg1 = new BigDecimal(recipeQty);  
	        double recipeQty1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	        BigDecimal bg2 = new BigDecimal(sumDays);  
	        double sumDays1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			stoRecipe.setRecipeCost(recipeCost1);//处方金额(零售金额)
			stoRecipe.setRecipeQty(recipeQty1);//处方中药品数量
			stoRecipe.setSumDays(sumDays1);//处方内药品剂数合计
			stoRecipe.setCreateUser(user2.getAccount());
			stoRecipe.setCreateDept(department.getDeptCode());
			stoRecipe.setCreateTime(DateUtils.getCurrentTime());
			stoRecipe.setValidState(1);
			stoRecipe.setModifyFlag(0);
			String key = "";
			//查询配药台，如果存在key相同的值，则使用原来的
			Map<String, List<StoTerminal>> stoTerminalMap = this.getStoTerminal(execDeptCode, registerInfo, feedetail);
			for (Map.Entry<String, List<StoTerminal>> m : stoTerminalMap.entrySet()) {
				if(!map.containsKey(m.getKey())){
					map.put(m.getKey(), m.getValue());
					key = m.getKey();
				}
				key = m.getKey();
			}
			List<StoTerminal> list2 = map.get(key);
			if(list2.size()==1&&list2.size()>0){
				StoTerminal stoTerminalNo = list2.get(0);//发药终端
				stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
				stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
				stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
				stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
				double stoQTY = 0.0;
				if(stoTerminalNo.getDrugQty()!=null){
					stoQTY = stoTerminalNo.getDrugQty();
				}
				stoTerminalNo.setDrugQty(stoQTY+recipeQty1);//更改待发药品数量
				medicineListInInterDAO.update(stoTerminalNo);
			}
			if(list2.size()==2){
				StoTerminal stoTerminalNo = list2.get(0);
				StoTerminal stoTerminal = list2.get(1);
				if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
					StoTerminal stoTerminal1 = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
					stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
					stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
					stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
					stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
				}else{
					stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
					stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
					stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
					stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
				}
				if(stoTerminalNo.getCode()!=null){
					stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()==null?0:stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
					medicineListInInterDAO.update(stoTerminalNo);
				}
			}
			
			String[] split = key.split("_");
			String tcode = split[split.length-1];
			stoRecipe.setDrugedTerminal(tcode);
//			if(!"".equals(wondowType)){
//				wondowType = wondowType + ",";
//			}
//			wondowType = wondowType + stoRecipe.getSendTerminal();
//			medicineListInInterDAO.save(stoRecipe);
		}
		return map;
	}
	/**
	 * @Description  获取配药台
	 * @author  marongbin
	 * @createDate： 2017年2月15日 下午1:52:54 
	 * @modifier 
	 * @modifyDate：
	 * @param execDeptCode
	 * @return: Map<String,List<StoTerminal>>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public Map<String,List<StoTerminal>> getStoTerminal(String execDeptCode,RegistrationNow registerInfo,List<OutpatientFeedetailNow> feedetail){
		Map<String,List<StoTerminal>> map = new HashMap<String,List<StoTerminal>>();
		List<StoTerminal> list = new ArrayList<StoTerminal>();
		//按照特殊收费窗口查找配药台
		Integer itemType = 4;//特殊收费窗口
		String deptCode = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		SpeNalVo speNalVoList = null;
		for (OutpatientFeedetailNow out : feedetail) {
			if("1".equals(out.getDrugFlag())){
				if (StringUtils.isNotBlank(out.getExecDpcd())) {
					speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),deptCode,itemType);
					if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
						break;
					}
				}
			}
		}
		if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
			StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
			if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
				StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
				list.add(0,stoTerminalNo);
				list.add(1,stoTerminal);
				map.put(itemType+"_"+execDeptCode+"_"+deptCode+"_"+stoTerminalNo.getCode(), list);
			}else{
				list.add(0,stoTerminalNo);
				map.put(itemType+"_"+execDeptCode+"_"+deptCode+"_"+stoTerminalNo.getCode(), list);
			}
		}else{
			itemType = 2;//专科
			for (OutpatientFeedetailNow out : feedetail) {
				if("1".equals(out.getDrugFlag())){
					if (StringUtils.isNotBlank(out.getExecDpcd())) {
						speNalVoList = medicineListInInterDAO.querySpeNalVoBy(execDeptCode,registerInfo.getDeptCode(),itemType);
						if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
							break;
						}
					}
				}
			}
			if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
//				stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
				StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
				if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
					StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
//					stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
//					stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
//					stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
//					stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
					list.add(0,stoTerminalNo);
					list.add(1,stoTerminal);
					map.put(itemType+"_"+execDeptCode+"_"+registerInfo.getDeptCode()+"_"+stoTerminalNo.getCode(), list);
				}else{
//					stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
//					stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
//					stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
//					stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
					list.add(0,stoTerminalNo);
					map.put(itemType+"_"+execDeptCode+"_"+registerInfo.getDeptCode()+"_"+stoTerminalNo.getCode(), list);
				}
//				stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
//				medicineListInInterDAO.update(stoTerminalNo);
			}else{
				itemType = 3;//结算类别
				for (OutpatientFeedetailNow out : feedetail) {
					if("1".equals(out.getDrugFlag())){
						if (StringUtils.isNotBlank(out.getExecDpcd())) {
							speNalVoList = medicineListInInterDAO.querySpeNalVoBy(execDeptCode,registerInfo.getPaykindCode(),itemType);
							if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
								break;
							}
						}
					}
				}
				if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
					StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
					if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
						StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
						list.add(0,stoTerminalNo);
						list.add(1,stoTerminal);
						map.put(itemType+"_"+execDeptCode+"_"+registerInfo.getPaykindCode()+"_"+stoTerminalNo.getCode(), list);
					}else{
						list.add(0,stoTerminalNo);
						map.put(itemType+"_"+execDeptCode+"_"+registerInfo.getPaykindCode()+"_"+stoTerminalNo.getCode(), list);
					}
				}else{
					itemType = 5;//挂号级别
					for (OutpatientFeedetailNow out : feedetail) {
						if("1".equals(out.getDrugFlag())){
							if (StringUtils.isNotBlank(out.getExecDpcd())) {
								speNalVoList = medicineListInInterDAO.querySpeNalVoBy(execDeptCode,registerInfo.getReglevlCode(),itemType);
								if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
									break;
								}
							}
						}
					}
					if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
						StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
						if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
							StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
							list.add(0,stoTerminalNo);
							list.add(1,stoTerminal);
							map.put(itemType+"_"+execDeptCode+"_"+registerInfo.getReglevlCode()+"_"+stoTerminalNo.getCode(), list);
						}else{
							list.add(0,stoTerminalNo);
							map.put(itemType+"_"+execDeptCode+"_"+registerInfo.getReglevlCode()+"_"+stoTerminalNo.getCode(), list);
						}
					}else{
						itemType = 1;//特殊药品
						for(OutpatientFeedetailNow modlse :feedetail){
							for (OutpatientFeedetailNow out : feedetail) {
								if("1".equals(out.getDrugFlag())){
									if (StringUtils.isNotBlank(out.getExecDpcd())) {
										speNalVoList = medicineListInInterDAO.querySpeNalVoBy(modlse.getExecDpcd(),modlse.getItemCode(),itemType);
										if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
											break;
										}
									}
								}
							}
							if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
								StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端(获取到的是配药台)
								if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
									StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
									list.add(1,stoTerminal);
									list.add(0,stoTerminalNo);
									map.put(itemType+"_"+execDeptCode+"_"+modlse.getItemCode()+"_"+stoTerminalNo.getCode(), list);
								}else{
									list.add(0,stoTerminalNo);
									map.put(itemType+"_"+execDeptCode+"_"+modlse.getItemCode()+"_"+stoTerminalNo.getCode(), list);
								}
								break;
							}
						}
					}
				}
			}
		}
		if(StringUtils.isBlank(speNalVoList.gettCode())){
			//获取调价方式
			Map<String, String> extendMap = sendWicketInInterDAO.getBusinessExtend(execDeptCode);
			String tjfs = extendMap.get("TJFS");
			//获取未配药的调剂信息
			StoTerminal terminal = medicineListInInterDAO.checkIshadUnsend(feedetail.get(0).getClinicCode(), execDeptCode);
			//判断是否存在未配药的调剂信息
			if(terminal!=null&&StringUtils.isNotBlank(terminal.getId())){
				StoTerminal stoTerminal = medicineListInInterDAO.queryStoTerminal(execDeptCode,tjfs);//配药终端
				StoTerminal stoTerminalNo = medicineListInInterDAO.getStoTerminal(stoTerminal.getCode());//发药终端
				list.add(0,stoTerminalNo);
				list.add(1,stoTerminal);
				map.put(execDeptCode+"_"+stoTerminal.getCode(), list);
			}else{
				StoTerminal stoTerminal = medicineListInInterDAO.queryStoTerminal(execDeptCode,tjfs);//配药终端
				StoTerminal stoTerminalNo = medicineListInInterDAO.getStoTerminal(stoTerminal.getCode());//发药终端
				if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
					StoTerminal repalceStoTerminalNo = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());//代替发药终端
					list.add(0,repalceStoTerminalNo);
					list.add(1,stoTerminal);
					map.put(execDeptCode+"_"+stoTerminal.getCode(), list);
				}else{
					list.add(0,stoTerminalNo);
					list.add(1,stoTerminal);
					map.put(execDeptCode+"_"+stoTerminal.getCode(), list);
				}
			}
		}
		return map;
	}
	/**
	 * @Description 出库申请单
	 * @author  marongbin
	 * @createDate： 2016年11月11日 下午2:01:29 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void drugApplyout(List<OutpatientFeedetailNow> feedetailList,RegistrationNow registerInfo,SysDepartment department,User user2){
		if(feedetailList!=null&&feedetailList.size()>0){
			for (OutpatientFeedetailNow fee : feedetailList) {
				if("1".equals(fee.getDrugFlag())){
					OutpatientFeedetailNow  feedetailLists = medicineListInInterDAO.queryDrugInfoList(fee.getId());
					DrugInfo drugInfo = medicineListInInterDAO.queryDrugInfoById(feedetailLists.getItemCode());
					int applyNumber = Integer.parseInt(medicineListInInterDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
					String yearLast = new SimpleDateFormat("yyMM").format(new Date());
					int value = keyvalueInInterDAO.getVal(department.getDeptCode(),"内部入库申请单号",yearLast);
					String applyBillcode = "0" + yearLast + value;//组成内部入库申请单号
					DrugApplyoutNow drugApplyout = new DrugApplyoutNow();
					drugApplyout.setApplyNumber(applyNumber);//申请流水号
					drugApplyout.setDeptCode(registerInfo.getDeptCode());//申请科室
					drugApplyout.setDrugDeptCode(feedetailLists.getExecDpcd());//发药科室
					drugApplyout.setOpType(1);//操作类型分类
					drugApplyout.setGroupCode(null);//批次号,摆药生成出库记录时反写
					drugApplyout.setDrugCode(feedetailLists.getItemCode());//药品id
					drugApplyout.setTradeName(drugInfo.getName());//药品名
					drugApplyout.setBatchNo(null);//批号 ,摆药生成出库记录时反写
					drugApplyout.setDrugType(drugInfo.getDrugType());//药品类别
					drugApplyout.setDrugQuality(drugInfo.getDrugNature());//药品性质
					drugApplyout.setSpecs(drugInfo.getSpec());//规格
					BusinessDictionary packunit =  innerCodeDao.getDictionaryByCode(HisParameters.DRUGPACKUNIT,drugInfo.getDrugPackagingunit());
					BusinessDictionary miniunit =  innerCodeDao.getDictionaryByCode(HisParameters.DRUGMINUNIT,drugInfo.getUnit());
					String strDoseUnit = "";
					if(StringUtils.isNotBlank(feedetailLists.getDoseUnit())){
						BusinessDictionary doseUnit =  innerCodeDao.getDictionaryByCode(HisParameters.DOSEUNIT,feedetailLists.getDoseUnit());
						drugApplyout.setDoseUnit(doseUnit.getName());//计量单位
					}
					drugApplyout.setPackUnit(packunit.getName());//包装单位
					drugApplyout.setPackQty(drugInfo.getPackagingnum());//包装数量
					drugApplyout.setMinUnit(miniunit.getName());//最小单位
					drugApplyout.setShowFlag(feedetailLists.getExtFlag3());
					//显示单位为对应单位的name
					if(feedetailLists.getExtFlag3() == 1){
						drugApplyout.setShowUnit(packunit.getName());
					}
					if(feedetailLists.getExtFlag3() == 0){
						drugApplyout.setShowUnit(miniunit.getName());
					}
					//0是最小单位，1是包装单位
					drugApplyout.setApplyNum(feedetailLists.getQty());//申请数量
					drugApplyout.setValidState(1);//有效
					drugApplyout.setRetailPrice(drugInfo.getDrugRetailprice());//零售价
					drugApplyout.setWholesalePrice(drugInfo.getDrugWholesaleprice());//批发价
					drugApplyout.setApplyBillcode(applyBillcode);//申请单号
					drugApplyout.setApplyOpercode(registerInfo.getDoctCode());//申请人
					drugApplyout.setApplyDate(DateUtils.getCurrentTime());//申请日期
					drugApplyout.setApplyState(0);//申请状态
					drugApplyout.setBillclassCode("1");//查询发药药房对应的发药台对应的摆药单类型
					drugApplyout.setDays(feedetailLists.getDays());//申请付数
					drugApplyout.setPreoutFlag(1);//预扣库存，来源于预扣库存的参数，参数设置预扣库存是，此处为是，否则为否。预扣库存的情况下，采取更新仓库主表中的预扣库存数量。
					drugApplyout.setChargeFlag(1);//已收费
					drugApplyout.setPatientId(registerInfo.getClinicCode());//患者门诊号
					drugApplyout.setPatientDept(registerInfo.getDeptCode());//患者科室
					if(feedetailLists.getDoseOnce() != null){
						drugApplyout.setDoseOnce(feedetailLists.getDoseOnce().doubleValue());//每次剂量
					}
					drugApplyout.setUsageCode(feedetailLists.getUsageCode());//用法代码
					drugApplyout.setUseName(feedetailLists.getUseName());//用法名称
					drugApplyout.setDfqFreq(feedetailLists.getFrequencyCode());//频次代码
					drugApplyout.setOrderType("1");//医嘱类别
					drugApplyout.setMoOrder(feedetailLists.getMoOrder());//医嘱流水号
					drugApplyout.setCombNo(feedetailLists.getCombNo());//组合号 
					drugApplyout.setRecipeNo(feedetailLists.getRecipeNo());//处方号
					int sequenceNo = 0;
					if(feedetailLists.getSequenceNo() != null){
						sequenceNo = feedetailLists.getSequenceNo();
					}
					drugApplyout.setSequenceNo(sequenceNo);//处方内流水号
					drugApplyout.setCreateUser(department.getDeptCode());
					drugApplyout.setCreateDept(department.getDeptCode());
					drugApplyout.setCreateTime(DateUtils.getCurrentTime());
					drugApplyout.setPrintEmplName(user2.getName());
					drugApplyout.setDeptName(department.getDeptName());
					drugApplyout.setDfqCexp(fee.getFrequencyName());
					drugApplyout.setHospitalId(HisParameters.CURRENTHOSPITALID);
					drugApplyout.setAreaCode(HisParameters.CURRENTHOSPITALCODE);
					//drugProperties
					String drugQualityName = "";
					String drugQuality = drugApplyout.getDrugQuality();
					if(StringUtils.isNotBlank(drugQuality)){//渲染药品性质名称
						List<BusinessDictionary> list = innerCodeDao.getDictionary("drugProperties");
						for (BusinessDictionary businessDictionary : list) {
							if(drugQuality.equals(businessDictionary.getEncode())){
								drugApplyout.setDrugQulityName(businessDictionary.getName());
							}
						}
					}
					medicineListInInterDAO.save(drugApplyout);
				}
			}
		}
		
	}
	
	/**
	 * @Description 预扣库存
	 * @author  marongbin
	 * @createDate： 2016年11月11日 下午2:56:19 
	 * @modifier 
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	public void outFeedetial(List<OutpatientFeedetailNow> feedetailList){
		if(feedetailList!=null&&feedetailList.size()>0){
			for (OutpatientFeedetailNow fee : feedetailList) {
				if("1".equals(fee.getDrugFlag())){
					OutpatientFeedetailNow outFeedetail = medicineListInInterDAO.queryOutFeedetail(fee.getId());
					businessStockInfoInInterService.withholdStock(outFeedetail.getExecDpcd(),outFeedetail.getItemCode(),outFeedetail.getQty(),outFeedetail.getExtFlag3().toString());
				}
			}
		}
	}

	@Override
	public Map<String, String> getInvoiceNoByCode(String id, String invoiceType,int num) {
		return medicineListInInterDAO.queryFinanceInvoiceNoByNum(id, invoiceType,num);
	}

	@Override
	public List<OutpatientFeedetailNow> getchangeByconts(List<OutpatientFeedetailNow> feelist, String feeIds,String contsCode) {
		if(feelist==null||feelist.size()<1){
			if(StringUtils.isNotBlank(feeIds)){
				feelist = medicineListInInterDAO.findByIds(feeIds);
			}
		}
		BusinessContractunit contractunit = medicineListInInterDAO.queryCountByPaykindCode(contsCode);
		for (OutpatientFeedetailNow fee : feelist) {
			fee.setPayCost(fee.getTotCost()*contractunit.getPayRatio());
			fee.setPubCost(fee.getTotCost()*contractunit.getPubRatio());
			fee.setOwnCost(fee.getTotCost()*contractunit.getOwnRatio());
			fee.setEcoCost(fee.getTotCost()*contractunit.getEcoRatio());
		}
		return feelist;
	}

	@Override
	public Map<String, String> changeInvoiceNO(String id, String invoiceNo) {
		Map<String,String> map = new HashMap<String,String>();
		List<FinanceInvoice> fi = medicineListInInterDAO.getFinInvoice(HisParameters.OUTPATIENTINVOICETYPE, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		String zero = "";//补位的0
		int a = 0;
		boolean flag = false;
		if(fi!=null&&fi.size()>0){
			int invoice = Integer.parseInt(invoiceNo.substring(1));//待修改发票号后面的数字
			for (FinanceInvoice f : fi) {
				a+=1;
				//终止号
				int endNo = Integer.parseInt(f.getInvoiceEndno().substring(1));
				//起始号
				int startNo = Integer.parseInt(f.getInvoiceStartno().substring(1));
				if(Math.min(endNo, invoice)==Math.max(startNo, invoice)){//当待修改的发票号在该组的时候
					flag = true;
					int length = 13 - ((invoice-1)+"").length();
					for(int i=0;i<length;i++){
						zero += "0";
					}
					//拼接发票号
					String newInvs = f.getInvoiceUsedno().substring(0, 1)+zero+(invoice-1);
					zero = "";
					f.setInvoiceUsedno(newInvs);
					medicineListInInterDAO.update(f);
					//之前的发票退库
					FinanceInvoiceStorage store = new FinanceInvoiceStorage();
					store.setInvoiceEndno(newInvs);
					store.setInvoiceUsedno(f.getInvoiceUsedno());
					int usedno = Integer.parseInt(f.getInvoiceUsedno().substring(1));
					int slength = 13 - ((usedno+1)+"").length();
					for(int i=0;i<slength;i++){
						zero += "0";
					}
					String newstoreStart = f.getInvoiceUsedno().substring(0, 1)+zero+(usedno+1);
					store.setInvoiceStartno(newstoreStart);
					store.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					store.setInvoiceCode(HisParameters.OUTPATIENTINVOICETYPE);
					store.setInvoiceType(HisParameters.OUTPATIENTINVOICETYPE);
					store.setInvoiceUseState(0);
					medicineListInInterDAO.save(store);
					map.put("resMsg", "success");
					map.put("resCode", "修改成功！");
					break;
				}
				
			}
			if(flag&&a>1){
				for (int i = 0; i <a; i++) {
					FinanceInvoice invoice2 = fi.get(i);
					invoice2.setInvoiceUsestate(-1);
					medicineListInInterDAO.update(invoice2);
					//之前的发票退库
					FinanceInvoiceStorage store = new FinanceInvoiceStorage();
					store.setInvoiceEndno(invoice2.getInvoiceEndno());
					store.setInvoiceUsedno(invoice2.getInvoiceUsedno());
					int usedno = Integer.parseInt(invoice2.getInvoiceUsedno().substring(1));
					int slength = 13 - ((usedno+1)+"").length();
					for(int j=0;j<slength;j++){
						zero += "0";
					}
					String newstoreStart = invoice2.getInvoiceUsedno().substring(0, 1)+zero+(usedno+1);
					store.setInvoiceStartno(newstoreStart);
					store.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					store.setInvoiceCode(HisParameters.OUTPATIENTINVOICETYPE);
					store.setInvoiceType(HisParameters.OUTPATIENTINVOICETYPE);
					store.setInvoiceUseState(0);
					medicineListInInterDAO.save(store);
				}
			}
			if(!flag){
				map.put("resMsg", "success");
				map.put("resCode", "该号未在领取号组里，不可修改！");
			}
		}
		return map;
	}

	/**  
	 * 
	 * 保存药品明细信息
	 * @Author: zxl
	 * @CreateDate: 2017年7月4日 下午6:07:53 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月4日 下午6:07:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void save(InpatientMedicineListNow med) {
		medicineListInInterDAO.save(med);
		
	}

}









