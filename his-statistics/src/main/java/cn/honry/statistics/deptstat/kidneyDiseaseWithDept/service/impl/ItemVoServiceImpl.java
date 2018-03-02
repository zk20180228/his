package cn.honry.statistics.deptstat.kidneyDiseaseWithDept.service.impl;

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

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.kidneyDiseaseWithDept.dao.InitKidneyDiseaseWithDeptDao;
import cn.honry.inner.statistics.outpatientUseMedic.dao.InitUseMedicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.dao.ItemVoDao;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.service.ItemVoService;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.ItemVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service("itemVoService")
@Transactional
@SuppressWarnings({ "all" })
public class ItemVoServiceImpl implements ItemVoService{
	@Autowired
	@Qualifier(value = "itemVoDao")
	private ItemVoDao itemVoDao;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value="initKidneyDiseaseWithDeptDao")
	private InitKidneyDiseaseWithDeptDao initKidneyDiseaseWithDeptDao;
	/**  
	 * 
	 * 通过科室查询内容
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月2日 下午8:21:57 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月2日 下午8:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ItemVo quertItemVo(String date,String deptCode) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> tnL = null;
		List<String> tnL2 = null;
		List<String> tnL3 = null;
		List<String> tnL4 = null;
		String begin=null;
		String end=null;
		try {
			Date stime = format.parse(date);
			Calendar a=Calendar.getInstance();
		    a.setTime(stime); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=date+"-01 00:00:00";
			end=date+"-"+MaxDate+" 23:59:59";
			Date sTime = format2.parse(begin);
			Date eTime = format2.parse(end);
			
			//获取住院数据保留时间
			String dateNum1 = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum1.equals("1")){
				dateNum1="30";
			}
			//获取门诊数据保留时间
			String dateNum2 = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
			//获得当前时间
			Date dTime = format2.parse(format2.format(new Date()));
			//获得住院在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum1)+1);
			//获得住院在线库数据应保留最小时间
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
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<String> tnLs = new ArrayList<String>();
		tnLs.add(tnL.get(0));
		tnLs.add(tnL2.get(0));
		tnLs.add(tnL3.get(0));
		tnLs.add(tnL4.get(0));
		return itemVoDao.quertItemVo(tnLs,begin, end,deptCode);
	}
	/*******************************************************mongodb*************************************************/
	/**  
	 * 
	 * 从mongo通过科室查询内容
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月6日 下午4:20:06 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月6日 下午4:20:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ItemVo itemVos(String date, String deptCode) {
		List<ItemVo> vos=new ArrayList<ItemVo>();
		ItemVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", date);
		bdObject.append("deptCode", deptCode);
		DBCursor cursor = new MongoBasicDao().findAlldata("SBKSDBB_SBKSDBB_MONTH", bdObject);
		while(cursor.hasNext()){
			vo = new ItemVo();
			DBObject dbCursor = cursor.next();
			String dept_name=(String) dbCursor.get("deptName");
			Integer ruYuNum = (Integer) dbCursor.get("ruYuNum");	
			Integer chuYUNum = (Integer) dbCursor.get("chuYUNum");
			Integer beds = (Integer) dbCursor.get("beds");
			Integer bedUsed = (Integer)dbCursor.get("bedUsed");
			Double avgInYuDays = (Double) dbCursor.get("avgInYuDays");
			Integer workNum = (Integer) dbCursor.get("workNum");
			Double menCost = (Double) dbCursor.get("menCost")/10000;
			Double zhuCost = (Double) dbCursor.get("zhuCost")/10000;
			vo.setDept_name(dept_name);
			vo.setRuYuNum(ruYuNum);
			vo.setChuYUNum(chuYUNum);
			vo.setBeds(beds);
			vo.setBedUsed(bedUsed);
			vo.setAvgInYuDays(avgInYuDays);
			vo.setWorkNum(workNum);
			vo.setMenCost(menCost);
			vo.setZhuCost(zhuCost);
			vos.add(vo);
		}
		if(vos!=null&&vos.size()>0){
			return vos.get(0);
		}else{
			return new ItemVo();
		}
	}
	/**  
	 * 
	 * 初始化数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年9月8日 下午2:35:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年9月8日 下午2:35:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void init_SBKSDBB(String begin, String end, Integer type)
			throws Exception {
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&type!=null){
			String menuAlias="SBKSDBB";
			if(1==type){//日数据 dateformate:yyyy-MM-dd
				initKidneyDiseaseWithDeptDao.init_SBKSDBB(menuAlias, "HIS", begin);
			}else if(2==type){//月数据 dateformate:yyyy-MM 
				Date beginDate=DateUtils.parseDateY_M(begin);
				Date endDate=DateUtils.parseDateY_M(end);
				Calendar ca=Calendar.getInstance();
				ca.setTime(beginDate);
				while(DateUtils.compareDate(ca.getTime(), endDate)<=0){
					begin=DateUtils.formatDateY_M(ca.getTime());
					initKidneyDiseaseWithDeptDao.init_SBKSDBB_MONTH(menuAlias, "2", begin);
					ca.add(Calendar.MONTH, 1);//下一月
				}
			}
		}
	}
}
