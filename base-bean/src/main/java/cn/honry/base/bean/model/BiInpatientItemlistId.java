package cn.honry.base.bean.model;

/**
 * BiInpatientItemlistId entity. @author MyEclipse Persistence Tools
 */

public class BiInpatientItemlistId implements java.io.Serializable {

	// Fields

	private String recipeNo;
	private Short sequenceNo;

	// Constructors

	/** default constructor */
	public BiInpatientItemlistId() {
	}

	/** full constructor */
	public BiInpatientItemlistId(String recipeNo, Short sequenceNo) {
		this.recipeNo = recipeNo;
		this.sequenceNo = sequenceNo;
	}

	// Property accessors

	public String getRecipeNo() {
		return this.recipeNo;
	}

	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}

	public Short getSequenceNo() {
		return this.sequenceNo;
	}

	public void setSequenceNo(Short sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BiInpatientItemlistId))
			return false;
		BiInpatientItemlistId castOther = (BiInpatientItemlistId) other;

		return ((this.getRecipeNo() == castOther.getRecipeNo()) || (this
				.getRecipeNo() != null && castOther.getRecipeNo() != null && this
				.getRecipeNo().equals(castOther.getRecipeNo())))
				&& ((this.getSequenceNo() == castOther.getSequenceNo()) || (this
						.getSequenceNo() != null
						&& castOther.getSequenceNo() != null && this
						.getSequenceNo().equals(castOther.getSequenceNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRecipeNo() == null ? 0 : this.getRecipeNo().hashCode());
		result = 37
				* result
				+ (getSequenceNo() == null ? 0 : this.getSequenceNo()
						.hashCode());
		return result;
	}

}