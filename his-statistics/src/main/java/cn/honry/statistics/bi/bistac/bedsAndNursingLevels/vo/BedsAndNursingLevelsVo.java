package cn.honry.statistics.bi.bistac.bedsAndNursingLevels.vo;


public class BedsAndNursingLevelsVo {
	
	private String state;//床位状态
	private String value;//值
	private String name;//值
	
	private String onelevel;//一级护理
	private String twolevel;//二级护理
	private String threelevel;//三级护理
	private String speciallevel;//特级护理
	
	
	
	
	public String getValue() {
		return value;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOnelevel() {
		return onelevel;
	}
	public void setOnelevel(String onelevel) {
		this.onelevel = onelevel;
	}
	public String getTwolevel() {
		return twolevel;
	}
	public void setTwolevel(String twolevel) {
		this.twolevel = twolevel;
	}
	public String getThreelevel() {
		return threelevel;
	}
	public void setThreelevel(String threelevel) {
		this.threelevel = threelevel;
	}
	public String getSpeciallevel() {
		return speciallevel;
	}
	public void setSpeciallevel(String speciallevel) {
		this.speciallevel = speciallevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
