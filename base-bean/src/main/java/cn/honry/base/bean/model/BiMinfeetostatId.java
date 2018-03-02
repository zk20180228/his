package cn.honry.base.bean.model;

/**
 * BiMinfeetostatId entity. @author MyEclipse Persistence Tools
 */

public class BiMinfeetostatId implements java.io.Serializable {

	// Fields

	private String id;
	private String reportCode;
	private String reportType;
	private String reportName;
	private String minfeeCode;
	private String minfeeName;
	private String feeStatCode;
	private String feeStatName;
	private String centerStatcode;
	private String centerStateName;
	private String exedeptId;
	private String stopFlg;

	// Constructors

	/** default constructor */
	public BiMinfeetostatId() {
	}

	/** minimal constructor */
	public BiMinfeetostatId(String id) {
		this.id = id;
	}

	/** full constructor */
	public BiMinfeetostatId(String id, String reportCode, String reportType,
			String reportName, String minfeeCode, String minfeeName,
			String feeStatCode, String feeStatName, String centerStatcode,
			String centerStateName, String exedeptId, String stopFlg) {
		this.id = id;
		this.reportCode = reportCode;
		this.reportType = reportType;
		this.reportName = reportName;
		this.minfeeCode = minfeeCode;
		this.minfeeName = minfeeName;
		this.feeStatCode = feeStatCode;
		this.feeStatName = feeStatName;
		this.centerStatcode = centerStatcode;
		this.centerStateName = centerStateName;
		this.exedeptId = exedeptId;
		this.stopFlg = stopFlg;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getMinfeeCode() {
		return this.minfeeCode;
	}

	public void setMinfeeCode(String minfeeCode) {
		this.minfeeCode = minfeeCode;
	}

	public String getMinfeeName() {
		return this.minfeeName;
	}

	public void setMinfeeName(String minfeeName) {
		this.minfeeName = minfeeName;
	}

	public String getFeeStatCode() {
		return this.feeStatCode;
	}

	public void setFeeStatCode(String feeStatCode) {
		this.feeStatCode = feeStatCode;
	}

	public String getFeeStatName() {
		return this.feeStatName;
	}

	public void setFeeStatName(String feeStatName) {
		this.feeStatName = feeStatName;
	}

	public String getCenterStatcode() {
		return this.centerStatcode;
	}

	public void setCenterStatcode(String centerStatcode) {
		this.centerStatcode = centerStatcode;
	}

	public String getCenterStateName() {
		return this.centerStateName;
	}

	public void setCenterStateName(String centerStateName) {
		this.centerStateName = centerStateName;
	}

	public String getExedeptId() {
		return this.exedeptId;
	}

	public void setExedeptId(String exedeptId) {
		this.exedeptId = exedeptId;
	}

	public String getStopFlg() {
		return this.stopFlg;
	}

	public void setStopFlg(String stopFlg) {
		this.stopFlg = stopFlg;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiMinfeetostatId))
			return false;
		BiMinfeetostatId castOther = (BiMinfeetostatId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getReportCode() == castOther.getReportCode()) || (this
						.getReportCode() != null
						&& castOther.getReportCode() != null && this
						.getReportCode().equals(castOther.getReportCode())))
				&& ((this.getReportType() == castOther.getReportType()) || (this
						.getReportType() != null
						&& castOther.getReportType() != null && this
						.getReportType().equals(castOther.getReportType())))
				&& ((this.getReportName() == castOther.getReportName()) || (this
						.getReportName() != null
						&& castOther.getReportName() != null && this
						.getReportName().equals(castOther.getReportName())))
				&& ((this.getMinfeeCode() == castOther.getMinfeeCode()) || (this
						.getMinfeeCode() != null
						&& castOther.getMinfeeCode() != null && this
						.getMinfeeCode().equals(castOther.getMinfeeCode())))
				&& ((this.getMinfeeName() == castOther.getMinfeeName()) || (this
						.getMinfeeName() != null
						&& castOther.getMinfeeName() != null && this
						.getMinfeeName().equals(castOther.getMinfeeName())))
				&& ((this.getFeeStatCode() == castOther.getFeeStatCode()) || (this
						.getFeeStatCode() != null
						&& castOther.getFeeStatCode() != null && this
						.getFeeStatCode().equals(castOther.getFeeStatCode())))
				&& ((this.getFeeStatName() == castOther.getFeeStatName()) || (this
						.getFeeStatName() != null
						&& castOther.getFeeStatName() != null && this
						.getFeeStatName().equals(castOther.getFeeStatName())))
				&& ((this.getCenterStatcode() == castOther.getCenterStatcode()) || (this
						.getCenterStatcode() != null
						&& castOther.getCenterStatcode() != null && this
						.getCenterStatcode().equals(
								castOther.getCenterStatcode())))
				&& ((this.getCenterStateName() == castOther
						.getCenterStateName()) || (this.getCenterStateName() != null
						&& castOther.getCenterStateName() != null && this
						.getCenterStateName().equals(
								castOther.getCenterStateName())))
				&& ((this.getExedeptId() == castOther.getExedeptId()) || (this
						.getExedeptId() != null
						&& castOther.getExedeptId() != null && this
						.getExedeptId().equals(castOther.getExedeptId())))
				&& ((this.getStopFlg() == castOther.getStopFlg()) || (this
						.getStopFlg() != null && castOther.getStopFlg() != null && this
						.getStopFlg().equals(castOther.getStopFlg())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37
				* result
				+ (getReportCode() == null ? 0 : this.getReportCode()
						.hashCode());
		result = 37
				* result
				+ (getReportType() == null ? 0 : this.getReportType()
						.hashCode());
		result = 37
				* result
				+ (getReportName() == null ? 0 : this.getReportName()
						.hashCode());
		result = 37
				* result
				+ (getMinfeeCode() == null ? 0 : this.getMinfeeCode()
						.hashCode());
		result = 37
				* result
				+ (getMinfeeName() == null ? 0 : this.getMinfeeName()
						.hashCode());
		result = 37
				* result
				+ (getFeeStatCode() == null ? 0 : this.getFeeStatCode()
						.hashCode());
		result = 37
				* result
				+ (getFeeStatName() == null ? 0 : this.getFeeStatName()
						.hashCode());
		result = 37
				* result
				+ (getCenterStatcode() == null ? 0 : this.getCenterStatcode()
						.hashCode());
		result = 37
				* result
				+ (getCenterStateName() == null ? 0 : this.getCenterStateName()
						.hashCode());
		result = 37 * result
				+ (getExedeptId() == null ? 0 : this.getExedeptId().hashCode());
		result = 37 * result
				+ (getStopFlg() == null ? 0 : this.getStopFlg().hashCode());
		return result;
	}

}