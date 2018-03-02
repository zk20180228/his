package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * ClassName: DrugStoragepara 
 * @Description: 库房参数表实体
 * @author lt
 * @date 2015-7-15
 */
@SuppressWarnings("serial")
public class DrugStoragepara extends Entity implements java.io.Serializable {

	// Fields
	/**科室编号*/
	private String deptCode;
	/**库房最高库存量(天)*/
	private Short storeMaxDays;
	/**库房最低库存量(天)*/
	private Short storeMinDays;
	/**参考天数*/
	private Short referenceDays;
	/**是否按批号管理药品*/
	private Integer batchFlag;
	/**是否管理药品库存*/
	private Integer storeFlag;
	/**库存管理时默认的单位*/
	private Integer unitFlag;
	/**是否按药柜管理药品*/
	private Integer arkFlag;
	/**入库单据号*/
	private String inListCode;
	/**出库单据号*/
	private String outListCode;
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Short getStoreMaxDays() {
		return storeMaxDays;
	}
	public void setStoreMaxDays(Short storeMaxDays) {
		this.storeMaxDays = storeMaxDays;
	}
	public Short getStoreMinDays() {
		return storeMinDays;
	}
	public void setStoreMinDays(Short storeMinDays) {
		this.storeMinDays = storeMinDays;
	}
	public Short getReferenceDays() {
		return referenceDays;
	}
	public void setReferenceDays(Short referenceDays) {
		this.referenceDays = referenceDays;
	}
	public Integer getBatchFlag() {
		return batchFlag;
	}
	public void setBatchFlag(Integer batchFlag) {
		this.batchFlag = batchFlag;
	}
	public Integer getStoreFlag() {
		return storeFlag;
	}
	public void setStoreFlag(Integer storeFlag) {
		this.storeFlag = storeFlag;
	}
	public Integer getUnitFlag() {
		return unitFlag;
	}
	public void setUnitFlag(Integer unitFlag) {
		this.unitFlag = unitFlag;
	}
	public Integer getArkFlag() {
		return arkFlag;
	}
	public void setArkFlag(Integer arkFlag) {
		this.arkFlag = arkFlag;
	}
	public String getInListCode() {
		return inListCode;
	}
	public void setInListCode(String inListCode) {
		this.inListCode = inListCode;
	}
	public String getOutListCode() {
		return outListCode;
	}
	public void setOutListCode(String outListCode) {
		this.outListCode = outListCode;
	}
	
	

}