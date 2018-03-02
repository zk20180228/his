package cn.honry.base.bean.model;

import java.util.Date;

public class InformationCheck {
	private String id;
	/*
	 * 文章id
	 */
	private String informationId;
	/*
	 * 是否审核 1是 0否
	 */
	private Integer isCheck;
	/*
	 * 审核人
	 */
	private String checkPersion;
	/*
	 * 审核时间
	 */
	private Date checkTime;
	/*
	 * 审核人类型   0:全部人员,1:按个人,2:角色,3:科室,4:职务
	 */
	private String personType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInformationId() {
		return informationId;
	}
	public void setInformationId(String informationId) {
		this.informationId = informationId;
	}
	public Integer getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}
	public String getCheckPersion() {
		return checkPersion;
	}
	public void setCheckPersion(String checkPersion) {
		this.checkPersion = checkPersion;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getPersonType() {
		return personType;
	}
	public void setPersonType(String personType) {
		this.personType = personType;
	}
	
	
}
