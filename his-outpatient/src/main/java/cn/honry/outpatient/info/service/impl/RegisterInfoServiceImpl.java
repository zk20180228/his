package cn.honry.outpatient.info.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessPayMode;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.info.dao.InvoiceRegisterDAO;
import cn.honry.outpatient.info.dao.InvoicedetailInfoDAO;
import cn.honry.outpatient.info.dao.PayModeInfoDAO;
import cn.honry.outpatient.info.dao.RegisterInfoDAO;
import cn.honry.outpatient.info.service.RegisterInfoService;
import cn.honry.outpatient.info.vo.EmpInfoVo;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.info.vo.InfoStatistics;
import cn.honry.outpatient.info.vo.InfoVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("registerInfoService")
@Transactional(rollbackFor={Throwable.class})
@SuppressWarnings({ "all" })
public class RegisterInfoServiceImpl implements RegisterInfoService{

	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "registerInfoDAO")
	private RegisterInfoDAO registerInfoDAO;
	
	@Autowired
	@Qualifier(value = "invoicedetailInfoDAO")
	private InvoicedetailInfoDAO invoicedetailInfoDAO;//发票明细DAO
	
	@Autowired
	@Qualifier(value = "invoiceRegisterDAO")
	private InvoiceRegisterDAO invoiceRegisterDAO;//发票明细DAO
	
	@Autowired
	@Qualifier(value = "payModeInfoDAO")
	private PayModeInfoDAO payModeInfoDAO;//结算支付情况表
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public RegisterInfo get(String id) {
		return registerInfoDAO.get(id);
	}
	
	@Override
	public void saveOrUpdate(RegisterInfo entity) {
	}

	
	/**  
	 *  
	 * @Description：  门诊卡id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-25 下午11:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 下午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo gethz(String idcardNo) {
		return registerInfoDAO.gethz(idcardNo);
	}
	/**  
	 *  
	 * @Description：  查询患者树
	 *@Author：wujiao
	 * @CreateDate：2015-6-25 上午3:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 上午3:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RegisterInfo> getInfoByEmployeeId(String id,String type) {
		return registerInfoDAO.getInfoByEmployeeId(id,type);
	}
	/**  
	 *  
	 * @Description：  单击患者查询id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-25 下午5:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 下午5:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterInfo gethzid(String id) {
		return registerInfoDAO.gethzid(id);
	}
	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<SysDepartment> deptCombobox() {
		List<SysDepartment> lst = registerInfoDAO.deptCombobox();
		return lst;
	}
	/**  
	 * @Description：  挂号级别（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param q 用于下拉的即时查询（拼音，五笔，自定义）
	 */
	@Override
	public List<RegisterGrade> gradeCombobox(String q) {
		List<RegisterGrade> lst = registerInfoDAO.gradeCombobox(q);
		return lst;
	}
	/**  
	 * @Description：  挂号专家（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<EmpInfoVo> empCombobox(String dept, String grade,Integer midday) {
		List<EmpInfoVo> list = registerInfoDAO.empCombobox(dept,grade,midday);
		return list;
	}
	/**  
	 *  
	 * @Description：  合同单位
	 * @parm:
	 * @Author：liudelin
	 * @CreateDate：2015-6-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessContractunit> queryBusinessContractunit() {
		List<BusinessContractunit> lst = registerInfoDAO.queryBusinessContractunit();
		return lst;
	}
	/**  
	 *  
	 * @Description：  显示挂号费
	 * @parm:id（合同单位ID）
	 * @Author：liudelin
	 * @CreateDate：2015-6-17 下午05:12:16  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public RegisterFee queryRegisterFee(String id, String gradeId) {
		 RegisterFee  registerFee = registerInfoDAO.queryRegisterFee(id,gradeId);
		return registerFee;
	}
	/**  
	 * @Description：  值班列表
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<InfoVo> findInfoList(String deptId, String empId, String gradeId, String midday) {
		List<InfoVo> lst = registerInfoDAO.findInfoList(deptId,empId,gradeId,midday);
		return lst;
	}
	/**  
	 * @Description：  根据职称转换
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterGrade queryGradeTitle(String encode) {
		RegisterGrade registerGrade = registerInfoDAO.queryGradeTitle(encode);
		return registerGrade;
	}
	/**  
	 * @Description：  查询信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public InfoPatient queryRegisterInfo(String idcardNo) {
		InfoPatient infoPatient = registerInfoDAO.queryRegisterInfo(idcardNo);
		return infoPatient;
	}
	/**  
	 * @Description：  根据患者身份证号查询是否有预约
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterPreregister findPreregister(String patientLinkdoorno) {
		RegisterPreregister preregister = registerInfoDAO.findPreregister(patientLinkdoorno);
		return preregister;
	}
	/**  
	 * @Description：  统计
	 * @Author：ldl
	 * @CreateDate：2015-11-18 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public InfoStatistics queryStatistics(String empId,String midday) {
		InfoStatistics infoStatistics = registerInfoDAO.queryStatistics(empId,midday);
		return infoStatistics;
	}
	/**  
	 * @Description：  统计医生加号人数
	 * @Author：ldl
	 * @CreateDate：2015-11-18 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterInfo findInfoAdd(String empId) {
		RegisterInfo registerInfo = registerInfoDAO.findInfoAdd(empId);
		return registerInfo;
	}
	/**  
	 * @Description：  查看历史信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public List<RegisterInfo> findInfoHisList(String idcardNo) {
		List<RegisterInfo> lst = registerInfoDAO.findInfoHisList(idcardNo);
		return lst;
	}
	/**  
	 * @Description：  验证
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	@Override
	public RegisterInfo findInfoVo(String deptId, String empId, String gradeId,String midday,String blhcs) {
		return registerInfoDAO.findInfoVo(deptId,empId,gradeId, midday,blhcs);
	}

	@Override
	public Map<String, String> getGradeMap() {
		Map<String, String> gradeMap = new HashMap<String, String>();
		List<RegisterGrade> list = registerInfoDAO.gradeCombobox(null);
		if(list!=null&&list.size()>0){
			for(RegisterGrade grade:list){
				gradeMap.put(grade.getCode(), grade.getName());
			}
		}
		return gradeMap;
	}
	/**  
	 * @Description：  查询预约列表
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	public List<RegisterPreregister> findPreregisterList(String preregisterNo, String preregisterCertificatesno, String preregisterName, String preDate, String phone) throws ParseException {
		List<RegisterPreregister> lst = registerInfoDAO.findPreregisterList(preregisterNo,preregisterCertificatesno,preregisterName,preDate,phone);
		return lst;
	}
	
	@Override
	public String saveOrUpdateInfo(RegisterInfo entity,OutpatientAccount account,String no) {
		if("4".equals(entity.getPayType())){
			account.setAccountBalance(account.getAccountBalance()-Double.valueOf(entity.getFee()));
			registerInfoDAO.save(account);
			
			//添加门诊账户明细表
			OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
			accountrecord.setId(null);//ID
			accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
			accountrecord.setAccountId(account.getId());//门诊账户编号
			accountrecord.setOpertype(4);//操作类型
			accountrecord.setMoney(-Double.valueOf(entity.getFee()));//交易金额
			accountrecord.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//相关科室
			accountrecord.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员 
			accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
			accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
			accountrecord.setValid(0);//是否有效
			accountrecord.setInvoiceType("402880a54e3e0568014e3e06b3580001");//发票类型
			accountrecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			accountrecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			accountrecord.setCreateTime(DateUtils.getCurrentTime());
			accountrecord.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			accountrecord.setUpdateTime(DateUtils.getCurrentTime());
			registerInfoDAO.save(accountrecord);
			
		}
		
		//系统参数是否保存发票信息
		HospitalParameter parameter = registerInfoDAO.invocePemen();
		//根据医生和午别查询出当前挂号序号
		RegisterInfo info = registerInfoDAO.queryInfoByOrder(entity.getExpxrt(),entity.getMidday());
		//查询序列得出序号
		String invoiceSeq = registerInfoDAO.getSequece("SEQ_INVOICE_SEQ");
		//发票序号
		invoiceSeq = String.format("%14s",invoiceSeq).replaceAll("\\s","0");
		if(info.getOrder()==null){
			entity.setOrder(1);
			entity.setSeeOptimize(1);
		}else{
			entity.setOrder(info.getOrder()+1);
			entity.setSeeOptimize(info.getOrder()+1);
		}
		Integer value = registerInfoDAO.keyvalueDAO();
		entity.setId(null);
		entity.setPayType(entity.getPayType());
		entity.setBank(entity.getBank());
		entity.setBankAccount(entity.getBankAccount());
		entity.setBankBillno(entity.getBankBillno());
		entity.setBankUnit(entity.getBankUnit());
		entity.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		entity.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		entity.setCreateTime(DateUtils.getCurrentTime());
		entity.setDate(DateUtils.getCurrentTime());
		Patient patient = new Patient();
		patient.setId(entity.getPatientIds());
		entity.setPatientId(patient);
		entity.setInvoiceNo(no);
		registerInfoDAO.save(entity);
		String  id = entity.getId();
		//修改发票号
		SysEmployee employee = registerInfoDAO.querySysEmployeeId(ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
		String invoiceType = "402880a54e3e0568014e3e06b3580001";//门诊发票类型
		registerInfoDAO.updateFinanceInvoice(employee.getId(),invoiceType,no);
		
		//添加发票使用记录
		InvoiceUsageRecord usageRecord = new InvoiceUsageRecord();
		usageRecord.setId(null);
		usageRecord.setUserId(employee.getId());
		usageRecord.setUserCode(employee.getCode());
		usageRecord.setUserType(2);
		usageRecord.setInvoiceNo(no);
		usageRecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		usageRecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		usageRecord.setCreateTime(DateUtils.getCurrentTime());
		usageRecord.setUserName(employee.getName());
		usageRecord.setInvoiceUsestate(1);
		registerInfoDAO.save(usageRecord);
		
		
		if(!"0".equals(parameter.getParameterValue())){
			//添加发票明细表
			FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
			invoicedetail.setId(null);//ID
			invoicedetail.setInvoiceNo(no);//发票号
			invoicedetail.setTransType(1);//正反类型1正2反
			invoicedetail.setInvoSequence("1");//发票号内流水号
			invoicedetail.setInvoCode("402880a75398d798015398da0baa0002");//发票科目代码
			invoicedetail.setInvoName("挂号费");//发票科目名称
			invoicedetail.setPubCost(0.00);//可报效金额
			invoicedetail.setOwnCost(0.00);//不可报效金额
			invoicedetail.setPayCost(entity.getFee());//自付金额
			invoicedetail.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//执行科室ID
			invoicedetail.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//执行科室名称
			invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
		//	invoicedetail.setOperCode(userId);//操作人
			invoicedetail.setBalanceFlag(0);//日结状态
			invoicedetail.setBalanceNo(null);//日结标识号
			invoicedetail.setBalanceOpcd(null);//日结人
			invoicedetail.setBalanceDate(null);//日结时间
			invoicedetail.setCancelFlag(1);//状态
			invoicedetail.setInvoiceSeq(invoiceSeq);//发票序号
			invoicedetail.setInvoSequence("1");//发票内流水号
			invoicedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			invoicedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			invoicedetail.setCreateTime(DateUtils.getCurrentTime());
			invoicedetailInfoDAO.save(invoicedetail);
			
			//结算信息表
			FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
			invoiceInfo.setId(null);
			invoiceInfo.setInvoiceNo(no);//发票号
			invoiceInfo.setTransType(1);//交易类型
			invoiceInfo.setCardNo(entity.getMidicalrecordId());//病历号
			invoiceInfo.setRegDate(entity.getDate());//挂号日期
			invoiceInfo.setName(entity.getPatientId().getPatientName());//患者姓名
			invoiceInfo.setPaykindCode(null);//结算类别
			invoiceInfo.setPactCode(entity.getContractunit());//合同单位代码
			invoiceInfo.setPactName(entity.getContractunitName());//合同单位名称
			invoiceInfo.setMcardNo(null);//个人编号
			invoiceInfo.setMedicalType(null);//医疗类别
			invoiceInfo.setTotCost(entity.getFee());//总金额
			invoiceInfo.setPubCost(null);//可报效金额
			invoiceInfo.setOwnCost(null);//不可报效金额
			invoiceInfo.setPayCost(entity.getFee());//自付金额
			invoiceInfo.setBack1(null);//预留1
			invoiceInfo.setBack2(null);//预留2
			invoiceInfo.setBack3(null);//预留3
			invoiceInfo.setRealCost(entity.getFee());//实付金额
			invoiceInfo.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
			invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
			invoiceInfo.setExamineFlag(0);//团体/个人
			invoiceInfo.setCancelFlag(1);//有效
			invoiceInfo.setCancelInvoice(null);//作废票据号
			invoiceInfo.setCancelCode(null);//作废操作员
			invoiceInfo.setCancelDate(null);//作废时间
			invoiceInfo.setInvoiceSeq(invoiceSeq);//发票序号
			invoiceInfo.setInvoiceComb("");//一次收费序号
			invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
			invoiceRegisterDAO.save(invoiceInfo);
			
			//结算支付情况表
			BusinessPayMode payMode = new BusinessPayMode();
			payMode.setId(null);//id
			payMode.setInvoiceNo(no);//发票号
			payMode.setTransType(1);//交易类型
			payMode.setSequenceNo(1);//交易流水号
			payMode.setModeCode(entity.getPayType());//支付方式
			payMode.setTotCost(entity.getFee());//应付金额
			payMode.setRealCost(entity.getFee());//实付金额
			payMode.setBankCode(null);//开户银行代码
			payMode.setBankName(null);//开户银行名称
			payMode.setAccount("");//账号
			payMode.setPosNo("");//pos机号
			payMode.setCheckNo("");//支票号
			payMode.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
			payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
			payMode.setCheckFlag(0);///**0未核查/1已核查**/
			payMode.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
			payMode.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			payMode.setCreateTime(DateUtils.getCurrentTime());
			payModeInfoDAO.save(payMode);
		}
		//预约挂号修改
		if(StringUtils.isNotBlank(entity.getPreregisterNo())){
			registerInfoDAO.updatePreregister(entity.getPreregisterNo());
		}
		
		return id;
	}
	/**  
	 * @Description：  查询预约回显
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	public Patient queryPreregisterCertificatesno(String preregisterCertificatesno) {
		Patient patientList = registerInfoDAO.queryPreregisterCertificatesno(preregisterCertificatesno);
		return patientList;
	}
	/**  
	 * @Description：  判断是否存在就诊卡号
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public InfoPatient judgeIdcrad(String preNo) {
		InfoPatient patientIdcard = registerInfoDAO.judgeIdcrad(preNo);
		return patientIdcard;
	}
	/**  
	 * @Description：  退号信息列表查询
	 * @Author：ldl
	 * @CreateDate：2015-11-25
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<RegisterInfo> queryBackNo(String idcardId) {
		List<RegisterInfo> lst = registerInfoDAO.queryBackNo(idcardId);
		return lst;
	}
	/**  
	 * @Description：  退号
	 * @Author：ldl
	 * @CreateDate：2015-11-25 
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Override
	public Map<String,String> saveOrUpdateInfoBack(RegisterInfo entity,String quitreason,String payType) {
		Map<String,String> map = new HashMap<String, String>();
		//扣患者账户
		if("2".equals(payType)){
			OutpatientAccount account = registerInfoDAO.getAccountByMedicalrecord(entity.getMidicalrecordId());
			if(StringUtils.isBlank(account.getId())){
				map.put("resMsg", "error");
				map.put("resCode", "此患者无账户信息,请联系管理员!");
			}else if(account.getAccountState()==0){
				map.put("resMsg", "error");
				map.put("resCode", "账户已停用,请联系管理员!");
			}else if(account.getAccountState()==2){
				map.put("resMsg", "error");
				map.put("resCode", "账户已注销!");
			}else{
				account.setAccountBalance(account.getAccountBalance()+Double.valueOf(entity.getFee()));
				registerInfoDAO.save(account);
				
				//添加门诊账户明细表
				OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
				accountrecord.setId(null);//ID
				accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
				accountrecord.setAccountId(account.getId());//门诊账户编号
				accountrecord.setOpertype(5);//操作类型
				accountrecord.setMoney(Double.valueOf(entity.getFee()));//交易金额
				accountrecord.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//相关科室
				accountrecord.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员 
				accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
				accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
				accountrecord.setValid(0);//是否有效
				accountrecord.setInvoiceType("402880a54e3e0568014e3e06b3580001");//发票类型
				accountrecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				accountrecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				accountrecord.setCreateTime(DateUtils.getCurrentTime());
				accountrecord.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				accountrecord.setUpdateTime(DateUtils.getCurrentTime());
				registerInfoDAO.save(accountrecord);
				
				entity.setUpdateTime(DateUtils.getCurrentTime());
				entity.setStatus(0);
				entity.setQuitreason(quitreason);
				registerInfoDAO.update(entity);
				OperationUtils.getInstance().conserve(entity.getId(),"挂号信息","UPDATE","T_HOSPITAL",OperationUtils.LOGACTIONUPDATE);
				
				//添加发票明细表
				FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
				invoicedetail.setId(null);//ID
				invoicedetail.setInvoiceNo(entity.getInvoiceNo());//发票号
				invoicedetail.setTransType(2);//正反类型1正2反
				invoicedetail.setInvoSequence("1");//发票号内流水号
				invoicedetail.setInvoCode("402880a75398d798015398da0baa0002");//发票科目代码
				invoicedetail.setInvoName("挂号费");//发票科目名称
				invoicedetail.setPubCost(0.00);//可报效金额
				invoicedetail.setOwnCost(0.00);//不可报效金额
				invoicedetail.setPayCost(entity.getFee());//自付金额
				invoicedetail.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//执行科室ID
				invoicedetail.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//执行科室名称
				invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
			//	invoicedetail.setOperCode(userId);//操作人
				invoicedetail.setBalanceFlag(0);//日结状态
				invoicedetail.setBalanceNo(null);//日结标识号
				invoicedetail.setBalanceOpcd(null);//日结人
				invoicedetail.setBalanceDate(null);//日结时间
				invoicedetail.setCancelFlag(1);//状态
				invoicedetail.setInvoiceSeq("");//发票序号
				invoicedetail.setInvoSequence("1");//发票内流水号
				invoicedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				invoicedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				invoicedetail.setCreateTime(DateUtils.getCurrentTime());
				invoicedetailInfoDAO.save(invoicedetail);
				
				//结算信息表
				FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
				invoiceInfo.setId(null);
				invoiceInfo.setInvoiceNo(entity.getInvoiceNo());//发票号
				invoiceInfo.setTransType(2);//交易类型
				invoiceInfo.setCardNo(entity.getMidicalrecordId());//病历号
				invoiceInfo.setRegDate(entity.getDate());//挂号日期
				invoiceInfo.setName(entity.getPatientId().getPatientName());//患者姓名
				invoiceInfo.setPaykindCode(null);//结算类别
				invoiceInfo.setPactCode(entity.getContractunit());//合同单位代码
				invoiceInfo.setPactName(entity.getContractunitName());//合同单位名称
				invoiceInfo.setMcardNo(null);//个人编号
				invoiceInfo.setMedicalType(null);//医疗类别
				invoiceInfo.setTotCost(entity.getFee());//总金额
				invoiceInfo.setPubCost(null);//可报效金额
				invoiceInfo.setOwnCost(null);//不可报效金额
				invoiceInfo.setPayCost(entity.getFee());//自付金额
				invoiceInfo.setBack1(null);//预留1
				invoiceInfo.setBack2(null);//预留2
				invoiceInfo.setBack3(null);//预留3
				invoiceInfo.setRealCost(entity.getFee());//实付金额
				invoiceInfo.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
				invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
				invoiceInfo.setExamineFlag(0);//团体/个人
				invoiceInfo.setCancelFlag(1);//有效
				invoiceInfo.setCancelInvoice(null);//作废票据号
				invoiceInfo.setCancelCode(null);//作废操作员
				invoiceInfo.setCancelDate(null);//作废时间
				invoiceInfo.setInvoiceSeq("");//发票序号
				invoiceInfo.setInvoiceComb("");//一次收费序号
				invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
				invoiceRegisterDAO.save(invoiceInfo);
				
				//结算支付情况表
				BusinessPayMode payMode = new BusinessPayMode();
				payMode.setId(null);//id
				payMode.setInvoiceNo(entity.getInvoiceNo());//发票号
				payMode.setTransType(2);//交易类型
				payMode.setSequenceNo(1);//交易流水号
				payMode.setModeCode(payType);//支付方式
				payMode.setTotCost(entity.getFee());//应付金额
				payMode.setRealCost(entity.getFee());//实付金额
				payMode.setBankCode(null);//开户银行代码
				payMode.setBankName(null);//开户银行名称
				payMode.setAccount("");//账号
				payMode.setPosNo("");//pos机号
				payMode.setCheckNo("");//支票号
				payMode.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
				payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
				payMode.setCheckFlag(0);///**0未核查/1已核查**/
				payModeInfoDAO.save(payMode);
				
				
				
				//查询计算记录
				List<RegisterDaybalance> daybalance = registerInfoDAO.querydaybalan();
				if(daybalance!=null&&daybalance.size()>0){
					for (RegisterDaybalance modl : daybalance) {
						if(modl.getEndTime().after(entity.getDate())&&modl.getStartTime().before(entity.getDate())){
							//查询挂号员详情表
							List<RegisterBalancedetail> balancedetail = registerInfoDAO.querybalan(modl.getId(),entity.getPayType());
							if(balancedetail!=null&&balancedetail.size()>0){
								for (RegisterBalancedetail modls : balancedetail) {
									modls.setQuitNum(modls.getQuitNum()+1);
									modls.setQuitFee(modls.getQuitFee()+entity.getFee());
									modls.setSumNum(modls.getRegNum()-modls.getQuitNum());
									modls.setSumFee(modls.getRegFee()-modls.getQuitFee());
									registerInfoDAO.updateBalan(modls);
								}
							}
						}
					}
				}
				map.put("resMsg", "success");
				map.put("resCode", "退号成功");
			}
		}else if("1".equals(payType)){
			entity.setUpdateTime(DateUtils.getCurrentTime());
			entity.setStatus(0);
			entity.setQuitreason(quitreason);
			registerInfoDAO.update(entity);
			OperationUtils.getInstance().conserve(entity.getId(),"挂号信息","UPDATE","T_HOSPITAL",OperationUtils.LOGACTIONUPDATE);
			
			//添加发票明细表
			FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
			invoicedetail.setId(null);//ID
			invoicedetail.setInvoiceNo(entity.getInvoiceNo());//发票号
			invoicedetail.setTransType(2);//正反类型1正2反
			invoicedetail.setInvoSequence("1");//发票号内流水号
			invoicedetail.setInvoCode("402880a75398d798015398da0baa0002");//发票科目代码
			invoicedetail.setInvoName("挂号费");//发票科目名称
			invoicedetail.setPubCost(0.00);//可报效金额
			invoicedetail.setOwnCost(0.00);//不可报效金额
			invoicedetail.setPayCost(entity.getFee());//自付金额
			invoicedetail.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//执行科室ID
			invoicedetail.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());//执行科室名称
			invoicedetail.setOperDate(DateUtils.getCurrentTime());//操作时间
			invoicedetail.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作人
			invoicedetail.setBalanceFlag(0);//日结状态
			invoicedetail.setBalanceNo(null);//日结标识号
			invoicedetail.setBalanceOpcd(null);//日结人
			invoicedetail.setBalanceDate(null);//日结时间
			invoicedetail.setCancelFlag(1);//状态
			invoicedetail.setInvoiceSeq("");//发票序号
			invoicedetail.setInvoSequence("1");//发票内流水号
			invoicedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			invoicedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			invoicedetail.setCreateTime(DateUtils.getCurrentTime());
			invoicedetailInfoDAO.save(invoicedetail);
			
			//结算信息表
			FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
			invoiceInfo.setId(null);
			invoiceInfo.setInvoiceNo(entity.getInvoiceNo());//发票号
			invoiceInfo.setTransType(2);//交易类型
			invoiceInfo.setCardNo(entity.getMidicalrecordId());//病历号
			invoiceInfo.setRegDate(entity.getDate());//挂号日期
			invoiceInfo.setName(entity.getPatientId().getPatientName());//患者姓名
			invoiceInfo.setPaykindCode(null);//结算类别
			invoiceInfo.setPactCode(entity.getContractunit());//合同单位代码
			invoiceInfo.setPactName(entity.getContractunitName());//合同单位名称
			invoiceInfo.setMcardNo(null);//个人编号
			invoiceInfo.setMedicalType(null);//医疗类别
			invoiceInfo.setTotCost(entity.getFee());//总金额
			invoiceInfo.setPubCost(null);//可报效金额
			invoiceInfo.setOwnCost(null);//不可报效金额
			invoiceInfo.setPayCost(entity.getFee());//自付金额
			invoiceInfo.setBack1(null);//预留1
			invoiceInfo.setBack2(null);//预留2
			invoiceInfo.setBack3(null);//预留3
			invoiceInfo.setRealCost(entity.getFee());//实付金额
			invoiceInfo.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
			invoiceInfo.setOperDate(DateUtils.getCurrentTime());//结算时间
			invoiceInfo.setExamineFlag(0);//团体/个人
			invoiceInfo.setCancelFlag(1);//有效
			invoiceInfo.setCancelInvoice(null);//作废票据号
			invoiceInfo.setCancelCode(null);//作废操作员
			invoiceInfo.setCancelDate(null);//作废时间
			invoiceInfo.setInvoiceSeq("");//发票序号
			invoiceInfo.setInvoiceComb("");//一次收费序号
			invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			invoiceInfo.setCreateTime(DateUtils.getCurrentTime());
			invoiceRegisterDAO.save(invoiceInfo);
			
			//结算支付情况表
			BusinessPayMode payMode = new BusinessPayMode();
			payMode.setId(null);//id
			payMode.setInvoiceNo(entity.getInvoiceNo());//发票号
			payMode.setTransType(2);//交易类型
			payMode.setSequenceNo(1);//交易流水号
			payMode.setModeCode(payType);//支付方式
			payMode.setTotCost(entity.getFee());//应付金额
			payMode.setRealCost(entity.getFee());//实付金额
			payMode.setBankCode(null);//开户银行代码
			payMode.setBankName(null);//开户银行名称
			payMode.setAccount("");//账号
			payMode.setPosNo("");//pos机号
			payMode.setCheckNo("");//支票号
			payMode.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//结算人
			payMode.setOperDate(DateUtils.getCurrentTime());//结算时间
			payMode.setCheckFlag(0);///**0未核查/1已核查**/
			payModeInfoDAO.save(payMode);
			
			
			
			//查询计算记录
			List<RegisterDaybalance> daybalance = registerInfoDAO.querydaybalan();
			if(daybalance!=null&&daybalance.size()>0){
				for (RegisterDaybalance modl : daybalance) {
					if(modl.getEndTime().after(entity.getDate())&&modl.getStartTime().before(entity.getDate())){
						//查询挂号员详情表
						List<RegisterBalancedetail> balancedetail = registerInfoDAO.querybalan(modl.getId(),entity.getPayType());
						if(balancedetail!=null&&balancedetail.size()>0){
							for (RegisterBalancedetail modls : balancedetail) {
								modls.setQuitNum(modls.getQuitNum()+1);
								modls.setQuitFee(modls.getQuitFee()+entity.getFee());
								modls.setSumNum(modls.getRegNum()-modls.getQuitNum());
								modls.setSumFee(modls.getRegFee()-modls.getQuitFee());
								registerInfoDAO.updateBalan(modls);
							}
						}
					}
				}
			}
			map.put("resMsg", "success");
			map.put("resCode", "退号成功");
		}
		return map;
	}
	/**  
	 * @Description：  特诊挂号费
	 * @Author：ldl
	 * @CreateDate：2015-12-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	@Override
	public HospitalParameter speciallimitInfo(String speciallimitInfo) {
		HospitalParameter parameter = registerInfoDAO.speciallimitInfo(speciallimitInfo);
		return parameter;
	}

	@Override
	public RegisterInfo getInfoByClinicCode(String clinicCode) {
		
		return registerInfoDAO.getInfoByClinicCode(clinicCode);
	}

	@Override
	public List<RegisterInfo> getInfo(String no) {
		return registerInfoDAO.getInfo(no);
	}

	@Override
	public SysEmployee querySysEmployeeId(String id) {
		return registerInfoDAO.querySysEmployeeId(id);
	}

	@Override
	public Map<String, String> queryFinanceInvoiceNo() {
		SysEmployee employee = registerInfoDAO.querySysEmployeeId(ShiroSessionUtils.getCurrentUserFromShiroSession().getId());
		String invoiceType = "402880a54e3e0568014e3e06b3580001";//门诊发票类型
		Map<String,String> map=new HashMap<String,String>();
		map = registerInfoDAO.queryFinanceInvoiceNo(employee.getId(),invoiceType);//获得领取发票的那一条信息
		return map;
	}

	@Override
	public void iReportUpdate(RegisterInfo info) {
		info.setInvoiceprintflag(1);
		registerInfoDAO.save(info);
	}

	@Override
	public PatientBlackList queryBlackList(String infoMedicalrecordId) {
		PatientBlackList blackList =  registerInfoDAO.queryBlackList(infoMedicalrecordId);
		return blackList;
	}

	@Override
	public HospitalParameter changePay() {
		HospitalParameter parameter = registerInfoDAO.changePay();
		return parameter;
	}

	@Override
	public HospitalParameter invocePemen() {
		HospitalParameter parameter = registerInfoDAO.invocePemen();//系统参数是否保存发票信息
		return parameter;
	}

	@Override
	public OutpatientAccount getAccountByMedicalrecord(String midicalrecordId) {
		return registerInfoDAO.getAccountByMedicalrecord(midicalrecordId);
	}

	@Override
	public List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId) {
		return registerInfoDAO.queryAccountrecord(midicalrecordId);
	}

	@Override
	public OutpatientAccount veriPassWord(String md5Hex, String blhcs) {
		return registerInfoDAO.veriPassWord(md5Hex,blhcs);
	}


}
