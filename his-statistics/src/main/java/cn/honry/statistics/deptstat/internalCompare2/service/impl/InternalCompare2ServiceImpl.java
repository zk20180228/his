package cn.honry.statistics.deptstat.internalCompare2.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.internalCompare2.dao.InternalCompare2Dao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.internalCompare2.dao.InternalCompare2DAO;
import cn.honry.statistics.deptstat.internalCompare2.service.InternalCompare2Service;
import cn.honry.statistics.deptstat.internalCompare2.vo.InternalCompare2Vo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("internalCompare2Service")
@Transactional
@SuppressWarnings({ "all" })
public class InternalCompare2ServiceImpl implements InternalCompare2Service {
	@Autowired
	@Qualifier(value = "internalCompare2DAO")
	private InternalCompare2DAO internalCompare2DAO;
	static SimpleDateFormat sdFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar ca = Calendar.getInstance();
	@Autowired
	@Qualifier(value="internalCompare2Dao")
	private InternalCompare2Dao internalCompare2Dao;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier("deptInInterService")
	private DeptInInterService deptInInterService;
	
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}

	@Override
	public List<InternalCompare2Vo> queryinternalCompare2list(String timed, String Stime, String deptCode1List) throws Exception {
		boolean sign=new MongoBasicDao().isCollection("YYNKYXBHNEYXBDBBT_MONTH");
		List<InternalCompare2Vo> list=new ArrayList<InternalCompare2Vo>();
		if(sign){
			list=internalCompare2DAO.queryForDBSque(timed, Stime,deptCode1List);
			if(list.size()>0){
				return list;
			}
			return new ArrayList<InternalCompare2Vo>();
		}else{
			Calendar cal = Calendar.getInstance();
			// 下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推
			cal.set(Calendar.MONTH, Integer.parseInt(timed.split("-")[1]));
			// 调到上个月
			cal.add(Calendar.MONTH, -1);
			// 得到一个月最最后一天日期(31/30/29/28)
			int MaxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			Calendar cal1 = Calendar.getInstance();
			// 下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推
			cal1.set(Calendar.MONTH, Integer.parseInt(Stime.split("-")[1]));
			// 调到上个月
			cal1.add(Calendar.MONTH, -1);
			// 得到一个月最最后一天日期(31/30/29/28)
			int MaxDay1 = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
			List<String> tnLMZ1 = getTnLMZ(timed.split("-")[0] + "-" + "01-01", timed + "-" + MaxDay);
			List<String> tnLMZ2 = getTnLMZ(Stime.split("-")[0] + "-" + "01-01", Stime + "-" + MaxDay1);
			List<String> tnLZY1 = getTnLZY(timed.split("-")[0] + "-" + "01-01", timed + "-" + MaxDay);
			List<String> tnLZY2 = getTnLZY(Stime.split("-")[0] + "-" + "01-01", Stime +"-"+MaxDay1);
		    return internalCompare2DAO.queryinternalCompare2list(timed, Stime,deptCode1List, tnLMZ1, tnLMZ2, tnLZY1, tnLZY2);
		}
	}

	private List<String> getTnLMZ(Map<String, String> map) {
		String startTime = "";
		String endTime = "";
		List<String> tnL = null;
		// 循环时间段
		for (String dateArr : map.keySet()) {
			startTime = dateArr;// 查询的时间段
			endTime = map.get(dateArr);// 查询的时间段
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			try {
				//获取门诊保存时间
				String dateNum =  parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
				// 3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				// 4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
				tnL = new ArrayList<String>();
				// 判断查询类型
				if (DateUtils.compareDate(sTime, cTime) == -1) {
					if (DateUtils.compareDate(eTime, cTime) == -1) {// 1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						// 获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,
								"T_OUTPATIENT_FEEDETAIL", startTime, endTime);
					} else {// 2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
							// 获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
						// 获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,
								"T_OUTPATIENT_FEEDETAIL", yNum + 1);
						tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
					}
				} else {// 3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tnL = new ArrayList<String>();
			}
		}
		return tnL;
	}

	private List<String> getTnLZY(Map<String, String> map) {
		String startTime = "";
		String endTime = "";
		List<String> tnL = null;
		// 循环时间段
		for (String dateArr : map.keySet()) {
			startTime = dateArr;// 查询的时间段
			endTime = map.get(dateArr);// 查询的时间段
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			try {
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if (dateNum.equals("1")) {
					dateNum = "30";
				}
				// 3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				// 4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
				tnL = new ArrayList<String>();
				// 判断查询类型
				if (DateUtils.compareDate(sTime, cTime) == -1) {
					if (DateUtils.compareDate(eTime, cTime) == -1) {// 1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						// 获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,
								"T_INPATIENT_FEEINFO", startTime, endTime);
					} else {// 2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						// 获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), startTime);
						// 获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,
								"T_INPATIENT_FEEINFO", yNum + 1);
						tnL.add(0, "T_INPATIENT_FEEINFO_NOW");
					}
				} else {// 3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_INPATIENT_FEEINFO_NOW");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tnL = new ArrayList<String>();
			}
		}
		return tnL;
	}

	public  static Map<String,String> queryBetweenTime(String begin,String end,Integer hours,Map<String,String> map){
		
		 try {
		if(StringUtils.isNotBlank(begin)){
			Date date= sdFull.parse(begin);//开始时间
			String[] dateone=begin.split(" ");
			String[] dateArr=dateone[0].split("-");
			String[] dateArr1=dateone[1].split(":");
			Date date1= sdFull.parse(end);//结束时间
			if(date.getTime()>=date1.getTime()){//如果开始时间大于结束时间  结束
				return map;
			}else{
				ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
				if(hours==null){//小时为空 按天查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.DATE, 1);//开始时间加1天
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}else{//按小时查
					String key=sdFull.format(ca.getTime());
					ca.add(Calendar.HOUR, hours);
					begin=sdFull.format(ca.getTime());
					map.put(key,begin);
				}
				return queryBetweenTime(begin,end,hours,map);
			}
		}
		} catch (ParseException e) {
		}//开始时间
		return map;
	}
	public static boolean betweenDateSign(String begin,String end){
		boolean sign=false;
		try {
			if(StringUtils.isNotBlank(begin)){
				
			
		Date date= sdFull.parse(begin);
		String[] dateone=begin.split(" ");
		String[] dateArr=dateone[0].split("-");
		String[] dateArr1=dateone[1].split(":");
		Date date1= sdFull.parse(end);//结束时间
		ca.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr1[0]),Integer.parseInt(dateArr1[1]),Integer.parseInt(dateArr1[2]));
		ca.add(Calendar.HOUR, 2);
		Calendar ca1=Calendar.getInstance();
		ca1.set(Calendar.HOUR, 0);
		ca1.set(Calendar.MINUTE, 0);
		ca1.set(Calendar.SECOND, 0);
		if(ca.getTime().getTime()>=date1.getTime()&&ca.getTime().getTime()>ca1.getTime().getTime()){
			sign=true;
		}
			}
		} catch (ParseException e) {
		}
		
		return sign;
		
	}

	private List<String> getTnLMZ(String begin, String end) {
		List<String> tnL = null;
		try {
			// 获取当前表最大时间及最小时间
			Date sTime = DateUtils.parseDateY_M_D(begin);
			Date eTime = DateUtils.parseDateY_M_D(end);
			// 3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			// 4.获得在线库数据应保留最小时间
			tnL = new ArrayList<String>();
			// 判断查询类型
			if (DateUtils.compareDate(sTime, dTime) != 0) {// 在线表只保存当天的数据
				if (DateUtils.compareDate(eTime, dTime) == -1) {// 1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）--->结束时间小于当前时间
					// 获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,
							"T_OUTPATIENT_FEEDETAIL", begin, end);
				} else {// 2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						// 获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(dTime), begin);// ------>结束时间大于等于当前时间
					// 获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,
							"T_OUTPATIENT_FEEDETAIL", yNum + 1);
					tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
				}
			} else {// 3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		return tnL;
	}

	private List<String> getTnLZY(String begin, String end) {
		List<String> tnL = null;
		try {
			// 获取当前表最大时间及最小时间
			Date sTime = DateUtils.parseDateY_M_D(begin);
			Date eTime = DateUtils.parseDateY_M_D(end);
			// 2.获取门诊数据保留时间
			// 挂号排班的只保存当天在线表只保存当天的数据

			// 3.获得当前时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			// 4.获得在线库数据应保留最小时间
			// Date cTime =
			// DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
			tnL = new ArrayList<String>();
			// 判断查询类型
			if (DateUtils.compareDate(sTime, dTime) != 0) {// 在线表只保存当天的数据
				if (DateUtils.compareDate(eTime, dTime) == -1) {// 1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）--->结束时间小于当前时间
					// 获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,
							"T_INPATIENT_FEEINFO", begin, end);
				} else {// 2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					// 获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(dTime), begin);// ------>结束时间大于等于当前时间
					// 获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,
							"T_INPATIENT_FEEINFO", yNum + 1);
					tnL.add(0, "T_INPATIENT_FEEINFO_NOW");
				}
			} else {// 3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		return tnL;
	}

	@Override
	public void initCompare2list(String timed, String Stime) throws Exception {
			List<String> tnL = null;
			Date sTime = DateUtils.parseDateY_M_D(timed);
			Date eTime = DateUtils.parseDateY_M_D(Stime);

			try {
				String dateNum =parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME); 
				// 3.获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				// 4.获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
				tnL = new ArrayList<String>();
				// 判断查询类型
				if (DateUtils.compareDate(sTime, cTime) == -1) {
					if (DateUtils.compareDate(eTime, cTime) == -1) {// 1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						// 获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,
								"T_OUTPATIENT_FEEDETAIL", timed, Stime);
					} else {// 2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
							// 获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), timed);
						// 获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,
								"T_OUTPATIENT_FEEDETAIL", yNum + 1);
						tnL.add(0, "T_OUTPATIENT_FEEDETAIL_NOW");
					}
				} else {// 3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_OUTPATIENT_FEEDETAIL_NOW");
				}

			} catch (Exception e) {
				e.printStackTrace();
				tnL = new ArrayList<String>();
			}

			List<String> maintnl = null;
			try {
				String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
				if (dateNum.equals("1")) {
					dateNum = "30";
				}
				// 获得当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dTime = df.parse(df.format(new Date()));
				// 获得在线库数据应保留最小时间
				Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum) + 1);
				maintnl = new ArrayList<String>();
				if (DateUtils.compareDate(sTime, cTime) == -1) {// 当开始时间小于挂号主表的最小时间时、即要从分区表中取数据
					if (DateUtils.compareDate(eTime, cTime) == -1) {// 当结束时间小于挂号主表中的最小时间时，只查询分区表
						maintnl = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,
								"T_INPATIENT_FEEINFO", timed, Stime);
					} else {// 查询主表和分区表
							// 获取时间差（年）
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime), timed);
						// 获取相差年份的分区集合
						maintnl = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,
								"T_INPATIENT_FEEINFO", yNum + 1);
						maintnl.add(0, "T_INPATIENT_FEEINFO_NOW");
					}
				} else {// 3.只查询主表（查询的开始时间大或等于表中的最小时间）
					maintnl.add("T_INPATIENT_FEEINFO_NOW");

				}
			} catch (Exception e) {
				maintnl = new ArrayList<String>();
			}
			internalCompare2DAO.initMZZYTotalByDayOrHours(tnL, maintnl, timed, Stime, null);

