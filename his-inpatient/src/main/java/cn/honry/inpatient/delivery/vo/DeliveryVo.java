package cn.honry.inpatient.delivery.vo;

import java.util.Date;
import java.util.List;

/**
 * 集中发送虚拟实体
 * @author  lyy
 * @createDate： 2016年5月13日 下午5:12:54 
 * @modifier lyy
 * @modifyDate：2016年5月13日 下午5:12:54
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
public class DeliveryVo {
	private String id;
	private String deptName; //取药药房
	//住院登记表
	private String bedId;  //病床号
	private String name;   //患者姓名
	//药品出库申请表
	private Integer applyNumber; //流水号
	private String specs;  //规格
	private String dfqFreq;  //频次
	private String useName;  //用法
	private Integer applyNumSum;  //总量
	private String minUnit;	  //单位
	private String showUnit;//显示的单位
	private Integer showFlag; //显示的单位标记 1 包装单位2最小单位
	private Integer packQty; //包装数量
	
	private String doseOnce;//单次
	private String deptCode;  //申请科室
	private Integer sendType;  //发送类别
	private String billclassCode; //摆药单分类
	private String applyOpercode;  //发药人
	private Date applyDate;  //发药时间
	private String printEmpl;	//发送人
	private Date printDate;  //发送时间
	private Integer validState;	//有效性
	private Integer print;    //申请状态为2是已摆药其他都为未摆药
	private String combNo;  //组合号
	private String drugCode; //药品编码  （判断条件）
	//药品基本信息维护
	private String drugName;  //药品名称
	private String pinyin;  //拼音码
	private String wb;		//五笔码
	//摆药分类
	private String billclassName;   //摆药单名称
	private String choose;   //选择
	private String drugEdBill;    //摆药单号
	private String inpatientNo;  //住院流水号
	
	private String drugDeptName;//取药科室名称
	
	private String printEmplName;//发送人姓名
	private String drugedEmplName;//发药人名称
	private String applyDeptName;//申请科室
	private List<DeliveryVo> deliverList;
	private Double RetailPrice;//金额
	
	
	public String getShowUnit() {
		return showUnit;
	}
	public void setShowUnit(String showUnit) {
		this.showUnit = showUnit;
	}
	public Integer getPackQty() {
		return packQty;
	}
	public void setPackQty(Integer packQty) {
		this.packQty = packQty;
	}
	public Integer getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}
	public Double getRetailPrice() {
		return RetailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		RetailPrice = retailPrice;
	}
	public String getDoseOnce() {
		return doseOnce;
	}
	public void setDoseOnce(String doseOnce) {
		this.doseOnce = doseOnce;
	}
	public List<DeliveryVo> getDeliverList() {
		return deliverList;
	}
	public void setDeliverList(List<DeliveryVo> deliverList) {
		this.deliverList = deliverList;
	}
	public String getPrintEmplName() {
		return printEmplName;
	}
	public void setPrintEmplName(String printEmplName) {
		this.printEmplName = printEmplName;
	}
	public String getDrugedEmplName() {
		return drugedEmplName;
	}
	public void setDrugedEmplName(String drugedEmplName) {
		this.drugedEmplName = drugedEmplName;
	}
	public String getDrugDeptName() {
		return drugDeptName;
	}
	public void setDrugDeptName(String drugDeptName) {
		this.drugDeptName = drugDeptName;
	}
	public String getApplyDeptName() {
		return applyDeptName;
	}
	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getDrugEdBill() {
		return drugEdBill;
	}
	public void setDrugEdBill(String drugEdBill) {
		this.drugEdBill = drugEdBill;
	}
	public String getChoose() {
		return choose;
	}
	public void setChoose(String choose) {
		this.choose = choose;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBillclassName() {
		return billclassName;
	}
	public void setBillclassName(String billclassName) {
		this.billclassName = billclassName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getApplyNumber() {
		return applyNumber;
	}
	public void setApplyNumber(Integer applyNumber) {
		this.applyNumber = applyNumber;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getDfqFreq() {
		return dfqFreq;
	}
	public void setDfqFreq(String dfqFreq) {
		this.dfqFreq = dfqFreq;
	}
	public String getUseName() {
		return useName;
	}
	public void setUseName(String useName) {
		this.useName = useName;
	}
	public Integer getApplyNumSum() {
		return applyNumSum;
	}
	public void setApplyNumSum(Integer applyNumSum) {
		this.applyNumSum = applyNumSum;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
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
	public Integer getValidState() {
		return validState;
	}
	public void setValidState(Integer validState) {
		this.validState = validState;
	}
	public Integer getPrint() {
		return print;
	}
	public void setPrint(Integer print) {
		this.print = print;
	}
	public String getCombNo() {
		return combNo;
	}
	public void setCombNo(String combNo) {
		this.combNo = combNo;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
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
	@Override
	public String toString() {
		return "DeliveryVo [id=" + id + ", deptName=" + deptName + ", bedId="
				+ bedId + ", name=" + name + ", applyNumber=" + applyNumber
				+ ", specs=" + specs + ", dfqFreq=" + dfqFreq + ", useName="
				+ useName + ", applyNumSum=" + applyNumSum + ", minUnit="
				+ minUnit + ", doseOnce=" + doseOnce + ", deptCode=" + deptCode
				+ ", sendType=" + sendType + ", billclassCode=" + billclassCode
				+ ", applyOpercode=" + applyOpercode + ", applyDate="
				+ applyDate + ", printEmpl=" + printEmpl + ", printDate="
				+ printDate + ", validState=" + validState + ", print=" + print
				+ ", combNo=" + combNo + ", drugCode=" + drugCode
				+ ", drugName=" + drugName + ", pinyin=" + pinyin + ", wb="
				+ wb + ", billclassName=" + billclassName + ", choose="
				+ choose + ", drugEdBill=" + drugEdBill + ", inpatientNo="
				+ inpatientNo + ", drugDeptName=" + drugDeptName
				+ ", printEmplName=" + printEmplName + ", drugedEmplName="
				+ drugedEmplName + ", applyDeptName=" + applyDeptName
				+ ", deliverList=" + deliverList + ", RetailPrice="
				+ RetailPrice + "]";
	}
	
	
}
