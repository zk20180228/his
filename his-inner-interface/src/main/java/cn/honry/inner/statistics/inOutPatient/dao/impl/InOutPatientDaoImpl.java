package cn.honry.inner.statistics.inOutPatient.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.inOutPatient.dao.InOutPatientDao;
import cn.honry.inner.statistics.inOutPatient.vo.InOutPatient;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
@Repository("inOutPatientDao")
@SuppressWarnings("all")
public class InOutPatientDaoImpl implements InOutPatientDao{
	private final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	private final String[] inpatientInfo={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};//住院主表
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}

	@Override
	public void initZCYRCTJ(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		
		StringBuffer buffer=new StringBuffer(1100);
		String begin=date+" 00:00:00";//开始时间
		String end=date+" 23:59:59";//结束时间
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientInfo, "ZY");
		if(tnL!=null&&tnL.size()>0){
		buffer.append("select f.statDate as statDate,f.stat as stat,sum(f.con) as con from( ");
		buffer.append("select to_char(b.statDate,'yyyy-mm-dd') as statDate,b.stat as stat,count(b.statDate) as con from ( ");
//		
//		buffer.append("select t.in_date as statDate,cast('IN' as varchar2(2)) as stat from t_inpatient_info_now t where ");
//		buffer.append(" t.in_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.in_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
//		buffer.append("union all ");
//		buffer.append("select t.in_date as statDate,cast('IN' as varchar2(2)) as stat from t_inpatient_info t where ");
//		buffer.append("t.in_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.in_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
//		buffer.append("union all ");
//		buffer.append("select t.out_date as statDate,cast('OUT' as varchar2(3)) as stat from t_inpatient_info t where ");
//		buffer.append("t.out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
//		buffer.append("union all ");
//		buffer.append("select t.out_date as statDate,cast('OUT' as varchar2(3)) as stat from t_inpatient_info_now t where t.in_state='O' ");
//		buffer.append("and t.out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select t"+i+".IN_DATE as statDate,cast('IN' as varchar2(2)) as stat ");
			buffer.append("from "+tnL.get(i)+" t"+i+" ");
			buffer.append("where ");
			buffer.append("t"+i+".IN_DATE>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".IN_DATE<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		}
		//TODO 分区查注掉
			buffer.append("union all ");
			for(int i=0,len=tnL.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append("select t"+i+".OUT_DATE as statDate,cast('OUT' as varchar2(3)) as stat ");
				buffer.append("from "+tnL.get(i)+" t"+i+" ");
				buffer.append("where t"+i+".in_state='O' ");
				buffer.append("and t"+i+".OUT_DATE>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".OUT_DATE<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			}
			
			buffer.append(") b group by b.statDate, b.stat ");
			buffer.append(") f group by f.statDate,f.stat ");
			List<InOutPatient> list=namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<InOutPatient>(){
				@Override
				public InOutPatient mapRow(ResultSet rs, int arg1)
						throws SQLException {
					InOutPatient vo=new InOutPatient();
					vo.setDate(rs.getString("statDate"));
					vo.setStat(rs.getString("stat"));
					vo.setNum(rs.getInt("con"));
					return vo;
				}
			});
			DBObject query = new BasicDBObject();
			query.put("statDate", date);//移除数据条件
			new MongoBasicDao().remove("INPATIENTINOUT_DAY", query);//删除原来的数据
			
			if(list.size()>0){
				for(InOutPatient vo:list){
					if(vo.getNum()!=null){
						Document doucment1=new Document();
						doucment1.append("statDate", vo.getDate());
						doucment1.append("stat", vo.getStat());
						Document document = new Document();
						document.append("statDate", vo.getDate());
						document.append("stat", vo.getStat());
						document.append("num", vo.getNum());
						new MongoBasicDao().update("INPATIENTINOUT_DAY", doucment1, document, true);
						
					}
				}
				if(!"HIS".equals(type)){
					initZCYRCTJMoreDay("INPATIENTINOUT", type, date);
				}
			}
			wordLoadDocDao.saveMongoLog(beginDate, menuAlias, list, date);
		}	
	}

	@Override
	public void initZCYRCTJMoreDay(String menuAlias, String type, String date) {
		Date beginDate=new Date();
		BasicDBObject bdObject = new BasicDBObject();
		
		Map<String,Integer> map=new HashMap<String,Integer>();
		String temp;//月数据
		String queryMongo=null;
		String saveMongo=null;
		Integer nu;
		String begin;
		
		String end;
		String temp1;//key
		if("2".equals(type)){
			//计算最后一个月日期
			temp=date.substring(0,7);
			begin=temp+"-01";//开始时间
			end=returnEndTime(date);
			queryMongo=menuAlias+"_DAY";
			saveMongo=menuAlias+"_MONTH";
		}else{
			temp=date.substring(0,4);
			begin=temp+"-01";
			end=temp+"-12";
			queryMongo=menuAlias+"_MONTH";
			saveMongo=menuAlias+"_YEAR";
		}
				
				BasicDBList dateList=new BasicDBList();
				BasicDBObject data1= new BasicDBObject();//查询开始时间
				BasicDBObject data2= new BasicDBObject();//查询结束时间
			    data1.append("statDate", new BasicDBObject("$gte",begin));
			    data2.append("statDate", new BasicDBObject("$lte",end));
			    dateList.add(data1);
			    dateList.add(data2);
			    bdObject.put("$and", dateList);
				DBCursor cursor=new MongoBasicDao().findAlldata(queryMongo, bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 Integer value = (Integer) dbCursor.get("num") ;//总量
					 String stat =(String)dbCursor.get("stat");//出院住院
			        temp1=temp+"&"+stat;
			    if(map.containsKey(temp1)){//如果key存在 比较name
						nu=map.get(temp1);
						nu+=value;
						map.put(temp1, nu);
				 }else{//如果key不存在   添加到map1中
					 map.put(temp1, value);
				    }
				   }
		DBObject query = new BasicDBObject();
		query.put("statDate", date);//移除数据条件
		new MongoBasicDao().remove(saveMongo, query);//删除原来的数据
		String[] strKey;
		int i=0;
		for(String key:map.keySet()){
			  strKey=key.split("&");
			  if(strKey.length==2){
				  Document doucment1=new Document();
					doucment1.append("statDate", strKey[0]);
					doucment1.append("stat",  strKey[1]);
					Document document = new Document();
					document.append("statDate", strKey[0]);
					document.append("stat",  strKey[1]);
					document.append("num",  map.get(key));
					new MongoBasicDao().update(saveMongo, doucment1, document, true);  
					i++;
			  }
			}
		wordLoadDocDao.saveMongoLog(beginDate, menuAlias, new ArrayList<String>(i), date);
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
