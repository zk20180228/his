package cn.honry.finance.medicinelist.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessPayMode;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugStockinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.OutpatientItemlist;
import cn.honry.base.bean.model.OutpatientMedicinelist;
import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.medicinelist.dao.MedicinelistDAO;
import cn.honry.finance.medicinelist.dao.StoRecipeDAO;
import cn.honry.finance.medicinelist.service.MedicinelistService;
import cn.honry.finance.medicinelist.vo.FeeCodeVo;
import cn.honry.finance.medicinelist.vo.RecipeNoVo;
import cn.honry.finance.medicinelist.vo.RecipedetailVo;
import cn.honry.finance.medicinelist.vo.SpeNalVo;
import cn.honry.finance.medicinelist.vo.UndrugAndWare;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.invoiceInfo.InvoiceInfoInInterDAO;
import cn.honry.inner.baseinfo.invoicedetail.InvoicedetailInInterDAO;
import cn.honry.inner.baseinfo.payMode.dao.PayModeInInterDAO;
import cn.honry.inner.baseinfo.stack.dao.StackInInterDAO;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.inner.drug.applyout.dao.ApplyoutInInterDAO;
import cn.honry.inner.drug.outstore.service.OutStoreInInterService;
import cn.honry.inner.drug.undrug.dao.UndrugInInterDAO;
import cn.honry.inner.drug.undrugZtinfo.dao.UndrugZtinfoInInterDAO;
import cn.honry.inner.outpatient.feedetail.dao.FeedetailTecIntDAO;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.outpatient.recipedetail.dao.RecipedetailInInterDAO;
import cn.honry.inner.outpatient.register.dao.RegisterInfoInInterDAO;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.technical.mat.service.MatOutPutInInterService;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.inner.technical.taDetail.dao.TecTaDetailInInterDAO;
import cn.honry.inner.vo.MedicalVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import cn.honry.patinent.account.dao.AccountrepayDetailDAO;

@Service("medicinelistService")
@Transactional
@SuppressWarnings({ "all" })
public class MedicinelistServiceImpl implements MedicinelistService{

	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO codeInInterDAO;

	@Autowired
	@Qualifier(value = "medicinelistDAO")
	private MedicinelistDAO medicinelistDAO;
	@Autowired
	@Qualifier(value = "stackInInterDAO")
	private StackInInterDAO stackDAO;//组套DAO
	@Autowired
	@Qualifier(value = "stackinfoInInterDAO")
	private StackinfoInInterDAO stackinfoDAO;//组套详情DAO
	@Autowired
	@Qualifier(value = "recipedetailInInterDAO")
	private RecipedetailInInterDAO recipedetailDAO;//医嘱DAO
	@Autowired
	@Qualifier(value = "feedetailTecIntDAO")
	private FeedetailTecIntDAO feedetailDAO;//医嘱明细DAO
	@Autowired
	@Qualifier(value = "registerInfoInInterDAO")
	private RegisterInfoInInterDAO registerInfoDAO;//挂号DAO
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "invoicedetailInInterDAO")
	private InvoicedetailInInterDAO invoicedetailDAO;//发票明细DAO
	@Autowired
	@Qualifier(value = "tecTaDetailInInterDAO")
	private TecTaDetailInInterDAO tecTaDetailDAO;//终端
	@Autowired
	@Qualifier(value = "invoiceInfoInInterDAO")
	private InvoiceInfoInInterDAO invoiceInfoDAO;//发票结算
	@Autowired
	@Qualifier(value = "payModeInInterDAO")
	private PayModeInInterDAO payModeDAO;//支付方式
