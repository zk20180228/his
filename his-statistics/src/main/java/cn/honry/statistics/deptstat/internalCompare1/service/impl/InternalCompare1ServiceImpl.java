package cn.honry.statistics.deptstat.internalCompare1.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import cn.honry.inner.statistics.internalCompare1.dao.InnerCompareDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.internalCompare1.dao.InternalCompare1Dao;
import cn.honry.statistics.deptstat.internalCompare1.service.InternalCompare1Service;
import cn.honry.statistics.deptstat.internalCompare1.vo.FicDeptVO;
import cn.honry.statistics.deptstat.internalCompare1.vo.InternalCompare1Vo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.NumberUtil;

@Service("internalCompare1Service")
@Transactional
@SuppressWarnings({"all"})
public class InternalCompare1ServiceImpl implements InternalCompare1Service {
	@Autowired
	@Qualifier(value="innerCompareDao")
	private InnerCompareDao innerCompareDao;
	
	public void setInnerCompareDao(InnerCompareDao innerCompareDao) {
		this.innerCompareDao = innerCompareDao;
	}
	@Resource
	private InternalCompare1Dao internalCompare1Dao;
	@Resource
	private ParameterInnerDAO parameterInnerDAO;
	
	static SimpleDateFormat sdFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Calendar ca = Calendar.getInstance();

