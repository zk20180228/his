package cn.honry.inpatient.recall.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientDayreportDetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.recall.dao.InpatientReportDetailDAO;

@Repository("inpatientReportDetailDAO")
@SuppressWarnings({ "all" })
public class InpatientReportDetailDAOImpl extends HibernateEntityDao<InpatientDayreportDetail> implements InpatientReportDetailDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
}
