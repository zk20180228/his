package cn.honry.outpatient.advice.vo;


public class DrugAndUnDrugVo {
	private String id;//id
	private String name;//名称
	private String code;//名称
	private String type;//类型
	private Integer delFlg;//删除标志
	private Integer stopFlg;//停用标志
	private Double price;//价格
	private Double priceChil;//儿童价格
	private Integer ty;//药品1非药品0
	private Integer surSum;//剩余数量
	private String grade;//药品等级
	private Integer splitattr;//拆分属性
	private Integer isSubmit;//是否终端确认
	private Integer isMake;//是否终端预约
	private Integer isStack;//是否组套
	
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
	public Integer getDelFlg() {
		return delFlg;
	}
	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}
	public Integer getStopFlg() {
		return stopFlg;
	}
	public void setStopFlg(Integer stopFlg) {
		this.stopFlg = stopFlg;
	}
	public Integer getTy() {
		return ty;
	}
	public void setTy(Integer ty) {
		this.ty = ty;
	}
	public Integer getSurSum() {
		return surSum;
	}
	public void setSurSum(Integer surSum) {
		this.surSum = surSum;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Integer getSplitattr() {
		return splitattr;
	}
	public void setSplitattr(Integer splitattr) {
		this.splitattr = splitattr;
	}
	public Integer getIsSubmit() {
		return isSubmit;
	}
	public void setIsSubmit(Integer isSubmit) {
		this.isSubmit = isSubmit;
	}
	public Double getPriceChil() {
		return priceChil;
	}
	public void setPriceChil(Double priceChil) {
		this.priceChil = priceChil;
	}
	public Integer getIsStack() {
		return isStack;
	}
	public void setIsStack(Integer isStack) {
		this.isStack = isStack;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getIsMake() {
		return isMake;
	}
	public void setIsMake(Integer isMake) {
		this.isMake = isMake;
	}
}
