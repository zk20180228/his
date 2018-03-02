package cn.honry.statistics.bi.bistac.registerTotal.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.deptWorkCount.vo.DeptWorkCountVo;
import cn.honry.statistics.bi.bistac.mongoDataInit.vo.DoctorWorkCountVo;
import cn.honry.statistics.bi.bistac.registerTotal.dao.RegisterTotalDao;
import cn.honry.utils.JSONUtils;
@Repository("registerTotalDao")
public class RegisterTotalDaoImpl extends HibernateEntityDao<DoctorWorkCountVo> implements RegisterTotalDao{
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public static final String TABLENAME_KSGZLTJ_DAY="KSGZLTJ_DAY";//科室工作量统计日表
	public static final String TABLENAME_KSGZLTJ_MONTH="KSGZLTJ_MONTH";//科室工作量统计月表
	public static final String TABLENAME_KSGZLTJ_YEAR="KSGZLTJ_YEAR";//科室工作量统计月表
	
	//获取数据库连接
		private Connection getConnection() throws ClassNotFoundException, SQLException{
			System.out.println("连接数据库");
	        Class.forName("oracle.jdbc.driver.OracleDriver");//使用class类来加载程序
	        Connection conn=null;
	        conn =DriverManager.getConnection("jdbc:oracle:thin:@129.1.169.38:1521:his2","zdxxc","ZDYFYDataETL2018"); //连接数据库
			return conn;
		}
		private void closeStream(Connection conn,Statement stmt,ResultSet rs){
			try {
					if(rs!=null){
						rs.close();
					}
					if(stmt!=null){
						stmt.close();
					}
					if(conn!=null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	@Override
	public void initRegisterTotal(String stime) {
				StringBuffer sb  = new StringBuffer();
				sb.append(" select ");
				sb.append(" d.DEPT_NAME as deptName, ");
				sb.append(" f.*, ");
				sb.append(" nvl((jzmoney+yzmoney+yymoney+ghmoney+fzmoney+pzmoney),0) as totalmoney, ");
				sb.append(" nvl((jzcount+yzcount+yycount+ghcount+fzcount+pzcount),0)as totalcount ");
				sb.append(" from ( ");
				sb.append(" SELECT ");
				sb.append(" r.deptCode as deptCode, ");
				sb.append(" r.REGDATE as regDate, ");
				sb.append(" nvl(sum(r.gjjzmzjcount),0) as gjjzmzjcount, ");
				sb.append(" nvl(sum(r.gjjzmzjmoney),0) as gjjzmzjmoney, ");
				sb.append(" nvl(sum(r.sjzmzjcount),0) as sjzmzjcount, ");
				sb.append(" nvl(sum(r.sjzmzjmoney),0) as sjzmzjmoney, ");
				sb.append(" nvl(sum(r.zmzjcount),0) as zmzjcount, ");
				sb.append(" nvl(sum(r.zmzjmoney),0) as zmzjmoney, ");
				sb.append(" nvl(sum(r.jscount),0) as jscount, ");
				sb.append(" nvl(sum(r.jsmoney),0) as jsmoney, ");
				sb.append(" nvl(sum(r.fjscount),0) as fjscount, ");
				sb.append(" nvl(sum(r.fjsmoney),0) as fjsmoney, ");
				sb.append(" nvl(sum(r.jymzcount),0) as jymzcount, ");
				sb.append(" nvl(sum(r.jymzmoney),0) as jymzmoney, ");
				sb.append(" nvl(sum(r.ybyscount),0) as ybyscount, ");
				sb.append(" nvl(sum(r.ybysmoney),0) as ybysmoney, ");
				sb.append(" nvl(sum(r.zzyscount),0) as zzyscount, ");
				sb.append(" nvl(sum(r.zzysmoney),0) as zzysmoney, ");
				sb.append(" nvl(sum(r.lnyzcount),0) as lnyzcount, ");
				sb.append(" nvl(sum(r.lnyzmoney),0) as lnyzmoney, ");
				sb.append(" nvl(sum(r.slzcfcount),0) as slzcfcount, ");
				sb.append(" nvl(sum(r.slzcfmoney),0) as slzcfmoney, ");
				sb.append(" nvl(sum(r.jmjskcount),0) as jmjskcount, ");
				sb.append(" nvl(sum(r.jmjskmoney),0) as jmjskmoney, ");
				sb.append(" nvl(sum(r.jzcount),0) as jzcount, ");
				sb.append(" nvl(sum(r.jzmoney),0) as jzmoney, ");
				sb.append(" nvl(sum(r.yzcount),0) as yzcount, ");
				sb.append(" nvl(sum(r.yzmoney),0) as yzmoney, ");
				sb.append(" nvl(sum(r.yycount),0) as yycount, ");
				sb.append(" nvl(sum(r.yymoney),0) as yymoney, ");
				sb.append(" nvl(sum(r.ghcount),0) as ghcount, ");
				sb.append(" nvl(sum(r.ghmoney),0) as ghmoney, ");
				sb.append(" nvl(sum(r.fzcount),0) as fzcount, ");
				sb.append(" nvl(sum(r.fzmoney),0) as fzmoney, ");
				sb.append(" nvl(sum(r.pzcount),0) as pzcount, ");
				sb.append(" nvl(sum(r.pzmoney),0) as pzmoney ");
				sb.append(" from ( ");
				
					sb.append(" select ");
					sb.append(" t.DEPT_CODE as deptCode, ");
					sb.append(" to_char(T.REG_DATE,'yyyy-mm-dd') as regDate, ");
					sb.append(" sum(decode(t.reglevl_code,'37',1,0)) as gjjzmzjcount, ");
					sb.append(" sum(decode(t.reglevl_code,'37',nvl(t.sum_cost,0),0)) as gjjzmzjmoney, ");//nvl(t.sum_cost,0),虽然sum_cost是number类型,但是默认值并不是0，而是null，为了防止null，取0
					sb.append(" sum(decode(t.reglevl_code,'36',1,0)) as sjzmzjcount, ");
					sb.append(" sum(decode(t.reglevl_code,'36',nvl(t.sum_cost,0),0)) as sjzmzjmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'01',1,0)) as zmzjcount, ");
					sb.append(" sum(decode(t.reglevl_code,'01',nvl(t.sum_cost,0),0)) as zmzjmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'02',1,0)) as jscount, ");
					sb.append(" sum(decode(t.reglevl_code,'02',nvl(t.sum_cost,0),0)) as jsmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'03',1,0)) as fjscount, ");
					sb.append(" sum(decode(t.reglevl_code,'03',nvl(t.sum_cost,0),0)) as fjsmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'05',1,0)) as jymzcount, ");
					sb.append(" sum(decode(t.reglevl_code,'05',nvl(t.sum_cost,0),0)) as jymzmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'38',1,0)) as ybyscount, ");
					sb.append(" sum(decode(t.reglevl_code,'38',nvl(t.sum_cost,0),0)) as ybysmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'04',1,0)) as zzyscount, ");
					sb.append(" sum(decode(t.reglevl_code,'04',nvl(t.sum_cost,0),0)) as zzysmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'06',1,0)) as lnyzcount, ");
					sb.append(" sum(decode(t.reglevl_code,'06',nvl(t.sum_cost,0),0)) as lnyzmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'07',1,0)) as slzcfcount, ");
					sb.append(" sum(decode(t.reglevl_code,'07',nvl(t.sum_cost,0),0)) as slzcfmoney, ");
					sb.append(" sum(decode(t.reglevl_code,'66',1,0)) as jmjskcount, ");
					sb.append(" sum(decode(t.reglevl_code,'66',nvl(t.sum_cost,0),0)) as jmjskmoney, ");
					
					sb.append(" sum(decode(nvl(g.TriageType_Code,'null'), '0', 1, 0)) as jzcount, ");
					sb.append(" sum(decode( nvl(g.TriageType_Code,'null'), '0', nvl(t.sum_cost, 0), 0)) as jzmoney, ");
					
					sb.append(" sum(decode(nvl(g.TriageType_Code,'null'), '1', 1, 0)) as yzcount, ");
					sb.append(" sum(decode(nvl(g.TriageType_Code,'null'),'1',nvl(t.sum_cost, 0),0)) as yzmoney, ");
					
					sb.append(" sum(decode(decode(nvl(g.TriageType_Code,t.YNBOOK||'yynull'),'1yynull','1yynull','2','1yynull',decode(nvl(instr('345',g.TriageType_Code),0),0,'null',t.YNBOOK||'yynull') ), '1yynull', 1, 0)) as yycount, ");
					sb.append(" sum(decode(decode(nvl(g.TriageType_Code,t.YNBOOK||'yynull'),'1yynull','1yynull','2','1yynull',decode(nvl(instr('345',g.TriageType_Code),0),0,'null',t.YNBOOK||'yynull') ), '1yynull', nvl(t.sum_cost, 0), 0)) as yymoney, ");
					
					sb.append(" sum(decode(g.TriageType_Code,'3',decode(t.YNBOOK,'0',1,0),0)) as ghcount, ");
					sb.append(" sum(decode(g.TriageType_Code,'3',decode(t.YNBOOK,'0',nvl(t.sum_cost, 0),0),0)) as ghmoney, ");
					
					sb.append(" sum(decode(g.TriageType_Code,'5',decode(t.YNBOOK,'0',1,0),0)) as fzcount, ");
					sb.append(" sum(decode(g.TriageType_Code,'5',decode(t.YNBOOK,'0',nvl(t.sum_cost, 0),0),0)) as fzmoney, ");
					
					sb.append(" sum(decode(nvl(g.TriageType_Code,t.YNBOOK||'pznull'),'0pznull',1,'4',decode(t.YNBOOK,'0',1,0))) as pzcount, ");
					sb.append(" sum(decode(nvl(g.TriageType_Code,t.YNBOOK||'pznull'),'0pznull',nvl(t.sum_cost, 0),'4',decode(t.YNBOOK,'0',nvl(t.sum_cost, 0),0))) as pzmoney ");
					sb.append(" FROM ");
					sb.append(" T_REGISTER_MAIN t left join T_REGISTER_TRIAGE g on t.CARD_NO=g.CARD_NO and t.CLINIC_CODE=g.CLINIC_CODE ");
					sb.append(" WHERE ");
					sb.append(" t.DEL_FLG = 0 ");
					sb.append(" AND t.STOP_FLG = 0 ");
					sb.append(" AND t.trans_type = 1 ");
					sb.append(" AND t.IN_STATE = 0 ");
					sb.append(" AND t.VALID_FLAG = 1");	
					sb.append(" AND t.REG_DATE >= TO_DATE ('"+stime+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
					sb.append(" AND t.REG_DATE <= TO_DATE ('"+stime+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
					sb.append(" GROUP BY ");
					sb.append(" t.DEPT_CODE, ");
					sb.append(" t.REG_DATE ");
					
				sb.append(" ) r ");
				sb.append(" group by ");
				sb.append(" r.deptCode, ");
				sb.append(" r.REGDATE ");
				sb.append(" ) f ,T_DEPARTMENT d  ");
				sb.append(" where ");
				sb.append(" f.DEPTCODE=d.dept_code ");
				//按天查询，按天更新,调用者是一天一天传的时间
				List<DeptWorkCountVo> list=this.getSession().createSQLQuery(sb.toString())
						.addScalar("deptName")
						.addScalar("deptCode")
						.addScalar("regDate")
						.addScalar("gjjzmzjcount",Hibernate.INTEGER)
						.addScalar("gjjzmzjmoney",Hibernate.DOUBLE)
						.addScalar("sjzmzjcount",Hibernate.INTEGER)
						.addScalar("sjzmzjmoney",Hibernate.DOUBLE)
						.addScalar("zmzjcount",Hibernate.INTEGER)
						.addScalar("zmzjmoney",Hibernate.DOUBLE)
						.addScalar("jscount",Hibernate.INTEGER)
						.addScalar("jsmoney",Hibernate.DOUBLE)
						.addScalar("fjscount",Hibernate.INTEGER)
						.addScalar("fjsmoney",Hibernate.DOUBLE)
						.addScalar("jymzcount",Hibernate.INTEGER)
						.addScalar("jymzmoney",Hibernate.DOUBLE)
						.addScalar("ybyscount",Hibernate.INTEGER)
						.addScalar("ybysmoney",Hibernate.DOUBLE)
						.addScalar("zzyscount",Hibernate.INTEGER)
						.addScalar("zzysmoney",Hibernate.DOUBLE)
						.addScalar("lnyzcount",Hibernate.INTEGER)
						.addScalar("lnyzmoney",Hibernate.DOUBLE)
						.addScalar("slzcfcount",Hibernate.INTEGER)
						.addScalar("slzcfmoney",Hibernate.DOUBLE)
						.addScalar("jmjskcount",Hibernate.INTEGER)
						.addScalar("jmjskmoney",Hibernate.DOUBLE)
						.addScalar("jzcount",Hibernate.INTEGER)
						.addScalar("jzmoney",Hibernate.DOUBLE)
						.addScalar("yzcount",Hibernate.INTEGER)
						.addScalar("yzmoney",Hibernate.DOUBLE)
						.addScalar("yycount",Hibernate.INTEGER)
						.addScalar("yymoney",Hibernate.DOUBLE)
						.addScalar("ghcount",Hibernate.INTEGER)
						.addScalar("ghmoney",Hibernate.DOUBLE)
						.addScalar("fzcount",Hibernate.INTEGER)
						.addScalar("fzmoney",Hibernate.DOUBLE)
						.addScalar("pzcount",Hibernate.INTEGER)
						.addScalar("pzmoney",Hibernate.DOUBLE)
						.addScalar("totalcount",Hibernate.INTEGER)
						.addScalar("totalmoney",Hibernate.DOUBLE)
						.setResultTransformer(Transformers.aliasToBean(DeptWorkCountVo.class)).list();
					//保存数据
					saveKSDate(list,stime);
				
	}
	private void saveKSDate(List<DeptWorkCountVo> list,String stime){
		DBObject query = new BasicDBObject();//删除相同的时间和科室的文档
		query.put("regDate", stime);
		new MongoBasicDao().remove(TABLENAME_KSGZLTJ_DAY, query);//先删除
		if(list!=null&&list.size()>0){
			List<DBObject> voList = new ArrayList<DBObject>();
					for(DeptWorkCountVo v:list){
						BasicDBObject obj = new BasicDBObject();
						obj.append("deptCode", v.getDeptCode());
						obj.append("regDate", v.getRegDate());
						String json = JSONUtils.toJson(v);
						obj.append("value", json);
						voList.add(obj);
					}
					new MongoBasicDao().insertDataByList(TABLENAME_KSGZLTJ_DAY, voList);//添加新数据
					//同步更新月和年
					this.initRegisterTotalMonthAndYear(stime.substring(0, 7), "2");
					this.initRegisterTotalMonthAndYear(stime.substring(0, 4), "3");
		}
	}
	@Override
	public void initRegisterTotalMonthAndYear(String stime,String type) {
		if("2".equals(type)){//初始化月
			//得到当年的月的最后一天
			stime=stime+"-01";
			String endTime=returnEndTime(stime);
			insertDataToMongo(TABLENAME_KSGZLTJ_DAY,TABLENAME_KSGZLTJ_MONTH, stime, endTime, type);
		}
		if("3".equals(type)){//初始化年
			String startTime = stime+"-01";
			String endTime = stime+"-12";
			insertDataToMongo(TABLENAME_KSGZLTJ_MONTH,TABLENAME_KSGZLTJ_YEAR, startTime, endTime, type);
		}
	}
	
	public String getLastDay(String date){
		date= date.substring(0, 7);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date time =null;
		try {
			 time = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 Calendar  calendar =  Calendar.getInstance(); 
		 calendar.setTime(time);
		 final  int  lastDay  =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		 Date   lastDate   =   calendar.getTime();  
	     lastDate.setDate(lastDay);  
	     SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat1.format(lastDate).substring(8, 10);
	}
	
	public void insertDataToMongo(String readCollection,String insertCollection,String startTime,String endTime,String type){
		BasicDBObject dbObject = new BasicDBObject();
		BasicDBList dbList = new BasicDBList();
		dbList.add(new BasicDBObject("regDate",new BasicDBObject("$gte",startTime)));
		dbList.add(new BasicDBObject("regDate",new BasicDBObject("$lte",endTime)));
		dbObject.put("$and",dbList);
		DBCursor dbCursor = new MongoBasicDao().findAlldata(readCollection, dbObject);
		HashMap<String,DeptWorkCountVo> map = new HashMap<String, DeptWorkCountVo>();//去重时的map
		while(dbCursor.hasNext()){//查询，本月/年内，相同科室编号的要相加，不能重复,去重时放入map
			DBObject cursor = dbCursor.next();
			String s = cursor.get("value").toString();//一个value就是一个对应的vo
			String code =cursor.get("deptCode").toString();
			DeptWorkCountVo v=null;
			try {
				v = JSONUtils.fromJson(s,DeptWorkCountVo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			v.setDeptCode(code);//设置科室编号
			DeptWorkCountVo vo = map.get(code);
			if(vo!=null){//相同编号的，结果进行相加
				vo.setGjjzmzjcount(vo.getGjjzmzjcount()+v.getGjjzmzjcount());
				vo.setGjjzmzjmoney(vo.getGjjzmzjmoney()+v.getGjjzmzjmoney());
				vo.setSjzmzjcount(vo.getSjzmzjcount()+v.getSjzmzjcount());
				vo.setSjzmzjmoney(vo.getSjzmzjmoney()+v.getSjzmzjmoney());
				vo.setZmzjcount(vo.getZmzjcount()+v.getZmzjcount());
				vo.setZmzjmoney(vo.getZmzjmoney()+v.getZmzjmoney());
				vo.setJscount(vo.getJscount()+v.getJscount());
				vo.setJsmoney(vo.getJsmoney()+v.getJsmoney());
				vo.setFjscount(vo.getFjscount()+v.getFjscount());
				vo.setFjsmoney(vo.getFjsmoney()+v.getFjsmoney());
				vo.setJymzcount(vo.getJymzcount()+v.getJymzcount());
				vo.setJymzmoney(vo.getJymzmoney()+v.getJymzmoney());
				vo.setYbyscount(vo.getYbyscount()+v.getYbyscount());
				vo.setYbysmoney(vo.getYbysmoney()+v.getYbysmoney());
				vo.setZzyscount(vo.getZzyscount()+v.getZzyscount());
				vo.setZzysmoney(vo.getZzysmoney()+v.getZzysmoney());
				vo.setLnyzcount(vo.getLnyzcount()+v.getLnyzcount());
				vo.setLnyzmoney(vo.getLnyzmoney()+v.getLnyzmoney());
				vo.setSlzcfcount(vo.getSlzcfcount()+v.getSlzcfcount());
				vo.setSlzcfmoney(vo.getSlzcfmoney()+v.getSlzcfmoney());
				vo.setJmjskcount(vo.getJmjskcount()+v.getJmjskcount());
				vo.setJmjskmoney(vo.getJmjskmoney()+v.getJmjskmoney());
				vo.setJzcount(vo.getJzcount()+v.getJzcount());
				vo.setJzmoney(vo.getJzmoney()+v.getJzmoney());
				vo.setYzcount(vo.getYzcount()+v.getYzcount());
				vo.setYzmoney(vo.getYzmoney()+v.getYzmoney());
				vo.setYycount(vo.getYycount()+v.getYycount());
				vo.setYymoney(vo.getYymoney()+v.getYymoney());
				vo.setGhcount(vo.getGhcount()+v.getGhcount());
				vo.setGhmoney(vo.getGhmoney()+v.getGhmoney());
				vo.setFzcount(vo.getFzcount()+v.getFzcount());
				vo.setFzmoney(vo.getFzmoney()+v.getFzmoney());
				vo.setPzcount(vo.getPzcount()+v.getPzcount());
				vo.setPzmoney(vo.getPzmoney()+v.getPzmoney());
				vo.setTotalcount(vo.getTotalcount()+v.getTotalcount());
				vo.setTotalmoney(vo.getTotalmoney()+v.getTotalmoney());			
			}else{
				try {
					DeptWorkCountVo d = new DeptWorkCountVo();
					BeanUtils.copyProperties(d, v);
					map.put(code, d);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
			}
			
		}
		//将以上的数据存入mongo中
		ArrayList<DeptWorkCountVo> list = new ArrayList<DeptWorkCountVo>();
		list.addAll(map.values());
		
		if("2".equals(type)){
			startTime=startTime.substring(0,7);
		}
		if("3".equals(type)){
			startTime=startTime.substring(0,4);
		}
		
		DBObject query = new BasicDBObject();//删除相同的时间和科室的文档
		query.put("regDate", startTime);
		new MongoBasicDao().remove(insertCollection, query);//先删除
		
		if(list.size()>0){
			List<DBObject> voList = new ArrayList<DBObject>();
			for(DeptWorkCountVo dv:list){
				BasicDBObject obj = new BasicDBObject();
				obj.append("regDate", startTime);//存月
				String dCode=dv.getDeptCode();
				obj.append("deptCode",dCode);
				String json= JSONUtils.toJson(dv);
				obj.append("value", json);
				voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(insertCollection, voList);//添加新数据
		}
	}
	@Override
	public void initRegisterDoctorTotal(String searchTime) {
		StringBuffer buffer=new StringBuffer();
		buffer.append("select t.doct_code doctorCode,e.EMPLOYEE_NAME as doctorName,");
		buffer.append("count(1) doctorGzlNum,sum(t.OWN_COST + t.PUB_COST + t.PAY_COST) doctorCost ");
		buffer.append("FROM T_REGISTER_MAIN t,t_employee e ");
		buffer.append("WHERE t.trans_type = 1 AND t.IN_STATE = 0 AND t.VALID_FLAG = 1 ");
		buffer.append("AND t.REG_DATE >=TO_DATE('"+searchTime+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
		buffer.append("AND t.REG_DATE <=TO_DATE('"+searchTime+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
		buffer.append("and t.doct_code = e.EMPLOYEE_JOBNO ");
		buffer.append("and t.doct_code is not null ");
		buffer.append("GROUP BY e.EMPLOYEE_NAME,t.doct_code ");
		List<DoctorWorkCountVo> list=this.getSession().createSQLQuery(buffer.toString()).addScalar("doctorCode")
				.addScalar("doctorName").addScalar("doctorGzlNum",Hibernate.INTEGER).addScalar("doctorCost",Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(DoctorWorkCountVo.class)).list();
		saveDoctorDate(searchTime,list);
	}
	private void saveDoctorDate(String searchTime,List<DoctorWorkCountVo> list){
		DBObject query = new BasicDBObject();
		query.put("regDate",searchTime);
		String saveMonoDb="GHYSGZLTJ_DAY";
		new MongoBasicDao().remove(saveMonoDb, query);
		if(list.size()>0){
			List<DBObject> voList = new ArrayList<DBObject>();
			for(DoctorWorkCountVo vo:list){
				BasicDBObject obj = new BasicDBObject();
				obj.append("regDate",searchTime);
				obj.append("doctorName", vo.getDoctorName());
				obj.append("doctorCode", vo.getDoctorCode());
				obj.append("doctorGzlNum", vo.getDoctorGzlNum());
				obj.append("doctorCost", vo.getDoctorCost());
				voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(saveMonoDb, voList);//添加新数据
			this.initRegisterDoctorTotalMonthAndYear(searchTime.substring(0,7), "2");
			this.initRegisterDoctorTotalMonthAndYear(searchTime.substring(0,4), "3");
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
	@Override
	public void initRegisterDoctorTotalMonthAndYear(String date, String type) {
		String temp=null;
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		String menuAlias="GHYSGZLTJ";
		if("2".equals(type)){
			temp=date.substring(0,7);
			//计算最后一个月日期
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_DAY";
			saveMongo=menuAlias+"_MONTH";
			end=returnEndTime(date);
		}else {
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_MONTH";
			saveMongo=menuAlias+"_YEAR";
		}
		BasicDBObject bdObject = new BasicDBObject();
		
		BasicDBList dateList=new BasicDBList();
		BasicDBObject data1= new BasicDBObject();//查询开始时间
		BasicDBObject data2= new BasicDBObject();//查询结束时间
		
	    data1.append("regDate", new BasicDBObject("$gte",begin));
	    data2.append("regDate", new BasicDBObject("$lte",end));
	    
	    dateList.add(data1);
	    dateList.add(data2);
	    bdObject.put("$and", dateList);
	    
	    DBObject _group = new BasicDBObject("doctorCode", "$doctorCode").append("doctorName", "$doctorName");  
		DBObject groupFields = new BasicDBObject("_id", _group);
		groupFields.put("doctorGzlNum", new BasicDBObject("$sum","$doctorGzlNum")); 
		groupFields.put("doctorCost", new BasicDBObject("$sum","$doctorCost")); 
		DBObject match = new BasicDBObject("$match", bdObject);
		DBObject group = new BasicDBObject("$group", groupFields);
		AggregationOutput output = new MongoBasicDao().findGroupBy(queryMongo, match, group);
		Iterator<DBObject> it = output.results().iterator();
		List<DBObject> voList = new ArrayList<DBObject>();
		while(it.hasNext()){
			BasicDBObject obj = new BasicDBObject();
			BasicDBObject dbo = ( BasicDBObject ) it.next();
			BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
			obj.append("regDate", date);
			obj.append("doctorName", keyValus.getString("doctorName"));
			obj.append("doctorCode", keyValus.getString("doctorCode"));
			obj.append("doctorGzlNum", dbo.getInt("doctorGzlNum"));
			obj.append("doctorCost", dbo.getDouble("doctorCost"));
			voList.add(obj);
		}
		DBObject query = new BasicDBObject();
		query.put("regDate",date);
		new MongoBasicDao().remove(saveMongo, query);
		if(voList.size()>0){
			new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
		}
	}
	@Override
	public void tenTimingPerformRegisterTotal(String stime) {
		Connection conn=null;
		Statement stmt= null;//表示数据库的更新
		ResultSet result = null;//查询数据库 
		try {
			StringBuffer sb  = new StringBuffer();
			sb.append(" select ");
			sb.append(" d.DEPT_NAME as deptName, ");
			sb.append(" f.*, ");
			sb.append(" nvl((jzmoney+yzmoney+yymoney+ghmoney+fzmoney+pzmoney),0) as totalmoney, ");
			sb.append(" nvl((jzcount+yzcount+yycount+ghcount+fzcount+pzcount),0)as totalcount ");
			sb.append(" from ( ");
			sb.append(" SELECT ");
			sb.append(" r.deptCode as deptCode, ");
			sb.append(" r.REGDATE as regDate, ");
			sb.append(" nvl(sum(r.gjjzmzjcount),0) as gjjzmzjcount, ");
			sb.append(" nvl(sum(r.gjjzmzjmoney),0) as gjjzmzjmoney, ");
			sb.append(" nvl(sum(r.sjzmzjcount),0) as sjzmzjcount, ");
			sb.append(" nvl(sum(r.sjzmzjmoney),0) as sjzmzjmoney, ");
			sb.append(" nvl(sum(r.zmzjcount),0) as zmzjcount, ");
			sb.append(" nvl(sum(r.zmzjmoney),0) as zmzjmoney, ");
			sb.append(" nvl(sum(r.jscount),0) as jscount, ");
			sb.append(" nvl(sum(r.jsmoney),0) as jsmoney, ");
			sb.append(" nvl(sum(r.fjscount),0) as fjscount, ");
			sb.append(" nvl(sum(r.fjsmoney),0) as fjsmoney, ");
			sb.append(" nvl(sum(r.jymzcount),0) as jymzcount, ");
			sb.append(" nvl(sum(r.jymzmoney),0) as jymzmoney, ");
			sb.append(" nvl(sum(r.ybyscount),0) as ybyscount, ");
			sb.append(" nvl(sum(r.ybysmoney),0) as ybysmoney, ");
			sb.append(" nvl(sum(r.zzyscount),0) as zzyscount, ");
			sb.append(" nvl(sum(r.zzysmoney),0) as zzysmoney, ");
			sb.append(" nvl(sum(r.lnyzcount),0) as lnyzcount, ");
			sb.append(" nvl(sum(r.lnyzmoney),0) as lnyzmoney, ");
			sb.append(" nvl(sum(r.slzcfcount),0) as slzcfcount, ");
			sb.append(" nvl(sum(r.slzcfmoney),0) as slzcfmoney, ");
			sb.append(" nvl(sum(r.jmjskcount),0) as jmjskcount, ");
			sb.append(" nvl(sum(r.jmjskmoney),0) as jmjskmoney, ");
			sb.append(" nvl(sum(r.jzcount),0) as jzcount, ");
			sb.append(" nvl(sum(r.jzmoney),0) as jzmoney, ");
			sb.append(" nvl(sum(r.yzcount),0) as yzcount, ");
			sb.append(" nvl(sum(r.yzmoney),0) as yzmoney, ");
			sb.append(" nvl(sum(r.yycount),0) as yycount, ");
			sb.append(" nvl(sum(r.yymoney),0) as yymoney, ");
			sb.append(" nvl(sum(r.ghcount),0) as ghcount, ");
			sb.append(" nvl(sum(r.ghmoney),0) as ghmoney, ");
			sb.append(" nvl(sum(r.fzcount),0) as fzcount, ");
			sb.append(" nvl(sum(r.fzmoney),0) as fzmoney, ");
			sb.append(" nvl(sum(r.pzcount),0) as pzcount, ");
			sb.append(" nvl(sum(r.pzmoney),0) as pzmoney ");
			sb.append(" from ( ");
			
			sb.append(" select ");
			sb.append(" t.DEPT_CODE as deptCode, ");
			sb.append(" to_char(T.REG_DATE,'yyyy-mm-dd') as regDate, ");
			sb.append(" sum(decode(t.reglevl_code,'37',1,0)) as gjjzmzjcount, ");
			sb.append(" sum(decode(t.reglevl_code,'37',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as gjjzmzjmoney, ");//nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),虽然sum_cost是number类型,但是默认值并不是0，而是null，为了防止null，取0
			sb.append(" sum(decode(t.reglevl_code,'36',1,0)) as sjzmzjcount, ");
			sb.append(" sum(decode(t.reglevl_code,'36',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as sjzmzjmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'01',1,0)) as zmzjcount, ");
			sb.append(" sum(decode(t.reglevl_code,'01',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as zmzjmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'02',1,0)) as jscount, ");
			sb.append(" sum(decode(t.reglevl_code,'02',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as jsmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'03',1,0)) as fjscount, ");
			sb.append(" sum(decode(t.reglevl_code,'03',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as fjsmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'05',1,0)) as jymzcount, ");
			sb.append(" sum(decode(t.reglevl_code,'05',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as jymzmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'38',1,0)) as ybyscount, ");
			sb.append(" sum(decode(t.reglevl_code,'38',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as ybysmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'04',1,0)) as zzyscount, ");
			sb.append(" sum(decode(t.reglevl_code,'04',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as zzysmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'06',1,0)) as lnyzcount, ");
			sb.append(" sum(decode(t.reglevl_code,'06',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as lnyzmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'07',1,0)) as slzcfcount, ");
			sb.append(" sum(decode(t.reglevl_code,'07',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as slzcfmoney, ");
			sb.append(" sum(decode(t.reglevl_code,'66',1,0)) as jmjskcount, ");
			sb.append(" sum(decode(t.reglevl_code,'66',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST),0),0)) as jmjskmoney, ");
			
			sb.append(" sum(decode(nvl(g.TriageType_Code,'null'), '0', 1, 0)) as jzcount, ");
			sb.append(" sum(decode( nvl(g.TriageType_Code,'null'), '0', nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST), 0), 0)) as jzmoney, ");
			
			sb.append(" sum(decode(nvl(g.TriageType_Code,'null'), '1', 1, 0)) as yzcount, ");
			sb.append(" sum(decode(nvl(g.TriageType_Code,'null'),'1',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST), 0),0)) as yzmoney, ");
			
			sb.append(" sum(decode(decode(nvl(g.TriageType_Code,t.YNBOOK||'yynull'),'1yynull','1yynull','2','1yynull',decode(nvl(instr('345',g.TriageType_Code),0),0,'null',t.YNBOOK||'yynull') ), '1yynull', 1, 0)) as yycount, ");
			sb.append(" sum(decode(decode(nvl(g.TriageType_Code,t.YNBOOK||'yynull'),'1yynull','1yynull','2','1yynull',decode(nvl(instr('345',g.TriageType_Code),0),0,'null',t.YNBOOK||'yynull') ), '1yynull', nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST), 0), 0)) as yymoney, ");
			
			sb.append(" sum(decode(g.TriageType_Code,'3',decode(t.YNBOOK,'0',1,0),0)) as ghcount, ");
			sb.append(" sum(decode(g.TriageType_Code,'3',decode(t.YNBOOK,'0',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST), 0),0),0)) as ghmoney, ");
			
			sb.append(" sum(decode(g.TriageType_Code,'5',decode(t.YNBOOK,'0',1,0),0)) as fzcount, ");
			sb.append(" sum(decode(g.TriageType_Code,'5',decode(t.YNBOOK,'0',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST), 0),0),0)) as fzmoney, ");
			
			sb.append(" sum(decode(nvl(g.TriageType_Code,t.YNBOOK||'pznull'),'0pznull',1,'4',decode(t.YNBOOK,'0',1,0))) as pzcount, ");
			sb.append(" sum(decode(nvl(g.TriageType_Code,t.YNBOOK||'pznull'),'0pznull',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST), 0),'4',decode(t.YNBOOK,'0',nvl((t.OWN_COST + t.PUB_COST + t.PAY_COST), 0),0))) as pzmoney ");
				sb.append(" FROM ");
				sb.append(" zdhis.FIN_OPR_REGISTER t left join zdhis.fin_opr_registertriage g on t.CARD_NO=g.CARD_NO and t.CLINIC_CODE=g.CLINIC_CODE ");
				sb.append(" WHERE ");
				sb.append(" t.trans_type = '1' AND t.IN_STATE = 'N' AND t.VALID_FLAG = '1' ");
				sb.append(" AND t.REG_DATE >= TO_DATE ('"+stime+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" AND t.REG_DATE <= TO_DATE ('"+stime+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" GROUP BY ");
				sb.append(" t.DEPT_CODE, ");
				sb.append(" t.REG_DATE ");
			sb.append(" ) r ");
			sb.append(" group by ");
			sb.append(" r.deptCode, ");
			sb.append(" r.REGDATE ");
			sb.append(" ) f ,zdhis.com_DEPARTMENT d  ");
			sb.append(" where ");
			sb.append(" f.DEPTCODE=d.dept_code ");
			
			conn=this.getConnection();
			stmt = conn.createStatement();
			result=stmt.executeQuery(sb.toString());
			List<DeptWorkCountVo> list=new ArrayList<DeptWorkCountVo>();
			while(result.next()){
				DeptWorkCountVo vo=new DeptWorkCountVo();
				vo.setDeptCode(result.getString("deptCode"));
				vo.setDeptName(result.getString("deptName"));
				vo.setRegDate(result.getString("regDate"));
				vo.setGjjzmzjcount(result.getInt("gjjzmzjcount"));
				vo.setGjjzmzjmoney(result.getDouble("gjjzmzjmoney"));
				vo.setSjzmzjcount(result.getInt("sjzmzjcount"));
				vo.setSjzmzjmoney(result.getDouble("sjzmzjmoney"));
				vo.setZmzjcount(result.getInt("zmzjcount"));
				vo.setZmzjmoney(result.getDouble("zmzjmoney"));
				vo.setJscount(result.getInt("jscount"));
				vo.setJsmoney(result.getDouble("jsmoney"));
				vo.setFjscount(result.getInt("fjscount"));
				vo.setFjsmoney(result.getDouble("fjsmoney"));
				vo.setJymzcount(result.getInt("jymzcount"));
				vo.setJymzmoney(result.getDouble("jymzmoney"));
				vo.setYbyscount(result.getInt("ybyscount"));
				vo.setYbysmoney(result.getDouble("ybysmoney"));
				vo.setZzyscount(result.getInt("zzyscount"));
				vo.setZzysmoney(result.getDouble("zzysmoney"));
				vo.setLnyzcount(result.getInt("lnyzcount"));
				vo.setLnyzmoney(result.getDouble("lnyzmoney"));
				vo.setSlzcfcount(result.getInt("slzcfcount"));
				vo.setSlzcfmoney(result.getDouble("slzcfmoney"));
				vo.setJmjskcount(result.getInt("jmjskcount"));
				vo.setJmjskmoney(result.getDouble("jmjskmoney"));
				vo.setJzcount(result.getInt("jzcount"));
				vo.setJzmoney(result.getDouble("jzmoney"));
				vo.setYzcount(result.getInt("yzcount"));
				vo.setYzmoney(result.getDouble("yzmoney"));
				vo.setYycount(result.getInt("yycount"));
				vo.setYymoney(result.getDouble("yymoney"));
				vo.setGhcount(result.getInt("ghcount"));
				vo.setGhmoney(result.getDouble("ghmoney"));
				vo.setFzcount(result.getInt("fzcount"));
				vo.setFzmoney(result.getDouble("fzmoney"));
				vo.setPzcount(result.getInt("pzcount"));
				vo.setPzmoney(result.getDouble("pzmoney"));
				vo.setTotalcount(result.getInt("totalcount"));
				vo.setTotalmoney(result.getDouble("totalmoney"));
				list.add(vo);
			}
				saveKSDate(list,stime);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			this.closeStream(conn, stmt, result);
		}
	}
	@Override
	public void tenTimingPerformRegisterDoctorTotal(String searchTime) {
		
		Connection conn=null;
		Statement stmt= null;//表示数据库的更新
		ResultSet result = null;//查询数据库 
		try {
			StringBuffer buffer=new StringBuffer();
			buffer.append("select t.doct_code doctorCode,e.empl_name as doctorName,");
			buffer.append("count(1) doctorGzlNum,sum(t.OWN_COST + t.PUB_COST + t.PAY_COST) doctorCost ");
			buffer.append("FROM zdhis.FIN_OPR_REGISTER t,zdhis.com_employee e ");
			buffer.append("WHERE t.trans_type = '1' AND t.IN_STATE = 'N' AND t.VALID_FLAG = '1' ");
			buffer.append("AND t.REG_DATE >=TO_DATE('"+searchTime+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("AND t.REG_DATE <=TO_DATE('"+searchTime+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
			buffer.append("and t.doct_code = e.EMPL_CODE ");
			buffer.append("and t.doct_code is not null ");
			buffer.append("GROUP BY e.empl_name,t.doct_code ");
			conn=this.getConnection();
			stmt = conn.createStatement();
			result=stmt.executeQuery(buffer.toString());
			List<DoctorWorkCountVo> list=new ArrayList<DoctorWorkCountVo>();
			while(result.next()){
				DoctorWorkCountVo vo=new DoctorWorkCountVo();
				vo.setDoctorCode(result.getString("doctorCode"));
				vo.setDoctorName(result.getString("doctorName"));
				vo.setDoctorGzlNum(result.getInt("doctorGzlNum"));
				vo.setDoctorCost(result.getDouble("doctorCost"));
				list.add(vo);
			}
			saveDoctorDate(searchTime,list);
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			this.closeStream(conn, stmt, result);
		}
		
		
		
	}

}
