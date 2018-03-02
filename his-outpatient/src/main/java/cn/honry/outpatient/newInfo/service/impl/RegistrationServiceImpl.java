package cn.honry.outpatient.newInfo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.Hospital;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.outpatient.grade.service.GradeInInterService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.newChangeDeptLog.dao.ChangeDeptDAO;
import cn.honry.outpatient.newInfo.dao.RegistrationDAO;
import cn.honry.outpatient.newInfo.service.RegistrationService;
import cn.honry.outpatient.newInfo.vo.EmpInfoVo;
import cn.honry.outpatient.newInfo.vo.HospitalVo;
import cn.honry.outpatient.newInfo.vo.InfoStatistics;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.outpatient.newInfo.vo.RegPrintVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;

@Service("ationService")
@Transactional(rollbackFor={Throwable.class})
@SuppressWarnings({ "all" })
public class RegistrationServiceImpl implements RegistrationService{
	
	@Resource
	private RedisUtil redis;
	
	/** 挂号数据库操作类 **/
	@Autowired
	@Qualifier(value = "ationDAO")
	private RegistrationDAO ationDAO;
	@Autowired
	@Qualifier(value="innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService codeInInterService;
	public void setCodeInInterService(CodeInInterService codeInInterService) {
		this.codeInInterService = codeInInterService;
	}
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	public void setEmployeeInInterService(EmployeeInInterService employeeInInterService) {
		this.employeeInInterService = employeeInInterService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	@Autowired
	@Qualifier(value = "gradeInInterService")
	private GradeInInterService gradeInInterService;
	public void setGradeInInterService(GradeInInterService gradeInInterService) {
		this.gradeInInterService = gradeInInterService;
	}

	@Autowired
	@Qualifier(value = "changeDeptDAO")
	private ChangeDeptDAO changeDeptDAO;
	
	

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public RegistrationNow get(String id) {
		return ationDAO.get(id);
	}



	@Override
	public void saveOrUpdate(RegistrationNow id) {
	}

	@Override
	public List<RegisterGrade> gradeCombobox(String q) {
		return ationDAO.gradeCombobox(q);
	}

	@Override
	public List<SysDepartment> deptCombobox(String q) {
		if(StringUtils.isBlank(q)){
			List<SysDepartment> list= (List<SysDepartment>) redis.get("dept_queryAll");
			if(list!=null && list.size()>0){
				return list;
			}
		}
		return ationDAO.deptCombobox(q);
	}

	@Override
	public List<EmpInfoVo> empCombobox(String deptCode, String reglevlCode,Integer noonCode,String q) {
		return ationDAO.empCombobox(deptCode,reglevlCode,noonCode,q);
	}

	@Override
	public List<BusinessContractunit> contCombobox(String q) {
		if(StringUtils.isBlank(q)){
			List<BusinessContractunit> list=(List<BusinessContractunit>) redis.get("contractunit_queryAll");
			if(list!=null){
				return list;
			}
		}
		return ationDAO.contCombobox(q);
	}


	@Override
	public List<InfoVo> findInfoList(String deptCode, String doctCode,String reglevlCode, Integer noonCode) {
		if(noonCode==null){
			int hour = DateUtils.getHour();
			if(hour>=8 && hour<12){
				noonCode=1;
			}else if(hour>=12 && hour<20){
				noonCode=2;
			}else{
				noonCode=3;
			}
		}
		return ationDAO.findInfoList(deptCode,doctCode,reglevlCode,noonCode);
	}

	@Override
	public InfoStatistics queryStatistics(String doctCode, Integer noonCode) {
		return ationDAO.queryStatistics(doctCode,noonCode);
	}

	@Override
	public RegistrationNow queryRegistrationByEmp(String doctCode) {
		return ationDAO.queryRegistrationByEmp(doctCode);
	}

	@Override
	public HospitalParameter speciallimitInfo(String speciallimitInfo) {
		return ationDAO.speciallimitInfo(speciallimitInfo);
	}

	@Override
	public RegisterFee feeCombobox(String pactCode, String reglevlCode) {
		return ationDAO.feeCombobox(pactCode,reglevlCode);
	}

	@Override
	public Map<String, Object> queryRegisterInfo(String cardNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		//跟就就诊号查询该就诊卡号是否存在，是否有效
		PatientIdcard patientIdcard = ationDAO.queryPatientIdcard(cardNo);
		if(StringUtils.isBlank(patientIdcard.getId())){
			map.put("resMsg", "error");
			map.put("resCode", "此卡号不存在，请确认后输入");
		}else if(patientIdcard.getIdcardStatus()==2){
			map.put("resMsg", "error");
			map.put("resCode", "该卡号已经停用");
		}else if(patientIdcard.getIdcardStatus()==3){
			map.put("resMsg", "error");
			map.put("resCode", "该卡号已经失效");
		}else{
			//判断该患者在不在黑名单中
			PatientBlackList blackList = ationDAO.queryBlackList(patientIdcard.getPatient().getId());
			if(StringUtils.isNotBlank(blackList.getId())){
				map.put("resMsg", "error");
				map.put("resCode", "该患者已经在黑名单中，请联系工作人员");
			}else{
				RegisterPreregisterNow preregister = ationDAO.findPreregister(patientIdcard.getPatient().getPatientCertificatesno());
				if(StringUtils.isNotBlank(preregister.getId())){
					map.put("resMsg", "success");
					map.put("preregister", preregister.getPreregisterNo());
				}else{
					map.put("resMsg", "succ");
					map.put("preregister", preregister.getPreregisterNo());
				}
				map.put("patient", patientIdcard.getPatient());
				map.put("cardId", patientIdcard.getId());
			}
		}
		return map;
	}

	@Override
	public List<SysDepartment> querydeptComboboxs() {
		List<SysDepartment> list = (List<SysDepartment>) redis.get("dept_queryAll");
		if(list!=null && list.size()>0){
			return list;
		}
		return ationDAO.querydeptComboboxs();
	}

	@Override
	public List<RegisterGrade> querygradeComboboxs() {
		return ationDAO.querygradeComboboxs();
	}

	@Override
	public List<SysEmployee> queryempComboboxs() {
		List<SysEmployee> list = (List<SysEmployee>) redis.get("emp_queryAll");
		if(list!=null && list.size()>0){
			return list;
		}
		return ationDAO.queryempComboboxs();
	}

	@Override
	public List<RegistrationNow> findInfoHisList(String cardNo) {
		return ationDAO.findInfoHisList(cardNo);
	}
	
	@Override
	public Map<String, String> saveInfo(RegistrationNow ationInfo,String docSource)throws Exception {
			//1.判断有没有发票
			String id = "";//返回的ID
			Map<String,String> map = new  HashMap<String,String>();
			Map<String,String> mapinfo = new  HashMap<String,String>();
			OutpatientAccount account = new OutpatientAccount();
			String invoiceType = "01";//门诊发票类型
			String accountNo = null;
			
			map = ationDAO.queryFinanceInvoiceNo(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),invoiceType);
			if("error".equals(map.get("resMsg"))){
				mapinfo.put("resMsg", map.get("resMsg"));
				mapinfo.put("resCode", map.get("resCode"));
				return mapinfo;
			}else{
			//2.判断支付方式，如果是院内账户支付需要判断有效性
				String no = map.get("resCode");//发票号
				if("YS".equals(ationInfo.getPayType())){
					account = ationDAO.queryAccountByMedicalrecord(ationInfo.getMidicalrecordId());
					accountNo = account.getAccountName();
					if(account.getId()==null){
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "该病历号无账户信息,请联系管理员!");
						return mapinfo;
					}else if(account.getAccountState()==0){//停用
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "账户已停用,请联系管理员!");
						return mapinfo;
					}else if(account.getAccountState()==2){//注销3结清4冻结
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "账户已注销!");
						return mapinfo;
					}else if(account.getAccountState()==1&&Double.valueOf(ationInfo.getSumCost())>account.getAccountBalance()){//总金额大于剩余的门诊金额无法结账
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "账户剩余金额["+account.getAccountBalance()+"],请充值缴费后结算!");
						return mapinfo;
					}else if(account.getAccountDaylimit()<Double.valueOf(ationInfo.getSumCost())){
						mapinfo.put("resMsg", "error");
						mapinfo.put("resCode", "已超出单日消费限额:"+account.getAccountDaylimit()+"元!");
						return mapinfo;
					}else{
						List<OutpatientAccount> accountrecords = ationDAO.queryAccountrecord(ationInfo.getMidicalrecordId());
						Double sum = 0.0;//已使用金额
						if(accountrecords.size()>0){
								if(ationInfo.getBookFee()!=0){
									 account.setAccountBalance(account.getAccountBalance()-(ationInfo.getSumCost()+ationInfo.getBookFee()));
								}else{
									account.setAccountBalance(account.getAccountBalance()-ationInfo.getSumCost());
								}
							    
								ationDAO.save(account);
								
								//添加门诊账户明细表
								OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
								accountrecord.setId(null);//ID
								accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
								accountrecord.setAccountId(account.getId());//门诊账户编号
								accountrecord.setOpertype(4);//操作类型
								if(ationInfo.getBookFee()!=0){
									accountrecord.setMoney(-(ationInfo.getSumCost()+ationInfo.getBookFee()));//交易金额
								}else{
									accountrecord.setMoney(-ationInfo.getSumCost());//交易金额
								}
								accountrecord.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//相关科室
								accountrecord.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员 
								accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
								accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
								accountrecord.setValid(0);//是否有效
								accountrecord.setInvoiceType("01");//发票类型
								accountrecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
								accountrecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
								accountrecord.setCreateTime(DateUtils.getCurrentTime());
								accountrecord.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
								accountrecord.setUpdateTime(DateUtils.getCurrentTime());
								ationDAO.save(accountrecord);
						}
					}
				}
				//更新医生号源表已挂人数 docSource;
				RegisterDocSource info = ationDAO.findNewInfoList(null, null, null, null, docSource).get(0);
				if(ationInfo.getYnbook()!=null && "02".equals(ationInfo.getYnbook())){//患者有预约
					info.setPreclinicSum(info.getPreclinicSum()+1);//已取号人数+1
				}else{
					info.setClinicSum(info.getClinicSum()+1);//已挂人数+1
				}
				ationDAO.update(info);
			}
			
			//系统参数是否保存发票信息
			HospitalParameter parameter = ationDAO.invocePemen();
			//根据医生和午别查询出当前挂号序号
			RegistrationNow info = ationDAO.queryInfoByOrder(ationInfo.getDoctCode(),ationInfo.getNoonCode());
			if(info.getOrderNo()==null){
				ationInfo.setOrderNo(1);
				
			}else{
				ationInfo.setOrderNo(info.getOrderNo()+1);
			}
			//ADDRESS,IS_ACCOUNT=0,UP_FLAG=0,CREATEHOS,HOSPITAL_ID,AREA_CODE,DEPT_CODE
			ationInfo.setSeeno(ationInfo.getOrderNo());//设置看诊序号
			ationInfo.setIsAccount(0);//账户流程标识1 账户挂号 0普通
			ationInfo.setUpFlag("0");//上传标记 0-未上传 1-上传
			if(StringUtils.isNotBlank(ationInfo.getDeptCode())){
				HospitalVo hospitalVo = ationDAO.queryHospitalInfo(ationInfo.getDeptCode());
				//设置建立医院
				ationInfo.setCreatehos(hospitalVo.getHospital_id().toString());
				//设置所属医院
				ationInfo.setHospitalId(hospitalVo.getHospital_id());
				//设置所属院区
				ationInfo.setAreaCode(hospitalVo.getDept_area_code());
			}
			String payKindCode = "";
			//结算类别
			if(ationInfo.getPactCode()!=null&&ationInfo.getPactCode()!=""){
				List<BusinessContractunit> businessContractunitList = ationDAO.querypackCode(ationInfo.getPactCode());
				ationInfo.setPaykindCode(businessContractunitList.get(0).getPaykindCode());
				payKindCode = businessContractunitList.get(0).getPaykindCode();
				if("01".equals(businessContractunitList.get(0).getPaykindCode())){
					ationInfo.setPaykindName("自费");
				}else if("02".equals(businessContractunitList.get(0).getPaykindCode())){
					ationInfo.setPaykindName("保险");
				}else if("03".equals(businessContractunitList.get(0).getPaykindCode())){
					ationInfo.setPaykindName("公费在职");
				}else if("04".equals(businessContractunitList.get(0).getPaykindCode())){
					ationInfo.setPaykindName("公费退休");
				}else if("05".equals(businessContractunitList.get(0).getPaykindCode())){
					ationInfo.setPaykindName("公费干部");
				}
			}
			Map<String,String> noonMap = codeInInterService.getBusDictionaryMap("midday");
			Map<String,String> sexMap = codeInInterService.getBusDictionaryMap("sex");
			ationInfo.setRecipeNo(ationDAO.getSequece("SEQ_ADVICE_RECIPENO"));//处方号
			Integer value = ationDAO.keyvalueDAO();
			ationInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			ationInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			ationInfo.setCreateTime(DateUtils.getCurrentTime());//创建时间
			ationInfo.setTransType(1);//交易类型
			ationInfo.setPayCost(0.00);//自付金额
			ationInfo.setRegDate(DateUtils.getCurrentTime());//挂号时间
			Patient patient = ationDAO.queryPatientByBLh(ationInfo.getMidicalrecordId());//查询患者信息
			ationInfo.setPatientName(patient.getPatientName());//患者姓名
			ationInfo.setPatientIdenno(patient.getPatientCertificatesno());//证件号
			ationInfo.setPatientSex(patient.getPatientSex());//性别
			ationInfo.setPatientBirthday(patient.getPatientBirthday());//生日
			ationInfo.setRelaPhone(patient.getPatientPhone());//电话
			ationInfo.setAddress(patient.getPatientAddress());//地址
			ationInfo.setInvoiceNo(map.get("resCode"));//发票号
			RegisterFee feeRegister = ationDAO.findFeeByfee(ationInfo.getPactCode(),ationInfo.getReglevlCode());
			ationInfo.setRegFee(feeRegister.getRegisterFee());//挂号费
			ationInfo.setRegFeeCode("20");//挂号费代码
			ationInfo.setChckFee(feeRegister.getCheckFee());//检查费
			ationInfo.setChckFeeCode("07");//检查费代码
			ationInfo.setDiagFee(feeRegister.getTreatmentFee());//诊察费
			ationInfo.setDiagFeeCode("16");//诊察费代码
			ationInfo.setOthFee(feeRegister.getOtherFee());//附加费
			ationInfo.setOthFeeCode("23");//附件费代码
			ationInfo.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员代码
			ationInfo.setOperDate(DateUtils.getCurrentTime());//操作时间
			ationInfo.setCardType(patient.getPatientCertificatestype());//证件类别
			ationInfo.setAccountNo(accountNo);//账户名称
			ationInfo.setUpdateTime(DateUtils.getCurrentTime());
			ationInfo.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			ationInfo.setNoonCodeNmae(noonMap.get(ationInfo.getNoonCode().toString()));
			if(ationInfo.getPatientSex()!=null){
				ationInfo.setPatientSexName(sexMap.get(ationInfo.getPatientSex().toString()));
			}
			if(ationInfo.getDeptName().contains("急诊")){
				ationInfo.setEmergency_flag(1);
			}else{
				ationInfo.setEmergency_flag(0);
			}
			BusinessDictionary triagelevel =  innerCodeDao.getDictionaryByName(HisParameters.TRIAGELEVEL,HisParameters.TRIAGELEVELPT);
			ationInfo.setSeeOptimize(Integer.valueOf(triagelevel.getEncode()));
			ationDAO.save(ationInfo);
			String  mainId = ationInfo.getId();
			//查询序列得出序号
			String invoiceSeq = ationDAO.getSequece("SEQ_INVOICE_SEQ");
			//发票序号
			invoiceSeq = String.format("%14s",invoiceSeq).replaceAll("\\s","0");
			FinanceInvoicedetailNow invoicedetail = new FinanceInvoicedetailNow();
			invoicedetail.setId(null);//ID 
			invoicedetail.setInvoiceNo(map.get("resCode"));/**发票号 **/
			invoicedetail.setTransType(1);/**交易类型,1正,2反 **/
			invoicedetail.setInvoSequence("1");/**发票内流水号 **/
			invoicedetail.setInvoCode("20");/**发票科目代码 **/
			invoicedetail.setInvoName("挂号费");/**发票科目名称 **/
			invoicedetail.setPubCost(0.0);/**可报效金额 **/
			invoicedetail.setOwnCost(0.0);/**不可报效金额  **/
			invoicedetail.setPayCost(feeRegister.getRegisterFee());/**自付金额 **/
			invoicedetail.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());/**执行科室 **/
			invoicedetail.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());/**执行科室名称 **/
			invoicedetail.setOperDate(DateUtils.getCurrentTime());/**操作时间 **/
			invoicedetail.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());/**操作员 **/
			invoicedetail.setBalanceFlag(0);/**0未日结/1已日结 **/
			invoicedetail.setCancelFlag(1);/**1正常，0作废，2重打，3注销 **/
			invoicedetail.setInvoiceSeq(invoiceSeq);
			invoicedetail.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			invoicedetail.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			invoicedetail.setCreateTime(DateUtils.getCurrentTime());//创建时间
			ationDAO.save(invoicedetail);
			
			FinanceInvoicedetailNow invoicedetail1 = new FinanceInvoicedetailNow();
			invoicedetail1.setId(null);//ID 
			invoicedetail1.setInvoiceNo(map.get("resCode"));/**发票号 **/
			invoicedetail1.setTransType(1);/**交易类型,1正,2反 **/
			invoicedetail1.setInvoSequence("2");/**发票内流水号 **/
			invoicedetail1.setInvoCode("07");/**发票科目代码 **/
			invoicedetail1.setInvoName("检查费");/**发票科目名称 **/
			invoicedetail1.setPubCost(0.0);/**可报效金额 **/
			invoicedetail1.setOwnCost(0.0);/**不可报效金额  **/
			invoicedetail1.setPayCost(feeRegister.getCheckFee());/**自付金额 **/
			invoicedetail1.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());/**执行科室 **/
			invoicedetail1.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());/**执行科室名称 **/
			invoicedetail1.setOperDate(DateUtils.getCurrentTime());/**操作时间 **/
			invoicedetail1.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());/**操作员 **/
			invoicedetail1.setBalanceFlag(0);/**0未日结/1已日结 **/
			invoicedetail1.setCancelFlag(1);/**1正常，0作废，2重打，3注销 **/
			invoicedetail1.setInvoiceSeq(invoiceSeq);
			invoicedetail1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			invoicedetail1.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			invoicedetail1.setCreateTime(DateUtils.getCurrentTime());//创建时间
			ationDAO.save(invoicedetail1);
			
			FinanceInvoicedetailNow invoicedetail2 = new FinanceInvoicedetailNow();
			invoicedetail2.setId(null);//ID 
			invoicedetail2.setInvoiceNo(map.get("resCode"));/**发票号 **/
			invoicedetail2.setTransType(1);/**交易类型,1正,2反 **/
			invoicedetail2.setInvoSequence("3");/**发票内流水号 **/
			invoicedetail2.setInvoCode("16");/**发票科目代码 **/
			invoicedetail2.setInvoName("诊察费");/**发票科目名称 **/
			invoicedetail2.setPubCost(0.0);/**可报效金额 **/
			invoicedetail2.setOwnCost(0.0);/**不可报效金额  **/
			invoicedetail2.setPayCost(feeRegister.getTreatmentFee());/**自付金额 **/
			invoicedetail2.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());/**执行科室 **/
			invoicedetail2.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());/**执行科室名称 **/
			invoicedetail2.setOperDate(DateUtils.getCurrentTime());/**操作时间 **/
			invoicedetail2.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());/**操作员 **/
			invoicedetail2.setBalanceFlag(0);/**0未日结/1已日结 **/
			invoicedetail2.setCancelFlag(1);/**1正常，0作废，2重打，3注销 **/
			invoicedetail2.setInvoiceSeq(invoiceSeq);
			invoicedetail2.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			invoicedetail2.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			invoicedetail2.setCreateTime(DateUtils.getCurrentTime());//创建时间
			ationDAO.save(invoicedetail2);
			
			FinanceInvoicedetailNow invoicedetail3 = new FinanceInvoicedetailNow();
			invoicedetail3.setId(null);//ID 
			invoicedetail3.setInvoiceNo(map.get("resCode"));/**发票号 **/
			invoicedetail3.setTransType(1);/**交易类型,1正,2反 **/
			invoicedetail3.setInvoSequence("4");/**发票内流水号 **/
			invoicedetail3.setInvoCode("17");/**发票科目代码 **/
			invoicedetail3.setInvoName("其他费");/**发票科目名称 **/
			invoicedetail3.setPubCost(0.0);/**可报效金额 **/
			invoicedetail3.setOwnCost(0.0);/**不可报效金额  **/
			invoicedetail3.setPayCost(feeRegister.getOtherFee());/**自付金额 **/
			invoicedetail3.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());/**执行科室 **/
			invoicedetail3.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());/**执行科室名称 **/
			invoicedetail3.setOperDate(DateUtils.getCurrentTime());/**操作时间 **/
			invoicedetail3.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());/**操作员 **/
			invoicedetail3.setBalanceFlag(0);/**0未日结/1已日结 **/
			invoicedetail3.setCancelFlag(1);/**1正常，0作废，2重打，3注销 **/
			invoicedetail3.setInvoiceSeq(invoiceSeq);
			invoicedetail3.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			invoicedetail3.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			invoicedetail3.setCreateTime(DateUtils.getCurrentTime());//创建时间
			ationDAO.save(invoicedetail3);
			
			FinanceInvoicedetailNow invoicedetail4 = new FinanceInvoicedetailNow();
			invoicedetail4.setId(null);//ID 
			invoicedetail4.setInvoiceNo(map.get("resCode"));/**发票号 **/
			invoicedetail4.setTransType(1);/**交易类型,1正,2反 **/
			invoicedetail4.setInvoSequence("5");/**发票内流水号 **/
			invoicedetail4.setInvoCode("24");/**发票科目代码 **/
			invoicedetail4.setInvoName("病历本费");/**发票科目名称 **/
			invoicedetail4.setPubCost(0.0);/**可报效金额 **/
			invoicedetail4.setOwnCost(0.0);/**不可报效金额  **/
			invoicedetail4.setPayCost(ationInfo.getBookFee());/**自付金额 **/
			invoicedetail4.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());/**执行科室 **/
			invoicedetail4.setDeptName(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptName());/**执行科室名称 **/
			invoicedetail4.setOperDate(DateUtils.getCurrentTime());/**操作时间 **/
			invoicedetail4.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());/**操作员 **/
			invoicedetail4.setBalanceFlag(0);/**0未日结/1已日结 **/
			invoicedetail4.setCancelFlag(1);/**1正常，0作废，2重打，3注销 **/
			invoicedetail4.setInvoiceSeq(invoiceSeq);
			invoicedetail4.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			invoicedetail4.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			invoicedetail4.setCreateTime(DateUtils.getCurrentTime());//创建时间
			ationDAO.save(invoicedetail4);
			
			FinanceInvoiceInfoNow invoiceInfo = new FinanceInvoiceInfoNow();
			invoiceInfo.setId(null);//id
			invoiceInfo.setInvoiceNo(map.get("resCode"));/**发票号 **/
			invoiceInfo.setTransType(1);/**交易类型,1正，2反 **/
			invoiceInfo.setCardNo(ationInfo.getMidicalrecordId());/**病历卡号 **/
			invoiceInfo.setRegDate(DateUtils.getCurrentTime());/**挂号日期 **/
			invoiceInfo.setName(patient.getPatientName());/**患者姓名 **/
			invoiceInfo.setPactCode(ationInfo.getPactCode());/**合同单位代码 **/
			invoiceInfo.setPactName(ationInfo.getPactName());/**合同单位名称 **/
	//		invoiceInfo.setMcardNo(patient.getpatien);/**个人编号 **/
	//		invoiceInfo.setMedicalType(ationInfo.get);
			invoiceInfo.setTotCost(ationInfo.getSumCost());/**总额 **/
			invoiceInfo.setPubCost(0.0);/**可报效金额 **/
			invoiceInfo.setOwnCost(0.0);/**不可报效金额 **/
			invoiceInfo.setPayCost(ationInfo.getSumCost());/**自付金额 **/
			invoiceInfo.setRealCost(ationInfo.getSumCost());/**实付金额 **/
			invoiceInfo.setInvoiceSeq(invoiceSeq);/**发票序号，一次结算产生多张发票的combNo **/
			invoiceInfo.setClinicCode(ationInfo.getClinicCode());/**挂号流水号 **/
			invoiceInfo.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			invoiceInfo.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			invoiceInfo.setCreateTime(DateUtils.getCurrentTime());//创建时间
			invoiceInfo.setPaykindCode(payKindCode);
			ationDAO.save(invoiceInfo);
			
			BusinessPayModeNow payMode = new BusinessPayModeNow();
			payMode.setId(null);//id
			payMode.setInvoiceNo(map.get("resCode"));/**发票号 **/
			payMode.setTransType(1);/**交易类型,1正，2反**/
			payMode.setSequenceNo(1);/**交易流水号**/
			payMode.setModeCode(ationInfo.getPayType());/**支付方式**/
			payMode.setTotCost(ationInfo.getSumCost());/**应付金额**/
			payMode.setRealCost(ationInfo.getSumCost());/**实付金额**/
			payMode.setCheckFlag(0);/**0未核查/1已核查**/
			payMode.setBalanceFlag(0);/**0未日结/1已日结**/
			payMode.setCorrectFlag(0);/**0未对帐/1已对帐**/
			payMode.setInvoiceSeq(invoiceSeq);/**发票序号，一次结算产生多张发票的combNo**/
			payMode.setCancelFlag(1);/**1正常，0作废，2重打，3注销**/
			payMode.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			payMode.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			payMode.setCreateTime(DateUtils.getCurrentTime());//创建时间
			payMode.setHospitalID(HisParameters.CURRENTHOSPITALID);
			payMode.setAreaCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getAreaCode());
			ationDAO.save(payMode);
			
			ationDAO.updateFinanceInvoice(map.get("invooiceId"),ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount(),invoiceType,map.get("resCode"));
			
			mapinfo.put("resMsg", "success");
			mapinfo.put("resCode",mainId);
			return mapinfo;
	}

	@Override
	public Map<String, String> findInfoVo(String deptCode, String doctCode,String reglevlCode, Integer noonCode, String midicalrecordId) {
		Map<String,String> map = new HashMap<String, String>();
		List<RegistrationNow> ationList = ationDAO.findInfoVoList(deptCode,doctCode,reglevlCode,noonCode,midicalrecordId);
		//查询时否存在号源信息
		List<RegisterDocSource> list = ationDAO.findNewInfoList(deptCode, doctCode, reglevlCode, noonCode, null);
		if(list!=null && list.size()>0){
			map.put("resMsgs", "success");
		}else{
			map.put("resMsgs", "error");
		}
		if(ationList!=null && ationList.size()>0){
			map.put("resMsg", "error");
		}else{
			map.put("resMsg", "success");
		}
		return map;
	}

	@Override
	public HospitalParameter changePay() {
		return ationDAO.changePay();
	}

	@Override
	public OutpatientAccount veriPassWord(String passwords,String midicalrecordId) {
		Map<String,String> map = new HashMap<String, String>();
		OutpatientAccount account = ationDAO.veriPassWord(DigestUtils.md5Hex(passwords),midicalrecordId);
		if(account==null){
			new OutpatientAccount();
		}
		
		return account;
		
	}


	@Override
	public List<RegisterPreregisterNow> findPreregisterList(RegisterPreregisterNow preregister,String page,String rows) {
		return ationDAO.findPreregisterList(preregister,page,rows);
	}


	@Override
	public InfoPatient judgeIdcrad(String preregisterCertificatesno) {
		return ationDAO.judgeIdcrad(preregisterCertificatesno);
	}


	@Override
	public List<RegistrationNow> queryBackNo(String cardNo) {
		return ationDAO.queryBackNo(cardNo);
	}
	
	@Override
	public List<RegistrationNow> queryBackNo(String cardNo, String no) {
		return ationDAO.queryBackNo(cardNo, no);
	}


	@Override
	public Map<String, String> saveOrUpdateInfoBack(RegistrationNow registrations,String backnumberReason, String payType)throws Exception  {
			Map<String,String> map = new HashMap<String, String>();
			//处理挂号主表
			RegistrationNow ation = changeDeptDAO.getById(registrations.getId());
			//扣患者账户
			if("2".equals(payType)){
				OutpatientAccount account = ationDAO.queryAccountByMedicalrecord(registrations.getMidicalrecordId());
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
				}else if(ation.getCheckFlag()==1){
					map.put("resMsg", "error");
					map.put("resCode", "已封账，不可退费!");
					return map;
				}else{
					account.setAccountBalance(account.getAccountBalance()+Double.valueOf(registrations.getSumCost()));
					ationDAO.save(account);
					
					//添加门诊账户明细表
					OutpatientAccountrecord accountrecord = new OutpatientAccountrecord();
					accountrecord.setId(null);//ID
					accountrecord.setMedicalrecordId(account.getMedicalrecordId());//病历号
					accountrecord.setAccountId(account.getId());//门诊账户编号
					accountrecord.setOpertype(5);//操作类型
					accountrecord.setMoney(Double.valueOf(registrations.getSumCost()));//交易金额
					accountrecord.setDeptCode(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//相关科室
					accountrecord.setOperCode(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//操作员 
					accountrecord.setOperDate(DateUtils.getCurrentTime());//操作时间
					accountrecord.setAccountBalance(account.getAccountBalance());//交易后余额
					accountrecord.setValid(0);//是否有效
					accountrecord.setInvoiceType("01");//发票类型
					accountrecord.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					accountrecord.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
					accountrecord.setCreateTime(DateUtils.getCurrentTime());
					accountrecord.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
					accountrecord.setUpdateTime(DateUtils.getCurrentTime());
					ationDAO.save(accountrecord);
				}
			}
			
			
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
			ation1.setBacknumberReason(backnumberReason);
			ation1.setMidicalrecordId(ation.getMidicalrecordId());
			ation1.setInState(2);
			ation1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());//创建人
			ation1.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());//创建科室
			ation1.setCreateTime(DateUtils.getCurrentTime());//创建时间
			ation1.setUpdateTime(DateUtils.getCurrentTime());
			ation1.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			ationDAO.save(ation1);
			
			List<FinanceInvoicedetailNow> invoicedetailList = changeDeptDAO.findInvoicedetailList(ation.getInvoiceNo());
			if(invoicedetailList.size()>0){
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
					ationDAO.save(invoicedetail);
				}
				
				for(FinanceInvoicedetailNow modls : invoicedetailList){
					modls.setCancelFlag(2);
					ationDAO.update(modls);
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
			invoiceInfo1.setTotCost(-invoiceInfo.getTotCost());//总金额
			invoiceInfo1.setPubCost(-invoiceInfo.getPubCost());//可报效金额
			invoiceInfo1.setOwnCost(-invoiceInfo.getOwnCost());//不可报效金额
			invoiceInfo1.setPayCost(-invoiceInfo.getPayCost());//自付金额
			invoiceInfo1.setRealCost(-invoiceInfo.getRealCost());//实付金额
			invoiceInfo1.setExamineFlag(0);//团体/个人
			invoiceInfo1.setCancelFlag(1);//有效
			invoiceInfo1.setCancelInvoice(invoiceInfo.getCancelInvoice());//作废票据号
			invoiceInfo1.setCancelCode(invoiceInfo.getCancelCode());//作废操作员
			invoiceInfo1.setCancelDate(invoiceInfo.getCancelDate());//作废时间
			invoiceInfo1.setInvoiceSeq(invoiceInfo.getInvoiceSeq());//发票序号
			invoiceInfo1.setInvoiceComb(invoiceInfo.getInvoiceComb());//一次收费序号
			invoiceInfo1.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			invoiceInfo1.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
			invoiceInfo1.setCreateTime(DateUtils.getCurrentTime());
			ationDAO.save(invoiceInfo1);
			
			BusinessPayModeNow pay = ationDAO.queryPayMode(ation.getInvoiceNo());
			BusinessPayModeNow payMode = new BusinessPayModeNow	();
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
			
			pay.setCancelFlag(0);
			ationDAO.update(pay);
			
			invoiceInfo.setCancelFlag(3);
			ationDAO.update(invoiceInfo);
			
			List<RegistrationNow> list = ationDAO.getRegisterByCliNo(ation.getClinicCode());
			List<RegistrationNow> list2 = new ArrayList<RegistrationNow>();
			if(list!=null && list.size()>0){
				for (RegistrationNow r : list) {
					if(r.getInState()==1){
						r.setValidFlag(0);
						list2.add(r);
					}
				}
				ationDAO.saveOrUpdateList(list);
			}
			
			ation.setInState(2);
			ation.setValidFlag(0);
			ationDAO.update(ation);
			
			//当日结时退号，更新日结信息
			if(ation.getBalanceFlag()==1){
				RegisterDaybalance regDaybalance = ationDAO.getRegDaybalance(ation.getCreateDept(), ation.getBalanceOpcd(), ation.getBalanceDate());
				if(regDaybalance!=null){
					RegisterBalancedetail balancedetail = ationDAO.getRegBalanceDetail(regDaybalance, pay.getModeCode());
					RegisterFee registerFee = ationDAO.findFeeByfee(ation.getPactCode(),ation.getReglevlCode());
					if(balancedetail!=null){
						balancedetail.setQuitFee(balancedetail.getQuitFee()+registerFee.getRegisterFee());
						balancedetail.setQuitNum(balancedetail.getQuitNum()+1);
						balancedetail.setRegFee(balancedetail.getRegFee()-registerFee.getRegisterFee());
						balancedetail.setRegNum(balancedetail.getRegNum()-1);
						balancedetail.setSumFee(balancedetail.getSumFee()-registerFee.getRegisterFee());
						balancedetail.setSumNum(balancedetail.getSumNum()-1);
						ationDAO.update(balancedetail);
					}
				}
				OperationUtils.getInstance().conserve(null, "退号", "UPDATE", "T_REGISTER_BALANCEDETAIL", OperationUtils.LOGACTIONTONMAINBACKAFTERDAYBALANCE);
			}
			
			//更新医生号源表已挂人数 docSource;
			RegisterDocSource info = ationDAO.findNewInfoList(ation.getDeptCode(), ation.getDoctCode(), ation.getReglevlCode(), ation.getNoonCode(), null).get(0);
			info.setClinicSum(info.getClinicSum()-1);
			ationDAO.update(info);
			
			map.put("resMsg", "success");
			map.put("resCode", "退号成功");
			return map;
	}
	
	/**  
	 *  
	 * @Description：诊出
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,String> passPatient(String clinicNo) {
		Map<String,String> map = new HashMap<String, String>();
		RegistrationNow registration = ationDAO.getPatient(clinicNo);
		if(registration==null){
			map.put("resMsg", "error");
			map.put("resCode", "挂号信息不存在！");
			return map;
		}
		if(registration.getYnsee()==1){
			map.put("resMsg", "error");
			map.put("resCode", "患者["+registration.getPatientName()+"]已诊出！");
			return map;
		}
		Date date = new Date();
		registration.setEndTime(date);
		registration.setYnsee(1);
		registration.setSeeDate(date);
		registration.setSeeDpcd(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		registration.setSeeDocd(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		ationDAO.save(registration);
		map.put("resMsg", "success");
		map.put("resCode", "诊出成功！");
		return map;
	}

	@Override
	public void updateRegistration(RegistrationNow ationInfo,String q) {
		RegistrationNow registration2 = ationDAO.queryRegistrationById(ationInfo.getId());
		
		if("0".equals(q)){
			registration2.setInvoicePrintFlag(0);
			registration2.setPrintInvoicecnt(0);
		}else{
			registration2.setInvoicePrintFlag(1);
			registration2.setPrintInvoicecnt(1);
		}
		ationDAO.save(registration2);
	}

	@Override
	public void saveRegisterPreregister(RegisterPreregisterNow registerPreregister) {
		ationDAO.saveRegisterPreregister(registerPreregister);
		
	}

	/**  
	 * @Description：新  值班列表
	 * @Author：GH
	 * @CreateDate：2016年12月2日17:21:35 
	 * @ModifyRmk：  null值 用于更新挂号人数的方法  本方法不适用
	 * @version 1.0
	 */
	@Override
	public List<RegisterDocSource> findNewInfoList(String deptId, String empId, String gradeId, Integer midday) {
		List<RegisterDocSource> lst = ationDAO.findNewInfoList(deptId,empId,gradeId,midday,null);
		return lst;
	}

	@Override
	public List<RegistrationNow> queryRestration(RegistrationNow actionIfo,String page,String rows) {
		return ationDAO.queryRestration(actionIfo,page,rows);
	}
	public int queryRestrationTotal(RegistrationNow actionInfo,String page,String rows){
		return ationDAO.queryRestrationTotal(actionInfo, page, rows);
	}

	@Override
	public List<OutpatientRecipedetailNow> checkISsee(String clinicCode) {
		return ationDAO.checkISsee(clinicCode);
	}

	@Override
	public RegPrintVO getRegByID(String id) {
		return ationDAO.getRegByid(id);
	}

	@Override
	public int isEmployeeBlack() {
		return ationDAO.isEmployeeBlack();
	}
}
