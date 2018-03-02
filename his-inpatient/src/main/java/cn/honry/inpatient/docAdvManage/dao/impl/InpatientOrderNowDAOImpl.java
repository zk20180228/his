package cn.honry.inpatient.docAdvManage.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.docAdvManage.dao.InpatientOrderNowDAO;
@Repository("inpatientOrderNowDAO")
@SuppressWarnings({ "all" })
public class InpatientOrderNowDAOImpl extends HibernateEntityDao<InpatientOrderNow> implements InpatientOrderNowDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InpatientOrderNow> getPage(String page, String rows,
			String hql) {
		return super.getPage(hql,page, rows);
	}

	@Override
	public int getTotal(String hql) {
		return super.getTotal(hql);
	}
}
