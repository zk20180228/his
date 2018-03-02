package cn.honry.statistics.bi.bistac.undrugDataAnalysis.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.dao.UndrugDataAnalysisDao;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.vo.UndrugDataVo;
@Repository("undrugAnalysisDao")
@SuppressWarnings({ "all" })
public class UndrugDataAnalysisDaoImpl extends  HibernateEntityDao<Dashboard> implements UndrugDataAnalysisDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<UndrugDataVo> queryUndrugDataFromOracle(List<String> tnL,
			String firstData, String endData) throws Exception {
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		if(tnL==null||tnL.size()<1){
			return new ArrayList<UndrugDataVo>();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(total) as total, feeType from (");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" union all ");
			}
			sb.append("  select t").append(i).append(".TOT_COST as total, c.MINFEE_NAME as feeType  from ").append(tnL.get(i))
			.append(" t").append(i).append(", t_charge_minfeetostat c  where c.MINFEE_CODE = t").append(i).append(".fee_code")
			.append(" and t").append(i).append(".fee_date > to_date('"+firstData+"', 'yyyy-MM-dd hh24:mi:ss')  and t")
			.append(i).append(".fee_date < to_date('"+endData+"', 'yyyy-MM-dd hh24:mi:ss')");

	}
		sb.append(" )  group by feeType");
		List<UndrugDataVo> list = this.getSession().createSQLQuery(sb.toString()).addScalar("total",Hibernate.DOUBLE).addScalar("feeType",Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(UndrugDataVo.class)).list();
		return list;
	}
	@Override
	public List<UndrugDataVo> queryUndrugDeptFeeTop5(List<String> tnL,
			String firstData, String endData) throws Exception{
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		if(tnL==null||tnL.size()<1){
			return new ArrayList<UndrugDataVo>();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select * from  ( select m.*,rownum as num  from  ((");
		for(int  i =0;i<tnL.size();i++){
			if(i!=0){
				sb.append("  union all  ");
			}
			sb.append(" select sum(t").append(i).append(".tot_cost) as total,t").append(i).append(".execute_deptcode as deptName from ").append(tnL.get(i))
			.append("  t").append(i).append(" where t").append(i).append(".fee_date>to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and t")
			.append(i).append(".fee_date<to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss') ").append(" group by t").append(i).append(".execute_deptcode");
		}
		sb.append("  ) order by total desc) m) n  where num<6");
		List<UndrugDataVo> list = this.getSession().createSQLQuery(sb.toString()).addScalar("deptName").addScalar("total",Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(UndrugDataVo.class)).list();
		return list;
	}
	/* (非 Javadoc)
	* <p>Description:oracle查询医生收入前五 </p>
	* @param searchDate
	* @param dateSign
	* @param queryMongo
	* @return
	* @throws Exception
	*/
	@Override
	public List<UndrugDataVo> queryUndrugDocFeeTop5(List<String> tnL,
			String firstData, String endData) throws Exception {
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		if(tnL==null||tnL.size()<1){
			return new ArrayList<UndrugDataVo>();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select m.total,(select e.employee_name from t_employee e where e.employee_code=m.docName) as docName,rownum from (select sum(total) as total,docName from (");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("  union all ");
			}
			sb.append("  select t").append(i).append(".tot_cost as total,t").append(i).append(".recipe_doccode as docName from ").append(tnL.get(i)).append(" t").append(i)
			.append(" where t").append(i).append(".fee_date>to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and t").append(i).append(".fee_date<to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss') ");
		}
		sb.append(") where total is not null group by docName order by total desc) m where rownum<6");
		List<UndrugDataVo> list = this.getSession().createSQLQuery(sb.toString()).addScalar("total",Hibernate.DOUBLE).addScalar("docName")
		.setResultTransformer(Transformers.aliasToBean(UndrugDataVo.class)).list();
		return list;
	}
	/* (非 Javadoc)
	* <p>Description:oracle查询非药品同比 </p>
	* @param searchDate
	* @param dateSign
	* @param queryMongo
	* @return
	* @throws Exception
	*/
	@Override
	public List<UndrugDataVo> queryUndrugFeeMoM(List<String> tnL,
			String firstData, String endData, String type) throws Exception{
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		String dateType="";
		if("年".equals(type)){
			dateType="yyyy";
		}else if("月".equals(type)){
			dateType="yyyy-MM";
		}else if("日".equals(type)){
			dateType="yyyy-MM-dd";
		}
		if(tnL==null||tnL.size()<1){
			return new ArrayList<UndrugDataVo>();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(total) as total,name from (");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("  union all ");
			}
			sb.append(" (select t").append(i).append(".tot_cost as total, to_char(t").append(i).append(".fee_date, '"+dateType+"') as name from ")
			.append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".fee_date > to_date('"+firstData+"', 'yyyy-MM-dd hh24:mi:ss') and t")
			.append(i).append(".fee_date < to_date('"+endData+"', 'yyyy-MM-dd hh24:mi:ss'))");
		}
		sb.append(") group by name order by name desc");
		List<UndrugDataVo> list = this.getSession().createSQLQuery(sb.toString()).addScalar("total",Hibernate.DOUBLE).addScalar("name")
		.setResultTransformer(Transformers.aliasToBean(UndrugDataVo.class)).list();
		return list;
	}
	@Override
	public UndrugDataVo queryUndrugFeeSameDOM(List<String> tnL,
			String firstData, String endData, String type) throws Exception {
		firstData+=" 00:00:00";
		endData+=" 23:59:59";
		String dateType="";
	    if("月".equals(type)){
			dateType="yyyy-MM";
		}else if("日".equals(type)){
			dateType="yyyy-MM-dd";
		}
		if(tnL==null||tnL.size()<1){
			return new UndrugDataVo();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(m.total) as total,m.name from (");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" union all  ");
			}
			sb.append("select t").append(i).append(".tot_cost as total, to_char(t").append(i).append(".fee_date, '"+dateType+"') as name from ")
			.append(tnL.get(i)).append(" t").append(i).append(" where t").append(i).append(".fee_date>to_date('"+firstData+"','yyyy-MM-dd hh24:mi:ss') and  t")
			.append(i).append(".fee_date<to_date('"+endData+"','yyyy-MM-dd hh24:mi:ss')");
		}
		sb.append(") m group by m.name");
		List<UndrugDataVo> list = this.getSession().createSQLQuery(sb.toString()).addScalar("total",Hibernate.DOUBLE).addScalar("name")
		.setResultTransformer(Transformers.aliasToBean(UndrugDataVo.class)).list();
		if(list.size()>0 && list!=null){
			return list.get(0);
		}else{
			return new UndrugDataVo();
		}
	}
	@Override
	public List<UndrugDataVo> queryUndrugTypeFeeFromDB(String searchDate, String dateSign,String queryMongo) throws Exception{
		int sLength=searchDate.length();
		String[] date=searchDate.split("-");
		String searchName;
		if("1".equals(dateSign)){
			if(sLength!=4){
				return new ArrayList<UndrugDataVo>();
			}
			searchName=date[0];
		}else if("2".equals(dateSign)){
			if(sLength!=7){
				return new ArrayList<UndrugDataVo>();
			}
			searchName=date[0]+"-"+date[1];
		}else {
			if(sLength!=10){
				return new ArrayList<UndrugDataVo>();
			}
			searchName=searchDate;
		}
		BasicDBObject bdObject = new BasicDBObject();
		List<UndrugDataVo> list=new ArrayList<UndrugDataVo>();
		bdObject.append("name", searchName);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			UndrugDataVo voOne=new  UndrugDataVo();
			dbCursor = cursor.next();
			String stat = (String) dbCursor.get("stat");
			String name = (String) dbCursor.get("name");
			Double doubValue=(Double) dbCursor.get("douValue");
			voOne.setTotal(doubValue);
			voOne.setFeeType(stat);
			list.add(voOne);
			}
		return list;
	}
	
	@Override
	public List<UndrugDataVo> queryUndrugDeptFeeTop5FromDB(String searchDate,
			String dateSign,String queryMongo) throws Exception{
		String [] dateArr=searchDate.split("-");
		int sLength=searchDate.length();
		String searchName;
		if("1".equals(dateSign)){
			if(sLength!=4){
				return new ArrayList<UndrugDataVo>();
			}
			searchName=dateArr[0];
		}else if("2".equals(dateSign)){
			if(sLength!=7){
				return new ArrayList<UndrugDataVo>();
			}
			searchName=dateArr[0]+"-"+dateArr[1];
		}else {
			if(sLength!=10){
				return new ArrayList<UndrugDataVo>();
			}
			searchName=searchDate;
		}
		int temp=5;
		BasicDBObject bdObject=new BasicDBObject();
		bdObject.append("name", searchName);
		DBCursor cursor = new MongoBasicDao().findAlldataBySort(queryMongo,bdObject, "douValue");
		DBObject dbCursor;
		List<UndrugDataVo> list=new ArrayList<UndrugDataVo>();
		while(cursor.hasNext()){
			UndrugDataVo voOne=new  UndrugDataVo();
			 dbCursor = cursor.next();
			 String dept = (String) dbCursor.get("dept");
			 String name = (String) dbCursor.get("name");
			 Double doubValue=(Double) dbCursor.get("douValue");
			voOne.setTotal(doubValue);
			voOne.setDeptName(dept);
			if(dept!=null){
				temp--;
				list.add(voOne);
			 }
			if(temp==0){
				break;
			}
			}
		return list;
	}
	@Override
	public List<UndrugDataVo> queryUndrugDocFeeTop5FromDB(String searchDate,
			String dateSign,String queryMongo) throws Exception {
		 String[] dateArr=searchDate.split("-");
			int sLength=searchDate.length();
			String searchName;
			if("1".equals(dateSign)){
				if(sLength!=4){
					return new ArrayList<UndrugDataVo>();
				}
				searchName=dateArr[0];
			}else if("2".equals(dateSign)){
				if(sLength!=7){
					return new ArrayList<UndrugDataVo>();
				}
				searchName=dateArr[0]+"-"+dateArr[1];
			}else {
				if(sLength!=10){
					return new ArrayList<UndrugDataVo>();
				}
				searchName=searchDate;
			}
			int temp=5;
			BasicDBObject bdObject=new BasicDBObject();
			bdObject.append("name", searchName);
			DBCursor cursor = new MongoBasicDao().findAlldataBySort(queryMongo,bdObject, "douValue");
			DBObject dbCursor;
			List<UndrugDataVo> list=new ArrayList<UndrugDataVo>();
			while(cursor.hasNext()){
				UndrugDataVo voOne=new  UndrugDataVo();
				 dbCursor = cursor.next();
				 String doctor = (String) dbCursor.get("doctor");
				 String name = (String) dbCursor.get("name");
				 Double doubValue=(Double) dbCursor.get("douValue");
				voOne.setTotal(doubValue);
				voOne.setName(name);
				if(null!=doctor){
					temp--;
					voOne.setDocName(doctor);
					list.add(voOne);
				}
				if(temp==0){
					break;
				}
				}
			return list;
	}
	@Override
	public List<UndrugDataVo> queryUndrugFeeSameDOMFromDB(String searchDate,
			String dateSign,String queryMongo) throws Exception{
		if(searchDate==null){
			return new ArrayList<UndrugDataVo>();
		}
		int eLength=searchDate.length();
		String tableName;//查询表
		if("2".equals(dateSign)){//月
			if(eLength!=7){
				return new ArrayList<UndrugDataVo>();
			}
		}else{//日
			if(eLength!=10){
				return new ArrayList<UndrugDataVo>();
			}
		}
		BasicDBObject bdObject = new BasicDBObject();
		List<UndrugDataVo> list=new ArrayList<UndrugDataVo>();
		String[] dateArr=this.conMonth(searchDate, dateSign);
		for(String vo:dateArr){
		bdObject.append("name", vo);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		if(!cursor.hasNext()){
			UndrugDataVo voOne=new  UndrugDataVo();
			voOne.setName(vo);
			voOne.setTotal(0.00);
			list.add(voOne);
			continue;
		}
		UndrugDataVo voOne=new  UndrugDataVo();
		Double dou=0.00;
		voOne.setName(vo);
		Double doubValue;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 doubValue=(Double) dbCursor.get("douValue");
			 dou+=doubValue;
			}
		voOne.setTotal(dou);
		list.add(voOne);
		}
		return list;
	}
	
		@Override
		public List<UndrugDataVo> queryUndrugFeeMoMFromDB(String searchDate,
				String dateSign,String queryMongo) throws Exception {
			if(searchDate==null){
				return new  ArrayList<UndrugDataVo>();
			}
			int bLength=searchDate.length();
			if("1".equals(dateSign)){//年
				if(bLength!=4){
					return new  ArrayList<UndrugDataVo>();
				}
			}else if("2".equals(dateSign)){//月
				if(bLength!=7){
					return new  ArrayList<UndrugDataVo>();
				}
			}else{//日
				if(bLength!=10){
					return new  ArrayList<UndrugDataVo>();
				}
			}
			String[] dateArr=this.conYear(searchDate, dateSign);
			BasicDBObject bdObject = new BasicDBObject();
			List<UndrugDataVo> list=new ArrayList<UndrugDataVo>();
			for(String vo:dateArr){
			bdObject.append("name", vo);
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			if(!cursor.hasNext()){
				UndrugDataVo voOne=new  UndrugDataVo();
				voOne.setName(vo);
				voOne.setTotal(0.00);
				list.add(voOne);
				continue;
			}
			UndrugDataVo voOne=new  UndrugDataVo();
			Double dou=0.00;
			voOne.setName(vo);
			Double doubValue;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 doubValue=(Double) dbCursor.get("douValue");
				 dou+=doubValue;
				}
			voOne.setTotal(dou);
			list.add(voOne);
			}
			return list;
		}
	/**
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conYear(String date,String dateSing){
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		 SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
		 try {
			 if("1".equals(dateSing)){
			
				date=sdf.format(sdf2.parse(date));
			
			 }else if("2".equals(dateSing)){
				 date=sdf.format(sdf1.parse(date)); 
			 }
		 } catch (ParseException e) {
			} 
		 Calendar ca = Calendar .getInstance();
		 String[] dateOne =date.split("-");
		 String[] strArr=new String[7];
		 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2])); 
		for(int i=0;i<7;i++){
			if("1".equals(dateSing)){
				strArr[i]=sdf2.format(ca.getTime());
				ca.add(Calendar.YEAR, -1);
			}else if("2".equals(dateSing)){
				strArr[i]=sdf1.format(ca.getTime());
				ca.add(Calendar.MONTH, -1);
			}else{
				strArr[i]=sdf.format(ca.getTime());
				ca.add(Calendar.DATE, -1);
			}
		}
		 return strArr;
	}
	/**
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conMonth(String date,String dateSing){
		String [] strArr=new String[7];
		String[] dateArr=date.split("-");
		int dateTemp=Integer.parseInt(dateArr[0]);
		for(int i=0;i<7;i++){
			if("2".equals(dateSing)){//月同比
				strArr[i]=(dateTemp-i)+"-"+dateArr[1];
			}else{
				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
			}
		}
		
		return strArr;
	}
	
}
