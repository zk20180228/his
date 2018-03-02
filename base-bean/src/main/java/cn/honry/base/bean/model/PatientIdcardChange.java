package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


@SuppressWarnings("serial")
public class PatientIdcardChange extends Entity implements java.io.Serializable {

	// Fields
	/**就诊卡编号 **/
	private String idcard_Id;
	/**患者编号*/
	private String patient;
	/**旧卡卡号*/
	private String oldIdcardNo;
	/**旧卡卡类型1:磁卡2;IC卡3保障卡，从编码表里读取*/
	private String oldIdcardType;
	/**新卡卡号*/
	private String idcardNo;
	/**新卡卡类型1:磁卡2;IC卡3保障卡，从编码表里读取*/
	private String idcardType;
	/**变更状态 1补卡,2退卡**/
	private Integer changeStatus;

	public String getIdcard_Id() {
		return idcard_Id;
	}
	public void setIdcard_Id(String idcard_Id) {
		this.idcard_Id = idcard_Id;
	}
	public String getPatient() {
		return patient;
	}
	public void setPatient(String patient) {
		this.patient = patient;
	}
	public String getOldIdcardNo() {
		return oldIdcardNo;
	}
	public void setOldIdcardNo(String oldIdcardNo) {
		this.oldIdcardNo = oldIdcardNo;
	}
	public String getOldIdcardType() {
		return oldIdcardType;
	}
	public void setOldIdcardType(String oldIdcardType) {
		this.oldIdcardType = oldIdcardType;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getIdcardType() {
		return idcardType;
	}
	public void setIdcardType(String idcardType) {
		this.idcardType = idcardType;
	}
	public Integer getChangeStatus() {
		return changeStatus;
	}
	public void setChangeStatus(Integer changeStatus) {
		this.changeStatus = changeStatus;
	}
	
}
	