package cn.honry.inpatient.surety.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientSurety;
import cn.honry.base.service.BaseService;

/**  
 *  
 * @className：SuretyService
 * @Description： 担保金Service
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface SuretyService extends BaseService<InpatientSurety>{

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
	
	/**
	 *
	 * @Description：保存担保金
	 * @Author：aizhonghua
	 * @CreateDate：2016年4月11日 下午4:05:36 
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0
	 * @param surety 担保金对象
	 *
	 */
	String save(InpatientSurety surety);

	void removeSuretyByids(String[] ids);
	
}
