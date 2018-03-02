package cn.honry.statistics.bi.bistac.operationDept.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.bistac.operationDept.dao.OperationDeptTotalDao;
import cn.honry.statistics.bi.bistac.operationDept.service.OperationDeptTotalService;
@Service("operationDeptTotalService")
@Transactional
public class OperationDeptTotalServiceImpl implements OperationDeptTotalService{
	@Autowired
	@Qualifier(value="operationDeptTotalDao")
	private OperationDeptTotalDao operationDeptTotalDao;
	
	public void setOperationDeptTotalDao(OperationDeptTotalDao operationDeptTotalDao) {
		this.operationDeptTotalDao = operationDeptTotalDao;
	}
	private static final String DATEFORMATREGEX = "20[012]\\d-[01]\\d-[0123]\\d\\s\\d\\d:\\d\\d:\\d\\d";
	private static final Logger logger = Logger.getLogger(OperationDeptTotalServiceImpl.class);
	@Override
	/**手术科室按天统计*/
	public void initOperationDeptTotalByDay(String startTime,String endTime) {
		
		if(startTime.matches(DATEFORMATREGEX)&&endTime.matches(DATEFORMATREGEX)){
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Map<String, String> betweenTime =null;
			boolean b = timeDifference(startTime,endTime);
			if(b){
				betweenTime = queryBetweenTime(startTime, endTime, null, map);
				
			}else{
				betweenTime = queryBetweenTime(startTime, endTime, "1", map);
			}
			if(betweenTime!=null){
				Set<Entry<String,String>> set = betweenTime.entrySet();
				Iterator<Entry<String, String>> iterator = set.iterator();
				while(iterator.hasNext()){
					Entry<String, String> next = iterator.next();
					String key = next.getKey();
					String value = next.getValue();
					operationDeptTotalDao.initOperationDeptTotalByDay(key,value);
				}
			}
		}else{
			logger.equals("日期格式不正确!!");
			System.out.println("日期格式不正确!!");
		}
	}
	@Override
	/**手术医生按天统计*/
	public void initOperationDocTotalByDay(String startTime, String endTime) {
		if(startTime.matches(DATEFORMATREGEX)&&endTime.matches(DATEFORMATREGEX)){
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Map<String, String> betweenTime =null;
			boolean b = timeDifference(startTime,endTime);
			if(b){
				//如果为true说明按小时查询
				betweenTime = queryBetweenTime(startTime, endTime, null, map);
			}else{
				//如果为false说明按天查询
				betweenTime = queryBetweenTime(startTime, endTime, "1", map);
			}
			if(betweenTime!=null){
				Set<Entry<String,String>> set = betweenTime.entrySet();
				Iterator<Entry<String, String>> iterator = set.iterator();
				while(iterator.hasNext()){
					Entry<String, String> next = iterator.next();
					String key = next.getKey();
					String value = next.getValue();
					operationDeptTotalDao.initOperationDocTotalByDay(key,value);
				}
			}
		}else{
			logger.equals("日期格式不正确!!");
			System.out.println("日期格式不正确!!");
		}
		
	}
	@Override
	/**手术科室按月统计*/
	public void initoperationDeptTotalByMonth(String startTime, String endTime) {
		if(startTime.matches(DATEFORMATREGEX)&&endTime.matches(DATEFORMATREGEX)){
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Map<String, String> betweenTime =queryBetweenTime(startTime,endTime,"2",map);
			Set<Entry<String,String>> set = betweenTime.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			while(iterator.hasNext()){
				Entry<String, String> next = iterator.next();
				String key = next.getKey();
				String value = next.getValue();
				operationDeptTotalDao.initOperationDeptTotalByMonth(key,value);
			}
		}else{
			logger.equals("日期格式不正确!!");
			System.out.println("日期格式不正确!!");
		}
	}
	@Override
	/**手术医生按月统计*/
	public void initOperationDocTotalByMouth(String startTime, String endTime) {
		if(startTime.matches(DATEFORMATREGEX)&&endTime.matches(DATEFORMATREGEX)){
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Map<String, String> betweenTime =queryBetweenTime(startTime,endTime,"2",map);
			Set<Entry<String,String>> set = betweenTime.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			while(iterator.hasNext()){
				Entry<String, String> next = iterator.next();
				String key = next.getKey();
				String value = next.getValue();
				operationDeptTotalDao.initOperationDocTotalByMonth(key,value);
			}
		}else{
			logger.equals("日期格式不正确!!");
			System.out.println("日期格式不正确!!");
		}
	}
	/**手术科室按年统计 */
	@Override
	public void initoperationDeptTotalByYear(String startTime, String endTime) {
		if(startTime.matches(DATEFORMATREGEX)&&endTime.matches(DATEFORMATREGEX)){
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Map<String, String> betweenTime =queryBetweenTime(startTime,endTime,"3",map);
			Set<Entry<String,String>> set = betweenTime.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			while(iterator.hasNext()){
				Entry<String, String> next = iterator.next();
				String key = next.getKey();
				String value = next.getValue();
				operationDeptTotalDao.initOperationDeptTotalByYear(key,value);
			}
		}else{
			logger.equals("日期格式不正确!!");
			System.out.println("日期格式不正确!!");
		}
	}
	/**手术科室按年统计 */
	@Override
	public void initOperationDocTotalByYear(String startTime, String endTime) {
		if(startTime.matches(DATEFORMATREGEX)&&endTime.matches(DATEFORMATREGEX)){
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
			Map<String, String> betweenTime =queryBetweenTime(startTime,endTime,"3",map);
			Set<Entry<String,String>> set = betweenTime.entrySet();
			Iterator<Entry<String, String>> iterator = set.iterator();
			while(iterator.hasNext()){
				Entry<String, String> next = iterator.next();
				String key = next.getKey();
				String value = next.getValue();
				operationDeptTotalDao.initOperationDocTotalByYear(key,value);
			}
		}else{
			logger.equals("日期格式不正确!!");
			System.out.println("日期格式不正确!!");
		}
	}
	/**
	 * 时间计算，返回各个时间节点
	 * @param begin
	 * @param end
	 * @param flg
	 * @param map
	 * @return
	 */
	public  Map<String,String> queryBetweenTime(String begin,String end,String flg,Map<String,String> map){
		SimpleDateFormat sdFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar ca=Calendar.getInstance();
		 try {
			Date date= sdFull.parse(begin);//开始时间
			String[] dateone=begin.split(" ");
			String[] dateArr=dateone[0].split("-");
			String[] dateArr1=dateone[1].split(":");
			Date date1= sdFull.parse(end);//结束时间
			if(date.getTime()>=date1.getTime()){//如果开始时间大于结束时间  结束
				return map;
			}else{
				ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
				if("1".equals(flg)){//小时为空 按天查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.DATE, 1);//开始时间加1天
					begin=sdFull.format(ca.getTime());
					map.put(key,begin );
				}else if("2".equals(flg)){//按月查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}else if("3".equals(flg)){//按月查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.YEAR, 1);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}else {//按小时查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.HOUR, 1);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}
				return queryBetweenTime(begin,end,flg,map);
			}
		} catch (ParseException e) {
		}//开始时间
		return map;
	}
	/**
	 * 日期时间差
	 * true 两个小时以内
	 * false 两个小时以外
	 */
	public boolean timeDifference(String startDate,String endDate) {
		 SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
		 try {
			 Date begins = fmt.parse(startDate);
			 if(fmt.format(begins).toString().equals(fmt.format(new Date()).toString())){
		    	fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
					 Date begin = fmt.parse(startDate);
					 Date end = fmt.parse(endDate);
					 long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
				 	 long hour=between%(24*3600)/3600;
				     if(hour<=2){
				         return true;
				     }
				 	 return false;
				  
		      }
		  } catch (ParseException e) {
			  
		  }
	     return false;
	}
	@Override
	public void initOperation(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				initOperationDocTotalByDay(begin+" 00:00:00", end+" 00:00:00");
				initOperationDeptTotalByDay(begin+" 00:00:00", end+" 00:00:00");	
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				operationDeptTotalDao.initOperationDeptTotalByMonth(begin, end);
				operationDeptTotalDao.initOperationDocTotalByMonth(begin, end);
			}else if(3==type){//年数据dateformate:yyyy
				operationDeptTotalDao.initOperationDocTotalByYear(begin,end);
				operationDeptTotalDao.initOperationDeptTotalByYear(begin,end);
			}
		}
		
	}
	
}
