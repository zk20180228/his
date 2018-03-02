package cn.honry.inpatient.inpatientProof.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.InpatientProof;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.Registration;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.inpatientProof.vo.PoorfBill;
import cn.honry.inpatient.inpatientProof.vo.proReg;



/**   
*  
* @className：InpatientProof
* @description：  住院证明Dao
* @author：wujiao
* @createDate：2015-6-24 上午10:41:37  
* @modifier：wujiao
* @modifyDate：2015-6-24 上午10:41:37  
* @modifyRmk：  
* @version 1.0
 */
@SuppressWarnings({"all"})
public interface InpatientProofDao extends EntityDao<InpatientProof>{
	
	/**  
	 * 查询病人信息
	 * @Description：  查询病人信息
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	InpatientProof getProof(String id);
	
	/**  
	 * 查询病人树
	 * @Description：  查询病人树
	 * @Author：wujiao
	 * @CreateDate：2015-6-24 上午10:56:35  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-6-24 上午10:56:35   
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientProof> findTree(boolean b);
	
	/**  
	 * 根据挂号记录ID查询患者信息
	 * @Description： 根据挂号记录ID查询患者信息
	 * @Author：tcj
	 * @CreateDate：2015-12-3
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	RegistrationNow queryMedicalrecordId(String medicalrecordId);
	
	/**
	 * 根据患者号查询患者信息和挂号信息  
	 * @Description：  根据患者号查询患者信息和挂号信息
	 * @Author：tcj
	 * @CreateDate：2015-12-7
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<proReg> queryInfoListHis(String medicalrecordId,Date now);
	
	/**
	 * 查询全部住院证明  
	 * @Description：  查询全部住院证明
	 * @Author：zhenglin
	 * @CreateDate：2015-12-17
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientProof> allProofInfo();
	
	/**
	 * 根据病历号查询患者当日的挂号记录
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @return
	 */
	List<RegistrationNow> queryDateInfo(String medicalrecordId);
	
	/**
	 * 根据病历号查询患者当日的开证记录
	 * @CreateDate：2015-12-25
	 * @param medicalrecordId
	 * @return
	 */
	List<InpatientProof> queryProofDate(String medicalrecordId, Date now);
	
	/**
	 * 查询病区
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	List<SysDepartment> querybingqu(String id,String param);
	
	/**
	 * 查询该患者是否处于住院状态(登记，接诊)、是否有处于有效状态的住院证
	 * @CreateDate：2016-2-20
	 * @param 
	 * @return
	 */
	String queryInpatientInfo(String zjhm);
	
	/**
	 * 根据传过来的号码（病历号、门诊号、就诊卡号、医疗证号）查询患者信息  
	 * @Description：  根据传过来的号码（病历号、门诊号、就诊卡号、医疗证号）查询患者信息
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<Patient> queryByNumber(String medicalrecordId);
	/**
	 * 通过挂号记录查询证明表里是否有证明记录（参数:病历号）
	 * @CreateDate：2016-3-16
	 * @param 
	 * @return
	 */
	List<InpatientProof> searchProofByResinfo(String medicalrecordId,String regisNo);
	/**
	 * 住院科室下拉框
	 * 住院证明住院科室下拉
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	List<SysDepartment> queryInHosDept(String param);
	/**
	 * 通过门诊号（预约号）查询患者的是否有未收费项目
	 * @CreateDate：2016-1-5
	 * @param departmentId
	 * @return
	 */
	String queryShoufeiState(String no) ;
	/**  
	 * @Description：通过病历号查询患者的在院状态
	 * @Author：tcj
	 * @CreateDate：2016-3-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String queryPatientStateBymz(String medicalrecordId) ;
	
	/**
	 * 
	 * <p>住院证查询病区 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:15:50 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:15:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param departmentCode
	 * @param q
	 * @return:
	 *
	 */
	List<SysDepartment> queryKeshi(String departmentCode, String q);
	
	/**
	 * 
	 * 
	 * <p>住院证明保存 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:25:38 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:25:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inpatientProof:
	 *
	 */
	void savePreregister(InpatientProof inpatientProof);
	
	/**
	 * 
	 * 
	 * <p>住院证明更新 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月3日 下午4:26:00 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月3日 下午4:26:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inpatientProof
	 * @return:
	 *
	 */
	int updatePreregister(InpatientProof inpatientProof) ;
	
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
	 *
	 */
	List<PoorfBill> queryProofByIds(String ids);
	
	

}
