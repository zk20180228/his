package cn.honry.statistics.doctor.wordLoadDoctorTotal.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.HospitalWork;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.MenuVO;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.dao.WordLoadDoctorTotalDao;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("wordLoadDoctorTotalDao")
@SuppressWarnings("all")
public class WordLoadDoctorTotalDaoImpl implements WordLoadDoctorTotalDao {
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier(value="dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public void setDataJurisInInterDAO(DataJurisInInterDAO dataJurisInInterDAO) {
		this.dataJurisInInterDAO = dataJurisInInterDAO;
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	@Override
	public List<WordLoadVO> queryInhosWordTotal(String begin, String end,
			String dept, String doctorCode) {
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		bdObjectTimeS.put("workdate", new BasicDBObject("$gt",begin));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("workdate", new BasicDBObject("$lte",end));
		condList.add(bdObjectTimeE);
		bdObject.append("$and", condList);
		if(StringUtils.isNotBlank(dept)){
			bdObject.append("deptCode", dept);
		}
		if(StringUtils.isNotBlank(doctorCode)){
			bdObject.append("doctorCode", dept);
		}
		DBCursor cursor = new MongoBasicDao().findAlldata("ZYTJFXZYYSGZLTJ", bdObject);
		DBObject dbCursor;
		List<WordLoadVO> list1=new ArrayList<WordLoadVO>();
		
		while(cursor.hasNext()){
			WordLoadVO voOne=new  WordLoadVO();
			 dbCursor = cursor.next();
			 String deptCode = (String) dbCursor.get("deptCode") ;
			 String doctorCD = (String) dbCursor.get("doctorCode");
			 String doctorNM = (String) dbCursor.get("doctorName");
			 Integer total = (Integer) dbCursor.get("total");
			 String workDate=(String)dbCursor.get("workdate");
			voOne.setDeptCode(deptCode);
			voOne.setDoctorCode(doctorCD);
			voOne.setDoctorName(doctorNM);
			voOne.setWorkTotal(total);
			voOne.setWorkDate(workDate);
			list1.add(voOne);
		}
			return list1;
	}
	@Override
	public void init_ZYYSGZLTJ_Num(List<String> tnL, String begin,String end) {
		if(tnL!=null&&tnL.size()>0){
		Date beginDate=new Date();
		String menuAlias="ZYYSGZLTJ_NUM_DAY";
		String date=begin.substring(0,10);
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
		List<WordLoadVO> list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
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
		if(list!=null && list.size()>0){
			for(WordLoadVO vo:list){
				if(vo.getWorkTotal()!=null&&vo.getMoneyTotal()!=null){
				Document doucment1=new Document();
				doucment1.append("deptCode", vo.getDeptCode());
				doucment1.append("deptName", vo.getDeptName());
				doucment1.append("doctorCode", vo.getDeptCode());
				doucment1.append("workdate", vo.getWorkDate());
				doucment1.append("doctorName", vo.getDoctorName());
				Document document = new Document();
				document.append("deptCode", vo.getDeptCode());
				document.append("deptName", vo.getDeptName());
				document.append("doctorCode", vo.getDoctorCode());
				document.append("doctorName", vo.getDoctorName());
				document.append("total", vo.getWorkTotal());
				document.append("workdate", vo.getWorkDate());
				document.append("moneyTotal",vo.getMoneyTotal());
				new MongoBasicDao().update(menuAlias, doucment1, document, true);
			           }
			     }
			}
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias, list, date);
		}
	}
	@Override
	public void init_ZYYSGZLTJ_Num_MoreDay(String begin, String end,String type) {
		Date beginDate=new Date();
		
		Map<String,Integer> map=new HashMap<String,Integer>();//科室  数量
		Map<String,Double> map1=new HashMap<String,Double>();//科室  金额
		String temp;//月数据
		Integer nu;
		String temp1;//key
		
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		Double dou;//金额
		if("2".equals(type)){
			temp=begin.substring(0,7);
			queryMongo="ZYYSGZLTJ_NUM_DAY";
			saveMongo="ZYYSGZLTJ_NUM_MONTH";
		}else{
			temp=begin.substring(0,4);
			queryMongo="ZYYSGZLTJ_NUM_MONTH";
			saveMongo="ZYYSGZLTJ_NUM_YEAR";
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
					 Integer value = (Integer) dbCursor.get("total") ;//总量
					 String deptCode =(String)dbCursor.get("deptCode");//科室代码
					 String  doctor=(String)dbCursor.get("doctorCode");//住院医生
					 String  doctorName=(String)dbCursor.get("doctorName");//住院医生name
					 Double  moneyTotal=(Double)dbCursor.get("moneyTotal");//金额
					 
			        temp1=temp+"&"+deptCode+"&"+doctor+"&"+doctorName; 
			    if(map.containsKey(temp1)){//如果key存在 比较name
						nu=map.get(temp1);
						nu+=value;
						map.put(temp1, nu);
						dou=map1.get(temp1);
						dou+=moneyTotal;
						map1.put(temp1, dou);
				 }else{//如果key不存在   添加到map1中
					 map.put(temp1, value);
					 map1.put(temp1, moneyTotal);
				    }
				   }
		String[] strKey;
		int i=0;
		for(String key:map.keySet()){
			  strKey=key.split("&");
			  	Document doucment1=new Document();
				doucment1.append("deptCode", strKey[1]);
				doucment1.append("doctorCode",  strKey[2]);
				doucment1.append("workdate",  strKey[0]);
				doucment1.append("doctorName",  strKey[3]);
				Document document = new Document();
				document.append("deptCode",  strKey[1]);
				document.append("doctorCode",  strKey[2]);
				document.append("doctorName",  strKey[3]);
				document.append("total",map.get(key));
				document.append("workdate",  strKey[0]);
				document.append("moneyTotal", map1.get(key));
				new MongoBasicDao().update(saveMongo, doucment1, document, true);
				i++;
			}
		map1=null;
		map=null;
		
		wordLoadDocDao.saveMongoLog(beginDate, saveMongo,new ArrayList(i), temp);
	}
	
