package cn.honry.statistics.finance.detailedDaily.service.Impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.finance.detailedDaily.dao.DetailedDailyDAO;
import cn.honry.statistics.finance.detailedDaily.service.DetailedDailyService;
import cn.honry.statistics.finance.detailedDaily.vo.VdetailedDaily;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;


@Service("detailedDailyService")
@Transactional
@SuppressWarnings({ "all" })
public class DetailedDailyServiceImpl implements DetailedDailyService{
	@Autowired
	@Qualifier(value = "detailedDailyDAO")
	private DetailedDailyDAO detailedDailyDAO;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public VdetailedDaily get(String arg0) {
		return detailedDailyDAO.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(VdetailedDaily arg0) {
		
	}
	@Override
	public List<VdetailedDaily> queryVdetailedDaily(String beginDate,
			String endDate, String page, String rows) throws Exception {
		List<String> tnL = getTnL(beginDate,endDate);
		return detailedDailyDAO.queryVdetailedDaily(tnL,beginDate, endDate, page, rows);
	}
	public List<VdetailedDaily> queryVdetailedDailyAll(String beginDate,
			String endDate, String page, String rows) throws Exception {
		List<String> tnL = getTnL(beginDate,endDate);
		return detailedDailyDAO.queryVdetailedDaily(tnL,beginDate, endDate, page, rows);
	}
	public List<String> getTnL(String startData,String endData){
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(startData);
			Date eTime = DateUtils.parseDateY_M_D(endData);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCELIST",startData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCELIST",yNum+1);
					tnL.add(0,"T_INPATIENT_BALANCELIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_BALANCELIST_NOW");
			}	
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return tnL;
	}

	@Override
	public int queryVdetailedDailyTotal(String beginDate, String endDate) throws Exception {
		String redKey = "JZCCDMXRB:"+beginDate+"_"+endDate;
		redKey=redKey.replaceAll(",", "-");
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			List<String> tnL = getTnL(beginDate, endDate);
			totalNum = detailedDailyDAO.queryVdetailedDailyTotal(tnL,beginDate, endDate);
			redisUtil.set(redKey, totalNum);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		return totalNum;
	}
	@Override
	public FileUtil export(List<VdetailedDaily> list, FileUtil fUtil) {
		for (VdetailedDaily model : list) {
			String record="";
				record = CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getInpatientNo()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getStatName()) + ",";
				record += model.getTotCost() + ",";
				record += CommonStringUtils.trimToEmpty(model.getOperName()) ;
				try {
					fUtil.write(record);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return fUtil;
	}
}
