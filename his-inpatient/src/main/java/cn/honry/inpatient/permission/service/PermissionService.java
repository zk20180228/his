package cn.honry.inpatient.permission.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPermission;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
/**   
* @className：PermissionService
* @description：  医嘱授权service
* @author：tuchuanjiang
* @createDate：2015-12-21 下午19：55  
* @version 1.0
 */
public interface PermissionService extends BaseService<InpatientPermission>{
	/**  
	 * @Description：通过住院流水号查询医嘱授权历史记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientPermission> queryListById(String no) throws Exception;
	/**  
	 * @Description：通过Id删除医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 上午11:50  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void delById(String id) throws Exception;
	/**  
	 * @Description：通过病历号 查询医嘱授权记录信息
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 15 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InpatientPermission queryPermissionById(String inpatientNo) throws Exception;
	/**  
	 * @Description：保存或修改医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	void saveOrUpdateList(InpatientPermission inpatientPermission) throws Exception;
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  dh
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @version 1.0
	 * @throws Exception 
	**/
	List<InpatientInfoNow> queryByMedicall(String medicalNo) throws Exception;
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  tcj
	 * @CreateDate： 2016-3-29
	 * @param @return   
	 * @version 1.0
	 * @throws Exception 
	**/
	String queryDeptType(String id) throws Exception;
	/**  
	 * @Description：根据科室类型查询相应的科室list
	 * @Author：TCJ
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param q 
	 * @throws Exception 
	 */
	List<SysDepartment> queryDeptByType(String state, String q) throws Exception;
	/**  
	 * @Description：查询患者信息
	 * @Author：TCJ
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	Patient queryPatientInfo(String mid) throws Exception;
	/**  
	 * 科室与医生联动（医生可登陆的科室）
	 * @Author：TCJ
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> queryLoginUserDept(String departmentId) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>根据住院流水号查询授权资料总条数 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午5:10:20 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午5:10:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inno 住院流水号
	 * @return:
	 * @throws Exception 
	 *
	 */
	int getQueryListByIdCount(String inno) throws Exception;
	
	/**  
	 * @Description：授权医师下拉框
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> employeeCombobox(String departmentId, String q) throws Exception;
	

}
