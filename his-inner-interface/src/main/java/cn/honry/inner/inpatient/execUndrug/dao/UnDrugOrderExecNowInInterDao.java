package cn.honry.inner.inpatient.execUndrug.dao;


import java.util.List;

import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.dao.EntityDao;

public interface UnDrugOrderExecNowInInterDao extends EntityDao<InpatientExecundrugNow> {

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
	List<InpatientExecundrugNow> getPageInfo(String hql, String page, String rows);

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
	 * 获得患者住院号和需要发送的执行记录id的集合
	 * @Author：aizhonghua
	 * @CreateDate：2016年5月25日 上午9:02:02 
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016年5月25日 上午9:02:02 
	 * @ModifyRmk：
	 * @version： 1.0：
	 * @param：orderIds执行记录id
	 * @param：patNoData患者住院流水号
	 *
	 */
	List<InpatientExecundrugNow> getUnDrugListByIdsAndPatNo(String orderIds,String patNoData);
}
