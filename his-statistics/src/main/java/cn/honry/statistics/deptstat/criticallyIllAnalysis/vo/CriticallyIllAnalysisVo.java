package cn.honry.statistics.deptstat.criticallyIllAnalysis.vo;


/**
 *重症患者占比分析VO
 * 
 */
public class CriticallyIllAnalysisVo {
	
	private String  deptCode;//科室
	private String  outPatient;//出院人数
	private String  allPatient;//全院患者
	private String  proportion ;//占比
	private String  illPatient;//重症科室患者
	private String  illPatPro ;//重症占比
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getOutPatient() {
		return outPatient;
	}
	public void setOutPatient(String outPatient) {
		this.outPatient = outPatient;
	}
	public String getAllPatient() {
		return allPatient;
	}
	public void setAllPatient(String allPatient) {
		this.allPatient = allPatient;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getIllPatient() {
		return illPatient;
	}
	public void setIllPatient(String illPatient) {
		this.illPatient = illPatient;
	}
	public String getIllPatPro() {
		return illPatPro;
	}
	public void setIllPatPro(String illPatPro) {
		this.illPatPro = illPatPro;
	}
	
	
}