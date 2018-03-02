package cn.honry.statistics.bi.bistac.undrugDataAnalysis.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.dao.UndrugDataAnalysisDao;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.service.UndrugDataAnalysisService;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.vo.UndrugDataVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
@Service("undrugAnalysisService")
@Transactional
@SuppressWarnings({ "all" })
public class UndrugDataAnalysisServiceImpl implements UndrugDataAnalysisService{
	@Autowired
	@Qualifier(value = "undrugAnalysisDao")
	private UndrugDataAnalysisDao undrugDataAnalysisDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	
	@Override
	public List<UndrugDataVo> queryUndrugTypeFee(String firstData,
			String endData) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(firstData);
			Date eTime = DateUtils.parseDateY_M_D(endData);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",firstData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return undrugDataAnalysisDao.queryUndrugDataFromOracle(tnL,firstData,endData);
	}

	@Override
	public List<UndrugDataVo> queryUndrugDeptFeeTop5(String firstData,
			String endData) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(firstData);
			Date eTime = DateUtils.parseDateY_M_D(endData);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",firstData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return undrugDataAnalysisDao.queryUndrugDeptFeeTop5(tnL,firstData,endData);
	}

	@Override
	public Map<String, String> querydeptCodeAndNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<SysDepartment> list=deptInInterDAO.getAll();
		if(list!=null&&list.size()>0){
			for(SysDepartment d:list){
				map.put(d.getDeptCode(),d.getDeptName());
			}
		}
		return map;
	}

	@Override
	public List<UndrugDataVo> queryUndrugDocFeeTop5(String firstData,
			String endData) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(firstData);
			Date eTime = DateUtils.parseDateY_M_D(endData);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",firstData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return undrugDataAnalysisDao.queryUndrugDocFeeTop5(tnL,firstData,endData);
	}

	@Override
	public List<UndrugDataVo> queryUndrugFeeMoM(String firstData,String endData,String type) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(firstData);
			Date eTime = DateUtils.parseDateY_M_D(endData);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",firstData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return undrugDataAnalysisDao.queryUndrugFeeMoM(tnL,firstData,endData,type);
	}

	@Override
	public UndrugDataVo queryUndrugFeeSameDOM(String firstData, String endData,
			String type) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(firstData);
			Date eTime = DateUtils.parseDateY_M_D(endData);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",firstData,endData);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),firstData);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return undrugDataAnalysisDao.queryUndrugFeeSameDOM(tnL,firstData,endData,type);
	}

	@Override
	public List<UndrugDataVo> queryUndrugTypeFeeFromDB(String searchTime, String t,String queryMongo) throws Exception {
		return undrugDataAnalysisDao.queryUndrugTypeFeeFromDB(searchTime,t,queryMongo);
	}

	@Override
	public List<UndrugDataVo> queryUndrugDeptFeeTop5FromDB(String searchTime,
			String t,String queryMongo) throws Exception {
		return undrugDataAnalysisDao.queryUndrugDeptFeeTop5FromDB(searchTime,t,queryMongo);
	}

	@Override
	public List<UndrugDataVo> queryUndrugDocFeeTop5FromDB(String searchTime,
			String t,String queryMongo) throws Exception {
		return undrugDataAnalysisDao.queryUndrugDocFeeTop5FromDB(searchTime,t,queryMongo);
	}

	@Override
	public List<UndrugDataVo> queryUndrugFeeSameDOMFromDB(String searchTime,
			String t,String queryMongo) throws Exception {
		return undrugDataAnalysisDao.queryUndrugFeeSameDOMFromDB(searchTime,t,queryMongo);
	}

	@Override
	public List<UndrugDataVo> queryUndrugFeeMoMFromDB(String searchTime,
			String t,String queryMongo) throws Exception {
		return undrugDataAnalysisDao.queryUndrugFeeMoMFromDB(searchTime,t,queryMongo);
	}
}
