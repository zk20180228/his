package cn.honry.base.bean.model;


import cn.honry.base.bean.business.Entity;

/**
 * 手术诊断
 * @author zhangjin
 * @CreateDate：2016-04-11 
 *
 */
public class OperationDiagnose extends Entity{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 手术序号
	 */
	private String operationId;
	/**
	 * 住院流水号
	 */
	private String inpatientNo;
	/**
	 * 发生序号
	 */
	private Integer happenNo;
	/**
	 * 就诊卡号/病历号
	 */
	private String cardNo;
	/**
	 * 诊断类别
	 */
	private String diagKind;
	/**
	 * 诊断代码
	 */
	private String icdCode;
	/**
	 * 诊断名称
	 */
	private String diagName;
	/**
	 * 是否无效 1有效 0无效
	 */
	private String diagFlag;

	public String getOperationId() {
		return this.operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getInpatientNo() {
		return this.inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public Integer getHappenNo() {
		return this.happenNo;
	}

	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getDiagKind() {
		return this.diagKind;
	}

	public void setDiagKind(String diagKind) {
		this.diagKind = diagKind;
	}

	public String getIcdCode() {
		return this.icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public String getDiagName() {
		return this.diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

	public String getDiagFlag() {
		return this.diagFlag;
	}

	public void setDiagFlag(String diagFlag) {
		this.diagFlag = diagFlag;
	}

}