 package cn.honry.inpatient.outBalance.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InpatientDerate;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.admission.dao.AdmissionDAO;
import cn.honry.inpatient.info.dao.InpatientInfoDAO;
import cn.honry.inpatient.outBalance.dao.InpatientAccountListDAO;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceHeadDAO;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceListDAO;
import cn.honry.inpatient.outBalance.dao.InpatientInPrepayDAO;
import cn.honry.inpatient.outBalance.dao.InpatientShiftDAO;
import cn.honry.inpatient.outBalance.dao.OutBalanceDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("inpatientAccountListService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientAccountListServiceImpl implements cn.honry.inpatient.outBalance.service.InpatientAccountListService{
		
	@Autowired
	@Qualifier(value = "inpatientAccountListDAO")
	private InpatientAccountListDAO inpatientAccountListDAO;
	@Autowired
	@Qualifier(value = "outBalanceDAO")
	private OutBalanceDAO outBalanceDAO;
	@Autowired
	@Qualifier(value = "inpatientShiftDAO")
	private InpatientShiftDAO inpatientShiftDAO;
	@Autowired
	@Qualifier(value = "admissionDAO")
	private AdmissionDAO admissionDAO;
	@Autowired
	@Qualifier(value = "inpatientInPrepayDAO")
	private InpatientInPrepayDAO inpatientInPrepayDAO;
	@Autowired
	@Qualifier(value = "inpatientBalanceHeadDAO")
	private InpatientBalanceHeadDAO inpatientBalanceHeadDAO;
	@Autowired
	@Qualifier(value = "inpatientBalanceListDAO")
	private InpatientBalanceListDAO inpatientBalanceListDAO;
	@Autowired
	@Qualifier(value = "inpatientInfoDAO")
	private InpatientInfoDAO inpatientDAO;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientAccount get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientAccount entity) {
		
	}

	@Override
	public List<InpatientAccount> getID(String inpatientNo) throws Exception {
		List<InpatientAccount> lst = inpatientAccountListDAO.getID(inpatientNo);
		return lst;
	}

	@Override
	public List<PatientAccountrepaydetail> getAccount(String id)
			throws Exception {
		List<PatientAccountrepaydetail> lst = inpatientAccountListDAO.getAccount(id);
		return lst;
	}
	
	@Override
	public List<InpatientInfo> getinpatient(String inpatientNo)
			throws Exception {
		List<InpatientInfo> lst = inpatientAccountListDAO.getinpatient(inpatientNo);
		return lst;
	}
	
	@Override
	public List<InpatientBalanceHead> queryinpatientNo(String inpatientNo)
			throws Exception {
		List<InpatientBalanceHead> lst = inpatientAccountListDAO.queryinpatientNo(inpatientNo);
		return lst;
	}

	@Override
	public List<InpatientBalancePay> getbalanceOpercode(String balanceOpercode)
			throws Exception {
		List<InpatientBalancePay> lst = inpatientAccountListDAO.getbalanceOpercode(balanceOpercode);
		return lst;
	}
	
	@Override
	public void UpdateInpatientInPrepay(String inpatientNo,String balanceNo,String invoiceNo) {
		inpatientAccountListDAO.UpdateInpatientInPrepay(inpatientNo,balanceNo,invoiceNo);
		OperationUtils.getInstance().conserve(inpatientNo,"预交金表","UPDATE","T_INPATIENT_INPREPAY",OperationUtils.LOGACTIONDELETE);
		inpatientAccountListDAO.UpdateInpatientDerate(inpatientNo,balanceNo,invoiceNo);
		OperationUtils.getInstance().conserve(inpatientNo,"费用减免表","UPDATE","T_INPATIENT_DERATE",OperationUtils.LOGACTIONDELETE);
		inpatientAccountListDAO.UpdateInpatientFeeInfo(inpatientNo,balanceNo,invoiceNo);
		OperationUtils.getInstance().conserve(inpatientNo,"住院费用汇总表","UPDATE","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONDELETE);
		inpatientAccountListDAO.UpdateInpatientMedicineList(inpatientNo,balanceNo,invoiceNo);
		OperationUtils.getInstance().conserve(inpatientNo,"住院药品明细表","UPDATE","T_INPATIENT_MEDICINELIST",OperationUtils.LOGACTIONDELETE);
		inpatientAccountListDAO.UpdateInpatientItemList(inpatientNo,balanceNo,invoiceNo);
		OperationUtils.getInstance().conserve(inpatientNo,"住院非药品明细表","UPDATE","T_INPATIENT_ITEMLIST",OperationUtils.LOGACTIONDELETE);
	}
	
	
	/**  
	 *  
	 * @Description：  保存住院收费结算实付表
	 * @Author：dh
	 * @ModifyDate：2015-1-7
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public void saveInpatientBalancePay(InpatientBalancePay inpatientBalancePay) {
		inpatientAccountListDAO.saveInpatientBalancePay(inpatientBalancePay);
		OperationUtils.getInstance().conserve(null, "住院收费结算实付表", "INSERT_INTO", "T_INPATIENT_BALANCEPAY", OperationUtils.LOGACTIONINSERT);
	}
	/**  
	 *  
	 * @Description：  保存费用减免表
	 * @Author：dh
	 * @ModifyDate：2015-1-7
	 * @ModifyRmk： 
	 * @version 1.0
	 *
	 */
	@Override
	public void saveInpatientDerate(InpatientDerate inpatientDerate) {
		inpatientAccountListDAO.saveInpatientDerate(inpatientDerate);
		OperationUtils.getInstance().conserve(null, "费用减免表", "INSERT_INTO", "T_INPATIENT_DERATE", OperationUtils.LOGACTIONINSERT);
	}

	@Override
	public void saveInpatientShiftData(InpatientShiftData inpatientShiftData) {
		inpatientAccountListDAO.saveInpatientShiftData(inpatientShiftData);
		OperationUtils.getInstance().conserve(null, "资料变更表", "INSERT_INTO", "T_INPATIENT_SHIFTDATA", OperationUtils.LOGACTIONINSERT);
	}
	@Override
	public void saveOrUpdate(String infoJson) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		SysDepartment dept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		Gson gson = new Gson();  
		List<InpatientBalancePay> modelList = null;
		try {
			modelList = gson.fromJson(infoJson, new TypeToken<List<InpatientBalancePay>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (InpatientBalancePay entity : modelList) {
			if(entity != null){
					entity.setCreateUser(user.getAccount());
					entity.setCreateDept(dept.getDeptName());
					entity.setCreateTime(new Date());
					OperationUtils.getInstance().conserve(null,"住院收费结算实付表","INSERT_INTO","T_INPATIENT_BALANCEPAY",OperationUtils.LOGACTIONINSERT);
				}
				inpatientAccountListDAO.save(entity);
			}
		}


	@Override
	public List<PatientAccountrepaydetail> QueryPatientAccountrepaydetail(
			String id) throws Exception {
		List<PatientAccountrepaydetail> lst = inpatientAccountListDAO.QueryPatientAccountrepaydetail(id);
		return lst;
	}

	@Override
	public List<PatientAccountrepaydetail> SumAccount(String id)
			throws Exception {
		List<PatientAccountrepaydetail> lst = inpatientAccountListDAO.SumAccount(id);
		return lst;
	}

	@Override
	public List<PatientAccountrepaydetail> QueryPatientAccountrepaydetailID(
			String id) throws Exception {
		List<PatientAccountrepaydetail> lst = inpatientAccountListDAO.QueryPatientAccountrepaydetailID(id);
		return lst;
	}

	@Override
	public List<PatientAccountrepaydetail> Queryzhuanya(String id)
			throws Exception {
		List<PatientAccountrepaydetail> lst = inpatientAccountListDAO.Queryzhuanya(id);
		return lst;
	}

	@Override
	public void UpdateInpatientInfo(String inpatientNo) {
		inpatientAccountListDAO.UpdateInpatientInfo(inpatientNo);
		OperationUtils.getInstance().conserve(inpatientNo,"住院登记表","UPDATE","T_INPATIENT_INFO",OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public void saveInpatientBalanceHead(InpatientBalanceHead inpatientBalanceHead) {
		inpatientAccountListDAO.saveInpatientBalanceHead(inpatientBalanceHead);
		OperationUtils.getInstance().conserve(null,"结算头表","INSERT INTO","T_INPATIENT_BALANCEHEAD",OperationUtils.LOGACTIONINSERT);
		
	}

	@Override
	public List<PatientAccountrepaydetail> getmedicalrecordId(String medicalrecordId)
			throws Exception {
		return inpatientAccountListDAO.getmedicalrecordId(medicalrecordId);
	}

	@Override
	public List<PatientAccountrepaydetail> getmedicalrecordIdZhuanya(
			String medicalrecordId) {
		return inpatientAccountListDAO.Queryzhuanya(medicalrecordId);
	}
	@Override
	public List<PatientAccountrepaydetail> getpayment(String medicalrecordId) {
		return inpatientAccountListDAO.getpayment(medicalrecordId);
	}

	@Override
	public List<PatientAccountrepaydetail> getpaymentzonge(
			String medicalrecordId) {
		return inpatientAccountListDAO.getpaymentzonge(medicalrecordId);
	}

	@Override
	public String saveBalance(Map<String, String> parameterMap,String zfJson) throws Exception {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		SysEmployee  empId = inpatientAccountListDAO.queryEmployee(userId);
		SysDepartment dept = ShiroSessionUtils
				.getCurrentUserDepartmentFromShiroSession();
		String msg = "";
 		//1.判断系统参数  住院日结后是否可以继续收费
		String code = "Endofday";
		String rj = "";
		if(rj=="0"){
			return msg="住院日结后不可以继续收费！";
		}else{
			List<InpatientCancelitemNow> lst = outBalanceDAO.getInpatientCancelitem(parameterMap.get("inpatientNo"));
			if(lst.size()>0){
				return msg="存在未确认的退费申请！";
			}else{
				List<OutpatientDrugcontrol> controlList = outBalanceDAO.queryDrugcontrol(parameterMap.get("inpatientNo"));
				if(controlList.size()>0){
					return msg="存在未确认的退药单！";
				}else{
					try {
					//1.将患者未结算的预交金信息（预交金列表中除转入预交金外）设置为结算状态，记录发票号、结算序号、结算时间、结算人
					inpatientAccountListDAO.UpdateInpatientInPrepay(parameterMap.get("medicalrecordId"),parameterMap.get("balanceNo"),parameterMap.get("invoiceNo"));
					OperationUtils.getInstance().conserve(parameterMap.get("inpatientNo"),"预交金表","UPDATE","T_INPATIENT_INPREPAY",OperationUtils.LOGACTIONDELETE);
					} catch (Exception e) {
						msg="处理预交金结算状态时出错！";
						return msg;
					}
					
					try {
						//2.将患者未结算的转入预交金设置为结算状态，记录操作人，操作时间和结算序号
						inpatientAccountListDAO.UpdateInpatientChangeprepay(parameterMap.get("inpatientNo"),parameterMap.get("balanceNo"));
						OperationUtils.getInstance().conserve(parameterMap.get("inpatientNo"),"转入预交金表","UPDATE","T_INPATIENT_INPREPAY",OperationUtils.LOGACTIONDELETE);
					} catch (Exception e) {
						msg="处理转入预交金结算状态时出错！";
						return msg;
					}
					
					
					try {
						//3.生成相应支付方式的结算记录，结算状态、结算时间取当前时间、结算人为当前人、结算序号取生成的结算序号、发票号取领取的发票号
						List<InpatientBalancePay> modelList=new ArrayList<InpatientBalancePay>();
						try {
							zfJson=zfJson.replaceAll("\"[a-zA-Z0-9]+\":\"\",", "")
									.replaceAll(",\"[a-zA-Z0-9]+\":\"\"", "")
									.replaceAll(",\"[a-zA-Z0-9]+\":\"\",", "");
							modelList = JSONUtils.fromJson(zfJson,  new TypeToken<List<InpatientBalancePay>>(){}, "yyyy-MM-dd hh:mm:ss");
						} catch (Exception e) {
							e.printStackTrace();
						}
						for (InpatientBalancePay entity : modelList) {
							if (entity != null) {
								entity.setId(null);//id
								entity.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
								entity.setTransType(1);//正交易
								entity.setTransKind(1);//交易种类 0 预交款 1 结算款
								entity.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
								entity.setReutrnorsupplyFlag(1);//返回或补收标志 1 补收 2 返还
								entity.setBalanceOpercode(empId.getId());//结算人代码
								entity.setBalanceDate(DateUtils.getCurrentTime());//结算时间
								entity.setCreateUser(empId.getId());
								entity.setCreateTime(DateUtils.getCurrentTime());
								entity.setUpdateUser(empId.getId());
								entity.setUpdateTime(DateUtils.getCurrentTime());
								OperationUtils.getInstance().conserve(null, "住院收费实付表","INSERT_INTO", "T_INPATIENT_BALANCEPAY",OperationUtils.LOGACTIONINSERT);
								inpatientInPrepayDAO.save(entity);
							}
						}
					} catch (Exception e) {
						msg="处理住院实付信息时出错！";
						return msg;
					}
					
					
					try {
						//4.减免金额
						String parameterValue = parameterInnerDAO.getParameterByCode("UpdateDerate");
						if(parameterValue==""){
							parameterValue = "2";
						}
						InpatientDerate inpatientDerate = new InpatientDerate();
						List<InpatientFeeInfo> InpatientFeeInfoList = null;
						String jianmian = parameterMap.get("jmMoney");
						if("0".equals(jianmian)){
							
						}else{
							if(parameterValue=="1"){//可修改
								for (InpatientFeeInfo inpatientFeeInfo : InpatientFeeInfoList) {
									inpatientDerate.setId(null);
									inpatientDerate.setTransType(1);//交易类型
									inpatientDerate.setClinicNo(parameterMap.get("inpatientNo"));//流水号
									InpatientDerate inpatient = null;
									inpatientDerate.setHappenNo(inpatient.getHappenNo());//发生序号
									inpatientDerate.setDerateKind("1");//减免种类
									inpatientDerate.setRecipeNo(inpatientFeeInfo.getRecipeNo());//处方号
									inpatientDerate.setFeeCode(inpatientFeeInfo.getFeeCode());//最小费用代码
									Double OwnCost = (inpatientFeeInfo.getOwnCost()/Double.valueOf(parameterMap.get("zfMoney")))*inpatientFeeInfo.getOwnCost();
									inpatientDerate.setDerateCost(OwnCost);//减免金额
									inpatientDerate.setDerateCause("结算减免");//减免原因
									inpatientDerate.setDerateType("3");//减免类型
									inpatientDerate.setBalanceState("1");//结算状态
									inpatientDerate.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
									List<InpatientInfoNow> InpatientInfoList = outBalanceDAO.InpatientInfoqueryFee(parameterMap.get("inpatientNo"));
									inpatientDerate.setDeptCode(InpatientInfoList.get(0).getDeptCode());//减免科室
									inpatientDerate.setCreateUser(empId.getId());
									inpatientDerate.setCreateTime(DateUtils.getCurrentTime());
									inpatientDerate.setUpdateUser(empId.getId());
									inpatientDerate.setUpdateTime(DateUtils.getCurrentTime());
								}
							}else if(parameterValue=="2"){//不可修改
								inpatientAccountListDAO.UpdateInpatientDerate(parameterMap.get("inpatientNo"),parameterMap.get("balanceNo"),parameterMap.get("invoiceNo"));
							}
						}
					} catch (Exception e) {
						msg="处理减免金额时出错！";
						return msg;
					}
					
					String parameterValue2 = parameterInnerDAO.getParameterByCode("isBabyP");
					String parameterValue3 = parameterInnerDAO.getParameterByCode("isPreferentialP");
					String parameterValue4 = parameterInnerDAO.getParameterByCode("isFoodP");
					String parameterValue5 = parameterInnerDAO.getParameterByCode("isderateP");
					String parameterValue6 = parameterInnerDAO.getParameterByCode("isDrugP");
					
					List<InpatientFeeInfo> in = null;
					List<InpatientInfoNow> InpatientInfoList = outBalanceDAO.InpatientInfoqueryFee(parameterMap.get("inpatientNo"));
					try {
						//5.结算头表
						InpatientBalanceHead Head=new InpatientBalanceHead();
						Head.setId(null);//id
						if(in.get(0).getPaykindCode()==null){
							Head.setPaykindCode(null);//结算类别
						}else{
							Head.setPaykindCode(in.get(0).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
						}
						Head.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号码
						Head.setTransType(1);//交易类型 
						Head.setInpatientNo(parameterMap.get("inpatientNo")); //流水号
						Head.setName(InpatientInfoList.get(0).getPatientName()); //姓名
						Head.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						if(in.get(0).getPaykindCode()==null){
							Head.setPaykindCode(null);//结算类别
						}else{
							Head.setPaykindCode(in.get(0).getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
						}
						Head.setPactCode(in.get(0).getPactCode());//合同代码
						Head.setPrepayCost(Double.valueOf(parameterMap.get("yj")));//预交金额
						Head.setChangePrepaycost(null);//转入预交金额
						Head.setTotCost(in.get(0).getTotCost());//费用金额
						Head.setOwnCost(in.get(0).getOwnCost());//自费金额
						Head.setPayCost(in.get(0).getPayCost());//自付金额
						Head.setPubCost(in.get(0).getPubCost());//公费金额
						Head.setEcoCost(in.get(0).getEcoCost());//优惠金额
						Head.setDerCost(Double.valueOf(parameterMap.get("jmMoney")));//减免金额
						Head.setWasteFlag(1);//扩展标志1
						String sh1 = parameterMap.get("sh1");
						String sh = parameterMap.get("sh");
						if("".equals(sh1)){
							Head.setSupplyCost(Double.valueOf(parameterMap.get("sh")));//补收金额
						}
						if("".equals(parameterMap.get("sh"))){
							Head.setReturnCost(Double.valueOf(parameterMap.get("sh1")));//返还金额
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					    try {
							Date inDate = sdf.parse(parameterMap.get("inDate"));
							Date outDate = sdf.parse(parameterMap.get("outDate"));
							Head.setBeginDate(inDate);//起始日期
							Head.setEndDate(outDate);//终止日期
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Head.setBalanceType(2);//结算类型 1:在院结算，2:出院结算，3:直接结算，4:重结算 5:结转，6:欠费结算
						Head.setBalanceOpercode(empId.getId());//结算人代码
						Head.setBalanceDate(DateUtils.getCurrentTime());//结算时间
						Head.setBalanceoperDeptcode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//结算员科室
						Head.setCheckFlag(1);//0未核查/1已核查
						//TODO 核查人  核查人name
						Head.setCreateUser(empId.getId());
						Head.setCreateTime(DateUtils.getCurrentTime());
						Head.setUpdateUser(empId.getId());
						Head.setUpdateTime(DateUtils.getCurrentTime());
						inpatientBalanceHeadDAO.save(Head);
					} catch (Exception e) {
						e.printStackTrace();
						msg="处理住院费用汇总信息状态时出错！";
						return msg;
					}
					
					List<InpatientFeeInfoNow> InpatientFeeList;
					try {
						InpatientFeeList = outBalanceDAO.InpatientFeeList(parameterMap.get("inpatientNo"),null,null);
						for (InpatientFeeInfoNow Fee : InpatientFeeList) {
							List<MinfeeStatCode> MinfeeStatCodeList = null;
							//6.住院结算明细表
							InpatientBalanceList inpatientBalanceList = new InpatientBalanceList();
							inpatientBalanceList.setId(null);//id
							inpatientBalanceList.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号码
							inpatientBalanceList.setTransType(1);//交易类型,1正交易，2反交易
							inpatientBalanceList.setName(InpatientInfoList.get(0).getPatientName()); //姓名
							inpatientBalanceList.setInpatientNo(parameterMap.get("inpatientNo"));//住院流水号
							if(Fee.getPaykindCode()==null){
								inpatientBalanceList.setPaykindCode(null);//结算类别
							}else{
								inpatientBalanceList.setPaykindCode(Fee.getPaykindCode());//结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干
							}
							inpatientBalanceList.setPactCode(Fee.getPactCode());//合同单位
							inpatientBalanceList.setDeptCode(InpatientInfoList.get(0).getDeptCode());//在院科室
							inpatientBalanceList.setStatCode(MinfeeStatCodeList.get(0).getFeeStatCode());//统计大类
							inpatientBalanceList.setStatName(MinfeeStatCodeList.get(0).getFeeStatName());//统计大类名称
							inpatientBalanceList.setTotCost(Fee.getTotCost());//费用金额
							inpatientBalanceList.setOwnCost(Fee.getOwnCost());//自费金额
							inpatientBalanceList.setPayCost(Fee.getPayCost());//自付金额
							inpatientBalanceList.setPubCost(Fee.getPubCost());//公费金额
							inpatientBalanceList.setEcoCost(Fee.getEcoCost());//优惠金额
							inpatientBalanceList.setBalanceOpercode(empId.getId());//结算人代码
							inpatientBalanceList.setBalanceDate(DateUtils.getCurrentTime());//结算时间
							inpatientBalanceList.setBalanceType(3);//结算类型 1:在院结算，2:转科结算，3:出院结算，4:重结算 5:结转
							inpatientBalanceList.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
							inpatientBalanceList.setBalanceoperDeptcode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//结算员科室
							inpatientBalanceList.setCreateUser(empId.getId());
							inpatientBalanceList.setCreateTime(DateUtils.getCurrentTime());
							inpatientBalanceList.setUpdateUser(empId.getId());
							inpatientBalanceList.setUpdateTime(DateUtils.getCurrentTime());
							inpatientBalanceListDAO.save(inpatientBalanceList);
						}
					} catch (Exception e1) {
						msg="处理住院费用汇总信息状态时出错！";
						return msg;
					}
					
					try {
						//7.根据页面的预交金信息生成一条交易种类为预交款、支付方式为现金的补收状态住院收费结算实付记录
						InpatientBalancePay model=new InpatientBalancePay();
						model.setId(null);//id
						model.setInvoiceNo(parameterMap.get("invoiceNo"));//发票号
						model.setTransType(1);//正交易
						model.setTransKind(0);//交易种类 0 预交款 1 结算款
						model.setPayWay("1");//支付方式  现金
						model.setChangePrepaycost(Double.valueOf(parameterMap.get("yj")));//预交金
						model.setBalanceNo(Integer.valueOf(parameterMap.get("balanceNo")));//结算序号
						model.setReutrnorsupplyFlag(1);//返回或补收标志 1 补收 2 返还
						model.setBalanceOpercode(empId.getId());//结算人代码
						model.setBalanceDate(DateUtils.getCurrentTime());//结算时间
						model.setCreateUser(empId.getId());
						model.setCreateTime(DateUtils.getCurrentTime());
						model.setUpdateUser(empId.getId());
						model.setUpdateTime(DateUtils.getCurrentTime());
						OperationUtils.getInstance().conserve(null, "住院收费实付表","INSERT_INTO", "T_INPATIENT_BALANCEPAY",OperationUtils.LOGACTIONINSERT);
						inpatientInPrepayDAO.save(model);
					} catch (Exception e) {
						msg="处理预交金生成实付信息时出错！";
						return msg;
					}
					
					
					
				    try {
				    	//9.更新住院药品、非药品费用明细为结算状态、记录结算序号及发票号
						inpatientAccountListDAO.UpdateInpatientMedicineList(parameterMap.get("inpatientNo"),parameterMap.get("balanceNo"),parameterMap.get("invoiceNo"));
						OperationUtils.getInstance().conserve(parameterMap.get("inpatientNo"),"住院药品明细表","UPDATE","T_INPATIENT_MEDICINELIST",OperationUtils.LOGACTIONUPDATE);
						inpatientAccountListDAO.UpdateInpatientItemList(parameterMap.get("inpatientNo"),parameterMap.get("balanceNo"),parameterMap.get("invoiceNo"));
						OperationUtils.getInstance().conserve(parameterMap.get("inpatientNo"),"住院非药品明细表","UPDATE","T_INPATIENT_ITEMLIST",OperationUtils.LOGACTIONUPDATE);
					} catch (Exception e) {
						msg="处理药品非药品状态时出错！";
						return msg;
					}
				    
				    try {
				    	//10.
						//修改发票
						String invoiceType = "03";
						outBalanceDAO.saveInvoiceFinance(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),parameterMap.get("invoiceNo"),invoiceType);
				    } catch (Exception e) {
				    	msg="处理修改发票时出错！";
				    	return msg;
				    }
				    
				    
				    try {
				    	//11.保存资料变更表
						InpatientShiftData inpatientShiftData =new InpatientShiftData();
						inpatientShiftData.setClinicNo(parameterMap.get("inpatientNo"));//住院流水号
						inpatientShiftData.setShiftType("BA");//变更类型
							//查询最大的发生序号
						List<InpatientShiftData> inpatientShift= admissionDAO.queryMaxHappenNo();
						inpatientShiftData.setHappenNo(inpatientShift.get(0).getHappenNo()+1);//发生序号
						inpatientShiftDAO.save(inpatientShiftData);
						OperationUtils.getInstance().conserve(null, "资料变更表", "INSERT_INTO", "T_INPATIENT_SHIFTDATA", OperationUtils.LOGACTIONINSERT);
				    } catch (Exception e) {
				    	msg="保存资料变更时出错！";
				    	return msg;
				    }
				    
				    try {
				    	//12.对患者进行开账和修改结算序号
				    	List<InpatientFeeInfo> FeeInfo =null;
				    	List<InpatientInfoNow> Inpatien = outBalanceDAO.InpatientInfoqueryFee(parameterMap.get("inpatientNo"));
				    	for (InpatientInfoNow info : Inpatien) {
				    		info.setInState("O");
				    		info.setStopAcount(0);
				    		info.setBalanceNo(info.getBalanceNo()+1);
				    		info.setBalanceDate(new Date());
				    		info.setTotCost(info.getTotCost()-FeeInfo.get(0).getTotCost());
				    		info.setOwnCost(info.getOwnCost()-FeeInfo.get(0).getOwnCost());
				    		info.setPayCost(info.getPayCost()-FeeInfo.get(0).getPayCost());
				    		info.setPubCost(info.getPubCost()-FeeInfo.get(0).getPubCost());
				    		info.setEcoCost(info.getEcoCost()-FeeInfo.get(0).getEcoCost());
				    		info.setBalancePrepay(info.getBalancePrepay()+Double.valueOf(parameterMap.get("yj")));
				    		inpatientDAO.save(info);
						}
						OperationUtils.getInstance().conserve(parameterMap.get("inpatientNo"),"住院登记表","UPDATE","T_INPATIENT_INFO",OperationUtils.LOGACTIONUPDATE);
				    } catch (Exception e) {
				    	e.printStackTrace();
				    	msg="处理住院主表信息时出错！";
				    	return msg;
				    }
				    
				    
				    try {
					//8.更新住院费用汇总信息为结算状态，记录结算序号、结算时间、结算操作人，发票号使用主发票号
						inpatientAccountListDAO.UpdateInpatientFeeInfo(parameterMap.get("inpatientNo"),parameterMap.get("balanceNo"),parameterMap.get("invoiceNo"));
						OperationUtils.getInstance().conserve(parameterMap.get("inpatientNo"),"住院费用汇总表","UPDATE","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONUPDATE);
					} catch (Exception e) {
						msg="处理住院费用汇总信息状态时出错！";
						return msg;
					}
				}
			}
		}
		return "success";
	}
}
