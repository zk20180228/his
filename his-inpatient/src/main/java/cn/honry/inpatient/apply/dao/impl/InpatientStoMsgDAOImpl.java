package cn.honry.inpatient.apply.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientStoMsg;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.apply.dao.InpatientStoMsgDAO;
@Repository("inpatientStoMsgDAO")
@SuppressWarnings({ "all" })
public class InpatientStoMsgDAOImpl extends HibernateEntityDao<InpatientStoMsg> implements InpatientStoMsgDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
