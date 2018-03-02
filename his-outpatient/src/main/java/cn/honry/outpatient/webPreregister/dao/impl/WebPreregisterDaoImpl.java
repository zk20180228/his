package cn.honry.outpatient.webPreregister.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterPreregisterNow;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.webPreregister.dao.WebPreregisterDao;
import cn.honry.utils.DateUtils;

@Repository("webPreregisterDao")
@SuppressWarnings({ "all" })

public class WebPreregisterDaoImpl extends HibernateEntityDao<RegisterPreregisterNow> 
implements WebPreregisterDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 根据科室编码和日期查询某科室下医生的排班信息
	 * @param deptCode 科室编码
	 * @param rq 日期
	 * @param firstResult 开始位置
	 * @param rows 每页显示的记录数
	 * @return
	 */
	public List<RegisterScheduleNow> getRegisterList(String deptCode,String rq,int firstResult,int rows)throws Exception{
		Date date =null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd");
		
		date=sdf.parse(rq);
		StringBuffer sbf = new StringBuffer();
		sbf.append("select t.schedule_id as id, t.schedule_doctor as doctor,");
		sbf.append(" (t.schedule_prelimit  - count(r.preregister_id)) as netLimit, t.schedule_midday as midday,");
		sbf.append(" t.schedule_starttime as startTime, t.schedule_endtime as endTime, t.schedule_workdept as scheduleWorkdept,");
		sbf.append(" t.schedule_reggrade as reggrade, t.schedule_doctorname as scheduleDoctorname ");
		sbf.append(" from t_register_schedule_now t left join t_register_preregister_now r ");
		sbf.append(" on t.schedule_midday = r.preregister_midday and trunc(t.schedule_date, 'dd') = trunc(r.PREREGISTER_DATE, 'dd') ");
		sbf.append(" and t.schedule_workdept = r.preregister_dept where t.schedule_isstop = 2 and t.stop_flg = 0 and t.del_flg = 0 ");
		sbf.append(" and t.schedule_class = 1 and t.schedule_workdept = '"+deptCode+"' and t.schedule_prelimit  > 0 ");
		sbf.append(" and trunc(t.schedule_date, 'dd') = to_date('"+rq+"','yyyy-mm-dd') ");
		sbf.append(" group by t.schedule_id, t.schedule_doctor ,t.schedule_prelimit , t.schedule_workdept, t.schedule_midday,");
		sbf.append(" t.schedule_starttime,t.schedule_endtime,t.schedule_reggrade,t.schedule_doctorname");
		SQLQuery queryObject = this.getSession().createSQLQuery(sbf.toString());
		queryObject.addScalar("id").addScalar("doctor").addScalar("startTime").addScalar("endTime").addScalar("scheduleWorkdept")
		.addScalar("reggrade").addScalar("scheduleDoctorname").addScalar("netLimit", Hibernate.INTEGER).addScalar("midday", Hibernate.INTEGER);
		List<RegisterScheduleNow> list = queryObject.setResultTransformer(Transformers.aliasToBean(RegisterScheduleNow.class)).list();
		
		
		
		if(list==null|| list.size()==0){//无排班信息时从排班模板中取数据
			int weekOfDay = DateUtils.getWeekOfDay(rq);
			 
			 StringBuffer hql = new StringBuffer();
			 hql.append("select t.model_id as id, t.model_doctor as modelDoctor, ( t.model_prelimit - count(r.preregister_id)) as modelNetlimit, ");
			 hql.append(" t.model_workdept  as modelWorkdept, t.model_reggrade  as modelReggrade, t.model_starttime as modelStartTime, ");
			 hql.append(" t.model_endtime as modelEndTime, t.model_midday    as modelMidday from t_register_schedulemodel t ");
			 hql.append(" left join t_register_preregister_now r on  t.model_midday = r.preregister_midday ");
			 hql.append(" and t.model_workdept = r.preregister_dept and trunc(r.PREREGISTER_DATE, 'dd') = to_date('"+rq+"', 'yyyy-mm-dd') ");
			 hql.append(" where t.model_class = 1 and t.stop_flg = 0 and t.del_flg = 0  and t.model_workdept = '"+deptCode+"' ");
			 hql.append(" and t.model_week = "+weekOfDay+" and t.model_prelimit > 0 group by  t.model_id  ,");
			 hql.append(" t.model_doctor ,t.model_prelimit,t.model_workdept,t.model_reggrade,t.model_starttime,t.model_endtime,t.model_midday");
			 SQLQuery sql = this.getSession().createSQLQuery(hql.toString());
			 sql.addScalar("id").addScalar("modelDoctor").addScalar("modelNetlimit",Hibernate.INTEGER).addScalar("modelWorkdept").addScalar("modelReggrade").addScalar("modelStartTime")
			 .addScalar("modelEndTime").addScalar("modelMidday",Hibernate.INTEGER);
			 List<RegisterSchedulemodel> list2 = sql.setResultTransformer(Transformers.aliasToBean(RegisterSchedulemodel.class)).list();
			 if(list2!=null && list2.size()>0){
				 for (RegisterSchedulemodel model : list2) {
					 RegisterScheduleNow schedule = new RegisterScheduleNow();
					 schedule.setId(model.getId());
					 schedule.setDoctor(model.getModelDoctor());
					 schedule.setNetLimit(model.getModelNetlimit());
					 schedule.setScheduleWorkdept(model.getModelWorkdept());
					 schedule.setReggrade(model.getModelReggrade());
					 schedule.setStartTime(model.getModelStartTime());
					 schedule.setEndTime(model.getModelEndTime());
					 schedule.setMidday(model.getModelMidday());
					 schedule.setScheduleClass(1);
					 list.add(schedule);
				 }
			 }
		}
		return list;
	}
}
