package cn.honry.inner.inpatient.execdrug.service;


import java.util.List;

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;

public interface DrugExecOrderInInterService extends BaseService<InpatientExecdrug> {

	List<InpatientExecdrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime);
	/**
	 * 根据药品用法  类别  医嘱类别查询药瞩执行记录
	 * @param userCode
	 * @param typtCode
	 * @param drugType
	 * @return
	 */
	List<InpatientExecdrug> getListExecdrug(String userCode,String typtCode,String drugType);
	/**
	 * 根据药品 用法  类别  医嘱类别查询非药瞩执行记录
	 * @param userCode
	 * @param typtCode
	 * @param drugType
	 * @return
	 */
	List<InpatientExecundrug> getListExecundrug(String userCode,String typtCode,String drugType);
	/**
	 * 科室
	 */
	SysDepartment SysDepartment(String id);
	/**
	 * 查询药瞩执行记录
	 */
	List<InpatientExecdrug> queryExecdrugpage(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String page,String rows,String inpatientNo);
	/**
	 * 查询药瞩执行记录总条数
	 */
	int queryExecdrugToatl(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo);
	/**
	 * 查询非药瞩执行记录
	 */
	List<InpatientExecundrug> queryExecundrugpage(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String page,String rows,String inpatientNo);
	/**
	 * 查询非药瞩执行记录总条数
	 */
	int queryExecundrugToatl(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo);
	/**
	 * 根据执行单流水号查询执行单名称
	 */
	InpatientExecbill queryBillName(String billNo);
}