	@Override
	public void init_ZYYSGZLTJ_Total(List<String> tnL, String begin,
			String end){
		
		if(tnL!=null&&tnL.size()>0){
			String menuAlias="ZYYSGZLTJ_TOTAL_DAY";//表名
			String date=begin.substring(0,10);//更新时间
			
			Date beginDate=new Date();
			StringBuffer buffer=new StringBuffer(150);
			buffer.append("select b.dat as date1,count(b.code) as con from ( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select t"+i+".house_doc_code as code,to_char(t"+i+".in_date,'yyyy-mm-dd') as dat  ");
				buffer.append("from "+tnL.get(i)+" t"+i+" ");
				buffer.append("where t"+i+".in_date >= to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".in_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			buffer.append(") b  group by b.dat ");
			List<WordLoadVO> list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
				@Override
				public WordLoadVO mapRow(ResultSet rs, int rownum)
						throws SQLException {
					WordLoadVO vo=new WordLoadVO();
					vo.setWorkDate(rs.getString("date1"));
					vo.setWorkTotal(rs.getInt("con"));
					return vo;
				}
			});
			if(list!=null && list.size()>0){
				for(WordLoadVO vo:list){
					if(vo.getWorkTotal()!=null){
					Document doucment1=new Document();
					doucment1.append("workdate", vo.getWorkDate());
					Document document = new Document();
					document.append("total", vo.getWorkTotal());
					document.append("workdate", vo.getWorkDate());
					new MongoBasicDao().update(menuAlias, doucment1, document, true);
					}
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias, list, date);
		}
		
	}
	@Override
	public void init_ZYYSGZLTJ_Total_MoreDay(String begin, String end,String type) {
		Date beginDate=new Date();
		Map<String,Integer> map=new HashMap<String,Integer>();
		String temp;//月数据
		Integer dou;
		String temp1;//key
		
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		queryMongo="ZYYSGZLTJ_TOTAL";
		saveMongo="ZYYSGZLTJ_TOTAL";
		if("2".equals(type)){
			temp=begin.substring(0,7);
			queryMongo+="_DAY";
			saveMongo+="_MONTH";
		}else {
			temp=begin.substring(0,4);
			queryMongo+="_MONTH";
			saveMongo+="_YEAR";
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
					Dashboard voOne=new  Dashboard();
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
				int i=0;
			for(String key:map.keySet()){
				    Document doucment1=new Document();
				    doucment1.append("workdate", key);
				    Document document = new Document();
					document.append("total",map.get(key));
					document.append("workdate", key);
				new MongoBasicDao().update(saveMongo, doucment1, document, true);
				i++;
				}
		wordLoadDocDao.saveMongoLog(beginDate, saveMongo, new ArrayList(i), temp);
	}
	
	/**
	 * 科室医生工作量数据初始化
	 */
	@Override
	public void init_ZYYSGZLTJ_Detail(List<String> tnL, String begin,
			String end) {
		if(tnL!=null&&tnL.size()>0){
			Date beginDate=new Date();
			String menuAlias="ZYYSGZLTJ_DETAIL_DAY";
			String date=begin.substring(0,10);
			
			StringBuffer buffer=new StringBuffer(500);
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
			if(list!=null && list.size()>0){
				for(WordLoadVO vo:list){
					if(vo.getOpenTotal()!=null&&vo.getMoneyTotal()!=null){
					Document doucment1=new Document();
					doucment1.append("workdate", vo.getWorkDate());
					doucment1.append("deptCode", vo.getDeptCode());
					doucment1.append("doctorCode", vo.getDoctorCode());
					Document document = new Document();
					document.append("total", vo.getOpenTotal());
					document.append("workdate", vo.getWorkDate());
					document.append("deptCode", vo.getDeptCode());
					document.append("deptName", vo.getDeptName());
					document.append("doctorCode", vo.getDoctorCode());
					document.append("doctorName", vo.getDoctorName());
					document.append("moneyTotal", vo.getMoneyTotal());
					new MongoBasicDao().update(menuAlias, doucment1, document, true);
					}
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias, list, date);
			
		}
	}
	@Override
	public void init_ZYYSGZLTJ_Detail_MoreDay(String begin, String end,String type,String docOrDept) {
		Date beginDate=new Date();
		
		String temp;//临时时间
		String queryMongo=null;//查询的表
		String saveMongo=null;//保存的表
		Double dou;//金额
		Double con;//数量
		String temp1;//map的key
		Map<String,Double> map=new HashMap<String,Double>();//存储开立数量
		Map<String,Double> map1=new HashMap<String,Double>();//存储金额
		
		String searchNo1;//查询的字段
		String searchNo2;//查询的字段
		String menuAlias="ZYYSGZLTJ";
		
		String tempMenuAlias=menuAlias;
		if("DOC".equals(docOrDept)){
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
			temp=begin.substring(0,7);
			queryMongo=menuAlias+"_DETAIL_DAY";
			saveMongo=tempMenuAlias+"_DETAIL_MONTH";
		}else{
			temp=begin.substring(0,4);
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
				String[] dateArr;
				int i=0;
			for(String key:map.keySet()){
			    dateArr=key.split("&");
			    Document doucment1=new Document();
			    doucment1.append("workdate", dateArr[0]);
			    doucment1.append(searchNo1, dateArr[1]);
			    doucment1.append(searchNo2, dateArr[2]);
			    Document document = new Document();
			    document.append("workdate", dateArr[0]);
			    document.append(searchNo1, dateArr[1]);
			    document.append(searchNo2, dateArr[2]);
			    document.append("total", map.get(key));//开立数量
			    document.append("moneyTotal", map1.get(key));//金额
				new MongoBasicDao().update(saveMongo, doucment1, document, true);
				i++;
				}
		wordLoadDocDao.saveMongoLog(beginDate, saveMongo, new ArrayList(i), temp);
	}
	
/**********************************************************************************************************************************************/
/**
 * 环比
 * @param date
 * @param dateSing
 * @return
 */
public String[] conYear(String date,String dateSing){
	 Calendar ca = Calendar .getInstance();
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
	 String[] dateOne =date.split("-");
	 String[] strArr=new String[6];
	 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
	for(int i=0;i<6;i++){
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
 * 同比
 * @param date
 * @param dateSing
 * @return
 */
public String[] conMonth(String date,String dateSing){
	String [] strArr=new String[6];
	String[] dateArr=date.split("-");
	int dateTemp=Integer.parseInt(dateArr[0]);
	for(int i=0;i<6;i++){
		if("2".equals(dateSing)){//月同比
			strArr[i]=(dateTemp-i)+"-"+dateArr[1];
		}else{
			strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
		}
	}
	
	return strArr;
}
/**
 * 日期前推七天 sql查
 * @param date
 * @param dateSing
 * @return
 */
public Map<String,String> conMonth(String date){
	String[] dateArr=date.split("-");
	Calendar ca=Calendar.getInstance();
	ca.set(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1])-1,Integer.parseInt(dateArr[2]),0,0,0);
	String key;
	Map<String,String> map=new HashMap<String,String>();
	for(int i=0;i<7;i++){
		key=sd.format(ca.getTime());
		ca.add(Calendar.DATE, -1);
		map.put(key,sd.format(ca.getTime()));
	}
	return map;
}
/**
 * 日期前推七天 
 * @param date
 * @param dateSing
 * @return
 */
public String[] OneWeek(String date){
	String [] strArr=new String[7];
	String[] dateArr=date.split("-");
	Calendar ca=Calendar.getInstance();
	ca.set(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1])-1,Integer.parseInt(dateArr[2]));
	String key;
	Map<String,String> map=new HashMap<String,String>();
	for(int i=0;i<7;i++){
		strArr[i]=sdf.format(ca.getTime());
		ca.add(Calendar.DATE, -1);
	}
	return strArr;
}
//获取日期每个月的每天
	public List<String> reMonthDay(String begin,String end,List<String> list){
		 if(begin!=null){
			 Date date;
			 Date endTime;
			try {
				 date = sd.parse(begin);
				 endTime=sd.parse(end);
				 begin=sdf.format(date);
				 String[] dateArr=begin.split("-");
				 Calendar ca=Calendar.getInstance();
				 ca.set(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1])-1,Integer.parseInt(dateArr[2]));
				if(date.getTime()>=endTime.getTime()){
					begin=sdf.format(ca.getTime());
					list.add(begin);
					return list;
				}else{
					begin=sdf.format(ca.getTime());
					ca.add(Calendar.DATE, 1);
					list.add(begin);
					begin=sd.format(ca.getTime());
					return reMonthDay(begin,end,list);
				}
				
			} catch (ParseException e) {
				return list;
			}
		 }else{
			 return new ArrayList<String>();
		 }
	}
	//获取每年的每月
	public List<String> reYearMonth(String begin,String end,List<String> list){
		 if(begin!=null){
			 Date date;
			 Date endTime;
			try {
				 date = sd.parse(begin);
				 endTime=sd.parse(end);
				 begin=sdfMonth.format(date);//
				 String[] dateArr=begin.split("-");
				 Calendar ca=Calendar.getInstance();
				 ca.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
				 ca.set(Calendar.MONTH,Integer.parseInt(dateArr[1])-1);
				if(date.getTime()>=endTime.getTime()){
					begin=sdfMonth.format(ca.getTime());
					list.add(begin);
					return list;
				}else{
					begin=sdfMonth.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					list.add(begin);
					begin=sd.format(ca.getTime());
					return reYearMonth(begin,end,list);
				}
			}catch (ParseException e) {
				return list;
			}
		 }else{
			 return new ArrayList<String>();
		 }
	}
	/**
	 * 环比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conYearOne(String date,String dateSing){
		 Calendar ca = Calendar .getInstance();
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
		 String[] dateOne =date.split("-");
		 String[] strArr=new String[6];
		 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
		for(int i=0;i<6;i++){
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
	 * 同比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conMonthOne(String date,String dateSing){
		String [] strArr=new String[6];
		String[] dateArr=date.split("-");
		int dateTemp=Integer.parseInt(dateArr[0]);
		for(int i=0;i<6;i++){
			if("2".equals(dateSing)){//月同比
				strArr[i]=(dateTemp-i)+"-"+dateArr[1];
			}else{
				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
			}
		}
		return strArr;
	}
/*************************************************************************************************************************/
	/**
	 * 科室   医生 明细工作量查询
	 */
	@Override
	public List<WordLoadVO> queryForDB(String date,String dateSign) {
		BasicDBObject bdObject = new BasicDBObject();
		List<WordLoadVO> list=new ArrayList<WordLoadVO>();
		bdObject.append("workdate", date);
		DBCursor cursor = new MongoBasicDao().findAlldata("ZYTJFXZYYSGZLTJ", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			WordLoadVO voOne=new  WordLoadVO();
			 dbCursor = cursor.next();
			 Integer  value = (Integer) dbCursor.get("total") ;//工作量
			 String  workDate=(String) dbCursor.get("workdate") ;//工作时间
			 Double moneyTotal=(Double) dbCursor.get("moneyTotal") ;//金额
			 String doctorName=(String)dbCursor.get("doctorName") ;//医生姓名
			 String deptName=(String)dbCursor.get("deptCode") ;//科室
			 voOne.setWorkTotal(value);
			 voOne.setWorkDate(workDate);
			 voOne.setMoneyTotal(moneyTotal);
			 voOne.setDoctorName(doctorName);
			 voOne.setDeptCode(deptName);
			 list.add(voOne);
		  }
		return list;
	}
	/**
	 * 数据展示
	 */
	@Override
	public Map queryForDBList(String begin, String end,String depts,String doctors,String menuAlias,String row,String page) {
		Map map=new HashMap();
		map.put("total", 0);
		map.put("rows", new ArrayList<WordLoadVO>());
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBList mongoDeptList = new BasicDBList();
		BasicDBList mongoDocList = new BasicDBList();
		List<WordLoadVO> list=new ArrayList<WordLoadVO>();
		bdObjectTimeS.put("workdate",new BasicDBObject("$gte",begin));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("workdate",new BasicDBObject("$lte",end));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		boolean sign=true;
		if(StringUtils.isBlank(depts)){//如果科室为空 查询授权科室
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
			if(deptList == null && deptList.size() == 0){//如果授权科室为空 查询当前医生工作量
				sign=false;
				bdObject.append("doctorCode", jobNo);
			}else{
				int len=deptList.size();
				if(len>0){
					for(int i = 0;i<len;i++){
						mongoDeptList.add(new BasicDBObject("deptCode",deptList.get(i).getDeptCode()));
					}
					bdObject.put("$or", mongoDeptList);
				}
			}
		}else{
			String[] dept= depts.split(",");
			for(String dep:dept){
				mongoDeptList.add(new BasicDBObject("deptCode",dep));
			}
			bdObject.put("$or", mongoDeptList);
		}
		if(StringUtils.isNotBlank(doctors)&&sign){
			String[] doctor= doctors.split(",");
			for(String doc:doctor){
				mongoDocList.add(new BasicDBObject("doctorCode",doc));
			}
			bdObject.put("$or", mongoDocList);
		}
		DBCursor cursor1 = new MongoBasicDao().findAlldata("ZYYSGZLTJ_DETAIL_DAY",bdObject);
		DBObject dbCursor1;
		int num=0;
		while(cursor1.hasNext()){
			 dbCursor1 = cursor1.next();
			 num++;
		 }
		DBCursor cursor = new MongoBasicDao().findAllDataSortBy("ZYYSGZLTJ_DETAIL_DAY", "_id", bdObject,Integer.parseInt(row),Integer.parseInt( page));
		DBObject dbCursor;
		while(cursor.hasNext()){
			WordLoadVO voOne=new  WordLoadVO();
			 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("total") ;//开立数量
				 Double moneyTotal=(Double) dbCursor.get("moneyTotal") ;//金额
				 String doctorName=(String)dbCursor.get("doctorName") ;//医生姓名
				 String deptName=(String)dbCursor.get("deptName") ;//科室
				 voOne.setOpenTotal(value);
				 voOne.setMoneyTotal(moneyTotal);
				 voOne.setDoctorName(doctorName);
				 voOne.setDeptCode(deptName);
				 list.add(voOne);
		  }
		map.put("total", num);
		map.put("rows", list);
		return map;
	}
