package cn.honry.inner.inpatient.nurseInfoPack.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.VidExecdrugBedname;
import cn.honry.base.bean.model.VidExecdrugBednameKs;
import cn.honry.base.bean.model.VidExecundrugBedname;
import cn.honry.base.bean.model.VidExecundrugBednameKs;
import cn.honry.base.bean.model.VidInfoOrder;
import cn.honry.inner.inpatient.nurseInfoPack.dao.NurseInfoPackInInterDao;
import cn.honry.inner.inpatient.nurseInfoPack.service.NurseInfoPackInInterService;


@Service("nurseInfoPackInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class NurseInfoPackInInterServiceImpl implements NurseInfoPackInInterService {
	@Autowired
	@Qualifier(value = "nurseInfoPackInInterDao")
	private NurseInfoPackInInterDao nurseInfoPackInInterDao;
	
	
	public VidInfoOrder getInfoID(String id) {
		return nurseInfoPackInInterDao.getIdbyORder(id);
	}
	
	private VidInfoOrder order;


	public void removeUnused(String id) {
	}

	public VidInfoOrder get(String id) {
		return null;
	}

	public void saveOrUpdate(VidInfoOrder entity) {
		
	}
	
	
	/**
	 * 带条件分页查询临时医嘱
	 */
	public List<InpatientOrder> viewInfos(InpatientOrder vidInfoOrder, String rows,
			String page) {
		return nurseInfoPackInInterDao.viewInfos(vidInfoOrder, rows, page);
	}


	/**
	 * 查询临时医嘱的总数
	 */
	public int getTotals(InpatientOrder vidInfoOrder) {
		return nurseInfoPackInInterDao.getTotals(vidInfoOrder);
	}

	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	

	/**
	 * 带条件分页查询长期医嘱
	 */
	public List<InpatientOrder> viewInfo(InpatientOrder vidInfoOrder, String rows,
			String page) {
		return nurseInfoPackInInterDao.viewInfo(vidInfoOrder, rows, page);
	}


	/**
	 * 获得长期医嘱的总数
	 */
	public int getTotal(InpatientOrder vidInfoOrder) {
		return nurseInfoPackInInterDao.getTotal(vidInfoOrder);
	}


	//根据系统参数名称获得数据
	public HospitalParameter getByHosInfoByName(String name) {
		return nurseInfoPackInInterDao.getByHosInfoByName(name);
	}

	public VidInfoOrder getOrderByOrderId(String id) {
		return nurseInfoPackInInterDao.getOrderByOrderId(id);
	}

	public List<InpatientKind> getCombobox() {
		return nurseInfoPackInInterDao.getCombobox();
	}

	/**
	 * 分页查询药嘱执行档
	 */
	public List<VidExecdrugBedname> execDruglist(VidExecdrugBedname execdrug,String page, String rows,String deptId) {
		return nurseInfoPackInInterDao.execDruglist(execdrug, page, rows,deptId);
	}
    /**
     * 统计药嘱执行档数据
     */
	public int execDrugTotal(VidExecdrugBedname execdrug,String deptId) {
		return nurseInfoPackInInterDao.execDrugTotal(execdrug,deptId);
	}
	/**
	 * 分页查询药嘱执行档ks
	 */
	public List<VidExecdrugBednameKs> execDruglistks(VidExecdrugBednameKs execdrug,String page, String rows,String deptId) {
		return nurseInfoPackInInterDao.execDruglistks(execdrug, page, rows,deptId);
	}
	/**
	 * 统计药嘱执行档数据ks
	 */
	public int execDrugTotalks(VidExecdrugBednameKs execdrug,String deptId) {
		return nurseInfoPackInInterDao.execDrugTotalks(execdrug,deptId);
	}
    /**
     * 分页查询非药品执行档
     */
	public List<VidExecundrugBedname> execUnDrugList(VidExecundrugBedname execundrug, String page, String rows,String deptId) {
		return nurseInfoPackInInterDao.execUnDrugList(execundrug, page, rows,deptId);
	}
	 /**
	  * 统计非要执行档
	  */
	public int execUnDrugTotal(VidExecundrugBedname execundrug,String deptId) {
		return nurseInfoPackInInterDao.execUnDrugTotal(execundrug,deptId);
	}
	/**
	 * 分页查询非药品执行档
	 */
	public List<VidExecundrugBednameKs> execUnDrugListks(VidExecundrugBednameKs execundrug, String page, String rows,String deptId) {
		return nurseInfoPackInInterDao.execUnDrugListks(execundrug, page, rows,deptId);
	}
	/**
	 * 统计非要执行档
	 */
	public int execUnDrugTotalks(VidExecundrugBednameKs execundrug,String deptId) {
		return nurseInfoPackInInterDao.execUnDrugTotalks(execundrug,deptId);
	}
	/**
	 * 渲染患者
	 */
	public List<InpatientInfo> infos() {
		return nurseInfoPackInInterDao.infos();
	}
	
}
