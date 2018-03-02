package cn.honry.inner.technical.taDetail.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.TecTaDetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.technical.taDetail.dao.TecTaDetailInInterDAO;

@Repository("tecTaDetailInInterDAO")
@SuppressWarnings({ "all" })
public class TecTaDetailInInterDAOImpl extends HibernateEntityDao<TecTaDetail> implements TecTaDetailInInterDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
