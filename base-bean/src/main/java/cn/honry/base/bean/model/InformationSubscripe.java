package cn.honry.base.bean.model;

public class InformationSubscripe {
	private String id;
	/*
	 * 订阅人
	 */
	private String subscripePerson;
	/*
	 * 文章id
	 */
	private String informationId;
	/*
	 * 是否阅读 0 否 1是
	 */
	private Integer isRead;
	/*
	 * 类型 订阅人类型 0:全部人员,1:按个人,2:角色,3:科室,4:职务
	 */
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubscripePerson() {
		return subscripePerson;
	}
	public void setSubscripePerson(String subscripePerson) {
		this.subscripePerson = subscripePerson;
	}
	public String getInformationId() {
		return informationId;
	}
	public void setInformationId(String informationId) {
		this.informationId = informationId;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
