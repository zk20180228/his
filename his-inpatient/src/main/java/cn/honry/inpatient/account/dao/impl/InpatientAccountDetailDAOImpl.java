package cn.honry.inpatient.account.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientAccountdetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inpatient.account.dao.InpatientAccountDetailDAO;
@SuppressWarnings({"all"})
@Repository("inpatientAccountdetailDAO")
public class InpatientAccountDetailDAOImpl extends HibernateEntityDao<InpatientAccountdetail> implements InpatientAccountDetailDAO{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public void delByParentId(String id) {
		String hql = "delete InpatientAccountdetail ina where ina.inpatientAccount.id = ? ";
		this.excUpdateHql(hql, id);
	}
	
	
}
