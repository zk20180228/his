package cn.honry.inner.statistics.totalIncomeCount.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.formula.ptg.AreaNPtg;
import org.bson.Document;
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
import cn.honry.inner.statistics.hosIncomeCount.utils.LastDayUtils;
import cn.honry.inner.statistics.incomeSta.dao.IncomeStaDao;
import cn.honry.inner.statistics.registerInfoGzltj.dao.InnerRegisterInfoGzltjDao;
import cn.honry.inner.statistics.totalIncomeCount.dao.TotalIncomeCountDao;
import cn.honry.inner.statistics.totalIncomeCount.service.TotalIncomeCountService;
import cn.honry.inner.statistics.totalIncomeCount.vo.MapVo;
import cn.honry.inner.statistics.totalIncomeCount.vo.TotalIncomeCountVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
/**
 * @Description:总收入情况统计初始化数据到mongodb中
 * @author:zhangkui
 * @time:2017年6月21日 下午5:44:27
 */
@Transactional
@Service("totalIncomeCountService")
@SuppressWarnings({"all"})
public class TotalIncomeCountServiceImpl implements TotalIncomeCountService{
	
	@Resource
	private TotalIncomeCountDao totalIncomeCountDao;
	
	@Resource
	private RedisUtil redisUtil;//将费别的名排好序存入缓存
	
	@Resource
	private IncomeStaDao incomeStaDao;//缓存中不存在排好序的费别名时，从数据库查
	

	public void init_ZSRQKTJ_dataByDay(String menuAlias,String type,String date) {
	
		//根据日期获取当天的统计数据,存入mongodb中
		MongoLog mong = new MongoLog();
		mong.setCountStartTime(new Date());
		
		List<MapVo> list = totalIncomeCountDao.init_ZSRQKTJ_dataByDay(date);
		
		if(list==null||list.size()==0){
			return;
		}
	
		//得到总费用
		Double fee= 0.0;
		HashMap<String, MapVo> feeMap = new HashMap<String,MapVo>();//各项费用map
		HashMap<String, MapVo> areaMap = new HashMap<String,MapVo>();
		for(MapVo v:list){
				fee+=v.getValue();//Double.parseDouble();
				String areaName = v.getAreaName();//院区名字
				String name = v.getName();//费别名字
				MapVo feeVo = feeMap.get(name);
				if(feeVo!=null){
					feeVo.setValue(feeVo.getValue()+v.getValue());
				}else{
					MapVo mapVo = new MapVo();
					mapVo.setName(name);
					mapVo.setValue(v.getValue());
					feeMap.put(name, mapVo);
				}
				MapVo areaVo = areaMap.get(areaName);
				if(areaVo!=null){
					areaVo.setValue(areaVo.getValue()+v.getValue());
				}else{
					MapVo mapVo = new MapVo();
					mapVo.setValue(v.getValue());
					mapVo.setName(areaName);//把name的值变为院区的名字，最终需要的是name属性
					areaMap.put(areaName, mapVo);
				}
				
		}
		
		List<MapVo> feeList =  new ArrayList<MapVo>();
		List<MapVo> areaList=  new ArrayList<MapVo>();
		List<String> nameList=null;
		try {
			
			//保证费别按缓存中的顺序来取
			String redKey ="feeNameListSort";
			nameList = (List<String>) redisUtil.get(redKey);
			if(nameList==null||nameList.size()==0){
				nameList=incomeStaDao.queryNameList();//费别按要求的顺序存放的list
				redisUtil.set(redKey, nameList);//设置到缓存中
				redisUtil.persist(redKey);
			}
		} catch (Exception e) {
			nameList=incomeStaDao.queryNameList();//redis如果挂了，保证能走数据库
			if(nameList==null){
				nameList=new ArrayList<String>();
			}
			e.printStackTrace();
		}
		
		for(String name:nameList){
			MapVo vo = feeMap.get(name);
			if(vo!=null){
				feeList.add(vo);
			}
		}
		
		areaList.addAll(areaMap.values());
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		if("1".equals(type)){//按天更新
			map.put("dayTotCost", fee);
			map.put("feeOfDay", feeList);
			map.put("areaOfDay", areaList);
		}
		if(map.size()==0){
			return ;
		}
	
		//更新mongo中当天的数据
		String json = JSONUtils.toJson(map);
		Document document = new Document();
		document.append("date", date);
		document.append("value", json);
		
		BasicDBObject query = new BasicDBObject();
		query.put("date", date);
		try {
			new MongoBasicDao().remove(menuAlias+"_DAY", query);//删除原来的数据
			new MongoBasicDao().insertData(menuAlias+"_DAY", document);//添加新的数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Date d = DateUtils.parseDateY_M_D(date);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias + "_DAY");
		mong.setCountEndTime(new Date());
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());
		totalIncomeCountDao.save(mong);
		this.init_ZSRQKTJ_dataMonthOrYear(menuAlias, "2", date);//级联更新月表
		this.init_ZSRQKTJ_dataMonthOrYear(menuAlias, "3", date);//级联更新年表
		
	}

	
	
