package cn.honry.statistics.drug.dosageDetail.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.StoRecipe;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.dosageDetail.dao.DosDetailDao;
import cn.honry.statistics.drug.dosageDetail.service.DosDetailService;
import cn.honry.statistics.drug.dosageDetail.vo.DetailVo;
import cn.honry.statistics.sys.reportForms.dao.ReportFormsDao;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;

@Service("dosDetailService")
@Transactional(rollbackFor = {Throwable.class})
@SuppressWarnings("all")
public class DosDetailServiceImpl implements DosDetailService {
	
	/***
	 * 注入本类dao
	 */
	@Autowired
	@Qualifier(value = "dosDetailDao")
	private DosDetailDao dosDetailDao;
	@Autowired
	@Qualifier(value = "reportFormsDAO")
	private ReportFormsDao reportFormsDAO;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public StoRecipe get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(StoRecipe arg0) {
		
	}

	@Override
	public List<SysDepartment> deptForType(String type) {
		
		return dosDetailDao.deptForType(type);
	}

	/***
	 * 
	 * @Title: queryDetail 
	 * @author  WFJ
	 * @createDate ：2016年6月23日
	 * @param typeView 视图方式 0药房_终端显示 ；1药房_人员显示；2人员检索
	 * @param typeValue value值
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param param 配发药标识，1配药，2发药
	 * @return List<StoRecipe>
	 * @version 1.0
	 */
	@Override
	public List<DetailVo> queryDetail(String typeView, String typeValue, String beginDate, String endDate,String param,String code) {
		
		List<DetailVo> list = new ArrayList<DetailVo>();
		 
		//获取门诊数据保留时间
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		
		//获得当前时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dTime;
		try {
			dTime = df.parse(df.format(new Date()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		//获得在线库数据应保留最小时间
		Date cTime = DateUtils.addDay(dTime,-Integer.parseInt(dateNum)+1);
		
		if("0".equals(typeView)){
			
			Date sTime = DateUtils.parseDateY_M_D(beginDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			String invoiceDetialTabName = null;
			List<String> invoicePartitionName = new ArrayList<String>();
			List<String> feedetialPartitionName = new ArrayList<String>();
			
			//StatVo feeDetial = reportFormsDAO.findMaxMinByTabNameAndField("T_STO_RECIPE_NOW", "FEE_DATE");
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_STO_RECIPE",beginDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginDate);
					
					//获取相差年分的分区集合，默认加1
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_STO_RECIPE",yNum+1);
					feedetialPartitionName.add(0,"T_STO_RECIPE_NOW");
				}
			}else{
			
				feedetialPartitionName.add("T_STO_RECIPE_NOW");
			}
			
			list = dosDetailDao.queryDetail0(feedetialPartitionName, typeValue, beginDate, endDate,param,code, typeView);
		}else if("1".equals(typeView)){
			Date sTime = DateUtils.parseDateY_M_D(beginDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			String invoiceDetialTabName = null;
			List<String> invoicePartitionName = new ArrayList<String>();
			List<String> feedetialPartitionName = new ArrayList<String>();
			
			//StatVo feeDetial = reportFormsDAO.findMaxMinByTabNameAndField("T_STO_RECIPE_NOW", "DRUGED_DATE");
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_STO_RECIPE",beginDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginDate);
					
					//获取相差年分的分区集合，默认加1
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_STO_RECIPE",yNum+1);
					feedetialPartitionName.add(0,"T_STO_RECIPE_NOW");
				}
			}else{
				feedetialPartitionName.add("T_STO_RECIPE_NOW");
			}
			list = dosDetailDao.queryDetail1(feedetialPartitionName,typeValue, beginDate, endDate,param,code);
			for(DetailVo model : list){
				model.setDrugedOper(dosDetailDao.queryEmployee(model.getJobNo()));
			}
		}else if ("2".equals(typeView)) {
			Date sTime = DateUtils.parseDateY_M_D(beginDate);
			Date eTime = DateUtils.parseDateY_M_D(endDate);
			String invoiceDetialTabName = null;
			List<String> invoicePartitionName = new ArrayList<String>();
			List<String> feedetialPartitionName = new ArrayList<String>();
			
			//StatVo feeDetial = reportFormsDAO.findMaxMinByTabNameAndField("T_STO_RECIPE_NOW", "DRUGED_DATE");
			if(DateUtils.compareDate(sTime, cTime)==-1){//查询历史表
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					
					//获取需要查询的全部分区
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_STO_RECIPE",beginDate,endDate);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beginDate);
					
					//获取相差年分的分区集合，默认加1
					feedetialPartitionName = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_STO_RECIPE",yNum+1);
					feedetialPartitionName.add(0,"T_STO_RECIPE_NOW");
				}
			}else{
				feedetialPartitionName.add("T_STO_RECIPE_NOW");
			}
			
			list = dosDetailDao.queryDetail2(feedetialPartitionName,typeValue, beginDate, endDate,param,code);
			for(DetailVo model : list){
				model.setDrugedOper(dosDetailDao.queryEmployee(model.getJobNo()));
			}
		}
		
		return list;
	}

	@Override
	public FileUtil export(List<DetailVo> list, FileUtil fUtil,String typeView) throws Exception {
		
		for(DetailVo vo : list){
			String record="";			
			if("0".equals(typeView)){
				record = CommonStringUtils.trimToEmpty(vo.getDrugedTerminal()) + ",";
			}else if("1".equals(typeView)){
				record = CommonStringUtils.trimToEmpty(vo.getJobNo()) + ",";
				record += CommonStringUtils.trimToEmpty(vo.getDrugedOper()) + ",";
			}else if("2".equals(typeView)){
				record = CommonStringUtils.trimToEmpty(vo.getDrugedOper()) + ",";
				record += CommonStringUtils.trimToEmpty(vo.getDrugedTerminal()) + ",";
			}
			record += vo.getRecipeCount()+ ",";
			record += vo.getRecipeQty() + ",";
			record += vo.getRecipeCost() + ",";
			record += vo.getSumDays() + ",";
			
			fUtil.write(record);
		}
		
		return fUtil;
	}

}
