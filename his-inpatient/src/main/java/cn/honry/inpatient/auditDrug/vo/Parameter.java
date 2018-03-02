package cn.honry.inpatient.auditDrug.vo;
/**  
 * @Description：  前后台传值参数
 * @Author：ldl
 * @CreateDate：2016-04-29
 * @ModifyRmk：  
 * @version 1.0
 */
public class Parameter {
	/**系统参数 是否摆药时需要核准*/
	private String parameterHz;
	/**系统参数 退药时是直接退费 还是发起退费申请*/
	private String parameterTf;
	/**发送类型*/
	private Integer sendType;
	/**所属摆药单类型代码集合*/
	private String  billclassCode;
	/**进入页面默认摆药台*/
	private String controlId;
	/**摆药台分类 1摆药台 2退药台*/
	private Integer opType;
	/**病历号*/
	private String medicalrecordId;
	/**摆药单号*/
	private String drugedBill;
	/**摆药单通知类型*/
	private String sendFlag;
	/**申请类型*/
	private String applyState;
	/**摆药台数据显示*/
	private Integer showLevel;
	/**摆药单对应科室*/
	private String deptId;
	/**申请单号集合*/
	private String applyNumberCode;
	/**出库单Id集合*/
	private String ids;
	/**就诊卡号*/
	private String idCard;
	
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getParameterHz() {
		return parameterHz;
	}
	public void setParameterHz(String parameterHz) {
		this.parameterHz = parameterHz;
	}
	public String getParameterTf() {
		return parameterTf;
	}
	public void setParameterTf(String parameterTf) {
		this.parameterTf = parameterTf;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public String getBillclassCode() {
		return billclassCode;
	}
	public void setBillclassCode(String billclassCode) {
		this.billclassCode = billclassCode;
	}
	public String getControlId() {
		return controlId;
	}
	public void setControlId(String controlId) {
		this.controlId = controlId;
	}
	public Integer getOpType() {
		return opType;
	}
	public void setOpType(Integer opType) {
		this.opType = opType;
	}
	public String getMedicalrecordId() {
		return medicalrecordId;
	}
	public void setMedicalrecordId(String medicalrecordId) {
		this.medicalrecordId = medicalrecordId;
	}
	public String getDrugedBill() {
		return drugedBill;
	}
	public void setDrugedBill(String drugedBill) {
		this.drugedBill = drugedBill;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	public String getApplyState() {
		return applyState;
	}
	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}
	public Integer getShowLevel() {
		return showLevel;
	}
	public void setShowLevel(Integer showLevel) {
		this.showLevel = showLevel;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getApplyNumberCode() {
		return applyNumberCode;
	}
	public void setApplyNumberCode(String applyNumberCode) {
		this.applyNumberCode = applyNumberCode;
	}
	
}
