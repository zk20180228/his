package cn.honry.statistics.finance.inoutstore.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.inpatient.inpatientOrder.dao.InpatientOrderInInterDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.inoutstore.dao.InOutStoreDao;
import cn.honry.statistics.finance.inoutstore.service.InOutStoreService;
import cn.honry.statistics.finance.inoutstore.vo.StoreResultVO;
import cn.honry.statistics.finance.inoutstore.vo.StoreSearchVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;

/**  
 *  住院药房入出库台账查询Service实现类
 * @Author:luyanshou
 * @version 1.0
 */
@Service("inOutStoreService")
@Transactional
@SuppressWarnings({"all"})
public class InOutStoreServiceImpl implements InOutStoreService{

	private InOutStoreDao inOutStoreDao;
	@Autowired
	@Qualifier(value="inpatientOrderInInterDao")
	private InpatientOrderInInterDao inpatientOrderInInterDao;
	@Autowired
	@Qualifier(value="inOutStoreDao")
	public void setInOutStoreDao(InOutStoreDao inOutStoreDao) {
		this.inOutStoreDao = inOutStoreDao;
	}
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/**
	 * 查询药品名称和编码
	 * @throws Exception 
	 */
	public List<StoreResultVO> getdrugInfo(String q) throws Exception{
		return inOutStoreDao.getdrugInfo(q);
	}
	
	/**
	 * 查询入出库记录数据
	 * @throws Exception 
	 */
	public Map getStoreData(StoreSearchVo vo,int firstResult,int page,int rows) throws Exception{
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(vo.getBeginTime());
			Date eTime = DateUtils.parseDateY_M_D(vo.getEndTime());
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",vo.getBeginTime(),vo.getEndTime());
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),vo.getBeginTime());
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_OUTSTORE",yNum+1);
					tnL.add(0,"T_DRUG_OUTSTORE_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_DRUG_OUTSTORE_NOW");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		Map map= new HashMap();
		List<StoreResultVO> list = inOutStoreDao.getStoreData(tnL,vo,firstResult,page,rows);
//		
//		Map<String,String> CodeMap=new HashMap<String,String>();
//		List<SysDepartment> sysDept=inpatientOrderInInterDao.querydeptCombobox();
//		for(SysDepartment vo1:sysDept){
//			CodeMap.put(vo1.getDeptCode(), vo1.getDeptName());
//		}
//		sysDept=null;
//		for(StoreResultVO vo2:list){
//			if(null!=vo2.getDrugDeptCode()){
//				vo2.setDrugDeptName(CodeMap.get(vo2.getDrugDeptCode()));
//			}
//		}
		String redKey = "ZYYFRCKTZCX:"+vo.getBeginTime()+"_"+vo.getEndTime()+"_"+vo.getCode()+"_"+vo.getType();
		redKey=redKey.replaceAll(",", "-");
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			totalNum = inOutStoreDao.getCount(tnL,vo,page,rows);
			redisUtil.set(redKey, totalNum);
		}
		String val = parameterInnerDAO.getParameterByCode("deadTime");
		if(StringUtils.isNotBlank(val)){
			redisUtil.expire(redKey,Integer.valueOf(val));
		}else{
			redisUtil.expire(redKey, 300);
		}
		map.put("total", totalNum);
		map.put("rows",  list);
		return map;
	}
	
	
}
