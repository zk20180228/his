package cn.honry.finance.outpatientAccount.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.finance.outpatientAccount.dao.AccountrecordDAO;
import cn.honry.finance.outpatientAccount.dao.OutAccountDAO;
import cn.honry.finance.outpatientAccount.dao.PatientAccountDAO;
import cn.honry.finance.outpatientAccount.service.OutAccountService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

/**
 * 门诊患者账户管理
 * @author  wangfujun
 * @date 创建时间：2016年3月28日 下午2:00:00
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@Service("outAccountService")
@Transactional
@SuppressWarnings({"all"})
public class OutAccountServiceImpl implements OutAccountService {
	
	//注入本类  预交金dao
	@Autowired
	@Qualifier(value = "outAccountDAO")
	private OutAccountDAO outAccountDAO;
	
	//注入患者账户dao
	@Autowired
	@Qualifier(value = "patientAccountDAO")
	private PatientAccountDAO patientAccountDAO;
	
	//注入患者账户操作流水dao
	@Autowired
	@Qualifier(value = "accountrecordDAO")
	private AccountrecordDAO accountrecordDAO;
	
	@Override
	public OutpatientOutprepay get(String arg0) {
		return outAccountDAO.get(arg0);
	}

	
	
	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OutpatientOutprepay arg0) {
		
	}

	@Override
	public PatientIdcard getForidcardNo(String idcardNo,String menuAlias) {
		return outAccountDAO.getForidcardNo(idcardNo,menuAlias);
	}

	@Override
	public OutpatientAccount getForidcardid(String menuAlias, String idcardid) {
		return outAccountDAO.getForidcardid(menuAlias, idcardid);
	}

	@Override
	public void addAccount(String idcardNo, OutpatientAccount account,String menuAlias) {
		//当前登陆信息
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		//生成32位UUID
		 String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");  
		
		//患者账户表，创建账户
		PatientIdcard patientIdcard = outAccountDAO.getForidcardNo(idcardNo,menuAlias);
		account.setId(uuid);
		account.setMedicalrecordId( patientIdcard.getPatient().getMedicalrecordId() );
		account.setIdcardId( patientIdcard.getId() );
		String pwdString=account.getAccountPassword();
		if(StringUtils.isNotBlank(pwdString)){
			account.setAccountPassword(DigestUtils.md5Hex(pwdString));
		}else{
			account.setAccountPassword(null);
		}
		account.setCreateDept(deptid);
		account.setCreateUser(userid);
		account.setCreateTime(DateUtils.getCurrentTime());
		account.setUpdateUser(userid);
		account.setUpdateTime(DateUtils.getCurrentTime());
		patientAccountDAO.save(account);
		
		//者账户操作流水表
		OutpatientAccountrecord accountrecord=new OutpatientAccountrecord();
		accountrecord.setMedicalrecordId(patientIdcard.getPatient().getMedicalrecordId());
		accountrecord.setAccountId(uuid);
		/** 操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打   **/
		accountrecord.setOpertype(1);
		accountrecord.setDeptCode(deptid);
		accountrecord.setOperCode(userid);
		accountrecord.setOperDate(DateUtils.getCurrentTime());
		/***是否有效：0有效;1无效**/
		accountrecord.setValid(0);
		accountrecord.setCreateDept(deptid);
		accountrecord.setCreateUser(userid);
		accountrecord.setCreateTime(DateUtils.getCurrentTime());
		accountrecord.setUpdateUser(userid);
		accountrecord.setUpdateTime(DateUtils.getCurrentTime());
		accountrecordDAO.save(accountrecord);
	}

	@Override
	public void saveOutprepay(String idcardNo, OutpatientOutprepay outprepay,String menuAlias){
		int flag = 0;//0库存修改产生并发异常或其他异常；1正常保存 
		//当前登陆信息
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		//获取就诊卡信息
		PatientIdcard patientIdcard = outAccountDAO.getForidcardNo(idcardNo,menuAlias);
		//患者信息
		Patient patient=patientIdcard.getPatient();
		//根据就诊卡编号，获取患者账户信息
		OutpatientAccount patientAccount=outAccountDAO.getForidcardid(menuAlias, patientIdcard.getId());
		/***
		 * 操作预交金表
		 * 该表中存有预交金余额，已添加乐观锁，在并发更新时将抛出StaleObjectStateException异常
		 */
		outprepay.setMedicalrecordId(patient.getMedicalrecordId());
		outprepay.setAccountId(patientAccount.getId());
		//发生序号（序列，5位）
		String happenNo=outAccountDAO.getSeqByNameorNum("SEQ_TOP_OUTPREPAY_HAPPENNO", 5);
		outprepay.setHappenNo(Integer.valueOf(happenNo));
		outprepay.setPatientName(patient.getPatientName());
		//预交金收据好（序列，14位）
		String receiptNo=outAccountDAO.getSeqByNameorNum("SEQ_TOP_OUTPREPAY_RECEIPTNO", 14);
		outprepay.setReceiptNo(receiptNo);
		/***预交金状态：0反还，1收取，2重打*/
		outprepay.setPrepayState(1);
		outprepay.setCreateDept(deptid);
		outprepay.setCreateUser(userid);
		outprepay.setCreateTime(DateUtils.getCurrentTime());
		outprepay.setUpdateUser(userid);
		outprepay.setUpdateTime(DateUtils.getCurrentTime());
		outAccountDAO.save(outprepay);
		
		/***
		 * 操作患者账户流水表
		 * 只是插入数据，余额来源是患者账户表，故不对其加版本锁
		 */
		OutpatientAccountrecord accountrecord=new OutpatientAccountrecord();
		accountrecord.setMedicalrecordId(patient.getMedicalrecordId());
		accountrecord.setAccountId(patientAccount.getId());
		/** 操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打   **/
		accountrecord.setOpertype(0);
		accountrecord.setMoney(outprepay.getPrepayCost());
		accountrecord.setDeptCode(deptid);
		accountrecord.setOperCode(userid);
		accountrecord.setOperDate(DateUtils.getCurrentTime());
		/***是否有效：0有效;1无效**/
		accountrecord.setValid(0);
		//交易后余额
		accountrecord.setAccountBalance(patientAccount.getAccountBalance()+outprepay.getPrepayCost());
		/***
		 * 发票类型（编码表）写死的数据
		 * 				ID					   Code     Name
		 * 402880a54e3e0568014e3e06b3580001	 	01		门诊
		 * 402880a54e3e0568014e3e07b9150004		02		收费
		 * 402880a54e3e0568014e3e06f6800002		03		住院
		 * 402880984f6d7e2b014f6d8151200003		04		预交收据
		 * 402880a54e3e0568014e3e0728e70003		05		账户
		 * 
		 * 现在发票类型存code编码  2016/7/22
		 */
		accountrecord.setInvoiceType("04");
		accountrecord.setCreateDept(deptid);
		accountrecord.setCreateUser(userid);
		accountrecord.setCreateTime(DateUtils.getCurrentTime());
		accountrecord.setUpdateUser(userid);
		accountrecord.setUpdateTime(DateUtils.getCurrentTime());
		accountrecordDAO.save(accountrecord);
		
		/***
		 * 操作患者账户表
		 */
		OutpatientAccount account=patientAccount;
		account.setAccountBalance(patientAccount.getAccountBalance()+outprepay.getPrepayCost());
		account.setUpdateUser(userid);
		account.setUpdateTime(DateUtils.getCurrentTime());
		try {
			patientAccountDAO.update(account);
			flag = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(flag == 0 ){
				new RuntimeException("事务并发或其他异常，请稍后重试！");
			}
		}
	}

	@Override
	public List<OutpatientOutprepay> queryPrestore(String accountID,String ishistory, String menuAlias,String page,String rows) {
		return outAccountDAO.queryPrestore(accountID, ishistory, menuAlias,page,rows);
	}

	@Override
	public List<OutpatientAccountrecord> queryDatailed(String accountID,
			String menuAlias,String page,String rows) {
		return accountrecordDAO.queryDatailed(accountID, menuAlias,page,rows);
	}

	
	/***
	 * 此方法是更新患者账户的账户状态
	 * 状态标识：	0停用 1正常 2注销 
	 */
	@Override
	public void updateAccount(OutpatientAccount account, String idcardNo,String blhString,String idcardQuery,String menuAlias) {
		//当前登陆信息
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		/***
		 * idcardQuery 判断读卡的方式 
		 *   1 就诊卡读取（idcardNo为就诊卡卡号），
		 * 	 0病历号读取（blhString为病历号）
		 */
		PatientIdcard patientIdcard;
		OutpatientAccount outpatientAccount;
		if(!StringUtils.isBlank(idcardQuery) && account.getAccountState()==1 && "0".equals(idcardQuery)){
			Patient patient = patientAccountDAO.getForblh(blhString);
			patientIdcard = patientAccountDAO.getForPatientId(patient.getId());
			outpatientAccount = patientAccountDAO.getAccounForblh(blhString);
			
			account.setId(outpatientAccount.getId());
			account.setIdcardId(patientIdcard.getId());
			account.setUpdateUser(userid);
			account.setUpdateTime(DateUtils.getCurrentTime());
			patientAccountDAO.updateAccount(account, menuAlias);
			
		}else{
			//根据就诊卡号查询就诊卡信息，根据就诊卡编号查询患者账户信息
			patientIdcard = outAccountDAO.getForidcardNo(idcardNo,menuAlias);
			outpatientAccount = outAccountDAO.getForidcardid(menuAlias, patientIdcard.getId());
			
			account.setId(outpatientAccount.getId());
			account.setUpdateUser(userid);
			account.setUpdateTime(DateUtils.getCurrentTime());
			patientAccountDAO.updateAccount(account, menuAlias);
		}
		
		/***
		 * 操作患者账户明细
		 */
		OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
		accountrecord.setMedicalrecordId(patientIdcard.getPatient().getMedicalrecordId());
		accountrecord.setAccountId(outpatientAccount.getId());
		/***操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打**/
		/***账户状态：0停用 1正常 2注销**/
		if(account.getAccountState() == 0){
			accountrecord.setOpertype(2);
		}else if(account.getAccountState() == 1){
			accountrecord.setOpertype(3);
		}else{
			accountrecord.setOpertype(6);
		}
		accountrecord.setDeptCode(deptid);
		accountrecord.setOperCode(userid);
		accountrecord.setOperDate(DateUtils.getCurrentTime());
		/***是否有效：0有效1无效*/
		accountrecord.setValid(0);
		accountrecord.setAccountBalance(outpatientAccount.getAccountBalance());
		accountrecord.setCreateDept(deptid);
		accountrecord.setCreateUser(userid);
		accountrecord.setCreateTime(DateUtils.getCurrentTime());
		accountrecord.setUpdateUser(userid);
		accountrecord.setUpdateTime(DateUtils.getCurrentTime());
		accountrecordDAO.save(accountrecord);
	}

	@Override
	public void settleAccount( String idcardNo,String menuAlias) {
		//根据就诊卡号查询就诊卡信息，根据就诊卡编号查询患者账户信息
		PatientIdcard patientIdcard = outAccountDAO.getForidcardNo(idcardNo,menuAlias);
		OutpatientAccount outpatientAccount = outAccountDAO.getForidcardid(menuAlias, patientIdcard.getId());
		//之前余额
		Double accountBalance = outpatientAccount.getAccountBalance();
		//当前登陆信息
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		/***
		 * 结清账户，患者账户余额至空
		 */
		outpatientAccount.setAccountBalance(0.0);
		outpatientAccount.setUpdateUser(userid);
		outpatientAccount.setUpdateTime(DateUtils.getCurrentTime());
		patientAccountDAO.update(outpatientAccount);
		/****
		 * 患者账户操作明细
		 */
		OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
		accountrecord.setMedicalrecordId(patientIdcard.getPatient().getMedicalrecordId());
		accountrecord.setAccountId(outpatientAccount.getId());
		/***操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打**/
		accountrecord.setOpertype(10);
		accountrecord.setMoney(-accountBalance);
		accountrecord.setDeptCode(deptid);
		accountrecord.setOperCode(userid);
		accountrecord.setOperDate(DateUtils.getCurrentTime());
		/***是否有效：0有效1无效*/
		accountrecord.setValid(0);
		accountrecord.setAccountBalance(outpatientAccount.getAccountBalance());
		accountrecord.setCreateDept(deptid);
		accountrecord.setCreateUser(userid);
		accountrecord.setCreateTime(DateUtils.getCurrentTime());
		accountrecord.setUpdateUser(userid);
		accountrecord.setUpdateTime(DateUtils.getCurrentTime());
		accountrecordDAO.save(accountrecord);
		/***
		 * 患者预交金表，更改状态
		 * 是否历史数据(结清帐户以前的数据)1是 0否
		 */
		List<OutpatientOutprepay> outAccountList1 = outAccountDAO.queryPrestore(outpatientAccount.getId(),"0",menuAlias,null,null);
		List<OutpatientOutprepay> outAccountList2 = new ArrayList<OutpatientOutprepay>();
		if(outAccountList1 != null && outAccountList1.size() > 0){
			for(OutpatientOutprepay vo : outAccountList1){
				OutpatientOutprepay model = vo;
				model.setIshistory(1);
				outAccountList2.add(model);
			}
		}
		outAccountDAO.saveOrUpdateList(outAccountList2);
	}

	@Override
	public OutpatientAccount getAccountForID(String arg0) {
		return patientAccountDAO.get(arg0);
	}

	@Override
	public void returnOutprepay(String accountID, String outAccountID,
			String resType) {
		//当前登陆信息
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		//患者账户记录
		OutpatientAccount vo = patientAccountDAO.get(accountID);
		//预交金记录
		OutpatientOutprepay model = outAccountDAO.get(outAccountID);
		Double voMoney = vo.getAccountBalance();
		Double modelMoney = model.getPrepayCost();
		
		/** 1 部分退款，0 全额退款***/
		if("0".equals(resType)){
			//操作预交金表
			/**预交金状态：0反还，1收取，2重打**/
			model.setPrepayState(0);
			model.setUpdateUser(userid);
			model.setUpdateTime(DateUtils.getCurrentTime());
			outAccountDAO.update(model);
			//操作患者账户表
			vo.setAccountBalance(voMoney-modelMoney);
			vo.setUpdateUser(userid);
			vo.setUpdateTime(DateUtils.getCurrentTime());
			//操作患者操作明细表
			OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
			accountrecord.setMedicalrecordId(vo.getMedicalrecordId());
			accountrecord.setAccountId(accountID);
			/**操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打**/
			accountrecord.setOpertype(8);
			accountrecord.setMoney(-modelMoney);
			accountrecord.setDeptCode(deptid);
			accountrecord.setOperCode(userid);
			accountrecord.setOperDate(DateUtils.getCurrentTime());
			accountrecord.setValid(0);
			accountrecord.setAccountBalance(voMoney-modelMoney);
			accountrecord.setCreateDept(deptid);
			accountrecord.setCreateUser(userid);
			accountrecord.setCreateTime(DateUtils.getCurrentTime());
			accountrecord.setUpdateUser(userid);
			accountrecord.setUpdateTime(DateUtils.getCurrentTime());
			accountrecordDAO.save(accountrecord);
		}else{
			//操作预交金表
			/**预交金状态：0反还，1收取，2重打**/
			model.setPrepayState(0);
			model.setUpdateUser(userid);
			model.setUpdateTime(DateUtils.getCurrentTime());
			outAccountDAO.update(model);
			//操作患者账户表
			vo.setAccountBalance(0.0);
			vo.setUpdateUser(userid);
			vo.setUpdateTime(DateUtils.getCurrentTime());
			//操作患者操作明细表
			OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
			accountrecord.setMedicalrecordId(vo.getMedicalrecordId());
			accountrecord.setAccountId(accountID);
			/**操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打**/
			accountrecord.setOpertype(8);
			accountrecord.setMoney(-voMoney);
			accountrecord.setDeptCode(deptid);
			accountrecord.setOperCode(userid);
			accountrecord.setOperDate(DateUtils.getCurrentTime());
			accountrecord.setValid(0);
			accountrecord.setAccountBalance(0.0);
			accountrecord.setCreateDept(deptid);
			accountrecord.setCreateUser(userid);
			accountrecord.setCreateTime(DateUtils.getCurrentTime());
			accountrecord.setUpdateUser(userid);
			accountrecord.setUpdateTime(DateUtils.getCurrentTime());
			accountrecordDAO.save(accountrecord);
		}
	}

	@Override
	public void makeOutprepay(String outprepayID) {
		//当前登陆信息
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		//预交金信息
		OutpatientOutprepay outprepay = outAccountDAO.get(outprepayID);
		//患者账户信息
		OutpatientAccount account = patientAccountDAO.get(outprepay.getAccountId());
		/**
		 * 操作预交金表
		 */
		outprepay.setPrepayState(2);
		Integer num = outprepay.getPrintTimes();
		if(num == null){
			outprepay.setPrintTimes(1);
		}else{
			outprepay.setPrintTimes(num + 1);
		}
		String oldRecipeno = outprepay.getReceiptNo();
		//预交金收据好（序列，14位）
		String receiptNo = outAccountDAO.getSeqByNameorNum("SEQ_TOP_OUTPREPAY_RECEIPTNO", 14);
		outprepay.setReceiptNo(receiptNo);
		outprepay.setOldRecipeno(oldRecipeno);
		outprepay.setUpdateUser(userid);
		outprepay.setUpdateTime(DateUtils.getCurrentTime());
		outAccountDAO.save(outprepay);
		/***
		 * 操作账户明细表
		 */
		OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
		accountrecord.setMedicalrecordId(account.getMedicalrecordId());
		accountrecord.setAccountId(account.getId());
		/**操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打**/
		accountrecord.setOpertype(12);
		accountrecord.setDeptCode(deptid);
		accountrecord.setOperCode(userid);
		accountrecord.setOperDate(DateUtils.getCurrentTime());
		accountrecord.setValid(0);
		accountrecord.setAccountBalance(account.getAccountBalance());
		accountrecord.setCreateDept(deptid);
		accountrecord.setCreateUser(userid);
		accountrecord.setCreateTime(DateUtils.getCurrentTime());
		accountrecord.setUpdateUser(userid);
		accountrecord.setUpdateTime(DateUtils.getCurrentTime());
		accountrecordDAO.save(accountrecord);
	}

	@Override
	public Boolean uppwdAccount(String idcardNo, String oldpwd, String nowpwd,String menuAlias) {
		//当前登陆信息
		String deptid = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode();
		String userid = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		//根据就诊卡号查询就诊卡信息，根据就诊卡编号查询患者账户信息
		PatientIdcard patientIdcard = outAccountDAO.getForidcardNo(idcardNo,menuAlias);
		OutpatientAccount outpatientAccount = outAccountDAO.getForidcardid(menuAlias, patientIdcard.getId());
		String oldpwdmd5 = DigestUtils.md5Hex(oldpwd);
		String nowpwdmd5 = DigestUtils.md5Hex(nowpwd);
		String kupwd = outpatientAccount.getAccountPassword();
		//与原密码是否一致
		if(oldpwdmd5.equals(kupwd)){
			outpatientAccount.setAccountPassword(nowpwdmd5);
			outpatientAccount.setUpdateUser(userid);
			outpatientAccount.setUpdateTime(DateUtils.getCurrentTime());
			patientAccountDAO.update(outpatientAccount);
			
			/***
			 * 操作患者账户明细
			 */
			OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
			accountrecord.setMedicalrecordId(outpatientAccount.getMedicalrecordId());
			accountrecord.setAccountId(outpatientAccount.getId());
			/**操作类型：0存预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权12补打**/
			accountrecord.setOpertype(9);
			accountrecord.setDeptCode(deptid);
			accountrecord.setOperCode(userid);
			accountrecord.setOperDate(DateUtils.getCurrentTime());
			accountrecord.setValid(0);
			accountrecord.setAccountBalance(outpatientAccount.getAccountBalance());
			accountrecord.setCreateDept(deptid);
			accountrecord.setCreateUser(userid);
			accountrecord.setCreateTime(DateUtils.getCurrentTime());
			accountrecord.setUpdateUser(userid);
			accountrecord.setUpdateTime(DateUtils.getCurrentTime());
			accountrecordDAO.save(accountrecord);
			return true;
		}
		return false;
	}

	@Override
	public int getTotal(String accountID, String menuAlias) {
		return accountrecordDAO.getTotal(accountID, menuAlias);
	}

	@Override
	public int getTotal(String accountID, String ishistory, String menuAlias) {
		return outAccountDAO.getTotal(accountID, ishistory, menuAlias);
	}

	@Override
	public Patient getForblh(String blhString) {
		return patientAccountDAO.getForblh(blhString);
	}

	@Override
	public PatientIdcard getForPatientId(String patientid) {
		return patientAccountDAO.getForPatientId(patientid);
	}

	@Override
	public OutpatientAccount getAccounForblh(String blhString) {
		return patientAccountDAO.getAccounForblh(blhString);
	}



	@Override
	public int findAccounState(String idcardNo) {
		//根据就诊卡号查询就诊卡信息，根据就诊卡编号查询患者账户信息
		PatientIdcard patientIdcard = outAccountDAO.getForidcardNo(idcardNo,"");
		OutpatientAccount outpatientAccount = outAccountDAO.getForidcardid("", patientIdcard.getId());
		return outpatientAccount.getAccountState();
	}



	@Override
	public void updayLimit(String idcardNo,Double accountDaylimit) {
		//根据就诊卡号查询就诊卡信息，根据就诊卡编号查询患者账户信息
		PatientIdcard patientIdcard = outAccountDAO.getForidcardNo(idcardNo,"");
		OutpatientAccount outpatientAccount = outAccountDAO.getForidcardid("", patientIdcard.getId());
		outpatientAccount.setAccountDaylimit(accountDaylimit);
		outAccountDAO.update(outpatientAccount);
	}
}
