package cn.honry.inner.outpatient.schedule.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.bean.model.RegisterPreregister;
import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.outpatient.schedule.dao.ScheduleInInterDAO;
import cn.honry.utils.DateUtils;
@Repository("scheduleInInterDAO")
public class ScheduleInInterDAOImpl extends HibernateEntityDao<RegisterScheduleNow> implements
		ScheduleInInterDAO {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**  
	 * @Description：  判断数据是否已经在排班表
	 * @Author：zhangjin
	 * @CreateDate：2016-11-21
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public boolean getboolean(Object object, String department, Integer week,
			String doctor, Integer midday,Date day) {
		String dat=DateUtils.formatDateY_M_D(day);
		String hql="select SCHEDULE_ID as id from T_REGISTER_SCHEDULE_NOW where stop_flg=0 and del_flg=0 ";
		if(StringUtils.isNotBlank(department)){
			hql+=" and SCHEDULE_DEPTID='"+department+"' ";
		}
		if(StringUtils.isNotBlank(doctor)){
			hql+=" and SCHEDULE_DOCTOR='"+doctor+"' ";
		}
		if(week!=null){
			hql +=" and SCHEDULE_WEEK="+week;
		}
		if(midday!=null){
			hql+=" and SCHEDULE_MIDDAY="+midday;
		}
		if(day!=null){
			hql+=" and to_char(SCHEDULE_DATE,'yyyy-MM-dd')='"+dat+"' ";
		}
		List<RegisterSchedule> list=this.getSession().createSQLQuery(hql).addScalar("id").setResultTransformer(Transformers.aliasToBean(RegisterSchedule.class)).list();
		if(list!=null&&list.size()>0){
			return true;
		}
		return false;
	}

	/**  
	 * @Description：  判断数据是否已经在排班表
	 * @Author：zhangjin
	 * @CreateDate：2016-12-3
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public List<RegisterScheduleNow> getDaydate(String day) {
		StringBuffer sb=new StringBuffer();
		sb.append(" from RegisterScheduleNow where trunc(date,'dd')=to_date(?,'yyyy-MM-dd') and scheduleClass=1 ");
		
//		sb.append("insert into t_register_doc_source");
//		sb.append("(ID,EMPLOYEE_CODE,EMPLOYEE_NAME,grade_code,");
//		sb.append("grade_name,dept_code,dept_name,midday_code,");
//		sb.append("midday_name,clinic_code,clinic_name,limit_sum,peciallimit_sum,");
//		sb.append("clinic_sum,appflag,isstop,stopreason,createtime) ");
//		sb.append(" select o.schedule_id,o.schedule_doctor,");
//		sb.append("o.schedule_doctorname,o.schedule_reggrade,");
//		sb.append(" g.grade_name,o.schedule_deptid,o.schedule_deptname,");
//		sb.append(" o.schedule_midday,o.schedule_middayname,");
//		sb.append(" o.schedule_clinicid,o.schedule_clinicname,o.schedule_limit,");
//		sb.append(" o.schedule_peciallimit,o.schedule_appflag,o.schedule_isstop,");
//		sb.append(" o.schedule_stopreason,to_date('"+day+"','yyyy-MM-dd HH24:mm:ss')");
//		sb.append("   from t_register_schedule_now o");
//		sb.append("   left join t_Register_Grade g");
//		sb.append("   on g.grade_code=o.schedule_reggrade");
//		sb.append("  where trunc(o.schedule_date,'dd') = to_date('"+day+"','yyyy-MM-dd')");
//		int num=this.getSession().createSQLQuery(sb.toString()).executeUpdate();
//		if(num>0){
//			return "ok";
//		}
//		return "No";
		List<RegisterScheduleNow> list =this.find(sb.toString(), day);
		if(list!=null&&list.size()>0){
				return list;
		}
		return null;
	}
	/**  
	 * @Description： 获取挂号级别
	 * @Author：zhangjin
	 * @CreateDate：2016-12-3
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public Map<String,String> getRegisterGrade() {
		StringBuffer sb=new StringBuffer();
		sb.append("select code as code,name as name  from RegisterGrade where del_flg=0 and stop_flg=0 ");
		List<RegisterGrade> list=this.getSession().createQuery(sb.toString()).setResultTransformer(Transformers.aliasToBean(RegisterGrade.class)).list();
		Map<String,String> map=new HashMap<String, String>();
		if(list!=null&&list.size()>0){
			for(RegisterGrade li:list){
				map.put(li.getCode(), li.getName());
			}
			return map;
		}
		return null;
	}

	/**  
	 * @Description：  获取预约数
	 * @Author：zhangjin
	 * @CreateDate：2016-12-3
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public int getNNt(String department, String doctor, Integer midday,
			Date date) {
		String day=DateUtils.formatDateY_M_D(date);
		StringBuffer sb=new StringBuffer();
		sb.append("select schedule_id as id from T_REGISTER_PREREGISTER_NOW where stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(department)){
			sb.append(" and PREREGISTER_DEPT ='"+department+"'");
		}
		if(StringUtils.isNotBlank(doctor)){
			sb.append(" and PREREGISTER_EXPERT ='"+doctor+"'");
		}
		if(midday!=null){
			sb.append(" and PREREGISTER_MIDDAY ='"+midday+"'");
		}
		if(midday!=null){
			sb.append(" and trunc(PREREGISTER_DATE,'dd') =to_date('"+day+"','yyyy-MM-dd')");
		}
		List<RegisterPreregister> num=this.getSession().createSQLQuery(sb.toString()).addScalar("id").setResultTransformer(Transformers.aliasToBean(RegisterPreregister.class)).list();
		if(num!=null&&num.size()>0){
			return num.size();
		}
		return 0;
	}
	/**  
	 * @Description：  获取参数
	 * @Author：zhangjin
	 * @CreateDate：2016-12-15
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	@Override
	public List<HospitalParameter> getParameter(String str) {
		StringBuffer sb=new StringBuffer();
		sb.append(" from HospitalParameter  ");
		if(StringUtils.isNotBlank(str)){
			sb.append(" where parameterCode=:no");
		}
		List<HospitalParameter> list=this.getSession().createQuery(sb.toString()).setParameter("no", str).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	
}
