package cn.honry.inner.statistics.inneroperationProportion.dao.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.inneroperationProportion.dao.InnerOperaPropDao;
import cn.honry.inner.statistics.inneroperationProportion.vo.InnerOperation;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Repository("innerOperaPropDao")
@SuppressWarnings("all")
public class InnerOperaPropDaoImpl extends HibernateDaoSupport implements InnerOperaPropDao {
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private  WordLoadDocDao wordLoadDocDao;
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Override
	public void init_OperaProption(String menuAlias, String type, String date) {
		if("2".equals(type)){
			Date beginDate=new Date();
		
			date=date.substring(0,7);
			String firstData=date+"-01 00:00:00";
			String endData=returnEndTime(date+"-01")+" 23:59:59";
			StringBuffer sb = new StringBuffer();
			sb.append("select (select t.dept_name from t_department t where t.dept_code=code) as deptName, (select t.DEPT_REGISTERORDER  from t_department t where t.dept_code=code ) num,code as deptCode,total,nvl(total1,0)  total1,nvl(total2,0) total2,rownum as rn from "
					+ " (  select D.*,B.TOTAL2   FROM  ( select s.DEPT_code code, count(1) as total2 from  (select distinct t.dept_code,t.inpatient_no from (");
			sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
			sb.append(" ) t, T_OPERATION_RECORD r where t.in_state in ('O') and t.inpatient_no = r.clinic_code and r.YNVALID = '1' and "
					+ " t.out_date>=to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<=to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss')) s "
					+ "group by s.dept_code) B right join (select  C.*,A.total1 from  ( select s.old_dept_code code, count(1) as total1 from(");
			sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
			sb.append(") t , T_INPATIENT_SHIFTAPPLY s where t.in_state in ('O') and t.inpatient_no = s.inpatient_no and s.shift_state = '2' and "
					+ " t.out_date>=to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<=to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss') "
					+ "group by s.old_dept_code) A right join ( select t.DEPT_code code, count(1) as total from (");
			sb.append(" select t.inpatient_no,t.in_state,t.dept_code,t.out_date from t_inpatient_info t  union all select t1.inpatient_no,t1.in_state,t1.dept_code,t1.out_date from t_inpatient_info_now t1 ");
			sb.append(")t where t.in_state in ('O') and  t.out_date>=to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t.out_date<=to_date('"+endData+"',"
					+ "'yyyy-MM-dd hh24:mi:ss') ");
			sb.append(" group by dept_code) C on C.code=A.code ) D  on D.code=B.code) order by num");
			String sql=sb.toString();
			
			List<InnerOperation> list = this.getSession().createSQLQuery(sql).addScalar("deptCode").addScalar("total",Hibernate.INTEGER).addScalar("total1",Hibernate.INTEGER).addScalar("deptName")
			.addScalar("total2",Hibernate.INTEGER).setResultTransformer(Transformers.aliasToBean(InnerOperation.class)).list();
			
			DBObject query = new BasicDBObject();
			query.put("workdate", date);//移除数据条件
			new MongoBasicDao().remove("SSZBTJ", query);
			
			if(list!=null && list.size()>0){
				for(InnerOperation vo:list){
					DecimalFormat df = new DecimalFormat("#.00");
					Document doucment1=new Document();
					doucment1.append("workdate", date);
					doucment1.append("deptCode", vo.getDeptCode());
					Document document = new Document();
					document.append("total", vo.getTotal());
					document.append("total1", vo.getTotal1());
					document.append("total2", vo.getTotal2());
					document.append("workdate",date);
					document.append("deptCode", vo.getDeptCode());
					document.append("deptName",vo.getDeptName());
					if(vo.getTotal()==null||vo.getTotal()==0){
						document.append("proportion",-1.0);
					}else{
						document.append("proportion",Double.valueOf(df.format(((double)(vo.getTotal2())/vo.getTotal())*100)));
					}
					new MongoBasicDao().update("SSZBTJ", doucment1, document, true);
				}
				wordLoadDocDao.saveMongoLog(beginDate, menuAlias, list, date);
			}
		}
	}
	
	
	public String returnEndTime(String date){
		String end=null;
		date=date.substring(0,7)+"-01";
		Calendar ca=Calendar.getInstance(Locale.CHINESE);
		try {
			ca.setTime(df.parse(date));
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DATE, -1);
			end=df.format(ca.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}

}
