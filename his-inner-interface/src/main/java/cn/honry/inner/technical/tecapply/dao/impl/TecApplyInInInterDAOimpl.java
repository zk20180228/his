package cn.honry.inner.technical.tecapply.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.TecApply;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.technical.tecapply.dao.TecApplyInInInterDAO;

@Repository("tecApplyInInInterDAO")
@SuppressWarnings({ "all" })
public class TecApplyInInInterDAOimpl extends HibernateEntityDao<TecApply> implements TecApplyInInInterDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
