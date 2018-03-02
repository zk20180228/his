package cn.honry.outpatient.newInfo.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessPayMode;
import cn.honry.base.bean.model.BusinessPayModeNow;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.bean.model.RegisterDaybalance;
import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.newInfo.vo.EmpInfoVo;
import cn.honry.outpatient.newInfo.vo.HospitalVo;
import cn.honry.outpatient.newInfo.vo.InfoStatistics;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.outpatient.newInfo.vo.RegPrintVO;
@SuppressWarnings({"all"})
public interface RegistrationDAO extends EntityDao<RegistrationNow>{
	/**  
	 * @Description：  挂号级别下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegisterGrade> gradeCombobox(String q);
	/**  
	 * @Description：  挂号科室下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> deptCombobox(String q);
	/**  
	 * @Description：  挂号专家下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @ModifyRmk：  
	 * @param： deptCode 科室
	 * @param： reglevlCode 挂号级别
	 * @param： noonCode 午别
	 * @version 1.0
	 */
	List<EmpInfoVo> empCombobox(String deptCode, String reglevlCode,Integer noonCode,String q);
	/**  
	 * @Description：  合同单位下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @version 1.0
	 */
	List<BusinessContractunit> contCombobox(String q);
	/**  
	 * @Description：  值班列表
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @version 1.0
	 */
	List<InfoVo> findInfoList(String deptCode, String doctCode,String reglevlCode, Integer noonCode);
	/**  
	 * @Description：  统计
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @version 1.0
	 */
	InfoStatistics queryStatistics(String doctCode, Integer noonCode);
	/**  
	 * @Description：  统计
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @param： doctCode 专家
	 * @version 1.0
	 */
	RegistrationNow queryRegistrationByEmp(String doctCode);
	/**  
	 * @Description： 特诊挂号费
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param： speciallimitInfo 参数特诊
	 * @version 1.0
	 */
	HospitalParameter speciallimitInfo(String speciallimitInfo);
	/**  
	 * @Description： 挂号费
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param： pactCode 合同单位
	 * @param： reglevlCode 挂号级别
	 * @version 1.0
	 */
	RegisterFee feeCombobox(String pactCode, String reglevlCode);
	/**  
	 * @Description： 跟就就诊号查询该就诊卡号是否存在，是否有效
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param： cardNo 就诊卡号
	 * @version 1.0
	 */
	PatientIdcard queryPatientIdcard(String cardNo);
	/**  
	 * @Description： 判断患者在不在黑名单中
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param： id 患者ID
	 * @version 1.0
	 */
	PatientBlackList queryBlackList(String id);
	/**  
	 * @Description： 判断患者是否有预约
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param： id 患者ID
	 * @version 1.0
	 */
	RegisterPreregisterNow findPreregister(String patientCertificatesno);
	/**  
	 * @Description： 渲染科室
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> querydeptComboboxs();
	/**  
	 * @Description： 渲染级别
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegisterGrade> querygradeComboboxs();
	/**  
	 * @Description： 渲染专家
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> queryempComboboxs();
	/**  
	 * @Description： 查询历史挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param cardNo 就诊卡号
	 * @version 1.0
	 */
	List<RegistrationNow> findInfoHisList(String cardNo);
	/**  
	 * @Description： 领取发票 
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： id 医生ID
	 * @param： invoiceType 发票类型
	 * @version 1.0
	 */
	Map<String, String> queryFinanceInvoiceNo(String id, String invoiceType);
	/**  
	 * @Description： 查询账户状态
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： midicalrecordId 病历号
	 * @version 1.0
	 */
	OutpatientAccount queryAccountByMedicalrecord(String midicalrecordId);
	/**  
	 * @Description： 查询账户操作统计累计金额
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： midicalrecordId 病历号
	 * @version 1.0
	 */
	List<OutpatientAccount> queryAccountrecord(String midicalrecordId);
	/**  
	 * @Description： 判断该患者当日有没有挂过号
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： deptCode 科室
	 * @param： doctCode 专家
	 * @param： reglevlCode 级别
	 * @param： noonCode 午别
	 * @param： midicalrecordId 病历号
	 * @version 1.0
	 */
	List<RegistrationNow> findInfoVoList(String deptCode, String doctCode,String reglevlCode, Integer noonCode, String midicalrecordId);
	/**  
	 * @Description： 病历本费
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	HospitalParameter changePay();
	/**  
	 * @Description： 验证密码
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： passwords 密码
	 * @param： midicalrecordId 病历号
	 * @version 1.0
	 */
	OutpatientAccount veriPassWord(String md5Hex, String midicalrecordId);
	/**  
	 * @Description：系统参数是否保存发票信息
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	HospitalParameter invocePemen();
	/**  
	 * @Description：根据医生和午别查询出当前挂号序号
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： doctCode 专家
	 * @param： noonCode 午别
	 * @version 1.0
	 */
	RegistrationNow queryInfoByOrder(String doctCode, Integer noonCode);
	/**  
	 * @Description：获得参数
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	Integer keyvalueDAO();
	/**  
	 * @Description：根据病历号查询患者表
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： midicalrecordId 病历号
	 * @version 1.0
	 */
	Patient queryPatientByBLh(String midicalrecordId);
	/**  
	 * @Description：查询挂号费
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： pactCode 合同单位
	 * @param： reglevlCode 挂号级别
	 * @version 1.0
	 */
	RegisterFee findFeeByfee(String pactCode, String reglevlCode);
	/**  
	 * @Description：根据合同ID查询Code
	 * @Author：liudelin
	 * @CreateDate：2016-07-12
	 * @ModifyRmk：  
	 * @param： pactCode 合同单位ID
	 * @version 1.0
	 */
	BusinessContractunit findContractunitById(String pactCode);
	/**  
	 * @Description：查询预约挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-13
	 * @ModifyRmk：  
	 * @param： preregister 预约挂号实体
	 * @version 1.0
	 */
	List<RegisterPreregisterNow> findPreregisterList(RegisterPreregisterNow preregister,String page,String rows);
	/**  
	 * @Description： 判断是否存在就诊卡号
	 * @Author：liudelin
	 * @CreateDate：2016-07-13
	 * @ModifyRmk：  
	 * @param： preregisterCertificatesno 证件号
	 * @version 1.0
	 */
	InfoPatient judgeIdcrad(String preregisterCertificatesno);
	/**  
	 * @Description： 查询挂号信息
	 * @Author：liudelin
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @param： cardNo 就诊卡号
	 * @version 1.0
	 */
	List<RegistrationNow> queryBackNo(String cardNo);
	/**
	 * 根据就诊卡号或者门诊号查询
	 * @param cardNo
	 * @param no
	 * @return
	 */
	List<RegistrationNow> queryBackNo(String cardNo, String no);
	/**  
	 * @Description： 根据发票号查询记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @param： invoiceNo 发票号
	 * @version 1.0
	 */
	BusinessPayModeNow queryPayMode(String invoiceNo);
	/**  
	 * @Description：修改发票
	 * @Author：liudelin
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @param： invoiceNo 发票号
	 * @param： invoiceId 发票号所在发票组的id
	 * @version 1.0
	 */
	void updateFinanceInvoice(String invoiceId,String id, String invoiceType, String invoiceNo);
	
