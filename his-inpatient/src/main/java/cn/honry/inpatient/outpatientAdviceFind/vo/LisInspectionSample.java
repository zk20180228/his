package cn.honry.inpatient.outpatientAdviceFind.vo;

public class LisInspectionSample {

	private String INSPECTION_ID; //id
	private String INPATIENT_ID; //病历号
	private String PATIENT_NAME; //姓名
	private String PATIENT_SEX; //性别
	private String AGE_INPUT; //年龄
	private String PATIENT_DEPT_NAME; //科室
	private String TEST_ORDER_NAME; //项目
	
	
	
	
	public String getINSPECTION_ID() {
		return INSPECTION_ID;
	}
	public void setINSPECTION_ID(String iNSPECTION_ID) {
		INSPECTION_ID = iNSPECTION_ID;
	}
	public String getINPATIENT_ID() {
		return INPATIENT_ID;
	}
	public void setINPATIENT_ID(String iNPATIENT_ID) {
		INPATIENT_ID = iNPATIENT_ID;
	}
	public String getPATIENT_NAME() {
		return PATIENT_NAME;
	}
	public void setPATIENT_NAME(String pATIENT_NAME) {
		PATIENT_NAME = pATIENT_NAME;
	}
	public String getPATIENT_SEX() {
		return PATIENT_SEX;
	}
	public void setPATIENT_SEX(String pATIENT_SEX) {
		PATIENT_SEX = pATIENT_SEX;
	}
	public String getAGE_INPUT() {
		return AGE_INPUT;
	}
	public void setAGE_INPUT(String aGE_INPUT) {
		AGE_INPUT = aGE_INPUT;
	}
	public String getPATIENT_DEPT_NAME() {
		return PATIENT_DEPT_NAME;
	}
	public void setPATIENT_DEPT_NAME(String pATIENT_DEPT_NAME) {
		PATIENT_DEPT_NAME = pATIENT_DEPT_NAME;
	}
	public String getTEST_ORDER_NAME() {
		return TEST_ORDER_NAME;
	}
	public void setTEST_ORDER_NAME(String tEST_ORDER_NAME) {
		TEST_ORDER_NAME = tEST_ORDER_NAME;
	}
	
	
}
