package cn.honry.mobile.personMenu.service;

import java.util.List;

import cn.honry.base.bean.model.SysUserMenuFunjuris;
import cn.honry.base.service.BaseService;
import cn.honry.inner.system.menu.vo.ParentMenuVo;
import cn.honry.mobile.personMenu.vo.MenuVo;
import cn.honry.mobile.personMenu.vo.UserVo;

/**  
 * 
 * <p>个人栏目维护</p>
 * @Author: dutianliang
 * @CreateDate: 2017年7月15日 下午4:50:57 
 * @Modifier: dutianliang
 * @ModifyDate: 2017年7月15日 下午4:50:57 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface PersonMenuService extends BaseService<SysUserMenuFunjuris>{
	
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
	 * <p>保存信息</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月10日 下午7:27:18 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月10日 下午7:27:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param response
	 * @param infoJson json串
	 * @param userAccount 账号
	 *
	 */
	void saveMenus(String infoJson, String userAccount, String parentId);


}
