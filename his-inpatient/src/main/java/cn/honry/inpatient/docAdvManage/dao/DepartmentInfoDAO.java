package cn.honry.inpatient.docAdvManage.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;

public interface DepartmentInfoDAO {
	/**  
	 *  
	 * @Description： 查询科室资料
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-12   
	 * @version 1.0
	 *
	 */
	List<SysDepartment> queryPage(String page, String rows, SysDepartment entity);

	/**  
	 *  
	 * @Description： 查询科室资料总条数
	 * @Author：yeguanqun
	 * @CreateDate：2016-1-12   
	 * @version 1.0
	 *
	 */
	int queryTotal(SysDepartment entity);
}
