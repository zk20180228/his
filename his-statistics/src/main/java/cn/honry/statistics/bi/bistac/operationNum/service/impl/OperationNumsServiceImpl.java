package cn.honry.statistics.bi.bistac.operationNum.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.statistics.operationNum.dao.InnerOperationNumDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.deptAndFeeData.vo.StatisticsVo;
import cn.honry.statistics.bi.bistac.operationNum.dao.OperationNumsDao;
import cn.honry.statistics.bi.bistac.operationNum.service.OperationNumsService;
import cn.honry.statistics.bi.bistac.operationNum.vo.OperationNumsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("operationNumsService")
@Transactional
@SuppressWarnings({ "all" })
public class OperationNumsServiceImpl implements OperationNumsService{

	@Autowired
	@Qualifier(value = "operationNumsDao")
	private OperationNumsDao operationNumsDao;

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Autowired
	@Qualifier(value = "innerOperationNumDao")
	private InnerOperationNumDao innerOperationNumDao;
	
	/**
	 *  初始化手术例数（门诊、住院、介入）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsToDB(String startDate,String endDate) {
		operationNumsDao.saveOperationNumsToDB(startDate,endDate);
	}

	/**
	 *  初始化手术例数（科室TOP5）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsTopDeptToDB(String startDate,String endDate) {
		operationNumsDao.saveOperationNumsTopDeptToDB(startDate,endDate);
		
	}

	/**
	 *  初始化手术例数（医生TOP5）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsTopDocToDB(String startDate,String endDate) {
		operationNumsDao.saveOperationNumsTopDocToDB(startDate,endDate);
		
	}

	/**
	 *  初始化手术例数（同环比）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsYoyRatioToDB(String startDate,String endDate) {
		List<String> tnL = new ArrayList<String>();
		boolean times=timeDifference(startDate,endDate);
		Map<String,String> dateTime;
		if(!times){
			dateTime=queryBetweenTime(startDate,endDate,0,new HashMap<String, String>());
		}else{
			dateTime=queryBetweenTime(startDate,endDate,1,new HashMap<String, String>());
		}
		for(String begin:dateTime.keySet()){
			tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",begin,dateTime.get(begin));
		}
		operationNumsDao.saveOperNumsYoyRatioToDB(startDate,endDate);
	}

	/**
	 *  初始化手术例数（门诊、住院、介入）按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperationNumsMonthToDB(String startDate, String endDate) {
		operationNumsDao.saveOperationNumsMonthToDB(startDate,endDate);
	}

	public  Map<String,String> queryBetweenTime(String begin,String end,int flg,Map<String,String> map){
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
				if(flg==0){//小时为空 按天查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.DATE, 1);//开始时间加1天
					begin=sdFull.format(ca.getTime());
					map.put(key,begin );
				}else{//按小时查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.HOUR,1);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}
				return queryBetweenTime(begin,end,flg,map);
			}
		} catch (ParseException e) {
		}//开始时间
		return map;
	}
	
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
	public void saveOperationNumsYearToDB(String startDate, String endDate) {
		operationNumsDao.saveOperationNumsYearToDB(startDate,endDate);
		
	}

	@Override
	public void saveOperNumsTopDeptMonthToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsTopDeptMonthToDB(startDate,endDate);
		
	}

	/**
	 *  初始化手术例数科室前五年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsTopDeptYearToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsTopDeptYearToDB(startDate,endDate);
		
	}
	/**
	 *  初始化手术例数医生前五按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsTopDocMonthToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsTopDocMonthToDB(startDate,endDate);
		
	}

	/**
	 *  初始化手术例数医生前五按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsTopDocYearToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsTopDocYearToDB(startDate,endDate);
		
	}

	/**
	 * 初始化手术例数同环比按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsYoyRatioMonthToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsYoyRatioMonthToDB(startDate,endDate);
		
	}

	/**
	 *  初始化手术例数同环比按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsYoyRatioYearToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsYoyRatioMonthToDB(startDate,endDate);
		
	}

	/**
	 *  初始化手术例数同环比按天
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsOpTypeToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsOpTypeToDB(startDate, endDate);
		
	}

	/**
	 *  初始化手术例数同环比按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	@Override
	public void saveOperNumsOpTypeMonthToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsOpTypeMonthToDB(startDate, endDate);
		
	}

	/**
	 *  初始化手术例数同环比按年份
	 * @author zhuxiaolu 
	 * @param startDate
	 * @param endDate
	 *  @return:void
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public void saveOperNumsOpTypeYearToDB(String startDate, String endDate) {
		operationNumsDao.saveOperNumsOpTypeYearToDB(startDate, endDate);
		
	}

	/**
	 *  查询在做或已完成（当天）
	 * @author zhuxiaolu 
	 * @param startDate
	 * @param endDate
	 *  @return: List<OperationNumsVo>
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public List<OperationNumsVo> getDoingOrFinish(String startDate,
			String endDate) {
		return operationNumsDao.getDoingOrFinish(startDate,endDate);
	}
	/**
	 *   手术例数统计（住院门诊）
	 *  @author zhuxiaolu 
	 *  @param startDate
	 *  @param endDate
	 *  @return: List<OperationNumsVo>
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public List<OperationNumsVo> queryTotalCount(String searchTime,
			String dateSign) {
		List<OperationNumsVo> list= operationNumsDao.queryTotalCount(searchTime,dateSign);
		if(list.size()==0){
			Map<String,String> map=getTimes(searchTime,dateSign);
			String startDate=map.get("start");
			String endDate =map.get("end");
			List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",startDate,endDate);
			if(tnL!=null&&tnL.size()==0){
				tnL.add("T_OPERATION_RECORD");
			}
			list= operationNumsDao.queryTotalCountToDB(tnL,startDate,endDate,dateSign);
			List<String> listnew=new ArrayList<String>();
			listnew.add("门诊");
			listnew.add("住院");
			for(int i=0;i<list.size();i++){
				if(listnew.contains(list.get(i).getName())){
					listnew.remove(list.get(i).getName());
				}
			}
			for(int i=0;i<listnew.size();i++){
				OperationNumsVo vo=new OperationNumsVo();
				vo.setName(listnew.get(i));
				vo.setNums(0);
				list.add(vo);
			}
		}
		return list;
	}
	/**
	 *   手术例数统计（科室top5）
	 *  @author zhuxiaolu 
	 *  @param startDate
	 *  @param endDate
	 *  @return: List<OperationNumsVo>
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public List<OperationNumsVo> queryOperationNumsTopDept(String searchTime,
			String dateSign) {
		List<OperationNumsVo> list= operationNumsDao.queryOperationNumsTopDept(searchTime,dateSign);
		if(list.size()==0){
			Map<String,String> map=getTimes(searchTime,dateSign);
			String startDate=map.get("start");
			String endDate =map.get("end");
			List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",startDate,endDate);
			if(tnL!=null&&tnL.size()==0){
				tnL.add("T_OPERATION_RECORD");
			}
			list= operationNumsDao.queryNumsTopDeptToDB(tnL,startDate,endDate,dateSign);
		}
		return list;
	}

	/**
	 *   手术例数统计（医生top5）
	 *  @author zhuxiaolu 
	 *  @param startDate
	 *  @param endDate
	 *  @return: List<OperationNumsVo>
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public List<OperationNumsVo> queryOperationNumsTopDoc(String searchTime,
			String dateSign) {
		List<OperationNumsVo> list= operationNumsDao.queryOperationNumsTopDoc(searchTime,dateSign);
		if(list.size()==0){
			Map<String,String> map=getTimes(searchTime,dateSign);
			String startDate=map.get("start");
			String endDate =map.get("end");
			List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",startDate,endDate);
			if(tnL!=null&&tnL.size()==0){
				tnL.add("T_OPERATION_RECORD");
			}
			list= operationNumsDao.queryNumsTopDocToDB(tnL,startDate,endDate,dateSign);
		}
		return list;
	}

	/**
	 *  手术例数统计（手术类别）
	 *  @author zhuxiaolu 
	 *  @param startDate
	 *  @param endDate
	 *  @return: List<OperationNumsVo>
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public List<OperationNumsVo> queryNumsOpType(String searchTime,
			String dateSign) {
		List<OperationNumsVo> list= operationNumsDao.queryNumsOpType(searchTime,dateSign);
		if(list.size()==0){
			Map<String,String> map=getTimes(searchTime,dateSign);
			String startDate=map.get("start");
			String endDate =map.get("end");
			List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",startDate,endDate);
			if(tnL!=null&&tnL.size()==0){
				tnL.add("T_OPERATION_RECORD");
			}
			list= operationNumsDao.queryNumsOpTypeToDB(tnL,startDate,endDate,dateSign);
		}
		return list;
	}
	/**
	 *  根据传入一个时间返回开始时间和 结束时间map
	 *  @author zhuxiaolu 
	 *  @param startDate
	 *  @param endDate
	 *  @return: List<OperationNumsVo>
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	public Map<String,String> getTimes(String searchTime,String dateSign) {
		Map<String,String> map=new HashMap<String, String>();
		String begin="";
		String end="";
		if("1".equals(dateSign)){
			begin=searchTime+"-01-01 00:00:00";
			if(DateUtils.formatDateY(new Date()).equals(searchTime)){
				end=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				end=searchTime+"-12-31 23:59:59";
			}
		}else if("2".equals(dateSign)){
			begin=searchTime+"-01";
			if(DateUtils.formatDateY_M(new Date()).equals(searchTime)){
				end=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
				try {
					Date date = sdf.parse(begin);
					Calendar calendar = Calendar.getInstance();
			        calendar.setTime(date);
			        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			        end=DateUtils.formatDateY_M_D_H_M_S(calendar.getTime());
				} catch (ParseException e) {
				}
			}
			begin=searchTime+"-01 00:00:00";
		}else{
			begin=searchTime+" 00:00:00";
			if(DateUtils.formatDateY_M_D(new Date()).equals(searchTime)){
				end=DateUtils.formatDateY_M_D_H_M_S(new Date());
			}else{
				end=searchTime+" 23:59:59";
			}
		}
		map.put("start", begin);
		map.put("end", end);
		return map;
	}

	/**
	 *  查询已完成手术例数（昨天）
	 *  @author zhuxiaolu 
	 *  @param startDate
	 *  @param endDate
	 *  @return: List<OperationNumsVo>
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 */
	@Override
	public List<OperationNumsVo> getYesterDayFinish(String startDate,
			String endDate) {
		return operationNumsDao.getYesterDayFinish(startDate,endDate);
	}
	/**
	 * 手术例数统计环比
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryRatioCount(String searchTime,
			String dateSign) {
		List<OperationNumsVo> list=  operationNumsDao.queryRatioCount(searchTime,dateSign);
		if(list!=null&&list.size()==0){
			Map<String,String> timeMap=this.queryBetweenTime(searchTime, dateSign,"HB");//6月内时间数组
			for(String begin:timeMap.keySet()){
				OperationNumsVo vo=operationNumsDao.queryYoyCountToDB(begin,timeMap.get(begin),dateSign);
				list.add(vo);
			}
			
		}
		Collections.sort(list, new Comparator(){  
	        @Override  
	        public int compare(Object o1, Object o2) {  
	        	OperationNumsVo stu1=(OperationNumsVo)o1;  
	        	OperationNumsVo stu2=(OperationNumsVo)o2;  
	            return stu1.getName().compareTo(stu2.getName());  
	        }             
	    });
		return list;
	}

	/**
	 * 手术例数统计同
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryYoyCount(String searchTime,
			String dateSign) {
		List<OperationNumsVo> list=  operationNumsDao.queryYoyCount(searchTime,dateSign);
		if(list!=null&&list.size()==0){
			Map<String,String> timeMap=this.queryBetweenTime(searchTime, dateSign,"TB");//6月内时间数组
			for(String begin:timeMap.keySet()){
				OperationNumsVo vo=operationNumsDao.queryYoyCountToDB(begin,timeMap.get(begin),dateSign);
				list.add(vo);
			}
			
		}
		Collections.sort(list, new Comparator(){  
	        @Override  
	        public int compare(Object o1, Object o2) {  
	        	OperationNumsVo stu1=(OperationNumsVo)o1;  
	        	OperationNumsVo stu2=(OperationNumsVo)o2;  
	            return stu1.getName().compareTo(stu2.getName());  
	        }             
	    }); 
		return list;
	}

	/**
	 * 根据传入时间获取同环比时间段（六个）
	 * @param begin yyyy  yyyy-MM yyyy-MM-dd
	 * @param dateSign 1 2 3
	 * @param type TB HB TB没有年同比
	 * @return
	 */
	public  static Map<String,String> queryBetweenTime(String begin,String dateSign,String type){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		String key;
		if("TB".equals(type)){
			if("2".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1,1,0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.MONTH, -1);
					ca.add(Calendar.YEAR, -1);
				}
			}else if("3".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]), 0, 0, 0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.DATE, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.DATE, -1);
					ca.add(Calendar.YEAR, -1);
				}
			}
			return map;
		}else if("HB".equals(type)){
			if("1".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]),0,1,0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.YEAR, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.YEAR, -2);
				}
			}else if("2".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,1,0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					ca.add(Calendar.DATE, 0);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.MONTH, -2);
				} 
			}else if("3".equals(dateSign)){
				String[] date=begin.split("-");
				Calendar ca=Calendar.getInstance();
				ca.set(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]),0,0,0);
				for(int i=0;i<6;i++){
					key=sdf.format(ca.getTime());
					ca.add(Calendar.DATE, 1);
					map.put(key, sdf.format(ca.getTime()));
					ca.add(Calendar.DATE, -2);
				}
			}
			return map;
		}
		return new HashMap<String,String>();
	}

	/**
	 * 手术例数统计(普通或门诊)
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	@Override
	public List<OperationNumsVo> queryJzOrPtCount(String searchTime,
			String dateSign) {
		//获取时间map
		Map<String,String> map=getTimes(searchTime,dateSign);
		String startDate=map.get("start");
		String endDate =map.get("end");
		List<String> tnL=ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OPERATION_RECORD",startDate,endDate);
		if(tnL!=null&&tnL.size()==0){
			tnL.add("T_OPERATION_RECORD");
		}
		return  operationNumsDao.queryJzOrPtCountToDB(tnL,startDate,endDate,dateSign);
	}

	@Override
	public void initDate(String beginDate, String endDate, Integer type) {
		if(StringUtils.isNotBlank(beginDate)&&StringUtils.isNotBlank(endDate)&&type!=null){
			String menuAlias="SSLSTJ";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				String his="HIS";
				innerOperationNumDao.initOperNumsMZJ(menuAlias, his, beginDate);
//				innerOperationNumDao.initOperNumsType(menuAlias, his, beginDate);
//				innerOperationNumDao.initOperNumsDept(menuAlias, his, beginDate);
//				innerOperationNumDao.initOperNumsDoc(menuAlias, his, beginDate);
				innerOperationNumDao.initOperNumsYR(menuAlias, his, beginDate);
				innerOperationNumDao.initPcOperationNumbers(menuAlias, his, beginDate);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date begin=DateUtils.parseDateY_M(beginDate);
				Calendar ca=Calendar.getInstance();
				ca.setTime(begin);
				Date end=DateUtils.parseDateY_M(endDate);
				while(DateUtils.compareDate(ca.getTime(), end)<=0){
					beginDate=DateUtils.formatDateY_M(ca.getTime());
					innerOperationNumDao.initOperNumsMZJYearOrMonth(menuAlias, "2", beginDate);
//					innerOperationNumDao.initOperNumsTypeYearOrMonth(menuAlias, "2", beginDate);
//					innerOperationNumDao.initOperNumsDeptYearOrMonth(menuAlias, "2", beginDate);
//					innerOperationNumDao.initOperNumsDocYearOrMonth(menuAlias, "2", beginDate);
					innerOperationNumDao.initOperNumsYRYearOrMonth(menuAlias, "2", beginDate);
					innerOperationNumDao.initPcOperationNumbersMonthAndYear(menuAlias, "2", beginDate);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
				Date begin=DateUtils.parseDateY(beginDate);
				Date end=DateUtils.parseDateY(endDate);
				Calendar ca=Calendar.getInstance();
				ca.setTime(begin);
				while(DateUtils.compareDate(ca.getTime(), end)<=0){
					beginDate=DateUtils.formatDateY(ca.getTime());
					innerOperationNumDao.initOperNumsMZJYearOrMonth(menuAlias, "3", beginDate);
//					innerOperationNumDao.initOperNumsTypeYearOrMonth(menuAlias, "3", beginDate);
//					innerOperationNumDao.initOperNumsDeptYearOrMonth(menuAlias, "3", beginDate);
//					innerOperationNumDao.initOperNumsDocYearOrMonth(menuAlias, "3", beginDate);
					innerOperationNumDao.initOperNumsYRYearOrMonth(menuAlias, "3", beginDate);
					innerOperationNumDao.initPcOperationNumbersMonthAndYear(menuAlias, "3", beginDate);
					ca.add(Calendar.YEAR, 1);
				}
			}
		}
		
	}
	
	
	
	
	
	/**
	 * 查询手术例数
	 * @param startDate
	 * @param timeType 查询时间格式
	 * 			1：YYYY-dd-mm 按天
	 * 			2：YYYY-dd	    按月
	 * 			3：YYYY		    按年
	 * @return
	 */
	public List<OperationNumsVo> query(String startDate,Integer timeType){
		if(0 == timeType){
			startDate = DateUtils.formatDateY_M_D(new Date());
			timeType = 1;
		}
		
		return operationNumsDao.queryForMongoDB(startDate, timeType);
	}

	@Override
	public List<StatisticsVo> queryMom(String startDate, Integer timeType) throws ParseException {
		if(0 == timeType){
			startDate = DateUtils.formatDateY_M_D(new Date());
			timeType = 1;
		}
		String collection=this.getCollection(timeType);
		String endTime=null;
		if(StringUtils.isBlank(startDate)){
			return new  ArrayList<StatisticsVo>();
		}
		Date start=null;
		Date end=null;
		switch (timeType) {
		case 1:
			start=DateUtils.parseDateY_M_D(startDate);
			end = DateUtils.addDay(start,-5);
			break;
		case 2:
			start=DateUtils.parseDateY_M(startDate);
			end = DateUtils.addMonth(start,-5);
			break;
		case 3:
			start=DateUtils.parseDateY(startDate);
			end = DateUtils.addYear(start,-5);
			break;
		default:
			break;
		}
		List<StatisticsVo> list=new ArrayList<StatisticsVo>();
		DateFormat dfm=new SimpleDateFormat("yyyy-MM-dd".substring(0, 13-timeType*3));
		while(DateUtils.compareDate(end,start)<=0){
			startDate=dfm.format(end);
			List<StatisticsVo> voList=operationNumsDao.queryMoy(collection, startDate, null,timeType);
			voList.get(0).setName(startDate);
			list.add(voList.get(0));
			end=returnEndDate(timeType,end);
		}
		return list;
	}
	private Date returnEndDate(Integer timeType,Date endDate){
		if(1==timeType){
			endDate=DateUtils.addDay(endDate, 1);
		}else if(2==timeType){
			endDate=DateUtils.addMonth(endDate, 1);
		}else{
			endDate=DateUtils.addYear(endDate, 1);	
		}
		return endDate;
	}
	@Override
	public List<StatisticsVo> queryYoy(String startDate, Integer timeType) throws ParseException {
		String startTime = "";
		String endTime = "";
		List<StatisticsVo> voList = new ArrayList<StatisticsVo>();
		if(0 == timeType){
			startDate = DateUtils.formatDateY_M_D(new Date());
			timeType = 1;
		}
		String collection=this.getCollection(timeType);
		switch (timeType) {
		case 1:
			startTime = startDate;
			for(int i = 5;i >= 0;i--){
				String start = DateUtils.formatDateY_M_D(DateUtils.addYear(DateUtils.parseDateY_M_D(startTime),-i));
				List<StatisticsVo> listDay = operationNumsDao.queryMoy(collection, start, null,timeType);
				listDay.get(0).setName(start);
				voList.add(listDay.get(0));
			}
			break;
		case 2:
			startTime = startDate.substring(0, 7);
			for(int i = 5;i >= 0;i--){	
				String start = DateUtils.formatDateY_M(DateUtils.addYear(DateUtils.parseDateY_M(startTime),-i));
				List<StatisticsVo> listMonth = operationNumsDao.queryMoy(collection, start, null,timeType);
				listMonth.get(0).setName(start.substring(0,7));
				voList.addAll(listMonth);
			}
			break;
		default:
			break;
		}
		return voList;
	}

	@Override
	public void initDateOpType() {
		innerOperationNumDao.initOperOpTypeToDB();
	}
	private String getCollection(Integer timeType){
		String queryMzZyCollection="SSLSTJ_OPERNUMSMZJ_";
		if(1==timeType){
			queryMzZyCollection+="DAY";
		}else if(2==timeType){
			queryMzZyCollection+="MONTH";
		}else{
			queryMzZyCollection+="YEAR";
		}
		return queryMzZyCollection;
	}
}
