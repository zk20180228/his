package cn.honry.inner.statistics.outpatientDocRecipe.vo;

/**
 * 用于统计的自定义查询
 * @author user
 *
 */
public class CustomVo {

	private String name;//名称
	private Double value=0.0;//值
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	
}
