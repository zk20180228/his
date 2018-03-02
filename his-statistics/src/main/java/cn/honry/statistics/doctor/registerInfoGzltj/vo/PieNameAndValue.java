package cn.honry.statistics.doctor.registerInfoGzltj.vo;

public class PieNameAndValue {
	private String name;//名称
	private Integer value=0;//值
	private Integer total=0;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
}