//	@Autowired
//	@Qualifier(value = "repayDetailDAO")
//	private AccountrepayDetailDAO repayDetailDAO;//账户明细
	/*@Autowired
	@Qualifier(value = "detailDAO")
	private AccountDetailDAO detailDAO;//账户信息
*/	@Autowired
	@Qualifier(value = "stoRecipeDAO")
	private StoRecipeDAO stoRecipeDAO;//处方调剂头表
	@Autowired
	@Qualifier(value = "applyoutInInterDAO")
	private ApplyoutInInterDAO applyoutDAO;//出库申请表
	@Autowired
	@Qualifier(value = "keyvalueInInterDAO")
	private KeyvalueInInterDAO keyvalueDAO;//参数表
	@Autowired
	@Qualifier(value = "outstoreInInterService")
	private OutStoreInInterService outstoreService;
	@Autowired
	@Qualifier(value = "matOutPutInInterService")
	private MatOutPutInInterService matOutPutService;
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListDAO;
	@Autowired
	@Qualifier(value = "undrugZtinfoInInterDAO")
	private UndrugZtinfoInInterDAO undrugZtinfoInInterDAO;//复合项目
	@Autowired
	@Qualifier(value = "undrugInInterDAO")
	private UndrugInInterDAO undrugInInterDAO;//复合项目
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;
	@Override
	public void removeUnused(String id) {
	}
	//公共方法（根据id查询）
	@Override
	public OutpatientFeedetail get(String id) {
		return null;
	}
	//公共方法（修改）
	@Override
	public void saveOrUpdate(OutpatientFeedetail entity) {
	}
	
	@Override
	public SysEmployee queryEmployee(String id) {
		SysEmployee employee = medicinelistDAO.queryEmployee(id);
		return employee;
	}
	@Override
	public Map<String, String> queryFinanceInvoiceNo(String id,String invoiceType) {
		//获得领取发票的那一条信息
		Map<String,String> map = medicineListDAO.queryFinanceInvoiceNo(id,invoiceType);
		return map;
	}
	@Override
	public List<Patient> vagueFindPatientById(String patientNo) {
		return medicinelistDAO.vagueFindPatientById(patientNo);
	}
	@Override
	public Map<String, Object> queryBlhOrCardNo(OutpatientFeedetailNow feedetail) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(feedetail.getCardNo())){//判断就诊卡号是否为空
			//根据就诊卡查询病历号
			PatientIdcard patientIdcard = medicinelistDAO.queryPatientIdcardByBlh(feedetail.getCardNo());
			if(patientIdcard.getPatient()==null){
				map.put("resMsg", "error");
				map.put("resCode", "患者不存在，请检查就诊卡号是否有误！");
				return map;
			}
			feedetail.setPatientNo(patientIdcard.getPatient().getMedicalrecordId());
		}
		//根据病历号查询患者信息
		List<Patient> patientList = medicinelistDAO.findPatientById(feedetail.getPatientNo());
		if(patientList.size()==0){//没有患者时
			map.put("resMsg", "error");
			map.put("resCode", "此病历号不存在患者，请重新输入");
		}else{//有患者时
			//验证此病历号是否存在有效的就诊卡号
			PatientIdcard idcard = medicinelistDAO.queryIdcard(feedetail.getPatientNo());
			if(StringUtils.isBlank(idcard.getId())){//就诊卡无效时
				map.put("resMsg", "error");
				map.put("resCode", "就诊卡无效");
			}else{//就诊卡有效时
				//根据患者Id查询是否在黑名单中
				PatientBlackList blackList = medicinelistDAO.queryBlackList(feedetail.getPatientNo());
				if(StringUtils.isNotBlank(blackList.getId())){//判断患者是否在黑名单中
					map.put("resMsg", "error");
					map.put("resCode", "该患者在黑名单中");
				}else{
					//查询挂号有效期（在系统参数中）
					HospitalParameter parameter = medicinelistDAO.queryParameterInfoTime();
					//根据患者病历号查询该患者挂号信息和挂号有效期
					List<RegistrationNow> infoList = medicinelistDAO.findRegisterInfo(feedetail.getPatientNo(),parameter.getParameterValue());
					if(infoList.size()==0){//有患者没有挂号信息时
						map.put("resMsg", "error");
						map.put("resCode", "该患者没有挂号信息");
					}else if(infoList.size()==1){
						map.put("resMsg", "succ");
						map.put("info",infoList.get(0));
					}else if(infoList.size()>1){
						map.put("resMsg", "success");
						map.put("infoList",infoList);
					}
				}
			}
		}
		return map;
	}
	@Override
	public List<SysDepartment> findDepartment() {
		return medicinelistDAO.findDepartment();
	}
	
	@Override
	public List<BusinessContractunit> findContractunit() {
		return medicinelistDAO.findContractunit();
	}
	
	@Override
	public List<SysEmployee> findEmployee() {
		return medicinelistDAO.findEmployee();
	}
	
	@Override
	public List<RegistrationNow> findRegisterInfo(String patientNo) {
		//查询挂号有效期（在系统参数中）
		HospitalParameter parameter = medicinelistDAO.queryParameterInfoTime();
		return medicinelistDAO.findRegisterInfo(patientNo,parameter.getParameterValue());
	}
	
	@Override
	public List<SysEmployee> findEmployeeList(String regDpcd) {
		return medicinelistDAO.findEmployeeList(regDpcd);
	}
	@Override
	public List<RecipedetailVo> findFeedetailStatistics(String clinicCode,String regDpcdName, String doctCodeName) {
		//根据门诊号查询出处方号
		List<RecipeNoVo> feedetailListRecipeNo = medicinelistDAO.findFeedetailRecipeNo(clinicCode);
		//new 显示字段列表VO（List）
		List<RecipedetailVo> recVoList = new  ArrayList<RecipedetailVo>();
		//定义总钱数
		Double sumMoney = 0.00;
		if(feedetailListRecipeNo.size()>0){//判断是否查到处方号，过没有会新建一条状态是新开，总金额为0的数据
			for(RecipeNoVo modls : feedetailListRecipeNo){//遍历得到的处方号
				//根据遍历得到的处方号查询该处方号下的处方
				List<OutpatientFeedetailNow> feedetailList = medicinelistDAO.queryFeedetailList(modls.getRecipeNo());
				if(feedetailList.size()==1){//当该处方号下只有一条处方时
					//new 显示字段VO（实体）
					RecipedetailVo recipedetailVo = new RecipedetailVo();
					recipedetailVo.setRecipeNo(feedetailList.get(0).getRecipeNo());
					recipedetailVo.setDept(modls.getDept());
					recipedetailVo.setEmp(modls.getEmp());
					recipedetailVo.setRecipedStatus(0);
					BigDecimal sumMoneys = new   BigDecimal(feedetailList.get(0).getTotCost());  
					Double sumMoneyss = sumMoneys.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					recipedetailVo.setSumMoney(sumMoneyss);
					recipedetailVo.setRegDate(feedetailList.get(0).getRegDate());
					recipedetailVo.setOperDate(feedetailList.get(0).getOperDate());
					recipedetailVo.setDeptName(feedetailList.get(0).getRegDpcdname());
					recipedetailVo.setEmpName(feedetailList.get(0).getDoctCodename());
					recVoList.add(recipedetailVo);
				}else if(feedetailList.size()>1){//当该处方下有多条处方时，进行相加
					//new 显示字段VO（实体）
					RecipedetailVo recipedetailVo = new RecipedetailVo();
					recipedetailVo.setRecipeNo(feedetailList.get(0).getRecipeNo());
					recipedetailVo.setDept(modls.getDept());
					recipedetailVo.setEmp(modls.getEmp());
					recipedetailVo.setRecipedStatus(0);
					for(OutpatientFeedetailNow modl :feedetailList ){
						sumMoney = sumMoney +modl.getTotCost();
					}
					BigDecimal sumMoneys = new   BigDecimal(sumMoney);  
					Double sumMoneyss = sumMoneys.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					recipedetailVo.setSumMoney(sumMoneyss);
					recipedetailVo.setRegDate(feedetailList.get(0).getRegDate());
					recipedetailVo.setOperDate(feedetailList.get(0).getOperDate());
					recipedetailVo.setDeptName(feedetailList.get(0).getRegDpcdname());
					recipedetailVo.setEmpName(feedetailList.get(0).getDoctCodename());
					recVoList.add(recipedetailVo);
					sumMoney = 0.00;
				}
			}
		}else{
			if(StringUtils.isNotBlank(clinicCode)){//当没有查到处方号时，会新建一个处方信息
				//new 显示字段VO（实体）
				RecipedetailVo recipedetailVo = new RecipedetailVo();
				recipedetailVo.setRecipeNo(null);
				recipedetailVo.setDept("");//无实际意义
				recipedetailVo.setDeptName(regDpcdName);//开立医生科室name
				recipedetailVo.setEmp(doctCodeName);//无实际意义
				recipedetailVo.setEmpName(doctCodeName);//开立医生name
				recipedetailVo.setRecipedStatus(4);
				recipedetailVo.setSumMoney(0.00);
				recipedetailVo.setRegDate(new Date());
				recipedetailVo.setOperDate(new Date());
				recVoList.add(recipedetailVo);
			}
		}
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new BeanComparator("recipeNo"), true);
		Collections.sort(recVoList,chain);
		return recVoList;
	}
	
	@Override
	public BusinessContractunit queryCountByPaykindCode(String count) {
		return  medicinelistDAO.queryCountByPaykindCode(count);
	}
	
	@Override
	public List<MedicalVo> findFeedetailDetails(String recipeNo) {
		//根据处方号查询出每条处方记录
		List<OutpatientFeedetailNow> lst = medicinelistDAO.findFeedetailDetails(recipeNo);
		//new一个拥有页面显示所有的字段的VO并且把查到的处方记录SET到VO中
		List<MedicalVo> list = new  ArrayList<MedicalVo>();
		//定义一个对象
		MedicalVo adviceVoList =null;
		for(OutpatientFeedetailNow modls : lst){//遍历得到每一条处方添加到页面显示的VO中
			adviceVoList=new MedicalVo();//初始化VO
			adviceVoList.setFeedetailId(modls.getId());  //明细表id
			adviceVoList.setAdviceId(modls.getItemCode());//药品/非药品ID
			adviceVoList.setAdviceName(modls.getItemName());//药品/非药品名称
			BigDecimal unitPrice = new   BigDecimal(modls.getUnitPrice());  
			Double unitPrices = unitPrice.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			adviceVoList.setAdPrice(unitPrices);//单价
			adviceVoList.setDosageHid(modls.getDoseOnce());//每次用量
			adviceVoList.setAdMinUnitHid(modls.getPriceUnit());//单位
			adviceVoList.setGroup(modls.getCombNo());//组
			adviceVoList.setTotalNum(modls.getQty());//数量
			adviceVoList.setUnit(modls.getPriceUnit()); 
			adviceVoList.setSetNum(modls.getDays());
			adviceVoList.setFrequencyHid(modls.getFrequencyCode());
			adviceVoList.setUsageNameHid(modls.getUseName());
			adviceVoList.setOpenDoctor(modls.getDoctCode());
			adviceVoList.setExecutiveDept(modls.getExecDpcd());
			adviceVoList.setIsUrgentHid(modls.getEmcFlag());
			adviceVoList.setInspectPart(modls.getCheckBody());
			//adviceVoList.setRemark(modls.get) //备注
			adviceVoList.setOpenDept(modls.getRegDpcd());
			adviceVoList.setDrugFlag(modls.getDrugFlag());//药品非药品
			adviceVoList.setFeeCode(modls.getFeeCode());//最小费用单位代码
			//根据最小费用代码ID查询到最小费用代码的encode
			BusinessDictionary drugminimumcost =  codeInInterDAO.getDictionaryByCode("drugMinimumcost",modls.getFeeCode());
			//根据最小费用代码的encode查询统计大类中统计大类的encode和name
			MinfeeStatCode minfeeStatCode = medicinelistDAO.minfeeStatCodeByEncode(drugminimumcost.getEncode());
			adviceVoList.setFeeStatName(minfeeStatCode.getFeeStatName());//统计大类名称
			adviceVoList.setFeeStatCode(minfeeStatCode.getFeeStatCode());//统计大类代码
			adviceVoList.setRecipeNo(modls.getRecipeNo());//处方号
			adviceVoList.setInjectionNum(modls.getInjectNumber());//院注次数
			adviceVoList.setSysType(modls.getClassCode());//系统类别
			adviceVoList.setSubjobFlag(modls.getSubjobFlag());//辅材标志
			adviceVoList.setExtendOne(modls.getExtendOne());//拓展字段
			adviceVoList.setMoOrder(modls.getMoOrder());//医嘱流水号
			adviceVoList.setAdPriceSum(modls.getTotCost());//单条处方的总钱数
			adviceVoList.setConfirmCode(modls.getConfirmCode());//确认人
			adviceVoList.setConfirmDate(modls.getConfirmDate());//确认时间
			adviceVoList.setConfirmDept(modls.getConfirmDept());//确认科室
			adviceVoList.setConfirmFlag(modls.getConfirmFlag());//确认状态
			adviceVoList.setConfirmNum(modls.getConfirmNum());//确认数量
			adviceVoList.setPubCost(modls.getPubCost()==null?0.0:modls.getPubCost());//公费金额、可报销金额
			adviceVoList.setEcoCost(modls.getEcoCost()==null?0.0:modls.getEcoCost());//优惠金额
			adviceVoList.setOverCost(modls.getOverCost()==null?0.0:modls.getOverCost());//超标金额
			adviceVoList.setExcessCost(modls.getExcessCost()==null?0.0:modls.getExcessCost());//药品超标金额
			adviceVoList.setDrugOwncost(modls.getDrugOwncost()==null?0.0:modls.getDrugOwncost());//自费药金额
			adviceVoList.setPayCost(modls.getPayCost()==null?0.0:modls.getPayCost());//自付金额
			
			if("1".equals(modls.getDrugFlag())){
				DrugInfo drugInfo = medicinelistDAO.queryDrugInfoById(modls.getItemCode());
				adviceVoList.setIssubmit(drugInfo.getDrugIsterminalsubmit());//是否需要确认
			}else{
				DrugUndrug drugUndrug = medicinelistDAO.queryUnDrugById(modls.getItemCode());
				adviceVoList.setIssubmit(drugUndrug.getUndrugIssubmit());//是否需要确认
			}
			adviceVoList.setExtFlag3(modls.getExtFlag3());
			list.add(adviceVoList);
		} 
		return list;
	}
	
	@Override
	public List<FeeCodeVo> findMinfeeStatCodeByMinfeeCodes(String jsonRowsList) {
		//new一个map
		Map<String,Double> map = new HashMap<String, Double>();
		//new一个页面显示的vo
		List<FeeCodeVo> feeCodeVoList = new ArrayList<FeeCodeVo>();
		Gson gson = new Gson();
		//将页面传过来的处方json转化成vo
		List<MedicalVo> modelList = gson.fromJson(jsonRowsList, new TypeToken<List<MedicalVo>>(){}.getType());
		if(modelList.size()>0){//当vo存在数据时
			List<MedicalVo> modelNewList = new ArrayList<MedicalVo>();
			for(MedicalVo vo : modelList){
				DrugUndrug unDrugInfo = undrugInInterDAO.get(vo.getFeedetailId());
				if(unDrugInfo!=null&&unDrugInfo.getUndrugIsstack()!=null&&unDrugInfo.getUndrugIsstack()==1){
					List<MedicalVo> medicalVoList = undrugZtinfoInInterDAO.queryMedicalVoByCode(unDrugInfo.getCode());
					if(medicalVoList!=null&&medicalVoList.size()>0){
						MedicalVo medicalVo = null;
						for(MedicalVo medical : medicalVoList){
							medicalVo = new MedicalVo();
							medicalVo.setFeeStatCode(medical.getFeeStatCode());
							medicalVo.setFeeStatName(medical.getFeeStatName());
							medicalVo.setAdPriceSum(medical.getAdPriceSum()*vo.getTotalNum());
							
							modelNewList.add(medicalVo);
						}
					}
				}else{
					modelNewList.add(vo);
				}
			}
			for(MedicalVo vo : modelNewList){//遍历VO 将相同统计大类的处方中的钱数相加
				if(map.get(vo.getFeeStatCode()+"_"+vo.getFeeStatName())==null){
					map.put(vo.getFeeStatCode()+"_"+vo.getFeeStatName(), vo.getAdPriceSum());
				}else{
					map.put(vo.getFeeStatCode()+"_"+vo.getFeeStatName(),map.get(vo.getFeeStatCode()+"_"+vo.getFeeStatName())+vo.getAdPriceSum());
				}
			}
		}
		if(map.size()>0){//当统计之后有数据时
			FeeCodeVo vo = null;
			for(Map.Entry<String, Double> entry : map.entrySet()){//根据页面显示格式将数据改成相应的格式
				vo = new FeeCodeVo();
				String[] arr = entry.getKey().split("_");
				vo.setFeeTypeCode(arr[0]);
				vo.setFeeTypeName(arr[1]);
				BigDecimal fee = new   BigDecimal(entry.getValue());
				Double fees = fee.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
				vo.setFees(fees);
				feeCodeVoList.add(vo);
			}
		}
		return feeCodeVoList;
	}
	
	@Override
	public List<SysDepartment> quertComboboxDept() {
		return medicinelistDAO.quertComboboxDept();
	}
	
	@Override
	public int getTotalUndrug(String undrugCodes) {
		return medicinelistDAO.getTotalUndrug(undrugCodes);
	}
	
	@Override
	public List<UndrugAndWare> findUndrugAndWareList(String undrugCodes,String page,String rows) {
		return medicinelistDAO.findUndrugAndWareList(undrugCodes,page,rows);
	}
	
	@Override
	public MinfeeStatCode minfeeStatCodeByEncode(String encode) {
		return medicinelistDAO.minfeeStatCodeByEncode(encode);
	}
	@Override
	public List<MedicalVo> findOdditionalitemByTypeCode(String undrugId) {
		return medicinelistDAO.findOdditionalitemByTypeCode(undrugId);
	}
	@Override
	public List<BusinessFrequency> findFrequency() {
		return medicinelistDAO.findFrequency();
	}
	
	/**
	 * @Description  
	 * @author  marongbin
	 * @createDate： 2017年2月27日 下午5:20:52 
	 * @modifier  修改时间：2017-2-27 17:21:10；修改内容：需要预约或确认你的生成申请流水号（一个项目生成一个申请流水号）
	 * @modifyDate：
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public void savePrice(String jsonRowsList, String clinicCode) {
		RegistrationNow info = medicinelistDAO.findRegisterInfoByNo(clinicCode);//根据门诊号查询挂号信息
		Map<String,Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		String adviceStreamNo = "";
		OutpatientFeedetailNow patientFeedetail = medicinelistDAO.queryNOByclinicCode(clinicCode);//查询收费序列
		DrugUndrug undrug = new DrugUndrug();
		String recipeSeq = "";//收费序列
		if(StringUtils.isBlank(patientFeedetail.getRecipeSeq())){
			recipeSeq = medicinelistDAO.getSeqByName("SEQ_ADVICE_CHARGESEQ"); 
		}else{
			recipeSeq = patientFeedetail.getRecipeSeq();
		}
		List<MedicalVo> modelList = gson.fromJson(jsonRowsList, new TypeToken<List<MedicalVo>>(){}.getType());
		int no = 0;
		if(modelList.size()>0){
			List<OutpatientFeedetailNow> feeList = new ArrayList<OutpatientFeedetailNow>();
			OutpatientFeedetailNow feedetail = null;
			for(MedicalVo vo : modelList){
				no = no + 1;
				undrug = medicinelistDAO.queryUnDrugById(vo.getFeedetailId());
				if(undrug.getUndrugIsstack()!=null&&undrug.getUndrugIsstack()==1){
					List<DrugUndrug> infoList = undrugZtinfoInInterDAO.getUndrugZtinfoByPackageCode(undrug.getCode());
					if(infoList!=null&&infoList.size()>0){
						String recipeNo = medicinelistDAO.getSeqByName("SEQ_ADVICE_RECIPENO");
						String Groupno = medicinelistDAO.getSeqByName("SEQ_ADVICE_GROUPNO");
						for(DrugUndrug ztinfo : infoList){
							no = no + 1;
							feedetail = new OutpatientFeedetailNow();
							feedetail.setRecipeNo(recipeNo);//处方号
							feedetail.setSequenceNo(no);//处方内流水号
							feedetail.setTransType(1);//交易类型
							feedetail.setCardNo(info.getCardNo());//就诊卡号
							feedetail.setRegDate(info.getRegDate());//挂号日期
							feedetail.setRegDpcd(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//开单科室
							feedetail.setDoctCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//开单医生
							feedetail.setDoctDept(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());//开单医生所属科室
							feedetail.setItemCode(ztinfo.getCode());//项目代码
							feedetail.setItemName(ztinfo.getName());//项目名称
							feedetail.setDrugFlag(0+"");//是否是药品
							feedetail.setFeeCode(ztinfo.getUndrugMinimumcost());//最小费用代码
							feedetail.setClassCode(ztinfo.getUndrugSystype());//系统类别
							feedetail.setUnitPrice(ztinfo.getDefaultprice());//单价
							feedetail.setQty(vo.getTotalNum());//数量
							BigDecimal big = new BigDecimal(vo.getTotalNum()*ztinfo.getDefaultprice());
							feedetail.setTotCost(big.setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue());//现金金额   保留四位有效数字
							feedetail.setExecDpcd(vo.getExecutiveDept());//执行科室代码
							feedetail.setExecDpnm(deptInInterService.getDeptCode(vo.getExecutiveDept()).getDeptName());//执行科室名称
							feedetail.setMainDrug(0);//主药标志
							feedetail.setCombNo(Groupno);//组合号
							feedetail.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//划价人
							feedetail.setOperDate(DateUtils.getCurrentTime());//划价时间
							feedetail.setPayFlag(0);//划价
							feedetail.setCancelFlag(1);//1正常
							feedetail.setConfirmFlag(0);//未确认
							feedetail.setExtFlag1(0);
							feedetail.setExtFlag2(0);
							feedetail.setExtFlag(0);
							feedetail.setRecipeSeq(recipeSeq);//收费序列
							adviceStreamNo = medicinelistDAO.getSeqByName("SEQ_ADVICE_STREAMNO");
							feedetail.setMoOrder(adviceStreamNo);//医嘱流水号
							feedetail.setCostSource(0);//费用来源
							feedetail.setAccountFlag(0);//已扣账户
							feedetail.setUseFlag(0);//未出结果
							feedetail.setClinicCode(clinicCode);//门诊号
							feedetail.setPatientNo(info.getMidicalrecordId());//病历号
							if(vo.getSubjobFlag() == null){
								map.put(vo.getFeedetailId()+"F", no+"");
							}else{
								if(map.containsKey(vo.getFollow())){
									feedetail.setExtendOne(map.get(vo.getFollow())+"");
									feedetail.setSubjobFlag(1);//辅材标志
								}
							}
							feedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
							feedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
							feedetail.setCreateTime(DateUtils.getCurrentTime());
							feedetail.setConfirmNum(0D);//确认数量
							feedetail.setPackageCode(undrug.getCode());
							feedetail.setPackageName(vo.getAdviceName());
							feedetail.setRegDpcdname(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//开单科室名称
							feedetail.setDoctCodename(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());;//开方医师姓名
							feedetail.setDoctDeptname(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptName());//开方医师所在科室名称
							feedetail.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//划价人姓名
							if(ztinfo.getUndrugIspreorder()!=null&&ztinfo.getUndrugIssubmit()!=null){
								if(1==ztinfo.getUndrugIspreorder()||1==ztinfo.getUndrugIssubmit()){
									feedetail.setSampleId(medicinelistDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER"));//申请流水号
								}
							}
							feedetail.setPriceUnit(undrug.getUnit());
							feeList.add(feedetail);
						}
					}
				}else{
					feedetail = new OutpatientFeedetailNow();
					feedetail.setId(null);//id
					feedetail.setRecipeNo(medicinelistDAO.getSeqByName("SEQ_ADVICE_RECIPENO"));//处方号
					feedetail.setSequenceNo(no);//处方内流水号
					feedetail.setTransType(1);//交易类型
					feedetail.setCardNo(info.getCardNo());//就诊卡号
					feedetail.setRegDate(info.getRegDate());//挂号日期
					feedetail.setRegDpcd(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//开单科室
					feedetail.setDoctCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//开单医生
					feedetail.setDoctDept(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());//开单医生所属科室
					feedetail.setItemCode(undrug.getCode());//项目代码
					feedetail.setItemName(vo.getAdviceName());//项目名称
					feedetail.setDrugFlag(0+"");//是否是药品
					feedetail.setFeeCode(vo.getFeeCode());//最小费用代码
					feedetail.setClassCode(vo.getSysType());//系统类别
					feedetail.setUnitPrice(vo.getAdPrice());//单价
					feedetail.setQty(vo.getTotalNum());//数量
					feedetail.setTotCost(vo.getAdPriceSum());//现金金额
					feedetail.setExecDpcd(vo.getExecutiveDept());//执行科室代码
					feedetail.setExecDpnm(deptInInterService.getDeptCode(vo.getExecutiveDept()).getDeptName());//执行科室名称
					feedetail.setMainDrug(0);//主药标志
					feedetail.setCombNo(medicinelistDAO.getSeqByName("SEQ_ADVICE_GROUPNO"));//组合号
					feedetail.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//划价人
					feedetail.setOperDate(DateUtils.getCurrentTime());//划价时间
					feedetail.setPayFlag(0);//划价
					feedetail.setCancelFlag(1);//1正常
					feedetail.setConfirmFlag(0);//未确认
					feedetail.setExtFlag1(0);
					feedetail.setExtFlag2(0);
					feedetail.setExtFlag(0);
					feedetail.setRecipeSeq(recipeSeq);//收费序列
					adviceStreamNo = medicinelistDAO.getSeqByName("SEQ_ADVICE_STREAMNO");
					feedetail.setMoOrder(adviceStreamNo);//医嘱流水号
					feedetail.setCostSource(0);//费用来源
					feedetail.setAccountFlag(0);//已扣账户
					feedetail.setUseFlag(0);//未出结果
					feedetail.setClinicCode(clinicCode);//门诊号
					feedetail.setPatientNo(info.getMidicalrecordId());//病历号
					if(vo.getSubjobFlag() == null){
						map.put(vo.getFeedetailId()+"F", no+"");
					}else{
						if(map.containsKey(vo.getFollow())){
							feedetail.setExtendOne(map.get(vo.getFollow())+"");
							feedetail.setSubjobFlag(1);//辅材标志
						}
					}
					feedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					feedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					feedetail.setCreateTime(DateUtils.getCurrentTime());
					feedetail.setConfirmNum(0D);//确认数量
					feedetail.setRegDpcdname(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//开单科室名称
					feedetail.setDoctCodename(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//开方医师姓名
					feedetail.setDoctDeptname(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptName());//开方医师所在科室名称
					feedetail.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//划价人姓名
					if((undrug.getUndrugIspreorder()!=null&&1==undrug.getUndrugIspreorder())||(undrug.getUndrugIssubmit()!=null&&1==undrug.getUndrugIssubmit())){
						feedetail.setSampleId(medicinelistDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER"));//申请流水号
					}
					feedetail.setPriceUnit(vo.getUnit());
					feeList.add(feedetail);
				}
			}
			try{
				medicinelistDAO.saveOrUpdateList(feeList);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override
	public List<SysDepartment> queryEdComboboxDept() {
		return medicinelistDAO.queryEdComboboxDept();
	}
	
	@Override
	public RegistrationNow queryInfoByNo(String clinicCode) {
		return medicinelistDAO.queryInfoByNo(clinicCode);
	}
	
	
	@Override
	public Map<String,Object> saveCharge(Map<String, String> parameterMap,Registration registerInfo, OutpatientAccount account,String jsonRowsList) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> matMap = new HashMap<String, Object>();
		String deptName = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName();
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		//根据用户ID查询员工ID  
		SysEmployee employee = medicinelistDAO.queryEmployee(userId);
		//查询出所有的医技科室
		List<SysDepartment> departmentList = medicinelistDAO.chargeImplementDepartmentList();
		String invoiceSeq = ""; //发票序号 
		String invoiceComb = "";//一次发票序号
		//发票序号
		OutpatientFeedetailNow outpatientFeedetail = medicinelistDAO.queryFeedetailInvoiceNo(registerInfo.getClinicCode());
		List<OutpatientFeedetailNow> matOutFee = medicinelistDAO.findMatFee(parameterMap.get("feedetaiNotIds"));
		if(matOutFee.size()>0){
			for(OutpatientFeedetailNow  matOut:matOutFee){
				OutputInInterVO vo = new OutputInInterVO();
				vo.setStorageCode(matOut.getExecDpcd());//执行科室
				vo.setInpatientNo(registerInfo.getClinicCode());//门诊号
				vo.setUndrugItemCode(matOut.getItemCode());//非药品ID
				vo.setRecipeNo(matOut.getRecipeNo());//处方号
				vo.setSequenceNo(matOut.getSequenceNo());//处方内流水号
				vo.setApplyNum(matOut.getConfirmNum());//确认数量
				vo.setUseNum(matOut.getConfirmNum());//执行数量
				vo.setTransType(1);//交易类型
				vo.setFlag("I");//门诊
				matMap = matOutPutService.addRecord(vo);
				if(!"0".equals(matMap.get("resCode").toString())&&!"3".equals(matMap.get("resCode").toString())){
					if("1".equals(matMap.get("resCode").toString())){
						matMap.put("resMsg", "error");
						matMap.put("resCode", "库存不足");
					}
					if("2".equals(matMap.get("resCode").toString())){
						matMap.put("resMsg", "error");
						matMap.put("resCode", "此物资出库其他条件不满足");
					}
					return matMap;
				}else{
					matMap.put("resMsg", "success");
					matMap.put("resCode", "ok");
				}
			}
		}
		
		//用住院账户支付
		if(StringUtils.isNotBlank(parameterMap.get("hospitalAccount"))&&!"0".equals(parameterMap.get("hospitalAccount"))){
			//根据病例号或得账户信息
			account = medicinelistDAO.getAccountByMedicalrecord(registerInfo.getMidicalrecordId());
			if(account.getId()==null){
				matMap.put("resMsg", "error");
				matMap.put("resCode", "该病历号无账户信息,请联系管理员!");
				return matMap;
			}else if(account.getAccountState()==0){//停用
				matMap.put("resMsg", "error");
				matMap.put("resCode", "账户已停用,请联系管理员!");
				return matMap;
			}else if(account.getAccountState()==2){//注销3结清4冻结
				matMap.put("resMsg", "error");
				matMap.put("resCode", "账户已注销!");
				return matMap;
			}else if(account.getAccountState()==1&&Double.valueOf(parameterMap.get("hospitalAccount"))>account.getAccountBalance()){//总金额大于剩余的门诊金额无法结账
				matMap.put("resMsg", "error");
				matMap.put("resCode", "账户剩余金额["+account.getAccountBalance()+"],请充值缴费后结算!");
				return matMap;
			}else if(account.getAccountDaylimit()!=0){//当当日消费额限不等于0时
				List<OutpatientAccountrecord> accountrecord = medicinelistDAO.queryAccountrecord(registerInfo.getMidicalrecordId());
				Double sum = 0.0;//已使用金额
				if(accountrecord.size()>0){
					for(OutpatientAccountrecord modls : accountrecord){//遍历符合条件的信息
						if(modls.getMoney() != null){
							sum = sum + modls.getMoney();
						}
					}
					if(account.getAccountDaylimit()<-(sum-Double.valueOf(parameterMap.get("hospitalAccount")))){
						matMap.put("resMsg", "error");
						matMap.put("resCode", "账户已经超当日额限!当日还可以缴费["+(account.getAccountDaylimit()+sum)+"]");
						return matMap;
					}
				}
			}
		}
		
		Gson gson = new Gson();
		String recipeNo = medicinelistDAO.getSeqByName("SEQ_ADVICE_RECIPENO"); //或得处方号
		String moOrder = medicinelistDAO.getSeqByName("SEQ_ADVICE_STREAMNO"); //医嘱流水号
		String recipeSeq = "";//收费序列
		OutpatientFeedetailNow patientFeedetail = medicinelistDAO.queryNOByclinicCode(parameterMap.get("clinicCode"));//查询收费序列
		if(StringUtils.isBlank(patientFeedetail.getRecipeSeq())){
			recipeSeq = medicinelistDAO.getSeqByName("SEQ_ADVICE_CHARGESEQ"); 
		}else{
			recipeSeq = patientFeedetail.getRecipeSeq();
		}
		List<MedicalVo> modelList = gson.fromJson(jsonRowsList, new TypeToken<List<MedicalVo>>(){}.getType());
		if(StringUtils.isNotBlank(outpatientFeedetail.getInvoiceSeq())){
			invoiceSeq =outpatientFeedetail.getInvoiceSeq();
		}else{
			invoiceSeq = medicinelistDAO.getSequece("SEQ_INVOICE_SEQ");//查询序列得出序号
			int invoiceSeqLength = invoiceSeq.length();
			int invoiceSeqEnd = 14-invoiceSeqLength;
			String invoiceSeqStr = "";
			for(int i =0;i<invoiceSeqEnd;i++){
				invoiceSeqStr = invoiceSeqStr +"0";
			}
			invoiceSeq = invoiceSeqStr + invoiceSeq;
		}
		//parameterMap 所有参数
		if(StringUtils.isNotBlank(parameterMap.get("hospitalAccount"))&&!"0".equals(parameterMap.get("hospitalAccount"))){
			//更新门诊账户表
			account.setAccountBalance(account.getAccountBalance()-Double.valueOf(parameterMap.get("hospitalAccount")));
			medicinelistDAO.save(account);
			
			//添加门诊账户明细表
			OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
			accountrecord.setId(null);//ID
			accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
			accountrecord.setAccountId(account.getId());//门诊账户编号
			accountrecord.setOpertype(4);//操作类型
			accountrecord.setMoney(-Double.valueOf(parameterMap.get("hospitalAccount")));//交易金额
			accountrecord.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//相关科室
			accountrecord.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员 
			accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
			accountrecord.setAccountBalance(account.getAccountBalance()-Double.valueOf(parameterMap.get("hospitalAccount")));//交易后余额
			accountrecord.setValid(0);//是否有效
			accountrecord.setInvoiceType("402880a54e3e0568014e3e07b9150004");//发票类型
			accountrecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			accountrecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			accountrecord.setCreateTime(DateUtils.getCurrentTime());
			accountrecord.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			accountrecord.setUpdateTime(DateUtils.getCurrentTime());
			medicinelistDAO.save(accountrecord);
		}
		//添加发票明细表
		if(StringUtils.isNotBlank(parameterMap.get("rowFee")) && StringUtils.isNotBlank(parameterMap.get("rowTypeName")) && StringUtils.isNotBlank(parameterMap.get("rowTypeCode"))){
			String[] feeStr = parameterMap.get("rowFee").split(","); 
			String[] TypeNameStr = parameterMap.get("rowTypeName").split(","); 
			String[] TypeCodeStr = parameterMap.get("rowTypeCode").split(",");
			for(int i=0;i<feeStr.length;i++){
				FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
				invoicedetail.setId(null);//ID
				invoicedetail.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
				invoicedetail.setTransType(1);//正反类型1正2反
				invoicedetail.setInvoSequence(null);//发票号内流水号
				invoicedetail.setInvoCode(TypeCodeStr[i]);//发票科目代码
				invoicedetail.setInvoName(TypeNameStr[i]);//发票科目名称
				invoicedetail.setPubCost(0.00);//可报效金额
				invoicedetail.setOwnCost(0.00);//不可报效金额
				if(StringUtils.isNotBlank(feeStr[i])){
					Double fee = Double.valueOf(feeStr[i]);
					invoicedetail.setPayCost(fee);//自付金额
				}
				invoicedetail.setDeptCode(deptId);//执行科室ID
				invoicedetail.setDeptName(deptName);//执行科室名称
				invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
			//	invoicedetail.setOperCode(userId);//操作人
				invoicedetail.setBalanceFlag(0);//日结状态
				invoicedetail.setBalanceNo(null);//日结标识号
				invoicedetail.setBalanceOpcd(null);//日结人
				invoicedetail.setBalanceDate(null);//日结时间
				invoicedetail.setCancelFlag(1);//状态
				invoicedetail.setInvoiceSeq(invoiceSeq);//发票序号
				invoicedetail.setInvoSequence(i+"");//发票内流水号
				invoicedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				invoicedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				invoicedetail.setCreateTime(DateUtils.getCurrentTime());
				invoicedetailDAO.save(invoicedetail);
			}
		}
		List<OutpatientRecipedetailNow> recipedetailList = medicinelistDAO.queryRecipedetailList(parameterMap.get("recipedetailIds"));//处方表
		List<OutpatientFeedetailNow> feedetailListNot = medicinelistDAO.saveOrUpdateFeedetailListNot(parameterMap.get("feedetaiNotIds"));//门诊非药品明细表
		List<OutpatientFeedetailNow> feedetailList = medicinelistDAO.saveOrUpdateFeedetailList(parameterMap.get("feedetailIds"));//门诊药品明细表
		//门诊处方明细表处理
		//门诊非药品明细表处理
		List<OutpatientItemlist> itemlist = new ArrayList<OutpatientItemlist>();
		List<OutpatientMedicinelist> medicinelist = new ArrayList<OutpatientMedicinelist>();
		List<OutpatientMedicinelist> medicinelistList = new ArrayList<OutpatientMedicinelist>();
		List<OutpatientItemlist> itemlistList = new ArrayList<OutpatientItemlist>();
		OutpatientRecipedetail recipedetail = new OutpatientRecipedetail();//门诊处方
		
		
		//门诊处方处理
		for(OutpatientRecipedetailNow RList :recipedetailList){
			RList.setId(RList.getId());
			RList.setStatus(1);
			RList.setCreateUser(userId);
			RList.setCreateDept(deptId);
			RList.setCreateTime(new Date());
			recipedetailDAO.update(RList);//保存门诊处方
		}
		int q = 0;
		for(OutpatientFeedetailNow Flist :feedetailListNot){
			q=q+1;
			Flist.setId(Flist.getId());
			Flist.setPayFlag(1);
			Flist.setCreateUser(userId);
			Flist.setCreateDept(deptId);
			Flist.setFeeDate(DateUtils.getCurrentTime());
			Flist.setCreateTime(new Date());
			if(Flist.getConfirmNum() != null){
				Flist.setNobackNum(Flist.getConfirmNum());
			}
			Flist.setExecDpnm(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
			Flist.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			Flist.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			Flist.setFeeCpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			Flist.setInvoiceNo(parameterMap.get("invoiceNo"));
			Flist.setInvoiceSeq(invoiceSeq);
			Flist.setInvoSequence(q+"");
			Flist.setCreateTime(DateUtils.getCurrentTime());
			feedetailDAO.update(Flist);//保存门诊处方
		}
		int a = 0;
		for(OutpatientFeedetailNow FNlist :feedetailList){
			a = a + 1;
			FNlist.setId(FNlist.getId());
			FNlist.setPayFlag(1);
			FNlist.setCreateUser(userId);
			FNlist.setCreateDept(deptId);
			FNlist.setCreateTime(new Date());
			if(FNlist.getConfirmNum() != null){
				FNlist.setNobackNum(FNlist.getConfirmNum());
			}
			FNlist.setExecDpnm(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
			FNlist.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			FNlist.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			FNlist.setFeeDate(DateUtils.getCurrentTime());
			FNlist.setFeeCpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			FNlist.setInvoiceSeq(invoiceSeq);
			FNlist.setInvoiceNo(parameterMap.get("invoiceNo"));
			FNlist.setInvoSequence(a+"");
			FNlist.setFeeDate(DateUtils.getCurrentTime());
			FNlist.setCreateTime(DateUtils.getCurrentTime());
			feedetailDAO.update(FNlist);//保存门诊处方
		}
		int no = 0;
		if(modelList.size()>0){
			for(MedicalVo vo : modelList){
				if("1".equals(vo.getStust())){
					no = no + 1;
					String groupNo = medicinelistDAO.getSeqByName("SEQ_ADVICE_GROUPNO"); //或得组合号
					OutpatientFeedetail feedetail = new OutpatientFeedetail();
					feedetail.setId(null);//id
					feedetail.setRecipeNo(recipeNo);//处方号
					feedetail.setSequenceNo(no);//处方内流水号
					feedetail.setTransType(1);//交易类型
					feedetail.setCardNo(registerInfo.getCardNo());//就诊卡号
					feedetail.setRegDate(registerInfo.getRegDate());//挂号日期
					feedetail.setRegDpcd(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//开单科室
					feedetail.setDoctCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//开单医生
					feedetail.setDoctDept(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());//开单医生所属科室
					feedetail.setItemCode(vo.getFeedetailId());//项目代码
					feedetail.setItemName(vo.getAdviceName());//项目名称
					feedetail.setDrugFlag(0+"");//是否是药品
					feedetail.setFeeCode(vo.getFeeCode());//最小费用代码
					feedetail.setClassCode(vo.getSysType());//系统类别
					feedetail.setUnitPrice(vo.getAdPrice());//单价
					feedetail.setQty(vo.getTotalNum());//数量
					feedetail.setTotCost(vo.getAdPriceSum());//现金金额
					feedetail.setExecDpcd(vo.getExecutiveDept());//执行科室代码
					feedetail.setExecDpnm(null);//执行科室名称
					feedetail.setMainDrug(0);//主药标志
					feedetail.setCombNo(groupNo);//组合号
					feedetail.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//划价人
					feedetail.setOperDate(DateUtils.getCurrentTime());//划价时间
					feedetail.setPayFlag(1);//划价
					feedetail.setCancelFlag(1);//1正常
					feedetail.setConfirmFlag(0);//未确认
					feedetail.setExtFlag1(0);
					feedetail.setExtFlag2(0);
					feedetail.setExtFlag(0);
					feedetail.setPriceUnit(vo.getUnit());//单位
					feedetail.setRecipeSeq(recipeSeq);//收费序列
					feedetail.setMoOrder(moOrder);//医嘱流水号
					feedetail.setCostSource(0);//费用来源
					feedetail.setAccountFlag(1);//已扣账户
					feedetail.setUseFlag(0);//未出结果
					feedetail.setClinicCode(registerInfo.getClinicCode());//门诊号
					feedetail.setPatientNo(registerInfo.getMidicalrecordId());//病历号
					if(vo.getSubjobFlag() == null){
						map.put(vo.getFeedetailId()+"F", no+"");
						feedetail.setSubjobFlag(0);//辅材标志
					}else{
						if(map.containsKey(vo.getFollow())){
							feedetail.setExtendOne(map.get(vo.getFollow())+"");
							feedetail.setSubjobFlag(1);//辅材标志
						}
					}
					feedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					feedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					feedetail.setCreateTime(DateUtils.getCurrentTime());
					feedetail.setFeeDate(DateUtils.getCurrentTime());
					feedetail.setFeeDate(DateUtils.getCurrentTime());
					feedetail.setFeeCpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					feedetail.setInvoiceNo(parameterMap.get("invoiceNo"));
					feedetail.setDrugFlag("0");
					feedetail.setNobackNum(vo.getConfirmNum());//可退数量
					feedetail.setExecDpcd(deptId);
					feedetail.setExecDpnm(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
					feedetail.setInvoiceSeq(invoiceSeq);
					feedetail.setInvoSequence(no+"");
					feedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					feedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					feedetail.setCreateTime(DateUtils.getCurrentTime());
					feedetail.setPayFlag(1);
					medicinelistDAO.save(feedetail);
				}
			}
		}
		//结算信息表
		FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
		invoiceInfo.setId(null);
		invoiceInfo.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
		invoiceInfo.setTransType(1);//交易类型
		invoiceInfo.setCardNo(registerInfo.getMidicalrecordId());//病历号
		invoiceInfo.setRegDate(registerInfo.getRegDate());//挂号日期
		invoiceInfo.setName(registerInfo.getPatientName());//患者姓名
		invoiceInfo.setPaykindCode(null);//结算类别
		invoiceInfo.setPactCode(registerInfo.getPactCode());//合同单位代码
		invoiceInfo.setPactName(registerInfo.getPactName());//合同单位名称
		invoiceInfo.setMcardNo(null);//个人编号
		invoiceInfo.setMedicalType(null);//医疗类别
		invoiceInfo.setTotCost(Double.valueOf(parameterMap.get("totCost")));//总金额
		invoiceInfo.setPubCost(null);//可报效金额
		invoiceInfo.setOwnCost(null);//不可报效金额
		invoiceInfo.setPayCost(Double.valueOf(parameterMap.get("totCost")));//自付金额
		invoiceInfo.setBack1(null);//预留1
		invoiceInfo.setBack2(null);//预留2
		invoiceInfo.setBack3(null);//预留3
		invoiceInfo.setRealCost(Double.valueOf(parameterMap.get("totCost")));//实付金额
		invoiceInfo.setOperCode(userId);//结算人
		invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
		invoiceInfo.setExamineFlag(0);//团体/个人
		invoiceInfo.setCancelFlag(1);//有效
		invoiceInfo.setCancelInvoice(null);//作废票据号
		invoiceInfo.setCancelCode(null);//作废操作员
		invoiceInfo.setCancelDate(null);//作废时间
		invoiceInfo.setInvoiceSeq(invoiceSeq);//发票序号
		invoiceInfo.setInvoiceComb(invoiceComb);//一次收费序号
		invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
		invoiceInfo.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
		invoiceInfoDAO.save(invoiceInfo);
		//保存支付情况表
		String[] arr = new String[]{parameterMap.get("cash"),parameterMap.get("bankCard"),parameterMap.get("check"),parameterMap.get("hospitalAccount")};
		for(int z=0;z<arr.length;z++){
			if(StringUtils.isNotBlank(arr[z])){
				BusinessPayMode payMode = new BusinessPayMode();
				payMode.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
				payMode.setTransType(1);//交易类型
				payMode.setSequenceNo(null);//交易内流水号
				payMode.setOperCode(employee.getId());//结算人
				payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
				if(arr[z].equals(parameterMap.get("cash"))){//支付方式
					payMode.setModeCode("1");
					payMode.setTotCost(Double.valueOf(parameterMap.get("cash")));//应付金额
					payMode.setRealCost(Double.valueOf(parameterMap.get("cash")));//实付金额
				}
				if(arr[z].equals(parameterMap.get("bankCard"))){
					payMode.setModeCode("2");
					payMode.setTotCost(Double.valueOf(parameterMap.get("bankCard")));//应付金额
					payMode.setRealCost(Double.valueOf(parameterMap.get("bankCard")));//实付金额
				}
				if(arr[z].equals(parameterMap.get("check"))){
					payMode.setModeCode("3");
					payMode.setTotCost(Double.valueOf(parameterMap.get("check")));//应付金额
					payMode.setRealCost(Double.valueOf(parameterMap.get("check")));//实付金额
					payMode.setBankCode(null);//开户银行代码
					payMode.setBankName(parameterMap.get("bankUniti"));//开户银行名称
					payMode.setAccount(parameterMap.get("banki"));//开户账户
					payMode.setPosNo(null);//pos机号
					payMode.setCheckNo(parameterMap.get("bankNo"));//支票号
					
				}
				if(arr[z].equals(parameterMap.get("hospitalAccount"))){
					payMode.setModeCode("4");
					payMode.setTotCost(Double.valueOf(parameterMap.get("hospitalAccount")));//应付金额
					payMode.setRealCost(Double.valueOf(parameterMap.get("hospitalAccount")));//实付金额
				}
				payMode.setBalanceFlag(0);//是否结算
				payMode.setCancelFlag(1);//状态
				payMode.setInvoiceSeq(invoiceSeq);//发票序号
				payMode.setInvoiceComb(invoiceComb);//一次收费序号
				payMode.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				payMode.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				payMode.setCreateTime(DateUtils.getCurrentTime());
				payMode.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
				payModeDAO.save(payMode);
			}
		}
		//
		String[] recipeNoArr = null;
		String[] feedetailArr = parameterMap.get("feedetailIds").split("','");
		if(StringUtils.isNotBlank(parameterMap.get("recipedetailIds"))){
			recipeNoArr = parameterMap.get("recipedetailIds").split("','");
			if(recipeNoArr.length>0){
				for(int r=0;r<recipeNoArr.length;r++){
					List<OutpatientFeedetailNow> feedetail = medicinelistDAO.queryFeedetailRecipeNo(recipeNoArr[r]);
					if(feedetail.size()>0){
						Double recipeCost = 0.0;//处方金额(零售金额)
						Double recipeQty = 0.0;//处方中药品数量
						StoRecipe stoRecipe = new StoRecipe();
						stoRecipe.setId(null);
						stoRecipe.setDrugDeptCode(feedetail.get(0).getExecDpcd());//执行科室/发放药房
						stoRecipe.setRecipeNo(recipeNoArr[r]);//处方号
						stoRecipe.setClassMeaningCode("1");//出库申请分类
						stoRecipe.setTransType(1);//交易类型
						stoRecipe.setRecipeState(0);//处方状态
						stoRecipe.setClinicCode(registerInfo.getClinicCode());//门诊号
						stoRecipe.setCardNo(registerInfo.getMidicalrecordId());//病例号
						stoRecipe.setPatientName(registerInfo.getPatientName());//患者姓名
						stoRecipe.setSexCode(registerInfo.getPatientSex());//性别
						stoRecipe.setBirthday(registerInfo.getPatientBirthday());//出生日期
						stoRecipe.setPaykindCode("");//结算类别
						stoRecipe.setDeptCode(registerInfo.getDeptCode());//患者科室代码
						stoRecipe.setDoctCode(registerInfo.getDoctCode());//开方医生
						stoRecipe.setDoctDept(registerInfo.getDeptCode());//开方医生所在科室
						stoRecipe.setFeeOper(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//收费人
						stoRecipe.setFeeDate(DateUtils.getCurrentTime());//收费时间
						stoRecipe.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
						stoRecipe.setFeeOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
						for(OutpatientFeedetailNow modlfeede : feedetail){
							if("1".equals(modlfeede.getDrugFlag())){
								recipeCost = recipeCost + modlfeede.getTotCost();
								recipeQty = recipeQty + modlfeede.getQty();
							}
						}
						stoRecipe.setRecipeCost(recipeCost);//处方金额(零售金额)
						stoRecipe.setRecipeQty(recipeQty);//处方中药品数量
						stoRecipe.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						stoRecipe.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
						stoRecipe.setCreateTime(DateUtils.getCurrentTime());
						stoRecipe.setValidState(1);
						stoRecipe.setModifyFlag(0);
						//按照特殊收费窗口查找配药台
						Integer itemType = 4;//特殊收费窗口
						SpeNalVo speNalVoList = medicinelistDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode(),itemType);
						if(speNalVoList.gettCode()!=null){
							stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
							StoTerminal stoTerminalNo = medicinelistDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
							if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
								stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
							}else{
								stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
							}
						}else{
							itemType = 2;//专科
							speNalVoList = medicinelistDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),registerInfo.getDeptCode(),itemType);
							if(speNalVoList.gettCode()!=null){
								stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
								StoTerminal stoTerminalNo = medicinelistDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
								if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
									stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
								}else{
									stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
								}
							}else{
								itemType = 3;//结算类别
								speNalVoList = medicinelistDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),registerInfo.getPaykindCode(),itemType);
								if(speNalVoList.gettCode()!=null){
									stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
									StoTerminal stoTerminalNo = medicinelistDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
									if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
										stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
									}else{
										stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
									}
								}else{
									itemType = 5;//挂号级别
									speNalVoList = medicinelistDAO.querySpeNalVoBy(feedetail.get(0).getExecDpcd(),registerInfo.getReglevlCode(),itemType);
									if(speNalVoList.gettCode()!=null){
										stoRecipe.setDrugedTerminal(speNalVoList.gettCode());
										StoTerminal stoTerminalNo = medicinelistDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
										if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
											stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
										}else{
											stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
										}
									}else{
										StoTerminal stoTerminal = medicinelistDAO.queryStoTerminal(feedetail.get(0).getExecDpcd());//配药终端
										StoTerminal stoTerminalNo = medicinelistDAO.queryStoTerminalNo(stoTerminal.getId());//发药终端
										if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
											stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
										}else{
											stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
										}
										stoRecipe.setDrugedTerminal(stoTerminal.getId());
									}
								}
							}
						}
						stoRecipeDAO.save(stoRecipe);
					}
				}
			}
		}
		//出库申请单
		String[] feedetailIdsArr = null;
		if(StringUtils.isNotBlank(parameterMap.get("feedetailIds"))){
			feedetailIdsArr = parameterMap.get("feedetailIds").split("','");
			if(feedetailIdsArr.length>0){
				for(int f=0;f<feedetailIdsArr.length;f++){
					OutpatientFeedetailNow  feedetailLists = medicinelistDAO.queryDrugInfoList(feedetailIdsArr[f]);
					DrugInfo drugInfo = medicinelistDAO.queryDrugInfoById(feedetailLists.getItemCode());
					int applyNumber = Integer.parseInt(applyoutDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
					String yearLast = new SimpleDateFormat("yyMM").format(new Date());
					int value = keyvalueDAO.getVal(deptId,"内部入库申请单号",yearLast);
					String applyBillcode = "0" + yearLast + value;//组成内部入库申请单号
					Double qty = feedetailLists.getQty();//获取药品的数量
					DrugApplyout drugApplyout = new DrugApplyout();
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
					drugApplyout.setPackUnit(drugInfo.getDrugPackagingunit());//包装单位
					drugApplyout.setPackQty(drugInfo.getPackagingnum());//包装数量
					drugApplyout.setMinUnit(drugInfo.getUnit());//最小单位
					drugApplyout.setShowFlag(null);
					drugApplyout.setShowUnit(null);
					drugApplyout.setValidState(1);//有效
					drugApplyout.setRetailPrice(drugInfo.getDrugRetailprice());//零售价
					drugApplyout.setWholesalePrice(drugInfo.getDrugWholesaleprice());//批发价
					drugApplyout.setApplyBillcode(applyBillcode);//申请单号
					drugApplyout.setApplyOpercode(registerInfo.getDoctCode());//申请人
					drugApplyout.setApplyDate(DateUtils.getCurrentTime());//申请日期
					drugApplyout.setApplyState(0);//申请状态
					drugApplyout.setApplyNum(Double.parseDouble(feedetailLists.getConfirmNum()+""));//申请数量
					drugApplyout.setBillclassCode("M1");//查询发药药房对应的发药台对应的摆药单类型
					drugApplyout.setShowFlag(feedetailLists.getExtFlag3());
					drugApplyout.setDays(1);//申请付数
					drugApplyout.setPreoutFlag(1);//预扣库存，来源于预扣库存的参数，参数设置预扣库存是，此处为是，否则为否。预扣库存的情况下，采取更新仓库主表中的预扣库存数量。
					drugApplyout.setChargeFlag(1);//已收费
					drugApplyout.setPatientId(registerInfo.getClinicCode() );//患者门诊号
					drugApplyout.setPatientDept(registerInfo.getDeptCode());//患者科室
					if(feedetailLists.getDoseOnce() != null){
						drugApplyout.setDoseOnce(feedetailLists.getDoseOnce().doubleValue());//每次剂量
					}
					drugApplyout.setDoseUnit(feedetailLists.getDoseUnit());//计量单位
					drugApplyout.setUsageCode(feedetailLists.getUsageCode());//用法代码
					drugApplyout.setUseName(feedetailLists.getUseName());//用法名称
					drugApplyout.setDfqFreq(feedetailLists.getFrequencyCode());//频次代码
					drugApplyout.setOrderType("1");//医嘱类别
					//drugApplyout.setMoOrder(feedetailLists.getId());//医嘱流水号
					drugApplyout.setCombNo(feedetailLists.getCombNo());//组合号 
					drugApplyout.setRecipeNo(feedetailLists.getRecipeNo());//处方号
					int sequenceNo = 0;
					if(feedetailLists.getSequenceNo() != null){
						sequenceNo = feedetailLists.getSequenceNo();
					}
					drugApplyout.setSequenceNo(sequenceNo);//处方内流水号
					drugApplyout.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					drugApplyout.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					drugApplyout.setCreateTime(DateUtils.getCurrentTime());
					drugApplyout.setDeptName(deptName);
					drugApplyout.setApplyOpername(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
					drugApplyout.setPrintEmplName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
					applyoutDAO.save(drugApplyout);
				}
			}
		}
		//预扣库存
		if(!"".equals(parameterMap.get("feedetailIds"))){
			String[] feedetailArrs = parameterMap.get("feedetailIds").split("','");
			if(feedetailArrs.length>0){
				for(int h=0;h<feedetailArrs.length;h++){
					OutpatientFeedetailNow outFeedetail = medicinelistDAO.queryOutFeedetail(feedetailArrs[h]);
					outstoreService.withholdStock(outFeedetail.getExecDpcd(),outFeedetail.getItemCode(),-outFeedetail.getNobackNum(),outFeedetail.getExtFlag3());
				}
			}
		}
		//修改发票
		String invoiceType = "402880a54e3e0568014e3e07b9150004";
		medicinelistDAO.saveInvoiceFinance(employee.getId(),parameterMap.get("invoiceNo"),invoiceType);
		map.put("resMsg", "success");
		map.put("resCode", "缴费成功");
		return matMap;
		
	}
	@Override
	public List<OutpatientFeedetailNow> findDrugStorage(String feedetailIds) {
		return medicinelistDAO.findDrugStorage(feedetailIds);
	}
	
	@Override
	public DrugStockinfo findStockinfoList(String execDpcd, String itemCode) {
		return medicinelistDAO.findStockinfoList(execDpcd,itemCode);
	}
	@Override
	public DrugInfo findDruginfoList(String itemCode) {
		return medicinelistDAO.findDruginfoList(itemCode);
	}
	@Override
	public OutpatientAccount getAccountByMedicalrecord(String midicalrecordId) {
		return medicinelistDAO.getAccountByMedicalrecord(midicalrecordId);
	}
	@Override
	public List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId) {
		return medicinelistDAO.queryAccountrecord(midicalrecordId);
	}
	@Override
	public OutpatientAccount veriPassWord(String md5Hex, String patientNo) {
		return medicinelistDAO.veriPassWord(md5Hex,patientNo);
	}
	@Override
	public Map<String, String> unDrugWarehouse(String feedetaiNotIds) {
		Map<String,String> map = new HashMap<String, String>();
		String[] feedetailIdsArr = null;
		if(!"".equals(feedetaiNotIds)){
			feedetailIdsArr = feedetaiNotIds.split("','");
			if(feedetailIdsArr.length>0){
				for(int i=0;i<feedetailIdsArr.length;i++){
					DrugUndrug undrug = medicinelistDAO.queryUnDrugById(feedetailIdsArr[i]);
					if(undrug.getUndrugIssubmit()==1){
						
					}
				}
			}
		}
		return map;
	}
	@Override
	public Map<String, String> getSystemTypeMap(String type) {
		List<BusinessDictionary> list = codeInInterDAO.getDictionary(type);
		Map<String,String> map = new HashMap<String,String>();
		for (BusinessDictionary b : list) {
			map.put(b.getEncode(), b.getName());
		}
		return map;
	}
	@Override
	public Map<String, String> getMoney(String feeID) {
		List<OutpatientFeedetailNow> feedetailList = medicineListDAO.getFeedetailByids(feeID);
		//根据门诊号进行分类		
		Map<String ,List<OutpatientFeedetailNow>>  clicnicCodeInvoiceTypeMap = new HashMap<String ,List<OutpatientFeedetailNow>>();
		//发票序号map key为cliniccode value为发票序号
		Map<String ,String>  invoiceSeqMap = new HashMap<String ,String>();
		//门诊号-发票号
		Map<String ,String>  invoiceNoMap = new HashMap<String ,String>();
		String invoiceType = parameterInnerDAO.getParameterByCode("invoiceFno");//获取分发票的类型
		
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
		}
		//获取多张发票号
		Map<String, String> queryFinanceInvoiceNoByNum = medicineListDAO.queryFinanceInvoiceNoByNum(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),HisParameters.OUTPATIENTINVOICETYPE, clicnicCodeInvoiceTypeMap.size());
		if("error".equals(queryFinanceInvoiceNoByNum.get("resMsg"))){
			throw new RuntimeException("INVOICE IS NOT ENOUGTH");
		}else{
			String[] invoiceNo = queryFinanceInvoiceNoByNum.get("resCode").split(",");
			int i = 0;
			for(Map.Entry<String ,List<OutpatientFeedetailNow>> invosMap : clicnicCodeInvoiceTypeMap.entrySet()){
				String clicnicCodeInvoiceType = invosMap.getKey();
				List<OutpatientFeedetailNow> value = invosMap.getValue();
				Double cost1 = 0.0;
				if(feedetailList.size()>0){
					for (OutpatientFeedetailNow fee : value) {
						cost1 += fee.getTotCost();
					}
				}
				BigDecimal bg = new BigDecimal(cost1);  
		        double cost = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				invoiceNoMap.put(clicnicCodeInvoiceType, invoiceNo[i]+"_"+cost+"_"+queryFinanceInvoiceNoByNum.get(invoiceNo[i]));
				i+=1;
			}
		}
		return invoiceNoMap;
	}
	public MinfeeStatCode getFeeStatCode(OutpatientFeedetailNow fee){
		//根据最小费用代码ID查询到最小费用代码的encode
		BusinessDictionary drugminimumcost =  innerCodeDao.getDictionaryByCode("drugMinimumcost",fee.getFeeCode());
		//根据最小费用代码的encode查询统计大类中统计大类的encode和name
		MinfeeStatCode minfeeStatCode = medicineListInInterDAO.minfeeStatCodeByEncode(drugminimumcost.getEncode());
		return minfeeStatCode;
	}
	
}
