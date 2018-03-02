package cn.honry.statistics.bi.bistac.toListView.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.toListView.dao.ToListViewDao;
import cn.honry.statistics.bi.bistac.toListView.service.ToListViewService;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListView;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListViewVo;
import cn.honry.statistics.util.ResultUtils;
import cn.honry.statistics.util.echartsVo.PieVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;

@Service("toListViewService")
@Transactional
@SuppressWarnings({"all"})
public class ToListViewServiceImpl implements ToListViewService{
	
	
	@Autowired
	@Qualifier(value = "toListViewDao")
	private ToListViewDao toListViewDao;
	
	/**  
	 * 
	 * 门急诊人次统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 下午2:38:18 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 下午2:38:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ToListViewVo queryVo(String date) {
		//获取当前表最大时间及最小时间
		ToListViewVo vo = toListViewDao.findMaxMin();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<String> tnL = new ArrayList<String>();
		try {
			Date sTime = format.parse(date);
			Date eTime = format.parse(date);
			tnL= new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, vo.getsTime())==-1){
				if(DateUtils.compareDate(eTime, vo.getsTime())==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",date,date);
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_REGISTER_MAIN_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return toListViewDao.queryVo(tnL,date, this.getLastMonth(date), this.getLastYear(date));
	}
	/**  
	 * 
	 * 封装求上一年同月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastYear(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.YEAR, -1);
        Date d = calendar.getTime();
        //上年
        String lastDate = format.format(d);
		return lastDate;
	}
	/**  
	 * 
	 * 封装求上一月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastMonth(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.MONTH, -1);
        Date dd = calendar.getTime();
        //上月
        String lastM = format.format(dd);
		return lastM;
	}
	@Override
	public ToListViewVo queryVoByMongo(String date) {
		return toListViewDao.queryVoByMongo(date, this.getLastMonth(date), this.getLastYear(date));
	}
	/**
	 * elasticsearch实现
	 * @param date
	 * @return
	 */
	@Override
	public ToListViewVo queryVoByES(String date, String staType) {
		Date thisDay = null;
		Date nextDay = null;
		try{
			switch (Integer.parseInt(staType)) {
			case 1://按天
				thisDay = DateUtils.parseDateY_M_D(date);
				nextDay = DateUtils.addDay(thisDay, 1);
				break;
			case 2://按月
				if(date.length()==7){
					thisDay = DateUtils.parseDateY_M(date);
				}else{
					thisDay = DateUtils.parseDateY_M_D(date);
				}
				nextDay = DateUtils.addDay(thisDay, 1);
				break;
			case 3://按年
				if(date.length()==4){
					thisDay = DateUtils.parseDateY(date);
				}else{
					thisDay = DateUtils.parseDateY_M_D(date);
				}
				nextDay = DateUtils.addDay(thisDay, 1);
				break;
			case 4://自定义
				return toListViewDao.queryVoByES(DateUtils.parseDateY_M_D(date.split(",")[0]),DateUtils.parseDateY_M_D(date.split(",")[1]),staType);
			case 5://环比按月
				if(date.length()==7){
					thisDay = DateUtils.parseDateY_M(date);
				}else{
					thisDay = DateUtils.parseDateY_M_D(date);
				}
				nextDay = DateUtils.addDay(thisDay, 1);
				break;
			case 6://同比按月
				thisDay = DateUtils.parseDateY_M(date);
				nextDay = DateUtils.addDay(thisDay, 1);
				break;

			default:
				thisDay = DateUtils.parseDateY_M_D(date);
				nextDay = DateUtils.addDay(thisDay, 1);
				break;
			}
		}catch(Exception e){
			System.out.println("==========="+date);
			e.printStackTrace();
		}
		
		return toListViewDao.queryVoByES(thisDay,nextDay,staType);
	}
	@Override
	public List<ToListView> querySixMomYoy(String date, String staType) {
		List<ToListView> toList = Collections.synchronizedList(new ArrayList<ToListView>());
		ToListView tlvMom = new ToListView();
		ToListView tlvYoy = new ToListView();
		ToListViewVo vo1 = new ToListViewVo();
		ToListViewVo vo2 = new ToListViewVo();
		ToListViewVo vo3 = new ToListViewVo();
		Date thisDay = null;
		switch (staType) {
		case "1":
			thisDay=DateUtils.parseDateY_M_D(date);;
			break;
		case "2":
			thisDay=DateUtils.parseDateY_M(date);;
			break;
		case "3":
			thisDay=DateUtils.parseDateY(date);
			break;
		}
		Date nextDay = null;
		String[] tbArr=new String[6];//存放同比日期
		String[] hbArr=new String[6];//存放环比日期
		String[] yArr=new String[6];//存放同比人次
		String[] dArr=new String[6];//存放环比人次
		for (int i = 0; i < 6; i++) {
			Date day= null;
			String d="";
			switch (staType) {
			case "1":
				day=DateUtils.addDay(thisDay,-i);
				d = DateUtils.formatDateY_M_D(day);
				break;
			case "2":
				day=DateUtils.addMonth(thisDay,-i);
				d = DateUtils.formatDateY_M(day);
				break;
			case "3":
				day=DateUtils.addYear(thisDay, -i);
				d = DateUtils.formatDateY(day);
				break;
			}
			hbArr[i]=d;
			Map<String, Object> tlvMap = queryVoByMongo(d, staType);
			ToListViewVo vo = (ToListViewVo) tlvMap.get("viewVo");
			Integer outpatientD = vo.getOutpatientD()==null?0:vo.getOutpatientD();
			dArr[i]=outpatientD.toString();
			if(!"3".equals(staType)){
				Date year = DateUtils.addYear(thisDay,-i);
				String y = "";
				if("1".equals(staType)){
					y = DateUtils.formatDateY_M_D(year);
				}else{
					y = DateUtils.formatDateY_M(year);
				}
				tbArr[i]=y;
				Map<String, Object> tlvMap1 = queryVoByMongo(y, staType);
				ToListViewVo viewVo = (ToListViewVo) tlvMap1.get("viewVo");
				Integer outpatientD2 = viewVo.getOutpatientD()==null?0:viewVo.getOutpatientD();
				yArr[i] = outpatientD2.toString();
			}
		}
		tlvMom.setNowMJNum(dArr[0]);
		tlvMom.setNowMJNumB1(dArr[1]);
		tlvMom.setNowMJNumB2(dArr[2]);
		tlvMom.setNowMJNumB3(dArr[3]);
		tlvMom.setNowMJNumB4(dArr[4]);
		tlvMom.setNowMJNumB5(dArr[5]);
		tlvMom.setNowTime(hbArr[0]);
		tlvMom.setNowTimeB1(hbArr[1]);
		tlvMom.setNowTimeB2(hbArr[2]);
		tlvMom.setNowTimeB3(hbArr[3]);
		tlvMom.setNowTimeB4(hbArr[4]);
		tlvMom.setNowTimeB5(hbArr[5]);
		if(!"3".equals(staType)){
			tlvYoy.setNowMJNum(yArr[0]);
			tlvYoy.setNowMJNumB1(yArr[1]);
			tlvYoy.setNowMJNumB2(yArr[2]);
			tlvYoy.setNowMJNumB3(yArr[3]);
			tlvYoy.setNowMJNumB4(yArr[4]);
			tlvYoy.setNowMJNumB5(yArr[5]);
			tlvYoy.setNowTime(tbArr[0]);
			tlvYoy.setNowTimeB1(tbArr[1]);
			tlvYoy.setNowTimeB2(tbArr[2]);
			tlvYoy.setNowTimeB3(tbArr[3]);
			tlvYoy.setNowTimeB4(tbArr[4]);
			tlvYoy.setNowTimeB5(tbArr[5]);
		}
		
		toList.add(tlvMom); 
		toList.add(tlvYoy);
		return toList;
	}
	
