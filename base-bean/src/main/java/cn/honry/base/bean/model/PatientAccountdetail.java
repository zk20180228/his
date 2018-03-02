package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

@SuppressWarnings("serial")
public class PatientAccountdetail extends Entity implements java.io.Serializable {

	/**账户编号**/
	private PatientAccount account;
	/**账户分类(1:门诊;2住院)*/
	private Integer detailAccounttype;
	/**摘要**/
	private String detailSummary;
	/**类型：0 预交金 1新建帐户 2停帐户 3重启帐户 4支付 5退费入户 6注销帐户 7授权支付 8退预交金 9修改密码 10结清余额11授权**/
	private String detailOptype;
	/**相关科室**/
	private String detailDeptid;
	/**金额**/
	private Double detailAmount;
	/**交易后余额**/
	private Double detailVancancy;
	/**发票号**/
	private String detailInvoiceno;
	/**发票类型 **/
	private String detailInvoicetype;
	/**1:窗口2自助机**/
	private Boolean detailSourcetype;
	/**自助机器号**/
	private String detailNachineno;
	/**对账流水号**/
	private String detailMathinetransno;

	public String getDetailSummary() {
		return this.detailSummary;
	}

	public void setDetailSummary(String detailSummary) {
		this.detailSummary = detailSummary;
	}

	public PatientAccount getAccount() {
		return account;
	}

	public void setAccount(PatientAccount account) {
		this.account = account;
	}

	public String getDetailOptype() {
		return detailOptype;
	}

	public void setDetailOptype(String detailOptype) {
		this.detailOptype = detailOptype;
	}

	public String getDetailDeptid() {
		return detailDeptid;
	}

	public void setDetailDeptid(String detailDeptid) {
		this.detailDeptid = detailDeptid;
	}

	public Double getDetailAmount() {
		return detailAmount;
	}

	public void setDetailAmount(Double detailAmount) {
		this.detailAmount = detailAmount;
	}

	public Double getDetailVancancy() {
		return detailVancancy;
	}

	public void setDetailVancancy(Double detailVancancy) {
		this.detailVancancy = detailVancancy;
	}

	public String getDetailInvoiceno() {
		return detailInvoiceno;
	}

	public void setDetailInvoiceno(String detailInvoiceno) {
		this.detailInvoiceno = detailInvoiceno;
	}

	public String getDetailInvoicetype() {
		return detailInvoicetype;
	}

	public void setDetailInvoicetype(String detailInvoicetype) {
		this.detailInvoicetype = detailInvoicetype;
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

	public Integer getDetailAccounttype() {
		return detailAccounttype;
	}

	public void setDetailAccounttype(Integer detailAccounttype) {
		this.detailAccounttype = detailAccounttype;
	}
}