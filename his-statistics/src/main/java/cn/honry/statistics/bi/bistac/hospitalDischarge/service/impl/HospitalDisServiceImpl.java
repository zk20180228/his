package cn.honry.statistics.bi.bistac.hospitalDischarge.service.impl;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.inOutPatient.dao.InOutPatientDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.bi.bistac.hospitalDischarge.dao.HospitalDisDao;
import cn.honry.statistics.bi.bistac.hospitalDischarge.service.HospitalDisService;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisChargeVo;
import cn.honry.statistics.bi.bistac.hospitalDischarge.vo.HospitalDisVo;
import cn.honry.utils.DateUtils;
@Service("hospitalDisService")
public class HospitalDisServiceImpl implements HospitalDisService{
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdFull=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar ca=Calendar.getInstance();
	@Autowired
	@Qualifier("hospitalDisDao")
	private HospitalDisDao hospitalDisDao;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="inOutPatientDao")
	private InOutPatientDao inOutPatientDao;
	
	public void setInOutPatientDao(InOutPatientDao inOutPatientDao) {
		this.inOutPatientDao = inOutPatientDao;
	}

/*******************************************************************************************************/
	public static boolean betweenDateSign(String begin,String end){
		boolean sign=false;
		try {
	if(StringUtils.isNotBlank(begin)){
		String[] dateone=begin.split(" ");
		String[] dateArr=dateone[0].split("-");
		String[] dateArr1=dateone[1].split(":");
		Date date1= sdFull.parse(end);//结束时间
		ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
		ca.add(Calendar.HOUR, 2);
		Calendar ca1=Calendar.getInstance();
		ca1.set(Calendar.HOUR, 0);
		ca1.set(Calendar.MINUTE, 0);
		ca1.set(Calendar.SECOND, 0);
		if(ca.getTime().getTime()>=date1.getTime()&&ca.getTime().getTime()>ca1.getTime().getTime()){
			sign=true;
		}
			}
		} catch (ParseException e) {
		}
		
		return sign;
	}
	public  static Map<String,String> queryBetweenTime(String begin,String end,Integer hours,Map<String,String> map){
		
		 try {
		if(StringUtils.isNotBlank(begin)){
			Date date= sdFull.parse(begin);//开始时间
			String[] dateone=begin.split(" ");
			String[] dateArr=dateone[0].split("-");
			String[] dateArr1=dateone[1].split(":");
			Date date1= sdFull.parse(end);//结束时间
			if(date.getTime()>=date1.getTime()){//如果开始时间大于结束时间  结束
				return map;
			}else{
				ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
				if(hours==null){//小时为空 按天查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.DATE, 1);//开始时间加1天
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}else{//按小时查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.HOUR, hours);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}
				return queryBetweenTime(begin,end,hours,map);
			}
		}
		} catch (ParseException e) {
		}//开始时间
		return map;
	}

	@Override
	public List<HospitalDisVo> querySameOrSque(String date, String dateSign,
			String type) {
		if(null==date||null==dateSign||null==type||"".equals(date)||"".equals(dateSign)||"".equals(type)){
			return new ArrayList<HospitalDisVo>();
		}
		String queryMongo="INPATIENTINOUT";
		if("1".equals(dateSign)){
			queryMongo+="_YEAR";
		}else if("2".equals(dateSign)){
			queryMongo+="_MONTH";
		}else {
			queryMongo+="_DAY";
		}
		boolean sign=new MongoBasicDao().isCollection(queryMongo);
		List<HospitalDisVo> list=new ArrayList<HospitalDisVo>();
		if(sign){
			String[] searchTimes;
			if("HB".equals(type)){
				searchTimes=this.conYear(date, dateSign);
			}else{
				searchTimes=this.conMonth(date, dateSign);
			}
			for(String searTime:searchTimes)
			list.add(hospitalDisDao.querySameOrSque(date, dateSign, searTime));
			if(list.size()>0){
				return list;
			}
			return new ArrayList<HospitalDisVo>();
		}else{
			Map<String,String> map=this.queryBetweenTime(date, dateSign,type);
			List<String> tnL=new ArrayList<String>();
			for(String key:map.keySet()){
//				tnL=this.returnTnL(key, map.get(key),orderTable);
				list.add(hospitalDisDao.querySameOrSque(tnL,dateSign, key, map.get(key)));
			}
		}
		return new ArrayList<HospitalDisVo>();
	}
	
	
	
	
	/**
	 * 
	 * @param begin yyyy  yyyy-MM yyyy-MM-dd
	 * @param dateSign 1 2 3
	 * @param type TB HB TB没有年同比
	 * @return
	 */
	public  static Map<String,String> queryBetweenTime(String begin,String dateSign,String type){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,String> map=new LinkedHashMap<String,String>();
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
				ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 0, 0, 0);
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
	 * 环比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conYear(String date,String dateSing){
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
	public String[] conMonth(String date,String dateSing){
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
	/**
	 * 计算时间
	 * @param date
	 * @param dateSign
	 * @return
	 */
	public List<String> lastSameq(String date,String dateSign){
		List<String> list=new ArrayList<String>();
		SimpleDateFormat sdf=null;
		Calendar ca=Calendar.getInstance();
		if("1".equals(dateSign)){
			sdf=new SimpleDateFormat("yyyy");
			ca.set(Calendar.YEAR, Integer.parseInt(date));
			ca.add(Calendar.YEAR, -1);//上年
			list.add(date);
			list.add(sdf.format(ca.getTime()));
		}else if("2".equals(dateSign)){
			sdf=new SimpleDateFormat("yyyy-MM");
			String[] dateArr=date.split("-");
			ca.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
			ca.set(Calendar.MONTH, Integer.parseInt(dateArr[1])-1);
			list.add(date);//当月
			ca.add(Calendar.MONTH, -1);
			list.add(sdf.format(ca.getTime()));//上月
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.YEAR, -1);
			list.add(sdf.format(ca.getTime()));
		}else{
			sdf=new SimpleDateFormat("yyyy-MM-dd");
			String[] dateArr=date.split("-");
			ca.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
			ca.set(Calendar.MONTH, Integer.parseInt(dateArr[1])-1);
			ca.set(Calendar.DATE, Integer.parseInt(dateArr[2]));
			list.add(date);//当月
			ca.add(Calendar.MONTH, -1);
			list.add(sdf.format(ca.getTime()));//上月
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.YEAR, -1);
			list.add(sdf.format(ca.getTime()));
		}
		return list;
	}
	//数据库查时间
	public Map<String,String> lastSameq1(String date,String dateSign){
		Map<String,String> map=new LinkedHashMap<String, String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar ca=Calendar.getInstance();
		String begin;
		if("1".equals(dateSign)){
			ca.set(Integer.parseInt(date), 0, 1, 0, 0, 0);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.YEAR, 1);
			map.put(begin, sdf.format(ca.getTime()));//今年
			ca.add(Calendar.YEAR, -2);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.YEAR, 1);
			map.put(begin, sdf.format(ca.getTime()));
		}else if("2".equals(dateSign)){
			String[] dateArr=date.split("-");
			ca.set(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1])-1, 1, 0, 0, 0);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.MONTH, 1);
			map.put(begin, sdf.format(ca.getTime()));//本月
			ca.add(Calendar.MONTH, -2);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.MONTH, 1);
			map.put(begin, sdf.format(ca.getTime()));//上月
			ca.add(Calendar.YEAR, -1);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.MONTH, 1);
			map.put(begin, sdf.format(ca.getTime()));//上年同月
		}else{
			String[] dateArr=date.split("-");
			ca.set(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]), 0, 0, 0);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.DATE, 1);
			map.put(begin, sdf.format(ca.getTime()));//今天
			ca.add(Calendar.MONTH,-1 );
			ca.add(Calendar.DATE, -1);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.DATE, 1);
			map.put(begin, sdf.format(ca.getTime()));
			ca.add(Calendar.YEAR, -1);
			ca.add(Calendar.DATE, -1);
			begin=sdf.format(ca.getTime());
			ca.add(Calendar.DATE, 1);
			map.put(begin, sdf.format(ca.getTime()));
		}
		return map;
	}

	@Override
	public List<Map<String,HospitalDisVo>> queryDate(String date, String dateSign) {
		if(StringUtils.isBlank(date)||StringUtils.isBlank(dateSign)){
			return new ArrayList<Map<String,HospitalDisVo>>();
		}
		String queryMongo="INPATIENTINOUT";
		if("1".equals(dateSign)){
			queryMongo+="_YEAR";
		}else if("2".equals(dateSign)){
			queryMongo+="_MONTH";
		}else {
			queryMongo+="_DAY";
		}
		
		boolean sign=new MongoBasicDao().isCollection(queryMongo);
		List<Map<String,HospitalDisVo>> list=new ArrayList<Map<String,HospitalDisVo>>();
		if(sign){
			List<String> searchTimes=this.lastSameq(date, dateSign);
			for(String searTime:searchTimes)
			list.add(hospitalDisDao.queryHospitalDisList(searTime, dateSign));
			if(list.size()>0){
				return list;
			}
			return  new ArrayList<Map<String,HospitalDisVo>>();
		}else{
			List<HospitalDisVo> list1=new ArrayList<HospitalDisVo>();
			Map<String,String> map=this.lastSameq1(date, dateSign);//时间段
			
			List<String> tnL=new ArrayList<String>();
			for(String key:map.keySet()){
				Map<String,HospitalDisVo> voMap=new  LinkedHashMap<String,HospitalDisVo>(2);
//				tnL=this.returnTnL(key, map.get(key),orderTable);
				list1=hospitalDisDao.queryHospitalDisList(tnL, key, map.get(key));
				voMap.put("ZY", list1.get(0));
				voMap.put("MZ", list1.get(0));
				list.add(voMap);
			}
		}
		return list;
	}
	/**
	 * 饼图
	 */
	@Override
	public List<HospitalDisVo> queryPiechat(String date, String dateSign) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 自定义查询 
	 * @param  begin 开始时间
	 * @param  end 结束时间
	 */
	@Override
	public List<HospitalDisVo> queryFeelHos(String begin, String end) {
		if(StringUtils.isBlank(begin)||StringUtils.isBlank(end)){
			return new ArrayList<HospitalDisVo>();
		}
		boolean sign=new MongoBasicDao().isCollection("INPATIENTINOUT_DAY");
		List<HospitalDisVo> list=new ArrayList<HospitalDisVo>();
		if(sign){
			list=hospitalDisDao.queryFeelHos(begin, end);
		}else{
			begin=begin+" 00:00:00";
			end=end+" 00:00:00";
			list=hospitalDisDao.queryHospitalDisList(null, begin,end);
		}
		return list;
	}
	@Override
	public void init_ZCYRCTj(String begin, String end, Integer type){
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="INPATIENTINOUT";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				inOutPatientDao.initZCYRCTJ(menuAlias, "HIS", begin);
//				this.initOneDay(begin+" 00:00:00", end+" 23:59:59");
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());//本月第一天
					inOutPatientDao.initZCYRCTJMoreDay(menuAlias, "2", begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
//				date=date.substring(0,7)+"-01";
//				Calendar ca=Calendar.getInstance(Locale.CHINESE);
//				try {
//					DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//					ca.setTime(df.parse(end+"-01"));
//					ca.add(Calendar.MONTH, 1);
//					ca.add(Calendar.DATE, -1);
//					end=df.format(ca.getTime());
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				this.initOneMonth(begin+"-01 00:00:00", end+" 23:59:59");
			}else if(3==type){//年数据dateformate:yyyy
				Date beginDate=DateUtils.parseDateY(begin);
				Date endDate=DateUtils.parseDateY(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY(ca.getTime());
					inOutPatientDao.initZCYRCTJMoreDay(menuAlias, "3", begin);
					ca.add(Calendar.YEAR, 1);
				}
//				this.initOneYear(begin+"-01-01 00:00:00", end+"-12-31 23:59:59");
			}
		}
	}

	@Override
	public HospitalDisChargeVo queryInOutList(String begin, String end,String dateSign) {
		if(StringUtils.isBlank(begin)){
			return new HospitalDisChargeVo();
		}
		if(StringUtils.isBlank(dateSign)){
			dateSign="4";//自定义
		}
		switch(dateSign){
			case "1"://年
				begin=begin.substring(0,4)+"-01-01";
				end=begin.substring(0,4)+"-12-31";
				break;
			case "2"://月
				begin=begin.substring(0,7)+"-01";
				end=returnEndTime(begin);
				break;
			case "3":
				end=begin;
				break;
		}
		return hospitalDisDao.queryInOutList(begin, end);
	}
	
	public String returnEndTime(String date){
		String end=null;
		date=date.substring(0,7)+"-01";
		Calendar ca=Calendar.getInstance(Locale.CHINESE);
		try {
			ca.setTime(df.parse(date));
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DATE, -1);
			end=df.format(ca.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end;
	}
}
