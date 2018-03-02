package cn.honry.inner.statistics.incomeSta.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import cn.honry.inner.statistics.incomeSta.dao.IncomeStaDao;
import cn.honry.inner.statistics.incomeSta.service.IncomeStaService;
import cn.honry.inner.statistics.incomeSta.vo.IncomeDetailVo;
import cn.honry.inner.statistics.incomeSta.vo.IncomeStatisticsVO;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Transactional
@Service("incomeStaService")
@SuppressWarnings({"all"})
public class IncomeStaServiceImpl implements IncomeStaService {

	@Autowired
	@Qualifier(value="incomeStaDao")
	private IncomeStaDao incomeStaDao;
	@Autowired
	@Qualifier(value = "innerRegisterInfoGzltjDao")
	private InnerRegisterInfoGzltjDao innerRegisterInfoGzltjDao;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	
	@Override
	public void init(String menuAlias, String type, String date) {
		//根据日期获取当天的统计数据,存入mongodb中
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(new Date());
		
		List<IncomeStatisticsVO> list = incomeStaDao.queryVo(date);
		if(list==null||list.size()==0){
			return ;
		}
		
		Double totalFee = 0.0;//总费用
		HashMap<String, Double> feeMap = new HashMap<String, Double>();//各项费用map
		HashMap<String, Double> areaMap = new HashMap<String, Double>();//各院区费用map
		
		for(IncomeStatisticsVO svo : list){
			String feeName = svo.getFeename();//费别
			String areaName = svo.getAreaname();//院区
			Double fee = svo.getFee();//费用
			//统计相同院区各费别费用
			if(feeMap.get(feeName)==null){
				feeMap.put(feeName, fee);
			}else{
				Double fn = feeMap.get(feeName);
				feeMap.put(feeName, fn+fee);
			}
			//统计相同院区的费用
			if(areaMap.get(areaName)==null){
				areaMap.put(areaName, fee);
			}else{
				Double areaFee = areaMap.get(areaName);
				areaMap.put(areaName, areaFee+fee);
			}
			totalFee += fee;
		}
		
		List<IncomeDetailVo> feeList = new ArrayList<IncomeDetailVo>();//各费别费用list
		String redKey = "feeNameListSort";
		List<String> nameList =  (List<String>) redisUtil.get(redKey);//[西药费, 中成药费, 中草药费, 床位费, 治疗费, 检查费, 放射费, 化验费, 手术费, 输血费, 输氧费, 材料费, 其他, 护理费, 麻醉费, 诊察费, 其他, 疫苗费]
		nameList = incomeStaDao.queryNameList();//记录mongo的name存放顺序[西药费, 中成药费, 中草药费, 床位费, 治疗费, 检查费, 放射费, 化验费, 手术费, 输血费, 输氧费, 材料费, 其他, 护理费, 麻醉费, 诊察费, 其他, 疫苗费]
		redisUtil.set(redKey, nameList);
		if(nameList==null||nameList.size()<=0){
			redisUtil.persist(redKey);
		}
		for(String name : nameList){
			IncomeDetailVo dvo = new IncomeDetailVo();
			dvo.setName(name);
			if(feeMap.get(name)==null){
				continue;
			}
			dvo.setValue(feeMap.get(name));
			feeList.add(dvo);
		}
		
		List<IncomeDetailVo> areaList = new ArrayList<IncomeDetailVo>();//各院区费用list
		for(Map.Entry<String, Double> entry : areaMap.entrySet()){
			IncomeDetailVo dvo = new IncomeDetailVo();
			dvo.setName(entry.getKey());
			dvo.setValue(entry.getValue());
			areaList.add(dvo);
		}
		//处理环比时间
		String hbDate = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(date),-1));//环比上期时间
		//根据环比上期时间。查询环比上期数据
		BasicDBObject whereHb = new BasicDBObject();
		whereHb.put("date", hbDate);
		DBCursor cursorHb = new MongoBasicDao().findAlldata(menuAlias+"_DAY", whereHb);
		String hbTime = DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(date),-5))+","+ 
				DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(date),-4))+","+
				DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(date),-3))+","+ 
				DateUtils.formatDateY_M_D(DateUtils.addDay(DateUtils.parseDateY_M_D(date),-2))+","+hbDate+","+date;
		String hbData = "0.0,0.0,0.0,0.0,0.0,"+totalFee;
		if(cursorHb.hasNext()){
			Object object = cursorHb.next().get("value");
			String objectS = object.toString();
			hbTime = objectS.substring(objectS.indexOf("xAxisByHuanbiByDay\":["), objectS.length());
			hbTime = hbTime.substring(hbTime.indexOf("[")+1, hbTime.indexOf("]"));
			hbTime = hbTime.substring(hbTime.indexOf(",")+1,hbTime.length())+",\""+date+"\"";
			hbTime = hbTime.replaceAll("\"", "");
			hbData = objectS.substring(objectS.indexOf("huanbiByDay\":["), objectS.length());
			hbData = hbData.substring(hbData.indexOf("[")+1, hbData.indexOf("]"));
			hbData = hbData.substring(hbData.indexOf(",")+1,hbData.length())+","+totalFee;
		}
		//处理同比时间
		String tbDate = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-1));//同比上期时间
		//根据同比上期时间。查询同比上期数据
		BasicDBObject whereTb = new BasicDBObject();
		whereTb.put("date", tbDate);
		DBCursor cursorTb = new MongoBasicDao().findAlldata(menuAlias+"_DAY", whereTb);
		String tbTime = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-5))+","+ 
				DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-4))+","+
				DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-3))+","+ 
				DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-2))+","+hbDate+","+date;
		String tbData = "0.0,0.0,0.0,0.0,0.0,"+totalFee;
		if(cursorTb.hasNext()){
			Object object = cursorTb.next().get("value");
			String objectS = object.toString();
			tbTime = objectS.substring(objectS.indexOf("xAxisByTongbiByDay\":["), objectS.length());
			tbTime = tbTime.substring(tbTime.indexOf("[")+1, tbTime.indexOf("]"));//["2012-03-30","2013-03-30","2014-03-30","2015-03-30","2016-03-30","2017-03-30"
			tbTime = tbTime.substring(tbTime.indexOf(",")+1,tbTime.length())+",\""+date+"\"";
			tbTime = tbTime.replaceAll("\"", "");
			tbData = objectS.substring(objectS.indexOf("tongbiByDay\":["), objectS.length());
			tbData = tbData.substring(tbData.indexOf("[")+1, tbData.indexOf("]"));
			tbData = tbData.substring(tbData.indexOf(",")+1,tbData.length())+","+totalFee;
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if("1".equals(type)){
			map.put("feeOfDay", feeList);
			List<Double> ldTb = new ArrayList<>();
			do{
				ldTb.add(Double.parseDouble(tbData.split(",")[0]));
				tbData=tbData.substring(tbData.indexOf(",")+1, tbData.length());
			}while(tbData.contains(","));
			ldTb.add(Double.parseDouble(tbData));
			map.put("tongbiByDay", ldTb);
			map.put("dayTotCost", totalFee);
			map.put("xAxisByTongbiByDay", Arrays.asList(tbTime.split(",")));
			map.put("xAxisByHuanbiByDay", Arrays.asList(hbTime.split(",")));
			List<Double> ldHb = new ArrayList<>();
			do{
				ldHb.add(Double.parseDouble(hbData.split(",")[0]));
				hbData=hbData.substring(hbData.indexOf(",")+1, hbData.length());
			}while(hbData.contains(","));
			ldHb.add(Double.parseDouble(hbData));
			map.put("huanbiByDay", ldHb);
			map.put("areaOfDay", areaList);
		}
		if(map.size()==0){
			return ;
		}
		
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
		
		//更新当月的统计数据
		init_MonthOrYear(menuAlias, "2", date, nameList);
		//更新当年的统计数据
		init_MonthOrYear(menuAlias, "3", date, nameList);
	}
	/**
	 * 计算当月或当年的数据
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 开始时间
	 */
	public void init_MonthOrYear(String menuAlias,String type,String date,List<String> nameList){
		//更新当月或当年的统计数据(从_DAY或_Month表中查询数据,重新计算)
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(new Date());
		
		String dateM = date.substring(0, 7);//当月
		String dateY = date.substring(0, 4);//当年
		
		Double totalFee = 0.0;//总费用
		HashMap<String, Double> feeMap = new HashMap<String, Double>();//各项费用map
		HashMap<String, Double> areaMap = new HashMap<String, Double>();//各院区费用map
		List<IncomeDetailVo> feeList = new ArrayList<IncomeDetailVo>();//各费别费用list
		List<IncomeDetailVo> areaList = new ArrayList<IncomeDetailVo>();//各院区费用list
		
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
		TypeToken<List<IncomeDetailVo>> typeToken = new TypeToken<List<IncomeDetailVo>>(){};
		if("2".equals(type)){
			while(cursor.hasNext()){
				Object object = cursor.next().get("value");
				String objectS = object.toString();
				String s1 = objectS.substring(objectS.indexOf("feeOfDay\":["), objectS.length());
				s1 = s1.substring(s1.indexOf("["), s1.indexOf("]")+1);
				String s2 = objectS.substring(objectS.indexOf("areaOfDay\":["), objectS.length());
				s2 = s2.substring(s2.indexOf("["), s2.indexOf("]")+1);
				
				try {
					if(StringUtils.isNotBlank(s1)){
						//处理费别数据
						feeList.addAll(JSONUtils.fromJson(s1,  typeToken));
					}
					if(StringUtils.isNotBlank(s2)){
						//处理院区数据
						areaList.addAll(JSONUtils.fromJson(s2, typeToken));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//当月总费用
				String t1 = objectS.substring(objectS.indexOf("dayTotCost\":"), objectS.length());
				t1 = t1.substring(t1.indexOf(":")+1, t1.indexOf(","));
				totalFee += Double.parseDouble(t1);
			}
		}
		if("3".equals(type)){
			while(cursor.hasNext()){
				Object object = cursor.next().get("value");
				String objectS = object.toString();
				String s1 = objectS.substring(objectS.indexOf("feeOfMonth\":["), objectS.length());
				s1 = s1.substring(s1.indexOf("["), s1.indexOf("]")+1);
				String s2 = objectS.substring(objectS.indexOf("areaOfMonth\":["), objectS.length());
				s2 = s2.substring(s2.indexOf("["), s2.indexOf("]")+1);
				try {
					if(StringUtils.isNotBlank(s1)){
						//处理费别数据
						feeList.addAll(JSONUtils.fromJson(s1,  typeToken));
					}
					if(StringUtils.isNotBlank(s2)){
						//处理院区数据
						areaList.addAll(JSONUtils.fromJson(s2, typeToken));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//当月总费用
				String t1 = objectS.substring(objectS.indexOf("monthTotCost\":"), objectS.length());
				t1 = t1.substring(t1.indexOf(":")+1, t1.indexOf(","));
				totalFee += Double.parseDouble(t1);
			}
		}
		//整合费别数据,统计相同院区各费别费用
		for(IncomeDetailVo idvo : feeList){
			if(feeMap.get(idvo.getName())==null){
				feeMap.put(idvo.getName(), idvo.getValue());
			}else{
				Double fn = feeMap.get(idvo.getName());
				feeMap.put(idvo.getName(), fn+idvo.getValue());
			}
		}
		//整合院区数据,统计相同院区的费用
		for(IncomeDetailVo idvo : areaList){
			if(areaMap.get(idvo.getName())==null){
				areaMap.put(idvo.getName(), idvo.getValue());
			}else{
				Double fn = areaMap.get(idvo.getName());
				areaMap.put(idvo.getName(), fn+idvo.getValue());
			}
		}
		
		DBObject query = new BasicDBObject();
		Date d = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		if("2".equals(type)){
			//处理环比时间
			String hbDate = DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-1)).substring(0, 7);//环比上期时间
			//根据环比上期时间。查询环比上期数据
			BasicDBObject whereHb = new BasicDBObject();
			whereHb.put("date", hbDate);
			DBCursor cursorHb = new MongoBasicDao().findAlldata(menuAlias+"_MONTH", whereHb);
			String hbTime = DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-5)).substring(0, 7)+","+ 
					DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-4)).substring(0, 7)+","+
					DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-3)).substring(0, 7)+","+ 
					DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-2)).substring(0, 7)+","+hbDate+","+dateM;
			String hbData = "0.0,0.0,0.0,0.0,0.0,"+totalFee;
			if(cursorHb.hasNext()){
				Object object = cursorHb.next().get("value");
				String objectS = object.toString();
				hbTime = objectS.substring(objectS.indexOf("xAxisByHuanbiByMonth\":["), objectS.length());
				hbTime = hbTime.substring(hbTime.indexOf("[")+1, hbTime.indexOf("]"));
				hbTime = hbTime.substring(hbTime.indexOf(",")+1,hbTime.length())+",\""+date+"\"";
				hbTime = hbTime.replaceAll("\"", "");
				hbData = objectS.substring(objectS.indexOf("huanbiByMonth\":["), objectS.length());
				hbData = hbData.substring(hbData.indexOf("[")+1, hbData.indexOf("]"));//1.4290722195E8,1.4424682115E8,1.5029688281E8,1.3207338023E8,1.5777730958E8,1.7863130518E8
				hbData = hbData.substring(hbData.indexOf(",")+1,hbData.length())+","+totalFee;//1.4424682115E8,1.5029688281E8,1.3207338023E8,1.5777730958E8,1.7863130518E8,8.540275866000013E7
			}
			//处理同比时间
			String tbDate = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-1)).substring(0, 7);//同比上期时间
			//根据同比上期时间。查询同比上期数据
			BasicDBObject whereTb = new BasicDBObject();
			whereTb.put("date", tbDate);
			DBCursor cursorTb = new MongoBasicDao().findAlldata(menuAlias+"_MONTH", whereTb);
			String tbTime = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-5)).substring(0, 7)+","+ 
					DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-4)).substring(0, 7)+","+
					DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-3)).substring(0, 7)+","+ 
					DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-2)).substring(0, 7)+","+tbDate+","+dateM;
			String tbData = "0.0,0.0,0.0,0.0,0.0,"+totalFee;
			if(cursorTb.hasNext()){
				Object object = cursorTb.next().get("value");
				String objectS = object.toString();
				tbTime = objectS.substring(objectS.indexOf("xAxisByTongbiByMonth\":["), objectS.length());
				tbTime = tbTime.substring(tbTime.indexOf("[")+1, tbTime.indexOf("]"));//["2012-03-30","2013-03-30","2014-03-30","2015-03-30","2016-03-30","2017-03-30"
				tbTime = tbTime.substring(tbTime.indexOf(",")+1,tbTime.length())+",\""+date+"\"";
				tbTime = tbTime.replaceAll("\"", "");
				tbData = objectS.substring(objectS.indexOf("tongbiByMonth\":["), objectS.length());
				tbData = tbData.substring(tbData.indexOf("[")+1, tbData.indexOf("]"));//5.146614331E7,7.259453083E7,8.972580239E7,1.0770563115E8,1.2010813378E8,1.4261698919E8
				tbData = tbData.substring(tbData.indexOf(",")+1,tbData.length())+","+totalFee;//7.259453083E7,8.972580239E7,1.0770563115E8,1.2010813378E8,1.4261698919E8,8.540275866000013E7
			}
			List<IncomeDetailVo> feeListM = new ArrayList<IncomeDetailVo>();//各费别费用list
			for(String feeName : nameList){
				IncomeDetailVo dvo = new IncomeDetailVo();
				dvo.setName(feeName);
				if(feeMap.get(feeName)==null){
					continue;
				}
				dvo.setValue(feeMap.get(feeName));
				feeListM.add(dvo);
			}
			
			List<IncomeDetailVo> areaListM = new ArrayList<IncomeDetailVo>();//各院区费用list
			for(Map.Entry<String, Double> entry : areaMap.entrySet()){
				IncomeDetailVo dvo = new IncomeDetailVo();
				dvo.setName(entry.getKey());
				dvo.setValue(entry.getValue());
				areaListM.add(dvo);
			}
			query.put("date", dateM);
			d=DateUtils.parseDateY_M(dateM);
			menuAlias+="_MONTH";
			map.put("feeOfMonth", feeListM);
			List<Double> ldTb = new ArrayList<>();
			do{
				ldTb.add(Double.parseDouble(tbData.split(",")[0]));
				tbData=tbData.substring(tbData.indexOf(",")+1, tbData.length());
			}while(tbData.contains(","));
			ldTb.add(Double.parseDouble(tbData));
			map.put("tongbiByMonth", ldTb);
			map.put("monthTotCost", totalFee);
			List<String> lsTb = Arrays.asList(tbTime.split(","));
			map.put("xAxisByTongbiByMonth", lsTb);
			List<String> lsHb = Arrays.asList(hbTime.split(","));
			map.put("xAxisByHuanbiByMonth", lsHb);
			List<Double> ldHb = new ArrayList<>();
			do{
				ldHb.add(Double.parseDouble(hbData.split(",")[0]));
				hbData=hbData.substring(hbData.indexOf(",")+1, hbData.length());
			}while(hbData.contains(","));
			ldHb.add(Double.parseDouble(hbData));
			map.put("huanbiByMonth", ldHb);
			map.put("areaOfMonth", areaListM);
			date = dateM;
		}
		if("3".equals(type)){
			//处理环比时间
			String hbDate = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(date),-1)).substring(0, 4);//环比上期时间
			//根据环比上期时间。查询环比上期数据
			BasicDBObject whereHb = new BasicDBObject();
			whereHb.put("date", hbDate);
			DBCursor cursorHb = new MongoBasicDao().findAlldata(menuAlias+"_YEAR", whereHb);
			String hbTime = DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-5)).substring(0, 4)+","+ 
					DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-4)).substring(0, 4)+","+
					DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-3)).substring(0, 4)+","+ 
					DateUtils.formatDateY_M_D(DateUtils.addMonth(DateUtils.parseDateY_M_D(date),-2)).substring(0, 4)+","+hbDate+","+dateY;
			String hbData = "0.0,0.0,0.0,0.0,0.0,"+totalFee;
			if(cursorHb.hasNext()){
				Object object = cursorHb.next().get("value");
				String objectS = object.toString();
				hbTime = objectS.substring(objectS.indexOf("xAxisByHuanbiByYear\":["), objectS.length());
				hbTime = hbTime.substring(hbTime.indexOf("[")+1, hbTime.indexOf("]"));
				hbTime = hbTime.substring(hbTime.indexOf(",")+1,hbTime.length())+",\""+date+"\"";
				hbTime = hbTime.replaceAll("\"", "");
				hbData = objectS.substring(objectS.indexOf("huanbiByYear\":["), objectS.length());
				hbData = hbData.substring(hbData.indexOf("[")+1, hbData.indexOf("]"));
				hbData = hbData.substring(hbData.indexOf(",")+1,hbData.length())+","+totalFee;
			}
			
			List<IncomeDetailVo> feeListY = new ArrayList<IncomeDetailVo>();//各费别费用list
			for(String feeName : nameList){
				IncomeDetailVo dvo = new IncomeDetailVo();
				dvo.setName(feeName);
				if(feeMap.get(feeName)==null){
					continue;
				}
				dvo.setValue(feeMap.get(feeName));
				feeListY.add(dvo);
			}
			
			List<IncomeDetailVo> areaListY = new ArrayList<IncomeDetailVo>();//各院区费用list
			for(Map.Entry<String, Double> entry : areaMap.entrySet()){
				IncomeDetailVo dvo = new IncomeDetailVo();
				dvo.setName(entry.getKey());
				dvo.setValue(entry.getValue());
				areaListY.add(dvo);
			}
			
			query.put("date", dateY);
			d=DateUtils.parseDateY(dateY);
			menuAlias+="_YEAR";
			map.put("feeOfYear", feeListY);
			map.put("yearTotCost", totalFee);
			List<String> ls = Arrays.asList(hbTime.split(","));
			map.put("xAxisByHuanbiByYear", ls);
			List<Double> ld = new ArrayList<>();
			do{
				ld.add(Double.parseDouble(hbData.split(",")[0]));
				hbData=hbData.substring(hbData.indexOf(",")+1, hbData.length());
			}while(hbData.contains(","));
			ld.add(Double.parseDouble(hbData));
			map.put("huanbiByYear", ld);
			map.put("areaOfYear", areaListY);
			date = dateY;
		}
		if(map.size()==0){
			return ;
		}
		
		String json = JSONUtils.toJson(map);
		Document document = new Document();
		document.append("date", date);
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
