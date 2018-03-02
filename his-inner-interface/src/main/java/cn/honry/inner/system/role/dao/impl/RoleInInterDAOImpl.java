package cn.honry.inner.system.role.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysRole;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.role.dao.RoleInInterDAO;
import cn.honry.utils.HisParameters;

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
@Repository("roleInInterDAO")
@SuppressWarnings({ "all" })
public class RoleInInterDAOImpl extends HibernateEntityDao<SysRole> implements RoleInInterDAO{
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
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
		String hql = "FROM SysRole r WHERE r.id = '"+roleId+"' ";
		List<SysRole> roleList=super.findByObjectProperty(hql, null);
		if(roleList!=null && roleList.size()>0){
			return roleList.get(0); 
		}
		return null;
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
	public List<SysRole> findRoleByUserId(String userId) {
		String sql = "SELECT R.ROLE_ID AS id,R.ROLE_NAME AS name,R.Role_Icon AS icon,R.Role_ALIAS AS Alias FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_SYS_ROLE R WHERE R.ROLE_ID in ( "+
					" SELECT URR.UR_ROLE_ID FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_SYS_USER_ROLE_RELATION URR WHERE URR.UR_USER_ID = ? "+
					" ) AND R.DEL_FLG!=1 AND R.STOP_FLG!=1 ORDER BY R.ROLE_PATH";
		List<SysRole> roleList = this.getSession().createSQLQuery(sql)
								.addScalar("id",Hibernate.STRING).addScalar("name",Hibernate.STRING).addScalar("icon",Hibernate.STRING).addScalar("Alias",Hibernate.STRING)
								.setString(0, userId)
								.setResultTransformer(Transformers.aliasToBean(SysRole.class)).list();
		if(roleList!=null && roleList.size()>0){
			return roleList; 
		}
		return null;
	}
	
}
