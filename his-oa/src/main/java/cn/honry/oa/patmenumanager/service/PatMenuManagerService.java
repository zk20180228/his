package cn.honry.oa.patmenumanager.service;


import java.util.List;

import cn.honry.oa.patmenumanager.vo.MenuVo;


public interface PatMenuManagerService {

	/**
	 * <p>展示栏目树</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月17日 下午6:35:39 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月17日 下午6:35:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<TreeJson>
	 *
	 */
	MenuVo showTree(String pid) throws Exception;

	/**
	 * <p>根据id删除某个栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月17日 下午6:35:58 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月17日 下午6:35:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	void delMenu(MenuVo menu) throws Exception;

	/**
	 * 
	 * <p>通过id获取menu</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月18日 上午9:47:17 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月18日 上午9:47:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: MenuVo
	 *
	 */
	MenuVo getById(String id) throws Exception;
	/**
	 * 
	 * <p>通过id获取menu</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月18日 上午9:47:17 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月18日 上午9:47:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: MenuVo
	 *
	 */
	List<MenuVo> queryMenuByParentcode(String id);

	/**
	 * 
	 * <p>栏目保存的方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午3:58:48 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午3:58:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	void save(MenuVo menu) throws Exception;

	/**
	 * 
	 * <p>treeid为栏目code</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午3:58:48 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午3:58:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	MenuVo showTreeWithCode(String pid) throws Exception;

	/**
	 * 
	 * <p>根据code查询栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:27:58 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:27:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> queryMenuByCode(String code);

	/**
	 * 
	 * <p>根据id查询栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:27:58 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:27:58 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> queryMenuById(String id);

	MenuVo showTreeTwo(String string) throws Exception;
	/**
	 * 向上移动
	 * @param menu
	 */
	void moveUp(MenuVo menu);

	/**
	 * 向下移动
	 * @param menu
	 */
	void moveDown(MenuVo menu);

}
