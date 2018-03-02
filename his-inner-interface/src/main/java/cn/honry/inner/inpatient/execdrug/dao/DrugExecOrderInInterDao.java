package cn.honry.inner.inpatient.execdrug.dao;


import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;

public interface DrugExecOrderInInterDao extends EntityDao<InpatientExecdrug> {

	/**  
	 *  
	 * @Description：  分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientExecdrug> getPageInfo(String hql, String page, String rows);


	/**  
	 *  
	 * @Description：  分页查询 - 统计数量
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotalInfo(String hql);

	/**  
	 *  
	 * @Description：  查询医嘱执行药品信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientExecdrugNow> getList(String id);
	
	/**
	 * 保存医嘱执行档
	 * @author  zl
	 * @createDate： 2016年4月15日 上午9:32:07 
	 * @modifier zl
	 * @modifyDate：2016年4月15日 上午9:32:07
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	void saveExDrugList(List<InpatientExecdrug> execdrug);

	/**
	 * 根据患者住院流水号获取患者药品执行记录
	 * @author  liujl
	 * @createDate： 2016年5月9日 下午17:325:07 
	 * @modifier liujl
	 * @modifyDate：2016年5月9日 下午17:325:07 
	 * @param：  inpatientNo 住院流水号
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientExecdrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime);

	/**
	 * 获取患者一段时间内应该收费但是未收的药品医嘱执行记录
	 * @param order 医嘱对象
	 * @param stopTime 医嘱停止时间
	 * @return
	 */
	List<InpatientExecdrug> getListNoFeeExec(InpatientOrder order, Date stopTime);

	/**
	 * 根据执行信息id获得药品执行记录
	 * @author  aizhonghua
	 * @createDate： 2016年5月25日 下午15:35:07 
	 * @modifier aizhonghua
	 * @modifyDate：2016年5月25日 下午15:35:07 
	 * @param：  exeId 住院流水号
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientExecdrug> getListNoExecByExeId(String exeId);
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
