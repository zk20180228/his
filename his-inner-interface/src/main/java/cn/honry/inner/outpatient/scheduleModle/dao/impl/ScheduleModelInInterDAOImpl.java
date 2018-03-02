package cn.honry.inner.outpatient.scheduleModle.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.scheduleModle.dao.ScheduleModleInInterDAO;
@Repository("scheduleModleInInterDAO")
public class ScheduleModelInInterDAOImpl extends
		HibernateEntityDao<RegisterSchedulemodel> implements
		ScheduleModleInInterDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 * @Description：  查询
	 * @Author：zhangjin
	 * @CreateDate：2016-11-21
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public List<RegisterSchedulemodel> getmodelList(int scheduleModelhql) {
		StringBuffer sb=new StringBuffer();
		sb.append("select d.dept_name as deptName,m.employee_name empName,c.clinic_name as clinName,");
		sb.append(" y.code_name as codeName, t.model_id as id,");
		sb.append(" t.model_doctor as modelDoctor, t.model_week as modelWeek,t.model_midday as modelMidday,");
		sb.append("t.model_starttime as modelStartTime,");
		sb.append(" t.model_endtime as modelEndTime,t.model_type as modeType,t.model_deptid as department,");
		sb.append("t.model_clinicid as clinic,t.model_limit as modelLimit,t.model_prelimit as modelPrelimit,");
		sb.append("t.model_phonelimit as modelPhonelimit,t.model_netlimit as modelNetlimit,");
		sb.append("t.model_speciallimit as modelSpeciallimit,t.model_appflag as modelAppflag,");
		sb.append("t.model_reggrade as modelReggrade,t.model_remark as modelRemark,");
		sb.append("t.model_workdept as modelWorkdept,t.model_class as modelClass from  t_Register_Schedulemodel t "); 
		sb.append(" left join t_Department d on t.model_deptid=d.dept_code ");
		sb.append(" left join t_employee m on t.model_doctor=m.employee_jobNo ");
		sb.append(" left join t_Clinic c on c.id=t.model_clinicid ");
		sb.append(" left join t_business_dictionary  y on y.code_encode=t.model_midday ");
		sb.append(" where y.code_type='midday' and t.stop_flg = 0 AND t.del_flg = 0 ");
		if(scheduleModelhql!=0){
			sb.append(" and  t.model_week='"+scheduleModelhql+"' ");
		}
		SQLQuery query=this.getSession().createSQLQuery(sb.toString())
				 .addScalar("deptName").addScalar("empName").addScalar("clinName").addScalar("codeName")
				 .addScalar("id").addScalar("modelDoctor").addScalar("modelWeek",Hibernate.INTEGER)
				 .addScalar("modelMidday",Hibernate.INTEGER).addScalar("modelStartTime")
				 .addScalar("modelEndTime").addScalar("modeType",Hibernate.INTEGER).addScalar("department")
				 .addScalar("clinic").addScalar("modelLimit",Hibernate.INTEGER)
				 .addScalar("modelPrelimit",Hibernate.INTEGER).addScalar("modelPhonelimit",Hibernate.INTEGER)
				 .addScalar("modelNetlimit",Hibernate.INTEGER).addScalar("modelSpeciallimit",Hibernate.INTEGER)
				 .addScalar("modelAppflag",Hibernate.INTEGER).addScalar("modelReggrade").addScalar("modelRemark")
				 .addScalar("modelWorkdept").addScalar("modelClass",Hibernate.INTEGER);
			List<RegisterSchedulemodel> list=query.setResultTransformer(Transformers.aliasToBean(RegisterSchedulemodel.class)).list();
		 if(list!=null&&list.size()>0){
			 return list;
		 }
		return null;
	}
}