	/**  
	 * @Description：获得患者挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	RegistrationNow queryInfoByCliNo(String clinicCode);
	
	/**  
	 *  
	 * @Description：  根据病历号和看诊序号查询当天的挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-11 下午03:53:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-11 下午03:53:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegistrationNow getRegisterInfoByPatientNoAndNo(String patientNo, String no);
	
	/**  
	 *  
	 * @Description：通过看诊号获得挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-12-13 下午02:23:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-12-13 下午02:23:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegistrationNow getPatient(String clinicNo);
	/**
	 * @Description：根据合同单位查询结算类别
	 * @Author：donghe
	 * @CreateDate：2016-10-19 下午02:23:28 
	 */
	List<BusinessContractunit> querypackCode(String code);
	/**
	 * @Description：根据id查询挂号记录
	 * @Author：donghe
	 * @CreateDate：2016-10-19 下午02:23:28 
	 */
	RegistrationNow queryRegistrationById(String code);
	
	/**  
	 *  
	 * @Description：  将当天预约患者，预约状态为有效更改为加号
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-10-25 上午3:56:35  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	void saveRegisterPreregister(RegisterPreregisterNow registerPreregister);
	
	/**  
	 * @Description：查询医生号源表的挂号数据
	 * @Author：GH 
	 * @CreateDate：2016年12月2日16:20:10
	 * @ModifyRmk：查询医生号源表的挂号数据  RegisterDocSource
	 * @version 1.0
	 */
	public List<RegisterDocSource> findNewInfoList(String deptId, String empId,String gradeId, Integer midday,String docSource);
	/**
	 * @Description 根据日结人、日结时间、日结人所在科室查询日结信息
	 * @author  marongbin
	 * @createDate： 2017年1月6日 上午10:43:35 
	 * @modifier 
	 * @modifyDate：
	 * @param dept 日结人所在科室
	 * @param userid 日结人code
	 * @param date 日结时间
	 * @return: RegisterDaybalance
	 * @modifyRmk：  
	 * @version 1.0
	 */
	RegisterDaybalance getRegDaybalance(String dept,String userid,Date date);
	/**
	 * @Description 根据门诊号查询挂号信息
	 * @author  marongbin
	 * @createDate： 2017年1月6日 下午3:13:10 
	 * @modifier 
	 * @modifyDate：
	 * @param clinicCode 门诊号
	 * @return: List<RegistrationNow> 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<RegistrationNow> getRegisterByCliNo(String clinicCode);
	/**
	 * @Description 根据RegisterBalance的id和支付类型获取支付明细
	 * @author  marongbin
	 * @createDate： 2017年1月6日 下午4:45:17 
	 * @modifier 
	 * @modifyDate：
	 * @param id  BALANCE_ID 
	 * @param payType 支付类型
	 * @return: RegisterBalancedetail
	 * @modifyRmk：  
	 * @version 1.0
	 */
	RegisterBalancedetail getRegBalanceDetail(RegisterDaybalance balance,String payType);
	/**
	 * @Description queryRestration
	 * @author  marongbin
	 * @createDate： 2017年1月17日 上午10:47:50 
	 * @modifier 
	 * @modifyDate：
	 * @param actionIfo
	 * @return: List<RegistrationNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<RegistrationNow> queryRestration(RegistrationNow actionIfo,String page,String rows);
	/**
	 * @Description 查询总页数
	 * @author  marongbin
	 * @createDate： 2017年1月17日 下午3:08:19 
	 * @modifier 
	 * @modifyDate：
	 * @param actionInfo
	 * @param page
	 * @param rows
	 * @return: int
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int queryRestrationTotal(RegistrationNow actionInfo,String page,String rows);
	/**
	 * @Description 通过门诊号获取医嘱
	 * @author  marongbin
	 * @createDate： 2017年3月1日 下午8:33:51 
	 * @modifier 
	 * @modifyDate：
	 * @param clinicCode
	 * @return: List<OutpatientRecipedetailNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutpatientRecipedetailNow> checkISsee(String clinicCode);
	RegPrintVO getRegByid(String id);
	/**
	 *  当前登录人是否在挂号源黑名单
	 * @return
	 */
	int isEmployeeBlack();
	
	/**
	 * 
	 * <p>根据科室编号，获取所在医院的id,院区的code </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月20日 下午7:06:27 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月20日 下午7:06:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 科室的code
	 * @return
	 * @throws:
	 *
	 */
	public HospitalVo queryHospitalInfo(String deptCode);
	
}
