package cn.honry.inpatient.doctorAdvice.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.doctorAdvice.dao.InpatientExecbillDAO;
@Repository("inpatientExecbillDAO")
@SuppressWarnings({ "all" })
public class InpatientExecbillDAOImpl extends HibernateEntityDao<InpatientExecbill> implements InpatientExecbillDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
}
