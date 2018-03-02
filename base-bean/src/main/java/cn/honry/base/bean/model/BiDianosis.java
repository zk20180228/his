package cn.honry.base.bean.model;

/**
 * BiDianosis entity. @author MyEclipse Persistence Tools
 */

public class BiDianosis implements java.io.Serializable {

	// Fields

	private BiDianosisId id;

	// Constructors

	/** default constructor */
	public BiDianosis() {
	}

	/** full constructor */
	public BiDianosis(BiDianosisId id) {
		this.id = id;
	}

	// Property accessors

	public BiDianosisId getId() {
		return this.id;
	}

	public void setId(BiDianosisId id) {
		this.id = id;
	}

}