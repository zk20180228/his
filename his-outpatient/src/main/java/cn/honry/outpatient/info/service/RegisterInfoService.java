package cn.honry.outpatient.info.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.outpatient.info.vo.EmpInfoVo;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.info.vo.InfoStatistics;
import cn.honry.outpatient.info.vo.InfoVo;

public interface RegisterInfoService extends BaseService<RegisterInfo>{
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
	RegisterInfo gethz(String idcardNo);
	
	
	
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
	
	List<RegisterInfo> getInfoByEmployeeId(String id,String type);
	/**  
	 *  
	 * @Description：   单击患者查询id信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-25 下午5:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-25 下午5:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterInfo gethzid(String id);
	/**  
	 * @Description：  挂号科室（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysDepartment> deptCombobox();
	/**  
	 * @Description：  挂号级别（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<RegisterGrade> gradeCombobox(String q);
	/**  
	 * @Description：  挂号专家（下拉框）
	 * @Author：ldl
	 * @CreateDate：2015-10-21 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param grade
	 * @param dept 
	 * @param midday 
	 */
	List<EmpInfoVo> empCombobox(String dept, String grade, Integer midday);
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
	List<BusinessContractunit> queryBusinessContractunit();
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
	RegisterFee queryRegisterFee(String id, String gradeId);
	/**  
	 * @Description：  值班列表
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param midday 
	 * @param gradeId 
	 * @param empId 
	 */
	List<InfoVo> findInfoList(String deptId, String empId, String gradeId, String midday);
	/**  
	 * @Description：  根据职称转换
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	RegisterGrade queryGradeTitle(String encode);
	/**  
	 * @Description：  查询信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	InfoPatient queryRegisterInfo(String idcardNo);
	/**  
	 * @Description：  根据患者身份证号查询是否有预约
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	RegisterPreregister findPreregister(String patientLinkdoorno);
	/**  
	 * @Description：  统计
	 * @Author：ldl
	 * @CreateDate：2015-11-18 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param midday 
	 */
	InfoStatistics queryStatistics(String empId, String midday);
	/**  
	 * @Description：  统计医生加号人数
	 * @Author：ldl
	 * @CreateDate：2015-11-18 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	RegisterInfo findInfoAdd(String empId);
	/**  
	 * @Description：  查看历史信息
	 * @Author：ldl
	 * @CreateDate：2015-11-2 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<RegisterInfo> findInfoHisList(String idcardNo);
	/**  
	 * @Description：  验证
	 * @Author：ldl
	 * @CreateDate：2015-10-28 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	RegisterInfo findInfoVo(String deptId, String empId, String gradeId, String midday,String blhcs);

	/**  
	 *  
	 * @Description：  获得挂号级别Map
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-17 下午03:44:01  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-17 下午03:44:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> getGradeMap();
	/**  
	 * @Description：  查询预约列表
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param preregisterNo 
	 * @param preregisterName 
	 * @param preregisterCertificatesno 
	 * @param midday 
	 * @param preDate 
	 * @throws ParseException 
	 */
	List<RegisterPreregister> findPreregisterList(String preregisterNo, String preregisterCertificatesno, String preregisterName, String midday, String preDate) throws ParseException;
	/**  
	 *  
	 * @Description：   保存
	 * @Author：liudelin
	 * @CreateDate：2015-6-29 下午4:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param bankBillno 
	 * @param bankAccount 
	 * @param bank 
	 * @param bankUnit 
	 * @param payType 
	 * @param patientId 
	 * @param preregisterNo 
	 * @param account 
	 *
	 */
	String saveOrUpdateInfo(RegisterInfo registerInfo, OutpatientAccount account,String no);
	/**  
	 * @Description：  查询预约回显
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	Patient queryPreregisterCertificatesno(String preregisterCertificatesno);
	/**  
	 * @Description：  判断是否存在就诊卡号
	 * @Author：ldl
	 * @CreateDate：2015-11-20 上午10:40:16  
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	InfoPatient judgeIdcrad(String preNo);
	/**  
	 *  
	 * @Description：  退号信息列表查询
	 * @Author：ldl
	 * @CreateDate：2015-11-25
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterInfo> queryBackNo(String idcardId);
	/**  
	 * @Description：  退号
	 * @Author：ldl
	 * @CreateDate：2015-11-25 
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param payType 
	 */
	Map<String,String> saveOrUpdateInfoBack(RegisterInfo registerInfo, String quitreason, String payType);
	/**  
	 * @Description：  特诊挂号费
	 * @Author：ldl
	 * @CreateDate：2015-12-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	HospitalParameter speciallimitInfo(String speciallimitInfo);

	/**
	 * @Description:根据门诊号查询挂号信息
	 * @author: lt
	 * @CreateDate:2016-1-14
	 * @version:1.0
	 */
	RegisterInfo getInfoByClinicCode(String clinicCode);
	/**  
	 *  
	 * @Description： 根据就诊卡号查询挂号表
	 * @Author：kjh
	 * @CreateDate：2016-1-11上午09:07:48  
	 *
	 */
	List<RegisterInfo> getInfo(String no);
	/**
	 * @Description:根据用户ID查询员工ID
	 * @author: ldl
	 * @CreateDate:2016-3-21
	 */
	SysEmployee querySysEmployeeId(String id);
	/**
	 * @Description:查询发票
	 * @author: ldl
	 * @CreateDate:2016-3-21
	 */
	Map<String, String> queryFinanceInvoiceNo();
	/**  
	 * @Description：修改打印发票标记
	 * @Author：ldl
	 * @CreateDate：2016-3-22
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	void iReportUpdate(RegisterInfo info);
	/**  
	 * @Description：查询患者是否在黑名单中
	 * @Author：ldl
	 * @CreateDate：2016-3-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	PatientBlackList queryBlackList(String infoMedicalrecordId);
	/**  
	 * @Description：查询病历本费用
	 * @Author：ldl
	 * @CreateDate：2016-3-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	HospitalParameter changePay();
	/**  
	 * @Description：系统参数是否保存发票信息
	 * @Author：ldl
	 * @CreateDate：2016-3-23
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	HospitalParameter invocePemen();
	/**  
	 * @Description：根据病历号查询患者账户
	 * @Author：ldl
	 * @ModifyDate：2016-04-06
	 * @ModifyRmk：  
	 * @param:midicalrecordId 病历号
	 * @version 1.0
	 */
	OutpatientAccount getAccountByMedicalrecord(String midicalrecordId);
	/**  
	 * @Description：根据病历号查询患者账户的当日消费额限
	 * @Author：ldl
	 * @ModifyDate：2016-04-06
	 * @ModifyRmk：  
	 * @param:midicalrecordId 病历号
	 * @version 1.0
	 */
	List<OutpatientAccountrecord> queryAccountrecord(String midicalrecordId);
	/**  
	 * @Description：验证密码是否正确
	 * @Author：ldl
	 * @CreateDate：2016-4-6
	 * @ModifyRmk： 
	 * @param:md5Hex 密码 
	 * @param：blhcs 病历号
	 * @version 1.0
	 */
	OutpatientAccount veriPassWord(String md5Hex, String blhcs);
	
	
}
