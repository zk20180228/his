package cn.honry.inner.baseinfo.invoiceInfo.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.invoiceInfo.InvoiceInfoInInterDAO;

@Repository("invoiceInfoInInterDAO")
@SuppressWarnings({ "all" })
public class InvoiceInfoInInterDAOImpl extends HibernateEntityDao<FinanceInvoiceInfoNow> implements InvoiceInfoInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
