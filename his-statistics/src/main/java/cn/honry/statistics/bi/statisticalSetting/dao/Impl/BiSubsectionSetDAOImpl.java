package cn.honry.statistics.bi.statisticalSetting.dao.Impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiSubsectionSet;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.statisticalSetting.dao.BiSubsectionSetDAO;

@Repository("biSubsectionSetDAO")
@SuppressWarnings({"all"})
public class BiSubsectionSetDAOImpl extends HibernateEntityDao<BiSubsectionSet> implements BiSubsectionSetDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
