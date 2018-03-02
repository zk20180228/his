package cn.honry.statistics.bi.bistac.imStacData.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.imStacData.dao.ImStacDataDao;
import cn.honry.statistics.bi.bistac.imStacData.service.ImStacDataService;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.dao.ItemVoDao;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsjdbcDao;
import cn.honry.statistics.sys.reportForms.vo.ReportVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("imStacDataService")
@Transactional
@SuppressWarnings({ "all" })
public class ImStacDataServiceImpl implements ImStacDataService {
	@Autowired
	@Qualifier(value = "imStacDataDao")
	private ImStacDataDao imStacDataDao;
	public void setImStacDataDao(ImStacDataDao imStacDataDao) {
		this.imStacDataDao = imStacDataDao;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	@Autowired
	@Qualifier(value = "itemVoDao")
	private ItemVoDao itemVoDao;
	//获取费用类别fee_code
	@Autowired
	@Qualifier(value = "reportFormsjdbcDao")
	private ReportFormsjdbcDao reportFormsjdbcDao;
	@Override
	public void imEachDay() {
		imStacDataDao.imEachDay();
	}
	@Override
	public void imTableData() {
		imStacDataDao.imTableData();
	}

	
	/**
	 * 
	 * @Description:向mongodb更新每天的数据
	 * void
	 * @exception:
	 * @author: zk
	 * @time:2017年5月11日 下午5:27:28
	 */
	public void imEachDay_T_INPATIENT_INFO(){
		imStacDataDao.imEachDay_T_INPATIENT_INFO();
	};
	
	/**
	 * 
	 * @Description:向mongodb导入历史数据
	 * void
	 * @exception:
	 * @author: zk
	 * @time:2017年5月11日 下午5:27:28
	 */
	public void imTableData_T_INPATIENT_INFO(){
		imStacDataDao.imTableData_T_INPATIENT_INFO();
	}

	@Override
	public void imTableData_GZL() {
		imStacDataDao.imTableData_GZL();
	};
	/**  
	 * 
	 * 将处方明细表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月11日 下午3:48:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月11日 下午3:48:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData(String begin,String end) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dayBefore =null;//上一天
		String dayNew =null;//今天
		boolean timeType=true;//true 天,false 小时
		int days = this.getDays(begin, end);
		for (int i = 0; i <= days; i++) {
			try {
				dayNew=end;
				if (days==0) {
					String newDate = DateUtils.formatDateY_M_D(new Date());
					long stime = df.parse(newDate+" 00:00:00").getTime();
					long etime = df.parse(newDate+" 23:59:59").getTime();
					long beginTime = df.parse(begin).getTime();
					long endTime = df.parse(dayNew).getTime();
					if (stime<=beginTime&&endTime<=etime&&stime<=etime) {
						timeType=false;
						dayBefore = begin;
					}
				}else{
					dayBefore = this.getDayBefore(dayNew);
				}
				SimpleDateFormat format = null;
				List<String> tnL = null;
				//转换查询时间
				Date sTime = DateUtils.parseDateY_M_D_H_M_S(dayBefore);
				Date eTime = DateUtils.parseDateY_M_D_H_M_S(dayNew);
				//获取门诊数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//获得当前时间
				Date dTime = df.parse(df.format(new Date()));
				//获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
				tnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",dayBefore,dayNew);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),dayBefore);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
				imStacDataDao.inTableData(tnL.get(0),dayBefore,dayNew,timeType);
				end=dayBefore;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**  
	 * 
	 * 药占比（YZB）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午2:11:48 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午2:11:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_YZB() {
		imStacDataDao.inEachDay_YZB();
	}
	/**  
	 * 
	 * 将门诊用药天数表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午3:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午3:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_YYTS(String begin,String end) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dayBefore =null;//上一天
		String dayNew =null;//今天
		boolean timeType=true;//true 天,false 小时
		int days = this.getDays(begin, end);
		for (int i = 0; i <= days; i++) {
			try {
				dayNew=end;
				if (days==0) {
					String newDate = DateUtils.formatDateY_M_D(new Date());
					long stime = df.parse(newDate+" 00:00:00").getTime();
					long etime = df.parse(newDate+" 23:59:59").getTime();
					long beginTime = df.parse(begin).getTime();
					long endTime = df.parse(dayNew).getTime();
					if (stime<=beginTime&&endTime<=etime&&stime<=etime) {
						timeType=false;
						dayBefore = begin;
					}
				}else{
					dayBefore = this.getDayBefore(dayNew);
				}
				SimpleDateFormat format = null;
				List<String> tnL = null;
				//转换查询时间
				Date sTime = DateUtils.parseDateY_M_D_H_M_S(dayBefore);
				Date eTime = DateUtils.parseDateY_M_D_H_M_S(dayNew);
				//获取门诊数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//获得当前时间
				Date dTime = df.parse(df.format(new Date()));
				//获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
				tnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",dayBefore,dayNew);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),dayBefore);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
				imStacDataDao.inTableData_YYTS(tnL.get(0),dayBefore,dayNew,timeType);
				end=dayBefore;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	/**  
	 * 
	 * 门诊用药天数（YYTS）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午3:29:00 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午3:29:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_YYTS() {
		imStacDataDao.inEachDay_YYTS();
	}
	/**  
	 * 
	 * 将医生用药金额表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_YSYYJE(String begin,String end) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dayBefore =null;//上一天
		String dayNew =null;//今天
		boolean timeType=true;//true 天,false 小时
		int days = this.getDays(begin, end);
		for (int i = 0; i <= days; i++) {
			try {
				dayNew=end;
				if (days==0) {
					String newDate = DateUtils.formatDateY_M_D(new Date());
					long stime = df.parse(newDate+" 00:00:00").getTime();
					long etime = df.parse(newDate+" 23:59:59").getTime();
					long beginTime = df.parse(begin).getTime();
					long endTime = df.parse(dayNew).getTime();
					if (stime<=beginTime&&endTime<=etime&&stime<=etime) {
						timeType=false;
						dayBefore = begin;
					}
				}else{
					dayBefore = this.getDayBefore(dayNew);
				}
				SimpleDateFormat format = null;
				List<String> tnL = null;
				//转换查询时间
				Date sTime = DateUtils.parseDateY_M_D_H_M_S(dayBefore);
				Date eTime = DateUtils.parseDateY_M_D_H_M_S(dayNew);
				//获取门诊数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//获得当前时间
				Date dTime = df.parse(df.format(new Date()));
				//获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
				tnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",dayBefore,dayNew);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),dayBefore);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
				imStacDataDao.inTableData_YSYYJE(tnL.get(0),dayBefore,dayNew,timeType);
				end=dayBefore;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**  
	 * 
	 * 医生用药金额表（YSYYJE）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_YSYYJE() {
		imStacDataDao.inEachDay_YSYYJE();
	}
	/**  
	 * 
	 * 将科室用药金额表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_KSYYJE(String begin,String end) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dayBefore =null;//上一天
		String dayNew =null;//今天
		boolean timeType=true;//true 天,false 小时
		int days = this.getDays(begin, end);
		for (int i = 0; i <= days; i++) {
			try {
				dayNew=end;
				if (days==0) {
					String newDate = DateUtils.formatDateY_M_D(new Date());
					long stime = df.parse(newDate+" 00:00:00").getTime();
					long etime = df.parse(newDate+" 23:59:59").getTime();
					long beginTime = df.parse(begin).getTime();
					long endTime = df.parse(dayNew).getTime();
					if (stime<=beginTime&&endTime<=etime&&stime<=etime) {
						timeType=false;
						dayBefore = begin;
					}
				}else{
					dayBefore = this.getDayBefore(dayNew);
				}
				SimpleDateFormat format = null;
				List<String> tnL = null;
				//转换查询时间
				Date sTime = DateUtils.parseDateY_M_D_H_M_S(dayBefore);
				Date eTime = DateUtils.parseDateY_M_D_H_M_S(dayNew);
				//获取门诊数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//获得当前时间
				Date dTime = df.parse(df.format(new Date()));
				//获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
				tnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",dayBefore,dayNew);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),dayBefore);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
				imStacDataDao.inTableData_KSYYJE(tnL.get(0),dayBefore,dayNew,timeType);
				end=dayBefore;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**  
	 * 
	 * 科室用药金额表（KSYYJE）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_KSYYJE() {
		imStacDataDao.inEachDay_KSYYJE();
	}

	@Override
	public void imEachDay_GZL() {
		imStacDataDao.imEachDay_GZL();
	}
	/**
	 * @Description:门诊药房退费统计：把now表中的数据更新到mongodb中
	 * 结算信息表：T_INPATIENT_CANCELITEM_NOW
	 * 病区退费申请表：T_FINANCE_INVOICEINFO_NOW,T_FINANCE_INVOICEINFO 
	 * @author: zhangkui
	 * @time:2017年5月13日 下午8:04:54
	 */
	public void imEachDay_T_INPATIENT_CANCELITEM(){
		
		imStacDataDao.imEachDay_T_INPATIENT_CANCELITEM();
	}
	
	
	/**
	 * @Description:门诊药房退费统计：把数据导入mongodb中
	 * 结算信息表：T_FINANCE_INVOICEINFO_NOW，T_FINANCE_INVOICEINFO
	 * 病区退费申请表：T_FINANCE_INVOICEINFO_NOW，T_FINANCE_INVOICEINFO 
	 * @author: zhangkui
	 * @time:2017年5月12日 下午8:04:54
	 */
	public void imTableData_T_INPATIENT_CANCELITEM(){
		imStacDataDao.imTableData_T_INPATIENT_CANCELITEM();
	}
	@Override
	public void imTableData_GHYSGZL() {
		imStacDataDao.imTableData_GHYSGZL();
	}

	
	/**  
	 * 
	 * 将门诊月药品金额，用药数量，人次表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午3:37:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午3:37:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_YPJE() {
		imStacDataDao.inTableData_YPJE();
	}
	@Override
	public void inTableData_YPJE2(String begin,String end) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dayBefore =null;//上一天
		String dayNew =null;//今天
		boolean timeType=true;//true 天,false 小时
		int days = this.getDays(begin, end);
		for (int i = 0; i <= days; i++) {
			try {
				dayNew=end;
				if (days==0) {
					String newDate = DateUtils.formatDateY_M_D(new Date());
					long stime = df.parse(newDate+" 00:00:00").getTime();
					long etime = df.parse(newDate+" 23:59:59").getTime();
					long beginTime = df.parse(begin).getTime();
					long endTime = df.parse(dayNew).getTime();
					if (stime<=beginTime&&endTime<=etime&&stime<=etime) {
						timeType=false;
						dayBefore = begin;
					}
				}else{
					dayBefore = this.getDayBefore(dayNew);
				}
				SimpleDateFormat format = null;
				List<String> tnL = null;
				//转换查询时间
				Date sTime = DateUtils.parseDateY_M_D_H_M_S(dayBefore);
				Date eTime = DateUtils.parseDateY_M_D_H_M_S(dayNew);
				//获取门诊数据保留时间
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//获得当前时间
				Date dTime = df.parse(df.format(new Date()));
				//获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
				tnL = new ArrayList<String>();
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",dayBefore,dayNew);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),dayBefore);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						tnL.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
				imStacDataDao.inTableData_YPJE2(tnL.get(0),dayBefore,dayNew,timeType);
				end=dayBefore;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	/**  
	 * 
	 * 门诊月药品金额，用药数量，人次表(YPJE)向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午5:45:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午5:45:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */

	@Override
	public void inEachDay_YPJE2() {
		imStacDataDao.inEachDay_YPJE2();
	}
	/**  
	 * 
	 * 住院收入统计表（ZYSRTJ）导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月17日 下午7:07:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月17日 下午7:07:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_ZYSRTJ() {
		imStacDataDao.inTableData_ZYSRTJ();
	}


	/**
	 *@Description:门诊各项收入统计：把数据导入mongodb中
	 *处方明细表:T_OUTPATIENT_FEEDETAIL  T_OUTPATIENT_FEEDETAIL_NOW 
	 * @author: zhangkui
	 * @time:2017年5月18日 
	 */
	public void imTableData_T_OUTPATIENT_FEEDETAIL() {
		List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code
		imStacDataDao.imTableData_T_OUTPATIENT_FEEDETAIL(encode);
		
	}
	
	/**
	 *@Description:把门诊各项收入统计：把每天now表数据更新到mongodb中
	 *处方明细表: T_OUTPATIENT_FEEDETAIL_NOW 
	 * @author: zhangkui
	 * @time:2017年5月20日 
	 */
	public void imEachDay_T_OUTPATIENT_FEEDETAIL(){
		List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code
		imStacDataDao.imEachDay_T_OUTPATIENT_FEEDETAIL(encode);
	}
	
	
	/**
	 * 向mongodb中导入历史数据和在线库数据:治疗效果数据分析
	 * @param startTime 开始时间,格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间,格式：yyyy-MM-dd HH:mm:ss
	 * @throws ParseException 
	 *  @Author zhangkui
	 * @time 2017年5月22日
	 */
	public void imTableData_T_INPATIENT_INFO(String startTime, String endTime)throws ParseException{
		List tnL =null;
		//查分表
		try{
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
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
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询的开始时间小于在线库的最小时间
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
		}catch(Exception e){
			e.printStackTrace();
		}
		
		imStacDataDao.imTableData_T_INPATIENT_INFO(startTime, endTime,tnL);
	}
	
	/**
	 * @Description:门诊药房退费统计：把数据导入mongodb中
	 *病区退费申请表：T_INPATIENT_CANCELITEM_NOW,lT_INPATIENT_CANCELITEM
	 *结算信息表:T_FINANCE_INVOICEINFO_NOW,T_FINANCE_INVOICEINFO 
	 *@param startTime:开始时间 
	 *@param endTime :结束时间
	 * @author: zhangkui
	 * @throws Exception 
	 * @time:2017年5月22日 下午8:04:54
	 */
	public void imTableData_T_INPATIENT_CANCELITEM(String startTime,String endTime) throws Exception{
		List<String> invoiceInfoPartName = new ArrayList<String>();
		List<String> cancelPartName = new ArrayList<String>();
		Date sTime = DateUtils.parseDateY_M_D(startTime);
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		//获取门诊数据保留时间
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		//获得当前时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime=null;
		try {
			dTime = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获得在线库数据应保留最小时间
		Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
		
		if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				//获取需要查询的全部分区
				invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",startTime,endTime);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
				//获取相差年分的分区集合，默认加1
				invoiceInfoPartName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_FINANCE_INVOICEINFO",yNum+1);
				invoiceInfoPartName.add(0,"T_FINANCE_INVOICEINFO_NOW");
			}
		}else{
			invoiceInfoPartName.add("T_FINANCE_INVOICEINFO_NOW");
		}
		if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询历史表
				cancelPartName.add("T_INPATIENT_CANCELITEM");
			}else{//2.查询历史表和主表
				cancelPartName.add(0,"T_INPATIENT_CANCELITEM_NOW");
				cancelPartName.add(1,"T_INPATIENT_CANCELITEM");
			}
		}else{
			cancelPartName.add("T_INPATIENT_CANCELITEM_NOW");
		}
		
		imStacDataDao.imTableData_T_INPATIENT_CANCELITEM(startTime, endTime, invoiceInfoPartName, cancelPartName);
		
	}	

	
	/**
	 *@Description:门诊各项收入统计：把数据导入mongodb中
	 *处方明细表:T_OUTPATIENT_FEEDETAIL  T_OUTPATIENT_FEEDETAIL_NOW 
	 *@param startTime：开始时间
	 *@param endTime：结束时间
	 *@param table:T_OUTPATIENT_FEEDETAIL,处方明细表
	 *@param gcode:费用类别
	 * @author: zhangkui
	 * @throws ParseException 
	 * @time:2017年5月22日 
	 */
	public void imTableData_T_OUTPATIENT_FEEDETAIL(String startTime ,String endTime) throws ParseException{
			List<String> feedetialPartitionName = new ArrayList<String>();
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
//			StatVo feeDetial = reportFormsDAO.findMaxMinByTabNameAndField("T_OUTPATIENT_FEEDETAIL_NOW", "REG_DATE");
			List<ReportVo> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
					feedetialPartitionName.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
				}
			}else{
				feedetialPartitionName.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
			
			imStacDataDao.imTableData_T_OUTPATIENT_FEEDETAIL(startTime, endTime, feedetialPartitionName, encode);
	}
	
	/**
	 *@Description:科室统计--门诊住院工作同期对比表   病床信息表:T_BUSINESS_HOSPITALBED---->没有分区表，没有在线表  
	 * 挂号主表:T_REGISTER_MAIN----->有分区表 ,有在线表 ,走分区
	 * 住院表：T_INPATIENT_INFO----->没有分区表，有在线表 ，但是还要走分区
	 *@param:Btime 开始时间:注意按月更新,如果时间为2017-01-01 00:00:00-2017-06-07 00:00:00 更新数据为为1-6月的所有数据
	 *@param:Etime 结束时间:注意按月更新,如果时间为2017-01-01 00:00:00-2017-06-07 00:00:00 更新数据为为1-6月的所有数据
	 *@param:areaCode 院区编号，默认全部
	 *@param:ghList 挂号分表集合
	 *@param:zyList 住院分表集合
	 *@author zhangkui
	 * @throws Exception 
	 */
	//科室统计--门诊住院工作同期对比表
	public void  imTableData_KSTJ_MZZYGZTQDBB(String Btime, String Etime,String areaCode)throws Exception {
		
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
			
			 imStacDataDao.imTableData_KSTJ_MZZYGZTQDBB(Btime,Etime,areaCode,ghList,zyList);
		
	}

	
	
	
	
	
	
	
	/**
	 * 按天导入每天的工作量
	 */
	@Override
	public void imEachDay_GHYSGZL() {
		imStacDataDao.imEachDay_GHYSGZL();
	}

	@Override
	public void imTableData_GZLMonth() {
		imStacDataDao.imTableData_GZLMonth();
	}

	@Override
	public void imEachDay_GZLMonth() {
		imStacDataDao.imEachDay_GZLMonth();
	}

	@Override
	public void imTableData_GZLYear() {
		imStacDataDao.imTableData_GZLYear();
	}

	@Override
	public void imEachDay_GZLYear() {
		imStacDataDao.imEachDay_GZLYear();
	}

	@Override
	public void imTableData_MZCFGZL() {
		imStacDataDao.imTableData_MZCFGZL();
	}

	@Override
	public void imTableData_MZCFGZLMonth() {
		imStacDataDao.imTableData_MZCFGZLMonth();
	}
	
	@Override
	public void imTableData_MZCFGZLYear() {
		imStacDataDao.imTableData_MZCFGZLYear();
	}
	/**  
	 * 
	 * 上一天
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:49:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:49:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getDayBefore(String dateTime){
		Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yy-MM-dd").parse(dateTime);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());  
        return dayBefore+" 23:59:59";  
	}
	/**  
	 * 
	 * 两个时间段相差的天数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:49:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:49:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private int getDays(String begin,String end){
	    if (null == begin || null == end) {
	           return -1;
	       }
       long intervalMilli = DateUtils.parseDateY_M_D_H_M_S(end).getTime() - DateUtils.parseDateY_M_D_H_M_S(begin).getTime();
       return (int) (intervalMilli / (86400000));//24 * 60 * 60 * 1000=86400000
	}

	@Override
	public void imMZJZRCTJ(String startTime, String endTime) {
		imStacDataDao.imMZJZRCTJ(startTime ,endTime);
	}

	//门诊工作量-----按天-----有参
	@Override
	public void imTableData_GHYSGZLDAY(String startTime, String endTime) {
		try{
			imStacDataDao.imTableData_GHYSGZLDAY(startTime ,endTime);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void imTableData_GZLMonth1(String startTime, String endTime) {
		try{
			//1.转换查询时间
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取门诊数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			//4.获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
//			//获取当前表最大时间及最小时间
			List<String> tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
					tnL.add(0,"T_REGISTER_MAIN_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_REGISTER_MAIN_NOW");
			}
			imStacDataDao.imTableData_GZLMonth1(startTime ,endTime, tnL);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void inindata(List<StatisticsVo> list,String date){
		imStacDataDao.inindata(list, date);
	}
	/**  
	 * 
	 * 科室对比表（KSDBB）导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月5日 下午4:42:03 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月5日 下午4:42:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_KSDBB(String begin, String end) {
//		List<FicDeptVO> depts = itemVoDao.queryFicDeptVO();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dayBefore =null;//上一天
		String dayNew =null;//今天
		boolean timeType=true;//true 天,false 小时
		int days = this.getDays(begin, end);
		for (int i = 0; i <= days; i++) {
			try {
				dayNew=end;
				if (days==0) {
					String newDate = DateUtils.formatDateY_M_D(new Date());
					long stime = df.parse(newDate+" 00:00:00").getTime();
					long etime = df.parse(newDate+" 23:59:59").getTime();
					long beginTime = df.parse(begin).getTime();
					long endTime = df.parse(dayNew).getTime();
					if (stime<=beginTime&&endTime<=etime&&stime<=etime) {
						timeType=false;
						dayBefore = begin;
					}
				}else{
					dayBefore = this.getDayBefore(dayNew);
				}
				SimpleDateFormat format = null;
				List<String> tnL = null;
				List<String> tnL2 = null;
				List<String> tnL3 = null;
				List<String> tnL4 = null;
				//转换查询时间
				Date sTime = DateUtils.parseDateY_M_D_H_M_S(dayBefore);
				Date eTime = DateUtils.parseDateY_M_D_H_M_S(dayNew);
				//获取住院数据保留时间
				String dateNum1 = parameterInnerDAO.getParameterByCode("saveTime");
				if(dateNum1.equals("1")){
					dateNum1="30";
				}
				//获取门诊数据保留时间
				String dateNum2 = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				//获得当前时间
				Date dTime = df.parse(df.format(new Date()));
				//获得住院在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum1)+1);
				//获得门诊在线库数据应保留最小时间
				Date cTime2 = DateUtils.addDay(dTime, -Integer.parseInt(dateNum2)+1);
				tnL = new ArrayList<String>();
				tnL2 = new ArrayList<String>();
				tnL3 = new ArrayList<String>();
				tnL4 = new ArrayList<String>();
				//判断查询类型(住院主表T_INPATIENT_INFO)
				if(DateUtils.compareDate(sTime, cTime)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
					if(DateUtils.compareDate(eTime, cTime)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
					/*	//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",begin,end);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",yNum+1);
						tnL.add(0,"T_INPATIENT_INFO_NOW");*/
						tnL.add("T_INPATIENT_INFO");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_INPATIENT_INFO_NOW");
				}
				//判断查询类型(门诊挂号：挂号主表T_REGISTER_MAIN)
				if(DateUtils.compareDate(sTime, cTime2)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
					if(DateUtils.compareDate(eTime, cTime2)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
						//获取需要查询的全部分区
						tnL2 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",begin,end);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime2),begin);
						//获取相差年分的分区集合，默认加1
						tnL2 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",yNum+1);
						tnL2.add(0,"T_REGISTER_MAIN_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL2.add("T_REGISTER_MAIN_NOW");
				}
				//判断查询类型(住院费用汇总表T_INPATIENT_FEEINFO)
				if(DateUtils.compareDate(sTime, cTime)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
					if(DateUtils.compareDate(eTime, cTime)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
						//获取需要查询的全部分区
						tnL3 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",begin,end);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
						//获取相差年分的分区集合，默认加1
						tnL3 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
						tnL3.add(0,"T_INPATIENT_FEEINFO_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL3.add("T_INPATIENT_FEEINFO_NOW");
				}
				//判断查询类型(门诊处方明细表T_OUTPATIENT_FEEDETAIL)
				if(DateUtils.compareDate(sTime, cTime2)==-1){//当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
					if(DateUtils.compareDate(eTime, cTime2)==-1){//当结束时间小于挂号主表中的最小时间时，只查询分区表
  						//获取需要查询的全部分区
						tnL4 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",begin,end);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime2),begin);
						//获取相差年分的分区集合，默认加1
						tnL4 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",yNum+1);
						tnL4.add(0,"T_OUTPATIENT_FEEDETAIL_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL4.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
				List<String> tnLs = new ArrayList<String>();
				tnLs.add(tnL.get(0));
				tnLs.add(tnL2.get(0));
				tnLs.add(tnL3.get(0));
				tnLs.add(tnL4.get(0));
				imStacDataDao.inTableData_KSDBB(tnLs,dayBefore,dayNew,timeType);
				end=dayBefore;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	 //获取某月的最后一天
	public String getLastDay(String date){
		date= date.substring(0, 7);
		System.out.println(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date time =null;
		try {
			 time = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 Calendar  calendar =  Calendar.getInstance(); 
		 calendar.setTime(time);
		 final  int  lastDay  =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		 Date   lastDate   =   calendar.getTime();  
	     lastDate.setDate(lastDay);  
	     SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat1.format(lastDate).substring(8, 10);
	}
	
	
}
