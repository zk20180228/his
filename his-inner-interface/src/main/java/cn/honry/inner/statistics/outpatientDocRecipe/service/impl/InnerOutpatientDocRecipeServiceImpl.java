package cn.honry.inner.statistics.outpatientDocRecipe.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.operationNum.dao.InnerOperationNumDao;
import cn.honry.inner.statistics.operationNum.vo.InnerOperationNumsVo;
import cn.honry.inner.statistics.outpatientDocRecipe.dao.InnerOutpatientDocRecipeDao;
import cn.honry.inner.statistics.outpatientDocRecipe.service.InnerOutpatientDocRecipeService;
import cn.honry.inner.statistics.outpatientDocRecipe.vo.CustomVo;
import cn.honry.inner.statistics.outpatientDocRecipe.vo.OutpatientDocRecipeVo;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import redis.clients.jedis.Client;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


@Service("innerOutpatientDocRecipeService")
@Transactional
@SuppressWarnings({"all"})
public class InnerOutpatientDocRecipeServiceImpl implements InnerOutpatientDocRecipeService {
	@Autowired
	@Qualifier(value = "innerOutpatientDocRecipeDao")
	private InnerOutpatientDocRecipeDao innerOutpatientDocRecipeDao;
	/** 
	* @Description: 初始化门诊医生开单工作量
	* @param beginDate
	* @param endDate
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月6日
	*/
	@Override
	public void init_MZYSKDGZL(String menuAlias,String type,String beginDate){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal = Calendar.getInstance();  
        try {
			cal.setTime(sf.parse(beginDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
        cal.add(Calendar.DAY_OF_YEAR, +1);  
        String endDate = sf.format(cal.getTime());  
        //表名
      	String collectionName = null;
		//mong日志
		MongoLog mong = new MongoLog();
		//结果list
		List<OutpatientDocRecipeVo> resultList = new ArrayList<OutpatientDocRecipeVo>();
		mong.setId(null);
		mong.setCountStartTime(new Date());
		mong.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		mong.setCreateTime(new Date());
		mong.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		if("1".equals(type)){
      		mong.setMenuType("MZYSKDGZL_DAY");
			collectionName = "MZYSKDGZL_DAY";
      	}
		String[] st = beginDate.split("-");
		String[] et = endDate.split("-");
		final String sTime = st.length==2?beginDate+"-01":st.length==1?beginDate+"-01-01":beginDate;
		final String eTime = et.length==2?endDate+"-01":et.length==1?endDate+"-01-01":endDate;
		mong.setEndTime(DateUtils.parseDateY_M_D(eTime));
		mong.setStartTime(DateUtils.parseDateY_M_D(sTime));
		Integer size =null;
		//按日统计
		resultList =innerOutpatientDocRecipeDao.MZYSKDGZ(beginDate, endDate);
		if(resultList!=null&&resultList.size()>0){
			size =  resultList.size();
			DBObject objDay = new BasicDBObject();
			objDay.put("date", beginDate);
			new MongoBasicDao().remove(collectionName,objDay);
			//院区每天的总收入
			double dayTotalHZ =0.00;
			double dayTotalZD =0.00;
			double dayTotalHJ =0.00;
			//院区总看诊人数
			long totalPeopleNumHZ =0;
			long totalPeopleNumZD =0;
			long totalPeopleNumHJ =0;
			//院区总开单数
			long totalRecipeNumHZ =0;
			long totalRecipeNumZD =0;
			long totalRecipeNumHJ =0;
			List<CustomVo> listHZ = new ArrayList<CustomVo>();
			List<CustomVo> listZD = new ArrayList<CustomVo>();
			List<CustomVo> listHJ = new ArrayList<CustomVo>();
			Map<String,List<CustomVo>> resultMap = new LinkedHashMap<String,List<CustomVo>>();
			for(OutpatientDocRecipeVo vo :resultList){
				//设置主键
				String tmpKey = vo.getDeptCode()+"&"+vo.getDocCode()+"&"+vo.getPeopleNum()+"&"+vo.getRecipeNum()+"&"+vo.getTotalCost()+"&"+vo.getAreaCode();
				if(resultMap.containsKey(tmpKey)){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
		            List<CustomVo> cuntomVoList = new ArrayList<CustomVo>();  
		            CustomVo cuntomVo = new CustomVo();
		            cuntomVo.setName(vo.getFeeName());
		            cuntomVo.setValue(vo.getFeeCost());
					resultMap.get(tmpKey).add(cuntomVo);  
		        }else{
		        	//map中不存在，新建key，用来存放数据  
		        	List<CustomVo> cuntomVoList = new ArrayList<CustomVo>();  
		            CustomVo cuntomVo = new CustomVo();
		            cuntomVo.setName(vo.getFeeName());
		            cuntomVo.setValue(vo.getFeeCost()); 
		            cuntomVoList.add(cuntomVo);
		            resultMap.put(tmpKey, cuntomVoList);  
		         }  
			}
			
			Map<String,Double> mapHZ = new LinkedHashMap<String,Double>();
			Map<String,Double> mapZD = new LinkedHashMap<String,Double>();
			Map<String,Double> mapHJ = new LinkedHashMap<String,Double>();
			for (String key : resultMap.keySet()) {
				double s =0.0;
				String[] result = key.split("&");
				if("1".equals(result[5])){
					dayTotalHZ += Double.parseDouble(result[4]);
					totalPeopleNumHZ+= Integer.parseInt(result[2]);
					totalRecipeNumHZ+= Integer.parseInt(result[3]);
					List<CustomVo> cuntomVoList = resultMap.get(key);
					for(CustomVo cvo :cuntomVoList){
						if(mapHZ.containsKey(cvo.getName())){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
							mapHZ.put(cvo.getName(),mapHZ.get(cvo.getName())+cvo.getValue());
						}else{
				        	mapHZ.put(cvo.getName(), cvo.getValue());
				        }
					}
				}else if("2".equals(result[5])){
					dayTotalZD += Double.parseDouble(result[4]);
					totalPeopleNumZD+= Integer.parseInt(result[2]);
					totalRecipeNumZD+= Integer.parseInt(result[3]);
					List<CustomVo> cuntomVoList = resultMap.get(key);
					for(CustomVo cvo :cuntomVoList){
						if(mapZD.containsKey(cvo.getName())){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
							mapZD.put(cvo.getName(),mapZD.get(cvo.getName())+cvo.getValue());
						}else{
							mapZD.put(cvo.getName(), cvo.getValue());
				        }
					}
				}else if("3".equals(result[5])){
					dayTotalHJ += Double.parseDouble(result[4]);
					totalPeopleNumHJ+= Integer.parseInt(result[2]);
					totalRecipeNumHJ+= Integer.parseInt(result[3]);
					List<CustomVo> cuntomVoList = resultMap.get(key);
					for(CustomVo cvo :cuntomVoList){
						if(mapHJ.containsKey(cvo.getName())){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
							mapHJ.put(cvo.getName(),mapHJ.get(cvo.getName())+cvo.getValue());
						}else{
							mapHJ.put(cvo.getName(), cvo.getValue());
				        }
					}
					
				}
			}
			for (String key : mapHZ.keySet()) {
				CustomVo hz = new CustomVo();
				hz.setName(key);
				hz.setValue(new BigDecimal(mapHZ.get(key)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				listHZ.add(hz);
			}
			for (String key : mapZD.keySet()) {
				CustomVo hz = new CustomVo();
				hz.setName(key);
				hz.setValue(new BigDecimal(mapZD.get(key)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				listZD.add(hz);
			}
			for (String key : mapHJ.keySet()) {
				CustomVo hz = new CustomVo();
				hz.setName(key);
				hz.setValue(new BigDecimal(mapHJ.get(key)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				listHJ.add(hz);
			}
			dayTotalHZ = new BigDecimal(dayTotalHZ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			dayTotalZD = new BigDecimal(dayTotalZD).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			dayTotalHJ = new BigDecimal(dayTotalHJ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			Document docHZ = new Document();
			Document docZD = new Document();
			Document docHJ = new Document();
			Map<String,Object> valueMapHZ = new LinkedHashMap<String,Object>();
			Map<String,Object> valueMapZD = new LinkedHashMap<String,Object>();
			Map<String,Object> valueMapHJ = new LinkedHashMap<String,Object>();
			if(listHZ!=null&&listHZ.size()>0){
				valueMapHZ.put("feeInfo", listHZ);
			}else{
				CustomVo hz = new CustomVo();
				hz.setName(null);
				hz.setValue(null);	
				listHZ.add(hz);
				valueMapHZ.put("feeInfo", listHZ);
			}
			if(listZD!=null&&listZD.size()>0){
				valueMapZD.put("feeInfo", listZD);
			}else{
				CustomVo hz = new CustomVo();
				hz.setName(null);
				hz.setValue(null);	
				listZD.add(hz);
				valueMapZD.put("feeInfo",listZD);
			}
			if(listHJ!=null&&listHJ.size()>0){
				valueMapHJ.put("feeInfo", listHJ);
			}else{
				CustomVo hz = new CustomVo();
				hz.setName(null);
				hz.setValue(null);	
				listHJ.add(hz);
				valueMapHJ.put("feeInfo",listHJ);
			}
			docHZ.append("date", beginDate).append("deptCode","HZ").append("docCode","")
			.append("peopleNum",totalPeopleNumHZ).append("recipeNum",totalRecipeNumHZ)
			.append("totolCost", dayTotalHZ).append("value", JSONUtils.toJson(valueMapHZ));
			docZD.append("date", beginDate).append("deptCode","ZD").append("docCode","")
			.append("peopleNum",totalPeopleNumZD).append("recipeNum",totalRecipeNumZD)
			.append("totolCost", dayTotalZD).append("value", JSONUtils.toJson(valueMapZD));
			docHJ.append("date", beginDate).append("deptCode","HJ").append("docCode","")
			.append("peopleNum",totalPeopleNumHJ).append("recipeNum",totalRecipeNumHJ)
			.append("totolCost", dayTotalHJ).append("value", JSONUtils.toJson(valueMapHJ));
			new MongoBasicDao().insertData(collectionName, docHZ);
			new MongoBasicDao().insertData(collectionName, docZD);
			new MongoBasicDao().insertData(collectionName, docHJ);
			for (String key : resultMap.keySet()) {
				String[] result = key.split("&");
				Map<String,Object> valueMap = new LinkedHashMap<String,Object>();
				valueMap.put("feeInfo", resultMap.get(key));
				Document doc = new Document();
				doc.append("date", beginDate).append("deptCode", result[0]).append("docCode", result[1]).append("peopleNum", result[2])
				.append("recipeNum",result[3]).append("totolCost", result[4]).append("value",JSONUtils.toJson(valueMap));
				new MongoBasicDao().insertData(collectionName, doc);
			}
		}
		mong.setCountEndTime(new Date());
		mong.setTotalNum(size);
		mong.setState(1);
		innerOutpatientDocRecipeDao.saveMongoLog(mong);
		//更新当月的统计数据
		init_MonthOrYearData(menuAlias, "2", beginDate,endDate);
		//更新当年的统计数据
		init_MonthOrYearData(menuAlias, "3", beginDate,endDate);
	}
	/**
	 * 计算当月或当年的数据
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 开始时间
	 */
	public void init_MonthOrYearData(String menuAlias,String type,String beginDate,String endDate){
		//更新当月或当年的统计数据(从_DAY或_Month表中查询数据,重新计算)
		MongoLog mong = new MongoLog();
		//结果list
		List<OutpatientDocRecipeVo> resultList = new ArrayList<OutpatientDocRecipeVo>();
		mong.setId(null);
		mong.setCountStartTime(new Date());
		mong.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		mong.setCreateTime(new Date());
		mong.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		if("2".equals(type)){
			mong.setMenuType("MZYSKDGZL_MONTH");
		}
		if("3".equals(type)){
			mong.setMenuType("MZYSKDGZL_YEAR");
		}
		String[] st = beginDate.split("-");
		String[] et = endDate.split("-");
		final String sTime = st.length==2?beginDate+"-01":st.length==1?beginDate+"-01-01":beginDate;
		final String eTime = et.length==2?endDate+"-01":et.length==1?endDate+"-01-01":endDate;
		mong.setEndTime(DateUtils.parseDateY_M_D(eTime));
		mong.setStartTime(DateUtils.parseDateY_M_D(sTime));
		Integer size =null;
		
		//按月统计
		Date monthDate = DateUtils.parseDateY_M(beginDate);
		String monthStartDate = DateUtils.formatDateY_M(monthDate);//当前月
		Date monthNextDate = DateUtils.addMonth(monthDate, 1);//下一月
		String monthEndDate = DateUtils.formatDateY_M(monthNextDate);
		//按年统计
		Date yearDate = DateUtils.parseDateY(beginDate);
		String yearStartDate = DateUtils.formatDateY(yearDate);//当前年
		Date yearNextDate = DateUtils.addYear(yearDate, 1);//下一年
		String yearEndDate = DateUtils.formatDateY(yearNextDate);
		
		String queryMenuAlias=menuAlias;//查询时用的表名称
		if("2".equals(type)){
			BasicDBList dblist = new BasicDBList();
			BasicDBObject sdb = new BasicDBObject();
			sdb.put("date", new BasicDBObject("$gte", monthStartDate));
			BasicDBObject edb = new BasicDBObject();
			edb.put("date", new BasicDBObject("$lt", monthEndDate));
			dblist.add(sdb);
			dblist.add(edb);
			BasicDBObject monthWhere = new BasicDBObject();
			monthWhere.put("$and", dblist);
			DBCursor cursor = new MongoBasicDao().findAlldata("MZYSKDGZL_DAY", monthWhere);
			size= (int) new MongoBasicDao().findAllCountBy("MZYSKDGZL_DAY", monthWhere).longValue();
			DBObject objMonth = new BasicDBObject();
			objMonth.put("date", monthStartDate);
			new MongoBasicDao().remove(menuAlias+"_MONTH",objMonth);
			init_MonthOrYear(menuAlias,type,cursor,monthStartDate);
			
		}
		if("3".equals(type)){
			BasicDBList yearDbList = new BasicDBList();
			BasicDBObject yearStart = new BasicDBObject();
			yearStart.put("date", new BasicDBObject("$gte", yearStartDate));
			BasicDBObject yearEnd = new BasicDBObject();
			yearEnd.put("date", new BasicDBObject("$lt", yearEndDate));
			yearDbList.add(yearStart);
			yearDbList.add(yearEnd);
			BasicDBObject yearWhere = new BasicDBObject();
			yearWhere.put("$and", yearDbList);
			DBCursor yearCursor = new MongoBasicDao().findAlldata("MZYSKDGZL_MONTH", yearWhere);
			size= (int) new MongoBasicDao().findAllCountBy("MZYSKDGZL_MONTH", yearWhere).longValue();
			DBObject obj = new BasicDBObject();
			obj.put("date", yearStartDate);
			new MongoBasicDao().remove(menuAlias+"_YEAR",obj);
			init_MonthOrYear(menuAlias,type,yearCursor,yearStartDate);
		}
		mong.setCountEndTime(new Date());
		mong.setTotalNum(size);
		mong.setState(1);
		innerOutpatientDocRecipeDao.saveMongoLog(mong);
	}
	public void init_MonthOrYear(String menuAlias,String type,DBCursor cursor,String date){
		//院区的总收入
		double totalHZ =0.00;
		double totalZD =0.00;
		double totalHJ =0.00;
		//院区总看诊人数
		long totalPeopleNumHZ =0;
		long totalPeopleNumZD =0;
		long totalPeopleNumHJ =0;
		//院区总开单数
		long totalRecipeNumHZ =0;
		long totalRecipeNumZD =0;
		long totalRecipeNumHJ =0;
		Map<String,Double> mapHZ = new LinkedHashMap<String,Double>();
		Map<String,Double> mapZD = new LinkedHashMap<String,Double>();
		Map<String,Double> mapHJ = new LinkedHashMap<String,Double>();
		List<CustomVo> listHZ = new ArrayList<CustomVo>();
		List<CustomVo> listZD = new ArrayList<CustomVo>();
		List<CustomVo> listHJ = new ArrayList<CustomVo>();
		while(cursor.hasNext()){
			DBObject next = cursor.next();
			Document doc = new Document();
			if(next.get("deptCode")!=null&&!"".equals(next.get("deptCode"))){
				if((!"HZ".equals(next.get("deptCode"))) && (!"ZD".equals(next.get("deptCode"))) && (!"HJ".equals(next.get("deptCode")))){
					doc.append("date", date).append("deptCode",next.get("deptCode")).append("docCode",next.get("docCode")).append("peopleNum",next.get("peopleNum"))
					.append("recipeNum",next.get("recipeNum")).append("totolCost", next.get("totolCost")).append("value",next.get("value"));
					if("2".equals(type)){
						new MongoBasicDao().insertData(menuAlias+"_MONTH", doc);
					}
					if("3".equals(type)){
						new MongoBasicDao().insertData(menuAlias+"_YEAR", doc);
					}
					
				}else{
					if("HZ".equals(next.get("deptCode"))){
						totalHZ+= (Double)next.get("totolCost");
						totalPeopleNumHZ +=(Long)next.get("peopleNum");
						totalRecipeNumHZ +=(Long)next.get("recipeNum");
						String jsonString = (String)next.get("value");
				        // 读取JSON数据
						List<CustomVo> list=null;
			            try {
			            	if(StringUtils.isNotBlank(jsonString)){
			            		String string = jsonString.substring(11, jsonString.length()-1);
			            		if(!("[{}]").equals(string)){
			            			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
				            		listHZ.addAll(list);
			            		}
			            		 
			            	}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if("ZD".equals(next.get("deptCode"))){
						totalZD+= (Double)next.get("totolCost");
						totalPeopleNumZD +=(Long)next.get("peopleNum");
						totalRecipeNumZD +=(Long)next.get("recipeNum");
						if(next.get("value")!=null){
							String jsonString = (String) next.get("value");
							 // 读取JSON数据
							List<CustomVo> list=null;
				            try {
				            	if(StringUtils.isNotBlank(jsonString)){
				            		String string = jsonString.substring(11, jsonString.length()-1);
				            		if(!("[{}]").equals(string)){
				            			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
					            		listZD.addAll(list); 
				            		}
				            	}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else if("HJ".equals(next.get("deptCode"))){
						totalHJ+= (Double)next.get("totolCost");
						totalPeopleNumHJ +=(Long)next.get("peopleNum");
						totalRecipeNumHJ +=(Long)next.get("recipeNum");
						String jsonString = (String)next.get("value");
				        // 读取JSON数据
						List<CustomVo> list=null;
			            try {
			            	if(StringUtils.isNotBlank(jsonString)){
			            		String string = jsonString.substring(11, jsonString.length()-1);
			            		if(!("[{}]").equals(string)){
			            			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
				            		listHJ.addAll(list); 
			            		}
			            	}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}
		totalHZ = new BigDecimal(totalHZ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		totalZD = new BigDecimal(totalZD).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		totalHJ = new BigDecimal(totalHJ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		for(CustomVo cvo :listHZ){
			if(mapHZ.containsKey(cvo.getName())){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
				mapHZ.put(cvo.getName(),mapHZ.get(cvo.getName())+cvo.getValue());
			}else{
	        	mapHZ.put(cvo.getName(), cvo.getValue());
	        }
		}
		for(CustomVo cvo :listZD){
			if(mapZD.containsKey(cvo.getName())){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
				mapZD.put(cvo.getName(),mapZD.get(cvo.getName())+cvo.getValue());
			}else{
				mapZD.put(cvo.getName(), cvo.getValue());
			}
		}
		for(CustomVo cvo :listHJ){
			if(mapHJ.containsKey(cvo.getName())){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
				mapHJ.put(cvo.getName(),mapHJ.get(cvo.getName())+cvo.getValue());
			}else{
				mapHJ.put(cvo.getName(), cvo.getValue());
			}
		}
		//院区value中的费别信息
		List<CustomVo> resultListHZ = new ArrayList<CustomVo>();
		List<CustomVo> resultListZD = new ArrayList<CustomVo>();
		List<CustomVo> resultListHJ = new ArrayList<CustomVo>();
		for (String key : mapHZ.keySet()) {
			CustomVo hz = new CustomVo();
			hz.setName(key);
			hz.setValue(new BigDecimal(mapHZ.get(key)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			resultListHZ.add(hz);
		}
		for (String key : mapZD.keySet()) {
			CustomVo hz = new CustomVo();
			hz.setName(key);
			hz.setValue(new BigDecimal(mapZD.get(key)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			resultListZD.add(hz);
		}
		for (String key : mapHJ.keySet()) {
			CustomVo hz = new CustomVo();
			hz.setName(key);
			hz.setValue(new BigDecimal(mapHJ.get(key)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			resultListHJ.add(hz);
		}
		Document docHZ = new Document();
		Document docZD = new Document();
		Document docHJ = new Document();
		Map<String,Object> valueMapHZ = new LinkedHashMap<String,Object>();
		Map<String,Object> valueMapZD = new LinkedHashMap<String,Object>();
		Map<String,Object> valueMapHJ = new LinkedHashMap<String,Object>();
		valueMapHZ.put("feeInfo", resultListHZ);
		valueMapZD.put("feeInfo", resultListZD);
		valueMapHJ.put("feeInfo", resultListHJ);
		docHZ.append("date", date).append("deptCode","HZ").append("docCode","")
		.append("peopleNum",totalPeopleNumHZ).append("recipeNum",totalRecipeNumHZ).append("totolCost", totalHZ)
		.append("value", JSONUtils.toJson(valueMapHZ));
		
		docZD.append("date", date).append("deptCode","ZD").append("docCode","")
		.append("peopleNum",totalPeopleNumZD).append("recipeNum",totalRecipeNumZD).append("totolCost", totalZD)
		.append("value",JSONUtils.toJson(valueMapZD));
		
		docHJ.append("date", date).append("deptCode","HJ").append("docCode","")
		.append("peopleNum",totalPeopleNumHJ).append("recipeNum",totalRecipeNumHJ).append("totolCost", totalHJ)
		.append("value", JSONUtils.toJson(valueMapHJ));
		if("2".equals(type)){
			new MongoBasicDao().insertData(menuAlias+"_MONTH", docHZ);
			new MongoBasicDao().insertData(menuAlias+"_MONTH", docZD);
			new MongoBasicDao().insertData(menuAlias+"_MONTH", docHJ);
		}
		if("3".equals(type)){
			new MongoBasicDao().insertData(menuAlias+"_YEAR", docHZ);
			new MongoBasicDao().insertData(menuAlias+"_YEAR", docZD);
			new MongoBasicDao().insertData(menuAlias+"_YEAR", docHJ);
		}
		
	}
	
}
