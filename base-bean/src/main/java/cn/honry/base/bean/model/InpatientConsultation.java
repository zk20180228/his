package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;
/**  
 *  
 * @className：InpatientConsultation.java 
 * @Author：ygq
 * @CreateDate：2015-12-9  
 * @version 1.0
 *
 */
public class InpatientConsultation extends Entity implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	/**会诊流水号*/
	private String cnslNO;
	/**住院流水号(6位日期+6位流水号)*/
	private String inpatientNo;
	/**住院病历号*/
	private String patientNo;
	/**住院科室代码*/
	private String deptCode;
	/**护理站代码*/
	private String nueseCellCode;
	/**医嘱医师代号*/
	private String docCode;
	/**医嘱医师姓名*/
	private String docName; 
	/**填写申请日期*/
	private Date applyDate;
	/**预约会诊日期*/
	private Date cnslDate;
	/**会诊类型，1科室/0医生 2 院外会诊*/
	private String cnslKind;
	/**加急会诊,1是/0否*/
	private String urgentFlag;
	/**会诊科室*/
	private String cnslDeptcd;
	/**会诊医师*/
	private String cnslDoccd;
	/**会诊说明*/
	private String cnslNote;
	/**处方起始日*/
	private Date moStdt;
	/**处方结束日*/
	private Date moEddt;
	/**实际会诊日*/
	private Date cnslExdt;
	/**会诊结果*/
	private String cnslRslt;
	/**确认医生代码*/
	private String confirmDoccd;
	/**会诊状态,1申请/2确认*/
	private String cnslStatus;
	/**用户代码*/
	private String operCode;
	/**医院名称*/
	private String hosName;
	/**紧急说明*/
	private String urgentMemo;
	/**简要病历*/
	private String cnslNote1;
	/**检查结果*/
	private String cnslNote2;
	/**初步诊断意见*/
	private String cnslNote3;
	/**会诊地点*/
	private String location;
	/**患者床号*/
	private String bedNo;
	/**会诊纪录*/
	private String cnslRecord;
	/**会诊意见及建议*/
	private String cnslSuggestion;
	/**能开立医嘱,1是/0否*/
	private String createOrderFlag;
	/**目前诊断(申请)*/
	private String zd1;
	/**治疗措施(申请)*/
	private String zl1;
	/**会诊目的(申请)*/
	private String hzmd1;
	/**诊断(会诊者填写)*/
	private String zd2;
	/**处理(会诊者填写)*/
	private String cl2;
	 //新增字段 2017-06-12
   /**医院编码**/
   private Integer hospitalId;
   /**院区编码吗**/
   private String areaCode;
	public String getCnslNO() {
		return cnslNO;
	}
	public void setCnslNO(String cnslNO) {
		this.cnslNO = cnslNO;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getNueseCellCode() {
		return nueseCellCode;
	}
	public void setNueseCellCode(String nueseCellCode) {
		this.nueseCellCode = nueseCellCode;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getCnslDate() {
		return cnslDate;
	}
	public void setCnslDate(Date cnslDate) {
		this.cnslDate = cnslDate;
	}
	public String getCnslKind() {
		return cnslKind;
	}
	public void setCnslKind(String cnslKind) {
		this.cnslKind = cnslKind;
	}
	public String getUrgentFlag() {
		return urgentFlag;
	}
	public void setUrgentFlag(String urgentFlag) {
		this.urgentFlag = urgentFlag;
	}
	public String getCnslDeptcd() {
		return cnslDeptcd;
	}
	public void setCnslDeptcd(String cnslDeptcd) {
		this.cnslDeptcd = cnslDeptcd;
	}
	public String getCnslDoccd() {
		return cnslDoccd;
	}
	public void setCnslDoccd(String cnslDoccd) {
		this.cnslDoccd = cnslDoccd;
	}
	public String getCnslNote() {
		return cnslNote;
	}
	public void setCnslNote(String cnslNote) {
		this.cnslNote = cnslNote;
	}
	public Date getMoStdt() {
		return moStdt;
	}
	public void setMoStdt(Date moStdt) {
		this.moStdt = moStdt;
	}
	public Date getMoEddt() {
		return moEddt;
	}
	public void setMoEddt(Date moEddt) {
		this.moEddt = moEddt;
	}
	public Date getCnslExdt() {
		return cnslExdt;
	}
	public void setCnslExdt(Date cnslExdt) {
		this.cnslExdt = cnslExdt;
	}
	public String getCnslRslt() {
		return cnslRslt;
	}
	public void setCnslRslt(String cnslRslt) {
		this.cnslRslt = cnslRslt;
	}
	public String getConfirmDoccd() {
		return confirmDoccd;
	}
	public void setConfirmDoccd(String confirmDoccd) {
		this.confirmDoccd = confirmDoccd;
	}
	public String getCnslStatus() {
		return cnslStatus;
	}
	public void setCnslStatus(String cnslStatus) {
		this.cnslStatus = cnslStatus;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public String getHosName() {
		return hosName;
	}
	public void setHosName(String hosName) {
		this.hosName = hosName;
	}
	public String getUrgentMemo() {
		return urgentMemo;
	}
	public void setUrgentMemo(String urgentMemo) {
		this.urgentMemo = urgentMemo;
	}
	public String getCnslNote1() {
		return cnslNote1;
	}
	public void setCnslNote1(String cnslNote1) {
		this.cnslNote1 = cnslNote1;
	}
	public String getCnslNote2() {
		return cnslNote2;
	}
	public void setCnslNote2(String cnslNote2) {
		this.cnslNote2 = cnslNote2;
	}
	public String getCnslNote3() {
		return cnslNote3;
	}
	public void setCnslNote3(String cnslNote3) {
		this.cnslNote3 = cnslNote3;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getCnslRecord() {
		return cnslRecord;
	}
	public void setCnslRecord(String cnslRecord) {
		this.cnslRecord = cnslRecord;
	}
	public String getCnslSuggestion() {
		return cnslSuggestion;
	}
	public void setCnslSuggestion(String cnslSuggestion) {
		this.cnslSuggestion = cnslSuggestion;
	}
	public String getCreateOrderFlag() {
		return createOrderFlag;
	}
	public void setCreateOrderFlag(String createOrderFlag) {
		this.createOrderFlag = createOrderFlag;
	}
	public String getZd1() {
		return zd1;
	}
	public void setZd1(String zd1) {
		this.zd1 = zd1;
	}
	public String getZl1() {
		return zl1;
	}
	public void setZl1(String zl1) {
		this.zl1 = zl1;
	}
	public String getHzmd1() {
		return hzmd1;
	}
	public void setHzmd1(String hzmd1) {
		this.hzmd1 = hzmd1;
	}
	public String getZd2() {
		return zd2;
	}
	public void setZd2(String zd2) {
		this.zd2 = zd2;
	}
	public String getCl2() {
		return cl2;
	}
	public void setCl2(String cl2) {
		this.cl2 = cl2;
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
