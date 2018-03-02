package cn.honry.finance.medicinelist.vo;

public class SpeNalVo {

	//配药台
	private String tCode;
	//药房
	private String deptCode;
	//特殊项目代码
	private String itemCode;
	//项目类别
	private Integer itemType;
	
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	public String gettCode() {
		return tCode;
	}
	public void settCode(String tCode) {
		this.tCode = tCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
}
