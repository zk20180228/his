package cn.honry.inner.system.menuButton.service;

import java.util.List;

import cn.honry.base.bean.model.SysMenuButton;
import cn.honry.base.service.BaseService;

@SuppressWarnings({ "all" })
public interface HisbuttonInInterService extends BaseService<SysMenuButton>{

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
	public List<SysMenuButton> getListByMenuId(String id);
}
