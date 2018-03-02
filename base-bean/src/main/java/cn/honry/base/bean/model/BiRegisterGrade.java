package cn.honry.base.bean.model;

/**
 * BiRegisterGrade entity. @author MyEclipse Persistence Tools
 */

public class BiRegisterGrade implements java.io.Serializable {

	// Fields

	private String gradeId;
	private String hospitalCode;
	private String gradeName;
	private String gradeCode;
	private String gradeTitleCode;
	private String gradeTitle;
	private String gradeIsExpert;
	private String gradeIsFaculty;
	private String gradeIsSpecial;
	private String gradeIsdefault;
	private String validFlag;

	// Constructors

	/** default constructor */
	public BiRegisterGrade() {
	}

	/** full constructor */
	public BiRegisterGrade(String hospitalCode, String gradeName,
			String gradeCode, String gradeTitleCode, String gradeTitle,
			String gradeIsExpert, String gradeIsFaculty, String gradeIsSpecial,
			String gradeIsdefault, String validFlag) {
		this.hospitalCode = hospitalCode;
		this.gradeName = gradeName;
		this.gradeCode = gradeCode;
		this.gradeTitleCode = gradeTitleCode;
		this.gradeTitle = gradeTitle;
		this.gradeIsExpert = gradeIsExpert;
		this.gradeIsFaculty = gradeIsFaculty;
		this.gradeIsSpecial = gradeIsSpecial;
		this.gradeIsdefault = gradeIsdefault;
		this.validFlag = validFlag;
	}

	// Property accessors

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getHospitalCode() {
		return this.hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getGradeName() {
		return this.gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGradeCode() {
		return this.gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeTitleCode() {
		return this.gradeTitleCode;
	}

	public void setGradeTitleCode(String gradeTitleCode) {
		this.gradeTitleCode = gradeTitleCode;
	}

	public String getGradeTitle() {
		return this.gradeTitle;
	}

	public void setGradeTitle(String gradeTitle) {
		this.gradeTitle = gradeTitle;
	}

	public String getGradeIsExpert() {
		return this.gradeIsExpert;
	}

	public void setGradeIsExpert(String gradeIsExpert) {
		this.gradeIsExpert = gradeIsExpert;
	}

	public String getGradeIsFaculty() {
		return this.gradeIsFaculty;
	}

	public void setGradeIsFaculty(String gradeIsFaculty) {
		this.gradeIsFaculty = gradeIsFaculty;
	}

	public String getGradeIsSpecial() {
		return this.gradeIsSpecial;
	}

	public void setGradeIsSpecial(String gradeIsSpecial) {
		this.gradeIsSpecial = gradeIsSpecial;
	}

	public String getGradeIsdefault() {
		return this.gradeIsdefault;
	}

	public void setGradeIsdefault(String gradeIsdefault) {
		this.gradeIsdefault = gradeIsdefault;
	}

	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}