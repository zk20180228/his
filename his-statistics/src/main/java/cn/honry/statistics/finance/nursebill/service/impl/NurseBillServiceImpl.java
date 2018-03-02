package cn.honry.statistics.finance.nursebill.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.operation.billsearch.dao.BillSearchInInterDAO;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.finance.nursebill.dao.NurseBillDAO;
import cn.honry.statistics.finance.nursebill.service.NurseBillService;
import cn.honry.statistics.finance.nursebill.vo.NurseBillHzVo;
import cn.honry.statistics.finance.nursebill.vo.NurseBillMxVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

@Service("nurseBillService")
@Transactional
@SuppressWarnings({ "all" })
public class NurseBillServiceImpl implements NurseBillService{
	
	/** 住院取药统计dao **/
	@Autowired
	@Qualifier(value = "nurseBillDAO")
	private NurseBillDAO nurseBillDAO;

	/** 摆药单查询 **/
	@Autowired
	@Qualifier(value = "billSearchInInterDAO")
	private BillSearchInInterDAO billSearchDAO;
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
	
	@Override
	public NurseBillHzVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(NurseBillHzVo arg0) {
		
	}

	@Override
	public List<NurseBillHzVo> getNurseBillHz(String deptCode,String billClassCode,String applyState,String drugName,String page,String rows,String etime, String stime) throws Exception {
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
		return nurseBillDAO.getNurseBillHz(tnL, deptCode, billClassCode, stime, etime, applyState, page, rows,drugName);
	}

	@Override
	public int getHzTotal(String deptCode,String billClassCode,String drugName,String applyState,String etime, String stime) throws Exception {
		String redKey = "HSZLYCXHZ:"+stime+"_"+etime+"_"+deptCode+"_"+billClassCode+"_"+drugName+"_"+applyState;
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
				totalNum = nurseBillDAO.getHzTotal(tnL,deptCode, billClassCode, stime, etime, applyState,drugName);
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
	public List<NurseBillMxVo> getNurseBillMx(String deptCode, String billClassCode, String applyState, String page, String rows,String drugName,String beginTime, String endTime,StatVo statVo ) throws Exception {
		List<String> tnL=getTnl(endTime,beginTime, statVo);
		return nurseBillDAO.getNurseBillMx(tnL,deptCode, billClassCode, applyState, page, rows, drugName,beginTime, endTime);
	}

	public List<String>  getTnl(String etime, String stime,StatVo statVo){
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
		return tnL;
	}
	
	
	@Override
	public int getMxTotal(String deptCode,String billClassCode,String beginTime,String endTime,String applyState,String drugName,StatVo statVo) throws Exception {
		String redKey = "HSZLYCXMX"+beginTime+"_"+endTime+"_"+deptCode+"_"+billClassCode+"_"+applyState+"_"+drugName;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
			List<String> tnL=getTnl(endTime,beginTime, statVo);
			totalNum = nurseBillDAO.getMxTotal(tnL,deptCode, billClassCode, beginTime, endTime, applyState,drugName);
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
	
	/***
	 * 登录病区下的各科室摆药单信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月23日 
	 * @version 1.0
	 * @parameter 
	 * @since 
	 * @return List<TreeJson>
	 */
	@Override
	public List<TreeJson> treeNurseBillSearch() {
		//一级目录当前病区
		SysDepartment sysDept=ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String type=sysDept.getDeptType();
		List<DepartmentContact> depList = billSearchDAO.getDepConByPid(sysDept.getDeptCode());
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
		TreeJson topTreeJson = new TreeJson();
		topTreeJson.setId("1");
		topTreeJson.setText("病区"+"("+sysDept.getDeptName()+")");
		
		//二级目录显示医院维护的摆药单分类
		TreeJson aTreeJson=null;
		List<DrugBillclass> drugList= billSearchDAO.getDrugBillclass();
		if(depList!=null&&depList.size()>0){
			List<TreeJson> aTreeJsonList=new ArrayList<TreeJson>();
			for (DepartmentContact dc : depList) {
				aTreeJson = new TreeJson();
				aTreeJson.setId(dc.getDeptId());
				aTreeJson.setText(dc.getDeptName());
				if(drugList!=null&&drugList.size()>=0){
					List<TreeJson> bTreeJsonList=new ArrayList<TreeJson>();
					TreeJson bTreeJson= null;
					for (DrugBillclass drugBill : drugList) {
						bTreeJson = new TreeJson();
						bTreeJson.setId(drugBill.getId()+dc.getDeptId());
						bTreeJson.setText(drugBill.getBillclassName());
						Map<String,String> deptMap = new HashMap<String,String>();
						deptMap.put("bid", "2");
						deptMap.put("cid",drugBill.getBillclassCode());
						deptMap.put("did", dc.getDeptId());
						bTreeJson.setAttributes(deptMap);
						bTreeJsonList.add(bTreeJson);
					}
					aTreeJson.setChildren(bTreeJsonList);
				}
				Map<String,String> billMap = new HashMap<String,String>();
				billMap.put("pid","1");
				billMap.put("bid","1");
				aTreeJson.setAttributes(billMap);
				aTreeJsonList.add(aTreeJson);
			}
			topTreeJson.setChildren(aTreeJsonList);
		}
		treeJsonList.add(topTreeJson);

		return treeJsonList;
	}
	
	/**
	 * @Description:导出汇总列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	@Override
	public FileUtil exportNurseBillHzVo(List<NurseBillHzVo> list, FileUtil fUtil) {
		for (NurseBillHzVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getDrugName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getSpecs()) + ",";
			record += model.getApplySum() + ",";
			record += CommonStringUtils.trimToEmpty(model.getMinUnit()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getApplyDept()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugDept()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getBillClassName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getValidState()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugBasicPinYin()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugBasicWb()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getStates()) + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

	/**
	 * @Description:导出明细列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	 **/
	@Override
	public FileUtil exportNurseBillMxVo(List<NurseBillMxVo> list, FileUtil fUtil) {
		for (NurseBillMxVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getBedNo()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getPatientName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getMedicalRecordID()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getSpecs()) + ",";
			record += model.getDoseOnce() + ",";
			record += CommonStringUtils.trimToEmpty(model.getDoseUnit()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDfqFreq()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getUseName()) + ",";
			record += model.getApplyNum() + ",";
			record += CommonStringUtils.trimToEmpty(model.getMinUnit()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getApplyDept()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugDept()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getBillClassName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getValidState()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugBasicPinYin()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugBasicWb()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getStates()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getDrugedBill()) + ",";
			record += model.getPrintDate() + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

	@Override
	public List<NurseBillHzVo> getAllNurseBillHz(String deptCode,String billClassCode,String applyState,String drugName,String etime,String stime) throws Exception {
		return nurseBillDAO.getAllNurseBillHz(deptCode, billClassCode, stime,etime, applyState, drugName);
	}

	@Override
	public List<NurseBillMxVo> getAllNurseBillMx(String deptCode,String billClassCode,String applyState,String drugName,String etime,String stime) throws Exception {
		return nurseBillDAO.getAllNurseBillMx(deptCode, billClassCode, stime,etime, applyState, drugName);
	}

	@Override
	public StatVo findMaxMin() throws Exception {
		return nurseBillDAO.findMaxMin();
	}

	@Override
	public Map<String, String> queryPackUnitMap() throws Exception {
		List<Object []> list = nurseBillDAO.queryPackUnitMap();
		Map<String,String> map=new HashMap<String,String>();
		for(Object[] arr:list){
			map.put(arr[0].toString(), arr[1].toString());
		}
		return map;
	}
}
