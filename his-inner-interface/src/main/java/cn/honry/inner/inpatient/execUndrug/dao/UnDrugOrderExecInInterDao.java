package cn.honry.inner.inpatient.execUndrug.dao;


import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.dao.EntityDao;

public interface UnDrugOrderExecInInterDao extends EntityDao<InpatientExecundrug> {

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
	List<InpatientExecundrug> getPageInfo(String hql, String page, String rows);

	/**  
	 *  
	 * @Description：  分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	int getTotalInfo(String hql);

	/**  
	 *  
	 * @Description：  查询医嘱执行非药品信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-9 下午16:56:31   
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-9 下午16:56:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<InpatientExecundrugNow> getList(String id);
	/**
	 * 根据患者住院流水号获取患者非药品执行记录
	 * @author  liujl
	 * @createDate： 2016年5月9日 下午17:325:07 
	 * @modifier liujl
	 * @modifyDate：2016年5月9日 下午17:325:07 
	 * @param：  inpatientNo 住院流水号
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<InpatientExecundrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime);

	/**
	 * 获取患者一段时间内应该收费但是未收的药品医嘱执行记录
	 * @param order 医嘱对象
	 * @param stopTime 医嘱停止时间
	 * @return
	 */
	List<InpatientExecundrug> getListNoFeeExec(InpatientOrder order, Date stopTime);

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
	List<InpatientExecundrug> getListNoExecByExeId(String exeId);
	
}
