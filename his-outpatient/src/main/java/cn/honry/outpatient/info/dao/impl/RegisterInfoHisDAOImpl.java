package cn.honry.outpatient.info.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.PatientIdcard;
import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.RegisterInfoHis;
import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.utils.WebUtils;
import cn.honry.utils.DateUtils;
import cn.honry.outpatient.info.dao.RegisterInfoDAO;
import cn.honry.outpatient.info.dao.RegisterInfoHisDAO;

@Repository("registerInfoHisDAO")
@SuppressWarnings({ "all" })
public class RegisterInfoHisDAOImpl extends HibernateEntityDao<RegisterInfoHis> implements RegisterInfoHisDAO {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}


}
