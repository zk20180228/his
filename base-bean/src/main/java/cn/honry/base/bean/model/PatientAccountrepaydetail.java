package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @className：PatientAccountrepaydetail.java 
 * @Author：lt
 * @CreateDate：2015-6-25  
 * @Modifier：lt
 * @ModifyDate：2015-6-25  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class PatientAccountrepaydetail extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/**账户编号**/
	private PatientAccount account;
	/**账户分类(1:门诊;2住院)*/
	private Integer detailAccounttype;
	/**票据号**/
	private String detailInvoiceno;
	/**小票号**/
	private String detailBankbillno;
	/**优惠金额**/
	private Double detailEcocost;
	/**2反还，1收取，3重打，4结清账户**/
	private int detailOptype;
	/**重打次数**/
	private Integer detailPrinttimes=0;
	/**相关标志**/
	private String detailRefflag;
	/**原票据号**/
	private String detailOldinvoiceno;
	/**1:窗口2自助机**/
	private Boolean detailSourcetype;
	/**自助机器号**/
	private String detailNachineno;
	/**对账流水号**/
	private String detailMathinetransno;
	/**银行交易流水号**/
	private String detailBanktransno;
	/**日结编号**/
	private String detailBalanceno;
	/**是否日结**/
	private Boolean detailIsbalance;
	/**日结人**/
	private String detailBalanceuser;
	/**日结时间**/
	private Date detailBalancetime;
	/**清仓标志**/
	private Boolean detailIsoffbalance;
	/**清仓号**/
	private String detailOffbalanceno;
	/**清仓人**/
	private String detailOffbalanceuser;
	/**清仓时间**/
	private Date detailOffbalancetime;
	
	/**充值金额**/
	private Double detailDebitamount;
	/**消费金额**/
	private Double detailCreditamount;
	/**微信支付；支付宝支付；银联支付等，从编码表读取**/
	private String detailPaytype;
	/**开户单位**/
	private String detailBankunit;
	/**开户银行:从编码表里读取**/
	private String detailBank;
	/**银行账号**/
	private String detailBankaccount;
	/**是否历史**/
	private int detailIshis;
	/**关联的编号：例如挂号编号**/
	private String detailRefid;
	/**借方类型   门诊分有预存金(0)、;收费(1)、退费(2)、住院分有住院住院预交金(4)、转入费用 (5)、担保金额(6)、 转入预存金(7) 、 转押金(8)**/
	private Integer detailDepitamounttType=0;
	/**结算序号**/
	private String balanceSeq;
	
	public PatientAccount getAccount() {
		return account;
	}
	public void setAccount(PatientAccount account) {
		this.account = account;
	}
	public String getDetailInvoiceno() {
		return detailInvoiceno;
	}
	public void setDetailInvoiceno(String detailInvoiceno) {
		this.detailInvoiceno = detailInvoiceno;
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
	public int getDetailOptype() {
		return detailOptype;
	}
	public void setDetailOptype(int detailOptype) {
		this.detailOptype = detailOptype;
	}
	public Integer getDetailPrinttimes() {
		return detailPrinttimes;
	}
	public void setDetailPrinttimes(Integer detailPrinttimes) {
		this.detailPrinttimes = detailPrinttimes;
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
	public Boolean getDetailSourcetype() {
		return detailSourcetype;
	}
	public void setDetailSourcetype(Boolean detailSourcetype) {
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
	public Boolean getDetailIsbalance() {
		return detailIsbalance;
	}
	public void setDetailIsbalance(Boolean detailIsbalance) {
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
	public Boolean getDetailIsoffbalance() {
		return detailIsoffbalance;
	}
	public void setDetailIsoffbalance(Boolean detailIsoffbalance) {
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
	
	protected int getDetailIshis() {
		return detailIshis;
	}
	public String getDetailRefid() {
		return detailRefid;
	}
	public void setDetailIshis(int detailIshis) {
		this.detailIshis = detailIshis;
	}
	public void setDetailRefid(String detailRefid) {
		this.detailRefid = detailRefid;
	}
	public Integer getDetailAccounttype() {
		return detailAccounttype;
	}
	public void setDetailAccounttype(Integer detailAccounttype) {
		this.detailAccounttype = detailAccounttype;
	}
	public Integer getDetailDepitamounttType() {
		return detailDepitamounttType;
	}
	public void setDetailDepitamounttType(Integer detailDepitamounttType) {
		this.detailDepitamounttType = detailDepitamounttType;
	}
	public String getBalanceSeq() {
		return balanceSeq;
	}
	public void setBalanceSeq(String balanceSeq) {
		this.balanceSeq = balanceSeq;
	}
	
}