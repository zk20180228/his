package cn.honry.outpatient.advice.vo;


public class OdditionalitemAndUnDrugVo {
	private String id;//附材id
	private Integer qty;//数量
	private Double price;//价格
	private String unit;//单位
	private Double totalPrice;//总金额
	private String unDrugId;//非药品id
	private String name;//非药品名称
	private String spec;//非药品规格
	private String sysType;//非药品系统类型
	private String minimumCost;//非药品最小费用
	private String inspectionsite;//检查部位或标本
	private String dept;//执行科室
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getUnDrugId() {
		return unDrugId;
	}
	public void setUnDrugId(String unDrugId) {
		this.unDrugId = unDrugId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public String getMinimumCost() {
		return minimumCost;
	}
	public void setMinimumCost(String minimumCost) {
		this.minimumCost = minimumCost;
	}
	public String getInspectionsite() {
		return inspectionsite;
	}
	public void setInspectionsite(String inspectionsite) {
		this.inspectionsite = inspectionsite;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
}