	/**
	 * 从mongodb中获取门急诊人次统计的数据
	 * @param date 日期
	 * @param dateSign 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * @return
	 */
	public Map<String, Object> queryVoByMongo(String date,String dateSign){
		Map<String, Object> toListViewMap = new HashMap<String, Object>();
		if("4".equals(dateSign)){
			return queryCostom(date);
		}
		String key="MJZRCTJ_";
    	BasicDBObject where = new BasicDBObject();
    	where.append("date", date);
    	switch (dateSign) {
		case "1":
			key+="DAY";
			break;
		case "2":
			key+="MONTH";
			break;
		case "3":
			key+="YEAR";
			break;

		default:
			break;
		}
    	String s="";
    	String areaString = "";
    	DBCursor cursor = new MongoBasicDao().findAlldata(key, where);
    	while(cursor.hasNext()){
    		Object object = cursor.next().get("value");
    		if(object!=null){
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
    		}
    	}
		try {
			ToListViewVo viewVo = JSONUtils.fromJson(s, ToListViewVo.class);
			if(viewVo==null){
				viewVo=new ToListViewVo();
			}
			toListViewMap.put("viewVo", viewVo);
			toListViewMap.put("areaString", areaString);
			return toListViewMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, Object>();
	}
	/**
	 * 门急诊人次统计--自定义统计
	 * @param date 日期
	 * 开始时间和结束时间之间有一整月的数据则按月查询,有整年的数据则按年查询;
	 * 例如:查询 2015-03-16至2017-02-05 的数据:
	 * 2015-03-16至2015-03-31之间的数据按天查询,从日表(SRTJB_DAY)中获取;
	 * 2015-04-01至2015-12-31之间有整月的数据(4月、5月、6月、...、12月),从月表(SRTJB_MONTH)中获取;
	 * 2016-01-01至2016-12-31之间有整年的数据(2016年),从年表(SRTJB_YEAR)中获取;
	 * 2017-01-01至2017-01-31之间有整月的数据(1月),从月表(SRTJB_MONTH)中获取;
	 * 2017-02-01至2017-02-05之间的数据按天查询,从日表(SRTJB_DAY)中获取.
	 * @return
	 */
	public Map<String, Object> queryCostom(String date){
		Map<String, Object> toListViewMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(date)){
			return toListViewMap;
		}
		String[] dates = date.split(",");
		//传入开始时间，截止时间，解析时间，返回各时间组合
		Map<String, List<String>> dateMap = ResultUtils.getDate(dates[0], dates[1]);
		List<String> dayList = dateMap.get("day");//按日统计的list
		List<ToListViewVo> list = new ArrayList<ToListViewVo>();
		List<PieVo> areaList = new ArrayList<PieVo>();//院区list
		if(dayList.size()>0){
			for(String dateD : dayList){
				Map<String, Object> tlvMap = queryVoByMongo(dateD,"1");
				ToListViewVo tlv = (ToListViewVo) tlvMap.get("viewVo");
				String areaString = (String) tlvMap.get("areaString");
				if(StringUtils.isNotBlank(areaString)){
					try {
						List<PieVo> pieList = JSONUtils.fromJson(areaString, new TypeToken<List<PieVo>>(){});
						areaList.addAll(pieList);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				if(tlv!=null){
					list.add(tlv);
				}
			}
		}
		List<String> monthList = dateMap.get("month");//按月统计的list
		if(monthList.size()>0){
			for (String dateM : monthList) {
				Map<String, Object> tlvMap = queryVoByMongo(dateM,"2");
				ToListViewVo tlv = (ToListViewVo) tlvMap.get("viewVo");
				String areaString = (String) tlvMap.get("areaString");
				if(StringUtils.isNotBlank(areaString)){
					try {
						List<PieVo> pieList = JSONUtils.fromJson(areaString, new TypeToken<List<PieVo>>(){});
						areaList.addAll(pieList);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//				ToListViewVo tlv = queryVoByMongo(dateM,"2");
				if(tlv!=null){
					list.add(tlv);
				}
			}
		}
		List<String> yearList = dateMap.get("year");//按年统计的list
		if(yearList.size()>0){
			for (String dateY : yearList) {
				Map<String, Object> tlvMap = queryVoByMongo(dateY,"3");
				ToListViewVo tlv = (ToListViewVo) tlvMap.get("viewVo");
				String areaString = (String) tlvMap.get("areaString");
				if(StringUtils.isNotBlank(areaString)){
					try {
						List<PieVo> pieList = JSONUtils.fromJson(areaString, new TypeToken<List<PieVo>>(){});
						areaList.addAll(pieList);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//				ToListViewVo tlv = queryVoByMongo(dateY,"3");
				//年没有同比。所以年里面环比字段应为0
				tlv.setOutpatientLastY(0);
				tlv.setEmergencyLastY(0);
				if(tlv!=null){
					list.add(tlv);
				}
			}
		}
		ToListViewVo tlvTotal = new ToListViewVo();
		if(list.size()>0){
			for(ToListViewVo tlvEach : list){
				tlvTotal.setOutpatientD((tlvTotal.getOutpatientD()==null?0:tlvTotal.getOutpatientD())+(tlvEach.getOutpatientD()==null?0:tlvEach.getOutpatientD()));
				tlvTotal.setOutpatientLastM((tlvTotal.getOutpatientLastM()==null?0:tlvTotal.getOutpatientLastM())+(tlvEach.getOutpatientLastM()==null?0:tlvEach.getOutpatientLastM()));
				tlvTotal.setOutpatientLastY((tlvTotal.getOutpatientLastY()==null?0:tlvTotal.getOutpatientLastY())+(tlvEach.getOutpatientLastY()==null?0:tlvEach.getOutpatientLastY()));
				tlvTotal.setEmergencyD((tlvTotal.getEmergencyD()==null?0:tlvTotal.getEmergencyD())+(tlvEach.getEmergencyD()==null?0:tlvEach.getEmergencyD()));
				tlvTotal.setEmergencyLastM((tlvTotal.getEmergencyLastM()==null?0:tlvTotal.getEmergencyLastM())+(tlvEach.getEmergencyLastM()==null?0:tlvEach.getEmergencyLastM()));
				tlvTotal.setEmergencyLastY((tlvTotal.getEmergencyLastY()==null?0:tlvTotal.getEmergencyLastY())+(tlvEach.getEmergencyLastY()==null?0:tlvEach.getEmergencyLastY()));
			}
		}
		Map<String,Double> areaAllMap = new HashMap<String, Double>();
		for(PieVo pv : areaList){
			String areaName = pv.getName();
			Double areaValue = pv.getValue();
			if(areaAllMap.get(areaName)==null){
				areaAllMap.put(areaName, areaValue);
			}else{
				Double mapValue = areaAllMap.get(areaName);
				areaAllMap.put(areaName, mapValue+areaValue);
			}
		}
		List<PieVo> pieVoList = new ArrayList<PieVo>();
		JSONUtils.toJson(areaAllMap);
		for(String key : areaAllMap.keySet()){
			PieVo mapPieVo = new PieVo();
			mapPieVo.setName(key);
			mapPieVo.setValue(areaAllMap.get(key));
			pieVoList.add(mapPieVo);
		}
		JSONUtils.toJson(pieVoList);
		toListViewMap.put("viewVo", tlvTotal);
		toListViewMap.put("areaString", JSONUtils.toJson(pieVoList));
		return toListViewMap;
		
	}
	@Override
	public List<PieVo> queryAreaByES(String date, String staType) {
		Date sTime = null;
		Date eTime = null;
		switch (staType) {
		case "1":
			sTime = DateUtils.parseDateY_M_D(date);
			eTime = DateUtils.addDay(DateUtils.parseDateY_M_D(date),1);
			break;
		case "2":
			if(date.length()==7){
				sTime = DateUtils.parseDateY_M(date);
			}else{
				sTime = DateUtils.parseDateY_M_D(date);
			}
			eTime = DateUtils.addMonth(sTime,1);
			break;
		case "3":
			if(date.length()==4){
				sTime = DateUtils.parseDateY(date);
			}else{
				sTime = DateUtils.parseDateY_M_D(date);
			}
			eTime = DateUtils.addYear(sTime,1);
			break;

		default:
			break;
		}
		return toListViewDao.queryAreaByES(sTime,eTime);
	}
	@Override
	public void initPcRegisterDoctorWorkTotal(String date, String dateSign) {
		if("1".equals(dateSign)){
			toListViewDao.initPcRegisterDoctorWorkTotal(date, dateSign);
		}else{
			toListViewDao.initPcRegisterDoctorWorkTotalMonthAndYear(date, dateSign);
		}
		
	}
	
}
