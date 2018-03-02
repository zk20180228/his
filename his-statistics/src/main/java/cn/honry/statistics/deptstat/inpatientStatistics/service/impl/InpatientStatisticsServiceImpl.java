package cn.honry.statistics.deptstat.inpatientStatistics.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.statistics.inpatientStatistics.dao.InitInpatientStatisticsDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.monthlyDashboard.dao.MonthlyDashboardDao;
import cn.honry.statistics.deptstat.inpatientCount.dao.InpatientCountDao;
import cn.honry.statistics.deptstat.inpatientStatistics.dao.InpatientStatisticsDao;
import cn.honry.statistics.deptstat.inpatientStatistics.service.InpatientStatisticsService;
import cn.honry.statistics.deptstat.inpatientStatistics.vo.InpatientStatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("inpatientStatisticsService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientStatisticsServiceImpl implements InpatientStatisticsService{
	private final String[] inpatientInfo={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};//住院主表
	@Autowired
	@Qualifier(value = "inpatientStatisticsDao")
	private InpatientStatisticsDao inpatientStatisticsDao;
	@Autowired
	@Qualifier(value="initInpatientStatisticsDao")
    private InitInpatientStatisticsDao initInpatientStatisticsDao;
	
	public void setInitInpatientStatisticsDao(
			InitInpatientStatisticsDao initInpatientStatisticsDao) {
		this.initInpatientStatisticsDao = initInpatientStatisticsDao;
	}

	private String menuAlias;
    
	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	
	public void setInpatientStatisticsDao(
			InpatientStatisticsDao inpatientStatisticsDao) {
		this.inpatientStatisticsDao = inpatientStatisticsDao;
	}
	@Autowired
	@Qualifier(value = "monthlyDashboardDao")
	private MonthlyDashboardDao monthlyDashboardDao;
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}

	@Override
	public List<String> getHTimeList(String time) {
		List<String> timeList=new ArrayList<String>();
		int num = count(time,"-");
		if(num==0){//时间维度  年
			for(int i=4;i>=0;i--){
				timeList.add(""+(Integer.parseInt(time)-i));
			}
		}else if(num==1){//时间维度  月
			String[] arr = time.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			for(int i=5;i>=1;i--){
				Calendar cal = Calendar.getInstance();
				// 设置年份
				cal.set(Calendar.YEAR, year);
				// 设置月份
				cal.set(Calendar.MONTH, month - i);
				// 格式化日期
				time = sdf.format(cal.getTime());
				timeList.add(time);
			}
		}else if(num==2){
			String[] arr = time.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);
			// 获取环比时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(int i=4;i>=0;i--){
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, day);
			cal.add(Calendar.DATE, -i);
			time = sdf.format(cal.getTime());
			timeList.add(time);
			}
		}
		return timeList;
	}
	
	private int count(String srcText, String findText) {
	    int count = 0;
	    int index = 0;
	    while ((index = srcText.indexOf(findText, index)) != -1) {
	        index = index + findText.length();
	        count++;
	    }
	    return count;
	}

	@Override
	public List<String> getTTimeList(String time) {
		List<String> timeList=new ArrayList<String>();
		int num = count(time,"-");
		if(num==0){//时间维度  年
			timeList=null;
		}else if(num==1){//时间维度  月
			String[] arr = time.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			for(int i=4;i>=0;i--){
				time=year-i+"-"+(month<10?"0"+month:month);
				timeList.add(time);
			}
		}else if(num==2){//时间维度  日
			String[] arr = time.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);
			for(int i=4;i>=0;i--){
				time=year-i+"-"+(month<10?"0"+month:month)+"-"+(day<10?"0"+day:day);
				timeList.add(time);
			}
			}
		return timeList;
	}

	@Override
	public Map<String, String> queryDeptCodeName() {
		Map<String,String> map=new HashMap<String,String>();
		List<SysDepartment> list = inpatientStatisticsDao.queryDeptCodeName();
		if(list!=null&&list.size()>0){
			for(SysDepartment s:list){
				map.put(s.getDeptCode(), s.getDeptName());
			}
		}
		return map;
	}

	@Override
	public List<List<InpatientStatisticsVo>> queryDataList(String code, List<String> list,String flag) throws Exception {
		List<List<InpatientStatisticsVo>> iList=new ArrayList<List<InpatientStatisticsVo>>();
		if(list!=null&&list.size()>0){
			int num = count(list.get(0),"-");
			if(num==0){//年
				Boolean b = monthlyDashboardDao.isCollection("ZYRSTJ_YEAR");
				if(b){
					if(list!=null&&list.size()>0){
						//获取开始时间和结束时间
						for(int i=0;i<list.size();i++){
							List<InpatientStatisticsVo> smart= this.queryDataListFromDB(list.get(i),"ZYRSTJ_YEAR",code,flag);
							iList.add(smart);
						}
					}
				}else{
					if(list!=null&&list.size()>0){
						//获取开始时间和结束时间
						for(int i=0;i<list.size();i++){
							Map<String, String> tmap = querySE(list.get(i));
							List<InpatientStatisticsVo> smart=this.queryDataList(tmap.get("stime"),tmap.get("etime"),code,flag);
							iList.add(smart);
						}
					}
				}
			}else if(num==1){
				Boolean b = monthlyDashboardDao.isCollection("ZYRSTJ_MONTH");
                 if(b){
                	 if(list!=null&&list.size()>0){
                		 //获取开始时间和结束时间
                		 for(int i=0;i<list.size();i++){
                			 List<InpatientStatisticsVo> smart=this.queryDataListFromDB(list.get(i),"ZYRSTJ_MONTH",code,flag);
                			 iList.add(smart);
                	 }
         			}
				}else{
					if(list!=null&&list.size()>0){
						//获取开始时间和结束时间
						for(int i=0;i<list.size();i++){
							Map<String, String> tmap = querySE(list.get(i));
							List<InpatientStatisticsVo> smart=this.queryDataList(tmap.get("stime"),tmap.get("etime"),code,flag);
							iList.add(smart);
						}
					}
				}
			}else if(num==2){
				Boolean b = monthlyDashboardDao.isCollection("ZYRSTJ_DAY");
                if(b){
                	if(list!=null&&list.size()>0){
                		//获取开始时间和结束时间
                		for(int i=0;i<list.size();i++){
                			List<InpatientStatisticsVo> smart=this.queryDataListFromDB(list.get(i),"ZYRSTJ_DAY",code,flag);
                			iList.add(smart);
                		}
                	}
				}else{
					if(list!=null&&list.size()>0){
						//获取开始时间和结束时间
						for(int i=0;i<list.size();i++){
							Map<String, String> tmap = querySE(list.get(i));
							List<InpatientStatisticsVo> smart=this.queryDataList(tmap.get("stime"),tmap.get("etime"),code,flag);
							iList.add(smart);
						}
					}
				}
			}
		}
		return iList;
	}

	private List<InpatientStatisticsVo> queryDataListFromDB(String time,String collectName,String code, String flag) throws Exception {
		return inpatientStatisticsDao.queryDataListFromDB(time,collectName,code,flag);
	}

	private List<InpatientStatisticsVo> queryDataList(String firstData, String endData,
			String code, String flag) {
		List<String> tnL = null;
		try {
			//1.时间转换
//			Date sTime = DateUtils.parseDateY_M_D(firstData);
//			Date eTime = DateUtils.parseDateY_M_D(endData);
			tnL=wordLoadDocDao.returnInTables(firstData, endData, inpatientInfo, "ZY");
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return inpatientStatisticsDao.queryDataList(tnL,firstData,endData,code,flag);
	}

	private Map<String,String> querySE(String time) {
     Map<String,String> map=new HashMap<String,String>();
     int num = count(time,"-");
     String stime="";
     String etime="";
		if(num==0){//时间维度  年
			 stime=time+"-01-01";
			 etime=time+"-12-31";
		}else  if(num==1){//时间维度   月
			stime=time+"-01";
			String[] arr = time.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			Calendar cal = Calendar.getInstance();
			// 设置年份
			cal.set(Calendar.YEAR, year);
			// 设置月份
			cal.set(Calendar.MONTH, month - 1);
			// 获取某月最大天数
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			// 设置日历中月份的最大天数
			cal.set(Calendar.DAY_OF_MONTH, lastDay);
			// 格式化日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			etime = sdf.format(cal.getTime());
		}else  if(num==2){//时间维度  日
			 stime=time;
			 etime=stime;
		}
		map.put("stime", stime);
		map.put("etime", etime);
	    return map;
	}


	@Override
	public Map<String, String> queryAreaCodeName() {
		Map<String,String> map=new HashMap<String,String>();
		List<SysDepartment> list = inpatientStatisticsDao.queryAreaCodeName();
		if(list!=null&&list.size()>0){
			for(SysDepartment s:list){
				map.put(s.getDeptCode(), s.getDeptName());
			}
		}
		return map;
	}

	@Override
	public String queryDeptByAreaCode(String code) {
		return inpatientStatisticsDao.queryDeptByAreaCode(code);
	}

	@Override
	public List<SysDepartment> queryArea() {
		List<SysDepartment> list = inpatientStatisticsDao.queryAreaCodeName();
		return list;
	}

	@Override
	public void init_ZYRSTJ(String begin, String end, Integer type) throws Exception {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="timing";
				if(type==1){
					initInpatientStatisticsDao.init_ZYRSTJ(menuAlias, "1", begin);
					initInpatientStatisticsDao.inHostNumber(menuAlias, "1", begin);
			   }else if(type==2){
				String Stime=begin+"-01";
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(Stime);
				// 获取Calendar
				Calendar calendar = Calendar.getInstance();
				// 设置时间,当前时间不用设置
				calendar.setTime(date);
				// 设置日期为本月最大日期
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			    String Etime = format.format(calendar.getTime());
			    
				String e=end+"-01";
				Date date1=format.parse(e);
				// 获取Calendar
				Calendar calendar1 = Calendar.getInstance();
				// 设置时间,当前时间不用设置
				calendar.setTime(date1);
				// 设置日期为本月最大日期
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			    String etime = format.format(calendar.getTime());
			    while(format.parse(Stime).before(format.parse(etime))){
			    	initInpatientStatisticsDao.init_ZYRSTJ(menuAlias, "2", Stime);
			    	initInpatientStatisticsDao.inHostNumber(menuAlias, "2", begin);
//			    	inpatientStatisticsDao.saveOrUpdateToDB(Stime,Etime,type);
			    	calendar.setTime(format.parse(Etime));
			    	calendar.add(Calendar.MONTH, +1);
			    	calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			    	Stime = format.format(calendar.getTime());
			    	calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			    	Etime = format.format(calendar.getTime());
			}}else if(type==3){
				int start=Integer.parseInt(begin);
				String Stime=start+"-01-01";
				String Etime=start+"-12-31";
				String e=end+"-12-31";
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				while(format.parse(Stime).before(format.parse(e))){
					initInpatientStatisticsDao.init_ZYRSTJ(menuAlias, "3", Stime);
					initInpatientStatisticsDao.inHostNumber(menuAlias, "3", begin);
//					inpatientStatisticsDao.saveOrUpdateToDB(Stime,Etime,type);
					start++;
					Stime=start+"-01-01";
					Etime=start+"-12-31";
				}
			}
		}
	}

	@Override
	public String queryDeptByAreaCodes(String code) {
		return inpatientStatisticsDao.queryDeptByAreaCodes(code);
	}
}
