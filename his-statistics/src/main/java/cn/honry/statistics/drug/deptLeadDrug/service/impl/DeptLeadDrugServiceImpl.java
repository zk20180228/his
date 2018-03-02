package cn.honry.statistics.drug.deptLeadDrug.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.deptLeadDrug.dao.DeptLeadDrugDao;
import cn.honry.statistics.drug.deptLeadDrug.service.DeptLeadDrugService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
@Service("deptLeadDrugService")
@Transactional
@SuppressWarnings({ "all" })
public class DeptLeadDrugServiceImpl implements DeptLeadDrugService {

	@Override
	public DrugApplyout get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(DrugApplyout arg0) {
		
	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Autowired
	@Qualifier(value = "deptLeadDrugDao")
	private DeptLeadDrugDao deptLeadDrugDao;
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO codeInInterDAO;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	@Override
	public List<DrugApplyoutNow> queryTableList(String stime, String etime, String drugDept, String drugxz, String drugName,String page,String rows,StatVo statVo) throws Exception {
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(stime);
			Date eTime = DateUtils.parseDateY_M_D(etime);
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",stime,etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",yNum+1);
					tnL.add(0,"T_DRUG_APPLYOUT_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_DRUG_APPLYOUT_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return deptLeadDrugDao.queryTableList(tnL,stime,etime,drugDept,drugxz,drugName, page, rows);
	}
	
	@Override
	public int queryTableListTotal(String stime, String etime, String drugDept, String drugxz, String drugName,StatVo statVo) throws Exception {
		String redKey = "GKSLYQK:"+stime+"_"+etime+"_"+drugDept+"_"+drugxz+"_"+drugName;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
				List<String> tnL = null;
				try {
					//1.时间转换
					Date sTime = DateUtils.parseDateY_M_D(stime);
					Date eTime = DateUtils.parseDateY_M_D(etime);
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
							tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",stime,etime);
						}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
							//获得时间差(年)
							int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime);
							//获取相差年分的分区集合，默认加1
							tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_APPLYOUT",yNum+1);
							tnL.add(0,"T_DRUG_APPLYOUT_NOW");
						}
					}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
						tnL.add("T_DRUG_APPLYOUT_NOW");
					}
				} catch (Exception e) {
					e.printStackTrace();
					tnL = null;
				}
				totalNum = deptLeadDrugDao.queryTableListToatl(tnL,stime, etime, drugDept, drugxz, drugName);
				redisUtil.set(redKey, totalNum);
				
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		return totalNum;
	}
	
	@Override
	public List<SysDepartment> querydrugDept() throws Exception {
		return deptLeadDrugDao.querydrugDept();
	}

	@Override
	public List<DrugInfo> querydrugName() throws Exception {
		return deptLeadDrugDao.querydrugName();
	}

	@Override
	public List<BusinessDictionary> querydrugxz() {
		return codeInInterDAO.getDictionary("drugProperties");
	}

	@Override
	public StatVo findMaxMin() throws Exception {
		return  deptLeadDrugDao.findMaxMin();
	}

}
