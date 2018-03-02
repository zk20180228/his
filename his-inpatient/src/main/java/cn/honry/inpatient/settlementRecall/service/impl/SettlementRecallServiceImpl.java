package cn.honry.inpatient.settlementRecall.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessBedward;
import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.bean.model.InpatientBalancePayNow;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInPrepayNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.PatientAccountrepaydetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.inpatient.inprePay.dao.InprePayDAO;
import cn.honry.inpatient.outBalance.dao.OutBalanceDAO;
import cn.honry.inpatient.outBalance.service.OutBalanceService;
import cn.honry.inpatient.settlementRecall.dao.SettlementRecallDAO;
import cn.honry.inpatient.settlementRecall.service.SettlementRecallService;
import cn.honry.inpatient.settlementRecall.vo.VSettlementRecall;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Service("settlementRecallService")
@Transactional
@SuppressWarnings({"all"})
public class SettlementRecallServiceImpl implements SettlementRecallService{
	@Autowired
	private SettlementRecallDAO settlementRecallDAO;
	@Override
	public List<InpatientInfoNow> queryInfo(String inpatientNo) throws Exception {
		return settlementRecallDAO.queryInfo(inpatientNo);
	}
	@Autowired
	@Qualifier(value="inprePayDAO")
	private InprePayDAO inprePayDAO;
	
	public void setInprePayDAO(InprePayDAO inprePayDAO) {
		this.inprePayDAO = inprePayDAO;
	}
	/**结算service**/
	@Autowired
	@Qualifier(value = "outBalanceService")
	private OutBalanceService outBalanceService;
	@Autowired
	@Qualifier(value = "outBalanceDAO")
	private OutBalanceDAO outBalanceDAO;
	@Override
	public List<InpatientInfoNow> queryInfomedicalrecordId(String medicalrecordId)
			throws Exception {
		return settlementRecallDAO.queryInfomedicalrecordId(medicalrecordId);
	}
	@Override
	public void removeUnused(String id) {
	}
	@Override
	public VSettlementRecall get(String id) {
		return null;
	}
	@Override
	public void saveOrUpdate(VSettlementRecall entity) {
	}
	
