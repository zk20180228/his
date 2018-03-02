package cn.honry.statistics.drug.nurseDrugDispens.service.impl;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.inpatient.info.dao.InpatientInfoInInterDAO;
import cn.honry.inner.inpatient.info.vo.InfoInInterVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.drug.nurseDrugDispens.dao.NurseDrugDispensDAO;
import cn.honry.statistics.drug.nurseDrugDispens.service.NurseDrugDispensService;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensDetailVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensSumVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HibernateCascade;
import cn.honry.utils.HisParameters;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("nurseDrugDispensService")
@Transactional
@SuppressWarnings({ "all" })
public class NurseDrugDispensServiceImpl implements NurseDrugDispensService{
	@Autowired
	@Qualifier(value = "nurseDrugDispensDAO")
	private NurseDrugDispensDAO nurseDrugDispensDAO;
	@Autowired
	private InpatientInfoInInterDAO inpatientInfoDAO;
	public void setInpatientInfoDAO(InpatientInfoInInterDAO inpatientInfoDAO) {
		this.inpatientInfoDAO = inpatientInfoDAO;
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
	
	@Override
	public InpatientInfo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientInfo arg0) {
		
	}

	@Override
	public List<DrugDispensSumVo> queryDrugDispensSum(InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,String inpatientNoSerc, String flg, String type,String page,String rows) throws Exception {
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
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return nurseDrugDispensDAO.queryDrugDispensSum(tnL,inpatientInfo, startTime, endTime,drugName,inpatientNoSerc,type,page,rows);
	}

	@Override
	public List<DrugDispensDetailVo> queryDrugDispensDetail(InpatientInfoNow inpatientInfo,String startTime,String endTime,String drugName,String inpatientNoSerc, String flg, String type,String page,String rows) throws Exception {
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
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		return nurseDrugDispensDAO.queryDrugDispensDetail(tnL,inpatientInfo, startTime, endTime, drugName,inpatientNoSerc,type,page,rows);
	}
	
	
	@Override
	public List<TreeJson> treeNurseCharge(String deptId, String id,
			String type, InpatientInfoNow inpatientInfo, String a, String startTime,
			String endTime) throws Exception {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<TreeJson> treeJson = new ArrayList<TreeJson>();
		List<InfoInInterVo> listDept=null;
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginNursingStationShiroSession();
		if(department==null||department.equals("")){
			listDept=null;
		}else{
			listDept = nurseDrugDispensDAO.treeNurseCharge(department.getDeptCode(),type,inpatientInfo,a,startTime,endTime);
		}
		//根节点
		TreeJson gTreeJson = new TreeJson();
		gTreeJson.setId("1");
		gTreeJson.setText("本区患者");
		gTreeJson.setIconCls("icon-house");
		Map<String, String> map = new HashMap<String, String>();
		List<BusinessContractunit> list = inpatientInfoDAO.queryContractunit();
		if(list!=null&&list.size()>0){
			for(BusinessContractunit contractunit : list){
				map.put(contractunit.getEncode(), contractunit.getName());
			}
		}
		Map<String,String> gAttMap = new HashMap<String, String>();
		gTreeJson.setAttributes(gAttMap);
		TreeJson fTreeJson = null;
		Map<String,String> fAttMap = null;
		if(listDept!=null&&listDept.size()>0){
			for(InfoInInterVo infonfo : listDept){
				//二级节点(患者)
				fTreeJson = new TreeJson();
				fTreeJson.setId(infonfo.getInpatientNo());
				//渲染合同单位
				String pactCode=map.get(infonfo.getPactCode());
				if(StringUtils.isBlank(pactCode)){
					pactCode="自费";
				}
				fTreeJson.setText("【"+infonfo.getBedName()+"】【"+infonfo.getMedicalrecordId()+"】"+infonfo.getPatientName()+"【"+pactCode+"】");
				if("3".equals(infonfo.getReportSex())||"1".equals(infonfo.getReportSex())){
					fTreeJson.setIconCls("icon-user_b");
				}	
				else{
					fTreeJson.setIconCls("icon-user_female");
				}
				fTreeJson.setState("open");
				fAttMap = new HashMap<String, String>();
				fAttMap.put("pid", infonfo.getInState());
				fAttMap.put("no",infonfo.getInpatientNo());
				fTreeJson.setAttributes(fAttMap);
				treeJson.add(fTreeJson);
			}
			gTreeJson.setChildren(treeJson);
		}
		treeJsonList.add(gTreeJson);
		return treeJsonList;
		
	}

	@Override
	public List<InpatientInfoNow> findByinpatientNo(String inpatientNo) throws Exception {
		return nurseDrugDispensDAO.findByinpatientNo(inpatientNo);
	}

	@Override
	public Map<String, String> queryBillNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<DrugBillclass> list =nurseDrugDispensDAO.queryBillNameList() ;
		if(list!=null&&list.size()>0){
			for(DrugBillclass emp : list){
				map.put(emp.getBillclassCode(),emp.getBillclassName());
			}
		}
		return map;
	}

	@Override
	public int queryDrugDispensSumTotal(InpatientInfoNow inpatientInfo,
			String startTime, String endTime, String drugName,
			String inpatientNoSerc, String a, String type) {
		String redKey ="JZCCDMXRBHZ"+ startTime+"_"+endTime+"_"+drugName+"_"+type+"_"+a+"_"+inpatientNoSerc;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
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
				} catch (Exception e) {
					e.printStackTrace();
					tnL = null;
				}
				totalNum =nurseDrugDispensDAO.queryDrugDispensSumTotal(tnL,inpatientInfo, startTime, endTime,drugName,inpatientNoSerc,type);
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
	public int queryDrugDispensDetailTotal(InpatientInfoNow inpatientInfo,
			String startTime, String endTime, String drugName,
			String inpatientNoSerc, String a, String type) {
		String redKey ="JZCCDMXRBMX"+ startTime+"_"+endTime+"_"+drugName+"_"+type+"_"+a+"_"+inpatientNoSerc;
		Integer totalNum = (Integer) redisUtil.get(redKey);
		if(totalNum==null){
		List<String> tnL = null;
		try {
			//1.时间转换
			Date sTime = DateUtils.parseDateY_M_D(startTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			//2.获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if(dateNum.equals("1")){
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
		} catch (Exception e) {
			e.printStackTrace();
			tnL = null;
		}
		totalNum=nurseDrugDispensDAO.queryDrugDispensDetailTotal(tnL,inpatientInfo, startTime, endTime, drugName,inpatientNoSerc,type);
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
	
}
