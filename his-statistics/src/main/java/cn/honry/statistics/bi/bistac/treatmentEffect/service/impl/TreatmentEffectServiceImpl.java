package cn.honry.statistics.bi.bistac.treatmentEffect.service.impl;

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
import cn.honry.statistics.bi.bistac.treatmentEffect.dao.TreatmentEffectDao;
import cn.honry.statistics.bi.bistac.treatmentEffect.service.TreatmentEffectService;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
/**   
*  
* @className：TreatmentEffectService
* @description：治疗效果数据分析service实现类
* @author：zpty
* @createDate：2017-03-28  
* @modifyRmk：  
* @version 1.0
 */
@Service("treatmentEffectService")
@Transactional
@SuppressWarnings({"all"})
public class TreatmentEffectServiceImpl implements TreatmentEffectService {
	/**
	 * 注入chargeDao
	 */
	@Autowired
	@Qualifier(value = "treatmentEffectDao")
	private TreatmentEffectDao treatmentEffectDao;
	public void setTreatmentEffectDao(TreatmentEffectDao treatmentEffectDao) {
		this.treatmentEffectDao = treatmentEffectDao;
	}
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	
	@Override
	public TreatmentEffectVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(TreatmentEffectVo arg0) {
		
	}

	/**
	 * @Description:查询当前在院人数
	 * @Author：  zpty
	 * @CreateDate： 2017-3-28
	 * @version 1.0
	**/
	@Override
	public int queryInPeople() {
		return treatmentEffectDao.queryInPeople();
	}

	/**
	 * 查询各科室治疗效果的人数
	 * @author zpty
	 * @CreateDate：2017-03-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<TreatmentEffectVo> queryUserRecord(String yearSelect) {
		TreatmentEffectVo vo = treatmentEffectDao.findMaxMin();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
//		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		List<String> tnL = null;
		String begin=null;
		String end=null;
		String date=null;
		String deptName=null;
		try{
		 	begin = yearSelect+"-01-01";
			end = yearSelect+"-12-31";
//			begin = date+"-01";
//			end = date+"-"+MaxDate;
			Date sTime = DateUtils.parseDateY_M_D(begin);
			Date eTime = DateUtils.parseDateY_M_D(end);
			tnL = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, vo.getsTime())==-1){
				if(DateUtils.compareDate(eTime, vo.getsTime())==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_INFO",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(vo.getsTime()),begin);
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
		return treatmentEffectDao.queryUserRecord(tnL,date,deptName,begin,end);
	}


	/**
	 * @Description:根据年份，从mongodb中查数据
	 * @param date :年份，如：2017
	 * @return 返回封装TreatmentEffectVo的list集合
	 * List<TreatmentEffectVo>
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年5月11日 上午11:31:26
	 */
	public List<TreatmentEffectVo> queryUserRecordByMongo(String date){
		return treatmentEffectDao.queryUserRecordByMongo(date);
	}
	
}
