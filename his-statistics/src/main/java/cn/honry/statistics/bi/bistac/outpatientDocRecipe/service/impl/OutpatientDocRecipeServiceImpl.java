package cn.honry.statistics.bi.bistac.outpatientDocRecipe.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.mongoDataInit.dao.MongoDataInitDao;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.dao.OutpatientDocRecipeDao;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.service.OutpatientDocRecipeService;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.OutpatientDocRecipeVo;
import cn.honry.statistics.bi.bistac.outpatientDocRecipe.vo.StatisticsVo;
import cn.honry.statistics.util.customVo.CustomVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;

@Service("outpatientDocRecipeService")
@Transactional
@SuppressWarnings({"all"})
public class OutpatientDocRecipeServiceImpl implements OutpatientDocRecipeService{
	@Autowired
	@Qualifier(value = "outpatientDocRecipeDao")
	private OutpatientDocRecipeDao outpatientDocRecipeDao;
	@Autowired
	@Qualifier(value = "mongoDataInitDao")
	private MongoDataInitDao mongoDataInitDao;
	
	/** 
	* @Description: 初始化门诊医生开单工作量
	* @param beginDate
	* @param endDate
	* @param type
	* @return void    返回类型 
	* @author zx 
	 * @throws Exception 
	* @date 2017年7月6日
	*/
	@Override
	public void init_MZYSKDGZL(String beginDate, String endDate, Integer type) throws Exception {
		//表名
		String collectionName = null;
		//mong日志
		MongoLog mong = new MongoLog();
		//结果list
		List<OutpatientDocRecipeVo> resultList = new ArrayList<OutpatientDocRecipeVo>();
		mong.setId(null);
		mong.setCountStartTime(new Date());
		mong.setCreateDept(null);
		mong.setCreateTime(new Date());
		mong.setCreateUser(null);
		String[] st = beginDate.split("-");
		String[] et = endDate.split("-");
		final String sTime = st.length==2?beginDate+"-01":st.length==1?beginDate+"-01-01":beginDate;
		final String eTime = et.length==2?endDate+"-01":et.length==1?endDate+"-01-01":endDate;
		mong.setEndTime(DateUtils.parseDateY_M_D(eTime));
		mong.setStartTime(DateUtils.parseDateY_M_D(sTime));
		if("1".equals(type+"")){//日
			mong.setMenuType("MZYSKDGZL_DAY");
			collectionName = "MZYSKDGZL_DAY";
		}else if("2".equals(type+"")){//月
			mong.setMenuType("MZYSKDGZL_MONTH");
			collectionName = "MZYSKDGZL_MONTH";
		}else if("3".equals(type+"")){//年
			mong.setMenuType("MZYSKDGZL_YEAR");
			collectionName = "MZYSKDGZL_YEAR";
		}
		Integer size =null;
		if(type==null ||type==1){
			//按日统计
			resultList =outpatientDocRecipeDao.MZYSKDGZ(beginDate, endDate, type);
			if(resultList!=null&&resultList.size()>0){
				size =  resultList.size();
				DBObject obj = new BasicDBObject();
				obj.put("date", beginDate);
				new MongoBasicDao().remove("MZYSKDGZL_DAY",obj);
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
				Map<String,List<CustomVo>> resultMap = new LinkedHashMap<String,List<CustomVo>>();
				//整合同一个医生同一科室下的费别和费别费用
				for(OutpatientDocRecipeVo vo :resultList){
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
				//院区合并费别的map
				Map<String,Double> mapHZ = new LinkedHashMap<String,Double>();
				Map<String,Double> mapZD = new LinkedHashMap<String,Double>();
				Map<String,Double> mapHJ = new LinkedHashMap<String,Double>();
				for (String key : resultMap.keySet()) {
					String[] result = key.split("&");
					if("1".equals(result[5])){
						totalHZ+=  Double.parseDouble(result[4]);
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
						totalZD+=  Double.parseDouble(result[4]);
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
						totalHJ+=  Double.parseDouble(result[4]);
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
				//院区存放合并之后的费别结果list
				List<CustomVo> listHZ = new ArrayList<CustomVo>();
				List<CustomVo> listZD = new ArrayList<CustomVo>();
				List<CustomVo> listHJ = new ArrayList<CustomVo>();
				//根据费别的费用计算院区的总收入
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
				//总收入保留两位小数
				totalHZ = new BigDecimal(totalHZ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				totalZD = new BigDecimal(totalZD).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				totalHJ = new BigDecimal(totalHJ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				
				Document docHZ = new Document();
				Document docZD = new Document();
				Document docHJ = new Document();
				//院区的结果
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
				//插入院区信息
				docHZ.append("date", beginDate).append("deptCode","HZ").append("docCode","")
				.append("peopleNum",totalPeopleNumHZ).append("recipeNum",totalRecipeNumHZ)
				.append("totolCost", totalHZ).append("value", JSONUtils.toJson(valueMapHZ));
				
				docZD.append("date", beginDate).append("deptCode","ZD").append("docCode","")
				.append("peopleNum",totalPeopleNumZD).append("recipeNum",totalRecipeNumZD)
				.append("totolCost", totalZD).append("value", JSONUtils.toJson(valueMapZD));
				
				docHJ.append("date", beginDate).append("deptCode","HJ").append("docCode","")
				.append("peopleNum",totalPeopleNumHJ).append("recipeNum",totalRecipeNumHJ)
				.append("totolCost", totalHJ).append("value", JSONUtils.toJson(valueMapHJ));
				new MongoBasicDao().insertData(collectionName, docHZ);
				new MongoBasicDao().insertData(collectionName, docZD);
				new MongoBasicDao().insertData(collectionName, docHJ);
				//插入统计时间的数据
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
		}else if(type==2){
			//按月统计
			Date date1 = DateUtils.parseDateY_M(beginDate);
			Date date2 = DateUtils.parseDateY_M(endDate);
			while(DateUtils.compareDate(date1, date2)<=0){
				String sdate = DateUtils.formatDateY_M(date1);//当前月
				Date nextDate = DateUtils.addMonth(date1, 1);//下一月
				String edate = DateUtils.formatDateY_M(nextDate);
				BasicDBList dblist = new BasicDBList();
				BasicDBObject sdb = new BasicDBObject();
				sdb.put("date", new BasicDBObject("$gte", sdate));
				BasicDBObject edb = new BasicDBObject();
				edb.put("date", new BasicDBObject("$lt", edate));
				dblist.add(sdb);
				dblist.add(edb);
				BasicDBObject where = new BasicDBObject();
				where.put("$and", dblist);
				DBCursor cursor = new MongoBasicDao().findAlldata("MZYSKDGZL_DAY", where);
				size= (int) new MongoBasicDao().findAllCountBy("MZYSKDGZL_DAY", where).longValue();
				DBObject obj = new BasicDBObject();
				obj.put("date", sdate);
				new MongoBasicDao().remove(collectionName,obj);
				initMonthOrYear(collectionName, type, sdate, cursor);
				date1=nextDate;//nextDate-->2010-02-01
			}
		}else{//按年统计
			Date date1 = DateUtils.parseDateY(beginDate);
			Date date2 = DateUtils.parseDateY(endDate);
			while(DateUtils.compareDate(date1, date2)<=0){
				String sdate = DateUtils.formatDateY(date1);//当前年
				Date nextDate = DateUtils.addYear(date1, 1);//下一年
				String edate = DateUtils.formatDateY(nextDate);
				BasicDBList dblist = new BasicDBList();
				BasicDBObject sdb = new BasicDBObject();
				sdb.put("date", new BasicDBObject("$gte", sdate));
				BasicDBObject edb = new BasicDBObject();
				edb.put("date", new BasicDBObject("$lt", edate));
				dblist.add(sdb);
				dblist.add(edb);
				BasicDBObject where = new BasicDBObject();
				where.put("$and", dblist);
				DBCursor cursor = new MongoBasicDao().findAlldata("MZYSKDGZL_MONTH", where);
				size= (int) new MongoBasicDao().findAllCountBy("MZYSKDGZL_MONTH", where).longValue();
				DBObject obj = new BasicDBObject();
				obj.put("date", sdate);
				new MongoBasicDao().remove(collectionName,obj);
				initMonthOrYear(collectionName, type, sdate, cursor);
				date1=nextDate;//nextDate-->2010-02-01
			}
			
		}
		mong.setCountEndTime(new Date());
		mong.setTotalNum(size);
		mong.setState(1);
		mongoDataInitDao.saveMongoLog(mong);
	}
	public void initMonthOrYear(String collectionName,Integer type,String sdate,DBCursor cursor) throws Exception{
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
		//院区费别合并的map
		Map<String,Double> mapHZ = new LinkedHashMap<String,Double>();
		Map<String,Double> mapZD = new LinkedHashMap<String,Double>();
		Map<String,Double> mapHJ = new LinkedHashMap<String,Double>();
		//院区value中的费别信息
		List<CustomVo> listHZ = new ArrayList<CustomVo>();
		List<CustomVo> listZD = new ArrayList<CustomVo>();
		List<CustomVo> listHJ = new ArrayList<CustomVo>();

		while(cursor.hasNext()){
			DBObject next = cursor.next();
			Document doc = new Document();
			if(next.get("deptCode")!=null&&!"".equals(next.get("deptCode"))){
				if((!"HZ".equals(next.get("deptCode"))) && (!"ZD".equals(next.get("deptCode"))) && (!"HJ".equals(next.get("deptCode")))){
					doc.append("date", sdate).append("deptCode",next.get("deptCode")).append("docCode",next.get("docCode")).append("peopleNum",next.get("peopleNum"))
					.append("recipeNum",next.get("recipeNum")).append("totolCost", next.get("totolCost")).append("value",next.get("value"));
					new MongoBasicDao().insertData(collectionName, doc);
				}else{
					if("HZ".equals(next.get("deptCode"))){
						totalHZ+= (Double)next.get("totolCost");
						totalPeopleNumHZ +=(Long)next.get("peopleNum");
						totalRecipeNumHZ +=(Long)next.get("recipeNum");
						String jsonString = (String)next.get("value");
				        // 读取JSON数据
						List<CustomVo> list=null;
		            	if(StringUtils.isNotBlank(jsonString)){
		            		String string = jsonString.substring(11, jsonString.length()-1); 
		            		if(!("[{}]").equals(string)){
		            			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
			            		listHZ.addAll(list); 
		            		}
		            	}
					}else if("ZD".equals(next.get("deptCode"))){
						totalZD+= (Double)next.get("totolCost");
						totalPeopleNumZD +=(Long)next.get("peopleNum");
						totalRecipeNumZD +=(Long)next.get("recipeNum");
						if(next.get("value")!=null){
							String jsonString = (String) next.get("value");
							 // 读取JSON数据
							List<CustomVo> list=null;
			            	if(StringUtils.isNotBlank(jsonString)){
			            		String string = jsonString.substring(11, jsonString.length()-1);
			            		if(!("[{}]").equals(string)){
			            			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
				            		listZD.addAll(list); 
			            		}
			            	}
						}
					}else if("HJ".equals(next.get("deptCode"))){
						totalHJ+= (Double)next.get("totolCost");
						totalPeopleNumHJ +=(Long)next.get("peopleNum");
						totalRecipeNumHJ +=(Long)next.get("recipeNum");
						String jsonString = (String)next.get("value");
				        // 读取JSON数据
						List<CustomVo> list=null;
		            	if(StringUtils.isNotBlank(jsonString)){
		            		String string = jsonString.substring(11, jsonString.length()-1);
		            		if(!("[{}]").equals(string)){
		            			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
			            		listHJ.addAll(list); 
		            		}
		            		
		            	}
					}
				}
			}
			
		}
		//院区总费用保留两位小数
		totalHZ = new BigDecimal(totalHZ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		totalZD = new BigDecimal(totalZD).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		totalHJ = new BigDecimal(totalHJ).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		//院区的value合并
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
		
		docHZ.append("date", sdate).append("deptCode","HZ").append("docCode","")
		.append("peopleNum",totalPeopleNumHZ).append("recipeNum",totalRecipeNumHZ).append("totolCost", totalHZ)
		.append("value", JSONUtils.toJson(valueMapHZ));
		
		docZD.append("date", sdate).append("deptCode","ZD").append("docCode","")
		.append("peopleNum",totalPeopleNumZD).append("recipeNum",totalRecipeNumZD).append("totolCost", totalZD)
		.append("value",JSONUtils.toJson(valueMapZD));
		
		docHJ.append("date", sdate).append("deptCode","HJ").append("docCode","")
		.append("peopleNum",totalPeopleNumHJ).append("recipeNum",totalRecipeNumHJ).append("totolCost", totalHJ)
		.append("value", JSONUtils.toJson(valueMapHJ));
		new MongoBasicDao().insertData(collectionName, docHZ);
		new MongoBasicDao().insertData(collectionName, docZD);
		new MongoBasicDao().insertData(collectionName, docHJ);
	}
	/** 
	* @Description:门诊医生开单工作量统计 
	* @param dept 科室code
	* @param expxrt 医生code
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param page 页数
	* @param rows 行数
	* @throws Exception
	* @return Map<String,Object>    返回类型 
	* @author zx 
	* @date 2017年7月26日
	*/
	@Override
	public Map<String, Object> listStatisticsQueryByES(String dept, String expxrt, String sTime, String eTime,
			String page, String rows) throws Exception {
		//从mongdb中查询出院区的数据
		List<StatisticsVo> voListYQ = new ArrayList<StatisticsVo>();
		//符合格式的院区最终数据集合
		List<StatisticsVo> resultYQList = new ArrayList<StatisticsVo>();
		//从mongdb中查询的数据
		List<StatisticsVo> voList = new ArrayList<StatisticsVo>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		//每页要显示的行数
		Integer r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):20;
		//页数
		Integer p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		BasicDBList dblist = new BasicDBList();
		BasicDBList dblistYQ = new BasicDBList();
		BasicDBObject sdb = new BasicDBObject();
		BasicDBObject edb = new BasicDBObject();
		BasicDBObject ddb = new BasicDBObject();
		BasicDBObject erdb = new BasicDBObject();
		sdb.put("date", new BasicDBObject("$gte", sTime));
		edb.put("date", new BasicDBObject("$lt", eTime));
		//添加科室查询添加
		if (StringUtils.isNotBlank(dept)) {
			String[] depts = dept.split(",");
			BasicDBList values = new BasicDBList();
			for(int i=0;i<depts.length;i++){
				values.add(depts[i]);
			}
			ddb.put("deptCode",new BasicDBObject("$in", values));
			dblist.add(ddb);
		} 
		//添加医生查询条件
		if (StringUtils.isNotBlank(expxrt)) {
			BasicDBList values = new BasicDBList();
			String[] expxrts = expxrt.split(",");
			for(int i=0;i<expxrts.length;i++){
				values.add(expxrts[i]);
			}
			erdb.put("docCode",new BasicDBObject("$in", values));
			dblist.add(erdb);
		}
		//只用根据时间查询时进行院区的统计
		if(StringUtils.isBlank(dept)&&StringUtils.isBlank(expxrt)){
			BasicDBObject ddbYQ = new BasicDBObject();
			BasicDBList YQ = new BasicDBList();
			YQ.add("HZ");
			YQ.add("ZD");
			YQ.add("HJ");
			ddbYQ.put("deptCode",new BasicDBObject("$in",YQ));
			dblistYQ.add(ddbYQ);
			dblistYQ.add(sdb);
			dblistYQ.add(edb);
			BasicDBObject whereYQ = new BasicDBObject();
			whereYQ.put("$and", dblistYQ);
			DBCursor cursorYQ = new MongoBasicDao().findAlldata("MZYSKDGZL_DAY",whereYQ);
			StatisticsVo HZ = new StatisticsVo();
			StatisticsVo ZD = new StatisticsVo();
			StatisticsVo HJ = new StatisticsVo();
			while(cursorYQ.hasNext()){
				DBObject next = cursorYQ.next();
				Document doc = new Document();
				StatisticsVo vo = new StatisticsVo();
				vo.setDeptCode((String)next.get("deptCode"));
				vo.setDocCode((String)next.get("docCode"));
				vo.setPeopleNum(Integer.parseInt(next.get("peopleNum")+""));
				vo.setRecipeNum(Integer.parseInt(next.get("recipeNum")+""));
				vo.setTotleCost(Double.parseDouble(next.get("totolCost")+""));
				String jsonString = (String)next.get("value");
		        // 读取JSON数据
				List<CustomVo> list=null;
	        	if(StringUtils.isNotBlank(jsonString)){
	        		String string = jsonString.substring(11, jsonString.length()-1);
	        		if(!("[{}]").equals(string)){
	        			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
	        			this.setCost(vo, list);
	        		}
	        		
	        	}
	        	voListYQ.add(vo);
			}
			Map<String,StatisticsVo> yqMap = new LinkedHashMap<String,StatisticsVo>();
			for(StatisticsVo svo: voListYQ){
				String key = svo.getDeptCode();
				if(yqMap.containsKey(key)){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
					StatisticsVo statisticsVo = new StatisticsVo();
					statisticsVo.setDeptCode(svo.getDeptCode());
					statisticsVo.setDocCode(svo.getDocCode());
					statisticsVo.setPeopleNum(yqMap.get(key).getPeopleNum()+svo.getPeopleNum());
					statisticsVo.setRecipeNum(yqMap.get(key).getRecipeNum()+svo.getRecipeNum());
					statisticsVo.setHerbalCost(yqMap.get(key).getHerbalCost()+svo.getHerbalCost());
					statisticsVo.setWesternCost(yqMap.get(key).getWesternCost()+svo.getWesternCost());
					statisticsVo.setChineseCost(yqMap.get(key).getChineseCost()+svo.getChineseCost());
					statisticsVo.setChuangweiCost(yqMap.get(key).getChuangweiCost()+svo.getChuangweiCost());
					statisticsVo.setTreatmentCost(yqMap.get(key).getTreatmentCost()+svo.getTreatmentCost());
					statisticsVo.setInspectCost(yqMap.get(key).getInspectCost()+svo.getInspectCost());
					statisticsVo.setRadiationCost(yqMap.get(key).getRadiationCost()+svo.getRadiationCost());
					statisticsVo.setTestCost(yqMap.get(key).getTestCost()+svo.getTestCost());
					statisticsVo.setShoushuCost(yqMap.get(key).getShoushuCost()+svo.getShoushuCost());
					statisticsVo.setBloodCost(yqMap.get(key).getBloodCost()+svo.getBloodCost());
					statisticsVo.setO2Cost(yqMap.get(key).getO2Cost()+svo.getO2Cost());
					statisticsVo.setCailiaoCost(yqMap.get(key).getCailiaoCost()+svo.getCailiaoCost());
					statisticsVo.setYimiaoCost(yqMap.get(key).getYimiaoCost()+svo.getYimiaoCost());
					statisticsVo.setOtherCost(yqMap.get(key).getOtherCost()+svo.getOtherCost());
					statisticsVo.setTotleCost(yqMap.get(key).getTotleCost()+svo.getTotleCost());
					statisticsVo.setDeptCost(yqMap.get(key).getTotleCost()+svo.getTotleCost());
					yqMap.put(key, statisticsVo); 
				}else{
					yqMap.put(key, svo); 
				}
			}
			for (String key : yqMap.keySet()) {
				resultYQList.add(yqMap.get(key));
			}
		}
		dblist.add(sdb);
		dblist.add(edb);
		BasicDBObject where = new BasicDBObject();
		where.put("$and", dblist);
		//根据科室医生和时间查询数据
		DBCursor cursor = new MongoBasicDao().findAllDataSortBy("MZYSKDGZL_DAY", "date", where, r, p);
		DBObject obj = new BasicDBObject();
		while(cursor.hasNext()){
			DBObject next = cursor.next();
			Document doc = new Document();
			StatisticsVo vo = new StatisticsVo();
			vo.setDeptCode((String)next.get("deptCode"));
			vo.setDocCode((String)next.get("docCode"));
			vo.setPeopleNum(Integer.parseInt(next.get("peopleNum")+""));
			vo.setRecipeNum(Integer.parseInt(next.get("recipeNum")+""));
			vo.setTotleCost(Double.parseDouble(next.get("totolCost")+""));
			String jsonString = (String)next.get("value");
	        // 读取JSON数据
			List<CustomVo> list=null;
        	if(StringUtils.isNotBlank(jsonString)){
        		String string = jsonString.substring(11, jsonString.length()-1);
        		if(!("[{}]").equals(string)){
        			list = JSONUtils.fromJson(string, new TypeToken<List<CustomVo>>(){});
        			this.setCost(vo, list);
        		}
        		
        	}
        	voList.add(vo);
		}
		//移除日表中每日的院区统计信息，因为院区数据已经根据时间段合并
		List<StatisticsVo> removeList = new ArrayList<StatisticsVo>();
		for(StatisticsVo svo:voList){
			if("HZ".equals(svo.getDeptCode()) || "ZD".equals(svo.getDeptCode()) || "HJ".equals(svo.getDeptCode())){
				removeList.add(svo);
			}
		}
		voList.removeAll(removeList);
		Map<String,StatisticsVo> resultMap = new LinkedHashMap<String,StatisticsVo>();
		List<StatisticsVo> resultList = new ArrayList<StatisticsVo>();
		//合并一个时间段内同一科室同一医生的数据
		for(StatisticsVo svo:voList){
			String key = svo.getDeptCode()+svo.getDocCode();
			if(resultMap.containsKey(key)){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
				StatisticsVo statisticsVo = new StatisticsVo();
				statisticsVo.setDeptCode(svo.getDeptCode());
				statisticsVo.setDocCode(svo.getDocCode());
				statisticsVo.setPeopleNum(resultMap.get(key).getPeopleNum()+svo.getPeopleNum());
				statisticsVo.setRecipeNum(resultMap.get(key).getRecipeNum()+svo.getRecipeNum());
				statisticsVo.setHerbalCost(resultMap.get(key).getHerbalCost()+svo.getHerbalCost());
				statisticsVo.setWesternCost(resultMap.get(key).getWesternCost()+svo.getWesternCost());
				statisticsVo.setChineseCost(resultMap.get(key).getChineseCost()+svo.getChineseCost());
				statisticsVo.setChuangweiCost(resultMap.get(key).getChuangweiCost()+svo.getChuangweiCost());
				statisticsVo.setTreatmentCost(resultMap.get(key).getTreatmentCost()+svo.getTreatmentCost());
				statisticsVo.setInspectCost(resultMap.get(key).getInspectCost()+svo.getInspectCost());
				statisticsVo.setRadiationCost(resultMap.get(key).getRadiationCost()+svo.getRadiationCost());
				statisticsVo.setTestCost(resultMap.get(key).getTestCost()+svo.getTestCost());
				statisticsVo.setShoushuCost(resultMap.get(key).getShoushuCost()+svo.getShoushuCost());
				statisticsVo.setBloodCost(resultMap.get(key).getBloodCost()+svo.getBloodCost());
				statisticsVo.setO2Cost(resultMap.get(key).getO2Cost()+svo.getO2Cost());
				statisticsVo.setCailiaoCost(resultMap.get(key).getCailiaoCost()+svo.getCailiaoCost());
				statisticsVo.setYimiaoCost(resultMap.get(key).getYimiaoCost()+svo.getYimiaoCost());
				statisticsVo.setOtherCost(resultMap.get(key).getOtherCost()+svo.getOtherCost());
				statisticsVo.setDeptCost(resultMap.get(key).getDeptCost()+svo.getDeptCost());
				statisticsVo.setTotleCost(resultMap.get(key).getTotleCost()+svo.getTotleCost());
				resultMap.put(key, statisticsVo); 
			}else{
				 resultMap.put(key, svo); 
			}
		}
		//将院区信息整合到最终结果集合中
		resultList.addAll(resultYQList);
		for (String key : resultMap.keySet()) {
			resultList.add(resultMap.get(key));
		}
		//科室收入统计
		Map<String,Double> deptCostMap = new LinkedHashMap<String,Double>();
		for(StatisticsVo svo:resultList){
			if(deptCostMap.containsKey(svo.getDeptCode())){
				//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
				deptCostMap.put(svo.getDeptCode(),deptCostMap.get(svo.getDeptCode())+svo.getTotleCost());
			}else{
				deptCostMap.put(svo.getDeptCode(), svo.getTotleCost());
			}
	    }
		//同一科室下的数据赋值科室统计
		for (String key : deptCostMap.keySet()) {
			for(int i=0;i<resultList.size();i++){
				if(StringUtils.isNotBlank(resultList.get(i).getDeptCode())){
					if(resultList.get(i).getDeptCode().equals(key)){
						resultList.get(i).setDeptCost(deptCostMap.get(key));
					}
				}
			}
		}
		Collections.sort(resultList, new Comparator(){

			@Override
			public int compare(Object o1, Object o2) {
				 return ((java.text.RuleBasedCollator)java.text.Collator.getInstance(java.util.Locale.CHINA)).compare(((StatisticsVo)o1).getDeptCode(), ((StatisticsVo)o2).getDeptCode());  
			}
	    	
	    });  
		int size= (int) new MongoBasicDao().findAllCountBy("MZYSKDGZL_DAY", where).longValue();
		map.put("rows", resultList);
		map.put("total", size);
		return map;
	}
	private StatisticsVo setCost(StatisticsVo vo, List<CustomVo> list) {
		for(CustomVo cvo : list){
			String code = cvo.getName();
			switch(code.trim()){
				case "西药费"://西药费
					vo.setWesternCost(cvo.getValue());
					break;
				case "中成药费"://中成药费
					vo.setChineseCost(cvo.getValue());
					break;
				case "中草药费"://中草药费
					vo.setHerbalCost(cvo.getValue());
					break;
				case "床位费"://床位费
					vo.setChuangweiCost(cvo.getValue());
					break;
				case "治疗费"://治疗费
					vo.setTreatmentCost(cvo.getValue());
					break;
				case "检查费"://检查费
					vo.setInspectCost(cvo.getValue());
					break;
				case "放射费"://放射费
					vo.setRadiationCost(cvo.getValue());
					break;
				case "化验费"://化验费
					vo.setTestCost(cvo.getValue());
					break;
				case "手术费"://手术费
					vo.setShoushuCost(cvo.getValue());
					break;
				case "输血费"://输血费
					vo.setBloodCost(cvo.getValue());
					break;
				case "输氧费"://输氧费
					vo.setO2Cost(cvo.getValue());
					break;
				case "材料费"://材料费
					vo.setCailiaoCost(cvo.getValue());
					break;
				case "其他"://其他
					vo.setOtherCost(cvo.getValue());
					break;
				case "疫苗费"://疫苗费
					vo.setYimiaoCost(cvo.getValue());
					break;
			}
		}
		return vo;
		
	}
}
