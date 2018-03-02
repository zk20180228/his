package cn.honry.outpatient.info.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.VIdcardPatient;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.info.dao.ViewIdcardPatientsDAO;

@Repository("viewIdcardPatientsDAO")
@SuppressWarnings({ "all" })
public class ViewIdcardPatientsDAOImpl extends HibernateEntityDao<VIdcardPatient> implements ViewIdcardPatientsDAO {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

}

