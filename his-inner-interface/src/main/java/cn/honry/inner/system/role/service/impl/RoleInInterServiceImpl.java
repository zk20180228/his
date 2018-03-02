package cn.honry.inner.system.role.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysRole;
import cn.honry.inner.system.role.dao.RoleInInterDAO;
import cn.honry.inner.system.role.service.RoleInInterService;

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
@Service("roleInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class RoleInInterServiceImpl implements RoleInInterService{

	@Autowired
	@Qualifier(value = "roleInInterDAO")
	private RoleInInterDAO roleInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysRole get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(SysRole entity) {
		
	}

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
	@Override
	public SysRole getRoleById(String roleId) {
		return roleInInterDAO.getRoleById(roleId);
	}

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
	@Override
	public List<SysRole> findRoleByUserId(String id) {
		return roleInInterDAO.findRoleByUserId(id);
	}
	
}
