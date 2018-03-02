package cn.honry.statistics.bi.statisticalSetting.dao.Impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiIndexSet;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.statisticalSetting.dao.BiIndexSetDAO;
@Repository("biIndexSetDAO")
@SuppressWarnings({"all"})
public class BiIndexSetDAOImpl extends HibernateEntityDao<BiIndexSet> implements BiIndexSetDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
