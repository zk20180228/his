package cn.honry.inner.system.menu.dao.impl;

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

import cn.honry.base.bean.model.SysMenu;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.menu.dao.MenuInInterDAO;
import cn.honry.inner.system.menu.vo.MenuInInterVo;
import cn.honry.inner.system.menu.vo.ParentMenuVo;

/**  
 *  
 * @className：MenuInInterDAOImpl
 * @Description：  栏目接口
 * @Author：aizhonghua
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("menuInInterDAO")
@SuppressWarnings({ "all" })
public class MenuInInterDAOImpl extends HibernateEntityDao<SysMenu> implements MenuInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 *  
	 * 获得移动端栏目
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-23 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-23 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<MenuInInterVo> queryMenuMobList() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT sm.MENU_ID id,sm.MENU_NAME name,sm.MENU_ALIAS alias,sm.MENU_PARENT parent,sm.MENU_HAVESON haveson,sm.MENU_PATH path,sm.MENU_PINYIN pinYin,sm.MENU_WB wb,sm.MENU_LEVEL||'' inp ");
		buffer.append("FROM T_SYS_MENU sm WHERE MENU_PATH IN( ");
		buffer.append("SELECT MENU_PATH FROM ( ");
		buffer.append("SELECT DISTINCT REGEXP_SUBSTR(a.MENU_PATH ,'[^-]+',1,l) MENU_PATH ");
		buffer.append("FROM (SELECT DECODE(length(m.MENU_PATH), 5,m.MENU_PATH, ");
		buffer.append("10,m.MENU_PATH||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-5), ");
		buffer.append("15,m.MENU_PATH||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-5)||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-10), ");
		buffer.append("20,m.MENU_PATH||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-5)||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-10)||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-15), ");
		buffer.append("25,m.MENU_PATH||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-5)||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-10)||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-15)||'-'||substr(m.MENU_PATH,0,length(m.MENU_PATH)-20) ");
		buffer.append(")MENU_PATH FROM T_SYS_MENU m WHERE m.STOP_FLG = 0 AND m.DEL_FLG = 0 AND m.MENU_HAVESON =1 AND m.MENU_BELONG IN (3) and m.OPEN_STATE = 0");
		buffer.append(") a,(SELECT LEVEL l FROM DUAL CONNECT BY LEVEL<=100) b ");
		buffer.append("WHERE l<=LENGTH(a.MENU_PATH)-LENGTH(REPLACE(MENU_PATH,'-'))+1 ");
		buffer.append(")) and sm.STOP_FLG = 0 AND sm.DEL_FLG = 0 and sm.OPEN_STATE = 0 ORDER BY MENU_PATH ");
		List<MenuInInterVo> list = (List<MenuInInterVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("id")
						.addScalar("name")
						.addScalar("alias")
						.addScalar("parent")
						.addScalar("haveson",Hibernate.INTEGER)
						.addScalar("path")
						.addScalar("pinYin")
						.addScalar("wb")
						.addScalar("inp");
				return queryObject.setResultTransformer(Transformers.aliasToBean(MenuInInterVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public SysMenu getByMenuAlias(String menuAlias) {
		String hql = "from SysMenu where alias = ? and stop_flg = 0 and del_flg = 0";
		List<SysMenu> list = super.find(hql, menuAlias);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ParentMenuVo> queryMobileParentMenu() {
		final String sql = "select s.menu_alias alias,t.menu_alias parent from t_sys_menu s left join t_sys_menu t on s.menu_parent = t.menu_id "
				+ "where s.stop_flg = 0 and s.del_flg = 0 and t.stop_flg = 0 and t.del_flg = 0 and s.menu_belong = 3";
		List<ParentMenuVo> list = (List<ParentMenuVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql)
						.addScalar("alias")
						.addScalar("parent");
				return queryObject.setResultTransformer(Transformers.aliasToBean(ParentMenuVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<SysMenu> queryAllMobile() {
		String hql = "from SysMenu where belong = 3 and stop_flg = 0 and del_flg = 0";
		List<SysMenu> list = super.find(hql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
}
