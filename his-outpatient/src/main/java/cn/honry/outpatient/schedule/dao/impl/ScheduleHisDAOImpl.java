package cn.honry.outpatient.schedule.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.RegisterScheduleHis;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.utils.DateUtils;
import cn.honry.outpatient.schedule.dao.ScheduleDAO;
import cn.honry.outpatient.schedule.dao.ScheduleHisDAO;

@Repository("scheduleHisDAO")
@SuppressWarnings({ "all" })
public class ScheduleHisDAOImpl extends HibernateEntityDao<RegisterScheduleHis> implements ScheduleHisDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	
}
