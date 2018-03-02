package cn.honry.inpatient.outBalance.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientChangeprepay;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientBalanceHeadDAO;
import cn.honry.inpatient.outBalance.dao.InpatientChangeprepayDAO;

@Repository("inpatientChangeprepayDAO")
@SuppressWarnings("all")
public class InpatientChangeprepayDAOImpl extends HibernateEntityDao<InpatientChangeprepay> implements InpatientChangeprepayDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
