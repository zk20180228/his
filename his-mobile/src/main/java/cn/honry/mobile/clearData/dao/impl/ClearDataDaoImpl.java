package cn.honry.mobile.clearData.dao.impl;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.mobile.clearData.dao.ClearDataDao;
@Repository("clearDataDaoDao")
public class ClearDataDaoImpl extends HibernateEntityDao<MBlackList> implements ClearDataDao{
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void clearSchedule() throws Exception {
		final String sql = "delete from m_schedule";
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				return queryObject.executeUpdate();
			}
		});
	}

	@Override
	public void clearNotepad() throws Exception {
		final String sql = "delete from m_advice ";
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				return queryObject.executeUpdate();
			}
		});
	}

	@Override
	public void clearAdvice() throws Exception {
		final String sql = "delete from m_notepad";
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				return queryObject.executeUpdate();
			}
		});
	}

	@Override
	public void clearTodo() throws Exception {
		final String sql = "delete from m_todo";
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				return queryObject.executeUpdate();
			}
		});
	}

	@Override
	public void sendMes() throws Exception {
		final String sql = "update  ofbatchpush set  STATUS=1  where body like '%DB更新%' ";
		this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				return queryObject.executeUpdate();
			}
		});
		
	}
}
