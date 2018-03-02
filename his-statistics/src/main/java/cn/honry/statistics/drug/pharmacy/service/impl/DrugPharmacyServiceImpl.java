package cn.honry.statistics.drug.pharmacy.service.impl;

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
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.pharmacy.dao.DrugPharmacyDAO;
import cn.honry.statistics.drug.pharmacy.service.DrugPharmacyService;
import cn.honry.statistics.drug.pharmacy.vo.CopyOfPharmacyVoSecond;
import cn.honry.statistics.drug.pharmacy.vo.PharmacyVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
/**
 * 住院部取药统计service实现层
 * @author  lyy
 * @createDate： 2016年6月22日 上午10:21:50 
 * @modifier lyy
 * @modifyDate：2016年6月22日 上午10:21:50
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Service(value="drugPharmacyService")
@Transactional
@SuppressWarnings({"all"})
public class DrugPharmacyServiceImpl implements DrugPharmacyService{
	@Autowired
	@Qualifier(value="drugPharmacyDAO")
	private DrugPharmacyDAO drugPharmacyDAO;
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
	public PharmacyVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(PharmacyVo arg0) {
		
	}
	@Override
	public int getPageDrugPharmacyTotal(String startData, String endData,
			String drugCostType, String drugstore) {
		String redKey = "ZYBQYTJ:"+startData+"_"+endData+"_"+drugCostType+"_"+drugstore;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			List<String> tnL = null;
			try{
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
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",startData,endData);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",yNum+1);
						tnL.add(0,"T_DRUG_OUTSTORE_NOW");
					}
					
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_DRUG_OUTSTORE_NOW");
				}
				totalNum=drugPharmacyDAO.getPageDrugPharmacyTotal(tnL,startData,endData,drugCostType,drugstore);
				redisUtil.set(redKey, totalNum);
			}catch (Exception e) {
				e.printStackTrace();
				tnL = null;
			}
		}
		String val= parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		 return totalNum;
		 
	}
	@Override
	public List<PharmacyVo> getPageDrugPharmacy(String startData,String endData,String  drugCostType,String drugstore,String page,String rows) throws Exception {
		List<String> tnL = null;
		try{
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
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",startData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",yNum+1);
					tnL.add(0,"T_DRUG_OUTSTORE_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_DRUG_OUTSTORE_NOW");
			}
		}catch(Exception e){
			
		}
		return drugPharmacyDAO.getPageDrugPharmacy(tnL,startData,endData,drugCostType,drugstore,page, rows);
	}
	/**
	 * @author conglin
	 * @param startData
	 * @param endData
	 * @param drugCostType
	 * @param drugstore
	 * @return
	 * @throws Exception 
	 */
	public List<CopyOfPharmacyVoSecond> getPageDrugPharmacyPDF(String startData,String endData,String  drugCostType,String drugstore) throws Exception {
		List<String> tnL = null;
		try{
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
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",startData,endData);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",yNum+1);
				tnL.add(0,"T_DRUG_OUTSTORE_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_DRUG_OUTSTORE_NOW");
		}
	 }catch(Exception e){
			
		}
		return drugPharmacyDAO.getPageDrugPharmacyPDF(tnL,startData,endData,drugCostType,drugstore);
	}
	@Override
	public FileUtil export(List<CopyOfPharmacyVoSecond> list, FileUtil fUtil) {
		for (CopyOfPharmacyVoSecond pharmacyVo : list) {
			String record="";
			record=CommonStringUtils.trimToEmpty(pharmacyVo.getDrugName())+",";
			record+=CommonStringUtils.trimToEmpty(pharmacyVo.getOutState())+",";
			record+=CommonStringUtils.trimToEmpty(pharmacyVo.getOutType())+",";
			record+=CommonStringUtils.trimToEmpty(pharmacyVo.getSpec())+",";
			record+=pharmacyVo.getOutNum()+",";
			record+=CommonStringUtils.trimToEmpty(pharmacyVo.getDrugPackagingunit())+",";
			record+=pharmacyVo.getDrugRetailprice()+",";
			record+=pharmacyVo.getOutlCost()+",";
			try {
				fUtil.write(record);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}
	/**
	 * 查询所有的住院部取药列表
	 * @author  wangshujuan
	 * @createDate： 2017年7月22日 上午10:46:33 
	 * @modifier wangshujuan
	 * @modifyDate：2017年7月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<PharmacyVo> queryDrugPharmacyNew(String startData,String endData, String drugCostType, String drugstore, String page,String rows) {
		List<String> tnL = null;
		try{
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
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",startData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",yNum+1);
					tnL.add(0,"T_DRUG_OUTSTORE_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_DRUG_OUTSTORE_NOW");
			}
		}catch(Exception e){
			
		}
		return drugPharmacyDAO.queryDrugPharmacyNew(tnL,startData,endData,drugCostType,drugstore,page, rows);
	}
	/**
	 * 查询所有的住院部取药列表  总条数
	 * @author  wangshujuan
	 * @createDate： 2017年7月22日 上午10:46:33 
	 * @modifier wangshujuan
	 * @modifyDate：2017年7月22日 上午10:46:33
	 * @param：    
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotalDrugPharmacyNew(String startData, String endData,String drugCostType, String drugstore) {

		List<String> tnL = null;
		try{
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
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",startData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",yNum+1);
					tnL.add(0,"T_DRUG_OUTSTORE_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_DRUG_OUTSTORE_NOW");
			}
		}catch(Exception e){
			
		}
		return drugPharmacyDAO.getTotalDrugPharmacyNew(tnL,startData,endData,drugCostType,drugstore);
	}

}
