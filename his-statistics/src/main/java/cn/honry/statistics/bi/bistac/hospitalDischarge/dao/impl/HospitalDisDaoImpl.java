package cn.honry.statistics.bi.bistac.hospitalDischarge.dao.impl;

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

import org.bson.Document;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.hospitalDischarge.dao.HospitalDisDao;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisChargeVo;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisVo;
@Repository("hospitalDisDao")
@SuppressWarnings("all")
public class HospitalDisDaoImpl implements HospitalDisDao {
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");
	String[] inOut={"IN","OUT"};
	Map<String,String> InName=new HashMap(){{
		put("IN", "住院");
		put("OUT","出院");
	}};
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

/************************时间计算*****************************************************************************************/
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
		@Override
		public HospitalDisVo querySameOrSque(String date, String dateSign,String searchTime) {
			HospitalDisVo vo=new HospitalDisVo();
			String queryMongo="INPATIENTINOUT";
			if("1".equals(dateSign)){
				queryMongo+="_YEAR";
			}else if("2".equals(dateSign)){
				queryMongo+="_MONTH";
			}else {
				queryMongo+="_DAY";
			}
			vo.setDate(searchTime);
			BasicDBObject bdObject = new BasicDBObject();
			bdObject.append("statDate", vo.getDate());
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
			DBObject dbCursor;
			Integer tota=0;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 tota+=(Integer) dbCursor.get("num");
			}
			vo.setNum(tota);
			return vo;
		}

		@Override
		public HospitalDisVo querySameOrSque(List<String> tnL, String dateSign, String begin, String end) {
			String dateFormate;
			if("1".equals(dateSign)){
				dateFormate="yyyy";
			}else if("2".equals(dateSign)){
				dateFormate="yyyy-mm";
			}else{
				dateFormate="yyyy-mm-dd";
			}
			StringBuffer buffer=new StringBuffer(1100);
			buffer.append("select f.statDate as statDate,sum(f.con) as con from( ");
			buffer.append("select to_char(b.statDate,'"+dateFormate+"') as statDate,count(b.statDate) as con from ( ");
			buffer.append("select t.in_date as statDate from t_inpatient_info_now t where ");
			buffer.append("t.in_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.in_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("union all ");
			buffer.append("select t.in_date as statDate from t_inpatient_info t where ");
			buffer.append("t.in_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.in_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("union all ");
			buffer.append("select t.out_date as statDate from t_inpatient_info t where ");
			buffer.append("t.out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("union all ");
			buffer.append("select t.out_date as statDate from t_inpatient_info_now t where t.in_state='O' ");
			buffer.append("and t.out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			//TODO 分区查注掉
//			if(tnL!=null&&tnL.size()>0){
//				buffer.append("union all ");
//				for(int i=0,len=tnL.size();i<len;i++){
//					if(i>0){
//						buffer.append(" union all ");
//					}
//					buffer.append("select t"+i+".OUT_DATE as statDate,cast('OUT' as varchar2(2)) as stat ");
//					buffer.append("from "+tnL.get(i)+" t"+i+" ");
//					buffer.append("where t"+i+".in_state='O' ");
//					buffer.append("and t"+i+".OUT_DATE>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".OUT_DATE<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
//				}
//			}
				buffer.append(") b group by b.statDate ");
				buffer.append(") f group by f.statDate ");
				List<HospitalDisVo> list=namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<HospitalDisVo>(){
					@Override
					public HospitalDisVo mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HospitalDisVo vo=new HospitalDisVo();
						vo.setDate(rs.getString("statDate"));
						vo.setNum(rs.getInt("con"));
						return vo;
					}
				});
				if(list.size()>0){
					return list.get(0);
				}
				HospitalDisVo vo=new HospitalDisVo();
				if("1".equals(dateSign)){
					vo.setDate(begin.substring(0, 4));
				}else if("2".equals(dateSign)){
					vo.setDate(begin.substring(0, 7));
				}else{
					vo.setDate(begin.substring(0, 10));
				}
				vo.setNum(0);
				return vo;
		}

		@Override
		public List<HospitalDisVo> queryHospitalDis(String date, String dateSign) {
			List<HospitalDisVo> list=new ArrayList<HospitalDisVo>();
			for(String st:inOut){
				HospitalDisVo vo=new HospitalDisVo();
				vo.setDate(date);
				BasicDBObject bdObject = new BasicDBObject();
				bdObject.append("statDate", vo.getDate());
				bdObject.append("stat", st);
				DBCursor cursor = new MongoBasicDao().findAlldata("INPATIENTINOUT", bdObject);
				DBObject dbCursor;
				Integer tota=0;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 tota+=(Integer) dbCursor.get("num");
					 vo.setNum(tota);
					 list.add(vo);
				}
			}
			return list;
		}

		@Override
		public List<HospitalDisVo> queryFeelHos(String begin, String end) {
			List<HospitalDisVo> list=new ArrayList<HospitalDisVo>();
			BasicDBObject bdObject = new BasicDBObject();
			BasicDBObject bdObjectTimeS = new BasicDBObject();
			BasicDBObject bdObjectTimeE = new BasicDBObject();
			BasicDBList condList = new BasicDBList(); 
			bdObjectTimeS.put("statDate",new BasicDBObject("$gte",begin));
			condList.add(bdObjectTimeS);
			bdObjectTimeE.put("statDate",new BasicDBObject("$lte",end));
			condList.add(bdObjectTimeE);
			bdObject.put("$and", condList);
			for(String st:inOut){
				HospitalDisVo vo=new HospitalDisVo();
				bdObject.append("stat", st);
				DBCursor cursor = new MongoBasicDao().findAlldata("INPATIENTINOUT_DAY", bdObject);
				DBObject dbCursor;
				Integer tota=0;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 tota+=(Integer) dbCursor.get("num");
					
				}
				 vo.setStat(InName.get(st));
				 vo.setNum(tota);
				 list.add(vo);
			}
			return list;
		}
		/**
		 * 数据展示
		 */
		@Override
		public Map<String,HospitalDisVo> queryHospitalDisList(String date,String dateSign){
			Map<String,HospitalDisVo> map=new LinkedHashMap<String, HospitalDisVo>();
			String key;
			String queryMongo="INPATIENTINOUT";
			if("1".equals(dateSign)){
				queryMongo+="_YEAR";
			}else if("2".equals(dateSign)){
				queryMongo+="_MONTH";
			}else {
				queryMongo+="_DAY";
			}
			for(String st:inOut){
				key=date+"&"+InName.get(st);
				HospitalDisVo vo=new HospitalDisVo();
				vo.setDate(date);
				BasicDBObject bdObject = new BasicDBObject();
				bdObject.append("statDate", vo.getDate());
				bdObject.append("stat", st);
				DBCursor cursor = new MongoBasicDao().findAlldata(queryMongo, bdObject);
				if(!cursor.hasNext()){
					vo.setStat(InName.get(st));
					vo.setNum(0);
					map.put(key, vo);
					continue;
				}
				DBObject dbCursor;
				Integer tota=0;
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 tota+=(Integer) dbCursor.get("num");
				}
				 vo.setStat(InName.get(st));
				 vo.setNum(tota);
				 map.put(key, vo);
		}
		return map;
		
		}

		@Override
		public List<HospitalDisVo> queryHospitalDisList(
				List<String> tnL, String begin, String end) {
			StringBuffer buffer=new StringBuffer(1100);
			buffer.append("select f.stat as stat,sum(f.con) as con from( ");
			buffer.append("select b.stat as stat,count(b.stat) as con from ( ");
			buffer.append("select cast('IN' as varchar2(2)) as stat from t_inpatient_info_now t where ");
			buffer.append(" t.in_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.in_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("union all ");
			buffer.append("select cast('IN' as varchar2(2)) as stat from t_inpatient_info t where ");
			buffer.append("t.in_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.in_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("union all ");
			buffer.append("select cast('OUT' as varchar2(3)) as stat from t_inpatient_info t where ");
			buffer.append("t.out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("union all ");
			buffer.append("select cast('OUT' as varchar2(3)) as stat from t_inpatient_info_now t where t.in_state='O' ");
			buffer.append("and t.out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t.out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append(") b group by  b.stat ");
			buffer.append(") f group by  f.stat order by stat");
			List<HospitalDisVo> list=namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<HospitalDisVo>(){
				@Override
				public HospitalDisVo mapRow(ResultSet rs, int arg1)
						throws SQLException {
					HospitalDisVo vo=new HospitalDisVo();
					vo.setStat(rs.getString("stat"));
					vo.setNum(rs.getInt("con"));
					return vo;
				}
			});
			if(list.size()==0){
				HospitalDisVo in=new HospitalDisVo();
				in.setStat("住院");
				in.setNum(0);
				HospitalDisVo out=new HospitalDisVo();
				out.setStat("出院");
				out.setNum(0);
				list.add(in);
				list.add(out);
			}else if(list.size()==1){
				HospitalDisVo vo=new HospitalDisVo();
				if("IN".equals(list.get(0).getStat())){
					vo.setNum(0);
					vo.setStat("出院");
					list.add(vo);
				}else{
					vo.setNum(0);
					vo.setStat("住院");
					list.add(0,vo);
				}
				
				
			}
			return list;
		}

		@Override
		public HospitalDisChargeVo queryInOutList(String begin, String end) {
			StringBuffer buffer=new StringBuffer();
			buffer.append("select sum(nvl(t.RY_NUM,0)) as inHost,sum(nvl(t.CYWJ_NUM,0)+nvl(t.CYYJ_NUM,0)) outHost from t_business_yzcx_zhcx t ");
			buffer.append("where t.yq=0 and t.oper_date>=to_date('"+begin+"','yyyy-mm-dd') and t.oper_date<=to_date('"+end+"','yyyy-mm-dd')");
			List<HospitalDisChargeVo> list=namedParameterJdbcTemplate.query(buffer.toString(),new RowMapper<HospitalDisChargeVo>(){
				@Override
				public HospitalDisChargeVo mapRow(ResultSet rs, int arg1)
						throws SQLException {
					HospitalDisChargeVo vo=new HospitalDisChargeVo();
					vo.setInHost(rs.getInt("inHost"));
					vo.setOutHost(rs.getInt("outHost"));
					return vo;
				}
			});
			if(list.size()>0){
				return list.get(0);
			}
			return new HospitalDisChargeVo();
		}
		
		@Override
		public List<HospitalDisVo> queryFeelHos(List<String> tnL, String begin,
				String end) {
			// TODO Auto-generated method stub
			return null;
		}
}
