package cn.honry.inner.patient.patient.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface PatinmentInnerDao  extends EntityDao<Patient>{
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
	 * @Description：  根据证件类型和证件号获得患者id
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-6 上午09:30:15  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-6 上午09:30:15  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String getIdByNo(String certificatesType,String certificatesNo);
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
	 * @Description： 验证医保手册号是否已经存在
	 * @Author：lt
	 * @CreateDate：2015-10-15   
	 * @version 1.0
	 * @param handBook 
	 *
	 */
	boolean checkHandBook(String handBook,String patientId);
	/**
	 * 保存患者信息
	 * @param patient
	 * @return
	 */
	Patient savePatient(Patient patient);
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
	List<SysEmployee> findEmpByjobNo(String jobNo);
	
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
	 * 根据传过来的病历号来返回住院次数
	 * @date 2016-11-23
	 * @author zpty
	 * @version 1.0
	 * @return
	 */	
	Integer querySumByMedicalreId(String medicalrecordId);

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
	void updCradNoByIdOrCard(String id,String idcard,String cradNo);
}
