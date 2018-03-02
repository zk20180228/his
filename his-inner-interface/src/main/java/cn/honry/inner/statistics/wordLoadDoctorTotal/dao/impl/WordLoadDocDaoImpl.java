package cn.honry.inner.statistics.wordLoadDoctorTotal.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.Dashboard;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.HospitalWork;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.PcWorkTotal;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.WordLoadVO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Repository("wordLoadDocDao")
@SuppressWarnings("all")
public class WordLoadDocDaoImpl extends HibernateDaoSupport  implements WordLoadDocDao {
	private final String[] inpatientInfo={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};//住院主表
	private final String[] inpatiengOrder={"T_INPATIENT_ORDER_NOW","T_INPATIENT_ORDER"};//医嘱表
	private final String[] inpatientMedi={"T_INPATIENT_MEDICINELIST_NOW","T_INPATIENT_MEDICINELIST"};
	private final String[] inpatientItem={"T_INPATIENT_ITEMLIST_NOW","T_INPATIENT_ITEMLIST"};
	private final String[] outFee={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};
	private final String[] inpatientFee={"T_INPATIENT_FEEINFO_NOW","T_INPATIENT_FEEINFO"};
	private final String MZ="MZ";
	private final String ZY="ZY";
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Autowired
	@Qualifier(value="parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjDao")
	private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
	@Override
	public List<String> returnInTables(String startTime,String endTime, String[] tables,String type) {
		if(!"".equals(startTime)&&!"".equals(endTime)){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date sTime = DateUtils.parseDateY_M_D(startTime);//当天
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		
		List<String> tnL = null;
		try {
			String dateNum;
			if(MZ.equals(type)){
				dateNum=parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			}else{
				dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if(dateNum.equals("1")){
					dateNum="30";
				}
			}
			
			//3.获得当前时间
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,tables[1],startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,tables[1],yNum+1);
					tnL.add(0,tables[0]);
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add(tables[0]);
			}
			} catch (Exception e) {
				e.printStackTrace();
				tnL = new ArrayList<String>();
			}
		return tnL;
		}
		return null;
	}
	
	
	/**住院医生工作量明细**/
	@Override
	public void init_ZYYSGZLTJ_Detail(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
		Date beginDate=new Date();
		
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<String> tnL=this.returnInTables(begin, end, inpatiengOrder,ZY);
		if(tnL!=null){
			StringBuffer buffer=new StringBuffer(200);
			buffer.append("select to_char(b.data1,'yyyy-mm-dd') as workDate,b.doc as doctorCode,b.docName as doctorName,sum(b.num) as workTotal,sum(b.total) as moneyTotal,b.deptCode as deptCode,b.deptName as deptName  from( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select t"+i+".mo_date as data1,t"+i+".doc_code as doc,t"+i+".doc_name docName,");
				buffer.append("trunc(t"+i+".qty_tot, 2) as num,trunc(t"+i+".qty_tot * t"+i+".item_price, 2) as total,");
				buffer.append("t"+i+".exec_dpcd deptCode,t"+i+".exec_dpnm as deptName ");
				buffer.append("from "+tnL.get(i)+" t"+i+" ");
				buffer.append("where t"+i+".mo_stat = 2 ");
				buffer.append("and t"+i+".mo_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".mo_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss')  ");
			}
			buffer.append(") b group by b.data1,b.doc,b.docName,b.deptCode,b.deptName");
			List<WordLoadVO> list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
				@Override
				public WordLoadVO mapRow(ResultSet rs, int rownum)
						throws SQLException {
					WordLoadVO vo=new WordLoadVO();
					vo.setWorkDate(rs.getString("workDate"));//医嘱时间
					vo.setOpenTotal(rs.getDouble("workTotal"));//开立数量
					vo.setDoctorCode(rs.getString("doctorCode"));//医生code
					vo.setDoctorName(rs.getString("doctorName"));
					vo.setMoneyTotal(rs.getDouble("moneyTotal"));//开立金额
					vo.setDeptCode(rs.getString("deptCode"));//科室Code
					vo.setDeptName(rs.getString("deptName"));
					return vo;
				}
			});
			
			DBObject query = new BasicDBObject();
			query.put("workdate", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_DETAIL_DAY", query);//删除原来的数据
			
			if(list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				for(WordLoadVO vo:list){
					if(vo.getOpenTotal()!=null&&vo.getMoneyTotal()!=null){
						BasicDBObject obj = new BasicDBObject();
						obj.append("total", vo.getOpenTotal());
						obj.append("workdate", vo.getWorkDate());
						obj.append("deptCode", vo.getDeptCode());
						obj.append("deptName", vo.getDeptName());
						obj.append("doctorCode", vo.getDoctorCode());
						obj.append("doctorName", vo.getDoctorName());
						obj.append("moneyTotal", vo.getMoneyTotal());
						voList.add(obj);
						}
				     }
				new MongoBasicDao().insertDataByList(menuAlias+"_DETAIL_DAY", voList);//添加新数据
				voList=null;
				if(!"HIS".equals(type)){
					init_ZYYSGZLTJ_Detail_MoreDay(menuAlias,"2",date,"DOC");//月更新医生
					init_ZYYSGZLTJ_Detail_MoreDay(menuAlias,"3",date,"DOC");//年更新医生
					init_ZYYSGZLTJ_Detail_MoreDay(menuAlias,"2",date,"DEPT");//月更新医生
					init_ZYYSGZLTJ_Detail_MoreDay(menuAlias,"3",date,"DEPT");//年更新医生
				}
				}
			this.saveMongoLog(beginDate, menuAlias+"_DETAIL_DAY", list, date);
		}
		}
	}
	/**
	 * 
	 * @param menuAlias 栏目名
	 * @param type type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @param date
	 */
	public void init_ZYYSGZLTJ_Detail_MoreDay(String menuAlias, String type, String date,String deptOrDoc){
		Date beginDate=new Date();
		
		String temp;//临时时间
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		Double dou;//金额
		Double con;//数量
		String temp1;//map的key
		Map<String,Double> map=new HashMap<String,Double>();//存储开立数量
		Map<String,Double> map1=new HashMap<String,Double>();//存储金额
		
		String searchNo1;//查询的字段
		String searchNo2;//查询的字段
		String tempMenuAlias=menuAlias;
		if("DOC".equals(deptOrDoc)){
			tempMenuAlias=menuAlias+"_DOC";
			searchNo1="doctorCode";
			searchNo2="doctorName";
		}else{
			tempMenuAlias=menuAlias+"_DEPT";
			searchNo1="deptCode";
			searchNo2="deptName";
		}
		
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_DETAIL_DAY";
			saveMongo=tempMenuAlias+"_DETAIL_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=tempMenuAlias+"_DETAIL_MONTH";
			saveMongo=tempMenuAlias+"_DETAIL_YEAR";
		}
				BasicDBObject bdObject = new BasicDBObject();
				
				BasicDBList dateList=new BasicDBList();
				BasicDBObject data1= new BasicDBObject();//查询开始时间
				BasicDBObject data2= new BasicDBObject();//查询结束时间
				
			    data1.append("workdate", new BasicDBObject("$gte",begin));
			    data2.append("workdate", new BasicDBObject("$lte",end));
			    dateList.add(data1);
			    dateList.add(data2);
			    bdObject.put("$and", dateList);
				DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double  total = (Double) dbCursor.get("total");//开立数量
					 Double  money=(Double)dbCursor.get("moneyTotal"); //金额
					 String  searchCode=(String)dbCursor.get(searchNo1);
					 String  searchName=(String)dbCursor.get(searchNo2);
			        temp1=temp+"&"+searchCode+"&"+searchName;
					 if(map.containsKey(temp1)){//如果key存在 比较name
						con=map.get(temp1);//获取数量
						con+=total;
						map.put(temp1, con);
						dou=map1.get(temp1);//获取金额
						dou+=money;
						map1.put(temp1, dou);
					 }else{//如果key不存在   添加到map1中
				    	map.put(temp1, total);
				    	map1.put(temp1, money);
				    }
				  }
				
			DBObject query = new BasicDBObject();
			query.put("workdate", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			String[] dateArr;
			List<DBObject> voList = new ArrayList<DBObject>();
			for(String key:map.keySet()){
			    dateArr=key.split("&");
			    BasicDBObject obj = new BasicDBObject();
			    obj.append("workdate", dateArr[0]);
			    obj.append(searchNo1, dateArr[1]);
			    obj.append(searchNo2, dateArr[2]);
			    obj.append("total", map.get(key));//开立数量
			    obj.append("moneyTotal", map1.get(key));//金额
			    voList.add(obj);
			}
			
		map=null;
		map1=null;
		new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
		
		this.saveMongoLog(beginDate, saveMongo, voList, date);
		
	}
	

	/**住院医生工作量汇总**/
	@Override
	public void init_ZYYSGZLTJ_Total(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
		Date beginDate=new Date();
		
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<WordLoadVO> list=new ArrayList<WordLoadVO>();
		List<String> tnL=this.returnInTables(begin, end, inpatientInfo, ZY);
		if(tnL!=null&&tnL.size()>0){
		StringBuffer buffer=new StringBuffer(150);
		buffer.append("select b.dat as date1,count(b.code) as con from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select t"+i+".house_doc_code as code,to_char(t"+i+".in_date,'yyyy-mm-dd') as dat  ");
			buffer.append("from "+tnL.get(i)+" t"+i+" ");
			buffer.append("where t"+i+".in_date between to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		}
		buffer.append(") b  group by b.dat ");
		list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
			@Override
			public WordLoadVO mapRow(ResultSet rs, int rownum)
					throws SQLException {
				WordLoadVO vo=new WordLoadVO();
				vo.setWorkDate(rs.getString("date1"));
				vo.setWorkTotal(rs.getInt("con"));
				return vo;
			}
		});
		DBObject query = new BasicDBObject();
		query.put("workdate", date);//移除数据条件
		new MongoBasicDao().remove(menuAlias+"_TOTAL_DAY", query);
		
		if(list!=null && list.size()>0){
			List<DBObject> voList = new ArrayList<DBObject>();
			for(WordLoadVO vo:list){
				if(vo.getWorkTotal()!=null){
					BasicDBObject obj = new BasicDBObject();
					obj.append("total", vo.getWorkTotal());
					obj.append("workdate", vo.getWorkDate());
					voList.add(obj);
				}
			}
			new MongoBasicDao().insertDataByList(menuAlias+"_TOTAL_DAY", voList);//添加新数据
			if(!"HIS".equals(type)){
			 init_ZYYSGZLTJ_Total_MoreDay(menuAlias,"2",date);
			 init_ZYYSGZLTJ_Total_MoreDay(menuAlias,"3",date);
			}
		}
		
		}
		this.saveMongoLog(beginDate, menuAlias+"_TOTAL_DAY", list, date);
		}
		
	}
	public void init_ZYYSGZLTJ_Total_MoreDay(String menuAlias, String type, String date){
		Date beginDate=new Date();
		
		Map<String,Integer> map=new HashMap<String,Integer>();
		String temp;//月数据
		Integer dou;
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		
		String temp1;//key
		if("2".equals(type)){
			temp=date.substring(0,7);
			//计算最后一个月日期
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_TOTAL_DAY";
			saveMongo=menuAlias+"_TOTAL_MONTH";
			end=returnEndTime(date);
		}else {
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_TOTAL_MONTH";
			saveMongo=menuAlias+"_TOTAL_YEAR";
		}
		BasicDBObject bdObject = new BasicDBObject();
		
		BasicDBList dateList=new BasicDBList();
		BasicDBObject data1= new BasicDBObject();//查询开始时间
		BasicDBObject data2= new BasicDBObject();//查询结束时间
		
	    data1.append("workdate", new BasicDBObject("$gte",begin));
	    data2.append("workdate", new BasicDBObject("$lte",end));
	    dateList.add(data1);
	    dateList.add(data2);
	    bdObject.put("$and", dateList);
			    
				DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer  value = (Integer) dbCursor.get("total") ;//费用
			        temp1=temp;
					 if(map.containsKey(temp1)){//如果key存在 比较name
						dou=map.get(temp1);
						dou+=value;
						map.put(temp1, dou);
					 }else{//如果key不存在   添加到map1中
				    	map.put(temp1, value);
				    }
				  }
				
			DBObject query = new BasicDBObject();
			query.put("workdate", temp);//移除数据条件
			new MongoBasicDao().remove(saveMongo, query);
			
			List<DBObject> voList = new ArrayList<DBObject>();
			for(String key:map.keySet()){
				 BasicDBObject obj = new BasicDBObject();
				 obj.append("total",map.get(key));
				 obj.append("workdate", key);
				 voList.add(obj);
			}
			map=null;
			new MongoBasicDao().insertDataByList(saveMongo, voList);
			
			this.saveMongoLog(beginDate, saveMongo, voList, date);
	}


	@Override
	public void init_ZYYSGZLTJ_Num(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
		Date beginDate=new Date();
		
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<String> tnL=this.returnInTables(begin, end, inpatientInfo,ZY);
		List<WordLoadVO> list=new ArrayList<WordLoadVO>();
		if(tnL!=null&&tnL.size()>0){
			StringBuffer buffer=new StringBuffer(150);
			buffer.append("select b.code as doctorCode,(select e.dept_name from t_department e where e.dept_code=b.deptcode ) AS deptName,b.deptcode as deptCode,b.name as doctorName,b.dat as date1,count(b.code) as con,sum(b.total) as moneyTotal from ( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select t"+i+".DEPT_CODE as deptcode,t"+i+".house_doc_code as code,t"+i+".house_doc_name as name,to_char(t"+i+".in_date,'yyyy-mm-dd') as dat,t"+i+".TOT_COST AS total ");
				buffer.append("from "+tnL.get(i)+" t"+i+" ");
				buffer.append("where t"+i+".in_date between to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			buffer.append(") b  group by b.code,b.name,b.dat,b.deptcode ");
			list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
				@Override
				public WordLoadVO mapRow(ResultSet rs, int rownum)
						throws SQLException {
					WordLoadVO vo=new WordLoadVO();
					vo.setDeptCode(rs.getString("deptCode"));
					vo.setDeptName(rs.getString("deptName"));
					vo.setDoctorCode(rs.getString("doctorCode"));
					vo.setDoctorName(rs.getString("doctorName"));
					vo.setWorkDate(rs.getString("date1"));
					vo.setWorkTotal(rs.getInt("con"));
					vo.setMoneyTotal(rs.getDouble("moneyTotal"));
					
					return vo;
				}
			});
			DBObject query = new BasicDBObject();
			query.put("workdate", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_NUM_DAY", query);
			
			if(list!=null && list.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
				
				for(WordLoadVO vo:list){
					if(vo.getWorkTotal()!=null&&vo.getMoneyTotal()!=null){
						 BasicDBObject obj = new BasicDBObject();
						 obj.append("deptCode", vo.getDeptCode());
						 obj.append("deptName", vo.getDeptName());
						 obj.append("doctorCode", vo.getDoctorCode());
						 obj.append("doctorName", vo.getDoctorName());
						 obj.append("total", vo.getWorkTotal());
						 obj.append("workdate", vo.getWorkDate());
						 obj.append("moneyTotal",vo.getMoneyTotal());
						 voList.add(obj);
						 
				           }
				     }
				new MongoBasicDao().insertDataByList(menuAlias+"_NUM_DAY", voList);
				if(!"HIS".equals(type)){
					init_ZYYSGZLTJ_Num_MoreDay(menuAlias,"2",date);
					init_ZYYSGZLTJ_Num_MoreDay(menuAlias,"3",date);
				}
				}
			}
		
		this.saveMongoLog(beginDate, menuAlias+"_NUM_DAY", list, date);
		
		}
	}
	public void init_ZYYSGZLTJ_Num_MoreDay(String menuAlias, String type, String date){
		Date beginDate=new Date();
		
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Integer> map=new HashMap<String,Integer>();//科室  数量
		Map<String,Double> map1=new HashMap<String,Double>();//科室  金额
		
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		
		String temp;//月数据
		Integer nu;//数量
		Double dou;//金额
		String temp1;//key
		if("2".equals(type)){
			temp=date.substring(0,7);
			//计算最后一个月日期
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_NUM_DAY";
			saveMongo=menuAlias+"_NUM_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_NUM_MONTH";
			saveMongo=menuAlias+"_NUM_YEAR";
		}
		
				BasicDBList dateList=new BasicDBList();
				BasicDBObject data1= new BasicDBObject();//查询开始时间
				BasicDBObject data2= new BasicDBObject();//查询结束时间
				
			    data1.append("workdate", new BasicDBObject("$gte",begin));
			    data2.append("workdate", new BasicDBObject("$lte",end));
			    dateList.add(data1);
			    dateList.add(data2);
			    bdObject.put("$and", dateList);
				DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("total") ;//总量
					 Double  money=(Double)dbCursor.get("moneyTotal") ;//金额
					 String deptCode =(String)dbCursor.get("deptCode");//科室代码
					 String  doctor=(String)dbCursor.get("doctorCode");//住院医生
					 String  doctorName=(String)dbCursor.get("doctorName");//住院医生name
			        temp1=temp+"&"+deptCode+"&"+doctor+"&"+doctorName; 
			    if(map.containsKey(temp1)){//如果key存在 比较name
						nu=map.get(temp1);
						nu+=value;
						dou=map1.get(temp1);
						dou+=money;
						map1.put(temp1, dou);
						map.put(temp1, nu);
						
				 }else{//如果key不存在   添加到map1中
					 map.put(temp1, value);
					 map1.put(temp1, money);
				    }
				   }
				
		DBObject query = new BasicDBObject();
		query.put("workdate", temp);//移除数据条件
		new MongoBasicDao().remove(saveMongo, query);
				
		List<DBObject> voList = new ArrayList<DBObject>();
		String[] strKey;
		for(String key:map.keySet()){
				BasicDBObject obj = new BasicDBObject();
				strKey=key.split("&");
				obj.append("deptCode",  strKey[1]);
				obj.append("doctorCode",  strKey[2]);
				obj.append("doctorName",  strKey[3]);
				obj.append("total",map.get(key));
				obj.append("workdate",  strKey[0]);
				obj.append("moneyTotal", map1.get(key));
				voList.add(obj);
			}
		map=null;
		
		map1=null;
		
		new MongoBasicDao().insertDataByList(saveMongo, voList);
		this.saveMongoLog(beginDate, saveMongo, voList, date);
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
	/**
	 * 保存日志
	 * @param date 开始时间
	 * @param menuAlias 模块名
	 * @param list 大小
	 * @param queryDate 在线更新时间
	 */
	public void saveMongoLog(Date date,String menuAlias,List list,String queryDate){
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(date);
		int len=queryDate.length();
		Date d;
		if(4==len){
			d=DateUtils.parseDateY(queryDate);
		}else if(7==len){
			d=DateUtils.parseDateY_M(queryDate);
		}else{
			d=DateUtils.parseDateY_M_D(queryDate);
		}
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias);
		mong.setCountEndTime(new Date());
		mong.setTotalNum(list.size());
		mong.setCreateTime(new Date());
		innerRegisterInfoGzltjDao.save(mong);//保存日志
		
	}
	
	@Override
	public void init_ZYSRQK_Dept(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();//开始计时
			
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=this.returnInTables(begin, end, inpatientMedi, ZY);
			List<String> mainL=this.returnInTables(begin, end, inpatientItem, ZY);
		if(tnL!=null||mainL!=null){
			 StringBuffer buffer=new StringBuffer(1500);
			 buffer.append("select f.num as num,f.douValue as douValue,(select e.dept_name from t_department e where e.dept_code=f.dept ) as dept from( ");
			 buffer.append("select count(1) as num,sum(c.douValue) as douValue,c.dept as dept  from( ");
			 buffer.append("select sum(ti.value) as douValue,ti.dept as dept,ti.recipeNo from ( ");
			 for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				 buffer.append("select t"+i+".tot_cost as value,t"+i+".recipe_deptcode as dept,t"+i+".RECIPE_NO recipeNo "); 
				 buffer.append("from "+tnL.get(i)+" t"+i+" ");
				 buffer.append(" where t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			 }
			 if(mainL!=null){
				 buffer.append("union all "); 
			 }
			 for(int i=0,len=mainL.size();i<len;i++){
				 if(i>0){
					 buffer.append("union all ");
				 }
				 buffer.append("select  n"+i+".tot_cost as value, n"+i+".recipe_deptcode as dept,n"+i+".RECIPE_NO recipeNo "); 
				 buffer.append("from "+mainL.get(i)+" n"+i+" where n"+i+".send_flag = 1 ");
				 buffer.append(" and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			 }
			 buffer.append(") ti  ");
			 buffer.append("group by ti.dept,ti.recipeNo ");
			 buffer.append(") c group by c.dept ");
			 buffer.append(") f ");
			 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
						.addScalar("douValue",Hibernate.DOUBLE).addScalar("dept").addScalar("num",Hibernate.INTEGER)
						.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
			
			DBObject query = new BasicDBObject();
			query.put("name", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_DEPT_DAY", query);//删除原来的数据	
			 if(list!=null && list.size()>0){
				 List<DBObject> userList = new ArrayList<DBObject>();
						for(Dashboard vo:list){
							if(vo.getNum()!=null&&vo.getDouValue()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("value", vo.getDouValue());
								obj.append("name", date);
								obj.append("dept", vo.getDept());
								obj.append("num", vo.getNum());
								userList.add(obj);
							}
							}
						new MongoBasicDao().insertDataByList(menuAlias+"_DEPT_DAY", userList);
						if(!"HIS".equals(type)){
							init_ZYSRQK_DeptOrDoc(menuAlias,"2",date,"DEPT");
							init_ZYSRQK_DeptOrDoc(menuAlias,"3",date,"DEPT");
						}
					}
			 this.saveMongoLog(beginDate, menuAlias, list, date);
					}
		}
		
	}
	@Override
	public void init_ZYSRQK_DeptOrDoc(String menuAlias, String type, String date,String queryType){
		Date beginDate=new Date();
		
		String temp;//临时时间
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		Double dou;//金额
		Integer con;//数量
		String temp1;//map的key
		Map<String,Integer> map=new HashMap<String,Integer>();//存储开立数量
		Map<String,Double> map1=new HashMap<String,Double>();//存储金额
		
		String searchNo1;//查询的字段
		String tempMenuAlias=menuAlias;
		if("DOC".equals(queryType)){
			tempMenuAlias=menuAlias+"_DOC";
			searchNo1="doctor";
		}else{
			tempMenuAlias=menuAlias+"_DEPT";
			searchNo1="dept";
		}
		
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			queryMongo=tempMenuAlias+"_DETAIL_DAY";
			saveMongo=tempMenuAlias+"_MONTH";
			end=returnEndTime(date);
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=tempMenuAlias+"_MONTH";
			saveMongo=tempMenuAlias+"_YEAR";
		}
				BasicDBObject bdObject = new BasicDBObject();
				
				BasicDBList dateList=new BasicDBList();
				BasicDBObject data1= new BasicDBObject();//查询开始时间
				BasicDBObject data2= new BasicDBObject();//查询结束时间
				
			    data1.append("name", new BasicDBObject("$gte",begin));
			    data2.append("name", new BasicDBObject("$lte",end));
			    dateList.add(data1);
			    dateList.add(data2);
			    bdObject.put("$and", dateList);
				DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double  total = (Double) dbCursor.get("value");//金额
					 Integer  num=(Integer)dbCursor.get("num"); //数量
					 String  searchCode=(String)dbCursor.get(searchNo1);//查询字段
					 temp1=temp+"&"+searchCode;
					 if(map.containsKey(temp1)){//如果key存在 比较name
						con=map.get(temp1);//获取数量
						con+=num;
						map.put(temp1, con);
						dou=map1.get(temp1);//获取金额
						dou+=total;
						map1.put(temp1, dou);
					 }else{//如果key不存在   添加到map1中
				    	map.put(temp1, num);
				    	map1.put(temp1, total);
				    }
				  }
				
			DBObject query = new BasicDBObject();
			query.put("name", temp);//移除数据条件	
			new MongoBasicDao().remove(saveMongo, query);
			
			String[] dateArr;
			List<DBObject> voList = new ArrayList<DBObject>();
			for(String key:map.keySet()){
			    dateArr=key.split("&");
			    BasicDBObject obj = new BasicDBObject();
			    obj.append("name", dateArr[0]);
			    obj.append(searchNo1, dateArr[1]);
			    obj.append("num", map.get(key));//开立数量
			    obj.append("value", map1.get(key));//金额
			    voList.add(obj);
			}
			
		map=null;
		map1=null;
		new MongoBasicDao().insertDataByList(saveMongo, voList);//添加新数据
		this.saveMongoLog(beginDate, menuAlias, voList, date);
	
	}


	@Override
	public void init_ZYSRQK_Doc(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();//开始计时
			
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=this.returnInTables(begin, end, inpatientMedi, ZY);
			List<String> mainL=this.returnInTables(begin, end, inpatientItem, ZY);
		if(tnL!=null||mainL!=null){
			StringBuffer buffer=new StringBuffer(1500);
			 buffer.append("select f.con as num,f.douValue as douValue,(select e.employee_name from t_employee e where e.employee_code=f.doctor) as doctor from( ");
			 buffer.append("select count(1) as con,sum(c.douValue) as douValue,c.doctor as doctor  from( ");
			 buffer.append("select sum(ti.value) as douValue,ti.doc as doctor,ti.recipeNo from ( ");
			 for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				 buffer.append("select t"+i+".tot_cost as value,t"+i+".RECIPE_DOCCODE as doc,t"+i+".RECIPE_NO recipeNo "); 
				 buffer.append("from "+tnL.get(i)+" t"+i+" ");
				 buffer.append(" where t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			 }
			 if(mainL!=null){
				 buffer.append("union all "); 
			 }
			 for(int i=0,len=mainL.size();i<len;i++){
				 if(i>0){
					 buffer.append("union all ");
				 }
				 buffer.append("select n"+i+".tot_cost as value, n"+i+".RECIPE_DOCCODE as doc,n"+i+".RECIPE_NO recipeNo "); 
				 buffer.append("from "+mainL.get(i)+" n"+i+" where n"+i+".send_flag = 1 ");
				 buffer.append(" and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			 }
			 buffer.append(") ti  ");
			 buffer.append("group by ti.doc,ti.recipeNo ");
			 buffer.append(") c group by c.doctor ");
			 buffer.append(") f ");
			 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
						.addScalar("douValue",Hibernate.DOUBLE).addScalar("doctor").addScalar("num",Hibernate.INTEGER)
						.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
			 	
			 	DBObject query = new BasicDBObject();
				query.put("name", date);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_DOC_DAY", query);//删除原来的数据	
				 if(list!=null && list.size()>0){
					 List<DBObject> userList = new ArrayList<DBObject>();
							for(Dashboard vo:list){
								if(vo.getNum()!=null&&vo.getDouValue()!=null){
									BasicDBObject obj = new BasicDBObject();
									obj.append("value", vo.getDouValue());
									obj.append("name", date);
									obj.append("doctor", vo.getDoctor());
									obj.append("num", vo.getNum());
									userList.add(obj);
									}
								}
							new MongoBasicDao().insertDataByList(menuAlias+"_DOC_DAY", userList);
							if(!"HIS".equals(type)){
								init_ZYSRQK_DeptOrDoc(menuAlias,"2",date,"DOC");
								init_ZYSRQK_DeptOrDoc(menuAlias,"3",date,"DOC");
							}
							
						}
				 this.saveMongoLog(beginDate, menuAlias, list, date);
			}
		}
		
	}

	@Override
	public void init_ZYSRQK_MzZy(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();//开始计时
			
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=this.returnInTables(begin, end, inpatientFee, MZ);
			List<String> maintnl=this.returnInTables(begin, end, outFee, ZY);
			List<Dashboard> list=null;
			if(tnL!=null||maintnl!=null){
				StringBuffer buffer=new StringBuffer(2000);
				buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS douValue,b.sTime1 AS date1,b.classType AS classType from(");
				for(int i=0,len=maintnl.size();i<len;i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,cast('MZ' as varchar(2)) AS classType,");
					buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
					buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
					buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
					buffer.append("and t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
					buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
				}
				if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
					buffer.append(" union All ");
				}
				for(int i=0,len=tnL.size();i<len;i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS douValue,cast('ZY' as varchar2(2)) as classType,");
					buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
					buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
					buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
					buffer.append("and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
					buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
				}
				buffer.append(") b group by b.cost1,b.name,b.sTime1,b.classType");
				list=super.getSession().createSQLQuery(buffer.toString())
													.addScalar("douValue",Hibernate.DOUBLE)
													.addScalar("name")
													.addScalar("code")
													.addScalar("date1")
													.addScalar("classType")
													.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
				DBObject query = new BasicDBObject();
				query.put("date", date);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_MZZY_DAY", query);//删除原来的数据	
				
				if(list!=null && list.size()>0){
				 List<DBObject> userList = new ArrayList<DBObject>();
				for(Dashboard vo:list){
					if(null!=vo.getDouValue()){
						BasicDBObject bdObject1 = new BasicDBObject();
						bdObject1.append("value", vo.getDouValue());//金额
						bdObject1.append("name", vo.getName());//统计大类
						bdObject1.append("date", vo.getDate1());//日期
						bdObject1.append("classType", vo.getClassType());//分类MZ门诊 ZY住院
						userList.add(bdObject1);
					}
					
							}
				new MongoBasicDao().insertDataByList(menuAlias+"_MZZY_DAY", userList);
				if(!"HIS".equals(type)){
					init_ZYSRQK_MzZy_MoreDay(menuAlias,"2",date);
					init_ZYSRQK_MzZy_MoreDay(menuAlias,"3",date);
				}
						}
				}
			this.saveMongoLog(beginDate, menuAlias, list, date);
		}
		
	}
	@Override
	public void init_ZYSRQK_MzZy_MoreDay(String menuAlias, String type, String date){
		Date beginDate=new Date();
		Map<String,Double> map=new HashMap<String,Double>();
		String temp;//月数据
		Double dou;
		
		String temp1;//key
		String begin=null;//开始时间
		String end=null;//结束时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		
		if("2".equals(type)){
			temp=date.substring(0,7);
			//计算最后一个月日期
			begin=temp+"-01";//开始时间
			queryMongo=menuAlias+"_MZZY_DAY";
			saveMongo=menuAlias+"_MZZY_MONTH";
			end=returnEndTime(date);
		}else {
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_MZZY_MONTH";
			saveMongo=menuAlias+"_MZZY_YEAR";
		}
				BasicDBObject bdObject = new BasicDBObject();
				
				BasicDBList dateList=new BasicDBList();
				BasicDBObject data1= new BasicDBObject();//查询开始时间
				BasicDBObject data2= new BasicDBObject();//查询结束时间
				
			    data1.append("date", new BasicDBObject("$gte",begin));
			    data2.append("date", new BasicDBObject("$lte",end));
			    dateList.add(data1);
			    dateList.add(data2);
			    bdObject.put("$and", dateList);
				DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Double value = (Double) dbCursor.get("value") ;//费用
					 String name =(String) dbCursor.get("name");//费用类别
					 String classType=(String)dbCursor.get("classType");//门诊住院
			        temp1=temp+"&"+name+"&"+classType;
					 if(map.containsKey(temp1)){//如果key存在 比较name
						dou=map.get(temp1);
						dou+=value;
						map.put(temp1, dou);
				    
					 }else{//如果key不存在   添加到map1中
				    	map.put(temp1, value);
				    }
				  }
				DBObject query = new BasicDBObject();
				query.put("date", temp);//移除数据条件
				new MongoBasicDao().remove(saveMongo, query);//删除原来的数据	
		List<DBObject> userList = new ArrayList<DBObject>();	
		String[] strKey=null;
		for(String key:map.keySet()){
			BasicDBObject bdObject1 = new BasicDBObject();
			  strKey=key.split("&");
			  bdObject1.append("value",map.get(key));
			  bdObject1.append("name", strKey[1]);
			  bdObject1.append("classType", strKey[2]);
			  bdObject1.append("date", strKey[0]);
			  userList.add(bdObject1);
			}
		new MongoBasicDao().insertDataByList(saveMongo, userList);
		this.saveMongoLog(beginDate, menuAlias, userList, date);
		
	}


	@Override
	public void init_ZYYSGZLTJ_effect(String menuAlias, String type, String date) {
		   Date beginDate=new Date();
		   String begin=date+" 00:00:00";
		   String end=date+" 23:59:59";
		   List<String> tnL=this.returnInTables(begin, end, inpatientInfo, ZY);
		   if(tnL!=null&&tnL.size()>0){
				StringBuffer buffer=new StringBuffer(2000);
				buffer.append("select t.deptCode deptcode,t.doctor as doctor,nvl(t.hosVisitors,0) as hosVisitors,count(t.inpatient_no) as outVisitors, ");
				buffer.append("count(concurVisitors) concurVisitors, count(cureVisitors) cureVisitors,count(unCureVisitors) unCureVisitors,");
				buffer.append("count(betterVisitors) betterVisitors,count(deathVisitors) deathVisitors,sum(t.averageInhost) averageInhost,t.inDate as inDate from( ");
				buffer.append("select t.dept_code as deptCode, ");//科室
				buffer.append("t.HOUSE_DOC_CODE as doctor,");//--医生
				buffer.append("t.inpatient_no as inpatient_no,i.coun as hosVisitors,");//出院
				buffer.append("t.out_date-t.in_date as averageInhost,");//住院天数
				buffer.append("to_char(t.in_date,'yyyy-mm-dd') inDate ");
				buffer.append("from ( ");
				for(int i=0,len=tnL.size();i<len;i++){
					if(i>0){
						buffer.append(" union all ");
					}
					buffer.append("select dept_code,HOUSE_DOC_CODE,inpatient_no,in_state,out_date,in_date ");
					buffer.append(" from "+tnL.get(i)+" ");
					buffer.append("where out_DATE>=to_date('").append(begin).append("','yyyy-mm-dd HH24:mi:ss') ")
					.append("and out_DATE<=to_date('").append(end).append("','yyyy-mm-dd HH24:mi:ss') ");
				}
				buffer.append(" ) t left join (select sum(t1.coun) coun ,t1.HOUSE_DOC_CODE HOUSE_DOC_CODE,t1.dept_code dept_code from ( ");
				for(int i=0,len=tnL.size();i<len;i++){
					if(i>0){
						buffer.append(" union all ");
					}
					buffer.append("select count(1) as coun,dept_code,HOUSE_DOC_CODE  from "+tnL.get(i)+"  ");
					buffer.append("where IN_DATE>=to_date('").append(begin).append("','yyyy-mm-dd HH24:mi:ss') ")
					.append("and IN_DATE<=to_date('").append(end).append("','yyyy-mm-dd HH24:mi:ss') ");
					
					buffer.append("group by dept_code,HOUSE_DOC_CODE ");
				}
				
				buffer.append(" ) t1  group by t1.HOUSE_DOC_CODE,t1.dept_code ) i on i.dept_code=t.dept_code and i.HOUSE_DOC_CODE=t.HOUSE_DOC_CODE where t.in_state in('O') )t ");
				buffer.append("left join (select count(1) concurVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_KIND = 3 group by b.inpatient_no ) b on t.inpatient_no =  b.inpatient_no ");
				buffer.append("left join (select count(1) cureVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=0 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
				buffer.append("left join (select count(1) betterVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=1 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
				buffer.append("left join (select count(1) unCureVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=2 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
				buffer.append("left join (select count(1) deathVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=3 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
				buffer.append("group by t.deptCode, t.doctor,t.hosVisitors,t.inDate ");
				
				List<HospitalWork> list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap<String,String>(),new RowMapper<HospitalWork>() {
					@Override
					public HospitalWork mapRow(ResultSet rs, int rownum)
							throws SQLException {
						HospitalWork vo=new HospitalWork();
						vo.setDeptCode(rs.getString("deptCode"));
						vo.setDoctor(rs.getString("doctor"));
						vo.setHosVisitors(rs.getInt("hosVisitors"));
						vo.setOutVisitors(rs.getInt("outVisitors"));
						vo.setConcurVisitors(rs.getInt("concurVisitors"));
						vo.setCureVisitors(rs.getInt("cureVisitors"));
						vo.setUnCureVisitors(rs.getInt("unCureVisitors"));
						vo.setBetterVisitors(rs.getInt("betterVisitors"));
						vo.setDeathVisitors(rs.getInt("deathVisitors"));
						vo.setAverageInhost(rs.getInt("averageInhost"));
						vo.setInDate(rs.getString("inDate"));
						return vo;
					}
				});
				DBObject query = new BasicDBObject();
				query.put("inDate", date);//移除数据条件
				new MongoBasicDao().remove(menuAlias+"_EFFECT_DAY", query);//删除原来的数据	
				
				if(list!=null && list.size()>0){
				List<DBObject> userList = new ArrayList<DBObject>();
				for(HospitalWork vo:list){
					if( null != vo.getDeptCode() && null!=vo.getDoctor() ){
						BasicDBObject bdObject1 = new BasicDBObject();
						bdObject1.append("deptCode", vo.getDeptCode());//科室
						bdObject1.append("doctor", vo.getDoctor());//医生
						bdObject1.append("hosVisitors", vo.getHosVisitors());//住院人数
						bdObject1.append("outVisitors", vo.getOutVisitors());//出院人数
						bdObject1.append("concurVisitors", vo.getConcurVisitors());//并发症人数
						bdObject1.append("cureVisitors", vo.getCureVisitors());//治愈人数
						bdObject1.append("unCureVisitors", vo.getUnCureVisitors());//未治愈人数
						bdObject1.append("betterVisitors", vo.getBetterVisitors());//好转人数
						bdObject1.append("deathVisitors", vo.getDeathVisitors());//死亡人数
						bdObject1.append("averageInhost", vo.getAverageInhost());//住院天数
						bdObject1.append("inDate", date);//时间
						userList.add(bdObject1);
					}
					
				}
				list=null;
				new MongoBasicDao().insertDataByList(menuAlias+"_EFFECT_DAY", userList);
				this.saveMongoLog(beginDate, menuAlias, userList, date);
		   }
				
		  }
	}


	@Override
	public String returnColumn(String column) {
            return column.replaceAll(".*([';]+|(--)+).*", "");
	}


	@Override
	public void init_ZYSRQK_Deatail(String menuAlias, String type, String date) {
		if(StringUtils.isNotBlank(date)){
			Date beginDate=new Date();//开始计时
			
			String begin=date+" 00:00:00";//开始时间
			String end=date+" 23:59:59";//结束时间
			List<String> tnL=this.returnInTables(begin, end, inpatientMedi, ZY);
			List<String> mainL=this.returnInTables(begin, end, inpatientItem, ZY);
		if(tnL!=null||mainL!=null){
			 StringBuffer buffer=new StringBuffer(1500);
			 buffer.append("select f.num as num,f.douValue as douValue,f.dept deptCode,f.doctor docCode,(select e.dept_name from t_department e where e.dept_code=f.dept ) as dept,"
			 		+ "(select e.employee_name from t_employee e where e.employee_code=f.doctor) as doctor from( ");
			 buffer.append("select count(1) as num,sum(c.douValue) as douValue,c.dept as dept,c.doctor as doctor  from( ");
			 buffer.append("select sum(ti.value) as douValue,ti.dept as dept,ti.doctor  as doctor,ti.recipeNo from ( ");
			 for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				 buffer.append("select t"+i+".tot_cost as value,t"+i+".recipe_deptcode as dept,t"+i+".RECIPE_DOCCODE as doctor,t"+i+".RECIPE_NO recipeNo "); 
				 buffer.append("from "+tnL.get(i)+" t"+i+" ");
				 buffer.append(" where t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			 }
			 if(mainL!=null){
				 buffer.append("union all "); 
			 }
			 for(int i=0,len=mainL.size();i<len;i++){
				 if(i>0){
					 buffer.append("union all ");
				 }
				 buffer.append("select  n"+i+".tot_cost as value, n"+i+".recipe_deptcode as dept,n"+i+".RECIPE_DOCCODE as doctor,n"+i+".RECIPE_NO recipeNo "); 
				 buffer.append("from "+mainL.get(i)+" n"+i+" where n"+i+".send_flag = 1 ");
				 buffer.append(" and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			 }
			 buffer.append(") ti  ");
			 buffer.append("group by ti.dept,ti.recipeNo,ti.doctor ");
			 buffer.append(") c group by c.dept,c.doctor ");
			 buffer.append(") f ");
			 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
						.addScalar("douValue",Hibernate.DOUBLE).addScalar("dept").addScalar("num",Hibernate.INTEGER)
						.addScalar("deptCode").addScalar("docCode").addScalar("doctor")
						.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
			
			DBObject query = new BasicDBObject();
			query.put("name", date);//移除数据条件
			new MongoBasicDao().remove(menuAlias+"_DETAIL_DAY", query);//删除原来的数据	
			 if(list!=null && list.size()>0){
				 List<DBObject> userList = new ArrayList<DBObject>();
						for(Dashboard vo:list){
							if(vo.getNum()!=null&&vo.getDouValue()!=null){
								BasicDBObject obj = new BasicDBObject();
								obj.append("value", vo.getDouValue());
								obj.append("name", date);
								obj.append("dept", vo.getDept());
								obj.append("num", vo.getNum());
								obj.append("deptCode", vo.getDeptCode());
								obj.append("doctor", vo.getDoctor());
								obj.append("docCode", vo.getDocCode());
								userList.add(obj);
							}
						}
						new MongoBasicDao().insertDataByList(menuAlias+"_DETAIL_DAY", userList);
						if(!"HIS".equals(type)){
							init_ZYSRQK_DeptOrDoc(menuAlias,"2",date,"DEPT");
							init_ZYSRQK_DeptOrDoc(menuAlias,"3",date,"DEPT");
						}
					}
			 this.saveMongoLog(beginDate, menuAlias, list, date);
			}
		}
		
	}


	@Override
	public void pCDeptWorktotal(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		String begin=date+" 00:00:00";
		String end=date+" 23:59:59";
		String mongodbCollection="ZYYSGZL_PC_DEPT_DAY";
		List<String> tnL=this.returnInTables(begin, end, inpatientMedi, ZY);
		List<String> mainL=this.returnInTables(begin, end, inpatientItem, ZY);
		
		StringBuffer buffer=new StringBuffer();
		buffer.append("select r.deptCode deptCode,");
		buffer.append("(select t.dept_name from t_department t where t.dept_code=r.deptCode and t.del_flg=0 and t.stop_flg=0) deptName,");
		buffer.append("nvl(r.cfs, 0) cfs,nvl(r.ryrs, 0) ryrs,nvl(r.cyrs, 0) cyrs,");
		buffer.append("nvl(r.avgindate , 0) avgindate,");
		buffer.append("nvl(r.operationNum, 0) operationNum,nvl(r.criticallyNum, 0) criticallyNum,nvl(r.deadNum, 0) deadNum ");
		
		buffer.append("from (select r.deptCode deptCode, sum(nvl(r.cfs, 0)) cfs,sum(nvl(r.ryrs, 0)) ryrs,sum(nvl(r.cyrs, 0)) cyrs,");
		buffer.append("sum(nvl(r.indate, 0)) avgindate,sum(nvl(r.operationNum, 0)) operationNum,sum(nvl(r.criticallyNum, 0)) criticallyNum,");
		buffer.append("sum(nvl(r.deadNum, 0)) deadNum ");
		
		buffer.append("from (select count(1) ryrs,null as cfs,null as cyrs,null as indate,null as operationNum,null as criticallyNum,null as deadNum,t.dept_code deptCode ");
		buffer.append("from t_inpatient_info  t ");
		buffer.append("where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.in_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by t.dept_code ");
		buffer.append("union all ");
		buffer.append("select  count(1) ryrs,null as cfs,null as cyrs,null as indate,null as operationNum,null as criticallyNum,null as deadNum,t.dept_code deptCode ");
		buffer.append("from t_inpatient_info_now  t ");
		buffer.append("where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.in_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by t.dept_code  ");
		
		buffer.append("union all ");
		
		buffer.append(" select null as ryrs, null as cfs,sum(c.cyrs) cyrs,sum(c.indate) indate,null as operationNum,null as criticallyNum,null as deadNum,c.deptCode from ( ");
		buffer.append("select count(1) cyrs,t.dept_code deptCode,(t.out_date - t.in_date) indate ");
		buffer.append(" from t_inpatient_info  t ");
		buffer.append("where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date is not null ");
		buffer.append("group by t.dept_code, (t.out_date - t.in_date) ");
		buffer.append("union all ");
		buffer.append("select count(1) cyrs,t.dept_code deptCode,(t.out_date - t.in_date) indate ");
		buffer.append(" from t_inpatient_info_now  t ");
		buffer.append("where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date is not null ");
		buffer.append("and t.in_state='O' ");
		buffer.append("group by t.dept_code, (t.out_date - t.in_date) )c group by c.deptCode ");
		
		buffer.append("union all ");
		buffer.append("select null as ryrs,count(1) as cfs,null cyrs,null indate,null as operationNum,null as criticallyNum,null as deadNum,c.dept as deptCode ");
		 buffer.append("from( ");
		 buffer.append("select ti.dept as dept,ti.recipeNo from ( ");
		 for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			 buffer.append("select t"+i+".recipe_deptcode as dept,t"+i+".RECIPE_NO recipeNo "); 
			 buffer.append("from "+tnL.get(i)+" t"+i+" ");
			 buffer.append(" where t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		 }
		 if(mainL!=null){
			 buffer.append("union all "); 
		 }
		 for(int i=0,len=mainL.size();i<len;i++){
			 if(i>0){
				 buffer.append("union all ");
			 }
			 buffer.append("select  n"+i+".recipe_deptcode as dept,n"+i+".RECIPE_NO recipeNo "); 
			 buffer.append("from "+mainL.get(i)+" n"+i+" where n"+i+".send_flag = 1 ");
			 buffer.append(" and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		 }
		 buffer.append(") ti  ");
		 buffer.append("group by ti.dept,ti.recipeNo ) c ");
		 buffer.append(" group by c.dept ");
		 
		buffer.append("union all ");
		buffer.append("select null as ryrs, null as cfs,null cyrs,null indate,count(1) operationNum,null as criticallyNum,null as deadNum,t.DEPT_CODE deptCode ");
		buffer.append("from t_operation_record t ");
		buffer.append("where t.CREATETIME <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.CREATETIME >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.YNVALID = 1 and t.del_flg=0 and t.stop_flg=0 group by t.DEPT_CODE ");
		
		buffer.append("union all ");
		
		buffer.append("select null as ryrs,null as cfs, null cyrs,null indate,null operationNum,count(t.zg) criticallyNum,sum(DECODE(t.zg, '4', 1, 0)) deadNum,t.DEPT_CODE deptCode ");
		buffer.append("from t_emr_base t ");
		buffer.append("where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by t.dept_code) r group by r.deptCode ) r ");
		List<PcWorkTotal> list=this.getSession().createSQLQuery(buffer.toString()).addScalar("deptCode")
				.addScalar("deptName").addScalar("cfs",Hibernate.INTEGER).addScalar("ryrs",Hibernate.INTEGER).addScalar("cyrs",Hibernate.INTEGER).addScalar("operationNum",Hibernate.INTEGER).addScalar("criticallyNum",Hibernate.INTEGER)
				.addScalar("deadNum",Hibernate.INTEGER).addScalar("avgindate",Hibernate.DOUBLE).setResultTransformer(Transformers.aliasToBean(PcWorkTotal.class)).list();
		
		DBObject query = new BasicDBObject();
		query.put("date", date);//移除数据条件
		new MongoBasicDao().remove(mongodbCollection, query);//删除原来的数据
		if(list.size()>0){
			 List<DBObject> userList = new ArrayList<DBObject>();
				for(PcWorkTotal vo:list){
						BasicDBObject obj = new BasicDBObject();
						obj.append("date", date);
						obj.append("deptName", vo.getDeptName());
						obj.append("deptCode", vo.getDeptCode());
						obj.append("cfs", vo.getCfs());
						obj.append("ryrs", vo.getRyrs());
						obj.append("cyrs", vo.getCyrs());
						obj.append("avgindate", vo.getAvgindate());
						obj.append("operationNum", vo.getOperationNum());
						obj.append("criticallyNum", vo.getCriticallyNum());
						obj.append("deadNum", vo.getDeadNum());
						userList.add(obj);
				}
			new MongoBasicDao().insertDataByList(mongodbCollection, userList);
			}
		this.saveMongoLog(beginDate, menuAlias, list, date);	
	
	}


	@Override
	public void pcDeptWorkTotalMonthAndYear(String menuAlias, String type,
			String date) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		String begin=null;
		String end=null;
		String savemongoCollection=null;
		String querymongoCollection=null;
		if("2".equals(type)){
			begin=date+"-01";
			end=returnEndTime(begin);
			querymongoCollection="ZYYSGZL_PC_DEPT_DAY";
			savemongoCollection="ZYYSGZL_PC_DEPT_MONTH";
		}else{
			begin=date.substring(0,4)+"-01";
			end=date.substring(0,4)+"-12";
			querymongoCollection="ZYYSGZL_PC_DEPT_MONTH";
			savemongoCollection="ZYYSGZL_PC_DEPT_YEAR";
		}
		bdObjectTimeS.put("date",new BasicDBObject("$gte",begin));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("date",new BasicDBObject("$lte",end));
		condList.add(bdObjectTimeE);
		bdbObject.put("$and", condList);
		DBObject _group = new BasicDBObject("deptCode", "$deptCode").append("deptName", "$deptName");  
		DBObject groupFields = new BasicDBObject("_id", _group);
		groupFields.put("cfs", new BasicDBObject("$sum","$cfs")); 
		groupFields.put("ryrs", new BasicDBObject("$sum","$ryrs")); 
		groupFields.put("cyrs", new BasicDBObject("$sum","$cyrs")); 
		groupFields.put("avgindate", new BasicDBObject("$sum","$avgindate")); 
		groupFields.put("operationNum", new BasicDBObject("$sum","$operationNum")); 
		groupFields.put("criticallyNum", new BasicDBObject("$sum","$criticallyNum")); 
		groupFields.put("deadNum", new BasicDBObject("$sum","$deadNum")); 
		
		DBObject match = new BasicDBObject("$match", bdbObject);
		DBObject group = new BasicDBObject("$group", groupFields);
		AggregationOutput output = new MongoBasicDao().findGroupBy(querymongoCollection, match, group);
		Iterator<DBObject> it = output.results().iterator();
		List<PcWorkTotal> list=new ArrayList<PcWorkTotal>();
		while(it.hasNext()){
			PcWorkTotal vo=new PcWorkTotal();
			BasicDBObject dbo = ( BasicDBObject ) it.next();
			BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
			vo.setDeptName((String)keyValus.get("deptName"));
			vo.setDeptCode((String)keyValus.get("deptCode"));
			
			vo.setCfs(dbo.getInt("cfs"));
			vo.setRyrs(dbo.getInt("ryrs"));
			vo.setCyrs(dbo.getInt("cyrs"));
			vo.setAvgindate(dbo.getDouble("avgindate"));
			vo.setOperationNum(dbo.getInt("operationNum"));
			
			vo.setCriticallyNum(dbo.getInt("criticallyNum"));
			vo.setDeadNum(dbo.getInt("deadNum"));
			list.add(vo);
		}
		BasicDBObject removeDate=new BasicDBObject();
		removeDate.append("date", date);//删除数据
		new MongoBasicDao().remove(savemongoCollection, removeDate);
		List<DBObject> voList = new ArrayList<DBObject>();
		if(list.size()>0){
			for(PcWorkTotal vo:list){
				BasicDBObject obj = new BasicDBObject();
				obj.append("date", date);
				obj.append("deptName", vo.getDeptName());
				obj.append("deptCode", vo.getDeptCode());
				obj.append("cfs", vo.getCfs());
				obj.append("ryrs", vo.getRyrs());
				obj.append("cyrs", vo.getCyrs());
				obj.append("avgindate", vo.getAvgindate());
				obj.append("operationNum", vo.getOperationNum());
				obj.append("criticallyNum", vo.getCriticallyNum());
				obj.append("deadNum", vo.getDeadNum());
	    		voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(savemongoCollection, voList);//添加新数据
		
		}
	}


	@Override
	public void pcDoctorWorkTotalMonthAndYear(String menuAlias, String type,
			String date) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		String begin=null;
		String end=null;
		String savemongoCollection=null;
		String querymongoCollection=null;
		if("2".equals(type)){
			begin=date+"-01";
			end=returnEndTime(begin);
			querymongoCollection="ZYYSGZL_PC_DOCTOR_DAY";
			savemongoCollection="ZYYSGZL_PC_DOCTOR_MONTH";
		}else{
			begin=date.substring(0,4)+"-01";
			end=date.substring(0,4)+"-12";
			querymongoCollection="ZYYSGZL_PC_DOCTOR_MONTH";
			savemongoCollection="ZYYSGZL_PC_DOCTOR_YEAR";
		}
		bdObjectTimeS.put("date",new BasicDBObject("$gte",begin));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("date",new BasicDBObject("$lte",end));
		condList.add(bdObjectTimeE);
		bdbObject.put("$and", condList);
		
		DBObject _group = new BasicDBObject("deptCode", "$deptCode").append("docCode", "$docCode").append("docName", "$docName").append("deptName", "$deptName");  
		DBObject groupFields = new BasicDBObject("_id", _group);
		groupFields.put("cfs", new BasicDBObject("$sum","$cfs")); 
		groupFields.put("ryrs", new BasicDBObject("$sum","$ryrs")); 
		groupFields.put("cyrs", new BasicDBObject("$sum","$cyrs")); 
		groupFields.put("operationNum", new BasicDBObject("$sum","$operationNum")); 
		groupFields.put("avgindate", new BasicDBObject("$sum","$avgindate")); 
		groupFields.put("westernCost", new BasicDBObject("$sum","$westernCost")); 
		groupFields.put("chineseCost", new BasicDBObject("$sum","$chineseCost")); 
		groupFields.put("herbalCost", new BasicDBObject("$sum","$herbalCost")); 
		groupFields.put("allCost", new BasicDBObject("$sum","$allCost")); 
		groupFields.put("chuangweiCost", new BasicDBObject("$sum","$chuangweiCost")); 
		groupFields.put("treatmentCost", new BasicDBObject("$sum","$treatmentCost")); 
		groupFields.put("inspectCost", new BasicDBObject("$sum","$inspectCost")); 
		groupFields.put("radiationCost", new BasicDBObject("$sum","$radiationCost")); 
		groupFields.put("testCost", new BasicDBObject("$sum","$testCost")); 
		groupFields.put("shoushuCost", new BasicDBObject("$sum","$shoushuCost")); 
		groupFields.put("bloodCost", new BasicDBObject("$sum","$bloodCost")); 
		groupFields.put("o2Cost", new BasicDBObject("$sum","$o2Cost")); 
		groupFields.put("cailiaoCost", new BasicDBObject("$sum","$cailiaoCost")); 
		groupFields.put("yimiaoCost", new BasicDBObject("$sum","$yimiaoCost")); 
		groupFields.put("otherCost", new BasicDBObject("$sum","$otherCost")); 
		groupFields.put("medicalCost", new BasicDBObject("$sum","$medicalCost")); 
		groupFields.put("totle", new BasicDBObject("$sum","$totle"));
		groupFields.put("huliCost", new BasicDBObject("$sum","$huliCost"));
		groupFields.put("zhenchaCost", new BasicDBObject("$sum","$zhenchaCost"));
		
		DBObject match = new BasicDBObject("$match", bdbObject);
		DBObject group = new BasicDBObject("$group", groupFields);
		AggregationOutput output = new MongoBasicDao().findGroupBy(querymongoCollection, match, group);
		Iterator<DBObject> it = output.results().iterator();
		List<PcWorkTotal> list=new ArrayList<PcWorkTotal>();
		while(it.hasNext()){
			PcWorkTotal vo=new PcWorkTotal();
			BasicDBObject dbo = ( BasicDBObject ) it.next();
			BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
			vo.setDeptName((String)keyValus.get("deptName"));
			vo.setDoctorName((String)keyValus.get("docName"));
			vo.setDoctorCode((String)keyValus.get("docCode"));
			vo.setDeptCode((String)keyValus.get("deptCode"));
			
			vo.setAllCost(dbo.getDouble("allCost"));
			vo.setRyrs(dbo.getInt("ryrs"));
			vo.setCyrs(dbo.getInt("cyrs"));
			vo.setOperationNum(dbo.getInt("operationNum"));
			vo.setAvgindate(dbo.getDouble("avgindate"));
			vo.setCfs(dbo.getInt("cfs"));
			
			vo.setWesternCost(dbo.getDouble("westernCost"));
			vo.setChineseCost(dbo.getDouble("chineseCost"));
			vo.setHerbalCost(dbo.getDouble("herbalCost"));
			vo.setChuangweiCost(dbo.getDouble("chuangweiCost"));
			vo.setTreatmentCost(dbo.getDouble("treatmentCost"));
			vo.setInspectCost(dbo.getDouble("inspectCost"));
			vo.setRadiationCost(dbo.getDouble("radiationCost"));
			vo.setTestCost(dbo.getDouble("testCost"));
			vo.setShoushuCost(dbo.getDouble("shoushuCost"));
			vo.setBloodCost(dbo.getDouble("bloodCost"));
			vo.setO2Cost(dbo.getDouble("o2Cost"));
			vo.setCailiaoCost(dbo.getDouble("cailiaoCost"));
			vo.setYimiaoCost(dbo.getDouble("yimiaoCost"));
			vo.setOtherCost(dbo.getDouble("otherCost"));
			vo.setMedicalCost(dbo.getDouble("medicalCost"));
			vo.setTotle(dbo.getDouble("totle"));
			vo.setHuliCost(dbo.getDouble("huliCost"));
			vo.setZhenchaCost(dbo.getDouble("zhenchaCost"));
			list.add(vo);
		}
		BasicDBObject removeDate=new BasicDBObject();
		removeDate.append("date", date);//删除数据
		new MongoBasicDao().remove(savemongoCollection, removeDate);
		
		if(list.size()>0){
			List<DBObject> voList = new ArrayList<DBObject>();
			for(PcWorkTotal vo:list){
				BasicDBObject obj = new BasicDBObject();
				obj.append("date", date);
	    		obj.append("deptCode", vo.getDeptCode());
	    		obj.append("deptName", vo.getDeptName());
	    		obj.append("docCode", vo.getDoctorCode());
	    		obj.append("docName", vo.getDoctorName());
	    		obj.append("ryrs", vo.getRyrs());
	    		obj.append("cyrs", vo.getCyrs());
	    		obj.append("operationNum", vo.getOperationNum());
	    		obj.append("avgindate", vo.getAvgindate());
	    		obj.append("cfs", vo.getCfs());
	    		
	    		obj.append("westernCost", vo.getWesternCost());
	    		obj.append("chineseCost", vo.getChineseCost());
	    		obj.append("herbalCost", vo.getHerbalCost());
	    		obj.append("allCost", vo.getAllCost());
	    		obj.append("chuangweiCost", vo.getChuangweiCost());
	    		obj.append("treatmentCost", vo.getTreatmentCost());
	    		obj.append("inspectCost", vo.getInspectCost());
	    		obj.append("radiationCost", vo.getRadiationCost());
	    		obj.append("testCost", vo.getTestCost());
	    		obj.append("shoushuCost", vo.getShoushuCost());
	    		obj.append("bloodCost", vo.getBloodCost());
	    		obj.append("o2Cost", vo.getO2Cost());
	    		obj.append("cailiaoCost", vo.getCailiaoCost());
	    		obj.append("yimiaoCost", vo.getYimiaoCost());
	    		obj.append("otherCost", vo.getOtherCost());
	    		obj.append("medicalCost", vo.getMedicalCost());
	    		obj.append("totle", vo.getTotle());
	    		obj.append("huliCost", vo.getHuliCost());
	    		obj.append("zhenchaCost", vo.getZhenchaCost());
	    		voList.add(obj);
			}
			new MongoBasicDao().insertDataByList(savemongoCollection, voList);//添加新数据
			
		}
	}
}
