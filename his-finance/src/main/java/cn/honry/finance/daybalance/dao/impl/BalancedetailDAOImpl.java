package cn.honry.finance.daybalance.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterBalancedetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.finance.daybalance.dao.BalancedetailDAO;

@Repository("balancedetailDAO")
@SuppressWarnings({ "all" })
public class BalancedetailDAOImpl extends HibernateEntityDao<RegisterBalancedetail> implements BalancedetailDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
