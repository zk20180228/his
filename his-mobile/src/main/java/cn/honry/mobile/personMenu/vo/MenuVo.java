package cn.honry.mobile.personMenu.vo;

public class MenuVo{
	
	private String id;
	private String menuName;
	private String menuAlias;
	private String userAcc;
	private String menuParameter;
	private String menuDescription;
	private Integer rmMenuOrder;
	private Integer rmIsVisible;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuAlias() {
		return menuAlias;
	}
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	public String getUserAcc() {
		return userAcc;
	}
	public void setUserAcc(String userAcc) {
		this.userAcc = userAcc;
	}
	public String getMenuParameter() {
		return menuParameter;
	}
	public void setMenuParameter(String menuParameter) {
		this.menuParameter = menuParameter;
	}
	public String getMenuDescription() {
		return menuDescription;
	}
	public void setMenuDescription(String menuDescription) {
		this.menuDescription = menuDescription;
	}
	public Integer getRmMenuOrder() {
		return rmMenuOrder;
	}
	public void setRmMenuOrder(Integer rmMenuOrder) {
		this.rmMenuOrder = rmMenuOrder;
	}
	public Integer getRmIsVisible() {
		return rmIsVisible;
	}
	public void setRmIsVisible(Integer rmIsVisible) {
		this.rmIsVisible = rmIsVisible;
	}
	
	
}
