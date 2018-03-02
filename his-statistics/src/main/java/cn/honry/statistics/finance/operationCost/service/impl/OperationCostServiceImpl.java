package cn.honry.statistics.finance.operationCost.service.impl;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/***
 * 手术费用汇总service实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.finance.operationCost.dao.OperationCostDao;
import cn.honry.statistics.finance.operationCost.service.OperationCostService;
import cn.honry.statistics.finance.operationCost.vo.OperationCostVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
import cn.honry.utils.HisParameters;

/**
 * @author  tangfeishuai
 * @date 创建时间：2016年5月27日 下午5:53:48
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
@Service("operationCostService")
@Transactional
@SuppressWarnings({"all"})
public class OperationCostServiceImpl implements OperationCostService{
	
	@Autowired
	@Qualifier(value="operationCostDao")
	private OperationCostDao operationCostDao;

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	

	/**  
	 * 
	 * 手术费用汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationCostVo> queryOperationCost(String beganTime, String endTime, String inpatientNo,
			String execDept,String page,String rows, String identityCard) {
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beganTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",beganTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beganTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_now");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_now");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<OperationCostVo> opvoList=operationCostDao.OperationCostVo( beganTime, endTime, inpatientNo, execDept,page,rows,tnL,identityCard);
		return opvoList;
	}

	/**
	 * @Description:科室id与科室name map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String , String> 科室id与科室name map
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public Map<String, String> depMap() {
		HashMap<String , String> depMap=new HashMap<String ,String>();
		List<SysDepartment> depList=operationCostDao.depMentList();
		for (SysDepartment s : depList) {
			depMap.put(s.getDeptCode(), s.getDeptName());
		}
		return depMap;
	}
	/**
	 * @Description:根据条件查询手术费用汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public int getTotal(String beganTime, String endTime, String patientNo, String execDept) {
		
		return operationCostDao.getTotal(beganTime, endTime, patientNo, execDept);
	}

	/**
	 * @Description:根据条件查询所有手术费用汇总
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public List<OperationCostVo> allOperationCostVo(String beganTime, String endTime, String patientNo,
			String execDept) {
		return operationCostDao.allOperationCostVo(beganTime, endTime, patientNo, execDept);
				
	}
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	@Override
	public FileUtil export(List<OperationCostVo> list, FileUtil fUtil) {
		for (OperationCostVo model : list) {
			String record="";
			record = CommonStringUtils.trimToEmpty(model.getMedicalrecordId()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getExecDept()) + ",";
			record +=model.getTotCost() + ",";
			record += DateUtils.formatDateY_M_D_H_M_S(model.getFeeDate()) + ",";
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

	/**  
	 * 
	 * 获取科室
	 * @Author: zxl
	 * @CreateDate: 2017年7月5日 下午4:36:23 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月5日 下午4:36:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<MenuListVO> getDeptList() {
		return operationCostDao.getDeptList();
	}

	/**  
	 * 
	 * 手术费用汇总(分页总条数)
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:inpatientNo 病历号
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public int queryOperationCostTotal(String beganTime, String endTime,
			String inpatientNo, String execDept,String identityCard) {
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beganTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",beganTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beganTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_now");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_now");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return operationCostDao.queryOperationCostTotal( beganTime, endTime, inpatientNo, execDept,tnL,identityCard);
	}

	/**
	 * @Description:根据条件查询手术费用汇总(导出及打印)
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:beganTime 开始时间； endTime 结束时间； inpatientNo 住院流水号； execDept 执行科室 identityCard 身份证号
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @param  
	 */
	@Override
	public List<OperationCostVo> queryOperationCostOther(String beganTime,String endTime, String inpatientNo, String execDept,
			String identityCard) {
		List<String> tnL = null;
		try {
			Date sTime = DateUtils.parseDateY_M_D(beganTime);
			Date eTime = DateUtils.parseDateY_M_D(endTime);
			tnL = new ArrayList<String>();
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",beganTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),beganTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_FEEINFO",yNum+1);
					tnL.add(0,"T_INPATIENT_FEEINFO_now");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_FEEINFO_now");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<OperationCostVo> opvoList=operationCostDao.queryOperationCostOther( beganTime, endTime, inpatientNo, execDept,tnL,identityCard);
		return opvoList;
	}
}
