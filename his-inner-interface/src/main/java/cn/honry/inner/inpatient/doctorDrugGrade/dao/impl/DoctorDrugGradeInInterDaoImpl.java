package cn.honry.inner.inpatient.doctorDrugGrade.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDruggraDecontraStrank;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.doctorDrugGrade.dao.DoctorDrugGradeInInterDAO;

@Repository("doctorDrugGradeInInterDAO")
@SuppressWarnings({"all"})
public class DoctorDrugGradeInInterDaoImpl extends HibernateEntityDao<SysDruggraDecontraStrank> implements DoctorDrugGradeInInterDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	
}
