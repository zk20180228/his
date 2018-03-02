package cn.honry.inner.baseinfo.invoicedetail.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.invoicedetail.InvoicedetailInInterDAO;
@Repository("invoicedetailInInterDAO")
@SuppressWarnings({ "all" })
public class InvoicedetailInInterDAOImpl extends HibernateEntityDao<FinanceInvoicedetailNow> implements InvoicedetailInInterDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
