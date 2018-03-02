package cn.honry.statistics.drug.inventoryLog.service.impl;

import java.io.IOException;
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

import cn.honry.base.bean.model.DrugChecklogs;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.inventoryLog.dao.InventoryLogDao;
import cn.honry.statistics.drug.inventoryLog.service.InventoryLogService;
import cn.honry.statistics.drug.inventoryLog.vo.InventoryLogVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;

@Service("inventoryLogService")
@Transactional
@SuppressWarnings({ "all" })
public class InventoryLogServiceImpl implements InventoryLogService{

	@Autowired
	@Qualifier(value = "inventoryLogDao")
	private InventoryLogDao inventoryLogDao;

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Autowired
	@Qualifier(value = "redisUtil")
	private RedisUtil redisUtil;
	public void setRedisUtil(RedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
	@Override
	public DrugChecklogs get(String arg0) {
		return null;
	}


	@Override
	public void removeUnused(String arg0) {
		
	}


	@Override
	public void saveOrUpdate(DrugChecklogs arg0) {
		
	}
	
	/**  
	 *  
	 * @Description：  科室下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<SysDepartment> getComboboxdept() throws Exception {
		return inventoryLogDao.getComboboxdept();
	}

	/***
	 * 盘点日志查询(统计)记录
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public List<InventoryLogVo> queryInventoryLog(String Stime, String Etime,
			String dept, String page, String rows,String drug) throws Exception {
		List<String> tnL = getTnL(Stime, Etime);
		return inventoryLogDao.queryInventoryLog(tnL,Stime, Etime, dept, page, rows,drug);
	}
	public List<String> getTnL(String Stime,String Etime){
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(Stime);
			Date eTime = DateUtils.parseDateY_M_D(Etime);
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_DRUG_CHECKLOGS",Stime,Etime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),Stime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_DRUG_CHECKLOGS",yNum+1);
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_DRUG_CHECKLOGS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return tnL;
	}

	/***
	 * 盘点日志查询(统计)条数
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	 * @throws Exception 
	 */
	@Override
	public int queryInventoryLogTotle(String Stime, String Etime, String dept,String drug) throws Exception {
		String redKey = "PDRZCX:"+Stime+"_"+Etime+"_"+dept+"_"+drug;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			List<String> tnL = getTnL(Stime, Etime);
			totalNum = inventoryLogDao.queryInventoryLogTotle(tnL,Stime, Etime, dept,drug);
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

	/**  
	 *  
	 * @Description：  药品下拉框
	 * @Author：zpty
	 * @CreateDate：2016-6-22
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<DrugInfo> getComboboxdrug() throws Exception {
		return inventoryLogDao.getComboboxdrug();
	}

	/**
	 * @Description:导出 
	 * @Author： lt @CreateDate： 2015-9-10
	 * @param @throws Exception
	 * @return void
	 * @version 1.0
	 * @throws Exception 
	 **/
	@Override
	public List<InventoryLogVo> queryInvLogExp(String Stime, String Etime,
			String dept, String drug) throws Exception {
		List<String> tnL = null;
		tnL =getTnL(Stime, Etime); 
		return inventoryLogDao.queryInvLogExp(tnL,Stime, Etime, dept,drug);
	}

	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: zpty
	 * @CreateDate: 2016年6月22日 
	 * @version 1.0
	**/
	@Override
	public FileUtil export(List<InventoryLogVo> list, FileUtil fUtil) {
		for (InventoryLogVo model : list) {
			String record="";
				record += CommonStringUtils.trimToEmpty(model.getDeptName()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getTradeName()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getBatchNo()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getSpecs()) + ",";
				record += model.getRetailPrice() + ",";
				record += CommonStringUtils.trimToEmpty(model.getPackUnit()) + ",";
				record += model.getPackQty() + ",";
				record += model.getAdjustNum() + ",";
				record += CommonStringUtils.trimToEmpty(model.getAdjustUnit()) + ",";
				record += CommonStringUtils.trimToEmpty(model.getPlaceNo()) + ",";
				record += model.getCreatetime() + ",";
				record += CommonStringUtils.trimToEmpty(model.getUserName());
				try {
					fUtil.write(record);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return fUtil;
	}
}