/**
 * 医生工作量同比
 */
@Override
public List<WordLoadVO> queryForDBSame(String date, String dateSign,String queryMongo) {
	BasicDBObject bdObject = new BasicDBObject();
	String[] dateArr=this.conMonth(date, dateSign);
	List<WordLoadVO> list=new ArrayList<WordLoadVO>();
	for(String st:dateArr){
	bdObject.append("workdate", st);
	DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
	DBObject dbCursor;
	WordLoadVO voOne=new  WordLoadVO();
	if(!cursor.hasNext()){
		voOne.setWorkDate(st);
		voOne.setMoneyTotal(0.0);
		list.add(voOne);
		continue;
	}
	Double dou=0.0;
	while(cursor.hasNext()){
		 dbCursor = cursor.next();
		 Double  moneyTotal=(Double) dbCursor.get("moneyTotal");//金额
		 if(moneyTotal!=null){
			 dou+=moneyTotal;
		 }
	  }
	 voOne.setWorkDate(st);
	 voOne.setMoneyTotal(dou);
	 list.add(voOne);
	}
	return list;
}
/**
 * 环比
 */
@Override
public List<WordLoadVO> queryForDBSque(String date, String dateSign,String queryMongo) {
	BasicDBObject bdObject = new BasicDBObject();
	String[] dateArr=this.conYear(date, dateSign);
	List<WordLoadVO> list=new ArrayList<WordLoadVO>();
	for(String st:dateArr){
		bdObject.append("workdate", st);
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
		DBObject dbCursor;
		WordLoadVO voOne=new  WordLoadVO();
		if(!cursor.hasNext()){
			voOne.setWorkDate(st);
			voOne.setMoneyTotal(0.0);
			list.add(voOne);
			continue;
		 }
		Double dou=0.0;
		while(cursor.hasNext()){
			 dbCursor = cursor.next();
			 Double  moneyTotal=(Double) dbCursor.get("moneyTotal");//金额
			 if(moneyTotal!=null){
				 dou+=moneyTotal;
			 }
		  }
		 voOne.setWorkDate(st);
		 voOne.setMoneyTotal(dou);
		 list.add(voOne);
		}
		return list;
}
@Override
public WordLoadVO queryForOraSque(List<String> tnL, String begin, String end,String dateSign) {
	if(tnL!=null&&tnL.size()>0){
		String dateFormate;
		if("1".equals(dateSign)){
			dateFormate="yyyy";
		}else if("2".equals(dateSign)){
			dateFormate="yyyy-mm";
		}else{
			dateFormate="yyyy-mm-dd";
		}
		StringBuffer buffer=new StringBuffer(200);
		buffer.append("select to_char(b.data1,'"+dateFormate+"') as workDate,sum(b.total) as moneyTotal  from( ");
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
		buffer.append(") b group by b.data1");
	List<WordLoadVO> list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
		@Override
		public WordLoadVO mapRow(ResultSet rs, int rownum)
				throws SQLException {
			WordLoadVO vo=new WordLoadVO();
			vo.setWorkDate(rs.getString("workDate"));
			vo.setMoneyTotal(rs.getDouble("moneyTotal"));
			return vo;
		 }
		});
		if(list.size()>0){
			return list.get(0);
		}
	}
	WordLoadVO vo=new WordLoadVO();
	if("1".equals(dateSign)){
		vo.setWorkDate(begin.substring(0, 4));	
	}else if("2".equals(dateSign)){
		vo.setWorkDate(begin.substring(0, 7));
	}else{
		vo.setWorkDate(begin.substring(0, 10));
	}
	vo.setMoneyTotal(0.0);
	return vo;
}



