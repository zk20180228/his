package cn.honry.base.bean.model;

import java.math.BigDecimal;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 物资分类表
 */

public class MatKindinfo extends Entity {

	/**物品科目编码**/
	private String kindCode;
	/**物资分类**/
	private String kindType;
	/**路径**/
	private String kindPath;
	/**上级编码**/
	private String preCode;
	/**仓库代码**/
	private String storageCode;
	/**分类名称**/
	private String kindName;
	/**拼音码**/
	private String spellCode;
	/**五笔码**/
	private String wbCode;
	/**自定义码**/
	private String customCode;
	/**国家编码**/
	private String gbCode;
	/**有效范围(0-本科室,1-本科室及下级科室,2-全院,3-指定科室)**/
	private Integer effectArea;
	/**有效科室(EFFECT_AREA=3时有效)**/
	private String effectDept;
	/**是否按批次管理**/
	private Integer batchFlag;
	/**是否有效期管理**/
	private Integer validdateFlag;
	/**停用标记(0－停用,1－使用)**/
	private Integer validFlag;
	/**财务科目编码**/
	private String accountCode;
	/**财务科目名称**/
	private String accountName;
	/**排列序号**/
	private BigDecimal orderNo;
	/**财务收费标志(0－否,1－是)**/
	private Integer financeFlag;
	/**备注**/
	private String memo;
	/**操作员**/
	private String operCode;
	/**操作日期**/
	private Date operDate;
	

	public String getKindCode() {
		return this.kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getPreCode() {
		return this.preCode;
	}

	public void setPreCode(String preCode) {
		this.preCode = preCode;
	}

	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	public String getKindName() {
		return this.kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public String getSpellCode() {
		return this.spellCode;
	}

	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}

	public String getWbCode() {
		return this.wbCode;
	}

	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}

	public String getCustomCode() {
		return this.customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getGbCode() {
		return this.gbCode;
	}

	public void setGbCode(String gbCode) {
		this.gbCode = gbCode;
	}

	public Integer getEffectArea() {
		return this.effectArea;
	}

	public void setEffectArea(Integer effectArea) {
		this.effectArea = effectArea;
	}

	public String getEffectDept() {
		return this.effectDept;
	}

	public void setEffectDept(String effectDept) {
		this.effectDept = effectDept;
	}

	public Integer getBatchFlag() {
		return this.batchFlag;
	}

	public void setBatchFlag(Integer batchFlag) {
		this.batchFlag = batchFlag;
	}

	public Integer getValiddateFlag() {
		return this.validdateFlag;
	}

	public void setValiddateFlag(Integer validdateFlag) {
		this.validdateFlag = validdateFlag;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public String getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(BigDecimal orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getFinanceFlag() {
		return this.financeFlag;
	}

	public void setFinanceFlag(Integer financeFlag) {
		this.financeFlag = financeFlag;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOperCode() {
		return this.operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public void setKindType(String kindType) {
		this.kindType = kindType;
	}

	public String getKindType() {
		return kindType;
	}

	public void setKindPath(String kindPath) {
		this.kindPath = kindPath;
	}

	public String getKindPath() {
		return kindPath;
	}


}