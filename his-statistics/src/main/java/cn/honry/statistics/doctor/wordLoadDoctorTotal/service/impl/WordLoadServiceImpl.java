package cn.honry.statistics.doctor.wordLoadDoctorTotal.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.HospitalWork;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.bi.inpatient.inpatientDoctor.dao.InpatientDoctorDao;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.dao.WordLoadDoctorTotalDao;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.service.WordLoadService;
import cn.honry.statistics.doctor.wordLoadDoctorTotal.vo.WordLoadVO;
import cn.honry.utils.DateUtils;
@Service("wordLoadService")
public class WordLoadServiceImpl implements WordLoadService {
	String[] inTable={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};
	String[] orderTable={"T_INPATIENT_ORDER_NOW","T_INPATIENT_ORDER"};
	static SimpleDateFormat sdFull=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar ca=Calendar.getInstance();
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="wordLoadDoctorTotalDao")
	private WordLoadDoctorTotalDao wordLoadDoctorTotalDao;
	
	@Autowired
	@Qualifier(value="inpatientDoctorDao")
	private InpatientDoctorDao inpatientDoctorDao;
	
	
	public void setInpatientDoctorDao(InpatientDoctorDao inpatientDoctorDao) {
		this.inpatientDoctorDao = inpatientDoctorDao;
	}

	public void setWordLoadDoctorTotalDao(
			WordLoadDoctorTotalDao wordLoadDoctorTotalDao) {
		this.wordLoadDoctorTotalDao = wordLoadDoctorTotalDao;
	}
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}

	@Override
	public List<WordLoadVO> queryInhosWordTotal(String begin, String end,
			String dept, String doctorCode) {
		return wordLoadDoctorTotalDao.queryInhosWordTotal(begin, end, dept, doctorCode);
	}
	
	
	public void init_ZYYSGZLTJ_Total(String startTime, String endTime) {
		List<String> tnL=wordLoadDocDao.returnInTables(startTime, endTime, inTable, "ZY");
		wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Total(tnL, startTime, endTime);	
	}
	public void init_ZYYSGZLTJ_Detail(String startTime, String endTime) {
		List<String> tnL=wordLoadDocDao.returnInTables(startTime, endTime, orderTable, "ZY");
		wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Detail(tnL,startTime, endTime);	
	}
	public void init_ZYYSGZLTJ_Num(String startTime, String endTime) {
		List<String> tnL=wordLoadDocDao.returnInTables(startTime, endTime, inTable, "ZY");
		wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Num(tnL,startTime, endTime);	
	}
	
	
	
	
	
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
/**************************分区查询************************************************************************************************************************/
@Override
public List<WordLoadVO> queryForDB(String date, String dateSign) {
	if(null==date||null==dateSign||"".equals(date)||"".equals(dateSign)){
		return new ArrayList<WordLoadVO>();
	}
	String queryMongo=null;
	if("1".equals(dateSign)){
		queryMongo="ZYYSGZLTJ_NUM_YEAR";
	}else if("2".equals(dateSign)){
		queryMongo="ZYYSGZLTJ_NUM_MONTH";
	}else{
		queryMongo="ZYYSGZLTJ_NUM_DAY";
	}
	boolean sign=new MongoBasicDao().isCollection(queryMongo);
	List<WordLoadVO> list=new ArrayList<WordLoadVO>();
	if(sign){
		list=wordLoadDoctorTotalDao.queryForDB(date, dateSign);
		if(list.size()>0){
			return list;
		}
		return new ArrayList<WordLoadVO>();
	}else{//分区查
		List<String> dateList=this.returnBeginAndEnd(date, dateSign);
		List<String> tnL=new ArrayList<String>();
		tnL=wordLoadDocDao.returnInTables(dateList.get(0), dateList.get(1), inTable, "ZY");
		list=wordLoadDoctorTotalDao.queryForOra(tnL, dateList.get(0), dateList.get(1), dateSign);
	}
	return list;
 }
