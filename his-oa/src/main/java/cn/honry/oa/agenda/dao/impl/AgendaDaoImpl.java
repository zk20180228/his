package cn.honry.oa.agenda.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.Schedule;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.oa.agenda.dao.AgendaDao;
@Repository("agendaDao")
@SuppressWarnings({"all"})
public class AgendaDaoImpl extends HibernateEntityDao<Schedule> implements AgendaDao{
	        // 为父类HibernateDaoSupport注入sessionFactory的值
			@Resource(name = "sessionFactory")
			public void setSuperSessionFactory(SessionFactory sessionFactory) {
				super.setSessionFactory(sessionFactory);
			}
			@Resource
			private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
			
			@Override
			public List<Schedule> qeryScheduleList(String id) {
				String hql="from Schedule s where s.del_flg = 0 and s.stop_flg=0 and s.userId=:userId";
			    List<Schedule> list = this.getSession().createQuery(hql).setParameter("userId",id).list();
				return list;
			}

			@Override
			public void save(Schedule schedule) throws Exception{
				this.getHibernateTemplate().save(schedule);
			}

			@Override
			public Schedule queryById(String id) {
				String hql="from Schedule s where s.del_flg = 0 and s.stop_flg=0 and s.id=:id";
			    Schedule schedule = (Schedule) this.getSession().createQuery(hql).setParameter("id",id).uniqueResult();
				return schedule;
			}


			@Override
			public void update(Schedule schedule) throws Exception{
               this.getHibernateTemplate().update(schedule);				
			}

			@Override
			public List<Schedule> queryScheduleList(String id,String page,String rows) {
				Integer p=Integer.parseInt(page);
				Integer r=Integer.parseInt(rows);
				Integer s=(p-1)*r+1;
				Integer e=s+r-1;
				String sql="select * from (select m.*,rownum rn  from (select t.schedule_codes id,t.schedule_titile title,t.schedule_start_time end,"
						+ "t.all_day_flg  dayFlg,t.schedule_time time,t.is_zdy  isZDY,t.schedule_remark  remark,t.is_finish  isFinish from m_schedule t  where t.del_flg = 0 ";
				if(StringUtils.isNotBlank(id)){
					sql+="  and t.user_account='"+id+"'";
				}		
				sql+= " order by t.schedule_start_time desc) m ) where rn between "+s+" and "+e;
				List list = this.getSession().createSQLQuery(sql).addScalar("id").addScalar("title").addScalar("end",Hibernate.TIMESTAMP).addScalar("dayFlg",Hibernate.INTEGER)
				.addScalar("isFinish",Hibernate.INTEGER).addScalar("time",Hibernate.TIMESTAMP).addScalar("isZDY").addScalar("remark").setResultTransformer(Transformers.aliasToBean(Schedule.class)).list();
				return list;
			}

			@Override
			public int queryScheduleListTotal(String id) {
				String sql=" select count(1) from  m_schedule t where t.del_flg=0  ";
				if(StringUtils.isNotBlank(id)){
					sql+=" and  t.user_account='"+id+"'";
				}
				Object obj = this.getSession().createSQLQuery(sql).uniqueResult();
				return  Integer.parseInt(obj.toString());
			}
			
			
			
			
			
}
