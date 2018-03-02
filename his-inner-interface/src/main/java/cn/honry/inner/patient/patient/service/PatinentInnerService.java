package cn.honry.inner.patient.patient.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.PatientBlackList;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

public interface PatinentInnerService extends BaseService<Patient>{
	
	
	/**
	 * 删除
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */	
	void delete(Patient patient);
	/**
	 * 根据ID返回数据
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */	
	Patient queryById(String patient);
	/**  
	 *  
	 * @Description： 点击树节点查询
	 * @Author：wujiao
	 * @CreateDate：2015-7-6 下午11:12:01  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-7-6 下午11:12:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param typeno 
	 * @param type 
	 *
	 */
	Patient gethzid(String id, String type, String typeno);
	/**  
	 *  
	 * @Description：  验证医保手册号是否已经存在
	 * @Author：lt
	 * @CreateDate：2015-10-15   
	 * @version 1.0
	 * @param handBook 
	 *
	 */
	boolean checkHandBook(String handBook,String patientId);
	
	/**
	 * @Description保存患者信息
	 * @author tcj
	 * @param patient
	 * @return
	 */
	RegisterInfo savePatient(Patient patient);
	/**
	 * @Description: 通过病历号查询患者信息
	 * @Author：  tcj
	 * @CreateDate： 2015-12-25
	 * @version 1.0
	**/
	Patient queryByMedicalrecordId(String medicalrecordId);
	/**
	 * 根据员工工号查询员工
	 * zpty 20160324
	 * @param jobNo
	 * @return
	 */
	SysEmployee findEmpByjobNo(String jobNo);
	
	/**  
	 *  
	 * @Description：  合同单位下拉框
	 * @Author：zpty
	 * @CreateDate：2016-4-26 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<BusinessContractunit> queryUnitCombobox();
	
	/**  
	 *  
	 * @Description：保存(黑名单)
	 * @Author：kjh
	 * @CreateDate：2015-11-5 上午11:59:48  
	 * @version 1.0
	 *
	 */
	void save(PatientBlackList entity);
	
	/**
	 * 根据传过来的病历号来返回住院次数
	 * @date 2016-11-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 */	
	Integer querySumByMedicalreId(String medicalrecordId);
	
	/**
	 * 根据传过来的病历号来更新此患者的住院次数和住院流水号
	 * @date 2016-11-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 */	
	void updateByMedicalreId(String medicalrecordId,Integer inpatientSum,String inpatientNo);
	
	/**
	 * 根据就诊卡号获取病历号
	 * @date 2016-11-24
	 * @author zhangjin
	 * @version 1.0
	 * @return
	 */	
	String getMedicalrecordId(String cardNo);
	
	/**
	 * 更新患者数据中的cardNo字段  根据ID
	 * 2017年2月8日09:42:28
	 * GH
	 */
	 void updCradNoByIdOrCard(String id,String idcard, String cradNo);
}
