package cn.honry.inner.system.menuResou.dao;

/**  
 *  
 * @className：MenuResouInInterDao
 * @Description：  栏目功能接口
 * @Author：aizhonghua
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
import java.util.List;

import cn.honry.base.bean.model.SysMenuResourceCode;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface MenuResouInInterDao extends EntityDao<SysMenuResourceCode>{

	/**  
	 * 
	 * 获取栏目功能
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-23 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-23 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysMenuResourceCode> getMenuResouList();

}
