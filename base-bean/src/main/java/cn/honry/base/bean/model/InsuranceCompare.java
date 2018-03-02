package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 医疗保险对照表
 * @author a
 *
 */
public class InsuranceCompare extends Entity{
	private String pactCode;//合同单位
	private String hisCode;//本地项目编码
	private String hisName;//本地项目名称
	private String regularname;//本地项目别名
	private String centerCode;//医保收费项目编码
	private String centerSysClass;//项目类别 X-西药 Z-中药 L-诊疗项目 F-医疗服务设施
	private String centerName;//医保收费项目中文名称
	private String centerEname;//医保收费项目英文名称
	private String centerSpecs;//医保规格
	private String centerDose;//医保剂型编码
	private String centerSpell;//医保拼音代码
	private String centerFeeCode;//医保费用分类代码 1 床位费 2西药费3中药费4中成药5中草药6检查费7治疗费8放射费9手术费10化验费11输血费12输氧费13其他
	private String centerItemType;//医保目录级别 1 基本医疗范围 2 河南省厅补充
	private String centerItemGrade;//医保目录等级 1 甲类(统筹全部支付) 2 乙类(准予部分支付) 3 自费
	private Double centerRate;//自负比例
	private Double centerPrice;//基准价格
	private String centerMemo;//限制使用说明(医保备注)
	private String hisSpell;//医院拼音
	private String hisWbCode;//医院五笔码
	private String hisUserCode;//医院自定义码
	private String hisSpecs;//医院规格
	private Double hisPrice;//医院基本价格
	private String hisDose;//医院剂型
	private String specialLimitFlag;//特限标志
	private String specialLimitContent;//特限
	private String hisSysClass;//HIS项目类别 X-西药 Z-中药 L-诊疗项目 F-医疗服务设施
	private Double centerRateLx;//中心自负比例。--市离休
	private Double centerPriceLx;//中心价格。--市离休
	private String centerCodeLx;//中心编码。--市离休
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public String getHisCode() {
		return hisCode;
	}
	public void setHisCode(String hisCode) {
		this.hisCode = hisCode;
	}
	public String getHisName() {
		return hisName;
	}
	public void setHisName(String hisName) {
		this.hisName = hisName;
	}
	public String getRegularname() {
		return regularname;
	}
	public void setRegularname(String regularname) {
		this.regularname = regularname;
	}
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	public String getCenterSysClass() {
		return centerSysClass;
	}
	public void setCenterSysClass(String centerSysClass) {
		this.centerSysClass = centerSysClass;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public String getCenterEname() {
		return centerEname;
	}
	public void setCenterEname(String centerEname) {
		this.centerEname = centerEname;
	}
	public String getCenterSpecs() {
		return centerSpecs;
	}
	public void setCenterSpecs(String centerSpecs) {
		this.centerSpecs = centerSpecs;
	}
	public String getCenterDose() {
		return centerDose;
	}
	public void setCenterDose(String centerDose) {
		this.centerDose = centerDose;
	}
	public String getCenterSpell() {
		return centerSpell;
	}
	public void setCenterSpell(String centerSpell) {
		this.centerSpell = centerSpell;
	}
	public String getCenterFeeCode() {
		return centerFeeCode;
	}
	public void setCenterFeeCode(String centerFeeCode) {
		this.centerFeeCode = centerFeeCode;
	}
	public String getCenterItemType() {
		return centerItemType;
	}
	public void setCenterItemType(String centerItemType) {
		this.centerItemType = centerItemType;
	}
	public String getCenterItemGrade() {
		return centerItemGrade;
	}
	public void setCenterItemGrade(String centerItemGrade) {
		this.centerItemGrade = centerItemGrade;
	}
	public Double getCenterRate() {
		return centerRate;
	}
	public void setCenterRate(Double centerRate) {
		this.centerRate = centerRate;
	}
	public Double getCenterPrice() {
		return centerPrice;
	}
	public void setCenterPrice(Double centerPrice) {
		this.centerPrice = centerPrice;
	}
	public String getCenterMemo() {
		return centerMemo;
	}
	public void setCenterMemo(String centerMemo) {
		this.centerMemo = centerMemo;
	}
	public String getHisSpell() {
		return hisSpell;
	}
	public void setHisSpell(String hisSpell) {
		this.hisSpell = hisSpell;
	}
	public String getHisWbCode() {
		return hisWbCode;
	}
	public void setHisWbCode(String hisWbCode) {
		this.hisWbCode = hisWbCode;
	}
	public String getHisUserCode() {
		return hisUserCode;
	}
	public void setHisUserCode(String hisUserCode) {
		this.hisUserCode = hisUserCode;
	}
	public String getHisSpecs() {
		return hisSpecs;
	}
	public void setHisSpecs(String hisSpecs) {
		this.hisSpecs = hisSpecs;
	}
	public Double getHisPrice() {
		return hisPrice;
	}
	public void setHisPrice(Double hisPrice) {
		this.hisPrice = hisPrice;
	}
	public String getHisDose() {
		return hisDose;
	}
	public void setHisDose(String hisDose) {
		this.hisDose = hisDose;
	}
	public String getSpecialLimitFlag() {
		return specialLimitFlag;
	}
	public void setSpecialLimitFlag(String specialLimitFlag) {
		this.specialLimitFlag = specialLimitFlag;
	}
	public String getSpecialLimitContent() {
		return specialLimitContent;
	}
	public void setSpecialLimitContent(String specialLimitContent) {
		this.specialLimitContent = specialLimitContent;
	}
	public String getHisSysClass() {
		return hisSysClass;
	}
	public void setHisSysClass(String hisSysClass) {
		this.hisSysClass = hisSysClass;
	}
	public Double getCenterRateLx() {
		return centerRateLx;
	}
	public void setCenterRateLx(Double centerRateLx) {
		this.centerRateLx = centerRateLx;
	}
	public Double getCenterPriceLx() {
		return centerPriceLx;
	}
	public void setCenterPriceLx(Double centerPriceLx) {
		this.centerPriceLx = centerPriceLx;
	}
	public String getCenterCodeLx() {
		return centerCodeLx;
	}
	public void setCenterCodeLx(String centerCodeLx) {
		this.centerCodeLx = centerCodeLx;
	}
}
