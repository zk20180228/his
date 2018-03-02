package cn.honry.oa.activiti.bpm.process.vo;

public class OaProcessVo {

	private String id;//流程定义id
	private String name;//流程定义名称
	private String descn;//流程定义描述
	private String categoryCode;//流程分类id
	private String categoryName;//流程分类名称
	private String topFlow;//前置流程
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getTopFlow() {
		return topFlow;
	}
	public void setTopFlow(String topFlow) {
		this.topFlow = topFlow;
	}
	
}
