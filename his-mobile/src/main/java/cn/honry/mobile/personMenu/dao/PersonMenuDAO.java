package cn.honry.mobile.personMenu.dao;

import java.util.List;

import cn.honry.base.bean.model.SysUserMenuFunjuris;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.system.menu.vo.ParentMenuVo;
import cn.honry.mobile.personMenu.vo.MenuVo;
import cn.honry.mobile.personMenu.vo.UserVo;

/**  
 *  
 * @className：UserMenuFunJurisDAO
 * @Description：  用户栏目功能权限管理（移动端）
 * @Author：dutianliang
 * @CreateDate：2017-7-03 上午11:56:31  
 * @Modifier：dutianliang
 * @ModifyDate：2017-7-03 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface PersonMenuDAO extends EntityDao<SysUserMenuFunjuris>{
	/**  
	 * 
	 * <p>用户分页查询查询条数</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午4:22:30 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午4:22:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param q 查询条件
	 * @return: int 总条数
	 *
	 */
	int getUserTotal(String q);
	/**  
	 * 
	 * <p>用户分页查询查询当前页数据</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午4:22:33 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午4:22:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param page
	 * @param rows
	 * @param q 查询条件
	 * @return: List<UserVo> 当前页数据
	 *
	 */
	List<UserVo> getUserRows(String page, String rows, String q);
	/**  
	 * 
	 * <p>查询栏目下拉框（只有别名和名称两个字段，并且都是一级栏目）</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午5:36:27 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午5:36:27 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: List<ParentMenuVo> 栏目下拉框数据
	 *
	 */
	List<ParentMenuVo> queryMenuCombo();
	/**  
	 * 
	 * <p>查询栏目列表信息</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午5:54:43 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午5:54:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param parentId 父级id
	 * @param userAccount 用户账号
	 *
	 */
	List<MenuVo> queryMenuList(String parentId, String userAccount);

	/**  
	 * 
	 * <p>通过栏目别名以及员工账号查询权限</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月15日 下午5:49:10 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月15日 下午5:49:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param userAcc 员工账号
	 * @return: SysUserMenuFunjuris 权限
	 *
	 */
	SysUserMenuFunjuris getByMenuIdUserId(String menuAlias, String userAcc);

	
	
}
