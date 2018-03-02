package cn.honry.base.bean.model;

import java.util.Date;
import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @className：MetNuiConfirm
 * @Description：  医嘱执行确认
 * @Author：aizhonghua
 * @CreateDate：2016-03-08 下午19:51:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-03-08 下午19:51:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class MetNuiConfirm extends Entity {
	
	/**看诊序号**/
	private String seeNo;
	/**项目流水号**/
	private Integer sequenceNo;
	/**执行次数**/
	private Integer listNo;
	/**病历号**/
	private String cardNo;
	/**挂号日期**/
	private Date regDate;
	/**挂号科室**/
	private String deptCode;
	/**项目代码**/
	private String itemCode;
	/**项目名称**/
	private String itemName;
	/**1药品，2非药品**/
	private String drugFlag;
	/**系统类别**/
	private String classCode;
	/**最小费用代码**/
	private String feeCode;
	/**单价**/
	private Double unitPrice;
	/**开立数量**/
	private Integer qty;
	/**付数**/
	private Integer days;
	/**包装数量**/
	private Integer packQty;
	/**计价单位**/
	private String itemUnit;
	/**自费金额**/
	private Double ownCost;
	/**自负金额**/
	private Double payCost;
	/**报销金额**/
	private Double pubCost;
	/**每次用量**/
	private Double onceDose;
	/**每次用量单位**/
	private String onceUnit;
	/**剂型代码**/
	private String doseModelCode;
	/**频次**/
	private String frequencyCode;
	/**频次名称**/
	private String frequencyName;
	/**使用方法**/
	private String usageCode;
	/**用法名称**/
	private String usageName;
	/**用法英文缩写**/
	private String englishAb;
	/**执行科室代码**/
	private String execDpcd;
	/**执行科室名称**/
	private String execDpnm;
	/**主药标志**/
	private String mainDrug;
	/**组合号**/
	private String combNo;
	/**院内注射次数**/
	private Integer injectNumber;
	/**备注**/
	private String remark;
	/**开立医生**/
	private String doctCode;
	/**医生科室**/
	private String doctDpcd;
	/**加急标记0普通/1加急**/
	private String emcFlag;
	/**样本类型**/
	private String labType;
	/**检体**/
	private String checkBody;
	/**申请单号**/
	private String applyNo;
	/**确认人**/
	private String confirmCode;
	/**确认科室**/
	private String confirmDept;
	/**确认时间**/
	private Date confirmDate;
	
	/**用于查询-开始时间**/
	private Date startDate;
	/**用于查询-结束时间**/
	private Date endDate;
	
	public String getSeeNo() {
		return seeNo;
	}
	public void setSeeNo(String seeNo) {
		this.seeNo = seeNo;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Integer getListNo() {
		return listNo;
	}
	public void setListNo(Integer listNo) {
		this.listNo = listNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	public String getDrugFlag() {
		return drugFlag;
	}
	public void setDrugFlag(String drugFlag) {
		this.drugFlag = drugFlag;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public String getItemUnit() {
		return itemUnit;
	}
	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
	public Double getOwnCost() {
		return ownCost;
	}
	public void setOwnCost(Double ownCost) {
		this.ownCost = ownCost;
	}
	public Double getPayCost() {
		return payCost;
	}
	public void setPayCost(Double payCost) {
		this.payCost = payCost;
	}
	public Double getPubCost() {
		return pubCost;
	}
	public void setPubCost(Double pubCost) {
		this.pubCost = pubCost;
	}
	public Double getOnceDose() {
		return onceDose;
	}
	public void setOnceDose(Double onceDose) {
		this.onceDose = onceDose;
	}
	public String getOnceUnit() {
		return onceUnit;
	}
	public void setOnceUnit(String onceUnit) {
		this.onceUnit = onceUnit;
	}
	public String getDoseModelCode() {
		return doseModelCode;
	}
	public void setDoseModelCode(String doseModelCode) {
		this.doseModelCode = doseModelCode;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getFrequencyName() {
		return frequencyName;
	}
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
	public String getUsageCode() {
		return usageCode;
	}
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}
	public String getUsageName() {
		return usageName;
	}
	public void setUsageName(String usageName) {
		this.usageName = usageName;
	}
	public String getEnglishAb() {
		return englishAb;
	}
	public void setEnglishAb(String englishAb) {
		this.englishAb = englishAb;
	}
	public String getExecDpcd() {
		return execDpcd;
	}
	public void setExecDpcd(String execDpcd) {
		this.execDpcd = execDpcd;
	}
	public String getExecDpnm() {
		return execDpnm;
	}
	public void setExecDpnm(String execDpnm) {
		this.execDpnm = execDpnm;
	}
	public String getMainDrug() {
		return mainDrug;
	}
	public void setMainDrug(String mainDrug) {
		this.mainDrug = mainDrug;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public Integer getInjectNumber() {
		return injectNumber;
	}
	public void setInjectNumber(Integer injectNumber) {
		this.injectNumber = injectNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(String doctCode) {
		this.doctCode = doctCode;
	}
	public String getDoctDpcd() {
		return doctDpcd;
	}
	public void setDoctDpcd(String doctDpcd) {
		this.doctDpcd = doctDpcd;
	}
	public String getEmcFlag() {
		return emcFlag;
	}
	public void setEmcFlag(String emcFlag) {
		this.emcFlag = emcFlag;
	}
	public String getLabType() {
		return labType;
	}
	public void setLabType(String labType) {
		this.labType = labType;
	}
	public String getCheckBody() {
		return checkBody;
	}
	public void setCheckBody(String checkBody) {
		this.checkBody = checkBody;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getConfirmCode() {
		return confirmCode;
	}
	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}
	public String getConfirmDept() {
		return confirmDept;
	}
	public void setConfirmDept(String confirmDept) {
		this.confirmDept = confirmDept;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}