package cn.honry.outpatient.newChangeDeptLog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessPayMode;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.outpatient.newChangeDeptLog.dao.ChangeDeptDAO;
import cn.honry.outpatient.newChangeDeptLog.service.ChangeDeptService;
import cn.honry.outpatient.newInfo.dao.RegistrationDAO;
import cn.honry.outpatient.newInfo.vo.HospitalVo;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.MyBeanUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("changeDeptService")
@Transactional(rollbackFor={Throwable.class})
@SuppressWarnings({ "all" })
public class ChangeDeptServiceImpl implements ChangeDeptService{

	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "changeDeptDAO")
	private ChangeDeptDAO changeDeptDAO;
	
	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "ationDAO")
	private RegistrationDAO ationDAO;
	
	
	@Override
	public RegistrationNow get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(RegistrationNow arg0) {
	}

	@Override
	public List<RegistrationNow> queryRegisterMain(String cardNo,String clinicCode) {
		return changeDeptDAO.queryRegisterMain(cardNo,clinicCode);
	}

	@Override
	public List<RegisterChangeDeptLog> queryChangeDeptList(String ids) {
		return changeDeptDAO.queryChangeDeptList(ids);
	}

	@Override
	public RegistrationNow getById(String id) {
		return changeDeptDAO.getById(id);
	}

	@Override
	public List<SysDepartment> changeDepartmentCombobox() {
		return changeDeptDAO.changeDepartmentCombobox();
	}

	@Override
	public List<InfoVo> changeEmployeeCombobox(String gradeX, String newDept) {
		return changeDeptDAO.changeEmployeeCombobox(gradeX,newDept);
	}

	@Override
	public Map<String, String> registerChangeSave(RegisterChangeDeptLog changeDeptLog) {
		
		Map<String,String> map = new HashMap<String, String>();
		
		//添加换科记录
		changeDeptLog.setId(null);
		changeDeptLog.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		changeDeptLog.setCreateTime(DateUtils.getCurrentTime());
		changeDeptLog.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		changeDeptDAO.save(changeDeptLog);
		
		//处理挂号主表
		//添加
		RegistrationNow ation = changeDeptDAO.getById(changeDeptLog.getRigisterId());
		RegistrationNow  ation1 = new RegistrationNow();

		ation1.setClinicCode(ation.getClinicCode());
		ation1.setTransType(2);
		ation1.setCardId(ation.getCardId());
		ation1.setCardNo(ation.getCardNo());
		ation1.setRegDate(ation.getRegDate());
		ation1.setNoonCode(ation.getNoonCode());
		ation1.setPatientName(ation.getPatientName());
		ation1.setPatientIdenno(ation.getPatientIdenno());
		ation1.setPatientSex(ation.getPatientSex());
		ation1.setPatientBirthday(ation.getPatientBirthday());
		ation1.setPatientAge(ation.getPatientAge());
		ation1.setPatientAgeunit(ation.getPatientAgeunit());
		ation1.setRelaPhone(ation.getRelaPhone());
		ation1.setAddress(ation.getAddress());
		ation1.setCardType(ation1.getCardType());
		ation1.setPaykindName(ation.getPaykindName());
		ation1.setPactCode(ation.getPactCode());
		ation1.setPactName(ation.getPactName());
		ation1.setReglevlCode(ation.getReglevlCode());
		ation1.setReglevlName(ation.getReglevlName());
		ation1.setDeptCode(ation.getDeptCode());
		ation1.setDeptName(ation.getDeptName());
		ation1.setOrderNo(ation.getOrderNo());
		ation1.setDoctCode(ation.getDoctCode());
		ation1.setDoctName(ation.getDoctName());
		ation1.setYnregchrg(ation.getYnregchrg());
		ation1.setInvoiceNo(ation.getInvoiceNo());
		ation1.setYnbook(ation.getYnbook());
		ation1.setYnfr(1);
		ation1.setRegFeeCode(ation.getRegFeeCode());
		ation1.setRegFee(-ation.getRegFee());
		ation1.setChckFeeCode(ation.getChckFeeCode());
		ation1.setChckFee(-ation.getChckFee());
		ation1.setDiagFeeCode(ation.getDiagFeeCode());
		ation1.setDiagFee(-ation.getDiagFee());
		ation1.setOthFeeCode(ation.getOthFeeCode());
		ation1.setOthFee(-ation.getOthFee());
		ation1.setBookFeeCode(ation.getBookFeeCode());
		ation1.setBookFee(-ation.getBookFee());
		ation1.setBookFlag(ation.getBookFlag());
		ation1.setPayCost(ation.getPayCost());
		ation1.setSumCost(ation.getSumCost());
		ation1.setOperCode(ation.getOperCode());
		ation1.setOperDate(ation.getOperDate());
		ation1.setCancelOpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		ation1.setCancelDate(DateUtils.getCurrentTime());
		ation1.setMidicalrecordId(ation.getMidicalrecordId());
		ation1.setInState(1);
		ation1.setCardType(ation.getCardType());
		ation1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		ation1.setCreateTime(DateUtils.getCurrentTime());
		ation1.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		ation1.setUpdateTime(DateUtils.getCurrentTime());
		ation1.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		ation1.setNoonCodeNmae(ation.getNoonCodeNmae());
		ation1.setPatientName(ation.getPatientName());
		ation1.setPaykindCode(ation.getPaykindCode());
		ation1.setSchemaNo(ation.getSchemaNo());
		ation1.setRecipeNo(ation.getRecipeNo());
		ation1.setPatientSexName(ation.getPatientSexName());
		ation1.setSeeOptimize(ation.getSeeOptimize());
		changeDeptDAO.save(ation1);
		
		RegistrationNow  ation2 = new RegistrationNow();
		//根据医生和午别查询出当前挂号序号
		RegistrationNow info = ationDAO.queryInfoByOrder(changeDeptLog.getNewDoc(),changeDeptLog.getNewMidday());
		if(info.getOrderNo()==null){
			ation2.setOrderNo(1);
			
		}else{
			ation2.setOrderNo(info.getOrderNo()+1);
		}
		//ADDRESS,IS_ACCOUNT=0,UP_FLAG=0,CREATEHOS,HOSPITAL_ID,AREA_CODE,DEPT_CODE
		ation2.setSeeno(ation2.getOrderNo());//设置看诊序号
		ation2.setIsAccount(0);//账户流程标识1 账户挂号 0普通
		ation2.setUpFlag("0");//上传标记 0-未上传 1-上传
		if(StringUtils.isNotBlank(changeDeptLog.getNewDept())){
			HospitalVo hospitalVo = ationDAO.queryHospitalInfo(changeDeptLog.getNewDept());
			//设置建立医院
			ation2.setCreatehos(hospitalVo.getHospital_id().toString());
			//设置所属医院
			ation2.setHospitalId(hospitalVo.getHospital_id());
			//设置所属院区
			ation2.setAreaCode(hospitalVo.getDept_area_code());
		}
		ation2.setClinicCode(ation.getClinicCode());
		ation2.setTransType(1);
		ation2.setCardId(ation.getCardId());
		ation2.setCardNo(ation.getCardNo());
		ation2.setRegDate(ation.getRegDate());
		ation2.setCardType(ation.getCardType());
		ation2.setNoonCode(changeDeptLog.getNewMidday());
		ation2.setPatientName(ation.getPatientName());
		ation2.setPatientIdenno(ation.getPatientIdenno());
		ation2.setPatientSex(ation.getPatientSex());
		ation2.setPatientBirthday(ation.getPatientBirthday());
		ation2.setPatientAge(ation.getPatientAge());
		ation2.setPatientAgeunit(ation.getPatientAgeunit());
		ation2.setRelaPhone(ation.getRelaPhone());
		ation2.setAddress(ation.getAddress());
		ation2.setPatientIdenno(ation.getPatientIdenno());
		ation2.setCardType(ation1.getCardType());
		ation2.setPaykindName(ation.getPaykindName());
		ation2.setPactCode(ation.getPactCode());
		ation2.setPactName(ation.getPactName());
		ation2.setReglevlCode(changeDeptLog.getGradeX());
		ation2.setReglevlName(changeDeptLog.getGradeName());
		ation2.setDeptCode(changeDeptLog.getNewDept());
		ation2.setDeptName(changeDeptLog.getNewDeptName());
		ation2.setOrderNo(ation.getOrderNo());
		ation2.setDoctCode(changeDeptLog.getNewDoc());
		ation2.setDoctName(changeDeptLog.getNewDocName());
		ation2.setYnregchrg(ation.getYnregchrg());
		ation2.setInvoiceNo(ation.getInvoiceNo());
		ation2.setYnbook(ation.getYnbook());
		ation2.setYnfr(1);
		ation2.setRegFeeCode(ation.getRegFeeCode());
		ation2.setRegFee(ation.getRegFee());
		ation2.setChckFeeCode(ation.getChckFeeCode());
		ation2.setChckFee(ation.getChckFee());
		ation2.setDiagFeeCode(ation.getDiagFeeCode());
		ation2.setDiagFee(ation.getDiagFee());
		ation2.setOthFeeCode(ation.getOthFeeCode());
		ation2.setOthFee(ation.getOthFee());
		ation2.setBookFeeCode(ation.getBookFeeCode());
		ation2.setBookFee(ation.getBookFee());
		ation2.setBookFlag(ation.getBookFlag());
		ation2.setPayCost(ation.getPayCost());
		ation2.setSumCost(ation.getSumCost());
		ation2.setOperCode(ation.getOperCode());
		ation2.setOperDate(ation.getOperDate());
		ation2.setCancelOpcd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		ation2.setCancelDate(DateUtils.getCurrentTime());
		ation2.setMidicalrecordId(ation.getMidicalrecordId());
		ation2.setInState(0);
		ation2.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		ation2.setCreateTime(DateUtils.getCurrentTime());
		ation2.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		ation2.setUpdateTime(DateUtils.getCurrentTime());
		ation2.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		ation2.setNoonCodeNmae(ation.getNoonCodeNmae());
		ation2.setPatientName(ation.getPatientName());
		ation2.setPaykindCode(ation.getPaykindCode());
		ation2.setSchemaNo(ation.getSchemaNo());
		ation2.setRecipeNo(ation.getRecipeNo());
		ation2.setPatientSexName(ation.getPatientSexName());
		ation2.setSeeOptimize(ation.getSeeOptimize());
		changeDeptDAO.save(ation2);
		
		List<FinanceInvoicedetailNow> invoicedetailList = changeDeptDAO.findInvoicedetailList(ation.getInvoiceNo());
		if(invoicedetailList!=null && invoicedetailList.size()>0){
			for(FinanceInvoicedetailNow modls : invoicedetailList){
				FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
				invoicedetail.setId(null);//ID
				invoicedetail.setInvoiceNo(modls.getInvoiceNo());//发票号
				invoicedetail.setTransType(2);//正反类型1正2反
				invoicedetail.setInvoCode(modls.getInvoCode());//发票科目代码
				invoicedetail.setInvoName(modls.getInvoName());//发票科目名称
				invoicedetail.setPubCost(0.00);//可报效金额
				invoicedetail.setOwnCost(0.00);//不可报效金额
				invoicedetail.setPayCost(-modls.getPayCost());//自付金额
				invoicedetail.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//执行科室ID
				invoicedetail.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//执行科室名称
				invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
				invoicedetail.setOperCode(modls.getOperCode());//操作人
				invoicedetail.setBalanceFlag(0);//日结状态
				invoicedetail.setCancelFlag(1);//状态
				invoicedetail.setInvoiceSeq(modls.getInvoiceSeq());//发票序号
				invoicedetail.setInvoSequence(modls.getInvoSequence());//发票内流水号
				invoicedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				invoicedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				invoicedetail.setCreateTime(DateUtils.getCurrentTime());
				changeDeptDAO.save(invoicedetail);
			}
			
			for(FinanceInvoicedetailNow modls : invoicedetailList){
				FinanceInvoicedetailNow invoicedetail2 = new FinanceInvoicedetailNow();
				invoicedetail2.setId(null);//ID
				invoicedetail2.setInvoiceNo(modls.getInvoiceNo());//发票号
				invoicedetail2.setTransType(1);//正反类型1正2反
				invoicedetail2.setInvoCode(modls.getInvoCode());//发票科目代码
				invoicedetail2.setInvoName(modls.getInvoName());//发票科目名称
				invoicedetail2.setPubCost(0.00);//可报效金额
				invoicedetail2.setOwnCost(0.00);//不可报效金额
				invoicedetail2.setPayCost(modls.getPayCost());//自付金额
				invoicedetail2.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//执行科室ID
				invoicedetail2.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//执行科室名称
				invoicedetail2.setOperDate(DateUtils.getCurrentTime());//操作时间
				invoicedetail2.setOperCode(modls.getOperCode());//操作人
				invoicedetail2.setBalanceFlag(0);//日结状态
				invoicedetail2.setCancelFlag(1);//状态
				invoicedetail2.setInvoiceSeq(modls.getInvoiceSeq());//发票序号
				invoicedetail2.setInvoSequence(modls.getInvoSequence());//发票内流水号
				invoicedetail2.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				invoicedetail2.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				invoicedetail2.setCreateTime(DateUtils.getCurrentTime());
				changeDeptDAO.save(invoicedetail2);
			}
			
			for(FinanceInvoicedetailNow modls : invoicedetailList){
				modls.setCancelFlag(2);
				changeDeptDAO.update(modls);
			}
		}
		
		FinanceInvoiceInfoNow invoiceInfo = changeDeptDAO.findInvoiceInfo(ation.getInvoiceNo());
		FinanceInvoiceInfoNow invoiceInfo1 = new FinanceInvoiceInfoNow();
		invoiceInfo1.setId(null);
		invoiceInfo1.setInvoiceNo(invoiceInfo.getInvoiceNo());//发票号
		invoiceInfo1.setTransType(2);//交易类型
		invoiceInfo1.setCardNo(invoiceInfo.getCardNo());//病历号
		invoiceInfo1.setRegDate(invoiceInfo.getRegDate());//挂号日期
		invoiceInfo1.setName(invoiceInfo.getName());//患者姓名
		invoiceInfo1.setPaykindCode(invoiceInfo.getPaykindCode());//结算类别
		invoiceInfo1.setPactCode(invoiceInfo.getPactCode());//合同单位代码
		invoiceInfo1.setPactName(invoiceInfo.getPactName());//合同单位名称
		invoiceInfo1.setMcardNo(invoiceInfo.getMcardNo());//个人编号
		invoiceInfo1.setMedicalType(invoiceInfo.getMedicalType());//医疗类别
		invoiceInfo1.setTotCost(invoiceInfo.getTotCost());//总金额
		invoiceInfo1.setPubCost(-invoiceInfo.getPubCost());//可报效金额
		invoiceInfo1.setOwnCost(-invoiceInfo.getOwnCost());//不可报效金额
		invoiceInfo1.setPayCost(-invoiceInfo.getPayCost());//自付金额
		invoiceInfo1.setRealCost(invoiceInfo.getRealCost());//实付金额
		invoiceInfo1.setExamineFlag(0);//团体/个人
		invoiceInfo1.setCancelFlag(0);//有效
		invoiceInfo1.setCancelInvoice(invoiceInfo.getCancelInvoice());//作废票据号
		invoiceInfo1.setCancelCode(invoiceInfo.getCancelCode());//作废操作员
		invoiceInfo1.setCancelDate(invoiceInfo.getCancelDate());//作废时间
		invoiceInfo1.setInvoiceSeq(invoiceInfo.getInvoiceSeq());//发票序号
		invoiceInfo1.setInvoiceComb(invoiceInfo.getInvoiceComb());//一次收费序号
		invoiceInfo1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		invoiceInfo1.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		invoiceInfo1.setCreateTime(DateUtils.getCurrentTime());
		invoiceInfo1.setTransType(2);
		changeDeptDAO.save(invoiceInfo1);
		
		FinanceInvoiceInfoNow invoiceInfo2 = new FinanceInvoiceInfoNow();
		invoiceInfo2.setId(null);
		invoiceInfo2.setInvoiceNo(invoiceInfo.getInvoiceNo());//发票号
		invoiceInfo2.setTransType(2);//交易类型
		invoiceInfo2.setCardNo(invoiceInfo.getCardNo());//病历号
		invoiceInfo2.setRegDate(invoiceInfo.getRegDate());//挂号日期
		invoiceInfo2.setName(invoiceInfo.getName());//患者姓名
		invoiceInfo2.setPaykindCode(invoiceInfo.getPaykindCode());//结算类别
		invoiceInfo2.setPactCode(invoiceInfo.getPactCode());//合同单位代码
		invoiceInfo2.setPactName(invoiceInfo.getPactName());//合同单位名称
		invoiceInfo2.setMcardNo(invoiceInfo.getMcardNo());//个人编号
		invoiceInfo2.setMedicalType(invoiceInfo.getMedicalType());//医疗类别
		invoiceInfo2.setTotCost(invoiceInfo.getTotCost());//总金额
		invoiceInfo2.setPubCost(-invoiceInfo.getPubCost());//可报效金额
		invoiceInfo2.setOwnCost(-invoiceInfo.getOwnCost());//不可报效金额
		invoiceInfo2.setPayCost(-invoiceInfo.getPayCost());//自付金额
		invoiceInfo2.setRealCost(invoiceInfo.getRealCost());//实付金额
		invoiceInfo2.setExamineFlag(0);//团体/个人
		invoiceInfo2.setCancelFlag(1);//有效
		invoiceInfo2.setCancelInvoice(invoiceInfo.getCancelInvoice());//作废票据号
		invoiceInfo2.setCancelCode(invoiceInfo.getCancelCode());//作废操作员
		invoiceInfo2.setCancelDate(invoiceInfo.getCancelDate());//作废时间
		invoiceInfo2.setInvoiceSeq(invoiceInfo.getInvoiceSeq());//发票序号
		invoiceInfo2.setInvoiceComb(invoiceInfo.getInvoiceComb());//一次收费序号
		invoiceInfo2.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		invoiceInfo2.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		invoiceInfo2.setCreateTime(DateUtils.getCurrentTime());
		changeDeptDAO.save(invoiceInfo2);
		
		BusinessPayModeNow pay = ationDAO.queryPayMode(ation.getInvoiceNo());
		BusinessPayModeNow payMode = new BusinessPayModeNow();
		payMode.setId(null);//id
		payMode.setInvoiceNo(pay.getInvoiceNo());/**发票号 **/
		payMode.setTransType(2);/**交易类型,1正，2反**/
		payMode.setSequenceNo(1);/**交易流水号**/
		payMode.setModeCode(pay.getModeCode());/**支付方式**/
		payMode.setTotCost(-pay.getTotCost());/**应付金额**/
		payMode.setRealCost(-pay.getRealCost());/**实付金额**/
		payMode.setCheckFlag(0);/**0未核查/1已核查**/
		payMode.setBalanceFlag(0);/**0未日结/1已日结**/
		payMode.setCorrectFlag(0);/**0未对帐/1已对帐**/
		payMode.setInvoiceSeq(pay.getInvoiceSeq());/**发票序号，一次结算产生多张发票的combNo**/
		payMode.setCancelFlag(0);/**1正常，0作废，2重打，3注销**/
		payMode.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
		payMode.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
		payMode.setCreateTime(DateUtils.getCurrentTime());//创建时间
		ationDAO.save(payMode);
		
		BusinessPayMode payMode1 = new BusinessPayMode();
		payMode1.setId(null);//id
		payMode1.setInvoiceNo(pay.getInvoiceNo());/**发票号 **/
		payMode1.setTransType(1);/**交易类型,1正，2反**/
		payMode1.setSequenceNo(1);/**交易流水号**/
		payMode1.setModeCode(pay.getModeCode());/**支付方式**/
		payMode1.setTotCost(pay.getTotCost());/**应付金额**/
		payMode1.setRealCost(pay.getRealCost());/**实付金额**/
		payMode1.setCheckFlag(0);/**0未核查/1已核查**/
		payMode1.setBalanceFlag(0);/**0未日结/1已日结**/
		payMode1.setCorrectFlag(0);/**0未对帐/1已对帐**/
		payMode1.setInvoiceSeq(pay.getInvoiceSeq());/**发票序号，一次结算产生多张发票的combNo**/
		payMode1.setCancelFlag(1);/**1正常，0作废，2重打，3注销**/
		payMode1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
		payMode1.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
		payMode1.setCreateTime(DateUtils.getCurrentTime());//创建时间
		ationDAO.save(payMode);
		
		
		ation.setInState(1);
		changeDeptDAO.save(ation);
		
		invoiceInfo.setCancelFlag(3);
		changeDeptDAO.update(invoiceInfo);
		//更新原科室已挂人数，-1
		RegisterDocSource source = ationDAO.findNewInfoList(changeDeptLog.getOldDept(), changeDeptLog.getOldDoc(), null, ation.getNoonCode(), null).get(0);
		source.setClinicSum(source.getClinicSum()-1);
		ationDAO.update(source);
		//更新现科室已挂人数，+1
		RegisterDocSource source2 = ationDAO.findNewInfoList(changeDeptLog.getNewDept(), changeDeptLog.getNewDoc(), null, changeDeptLog.getNewMidday(), null).get(0);
		source2.setClinicSum(source2.getClinicSum()+1);
		ationDAO.update(source2);
		
		map.put("resMsg", "success");
		return map;
	}
 
	@Override
	public List<SysDepartment> querydeptComboboxs() {
		return changeDeptDAO.querydeptComboboxs();
	}

	@Override
	public List<SysEmployee> queryempComboboxs() {
		return changeDeptDAO.queryempComboboxs();
	}

	@Override
	public List<RegistrationNow> queryChangeDept(String cardNo, String clinicCode) {
		return changeDeptDAO.queryChangeDept(cardNo,clinicCode);
	}


}
