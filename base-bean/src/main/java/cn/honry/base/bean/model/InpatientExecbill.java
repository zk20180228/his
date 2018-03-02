package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @className：InpatientExecbill.java 
 * @Author：lt
 * @CreateDate：2015-12-12  
 * @version 1.0
 *
 */
public class InpatientExecbill extends Entity implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;

	private String nurseCellCode;
	private String billNo;
	private String billName;
	private String billKind;
	private String mark;
	private String itemFlag;
	//新加字段
	/**护理站名称**/
	private String nurseCellName;
	//新增字段 2017-06-12
   /**医院编码**/
   private Integer hospitalId;
   /**院区编码吗**/
   private String areaCode;
	public String getNurseCellName() {
		return nurseCellName;
	}
	public void setNurseCellName(String nurseCellName) {
		this.nurseCellName = nurseCellName;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getBillKind() {
		return billKind;
	}
	public void setBillKind(String billKind) {
		this.billKind = billKind;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getItemFlag() {
		return itemFlag;
	}
	public void setItemFlag(String itemFlag) {
		this.itemFlag = itemFlag;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
}
