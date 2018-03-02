package cn.honry.inpatient.permission.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientPermission;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
/**   
* @classname ： PermissionDao
* @description：  医嘱授权管理Dao
* @author：tuchuanjiang
* @createDate：2015-12-11 下午19：24  
* @version 1.0
 */
public interface PermissionDao extends EntityDao<InpatientPermission>{
	/**  
	 * @Description：通过住院流水号查询医嘱授权历史记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-21 下午 20：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientPermission> queryListById(String no);
	/**  
	 * @Description：通过Id删除医嘱授权记录
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 上午11:50  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	void delById(String[] idsArr, String userId);
	/**  
	 * @Description：通过病历号 查询医嘱授权记录信息
	 * @Author：TCJ
	 * @CreateDate：2015-12-22 下午 15 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	InpatientPermission queryPermissionById(String inpatientNo);
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  dh
	 * @CreateDate： 2015-7-1
	 * @param @param id
	 * @param @return   
	 * @return InpatientInfo  
	 * @version 1.0
	**/
	List<InpatientInfoNow> queryByMedicall(String medicalNo);
	/**
	 * @Description:根据病历号取到住院登记信息
	 * @Author：  tcj
	 * @CreateDate： 2016-3-29
	 * @param @return   
	 * @version 1.0
	**/
	String queryDeptType(String id);
	/**  
	 * @Description：根据科室类型查询相应的科室list
	 * @Author：TCJ
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param q 
	 */
	List<SysDepartment> queryDeptByType(String state, String q);
	/**  
	 * @Description：查询患者信息
	 * @Author：TCJ
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	Patient queryPatientInfo(String mid);
	/**  
	 * 科室与医生联动（医生可登陆的科室）
	 * @Author：TCJ
	 * @version 1.0
	 */
	List<SysEmployee> queryLoginUserDept(String departmentId);
	
	/**
	 * 
	 * 
	 * <p>通过住院流水号查询授权资料总条数 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月4日 下午5:11:34 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月4日 下午5:11:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param inno
	 * @return:
	 *
	 */
	int getQueryListByIdCount(String inno);
	
	/**  
	 * @Description：根据科室加载医生
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-3-29 下午 18 ：12  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysEmployee> employeeCombobox(String departmentId, String q);
	
	
}
