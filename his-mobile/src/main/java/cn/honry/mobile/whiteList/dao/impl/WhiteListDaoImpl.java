package cn.honry.mobile.whiteList.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MWhiteList;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.whiteList.dao.WhiteListDao;

@Repository("whiteListDao")
@SuppressWarnings({ "all" })
public class WhiteListDaoImpl extends HibernateEntityDao<MWhiteList> implements WhiteListDao{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Integer getTotal(MWhiteList mWhiteList) throws Exception {
		StringBuffer hql = new StringBuffer("from MWhiteList t where t.stop_flg = 0 and t.del_flg = 0");
		hql = getHql(mWhiteList, hql);
		Integer total = super.getTotal(hql.toString());
		if(total == null){
			return 0;
		}
		return total;
	}


	@Override
	public List<MWhiteList> getList(String rows, String page,
			MWhiteList mWhiteList) throws Exception {
		StringBuffer sql = new StringBuffer("select t.id,t.USER_ACCOUNT,t.machine_code,u.USER_NAME as userName,d.DEPT_NAME as deptName ,u.USER_SEX as userSex from M_WHITE_LIST t "); 
		sql.append("left join t_sys_user u on t.USER_ACCOUNT = u.USER_ACCOUNT and u.stop_flg = 0 and u.del_flg = 0 "); 
		sql.append("left join t_employee e on t.USER_ACCOUNT = e.EMPLOYEE_JOBNO and e.stop_flg = 0 and e.del_flg = 0 "); 
		sql.append("left join t_department d on e.dept_id =d.dept_code  and d.stop_flg = 0 and d.del_flg = 0 "); 
		sql.append("where t.stop_flg = 0 and t.del_flg = 0 "); 
		final String sqlsString = getHql(mWhiteList, sql).toString();
		List<MWhiteList> list = (List<MWhiteList>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sqlsString)
						.addScalar("id")
						.addScalar("user_account")
						.addScalar("machine_code")
						.addScalar("userName")
						.addScalar("deptName")
						.addScalar("userSex");
				return queryObject.setResultTransformer(Transformers.aliasToBean(MWhiteList.class)).list();
			}
		});
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MWhiteList>();
	}


	@Override
	public MWhiteList findWhiteByUserAccunt(MWhiteList mWhiteList)
			throws Exception {
		String hql = "from MWhiteList where user_account = ? and machine_code = ? and stop_flg = 0 and del_flg = 0";
		List<MWhiteList> list = super.find(hql, mWhiteList.getUser_account(), mWhiteList.getMachine_code());
		if(list != null && list.size() != 0){
			return list.get(0);
		}
		return null;
	}


	@Override
	public void delWhiteByUserAccount(MWhiteList mWhite) throws Exception {
		StringBuffer sql = new StringBuffer("delete from M_WHITE_LIST "); 
		sql.append(" where user_account =:userAccount and machine_code =:machineCode and stop_flg = 0 and del_flg = 0");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());  
		query.setParameter("userAccount", mWhite.getUser_account());  
		query.setParameter("machineCode", mWhite.getMachine_code());  
	    query.executeUpdate();
	}


	@Override
	public MWhiteList getMWhiteById(final String id) {
		final StringBuffer sql = new StringBuffer("select t.id,t.USER_ACCOUNT,t.machine_code,u.USER_NAME as userName,d.DEPT_NAME as deptName ,u.USER_SEX as userSex from M_WHITE_LIST t "); 
		sql.append("left join t_sys_user u on t.USER_ACCOUNT = u.USER_ACCOUNT and u.stop_flg = 0 and u.del_flg = 0 "); 
		sql.append("left join t_employee e on t.USER_ACCOUNT = e.EMPLOYEE_JOBNO and e.stop_flg = 0 and e.del_flg = 0 "); 
		sql.append("left join t_department d on e.dept_id =d.dept_code  and d.stop_flg = 0 and d.del_flg = 0 "); 
		sql.append("where t.ID = ? and t.stop_flg = 0 and t.del_flg = 0 "); 
		List<MWhiteList> list = (List<MWhiteList>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString())
						.addScalar("id")
						.addScalar("user_account")
						.addScalar("machine_code")
						.addScalar("userName")
						.addScalar("deptName")
						.addScalar("userSex");
					queryObject.setString(0, id);
				return queryObject.setResultTransformer(Transformers.aliasToBean(MWhiteList.class)).list();
			}
		});
		if(list != null && list.size() != 0){
			return list.get(0);
		}
		return null;
	}
	
	private StringBuffer getHql(MWhiteList mWhiteList, StringBuffer hql) {
		if (mWhiteList.getUser_account() != null) {
			hql.append(" and t.user_account like '%");
			hql.append(mWhiteList.getUser_account());
			hql.append("%'");
		}
		return hql;
	}

	@Override
	public void delWhiteById(MWhiteList mWhiteList) {
		StringBuffer sql = new StringBuffer("delete from M_WHITE_LIST "); 
		sql.append(" where id =:id and stop_flg = 0 and del_flg = 0");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());  
		query.setParameter("id", mWhiteList.getId());  
	    query.executeUpdate();
		
	}
}
