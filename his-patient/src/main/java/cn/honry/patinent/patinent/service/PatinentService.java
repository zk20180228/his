package cn.honry.patinent.patinent.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.inner.patient.patient.vo.CheckAccountVO;
import cn.honry.inner.patient.patient.vo.PatientIdcardVO;

public interface PatinentService extends BaseService<Patient>{
	/**
	 * 查询数据
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */
	List<PatientIdcardVO> listPatient(PatientIdcardVO vo,String page,String rows);	
	/**
	 * 查询数据,第二版,用next标注
	 * @date 2015-12-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 */
	List<PatientIdcardVO> listPatientNext(PatientIdcardVO vo,String idcardId);	
	/**
	 * 查询数据总数
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */	
	int getPatientCount(PatientIdcardVO vo);
	/**
	 * 修改
	 * @date 2015-06-02
	 * @author sgt
	 * @version 1.0
	 * @return
	 */	
	void edit(Patient patient);
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
	 * @Description: 验证患者，住院账户是否已结清及患者是否办理出院
	 * @Author：  lt
	 * @CreateDate： 2015-11-12
	 * @version 1.0
	**/
	CheckAccountVO checkAllAccount(String medicalId);
	
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
	 * 服务于就诊卡管理  多条数据时使用病历卡查询
	 * @date 2016年9月28日17:45:16
	 * @author GH
	 * @version 1.0
	 * @return
	 */
	List<PatientIdcardVO> listPatientByMedicalrecord(String idcardId);
}
