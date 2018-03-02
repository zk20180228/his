package cn.honry.base.bean.model;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  物资回收
 * @Author：zpty
 * @CreateDate：2017-11-22 上午09:35:05  
 *
 */
public class MatBack extends Entity{

	
	private static final long serialVersionUID = 1L;

	/** 回收流水号   **/
	private String matBackNo;
	/** 回收序号   **/
	private Integer itemNo;
	/** 物资所在科室代码   **/
	private String matDept;
	/** 物资所在科室名称   **/
	private String matDeptName;
	/** 回收科室代码   **/
	private String matBackDept;
	/** 回收科室名称  **/
	private String matBackDname;
	/** 回收项目代码   **/
	private String itemCode;
	/**  回收项目名称  **/
	private String itemName;
	/**  物品科目编码  **/
	private String kindCode;
	/**  物品科目名称  **/
	private String kindName;
	/** 规格   **/
	private String specs;
	/** 最小单位   **/
	private String minUnit;
	/** 大包装单位   **/
	private String packUnit;
	/** 大包装数量   **/
	private Integer packQty;
	/** 回收价格  **/
	private Double backPrice=0.0;
	/** 零售价格   **/
	private Double salePrice=0.0;
	/** 生产商代码   **/
	private String producerCode;
	/** 生产商名称   **/
	private String producerName;
	/** 回收数量   **/
	private Integer backNumber;
	/**  回收时间  **/
	private Date backTime;
	
	/**所属医院   **/
	private Integer hospitalId;
	/** 所属院区   **/
	private String areaCode;
	
	/** 页数   **/
	private String page;
	/**  每页行数  **/
	private String rows;
	
	public String getMatBackNo() {
		return matBackNo;
	}
	public void setMatBackNo(String matBackNo) {
		this.matBackNo = matBackNo;
	}
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	public String getMatDept() {
		return matDept;
	}
	public void setMatDept(String matDept) {
		this.matDept = matDept;
	}
	public String getMatBackDept() {
		return matBackDept;
	}
	public void setMatBackDept(String matBackDept) {
		this.matBackDept = matBackDept;
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
	public Double getBackPrice() {
		return backPrice;
	}
	public void setBackPrice(Double backPrice) {
		this.backPrice = backPrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
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
	public Integer getBackNumber() {
		return backNumber;
	}
	public void setBackNumber(Integer backNumber) {
		this.backNumber = backNumber;
	}
	public Date getBackTime() {
		return backTime;
	}
	public void setBackTime(Date backTime) {
		this.backTime = backTime;
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
	public String getMatDeptName() {
		return matDeptName;
	}
	public void setMatDeptName(String matDeptName) {
		this.matDeptName = matDeptName;
	}
	public String getMatBackDname() {
		return matBackDname;
	}
	public void setMatBackDname(String matBackDname) {
		this.matBackDname = matBackDname;
	}
	public String getKindName() {
		return kindName;
	}
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
	
}