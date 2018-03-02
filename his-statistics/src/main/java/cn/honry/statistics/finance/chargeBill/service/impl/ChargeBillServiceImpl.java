package cn.honry.statistics.finance.chargeBill.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.Patient;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.chargeBill.dao.ChargeBillDao;
import cn.honry.statistics.finance.chargeBill.service.ChargeBillService;
import cn.honry.statistics.finance.chargeBill.vo.ChargeBillVo;
import cn.honry.statistics.sys.reportForms.vo.DoctorWorkloadStatistics;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
/**   
*  
* @className：ChargeBill
* @description：住院患者费用清单service实现类
* @author：tcj
* @createDate：2016-04-09  
* @modifyRmk：  
* @version 1.0
 */
@Service("chargeBillService")
@Transactional
@SuppressWarnings({"all"})
public class ChargeBillServiceImpl implements ChargeBillService {
	/**
	 * 注入chargeDao
	 */
	@Autowired
	@Qualifier(value = "chargeBillDao")
	private ChargeBillDao chargeBillDao;
	public void setChargeBillDao(ChargeBillDao chargeBillDao) {
		this.chargeBillDao = chargeBillDao;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Override
	public ChargeBillVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(ChargeBillVo arg0) {
		
	}

	@Override
	public List<InpatientInfoNow> queryInpatientInfo(String medId) throws Exception {
		return chargeBillDao.queryInpatientInfo(medId);
	}

	@Override
	public List<ChargeBillVo> queryDatagridinfo(String inpatientNo,String startTime,String endTime,String sendfalg) throws Exception {
		List<String> tnlItem=findItem(startTime,endTime);
		List<String> tnlMed=findMed(startTime,endTime);
		return chargeBillDao.queryDatagridinfo(tnlItem,tnlMed,inpatientNo,startTime,endTime,sendfalg);
	}

	@Override
	public List<SysDepartment> queryZYDept() {
		return chargeBillDao.queryZYDept();
	}

	@Override
	public List<Patient> queryPatientbill(String medicalrecordId) throws Exception {
		return chargeBillDao.queryPatientbill(medicalrecordId);
	}
	@Override
	public List<User> queryEmpbill() {
		return chargeBillDao.queryEmpbill();
	}
	
	public List<String> findItem(String startTime,String endTime) {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(endTime,startTime);
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
		return tnL;
	}
	
	
	public List<String> findMed(String startTime,String endTime) {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(endTime,startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",yNum+1);
					tnL.add(0,"T_INPATIENT_MEDICINELIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_MEDICINELIST_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return tnL;
	}
	
}