//		}
	}

	@Override
	public void exportExcel(ServletOutputStream stream, List<InternalCompare2Vo> journalVos,String date) {
		int yeared = Integer.parseInt(date.split("-")[0])-1;
		int year = Integer.parseInt(date.split("-")[0]);
		//创建workbook   
	       HSSFWorkbook workbook = new HSSFWorkbook();   
	       //创建sheet页  
	       HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+"科室对比表");   
	       //创建单元格  
	       HSSFRow row = sheet.createRow(0);   
	       HSSFCell c0 = row.createCell(0);   
	       c0.setCellValue(new HSSFRichTextString("部门名称"));   
	       HSSFCell c1 = row.createCell(1);   
	       c1.setCellValue(new HSSFRichTextString("病区负责人"));   
	       HSSFCell c2 = row.createCell(2);   
	       c2.setCellValue(new HSSFRichTextString("总收入(万元)"));   
	       HSSFCell c3 = row.createCell(6);   
	       c3.setCellValue(new HSSFRichTextString("门诊总收入(万元)"));   
	       HSSFCell c4 = row.createCell(10);   
	       c4.setCellValue(new HSSFRichTextString("住院总收入(万元)"));   
	       HSSFRow row1 = sheet.createRow(1);   
	       HSSFCell c6 = row1.createCell(2);   
	       c6.setCellValue(new HSSFRichTextString(yeared+"年"));   
	       HSSFCell c7 = row1.createCell(3);   
	       c7.setCellValue(new HSSFRichTextString(year+"年"));   
	       HSSFCell c8 = row1.createCell(4);   
	       c8.setCellValue(new HSSFRichTextString("增长数"));  
	       HSSFCell c9 = row1.createCell(5);   
	       c9.setCellValue(new HSSFRichTextString("增长率"));   
	       HSSFCell c10 = row1.createCell(6);   
	       c10.setCellValue(new HSSFRichTextString(yeared+"年"));   
	       HSSFCell c11 = row1.createCell(7);   
	       c11.setCellValue(new HSSFRichTextString(year+"年"));  
	       HSSFCell c12 = row1.createCell(8);   
	       c12.setCellValue(new HSSFRichTextString("增长数"));   
	       HSSFCell c13 = row1.createCell(9);   
	       c13.setCellValue(new HSSFRichTextString("增长率"));   
	       HSSFCell c14 = row1.createCell(10);   
	       c14.setCellValue(new HSSFRichTextString(yeared+"年"));  
	       HSSFCell c15 = row1.createCell(11);   
	       c15.setCellValue(new HSSFRichTextString(year+"年"));  
	       HSSFCell c16 = row1.createCell(12);   
	       c16.setCellValue(new HSSFRichTextString("增长数"));  
	       HSSFCell c17 = row1.createCell(13);   
	       c17.setCellValue(new HSSFRichTextString("增长率"));  
	       Region region1 = new Region(0, (short)0, 1, (short)0);   
	       Region region2 = new Region(0, (short)1, 1, (short)1);   
	       Region region3 = new Region(0, (short)2, 0, (short)5);   
	       Region region4 = new Region(0, (short)6, 0, (short)9);   
	       Region region5 = new Region(0, (short)10, 0, (short)13);   
	       sheet.addMergedRegion(region1);   
	       sheet.addMergedRegion(region2);   
	       sheet.addMergedRegion(region3);   
	       sheet.addMergedRegion(region4);   
	       sheet.addMergedRegion(region5); 
	       int j=1;
	       Map<String,String> map= deptInInterService.querydeptCodeAndNameMap();
	       String deptCode;
	       for(InternalCompare2Vo vo:journalVos){
	       	HSSFRow hrow = sheet.createRow(j+1);
	       	deptCode=vo.getDeptCode();
	       	if(vo.getYearTol()!=null){
	       		if(deptCode.endsWith("院区")||"总合计".equals(deptCode)){
	       			hrow.createCell(0).setCellValue(vo.getDeptCode());
	       		}else{
	       			hrow.createCell(0).setCellValue(map.get(deptCode));
	       		}
	       	hrow.createCell(1).setCellValue(vo.getBingqu());
	       	hrow.createCell(2).setCellValue(vo.getYearedTol());
	       	hrow.createCell(3).setCellValue(vo.getYearTol());
	       	hrow.createCell(4).setCellValue(vo.getIncreaseTol());
	       	hrow.createCell(5).setCellValue(vo.getRateTol());
	       	hrow.createCell(6).setCellValue(vo.getYearedMZ());
	       	hrow.createCell(7).setCellValue(vo.getYearMZ());
	       	hrow.createCell(8).setCellValue(vo.getIncreaseMZ());
	       	hrow.createCell(9).setCellValue(vo.getRateMZ());
	       	hrow.createCell(10).setCellValue(vo.getYearedZY());
	       	hrow.createCell(11).setCellValue(vo.getYearZY());
	       	hrow.createCell(12).setCellValue(vo.getIncreaseZY());
	       	hrow.createCell(13).setCellValue(vo.getRateZY());
	       		j++;
	       	}
	       }
	       try {
				workbook.write(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}

	@Override
	public void init_YYNKYXBHNEYXBDBBT(String begin, String end, Integer type) {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="YYNKYXBHNEYXBDBBT";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());
					internalCompare2Dao.init_YYNKYXBHNEYXBDBBT(menuAlias, null, begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
			
			}
		}
		
	}
}
