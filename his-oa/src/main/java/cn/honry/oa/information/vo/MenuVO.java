package cn.honry.oa.information.vo;

public class MenuVO {
	/**栏目id**/
	private String id;
	/**栏目name**/
	private String name;
	/**栏目code**/
	private String code;
	/**直接发布,1:可以;2:不可以**/
	private Integer publishDirt;
	/**所属科室**/
	private String dept;
	/**是否开启评论**/
	private String isComment;
	/**父节点code**/
	private String parentCode;
	/**父节点name**/
	private String parentName;
	/**栏目路径**/
	private String path;
	/**父级栏目路径**/
	private String parentPath;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getPublishDirt() {
		return publishDirt;
	}
	public void setPublishDirt(Integer publishDirt) {
		this.publishDirt = publishDirt;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getIsComment() {
		return isComment;
	}
	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}
