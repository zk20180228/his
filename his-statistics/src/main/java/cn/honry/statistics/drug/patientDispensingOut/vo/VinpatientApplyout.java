package cn.honry.statistics.drug.patientDispensingOut.vo;

import java.util.Date;
import java.util.List;

public class VinpatientApplyout {
	/**床号**/
	private String bedName;
	/**姓名**/
	private String patientName;
	/**药品名称**/
	private String tradeName;
	/**规格**/
	private String specs;
	/**每次量**/
	private Double doseOnce;
	/**剂量单位**/
	private String doseUnit;
	/**频次**/
	private String dfqCexp;
	/**用法名称**/
	private String useName;
	/**最小单位**/
	private String min_unit;
	/**发药部门编码**/
	private String drugDeptCode;
	/**医嘱发送类型（1集中发送，2临时发送，3全部）**/
	private String sendType;
	/**摆药单分类代码**/
	private String billclassCode;
	/**申请人编码**/
	private String applyOpercode;
	/**申请日期**/
	private Date applyDate;
	/**操作员（打印人）**/
	private String printEmpl;
	/**操作日期（打印时间）**/
	private Date printDate;
	/**有效标记（1有效，0无效，2不摆药）**/
	private String validState;
	/**拼音码**/
	private String pinyin;
	/**五笔码**/
	private String wb;
	/**出库量/总量**/
	private Double applyNum;
	/**是否摆药**/
	private String baiyao;
	/**报表子表**/
	private List<VinpatientApplyout> vinpatientApplyout;
	
	public List<VinpatientApplyout> getVinpatientApplyout() {
		return vinpatientApplyout;
	}
	public void setVinpatientApplyout(List<VinpatientApplyout> vinpatientApplyout) {
		this.vinpatientApplyout = vinpatientApplyout;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public Double getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(Double doseOnce) {
		this.doseOnce = doseOnce;
	}
	public String getDoseUnit() {
		return doseUnit;
	}
	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}
	public String getDfqCexp() {
		return dfqCexp;
	}
	public void setDfqCexp(String dfqCexp) {
		this.dfqCexp = dfqCexp;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public String getMin_unit() {
		return min_unit;
	}
	public void setMin_unit(String min_unit) {
		this.min_unit = min_unit;
	}
	public String getDrugDeptCode() {
		return drugDeptCode;
	}
	public void setDrugDeptCode(String drugDeptCode) {
		this.drugDeptCode = drugDeptCode;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getBillclassCode() {
		return billclassCode;
	}
	public void setBillclassCode(String billclassCode) {
		this.billclassCode = billclassCode;
	}
	public String getApplyOpercode() {
		return applyOpercode;
	}
	public void setApplyOpercode(String applyOpercode) {
		this.applyOpercode = applyOpercode;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getPrintEmpl() {
		return printEmpl;
	}
	public void setPrintEmpl(String printEmpl) {
		this.printEmpl = printEmpl;
	}
	public Date getPrintDate() {
		return printDate;
	}
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
	public String getValidState() {
		return validState;
	}
	public void setValidState(String validState) {
		this.validState = validState;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public Double getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(Double applyNum) {
		this.applyNum = applyNum;
	}
	public String getBaiyao() {
		return baiyao;
	}
	public void setBaiyao(String baiyao) {
		this.baiyao = baiyao;
	}
	
}
