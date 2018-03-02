package cn.honry.oa.menumanager.dao;

import java.util.List;

import cn.honry.base.bean.model.OaMenu;
import cn.honry.base.bean.model.SysMenu;
import cn.honry.base.dao.EntityDao;
import cn.honry.oa.menumanager.vo.MenuVo;

public interface MenuManagerDao  extends EntityDao<OaMenu> {

	/**
	 * 
	 * <p>展示栏目树</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月17日 下午5:05:39 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月17日 下午5:05:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> showTree(String pid) throws Exception;

	String del(String id, String account);

	/**
	 * 
	 * <p>查询子节点</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:29:08 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:29:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> findAllChild(String id) throws Exception;

	/**
	 * 
	 * <p>根据父级code查找栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:29:31 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:29:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> queryMenuByParentcode(String id);

	Integer getMaxOrder();

	List<OaMenu> queryAllChildren(String path);

	/**
	 * 
	 * 
	 * <p>查询全部子节点</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:31:24 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:31:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> findAllChilds(String id);

	/**
	 * 
	 * <p>查询子节点用于展示树</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:29:08 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:29:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> findAllChildWithCode(String string);
	/**
	 * 
	 * <p>根据code查找栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:29:31 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:29:31 
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
	 * <p>根据id查找栏目</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:29:31 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:29:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<MenuVo> queryMenuById(String id);
	/**
	 * 
	 * <p>根据path查找栏目下面的一级节点</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:29:31 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:29:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<OaMenu> findFirst(String path);
	/**
	 * 
	 * <p>根据path查找栏目下面的节点除一级</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月26日 下午4:29:31 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月26日 下午4:29:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<MenuVo>
	 *
	 */
	List<OaMenu> findNoFirst(String path);

	List<MenuVo> findAllChildWithOutStop(String string);

	MenuVo getLast(Integer morder, String parentCode);

	MenuVo getNext(Integer morder, String parentcode);

}
