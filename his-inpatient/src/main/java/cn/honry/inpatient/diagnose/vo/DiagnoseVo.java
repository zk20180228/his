package cn.honry.inpatient.diagnose.vo;

import java.util.Date;



public class DiagnoseVo {
	private String detailId;//T_BUSINESS_DIAGNOSE(id)
	private String diagKind;//诊断类型
	private String diagDoct;//诊断医生
	private Date diagDate;//诊断时间
	private Integer mainFlay;//是否主诊断1是0否
	private String icdCode;//诊断icd码(T_BUSINESS_DIAGNOSE)
	private String diagCode;//诊断代码(T_BUSINESS_DIAGNOSE_MEDICARE)
	private String diagName;//诊断名称
	private Integer icdType;//icd类别
	private String  icdDiagCode;//icd10表中的icd码；
	private String  code;//T_BUSINESS_ICDMEDICARE中诊断代码
	private String id;//医保ID,icd诊断id
	private String icdId;//icd10表id
	private Integer main;//是否主诊断1是0否(隐藏)
	
	
	
	public Integer getMain() {
		return main;
	}
	public void setMain(Integer main) {
		this.main = main;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIcdId() {
		return icdId;
	}
	public void setIcdId(String icdId) {
		this.icdId = icdId;
	}
	public String getIcdDiagCode() {
		return icdDiagCode;
	}
	public void setIcdDiagCode(String icdDiagCode) {
		this.icdDiagCode = icdDiagCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getMainFlay() {
		return mainFlay;
	}
	public void setMainFlay(Integer mainFlay) {
		this.mainFlay = mainFlay;
	}
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	public String getDiagKind() {
		return diagKind;
	}
	public void setDiagKind(String diagKind) {
		this.diagKind = diagKind;
	}
	public String getDiagDoct() {
		return diagDoct;
	}
	public void setDiagDoct(String diagDoct) {
		this.diagDoct = diagDoct;
	}
	public Date getDiagDate() {
		return diagDate;
	}
	public void setDiagDate(Date diagDate) {
		this.diagDate = diagDate;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public String getDiagCode() {
		return diagCode;
	}
	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public Integer getIcdType() {
		return icdType;
	}
	public void setIcdType(Integer icdType) {
		this.icdType = icdType;
	}
	

}
