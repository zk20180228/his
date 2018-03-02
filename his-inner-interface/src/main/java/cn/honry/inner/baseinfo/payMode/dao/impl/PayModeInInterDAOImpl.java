package cn.honry.inner.baseinfo.payMode.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessPayMode;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.payMode.dao.PayModeInInterDAO;

@Repository("payModeInInterDAO")
@SuppressWarnings({ "all" })
public class PayModeInInterDAOImpl extends HibernateEntityDao<BusinessPayMode> implements PayModeInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
