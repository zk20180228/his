package cn.honry.inpatient.exitNoFee.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientHangbedinfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.exitNoFee.dao.InpatientHangbedinfoDAO;

@Repository("inpatientHangbedinfoDAO")
@SuppressWarnings({"all"})
public class InpatientHangbedinfoDAOImpl  extends HibernateEntityDao<InpatientHangbedinfo> implements InpatientHangbedinfoDAO{
	/**
	 * 注入Session
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
}
