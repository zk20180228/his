package cn.honry.statistics.deptstat.outandinpatient.service.impl;

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
import cn.honry.statistics.deptstat.outandinpatient.dao.OutAndInPatientDao;
import cn.honry.statistics.deptstat.outandinpatient.service.OutAndInPatientService;
import cn.honry.statistics.deptstat.outandinpatient.vo.GetOrOutPatient;
import cn.honry.statistics.deptstat.outandinpatient.vo.InPatientVo;
import cn.honry.statistics.deptstat.outandinpatient.vo.OutPatientVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("outAndInPatientService")
@Transactional
public class OutAndInPatientServiceImpl implements OutAndInPatientService {
	
	@Autowired
	@Qualifier(value = "outAndInPatientDao")
	private OutAndInPatientDao outAndInPatientDao;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	private List<String> getFenQu(String startTime, String endTime) {
		List<String> tnL;
		try {
			//获取当前表最大时间及最小时间
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取门诊数据保留时间
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
			if(DateUtils.compareDate(sTime, dTime)!=0){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
					tnL.add(0,"T_INPATIENT_INFO_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_INFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		return tnL;
	}

	@Override
	public List<InPatientVo> queryinPatientMsg(String startTime, String endTime,
			String dept,String menuAlias,String page,String rows) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime);
		List<InPatientVo> list = outAndInPatientDao.queryinPatientMsg(tnL,startTime,endTime,dept,menuAlias,page,rows);
		return list;
	}

	@Override
	public List<OutPatientVo> queryOutPatientMsg(String startTime,
			String endTime, String dept,String menuAlias,String page,String rows) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime);
		List<OutPatientVo> list = outAndInPatientDao.queryOutPatientMsg(tnL,startTime,endTime,dept,menuAlias,page,rows);
		return list;
	}

	@Override
	public List<GetOrOutPatient> queryGetInPatientMsg(String startTime,
			String endTime, String dept,String menuAlias,String page,String rows) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime);
		List<GetOrOutPatient> list = outAndInPatientDao.queryGetInPatientMsg(tnL,startTime,endTime,dept,menuAlias,page,rows);
		return list;
	}

	@Override
	public List<GetOrOutPatient> queryGetOutPatientMsg(String startTime,
			String endTime, String dept,String menuAlias,String page,String rows) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime);
		List<GetOrOutPatient> list = outAndInPatientDao.queryGetOutPatientMsg(tnL,startTime,endTime,dept,menuAlias,page,rows);
		return list;
	}

	@Override
	public int queryinPatientTotal(String startTime,
			String endTime, String dept, String menuAlias) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime);
		return outAndInPatientDao.queryinPatientTotal(tnL,startTime,endTime,dept,menuAlias);
	}

	@Override
	public int queryOutPatientMsg(String startTime,
			String endTime, String dept, String menuAlias) throws Exception {
		List<String> tnL = getFenQu(startTime, endTime);
		return outAndInPatientDao.queryOutPatientMsg(tnL, startTime, endTime, dept, menuAlias);
	}

	@Override
	public int queryGetInPatientMsg( String startTime,
			String endTime, String dept, String menuAlias) {
		List<String> tnL = getFenQu(startTime, endTime);
		return outAndInPatientDao.queryGetInPatientMsg(tnL, startTime, endTime, dept, menuAlias);
	}

	@Override
	public int queryGetOutPatientMsg( String startTime,
			String endTime, String dept, String menuAlias) {
		List<String> tnL = getFenQu(startTime, endTime);
		return outAndInPatientDao.queryGetOutPatientMsg(tnL, startTime, endTime, dept, menuAlias);
	}

}
