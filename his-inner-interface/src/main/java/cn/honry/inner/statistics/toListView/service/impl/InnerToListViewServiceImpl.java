package cn.honry.inner.statistics.toListView.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.toListView.dao.InnerToListViewDao;
import cn.honry.inner.statistics.toListView.service.InnerToListViewService;
import cn.honry.inner.statistics.toListView.vo.ToListViewVo;
import cn.honry.inner.statistics.totalIncomeCount.vo.MapVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Transactional
@Service("innerToListViewService")
//@SuppressWarnings({"all"})
public class InnerToListViewServiceImpl implements InnerToListViewService {
	@Autowired
	@Qualifier(value = "innerToListViewDao")
	private InnerToListViewDao innerToListViewDao; 
	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjDao")
	private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
	
	/**
	 * 将门急诊统计数据存入mongodb中
	 * @param menuAlias
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @param date 开始时间(格式为:YYYY-MM-DD)
	 */
	@Override
	public void init(String menuAlias, String type, String date) {
		//根据日期获取当天的统计数据,存入mongodb中
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(new Date());
		ToListViewVo vo = innerToListViewDao.queryVo(date);
		List<MapVo> pieVoList = innerToListViewDao.queryPieVO(date);
		int outpatientLastM = 0;
		int emergencyLastM = 0;
		int outpatientLastY = 0;
		int emergencyLastY = 0;
		String dateM = DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date), -1));
		String dateY = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date), -1));
		BasicDBObject where = new BasicDBObject();
		where.put("date", dateM);
		DBCursor cursorM = new MongoBasicDao().findAlldata(menuAlias+"_DAY", where);
		if(cursorM.hasNext()){
			Object object = cursorM.next().get("value");
			String viewVoString = object.toString();
			int intO = viewVoString.indexOf("outpatientD");
			int intE = viewVoString.indexOf("emergencyD");
			outpatientLastM = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intO)+1,viewVoString.indexOf(",", intO)));
			emergencyLastM = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intE)+1,viewVoString.indexOf(",", intE)));
//			JSONObject jsonObject = JSONObject.fromObject(object.toString());
//			outpatientLastM = Integer.parseInt(jsonObject.get("outpatientD").toString());
//			emergencyLastM = Integer.parseInt(jsonObject.get("emergencyD").toString());
		}
		vo.setOutpatientLastM(outpatientLastM);
		vo.setEmergencyLastM(emergencyLastM);
		where.clear();
		where.put("date", dateY);
		DBCursor cursorY = new MongoBasicDao().findAlldata(menuAlias+"_DAY", where);
		if(cursorY.hasNext()){
			Object object = cursorY.next().get("value");
			String viewVoString = object.toString();
			int intO = viewVoString.indexOf("outpatientD");
			int intE = viewVoString.indexOf("emergencyD");
			outpatientLastY = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intO)+1,viewVoString.indexOf(",", intO)));
			emergencyLastY = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intE)+1,viewVoString.indexOf(",", intE)));
