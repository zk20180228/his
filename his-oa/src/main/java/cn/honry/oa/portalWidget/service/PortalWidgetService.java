package cn.honry.oa.portalWidget.service;

import java.util.List;

import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.service.BaseService;

/**
 * OA系统首页组件维护
 * @author  zpty
 * @date 2017-7-15 15：40
 * @version 1.0
 */
public interface PortalWidgetService extends BaseService<OaPortalWidget>{
	/**  
	 * 
	 * 获取记录条数
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:03:03 
	 * @version: V1.0
	 * @param entiry 组件实体
	 * @return:
	 * @throws:
	 * @return: int 返回值类型
	 *
	 */
	int getTotal(OaPortalWidget entiry);
	/**  
	 * 
	 * 列表查询
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:03:35 
	 * @version: V1.0
	 * @param entity 组件实体
	 * @param page 页数
	 * @param rows 每页行数
	 * @return:
	 * @throws:
	 * @return: List<OaPortalWidget> 返回值类型
	 *
	 */
	List<OaPortalWidget> query(OaPortalWidget entity,String page,String rows);
	
	/**  
	 * 
	 * 删除
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:04:24 
	 * @Modifier: zpty
	 * @ModifyDate: 2017年7月17日 上午11:04:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param ids 待删除业务主键，可为多个
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void del(String ids);
	
}
