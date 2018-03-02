package cn.honry.inner.system.role.dao;

import java.util.List;

import cn.honry.base.bean.model.SysRole;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * 内部接口：角色 
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface RoleInInterDAO extends EntityDao<SysRole>{

	/**  
	 *  
	 * 根据id获得角色
	 * @Author：aizhonghua
	 * @CreateDate：2015-10-22 上午11:57:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-10-22 上午11:57:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	SysRole getRoleById(String roleId);

	/**  
	 *  
	 * 根据用户id获得该用户拥有的角色
	 * @Author：aizhonghua
	 * @CreateDate：2015-10-27 下午05:52:45  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-10-27 下午05:52:45  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysRole> findRoleByUserId(String id);

}
