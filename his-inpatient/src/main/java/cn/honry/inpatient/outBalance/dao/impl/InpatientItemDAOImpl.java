package cn.honry.inpatient.outBalance.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientItemDAO;

@Repository("inpatientItemDAO")
@SuppressWarnings("all")
public class InpatientItemDAOImpl extends HibernateEntityDao<InpatientItemList> implements InpatientItemDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
