package cn.honry.statistics.finance.drugIncomeCompare.dao.impl;

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

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.finance.drugIncomeCompare.dao.DrugIncomeCompareDao;
import cn.honry.utils.HisParameters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("drugIncomeCompareDao")
@SuppressWarnings({ "all" })
public class DrugIncomeCompareDaoImpl extends HibernateEntityDao<Dashboard>
		implements DrugIncomeCompareDao {
	@Resource(name = "sessionFactory")
	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 */
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 基础工具类,不支持参数名传参
	 */
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Dashboard> queryFee(String startTime, String endTime,
			List<String> tnL,String type)  throws Exception{
		if (tnL == null || tnL.size() < 0) {
			return new ArrayList<Dashboard>();
		}
		startTime+=" 00:00:00";
		endTime += " 23:59:59";
		StringBuffer sb = new StringBuffer(500);
		sb.append(
				"select c.stat as stat,sum(c.douValue)/10000 as douValue  from( ")
				.append("select b.code_name as stat,to_char(ti.name,'yyyy-MM-dd') AS name,sum(ti.value) as douValue from(  ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				sb.append(" union all ");
			}
			sb.append("select t").append(i).append(".drug_code as drugCode ,t")
					.append(i).append(".fee_date as name,t").append(i)
					.append(".tot_cost as value ").append("from ")
					.append(HisParameters.HISPARSCHEMAHISUSER)
					.append(tnL.get(i)).append("  t" + i).append(" where");
				sb.append(" t")
						.append(i)
						.append(".fee_date>=to_date('" + startTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
				sb.append(" and t")
						.append(i)
						.append(".fee_date<=to_date('" + endTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
		}
		sb.append(") ti ");
		sb.append("left join t_drug_info d on ti.drugCode = d.drug_code ");
		sb.append("left join t_business_dictionary b on d.DRUG_TYPE = b.code_encode ");
		sb.append("where b.code_type = 'drugType'");
		sb.append(" group by ti.name,ti.drugCode,b.code_name ");
		sb.append(") c group by  c.stat");
		return super
				.getSession()
				.createSQLQuery(sb.toString())
				.addScalar("stat")
				.addScalar("douValue", Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
	}

	@Override
	public List<Dashboard> queryDeptTop5(String startTime, String endTime,
			List<String> tnL,String type)  throws Exception{
		if (tnL == null || tnL.size() < 0) {
			return new ArrayList<Dashboard>();
		}
		startTime+=" 00:00:00";
		endTime += " 23:59:59";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from( ");
		buffer.append("select  f.name as name,f.douValue/10000 as douValue,(select e.dept_name from t_department e where e.dept_code=f.dept  ) as dept,row_number()over(partition by f.name order by f.douValue desc ) as rn from( ");
		buffer.append("select c.name as name,sum(c.douValue) as douValue,c.dept as dept  from( ");
		buffer.append("select to_char(ti.name,'yyyy') AS name,sum(ti.value) as douValue,ti.dept as dept from( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select t").append(i).append(".fee_date as name,t")
					.append(i).append(".tot_cost as value,t").append(i)
					.append(".execute_deptCode as dept ").append("from ")
					.append(HisParameters.HISPARSCHEMAHISUSER)
					.append(tnL.get(i)).append("  t" + i).append(" where");
				buffer.append(" t")
						.append(i)
						.append(".fee_date>=to_date('" + startTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
				buffer.append(" and t")
						.append(i)
						.append(".fee_date<=to_date('" + endTime 
								+ "','yyyy-MM-dd hh24:mi:ss')");
		}
		buffer.append(") ti ");
		buffer.append("group by ti.name,ti.dept ");
		buffer.append(") c group by c.name ,c.dept ");
		buffer.append(") f ");
		buffer.append(") e where rn<6 ");
		return super.getSession().createSQLQuery(buffer.toString())
				.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("dept")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
	}

	@Override
	public List<Dashboard> queryDocTop5(String startTime, String endTime,
			List<String> tnL,String type)  throws Exception{
		if (tnL == null || tnL.size() < 0) {
			return new ArrayList<Dashboard>();
		}
		startTime+=" 00:00:00";
		endTime += " 23:59:59";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from( ");
		buffer.append("select  f.name as name,f.douValue/10000,(select e.employee_name from t_employee e where e.employee_code=f.doc  ) as doctor,row_number()over(partition by f.name order by f.douValue desc ) as rn from( ");
		buffer.append("select c.name as name,sum(c.douValue) as douValue,c.doc as doc  from( ");
		buffer.append("select to_char(ti.name,'yyyy') AS name,sum(ti.value) as douValue,ti.doc as doc from( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select t").append(i).append(".fee_date as name,t")
					.append(i).append(".tot_cost as value,t").append(i)
					.append(".recipe_doccode as doc ").append("from ")
					.append(HisParameters.HISPARSCHEMAHISUSER)
					.append(tnL.get(i)).append("  t" + i).append(" where");
				buffer.append(" t")
						.append(i)
						.append(".fee_date>=to_date('" + startTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
				buffer.append(" and t")
						.append(i)
						.append(".fee_date<=to_date('" + endTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
		}
		buffer.append(") ti ");
		buffer.append("group by ti.name,ti.doc ) c group by  c.name ,c.doc ) f ) e where rn<6 ");
		
		return super.getSession().createSQLQuery(buffer.toString())
				.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("doctor")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
	}

	//查询环比
	@Override
	public List<Dashboard> compareFeeByYear(String startTime, String endTime,
			List<String> tnL, String type)  throws Exception{
		if (tnL == null || tnL.size() < 0) {
			return new ArrayList<Dashboard>();
		}
		startTime+=" 00:00:00";
		endTime += " 23:59:59";
		String dateType="";
		if("年".equals(type)){
			dateType="yyyy";
		}else if("月".equals(type)){
			dateType="yyyy-MM";
		}else if("日".equals(type)){
			dateType="yyyy-MM-dd";
		}
		StringBuffer sb = new StringBuffer(500);
		sb.append("select a.name , sum(a.cost)/10000 as douValue from( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				sb.append(" union all ");
			}
			sb.append("select to_char(t").append(i).append(".fee_date,'"+dateType+"') as name,sum(t")
					.append(i).append(".tot_cost) as cost").append(" from ")
					.append(HisParameters.HISPARSCHEMAHISUSER)
					.append(tnL.get(i)).append("  t" + i).append(" where");
				sb.append(" t")
						.append(i)
						.append(".fee_date>=to_date('" + startTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
				sb.append(" and t")
						.append(i)
						.append(".fee_date<=to_date('" + endTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
			sb.append(" GROUP BY TO_CHAR (t").append(i).append(".fee_date, '"+dateType+"')");
		}
		sb.append(" ) a GROUP BY a.name ORDER BY a.name");
		
		return super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("douValue",Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
	}

	//查询所用同比
	@Override
	public Dashboard compareFeeByMonthsInYears(String startTime,
			String endTime, List<String> tnL, String type)  throws Exception{
		if (tnL == null || tnL.size() < 0) {
			return new Dashboard();
		}
		startTime+=" 00:00:00";
		endTime += " 23:59:59";
		String dateType="";
	    if("月".equals(type)){
			dateType="yyyy-MM";
		}else if("日".equals(type)){
			dateType="yyyy-MM-dd";
		}
		StringBuffer sb = new StringBuffer(500);
		sb.append("select a.name , sum(a.cost)/10000 as douValue from( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				sb.append(" union all ");
			}
			sb.append("select to_char(t").append(i).append(".fee_date,'"+dateType+"') as name,sum(t")
					.append(i).append(".tot_cost) as cost").append(" from ")
					.append(HisParameters.HISPARSCHEMAHISUSER)
					.append(tnL.get(i)).append("  t" + i).append(" where");
				sb.append(" t")
						.append(i)
						.append(".fee_date>=to_date('" + startTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
				sb.append(" and t")
						.append(i)
						.append(".fee_date<=to_date('" + endTime
								+ "','yyyy-MM-dd hh24:mi:ss')");
			sb.append(" GROUP BY TO_CHAR (t").append(i).append(".fee_date, '"+dateType+"')");
		}
		sb.append(" ) a GROUP BY a.name ORDER BY a.name");
		
		 List<Dashboard> list = super.getSession().createSQLQuery(sb.toString())
				.addScalar("name").addScalar("douValue",Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new Dashboard();
		}
	}

	@Override
	public boolean isCollection(String name)  throws Exception{
		return new MongoBasicDao().isCollection(name);
	}
	/**
	 * 环比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conYear(String date,String dateSing) throws Exception{
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		 SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
		 try {
			 if("年".equals(dateSing)){
				 date=sdf.format(sdf2.parse(date));
			 }else if("月".equals(dateSing)){
				 date=sdf.format(sdf1.parse(date)); 
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 Calendar ca = Calendar .getInstance();
		 String[] dateOne =date.split("-");
		 String[] strArr=new String[7];
		 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
		for(int i=0;i<7;i++){
			if("年".equals(dateSing)){
				strArr[i]=sdf2.format(ca.getTime());
				ca.add(Calendar.YEAR, -1);
			}else if("月".equals(dateSing)){
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
	 * 同比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conMonth(String date,String dateSing) throws Exception{
		String [] strArr=new String[7];
		String[] dateArr=date.split("-");
		int dateTemp=Integer.parseInt(dateArr[0]);
		for(int i=0;i<7;i++){
			if("月".equals(dateSing)){//月同比
				strArr[i]=(dateTemp-i)+"-"+dateArr[1];
			}else{
				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
			}
		}
		return strArr;
	}

	@Override
	public List<Dashboard> queryDocTop5ByMongo(String searchTime, String type,String queryMongo)  throws Exception{
		String[] dateArr=searchTime.split("-");
		int sLength=searchTime.length();
		String searchName;
		if("年".equals(type)){
			if(sLength!=4){
				return new ArrayList<Dashboard>();
			}
			searchName=dateArr[0];
		}else if("月".equals(type)){
			if(sLength!=7){
				return new ArrayList<Dashboard>();
			}
			searchName=dateArr[0]+"-"+dateArr[1];
		}else {
			if(sLength!=10){
				return new ArrayList<Dashboard>();
			}
			searchName=searchTime;
		}
		int temp=5;
		BasicDBObject bdObject=new BasicDBObject();
		bdObject.append("name", searchName);
		DBCursor cursor = new MongoBasicDao().findAlldataBySort(queryMongo,bdObject, "douValue");
		DBObject dbCursor;
		List<Dashboard> list=new ArrayList<Dashboard>();
		while(cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			 dbCursor = cursor.next();
			 String doctor = (String) dbCursor.get("doctor");
			 String name = (String) dbCursor.get("name");
			 Double doubValue=(Double) dbCursor.get("douValue")/10000;
			voOne.setDouValue(doubValue);
			voOne.setName(name);
			if(null!=doctor){
				temp--;
				voOne.setDoctor(doctor);
				list.add(voOne);
			}
			if(temp==0){
				break;
			}
			}
		return list;
	}

	@Override
	public List<Dashboard> queryDeptTop5ByMongo(String searchTime, String type,String queryMongo) throws Exception {
		String [] dateArr=searchTime.split("-");
		int sLength=searchTime.length();
		String searchName;
		if("年".equals(type)){
			if(sLength!=4){
				return new ArrayList<Dashboard>();
			}
			searchName=dateArr[0];
		}else if("月".equals(type)){
			if(sLength!=7){
				return new ArrayList<Dashboard>();
			}
			searchName=dateArr[0]+"-"+dateArr[1];
		}else {
			if(sLength!=10){
				return new ArrayList<Dashboard>();
			}
			searchName=searchTime;
		}
		int temp=5;
		BasicDBObject bdObject=new BasicDBObject();
		bdObject.append("name", searchName);
		DBCursor cursor = new MongoBasicDao().findAlldataBySort(queryMongo,bdObject, "douValue");
		DBObject dbCursor;
		List<Dashboard> list=new ArrayList<Dashboard>();
		while(cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			 dbCursor = cursor.next();
			 String dept = (String) dbCursor.get("dept");
			 String name = (String) dbCursor.get("name");
			 Double doubValue=(Double) dbCursor.get("douValue")/10000;
			voOne.setDouValue(doubValue);
			voOne.setName(name);
			voOne.setDept(dept);
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
	public List<Dashboard> queryFeeByMongo(String searchTime, String type,String queryMongo)  throws Exception{
		int sLength=searchTime.length();
		String[] date=searchTime.split("-");
		String searchName;
		if("年".equals(type)){
			if(sLength!=4){
				return new ArrayList<Dashboard>();
			}
			searchName=date[0];
		}else if("月".equals(type)){
			if(sLength!=7){
				return new ArrayList<Dashboard>();
			}
			searchName=date[0]+"-"+date[1];
		}else {
			if(sLength!=10){
				return new ArrayList<Dashboard>();
			}
			searchName=searchTime;
		}
		BasicDBObject bdObject = new BasicDBObject();
		List<Dashboard> list=new ArrayList<Dashboard>();
		bdObject.append("name", searchName);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			 dbCursor = cursor.next();
			 String stat = (String) dbCursor.get("stat");
			 String name = (String) dbCursor.get("name");
			 Double doubValue=(Double) dbCursor.get("douValue")/10000;
			voOne.setDouValue(doubValue);
			voOne.setName(name);
			voOne.setStat(stat);
			list.add(voOne);
			}
		return list;
	}

	@Override
	public List<Dashboard> querysequentialDrugByMongo(String searchTime,
			String type,String queryMongo)  throws Exception{
		if(searchTime==null){
			return new  ArrayList<Dashboard>();
		}
		int bLength=searchTime.length();
		if("年".equals(type)){//年
			if(bLength!=4){
				return new  ArrayList<Dashboard>();
			}
		}else if("月".equals(type)){//月
			if(bLength!=7){
				return new  ArrayList<Dashboard>();
			}
		}else{//日
			if(bLength!=10){
				return new  ArrayList<Dashboard>();
			}
		}
		String[] dateArr=this.conYear(searchTime, type);
		BasicDBObject bdObject = new BasicDBObject();
		List<Dashboard> list=new ArrayList<Dashboard>();
		for(String vo:dateArr){
		bdObject.append("name", vo);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		if(!cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			voOne.setName(vo);
			voOne.setDouValue(0.00);
			list.add(voOne);
			continue;
		}
		Dashboard voOne=new  Dashboard();
		Double dou=0.00;
		voOne.setName(vo);
		Double doubValue;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 doubValue=(Double) dbCursor.get("douValue")/10000;
			 dou+=doubValue;
			}
		voOne.setDouValue(dou);
		list.add(voOne);
		}
		return list;
	}
	@Override
	public List<Dashboard> queryAllSameFeeByMongo(String searchTime, String type,String queryMongo) throws Exception {
		if(searchTime==null){
			return new ArrayList<Dashboard>();
		}
		int eLength=searchTime.length();
		String tableName;//查询表
		if("月".equals(type)){//月
			if(eLength!=7){
				return new ArrayList<Dashboard>();
			}
		}else{//日
			if(eLength!=10){
				return new ArrayList<Dashboard>();
			}
		}
		BasicDBObject bdObject = new BasicDBObject();
		List<Dashboard> list=new ArrayList<Dashboard>();
		String[] dateArr=this.conMonth(searchTime, type);
		for(String vo:dateArr){
		bdObject.append("name", vo);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		if(!cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			voOne.setName(vo);
			voOne.setDouValue(0.00);
			list.add(voOne);
			continue;
		}
		Dashboard voOne=new  Dashboard();
		Double dou=0.00;
		voOne.setName(vo);
		Double doubValue;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 doubValue=(Double) dbCursor.get("douValue")/10000;
			 dou+=doubValue;
			}
		voOne.setDouValue(dou);
		list.add(voOne);
		}
		return list;
	}
	
}
