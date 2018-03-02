package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：InpatientAccountrepaydetail.java 
 * @Description：住院账户金额明细实体
 * @Author：lt
 * @CreateDate：2015-6-30  
 * @version 1.0
 *
 */
public class InpatientAccountrepaydetail extends Entity implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	
	private InpatientAccount inpatientAccount;
	private String detailInvoiceno;
	private Double detailDebitamount;
	private Double detailCreditamount;
	private String detailPaytype;
	private String detailBankunit;
	private String detailBank;
	private String detailBankaccount;
	private String detailBankbillno;
	private Double detailEcocost;
	private Integer detailOptype;
	private Integer detailPrinttimes;
	private String detailRefid;
	private String detailRefflag;
	private String detailOldinvoiceno;
	private Integer detailIshis;
	private Integer detailSourcetype;
	private String detailNachineno;
	private String detailMathinetransno;
	private String detailBanktransno;
	private String detailBalanceno;
	private Integer detailIsbalance;
	private String detailBalanceuser;
	private Date detailBalancetime;
	private Integer detailIsoffbalance;
	private String detailOffbalanceno;
	private String detailOffbalanceuser;
	private Date detailOffbalancetime;

	// Property accessors
	
	public InpatientAccount getInpatientAccount() {
		return inpatientAccount;
	}
	public void setInpatientAccount(InpatientAccount inpatientAccount) {
		this.inpatientAccount = inpatientAccount;
	}
	public String getDetailInvoiceno() {
		return detailInvoiceno;
	}
	public void setDetailInvoiceno(String detailInvoiceno) {
		this.detailInvoiceno = detailInvoiceno;
	}
	public Double getDetailDebitamount() {
		return detailDebitamount;
	}
	public void setDetailDebitamount(Double detailDebitamount) {
		this.detailDebitamount = detailDebitamount;
	}
	public Double getDetailCreditamount() {
		return detailCreditamount;
	}
	public void setDetailCreditamount(Double detailCreditamount) {
		this.detailCreditamount = detailCreditamount;
	}
	public String getDetailPaytype() {
		return detailPaytype;
	}
	public void setDetailPaytype(String detailPaytype) {
		this.detailPaytype = detailPaytype;
	}
	public String getDetailBankunit() {
		return detailBankunit;
	}
	public void setDetailBankunit(String detailBankunit) {
		this.detailBankunit = detailBankunit;
	}
	public String getDetailBank() {
		return detailBank;
	}
	public void setDetailBank(String detailBank) {
		this.detailBank = detailBank;
	}
	public String getDetailBankaccount() {
		return detailBankaccount;
	}
	public void setDetailBankaccount(String detailBankaccount) {
		this.detailBankaccount = detailBankaccount;
	}
	public String getDetailBankbillno() {
		return detailBankbillno;
	}
	public void setDetailBankbillno(String detailBankbillno) {
		this.detailBankbillno = detailBankbillno;
	}
	public Double getDetailEcocost() {
		return detailEcocost;
	}
	public void setDetailEcocost(Double detailEcocost) {
		this.detailEcocost = detailEcocost;
	}
	public Integer getDetailOptype() {
		return detailOptype;
	}
	public void setDetailOptype(Integer detailOptype) {
		this.detailOptype = detailOptype;
	}
	public Integer getDetailPrinttimes() {
		return detailPrinttimes;
	}
	public void setDetailPrinttimes(Integer detailPrinttimes) {
		this.detailPrinttimes = detailPrinttimes;
	}
	public String getDetailRefid() {
		return detailRefid;
	}
	public void setDetailRefid(String detailRefid) {
		this.detailRefid = detailRefid;
	}
	public String getDetailRefflag() {
		return detailRefflag;
	}
	public void setDetailRefflag(String detailRefflag) {
		this.detailRefflag = detailRefflag;
	}
	public String getDetailOldinvoiceno() {
		return detailOldinvoiceno;
	}
	public void setDetailOldinvoiceno(String detailOldinvoiceno) {
		this.detailOldinvoiceno = detailOldinvoiceno;
	}
	public Integer getDetailIshis() {
		return detailIshis;
	}
	public void setDetailIshis(Integer detailIshis) {
		this.detailIshis = detailIshis;
	}
	public Integer getDetailSourcetype() {
		return detailSourcetype;
	}
	public void setDetailSourcetype(Integer detailSourcetype) {
		this.detailSourcetype = detailSourcetype;
	}
	public String getDetailNachineno() {
		return detailNachineno;
	}
	public void setDetailNachineno(String detailNachineno) {
		this.detailNachineno = detailNachineno;
	}
	public String getDetailMathinetransno() {
		return detailMathinetransno;
	}
	public void setDetailMathinetransno(String detailMathinetransno) {
		this.detailMathinetransno = detailMathinetransno;
	}
	public String getDetailBanktransno() {
		return detailBanktransno;
	}
	public void setDetailBanktransno(String detailBanktransno) {
		this.detailBanktransno = detailBanktransno;
	}
	public String getDetailBalanceno() {
		return detailBalanceno;
	}
	public void setDetailBalanceno(String detailBalanceno) {
		this.detailBalanceno = detailBalanceno;
	}
	public Integer getDetailIsbalance() {
		return detailIsbalance;
	}
	public void setDetailIsbalance(Integer detailIsbalance) {
		this.detailIsbalance = detailIsbalance;
	}
	public String getDetailBalanceuser() {
		return detailBalanceuser;
	}
	public void setDetailBalanceuser(String detailBalanceuser) {
		this.detailBalanceuser = detailBalanceuser;
	}
	public Date getDetailBalancetime() {
		return detailBalancetime;
	}
	public void setDetailBalancetime(Date detailBalancetime) {
		this.detailBalancetime = detailBalancetime;
	}
	public Integer getDetailIsoffbalance() {
		return detailIsoffbalance;
	}
	public void setDetailIsoffbalance(Integer detailIsoffbalance) {
		this.detailIsoffbalance = detailIsoffbalance;
	}
	public String getDetailOffbalanceno() {
		return detailOffbalanceno;
	}
	public void setDetailOffbalanceno(String detailOffbalanceno) {
		this.detailOffbalanceno = detailOffbalanceno;
	}
	public String getDetailOffbalanceuser() {
		return detailOffbalanceuser;
	}
	public void setDetailOffbalanceuser(String detailOffbalanceuser) {
		this.detailOffbalanceuser = detailOffbalanceuser;
	}
	public Date getDetailOffbalancetime() {
		return detailOffbalancetime;
	}
	public void setDetailOffbalancetime(Date detailOffbalancetime) {
		this.detailOffbalancetime = detailOffbalancetime;
	}
}