package cn.honry.inpatient.docAdvManage.vo;

public class SystemtypeVo {
	/**编码**/
	private String code;
	/**名称**/
	private String name;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String inputCode;
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
