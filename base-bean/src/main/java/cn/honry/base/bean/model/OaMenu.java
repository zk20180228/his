package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

public class OaMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**栏目id**/
	private String id;
	/**是否评论0:关闭评论1:开启评论**/
	private Integer mcomment;
	/**栏目名称**/
	private String name;
	/**栏目code**/
	private String code;
	/**栏目父级code**/
	private String parentcode;
	/**直接发布1:可以2:不可以**/
	private Integer publishdirt;
	/**栏目路径**/
	private String path;
	/**栏目父级路径**/
	private String parentpath;
	/**排序**/
	private Integer morder;
	/**是否删除0:未删除,1:删除**/
	private Integer del_flg;
	/**是否停用0:使用中,1:已停用**/
	private Integer stop_flag;
	/**栏目所属科室**/
	private String dept;
	/**栏目说明**/
	private String explain;
	/**删除时间**/
	private Date deleteTime;
	/**删除人员**/
	private String deleteUser;
	/**更新时间**/
	private Date updateTime;
	/**更新人员**/
	private String updateUser;
	/**创建时间**/
	private Date createTime;
	/**创建人员**/
	private String createUser;
	/**创建人员**/
	private String createDept;
	/**
	 * 栏目所属：0-医院；1-患者APP
	 */
	private String type = "0";
	
	public Date getDeleteTime() {
		return deleteTime;
	}
	
	public String getCreateDept() {
		return createDept;
	}

	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getMcomment() {
		return mcomment;
	}
	public void setMcomment(Integer mcomment) {
		this.mcomment = mcomment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	public Integer getPublishdirt() {
		return publishdirt;
	}
	public void setPublishdirt(Integer publishdirt) {
		this.publishdirt = publishdirt;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getParentpath() {
		return parentpath;
	}
	public void setParentpath(String parentpath) {
		this.parentpath = parentpath;
	}
	public Integer getMorder() {
		return morder;
	}
	public void setMorder(Integer morder) {
		this.morder = morder;
	}
	public Integer getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}
	public Integer getStop_flag() {
		return stop_flag;
	}
	public void setStop_flag(Integer stop_flag) {
		this.stop_flag = stop_flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
