package cn.honry.finance.medicinelist.vo;
//非药品与库存VO
public class UndrugAndWare {

	//名字
	private String undrugName;
	//代码
	private String undrugCode;
	//最小费用代码
	private String undrugMinCode;
	//规格
	private String spec;
	//价格
	private Double defaultprice;
	//单位
	private String unit;
	//自定义吗
	private String undrugInputcode;
	//拼音码
	private String pin;
	//五笔码
	private String wb;
	//注意事项
	private String undrugNotes;
	//状态
	private Double sum;
	//是否确认
	private Integer undrugIssubmit;
	//执行科室
	private String undrugDept;
	//系统类别
	private String undrugSystype;
	//条数
	private Integer totle;
	//组套号
	private String groupNo;
	/**是否需要确认**/
	private Integer issubmit;
	
	
	
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public Integer getTotle() {
		return totle;
	}
	public void setTotle(Integer totle) {
		this.totle = totle;
	}
	public String getUndrugSystype() {
		return undrugSystype;
	}
	public void setUndrugSystype(String undrugSystype) {
		this.undrugSystype = undrugSystype;
	}
	public String getUndrugDept() {
		return undrugDept;
	}
	public void setUndrugDept(String undrugDept) {
		this.undrugDept = undrugDept;
	}
	public Integer getUndrugIssubmit() {
		return undrugIssubmit;
	}
	public void setUndrugIssubmit(Integer undrugIssubmit) {
		this.undrugIssubmit = undrugIssubmit;
	}
	public String getUndrugName() {
		return undrugName;
	}
	public void setUndrugName(String undrugName) {
		this.undrugName = undrugName;
	}
	public String getUndrugCode() {
		return undrugCode;
	}
	public void setUndrugCode(String undrugCode) {
		this.undrugCode = undrugCode;
	}
	public String getUndrugMinCode() {
		return undrugMinCode;
	}
	public void setUndrugMinCode(String undrugMinCode) {
		this.undrugMinCode = undrugMinCode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Double getDefaultprice() {
		return defaultprice;
	}
	public void setDefaultprice(Double defaultprice) {
		this.defaultprice = defaultprice;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUndrugInputcode() {
		return undrugInputcode;
	}
	public void setUndrugInputcode(String undrugInputcode) {
		this.undrugInputcode = undrugInputcode;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getUndrugNotes() {
		return undrugNotes;
	}
	public void setUndrugNotes(String undrugNotes) {
		this.undrugNotes = undrugNotes;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public Integer getIssubmit() {
		return issubmit;
	}
	public void setIssubmit(Integer issubmit) {
		this.issubmit = issubmit;
	}
	
	
	
	
}
