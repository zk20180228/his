package cn.honry.mobile.offlinePush.service;

import java.util.List;

import cn.honry.base.bean.model.MOfofflinepush;

public interface OfflinePushService {

	/**  
	 * 
	 * 离线消息管理列表页面展示
	 * @Author: zxl
	 * @CreateDate: 2017年9月23日 上午11:03:59 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月23日 上午11:03:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:rows 行数
	 * @param:page 页数
	 * @param:queryName 用于模糊查询
	 * @throws:Exception
	 * @return: List<MOfofflinepush> 
	 *
	 */
	List<MOfofflinepush> getOfflineMesList(String rows, String page,
			String queryName);

	/**  
	 * 
	 * 离线消息管理总条数
	 * @Author: zxl
	 * @CreateDate: 2017年9月23日 上午11:03:59 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月23日 上午11:03:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:queryName 用于模糊查询
	 * @throws: Exception
	 * @return: Integer
	 *
	 */
	Integer getOfflineMesCount(String queryName);

	/**  
	 * 
	 * 根据id删除离线消息
	 * @Author: zxl
	 * @CreateDate: 2017年9月23日 上午11:03:59 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月23日 上午11:03:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:id
	 * @throws: Exception
	 * @return: 
	 *
	 */
	void delOfflineMes(String ids);

}
