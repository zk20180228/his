package cn.honry.base.bean.model;

import java.io.Serializable;

/**
 * 
 * <p>常用语管理</p>
 * @Author: yuke
 * @CreateDate: 2017年9月23日 上午9:37:23 
 * @Modifier: yuke
 * @ModifyDate: 2017年9月23日 上午9:37:23 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
public class OaCommon implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**常用语id**/
	private String id;
	/**表单名称**/
	private String tableCode;
	/**表单名称**/
	private String tableName;
	/**用户账户**/
	private String userAccount;
	/**用户名称**/
	private String userName;
	/**常用语**/
	private String common;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCommon() {
		return common;
	}
	public void setCommon(String common) {
		this.common = common;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

}
