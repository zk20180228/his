package cn.honry.inner.system.menu.dao;

import java.util.List;

import cn.honry.base.bean.model.SysMenu;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.system.menu.vo.MenuInInterVo;
import cn.honry.inner.system.menu.vo.ParentMenuVo;

/**  
 *  
 * @className：MenuInInterDAO
 * @Description：  栏目接口
 * @Author：aizhonghua
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface MenuInInterDAO extends EntityDao<SysMenu>{

	/**  
	 *  
	 * 获得移动端栏目
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-23 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-23 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<MenuInInterVo> queryMenuMobList();
	
	/**  
	 * 
	 * <p>通过栏目别名获得栏目实体</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月15日 下午2:48:38 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月15日 下午2:48:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	SysMenu getByMenuAlias(String menuAlias);
	/**  
	 * 
	 * <p>查询移动端父级栏目接口</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月15日 下午5:12:57 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月15日 下午5:12:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<ParentMenuVo> queryMobileParentMenu();

	/**  
	 * 
	 * <p>查询移动端栏目</p>
	 * @Author dutianliang
	 * @CreateDate 2018年2月10日 下午1:37:52
	 * @Modifier dutianliang
	 * @ModifyDate 2018年2月10日 下午1:37:52
	 * @ModifyRmk 
	 * @version V1.0
	 * @return
	 * @return List<SysMenu> 返回值类型
	 *
	 */
	List<SysMenu> queryAllMobile();
}
