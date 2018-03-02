package cn.honry.statistics.operation.recipelStat.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.operation.recipelStat.dao.RecipelStatDao;
import cn.honry.statistics.operation.recipelStat.service.RecipelStatService;
import cn.honry.statistics.operation.recipelStat.vo.RecipelInfoVo;
import cn.honry.statistics.operation.recipelStat.vo.RecipelStatVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;


/**  
 *  
 * @className：RecipelStatServiceImpl
 * @Description： 门诊处方查询
 * @Author：aizhonghua
 * @CreateDate：2016-6-23 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-23 下午04:41:31 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("recipelStatService")
@Transactional
@SuppressWarnings({ "all" })
public class RecipelStatServiceImpl implements RecipelStatService{

	@Autowired
	@Qualifier(value = "recipelStatDAO")
	private RecipelStatDao recipelStatDAO;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;

	/**  
	 *  
	 * 获得处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-23 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-23 下午04:41:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String,Object> getRows(String page,String rows,String startTime,String endTime,String type,String para,String vague) {

		Date sTime = DateUtils.parseDateY_M_D(startTime);
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		
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
		
		List<String> tnL = new ArrayList<String>();
	
		//判断查询类型
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				
				//获取需要查询的全部分区
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_STO_RECIPE",startTime,endTime);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
			
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
			
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_STO_RECIPE",yNum+1);
				tnL.add(0,"T_STO_RECIPE_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_STO_RECIPE_NOW");
		}
		
		return recipelStatDAO.getRows(page,rows,startTime,endTime,type,para,vague,tnL);
	}

	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<RecipelInfoVo> getRecipelInfoRows(String startTime, String endTime,String recipeNo) {
		
		//获取当前表最大时间及最小时间
		Date sTime = DateUtils.parseDateY_M_D(startTime);
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		
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
		
		List<String> tnL = new ArrayList<String>();
		
		//判断查询类型
		if(DateUtils.compareDate(sTime, cTime)==-1){
			if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
				
				//获取需要查询的全部分区
				tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",startTime,endTime);
			}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
				
				//获得时间差(年)
				int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
				
				//获取相差年分的分区集合，默认加1
				tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",yNum+1);
				tnL.add(0,"T_DRUG_APPLYOUT_NOW");
			}
		}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
			tnL.add("T_DRUG_APPLYOUT_NOW");
		}
		
		return recipelStatDAO.getRecipelInfoRows(recipeNo,tnL);
	}

	@Override
	public List<RecipelStatVo> getRecipelStatVos(String recipeNos) {
		
		return recipelStatDAO.getRecipelStatVos(recipeNos);
	}

	@Override
	public List<RecipelInfoVo> getRecipelInfos(String startTime,String endTime,String recipeNos) {
		
				//获取当前表最大时间及最小时间
				Date sTime = DateUtils.parseDateY_M_D(startTime);
				Date eTime = DateUtils.parseDateY_M_D(endTime);
				
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
				
				List<String> tnL = new ArrayList<String>();
				
				//判断查询类型
				if(DateUtils.compareDate(sTime, cTime)==-1){
					if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
						
						//获取需要查询的全部分区
						tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",startTime,endTime);
					}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
						
						//获得时间差(年)
						int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
						
						//获取相差年分的分区集合，默认加1
						tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",yNum+1);
						tnL.add(0,"T_DRUG_APPLYOUT_NOW");
					}
				}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
					tnL.add("T_DRUG_APPLYOUT_NOW");
				}
		
				return recipelStatDAO.getRecipelInfos(recipeNos,tnL);
	}

}
