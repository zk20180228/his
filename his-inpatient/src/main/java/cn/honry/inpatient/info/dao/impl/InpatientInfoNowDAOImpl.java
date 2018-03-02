package cn.honry.inpatient.info.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.info.dao.InpatientInfoNowDAO;


/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
@Repository("inpatientInfoNowDAO")
@SuppressWarnings({"all"})
public  class InpatientInfoNowDAOImpl extends HibernateEntityDao<InpatientInfoNow> implements InpatientInfoNowDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<InpatientInfoNow> getinfopage(String hql, String page, String rows) {
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getinfoTotal(String hql) {
		return super.getTotal(hql);
	}
	
	
}
