package cn.honry.outpatient.newInfo.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.OutpatientAccount;
import cn.honry.base.bean.model.OutpatientRecipedetailNow;
import cn.honry.base.bean.model.RegisterDocSource;
import cn.honry.base.bean.model.RegisterFee;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.outpatient.info.vo.InfoPatient;
import cn.honry.outpatient.newInfo.vo.EmpInfoVo;
import cn.honry.outpatient.newInfo.vo.InfoStatistics;
import cn.honry.outpatient.newInfo.vo.InfoVo;
import cn.honry.outpatient.newInfo.vo.RegPrintVO;

public interface RegistrationService extends BaseService<RegistrationNow>{
	/**  
	 * @Description：  挂号级别下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @version 1.0
	 */
	List<RegisterGrade> gradeCombobox(String q);
	/**  
	 * @Description：  挂号科室下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
	 * @version 1.0
	 */
	List<SysDepartment> deptCombobox(String q);
	/**  
	 * @Description：  挂号专家下拉框
	 * @Author：liudelin
	 * @CreateDate：2016-07-08 
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
	 * @param： deptCode 科室
	 * @param： doctCode 专家
	 * @param： reglevlCode 级别
	 * @param： noonCode 午别
	 * @version 1.0
	 */
	List<InfoVo> findInfoList(String deptCode, String doctCode,String reglevlCode, Integer noonCode);
	/**  
	 * @Description：  统计
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @param： doctCode 专家
	 * @param： noonCode 午别
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
	 * @Description： 查询患者
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param： cardNo 就诊卡号
	 * @version 1.0
	 */
	Map<String, Object> queryRegisterInfo(String cardNo);
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
	 * @Description： 查询历史挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-11
	 * @ModifyRmk：  
	 * @param ationInfo 挂号主表实体
	 * @version 1.0
	 */
	Map<String, String> saveInfo(RegistrationNow ationInfo,String docSource)throws Exception ;
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
	Map<String, String> findInfoVo(String deptCode, String doctCode,String reglevlCode, Integer noonCode, String midicalrecordId);
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
	OutpatientAccount veriPassWord(String passwords, String midicalrecordId);
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
	 * @Description： 退号
	 * @Author：liudelin
	 * @CreateDate：2016-07-15
	 * @ModifyRmk：  
	 * @param： registrations 挂号主表实体
	 * @param： backnumberReason 退号原因
	 * @param： payType 支付方式
	 * @version 1.0
	 */
	Map<String, String> saveOrUpdateInfoBack(RegistrationNow registrations,String backnumberReason, String payType)throws Exception ;
	
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
	Map<String,String> passPatient(String clinicNo);
	/**
	 * @Description：更新挂号表
	 * @Author：donghe
	 * @CreateDate：2016-10-19 下午02:23:28  
	 * @param registration
	 */
	void updateRegistration(RegistrationNow registration,String q);
	
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
	 * @Description：新  值班列表
	 * @Author：GH
	 * @CreateDate：2016年12月2日17:21:35 
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<RegisterDocSource> findNewInfoList(String deptId, String empId, String gradeId, Integer midday);
	/**
	 * @Description 根据就诊卡号、挂号医生、科室、挂号级别、午别查询挂号信息
	 * @author  marongbin
	 * @createDate： 2017年1月17日 上午10:44:56 
	 * @modifier 
	 * @modifyDate：
	 * @return: List<RegistrationNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<RegistrationNow> queryRestration(RegistrationNow actionIfo,String page,String rows);
	/**
	 * @Description 查询总页数
	 * @author  marongbin
	 * @createDate： 2017年1月17日 下午3:09:14 
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
	 * @createDate： 2017年3月1日 下午8:32:29 
	 * @modifier 
	 * @modifyDate：
	 * @param clinicCode
	 * @return: List<OutpatientRecipedetailNow>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<OutpatientRecipedetailNow> checkISsee(String clinicCode);
	/**根据id查询挂号信息
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2017年3月10日 下午7:43:23 
	 * @modifier 
	 * @modifyDate：
	 * @param id
	 * @return: RegistrationNow
	 * @modifyRmk：  
	 * @version 1.0
	 */
	RegPrintVO getRegByID(String id);
	/**
	 *  当前登录人是否在挂号源黑名单
	 * @return
	 */
	int isEmployeeBlack();
}
