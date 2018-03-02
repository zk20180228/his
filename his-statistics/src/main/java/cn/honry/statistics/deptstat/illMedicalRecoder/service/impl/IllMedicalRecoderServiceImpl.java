package cn.honry.statistics.deptstat.illMedicalRecoder.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.deptstat.diseaseSurveillance.vo.DiseaseSurveillanceVo;
import cn.honry.statistics.deptstat.illMedicalRecoder.dao.IllMedicalRecoderDao;
import cn.honry.statistics.deptstat.illMedicalRecoder.service.IllMedicalRecoderService;
import cn.honry.statistics.deptstat.illMedicalRecoder.vo.IllMedicalRecoderVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Service("illMedicalRecoderService")
@Transactional
@SuppressWarnings({"all"})
public class IllMedicalRecoderServiceImpl implements IllMedicalRecoderService{
	@Autowired
	@Qualifier(value = "illMedicalRecoderDao")
	private IllMedicalRecoderDao illMedicalRecoderDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<IllMedicalRecoderVo> queryIllMedicalRecoder(String deptCode,String menuAlias) {
		boolean flag=new MongoBasicDao().isCollection("WZBLRSBLTJFX");
		List<IllMedicalRecoderVo> list=new ArrayList<IllMedicalRecoderVo>();
		if(flag){
			list=illMedicalRecoderDao.queryIllMedicalRecoderForDB(deptCode,menuAlias);
		}else{
			list=illMedicalRecoderDao.queryIllMedicalRecoder(deptCode,menuAlias);
		}
		return list;
	}
	@Override
	public void saveIllMedicalToDB(String begin, String end) {
		illMedicalRecoderDao.saveIllMedicalToDB(null, null, begin, end);
		
	}
	
	/**  
	 * 
	 * 危重病历人数比例统计分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<IllMedicalRecoderVo> queryIllMedical(String startTime,String endTime,String deptCode,String menuAlias) {
		List<IllMedicalRecoderVo> list=new ArrayList<IllMedicalRecoderVo>();
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
			//判断查询类型   sTime早于
			if(DateUtils.compareDate(sTime, cTime)==-1){
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
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）t_inpatient_info，t_drug_applyout
				tnL.add("T_INPATIENT_INFO_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		list= illMedicalRecoderDao.queryIllMedical(tnL,startTime, endTime, deptCode, menuAlias);
		return list;
	}
}
