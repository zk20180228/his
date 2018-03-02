package cn.honry.base.bean.model;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  采购计划明细
 * @Author：zpty
 * @CreateDate：2017-11-20 上午09:35:05  
 *
 */
public class MatPlanDetail extends Entity{

	
	private static final long serialVersionUID = 1L;

	/** 采购流水号   **/
	private String procurementNo;
	/** 序号  **/
	private Integer serialNo;
	/** 物品编码   **/
	private String itemCode;
	/** 物品名称   **/
	private String itemName;
	/** 物品科目编码   **/
	private String kindCode;
	/** 物品科目名称   **/
	private String kindName;
	/** 规格   **/
	private String specs;
	/** 最小单位   **/
	private String minUnit;
	/** 大包装单位   **/
	private String packUnit;
	/** 大包装数量   **/
	private Integer packQty;
	/** 采购数量   **/
	private Integer procurementNum;
	/** 采购价格  **/
	private Double procurementPrice;
	/** 零售价格   **/
	private Double salePrice;
	/** 零售金额   **/
	private Double saleCost;
	/** 生产商代码   **/
	private String producerCode;
	/** 生产商名称   **/
	private String producerName;
	/** 审批数量   **/
	private Integer checkNum;
	
	/**所属医院   **/
	private Integer hospitalId;
	/** 所属院区   **/
	private String areaCode;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getProcurementNo() {
		return procurementNo;
	}
	public void setProcurementNo(String procurementNo) {
		this.procurementNo = procurementNo;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public Integer getProcurementNum() {
		return procurementNum;
	}
	public void setProcurementNum(Integer procurementNum) {
		this.procurementNum = procurementNum;
	}
	public Double getProcurementPrice() {
		return procurementPrice;
	}
	public void setProcurementPrice(Double procurementPrice) {
		this.procurementPrice = procurementPrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Double getSaleCost() {
		return saleCost;
	}
	public void setSaleCost(Double saleCost) {
		this.saleCost = saleCost;
	}
	public String getProducerCode() {
		return producerCode;
	}
	public void setProducerCode(String producerCode) {
		this.producerCode = producerCode;
	}
	public String getProducerName() {
		return producerName;
	}
	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}
	public Integer getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(Integer checkNum) {
		this.checkNum = checkNum;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getKindName() {
		return kindName;
	}
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
	
}