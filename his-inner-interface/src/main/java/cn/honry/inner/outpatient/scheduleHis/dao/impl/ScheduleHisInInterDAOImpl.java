package cn.honry.inner.outpatient.scheduleHis.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterScheduleHis;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.scheduleHis.dao.ScheduleHisInInterDAO;
@Repository("scheduleHisInInterDAOImpl")
public class ScheduleHisInInterDAOImpl extends HibernateEntityDao<RegisterScheduleHis> implements ScheduleHisInInterDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	
}
