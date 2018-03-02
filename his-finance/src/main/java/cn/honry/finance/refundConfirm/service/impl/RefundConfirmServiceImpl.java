package cn.honry.finance.refundConfirm.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientDaybalance;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.StoRecipeNow;
import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.TecApply;
import cn.honry.base.bean.model.TecTerminalApply;
import cn.honry.base.bean.model.User;
import cn.honry.finance.medicinelist.dao.MedicinelistDAO;
import cn.honry.finance.medicinelist.vo.FeeCodeVo;
import cn.honry.finance.medicinelist.vo.SpeNalVo;
import cn.honry.finance.refund.dao.RefundDAO;
import cn.honry.finance.refund.vo.MedicinelDrugList;
import cn.honry.finance.refund.vo.RecipeNosVo;
import cn.honry.finance.refundConfirm.dao.RefundConfirmDAO;
import cn.honry.finance.refundConfirm.service.RefundConfirmService;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.drug.applyout.dao.ApplyoutInInterDAO;
import cn.honry.inner.drug.outstore.service.OutStoreInInterService;
import cn.honry.inner.drug.sendWicket.dao.SendWicketInInterDAO;
import cn.honry.inner.drug.stockInfo.service.BusinessStockInfoInInterService;
import cn.honry.inner.outpatient.medicineList.dao.MedicineListInInterDAO;
import cn.honry.inner.outpatient.medicineList.vo.DrugOrUNDrugVo;
import cn.honry.inner.system.keyvalue.dao.KeyvalueInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.technical.mat.vo.OutputInInterVO;
import cn.honry.inner.technical.tecapply.dao.TecApplyInInInterDAO;
import cn.honry.inner.technical.terminalApply.dao.TerminalApplyInInterDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.ShiroSessionUtils;

@SuppressWarnings("all")
@Transactional
@Service("refundConfirmService")
public class RefundConfirmServiceImpl implements RefundConfirmService{

	@Autowired
	@Qualifier("refundConfirmDAO")
	private RefundConfirmDAO refundConfirmDAO;
	@Autowired
	@Qualifier("refundDAO")
	private RefundDAO refundDAO;
	@Autowired
	@Qualifier(value = "outstoreInInterService")
	private OutStoreInInterService outstoreService;
	@Autowired
	@Qualifier("medicinelistDAO")
	private MedicinelistDAO medicineListDAO;
	@Autowired
	@Qualifier(value = "keyvalueInInterDAO")
	private KeyvalueInInterDAO keyvalueDAO;//参数表
	@Autowired
	@Qualifier(value = "applyoutInInterDAO")
	private ApplyoutInInterDAO applyoutDAO;//出库申请表
	@Autowired
	@Qualifier(value = "medicineListInInterDAO")
	private MedicineListInInterDAO medicineListInInterDAO;
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO codeInInterDAO;
	@Autowired
	@Qualifier(value = "keyvalueInInterDAO")
	private KeyvalueInInterDAO keyvalueInInterDAO;//参数表
	@Autowired
	@Qualifier(value = "businessStockInfoInInterService")
	private BusinessStockInfoInInterService businessStockInfoInInterService;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	@Autowired
	@Qualifier(value = "sendWicketInInterDAO")
	private SendWicketInInterDAO sendWicketInInterDAO;
	@Autowired
	@Qualifier(value="terminalApplyInInterDAO")
	private TerminalApplyInInterDAO terminalApplyInInterDAO;
	@Autowired
	@Qualifier(value="tecApplyInInInterDAO")
	private TecApplyInInInterDAO tecApplyInInInterDAO;
	@Autowired
	@Qualifier(value = "medicinelistDAO")
	private MedicinelistDAO medicinelistDAO;
	@Override
	public InpatientCancelitem get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(InpatientCancelitem arg0) {
	}

	@Override
	public List<InpatientCancelitemNow> query(String billNo) {
		return refundConfirmDAO.query(billNo);
	}

