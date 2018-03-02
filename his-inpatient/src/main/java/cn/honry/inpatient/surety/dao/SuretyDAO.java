package cn.honry.inpatient.surety.dao;

import java.util.List;

import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：SuretyDAO
 * @Description：  担保金DAO
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface SuretyDAO extends EntityDao<InpatientSurety>{

	/**
	 *
	 * @Description：查询担保金信息 - 分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param：inpatientNo住院号
	 * @return 总条数
	 */
	int getSuretyTotal(String inpatientNo);

	/**
	 *
	 * @Description：查询担保金信息 - 分页查询 - 获得信息
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月12日 上午9:47:41 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param1：inpatientNo住院号
	 * @param2：page当前页数
	 * @param3：rows分页数量
	 * @return：分页信息
	 * 
	 */
	List<InpatientSurety> getSuretyPage(String inpatientNo, String page,String rows);

	void updateInpatientSurety(InpatientSurety ins);

}
