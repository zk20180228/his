package cn.honry.inner.inpatient.nurseInfoPack.dao;

import java.util.List;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.VidExecdrugBedname;
import cn.honry.base.bean.model.VidExecdrugBednameKs;
import cn.honry.base.bean.model.VidExecundrugBedname;
import cn.honry.base.bean.model.VidExecundrugBednameKs;
import cn.honry.base.bean.model.VidInfoOrder;
import cn.honry.base.dao.EntityDao;

public interface NurseInfoPackInInterDao extends EntityDao<VidInfoOrder> {

	
	VidInfoOrder getIdbyORder(String id);
	/**
	 * 带条件分页查询显示医嘱+住院登记表  临时医嘱
	 * @author zhenglin  2015-12-25
	 * @param vidInfoOrder
	 * @param rows
	 * @param page
	 * @return
	 */
	List<InpatientOrder> viewInfos(InpatientOrder vidInfoOrder,String rows,String page);
	/**
	 * 获得总数  临时医嘱
	 * @author zhenglin 2015-12-25
	 * @param vidInfoOrder
	 * @return
	 */
	int getTotals(InpatientOrder vidInfoOrder);
	// 查询全部
	List<VidInfoOrder> allList(VidInfoOrder vdd);
	//查询参数表根据name
	HospitalParameter getByHosInfoByName(String name);
	/**
	 * 获得长期医嘱全部数据
	 */
	List<InpatientInfo> getAlls(InpatientInfo info,String typeName,String deptId,String type);
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
	 * @Description：  患者树 .
	 */
	List<InpatientInfo> queryPatient(String deptId,String type);
	/**
	 * 
	 * @return
	 */
	List<SysDepartment> queryPharmacyByDept();
	/**
	 * 查询药品非药品 type="登录科室类型"
	 */
	List<InpatientOrder> allListByYao(InpatientOrder order,String begin,String dateend,String deptId,String type,String rows,String page);
	
	/**
	 * 得到分解数据总数
	 * @author  zl
	 * @createDate： 2016年4月25日 下午7:29:45 
	 * @modifier zl
	 * @modifyDate：2016年4月25日 下午7:29:45
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getResolveDataTotal(InpatientOrder order,String begin,String dateend,String deptId,String type);
	/**
	 * 根据主键获取视图信息
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
