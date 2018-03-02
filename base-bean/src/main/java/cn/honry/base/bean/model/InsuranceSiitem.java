package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 社保项目信息表(用于对照医保项目)
 * @author a
 *
 */
public class InsuranceSiitem extends Entity{
	private String pactCode;//合同单位编码
	private String itemCode;//项目编码
	private String sysClass;//项目类别[05:0-药品 1-诊疗 2-医疗服务]
	private String name;//医保收费项目中文名称
	private String ename;//医保收费项目英文名称
	private String specs;//规格
	private String doseCode;//剂型
	private String spellCode;//拼音码
	private String feeCode;//费用分类代码 1 床位费 2西药费3中药费4中成药5中草药6检查费7治疗费8放射费9手术费10化验费11输血费12输氧费13其他
	private String itemType;//医保目录级别 1 基本医疗范围 2 河南省厅补充
	private String itemGrade;//医保目录等级 1 甲类(统筹全部支付) 2 乙类(准予部分支付) 3 自费
	private Double rate;//自付比例
	private Double price;//基准价格
	private String memo;//限制使用说明(医保备注)
	private String aka302;//特限标志
	private String aka303;//特限描述
	private String hisSysClass;//项目类别
	private Double rateJm;//居民自付比例
	private Double rateLx;//市离休自付比例
	private Double priceLx;//市离休价格
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getSysClass() {
		return sysClass;
	}
	public void setSysClass(String sysClass) {
		this.sysClass = sysClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getDoseCode() {
		return doseCode;
	}
	public void setDoseCode(String doseCode) {
		this.doseCode = doseCode;
	}
	public String getSpellCode() {
		return spellCode;
	}
	public void setSpellCode(String spellCode) {
		this.spellCode = spellCode;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemGrade() {
		return itemGrade;
	}
	public void setItemGrade(String itemGrade) {
		this.itemGrade = itemGrade;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAka302() {
		return aka302;
	}
	public void setAka302(String aka302) {
		this.aka302 = aka302;
	}
	public String getAka303() {
		return aka303;
	}
	public void setAka303(String aka303) {
		this.aka303 = aka303;
	}
	public String getHisSysClass() {
		return hisSysClass;
	}
	public void setHisSysClass(String hisSysClass) {
		this.hisSysClass = hisSysClass;
	}
	public Double getRateJm() {
		return rateJm;
	}
	public void setRateJm(Double rateJm) {
		this.rateJm = rateJm;
	}
	public Double getRateLx() {
		return rateLx;
	}
	public void setRateLx(Double rateLx) {
		this.rateLx = rateLx;
	}
	public Double getPriceLx() {
		return priceLx;
	}
	public void setPriceLx(Double priceLx) {
		this.priceLx = priceLx;
	}

}
