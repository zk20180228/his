package cn.honry.statistics.outExecution.service.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientDrugbilldetail;
import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.leaveOrder.vo.InpatientExecDrugVo;
import cn.honry.statistics.leaveOrder.vo.InpatientExecUnDrugVo;
import cn.honry.statistics.outExecution.dao.OutExecutionDAO;
import cn.honry.statistics.outExecution.service.OutExecutionService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.SessionUtils;
import cn.honry.utils.TreeJson;

@Service("outExecutionService")
@Transactional
@SuppressWarnings({ "all" })
public class OutExecutionServiceImpl implements OutExecutionService{
	private final String[] tableInfo={"T_INPATIENT_INFO_NOW","T_INPATIENT_INFO"};
	private final String[] tableIExe={"T_INPATIENT_EXECDRUG_NOW","T_INPATIENT_EXECDRUG"};
	@Autowired
	@Qualifier(value = "outExecutionDAO")
	private OutExecutionDAO outExecutionDAO;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	public void setParameterInnerDAO(ParameterInnerDAO parameterInnerDAO) {
		this.parameterInnerDAO = parameterInnerDAO;
	}

	@Override
	public InpatientOrderNow get(String arg0) {
		return null;
	}


	@Override
	public void removeUnused(String arg0) {
		
	}


	@Override
	public void saveOrUpdate(InpatientOrderNow arg0) {
		
	}


	/**查询医嘱流水号   查询出对应数据条数来判断
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	@Override
	public Map<String, Object> queryLSHList(String queryBlh,String queryLsh, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String, Object>();
		//先查询住院信息 新老表   查询已出院患者的住院流水号条数
		List<InpatientInfoNow> infoList=outExecutionDAO.queryInfoNows(queryBlh,queryLsh, startTime, endTime);
		//如果流水号数据为多条    返回页面  弹窗让用户选择流水号
		if(infoList.size()>1){
			map.put("type", 1);
			map.put("rows", infoList);
		}else if(infoList.size()==0 || infoList==null){
			map.put("type", 0);
		}else{
			map.put("type", 2);
		}
		
		return map;
	}
	
	/**
	 * GH  根据弹窗选择的流水号查询 医嘱执行单药品数据
	 * 2017年2月23日11:40:58
	 */
	
	@Override
	public Map<String, Object> queryDrugLists(String queryLsh, String page,String rows,String startTime,String endTime ) {
		int total=outExecutionDAO.queryDrugTotal(queryLsh, startTime, endTime);
		List<InpatientExecDrugVo>list= outExecutionDAO.queryDrugList(queryLsh,startTime,endTime,page,rows);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		return map;
	}
	
	
	
	/**
	 * GH  根据弹窗选择的流水号查询 医嘱执行单 非 药品数据
	 * 2017年2月23日11:40:58
	 */
	
	@Override
	public Map<String, Object>  queryUnDrugList(String queryLsh,String page,String rows,String startTime,String endTime ) {
		int total=outExecutionDAO.queryDrugTotal(queryLsh, startTime, endTime);
		List<InpatientExecUnDrugVo>list= outExecutionDAO.queryUnDrugList(queryLsh,startTime,endTime,page,rows);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		return map;
	}


	@Override
	public List<InpatientExecbill> queryDocAdvExe(String ids, String billName,SysDepartment dept) {
		return outExecutionDAO.queryDocAdvExe(ids, billName,dept);
	}


	@Override
	public List queryDeptList() {
		List list =outExecutionDAO.queryDeptList();
		return list;
	}


	@Override
	public Map queryInpatientByDept(String deptCode) {
		Map map=outExecutionDAO.queryInpatientByDept(deptCode);
		return map;
	}
	
	
	
