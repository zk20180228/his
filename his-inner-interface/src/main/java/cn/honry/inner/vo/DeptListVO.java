package cn.honry.inner.vo;

import java.util.ArrayList;
import java.util.List;


public class DeptListVO {
	private String parentMenu;	//父级名称
	private List<DeptVO> menus = new ArrayList<DeptVO>();	//子级集合
	public String getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<DeptVO> getMenus() {
		return menus;
	}
	public void setMenus(List<DeptVO> menus) {
		this.menus = menus;
	}
	
}