	@Override
	public Map<String, String> refundSave(String drugApplyIds, String newInvoiceNo, String invoiceNo, String payType, String applyCost,String medicalRecord) {
		FinanceInvoiceInfoNow invoiceInfoApply = refundConfirmDAO.queryByInfo(invoiceNo);
		List<MedicinelDrugList> medicinelDrugList = new ArrayList<MedicinelDrugList>();
		List<InpatientCancelitemNow> cancelitemList = refundConfirmDAO.findByIds(drugApplyIds);
		
		
		if(cancelitemList.size()>0){
			for(InpatientCancelitemNow modls :cancelitemList){
				OutpatientFeedetailNow feedetailApply = refundConfirmDAO.queryByRecipeNo(modls.getRecipeNo(),modls.getSequenceNo());
				MedicinelDrugList medicinel = new MedicinelDrugList();
				medicinel.setItemName(modls.getItemName());/**药品名称**/
				medicinel.setCombNo(feedetailApply.getCombNo());/**组**/
				medicinel.setSpecs(feedetailApply.getSpecs());/**规格**/
				medicinel.setQty(feedetailApply.getQty().doubleValue());/**数量**/
				medicinel.setPriceUnit(feedetailApply.getPriceUnit());/**单位**/
				medicinel.setNobackNum(feedetailApply.getQty()-modls.getQuantity());/**可退数量**/
				medicinel.setTotCost((feedetailApply.getQty()-modls.getQuantity())*feedetailApply.getUnitPrice());/**金额**/
				medicinel.setDoseOnce(feedetailApply.getDoseOnce());/**每次用量**/
				medicinel.setFeeCpcd(feedetailApply.getFeeCpcd());/**收费人**/
				medicinel.setId(feedetailApply.getId());/**ID**/
				medicinel.setRecipeNo(modls.getRecipeNo());/**处方号**/
				medicinel.setDrugFlag(feedetailApply.getDrugFlag()+"");/**药品标志**/
				medicinel.setExecDpcd(feedetailApply.getExecDpcd());/**执行科室**/
				medicinel.setUnitPrice(feedetailApply.getUnitPrice());/**单价**/
				medicinel.setItemCode(feedetailApply.getItemCode());/**药品ID**/
				medicinel.setExtFlag3(feedetailApply.getExtFlag3());/**包装**/
				medicinel.setSequenceNo(modls.getSequenceNo());/**处方内流水号**/
				medicinel.setNobackNums(feedetailApply.getQty().doubleValue());/**虚拟可退数量**/
				medicinelDrugList.add(medicinel);
			}
		}
		
		
		Double price = (invoiceInfoApply.getTotCost())-Double.parseDouble(applyCost);
		Map<String, String> map = new HashMap<String, String>();
		if("2".equals(payType)){
			//判断患者是否存在院内账户
			OutpatientAccount account = refundDAO.getAccountByMedicalrecord(medicalRecord);
			if(StringUtils.isBlank(account.getId())){
				map.put("resMsg", "error");
				map.put("resCode", "此患者无账户信息,请联系管理员!");
				return map;
			}else if(account.getAccountState()==0){
				map.put("resMsg", "error");
				map.put("resCode", "账户已停用,请联系管理员!");
				return map;
			}else if(account.getAccountState()==2){
				map.put("resMsg", "error");
				map.put("resCode", "账户已注销!");
				return map;
			}else{
				account.setAccountBalance(account.getAccountBalance()+price);
				refundDAO.save(account);
				
				//添加门诊账户明细表
				OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
				accountrecord.setId(null);//ID
				accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
				accountrecord.setAccountId(account.getId());//门诊账户编号
				accountrecord.setOpertype(5);//操作类型
				accountrecord.setMoney(price);//交易金额
				accountrecord.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//相关科室
				accountrecord.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员 
				accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
				accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
				accountrecord.setValid(0);//是否有效
				accountrecord.setInvoiceType(HisParameters.OUTPATIENTINVOICETYPE);//发票类型
				accountrecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				accountrecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				accountrecord.setCreateTime(DateUtils.getCurrentTime());
				accountrecord.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				accountrecord.setUpdateTime(DateUtils.getCurrentTime());
				refundDAO.save(accountrecord);
			}
		}
		
		//初始化MAP
		Map<String, Object> outMap = new HashMap<String, Object>();
		//初始化发票序号
		String invoiceSeq = "";
		//根据发票号查询发票序号
		List<FinanceInvoiceInfoNow> invoiceInfoList = refundDAO.findInvoiceInfoByInvoiceNo(invoiceNo);
		if(invoiceInfoList.size()>0){
			for(FinanceInvoiceInfoNow info : invoiceInfoList){
				if(!"".equals(invoiceSeq)){
					invoiceSeq = invoiceSeq + "','";
				}
				invoiceSeq = invoiceSeq + info.getInvoiceSeq();
			}
		}
		//根据发票序号查询所有发票明细表中信息
		List<FinanceInvoicedetailNow> invoicedetailList = refundDAO.findInvoiceTailByInvoiceSeq(invoiceSeq);
		//根据发票序号查询所有发票结算表中信息
		List<FinanceInvoiceInfoNow> invoiceInfoLists = refundDAO.findInvoiceInfoByInvoiceSeqs(invoiceSeq);
		//支付方式情况表
		List<BusinessPayModeNow> payModeList = refundDAO.findPayModeListBySeq(invoiceSeq);
		//收费明细表
		List<OutpatientFeedetailNow> feedetailList = refundDAO.findFeedetailListBySeq(invoiceSeq);
		//初始化药品ID
		String drugIds = "";
		if(feedetailList.size()>0){
			for (OutpatientFeedetailNow fee : feedetailList) {
				if("1".equals(fee.getDrugFlag())){
					List<DrugApplyoutNow> list = refundDAO.findApplyoutByItemCode(fee.getItemCode(),fee.getMoOrder());
					for (DrugApplyoutNow app : list) {
						app.setApplyState(3);
						app.setValidState(0);
						refundDAO.update(app);
					}
				}
			}
		}
		//初始化处方号
		String recipeNo = "";
		if(feedetailList.size()>0){
			for(OutpatientFeedetailNow fee : feedetailList){
				if(!"".equals(recipeNo)){
					recipeNo = recipeNo + "','";
				}
				recipeNo = recipeNo + fee.getRecipeNo();
			}
		}
		//处方调剂头表
		List<StoRecipeNow> stoRecipeList = refundDAO.findStoRecipeByRecipNo(recipeNo);
		//发票内流水号
		if(invoicedetailList.size()>0){
			for(FinanceInvoicedetailNow tail : invoicedetailList){
				FinanceInvoicedetailNow n_tail=new FinanceInvoicedetailNow();
				BeanUtils.copyProperties(tail, n_tail);
				//修改原来发票明细信息
				tail.setCancelFlag(0);
				refundDAO.update(tail);
				//添加冲账记录
				n_tail.setId(null);//ID
				n_tail.setTransType(2);//正反类型1正2反
				n_tail.setPayCost(-n_tail.getPayCost());
				n_tail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				n_tail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				n_tail.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(n_tail);
				
			}
		}
		
		if(invoiceInfoLists.size()>0){
			for(FinanceInvoiceInfoNow info : invoiceInfoLists){
				FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
				BeanUtils.copyProperties(info,invoiceInfo);
				//修改原发票结算表
				info.setCancelFlag(0);
				refundDAO.update(info);
				//添加冲账记录
				invoiceInfo.setId(null);
				invoiceInfo.setTransType(2);//交易类型
				invoiceInfo.setTotCost(-invoiceInfo.getTotCost());
				invoiceInfo.setPayCost(-invoiceInfo.getPayCost());//自付金额
				invoiceInfo.setRealCost(-invoiceInfo.getRealCost());
				invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(invoiceInfo);
			}
		}
		
		if(payModeList.size()>0){
			for(BusinessPayModeNow pay :payModeList){
				BusinessPayModeNow payMode = new BusinessPayModeNow();
				BeanUtils.copyProperties(pay,payMode);
				//修改原数据
				pay.setCancelFlag(0);
				refundDAO.save(pay);
				//冲账记录
				payMode.setId(null);//ID
				payMode.setTransType(2);//交易类型
				payMode.setTotCost(-payMode.getTotCost());//应付金额
				payMode.setRealCost(-payMode.getRealCost());//实付金额
				payMode.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				payMode.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				payMode.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(payMode);
			}
		}
		
		if(feedetailList.size()>0){
			for(OutpatientFeedetailNow fee : feedetailList){
				OutpatientFeedetailNow feedetail = new OutpatientFeedetailNow();
				BeanUtils.copyProperties(fee,feedetail);
				//修改原纪录
				fee.setCancelFlag(0);
				refundDAO.update(fee);
				//插入冲账记录
				feedetail.setId(null);
				feedetail.setTransType(2);
				feedetail.setTotCost(-feedetail.getTotCost());
				feedetail.setQty(-feedetail.getQty());
				feedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				feedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				feedetail.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(feedetail);
			}
		}
		
		if(stoRecipeList.size()>0){
			for(StoRecipeNow sto : stoRecipeList){
				StoRecipeNow stoRecipe = new StoRecipeNow();
				BeanUtils.copyProperties(sto,stoRecipe);
				//修改原数据
				sto.setModifyFlag(1);
				sto.setValidState(0);
				refundDAO.update(sto);
				//冲账记录
				stoRecipe.setId(null);
				stoRecipe.setClassMeaningCode("3");
				stoRecipe.setTransType(2);
				stoRecipe.setModifyFlag(1);
				stoRecipe.setRecipeCost(-stoRecipe.getRecipeCost());
				stoRecipe.setRecipeQty(-stoRecipe.getRecipeQty());
				stoRecipe.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				stoRecipe.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				stoRecipe.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(stoRecipe);
			}
		}
		
		List<OutpatientFeedetailNow> feedetailListBT = new ArrayList<OutpatientFeedetailNow>();
		String clinicCode = null;//门诊号
		List<FeeCodeVo> feeCodeVoListBT = new ArrayList<FeeCodeVo>();
		String invoiceSeqs = medicineListDAO.getSeqByName("SEQ_INVOICE_SEQ");//查询序列得出序号
		
		if(medicinelDrugList.size()>0){
			for(MedicinelDrugList drugVo:medicinelDrugList){
				if(drugVo.getNobackNum()>0){
					OutpatientFeedetailNow feedetail = refundDAO.queryFeedetailById(drugVo.getId());
					clinicCode = feedetail.getClinicCode();//门诊号获取
					OutpatientFeedetailNow feedetailNew = new OutpatientFeedetailNow();
					BeanUtils.copyProperties(feedetail,feedetailNew);
					feedetailNew.setId(null);
					feedetailNew.setQty(drugVo.getNobackNum());//数量
					feedetailNew.setNobackNum(drugVo.getNobackNum());//可退数量
					feedetailNew.setConfirmNum(drugVo.getNobackNum());//确认数量
					feedetailNew.setTotCost((drugVo.getNobackNum())*feedetail.getUnitPrice());//费用金额
					feedetailNew.setInvoiceNo(newInvoiceNo);//新发票
					feedetailNew.setInvoiceSeq(invoiceSeqs);//新发票序号
					feedetailNew.setCancelFlag(1);
					feedetailNew.setFeeCpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//收费人
					feedetailNew.setFeeDate(new Date());//收费时间
					String moOrder = medicineListDAO.getSeqByName("SEQ_ADVICE_STREAMNO"); //医嘱流水号
					feedetailNew.setMoOrder(moOrder);
					String recipeSeq = medicineListDAO.getSeqByName("SEQ_ADVICE_CHARGESEQ"); //收费序列
					feedetailNew.setRecipeSeq(recipeSeq);
					String groupNo = medicineListDAO.getSeqByName("SEQ_ADVICE_GROUPNO"); //或得组合号
					feedetailNew.setCombNo(groupNo);
					feedetailListBT.add(feedetailNew);
					refundDAO.save(feedetailNew);
				}
			}
			
			if(feedetailListBT.size()>0){
				Map<String,Double> mapNo = new HashMap<String,Double>();
				if(feedetailListBT.size()>0){
					for(OutpatientFeedetailNow modls :feedetailListBT ){
						//最小费用代码转化统计大类
						BusinessDictionary drugminimumcost = codeInInterDAO.getDictionaryByCode("drugMinimumcost", modls.getFeeCode());
						MinfeeStatCode feeStatCode = refundDAO.findMinfeeStatCode(drugminimumcost.getEncode());
						if(mapNo.get(feeStatCode.getFeeStatCode()+"_"+feeStatCode.getFeeStatName())==null){
							mapNo.put(feeStatCode.getFeeStatCode()+"_"+feeStatCode.getFeeStatName(), modls.getTotCost());
						}else{
							mapNo.put(feeStatCode.getFeeStatCode()+"_"+feeStatCode.getFeeStatName(),mapNo.get(feeStatCode.getFeeStatCode()+"_"+feeStatCode.getFeeStatName())+modls.getTotCost());
						}
					}
				}
				if(mapNo.size()>0){//当统计之后有数据时
					FeeCodeVo vo = null;
					for(Map.Entry<String, Double> entry : mapNo.entrySet()){//根据页面显示格式将数据改成相应的格式
						vo = new FeeCodeVo();
						String[] arr = entry.getKey().split("_");
						vo.setFeeTypeCode(arr[0]);
						vo.setFeeTypeName(arr[1]);
						BigDecimal fee = new   BigDecimal(entry.getValue());
						Double fees = fee.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
						vo.setFees(fees);
						feeCodeVoListBT.add(vo);
					}
				}
			}
			
			//发票流水号
			int i =0;
			if(feeCodeVoListBT.size()>0){
				for(FeeCodeVo modl:feeCodeVoListBT){
					FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
					i++;
					invoicedetail.setId(null);//ID
					invoicedetail.setInvoiceNo(newInvoiceNo);//发票号
					invoicedetail.setTransType(1);//正反类型1正2反
					invoicedetail.setInvoSequence(null);//发票号内流水号
					invoicedetail.setInvoCode(modl.getFeeTypeCode());//发票科目代码
					invoicedetail.setInvoName(modl.getFeeTypeName());//发票科目名称
					invoicedetail.setPubCost(0.00);//可报效金额
					invoicedetail.setOwnCost(0.00);//不可报效金额
					invoicedetail.setPayCost(modl.getFees());//自付金额
					invoicedetail.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//执行科室ID
					invoicedetail.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//执行科室名称
					invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
					invoicedetail.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作人
					invoicedetail.setBalanceFlag(0);//日结状态
					invoicedetail.setBalanceNo(null);//日结标识号
					invoicedetail.setBalanceOpcd(null);//日结人
					invoicedetail.setBalanceDate(null);//日结时间
					invoicedetail.setCancelFlag(1);//状态
					invoicedetail.setInvoiceSeq(invoiceSeqs);//发票序号
					invoicedetail.setInvoSequence(i+"");//发票内流水号
					invoicedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					invoicedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					invoicedetail.setCreateTime(DateUtils.getCurrentTime());
					invoicedetail.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
					refundDAO.save(invoicedetail);
				}
			}
			RegistrationNow  registerInfo = refundDAO.queryRegisterInfo(clinicCode);
			
			
			BusinessPayModeNow payMode = new BusinessPayModeNow();
			payMode.setInvoiceNo(newInvoiceNo);//发票号
			payMode.setTransType(1);//交易类型
			payMode.setSequenceNo(1);//交易内流水号
			payMode.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
			payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
			payMode.setModeCode("1"); 
			payMode.setTotCost(price);//应付金额
			payMode.setRealCost(price);//实付金额
			payMode.setBalanceFlag(0);//是否结算
			payMode.setCancelFlag(1);//状态
			payMode.setInvoiceSeq(invoiceSeqs);//发票序号
			payMode.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			payMode.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			payMode.setCreateTime(DateUtils.getCurrentTime());
			payMode.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			refundDAO.save(payMode);
			
			int h = 0;//初始状态
			
			//初始化VO
			List<RecipeNosVo> recipeNosVoList = new ArrayList<RecipeNosVo>();
			RecipeNosVo recipeNosVo = new RecipeNosVo();
			//摆药单
			if(medicinelDrugList.size()>0){
				for(MedicinelDrugList modls : medicinelDrugList){
					if("1".equals(modls.getDrugFlag())&&modls.getNobackNum()>0){
						if(recipeNosVoList.size()>0){
							for(RecipeNosVo vo:recipeNosVoList){
								if(vo.getRecipeNo().equals(modls.getRecipeNo())){
									h = 1;
								}
							}
							if(h==0){
								recipeNosVo =new RecipeNosVo();
								recipeNosVo.setRecipeNo(modls.getRecipeNo());
								recipeNosVoList.add(recipeNosVo);
							}
							h=0;
						}else{
							recipeNosVo.setRecipeNo(modls.getRecipeNo());
							recipeNosVoList.add(recipeNosVo);
						}
					} 
				}
			}
			
			String wondowType ="";
			
			if(recipeNosVoList.size()>0){
				for(RecipeNosVo vo:recipeNosVoList){
					List<OutpatientFeedetailNow> feedetails = medicineListDAO.queryFeedetailRecipeNo(vo.getRecipeNo());
					Double recipeCost = 0.0;//处方金额(零售金额)
					Double recipeQty = 0.0;//处方中药品数量
					StoRecipeNow stoRecipe = new StoRecipeNow();
					stoRecipe.setId(null);
					stoRecipe.setDrugDeptCode(feedetails.get(0).getExecDpcd());//执行科室/发放药房
					stoRecipe.setRecipeNo(vo.getRecipeNo());//处方号
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
					stoRecipe.setInvoiceNo(newInvoiceNo);//发票号
					if(medicinelDrugList.size()>0){
						for(MedicinelDrugList drugVo:medicinelDrugList){
							if(drugVo.getNobackNum()>0 && "1".equals(drugVo.getDrugFlag())){
								recipeCost = recipeCost + (drugVo.getNobackNum()*drugVo.getUnitPrice());
								recipeQty = recipeQty + drugVo.getNobackNum();
							}
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
					SpeNalVo speNalVoList = medicineListDAO.querySpeNalVoBy(feedetails.get(0).getExecDpcd(),ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode(),itemType);
					if(speNalVoList.gettCode()!=null){
						StoTerminal stoTerminalNo = medicineListDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
						stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
						if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
							stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
						}else{
							stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
						}
					}else{
						itemType = 2;//专科
						speNalVoList = medicineListDAO.querySpeNalVoBy(feedetails.get(0).getExecDpcd(),registerInfo.getDeptCode(),itemType);
						if(speNalVoList.gettCode()!=null){
							StoTerminal stoTerminalNo = medicineListDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
							stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
							if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
								stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
							}else{
								stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
							}
						}else{
							itemType = 3;//结算类别
							speNalVoList = medicineListDAO.querySpeNalVoBy(feedetails.get(0).getExecDpcd(),registerInfo.getPaykindCode(),itemType);
							if(speNalVoList.gettCode()!=null){
								StoTerminal stoTerminalNo = medicineListDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
								stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
								if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
									stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
								}else{
									stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
								}
							}else{
								itemType = 5;//挂号级别
								speNalVoList = medicineListDAO.querySpeNalVoBy(feedetails.get(0).getExecDpcd(),registerInfo.getReglevlCode(),itemType);
								if(speNalVoList.gettCode()!=null){
									StoTerminal stoTerminalNo = medicineListDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
									stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
									if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
										stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
									}else{
										stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
									}
								}else{
									itemType = 1;//特殊药品
									for(OutpatientFeedetailNow modlse :feedetails){
										speNalVoList = medicineListDAO.querySpeNalVoBy(modlse.getExecDpcd(),modlse.getItemCode(),itemType);
										if(speNalVoList.gettCode()!=null){
											StoTerminal stoTerminalNo = medicineListDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
											stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
											if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
												stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
											}else{
												stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
											}
											break;
										}
									}
								}
							}
						}
					}
					if(speNalVoList.gettCode()==null){
						StoTerminal stoTerminal = medicineListDAO.queryStoTerminal(feedetails.get(0).getExecDpcd());//配药终端
						StoTerminal stoTerminalNo = medicineListDAO.queryStoTerminalNo(stoTerminal.getId());//发药终端
						if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
							stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
						}else{
							stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
						}
						stoRecipe.setDrugedTerminal(stoTerminal.getId());
					}
					if(!"".equals(wondowType)){
						wondowType = wondowType + ",";
					}
					wondowType = wondowType + stoRecipe.getSendTerminal();
					refundDAO.save(stoRecipe);
				}
			}
			
			//结算信息表
			FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
			invoiceInfo.setId(null);
			invoiceInfo.setInvoiceNo(newInvoiceNo);//发票号
			invoiceInfo.setTransType(1);//交易类型
			invoiceInfo.setCardNo(registerInfo.getMidicalrecordId());//病历号
			invoiceInfo.setRegDate(registerInfo.getRegDate());//挂号日期
			invoiceInfo.setName(registerInfo.getPatientName());//患者姓名
			invoiceInfo.setPaykindCode(null);//结算类别
			invoiceInfo.setPactCode(registerInfo.getPactCode());//合同单位代码
			BusinessContractunit contractunit = refundDAO.queryContractunitById(registerInfo.getPactCode());
			invoiceInfo.setPactName(contractunit.getName());//合同单位名称
			invoiceInfo.setTotCost(price);//总金额
			invoiceInfo.setPubCost(null);//可报效金额
			invoiceInfo.setOwnCost(null);//不可报效金额
			invoiceInfo.setPayCost(price);//自付金额
			invoiceInfo.setBack1(null);//预留1
			invoiceInfo.setBack2(null);//预留2
			invoiceInfo.setBack3(null);//预留3
			invoiceInfo.setRealCost(price);//实付金额
			invoiceInfo.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
			invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
			invoiceInfo.setExamineFlag(0);//团体/个人
			invoiceInfo.setCancelFlag(1);//有效
			invoiceInfo.setCancelInvoice(null);//作废票据号
			invoiceInfo.setCancelCode(null);//作废操作员
			invoiceInfo.setCancelDate(null);//作废时间
			invoiceInfo.setInvoiceSeq(invoiceSeqs);//发票序号
			invoiceInfo.setExtFlag(1);//拓展标识 1 自费
			invoiceInfo.setClinicCode(clinicCode);//门诊号
			invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
			invoiceInfo.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			refundDAO.save(invoiceInfo);
			
			
			//摆药单
			if(medicinelDrugList.size()>0){
				for(MedicinelDrugList modls : medicinelDrugList){
					if("1".equals(modls.getDrugFlag())&&modls.getNobackNum()>0){
						OutpatientFeedetailNow  feedetailLists = medicineListDAO.queryDrugInfoList(modls.getId());
						DrugInfo drugInfo = medicineListDAO.queryDrugInfoById(feedetailLists.getItemCode());
						int applyNumber = Integer.parseInt(applyoutDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
						String yearLast = new SimpleDateFormat("yyMM").format(new Date());
						int value = keyvalueDAO.getVal(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode(),"内部入库申请单号",yearLast);
						String applyBillcode = "0" + yearLast + value;//组成内部入库申请单号
						Double qty = modls.getNobackNum();//获取药品的数量
						DrugApplyoutNow drugApplyout = new DrugApplyoutNow();
						drugApplyout.setApplyNumber(applyNumber);//申请流水号
						drugApplyout.setDeptCode(registerInfo.getDeptCode());//申请科室
						drugApplyout.setDrugDeptCode(modls.getExecDpcd());//发药科室
						drugApplyout.setOpType(1);//操作类型分类
						drugApplyout.setDrugCode(modls.getId());//药品id
						drugApplyout.setTradeName(drugInfo.getName());//药品名
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
						drugApplyout.setApplyNum(modls.getNobackNum());//申请数量
						drugApplyout.setBillclassCode("M1");
						drugApplyout.setPreoutFlag(1);//预扣库存
						drugApplyout.setChargeFlag(1);//已收费
						//drugApplyout.setPatientId(registerInfo.getPatientId().getId());//患者Id
						drugApplyout.setPatientDept(registerInfo.getDeptCode());//患者科室
						if(modls.getDoseOnce() != null){
							drugApplyout.setDoseOnce(modls.getDoseOnce());//每次计量
						}
						drugApplyout.setDoseUnit(feedetailLists.getDoseUnit());//计量单位
						drugApplyout.setUsageCode(feedetailLists.getUsageCode());//用法代码
						drugApplyout.setUseName(feedetailLists.getUseName());//用法名称
						drugApplyout.setDfqFreq(feedetailLists.getFrequencyCode());//频次代码
						drugApplyout.setOrderType("1");//医嘱类别
						drugApplyout.setCombNo(modls.getCombNo());//组合号 
						drugApplyout.setRecipeNo(modls.getRecipeNo());//处方号
						int sequenceNo = 0;
						if(feedetailLists.getSequenceNo() != null){
							sequenceNo = feedetailLists.getSequenceNo();
						}
						drugApplyout.setSequenceNo(sequenceNo);//处方内流水号
						drugApplyout.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						drugApplyout.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
						drugApplyout.setCreateTime(DateUtils.getCurrentTime());
						drugApplyout.setApplyOpername(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						drugApplyout.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
						refundDAO.save(drugApplyout);
					} 
				}
			}
		}
		
		if(medicinelDrugList.size()>0){
			for(MedicinelDrugList modls : medicinelDrugList){
				if("1".equals(modls.getDrugFlag())&&modls.getNobackNum()>0){
					outstoreService.withholdStock(modls.getExecDpcd(),modls.getItemCode(),-modls.getNobackNum(),modls.getExtFlag3());
				}
			}
		}
		
		
		List<InpatientCancelitemNow> cancelitemListApply = refundConfirmDAO.findByApplyIds(drugApplyIds);
		if(cancelitemListApply.size()>0){
			for(InpatientCancelitemNow modls :cancelitemListApply){
				modls.setChargeFlag(1);
				modls.setConfirmDpcd(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				modls.setConfirmDpcdName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());
				modls.setConfirmCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				modls.setConfirmName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
				modls.setConfirmDate(new Date());
				modls.setChargeFlag(1);
				modls.setChargeCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				modls.setChargeName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
				modls.setChargeDate(new Date());
				refundConfirmDAO.update(modls);
			}
		}
		
		
		
		String invoiceType = HisParameters.OUTPATIENTINVOICETYPE;
		SysEmployee employee = medicineListDAO.queryEmployee(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		medicineListDAO.saveInvoiceFinance(employee.getJobNo(),newInvoiceNo,invoiceType);
		
		//添加发票使用记录
		InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
		usageRecord.setId(null);
		usageRecord.setUserId(employee.getId());
		usageRecord.setUserCode(employee.getCode());
		usageRecord.setUserType(2);
		usageRecord.setInvoiceNo(newInvoiceNo);
		usageRecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		usageRecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		usageRecord.setCreateTime(DateUtils.getCurrentTime());
		usageRecord.setUserName(employee.getName());
		usageRecord.setInvoiceUsestate(1);
		medicineListDAO.save(usageRecord);
		map.put("resMsg", "success");
		map.put("resCode", "success");
		map.put("resCodes", newInvoiceNo);
		return map;
	}
	@Override
	public Map<String, String> refundSaveNow(String drugApplyIds, String newInvoiceNo, String oldinvoiceNo, String payType, String applyCost,String medicalRecord) {
		//获取退费有效天数
		HospitalParameter parameter = refundDAO.queryParameter();
		List<OutpatientFeedetailNow> hadSendDrugList = refundDAO.findFeedetailByInvoiceNos(oldinvoiceNo,parameter.getParameterValue(),null);
		Map<String, String> map = new HashMap<String, String>();
		String payKindCode = "01";//初始化值，之后进行赋值
		String payKindName = "自费";//初始化值，之后进行赋值
		try{
		User user2 = (User) SessionUtils.getCurrentUserFromShiroSession();
		String userId = user2.getAccount();
		SysDepartment department = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptId = department.getDeptCode();
		String deptName = department.getDeptName();
		SysEmployee employee = medicineListInInterDAO.queryEmployee(userId);
		//一次发票序号
		String invoiceComb = medicineListInInterDAO.getSeqByName("SEQ_INVOICE_COMB");
		RegistrationNow registration = null;
		FinanceInvoiceInfoNow invoiceInfoApply = refundConfirmDAO.queryByInfo(oldinvoiceNo);
		List<MedicinelDrugList> medicinelDrugList = new ArrayList<MedicinelDrugList>();
		List<InpatientCancelitemNow> cancelitemList = refundConfirmDAO.findByApplyIds(drugApplyIds);
		
//		if(cancelitemList.size()>0){
//			for(InpatientCancelitemNow modls :cancelitemList){
//				OutpatientFeedetailNow feedetailApply = refundConfirmDAO.queryByRecipeNo(modls.getRecipeNo(),modls.getSequenceNo());
//				MedicinelDrugList medicinel = new MedicinelDrugList();
//				medicinel.setItemName(modls.getItemName());/**药品名称**/
//				medicinel.setCombNo(feedetailApply.getCombNo());/**组**/
//				medicinel.setSpecs(feedetailApply.getSpecs());/**规格**/
//				medicinel.setQty(feedetailApply.getQty().doubleValue());/**数量**/
//				medicinel.setPriceUnit(feedetailApply.getPriceUnit());/**单位**/
//				medicinel.setNobackNum(feedetailApply.getQty()-modls.getQuantity());/**可退数量**/
//				medicinel.setTotCost((feedetailApply.getQty()-modls.getQuantity())*feedetailApply.getUnitPrice());/**金额**/
//				medicinel.setDoseOnce(feedetailApply.getDoseOnce());/**每次用量**/
//				medicinel.setFeeCpcd(feedetailApply.getFeeCpcd());/**收费人**/
//				medicinel.setId(feedetailApply.getId());/**ID**/
//				medicinel.setRecipeNo(modls.getRecipeNo());/**处方号**/
//				medicinel.setDrugFlag(feedetailApply.getDrugFlag()+"");/**药品标志**/
//				medicinel.setExecDpcd(feedetailApply.getExecDpcd());/**执行科室**/
//				medicinel.setUnitPrice(feedetailApply.getUnitPrice());/**单价**/
//				medicinel.setItemCode(feedetailApply.getItemCode());/**药品ID**/
//				medicinel.setExtFlag3(feedetailApply.getExtFlag3());/**包装**/
//				medicinel.setSequenceNo(modls.getSequenceNo());/**处方内流水号**/
//				medicinel.setNobackNums(feedetailApply.getQty().doubleValue());/**虚拟可退数量**/
//				medicinelDrugList.add(medicinel);
//			}
//		}
		//初始化MAP
		Map<String, Object> outMap = new HashMap<String, Object>();
		//初始化发票序号
		String invoiceSeq = "";
		//根据发票序号查询所有发票明细表中信息
		List<FinanceInvoicedetailNow> invoicedetailList = refundDAO.findInvoiceTailByInvoiceNos(oldinvoiceNo);
		//根据发票序号查询所有发票结算表中信息
		List<FinanceInvoiceInfoNow> invoiceInfoLists = refundDAO.findInvoiceInfoByInvoiceNos(oldinvoiceNo);
		//支付方式情况表
		List<BusinessPayModeNow> payModeList = refundDAO.findPayModeListByNos(oldinvoiceNo);
		//收费明细表
		List<OutpatientFeedetailNow> feedetailList = refundDAO.findFeedetailListByNos(oldinvoiceNo);
		//日结和财务审核后是否可以退费
		String  back = parameterInnerDAO.getParameterByCode("outpatientChargeBack");//日结后是否可以退费标识    0否1是
		String  check = parameterInnerDAO.getParameterByCode("outpatientCheck");//财务审核后是否可以退费标识        0否1是
		if("0".equals(back)){//日结后不可以退费
			if(invoicedetailList.get(0).getBalanceFlag()==1){
				map.put("resMsg", "error");
				map.put("resCode", "已日结，不可进行退费！");
				return map;
			}else if("0".equals(check)&&invoicedetailList.get(0).getBalanceFlag()==1){//财务审核后不可以退费
				String balanceNo = invoicedetailList.get(0).getBalanceNo();
				OutpatientDaybalance daybalance = refundDAO.getDayBalancebyNO(balanceNo);
				if(daybalance.getCheckFlag()!=null&&daybalance.getCheckFlag()==2){
					map.put("resMsg", "error");
					map.put("resCode", "财务已审核，不可进行退费！");
					return map;
				}else{
					double monry = StringUtils.isNotBlank(applyCost)?Double.valueOf(applyCost):0D;
					daybalance.setTotCost(daybalance.getTotCost()-monry);
					refundDAO.update(daybalance);
				}
			}
		}
		if("2".equals(payType)){
			//判断患者是否存在院内账户
			OutpatientAccount account = refundDAO.getAccountByMedicalrecord(medicalRecord);
			if(StringUtils.isBlank(account.getId())){
				map.put("resMsg", "error");
				map.put("resCode", "此患者无账户信息,请联系管理员!");
				return map;
			}else if(account.getAccountState()==0){
				map.put("resMsg", "error");
				map.put("resCode", "账户已停用,请联系管理员!");
				return map;
			}else if(account.getAccountState()==2){
				map.put("resMsg", "error");
				map.put("resCode", "账户已注销!");
				return map;
			}else{
				account.setAccountBalance(account.getAccountBalance()+Double.parseDouble(applyCost));
				account.setUpdateTime(DateUtils.getCurrentTime());
				account.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				refundDAO.save(account);
				
				//添加门诊账户明细表
				OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
				accountrecord.setId(null);//ID
				accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
				accountrecord.setAccountId(account.getId());//门诊账户编号
				accountrecord.setOpertype(5);//操作类型
				accountrecord.setMoney(Double.parseDouble(applyCost));//交易金额
				accountrecord.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//相关科室
				accountrecord.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员 
				accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
				accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
				accountrecord.setValid(0);//是否有效
				accountrecord.setInvoiceType("02");//发票类型
				accountrecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				accountrecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				accountrecord.setCreateTime(DateUtils.getCurrentTime());
				accountrecord.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				accountrecord.setUpdateTime(DateUtils.getCurrentTime());
				refundDAO.save(accountrecord);
			}
		}
		//初始化药品ID
		String drugIds = "";
		if(feedetailList.size()>0){
			for (OutpatientFeedetailNow fee : feedetailList) {
				if("1".equals(fee.getDrugFlag())){//TODO 项目编码和医嘱流水号能不能保证唯一？待确定
					List<DrugApplyoutNow> list = refundDAO.findApplyoutByItemCode(fee.getItemCode(),fee.getMoOrder());
					for (DrugApplyoutNow app : list) {
						app.setApplyState(3);
						app.setValidState(0);
						app.setUpdateTime(DateUtils.getCurrentTime());
						app.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
						refundDAO.update(app);
					}
					//预扣库存处理
					businessStockInfoInInterService.withholdStock(fee.getExecDpcd(),fee.getItemCode(),-fee.getNobackNum(),fee.getExtFlag3().toString());
				}
			}
		}
		
		//初始化处方号
		String recipeNo = "";
		if(feedetailList.size()>0){
			for(OutpatientFeedetailNow fee : feedetailList){
				if(!"".equals(recipeNo)){
					recipeNo = recipeNo + "','";
				}
				recipeNo = recipeNo + fee.getRecipeNo();
			}
		}
		
		//查询出库申请单记录
//		List<DrugApplyout> applyout = refundDAO.findApplyoutByItemCode(drugIds);
//		if(applyout.size()>0){
//			for(DrugApplyout app : applyout){
//				app.setApplyState(3);
//				app.setValidState(0);
//				refundDAO.update(app);
//			}
//		}
		//处方调剂头表
		List<StoRecipeNow> stoRecipeList = refundDAO.findStoRecipeByRecipNo(recipeNo);
		
		//发票内流水号
		if(invoicedetailList.size()>0){
			for(FinanceInvoicedetailNow tail : invoicedetailList){
				FinanceInvoicedetailNow n_tail=new FinanceInvoicedetailNow();
				BeanUtils.copyProperties(tail, n_tail);
				//修改原来发票明细信息
				tail.setCancelFlag(0);
				tail.setUpdateTime(DateUtils.getCurrentTime());
				tail.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				refundDAO.update(tail);
				//添加冲账记录
				n_tail.setId(null);//ID
				n_tail.setTransType(2);//正反类型1正2反
				n_tail.setPayCost(n_tail.getPayCost());
				n_tail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				n_tail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				n_tail.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(n_tail);
				
			}
		}
		
		
		if(invoiceInfoLists.size()>0){
			for(FinanceInvoiceInfoNow info : invoiceInfoLists){
				FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
				BeanUtils.copyProperties(info,invoiceInfo);
				//修改原发票结算表
				info.setCancelFlag(0);
				info.setUpdateTime(DateUtils.getCurrentTime());
				info.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				refundDAO.update(info);
				//添加冲账记录
				invoiceInfo.setId(null);
				invoiceInfo.setTransType(2);//交易类型
				invoiceInfo.setTotCost(invoiceInfo.getTotCost());
				invoiceInfo.setPayCost(invoiceInfo.getPayCost());//自付金额
				invoiceInfo.setRealCost(invoiceInfo.getRealCost());
				invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(invoiceInfo);
			}
		}
		
		if(payModeList.size()>0){
			for(BusinessPayModeNow pay :payModeList){
				BusinessPayModeNow payMode = new BusinessPayModeNow();
				BeanUtils.copyProperties(pay,payMode);
				//修改原数据
				pay.setCancelFlag(0);
				pay.setUpdateTime(DateUtils.getCurrentTime());
				pay.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				refundDAO.save(pay);
				//冲账记录
				payMode.setId(null);//ID
				payMode.setTransType(2);//交易类型
				payMode.setTotCost(payMode.getTotCost());//应付金额
				payMode.setRealCost(payMode.getRealCost());//实付金额
				payMode.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				payMode.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				payMode.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(payMode);
			}
		}
//		if(feedetailList.size()>0){
//			for(OutpatientFeedetailNow fee : feedetailList){
//				OutpatientFeedetailNow feedetail = new OutpatientFeedetailNow();
//				BeanUtils.copyProperties(fee,feedetail);
//				//修改原纪录
//				fee.setCancelFlag(0);
//				fee.setUpdateTime(DateUtils.getCurrentTime());
//				fee.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
//				refundDAO.update(fee);
//				//插入冲账记录
//				feedetail.setId(null);
//				feedetail.setTransType(2);
//				feedetail.setTotCost(feedetail.getTotCost());
//				feedetail.setQty(feedetail.getQty());
//				feedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
//				feedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
//				feedetail.setCreateTime(DateUtils.getCurrentTime());
//				refundDAO.save(feedetail);
//			}
//		}
		if(stoRecipeList.size()>0){
			for(StoRecipeNow sto : stoRecipeList){
				StoRecipeNow stoRecipe = new StoRecipeNow();
				BeanUtils.copyProperties(sto,stoRecipe);
				//修改原数据
				sto.setModifyFlag(1);
				sto.setValidState(0);
				sto.setUpdateTime(DateUtils.getCurrentTime());
				sto.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				refundDAO.update(sto);
				//冲账记录
				stoRecipe.setId(null);
				stoRecipe.setClassMeaningCode("3");
				stoRecipe.setTransType(2);
				stoRecipe.setModifyFlag(1);
				stoRecipe.setRecipeCost(stoRecipe.getRecipeCost());
				stoRecipe.setRecipeQty(stoRecipe.getRecipeQty());
				stoRecipe.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				stoRecipe.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				stoRecipe.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(stoRecipe);
			}
		}
		//未退药的集合 包含已发药药品
		List<OutpatientFeedetailNow> feedetailListBT = new ArrayList<OutpatientFeedetailNow>();
		
		String clinicCode = null;//门诊号
		List<FeeCodeVo> feeCodeVoListBT = new ArrayList<FeeCodeVo>();
		String invoiceSeqs = medicineListDAO.getSeqByName("SEQ_INVOICE_SEQ");//查询序列得出序号
		for (OutpatientFeedetailNow f : feedetailList) {
			boolean flag = true;//如果是待退费项目 则为flse 否则为true
			for(InpatientCancelitemNow modls :cancelitemList){
				OutpatientFeedetailNow feedetailApply = refundConfirmDAO.queryByRecipeNo(modls.getRecipeNo(),modls.getSequenceNo());
				if(f.getId().equals(feedetailApply.getId())){//当所有处方明细和通过退费申请信息查出来的处方明细一致的话，即为待退项目
					if(feedetailApply.getConfirmNum()>0){//医技已确认项目生成新纪录
						OutpatientFeedetailNow cfmfee = new OutpatientFeedetailNow();
						BeanUtils.copyProperties(feedetailApply,cfmfee);
						cfmfee.setId(null);
						cfmfee.setCancelFlag(1);
						cfmfee.setQty(feedetailApply.getConfirmNum());
						cfmfee.setNobackNum(0D);
						cfmfee.setConfirmFlag(1);
						cfmfee.setConfirmNum(feedetailApply.getConfirmNum());
						cfmfee.setTotCost(feedetailApply.getConfirmNum()*feedetailApply.getUnitPrice());
						cfmfee.setPayFlag(1);
						refundDAO.save(cfmfee);
					}
					flag = false;
					if(f.getQty()-modls.getQuantity()-f.getConfirmNum()>0){//当开立数量大于退药数量时 即存在不退项目
						OutpatientFeedetailNow feedetailNew = new OutpatientFeedetailNow();
						BeanUtils.copyProperties(f,feedetailNew);
						feedetailNew.setId(null);
						feedetailNew.setQty(f.getQty()-modls.getQuantity()-f.getConfirmNum());
						feedetailNew.setNobackNum(f.getNobackNum()-modls.getQuantity()-f.getConfirmNum());
						feedetailNew.setConfirmNum(0D);
						feedetailNew.setTotCost((f.getQty()-modls.getQuantity()-f.getConfirmNum())*f.getUnitPrice());
						feedetailNew.setCancelFlag(1);
						feedetailNew.setFlay(f.getId());
						feedetailNew.setPayFlag(1);
						feedetailNew.setFeeCpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//收费人
						feedetailNew.setFeeDate(new Date());//收费时间
//						String moOrder = medicineListDAO.getSeqByName("SEQ_ADVICE_STREAMNO"); //医嘱流水号
//						feedetailNew.setMoOrder(moOrder);
//						String recipeSeq = medicineListDAO.getSeqByName("SEQ_ADVICE_CHARGESEQ"); //收费序列
//						feedetailNew.setRecipeSeq(recipeSeq);
//						String groupNo = medicineListDAO.getSeqByName("SEQ_ADVICE_GROUPNO"); //或得组合号
//						feedetailNew.setCombNo(groupNo);
						feedetailNew.setSampleId(medicinelistDAO.getSeqByName("SEQ_T_TEC_APPLYNUMBER"));//生成新的申请流水号（医技使用）
						feedetailListBT.add(feedetailNew);
					}
				}
			}
			if(flag){//不退项目
				OutpatientFeedetailNow feedetailNew = new OutpatientFeedetailNow();
				BeanUtils.copyProperties(f,feedetailNew);
				//判断是否发药，flay为null表示发药，非空表示未发药
				for (OutpatientFeedetailNow has : hadSendDrugList) {
					if(StringUtils.isNotBlank(has.getFlay())&&f.getId().equals(has.getId())){
						feedetailNew.setFlay(has.getFlay());//在处方调剂头表和出库申请时使用，为空处方调剂头表设为已发药状态，出库申请为已出库状态
					}
				}
				feedetailNew.setId(null);
				feedetailNew.setCancelFlag(1);
				feedetailNew.setFeeCpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//收费人
				feedetailNew.setFeeDate(new Date());//收费时间
				feedetailNew.setPayFlag(1);
				feedetailListBT.add(feedetailNew);
			}
			
		}
		if(feedetailList.size()>0){
			for(OutpatientFeedetailNow fee : feedetailList){
				OutpatientFeedetailNow feedetail = new OutpatientFeedetailNow();
				BeanUtils.copyProperties(fee,feedetail);
				//修改原纪录
				fee.setCancelFlag(0);
				fee.setUpdateTime(DateUtils.getCurrentTime());
				fee.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				refundDAO.update(fee);
				//插入冲账记录
				feedetail.setId(null);
				feedetail.setTransType(2);
				feedetail.setTotCost(feedetail.getTotCost());
				feedetail.setQty(feedetail.getQty());
				feedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				feedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				feedetail.setCreateTime(DateUtils.getCurrentTime());
				refundDAO.save(feedetail);
			}
		}
		//根据门诊号进行分类		
		Map<String ,List<OutpatientFeedetailNow>>  clicnicCodeInvoiceTypeMap = new HashMap<String ,List<OutpatientFeedetailNow>>();
		//发票序号map key为cliniccode value为发票序号
		Map<String ,String>  invoiceSeqMap = new HashMap<String ,String>();
		//门诊号-发票号
		Map<String ,String>  invoiceNoMap = new HashMap<String ,String>();
		//蜂聚处方号进行二次分类
		Map<String ,List<OutpatientFeedetailNow>>  recipeNoMap = new HashMap<String ,List<OutpatientFeedetailNow>>();
		//Map<处方号，发药窗口>
		Map<String,String> recipeWinMap = new HashMap<String, String>();
		String invoiceType = parameterInnerDAO.getParameterByCode("invoiceFno");//获取分发票的类型
		for (OutpatientFeedetailNow fee : feedetailListBT) {
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
					invoiceSeq = medicineListDAO.getSeqByName("SEQ_INVOICE_SEQ");//查询序列得出序号
				}
				invoiceSeqMap.put(fee.getClinicCode(), invoiceSeq);
			}
			//根据处方号进行分组
			if(recipeNoMap.containsKey(fee.getRecipeNo())){
				List<OutpatientFeedetailNow> list = recipeNoMap.get(fee.getRecipeNo());
				list.add(fee);
				recipeNoMap.put(fee.getRecipeNo(), list);
			}else{
				List<OutpatientFeedetailNow> list = new ArrayList<OutpatientFeedetailNow>();
				list.add(fee);
				recipeNoMap.put(fee.getRecipeNo(), list);
			}
			//预扣库存
			if("1".equals(fee.getDrugFlag())){
				OutpatientFeedetailNow outFeedetail = medicineListInInterDAO.queryOutFeedetail(fee.getId());
				businessStockInfoInInterService.withholdStock(fee.getExecDpcd(),fee.getItemCode(),fee.getNobackNum(),fee.getExtFlag3().toString());
			}
		}
		if(!(clicnicCodeInvoiceTypeMap.size()>0)){
			map.put("resMsg", "success");
			map.put("resCode", "退费成功！");
			map.put("invoiceNos", "");
			return map;
		}
		List<StoRecipeNow> stoRecplist = new ArrayList<StoRecipeNow>();
		//摆药单
		StoRecipeNow stoRecipe = null;
		if(recipeNoMap!=null&&recipeNoMap.size()>0){
			for(Map.Entry<String ,List<OutpatientFeedetailNow>> recipNoMap : recipeNoMap.entrySet()){
				String recipeN = recipNoMap.getKey();
				List<OutpatientFeedetailNow> feedetail = recipNoMap.getValue();
				registration = refundDAO.queryRegisterInfo(feedetail.get(0).getClinicCode());
//				String wondowT = this.stoRecipe(recipeNoList, recipeN, clicnicCode, invoiceNo, registration, employee, department, user2);
				String execDeptCode = "";
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
					stoRecipe = new StoRecipeNow();
					stoRecipe.setId(null);
					stoRecipe.setDrugDeptCode(execDeptCode);//执行科室/发放药房
					stoRecipe.setRecipeNo(recipeN);//处方号
					stoRecipe.setClassMeaningCode("1");//出库申请分类
					stoRecipe.setTransType(1);//交易类型
					stoRecipe.setRegDate(registration.getRegDate());//挂号日期
					/****
					 * 当flay为空时表示该项目已经发药，而发药时根据处方来的（即同一个处方下的所有药品都会发药），
					 * 所以当同一个处方号的药品中有一个发药了，其他的也发药了
					 */
					if(StringUtils.isBlank(feedetail.get(0).getFlay())){
						stoRecipe.setRecipeState(3);//处方状态 已发药
					}else{
						stoRecipe.setRecipeState(0);//处方状态  未发药
					}
					stoRecipe.setClinicCode(registration.getClinicCode());//门诊号
					stoRecipe.setCardNo(registration.getMidicalrecordId());//病例号
					stoRecipe.setPatientName(registration.getPatientName());//患者姓名
					stoRecipe.setSexCode(registration.getPatientSex());//性别
					stoRecipe.setBirthday(registration.getPatientBirthday());//出生日期
					stoRecipe.setPaykindCode("");//结算类别
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
					//按照特殊收费窗口查找配药台
					Integer itemType = 4;//特殊收费窗口
					cn.honry.inner.outpatient.medicineList.vo.SpeNalVo speNalVoList = null ;
					for (OutpatientFeedetailNow out : feedetail) {
						if("1".equals(out.getDrugFlag())){
							if (StringUtils.isNotBlank(out.getExecDpcd())) {
								speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode(),itemType);
								if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
									break;
								}
							}
						}
					}
					if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
						StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
						stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
						if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
							StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
							stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
							stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
							stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
							stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
						}else{
							stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
							stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
							stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
							stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
						}
						stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
						medicineListInInterDAO.update(stoTerminalNo);
					}else{
						itemType = 2;//专科
						for (OutpatientFeedetailNow out : feedetail) {
							if("1".equals(out.getDrugFlag())){
								if (StringUtils.isNotBlank(out.getExecDpcd())) {
									speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),registration.getDeptCode(),itemType);
									if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
										break;
									}
								}
							}
						}
						if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
							StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
							stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
							if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
								StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
								stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
								stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
								stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
								stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
							}else{
								stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
								stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
								stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
								stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
							}
							stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
							medicineListInInterDAO.update(stoTerminalNo);
						}else{
							itemType = 3;//结算类别
							for (OutpatientFeedetailNow out : feedetail) {
								if("1".equals(out.getDrugFlag())){
									if (StringUtils.isNotBlank(out.getExecDpcd())) {
										speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),registration.getPaykindCode(),itemType);
										if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
											break;
										}
									}
								}
							}
							if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
								StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
								stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
								if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
									StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
									stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
									stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
									stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
									stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
								}else{
									stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
									stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
									stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
									stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
								}
								stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
								medicineListInInterDAO.update(stoTerminalNo);
							}else{
								itemType = 5;//挂号级别
								for (OutpatientFeedetailNow out : feedetail) {
									if("1".equals(out.getDrugFlag())){
										if (StringUtils.isNotBlank(out.getExecDpcd())) {
											speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),registration.getReglevlCode(),itemType);
											if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
												break;
											}
										}
									}
								}
								if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
									StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
									stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
									if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
										StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
										stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
										stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
										stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
										stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
									}else{
										stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
										stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
										stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
										stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
									}
									stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
									medicineListInInterDAO.update(stoTerminalNo);
								}else{
									itemType = 1;//特殊药品
									for (OutpatientFeedetailNow out : feedetail) {
										if("1".equals(out.getDrugFlag())){
											if (StringUtils.isNotBlank(out.getExecDpcd())) {
												speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),out.getItemCode(),itemType);
												if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
													break;
												}
											}
										}
									}
									if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
										StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
										stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
										if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
											StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
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
										stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
										medicineListInInterDAO.update(stoTerminalNo);
										break;
									}
								}
							}
						}
					}
					if(StringUtils.isNotBlank(speNalVoList.gettCode())){
						StoTerminal stoTerminal;
						StoTerminal stoTerminalNo;
						//获取调价方式
						Map<String, String> extendMap = sendWicketInInterDAO.getBusinessExtend(execDeptCode);
						String tjfs = extendMap.get("TJFS");
						//获取未配药的调剂信息
						StoTerminal terminal = medicineListInInterDAO.checkIshadUnsend(feedetail.get(0).getClinicCode(), execDeptCode);
						//判断是否存在未配药的调剂信息
						if(terminal!=null&&StringUtils.isNotBlank(terminal.getCode())){
							stoTerminalNo = medicineListInInterDAO.getStoTerminal(terminal.getCode());//发药终端
							stoTerminal = medicineListInInterDAO.queryStoTerminal(execDeptCode,tjfs);//配药终端
						}else{
							stoTerminal = medicineListInInterDAO.queryStoTerminal(execDeptCode,tjfs);//配药终端
							stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(stoTerminal.getId());//发药终端
						}
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
						stoRecipe.setDrugedTerminal(stoTerminal.getCode());
						stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()==null?0:stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
						if(stoTerminalNo.getCode()!=null){
							stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
							medicineListInInterDAO.update(stoTerminalNo);
						}
					}
					stoRecplist.add(stoRecipe);
					medicineListInInterDAO.save(stoRecipe);
				}
				
				
				if(stoRecipe!=null&&StringUtils.isNotBlank(stoRecipe.getSendTerminal())){
					recipeWinMap.put(recipeN,stoRecipe.getSendTerminal());
				}
			}
		}
		//获取多张发票号
		String newInvoiceNos = "";
		Map<String, String> queryFinanceInvoiceNoByNum = new HashMap<String,String>();
		if(clicnicCodeInvoiceTypeMap.size()>0){
			queryFinanceInvoiceNoByNum = medicineListInInterDAO.queryFinanceInvoiceNoByNum(userId,HisParameters.OUTPATIENTINVOICETYPE, clicnicCodeInvoiceTypeMap.size());
			if("error".equals(queryFinanceInvoiceNoByNum.get("resMsg"))){
				throw new RuntimeException("INVOICE IS NOT ENOUGTH");
			}else{
				String[] invoiceNo = queryFinanceInvoiceNoByNum.get("resCode").split(",");
				int i = 0;
				for(Map.Entry<String ,List<OutpatientFeedetailNow>> invosMap : clicnicCodeInvoiceTypeMap.entrySet()){
					String clicnicCodeInvoiceType = invosMap.getKey();
					invoiceNoMap.put(clicnicCodeInvoiceType, invoiceNo[i]);
					if(newInvoiceNos!=""){
						newInvoiceNos+="','";
					}
					newInvoiceNos = newInvoiceNos + invoiceNo[i];
					i+=1;
				}
			}
		}
		if(clicnicCodeInvoiceTypeMap!=null&&clicnicCodeInvoiceTypeMap.size()>0){
			for(Map.Entry<String ,List<OutpatientFeedetailNow>> cmap : clicnicCodeInvoiceTypeMap.entrySet()){
				//根据统计大类进行二次分组 
				Map<String ,List<OutpatientFeedetailNow>>  feeStatCodeMap = new HashMap<String ,List<OutpatientFeedetailNow>>();
				String oldInvoiceNo = cmap.getValue().get(0).getInvoiceNo();
				String[] sKey = cmap.getKey().split("_");
				String feeStatCode = sKey[1];
				String clicnicCode = sKey[0];
				//获取发票号
				String invoiceNo = invoiceNoMap.get(cmap.getKey());
				registration = refundDAO.queryRegisterInfo(clicnicCode);
				int a = 0;
				for(OutpatientFeedetailNow fee : cmap.getValue()){
					a+=1;
//					//门诊收费处理
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
					for (StoRecipeNow sto : stoRecplist) {
						if(sto.getRecipeNo().equals(fee.getRecipeNo())){
							sto.setInvoiceNo(invoiceNo);
							medicineListInInterDAO.update(sto);
						}
					}
				}
				
				//修改发票和添加发票使用记录
				this.updateAndSaveInvoice(employee, invoiceNo, HisParameters.OUTPATIENTINVOICETYPE, department,queryFinanceInvoiceNoByNum.get(invoiceNo));
				//保存支付情况
				Double cost = this.getCostCurrentList(cmap.getValue());
				this.saveBusinessPay(cost,user2, department, invoiceNo, invoiceSeqMap.get(clicnicCode), invoiceComb);
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
					if(wondowType.indexOf(win)==-1){
						if(StringUtils.isNotBlank(wondowType)){
							wondowType += ",";
						}
						wondowType += win;
					}
				}
				//结算信息   
				this.businessInvoiceInfo(payKindCode,payKindName,oldInvoiceNo,cost, clicnicCode, invoiceNo, invoiceSeqMap.get(clicnicCode), registration, wondowType, user2, department, userId);
				//出库申请单
				this.drugApplyout(cmap.getValue(), registration, department,user2);
			}
		}
		//执行医技预约和医技确认
		for(OutpatientFeedetailNow f : feedetailListBT){
			//1.获取药品或非药品
			DrugOrUNDrugVo vo = medicineListInInterDAO.getDrugOrUNDrugVo(f.getItemCode(),f.getDrugFlag());
			//2.判断是否需要医技预约
				if(vo.getIspreorder()==null||vo.getIspreorder()==0){//不需要预约
					if(vo.getIssubmit()!=null&&vo.getIssubmit()==1){//需要确认
						//3.医技终端确认表
						TecTerminalApply tec = new TecTerminalApply();
						tec.setClinicNo(f.getClinicCode());//病历号
						tec.setCardNo(f.getCardNo());//就诊卡号
						tec.setCreateDept(department.getDeptCode());
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
					if(StringUtils.isNotBlank(f.getSampleId())){
						apply.setBookId(f.getSampleId());//预约单号
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
					apply.setItemCode(f.getItemCode());
					apply.setItemName(f.getItemName());
					apply.setMoOrder(f.getMoOrder());
					apply.setName(registration.getPatientName());
					apply.setQty(f.getQty().intValue());
					apply.setRectpeDeptCode(f.getDoctCode());//开单科室code
					apply.setRectpeNo(f.getRecipeNo());
					apply.setSequenceNo(f.getSequenceNo());
					apply.setStatus(0);//0 预约 1 生效 2 审核 3 作废
					apply.setTransType(f.getTransType());//交易类型 0 正交易 ，1 负交易
					apply.setCreateUser(userId);
					apply.setCreateTime(new Date());
					apply.setCreateDept(deptId);
					apply.setUpdateTime(new Date());
					apply.setUpdateUser(userId);
					tecApplyInInInterDAO.save(apply);
				}
		}
		for(InpatientCancelitemNow cancel:cancelitemList){
			//更改退费信息记录
			cancel.setChargeFlag(1);
			cancel.setConfirmFlag(1);
			cancel.setConfirmCode(user2.getAccount());
			cancel.setConfirmDate(new Date());
			cancel.setConfirmDpcd(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());
			cancel.setConfirmDpcdName(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptName());
			cancel.setConfirmName(user2.getName());
			refundConfirmDAO.update(cancel);
		}
		
		map.put("resMsg", "success");
		map.put("resCode", "success");
		map.put("invoiceNos", newInvoiceNos);
		return map;
	}catch(Exception e){
		throw new RuntimeException(e);
	}
}
/**
 * @Description 药品退费记录
 * @author  marongbin
 * @createDate： 2016年12月16日 上午11:22:12 
 * @modifier 
 * @modifyDate：
 * @param modls
 * @param invoiceNo 旧发票号
 * @param clinicCode 门诊号
 * @param registration 挂号实体
 * @param billCode: 申请单据号  void
 * @modifyRmk：  
 * @version 1.0
 */
