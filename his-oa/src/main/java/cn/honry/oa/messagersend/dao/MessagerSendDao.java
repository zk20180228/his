package cn.honry.oa.messagersend.dao;

import java.util.List;

import cn.honry.base.bean.model.SysEmployee;

public interface MessagerSendDao {
	/**  
	 * <p> </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月23日 下午7:38:53 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月23日 下午7:38:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 科室
	 * @param dutyCode 职务
	 * @param titleCode 职称
	 * @param acount 工号
	 * @return
	 * List<SysEmployee>
	 */
	List<String> getEmployeBysome(String dutyCode,String titleCode,String account,String deptCode);
	/**  
	 * <p>获取所有的员工 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月24日 下午2:26:08 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月24日 下午2:26:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * List<SysEmployee>
	 */
	List<SysEmployee> getAllEmployee();
}
