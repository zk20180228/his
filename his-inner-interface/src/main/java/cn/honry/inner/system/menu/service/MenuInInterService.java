package cn.honry.inner.system.menu.service;

import java.util.List;

import cn.honry.base.bean.model.SysMenu;
import cn.honry.base.service.BaseService;
import cn.honry.inner.system.menu.vo.ParentMenuVo;

/**  
 *  
 * @className：MenuInInterService
 * @Description：  栏目接口
 * @Author：aizhonghua
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface MenuInInterService extends BaseService<SysMenu>{

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

}
