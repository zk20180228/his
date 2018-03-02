package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**  
 * @className：InpatientShiftData.java 
 * @Description：资料变更记录表
 * @Author：hedong
 * @CreateDate：2015-08-12
 * @version 1.0
 */
public class InpatientShiftData extends Entity implements java.io.Serializable{
   	private static final long serialVersionUID = 1L;
	private String clinicNo;//医疗流水号 门诊是挂号单号  住院是住院流水号     VARCHAR2(14),
	private Integer happenNo;//发生序号      NUMBER(5),
	private String shiftType;//RD.转科，RB.转床，K.接诊，C.结算找回，B.住院登记,O 出院,OF 无费退院 BP 结算清单,DG诊断     NUMBER(1),hd 根据doc变更
	private String oldDataCode;//原资料代号  VARCHAR2(20),
	private String oldDataName;//原资料名称   VARCHAR2(64),
	private String newDataCode;//新资料代号   VARCHAR2(20),
	private String newDataName;//新资料名称   VARCHAR2(64),
	private String shiftCause;// 变更原因   VARCHAR2(32),
	private String mark;//备注
	private String tableName;//数据变更表名
	private String changePropertyName;//变更属性名
//	private Date createTime; //创建时间
	/** 新添加字段 医院编码  **/
	private Integer hospitalId;
	/** 新添加字段 院区编码  **/
	private String areaCode;
	
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
	public String getClinicNo() {
		return clinicNo;
	}
	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}
	public Integer getHappenNo() {
		return happenNo;
	}
	public void setHappenNo(Integer happenNo) {
		this.happenNo = happenNo;
	}
	public String getOldDataCode() {
		return oldDataCode;
	}
	public void setOldDataCode(String oldDataCode) {
		this.oldDataCode = oldDataCode;
	}
	public String getOldDataName() {
		return oldDataName;
	}
	public void setOldDataName(String oldDataName) {
		this.oldDataName = oldDataName;
	}
	public String getNewDataCode() {
		return newDataCode;
	}
	public void setNewDataCode(String newDataCode) {
		this.newDataCode = newDataCode;
	}
	public String getNewDataName() {
		return newDataName;
	}
	public void setNewDataName(String newDataName) {
		this.newDataName = newDataName;
	}
	public String getShiftCause() {
		return shiftCause;
	}
	public void setShiftCause(String shiftCause) {
		this.shiftCause = shiftCause;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	public String getShiftType() {
		return shiftType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getChangePropertyName() {
		return changePropertyName;
	}
	public void setChangePropertyName(String changePropertyName) {
		this.changePropertyName = changePropertyName;
	}
//	public Date getCreateTime() {
//		return createTime;
//	}
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
	  
}
