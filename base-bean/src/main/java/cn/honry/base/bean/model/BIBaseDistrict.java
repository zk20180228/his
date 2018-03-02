package cn.honry.base.bean.model;


public class BIBaseDistrict implements java.io.Serializable{

	/**主键编码**/
	private String id;
	/**城市代码**/
	private String cityCode;
	/**城市名称**/
	private String cityName;
	/**城市简称**/
	private String shortname;
	/**城市英文名称**/
	private String ename;
	/****/
	private String parentId;
	/**城市路径**/
	private String path;
	/**生成路径排序号**/
	private Integer ordertoPath;
	/**排序号**/
	private Integer order;
	/**所有父级**/
	private String upperPath;
	/**城市层级**/
	private Integer level;
	/**直辖市标识 1-非直辖市，2-直辖市**/
	private Integer municpalityFlag;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**自定义码**/
	private String defined;
	/**有效标识 1-有效，2-无效**/
	private Integer validFlag;
	/**备注**/
	private String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUpperPath() {
		return upperPath;
	}
	public void setUpperPath(String upperPath) {
		this.upperPath = upperPath;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getMunicpalityFlag() {
		return municpalityFlag;
	}
	public void setMunicpalityFlag(Integer municpalityFlag) {
		this.municpalityFlag = municpalityFlag;
	}
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
	
	public String getDefined() {
		return defined;
	}
	public void setDefined(String defined) {
		this.defined = defined;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrdertoPath() {
		return ordertoPath;
	}
	public void setOrdertoPath(Integer ordertoPath) {
		this.ordertoPath = ordertoPath;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	
	
	
	
}
