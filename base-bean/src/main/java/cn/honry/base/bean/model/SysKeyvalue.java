package cn.honry.base.bean.model;

public class SysKeyvalue implements java.io.Serializable{

	private String id;
	private String keyName;
	private String keyFlag;
	private Integer keyValue;
	//做当前年月用
	private String keyBak1;
	private String keyBak2;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getKeyFlag() {
		return keyFlag;
	}
	public void setKeyFlag(String keyFlag) {
		this.keyFlag = keyFlag;
	}
	public Integer getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(Integer keyValue) {
		this.keyValue = keyValue;
	}
	public String getKeyBak1() {
		return keyBak1;
	}
	public void setKeyBak1(String keyBak1) {
		this.keyBak1 = keyBak1;
	}
	public String getKeyBak2() {
		return keyBak2;
	}
	public void setKeyBak2(String keyBak2) {
		this.keyBak2 = keyBak2;
	}
	
	

}