	@Override
	public InpatientBalanceHeadNow queryHeadByInvoiceNo(String invoiceNoSearch) throws Exception {
		return settlementRecallDAO.queryHeadByInvoiceNo(invoiceNoSearch);
	}
	@Override
	public List<InpatientBalanceHeadNow> queryHeadByInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception {
		return settlementRecallDAO.queryHeadByInpatientNoAndBalanceNo(
				inpatientNoSearch, balanceNoSearch);
	}
	@Override
	public List<InpatientBalanceListNow> getBalanceListInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception {
		return settlementRecallDAO.getBalanceListInpatientNoAndBalanceNo(
				inpatientNoSearch, balanceNoSearch);
	}
	@Override
	public List<InpatientInPrepayNow>  getPrepayByInpatientNoAndBalanceNo(
			String inpatientNoSearch, String balanceNoSearch) throws Exception {
		return settlementRecallDAO.getPrepayByInpatientNoAndBalanceNo(
				inpatientNoSearch,balanceNoSearch);
	}
	@Override
	public List<InpatientBalanceHeadNow> queryBalanceHead(String inpatientNo) throws Exception {
		return settlementRecallDAO.queryBalanceHead(inpatientNo);
	}
	@Override
	public String balanceRecall(String invoiceNo, String balanceNo,String inpatientNo, String balanceHeadIds, String prePayIds, String payObjId, String balanceListIds,String patientInfoId) throws Exception{
		String msg = "";
		Date curDate = null;
		
		try {
			curDate =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new DateUtils().getDateTime()) ;
		} catch (ParseException e1) {
			System.out.println(e1.getMessage());
		}
		String financeGroupId = settlementRecallDAO.getFinanceGroupIdByUserId(ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
		
		//根据发票号 结算序号 查询住院收费结算实付表
		List<InpatientBalancePayNow>  payModeList = null;
		try {
			payModeList = settlementRecallDAO.getBlancePayList(invoiceNo,balanceNo);
		} catch (Exception e1) {
			msg="获取结算实付信息时出错！";
			return msg;
		}
		//根据住院号从住院主表中  获取新的结算序号 
		int newBalanceNo = settlementRecallDAO.getNewBalanceNo(inpatientNo);
		if(newBalanceNo==0){
			msg="获取新结算序号时出错！";
			return msg;
		}
		//获取当前用户领取的最大发票号
		Map<String,String> map=new HashMap<String,String>();
		//住院发票类型
		String invoiceType = "03";
		//查询发票（根据发票类型，和领取人（获得的员工Id））
		map = outBalanceService.queryFinanceInvoiceNo(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),invoiceType);
		String maxInvoiceNo = map.get("resCode").toString();
		
		
		if( maxInvoiceNo!=null){
			//修改发票
			outBalanceDAO.saveInvoiceFinance(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),maxInvoiceNo,invoiceType);
		}
		if(payModeList!=null){
			if(payModeList.size()>0){
				//检索要召回的费用信息（根据发票号及住院号获取费用汇总表信息）
				List<InpatientFeeInfoNow> feeList = null;
				try {
					feeList = settlementRecallDAO.getFeeInfo(invoiceNo,inpatientNo);
				} catch (Exception e) {
					msg="获取费用信息时出错！";
					return msg;
				}
				if(feeList!=null){
					if(feeList.size()>0){
						for(int a=0;a<feeList.size();a++){
							InpatientFeeInfoNow feeInfo = feeList.get(a);
							 //针对每一条汇总信息，分别插入一条负交易记录和一条正交易记录
							 //插入负交易记录
							InpatientFeeInfoNow newFeeInfoF = null;
								 try {
									 newFeeInfoF = getNewFeeInfo(feeInfo,newBalanceNo,"-",curDate);
								 } catch (ParseException e) {
									msg="获取交易记录信息时出错！";
									return msg;
								 }
								 InpatientFeeInfoNow newFeeInfoZ = null;
							 try {
								newFeeInfoZ =  getNewFeeInfo(feeInfo,newBalanceNo,"+",curDate);
							} catch (ParseException e) {
								msg="获取交易记录信息时出错！";
								return msg;
							}
							if(null!=newFeeInfoF&&null!=newFeeInfoZ){
								 try {
									 settlementRecallDAO.save(newFeeInfoF);
									 OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);
									 settlementRecallDAO.save(newFeeInfoZ);
									 OperationUtils.getInstance().conserve(null,"结算召回","INSERT INTO","T_INPATIENT_FEEINFO",OperationUtils.LOGACTIONINSERT);
								} catch (Exception e) {
									 msg="插入交易记录信息时出错！";
									 return msg;
								}	
							}else{
								msg="无法插入交易记录信息！";
								return msg;
							}
							
						}
					}else{
						msg="处理费用汇总信息时出错！";
						return msg;
					}
				}else{
					msg="处理费用汇总信息时出错！";
					return msg;
				}
			}
		}else{
			msg="处理住院收费结算实付信息时出错！";
			return msg;
		}
		//处理费用明细
		try { 
			//更新药品明细
			settlementRecallDAO.updateMedicineList(balanceNo,inpatientNo,newBalanceNo);
			//更新非药品明细
			settlementRecallDAO.updateItemList(balanceNo,inpatientNo,newBalanceNo);
		} catch (Exception e) {
			msg="处理费用明细时出错！";
			return msg;
		}
		
		//处理住院主表  balanceHeadId
		try {
			settlementRecallDAO.updateInpatientInfo(inpatientNo,balanceHeadIds,newBalanceNo,patientInfoId);
		} catch (Exception e) {
			msg="处理住院时出错！";
			return msg;
		}
		
		//处理预交金
		try {
			settlementRecallDAO.dealPrepay(inpatientNo,payObjId,curDate,newBalanceNo,prePayIds,financeGroupId,maxInvoiceNo);
		} catch (Exception e) {
			msg="处理预交金时出错！";
			return msg;
		}
		
		//处理结算明细
		try {
			settlementRecallDAO.dealBalanceList(balanceListIds,newBalanceNo,curDate);
		} catch (Exception e) {
			msg="处理结算明细时出错！";
			return msg;
		}
		
		//循环处理结算头表
		try {
			settlementRecallDAO.dealBalanceHead(balanceHeadIds,newBalanceNo,curDate,financeGroupId);
		} catch (Exception e) {
			msg="处理结算信息时出错！";
			return msg;
		}
		
		//处理结算实付表
		try {
			settlementRecallDAO.dealBalancePay(payModeList,newBalanceNo,curDate);
		} catch (Exception e) {
			msg="处理结算实付信息时出错！";
			return msg;
		}
		
		//处理减免表
		try {
			settlementRecallDAO.dealDerate(inpatientNo,balanceNo,curDate,newBalanceNo);
		} catch (Exception e) {
			msg="处理减免信息时出错！";
			return msg;
		}
		
		//插入变更记录到资料变更表
		try {
			settlementRecallDAO.insertShiftData(inpatientNo,balanceNo,newBalanceNo);
		} catch (Exception e) {
			msg="处理变更记录时出错！";
			return msg;
		}
		return "success";
	}
	
	public InpatientFeeInfoNow getNewFeeInfo(InpatientFeeInfoNow feeInfo,int newBalanceNo,String zfFlg,Date curDate) throws Exception{
		InpatientFeeInfoNow newFeeInfo = new InpatientFeeInfoNow();
		newFeeInfo.setRecipeNo(feeInfo.getRecipeNo());
		newFeeInfo.setFeeCode(feeInfo.getFeeCode());
		newFeeInfo.setInpatientNo(feeInfo.getInpatientNo());
		newFeeInfo.setName(feeInfo.getName());
		newFeeInfo.setPaykindCode(feeInfo.getPaykindCode());
		newFeeInfo.setPactCode(feeInfo.getPactCode());
		newFeeInfo.setInhosDeptcode(feeInfo.getInhosDeptcode());
		newFeeInfo.setNurseCellCode(feeInfo.getNurseCellCode());
		newFeeInfo.setRecipeDeptcode(feeInfo.getRecipeDeptcode());
		newFeeInfo.setExecuteDeptcode(feeInfo.getExecuteDeptcode());
		
		newFeeInfo.setStockDeptcode(feeInfo.getStockDeptcode());
		newFeeInfo.setStockDeptname(feeInfo.getStockDeptname());
		
		newFeeInfo.setRecipeDoccode(feeInfo.getRecipeDoccode());
		if("-".equals(zfFlg)){
			//金额取反
			if(feeInfo.getTotCost()!=null){
				newFeeInfo.setTotCost(-feeInfo.getTotCost());
			}
			if(feeInfo.getOwnCost()!=null){
				newFeeInfo.setOwnCost(-feeInfo.getOwnCost());
			}
			if(feeInfo.getPayCost()!=null){
				newFeeInfo.setPayCost(-feeInfo.getPayCost());
			}
			if(feeInfo.getPubCost()!=null){
				newFeeInfo.setPubCost(-feeInfo.getPubCost());
			}
			if(feeInfo.getEcoCost()!=null){
				newFeeInfo.setEcoCost(-feeInfo.getEcoCost());
			}
		}else{
			//金额取反
			if(feeInfo.getTotCost()!=null){
				newFeeInfo.setTotCost(feeInfo.getTotCost());
			}
			if(feeInfo.getOwnCost()!=null){
				newFeeInfo.setOwnCost(feeInfo.getOwnCost());
			}
			if(feeInfo.getPayCost()!=null){
				newFeeInfo.setPayCost(feeInfo.getPayCost());
			}
			if(feeInfo.getPubCost()!=null){
				newFeeInfo.setPubCost(feeInfo.getPubCost());
			}
			if(feeInfo.getEcoCost()!=null){
				newFeeInfo.setEcoCost(feeInfo.getEcoCost());
			}
		}
		newFeeInfo.setChargeOpercode(feeInfo.getChargeOpercode());
		newFeeInfo.setChargeDate(feeInfo.getChargeDate());
		newFeeInfo.setFeeOpercode(feeInfo.getFeeOpercode());
		newFeeInfo.setFeeDate(feeInfo.getFeeDate());
		if("-".equals(zfFlg)){
			newFeeInfo.setTransType(2);//反交易
		}else{
			newFeeInfo.setTransType(1);//正交易
		}
		if("-".equals(zfFlg)){
			newFeeInfo.setBalanceOpercode(feeInfo.getBalanceOpercode());
		}else{//正交易 结算人为null
			newFeeInfo.setBalanceOpercode(null);
		}
		if("-".equals(zfFlg)){
		   newFeeInfo.setBalanceDate(curDate);
		}else{//正交 结算时间为系统时间
		   newFeeInfo.setBalanceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new DateUtils().getDateTime()));
		}
		if("-".equals(zfFlg)){
			newFeeInfo.setInvoiceNo(feeInfo.getInvoiceNo());
		}else{//正交易 结算人为null
			newFeeInfo.setInvoiceNo(null); 
		}
		if("-".equals(zfFlg)){//结算序号
		  newFeeInfo.setBalanceNo(newBalanceNo);
		}else{
		  newFeeInfo.setBalanceNo(0);	
		}
		if("-".equals(zfFlg)){//正交易 结算状态
			newFeeInfo.setBalanceState(feeInfo.getBalanceState());
		}else{
			newFeeInfo.setBalanceState(0);
		}
		newFeeInfo.setCheckNo(feeInfo.getCheckNo());
		newFeeInfo.setBabyFlag(feeInfo.getBabyFlag());
		newFeeInfo.setExtFlag(feeInfo.getExtFlag());
		newFeeInfo.setExtCode(feeInfo.getExtCode());
		newFeeInfo.setExtDate(feeInfo.getExtDate());
		newFeeInfo.setExtOpercode(feeInfo.getExtOpercode());
		newFeeInfo.setFeeoperDeptcode(feeInfo.getFeeoperDeptcode());
		newFeeInfo.setExtFlag1(feeInfo.getExtFlag1());
		newFeeInfo.setExtFlag2(feeInfo.getExtFlag2());
		
		newFeeInfo.setHospitalId(HisParameters.CURRENTHOSPITALID);//医院id
		
		newFeeInfo.setAreaCode(inprePayDAO.getDeptArea(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode()));//院区
		return newFeeInfo;
	}
	@Override
	public String getdeptNameById(String deptCode) {
		return settlementRecallDAO.getdeptNameById(deptCode);
	}
	@Override
	public String getBedNameById(String bedId) {
		return settlementRecallDAO.getBedNameById(bedId);
	}
	@Override
	public String getEmployeeName(String houseDocCode) {
		return settlementRecallDAO.getEmployeeName(houseDocCode);
	}
	@Override
	public List<PatientAccountrepaydetail> getPatientAccountrepaydetail(
			String id, String balanceNoSearch) {
		return settlementRecallDAO.getPatientAccountrepaydetail(id, balanceNoSearch);
	}
	@Override
	public List<InpatientBedinfoNow> getInpatientBedinfo(String benId)
			throws Exception {
		return settlementRecallDAO.getInpatientBedinfo(benId);
	}
	@Override
	public List<BusinessHospitalbed> getBusinessHospitalbed(String bedId)
			throws Exception {
		return settlementRecallDAO.getBusinessHospitalbed(bedId);
	}
	@Override
	public List<BusinessBedward> getBusinessBedward(String businessBedward)
			throws Exception {
		return settlementRecallDAO.getBusinessBedward(businessBedward);
	}
	@Override
	public List<SysDepartment> getSysDepartment(String nursestation)
			throws Exception {
		return settlementRecallDAO.getSysDepartment(nursestation);
	}
	@Override
	public List<BusinessContractunit> getBusinessContractunit(String encode)
			throws Exception {
		return settlementRecallDAO.getBusinessContractunit(encode);
	}
	@Override
	public List<SysEmployee> getSysEmployee(String houseDocCode)
			throws Exception {
		return settlementRecallDAO.getSysEmployee(houseDocCode);
	}
	@Override
	public List<SysEmployee> getSysEmpName() {
		return settlementRecallDAO.getSysEmpName();
	}
	@Override
	public List<User> getUseridEmpName() throws Exception {
		return settlementRecallDAO.getUserIdEmpName();
	}
}
