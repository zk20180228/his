package cn.honry.oa.homePage.vo;

import java.io.Serializable;

/**
 * 
 * <p>栏目vo</p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月25日 下午7:40:51 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月25日 下午7:40:51 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class MenuVo implements Serializable{
	

	 
	private static final long serialVersionUID = 104005182457424958L;
	
	private String menuName;//栏目名字
	private String menuCode;//栏目的code
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	
	
	

}
