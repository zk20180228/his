package cn.honry.outpatient.newChangeDeptLog.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterChangeDeptLog;
import cn.honry.base.bean.model.RegistrationNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.outpatient.newInfo.vo.InfoVo;

public interface ChangeDeptService extends BaseService<RegistrationNow>{
	/**  
	 * @Description：  挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： cardNo 就诊卡号
	 * @param： clinicCode 门诊号 
	 * @version 1.0
	 */
	List<RegistrationNow> queryRegisterMain(String cardNo, String clinicCode);
	/**  
	 * @Description：  挂号换科记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： ids 挂号主表ＩＤ集合
	 * @version 1.0
	 */
	List<RegisterChangeDeptLog> queryChangeDeptList(String ids);
	/**  
	 * @Description：  根据ID查询挂号记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： id 挂号主表id
	 * @version 1.0
	 */
	RegistrationNow getById(String id);
	/**  
	 * @Description：  查询门诊挂号科室
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> changeDepartmentCombobox();
	/**  
	 * @Description：换科医生
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk：  
	 * @param： gradeX 挂号级别
	 * @param： newDept 挂号科室
	 * @version 1.0
	 */
	List<InfoVo> changeEmployeeCombobox(String gradeX, String newDept);
	/**  
	 * @Description：保存
	 * @Author：liudelin
	 * @CreateDate：2016-07-14
	 * @ModifyRmk： 
	 * @param： changeDeptLog 换科实体 
	 * @version 1.0
	 */
	Map<String, String> registerChangeSave(RegisterChangeDeptLog changeDeptLog);
	/**  
	 * @Description：渲染科室
	 * @Author：liudelin
	 * @CreateDate：2016-07-21
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysDepartment> querydeptComboboxs();
	/**  
	 * @Description：渲染人员
	 * @Author：liudelin
	 * @CreateDate：2016-07-21
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<SysEmployee> queryempComboboxs();
	/**  
	 * @Description：查询挂号主表中换科记录
	 * @Author：liudelin
	 * @CreateDate：2016-07-25
	 * @ModifyRmk： 
	 * @version 1.0
	 */
	List<RegistrationNow> queryChangeDept(String cardNo, String clinicCode);
}