//同比
@Override
public List<WordLoadVO> queryForDBSame(String date, String dateSign) {
	if(null==date||null==dateSign||"".equals(date)||"".equals(dateSign)||"1".equals(dateSign)){
		return new ArrayList<WordLoadVO>();
	}
	String queryMongo=null;
	if("1".equals(dateSign)){
		queryMongo="ZYYSGZLTJ_DOC_DETAIL_YEAR";
	}else if("2".equals(dateSign)){
		queryMongo="ZYYSGZLTJ_DOC_DETAIL_MONTH";
	}else{
		queryMongo="ZYYSGZLTJ_DETAIL_DAY";
	}
	boolean sign=new MongoBasicDao().isCollection(queryMongo);
	List<WordLoadVO> list=new ArrayList<WordLoadVO>();
	if(sign){
		list=wordLoadDoctorTotalDao.queryForDBSame(date, dateSign,queryMongo);
		if(list.size()>0){
			return list;
		}
		return new ArrayList<WordLoadVO>();
	}else{
		Map<String,String> map=this.queryBetweenTime(date, dateSign,"TB");
		List<String> tnL=new ArrayList<String>();
		for(String key:map.keySet()){
			tnL=wordLoadDocDao.returnInTables(key, map.get(key), orderTable, "ZY");
			list.add(wordLoadDoctorTotalDao.queryForOraSque(tnL, key, map.get(key), dateSign));
		}
		

	}
	
	return new ArrayList<WordLoadVO>();
}

@Override
public List<WordLoadVO> queryForDBSque(String date, String dateSign) {
	if(StringUtils.isBlank(date)||StringUtils.isBlank(dateSign)){
		return new ArrayList<WordLoadVO>();
	}
	String queryMongo=null;
	if("1".equals(dateSign)){
		queryMongo="ZYYSGZLTJ_DETAIL_DAY";
	}else if("2".equals(dateSign)){
		queryMongo="ZYYSGZLTJ_DOC_DETAIL_MONTH";
	}else{
		queryMongo="ZYYSGZLTJ_DOC_DETAIL_YEAR";
	}
	boolean sign=new MongoBasicDao().isCollection(queryMongo);
	List<WordLoadVO> list=new ArrayList<WordLoadVO>();
	if(sign){
		list=wordLoadDoctorTotalDao.queryForDBSque(date, dateSign,queryMongo);
		if(list.size()>0){
			return list;
		}
		return new ArrayList<WordLoadVO>();
	}else{
		Map<String,String> map=this.queryBetweenTime(date, dateSign,"HB");
		List<String> tnL=new ArrayList<String>();
		for(String key:map.keySet()){
			tnL=wordLoadDocDao.returnInTables(key, map.get(key), orderTable, "ZY");
			list.add(wordLoadDoctorTotalDao.queryForOraSque(tnL, key, map.get(key), dateSign));
		}
	}
	return new ArrayList<WordLoadVO>();
}
/**
 * 住院医生工作量 list
 */