	@Override
	public List<InternalCompare1Vo> getInternalCompare1(String prevTime, String time, String depts1, String deptName,boolean sign) {
		
		
		NumberUtil num = NumberUtil.init();
		
		List<InternalCompare1Vo>  compareList = new ArrayList<InternalCompare1Vo>();
		if(sign){
			compareList = internalCompare1Dao.queryForDBSque(prevTime, time, depts1, deptName);
		}else{
			String[] tables = {"T_REGISTER_MAIN","T_REGISTER_MAIN_NOW"};
			String[] tables1 = {"T_INPATIENT_INFO","T_INPATIENT_INFO_NOW"};
			List<String> tnL = getTnLMZ(prevTime, time, tables);
			List<String> tnL1 = getTnLMZ(prevTime, time, tables1);
			compareList= internalCompare1Dao.getInternalCompare1(tnL, tnL1, prevTime, time, depts1);
		}	
		if(compareList==null||compareList.size()==0){
			return new ArrayList<InternalCompare1Vo>();
		}
		//河医院区//郑东院区//惠济院区
		Map<String,InternalCompare1Vo> totalMap=new LinkedHashMap<String,InternalCompare1Vo>(){{
			InternalCompare1Vo total1 = new InternalCompare1Vo();
			total1.setDeptCode("总合计");
			total1.setDeptName("总合计");
			put("ALL",total1);
			InternalCompare1Vo hy1 = new InternalCompare1Vo();
			hy1.setDeptCode("河医院区");
			hy1.setDeptName("河医院区");
			put("1",hy1);
			InternalCompare1Vo zd1 = new InternalCompare1Vo();
			zd1.setDeptCode("郑东院区");
			zd1.setDeptName("郑东院区");
			put("2",zd1);
			InternalCompare1Vo hj1 = new InternalCompare1Vo();
			hj1.setDeptCode("惠济院区");
			hj1.setDeptName("惠济院区");
			put("3",hj1);
		}};
		for(InternalCompare1Vo vo:compareList){//进行记录的汇总计算
			InternalCompare1Vo equeryVo=totalMap.get(vo.getDistrict());
			InternalCompare1Vo AllVo=totalMap.get("ALL");
			if(equeryVo !=null){
				Integer registerCountPrev=vo.getRegisterCountPrev();
				Integer registerCount=vo.getRegisterCount();
				equeryVo.setRegisterCountPrev(equeryVo.getRegisterCountPrev()==null?registerCountPrev:equeryVo.getRegisterCountPrev()+registerCountPrev);
				equeryVo.setRegisterCount(equeryVo.getRegisterCount()==null?registerCount:equeryVo.getRegisterCount()+registerCount);
				
				AllVo.setRegisterCountPrev(AllVo.getRegisterCountPrev()==null?registerCountPrev:AllVo.getRegisterCountPrev()+registerCountPrev);
				AllVo.setRegisterCount(AllVo.getRegisterCount()==null?registerCount:AllVo.getRegisterCount()+registerCount);
				
				//入院人数（上一年）
				Integer inHospitalCountPrev=vo.getInHospitalCountPrev();
				//入院人数（当年）
				Integer inHospitalCount=vo.getInHospitalCount();
				equeryVo.setInHospitalCountPrev(equeryVo.getInHospitalCountPrev()==null?inHospitalCountPrev:inHospitalCountPrev+equeryVo.getInHospitalCountPrev());
				equeryVo.setInHospitalCount(equeryVo.getInHospitalCount()==null?inHospitalCount:inHospitalCount+equeryVo.getInHospitalCount());
			
				AllVo.setInHospitalCountPrev(AllVo.getInHospitalCountPrev()==null?inHospitalCountPrev:inHospitalCountPrev+AllVo.getInHospitalCountPrev());
				AllVo.setInHospitalCount(AllVo.getInHospitalCount()==null?inHospitalCount:inHospitalCount+AllVo.getInHospitalCount());
			
				
				//出院人数（上一年）
				Integer outHospitalCountPrev=vo.getOutHospitalCountPrev();
				//出院人数（当年）
				Integer outHospitalCount=vo.getOutHospitalCount();
				equeryVo.setOutHospitalCountPrev(equeryVo.getOutHospitalCountPrev()==null?outHospitalCountPrev:outHospitalCountPrev+equeryVo.getOutHospitalCountPrev());
				equeryVo.setOutHospitalCount(equeryVo.getOutHospitalCount()==null?outHospitalCount:outHospitalCount+equeryVo.getOutHospitalCount());
				
				AllVo.setOutHospitalCountPrev(AllVo.getOutHospitalCountPrev()==null?outHospitalCountPrev:outHospitalCountPrev+AllVo.getOutHospitalCountPrev());
				AllVo.setOutHospitalCount(AllVo.getOutHospitalCount()==null?outHospitalCount:outHospitalCount+AllVo.getOutHospitalCount());
				
				//平均住院天数（上一年）
				Double avgInpatientCountPrev=vo.getAvgInpatientCountPrev();
				//平均住院天数（当年）
				Double avgInpatientCount=vo.getAvgInpatientCount();
				equeryVo.setAvgInpatientCountPrev(equeryVo.getAvgInpatientCountPrev()==null?avgInpatientCountPrev:avgInpatientCountPrev+equeryVo.getAvgInpatientCountPrev());
				equeryVo.setAvgInpatientCount(equeryVo.getAvgInpatientCount()==null?avgInpatientCount:avgInpatientCount+equeryVo.getAvgInpatientCount());
			
				AllVo.setAvgInpatientCountPrev(AllVo.getAvgInpatientCountPrev()==null?avgInpatientCountPrev:avgInpatientCountPrev+AllVo.getAvgInpatientCountPrev());
				AllVo.setAvgInpatientCount(AllVo.getAvgInpatientCount()==null?avgInpatientCount:avgInpatientCount+AllVo.getAvgInpatientCount());
			
				//病床周转次数（上一年）
				Integer bedTurnoverCountPrev=vo.getBedTurnoverCountPrev();
				//病床周转次数（当年）
				Integer bedTurnoverCount=vo.getBedTurnoverCount();
				equeryVo.setBedTurnoverCountPrev(equeryVo.getBedTurnoverCountPrev()==null?bedTurnoverCountPrev:bedTurnoverCountPrev+equeryVo.getBedTurnoverCountPrev());
				equeryVo.setBedTurnoverCount(equeryVo.getBedTurnoverCount()==null?bedTurnoverCount:bedTurnoverCount+equeryVo.getBedTurnoverCount());
			
				AllVo.setBedTurnoverCountPrev(AllVo.getBedTurnoverCountPrev()==null?bedTurnoverCountPrev:bedTurnoverCountPrev+AllVo.getBedTurnoverCountPrev());
				AllVo.setBedTurnoverCount(AllVo.getBedTurnoverCount()==null?bedTurnoverCount:bedTurnoverCount+AllVo.getBedTurnoverCount());
			
				//病床(当年)
				Integer realBed=vo.getRealBed();
				//使用病床(当年)
				Integer realUsedBed=vo.getRealUsedBed();
				//病床(上年)
				Integer prevRealBed=vo.getPrevRealBed();
				//使用病床(上年)
				Integer prevRealUsedBed=vo.getPrevRealUsedBed();
				equeryVo.setRealBed(equeryVo.getRealBed()==null?realBed:realBed+equeryVo.getRealBed());
				equeryVo.setRealUsedBed(equeryVo.getRealUsedBed()==null?realUsedBed:realUsedBed+equeryVo.getRealUsedBed());
				equeryVo.setPrevRealBed(equeryVo.getPrevRealBed()==null?prevRealBed:prevRealBed+equeryVo.getPrevRealBed());
				equeryVo.setPrevRealUsedBed(equeryVo.getPrevRealUsedBed()==null?prevRealUsedBed:equeryVo.getPrevRealUsedBed()+prevRealUsedBed);
			
				AllVo.setRealBed(AllVo.getRealBed()==null?realBed:realBed+AllVo.getRealBed());
				AllVo.setRealUsedBed(AllVo.getRealUsedBed()==null?realUsedBed:realUsedBed+AllVo.getRealUsedBed());
				AllVo.setPrevRealBed(AllVo.getPrevRealBed()==null?prevRealBed:prevRealBed+AllVo.getPrevRealBed());
				AllVo.setPrevRealUsedBed(AllVo.getPrevRealUsedBed()==null?prevRealUsedBed:AllVo.getPrevRealUsedBed()+prevRealUsedBed);
			
				//危重未死（上年）
				Integer prevNodeath=vo.getPrevNodeath();
				//危重死亡（上年）
				Integer prevDeath=vo.getPrevDeath();
				//危重未死(当年)
				Integer nodeath=vo.getNodeath();
				//危重死亡(当年)
				Integer death=vo.getDeath();
				
				equeryVo.setPrevNodeath(equeryVo.getPrevNodeath()==null?prevNodeath:equeryVo.getPrevNodeath()+prevNodeath);
				equeryVo.setPrevDeath(equeryVo.getPrevDeath()==null?prevDeath:equeryVo.getPrevDeath()+prevDeath);
				equeryVo.setNodeath(equeryVo.getNodeath()==null?0:nodeath+equeryVo.getNodeath());
				equeryVo.setDeath(equeryVo.getDeath()==null?0:death+equeryVo.getDeath());
				
				AllVo.setPrevNodeath(AllVo.getPrevNodeath()==null?prevNodeath:AllVo.getPrevNodeath()+prevNodeath);
				AllVo.setPrevDeath(AllVo.getPrevDeath()==null?prevDeath:AllVo.getPrevDeath()+prevDeath);
				AllVo.setNodeath(AllVo.getNodeath()==null?0:nodeath+AllVo.getNodeath());
				AllVo.setDeath(AllVo.getDeath()==null?0:death+AllVo.getDeath());
			}
		}
		compareList=result(compareList);
		List<InternalCompare1Vo> fristResult=new ArrayList<InternalCompare1Vo>(4);
		for(String tot:totalMap.keySet()){
			InternalCompare1Vo vo=totalMap.get(tot);
			vo.setAvgInpatientCount(Double.valueOf(NumberUtil.init().format(vo.getAvgInpatientCount()/compareList.size(),2)));
			vo.setAvgInpatientCountPrev(Double.valueOf(NumberUtil.init().format(vo.getAvgInpatientCountPrev()/compareList.size(),2)));
			fristResult.add(totalMap.get(tot));
		}
		compareList.addAll(0, result(fristResult));
		
		return compareList;
	}
	private List<InternalCompare1Vo> result(List<InternalCompare1Vo> list){
		for(InternalCompare1Vo vo:list){
			//增长数(当年-上一年)
			Integer rr =vo.getRegisterCount()-vo.getRegisterCountPrev();
			vo.setRegisterRise(rr);
			vo.setRegisterRisePercent(Double.valueOf(NumberUtil.init().format(vo.getRegisterCountPrev()==0?0:rr/(double)vo.getRegisterCountPrev()*100,2)));
			
			Integer ir = vo.getInHospitalCount()-vo.getInHospitalCountPrev();
			vo.setInHospitalRise(ir);
			vo.setInHospitalPercent(Double.valueOf(NumberUtil.init().format(vo.getInHospitalCountPrev()==0?0:ir/(double)vo.getInHospitalCountPrev()*100,2)));
		
			Integer or = vo.getOutHospitalCount()-vo.getOutHospitalCountPrev();
			vo.setOutHospitalRise(or);
			vo.setOutHospitalPercent(Double.valueOf(NumberUtil.init().format(vo.getOutHospitalCountPrev()==0?0:or/(double)vo.getOutHospitalCountPrev()*100,2)));
		
			double rise=Double.parseDouble(NumberUtil.init().format(vo.getAvgInpatientCount()-vo.getAvgInpatientCountPrev(), 2));
			vo.setAvgInpatientRise(rise);
			vo.setAvgInpatientPercent(Double.parseDouble(NumberUtil.init().format(vo.getAvgInpatientCountPrev()==0?0:rise/vo.getAvgInpatientCountPrev()*100,2)));
		
			Integer bedTurnOverRise=vo.getBedTurnoverCount()-vo.getBedTurnoverCountPrev();
			vo.setBedTurnoverRise(bedTurnOverRise);
			vo.setBedTurnoverPercent(Double.valueOf(NumberUtil.init().format(vo.getBedTurnoverCountPrev()==0?0:bedTurnOverRise*100/(double)vo.getBedTurnoverCountPrev(),2)));
			
			vo.setBedUseRatePrev(Double.valueOf(NumberUtil.init().format(vo.getPrevRealBed()==0?0:vo.getPrevRealUsedBed()*100/(double)vo.getPrevRealBed(),2)));
			vo.setBedUseRate(Double.valueOf(NumberUtil.init().format(vo.getRealBed()==0?0:vo.getRealUsedBed()*100/(double)vo.getRealBed(),2)));
			
			Integer usedBed=vo.getRealUsedBed()-vo.getPrevRealUsedBed();
			vo.setBedUseRateRise(usedBed);
			vo.setBedUseRatePercent(Double.valueOf(NumberUtil.init().format(vo.getBedUseRate()-vo.getBedUseRatePrev(),2)));
			
			Integer prevTotal=vo.getPrevNodeath()+vo.getPrevDeath();
			vo.setRescueSuccessRatePrev(Double.valueOf(NumberUtil.init().format(prevTotal==0?0:vo.getPrevNodeath()*100/(double)prevTotal,2)));
			Integer Total=vo.getNodeath()+vo.getDeath();
			vo.setRescueSuccessRate(Double.valueOf(NumberUtil.init().format(Total==0?0:vo.getNodeath()*100/(double)Total,2)));
			Integer totalRise=vo.getDeath()-vo.getPrevDeath();
			vo.setRescueSuccessRateRise(totalRise);
			vo.setRescueSuccessRatePercent(Double.valueOf(NumberUtil.init().format(vo.getPrevDeath()==0?0:totalRise*100/(double)vo.getPrevDeath(),2)));
		}
		return list;
	}
	private List<String> getTnLMZ(String begin, String end, String[] tables) {
		List<String> tnL = null;
		try {
			// 获取当前表最大时间及最小时间
			Date sTime = DateUtils.parseDateY_M(begin);
			Date eTime = DateUtils.parseDateY_M(end);
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
							tables[0], begin, end);
				} else {// 2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						// 获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(dTime), begin);// ------>结束时间大于等于当前时间
					// 获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,
							tables[0], yNum + 1);
					tnL.add(0, tables[1]);
				}
			} else {// 3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add(tables[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		return tnL;
	}

	@Override
	public void initCompare1List(String begin, String end, String dept[]) {	
		List<String> tnL = null;
		List<String> tnL1 = null;
		for(String d : dept){
			internalCompare1Dao.saveInternalCompare1ToDB(tnL,tnL1, begin, end, d);
		}		
	}

	@Override
	public void exportExcel(ServletOutputStream stream,
			List<InternalCompare1Vo> journalVos, String date) {
		int prevYear = Integer.parseInt(date.split("-")[0])-1;
		int year = Integer.parseInt(date.split("-")[0]);
		//创建workbook   
	    HSSFWorkbook workbook = new HSSFWorkbook();   
	    //创建sheet页  
	    HSSFSheet sheet = workbook.createSheet(HisParameters.PREFIXFILENAME+"内科医学部和内二医学部对比表1");   
	    //创建单元格  
	    HSSFRow row = sheet.createRow(0);   
	    HSSFCell c0 = row.createCell(0); 
	    c0.setCellValue(new HSSFRichTextString("部门名称"));   
	    HSSFCell c1 = row.createCell(1);   
	    c1.setCellValue(new HSSFRichTextString("病区负责人"));
	    HSSFCell c2 = row.createCell(2);   
	    c2.setCellValue(new HSSFRichTextString("门诊量（人次）")); 
	    HSSFCell c3 = row.createCell(6);
	    c3.setCellValue(new HSSFRichTextString("入院人数（人次）"));
	    HSSFCell c4 = row.createCell(10);
	    c4.setCellValue(new HSSFRichTextString("出院人数（人次）"));
	    HSSFCell c5 = row.createCell(14);
	    c5.setCellValue(new HSSFRichTextString("平均住院天数（天）"));
	    HSSFCell c6 = row.createCell(18);
	    c6.setCellValue(new HSSFRichTextString("病床周转次数（次）"));
	    HSSFCell c7 = row.createCell(22);
	    c7.setCellValue(new HSSFRichTextString("床位使用率（%）"));
	    HSSFCell c8 = row.createCell(26);
	    c8.setCellValue(new HSSFRichTextString("危急重抢救成功率（%）"));
	    //门诊量（人次）
	    HSSFRow row1 = sheet.createRow(1);
	    HSSFCell c9 = row1.createCell(2);
	    c9.setCellValue(new HSSFRichTextString(prevYear+"年"));   
	    HSSFCell c10 = row1.createCell(3);   
	    c10.setCellValue(new HSSFRichTextString(year+"年")); 
	    HSSFCell c11 = row1.createCell(4);
	    c11.setCellValue(new HSSFRichTextString("增长数"));
	    HSSFCell c12 = row1.createCell(5);
	    c12.setCellValue(new HSSFRichTextString("增长率（%）"));
	    //入院人数（人次）
	    HSSFCell c13 = row1.createCell(6);
	    c13.setCellValue(new HSSFRichTextString(prevYear+"年"));   
	    HSSFCell c14 = row1.createCell(7);   
	    c14.setCellValue(new HSSFRichTextString(year+"年")); 
	    HSSFCell c15 = row1.createCell(8);
	    c15.setCellValue(new HSSFRichTextString("增长数"));
	    HSSFCell c16 = row1.createCell(9);
	    c16.setCellValue(new HSSFRichTextString("增长率（%）"));
	    //出院人数（人次）
	    HSSFCell c17 = row1.createCell(10);
	    c17.setCellValue(new HSSFRichTextString(prevYear+"年"));   
	    HSSFCell c18 = row1.createCell(11);   
	    c18.setCellValue(new HSSFRichTextString(year+"年")); 
	    HSSFCell c19 = row1.createCell(12);
	    c19.setCellValue(new HSSFRichTextString("增长数"));
	    HSSFCell c20 = row1.createCell(13);
	    c20.setCellValue(new HSSFRichTextString("增长率（%）"));
	    //平均住院天数（天）
	    HSSFCell c21 = row1.createCell(14);
	    c21.setCellValue(new HSSFRichTextString(prevYear+"年"));   
	    HSSFCell c22 = row1.createCell(15);   
	    c22.setCellValue(new HSSFRichTextString(year+"年")); 
	    HSSFCell c23 = row1.createCell(16);
	    c23.setCellValue(new HSSFRichTextString("增长数"));
	    HSSFCell c24 = row1.createCell(17);
	    c24.setCellValue(new HSSFRichTextString("增长率（%）"));
	    //病床周转次数（次）
	    HSSFCell c25 = row1.createCell(18);
	    c25.setCellValue(new HSSFRichTextString(prevYear+"年"));   
	    HSSFCell c26 = row1.createCell(19);   
	    c26.setCellValue(new HSSFRichTextString(year+"年")); 
	    HSSFCell c27 = row1.createCell(20);
	    c27.setCellValue(new HSSFRichTextString("增长数"));
	    HSSFCell c28 = row1.createCell(21);
	    c28.setCellValue(new HSSFRichTextString("增长率（%）"));
	    //床位使用率（%）
	    HSSFCell c29 = row1.createCell(22);
	    c29.setCellValue(new HSSFRichTextString(prevYear+"年"));   
	    HSSFCell c30 = row1.createCell(23);   
	    c30.setCellValue(new HSSFRichTextString(year+"年")); 
	    HSSFCell c31 = row1.createCell(24);
	    c31.setCellValue(new HSSFRichTextString("增长数"));
	    HSSFCell c32 = row1.createCell(25);
	    c32.setCellValue(new HSSFRichTextString("增长率（%）"));
	    //危急重抢救成功率（%）
	    HSSFCell c33 = row1.createCell(26);
	    c33.setCellValue(new HSSFRichTextString(prevYear+"年"));   
	    HSSFCell c34 = row1.createCell(27);   
	    c34.setCellValue(new HSSFRichTextString(year+"年")); 
	    HSSFCell c35 = row1.createCell(28);
	    c35.setCellValue(new HSSFRichTextString("增长数"));
	    HSSFCell c36 = row1.createCell(29);
	    c36.setCellValue(new HSSFRichTextString("增长率（%）"));
	    
	    Region region1 = new Region(0, (short)0, 1, (short)0); 
	    Region region2 = new Region(0, (short)1, 1, (short)1); 
	    Region region3 = new Region(0, (short)2, 0, (short)5);
	    Region region4 = new Region(0, (short)6, 0, (short)9);
	    Region region5 = new Region(0, (short)10, 0, (short)13);
	    Region region6 = new Region(0, (short)14, 0, (short)17);
	    Region region7 = new Region(0, (short)18, 0, (short)21);
	    Region region8 = new Region(0, (short)22, 0, (short)25);
	    sheet.addMergedRegion(region1);
	    sheet.addMergedRegion(region2);
	    sheet.addMergedRegion(region3);
	    sheet.addMergedRegion(region4);
	    sheet.addMergedRegion(region5);
	    sheet.addMergedRegion(region6);
	    sheet.addMergedRegion(region7);
	    sheet.addMergedRegion(region8);
	    
	    int j = 1;
	    for(InternalCompare1Vo vo : journalVos){
	    	//title
	    	if("内科医学部".equals(vo.getDeptName()) || "内二医学部".equals(vo.getDeptName())){
	    		HSSFRow hrow = sheet.createRow(j+1);
		    	hrow.createCell(0).setCellValue(vo.getDeptName());
		    	hrow.createCell(1).setCellValue("");
		    	hrow.createCell(2).setCellValue("");
		    	hrow.createCell(3).setCellValue("");
		    	hrow.createCell(4).setCellValue("");
		    	hrow.createCell(5).setCellValue("");
		    	hrow.createCell(6).setCellValue("");
		    	hrow.createCell(7).setCellValue("");
		    	hrow.createCell(8).setCellValue("");
		    	hrow.createCell(9).setCellValue("");
		    	hrow.createCell(10).setCellValue("");
		    	hrow.createCell(11).setCellValue("");
		    	hrow.createCell(12).setCellValue("");
		    	hrow.createCell(13).setCellValue("");
		    	hrow.createCell(14).setCellValue("");
		    	hrow.createCell(15).setCellValue("");
		    	hrow.createCell(16).setCellValue("");
		    	hrow.createCell(17).setCellValue("");
		    	j++;
	    	}
	    	if(vo.getRegisterCountPrev() != null){
	    		HSSFRow hrow = sheet.createRow(j+1);
		    	hrow.createCell(0).setCellValue(vo.getDeptName());
		    	hrow.createCell(1).setCellValue(vo.getDisLeader());
		    	hrow.createCell(2).setCellValue(vo.getRegisterCountPrev());
		    	hrow.createCell(3).setCellValue(vo.getRegisterCount());
		    	hrow.createCell(4).setCellValue(vo.getRegisterRise());
		    	hrow.createCell(5).setCellValue(vo.getRegisterRisePercent());
		    	hrow.createCell(6).setCellValue(vo.getInHospitalCountPrev());
		    	hrow.createCell(7).setCellValue(vo.getInHospitalCount());
		    	hrow.createCell(8).setCellValue(vo.getInHospitalRise());
		    	hrow.createCell(9).setCellValue(vo.getInHospitalPercent());
		    	hrow.createCell(10).setCellValue(vo.getOutHospitalCountPrev());
		    	hrow.createCell(11).setCellValue(vo.getOutHospitalCount());
		    	hrow.createCell(12).setCellValue(vo.getOutHospitalRise());
		    	hrow.createCell(13).setCellValue(vo.getOutHospitalPercent());
		    	hrow.createCell(14).setCellValue(vo.getAvgInpatientCountPrev());
		    	hrow.createCell(15).setCellValue(vo.getAvgInpatientCount());
		    	hrow.createCell(16).setCellValue(vo.getAvgInpatientRise());
		    	hrow.createCell(17).setCellValue(vo.getAvgInpatientPercent());
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
	public String getDept(String ficDeptCode) {
		List<FicDeptVO> list = internalCompare1Dao.getDept(ficDeptCode);
		StringBuffer ids =new StringBuffer();
		for(int i = 0;i < list.size();i++){
			if(i>0){
				ids.append("','");
			}
			ids.append(list.get(i).getId());
		}
		return ids.toString();
	}
	@Override
	public void initCompare1(String begin, String end, Integer type)throws Exception {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="YYNKYXBHNEYXBDBBO";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				Date endDate=DateUtils.parseDateY_M(end);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());
					innerCompareDao.initCompare(menuAlias, null, begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}else if(3==type){//年数据dateformate:yyyy
			
			}
		}
	}
}
	
