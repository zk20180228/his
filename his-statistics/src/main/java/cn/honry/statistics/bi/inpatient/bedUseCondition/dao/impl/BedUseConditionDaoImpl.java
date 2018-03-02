package cn.honry.statistics.bi.inpatient.bedUseCondition.dao.impl;

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
import cn.honry.statistics.bi.inpatient.bedUseCondition.dao.BedUseConditionDao;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;
import cn.honry.statistics.bi.inpatient.dischargePerson.vo.DischargePersonVo;

@Repository("bedUseConditionDao")
@SuppressWarnings({"all"})
public class BedUseConditionDaoImpl extends HibernateEntityDao<BedUseConditionVo> implements BedUseConditionDao {
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<SysDepartment> queryAllDept() {
		String sql="select bd.DEPT_ID as id, bd.DEPT_CODE as deptCode,bd.DEPT_NAME as deptName from T_DEPARTMENT bd where bd.dept_type='N'";
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
	public List<BedUseConditionVo> queryBedUseCondition(String deptCode, String years
			) {
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
		hql.append("select distinct d.dept_name as deptName, d.dept_code, nvl(abed.sums, 0) * "+day+" as actualBed, nvl(tt.actOpenBedDays, 0) - nvl(shif.actu, 0)+nvl(sf.open,0) as actOpenBedDay, nvl(tt.actOpenBedDays, 0) / "+day+" as aveOpenBed, ");
		hql.append("  nvl(actu.actOccBedDays, 0) as actOccBedDay,nvl(cc.disPatOccBedDays, 0) as disPatOccBedDay,nvl(cc.outHospitals, 0) as outHospital, nvl(m.ss,0) + nvl(n.sc,0) as sum  ");
		hql.append(" from T_DEPARTMENT d left join (select bed.ward_code, sum(bed.sum + t2.addBed) as sums from (select bd.ward_code, count(bd.BED_no) as sum from v_bed bd where bd.bed_status_code not in ('2') ");
		hql.append("  and bd.bed_organ_code not in ('2') group by ward_code) bed, (select r.ward_code, count(r.BED_NO) as addBed from v_bed r  where ceil( to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') - r.create_date) > 182 and r.bed_status_code not in ('2') ");
		
		hql.append(" and r.bed_organ_code = '2' group by r.ward_code) t2 where bed.ward_code = t2.ward_code group by bed.ward_code) abed on d.dept_code = abed.ward_code left join (select t1.ward_code,  ");
		hql.append(" sum(t1.actualBed + t2.addBed) * "+day+" as actOpenBedDays from (select b.ward_code, count(b.BED_NO) as actualBed from v_bed b where b.bed_status_code not in ('2,8') and b.bed_organ_code not in ('2') ");
		hql.append("  group by b.ward_code) t1,(select r.ward_code, count(r.BED_NO) as addBed from v_bed r where ceil( to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') - r.create_date) > 182  and r.bed_status_code not in ('2,8') and r.bed_organ_code = '2' ");
		
		hql.append(" group by r.ward_code) t2  where t1.ward_code = t2.ward_code group by t1.ward_code) tt on d.dept_code = tt.ward_code left join (select s.ward_code, sum(ceil(To_date('"+date2+"', 'yyyy-mm-dd hh24-mi-ss') - ");
		hql.append(" s.createtime)) as actu from v_business_hospitalbed_shif s where s.new_status = '8'  and s.createtime  between To_date('"+date1+"', 'yyyy-mm-dd hh24-mi-ss') and  To_date('"+date2+"', 'yyyy-mm-dd hh24-mi-ss') group by s.ward_code) shif on shif.ward_code = d.dept_code  left join ( select s.ward_code, sum(ceil(To_date('"+date2+"','yyyy-mm-dd hh24-mi-ss') - s.createtime)) ");
		hql.append(" as open from v_business_hospitalbed_shif s where s.new_status not in( '8')  and s.createtime  between To_date('"+date1+"', 'yyyy-mm-dd hh24-mi-ss') and  To_date('"+date2+"', 'yyyy-mm-dd hh24-mi-ss') group by s.ward_code ) sf on sf.ward_code = d.dept_code left join (select r.WARD_CODE, sum(ceil(nvl(CASE to_char(r.out_date,'yyyy')  WHEN '0001' THEN to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') WHEN '0002' THEN to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') ELSE r.out_date END , ");
		
		hql.append(" to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss')) -  r.ADMISS_TIME)) as actOccBedDays from v_bed_record r where r.ADMISS_TIME between To_date('"+date1+"', 'yyyy-mm-dd hh24-mi-ss') and ");
		hql.append("  To_date('"+date2+"', 'yyyy-mm-dd hh24-mi-ss') group by r.WARD_CODE) actu on actu.WARD_CODE = d.dept_code left join ");
		hql.append(" (select r.WARD_CODE,sum(ceil(r.out_date - r.ADMISS_TIME)) as disPatOccBedDays, count(r.INPATIENT_NO) as outHospitals from v_bed_record r where r.out_date between ");
		hql.append("  To_date('"+date1+"', 'yyyy-mm-dd hh24-mi-ss') and  To_date('"+date2+"', 'yyyy-mm-dd hh24-mi-ss') group by r.WARD_CODE) cc on cc. ward_code = d.dept_code ");
		
		hql.append(" left join （select bq.dept_code ,nvl(s.ss,0) as ss  from t_department_contact bq,v_inpatient_info_bed r,(select t2.pardept_id ,nvl(count(r.INPATIENT_NO),0)/"+workDay+" as ss from v_inpatient_info_bed r, t_department_contact t2 ");
		hql.append(" where t2.dept_id =r.dept_code and t2.reference_type = '03' and r.IN_SOURCE = '01' and r.IN_DATE between to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss')  ");
		hql.append(" group by t2.pardept_id) s where bq.id=s.pardept_id  group by bq.dept_code ,s.ss) m on m. dept_code = d.dept_id left join （select bq.dept_code ,nvl(s.sc,0) as sc from t_department_contact bq,v_inpatient_info_bed r, ");
		hql.append(" (select t2.pardept_id ,nvl(count(r.INPATIENT_NO),0)/"+day+" as sc from v_inpatient_info_bed r, t_department_contact t2 where t2.dept_id =r.dept_code and t2.reference_type = '03'  and r.IN_SOURCE = '02' and r.IN_DATE between ");
		hql.append(" to_date('"+date1+"', 'yyyy/MM/dd hh24:mi:ss') and to_date('"+date2+"', 'yyyy/MM/dd hh24:mi:ss') group by t2.pardept_id) s where bq.id=s.pardept_id group by bq.dept_code ,s.sc) n  ");
		hql.append("  on n.dept_code = d.dept_id where d.dept_type = 'N' and d.stop_flg = 0");
		
		
		
		if(deptCode!=null){
			if(!"".equals(deptCode)&&!"'1'".equals(deptCode)){
				hql.append(" and d.dept_code in("+deptCode+")");
			}
		}
		
		SQLQuery queryObject=this.getSession().createSQLQuery(hql.toString())
				.addScalar("deptName").addScalar("actualBed",Hibernate.INTEGER).addScalar("actOpenBedDay",Hibernate.INTEGER)
				.addScalar("aveOpenBed",Hibernate.INTEGER).addScalar("actOccBedDay",Hibernate.INTEGER)
				.addScalar("disPatOccBedDay",Hibernate.INTEGER).addScalar("outHospital",Hibernate.INTEGER).addScalar("sum",Hibernate.DOUBLE);
		List<BedUseConditionVo> bedUseConditionVo=queryObject.setResultTransformer(Transformers.aliasToBean(BedUseConditionVo.class)).list();
		if(bedUseConditionVo!=null&&bedUseConditionVo.size()>0){
			return bedUseConditionVo;
		}
		return new ArrayList<BedUseConditionVo>();
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
