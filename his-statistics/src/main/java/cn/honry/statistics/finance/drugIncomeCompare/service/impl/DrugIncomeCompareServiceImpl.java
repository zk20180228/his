package cn.honry.statistics.finance.drugIncomeCompare.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.vo.UndrugDataVo;
import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.finance.drugIncomeCompare.dao.DrugIncomeCompareDao;
import cn.honry.statistics.finance.drugIncomeCompare.service.DrugIncomeCompareService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("drugIncomeCompareService")
@Transactional
@SuppressWarnings({ "all" })
public class DrugIncomeCompareServiceImpl implements DrugIncomeCompareService {
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Autowired
	@Qualifier(value="drugIncomeCompareDao")
	private DrugIncomeCompareDao drugIncomeCompareDao;
	private List<String> getFenQu(String startTime, String endTime) {
		List<String> tnL;
		try {
			//获取当前表最大时间及最小时间
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, dTime)!=0){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",yNum+1);
					tnL.add(0,"T_INPATIENT_MEDICINELIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_MEDICINELIST_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		return tnL;
	}

	@Override
	public List<Dashboard> queryFee(String startTime, String endTime ,String type) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime); 
		
		return drugIncomeCompareDao.queryFee(startTime,endTime,tnL,type);
	}
	@Override
	public List<Dashboard> queryDeptTop5(String startTime, String endTime,String type) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime); 
		return drugIncomeCompareDao.queryDeptTop5(startTime,endTime,tnL,type);
	}
	@Override
	public List<Dashboard> queryDocTop5(String startTime, String endTime,String type) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime); 
		return drugIncomeCompareDao.queryDocTop5(startTime,endTime,tnL,type);
	}

	@Override
	public List<Dashboard> compareFeeByYear(String startTime, String endTime,
			String type) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime); 
		return drugIncomeCompareDao.compareFeeByYear(startTime,endTime,tnL,type);
	}

	@Override
	public Dashboard queryAllSameFee(String startTime, String endTime,
			String type) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime); 
		return drugIncomeCompareDao.compareFeeByMonthsInYears(startTime,endTime,tnL,type);
	}

	@Override
	public boolean isCollection(String name) throws Exception {
		
		return drugIncomeCompareDao.isCollection(name);
	}

	@Override
	public List<Dashboard> queryDocTop5ByMongo(String searchTime, String type,String queryMongo) throws Exception {
		return drugIncomeCompareDao.queryDocTop5ByMongo(searchTime,type,queryMongo);
	}

	@Override
	public List<Dashboard> queryDeptTop5ByMongo(String searchTime, String type,String queryMongo) throws Exception {
		return drugIncomeCompareDao.queryDeptTop5ByMongo(searchTime,type,queryMongo);
	}

	@Override
	public List<Dashboard> queryFeeByMongo(String searchTime, String type,String queryMongo) throws Exception {
		return drugIncomeCompareDao.queryFeeByMongo(searchTime,type,queryMongo);
	}

	@Override
	public List<Dashboard> querysequentialDrugByMongo(String searchTime, String type,String queryMongo) throws Exception {
		return drugIncomeCompareDao.querysequentialDrugByMongo(searchTime,type, queryMongo);
	}

	@Override
	public List<Dashboard> queryAllSameFeeByMongo(String searchTime,
			String type,String queryMongo) throws Exception {
		return drugIncomeCompareDao.queryAllSameFeeByMongo(searchTime,type,queryMongo);
	}

}
