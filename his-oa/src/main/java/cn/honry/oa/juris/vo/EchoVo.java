package cn.honry.oa.juris.vo;

import java.util.HashMap;

public class EchoVo {

	/**是否所有**/
	private boolean isAll;
	/**人员类别**/
	private HashMap<String,String> type;
	/**职务**/
	private HashMap<String,String> duties;
	/**级别**/
	private HashMap<String,String> level;
	/**院区**/
	private HashMap<String,String> area;
	/**个人**/
	private HashMap<String,String> pers;
	/**自定义**/
	private CustVo cust;
	
	public boolean isAll() {
		return isAll;
	}
	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
	public HashMap<String, String> getType() {
		return type;
	}
	public void setType(HashMap<String, String> type) {
		this.type = type;
	}
	public HashMap<String, String> getDuties() {
		return duties;
	}
	public void setDuties(HashMap<String, String> duties) {
		this.duties = duties;
	}
	public HashMap<String, String> getLevel() {
		return level;
	}
	public void setLevel(HashMap<String, String> level) {
		this.level = level;
	}
	public HashMap<String, String> getArea() {
		return area;
	}
	public void setArea(HashMap<String, String> area) {
		this.area = area;
	}
	public HashMap<String, String> getPers() {
		return pers;
	}
	public void setPers(HashMap<String, String> pers) {
		this.pers = pers;
	}
	public CustVo getCust() {
		return cust;
	}
	public void setCust(CustVo cust) {
		this.cust = cust;
	}
	
}
