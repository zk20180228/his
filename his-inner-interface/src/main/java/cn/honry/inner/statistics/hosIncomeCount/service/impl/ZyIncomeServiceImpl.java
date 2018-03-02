package cn.honry.inner.statistics.hosIncomeCount.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.hosIncomeCount.dao.ZyIncomeDao;
import cn.honry.inner.statistics.hosIncomeCount.service.ZyIncomeService;
import cn.honry.inner.statistics.hosIncomeCount.utils.LastDayUtils;
import cn.honry.inner.statistics.hosIncomeCount.utils.SortMapUtils;
import cn.honry.inner.statistics.hosIncomeCount.vo.MapVo;
import cn.honry.inner.statistics.hosIncomeCount.vo.ZyIncomeVo;
import cn.honry.inner.statistics.incomeSta.dao.IncomeStaDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;
@Transactional
@Service("zyIncomeService")
@SuppressWarnings({"all"})
public class ZyIncomeServiceImpl implements ZyIncomeService {
	
		@Resource
		private ZyIncomeDao zyIncomeDao;
		@Resource
		private RedisUtil redisUtil;//将费别的名排好序存入缓存
		@Resource
		private IncomeStaDao incomeStaDao;//缓存中不存在排好序的费别名时，从数据库查
		
		public void init_ZYSRTJ_dataByDay(String menuAlias,String type,String date){
			
			//根据日期获取当天的统计数据,存入mongodb中
			MongoLog mong = new MongoLog();
			mong.setCountStartTime(new Date());
			
			List<MapVo> list = zyIncomeDao.init_ZYSRTJ_dataByDay(date);
			if(list==null||list.size()==0){
				return ;
			}
			ArrayList<MapVo> feeList = new ArrayList<MapVo>();//费别
			HashMap<String,MapVo> feeMap = new HashMap<String, MapVo>();
			
			ArrayList<MapVo> deptList = new ArrayList<MapVo>();//科室
			HashMap<String,Double> deptMap = new HashMap<String, Double>();
			
			ArrayList<MapVo> totCostList = new ArrayList<MapVo>();
			Double totCost=0.0;
			for(MapVo m:list){
				totCost+=m.getValue();
				String name= m.getName();
				String deptName=m.getDeptName();
				//费别
				MapVo vo = feeMap.get(name);
				if(vo!=null){
					vo.setValue(m.getValue()+vo.getValue());
				}else{
					MapVo mapVo = new MapVo();
					mapVo.setName(name);
					mapVo.setValue(m.getValue());
					feeMap.put(name, mapVo);
				}
				//科室
				Double d = deptMap.get(deptName);
				if(d!=null){
					d+=m.getValue();
				}else{
					Double value=m.getValue();
					deptMap.put(deptName, value);
				}
				
			}
			if(feeMap.size()>=0){
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
			}
			
			// 科室的前10
			if (deptMap.size() > 0) {
				Entry<String, Double>[] entries = SortMapUtils.reverseMap(deptMap);
				int i = 0;
				Double vv = 0.0;
				for (Entry<String, Double> e : entries) {
					if (i < 10) {//如果科室不够10个,那么其他是0.0
						MapVo vo = new MapVo();
						vo.setName(e.getKey());
						vo.setValue(e.getValue());
						deptList.add(vo);
	
					} else {
						vv += (Double) e.getValue();
					}
					i++;
				}
				MapVo vo = new MapVo();
				vo.setName("其他");
				vo.setValue(vv);
				deptList.add(vo);
			}
			MapVo vo = new MapVo();
			vo.setName("总收入");
			vo.setValue(totCost);
			totCostList.add(vo);
			
			//计算6日同比的条件
			//date-->yyyy-MM-dd
			String year = date.substring(0, 4);//得到年
			int yearInt = Integer.parseInt(year);//转换为整形
			String collectionName=menuAlias+"_DAY";
			ArrayList<MapVo> tongbiList = new ArrayList<MapVo>();
			
			//计算6日环比的条件
			Date dateY_M_D = DateUtils.parseDateY_M_D(date);
			ArrayList<MapVo> hubiList = new ArrayList<MapVo>();
			for(int i =5;i>=0;i--){
				String dateTime = new Integer(yearInt-i).toString()+date.substring(4);
				DBCursor tbCursor = new MongoBasicDao().findAlldata(collectionName, new BasicDBObject("date",dateTime));//{date:'2012-06-26'}
				
				String time = DateUtils.formatDateY_M_D(DateUtils.addDay(dateY_M_D, -i));
				DBCursor hbCursor = new MongoBasicDao().findAlldata(collectionName, new BasicDBObject("date",time));
				if(i!=0){
					if(tbCursor!=null){
						if(tbCursor.hasNext()){
							String  deptAndFee = tbCursor.next().get("deptAndFee").toString();
							try {
								ZyIncomeVo zyIncomeVo = JSONUtils.fromJson(deptAndFee, new TypeToken<ZyIncomeVo>(){});
								Double value=zyIncomeVo.getTotCost().get(0).getValue();
								MapVo mapVo = new MapVo();
								mapVo.setValue(value);
								mapVo.setName(dateTime);
								tongbiList.add(mapVo);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{//当这个时间的数据不存在的时候,给个默认值
							MapVo mapVo = new MapVo();
							mapVo.setValue(0.0);
							mapVo.setName(dateTime);
							tongbiList.add(mapVo);
						}
					}
					if(hbCursor!=null){
						if(hbCursor.hasNext()){
							String deptAndFee = hbCursor.next().get("deptAndFee").toString();
							try {
								ZyIncomeVo zyIncomeVo = JSONUtils.fromJson(deptAndFee, new TypeToken<ZyIncomeVo>(){});
								Double value=zyIncomeVo.getTotCost().get(0).getValue();
								MapVo mapVo = new MapVo();
								mapVo.setValue(value);
								mapVo.setName(time);
								hubiList.add(mapVo);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{//当这个时间的数据不存在的时候,给个默认值
							MapVo mapVo = new MapVo();
							mapVo.setValue(0.0);
							mapVo.setName(time);
							hubiList.add(mapVo);
						}
					}
				}else{//当i=0,时间实际上是传入的date，就没必要再查了
					MapVo mapVo = new MapVo();
					mapVo.setValue(totCost);
					mapVo.setName(dateTime);
					
					tongbiList.add(mapVo);
					hubiList.add(mapVo);
				}
			}
			
			ZyIncomeVo feeAndDept = new ZyIncomeVo();//费别科室总收入的vo
			feeAndDept.setDeptPies(deptList);
			feeAndDept.setFeePies(feeList);
			feeAndDept.setTotCost(totCostList);
			
			ZyIncomeVo tonghuanbi = new ZyIncomeVo();//同环比list
			tonghuanbi.setHuanbiBars(hubiList);
			tonghuanbi.setTongbiBars(tongbiList);
			
			//把vo转换为json格式的字符串
			String deptAndFeeJson=JSONUtils.toJson(feeAndDept);
			String tonghuanbiJson=JSONUtils.toJson(tonghuanbi);
			
			//插入mongo中
			Document document = new Document();
			document.append("deptAndFee", deptAndFeeJson);
			document.append("tonghuanbi", tonghuanbiJson);
			document.append("date", date);
			
			BasicDBObject query = new BasicDBObject();//{date:'2017-06-26'}
			query.put("date", date);
			try {
				new MongoBasicDao().remove(collectionName, query);//如果有查询日数据，先删除
				new MongoBasicDao().insertData(collectionName, document);//插入新的数据
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Date d = DateUtils.parseDateY_M_D(date);
			mong.setStartTime(d);
			mong.setEndTime(d);
			mong.setState(1);
			mong.setMenuType(collectionName);
			mong.setCountEndTime(new Date());
			mong.setTotalNum(1);
			mong.setCreateTime(new Date());
			
			zyIncomeDao.save(mong);//保存mongo操作日志
			
			this.init_ZYSRTJ_dataMonthOrYear(menuAlias, "2", date);//级联更新月
			this.init_ZYSRTJ_dataMonthOrYear(menuAlias, "3", date);//级联更新年
		}
		
	
		//级联更新月，年的方法
		/**
		 * @Description:TODO  配合init_ZYSRTJ_dataByDay一起更新mongo中ZYSRTJ日月年表
		 * @param menuAlias 栏目名，和表名也有一定的关系
		 * @param type 取值：2,3分别代表更新月表，年表
		 * @param date 时间(格式为:YYYY-MM-DD)
		 * void
		 * @exception:
		 * @author: zhangkui
		 * @time:2017年6月24日 上午10:00:41
		 */
		public void init_ZYSRTJ_dataMonthOrYear(String menuAlias,String type,String date){
			
			//更新当月或当年的统计数据(从_DAY或_Month表中查询数据,重新计算)
			MongoLog mong = new MongoLog();
			mong.setCountStartTime(new Date());
			
			//date--->yyyy-MM-dd
			String dateM=date.substring(0,7);//yyyy-MM
			String dateY=date.substring(0,4);//yyyy
			
			//构造mongo查询的条件如：{$and:[{date:{$gte:本月的第一天}},{date:{$lte:本月的最后一天}}]}
			BasicDBObject obj1 = new BasicDBObject();
			BasicDBObject obj2 = new BasicDBObject();
			String queryMenuAlias=menuAlias;//查询时用的表名称
			if("2".equals(type)){
				obj1.append("date", new BasicDBObject("$gte",dateM+"-01"));//大于等于月的1号
				String lastDay=dateM+"-"+LastDayUtils.getLastDay(date);
				obj2.append("date", new BasicDBObject("$lte",lastDay));//小于等于月的最后一天
				queryMenuAlias+="_DAY";//更新月表数据时查日表
			}
			if("3".equals(type)){
				obj1.append("date", new BasicDBObject("$gte",dateY+"01"));//大于等于本年的1月
				obj2.append("date", new BasicDBObject("$lte",dateY+"-12"));//小于等于本年的最后一月
				queryMenuAlias+="_MONTH";//更新年表数据时查月表
			}
			BasicDBList conList = new BasicDBList();
			conList.add(obj1);
			conList.add(obj2);
			BasicDBObject where = new BasicDBObject();
			where.append("$and", conList);
			DBCursor cursor = new MongoBasicDao().findAlldata(queryMenuAlias, where);
			List<ZyIncomeVo> list=new ArrayList<ZyIncomeVo>();
			if(cursor!=null){
				while(cursor.hasNext()){
					DBObject dbObject = cursor.next();
					
					//mongo中的格式-->{"feePies":[{"name":"西药费","value":7975701.920000011},{"name":"材料费","value":5627370.639999935},{"name":"化验费","value":2066456.0},{"name":"治疗费","value":1730627.1},{"name":"手术费","value":1244566.9000000001},{"name":"放射费","value":1039807.5},{"name":"检查费","value":911430.0},{"name":"床位费","value":391425.7999999998},{"name":"中成药费","value":267400.05000000016},{"name":"输血费","value":168690.0},{"name":"护理费","value":59295.75},{"name":"输氧费","value":36718.8},{"name":"诊察费","value":35267.0},{"name":"其他","value":23962.590000000004},{"name":"中草药费","value":3283.3999999999996}],"totCost":[{"name":"总收入","value":2.158200345000002E7}],"deptPies":[{"name":"心内科三","value":500037.08999999997},{"name":"急诊外科","value":476204.48999999993},{"name":"心内科二","value":430667.71},{"name":"心内科五","value":413045.93},{"name":"心外科二","value":404433.80999999994},{"name":"乳腺外科一","value":359437.7799999999},{"name":"神经重症病区","value":340718.82},{"name":"神经外科三","value":331522.29},{"name":"乳腺外二科1","value":300125.8699999998},{"name":"骨科五","value":296944.68000000005},{"name":"其他","value":1.7728864980000023E7}]}
					String deptAndFee= dbObject.get("deptAndFee").toString();
					//转换为vo
					try {
						ZyIncomeVo deptAndFeeVo= JSONUtils.fromJson(deptAndFee, new TypeToken<ZyIncomeVo>(){});//封装了费别list,总金额list,可是前10加其他
						list.add(deptAndFeeVo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		if(list.size()==0){
			return ;
		}	
		
		//计算月/年的toCost
		Double totCost=0.0;
		LinkedHashMap<String, MapVo> feeMapVo = new LinkedHashMap<String, MapVo>();
		
		//这个map存放的数据个数很可能会大于11，因为每天的科室排名都不相同，导致每月，每年都不相同，但是最后的前10最终会出现在这个集合中
		LinkedHashMap<String, Double> deptMapVo = new LinkedHashMap<String, Double>();//注意存放的是name和与之对应的value
		
		//计算月/年费别，科室前10
		for(ZyIncomeVo v:list){
			totCost+=v.getTotCost().get(0).getValue();//计算总金额
			List<MapVo> feePies = v.getFeePies();//费别list
			List<MapVo> deptPies = v.getDeptPies();//科室前10的list
			if(feePies!=null&&feePies.size()>0){
				for(MapVo m:feePies){//对费别相同name的value要相加
					String name= m.getName();
					MapVo vo = feeMapVo.get(name);
					if(vo!=null){
						vo.setValue(vo.getValue()+m.getValue());
					}else{
						MapVo mapVo = new MapVo();
						mapVo.setName(name);
						mapVo.setValue(m.getValue());
						feeMapVo.put(name, mapVo);
					}
				}
			}
			
			if(deptPies!=null&&deptPies.size()>0){
				for(MapVo m :deptPies){
					String name= m.getName();
					Double value = deptMapVo.get(name);
					if(value!=null){
						value+=m.getValue();
					}else{
						Double d=m.getValue();
						deptMapVo.put(name, d);
					}
				}
			}
		}
		
		//总收入的vo
		MapVo totCostVo = new MapVo();
		totCostVo.setName("总收入");
		totCostVo.setValue(totCost);
		ArrayList<MapVo> totCostList = new ArrayList<MapVo>();
		totCostList.add(totCostVo);
		
		ArrayList<MapVo> feeList = new ArrayList<MapVo>();
		if(feeMapVo.size()>0){
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
				MapVo vo = feeMapVo.get(name);
				if(vo!=null){
					feeList.add(vo);
				}
			}
		}
		
		ArrayList<MapVo> deptList = new ArrayList<MapVo>();
		if(deptMapVo.size()>0){
			
			//得到'其他项'的值，其他不参与排序
			Double other= deptMapVo.get("其他");
			deptMapVo.remove("其他");//删除其他
			
			//对科室前10进行排序后，取前10和其他
			Entry<String,Double>[] entries = SortMapUtils.reverseMap(deptMapVo);//对科室进行逆序排序
			if(entries!=null){
				Double d=0.0;
				for(int i=0;i<entries.length;i++){
					if(i<10){
						MapVo mapVo = new MapVo();
						mapVo.setName(entries[i].getKey());
						mapVo.setValue(entries[i].getValue());
						deptList.add(mapVo);
					}else{
						d+=(Double)entries[i].getValue();//其他费用
					}
					
				}
				MapVo vo = new MapVo();
				vo.setName("其他");
				vo.setValue(d+other);
				deptList.add(vo);
			}
		}
		
		ZyIncomeVo deptAndFee= new ZyIncomeVo();//包含费别，总收入，科室前10的vo
		deptAndFee.setTotCost(totCostList);
		deptAndFee.setFeePies(feeList);
		deptAndFee.setDeptPies(deptList);
		
		
		ArrayList<MapVo> tongbiList = new ArrayList<MapVo>();
		ArrayList<MapVo> huanbiList = new ArrayList<MapVo>();
		int y=Integer.parseInt(dateY);//得到年
		
		//计算6月同环比---同比：同比：往前推年。环比：环比什么，往前推什么单位
		if("2".equals(type)){
		
			//6月同比的时间条件
			String month=date.substring(5,7);//date:yyyy-mm-dd-->得到月
			
			//6月环比的时间条件
			int mon=Integer.parseInt(month);
			String[] str=null;//环比的时间字符串数组,降序
			if(mon>5){
				str=new String[]{dateY+"-"+new Integer(mon-0).toString(),dateY+"-"+new Integer(mon-1).toString(),dateY+"-"+new Integer(mon-2).toString(),dateY+"-"+new Integer(mon-3).toString(),dateY+"-"+new Integer(mon-4).toString(),dateY+"-"+new Integer(mon-5).toString()};
			}else{
				String year=new Integer(y-1).toString();//前一年
				switch(mon){
					case 1:
						str= new String[]{dateY+"-01",year+"-12",year+"-11",year+"-10",year+"-09",year+"-08"};
						break;
					case 2:
						str= new String[]{dateY+"-02",dateY+"-01",year+"-12",year+"-11",year+"-10",year+"-09"};
						break;
					case 3:
						str= new String[]{dateY+"-03",dateY+"-02",dateY+"-01",year+"-12",year+"-11",year+"-10"};
						break;
					case 4:
						str= new String[]{dateY+"-04",dateY+"-03",dateY+"-02",dateY+"-01",year+"-12",year+"-11"};
						break;
					case 5:
						str= new String[]{dateY+"-05",dateY+"-04",dateY+"-03",dateY+"-02",dateY+"-01",year+"-12"};
						break;
				}
			}
			
			for(int i=5;i>=0;i--){
				if(i!=0){
			
					//6月同比
					 String time=new Integer(y-i).toString()+"-"+month;
				     DBCursor dbCursor = new MongoBasicDao().findAlldata(queryMenuAlias, new BasicDBObject("date",time));//{date:'2012-06'}
				     if(dbCursor!=null){
				    	 if(dbCursor.hasNext()){
				    		 String s=dbCursor.next().get("deptAndFee").toString();
				    		 try {
								 ZyIncomeVo incomeVo = JSONUtils.fromJson(s, new TypeToken<ZyIncomeVo>(){});
								 Double d = incomeVo.getTotCost().get(0).getValue();//得到该月的总收入
								 MapVo vo = new MapVo();
								 vo.setName(time);
								 vo.setValue(d);
								 tongbiList.add(vo);
				    		 } catch (Exception e) {
								 e.printStackTrace();
							 }
				    	 }else{//当没有数据时，给这个时间一个默认值
					    	 MapVo vo = new MapVo();
							 vo.setName(time);
							 vo.setValue(0.0);
							 tongbiList.add(vo);
					     }
				     }
				   
				     //6月环比
				     DBCursor hbCursor = new MongoBasicDao().findAlldata(queryMenuAlias, new BasicDBObject("date",str[i]));//{date:'2016-08'} 
				     if(hbCursor!=null){
				    		 if(hbCursor.hasNext()){
				    			 String s= hbCursor.next().get("deptAndFee").toString();
								try {
									ZyIncomeVo incomeVo= JSONUtils.fromJson(s, new TypeToken<ZyIncomeVo>(){});
									 Double d = incomeVo.getTotCost().get(0).getValue();//得到该月的总收入
									 MapVo vo = new MapVo();
									 vo.setName(str[i]);
									 vo.setValue(d);
									 huanbiList.add(vo);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
				    		 }else{//当没有数据时，给这个时间一个默认值
						    	 MapVo vo = new MapVo();
								 vo.setName(str[i]);
								 vo.setValue(0.0);
								 huanbiList.add(vo);
						    }
				    }
				}else{//当i=0，正好是更新的当月，就不用去mongo重在查询了
					MapVo vo = new MapVo();
					vo.setName(dateM);
					vo.setValue(totCost);
					tongbiList.add(vo);
					huanbiList.add(vo);
				}
			}
			
		}
		
		//计算6年环比
		if("3".equals(type)){//没有同比年只有环比年
			//6年环比
			for(int i=5;i>=0;i++){
				if (i!=0) {
					String year = new Integer(y - i).toString();
					DBCursor dbCursor = new MongoBasicDao().findAlldata(queryMenuAlias,new BasicDBObject("date", year));
					//{date:'2012'}
					if (dbCursor != null) {
						if (dbCursor.hasNext()) {
							String s = dbCursor.next().get("deptAndFee").toString();
							try {
								ZyIncomeVo vo = JSONUtils.fromJson(s,new TypeToken<ZyIncomeVo>() {});
								Double value = vo.getTotCost().get(0).getValue();
								MapVo mapVo = new MapVo();
								mapVo.setName(year);
								mapVo.setValue(value);
								huanbiList.add(mapVo);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{//当没有数据时，为这个时间设置默认值
							MapVo mapVo = new MapVo();
							mapVo.setName(year);
							mapVo.setValue(0.0);
							huanbiList.add(mapVo);
						}
					}
				}else{
					MapVo mapVo = new MapVo();
					mapVo.setName(dateY);
					mapVo.setValue(totCost);
					huanbiList.add(mapVo);
				}
			}
		}
		
		ZyIncomeVo tonghuanbi= new ZyIncomeVo();//同环比的vo
		if("2".equals(type)){
			tonghuanbi.setHuanbiBars(huanbiList);
			tonghuanbi.setTongbiBars(tongbiList);
		}
		
		if("3".equals(type)){//当是年的时候,mongo中是不能存在同比的空数组,格式是前人定的
			tonghuanbi.setHuanbiBars(huanbiList);
		}
		
		String deptAndFeeJson=JSONUtils.toJson(deptAndFee);
		String tonghuanbiJson=JSONUtils.toJson(tonghuanbi);
		
		BasicDBObject query = new BasicDBObject();
		Document document = new Document();
		document.append("deptAndFee", deptAndFeeJson);
		document.append("tonghuanbi", tonghuanbiJson);
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
		zyIncomeDao.save(mong);	//保存日志
			
		}
	
	
	

}
