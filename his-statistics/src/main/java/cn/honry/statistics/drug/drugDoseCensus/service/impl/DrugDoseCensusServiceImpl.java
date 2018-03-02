package cn.honry.statistics.drug.drugDoseCensus.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.deptstat.inpatientInfoTable.vo.InpatientInfoTableVo;
import cn.honry.statistics.drug.drugDoseCensus.dao.DrugDoseCensusDAO;
import cn.honry.statistics.drug.drugDoseCensus.service.DrugDoseCensusService;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;

/**
 * 住院发药量统计sevice实现类
 * @author  lyy
 * @createDate： 2016年6月20日 下午3:46:19 
 * @modifier lyy
 * @modifyDate：2016年6月20日 下午3:46:19
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Service("drugDoseCensusService")
@Transactional
@SuppressWarnings({ "all" })
public class DrugDoseCensusServiceImpl implements DrugDoseCensusService{
	@Autowired
	@Qualifier(value = "drugDoseCensusDao")
	private DrugDoseCensusDAO doseCensusDao;
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
	public DrugOutstore get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(DrugOutstore arg0) {
		
	}
	
	public List<String>  getTnl (String startData,String endData,StatVo statVo){
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
			e.printStackTrace();
			tnL = null;
		}
		return tnL;
	}
	
	@Override
	public List<DrugOutstore> getPageDrugDose(String startData,String endData,String drugstore,String page, String rows,StatVo statVo) throws Exception {
		List<String> tnL=getTnl(startData,endData,statVo);
		return doseCensusDao.queryDrugDose(tnL,startData,endData,drugstore,page,rows);
	}
	@Override
	public int getTatalDrugDose(String startData,String endData,String drugstore,StatVo statVo) throws Exception {
		String redKey = "ZYFYGZLTJ:"+startData+"_"+endData+"_"+drugstore;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			List<String> tnL=getTnl(startData,endData,statVo);
			totalNum = doseCensusDao.getTatalDrugDose(tnL,startData,endData,drugstore);
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
	public List<SysDepartment> queryStoreDept(String name) throws Exception {
		return doseCensusDao.queryStoreDept(name);
	}

	@Override
	public List<DrugOutstore> expQueryDrugDoseCensus(String startData, String endData, String drugstore,StatVo statVo) {
		List<String> tnL=getTnl(startData,endData,statVo);
		return doseCensusDao.expQueryDrugDoseCensus(tnL,startData,endData,drugstore);
	}

	@Override
	public FileUtil export(List<DrugOutstore> list, FileUtil fUtil) throws Exception {
		Map<String, String> map = doseCensusDao.getStoreDEptMap();
		if(map != null){
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setDrugDeptCode(map.get(list.get(i).getDrugDeptCode()));
			}
		}
		for (DrugOutstore outstore : list) {
			String record="";
			record=CommonStringUtils.trimToEmpty(outstore.getApproveOpercode())+",";
			record+=CommonStringUtils.trimToEmpty(outstore.getExamOpercode())+",";
			record+=CommonStringUtils.trimToEmpty(outstore.getOpType())+",";
			record+=CommonStringUtils.trimToEmpty(outstore.getDrugDeptCode())+",";
			record+=outstore.getOutNum()+",";
			try {
				fUtil.write(record);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

	@Override
	public StatVo findMaxMin() throws Exception {
		return doseCensusDao.findMaxMin();
	}

	@Override
	public Map<String, String> getStoreDEptMap() throws Exception {
		return doseCensusDao.getStoreDEptMap();
	}


	/**  
	 * 住院统计工作量
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<DrugOutstore> queryDrugDoseCen(String startData,String endData,String deptCode,String menuAlias,String page,String rows) {
		boolean flag=new MongoBasicDao().isCollection("ZYFYGZLTJ");
		List<DrugOutstore> list=new ArrayList<DrugOutstore>();
		if(flag){
			list=doseCensusDao.queryDrugDoseCenForDB(startData,endData,deptCode,menuAlias,page,rows);
		}else{
			List<String> tnL = null;
			List<String> tnL1 = null;
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
				tnL1 = new ArrayList<String>();
				//判断查询类型   sTime早于
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",startData,endData);
						tnL1 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",startData,endData);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",yNum+1);
						tnL1 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
						tnL.add(0,"T_DRUG_APPLYOUT_NOW");
						tnL1.add(0,"T_INPATIENT_INFO_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）t_inpatient_info，t_drug_applyout
					tnL.add("T_DRUG_APPLYOUT_NOW");
					tnL1.add("T_INPATIENT_INFO_NOW");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tnL = null;
			}
			return doseCensusDao.queryDrugDoseCen(tnL,tnL1,startData,endData,deptCode,menuAlias,page, rows);
		}
		return list;
	}

	/**  
	 * 住院统计工作量   总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月21日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月21日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalDrugDoseCen(String startData, String endData,String deptCode, String menuAlias) {
		List<String> tnL = null;
		List<String> tnL1 = null;
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
			tnL1 = new ArrayList<String>();
			//判断查询类型   sTime早于
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",startData,endData);
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",startData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",yNum+1);
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL.add(0,"T_DRUG_APPLYOUT_NOW");
					tnL1.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）t_inpatient_info，t_drug_applyout
				tnL.add("T_DRUG_APPLYOUT_NOW");
				tnL1.add("T_INPATIENT_INFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return doseCensusDao.getTotalDrugDoseCen(tnL,tnL1,startData,endData,deptCode,menuAlias);
	}
	
}
