package cn.honry.mobile.personMenu.dao.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysUserMenuFunjuris;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.system.menu.vo.ParentMenuVo;
import cn.honry.mobile.personMenu.dao.PersonMenuDAO;
import cn.honry.mobile.personMenu.vo.MenuVo;
import cn.honry.mobile.personMenu.vo.UserVo;

/**  
 *  
 * @className：UserMenuFunJurisDAOImpl
 * @Description：  用户栏目功能权限管理（移动端）
 * @Author：dutianliang
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：dutianliang
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("personMenuDAO")
@SuppressWarnings({ "all" })
public class PersonMenuDAOImpl extends HibernateEntityDao<SysUserMenuFunjuris> implements PersonMenuDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public int getUserTotal(final String q) {
		final String sql = "SELECT COUNT(1)"+getFunJurisUserSql(q);
		Integer total = (Integer) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				queryObject.setInteger(0,0).setInteger(1,0);
				if(StringUtils.isNotBlank(q)){
					queryObject.setString(2, q).setString(3, q)
							.setString(4, q).setString(5, q).setString(6, q);
				}
				return ((BigDecimal)queryObject.uniqueResult()).intValue();
			}
		});
		return total;
	}

	@Override
	public List<UserVo> getUserRows(final String page,final String rows,final String q) {
		final String sql = "SELECT u.USER_ACCOUNT account,u.USER_NAME name,u.USER_NICKNAME nickName,u.USER_PHONE phone,u.USER_EMAIL email,u.USER_REMARK remark"+getFunJurisUserSql(q);
		List<UserVo> list = (List<UserVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql)
						.addScalar("account")
						.addScalar("name")
						.addScalar("nickName")
						.addScalar("phone")
						.addScalar("email")
						.addScalar("remark");
				queryObject.setInteger(0,0).setInteger(1,0); 
				if(StringUtils.isNotBlank(q)){
					queryObject.setString(2, q).setString(3, q)
							.setString(4, q).setString(5, q).setString(6, q);
				}
				int start = Integer.parseInt(StringUtils.isBlank(page)?"1":page);
				int count = Integer.parseInt(StringUtils.isBlank(rows)?"20":rows);
				return queryObject.setFirstResult((start - 1) * count).setMaxResults(count).setResultTransformer(Transformers.aliasToBean(UserVo.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<UserVo>();
	}

	@Override
	public List<ParentMenuVo> queryMenuCombo() {
		String hqlString = "select t.name as name,t.id as id from SysMenu t where t.belong = 2 and t.stop_flg = 0 and t.del_flg = 0 and t.haveson = 0";
		List<ParentMenuVo> list = this.createQuery(hqlString.toString()).setResultTransformer(Transformers.aliasToBean(ParentMenuVo.class)).list();
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<ParentMenuVo>();
	}

	@Override
	public List<MenuVo> queryMenuList(final String parentId, final String userAccount) {
		final StringBuffer sql = new StringBuffer("select u.id as id,t.menu_name as menuName,");
		sql.append("t.menu_alias as menuAlias,u.user_acc as userAcc ,");
		sql.append("t.menu_parameter as menuParameter,t.menu_description as menuDescription,");
		sql.append("u.rm_is_visible as rmIsVisible,u.rm_menu_order as rmMenuOrder ");
		sql.append("from t_sys_user_menu_funjuris u ");
		sql.append("left join t_sys_menu t on u.menu_alias = t.menu_alias ");
		sql.append("where t.stop_flg = 0 and t.del_flg = 0 ");
		sql.append("and t.menu_haveson = 1 and t.menu_parent = ? ");
		sql.append("and u.user_acc = ? order by u.rm_is_visible, u.rm_menu_order");
		List<MenuVo> list = (List<MenuVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString())
						.addScalar("id")
						.addScalar("menuName")
						.addScalar("menuAlias")
						.addScalar("userAcc")
						.addScalar("menuParameter")
						.addScalar("menuDescription")
						.addScalar("rmIsVisible", Hibernate.INTEGER)
						.addScalar("rmMenuOrder", Hibernate.INTEGER);
				queryObject.setString(0, parentId).setString(1, userAccount);
				return queryObject.setResultTransformer(Transformers.aliasToBean(MenuVo.class)).list();
			}
		});
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<MenuVo>();
	}

	@Override
	public SysUserMenuFunjuris getByMenuIdUserId(String menuAlias,
			String userAcc) {
		String hql = "from SysUserMenuFunjuris where userAcc = ? and menuAlias = ?";
		List<SysUserMenuFunjuris> list = super.find(hql, userAcc,menuAlias);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	/**  
	 * 
	 * <p>获取用户列表Sql</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月15日 下午5:53:56 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月15日 下午5:53:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param q
	 *
	 */
	private String getFunJurisUserSql(String q) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM T_SYS_USER u WHERE u.STOP_FLG = ? AND u.DEL_FLG = ? ");
		if(StringUtils.isNotBlank(q)){
			buffer.append(" AND (UPPER(u.USER_ACCOUNT) LIKE ? OR u.USER_NAME LIKE ? OR u.USER_NICKNAME LIKE ? OR u.USER_PHONE LIKE ? OR u.USER_EMAIL LIKE ?) ");
		}
		return buffer.toString();
	}
}
