package cn.honry.outpatient.schedule.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javassist.bytecode.analysis.Type;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.employee.dao.EmployeeInInterDAO;
import cn.honry.outpatient.schedule.dao.ScheduleDAO;
import cn.honry.outpatient.scheduleModel.dao.ScheduleModelDAO;
import cn.honry.utils.DateUtils;

@Repository("scheduleDAO")
@SuppressWarnings({ "all" })
public class ScheduleDAOImpl extends HibernateEntityDao<RegisterScheduleNow> implements ScheduleDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value = "employeeInInterDAO")
	private EmployeeInInterDAO employeeInInterDAO;
	@Autowired
	@Qualifier(value = "scheduleModelDAO")
	private ScheduleModelDAO scheduleModelDAO;

	@Override
	public List<RegisterScheduleNow> getPage(RegisterScheduleNow entity,String page, String rows) {
		if(entity.getDate()==null||StringUtils.isBlank(entity.getDepartment())){
			return new ArrayList<RegisterScheduleNow>();
		}
		String hql = joint(entity);
		if(hql==null){
			return new ArrayList<RegisterScheduleNow>();
		}
		int p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):20;
		hql = " select * from (select tab.*,rownum rn from ("+hql+" ) tab where rownum<="+p*r+") where rn> "+(p-1)*r;
		SQLQuery query = this.getSession().createSQLQuery(hql);
		query.addScalar("newpeople",Hibernate.INTEGER).addScalar("scheduleWorkdept").addScalar("id").addScalar("scheduleClass",Hibernate.INTEGER).addScalar("doctor").addScalar("department").addScalar("clinic").addScalar("midday",Hibernate.INTEGER).addScalar("startTime").addScalar("endTime");
		query.addScalar("limit",Hibernate.INTEGER).addScalar("preLimit",Hibernate.INTEGER).addScalar("phoneLimit",Hibernate.INTEGER).addScalar("netLimit",Hibernate.INTEGER).addScalar("speciallimit",Hibernate.INTEGER).addScalar("stoprEason").addScalar("wilpeople",Hibernate.INTEGER);
		List<RegisterScheduleNow> list = query.setResultTransformer(Transformers.aliasToBean(RegisterScheduleNow.class)).list();
		return list;
	}

	@Override
	public int getTotal(RegisterScheduleNow entity) {
		if(entity.getDate()==null||StringUtils.isBlank(entity.getDepartment())){
			return 0;
		}
		String hql = joint(entity);
		if(hql==null){
			return 0;
		}
		return super.getSqlTotal(hql);
	}
	
	public String joint(RegisterScheduleNow schedule){
		StringBuffer sb = new StringBuffer();
		sb.append(" select max(nvl(d.clinic_sum,0)) as newpeople,s.SCHEDULE_ID as id,s.schedule_workdept as scheduleWorkdept, s.SCHEDULE_CLASS as scheduleClass,");
		sb.append(" s.SCHEDULE_DOCTOR as doctor,s.SCHEDULE_DEPTID as department,s.SCHEDULE_CLINICID as clinic,s.SCHEDULE_MIDDAY as midday,");
		sb.append(" s.SCHEDULE_STARTTIME as startTime,s.SCHEDULE_ENDTIME as endTime,  nvl(s.SCHEDULE_LIMIT,0) as limit,nvl(s.SCHEDULE_PRELIMIT,0) as preLimit,");
		sb.append(" nvl(s.SCHEDULE_PHONELIMIT,0) as phoneLimit,nvl(s.SCHEDULE_NETLIMIT,0) as netLimit, nvl(s.SCHEDULE_PECIALLIMIT,0)  as speciallimit,");
		sb.append(" s.SCHEDULE_STOPREASON as stoprEason ,count(p.schedule_id)   as wilpeople");
		sb.append(" from t_Register_Schedule_Now s left join t_Register_Doc_Source d on d.DEPT_CODE = s.SCHEDULE_WORKDEPT and ");
		sb.append(" s.SCHEDULE_DOCTOR = d.EMPLOYEE_CODE and d.MIDDAY_CODE = s.SCHEDULE_MIDDAY and trunc(d.Reg_Date, 'dd') = trunc(s.schedule_date, 'dd')");
		sb.append(" left join  T_REGISTER_PREREGISTER_NOW p on s.schedule_id = p.schedule_id  and p.stop_flg=0 and p.del_flg=0 ");
		sb.append(" WHERE s.del_flg = 0 AND  trunc(s.schedule_date,'dd')=to_date('"+DateUtils.formatDateY_M_D(schedule.getDate())+"','yyyy-mm-dd') AND s.schedule_workdept = '"+schedule.getDepartment()+"' ");
		if(schedule!=null&&StringUtils.isNotBlank(schedule.getSearch())){
			sb.append(" AND (s.schedule_doctor IN (SELECT e.employee_jobno FROM t_Employee e WHERE e.employee_name LIKE '%"+schedule.getSearch()+"%' ");
			sb.append(" OR e.employee_oldname LIKE '%"+schedule.getSearch()+"%' OR e.employee_pinyin LIKE '%"+schedule.getSearch().toUpperCase()+"%' ");
			sb.append(" OR e.employee_wb LIKE '%"+schedule.getSearch().toUpperCase()+"%' OR e.employee_inputcode LIKE '%"+schedule.getSearch().toUpperCase()+"%') ");
			sb.append(" OR s.schedule_clinicid IN (SELECT c.id FROM t_Clinic c WHERE c.clinic_name LIKE '%"+schedule.getSearch()+"%' ");
			sb.append(" OR c.clinic_Piyin LIKE '%"+schedule.getSearch().toUpperCase()+"%' ");
			sb.append(" OR c.clinic_Wb LIKE '%"+schedule.getSearch().toUpperCase()+"%' ");
			sb.append(" OR c.clinic_Inputcode LIKE '%"+schedule.getSearch().toUpperCase()+"%'))");
		}
		sb.append(" group by s.schedule_workdept,s.SCHEDULE_ID,s.SCHEDULE_CLASS, s.SCHEDULE_DOCTOR, s.SCHEDULE_DEPTID,");
		sb.append(" s.SCHEDULE_CLINICID,s.SCHEDULE_MIDDAY , s.SCHEDULE_STARTTIME, s.SCHEDULE_ENDTIME,s.SCHEDULE_LIMIT, ");
		sb.append(" s.SCHEDULE_PRELIMIT,s.SCHEDULE_PHONELIMIT , s.SCHEDULE_NETLIMIT ,s.SCHEDULE_PECIALLIMIT,s.SCHEDULE_STOPREASON ");
		return sb.toString();
	}


	@Override
	public List<RegisterScheduleNow> getzj(String id,String time) {
		String hql = "";
		hql = "FROM RegisterScheduleNow s WHERE s.del_flg = 0 AND TO_CHAR(s.date,'YYYY-MM-DD') = '"
				+ time + "'";
		if (id ==null){
			return new ArrayList<RegisterScheduleNow>();
		}
		
		hql = hql+" AND s.doctor = '"+id+"' ";
		hql = hql+" ORDER BY s.midday";
		List<RegisterScheduleNow> scheduleList = super.findByObjectProperty(hql, null);
		if(scheduleList.size()==0 || scheduleList==null){
			 hql="FROM RegisterSchedulemodel sm WHERE sm.modelDoctor= '"+id+"' AND sm.del_flg = 0 AND sm.modelWeek = '"+DateUtils.getWeekOfDay(time)+"'";
			 List<RegisterSchedulemodel> schedulemodelList = scheduleModelDAO.findByObjectProperty(hql, null);
			 List<RegisterScheduleNow> rsList = new ArrayList<RegisterScheduleNow>();
			 for(RegisterSchedulemodel modl : schedulemodelList){
				 RegisterScheduleNow registerSchedule=new RegisterScheduleNow();
				 registerSchedule.setDepartment(modl.getDepartment());
				 registerSchedule.setDoctor(modl.getModelDoctor());
				 registerSchedule.setPhoneLimit(modl.getModelPhonelimit());
				 registerSchedule.setNetLimit(modl.getModelNetlimit());
				 registerSchedule.setPreLimit(modl.getModelPrelimit());
				 rsList.add(registerSchedule);
			 }
			 return rsList;
		}
		return scheduleList;
	}


	@Override
	public List<RegisterScheduleNow> findSchedule(String dept, String grade,String emplo) {
		String hql = "FROM RegisterScheduleNow s WHERE TO_CHAR(s.date,'YYYY-MM-DD') = '"
				+ DateUtils.formatDateY_M_D(DateUtils.getCurrentTime()) + "'";
		if(StringUtils.isNotBlank(dept)){
			hql = hql+" AND s.department = '"+dept+"' ";
		}
		if(StringUtils.isNotBlank(grade)){
			String gradehql="FROM SysEmployee e WHERE e.title = '"+grade+"' ";
			List<SysEmployee>  employeeList = employeeInInterDAO.findByObjectProperty(gradehql, null);
			String eIds = "";
			for(SysEmployee employee: employeeList){
				eIds = eIds+employee.getId()+",";
			}
			eIds = eIds.replaceAll(",", "','");
			hql = hql+" AND s.doctor in ('"+eIds+"') ";
		}
		if(StringUtils.isNotBlank(emplo)){
			hql = hql+" AND s.doctor = '"+emplo+"' ";
		}
		List<RegisterScheduleNow> scheduleList=super.findByObjectProperty(hql, null);
		if(scheduleList==null||scheduleList.size()<=0){
			return new ArrayList<RegisterScheduleNow>();
		}
		return scheduleList;
	}

	/**  
	 *  
	 * @Description：  根据科室,日期,医生,午别查询该记录是否存在 如果id为空则查询全部符合条件的信息 如果id不为空查询除此id外的全部信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-17 下午04:30:05  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-17 下午04:30:05  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public boolean findModelByDateAndDoctor(String id, String deptId,Date date, String doctorId, Integer midday) {
		String hql = "SELECT count(id) FROM RegisterScheduleNow r WHERE " +
		" TO_CHAR(r.date,'YYYY-MM-DD') = '"+DateUtils.formatDateY_M_D(date)+
		"' AND r.doctor = '"+doctorId+"' AND r.midday = "+midday+" " +
		"AND r.stop_flg = 0 AND r.del_flg = 0 ";
		if(StringUtils.isNotBlank(id)){
			hql += " AND r.id != '"+id+"'";
		}
		Long count = (Long) getSession().createQuery(hql).uniqueResult();
		if(count>0){
			return true;
		}
		return false;
	}
	public List<RegisterScheduleNow> findOldSchedule(String id) {
		String sql = "select t.createtime as createTime from t_register_schedule_now t where t.schedule_id='"+id+"'";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("createTime",Hibernate.TIMESTAMP);
		List<RegisterScheduleNow> list = query.setResultTransformer(Transformers.aliasToBean(RegisterScheduleNow.class)).list();
		return list;
	}
	
	/**
	 * 根据排班id获取网络限额
	 * @param id
	 * @return
	 */
	public Integer getNetLimit(String id){
		//2017-02-17 改为预约限额
		String hql="select t.preLimit from RegisterScheduleNow t where t.id=? ";
		Integer result = (Integer) this.createQuery(hql, id).uniqueResult();
		if(result==null ||result<0){
			return 0;
		}
		return result;
	}
	
	/**
	 * 根据排班id获取挂号限额
	 * @param id
	 * @return
	 */
	public Integer getLimit(String id){
		String hql="select t.limit from RegisterScheduleNow t where t.id=? ";
		Integer result = (Integer) this.createQuery(hql, id).uniqueResult();
		if(result==null ||result<0){
			return 0;
		}
		return result;
	}
}

