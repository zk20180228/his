package cn.honry.inner.drug.outstore.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.drug.outstore.dao.OutStoreInInterDAO;

@Repository("outStoreInInterDAO")
@SuppressWarnings({ "all" })
public class OutStoreDAOImpl extends HibernateEntityDao<DrugOutstore> implements OutStoreInInterDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	
}
