package cn.honry.base.bean.model;

/**
 * BiInpatientBalancelistId entity. @author MyEclipse Persistence Tools
 */

public class BiInpatientBalancelistId implements java.io.Serializable {

	// Fields

	private String invoiceNo;
	private Short balanceNo;

	// Constructors

	/** default constructor */
	public BiInpatientBalancelistId() {
	}

	/** full constructor */
	public BiInpatientBalancelistId(String invoiceNo, Short balanceNo) {
		this.invoiceNo = invoiceNo;
		this.balanceNo = balanceNo;
	}

	// Property accessors

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Short getBalanceNo() {
		return this.balanceNo;
	}

	public void setBalanceNo(Short balanceNo) {
		this.balanceNo = balanceNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiInpatientBalancelistId))
			return false;
		BiInpatientBalancelistId castOther = (BiInpatientBalancelistId) other;

		return ((this.getInvoiceNo() == castOther.getInvoiceNo()) || (this
				.getInvoiceNo() != null && castOther.getInvoiceNo() != null && this
				.getInvoiceNo().equals(castOther.getInvoiceNo())))
				&& ((this.getBalanceNo() == castOther.getBalanceNo()) || (this
						.getBalanceNo() != null
						&& castOther.getBalanceNo() != null && this
						.getBalanceNo().equals(castOther.getBalanceNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getInvoiceNo() == null ? 0 : this.getInvoiceNo().hashCode());
		result = 37 * result
				+ (getBalanceNo() == null ? 0 : this.getBalanceNo().hashCode());
		return result;
	}

}