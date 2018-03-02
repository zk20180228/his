package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

/**
 *  
 * @Description： 业务变更表 
 * @Author：lyy
 * @CreateDate：2015-10-23 上午09:27:56  
 * @Modifier：lyy
 * @ModifyDate：2015-10-23 上午09:27:56  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class BusinessChange implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//主键id
	private String id;
	//主体名称
	private String subjectName;
	//主体编号
	private String subjectId;
	//变更表名
	private String tableName;
	//变更字段
	private String column;
	//变更字段名
	private String columnName;
	//旧值
	private String oldValue;
	//新值
	private String newValue;
	//会话ID					
	private Integer SessionId;
	//操作人员IP地址
	private String IP;
	//创建人
	private String createUser;
	//创建时间
	private Date createTime;
	//创建部门
	private String createDept;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	
	public Integer getSessionId() {
		return SessionId;
	}
	public void setSessionId(Integer sessionId) {
		SessionId = sessionId;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	
	

}