@Override
public List<WordLoadVO> queryForOra(List<String> tnL, String begin, String end,String dateSign) {
	if(tnL!=null&&tnL.size()>0){
		String dateFormate;
		if("1".equals(dateSign)){
			dateFormate="yyyy";
		}else if("2".equals(dateSign)){
			dateFormate="yyyy-mm";
		}else{
			dateFormate="yyyy-mm-dd";
		}
		StringBuffer buffer=new StringBuffer(150);
		buffer.append("select b.code as doctorCode,(select e.dept_name from t_department e where e.dept_code=b.deptcode ) AS deptCode,b.name as doctorName,b.dat as date1,count(b.code) as con,sum(b.total) as moneyTotal from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select t"+i+".DEPT_CODE as deptcode,t"+i+".house_doc_code as code,t"+i+".house_doc_name as name,to_char(t"+i+".in_date,'"+dateFormate+"') as dat,t"+i+".TOT_COST AS total ");
			buffer.append("from "+tnL.get(i)+" t"+i+" ");
			buffer.append("where t"+i+".in_date between to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		}
		buffer.append(") b  group by b.code,b.name,b.dat,b.deptcode ");
		List<WordLoadVO> list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
			@Override
			public WordLoadVO mapRow(ResultSet rs, int rownum)
					throws SQLException {
				WordLoadVO vo=new WordLoadVO();
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDoctorCode(rs.getString("doctorCode"));
				vo.setDoctorName(rs.getString("doctorName"));
				vo.setWorkDate(rs.getString("date1"));
				vo.setWorkTotal(rs.getInt("con"));
				vo.setMoneyTotal(rs.getDouble("moneyTotal"));
				return vo;
			}
		});
			if(list.size()>0){
				return list;
			}
		}
		return new ArrayList<WordLoadVO>();
	}



