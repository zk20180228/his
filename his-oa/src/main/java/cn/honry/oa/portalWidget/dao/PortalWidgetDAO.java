package cn.honry.oa.portalWidget.dao;

import java.util.List;

import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface PortalWidgetDAO extends EntityDao<OaPortalWidget>{

	/**  
	 * 
	 * 获取记录条数
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:49:18 
	 * @version: V1.0
	 * @param entity 组件实体(传入的条件)
	 * @return:
	 * @throws:
	 * @return: int 返回值类型
	 *
	 */
	int getTotal(OaPortalWidget entity);

	/**  
	 * 
	 * 获取列表信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:47:54 
	 * @version: V1.0
	 * @param entity 组件实体
	 * @param page 页数
	 * @param rows 每页行数
	 * @return:
	 * @throws:
	 * @return: List 返回值类型
	 *
	 */
	List query(OaPortalWidget entity,String page,String rows);
	
	/**  
	 * 
	 * 逻辑删除业务信息
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:46:23 
	 * @version: V1.0
	 * @param ids
	 * @return:
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	void deleteLogicById(String id);

	/**  
	 * 
	 * 通过ID查询组件实体
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:45:23 
	 * @version: V1.0
	 * @param id
	 * @return:
	 * @throws:
	 * @return: OaPortalWidget 返回值类型
	 *
	 */
	OaPortalWidget  getById(String id);

	/**  
	 * 
	 * 取出当前数据中最大的ID
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:42:24 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: String 返回值类型
	 *
	 */
	String getMaxId();

	/**  
	 * 
	 * 如果组件状态发生了改变,则要修改个人组件表中的该组件状态
	 * @Author: zpty
	 * @CreateDate: 2017年7月21日 下午3:16:03 
	 * @version: V1.0
	 * @param id 组件ID
	 * @param status: 组件状态
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void updateStatus(String id, Integer status);
}
