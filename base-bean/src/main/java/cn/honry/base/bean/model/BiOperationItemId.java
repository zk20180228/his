package cn.honry.base.bean.model;

/**
 * BiOperationItemId entity. @author MyEclipse Persistence Tools
 */

public class BiOperationItemId implements java.io.Serializable {

	// Fields

	private String operationId;
	private Long happenNo;

	// Constructors

	/** default constructor */
	public BiOperationItemId() {
	}

	/** full constructor */
	public BiOperationItemId(String operationId, Long happenNo) {
		this.operationId = operationId;
		this.happenNo = happenNo;
	}

	// Property accessors

	public String getOperationId() {
		return this.operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public Long getHappenNo() {
		return this.happenNo;
	}

	public void setHappenNo(Long happenNo) {
		this.happenNo = happenNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiOperationItemId))
			return false;
		BiOperationItemId castOther = (BiOperationItemId) other;

		return ((this.getOperationId() == castOther.getOperationId()) || (this
				.getOperationId() != null && castOther.getOperationId() != null && this
				.getOperationId().equals(castOther.getOperationId())))
				&& ((this.getHappenNo() == castOther.getHappenNo()) || (this
						.getHappenNo() != null
						&& castOther.getHappenNo() != null && this
						.getHappenNo().equals(castOther.getHappenNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getOperationId() == null ? 0 : this.getOperationId()
						.hashCode());
		result = 37 * result
				+ (getHappenNo() == null ? 0 : this.getHappenNo().hashCode());
		return result;
	}

}