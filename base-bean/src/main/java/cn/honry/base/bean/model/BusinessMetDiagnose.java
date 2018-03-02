package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 架构:单病种编码表
 * @author donghe
 * Date:2016/01/15 
 */
public class BusinessMetDiagnose extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**住院流水号**/
	private String inpatientNo;
	/**发生序号**/
	private String happenNo;
	/**就诊卡号**/
	private String cardNo;
	/**诊断类别**/
	private String diagKind;
	/**诊断代码**/
	private String icdCode;
	/**诊断名称**/
	private String diagName;
	/**诊断日期**/
	private Date diagDate;
	/**诊断医生代码**/
	private Integer doctCode;
	
	/**诊断医师名称**/
	private String diagDocName;
	/**是否无效 1有效 0无效**/
	private String diagFlag;
	/**科室**/
	private String deptCode;
	/**是否主诊断  1 主诊断 0 其他诊断**/
	private String mainFlag;
	/**手术序号**/
	private String operationno;
	/**备注**/
	private Integer mark;
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(String happenNo) {
		this.happenNo = happenNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getDiagKind() {
		return diagKind;
	}
	public void setDiagKind(String diagKind) {
		this.diagKind = diagKind;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public Date getDiagDate() {
		return diagDate;
	}
	public void setDiagDate(Date diagDate) {
		this.diagDate = diagDate;
	}
	public Integer getDoctCode() {
		return doctCode;
	}
	public void setDoctCode(Integer doctCode) {
		this.doctCode = doctCode;
	}
	public String getDiagDocName() {
		return diagDocName;
	}
	public void setDiagDocName(String diagDocName) {
		this.diagDocName = diagDocName;
	}
	public String getDiagFlag() {
		return diagFlag;
	}
	public void setDiagFlag(String diagFlag) {
		this.diagFlag = diagFlag;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getMainFlag() {
		return mainFlag;
	}
	public void setMainFlag(String mainFlag) {
		this.mainFlag = mainFlag;
	}
	public String getOperationno() {
		return operationno;
	}
	public void setOperationno(String operationno) {
		this.operationno = operationno;
	}
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	
}
