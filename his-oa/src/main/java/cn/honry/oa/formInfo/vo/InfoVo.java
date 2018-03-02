package cn.honry.oa.formInfo.vo;

import java.util.List;

public class InfoVo {
	
	private String code;
	private String name;
	private String mula;
	private List<SectVo> sections;
	
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
	public String getMula() {
		return mula;
	}
	public void setMula(String mula) {
		this.mula = mula;
	}
	public List<SectVo> getSections() {
		return sections;
	}
	public void setSections(List<SectVo> sections) {
		this.sections = sections;
	}
	
}
