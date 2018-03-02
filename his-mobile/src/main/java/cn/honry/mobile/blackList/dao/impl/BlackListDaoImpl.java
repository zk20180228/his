package cn.honry.mobile.blackList.dao.impl;

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

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.blackList.dao.BlackListDao;

@Repository("blackListDao")
@SuppressWarnings({ "all" })
public class BlackListDaoImpl extends HibernateEntityDao<MBlackList> implements BlackListDao{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Integer getTotal(MBlackList blackList) throws Exception {
		StringBuffer hql = new StringBuffer("from MBlackList t where t.stop_flg = 0 and t.del_flg = 0");
		hql = getHql(blackList, hql);
		Integer total = super.getTotal(hql.toString());
		if(total == null){
			return 0;
		}
		return total;
	}

	@Override
	public List<MBlackList> getList(String rows, String page,
			MBlackList blackList) throws Exception {
		StringBuffer sql = new StringBuffer("select t.id,t.USER_ACCOUNT,t.machine_code,u.USER_NAME as userName,d.DEPT_NAME as deptName ,u.USER_SEX as userSex from M_BLACK_LIST t "); 
		sql.append("left join t_sys_user u on t.USER_ACCOUNT = u.USER_ACCOUNT and u.stop_flg = 0 and u.del_flg = 0 "); 
		sql.append("left join t_employee e on t.USER_ACCOUNT = e.EMPLOYEE_JOBNO and e.stop_flg = 0 and e.del_flg = 0 "); 
		sql.append("left join t_department d on e.dept_id =d.dept_code  and d.stop_flg = 0 and d.del_flg = 0 "); 
		sql.append("where t.stop_flg = 0 and t.del_flg = 0 "); 
		final String sqlsString = getHql(blackList, sql).toString();
		List<MBlackList> list = (List<MBlackList>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sqlsString)
						.addScalar("id")
						.addScalar("user_account")
						.addScalar("machine_code")
						.addScalar("userName")
						.addScalar("deptName")
						.addScalar("userSex");
				return queryObject.setResultTransformer(Transformers.aliasToBean(MBlackList.class)).list();
			}
		});
		if(list != null && list.size() != 0){
			return list;
		}
		return new ArrayList<MBlackList>();
	}

	@Override
	public MBlackList findBlackByUserAccunt(MBlackList mBlackList)
			throws Exception {
		String hql = "from MBlackList where user_account = ? and machine_code = ? and stop_flg = 0 and del_flg = 0";
		List<MBlackList> list = super.find(hql, mBlackList.getUser_account(), mBlackList.getMachine_code());
		if(list != null && list.size() != 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void delBlackByUserAccount(MBlackList mBlack) throws Exception {
		StringBuffer sql = new StringBuffer("delete from M_BLACK_LIST "); 
		sql.append(" where user_account =:userAccount and machine_code =:machineCode and stop_flg = 0 and del_flg = 0");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());  
		query.setParameter("userAccount", mBlack.getUser_account());  
		query.setParameter("machineCode", mBlack.getMachine_code());  
	    query.executeUpdate();
	}

	@Override
	public MBlackList getMBlackById(final String id) {
		final StringBuffer sql = new StringBuffer("select t.id,t.USER_ACCOUNT,t.machine_code,u.USER_NAME as userName,d.DEPT_NAME as deptName ,u.USER_SEX as userSex from M_BLACK_LIST t "); 
		sql.append("left join t_sys_user u on t.USER_ACCOUNT = u.USER_ACCOUNT and u.stop_flg = 0 and u.del_flg = 0 "); 
		sql.append("left join t_employee e on t.USER_ACCOUNT = e.EMPLOYEE_JOBNO and e.stop_flg = 0 and e.del_flg = 0 "); 
		sql.append("left join t_department d on e.dept_id =d.dept_code  and d.stop_flg = 0 and d.del_flg = 0 "); 
		sql.append("where t.ID = ? and t.stop_flg = 0 and t.del_flg = 0 "); 
		List<MBlackList> list = (List<MBlackList>) this.getHibernateTemplate().execute(new HibernateCallback() {
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
				return queryObject.setResultTransformer(Transformers.aliasToBean(MBlackList.class)).list();
			}
		});
		if(list != null && list.size() != 0){
			return list.get(0);
		}
		return null;
	}
	private StringBuffer getHql(MBlackList blackList, StringBuffer hql) {
		if (blackList.getUser_account() != null) {
			hql.append(" and t.user_account like '%");
			hql.append(blackList.getUser_account());
			hql.append("%'");
		}
		return hql;
	}

	@Override
	public void delBlackById(MBlackList mBlackList) {
		StringBuffer sql = new StringBuffer("delete from M_BLACK_LIST "); 
		sql.append(" where id =:id and stop_flg = 0 and del_flg = 0");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());  
		query.setParameter("id", mBlackList.getId());  
	    query.executeUpdate();
		
	}
}
