package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 住院转入预交金 实体
 * @author  dh
 * @date 创建时间：2016年4月9日
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class InpatientChangeprepay extends Entity implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**转入医疗机构编码**/
	private String changeCode;
	/**转入类型,1 门诊转入，2 住院转入 3 分院转入**/
	private Integer changeType;
	/**医疗流水号**/
	private String clinicNo;
	/**姓名**/
	private String name;
	/**结算类别 01-自费  02-保险 03-公费在职 04-公费退休 05-公费高干**/
	private String paykindCode;
	/**合同单位**/
	private String pactCode;
	/**预交金额**/
	private Double prepayCost;
	/**转入操作员**/
	private String chargeOpercode;
	/**转入日期**/
	private Date chargeDate;
	/**结算序号**/
	private String balanceNo;
	/**结算序号**/
	private String balanceState;
	public String getChangeCode() {
		return changeCode;
	}
	public void setChangeCode(String changeCode) {
		this.changeCode = changeCode;
	}
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPaykindCode() {
		return paykindCode;
	}
	public void setPaykindCode(String paykindCode) {
		this.paykindCode = paykindCode;
	}
	public String getPactCode() {
		return pactCode;
	}
	public void setPactCode(String pactCode) {
		this.pactCode = pactCode;
	}
	public Double getPrepayCost() {
		return prepayCost;
	}
	public void setPrepayCost(Double prepayCost) {
		this.prepayCost = prepayCost;
	}
	public String getChargeOpercode() {
		return chargeOpercode;
	}
	public void setChargeOpercode(String chargeOpercode) {
		this.chargeOpercode = chargeOpercode;
	}
	public Date getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}
	public String getBalanceNo() {
		return balanceNo;
	}
	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}
	public String getBalanceState() {
		return balanceState;
	}
	public void setBalanceState(String balanceState) {
		this.balanceState = balanceState;
	}
	
}
