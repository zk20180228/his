package cn.honry.oa.workProcessManage.vo;

import java.io.Serializable;

/**
 * 
 * <p>工作流程管理实体 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月18日 上午11:23:41 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月18日 上午11:23:41 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class WorkProcessManageVo implements Serializable {
		
	private static final long serialVersionUID = -6922444880378775137L;
	
	private String menuName;//栏目的名字
	private String menuCode;//栏目的code
	private String fatherMenuCode;//栏目的父code
	
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
	public String getFatherMenuCode() {
		return fatherMenuCode;
	}
	public void setFatherMenuCode(String fatherMenuCode) {
		this.fatherMenuCode = fatherMenuCode;
	}
	
	
	
	
	
}
