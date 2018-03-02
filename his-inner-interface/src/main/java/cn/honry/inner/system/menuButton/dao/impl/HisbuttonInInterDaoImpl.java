package cn.honry.inner.system.menuButton.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysMenuButton;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.menu.vo.MenuInInterVo;
import cn.honry.inner.system.menuButton.dao.HisbuttonInInterDao;
import cn.honry.inner.system.menuButton.vo.MenuButInInterVo;

@Repository(value="hisbuttonInInterDao")
@SuppressWarnings({"all"})
public class HisbuttonInInterDaoImpl extends HibernateEntityDao<SysMenuButton> implements HisbuttonInInterDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 *  
	 * @Description：  获得该栏目拥有的按钮
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-3 下午02:59:08  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-3 下午02:59:08  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysMenuButton> getListByMenuId(String id) {
		String hql = "FROM SysMenuButton smb WHERE smb.sysMenu = '"+id+"'";
		List<SysMenuButton> menuButtonList=super.findByObjectProperty(hql, null);
		if(menuButtonList!=null && menuButtonList.size()>0){
			return menuButtonList;
		}
		return new ArrayList<SysMenuButton>();
	}

	/**  
	 *  
	 * 批量获取栏目功能
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-23 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-23 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<MenuButInInterVo> getMenuButtonByList(final List<String> idList) {
		if(idList==null||idList.size()==0){
			return null;
		}
		final String hql = "SELECT mb.BUTTON_MENU_ID menuId,mb.BUTTON_ID id,mb.BUTTON_NAME name,mb.BUTTON_ALIAS alias FROM T_SYS_MENU_BUTTON mb WHERE mb.BUTTON_MENU_ID IN (:idList)";
		List<MenuButInInterVo> list = (List<MenuButInInterVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(hql)
						.addScalar("menuId")
						.addScalar("id")
						.addScalar("name")
						.addScalar("alias");
				queryObject.setParameterList("idList", idList);
				return queryObject.setResultTransformer(Transformers.aliasToBean(MenuButInInterVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
}
