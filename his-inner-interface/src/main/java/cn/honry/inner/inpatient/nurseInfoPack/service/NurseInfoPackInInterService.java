package cn.honry.inner.inpatient.nurseInfoPack.service;

import java.util.List;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.VidExecdrugBedname;
import cn.honry.base.bean.model.VidExecdrugBednameKs;
import cn.honry.base.bean.model.VidExecundrugBedname;
import cn.honry.base.bean.model.VidExecundrugBednameKs;
import cn.honry.base.bean.model.VidInfoOrder;
import cn.honry.base.service.BaseService;

public interface NurseInfoPackInInterService extends BaseService<VidInfoOrder> {

	
	VidInfoOrder getInfoID(String id);
	

	/**
	 * 带条件分页查询显示医嘱+住院登记表
	 * @author zhenglin  2015-12-25
	 * @param vidInfoOrder
	 * @param rows
	 * @param page
	 * @return
	 */
	List<InpatientOrder> viewInfo(InpatientOrder vidInfoOrder,String rows,String page);
	
	/**
	 * 获得总数
	 * @author zhenglin 2015-12-25
	 * @param vidInfoOrder
	 * @return
	 */
	int getTotal(InpatientOrder vidInfoOrder);
	
	
	
	/**
	 * 带条件分页查询显示医嘱+住院登记表
	 * @author zhenglin  2015-12-25
	 * @param vidInfoOrder
	 * @param rows
	 * @param page
	 * @return
	 */
	List<InpatientOrder> viewInfos(InpatientOrder vidInfoOrder,String rows,String page);
	
	/**
	 * 获得总数
	 * @author zhenglin 2015-12-25
	 * @param vidInfoOrder
	 * @return
	 */
	int getTotals(InpatientOrder vidInfoOrder);
	
	
	/**
	 * 根据系统参数名称获得数据
	 * @param name
	 * @return
	 */
	HospitalParameter getByHosInfoByName(String name);
	
	
	/**
	 * 根据主键获得视图信息
	 * @param id
	 * @return
	 */
	VidInfoOrder getOrderByOrderId(String id);
	/**
	 * 医嘱类别下拉框
	 */
	List<InpatientKind> getCombobox();
	
	/**
	 * 分页查询药嘱执行档
	 * @param execdrug
	 * @param page
	 * @param rows
	 * @return
	 */
	List<VidExecdrugBedname> execDruglist(VidExecdrugBedname execdrug,String page,String rows,String deptId);
	/**
	 * 统计药嘱执行档
	 */
	int execDrugTotal(VidExecdrugBedname execdrug,String deptId);
	/**
	 * 分页查询药嘱执行档
	 * @param execdrug
	 * @param page
	 * @param rows
	 * @return
	 */
	List<VidExecdrugBednameKs> execDruglistks(VidExecdrugBednameKs execdrug,String page,String rows,String deptId);
	/**
	 * 统计药嘱执行档
	 */
	int execDrugTotalks(VidExecdrugBednameKs execdrug,String deptId);
	
	/**
	 * 分页查询非药品执行档
	 */
	List<VidExecundrugBedname> execUnDrugList(VidExecundrugBedname execundrug,String page,String rows,String deptId);
	/**
	 * 统计非药品执行档
	 */
	int execUnDrugTotal(VidExecundrugBedname execundrug,String deptId);
	/**
	 * 分页查询非药品执行档ks
	 */
	List<VidExecundrugBednameKs> execUnDrugListks(VidExecundrugBednameKs execundrug,String page,String rows,String deptId);
	/**
	 * 统计非药品执行档ks
	 */
	int execUnDrugTotalks(VidExecundrugBednameKs execundrug,String deptId);
	/**
	 * 渲染人员
	 */
	List<InpatientInfo> infos();
	/**
	 * 获取药品类别 zhenglin 2016-04-08
	 */
//	CodeSystemtype getTypeId();
}