//			JSONObject jsonObject = JSONObject.fromObject(object.toString());
//			outpatientLastY = Integer.parseInt(jsonObject.get("outpatientD").toString());
//			emergencyLastY = Integer.parseInt(jsonObject.get("emergencyD").toString());
		}
		vo.setOutpatientLastY(outpatientLastY);
		vo.setEmergencyLastY(emergencyLastY);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("viewVo", vo);
		map.put("areaList", pieVoList);
		String json = JSONUtils.toJson(map);
		Document document = new Document();
		document.append("date", date);
		document.append("value", json);
		
		DBObject query = new BasicDBObject();
		query.put("date", date);
		new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
		new MongoBasicDao().insertData(menuAlias+"_DAY", document);//添加新数据
		Date d = DateUtils.parseDateY_M_D(date);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias+"_DAY");
		mong.setCountEndTime(new Date());
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());
		innerRegisterInfoGzltjDao.save(mong);//保存日志
		
		if(!"HIS".equals(type)){
			//更新当月的统计数据
			init_MonthOrYear(menuAlias, "2", date);
			//更新当年的统计数据
			init_MonthOrYear(menuAlias, "3", date);
		}
	}
	/**
	 * 计算当月或当年的数据
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 开始时间
	 */
	@Override
	public void init_MonthOrYear(String menuAlias,String type,String date){
		ToListViewVo vo = new ToListViewVo();
		int outpatientD = 0;
		int emergencyD = 0;
		int outpatientLastM = 0;
		int emergencyLastM = 0;
		int outpatientLastY = 0;
		int emergencyLastY = 0;
		
		List<MapVo> areaList = new ArrayList<MapVo>();
		
		//更新当月或当年的统计数据(从_DAY或_Month表中查询数据,重新计算)
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(new Date());
		String dateM=null;
		String dateY=null;
		if("2".equals(type)){
			 dateM = date.substring(0, 7);//当月
		}else{
			dateY = date.substring(0, 4);//当年
		}
		BasicDBObject obj1= new BasicDBObject();
		BasicDBObject obj2= new BasicDBObject();
		
		String queryMenuAlias=menuAlias;//查询时用的表名称
		if("2".equals(type)){//按月统计
			obj1.append("date", new BasicDBObject("$gte",dateM+"-01"));//当月的1号
			obj2.append("date", new BasicDBObject("$lte",DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.addMonth(DateUtils.parseDateY_M_D(dateM+"-01"), 1), -1))));//当月的最后一天
			queryMenuAlias+="_DAY";//按月统计时,从日表中查询数据
		}
		if("3".equals(type)){//按年统计
			obj1.append("date", new BasicDBObject("$gte",dateY+"-01"));//当年的1月
			obj2.append("date", new BasicDBObject("$lte",dateY+"-12"));//当年的12月
			queryMenuAlias+="_MONTH";//按年统计时,从月表中查询数据
		}
		BasicDBList condList = new BasicDBList(); 
		condList.add(obj1);
		condList.add(obj2);
		BasicDBObject where= new BasicDBObject();
		where.append("$and", condList);
		
		DBCursor cursor = new MongoBasicDao().findAlldata(queryMenuAlias, where);
		
		//只需要把天的数据加起来就可以了。
		while(cursor.hasNext()){
			Object object = cursor.next().get("value");
			
			/*if(object!=null){
    			String objectS=object.toString();
    			if(objectS.contains("viewVo\":{")){
    				s = objectS.substring(objectS.indexOf("viewVo\":{"), objectS.length());
    				s = s.substring(s.indexOf("{"), s.indexOf("}")+1);
    				int i = objectS.indexOf("areaOf");
    				areaString = objectS.substring(objectS.indexOf("[",i), objectS.indexOf("]",i)+1);
//    				areaString = areaString.substring(areaString.indexOf("["), areaString.indexOf("]")+1);
    			}else{
    				s = objectS;
    			}
    		}*/
			
//			JSONObject jsonObject = JSONObject.fromObject(object.toString());
			String viewVoString = object.toString();
			int intO = viewVoString.indexOf("outpatientD");
			int intE = viewVoString.indexOf("emergencyD");
			//累加总诊人数
			outpatientD += Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intO)+1,viewVoString.indexOf(",", intO)));
			//累加急诊人数
			emergencyD += Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intE)+1,viewVoString.indexOf(",", intE)));
			int i = viewVoString.indexOf("areaOf");
			String areaString = viewVoString.substring(viewVoString.indexOf("[",i), viewVoString.indexOf("]",i)+1);
			if(StringUtils.isNotBlank(areaString)){
				try {
					List<MapVo> pieList = JSONUtils.fromJson(areaString, new TypeToken<List<MapVo>>(){});
					areaList.addAll(pieList);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		
		
		
		//累加院区
		Map<String,Double> areaAllMap = new HashMap<String, Double>();
		for(MapVo pv : areaList){
			String areaName = pv.getName();
			Double areaValue = pv.getValue();
			if(areaAllMap.get(areaName)==null){
				areaAllMap.put(areaName, areaValue);
			}else{
				Double mapValue = areaAllMap.get(areaName);
				areaAllMap.put(areaName, mapValue+areaValue);
			}
		}
		List<MapVo> pieVoList = new ArrayList<MapVo>();
		JSONUtils.toJson(areaAllMap);
		for(String key : areaAllMap.keySet()){
			MapVo mapPieVo = new MapVo();
			mapPieVo.setName(key);
			mapPieVo.setValue(areaAllMap.get(key));
			pieVoList.add(mapPieVo);
		}
//		JSONUtils.toJson(pieVoList);
		
		
		vo.setOutpatientD(outpatientD);
		vo.setEmergencyD(emergencyD);
		
		DBObject query = new BasicDBObject();
		Document document = new Document();
		Date d = null;
		if("2".equals(type)){
			menuAlias+="_MONTH";
			BasicDBObject whereM = new BasicDBObject();
			whereM.put("date", DateUtils.formatDateY_M(DateUtils.addMonth(DateUtils.parseDateY_M_D(dateM+"-01"),-1)));
			DBCursor cursorM = new MongoBasicDao().findAlldata(menuAlias, whereM);
			if(cursorM.hasNext()){
				Object object = cursorM.next().get("value");
//				JSONObject jsonObject = JSONObject.fromObject(object.toString());
				String viewVoString = object.toString();
				int intO = viewVoString.indexOf("outpatientD");
				int intE = viewVoString.indexOf("emergencyD");
				outpatientLastM = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intO)+1,viewVoString.indexOf(",", intO)));
				emergencyLastM = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intE)+1,viewVoString.indexOf(",", intE)));
