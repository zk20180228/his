package cn.honry.outpatient.info.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.FinanceInvoicedetailNow;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.info.dao.InvoicedetailInfoDAO;

@Repository("invoicedetailInfoDAO")
@SuppressWarnings({ "all" })
public class InvoicederaiInfoDAOImpl extends HibernateEntityDao<FinanceInvoicedetailNow> implements InvoicedetailInfoDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
}
