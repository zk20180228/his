package cn.honry.mobile.menuIcon.service;

import java.util.List;

import cn.honry.base.bean.model.MenuIcon;
import cn.honry.base.service.BaseService;


public interface MenuIconService  extends BaseService<MenuIcon> {

	/**  
	 * 
	 * 栏目图标列表页面展示
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
	 * @return: List<MenuIcon> 
	 *
	 */
	List<MenuIcon> getMenuIconList(String rows, String page, String queryName) throws Exception;

	/**  
	 * 
	 * 栏目图标列表页面总条数
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
	Integer getMenuIconCount(String queryName) throws Exception;

	/**  
	 * 
	 * 保存栏目图标
	 * @Author: zxl
	 * @CreateDate: 2017年9月23日 上午11:03:59 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月23日 上午11:03:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:menuIcon
	 * @throws: Exception
	 * @return: 
	 *
	 */
	void saveMenuIcon(MenuIcon menuIcon)  throws Exception;

	/**  
	 * 
	 * 删除栏目图标
	 * @Author: zxl
	 * @CreateDate: 2017年9月23日 上午11:03:59 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月23日 上午11:03:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:menuIcon
	 * @throws: Exception
	 * @return: 
	 *
	 */
	void delMenuIcon(String ids) throws Exception;

	/**  
	 * 
	 * 校验图片名称是否存在
	 * @Author: zxl
	 * @CreateDate: 2017年9月23日 上午11:03:59 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月23日 上午11:03:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:menuIcon
	 * @throws: Exception
	 * @return: 
	 *
	 */
	String ckeckName(String name,String id) throws Exception;

}