@Override
public Map<String,Object> queryForDBList(String begin, String end,String depts,String doctors,String menuAlias,String rows,String page) {
	Map<String,Object> map=new HashMap<String,Object>();
	map.put("total", 0);
	map.put("rows", new ArrayList<WordLoadVO>());
	if(null==begin||null==begin||"".equals(end)||"".equals(end)){
		return map;
	}
	boolean sign=new MongoBasicDao().isCollection("ZYSRQK_DETAIL_DAY");
	if(sign){
		map=wordLoadDoctorTotalDao.queryForDBList(begin, end,depts,doctors,menuAlias,rows,page);
		return map;
	}else{//分区查
		List<String> tnL=new ArrayList<String>();
		tnL=wordLoadDocDao.returnInTables(begin, end, orderTable, "ZY");
		map=wordLoadDoctorTotalDao.queryForOraList(tnL, begin, end,depts,doctors,menuAlias,rows,page);
	}
	return map;
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
	
public List<String> returnBeginAndEnd(String begin,String dateSign){
		 
		List<String> list=new ArrayList<String>(2);
		 if("1".equals(dateSign)){
			list.add(begin+"-01-01 00:00:00");
			list.add(begin+"-12-31 23:59:59");
		}else if("2".equals(dateSign)){
			list.add(begin+"-01 00:00:00");
			list.add(begin+"-31 23:59:59");
		}else{
			list.add(begin+" 00:00:00");
			list.add(begin+" 23:59:59");
		}
	 return list;
  }

	@Override
	public List<WordLoadVO> queryTop(String date, String dateSign,
			String docOrDept) {
		if(StringUtils.isBlank(dateSign)&&StringUtils.isBlank(date)&&StringUtils.isBlank(docOrDept)){
			return new ArrayList<WordLoadVO>();
		}
		String collections;//DB名
		if("1".equals(dateSign)){
			if("dept".equals(docOrDept)){
				collections="ZYYSGZLTJ_DEPT_DETAIL_YEAR";
			}else{
				collections="ZYYSGZLTJ_DOC_DETAIL_YEAR";
			}
			
			
		}else if("2".equals(dateSign)){
			if("dept".equals(docOrDept)){
				collections="ZYYSGZLTJ_DEPT_DETAIL_MONTH";
			}else{
				collections="ZYYSGZLTJ_DOC_DETAIL_MONTH";
			}
		}else{
			collections="ZYYSGZLTJ_DETAIL_DAY";
		}
		boolean sign=new MongoBasicDao().isCollection(collections);
		List<WordLoadVO> list=new ArrayList<WordLoadVO>();
		if(sign){
			list=wordLoadDoctorTotalDao.queryDeptDocTopFive(date, dateSign, collections, docOrDept);
			if(list.size()>0){
				return list;
			}
			return new ArrayList<WordLoadVO>();
		}else{
			List<String> dateList=this.returnBeginAndEnd(date, dateSign);
			List<String> tnL=new ArrayList<String>();
			tnL=wordLoadDocDao.returnInTables(dateList.get(0), dateList.get(1), orderTable, "ZY");
			list=wordLoadDoctorTotalDao.queryDeptDocTopFive(tnL, dateList.get(0), dateList.get(1),docOrDept);
		}
		return list;
	}
	
	@Override
	public List<MenuListVO> getDoctorList(String deptTypes,String menuAlias) {
		return wordLoadDoctorTotalDao.getDoctorList(deptTypes,menuAlias);
	}
	
	
	@Override
	public void init_ZYYSGZLTJ(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			if(1==type){//日数据 dateformate:yyyy-MM-dd
					String menuAlias="ZYYSGZLTJ";
					wordLoadDocDao.init_ZYYSGZLTJ_effect(menuAlias, null, begin);
//					begin=begin+" 00:00:00";
//					end=end+" 00:00:00";
					wordLoadDocDao.init_ZYYSGZLTJ_Total(menuAlias, "HIS", begin);
					wordLoadDocDao.init_ZYYSGZLTJ_Detail(menuAlias, "HIS", begin);
					wordLoadDocDao.init_ZYYSGZLTJ_Num(menuAlias, "HIS", begin);
//					init_ZYYSGZLTJ_Total(begin,end);
//					init_ZYYSGZLTJ_Detail(begin,end);
//					init_ZYYSGZLTJ_Num(begin,end);
					
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime())+"-01";//本月第一天
					ca.set(Calendar.DATE, ca.getActualMaximum(Calendar.DATE));
					end=DateUtils.formatDateY_M_D(ca.getTime());//本月最后一天
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Detail_MoreDay(begin, end, "2", "DOC");
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Detail_MoreDay(begin, end, "2", "DEPT");
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Num_MoreDay(begin, end, "2");
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Total_MoreDay(begin, end, "2");
					ca.add(Calendar.DATE, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
				Date beginDate=DateUtils.parseDateY(begin);
				Date endDate=DateUtils.parseDateY(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				String temp;
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY(ca.getTime())+"-01";
					temp=begin.substring(0,4);
					end=temp+"-12";
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Detail_MoreDay(begin, end, "3", "DOC");
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Detail_MoreDay(begin, end, "3", "DEPT");
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Num_MoreDay(begin, end, "3");
					wordLoadDoctorTotalDao.init_ZYYSGZLTJ_Total_MoreDay(begin, end, "3");
					ca.add(Calendar.YEAR, 1);
				}
			}
		}
		
	}

	@Override
	public void init_ZYSRQK(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="ZYSRQK";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
					String his="HIS";
//					wordLoadDocDao.init_ZYSRQK_Dept(menuAlias, his, begin);
//					wordLoadDocDao.init_ZYSRQK_Doc(menuAlias, his, begin);
//					wordLoadDocDao.init_ZYSRQK_Deatail(menuAlias, his, begin);
					wordLoadDocDao.pCDeptWorktotal(menuAlias, his, begin);
					inpatientDoctorDao.pcDoctorWorkTotal(begin);
//					wordLoadDocDao.init_ZYSRQK_MzZy(menuAlias, his, begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());//本月第一天
//					wordLoadDocDao.init_ZYSRQK_DeptOrDoc(menuAlias, "2", begin, "DOC");
//					wordLoadDocDao.init_ZYSRQK_DeptOrDoc(menuAlias, "2", begin, "DEPT");
//					wordLoadDocDao.init_ZYSRQK_MzZy_MoreDay(menuAlias, "2", begin);
					wordLoadDocDao.pcDeptWorkTotalMonthAndYear(menuAlias, "2", begin);
					wordLoadDocDao.pcDoctorWorkTotalMonthAndYear(menuAlias, "2", begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
				Date beginDate=DateUtils.parseDateY(begin);
				Date endDate=DateUtils.parseDateY(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY(ca.getTime());
//					wordLoadDocDao.init_ZYSRQK_DeptOrDoc(menuAlias, "3", begin, "DOC");
//					wordLoadDocDao.init_ZYSRQK_DeptOrDoc(menuAlias, "3", begin, "DEPT");
//					wordLoadDocDao.init_ZYSRQK_MzZy_MoreDay(menuAlias, "3", begin);
					wordLoadDocDao.pcDeptWorkTotalMonthAndYear(menuAlias, "3", begin);
					wordLoadDocDao.pcDoctorWorkTotalMonthAndYear(menuAlias, "3", begin);
					ca.add(Calendar.YEAR, 1);
				}
			}
		}
		
		
	}

	@Override
	public List<HospitalWork> queryHosWorkTotal( String begin,
			String end, String menuAlias, String depts, String doctors,String page,String rows) {
		if(StringUtils.isBlank(begin)||StringUtils.isBlank(end)){
			return new ArrayList<HospitalWork>();
		}
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end,new String[]{"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"}, "ZY");
		return wordLoadDoctorTotalDao.queryHosWorkTotal(tnL, begin, end, menuAlias, depts, doctors,page,rows);
	}

	@Override
	public int queryHosWorkTotal(String begin, String end, String menuAlias,
			String depts, String doctors) {
		if(StringUtils.isBlank(begin)||StringUtils.isBlank(end)){
			return 0;
		}
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end,new String[]{"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"}, "ZY");
		return wordLoadDoctorTotalDao.queryHosWorkTotal(tnL, begin, end, menuAlias, depts, doctors);	}

	@Override
	public Map<String, Object> queryHosWorkMap(String begin, String end,
			String menuAlias, String depts, String doctors, String page,
			String rows) {
		return wordLoadDoctorTotalDao.queryHosWorkTotal(begin, end, menuAlias, depts, doctors, page, rows);
	}
}
