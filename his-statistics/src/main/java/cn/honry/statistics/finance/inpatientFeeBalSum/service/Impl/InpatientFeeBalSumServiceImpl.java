package cn.honry.statistics.finance.inpatientFeeBalSum.service.Impl;

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

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.inpatientFeeBalSum.dao.InpatientFeeBalSumDAO;
import cn.honry.statistics.finance.inpatientFeeBalSum.service.InpatientFeeBalSumService;
import cn.honry.statistics.finance.inpatientFeeBalSum.vo.FeeBalSumVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
@Service("inpatientFeeBalSumService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientFeeBalSumServiceImpl implements InpatientFeeBalSumService{
	@Autowired
	@Qualifier(value = "inpatientFeeBalSumDAO")
	private InpatientFeeBalSumDAO inpatientFeeBalSumDAO;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Override
	public InpatientInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientInfo arg0) {
		
	}

	@Override
	public List<FeeBalSumVo> getPage(String page, String rows, String typeSerc,String etime,String stime,String deptCode) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_NOW");
		}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return inpatientFeeBalSumDAO.getPage(tnL,page, rows,typeSerc,etime,stime,deptCode);
	}

	@Override
	public int getTotal(String stime, String etime, String typeSerc,String deptCode) throws Exception {
		String redKey = "ZYCYBRYYFJSHZB:"+stime+"_"+etime+"_"+typeSerc+"_"+deptCode;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
				List<String> tnL = null;
				try {
					//1.时间转换
					Date sTime = DateUtils.parseDateY_M_D(stime);
					Date eTime = DateUtils.parseDateY_M_D(etime);
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
							tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",stime,etime);
						}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
							//获得时间差(年)
							int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
							//获取相差年分的分区集合，默认加1
							tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
							tnL.add(0,"T_INPATIENT_FEEINFO_NOW");
						}
					}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
						tnL.add("T_INPATIENT_FEEINFO_NOW");
					}
				} catch (Exception e) {
					e.printStackTrace();
					tnL = null;
				}
				totalNum = inpatientFeeBalSumDAO.getTotal(tnL,stime, etime, typeSerc,deptCode);
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
	public List<MinfeeStatCode> getFeeStatName(String feeStatCode) throws Exception {
		return inpatientFeeBalSumDAO.getFeeStatName(feeStatCode);
	}

	@Override
	public List<FeeBalSumVo> getFeeBalSum(String stime, String etime,String typeSerc) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_NOW");
		}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return inpatientFeeBalSumDAO.getFeeBalSum(tnL,stime, etime, typeSerc);
	}

	@Override
	public FileUtil export(List<FeeBalSumVo> list, FileUtil fUtil) throws Exception {
		int a=0;
		for (FeeBalSumVo model : list) {
			List<MinfeeStatCode> minfeeStatlist = inpatientFeeBalSumDAO.getFeeStatName(model.getFeeStatCode());
			String feeStatName = "";
			if(minfeeStatlist!=null&&minfeeStatlist.size()>0){
				feeStatName=minfeeStatlist.get(0).getFeeStatName();
			}
			a++;
			String record="";
				record = a + ",";
				record += CommonStringUtils.trimToEmpty(feeStatName) + ",";
				record += model.getCost();			
				try {
					fUtil.write(record);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return fUtil;
	}

}