@Override
public Map<String,Object> queryForOraList(List<String> tnL, String begin,
		String end,String depts,String doctors,String menuAlias,String row,String page) {
	Map<String,Object> map=new HashMap<String,Object>();
	map.put("total", 0);
	map.put("rows", new ArrayList<WordLoadVO>());
	List<List<String>> deptListAll =null;
	if(tnL!=null&&tnL.size()>0){
		if(StringUtils.isBlank(depts)){
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
			List<String> deptCodeList =new ArrayList<String>(500);;
			deptListAll = new ArrayList<List<String>>();
			if(deptList != null || deptList.size() > 0){
				SysDepartment dept=new SysDepartment();
				for (int i=0,len=deptList.size(),len1=len-1;i<len;i++){
					dept=deptList.get(i);
					String code = dept.getDeptCode();
					if(StringUtils.isNotBlank(code)){
						if((i+1)%500==0||i==len1){
							deptListAll.add(deptCodeList);
							deptCodeList=new ArrayList<String>(500);
						}
						deptCodeList.add(code);
					}
				}
				deptList=null;	
			}
		}
		StringBuffer buffer=new StringBuffer(400);
		buffer.append("select * from( ");
		buffer.append("select to_char(b.data1,'yyyy-mm-dd') as workDate,b.doc as doctorCode,b.docName as doctorName,sum(b.num) as workTotal,sum(b.total) as moneyTotal,b.deptCode as deptCode,b.deptName as deptName,rownum as rn  from( ");
		for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" union all ");
		}
		buffer.append("select t"+i+".mo_date as data1,t"+i+".doc_code as doc,t"+i+".doc_name docName,");
		buffer.append("trunc(t"+i+".qty_tot, 2) as num,trunc(t"+i+".qty_tot * t"+i+".item_price, 2) as total,");
		buffer.append("t"+i+".exec_dpcd as deptCode,t"+i+".exec_dpnm as deptName ");
		buffer.append("from "+tnL.get(i)+" t"+i+" ");
		buffer.append("where t"+i+".mo_stat = 2 ");
		buffer.append("and t"+i+".mo_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".mo_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss')  ");
		if(StringUtils.isNotBlank(depts)){
			depts="'"+depts.replace(",", "','")+"'";
			buffer.append("and t"+i+".exec_dpcd in("+depts+") ");
		}else{
			int len1=deptListAll.size();
			if(len1>0){
				buffer.append(" and ( ");
				for(int j=0;j<len1;j++){
					if(j>0){
						buffer.append(" or ");
					}
						buffer.append(" t"+i+".exec_dpcd in(:dept"+j+") ");
				}
					buffer.append(" ) ");
			}else if(StringUtils.isBlank(doctors)){
				buffer.append(" and t"+i+".doc_code='"+ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo()+"' ");
			}
		}
		if(StringUtils.isNotBlank(doctors)){
			doctors="'"+doctors.replace(",", "','")+"'";
			buffer.append("and t"+i+".doc_code in("+doctors+") ");
		}
	}
	buffer.append(") b where  rownum<"+row+"*"+page+" group by b.data1,b.doc,b.docName,b.deptCode,b.deptName,rownum ) where  rn>("+page+"-1)*"+row);
	Map<String,Object> mapParam=new HashMap<String,Object>();
	if(StringUtils.isBlank(depts)){
		for(int j=0,len=deptListAll.size();j<len;j++){
			mapParam.put("dept"+j, deptListAll.get(j));
		}
	}
	List<WordLoadVO> list=namedParameterJdbcTemplate.query(buffer.toString(),mapParam,new RowMapper<WordLoadVO>() {
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
	
	buffer=new StringBuffer(400);
	buffer.append("select nvl(sum(b.con),0) as con from( ");
	for(int i=0,len=tnL.size();i<len;i++){
	if(i>0){
		buffer.append(" union all ");
	}
	buffer.append("select count(1) as con ");
	buffer.append("from "+tnL.get(i)+" t"+i+" ");
	buffer.append("where t"+i+".mo_stat = 2 ");
	buffer.append("and t"+i+".mo_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".mo_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss')  ");
		if(StringUtils.isNotBlank(depts)){
			depts="'"+depts.replace(",", "','")+"'";
			buffer.append("and t"+i+".exec_dpcd in("+depts+") ");
		}else{
			int len1=deptListAll.size();
			if(len1>0){
				buffer.append(" and ( ");
				for(int j=0;j<len1;j++){
					if(j>0){
						buffer.append(" or ");
					}
					buffer.append(" t"+i+".exec_dpcd in(:dept"+j+") ");
				}
				buffer.append(" ) ");
			}else if(StringUtils.isBlank(doctors)){
				buffer.append(" and t"+i+".doc_code='"+ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo()+"' ");
			}
		}
		if(StringUtils.isNotBlank(doctors)){
			doctors="'"+doctors.replace(",", "','")+"'";
			buffer.append("and t"+i+".doc_code in("+doctors+") ");
		}
	}
	buffer.append(" ) b ");
	if(StringUtils.isBlank(depts)){
		for(int j=0,len=deptListAll.size();j<len;j++){
			mapParam.put("dept"+j, deptListAll.get(j));
		}
	}
	List<WordLoadVO> list1=namedParameterJdbcTemplate.query(buffer.toString(),mapParam,new RowMapper<WordLoadVO>() {
		@Override
		public WordLoadVO mapRow(ResultSet rs, int rownum)
				throws SQLException {
			WordLoadVO vo=new WordLoadVO();
			vo.setWorkTotal(rs.getInt("con"));
			return vo;
		}
	});
	map.put("rows", list);
	map.put("total",list1.get(0).getWorkTotal());
	}
	return map;
}

/**
 *  dept dept doc doc 
 * @param date
 * @param dateSign
 * @param deptOrDco
 * @return
 */
@Override
public List<WordLoadVO> queryDeptDocTopFive(String date, String dateSign,String collections,String deptOrDco) {
	String search;//查询列
	if("dept".equals(deptOrDco)){
		search="deptName";
	}else{
		search="doctorName";
	}
	BasicDBObject bdObject = new BasicDBObject();
	List<WordLoadVO> list=new ArrayList<WordLoadVO>();
	bdObject.append("workdate", date);
	DBCursor cursor = new MongoBasicDao().findAlldataBySort(collections, bdObject,"moneyTotal");
	DBObject dbCursor;
	int temp=5;
	while(cursor.hasNext()){
		WordLoadVO vo=new WordLoadVO();
		 dbCursor = cursor.next();
		 Double  moneyTotal=(Double) dbCursor.get("moneyTotal");//金额
		 String  top=(String) dbCursor.get(search);//字段
		 if(StringUtils.isNotBlank(top)){
			vo.setMoneyTotal(moneyTotal);
			vo.setTopName(top);
			list.add(vo);
			temp--;
		 }
		 if(temp<=0){
			 break;
		 }
	}	
	return list;
}
@Override
public List<WordLoadVO> queryDeptDocTopFive(List<String> tnL, String begin,
		String end, String deptOrDco) {
		String dateFormate;
		if(tnL!=null&&tnL.size()>0){
			String column="dept".equals(deptOrDco)?"exec_dpnm":"doc_name";
			StringBuffer buffer=new StringBuffer(200);
			buffer.append("select f.topName as topName,f.moneyTotal as moneyTotal from( ");
			buffer.append("select b.top as topName,sum(b.total) as moneyTotal,rownum as rn from( ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select t"+i+"."+column+" as top,");
				buffer.append("trunc(t"+i+".qty_tot, 2) as num,trunc(t"+i+".qty_tot * t"+i+".item_price, 2) as total ");
				buffer.append("from "+tnL.get(i)+" t"+i+" ");
				buffer.append("where t"+i+".mo_stat = 2 ");
				buffer.append("and t"+i+".mo_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".mo_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			buffer.append(") b group by b.top, rownum order by moneyTotal desc ");
			buffer.append(") f where f.rn<6 ");
			List<WordLoadVO> list=namedParameterJdbcTemplate.query(buffer.toString(),new HashMap(),new RowMapper<WordLoadVO>() {
				@Override
				public WordLoadVO mapRow(ResultSet rs, int rownum)
						throws SQLException {
					WordLoadVO vo=new WordLoadVO();
					vo.setMoneyTotal(rs.getDouble("moneyTotal"));//开立金额
					vo.setTopName(rs.getString("topName"));
					return vo;
				}
			});
			if(list.size()>0){
				return list;
			}
			
		}
	return new ArrayList<WordLoadVO>();
}
@Override
public List<MenuListVO> getDoctorList(String deptTypes, String menuAlias) {
	List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
	List<String> deptCodeList =new ArrayList<String>(500);;
	List<List<String>> deptListAll = new ArrayList<List<String>>();
	Map<String, Object> paraMap = new HashMap<String, Object>();
	StringBuffer buffer = new StringBuffer(300);
	buffer.append(" SELECT DE.DEPT_CODE as deptId,em.EMPLOYEE_jobNo as jobNo,EM.EMPLOYEE_NAME as name,de.DEPT_TYPE as type,");
	buffer.append(" em.EMPLOYEE_PINYIN as pinyin,em.EMPLOYEE_WB as wb,em.EMPLOYEE_INPUTCODE as inputCode ");
	buffer.append(" from T_EMPLOYEE em,T_DEPARTMENT de ");
	buffer.append(" where EM.DEPT_ID =DE.DEPT_CODE and DE.DEL_FLG=0 and DE.STOP_FLG=0 and em.STOP_FLG=0 and EM.DEL_FLG=0 ");
	buffer.append(" AND EM.employee_type = 1 ");
	if(deptList == null || deptList.size() == 0){
		buffer.append("and em.EMPLOYEE_JOBNO = '"+ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo()+"'");
	}else{
		SysDepartment dept=new SysDepartment();
		for (int i=0,len=deptList.size(),len1=len-1;i<len;i++) {
			dept=deptList.get(i);
			String code = dept.getDeptCode();
			if(StringUtils.isNotBlank(code)){
				if((i+1)%500==0||i==len1){
					deptListAll.add(deptCodeList);
					deptCodeList=new ArrayList<String>(500);
				}
				deptCodeList.add(code);
			}
		}
		deptList=null;
		buffer.append(" AND (");
		for(int i=0,len=deptListAll.size();i<len;i++){
			if(i>0){
				buffer.append(" or");
			}
			buffer.append(" DE.dept_code IN(:dept"+i+") ");
		}
		buffer.append(" )");
		buffer.append(" UNION ");
		buffer.append(" select l.DEPT_ID as deptId,l.USER_ID AS jobNo,E.EMPLOYEE_NAME AS name,d.DEPT_TYPE as type, ");
		buffer.append(" e.EMPLOYEE_PINYIN as pinyin,e.EMPLOYEE_WB as wb,e.EMPLOYEE_INPUTCODE as inputCode ");
		buffer.append(" FROM T_SYS_USER_LOGINDEPT l ");
		buffer.append(" INNER  JOIN T_DEPARTMENT D ON D .DEPT_CODE = l.dept_id ");
		buffer.append(" AND (");
		for(int i=0,len=deptListAll.size();i<len;i++){
			if(i>0){
				buffer.append(" or");
			}
			buffer.append(" l.dept_id IN (:dept"+i+")");
		}
		buffer.append(" )");
		buffer.append(" INNER JOIN T_EMPLOYEE E ON E .EMPLOYEE_ID = l.USER_ID and E.EMPLOYEE_type= 1 ");
	}
	List<MenuListVO> depts=new ArrayList<MenuListVO>();
	for(int i=0,len=deptListAll.size();i<len;i++){
		paraMap.put("dept"+i, deptListAll.get(i));
	}
	List<MenuVO> voList = namedParameterJdbcTemplate.query(buffer.toString(),paraMap, new RowMapper<MenuVO>(){
		@Override
		public MenuVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			MenuVO vo = new MenuVO();
			vo.setRelativeId(rs.getString("deptId"));
			vo.setType(rs.getString("type"));
			vo.setId(rs.getString("jobNo"));
			vo.setName(rs.getString("name"));
			vo.setInputCode(rs.getString("inputCode"));
			vo.setPinyin(rs.getString("pinyin"));
			vo.setWb(rs.getString("wb"));
			return vo;
		}
	});
	deptListAll=null;
	paraMap=null;
	List<MenuListVO> doctors=new ArrayList<MenuListVO>();
	String[] arr=new String[]{"C-门诊","I-住院","F-财务","L-后勤","PI-药库","T-医技(终端)","0-其它","D-机关(部门)","P-药房","N-护士站","S-科研","O-其他","OP-手术","U-自定义"};
	Map<String, MenuVO> userMap = new HashMap<String, MenuVO>();
	for(MenuVO vo : voList){
		if(userMap.get(vo.getId())!=null){
			MenuVO vo1 = new MenuVO();
			vo1 = userMap.get(vo.getId());
			vo1.setRelativeId(vo.getUserDeptId()+"-"+vo1.getRelativeId());
			userMap.remove(userMap.get(vo.getId()));
			userMap.put(vo1.getId(), vo1);
		}else{
			userMap.put(vo.getId(), vo);
			continue;
		}
	}
	List<MenuVO> voList1 = new ArrayList<MenuVO>();
	for(MenuVO mvo : userMap.values()){
		voList1.add(mvo);
	}
	for(int i=0;i<arr.length;i++){
		String[] arr1=arr[i].split("-");
		MenuListVO d=new MenuListVO();
		d.setParentMenu(arr1[1]);
		List<MenuVO> rs=new ArrayList<MenuVO>();
		for(MenuVO v:voList1){
			if(arr1[0].equals(v.getType())){
				rs.add(v);
			}				
		}
		d.setMenus(rs);
		doctors.add(d);
	}
	if(doctors!=null&&doctors.size()>0){
		return doctors;
	}
	return new ArrayList<MenuListVO>();
}
@Override
public List<HospitalWork> queryHosWorkTotal(List<String> tnL, String begin,
		String end, String menuAlias, String depts, String doctors,String page,String rows) {
    StringBuffer buffer=new StringBuffer(2000);
	buffer.append("select L.* from( ");
	buffer.append("select R.*,rownum rn from(");
	buffer.append("select t.deptCode deptcode,t.doctor as doctor,t.hosVisitors as hosVisitors,count(t.inpatient_no) as outVisitors, ");
	buffer.append("count(concurVisitors) concurVisitors, count(cureVisitors) cureVisitors,count(unCureVisitors) unCureVisitors,");
	buffer.append("count(betterVisitors) betterVisitors,count(deathVisitors) deathVisitors,sum(t.averageInhost) averageInhost from( ");
	buffer.append("select t.dept_code as deptCode, ");//科室
	buffer.append("t.HOUSE_DOC_CODE as doctor,");//--医生
	buffer.append("t.inpatient_no as inpatient_no,i.coun as hosVisitors,");//出院
	buffer.append("t.out_date-t.in_date as averageInhost ");//住院天数
	buffer.append(" from ( ");
	for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" union all ");
		}
		buffer.append("select dept_code,HOUSE_DOC_CODE,inpatient_no,in_state,out_date,in_date ");
		buffer.append(" from "+tnL.get(i)+" where 1=1 ");
		if(StringUtils.isNotBlank(depts)){//查询选中科室
			buffer.append(" and dept_code in('").append(depts.replace(",", "','")).append("') ");
		}else{//查询授权科室
			buffer.append(" and dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
		}
		buffer.append(" and IN_DATE>=to_date('").append(begin).append("','yyyy-mm-dd HH24:mi:ss') ")
		.append("and IN_DATE<=to_date('").append(end).append("','yyyy-mm-dd HH24:mi:ss') ");
		if(StringUtils.isNotBlank(doctors)){
			buffer.append(" and HOUSE_DOC_CODE in('").append(doctors.replace(",", "','")).append("') ");
		}
	}
	buffer.append(" ) t left join (select sum(t1.coun) coun ,t1.HOUSE_DOC_CODE HOUSE_DOC_CODE,t1.dept_code dept_code from ( ");
	for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" union all ");
		}
		buffer.append("select count(1) as coun,dept_code,HOUSE_DOC_CODE  from "+tnL.get(i)+"  ");
		buffer.append("where IN_DATE>=to_date('").append(begin).append("','yyyy-mm-dd HH24:mi:ss') ")
		.append("and IN_DATE<=to_date('").append(end).append("','yyyy-mm-dd HH24:mi:ss') ");
		if(StringUtils.isNotBlank(depts)){//查询选中科室
			buffer.append(" and dept_code in('").append(depts.replace(",", "','")).append("') ");
		}else{//查询授权科室
			buffer.append(" and dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
		}
		if(StringUtils.isNotBlank(doctors)){
			buffer.append(" and HOUSE_DOC_CODE in('").append(doctors.replace(",", "','")).append("') ");
		}
		buffer.append("group by dept_code,HOUSE_DOC_CODE ");
	}
	
	buffer.append(" ) t1  group by t1.HOUSE_DOC_CODE,t1.dept_code ) i on i.dept_code=t.dept_code and i.HOUSE_DOC_CODE=t.HOUSE_DOC_CODE where t.in_state in('O') )t ");
	buffer.append("left join (select count(1) concurVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_KIND = 3 group by b.inpatient_no ) b on t.inpatient_no =  b.inpatient_no ");
	buffer.append("left join (select count(1) cureVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=0 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
	buffer.append("left join (select count(1) betterVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=1 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
	buffer.append("left join (select count(1) unCureVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=2 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
	buffer.append("left join (select count(1) deathVisitors, b.inpatient_no from T_BUSINESS_DIAGNOSE b where b.DIAG_OUTSTATE=3 group by b.inpatient_no ) b on t.inpatient_no = b.inpatient_no ");
	buffer.append("group by t.deptCode , t.doctor ,t.hosVisitors ");
	
	buffer.append(" ) R where  rownum <=").append(page+"*"+rows+" ");
	buffer.append(" ) L where L.rn>=("+page+"-1)*"+rows);
	Map<String,String> map=new HashMap<String,String>();
	List<HospitalWork> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<HospitalWork>() {
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
			return vo;
		}
	});
	if(list.size()>0){
		return list;
	}
	return new ArrayList<HospitalWork>();
}
@Override
public int queryHosWorkTotal(List<String> tnL, String begin, String end,
		String menuAlias, String depts, String doctors) {
	StringBuffer buffer=new StringBuffer(500);
	buffer.append("select count(1) as total from ( ");
	buffer.append("select t.dept_code,t.HOUSE_DOC_CODE as total ");
	buffer.append(" from (");
	for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" union all ");
		}
		buffer.append("select dept_code,HOUSE_DOC_CODE  from "+tnL.get(i)+"  ");
		buffer.append("where IN_DATE>=to_date('").append(begin).append("','yyyy-mm-dd HH24:mi:ss') ")
		.append("and IN_DATE<=to_date('").append(end).append("','yyyy-mm-dd HH24:mi:ss') ");
		if(StringUtils.isNotBlank(depts)){//查询选中科室
			buffer.append(" and dept_code in('").append(depts.replace(",", "','")).append("') ");
		}else{//查询授权科室
			buffer.append(" and dept_code in(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
		}
		if(StringUtils.isNotBlank(doctors)){
			buffer.append(" and HOUSE_DOC_CODE in('").append(doctors.replace(",", "','")).append("') ");
		}
		buffer.append("and in_state='O' ");
	}
	
	buffer.append(" ) t ");
	buffer.append("group by t.dept_code,t.HOUSE_DOC_CODE ) ");
	Map<String,String> map=new HashMap<String,String>();
	List<HospitalWork> list=namedParameterJdbcTemplate.query(buffer.toString(),map,new RowMapper<HospitalWork>() {
		@Override
		public HospitalWork mapRow(ResultSet rs, int rownum)
				throws SQLException {
			HospitalWork vo=new HospitalWork();
			vo.setAverageInhost(rs.getInt("total"));
			return vo;
		}
	});
	if(list.size()>0){
		return list.get(0).getAverageInhost();
	}
	return 0;
}
@Override
public Map<String, Object> queryHosWorkTotal(String begin, String end,
		String menuAlias, String depts, String doctors, String page, String rows) {
	
	Map map=new HashMap();
	map.put("total", 0);
	map.put("rows", new ArrayList<HospitalWork>());
	
	BasicDBObject bdObject = new BasicDBObject();
	BasicDBObject bdObjectTimeS = new BasicDBObject();
	BasicDBObject bdObjectTimeE = new BasicDBObject();
	BasicDBList condList = new BasicDBList(); 
	
	BasicDBList mongoDeptList = new BasicDBList();
	BasicDBList mongoDocList = new BasicDBList();
	
	List<HospitalWork> list=new ArrayList<HospitalWork>();
	bdObjectTimeS.put("inDate",new BasicDBObject("$gte",begin));
	condList.add(bdObjectTimeS);
	bdObjectTimeE.put("inDate",new BasicDBObject("$lte",end));
	condList.add(bdObjectTimeE);
	bdObject.put("$and", condList);
	
	boolean sign=true;
	if(StringUtils.isBlank(depts)){//如果科室为空 查询授权科室
		String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
		List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
		if(deptList == null && deptList.size() == 0){//如果授权科室为空 查询当前医生工作量
			sign=false;
			bdObject.append("doctor", jobNo);
		}else{
			int len=deptList.size();
			if(len>0){
				for(int i = 0;i<len;i++){
					mongoDeptList.add(new BasicDBObject("deptCode",deptList.get(i).getDeptCode()));
				}
				bdObject.put("$or", mongoDeptList);
			}
		}
	}else{
		String[] dept= depts.split(",");
		for(String dep:dept){
			mongoDeptList.add(new BasicDBObject("deptCode",dep));
		}
		bdObject.put("$or", mongoDeptList);
	}
	
	if(StringUtils.isNotBlank(doctors)&&sign){
		String[] doctor= doctors.split(",");
		for(String doc:doctor){
			mongoDocList.add(new BasicDBObject("doctor",doc));
		}
		bdObject.put("$or", mongoDocList);
	}
	
	DBCursor cursor = new MongoBasicDao().findAlldataBySort("ZYYSGZLTJ_EFFECT_DAY", bdObject,"deptCode");
	DBObject dbCursor;
	String key;//主键
	List<Integer> value;//值
	List<Integer> valueVo;//
	List<Integer> tempValue;//中间list
	
	Map<String,List<Integer>> valueMap=new LinkedHashMap<String,List<Integer>>();
		while(cursor.hasNext()){
			 	dbCursor = cursor.next();
			 	valueVo=new ArrayList<Integer>(8);
			 	
				 String deptCode = (String) dbCursor.get("deptCode") ;//科室
				 String doctor=(String) dbCursor.get("doctor") ;//医生
				 valueVo.add((Integer)dbCursor.get("hosVisitors")) ;//住院人数
				 valueVo.add((Integer)dbCursor.get("outVisitors")) ;//出院
				 valueVo.add((Integer)dbCursor.get("concurVisitors")) ;//并发症
				 valueVo.add((Integer)dbCursor.get("cureVisitors") );//治愈
				 valueVo.add((Integer)dbCursor.get("unCureVisitors") );//未治愈
				 valueVo.add((Integer)dbCursor.get("betterVisitors")) ;//好转
				 valueVo.add((Integer)dbCursor.get("deathVisitors") );//死亡
				 valueVo.add((Integer)dbCursor.get("averageInhost")) ;//住院天数
				 key=deptCode+"&"+doctor;
				 
				 if(valueMap.containsKey(key)){
					 tempValue=new ArrayList<Integer>(8);
					 value=valueMap.get(key);
					 for(int i=0,len=value.size();i<len;i++){
						tempValue.add(value.get(i)+valueVo.get(i)); 
					 }
					 valueMap.put(key, tempValue);
					 tempValue=null;
				 }else{
					 valueMap.put(key, valueVo);
					 valueVo=null;
				 }
		}
		String[] keysArr=null;
		List<HospitalWork> returnList=new ArrayList<HospitalWork>();
		int beginRecoder=Integer.parseInt(rows)*(Integer.parseInt(page)-1);
		int endRecoder=Integer.parseInt(rows)*(Integer.parseInt(page));
		int total=0;
		for(String keys:valueMap.keySet()){
			total++;
			if(total>=beginRecoder &&total <endRecoder){
				keysArr=keys.split("&");
				tempValue=valueMap.get(keys);
				HospitalWork vo=new HospitalWork();
				vo.setDeptCode(keysArr[0]);
				vo.setDoctor(keysArr[1]);
				vo.setHosVisitors(tempValue.get(0));
				vo.setOutVisitors(tempValue.get(1));
				vo.setConcurVisitors(tempValue.get(2));
				vo.setCureVisitors(tempValue.get(3));
				vo.setUnCureVisitors(tempValue.get(4));
				vo.setBetterVisitors(tempValue.get(5));
				vo.setDeathVisitors(tempValue.get(6));
				vo.setAverageInhost(tempValue.get(7));
				returnList.add(vo);
			}
		}
		valueMap=null;
		tempValue=null;
	
	map.put("total", total);
	map.put("rows", returnList);
	return map;
}

}
