package cn.honry.inpatient.admission.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPostureInfo;
import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.inpatient.admission.vo.AdmissionVO;

public interface AdmissionDAO extends EntityDao<AdmissionVO>{
	/**
	 * 根据住院号获得VO需要的信息
	 * @param inpatientNo
	 * @return
	 * @throws Exception 
	 */
	AdmissionVO getVOByPatientNo(String medicalrecordId);
    /**
     * 保存VO
     * @param admissionVO
     * @throws Exception 
     */
	String saveAdmission(AdmissionVO admissionVO) throws Exception;
	/**
	 * 获得住院部医生
	 * @param deptName
	 * @return
	 * @throws Exception 
	 */
	List<SysEmployee> getZYDepartmentDoctors(String deptName) throws Exception;
	/**
	 * 根据部门id查询床位信息
	 * @param id
	 * @param status
	 * @return
	 */
	List<VidBedInfo> getBedInfoWithDeptId(String id, String status);
	/**
	 * 根据床位id获得床位信息 用于判断床位状态
	 * @param bedId
	 * @return
	 */
	BusinessHospitalbed getBedState(String bedId);
	/**
	 * 修改住院登记表中相关记录的有效状态
	 * @return
	 */
	void updateInpatientProof(AdmissionVO admissionVO);
	/**
	 * 查询资料变更表的最大发生序号
	 * @return
	 */
	List<InpatientShiftData> queryMaxHappenNo();
	/**  
	 * @Description：查询责任护士下拉框
	 * @Author：涂川江
	 * @CreateDate：2015-12-8下午18：:08
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> queryZerenhushi();
	/**  
	 * @Description： 查询患者是否存在
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<Patient> queryPatientexist(String medicalrecordId);
	/**
	 * 通过主键ID查询病床使用记录表中的记录
	 * @param bedId
	 * @return
	 */
	InpatientBedinfoNow querybedInfo(String bedId);
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> queryZhuyuanDoc(String q);
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String queryPatientStatInfo(String medId);
	
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param type 
	 * @param row 
	 * @param page 
	 */
	List<SysEmployee> findZhuyuanDoc(String name, String type, String page, String row);
	
	/**  
	 * @Description： 获取条数
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param type 
	 */
	int getTotalemp(String name, String type);
	
	/**
	 * 
	 * 
	 * <p>根据病历号查询住院登记状态下的患者 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:01:48 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:01:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medicalrecordId 病历号
	 * @return:
	 *
	 */
	List<InpatientInfoNow> getQueryInfo(String medicalrecordId);
	
	/**
	 * 判断床位是否被占用
	 * @param bed
	 * @param bedWard
	 * @return
	 */
	String isExistBed(String bed, String bedWard,String inpatientNo);
	
	/**
	 * 
	 * 
	 * <p>根据病历号查询病房接诊状态下的患者 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午2:56:23 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午2:56:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medicalrecordId 病历号
	 * @return:
	 *
	 */
	List<InpatientInfoNow>  querAdmiss(String medicalrecordId);
	
	/**
	 * 
	 * 
	 * <p>根据住院号查询病人体征信息 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午3:03:48 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午3:03:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param medicalrecordId 住院号
	 * @return:
	 *
	 */
	List<InpatientPostureInfo> queryPostureInfo(String medicalrecordId);
	
	

}
