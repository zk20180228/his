package cn.honry.statistics.drug.anesthetic.service.impl;

import java.io.IOException;
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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.drugAnesthetic.dao.InitDrugAnestheticDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.statistics.drug.anesthetic.dao.AnestheticDao;
import cn.honry.statistics.drug.anesthetic.service.AnestheticService;
import cn.honry.statistics.drug.anesthetic.vo.Anestheticvo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
@Service("anestheticService")
@Transactional
@SuppressWarnings({ "all" })
public class AnestheticServiceImpl implements AnestheticService{
	@Autowired
	@Qualifier(value = "initDrugAnestheticDao")
	private InitDrugAnestheticDao initDrugAnestheticDao;
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="anestheticDao")
	private AnestheticDao anestheticDao;
	/**
	 *
	 * @Description：麻醉精神药品统计
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public List<Anestheticvo> getAnestheList(String login,String end,String drug,String deptId, String rows, String page, String flag) {
		Map<String, List<String>> map = this.partition(login, end);
		return anestheticDao.getAnestheList(login, end, drug, deptId, rows, page, flag,map);
	}
	/**
	 *
	 * @Description：获取当前登陆科室
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public String getDeptName(String deptId) {
		return anestheticDao.getDeptName(deptId);
	}

	@Override
	public List<Anestheticvo> queryInvLogExp(String login, String end,
			String drug, String deptId) {
		Map<String, List<String>> map = this.partition(login, end);
		return anestheticDao.getAnestheList(login,end,drug,deptId,"","","1",map);
	}

	@Override
	public FileUtil export(List<Anestheticvo> list, FileUtil fUtil) {
		for (Anestheticvo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getPno()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getPatientName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDoctName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugSpec()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugPack()) + ",";
			record +=model.getNum()+",";
			record += (model.getDrugedDate() != null ? DateUtils.formatDateY_M_D_H_M_S(model.getDrugedDate()) : "") + ",";
			record += CommonStringUtils.trimToEmpty("") + ",";
			record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getMeark()) ;
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}
	@Override
	public Integer getAnestheTotal(String login, String end, String drug, String deptId) {
		Map<String, List<String>> map = this.partition(login, end);
		return anestheticDao.getAnestheTotal(login, end, drug, deptId,map);
	}
	public Map<String,List<String>> partition(String startTime, String endTime) {
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		try{
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取门诊数据保留时间
			String dateNum1 = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获取住院数据保留时间
			String dateNum2 = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum2)){
				dateNum2="30";
			}
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得门诊在线库数据应保留最小时间
			Date cTime1 = DateUtils.addDay(dTime,-Integer.parseInt(dateNum1)+1);
			//获得住院在线库数据应保留最小时间
			Date cTime2 = DateUtils.addDay(dTime, -Integer.parseInt(dateNum2)+1);
			//获取当前表最大时间及最小时间
			List<String> tnL1 = new ArrayList<String>();
			List<String> tnL2 = new ArrayList<String>();
			List<String> tnL3 = new ArrayList<String>();
			List<String> tnL4 = new ArrayList<String>();
			List<String> tnL5 = new ArrayList<String>();
			//判断查询类型T_DRUG_OUTSTORE药品出库记录表
			if(DateUtils.compareDate(sTime, cTime2)==-1){
				if(DateUtils.compareDate(eTime, cTime2)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime2),startTime);
					//获取相差年分的分区集合，默认加1
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",yNum+1);
					tnL1.add(0,"T_DRUG_OUTSTORE_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL1.add("T_DRUG_OUTSTORE_NOW");
			}
			//判断查询类型T_REGISTER_MAIN门诊挂号：挂号主表
			if(DateUtils.compareDate(sTime, cTime1)==-1){
				if(DateUtils.compareDate(eTime, cTime1)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime1),startTime);
					//获取相差年分的分区集合，默认加1
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
					tnL2.add(0,"T_REGISTER_MAIN_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL2.add("T_REGISTER_MAIN_NOW");
			}
			//判断查询类型T_INPATIENT_INFO住院主表
			if(DateUtils.compareDate(sTime, cTime2)==-1){
				if(DateUtils.compareDate(eTime, cTime2)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL3 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime2),startTime);
					//获取相差年分的分区集合，默认加1
					tnL3 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL3.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL3.add("T_INPATIENT_INFO_NOW");
			}
			//判断查询类型T_OUTPATIENT_FEEDETAIL处方明细表
			if(DateUtils.compareDate(sTime, cTime1)==-1){
				if(DateUtils.compareDate(eTime, cTime1)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL4 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime1),startTime);
					//获取相差年分的分区集合，默认加1
					tnL4 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					tnL4.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL4.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
			//判断查询类型T_INPATIENT_MEDICINELIST住院药品明细表
			if(DateUtils.compareDate(sTime, cTime2)==-1){
				if(DateUtils.compareDate(eTime, cTime2)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL5 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime2),startTime);
					//获取相差年分的分区集合，默认加1
					tnL5 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",yNum+1);
					tnL5.add(0,"T_INPATIENT_MEDICINELIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL5.add("T_INPATIENT_MEDICINELIST_NOW");
			}
			map.put("T_DRUG_OUTSTORE", tnL1);
			map.put("T_REGISTER_MAIN", tnL2);
			map.put("T_INPATIENT_INFO", tnL3);
			map.put("T_OUTPATIENT_FEEDETAIL", tnL4);
			map.put("T_INPATIENT_MEDICINELIST", tnL5);
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	/**  
	 * 
	 * 初始化数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月26日 下午7:21:45 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月26日 下午7:21:45 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void init_MZJSYPTJ(String begin, String end, Integer type)
			throws Exception {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="MZJSYPTJ";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				initDrugAnestheticDao.init_MZJSYPTJ_DAY(menuAlias, "HIS", begin);
			}
		}
	}
	/**  
	 * 
	 * 从mongondb中获取数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月28日 上午9:29:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月28日 上午9:29:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<Anestheticvo> queryAnestheticvo(String begin, String end, String drug, String deptId, String rows, String page) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Anestheticvo> vos=new ArrayList<Anestheticvo>();
		Anestheticvo vo=null;
		DBCursor cursor=null;
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBList dateList=new BasicDBList();
		BasicDBObject data1= new BasicDBObject();//查询开始时间
		BasicDBObject data2= new BasicDBObject();//查询结束时间
		data1.append("drugedDate", new BasicDBObject("$gte",begin));
		data2.append("drugedDate", new BasicDBObject("$lte",end));
		dateList.add(data1);
		dateList.add(data2);
		bdObject.put("$and", dateList);
		if (!"1".equals(drug)) {
			bdObject.put("drugType", drug);
		}
		if (!"1".equals(deptId)) {
			bdObject.put("deptCode", deptId);
		}
		if (rows==null && page==null) {
			cursor = new MongoBasicDao().findAlldata("MZJSYPTJ", bdObject);
		}else{
			cursor=new MongoBasicDao().findAllDataSortBy("MZJSYPTJ","drugedDate", bdObject,Integer.parseInt(rows),Integer.parseInt( page));
		}
		try {
			while(cursor.hasNext()){
				vo = new Anestheticvo();
				DBObject dbCursor = cursor.next();
				String  deptName =(String) dbCursor.get("deptName");
				String  pno =(String) dbCursor.get("pno");
				String  patientName =(String) dbCursor.get("patientName");
				String doctName=(String) dbCursor.get("doctName");
				String doctCode=(String) dbCursor.get("doctCode");
				String drugName=(String) dbCursor.get("drugName");
				String drugSpec=(String) dbCursor.get("drugSpec");
				String drugPack=(String) dbCursor.get("drugPack");
				Double num=(Double) dbCursor.get("num");
				String drugedDate=(String) dbCursor.get("drugedDate");
				String meark=(String) dbCursor.get("meark");
				String name=(String) dbCursor.get("name");
				vo.setDeptName(deptName);
				vo.setPno(pno);
				vo.setDoctCode(doctCode);
				vo.setDoctName(doctName);
				vo.setDrugedDate(format.parse(drugedDate));
				vo.setDrugName(drugName);
				vo.setDrugPack(drugPack);
				vo.setDrugSpec(drugSpec);
				vo.setMeark(meark);
				vo.setName(name);
				vo.setNum(num);
				vo.setPatientName(patientName);
				vos.add(vo);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(vos!=null&&vos.size()>0){
			return vos;
		}else{
			return new ArrayList<Anestheticvo>();
		}
	}

}
