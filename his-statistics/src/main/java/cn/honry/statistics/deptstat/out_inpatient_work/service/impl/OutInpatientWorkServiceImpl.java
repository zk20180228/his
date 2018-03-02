package cn.honry.statistics.deptstat.out_inpatient_work.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.out_inpatient_work.dao.OutInpatientWorkDao;
import cn.honry.statistics.deptstat.out_inpatient_work.service.OutInpatientWorkService;
import cn.honry.statistics.deptstat.out_inpatient_work.vo.OutInpatientWorkVo;
import cn.honry.statistics.sys.stop.vo.OutPatientVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;

@Service("outInpatientWorkService")
@Transactional
@SuppressWarnings({ "all" })
public class OutInpatientWorkServiceImpl implements OutInpatientWorkService{
	
	@Resource
	private OutInpatientWorkDao outInpatientWorkDao;
	
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	public List<OutInpatientWorkVo> outInpatientWorkList(String Btime, String Etime,String areaCode)throws Exception {
		
				 //病床信息表:T_BUSINESS_HOSPITALBED---->没有分区表，没有在线表  
				 //挂号主表:T_REGISTER_MAIN----->有分区表 ,有在线表 ,走分区
				 //住院表:T_INPATIENT_INFO----->没有分区表，有在线表
				List<String> ghList = new ArrayList<String>();//挂号表集合
				List<String> zyList = new ArrayList<String>();//住院表集合
				String beginTime=null;
				String endTime=null;
				int beforeOne=Integer.parseInt(Etime.substring(0, 4))-1;//查询年的前一年的int类型
				if(!StringUtils.isNotBlank(Btime)){//按月查询
					
					//计算开始时间，结束时间，走分区
					beginTime=beforeOne+Etime.substring(4, 7)+"-01";//查询数据的开始时间，往前推一年
					endTime=Etime+"-"+getLastDay(Etime);//查询数据的结束时间
				}else{//查询时间条件为某一年的几个月
					
					//计算开始时间，结束时间，走分区
					beginTime=beforeOne+Btime.substring(4, 7)+"-01";//查询数据的开始时间，往前推一年
					endTime=Etime+"-"+getLastDay(Etime);//查询数据的结束时间
				}

				//转换查询时间
				Date sTime = DateUtils.parseDateY_M_D(beginTime);
				Date eTime = DateUtils.parseDateY_M_D(endTime);
				
				//获取门诊数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				
				//获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				
				//获得挂号表在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
				
				//获取住院表数据保存的时间
				String s = parameterInnerDAO.getParameterByCode("saveTime");//单位：月，参数值：1，因此要乘以30，代表30天，见下一行代码
				
				//住院
				int zyNum = Integer.parseInt(s)*30;
				
				//获得住院在线库数据应保留最小时间(值是某一年月日)
				Date cZYTime = DateUtils.addDay(dTime, -zyNum+1);
				
				//判断是否走分区(挂号表)
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						
						ghList = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",beginTime,endTime);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginTime);
						
						//获取相差年分的分区集合，默认加1
						ghList = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
						ghList.add(0,"T_REGISTER_MAIN_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					ghList.add("T_REGISTER_MAIN_NOW");
				}
				
				//判断是否走分区(住院表)
				if(DateUtils.compareDate(sTime, cZYTime)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
					if(DateUtils.compareDate(eTime, cZYTime)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
						zyList = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",beginTime,endTime);
						if(zyList!=null&&zyList.size()==0){//说明没有分表,只需要添加历史线表即可，以后数据库添加分表后，可不走这个if代码块
							zyList.add("T_INPATIENT_INFO");
						}
					}else{//查询主表和分区表
						
						//获取时间差（年）
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cZYTime), beginTime);
						
						//获取相差年份的分区集合 
						zyList = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
						if(zyList!=null&&zyList.size()==0){//说明没有分表,只需要添加历史线表即可，以后数据库添加分表后，可不走这个if代码块
							zyList.add("T_INPATIENT_INFO");
						}
						zyList.add(0,"T_INPATIENT_INFO_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					zyList.add("T_INPATIENT_INFO_NOW");
				}	
			
			return outInpatientWorkDao.outInpatientWorkList(Btime,Etime,areaCode,ghList,zyList);
		
	}

	
	
	
	
	
	/**
	 * 
	 * <p>获取某月的最后一天 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年6月7日 下午8:11:00 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月5日 下午6:11:00 
	 * @ModifyRmk:  添加注释模板
	 * @version: V1.0
	 * @param date 长度大于：yyyy-MM-dd的格式
	 * @return 返回该月的最后一天的字符串表示形式，兼容闰年
	 * @throws Exception
	 *
	 */
	public String getLastDay(String date) throws Exception {
		 date= date.substring(0, 7);
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		 Date time =null;
		 time = dateFormat.parse(date);
		 Calendar  calendar =  Calendar.getInstance(); 
		 calendar.setTime(time);
		 final  int  lastDay  =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		 Date   lastDate   =   calendar.getTime();  
	     lastDate.setDate(lastDay);  
	     SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		
	     return dateFormat1.format(lastDate).substring(8, 10);
	}
	






	@Override
	public FileUtil export(List<OutInpatientWorkVo> list, FileUtil fUtil)throws Exception{
		for (OutInpatientWorkVo v : list) {
			String record="";
			record=v.getProjectName()+",";
			record+=v.getBeginNum()+",";
			record+=v.getEndNum()+",";
			record+=v.getIncreaseNum()+",";
			record+=v.getIncreasePercent()+",";
			fUtil.write(record);
		}
		return fUtil;
	}






	@Override
	public List<OutInpatientWorkVo> outInpatientWorkListByMongo(String Btime,String Etime, String areaCode) throws Exception {
		
		return outInpatientWorkDao.outInpatientWorkListByMongo(Btime, Etime, areaCode);
	}
}
