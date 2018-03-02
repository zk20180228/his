package cn.honry.oa.activiti.bpm.form.vo;

public class ButtonVo {

	private String name;
	private String lable;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public ButtonVo(String name, String lable) {
		super();
		this.name = name;
		this.lable = lable;
	}
	public ButtonVo() {
		super();
	}
	
}
