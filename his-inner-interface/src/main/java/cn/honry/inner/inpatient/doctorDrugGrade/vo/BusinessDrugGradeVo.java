package cn.honry.inner.inpatient.doctorDrugGrade.vo;

/**
 * @className：BusinessDrugGradeVo
 * @Description:药品等级表相关信息VO
 * @Author: huangbiao
 * @CreateDate: 2016年4月8日
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
public class BusinessDrugGradeVo {
	
	/**
	 * @Description:编号
	 */
	private String id;
	/**
	 * @Description:医药职级
	 */
	private String tpost;
	/**
	 * @Description:职级名称
	 */
	private String postname;
	/**
	 * @Description:药品等级
	 */
	private String druggraade;
	/**
	 * @Description:等级名称
	 */
	private String  graadename;
	/**
	 * @Description:描述
	 */
	private String description;
	/**
	 * @Description:排序
	 */
	private Integer order1;
	/**
	 * @Description:医院ID
	 */
	private Integer  hospitalId;
	/**
	 * @Description:是否适用标识
	 */
	private Integer useFlag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTpost() {
		return tpost;
	}
	public void setTpost(String tpost) {
		this.tpost = tpost;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public String getDruggraade() {
		return druggraade;
	}
	public void setDruggraade(String druggraade) {
		this.druggraade = druggraade;
	}
	public String getGraadename() {
		return graadename;
	}
	public void setGraadename(String graadename) {
		this.graadename = graadename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOrder1() {
		return order1;
	}
	public void setOrder1(Integer order1) {
		this.order1 = order1;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(Integer useFlag) {
		this.useFlag = useFlag;
	}
	
	
}
