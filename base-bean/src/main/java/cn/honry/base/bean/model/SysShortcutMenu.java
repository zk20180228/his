package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 系统栏目表
 * SysShortcutMenu entity. @author zpty
 */

public class SysShortcutMenu extends Entity {

	/**栏目编号**/
	private String menuId;
	/**用户编号**/
	private String userId;
	/**角色编号**/
	private String roleId;
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	
}