	//加载tree
	@Override
	public String treeInpatient(String id, String deptCode,String startTime,String endTime) {
		List<String> tnL=returnINFOTable(startTime,endTime,tableInfo);//分区表
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if (StringUtils.isBlank(id)) {// 根节点
			Map<Integer, String> rootMap = new HashMap<Integer, String>();
			rootMap.put(0, "病人列表");
			TreeJson rTreeJson = new TreeJson();
			Map<String, String> rAttMap = new HashMap<String, String>();
			rTreeJson.setId("0");
			rTreeJson.setText("病人列表");
			rTreeJson.setState("closed");
			rAttMap.put("pid", "root");
			rTreeJson.setAttributes(rAttMap);
			// 封装二级节点
			List clist = new ArrayList<>();
			// 根据科室查询克科室下的病人 按病历号去重后
			List list2 = outExecutionDAO.queryOneList(tnL,null,deptCode,startTime,endTime);
			Iterator it2 = list2.iterator();
			while (it2.hasNext()) {
				Object[] obj = (Object[]) it2.next();
				TreeJson tTreeJson = new TreeJson();
				Map<String, String> tAttMap = new HashMap<String, String>();
				tTreeJson.setId(obj[2].toString());
				tTreeJson.setState("closed");
				tTreeJson.setText(obj[1].toString()+"("+obj[2].toString()+")");
				tAttMap.put("pid", "0");
				tTreeJson.setAttributes(tAttMap);
				clist.add(tTreeJson);
			}
			rTreeJson.setChildren(clist);
			if (rTreeJson.getChildren() == null
					|| rTreeJson.getChildren().size() == 0) {
				rTreeJson.setState("open");
			}
			treeJsonList.add(rTreeJson);
		} else {
			List list = outExecutionDAO.queryOneList(tnL,id, deptCode,startTime,endTime);
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				TreeJson cTreeJson = new TreeJson();
				Map<String, String> cAttMap = new HashMap<String, String>();
				Object[] obj = (Object[]) iterator.next();
				cTreeJson.setId(obj[0].toString());
				cTreeJson.setText(obj[1].toString() + "(" + obj[0].toString()
						+ ")");
				cTreeJson.setState("open");
				cAttMap.put("pid", id);
				cTreeJson.setAttributes(cAttMap);
				treeJsonList.add(cTreeJson);
			}
		}
		return JSONUtils.toJson(treeJsonList);
	}


	@Override
	public List<InpatientExecbill> queryDocAdvExe(String ids, String billName,
			String deptCode) {
		return outExecutionDAO.queryDocAdvExe(ids, billName,deptCode);
	}


	@Override
	public List<InpatientExecundrugNow> queryExecundrugpage(String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String inpatientNo) {
	   return outExecutionDAO.queryExecundrugpage(billNo, validFlag, drugedFlag, beginDate, endDate, page, rows, inpatientNo);
	}


	@Override
	public int queryExecundrugToatl(String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String inpatientNo) {
		return outExecutionDAO.queryExecundrugToatl(billNo, validFlag, drugedFlag, beginDate, endDate, inpatientNo);
	}


	@Override
	public List queryExecdrugpage(String billNo,
			String validFlag, String drugedFlag, String beginDate,
			String endDate, String page, String rows, String inpatientNo) {
		return outExecutionDAO.queryExecdrugpage(returnINFOTable(beginDate,endDate,tableIExe),billNo, validFlag, drugedFlag, beginDate, endDate, page, rows, inpatientNo);
	}


	@Override
	public int queryExecdrugToatl(String billNo, String validFlag,
			String drugedFlag, String beginDate, String endDate,
			String inpatientNo) {
		return outExecutionDAO.queryExecdrugToatl(returnINFOTable(beginDate,endDate,tableIExe),billNo, validFlag, drugedFlag, beginDate, endDate, inpatientNo);
	}
	public List<String> returnINFOTable(String startTime,String endTime,String[] tableArr){
		List<String> tnL = null;
		Date sTime = DateUtils.parseDateY_M_D(startTime);
		Date eTime = DateUtils.parseDateY_M_D(endTime);
		try {
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
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,tableArr[1],startTime,endTime);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),startTime);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,tableArr[1],yNum+1);
					tnL.add(0,tableArr[0]);
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add(tableArr[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tnL = new ArrayList<String>();
		}
		return tnL;
	}
	
	@Override
	public Integer queryDrugBillDetail(String billNo) {
		return outExecutionDAO.queryDrugBillDetail(billNo);
	}
	
}

