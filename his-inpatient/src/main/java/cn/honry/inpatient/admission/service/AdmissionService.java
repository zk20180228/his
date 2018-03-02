package cn.honry.inpatient.admission.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPostureInfo;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.VidBedInfo;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.admission.vo.AdmissionVO;

public interface AdmissionService extends BaseService<AdmissionVO>{
    /**
     * 根据病历号获得VO需要的信息
     * @param medicalrecordId
     * @return
     * @throws Exception 
     */
	AdmissionVO getVOByPatientNo(String medicalrecordId) throws Exception;
    /**
     * VO保存操作
     * @param admissionVO
     * @throws Exception 
     */
	String saveAdmission(AdmissionVO admissionVO) throws Exception;
    /**
     * 获得住院部门
     * @param deptName
     * @throws Exception 
     */
	List<SysEmployee> getZYDepartmentDoctors(String deptName) throws Exception;
	/**
	 * 根据部门id查询床位信息
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception 
	 */
	List<VidBedInfo> getBedInfoWithDeptId(String id, String status) throws Exception;
	/**
	 * 根据床位id获得床位信息 用于判断床位状态
	 * @param bedId
	 * @return
	 * @throws Exception 
	 */
	BusinessHospitalbed getBedState(String bedId) throws Exception;
	/**  
	 * @Description：查询责任护士下拉框
	 * @Author：涂川江
	 * @CreateDate：2015-12-8下午18：:08
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> queryZerenhushi() throws Exception;
	/**  
	 * @Description： 查询患者是否存在
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<Patient> queryPatientexist(String medicalrecordId) throws Exception;
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> queryZhuyuanDoc(String q) throws Exception;
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：tcj
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	String queryPatientStatInfo(String medId) throws Exception;
	
	/**  
	 * @Description： 查询住院医生下拉框
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param name 
	 * @param type 
	 * @param row 
	 * @param page 
	 * @throws Exception 
	 */
	List<SysEmployee> findZhuyuanDoc(String name, String type, String page, String row) throws Exception;
	
	/**  
	 * @Description： 获取条数
	 * @Author：zhuxiaolu
	 * @CreateDate：2015-12-26
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param name 
	 * @param type 
	 * @throws Exception 
	 */
	int getTotalemp(String name, String type) throws Exception;
	
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
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow> getQueryInfo(String medicalrecordId) throws Exception;
	/**
	 * 判断床位是否被占用
	 * @param bed
	 * @param bedWard
	 * @return
	 * @throws Exception 
	 */
	String isExistBed(String bed, String bedWard,String inpatientNo) throws Exception;
	
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
	 * @throws Exception 
	 *
	 */
	List<InpatientInfoNow>  querAdmiss(String medicalrecordId) throws Exception;
	
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
	 * @throws Exception 
	 *
	 */
	List<InpatientPostureInfo> queryPostureInfo(String medicalrecordId) throws Exception;
	
}
