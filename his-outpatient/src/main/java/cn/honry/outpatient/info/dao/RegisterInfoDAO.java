package cn.honry.outpatient.info.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.HospitalParameter;
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
import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.outpatient.info.vo.EmpInfoVo;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.info.vo.InfoStatistics;
import cn.honry.outpatient.info.vo.InfoVo;

@SuppressWarnings({"all"})
public interface RegisterInfoDAO extends EntityDao<RegisterInfo>{
	
	
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
	 * @Description：  单击患者查询id信息
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
	 *  
	 * @Description：  根据时间及支付类型查询挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-1 上午09:27:19  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-1 上午09:27:19  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterInfo> getInfoByTime(Date startTime, Date endTime, String payTypeId,String registrarId);
	
	/**  
	 *  
	 * @Description：  根据部门id获得门诊挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-6 上午09:07:52  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-6 上午09:07:52  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterInfo> getInfoListbydeptId(String deptId);
	
	/**  
	 *  
	 * @Description： 根据病历号 查询挂号信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-19 下午01:09:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-19 下午01:09:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	RegisterInfo queryRegisterInfoByCaseNo(String id);

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
	 * @param q 用于下拉的即时查询（拼音，五笔，自定义）
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
	List<EmpInfoVo> empCombobox(String dept,String grade,Integer midday);
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
	 * @Description：  退号信息列表查询
	 * @Author：ldl
	 * @CreateDate：2015-11-25
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<RegisterInfo> queryBackNo(String idcardId);
	/**  
	 * @Description：  结算记录
	 * @Author：ldl
	 * @CreateDate：2015-11-30
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws IOException 
	 * @throws ParseException 
	 */
	List<RegisterDaybalance> querydaybalan();
	/**  
	 * @Description： 查询挂号员日结详情表
	 * @Author：ldl
	 * @CreateDate：2015-11-30
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param pay 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	List<RegisterBalancedetail> querybalan(String id, String payType);
	/**  
	 * @Description：修改挂号员日结详情表
	 * @Author：ldl
	 * @CreateDate：2015-11-30
	 * @ModifyRmk： 
	 * @version 1.0
	 * @param pay 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	void updateBalan(RegisterBalancedetail modls);

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
	 *  
	 * @Description： 根据就诊卡号查询挂号表
	 * @Author：kjh
	 * @CreateDate：2016-1-11上午09:07:48  
	 *
	 */
	List<RegisterInfo> getInfo(String no);
	/**
	 * @Description:根据门诊号查询挂号信息
	 * @author: lt
	 * @CreateDate:2016-1-14
	 * @version:1.0
	 */
	RegisterInfo getInfoByClinicCode(String clinicCode);
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
	Map<String, String> queryFinanceInvoiceNo(String id, String invoiceType);
	/**
	 * @Description:根据医生查询出当前挂号序号
	 * @author: ldl
	 * @param midday 
	 * @CreateDate:2016-3-21
	 */
	RegisterInfo queryInfoByOrder(String expxrt, Integer midday);
	/**
	 * @Description:获得预约号
	 * @author: ldl
	 * @param midday 
	 * @CreateDate:2016-3-21
	 */
	Integer keyvalueDAO();
	/**
	 * @Description:修改发票号
	 * @author: ldl
	 * @param midday 
	 * @CreateDate:2016-3-21
	 */
	void updateFinanceInvoice(String id,String invoiceType,String no);
	/**
	 * @Description:修改预约信息
	 * @author: ldl
	 * @param midday 
	 * @CreateDate:2016-3-22
	 */
	void updatePreregister(String preregisterNo);
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
	 * @Description：排班人员数据
	 * @Author：ldl
	 * @CreateDate：2016-3-24
	 * @ModifyRmk： 
	 * @version 1.0
	 * @throws ParseException 
	 */
	List<RegisterSchedule> scheduleCombobox(String dept, String grade);
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
