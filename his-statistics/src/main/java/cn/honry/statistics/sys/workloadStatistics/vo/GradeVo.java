package cn.honry.statistics.sys.workloadStatistics.vo;



/**
 * @Description 查询医生级别VO
 * @author  marongbin
 * @createDate： 2016年12月20日 下午6:04:14 
 * @modifier 
 * @modifyDate：
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public class GradeVo {
	/**
	 * 级别code
	 */
	private String gradeCode; 
	/**
	 * 级别name
	 */
	
	private String codeName;
	public String getGradeCode() {
		return gradeCode;
	}
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
}
