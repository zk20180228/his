package cn.honry.inner.vo;

import java.util.ArrayList;
import java.util.List;

/**  
 *  
 * @className：MenuListVO
 * @Description： 查询科室列表，定义父级子级
 * @Author：gaotiantian
 * @CreateDate：2017-4-14 下午7:15:24  
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-14 下午7:15:24  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class MenuListVO {
	private String parentMenu;	//父级名称
	private List<MenuVO> menus = new ArrayList<MenuVO>();	//子级集合
	public String getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<MenuVO> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuVO> menus) {
		this.menus = menus;
	}
		
}

