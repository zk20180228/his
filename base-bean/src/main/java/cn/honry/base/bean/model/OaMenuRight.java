package cn.honry.base.bean.model;

import java.io.Serializable;
/**
 * 
 * <p>oa栏目权限</p>
 * @Author: yuke
 * @CreateDate: 2017年7月20日 下午7:17:45 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月20日 下午7:17:45 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
public class OaMenuRight implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**权限id**/
	private String id;
	/**权限类型**/
	private String rightType;
	/**选择类型**/
	private String right;
	/**所选code**/
	private String code;
	/**code名称**/
	private String codeName;
	/**栏目id**/
	private String menuId;
	
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRightType() {
		return rightType;
	}
	public void setRightType(String rightType) {
		this.rightType = rightType;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

}
