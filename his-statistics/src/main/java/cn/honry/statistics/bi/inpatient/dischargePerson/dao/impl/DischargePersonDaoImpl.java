package cn.honry.statistics.bi.inpatient.dischargePerson.dao.impl;

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

import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.dischargePerson.dao.DischargePersonDao;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;

@Repository("dischargePersonDao")
@SuppressWarnings({"all"})
public class DischargePersonDaoImpl extends HibernateEntityDao<BiInpatientInfo> implements DischargePersonDao{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public List<DischargePersonVo> queryDischargePersonList(String deptCode,String years, String quarters, String months, String days) {
		StringBuffer hql=new StringBuffer();
		String date2="";
		String  date1="";
		if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(quarters))){
			String month1="";
			String month2="";
			if("1".equals(quarters)){
				month1+=1;
				month2+=3;
				date2+=years+"/"+month2+"/31 23:59:59";
			}else if("2".equals(quarters)){
				month1+=4;
				month2+=6;
				date2+=years+"/"+month2+"/30 23:59:59";
			}else if("3".equals(quarters)){
				month1+=7;
				month2+=9;
				date2+=years+"/"+month2+"/30 23:59:59";
			}else{
				month1+=10;
				month2+=12;
				date2+=years+"/"+month2+"/31 23:59:59";
			}
			 date1=years+"/"+month1+"/1 00:00:00";
		}
		
		if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(months))){
			date1=years+"/"+months+"/1 00:00:00";
			if("1".equals(months)||"3".equals(months)||"5".equals(months)||"7".equals(months)||"8".equals(months)||"10".equals(months)||"12".equals(months)){
				 date2=years+"/"+months+"/31 23:59:59";
			}else if("2".equals(months)){
				 date2=years+"/"+months+"/28 23:59:59";
			}else{
				 date2=years+"/"+months+"/30 23:59:59";
			}
		}
		
		if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(months))&&(StringUtils.isNotBlank(days))){
			 date1=years+"/"+months+"/"+days+" 00:00:00";
			date2=years+"/"+months+"/"+days+" 23:59:59";
		}
		if((StringUtils.isNotBlank(years))&&(StringUtils.isBlank(quarters))&&(StringUtils.isBlank(months))&&(StringUtils.isBlank(days))){
			 date1=years+"/1/1 00:00:00";
			date2=years+"/12/31 23:59:59";
			
		}
		
			hql.append("select  d.dept_name as deptName ,d.dept_code as  deptcode  , nvl(s.sum,0)  as hospitalizationTime ,  nvl(a.sum,0) as dischargePerson ,  nvl(trunc((a.sum/s.sum)*100,2),0)|| '%' as percentage from T_DEPARTMENT d  left join   (select sum(z.sum + x.sum) as sum,x.dept_code ");
			hql.append(" from (select count(t.inpatient_no) as sum,t.dept_code  from v_INPATIENT_INFO_DisPerson t where t.inpatient_time between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and  to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by t.dept_code) x ");
			hql.append(" left join  (select count(t1.inpatient_no) as sum,t1.dept_code from v_INPATIENT_INFO_DisPerson t1 where t1.in_state = 'O'  and T1.OUT_DATE between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss')");
			hql.append(" AND T1.inpatient_time NOT between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by t1.dept_code) z on x.dept_code=z.dept_code   group by x.dept_code) s  on d.dept_code=s.dept_code left join  ");
			hql.append(" (select count(r.inpatient_no) as sum,r.dept_code from v_INPATIENT_INFO_DisPerson r  where r.in_state = 'O' and r.OUT_DATE between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by r.dept_code) a  on d.dept_code=a.dept_code where  d.dept_type='I' and d.stop_flg = 0");
			
			
			if(!"".equals(deptCode)&&!("'1'").equals(deptCode)){
				hql.append(" and  d.dept_code in ("+deptCode+")");
			}
		
		
		SQLQuery queryObject=this.getSession().createSQLQuery(hql.toString())
				.addScalar("deptName").addScalar("hospitalizationTime",Hibernate.INTEGER).addScalar("dischargePerson",Hibernate.INTEGER).addScalar("percentage");
		List<DischargePersonVo> dischargePersonVo=queryObject.setResultTransformer(Transformers.aliasToBean(DischargePersonVo.class)).list();
		if(dischargePersonVo!=null&&dischargePersonVo.size()>0){
			return dischargePersonVo;
		}
		
		return new ArrayList<DischargePersonVo>();
	}
	
	@Override
	public List<SysDepartment> queryAllDept(String type) {
		String sql=null;
		if("1".equals(type)){
			sql="select bd.DEPT_ID as id, bd.DEPT_CODE as deptCode,bd.DEPT_NAME as deptName from T_DEPARTMENT bd where bd.dept_type='I'";
		}else{
			sql="select bd.DEPT_ID as id, bd.DEPT_CODE as deptCode,bd.DEPT_NAME as deptName from T_DEPARTMENT bd ";
		}
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
	public List<DischargePersonVo> loadPersonList(String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd ");
		String  date1="";
		String  date2="";
		Date date=new Date();
		String dates=sdf.format(date);
		if("0".equals(type)){
			date1=dates+" 00:00:00";
			date2=dates+" 23:59:59";
		}else if("1".equals(type)){
			String year=dates.substring(0, 4);
			Integer years=Integer.parseInt(year)-1;
			date1=years+"/"+dates.substring(5, 10)+" 00:00:00";
			date2=years+"/"+dates.substring(5, 10)+" 23:59:59";
		}else {
			String months=dates.substring(5, 7);
			Integer month=Integer.parseInt(months)-1;
			date1=dates.substring(0, 4)+"/"+month+dates.substring(7,10 )+" 00:00:00";
			date2=dates.substring(0, 4)+"/"+month+dates.substring(7, 10)+" 23:59:59";
		}
	
		
		
		StringBuffer hql=new StringBuffer();

		hql.append("select  d.dept_name as deptName ,d.dept_code as  deptcode  ,nvl(s.sum,0)  as hospitalizationTime , nvl(a.sum,0)  as dischargePerson ,  nvl(trunc((a.sum/s.sum)*100,2),0)|| '%' as percentage from T_DEPARTMENT d  left join   (select sum(z.sum + x.sum) as sum,x.dept_code ");
		hql.append(" from (select count(t.inpatient_no) as sum,t.dept_code  from v_INPATIENT_INFO_DisPerson t where t.inpatient_time between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and  to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by t.dept_code) x ");
		hql.append(" left join  (select count(t1.inpatient_no) as sum,t1.dept_code from v_INPATIENT_INFO_DisPerson t1 where t1.in_state = 'O'  and T1.OUT_DATE between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss')");
		hql.append(" AND T1.inpatient_time NOT between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by t1.dept_code) z on x.dept_code=z.dept_code   group by x.dept_code) s  on d.dept_code=s.dept_code left join  ");
		hql.append(" (select count(r.inpatient_no) as sum,r.dept_code from v_INPATIENT_INFO_DisPerson r  where r.in_state = 'O' and r.OUT_DATE between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by r.dept_code) a  on d.dept_code=a.dept_code where  d.dept_type='I' and d.stop_flg = 0");
		
	
		SQLQuery queryObject=this.getSession().createSQLQuery(hql.toString())
				.addScalar("deptName").addScalar("hospitalizationTime",Hibernate.INTEGER).addScalar("dischargePerson",Hibernate.INTEGER).addScalar("percentage");
		List<DischargePersonVo> dischargePersonVo=queryObject.setResultTransformer(Transformers.aliasToBean(DischargePersonVo.class)).list();
		if(dischargePersonVo!=null&&dischargePersonVo.size()>0){
			return dischargePersonVo;
		}
		return new ArrayList<DischargePersonVo>();
	}
	
	
	@Override
	public List<DischargePersonVo> loadBarPersonList(String type,String deptCode,String years, String quarters, String months, String days) {
		StringBuffer hql=new StringBuffer();
		String  date1="";
		String  date2="";
		if("1".equals(type)){
			if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(months))){
				Integer year=Integer.parseInt(years)-1;
				date1=year+"/"+months+"/1 00:00:00";
				if("1".equals(months)||"3".equals(months)||"5".equals(months)||"7".equals(months)||"8".equals(months)||"10".equals(months)||"12".equals(months)){
					 date2=year+"/"+months+"/31 23:59:59";
				}else if("2".equals(months)){
					 date2=year+"/"+months+"/28 23:59:59";
				}else{
					 date2=year+"/"+months+"/30 23:59:59";
				}
			}
			
			if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(months))&&(StringUtils.isNotBlank(days))){
				Integer year=Integer.parseInt(years)-1;
				date1=year+"/"+months+"/"+days+" 00:00:00";
				date2=year+"/"+months+"/"+days+" 23:59:59";
			}
			if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(quarters))){
				Integer year=Integer.parseInt(years)-1;
				String month1="";
				String month2="";
				if("1".equals(quarters)){
					month1+=1;
					month2+=3;
					date2+=year+"/"+month2+"/31 23:59:59";
				}else if("2".equals(quarters)){
					month1+=4;
					month2+=6;
					date2+=year+"/"+month2+"/30 23:59:59";
				}else if("3".equals(quarters)){
					month1+=7;
					month2+=9;
					date2+=year+"/"+month2+"/30 23:59:59";
				}else{
					month1+=10;
					month2+=12;
					date2+=year+"/"+month2+"/31 23:59:59";
				}
				 date1=year+"/"+month1+"/1 00:00:00";
			}
			
		}else {
			if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(months))){
				Integer month=Integer.parseInt(months)-1;
				if(month==0){
					month=12;
				}
				date1=years+"/"+month+"/1 00:00:00";
				if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
					 date2=years+"/"+month+"/31 23:59:59";
				}else if(month==2){
					 date2=years+"/"+month+"/28 23:59:59";
				}else{
					 date2=years+"/"+month+"/30 23:59:59";
				}
			}
			
			if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(months))&&(StringUtils.isNotBlank(days))){
				Integer month=Integer.parseInt(months)-1;
				if(month==0){
					month=12;
				}
				date1=years+"/"+month+"/"+days+" 00:00:00";
				date2=years+"/"+month+"/"+days+" 23:59:59";
			}
			
			if((StringUtils.isNotBlank(years))&&(StringUtils.isNotBlank(quarters))){
				String month1="";
				String month2="";
				if("1".equals(quarters)){
					month1+=10;
					month2+=12;
					date2+=years+"/"+month2+"/31 23:59:59";
				}else if("2".equals(quarters)){
					month1+=1;
					month2+=3;
					date2+=years+"/"+month2+"/31 23:59:59";
				}else if("3".equals(quarters)){
					month1+=4;
					month2+=6;
					date2+=years+"/"+month2+"/30 23:59:59";
				}else{
					month1+=7;
					month2+=9;
					date2+=years+"/"+month2+"/30 23:59:59";
				}
				 date1=years+"/"+month1+"/1 00:00:00";
			}
			if(StringUtils.isNotBlank(years)&&(StringUtils.isBlank(quarters))&&(StringUtils.isBlank(months))&&(StringUtils.isBlank(days))){
				Integer year=Integer.parseInt(years)-1;	
				 date1=year+"/1/1 00:00:00";
				date2=year+"/12/31 23:59:59";
			}
		}
		

			hql.append("select  d.dept_name as deptName ,d.dept_code as  deptcode  ,nvl(s.sum,0) as hospitalizationTime , nvl(s.sum,0)  as dischargePerson ,  nvl(trunc((a.sum/s.sum)*100,2),0)|| '%' as percentage from T_DEPARTMENT d  left join   (select sum(z.sum + x.sum) as sum,x.dept_code ");
			hql.append(" from (select count(t.inpatient_no) as sum,t.dept_code  from v_INPATIENT_INFO_DisPerson t where t.inpatient_time between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and  to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by t.dept_code) x ");
			hql.append(" left join  (select count(t1.inpatient_no) as sum,t1.dept_code from v_INPATIENT_INFO_DisPerson t1 where t1.in_state = 'O'  and T1.OUT_DATE between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss')");
			hql.append(" AND T1.inpatient_time NOT between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by t1.dept_code) z on x.dept_code=z.dept_code   group by x.dept_code) s  on d.dept_code=s.dept_code left join  ");
			hql.append(" (select count(r.inpatient_no) as sum,r.dept_code from v_INPATIENT_INFO_DisPerson r  where r.in_state = 'O' and r.OUT_DATE between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by r.dept_code) a  on d.dept_code=a.dept_code where  d.dept_type='I' and d.stop_flg = 0");
			
		
			if(!"".equals(deptCode)&&!("'1'").equals(deptCode)){
				hql.append(" and  d.dept_code in ("+deptCode+")");
			}
		
		SQLQuery queryObject=this.getSession().createSQLQuery(hql.toString())
				.addScalar("deptName").addScalar("hospitalizationTime",Hibernate.INTEGER).addScalar("dischargePerson",Hibernate.INTEGER).addScalar("percentage");
		List<DischargePersonVo> dischargePersonVo=queryObject.setResultTransformer(Transformers.aliasToBean(DischargePersonVo.class)).list();
		if(dischargePersonVo!=null&&dischargePersonVo.size()>0){
			return dischargePersonVo;
		}
		return new ArrayList<DischargePersonVo>();
	}

}