	//级联更新月，年的方法
	/**
	 * @Description:TODO  配合init_ZSRQKTJ_dataByDay一起更新mongo中ZSRQKTJ日月年表
	 * @param menuAlias 栏目名，和表名也有一定的关系
	 * @param type 取值：2,3分别代表更新月表，年表
	 * @param date 时间(格式为:YYYY-MM-DD)
	 * void
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月21日 下午3:10:34
	 */
	public void init_ZSRQKTJ_dataMonthOrYear(String menuAlias,String type,String date){
			
			//更新当月或当年的统计数据(从_DAY或_Month表中查询数据,重新计算)
			MongoLog mong = new MongoLog();
			mong.setCountStartTime(new Date());
			
			String dateM = date.substring(0, 7);//当年月
			String dateY = date.substring(0, 4);//当年
			
			List<TotalIncomeCountVo> list = new ArrayList<TotalIncomeCountVo>();
			BasicDBObject obj1 = new BasicDBObject();
			BasicDBObject obj2 = new BasicDBObject();//这个东西相当于一对{}
			String queryMenuAlias=menuAlias;//查询时用的表名称
			if("2".equals(type)){//更新月，数据由日表而来
				obj1.append("date", new BasicDBObject("$gte",dateM+"-01"));//大于等于本月1号
				String lastDay=dateM+"-"+LastDayUtils.getLastDay(date);//得到查询月最后一天
				obj2.append("date", new BasicDBObject("$lte",lastDay));//小于等于查询月最后一天，因为可能会更新历史表的数据
				queryMenuAlias+="_DAY";//从日表中查询数据统计到月表中
			}
			if("3".equals(type)){//按年更新，数据由月表而来
				obj1.append("date", new BasicDBObject("$gte",dateY+"-01"));//大于等于1月
				obj2.append("date", new BasicDBObject("$lte",dateY+"-12"));//小于等于当年的最后一月，因为可能会更新历史表的数据
				queryMenuAlias+="_MONTH";//从月表中查询数据统计到年表中
			}
		
			BasicDBList conList = new BasicDBList();//这个东西相当于一对[]
			conList.add(obj1);
			conList.add(obj2);
			BasicDBObject where = new BasicDBObject();//构建查询条件
			where.append("$and", conList);
			
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMenuAlias, where);
			if(cursor!=null){
				while(cursor.hasNext()){
					Object object = cursor.next().get("value");
					if(object!=null){
						String s = object.toString().replace("day", "").replace("Day", "").replace("month", "").replace("Month", "").replace("year", "").replace("Year", "");//把这些字符串去掉，为了下面更好的封装数据
						try {
							TotalIncomeCountVo vo=JSONUtils.fromJson(s, new TypeToken<TotalIncomeCountVo>(){});
							list.add(vo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		if(list.size()==0){
			return;
		}
		
		//计算月/年TotCost
		Double totCost=0.0;
		for(TotalIncomeCountVo v:list){
			totCost+=Double.parseDouble(v.getTotCost());
		}
		
		//计算月/年费别	//计算院区收入
		ArrayList<MapVo> freeOf = new ArrayList<MapVo>();
		LinkedHashMap<String,MapVo>  linkedHashMap = new LinkedHashMap<String, MapVo>();//保证顺序
		ArrayList<MapVo> areaOf = new ArrayList<MapVo>();
		LinkedHashMap<String,MapVo>  linkedHashMap02 = new LinkedHashMap<String, MapVo>();//保证顺序
		for(TotalIncomeCountVo v:list){
			List<MapVo> fee = v.getFeeOf();
			List<MapVo> area = v.getAreaOf();
			//费别
			if(fee!=null&&fee.size()>0){
				for(MapVo m:fee){
					String name= m.getName();
					MapVo mapVo = linkedHashMap.get(name);
					if(mapVo!=null){
						Double a = mapVo.getValue();
						Double b = m.getValue();
						mapVo.setValue(a+b);
					}else{
						MapVo vo = new MapVo();
						vo.setName(name);
						vo.setValue(m.getValue());
						linkedHashMap.put(name, vo);
					}
					
				}
			}
			
			if(area!=null&&area.size()>0){
				for(MapVo m:area){
					String areaName=m.getName();
					MapVo mapVo = linkedHashMap02.get(areaName);
					if(mapVo!=null){
						Double a = mapVo.getValue();
						Double b = m.getValue();
						mapVo.setValue(a+b);
					}else{
						MapVo vo = new MapVo();
						vo.setName(areaName);
						vo.setValue(m.getValue());
						linkedHashMap02.put(areaName, vo);
					}
					
				}
			}
			
			
		}
		List<String> nameList=null;
		try {
			
			//保证费别按缓存中的顺序来取
			String redKey ="feeNameListSort";
			nameList = (List<String>) redisUtil.get(redKey);
			if(nameList==null||nameList.size()==0){
				nameList=incomeStaDao.queryNameList();//费别按要求的顺序存放的list
				redisUtil.set(redKey, nameList);//设置到缓存中
				redisUtil.persist(redKey);
			}
		} catch (Exception e) {
			nameList=incomeStaDao.queryNameList();//redis如果挂了，去数据库查
			if(nameList==null){
				nameList=new ArrayList<String>();
			}
			e.printStackTrace();
		}
		for(String name:nameList){
			MapVo vo = linkedHashMap.get(name);
			if(vo!=null){
				freeOf.add(vo);
			}
		}
		
		areaOf.addAll(linkedHashMap02.values());//汇总成当月/年这一条数据的院区总收入
			
		Map<String,Object> map =new HashMap<String, Object>();
		if("2".equals(type)){
			map.put("monthTotCost", totCost);
			map.put("feeOfMonth", freeOf);
			map.put("areaOfMonth", areaOf);
		}
		if("3".equals(type)){
			map.put("yearTotCost", totCost);
			map.put("feeOfYear", freeOf);
			map.put("areaOfYear", areaOf);
		}
		
		//把map转换为json格式字符串
		String json= JSONUtils.toJson(map);
		
		DBObject query = new BasicDBObject();
		Document document = new Document();
		document.append("value", json);
		Date d = null;
		if("2".equals(type)){
			document.append("date", dateM);//年月
			query.put("date", dateM);
			d=DateUtils.parseDateY_M(dateM);
			menuAlias+="_MONTH";//月表
		}
		if("3".equals(type)){
			document.append("date", dateY);//年
			query.put("date", dateY);
			d=DateUtils.parseDateY(dateY);
			menuAlias+="_YEAR";//年表
		}
		
		try {
			new MongoBasicDao().remove(menuAlias, query);//删除原来的数据
			new MongoBasicDao().insertData(menuAlias, document);//添加新数据	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		// 记录日志
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setMenuType(menuAlias);
		mong.setCountEndTime(new Date());
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());

		totalIncomeCountDao.save(mong);// 保存日志
		
	}
	
	
	
	
}
