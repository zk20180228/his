package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：InpatientAccountdetail.java 
 * @Description：住院账户操作明细实体
 * @Author：lt
 * @CreateDate：2015-6-30  
 * @version 1.0
 *
 */
public class InpatientAccountdetail extends Entity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields

	private InpatientAccount inpatientAccount;
	private String detailOptype;
	private String detailDeptid;
	private Double detailAmount;
	private Double detailVancancy;
	private String detailInvoiceno;
	private String detailInvoicetype;
	private String detailSummary;
	private Integer detailSourcetype;
	private String detailNachineno;
	private String detailMathinetransno;


	// Property accessors


	public String getDetailOptype() {
		return this.detailOptype;
	}

	public void setDetailOptype(String detailOptype) {
		this.detailOptype = detailOptype;
	}

	public String getDetailDeptid() {
		return this.detailDeptid;
	}

	public void setDetailDeptid(String detailDeptid) {
		this.detailDeptid = detailDeptid;
	}

	public Double getDetailAmount() {
		return this.detailAmount;
	}

	public void setDetailAmount(Double detailAmount) {
		this.detailAmount = detailAmount;
	}

	public Double getDetailVancancy() {
		return this.detailVancancy;
	}

	public void setDetailVancancy(Double detailVancancy) {
		this.detailVancancy = detailVancancy;
	}

	public String getDetailInvoiceno() {
		return this.detailInvoiceno;
	}

	public void setDetailInvoiceno(String detailInvoiceno) {
		this.detailInvoiceno = detailInvoiceno;
	}

	public String getDetailInvoicetype() {
		return this.detailInvoicetype;
	}

	public void setDetailInvoicetype(String detailInvoicetype) {
		this.detailInvoicetype = detailInvoicetype;
	}

	public String getDetailSummary() {
		return this.detailSummary;
	}

	public void setDetailSummary(String detailSummary) {
		this.detailSummary = detailSummary;
	}

	public Integer getDetailSourcetype() {
		return detailSourcetype;
	}

	public void setDetailSourcetype(Integer detailSourcetype) {
		this.detailSourcetype = detailSourcetype;
	}

	public String getDetailNachineno() {
		return this.detailNachineno;
	}

	public void setDetailNachineno(String detailNachineno) {
		this.detailNachineno = detailNachineno;
	}

	public String getDetailMathinetransno() {
		return this.detailMathinetransno;
	}

	public void setDetailMathinetransno(String detailMathinetransno) {
		this.detailMathinetransno = detailMathinetransno;
	}

	public InpatientAccount getInpatientAccount() {
		return inpatientAccount;
	}

	public void setInpatientAccount(InpatientAccount inpatientAccount) {
		this.inpatientAccount = inpatientAccount;
	}

}