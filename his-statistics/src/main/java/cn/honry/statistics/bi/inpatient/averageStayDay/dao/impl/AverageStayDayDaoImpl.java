package cn.honry.statistics.bi.inpatient.averageStayDay.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.averageStayDay.dao.AverageStayDayDao;
import cn.honry.statistics.bi.inpatient.averageStayDay.vo.AverageStayDayVo;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;

@Repository("averageStayDayDao")
@SuppressWarnings({"all"})
public class AverageStayDayDaoImpl  extends HibernateEntityDao<AverageStayDayVo> implements AverageStayDayDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<SysDepartment> queryAllDept() {
		String sql="select bd.DEPT_ID as id, bd.DEPT_CODE as deptCode,bd.DEPT_NAME as deptName from T_DEPARTMENT bd where bd.dept_type in ('I')";
		SQLQuery queryObject=this.getSession().createSQLQuery(sql).addScalar("id").addScalar("deptCode").addScalar("deptName");
		List<SysDepartment> bdl=queryObject.setResultTransformer(Transformers.aliasToBean(SysDepartment.class)).list();
		if(bdl!=null&&bdl.size()>0){
			SysDepartment bd=new SysDepartment();
			bd.setId("1");
			bd.setDeptCode("1");
			bd.setDeptName("全部");
			bdl.add(0, bd);
			return bdl;
		}
		return new ArrayList<SysDepartment>();
	}

	
	@Override
	public List<AverageStayDayVo> queryAverageStayDay(String deptCode, String years) {
		String[] time =newDate(years).split(",");
		String date1=time[0];
		String date2=time[1];
		long day=365;
		long workDay=251;
		if(StringUtils.isBlank(years)){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ");
			Date date;
			try {
				date = sdf.parse( date1);
			    day=(new Date().getTime()-date.getTime())/(86400000); //24 * 60 * 60 * 1000=86400000
			    workDay=day-(day/7)*2;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		StringBuffer hql=new StringBuffer();
		hql.append("select distinct d.dept_name as deptName , d.dept_code,ceil(nvl(nvl(s.sum,0)/ s.out,0)) as aveStayDay  from T_DEPARTMENT d ");
		hql.append(" left join ( select r.dept_code,sum(ceil(r.out_date - r.in_date)) as sum ,  count(r.INPATIENT_NO) as out from v_inpatient_info_aveBed r ");
		hql.append(" where  r.out_date between To_date('"+date1+"', 'yyyy-mm-dd hh24-mi-ss') and To_date('"+date2+"', 'yyyy-mm-dd hh24-mi-ss')");
		hql.append(" group by r.dept_code) s  on d.dept_id=s.dept_code where d.del_flg=0 and d.stop_flg=0  and d.dept_type in('I') ");
		if(deptCode!=null){
			if(!"".equals(deptCode)&&!"'1'".equals(deptCode)){
				hql.append(" and d.dept_code in ("+deptCode+")");
			}
		}
		
		SQLQuery queryObject=this.getSession().createSQLQuery(hql.toString())
				.addScalar("deptName").addScalar("aveStayDay",Hibernate.INTEGER);
		List<AverageStayDayVo> averageStayDayVo=queryObject.setResultTransformer(Transformers.aliasToBean(AverageStayDayVo.class)).list();
		if(averageStayDayVo!=null&&averageStayDayVo.size()>0){
			return averageStayDayVo;
		}
		return new ArrayList<AverageStayDayVo>();
		
		
		
	}
	
	public String newDate(String years){
		String date2="";
		String  date1="";
	
		if(StringUtils.isNotBlank(years)){
			 date1=years+"/1/1 00:00:00";
			date2=years+"/12/31 23:59:59";
			
		}
		if(StringUtils.isBlank(years)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ");
			Date date=new Date();
			String dates=sdf.format(date);
			String year=dates.substring(0, 4);
			date1=year+"/1/1 00:00:00";
			date2=dates+" 23:59:59";
			
		}
		return date1+","+date2;
	}
}
