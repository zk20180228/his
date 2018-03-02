package cn.honry.base.bean.model;

import java.util.Date;

/**
 * OA系统首页组件实体类
 * @author zpty
 * Date:2017/7/15 15:00
 */
public class OaPortalWidget{
	private static final long serialVersionUID = 1L;
	/**组件编号**/
	private String id;
	/**组件名称**/
	private String name;
	/**组件地址**/
	private String url;
	/**组件状态(默认0-正常，1-禁用)**/
	private Integer status=0;
	/**建立人员**/
	private String createUser;
	/**建立时间**/
	private Date createTime;
	/**修改人员**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	/**删除人员**/
	private String deleteUser;
	/**删除时间**/
	private Date deleteTime;
	
	/**更多地址**/
	private String moreUrl;
	/**查看地址**/
	private String viewUrl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public String getMoreUrl() {
		return moreUrl;
	}
	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}
	public String getViewUrl() {
		return viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

}
