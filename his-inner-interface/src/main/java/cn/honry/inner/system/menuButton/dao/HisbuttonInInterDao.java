package cn.honry.inner.system.menuButton.dao;

import java.util.List;

import cn.honry.base.bean.model.SysMenuButton;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.system.menuButton.vo.MenuButInInterVo;
@SuppressWarnings({"all"})
public interface HisbuttonInInterDao extends EntityDao<SysMenuButton>{

	/**  
	 *  
	 * @Description：    获得该栏目拥有的按钮
	 * @Author：zhangjin
	 * @CreateDate：2016-7-1  
	 * @Modifier：
	 * @ModifyDate： 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysMenuButton> getListByMenuId(String id);

	/**  
	 *  
	 * 批量获取栏目功能
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-23 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-23 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<MenuButInInterVo> getMenuButtonByList(List<String> idList);


}
