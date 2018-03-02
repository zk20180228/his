package cn.honry.outpatient.preregister.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterPreregisterHis;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.preregister.dao.PreregisterHisDao;

@Repository("reregisterHisDAO")
@SuppressWarnings({ "all" })

public class PreregisterHisDaoImpl extends HibernateEntityDao<RegisterPreregisterHis> implements PreregisterHisDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	
}
