package cn.honry.statistics.bi.inpatient.peopleInHospital.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiInpatientInfo;
import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.inpatient.peopleInHospital.dao.PeopleInHospitalDao;
import cn.honry.statistics.bi.inpatient.peopleInHospital.vo.PeopleInHospitalVo;
import cn.honry.statistics.bi.outpatient.outpatientFeeType.vo.OutpatientFeeTypeVo;
import cn.honry.statistics.bi.register.vo.RegisterVo;
import cn.honry.statistics.util.dateVo.DateVo;

@Repository("peopleInHospitalDao")
@SuppressWarnings({"all"})
public class PeopleInHospitalDaoImpl extends HibernateEntityDao<BiInpatientInfo>  implements PeopleInHospitalDao{

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<PeopleInHospitalVo> queryregisterid(String[] diArrayKey, List<Map<String, List<String>>> list,
			int dateType, DateVo datevo) {
		//天数
		int num=365;
		String sqldate=null;
		//通过判断时间维度种类确定天数和
		if(dateType==1){
			num=365;
			String date1=datevo.getYear1()+"";
			String date2=datevo.getYear2()+"";//
			sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR between '"+date1+"'  and '"+date2+"'";
		}else if(dateType==2){
			String date1=datevo.getYear1()+"/"+datevo.getQuarter1();
			String date2=datevo.getYear2()+"/"+datevo.getQuarter2();
			num=90;
			sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR between '"+date1+"'  and '"+date2+"' ";
		}else if(dateType==3){
			num=30;
			String date1=datevo.getYear1()+"/"+datevo.getMonth1();
			String date2=datevo.getYear2()+"/"+datevo.getMonth2();
			sqldate=" select  d.ID_KEY from BI_DATE  d where d.YEAR_MONTH between '"+date1+"' and '"+date2+"' ";
		}else if(dateType==4){
			num=1;
			String date11=datevo.getYear1()+"/"+datevo.getMonth1();
			String date12=datevo.getYear2()+"/"+datevo.getMonth2();
			String date21=datevo.getDay1()+"";
			String date22=datevo.getDay2()+"";
			sqldate="select  d.ID_KEY from BI_DATE  d where d.YEAR_MONTH between '"+date11+"' and '"+date12+"' and d.day_of_month between '"+date21+"' and '"+date22+"'";
		}
		//查询时间
		StringBuffer sql=new StringBuffer();//sql语句的StringBuffer对象
		StringBuffer order=new StringBuffer();//最后order by 的顺序（即：选择的维度顺序）StringBuffer对象
	    sql.append(" select distinct  ");
	    for(int i=0;i<diArrayKey.length;i+=2){
	    	if("dept_code".equals(diArrayKey[i])){
	    		sql.append(" t.dept_code as deptName");
	    		order.append("t."+diArrayKey[i]);
	    	}
	    	if("report_sex".equals(diArrayKey[i])){
	    		sql.append(" t.report_sex as sex");
	    		order.append("t."+diArrayKey[i]);
	    	}
	    	if("in_source".equals(diArrayKey[i])){
	    		sql.append(" t.in_source as sourceName");
	    		order.append("t."+diArrayKey[i]);
	    	}
	    	if("critical_flag".equals(diArrayKey[i])){
	    		sql.append(" to_char(t.critical_flag) as criticalName");
	    		order.append("t."+diArrayKey[i]);
	    	}
	    	//当拼接的不是最后一个查询维度时，order后面拼接一个","（最后一个order条件不需要添加","）
			if(i!=(diArrayKey.length-2)){
				order.append(",");
			}
			sql.append(",");
	    }
	    sql.append("  count(t.INPATIENT_NO) as hospitalPerson, ");
	    sql.append("  TRUNC(100*count(t.INPATIENT_NO)/(select count(t.INPATIENT_NO) from view_tinpatient_info t where t.IN_STATE='I'),2) as percentage,  ");
	    if(dateType==1){
	    	sql.append("         to_char(t.IN_DATE,'yyyy') as timeChose");
	    }else if(dateType==2){
			sql.append("        to_char(to_char(t.IN_DATE,'yyyy')||'/'||to_char(t.IN_DATE,'q')) as timeChose ");
		}else if(dateType==3){
			sql.append("         to_char(t.IN_DATE,'yyyy/mm') as timeChose");
		}else if(dateType==4){
			sql.append("         to_char(t.IN_DATE,'yyyy/mm/dd') as timeChose");
		}
	    sql.append("	  from view_tinpatient_info t   ");
		sql.append("	 where t.in_state = 'I'  ");
		if(dateType==1){
			sql.append(" and to_char(t.IN_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'");
		}else if(dateType==2){
			sql.append(" and to_char(t.IN_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.IN_DATE,'q') between '"+datevo.getQuarter1()+"' and '"+datevo.getQuarter2()+"'   ");
		}else if(dateType==3){
			sql.append(" and to_char(t.IN_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.IN_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
		}else if(dateType==4){
			sql.append(" and to_char(t.IN_DATE,'yyyy') between '"+datevo.getYear1()+"' and '"+datevo.getYear2()+"'   ");
			sql.append(" and to_char(t.IN_DATE,'mm') between '"+(datevo.getMonth1()>9?datevo.getMonth1():"0"+datevo.getMonth1())+"' and '"+(datevo.getMonth2()>9?datevo.getMonth2():"0"+datevo.getMonth2())+"'");
			sql.append(" and to_char(t.IN_DATE,'dd') between '"+(datevo.getDay1()>9?datevo.getDay1():"0"+datevo.getDay1())+"' and '"+(datevo.getDay2()>9?datevo.getDay2():"0"+datevo.getDay2())+"'");
		}
		int a = 0;
		//遍历数组，添加查询条件（匹配所选择的维度拼接）
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" and t.dept_code  in ('"+strings+"')");
			}
			if("report_sex".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" and t.report_sex  in ('"+strings+"')");
			}
			if("in_source".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" and t.in_source  in ('"+strings+"')");
			}
			if("critical_flag".equals(diArrayKey[i])){
				String strings = list.get(a).get(diArrayKey[i]).toString().replace(",", "','").replace(" ","").replace("[","").replace("]","");
				sql.append(" and t.critical_flag  in ('"+strings+"')");
			}
			a++;
		}
		sql.append("	 group by  ");
		sql.append(order.toString());
		sql.append(",");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.IN_DATE,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(t.IN_DATE,'yyyy')  ");
			sql.append("        ,to_char(t.IN_DATE,'q')   ");
		}else if(dateType==3){
			sql.append("         to_char(t.IN_DATE,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.IN_DATE,'yyyy/mm/dd')");
		}
		sql.append("	 order by ");
		sql.append(order.toString());
		sql.append(",");
		//对于时间的排序放在最后
		if(dateType==1){
			sql.append("         to_char(t.IN_DATE,'yyyy') ");
		}else if(dateType==2){
			sql.append("         to_char(to_char(t.IN_DATE,'yyyy')||'/'||to_char(t.IN_DATE,'q'))   ");
		}else if(dateType==3){
			sql.append("         to_char(t.IN_DATE,'yyyy/mm') ");
		}else if(dateType==4){
			sql.append("         to_char(t.IN_DATE,'yyyy/mm/dd')");
		}
		SQLQuery queryObject=this.getSession().createSQLQuery(sql.toString());
		for(int i=0;i<diArrayKey.length;i+=2){
			if("dept_code".equals(diArrayKey[i])){
				queryObject.addScalar("deptName");
			}
			if("report_sex".equals(diArrayKey[i])){
				queryObject.addScalar("sex");
			}
			if("in_source".equals(diArrayKey[i])){
				queryObject.addScalar("sourceName");
			}
			if("critical_flag".equals(diArrayKey[i])){
				queryObject.addScalar("criticalName");
			}
		}
		queryObject.addScalar("hospitalPerson",Hibernate.INTEGER).addScalar("percentage",Hibernate.DOUBLE).addScalar("timeChose");
		List<PeopleInHospitalVo> dischargePersonVo=queryObject.setResultTransformer(Transformers.aliasToBean(PeopleInHospitalVo.class)).list();
		if(dischargePersonVo!=null&&dischargePersonVo.size()>0){
			return dischargePersonVo;
		}
		
		return new ArrayList<PeopleInHospitalVo>();
	}


}
