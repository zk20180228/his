package cn.honry.inpatient.outBalance.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.outBalance.dao.InpatientInPrepayDAO;
import cn.honry.inpatient.outBalance.dao.InpatientMedicineDAO;

@Repository("inpatientMedicineDAO")
@SuppressWarnings("all")
public class InpatientMedicineDAOImpl extends HibernateEntityDao<InpatientMedicineList> implements InpatientMedicineDAO{
	@Resource(name="sessionFactory")
	private void setHibernateSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
