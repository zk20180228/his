package cn.honry.base.bean.model;

/**
 * BiVitalSigns entity. @author MyEclipse Persistence Tools
 */

public class BiVitalSigns implements java.io.Serializable {

	// Fields

	private BiVitalSignsId id;

	// Constructors

	/** default constructor */
	public BiVitalSigns() {
	}

	/** full constructor */
	public BiVitalSigns(BiVitalSignsId id) {
		this.id = id;
	}

	// Property accessors

	public BiVitalSignsId getId() {
		return this.id;
	}

	public void setId(BiVitalSignsId id) {
		this.id = id;
	}

}