//				String viewVoString = object.toString();
//				outpatientD += Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intO)+1,viewVoString.indexOf(",", intO)));
//				emergencyD += Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intE)+1,viewVoString.indexOf(",", intE)));
			}
			vo.setOutpatientLastM(outpatientLastM);
			vo.setEmergencyLastM(emergencyLastM);
			whereM.clear();
			whereM.put("date", DateUtils.formatDateY_M(DateUtils.addYear(DateUtils.parseDateY_M_D(dateM+"-01"),-1)));
			DBCursor cursorY = new MongoBasicDao().findAlldata(menuAlias, whereM);
			if(cursorY.hasNext()){
				Object object = cursorY.next().get("value");
				String viewVoString = object.toString();
				int intO = viewVoString.indexOf("outpatientD");
				int intE = viewVoString.indexOf("emergencyD");
				outpatientLastY = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intO)+1,viewVoString.indexOf(",", intO)));
				emergencyLastY = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intE)+1,viewVoString.indexOf(",", intE)));
//				JSONObject jsonObject = JSONObject.fromObject(object.toString());
//				outpatientLastY = Integer.parseInt(jsonObject.get("outpatientD").toString());
//				emergencyLastY = Integer.parseInt(jsonObject.get("emergencyD").toString());
			}
			vo.setOutpatientLastY(outpatientLastY);
			vo.setEmergencyLastY(emergencyLastY);
			document.append("date", dateM);
//			document.append("value", JSONUtils.toJson(vo));
			query.put("date", dateM);
			d=DateUtils.parseDateY_M(dateM);
		}
		if("3".equals(type)){
			menuAlias+="_YEAR";
			BasicDBObject whereM = new BasicDBObject();
			whereM.put("date", String.valueOf((Integer.parseInt(dateY)-1)));
			DBCursor cursorM = new MongoBasicDao().findAlldata(menuAlias, whereM);
			if(cursorM.hasNext()){
				Object object = cursorM.next().get("value");
				String viewVoString = object.toString();
				int intO = viewVoString.indexOf("outpatientD");
				int intE = viewVoString.indexOf("emergencyD");
				outpatientLastM = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intO)+1,viewVoString.indexOf(",", intO)));
				emergencyLastM = Integer.parseInt(viewVoString.substring(viewVoString.indexOf(":", intE)+1,viewVoString.indexOf(",", intE)));
//				JSONObject jsonObject = JSONObject.fromObject(object.toString());
//				outpatientLastM = Integer.parseInt(jsonObject.get("outpatientD").toString());
//				emergencyLastM = Integer.parseInt(jsonObject.get("emergencyD").toString());
			}
			vo.setOutpatientLastM(outpatientLastY);
			vo.setEmergencyLastM(emergencyLastY);
			
			vo.setOutpatientLastY(outpatientLastM);
			vo.setEmergencyLastY(emergencyLastM);
			
			document.append("date", dateY);
			query.put("date", dateY);
			d=DateUtils.parseDateY(dateY);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("viewVo", vo);
		map.put("areaList", pieVoList);
		String json = JSONUtils.toJson(map);
		document.append("value", json);
		new MongoBasicDao().remove(menuAlias, query);//删除原来的数据
		new MongoBasicDao().insertData(menuAlias, document);//添加新数据
		
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias);
		mong.setCountEndTime(new Date());
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());
		innerRegisterInfoGzltjDao.save(mong);//保存日志
	}
}
