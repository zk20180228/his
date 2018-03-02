package cn.honry.outpatient.info.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessPayMode;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.info.dao.PayModeInfoDAO;

@Repository("payModeInfoDAO")
@SuppressWarnings({ "all" })
public class PayModeInfoDAOImpl  extends HibernateEntityDao<BusinessPayMode> implements PayModeInfoDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

}
