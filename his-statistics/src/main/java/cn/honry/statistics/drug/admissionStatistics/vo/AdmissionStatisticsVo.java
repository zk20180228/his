package cn.honry.statistics.drug.admissionStatistics.vo;
/***
 * 用药统计vo层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年6月22日 
 * @version 1.0
 */
public class AdmissionStatisticsVo {
	/**
	 * 基本药品码
	 */
	private String drugBasicCode;
	
	/**
	 * 招标流水号
	 */
	private String drugBiddingCode;
	
	/**
	 * 药品编码
	 */
	private String drugId;
	
	/**
	 * 名称
	 */
	private String drugName;
	
	/**
	 * 当前状态
	 */
	private String outState;
	
	/**
	 * 摆药状态
	 */
	private String opType;
	
	/**
	 * 规格
	 */
	private String drugSpec;
	
	/**
	 * 数量
	 */
	private Double num;
	
	/**
	 * 包装单位
	 */
	private String drugPackgingUnit;
	
	/**
	 * 金额
	 */
	private Double sum;
	
	/**
	 * 零售价
	 */
	private Double retailPrice;

	public String getDrugBasicCode() {
		return drugBasicCode;
	}

	public void setDrugBasicCode(String drugBasicCode) {
		this.drugBasicCode = drugBasicCode;
	}

	public String getDrugBiddingCode() {
		return drugBiddingCode;
	}

	public void setDrugBiddingCode(String drugBiddingCode) {
		this.drugBiddingCode = drugBiddingCode;
	}

	public String getDrugId() {
		return drugId;
	}

	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getOutState() {
		return outState;
	}

	public void setOutState(String outState) {
		this.outState = outState;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getDrugSpec() {
		return drugSpec;
	}

	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public String getDrugPackgingUnit() {
		return drugPackgingUnit;
	}

	public void setDrugPackgingUnit(String drugPackgingUnit) {
		this.drugPackgingUnit = drugPackgingUnit;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public Double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	
}
