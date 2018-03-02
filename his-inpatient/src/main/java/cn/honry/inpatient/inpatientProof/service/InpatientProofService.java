package cn.honry.inpatient.inpatientProof.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.inpatientProof.vo.PoorfBill;
import cn.honry.inpatient.inpatientProof.vo.proReg;
import cn.honry.utils.TreeJson;

/**   
*  
* @className：InpatientProofService
* @description：  住院证明Service
* @author：wujiao
* @createDate：2015-6-24 上午10:52:19  
* @modifyRmk：  
* @version 1.0
 */
public interface InpatientProofService extends BaseService<InpatientProof>{
	
	/**  
	 *  
	 * @Description：  查询病人信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientProof getProof(String id) throws Exception;
	
	/**  
	 *  
	 * @Description：  查询病人树
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<TreeJson> findTree(String deptId,String type) throws Exception;
	
	/**  
	 *  
	 * @Description：  添加修改
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	void saveOrUpdatePreregister(InpatientProof inpatientProof) throws Exception;
	
	/**  
	 * @Description：  根据挂号预约号（门诊号）查询患者信息
	 * @throws Exception 
	 * @Author：tcj
	 * @CreateDate：2015-12-3
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	RegistrationNow queryMedicalrecordId(String medicalrecordId) throws Exception;
	
	/**  
	 * @Description：  根据患者号查询患者信息和挂号信息
	 * @throws Exception 
	 * @Author：tcj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<proReg> queryInfoListHis(String medicalrecordId) throws Exception;
	
	/**查询全部住院证明
	 * @author zhenglin
	 * @CreateDate：2015-12-17
	 * @return
	 * @throws Exception 
	 */
	List<InpatientProof> allProofInfo() throws Exception;
	
	/**
	 * 根据病历号查询患者当日的挂号记录
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @return
	 * @throws Exception 
	 */
	List<RegistrationNow> queryDateInfo(String medicalrecordId) throws Exception;
	
	/**
	 * 根据病历号查询患者当日的开证记录
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @return
	 * @throws Exception 
	 */
	List<InpatientProof> queryProofDate(String medicalrecordId) throws Exception;
	
	/**
	 * 查询病区
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 * @throws Exception 
	 */
	List<SysDepartment> querybingqu(String id,String param) throws Exception;
	
	/**
	 * 查询该患者是否处于住院状态(登记，接诊)、是否有处于有效状态的住院证
	 * @CreateDate：2016-2-20
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	String queryInpatientInfo(String zjhm) throws Exception;
	
	/** 
	 * 根据传过来的号码（病历号、门诊号、就诊卡号、医疗证号）查询患者信息 
	 * @Description：  根据传过来的号码（病历号、门诊号、就诊卡号、医疗证号）查询患者信息
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<Patient> queryByNumber(String medicalrecordId) throws Exception;
	
	/**
	 * 通过挂号记录查询证明表里是否有证明记录（参数:病历号）
	 * @CreateDate：2016-3-16
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	List<InpatientProof> searchProofByResinfo(String medicalrecordId,String regisNo) throws Exception;
	
	/**
	 * 住院科室下拉框
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 * @throws Exception 
	 */
	List<SysDepartment> queryInHosDept(String param) throws Exception;
	
	/**
	 * 通过门诊号（预约号）查询患者的是否有未收费项目
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 * @throws Exception 
	 */
	String queryShoufeiState(String no) throws Exception;
	
	/**  
	 * 通过病历号查询患者的在院状态
	 * @Description：通过病历号查询患者的在院状态
	 * @Author：tcj
	 * @CreateDate：2016-3-30
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String queryPatientStateBymz(String medicalrecordId) throws Exception;
	
	/**
	 * 
	 * <p>住院证查询病区 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:12:03 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:12:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param departmentCode 
	 * @param q
	 * @return:
	 * @throws Exception 
	 *
	 */
	List<SysDepartment> queryKeshi(String departmentCode, String q) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>住院证明查询 打印</p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:19:05 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:19:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param ids 住院证明编号
	 * @return:
	 * @throws Exception 
	 *
	 */
	List<PoorfBill> queryProofByIds(String ids) throws Exception;
	

	

}
