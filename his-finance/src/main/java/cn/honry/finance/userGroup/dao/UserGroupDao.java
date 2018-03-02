package cn.honry.finance.userGroup.dao;

import java.util.List;

import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.finance.userGroup.vo.EmployeeGroupVo;
@SuppressWarnings({"all"})
public interface UserGroupDao extends EntityDao<FinanceUsergroup>{
	
	/**  
	 *  
	 * @Description：  返查询所有符合条件的数据
	 * @Author：kjh
	 * @CreateDate：2015-6-12   
	 * @version 1.0
	 *
	 */
	List<FinanceUsergroup> queryUsergroup(FinanceUsergroup financeUsergroup) throws Exception ;
	
	/**  
	 *  
	 * @Description：  获取所有符合条件的数据的总数
	 * @Author：kjh
	 * @CreateDate：2015-6-12   
	 * @version 1.0
	 *
	 */
	int getUsergroupCount(FinanceUsergroup financeUsergroup) throws Exception ;
	
	/**  
	 *  
	 * @Description：  删除数据
	 * @Author：kjh
	 * @CreateDate：2015-6-12   
	 * @version 1.0
	 *
	 */
	void delete(String ids) throws Exception ;
	/**  
	 *  
	 * @Description： 保存
	 * @Author：kjh
	 * @CreateDate：2015-6-16  
	 * @param: ids:员工id 
	 * @version 2.0
	 *
	 */
	void save(String ids,FinanceUsergroup financeUsergroup) throws Exception ;
	/**  
	 *  
	 * @Description： 修改
	 * @Author：kjh
	 * @CreateDate：2015-6-16 
	 * @param: ids:员工id oldGroupName：修改之前的财务组名
	 * @version 2.0
	 *
	 */
	void update(String ids,FinanceUsergroup financeUsergroup,String oldGroupName) throws Exception ;
	/**  
	 *  
	 * @Description： 根据组名查出组名下的所有员工
	 * @Author：kjh
	 * @CreateDate：2015-6-17   
	 * @param: oldGroupName：修改之前的财务组名
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	List<SysEmployee> findGroupEmployee(String groupName) throws Exception;
	/**  
	 *  
	 * @Description： 根据组名查出组名下的所有员工
	 * @Author：yegaunqun
	 * @CreateDate：2016-4-19   
	 * @param: groupName：财务组名
	 * @version 1.0
	 *
	 */
	List<EmployeeGroupVo> queryGroupEmployee(String groupName) throws Exception ;
	/**  
	 *  
	 * @Description：根据员工id和组名，删除数据
	 * @Author：kjh
	 * @CreateDate：2015-6-17 
	 * @param: employeeId：员工id   groupName：组名
	 * @version 1.0
	 *
	 */
	void delete(String employeeId,String groupName) throws Exception ;
	/**  
	 *  
	 * @Description： 组名查询出第一条财务组
	 * @Author：kjh
	 * @CreateDate：2015-6-17 
	 * @param: groupName：组名  
	 * @version 1.0
	 *
	 */
	FinanceUsergroup findGroup(String groupName) throws Exception ;
	/**  
	 *  
	 * @Description： 根据组名查询出所有财务组
	 * @Author：kjh
	 * @CreateDate：2015-6-19 
	 * @param: groupName：组名  
	 * @version 1.0
	 *
	 */
	List<FinanceUsergroup> findGroupAll(String groupName) throws Exception ;
	/**  
	 *  
	 * @Description： 读出数据库中最大的组编号 +1,如果没有的话返回1
	 * @Author：kjh
	 * @CreateDate：2015-6-23 
	 * @version 1.0
	 *
	 */
	int findMaxNo() throws Exception ;
}
