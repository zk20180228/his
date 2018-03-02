package cn.honry.mobile.ofoffline.service;

import java.util.List;

import cn.honry.base.bean.model.MOfoffline;

public interface OfofflineService {

	/**  
	 * 
	 * 离线管理列表页面展示
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
	 * @return: List<MOfoffline> 
	 *
	 */
	List<MOfoffline> getOfofflineList(String rows, String page, String queryName);

	/**  
	 * 
	 * 离线管理总条数
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
	Integer getOfofflineCount(String queryName);

	/**  
	 * 
	 * 根据id删除信息
	 * @Author: zxl
	 * @CreateDate: 2017年9月23日 上午11:03:59 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月23日 上午11:03:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:ids
	 * @throws: Exception
	 * @return: Integer
	 *
	 */
	void delOfoffline(String ids);

}
