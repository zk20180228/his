package cn.honry.mobile.ofBatchPush.dao;

import java.util.List;

import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.base.dao.EntityDao;

public interface OfBatchPushDao extends EntityDao<OFBatchPush> {

	/**  
	 * 
	 * 广播管理列表页面展示
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
	 * @return: List<OFBatchPush> 
	 *
	 */
	List<OFBatchPush> getOfBatchPushList(String rows, String page,
			String queryName);

	/**  
	 * 
	 * 广播管理总条数
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
	Integer getOfBatchPushCount(String queryName);

	/**  
	 * 
	 * 根据id删除广播消息
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
	void delOfBatchPush(String ids);
}
