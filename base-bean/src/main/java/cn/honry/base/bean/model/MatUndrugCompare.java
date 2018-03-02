package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**
 * 
 * @author  lyy
 * @createDate： 2016年2月19日 下午6:41:12 
 * @modifier lyy
 * @modifyDate：2016年2月19日 下午6:41:12  
 * @modifyRmk：  
 * @version 1.0
 */
public class MatUndrugCompare extends Entity implements Serializable{
	private String compareNo;   //对照流水号
	private String undrugItemCode;   //非药品编码
	private String undrugItemName;  //非药品名称
	private String undrugSpellCode;  //非药品拼音码
	private String undrugWbCode;  //非药品五笔码
	private String undrugCustomCode;   //非药品自定义码
	private String undrugGbCode;  //非药品国标码
	private String undrugSysClass;  //非药品系统类别
	private String ndrugFeeCode;  //非药品最小费用编码
	private String undrugStockUnit;  //非药品单位
	private String matItemCode;  //物资编码
	private Integer validFlag;  //有效标记
	private String operCode;  //操作员
	private Date operDate;  //操作时间
	public String getCompareNo() {
		return compareNo;
	}
	public void setCompareNo(String compareNo) {
		this.compareNo = compareNo;
	}
	public String getUndrugItemCode() {
		return undrugItemCode;
	}
	public void setUndrugItemCode(String undrugItemCode) {
		this.undrugItemCode = undrugItemCode;
	}
	public String getUndrugItemName() {
		return undrugItemName;
	}
	public void setUndrugItemName(String undrugItemName) {
		this.undrugItemName = undrugItemName;
	}
	public String getUndrugSpellCode() {
		return undrugSpellCode;
	}
	public void setUndrugSpellCode(String undrugSpellCode) {
		this.undrugSpellCode = undrugSpellCode;
	}
	public String getUndrugWbCode() {
		return undrugWbCode;
	}
	public void setUndrugWbCode(String undrugWbCode) {
		this.undrugWbCode = undrugWbCode;
	}
	public String getUndrugCustomCode() {
		return undrugCustomCode;
	}
	public void setUndrugCustomCode(String undrugCustomCode) {
		this.undrugCustomCode = undrugCustomCode;
	}
	public String getUndrugGbCode() {
		return undrugGbCode;
	}
	public void setUndrugGbCode(String undrugGbCode) {
		this.undrugGbCode = undrugGbCode;
	}
	public String getUndrugSysClass() {
		return undrugSysClass;
	}
	public void setUndrugSysClass(String undrugSysClass) {
		this.undrugSysClass = undrugSysClass;
	}
	public String getNdrugFeeCode() {
		return ndrugFeeCode;
	}
	public void setNdrugFeeCode(String ndrugFeeCode) {
		this.ndrugFeeCode = ndrugFeeCode;
	}
	public String getUndrugStockUnit() {
		return undrugStockUnit;
	}
	public void setUndrugStockUnit(String undrugStockUnit) {
		this.undrugStockUnit = undrugStockUnit;
	}
	public String getMatItemCode() {
		return matItemCode;
	}
	public void setMatItemCode(String matItemCode) {
		this.matItemCode = matItemCode;
	}
	public Integer getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
}
