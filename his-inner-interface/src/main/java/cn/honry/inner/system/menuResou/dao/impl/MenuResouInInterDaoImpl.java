package cn.honry.inner.system.menuResou.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysMenuResourceCode;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.menuResou.dao.MenuResouInInterDao;

/**  
 *  
 * @className：MenuResouInInterDaoImpl
 * @Description：  栏目功能接口
 * @Author：aizhonghua
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository(value="menuResouInInterDao")
@SuppressWarnings({"all"})
public class MenuResouInInterDaoImpl extends HibernateEntityDao<SysMenuResourceCode> implements MenuResouInInterDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 * 
	 * 获取栏目功能Map
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-23 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-23 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysMenuResourceCode> getMenuResouList() {
		String hql = "FROM SysMenuResourceCode mr";
		List<SysMenuResourceCode> list = super.findByObjectProperty(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
}
