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
public class TriageVo {
	/**
	 * 分诊级别code
	 */
	private String codeEncode; 
	/**
	 * 分诊级别name
	 */
	
	private String codeName;
	
	public String getCodeEncode() {
		return codeEncode;
	}
	public void setCodeEncode(String codeEncode) {
		this.codeEncode = codeEncode;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
}