public void drugCancelItemEntity(OutpatientFeedetailNow fee,String invoiceNo,String clinicCode,RegistrationNow registration,String billCode){
	InpatientCancelitemNow cancelitem = new InpatientCancelitemNow();
	cancelitem.setId(null);
	cancelitem.setBillCode(billCode); //申请单据号
	cancelitem.setInpatientNo(clinicCode); //门诊号
	cancelitem.setApplyFlag(1);//门诊
	cancelitem.setName(registration.getPatientName());//患者姓名
	cancelitem.setDeptCode(registration.getDeptCode());//患者所在科室
	cancelitem.setDeptName(registration.getDeptName());//患者所在科室名称
	cancelitem.setDrugFlag(1);//药品标志,1药品/2非药
	cancelitem.setItemCode(fee.getItemCode());//项目编码
	cancelitem.setItemName(fee.getItemName());//项目名称
	cancelitem.setSpecs(fee.getSpecs());//规格
	cancelitem.setSalePrice(fee.getUnitPrice());//零售价
	cancelitem.setQuantity(0.00);//申请退药数量（乘以付数后的总数量） 未做退药处理
	cancelitem.setDays(1);//付数
	cancelitem.setPriceUnit(fee.getPriceUnit()+"");//计价单位
	cancelitem.setExecDpcd(fee.getExecDpcd());//执行科室
	SysDepartment sys= deptInInterDAO.getDeptCode(fee.getExecDpcd());
	cancelitem.setExecDpcdName(sys.getDeptName());//执行科室名称
	cancelitem.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员编码
	cancelitem.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//操作员名称
	cancelitem.setOperDate(DateUtils.getCurrentTime());//操作时间
	cancelitem.setOperDpcd(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());//操作员所在科室
	cancelitem.setOperDpcdName(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptName());//操作员所在科室名称
	cancelitem.setRecipeNo(fee.getRecipeNo());//对应收费明细处方号
	cancelitem.setSequenceNo(fee.getSequenceNo());//对应处方内流水号
	cancelitem.setBillNo(invoiceNo);//确认单号
	cancelitem.setConfirmFlag(1); //退药确认标识 0未确认/1确认
	cancelitem.setConfirmDpcd(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//确认科室代码
	cancelitem.setConfirmDpcdName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//确认科室名称
	cancelitem.setConfirmCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//确认人编码
	cancelitem.setConfirmName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//确认人名称
	cancelitem.setConfirmDate(DateUtils.getCurrentTime());//确认时间
	cancelitem.setChargeFlag(1);//退费标识 0未退费/1退费/2作废
	cancelitem.setChargeCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//退费人
	cancelitem.setChargeName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
	cancelitem.setChargeDate(DateUtils.getCurrentTime());//退费时间
	cancelitem.setExtFlag(fee.getExtFlag3());//1 包装 单位 0, 最小单位
	cancelitem.setQty(fee.getNobackNum());//数量[22]
	cancelitem.setCardNo(registration.getMidicalrecordId());//病历卡号
	cancelitem.setReturnReason("门诊退费");
	cancelitem.setCreateTime(DateUtils.getCurrentTime());
	cancelitem.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	cancelitem.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	cancelitem.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
	cancelitem.setUpdateTime(DateUtils.getCurrentTime());
	cancelitem.setInvoCode(fee.getInvoCode());
	refundDAO.save(cancelitem);
}
/**
 * @Description 非药品退费记录
 * @author  marongbin
 * @createDate： 2016年12月16日 上午11:22:12 
 * @modifier 
 * @modifyDate：
 * @param modls
 * @param invoiceNo 旧发票号
 * @param clinicCode 门诊号
 * @param registration 挂号实体
 * @param billCode: 申请单据号  void
 * @modifyRmk：  
 * @version 1.0
 */
public void undrugCancelItemEntity(OutpatientFeedetailNow fee,String invoiceNo,String clinicCode,RegistrationNow registration,String billCode){
	InpatientCancelitemNow cancelitem = new InpatientCancelitemNow();
	cancelitem.setId(null);
	cancelitem.setBillCode(billCode); //申请单据号
	cancelitem.setApplyFlag(1);//门诊
	cancelitem.setInpatientNo(clinicCode); //门诊号
	cancelitem.setName(registration.getPatientName());//患者姓名
	cancelitem.setDeptCode(registration.getDeptCode());//患者所在科室
	cancelitem.setDeptName(registration.getDeptName());//患者所在科室名称
	cancelitem.setDrugFlag(2);//药品标志,1药品/2非药
	cancelitem.setItemCode(fee.getItemCode());//项目编码
	cancelitem.setItemName(fee.getItemName());//项目名称
	cancelitem.setSpecs(fee.getSpecs());//规格
	cancelitem.setSalePrice(fee.getUnitPrice());//零售价
	cancelitem.setQuantity(fee.getNobackNum());//申请退药数量（乘以付数后的总数量）
	cancelitem.setPriceUnit(fee.getPriceUnit()+"");//计价单位
	cancelitem.setExecDpcd(fee.getExecDpcd());//执行科室
	SysDepartment sys= deptInInterDAO.getDeptCode(fee.getExecDpcd());
	cancelitem.setExecDpcdName(sys.getDeptName());//执行科室名称
	cancelitem.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员编码
	cancelitem.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//操作员名称
	cancelitem.setOperDate(DateUtils.getCurrentTime());//操作时间
	cancelitem.setOperDpcd(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptCode());//操作员所在科室
	cancelitem.setOperDpcdName(ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession().getDeptName());//操作员所在科室名称
	cancelitem.setRecipeNo(fee.getRecipeNo());//对应收费明细处方号
	cancelitem.setSequenceNo(fee.getSequenceNo());//对应处方内流水号
	cancelitem.setBillNo(invoiceNo);//确认单号
	cancelitem.setConfirmFlag(1); //退药确认标识 0未确认/1确认
	cancelitem.setConfirmDpcd(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//确认科室代码
	cancelitem.setConfirmDpcdName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//确认科室名称
	cancelitem.setConfirmCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//确认人编码
	cancelitem.setConfirmName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());//确认人名称
	cancelitem.setConfirmDate(DateUtils.getCurrentTime());//确认时间
	cancelitem.setChargeFlag(1);//退费标识 0未退费/1退费/2作废
	cancelitem.setChargeCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//退费人
	cancelitem.setChargeName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
	cancelitem.setChargeDate(DateUtils.getCurrentTime());//退费时间
	cancelitem.setExtFlag(fee.getExtFlag3());//1 包装 单位 0, 最小单位
	cancelitem.setQty(fee.getNobackNum());//数量[22]
	cancelitem.setCardNo(registration.getMidicalrecordId());//病历卡号
	cancelitem.setReturnReason("门诊退费");
	cancelitem.setCreateTime(DateUtils.getCurrentTime());
	cancelitem.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	cancelitem.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	cancelitem.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
	cancelitem.setUpdateTime(DateUtils.getCurrentTime());
	refundDAO.save(cancelitem);
}
/**
 * @Description 获取统计大类
 * @author  marongbin
 * @createDate： 2016年12月16日 上午10:04:19 
 * @modifier 
 * @modifyDate：
 * @param fee
 * @return: MinfeeStatCode
 * @modifyRmk：  
 * @version 1.0
 */
public MinfeeStatCode getFeeStatCode(OutpatientFeedetailNow fee){
	//根据最小费用代码ID查询到最小费用代码的encode
	BusinessDictionary drugminimumcost =  codeInInterDAO.getDictionaryByCode("drugMinimumcost",fee.getFeeCode());
	//根据最小费用代码的encode查询统计大类中统计大类的encode和name
	MinfeeStatCode minfeeStatCode = refundDAO.findMinfeeStatCode(drugminimumcost.getEncode());
	return minfeeStatCode;
}
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
	invoicedetail.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
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
public void businessInvoiceInfo(String payKindCode,String payKindName,String oldInvoiceNo,Double cost,String clicnicCode, String invoiceNo,String invoiceSeq,RegistrationNow registerInfo,String wondowType,User user2,SysDepartment department,String userId){
	FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
	invoiceInfo.setId(null);
	invoiceInfo.setInvoiceNo(invoiceNo);//发票号
	invoiceInfo.setTransType(1);//交易类型
	invoiceInfo.setCardNo(registerInfo.getMidicalrecordId());//病历号
	invoiceInfo.setRegDate(registerInfo.getRegDate());//挂号日期
	invoiceInfo.setName(registerInfo.getPatientName());//患者姓名
	invoiceInfo.setPaykindCode(payKindCode);//结算类别
	invoiceInfo.setPaykindName(payKindName);
	invoiceInfo.setPactCode(registerInfo.getPactCode());//合同单位代码
	BusinessContractunit contractunit = refundDAO.queryContractunitById(registerInfo.getPactCode());
	invoiceInfo.setPactName(contractunit.getName());//合同单位名称
	invoiceInfo.setTotCost(cost);//总金额
	invoiceInfo.setPubCost(0.00);//可报效金额
	invoiceInfo.setOwnCost(cost);//不可报效金额
	invoiceInfo.setPayCost(0.00);//自付金额
	invoiceInfo.setBack1(null);//预留1
	invoiceInfo.setBack2(null);//预留2
	invoiceInfo.setBack3(null);//预留3
	invoiceInfo.setRealCost(cost);//实付金额
	invoiceInfo.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
	invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
	invoiceInfo.setExamineFlag(0);//团体/个人
	invoiceInfo.setCancelFlag(1);//有效
	invoiceInfo.setCancelInvoice(oldInvoiceNo);//作废票据号
	invoiceInfo.setCancelCode(null);//作废操作员
	invoiceInfo.setCancelDate(null);//作废时间
	invoiceInfo.setInvoiceSeq(invoiceSeq);//发票序号
	invoiceInfo.setExtFlag(1);//拓展标识 1 自费
	invoiceInfo.setClinicCode(clicnicCode);//门诊号
	invoiceInfo.setDrugWindow(wondowType);//发药窗口
	invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
	invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
	invoiceInfo.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
	refundDAO.save(invoiceInfo);
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
	FNlist.setPayFlag(1);//收费状态
	if(FNlist.getQty() != null){
		FNlist.setNobackNum(FNlist.getQty());//可退数量
	}
	FNlist.setCancelFlag(1);
	FNlist.setInvoSequence(a+"");
	FNlist.setFeeDate(DateUtils.getCurrentTime());
	FNlist.setInvoCode(feeStatCode);
	FNlist.setPayCost(0.00);//自付金额，和医保相关
	FNlist.setPubCost(0.00);//公费金额（可报销金额）
	FNlist.setOwnCost(FNlist.getTotCost());
	//根据统计大类的encode获取发票号
	FNlist.setInvoiceNo(invoiceNo);//发票号
	//根据发票号获取发票序号
	FNlist.setInvoiceSeq(invoiceSeq);//发票序号
//	String moOrder = medicineListDAO.getSeqByName("SEQ_ADVICE_STREAMNO"); //医嘱流水号
//	FNlist.setMoOrder(moOrder);
//	String recipeSeq = medicineListDAO.getSeqByName("SEQ_ADVICE_CHARGESEQ"); //收费序列
//	FNlist.setRecipeSeq(recipeSeq);
//	String groupNo = medicineListDAO.getSeqByName("SEQ_ADVICE_GROUPNO"); //或得组合号
//	FNlist.setCombNo(groupNo);
	FNlist.setId(null);
	medicineListInInterDAO.save(FNlist);//保存门诊处方
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
public void saveBusinessPay(Double cost,User user2,SysDepartment department,String invoiceNo,String invoiceSeq,String invoiceComb){
	BusinessPayModeNow payMode = new BusinessPayModeNow();
	payMode.setInvoiceNo(invoiceNo);//发票号
	payMode.setTransType(1);//交易类型
	payMode.setOperCode(user2.getAccount());//结算人
	payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
	payMode.setModeCode("CA");
	payMode.setTotCost(cost);//应付金额
	payMode.setRealCost(cost);//实付金额
	payMode.setBalanceFlag(0);//是否结算
	payMode.setCancelFlag(1);//状态1正常
	payMode.setInvoiceSeq(invoiceSeq);//发票序号合集
	payMode.setInvoiceComb(invoiceComb);//一次收费序号
	payMode.setCreateUser(user2.getAccount());
	payMode.setCreateDept(department.getDeptCode());
	payMode.setCreateTime(DateUtils.getCurrentTime());
	payMode.setOperName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
	medicineListInInterDAO.save(payMode);
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
public String stoRecipe(List<OutpatientFeedetailNow> feedetail,String recipeNo,String clinicCode,String invoiceNo,RegistrationNow registerInfo,SysEmployee employee,SysDepartment department,User user2){
	String wondowType = "";
	String execDeptCode = "";
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
		stoRecipe.setDrugDeptCode(execDeptCode);//执行科室/发放药房
		stoRecipe.setRecipeNo(recipeNo);//处方号
		stoRecipe.setClassMeaningCode("1");//出库申请分类
		stoRecipe.setTransType(1);//交易类型
		stoRecipe.setRegDate(registerInfo.getRegDate());//挂号日期
		/****
		 * 当flay为空时表示该项目已经发药，而发药时根据处方来的（即同一个处方下的所有药品都会发药），
		 * 所以当同一个处方号的药品中有一个发药了，其他的也发药了
		 */
		if(StringUtils.isBlank(feedetail.get(0).getFlay())){
			stoRecipe.setRecipeState(3);//处方状态 已发药
		}else{
			stoRecipe.setRecipeState(0);//处方状态  未发药
		}
		stoRecipe.setClinicCode(registerInfo.getClinicCode());//门诊号
		stoRecipe.setCardNo(registerInfo.getMidicalrecordId());//病例号
		stoRecipe.setPatientName(registerInfo.getPatientName());//患者姓名
		stoRecipe.setSexCode(registerInfo.getPatientSex());//性别
		stoRecipe.setBirthday(registerInfo.getPatientBirthday());//出生日期
		stoRecipe.setPaykindCode("");//结算类别
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
		//按照特殊收费窗口查找配药台
		Integer itemType = 4;//特殊收费窗口
		cn.honry.inner.outpatient.medicineList.vo.SpeNalVo speNalVoList = null ;
		for (OutpatientFeedetailNow out : feedetail) {
			if("1".equals(out.getDrugFlag())){
				if (StringUtils.isNotBlank(out.getExecDpcd())) {
					speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode(),itemType);
					if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
						break;
					}
				}
			}
		}
		if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
			StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
			stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
			if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
				StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
				stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
				stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
				stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
				stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
			}else{
				stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
				stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
				stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
				stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
			}
			stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
			medicineListInInterDAO.update(stoTerminalNo);
		}else{
			itemType = 2;//专科
			for (OutpatientFeedetailNow out : feedetail) {
				if("1".equals(out.getDrugFlag())){
					if (StringUtils.isNotBlank(out.getExecDpcd())) {
						speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),registerInfo.getDeptCode(),itemType);
						if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
							break;
						}
					}
				}
			}
			if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
				StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
				stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
				if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
					StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
					stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
					stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
					stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
					stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
				}else{
					stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
					stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
					stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
					stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
				}
				stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
				medicineListInInterDAO.update(stoTerminalNo);
			}else{
				itemType = 3;//结算类别
				for (OutpatientFeedetailNow out : feedetail) {
					if("1".equals(out.getDrugFlag())){
						if (StringUtils.isNotBlank(out.getExecDpcd())) {
							speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),registerInfo.getPaykindCode(),itemType);
							if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
								break;
							}
						}
					}
				}
				if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
					StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
					stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
					if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
						StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
						stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
						stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
						stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
						stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
					}else{
						stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
						stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
						stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
						stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
					}
					stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
					medicineListInInterDAO.update(stoTerminalNo);
				}else{
					itemType = 5;//挂号级别
					for (OutpatientFeedetailNow out : feedetail) {
						if("1".equals(out.getDrugFlag())){
							if (StringUtils.isNotBlank(out.getExecDpcd())) {
								speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),registerInfo.getReglevlCode(),itemType);
								if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
									break;
								}
							}
						}
					}
					if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
						StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
						stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
						if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
							StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
							stoRecipe.setSendTerminal(stoTerminalNo.getReplaceCode());
							stoRecipe.setSendTerminalName(stoTerminal.getSendWindowName());//发药窗口name
							stoRecipe.setDrugedTerminalCode(stoTerminal.getCode());//配药终端code
							stoRecipe.setDrugedTerminalName(stoTerminal.getName());//配药终端name
						}else{
							stoRecipe.setSendTerminal(stoTerminalNo.getSendWindow());
							stoRecipe.setDrugedTerminalCode(stoTerminalNo.getCode());//配药终端code
							stoRecipe.setDrugedTerminalName(stoTerminalNo.getName());//配药终端name
							stoRecipe.setSendTerminalName(stoTerminalNo.getSendWindowName());//发药窗口name
						}
						stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
						medicineListInInterDAO.update(stoTerminalNo);
					}else{
						itemType = 1;//特殊药品
						for (OutpatientFeedetailNow out : feedetail) {
							if("1".equals(out.getDrugFlag())){
								if (StringUtils.isNotBlank(out.getExecDpcd())) {
									speNalVoList = medicineListInInterDAO.querySpeNalVoBy(out.getExecDpcd(),out.getItemCode(),itemType);
									if(speNalVoList!=null&&speNalVoList.gettCode()!=null){
										break;
									}
								}
							}
						}
						if(speNalVoList!=null&&StringUtils.isNotBlank(speNalVoList.gettCode())){
							StoTerminal stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(speNalVoList.gettCode());//发药终端
							stoRecipe.setDrugedTerminal(stoTerminalNo.getCode());
							if("1".equals(stoTerminalNo.getCloseFlag())||stoTerminalNo.getCloseFlag()==null){
								StoTerminal stoTerminal = medicineListInInterDAO.getStoTerminal(stoTerminalNo.getReplaceCode());
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
							stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
							medicineListInInterDAO.update(stoTerminalNo);
						}
					}
				}
			}
		}
		if(speNalVoList==null&&StringUtils.isBlank(speNalVoList.gettCode())){
			StoTerminal stoTerminal;
			StoTerminal stoTerminalNo;
			//获取调价方式
			Map<String, String> extendMap = sendWicketInInterDAO.getBusinessExtend(execDeptCode);
			String tjfs = extendMap.get("TJFS");
			//获取未配药的调剂信息
			StoTerminal terminal = medicineListInInterDAO.checkIshadUnsend(feedetail.get(0).getClinicCode(), execDeptCode);
			//判断是否存在未配药的调剂信息
			if(terminal!=null&&StringUtils.isNotBlank(terminal.getCode())){
				stoTerminalNo = medicineListInInterDAO.getStoTerminal(terminal.getCode());//发药终端
				stoTerminal = medicineListInInterDAO.queryStoTerminal(execDeptCode,tjfs);//配药终端
			}else{
				stoTerminal = medicineListInInterDAO.queryStoTerminal(execDeptCode,tjfs);//配药终端
				stoTerminalNo = medicineListInInterDAO.queryStoTerminalNo(stoTerminal.getId());//发药终端
			}
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
			stoRecipe.setDrugedTerminal(stoTerminal.getCode());
			stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()==null?0:stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
			if(stoTerminalNo.getCode()!=null){
				stoTerminalNo.setDrugQty(stoTerminalNo.getDrugQty()+recipeQty1);//更改待发药品数量
				medicineListInInterDAO.update(stoTerminalNo);
			}
		}
		if(!"".equals(wondowType)){
			wondowType = wondowType + ",";
		}
		wondowType = wondowType + stoRecipe.getSendTerminal();
		medicineListInInterDAO.save(stoRecipe);
	}
	return wondowType;
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
//				OutpatientFeedetailNow  feedetailLists = medicineListInInterDAO.queryDrugInfoList(fee.getId());
				DrugInfo drugInfo = medicineListInInterDAO.queryDrugInfoById(fee.getItemCode());
				int applyNumber = Integer.parseInt(medicineListInInterDAO.getSequece("SEQ_DRUG_APPLYOUT").toString());//根据sequence 获取applyNumber
				String yearLast = new SimpleDateFormat("yyMM").format(new Date());
				int value = keyvalueInInterDAO.getVal(department.getDeptCode(),"内部入库申请单号",yearLast);
				String applyBillcode = "0" + yearLast + value;//组成内部入库申请单号
				DrugApplyoutNow drugApplyout = new DrugApplyoutNow();
				drugApplyout.setApplyNumber(applyNumber);//申请流水号
				drugApplyout.setDeptCode(registerInfo.getDeptCode());//申请科室
				drugApplyout.setDrugDeptCode(fee.getExecDpcd());//发药科室
				drugApplyout.setOpType(1);//操作类型分类
				drugApplyout.setGroupCode(null);//批次号,摆药生成出库记录时反写
				drugApplyout.setDrugCode(fee.getItemCode());//药品id
				drugApplyout.setTradeName(drugInfo.getName());//药品名
				drugApplyout.setBatchNo(null);//批号 ,摆药生成出库记录时反写
				drugApplyout.setDrugType(drugInfo.getDrugType());//药品类别
				drugApplyout.setDrugQuality(drugInfo.getDrugNature());//药品性质
				drugApplyout.setSpecs(drugInfo.getSpec());//规格
				drugApplyout.setValidState(1);//有效
				drugApplyout.setRetailPrice(drugInfo.getDrugRetailprice());//零售价
				drugApplyout.setWholesalePrice(drugInfo.getDrugWholesaleprice());//批发价
				drugApplyout.setApplyBillcode(applyBillcode);//申请单号
				drugApplyout.setApplyOpercode(registerInfo.getDoctCode());//申请人
				drugApplyout.setApplyDate(DateUtils.getCurrentTime());//申请日期
				if(StringUtils.isBlank(fee.getFlay())){
					drugApplyout.setApplyState(2);//申请状态
				}else{
					drugApplyout.setApplyState(0);//申请状态
				}
				drugApplyout.setApplyNum(fee.getQty());//申请数量
				drugApplyout.setBillclassCode("1");//查询发药药房对应的发药台对应的摆药单类型
				drugApplyout.setShowFlag(fee.getExtFlag3());
				drugApplyout.setDays(fee.getDays());//申请付数
				drugApplyout.setPreoutFlag(1);//预扣库存，来源于预扣库存的参数，参数设置预扣库存是，此处为是，否则为否。预扣库存的情况下，采取更新仓库主表中的预扣库存数量。
				drugApplyout.setChargeFlag(1);//已收费
				drugApplyout.setPatientId(registerInfo.getClinicCode());//患者门诊号
				drugApplyout.setPatientDept(registerInfo.getDeptCode());//患者科室
				if(fee.getDoseOnce() != null){
					drugApplyout.setDoseOnce(fee.getDoseOnce().doubleValue());//每次剂量
				}
				drugApplyout.setDoseUnit(fee.getDoseUnit());//计量单位
				drugApplyout.setUsageCode(fee.getUsageCode());//用法代码
				drugApplyout.setUseName(fee.getUseName());//用法名称
				drugApplyout.setDfqFreq(fee.getFrequencyCode());//频次代码
				drugApplyout.setOrderType("1");//医嘱类别
				drugApplyout.setMoOrder(fee.getMoOrder());//医嘱流水号
				drugApplyout.setCombNo(fee.getCombNo());//组合号 
				drugApplyout.setRecipeNo(fee.getRecipeNo());//处方号
				int sequenceNo = 0;
				if(fee.getSequenceNo() != null){
					sequenceNo = fee.getSequenceNo();
				}
				drugApplyout.setSequenceNo(sequenceNo);//处方内流水号
				drugApplyout.setCreateUser(department.getDeptCode());
				drugApplyout.setCreateDept(department.getDeptCode());
				drugApplyout.setCreateTime(DateUtils.getCurrentTime());
				drugApplyout.setPrintEmplName(user2.getName());
				drugApplyout.setDeptName(department.getDeptName());
				BusinessDictionary packunit =  innerCodeDao.getDictionaryByCode(HisParameters.DRUGPACKUNIT,drugInfo.getDrugPackagingunit());
				BusinessDictionary miniunit =  innerCodeDao.getDictionaryByCode(HisParameters.DRUGMINUNIT,drugInfo.getUnit());
				String strDoseUnit = "";
				if(StringUtils.isNotBlank(fee.getDoseUnit())){
					BusinessDictionary doseUnit =  innerCodeDao.getDictionaryByCode(HisParameters.DOSEUNIT,fee.getDoseUnit());
					drugApplyout.setDoseUnit(doseUnit.getName());//计量单位
				}
				drugApplyout.setPackUnit(packunit.getName());//包装单位
				drugApplyout.setPackQty(drugInfo.getPackagingnum());//包装数量
				drugApplyout.setMinUnit(miniunit.getName());//最小单位
				drugApplyout.setShowFlag(fee.getExtFlag3());
				//显示单位为对应单位的name
				if(fee.getExtFlag3() == 1){
					drugApplyout.setShowUnit(packunit.getName());
				}
				if(fee.getExtFlag3() == 0){
					drugApplyout.setShowUnit(miniunit.getName());
				}
				drugApplyout.setDfqCexp(fee.getFrequencyName());
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
				businessStockInfoInInterService.withholdStock(outFeedetail.getExecDpcd(),outFeedetail.getItemCode(),outFeedetail.getNobackNum(),outFeedetail.getExtFlag3().toString());
			}
		}
	}
}


}
