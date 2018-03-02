package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * 
 * 
 * <p>会诊模板实体</p>
 * @Author: XCL
 * @CreateDate: 2017年7月3日 下午6:19:28 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月3日 下午6:19:28 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class InpatientConsultationModel extends Entity implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**模板名称**/
	private String cnslmodName;
	
	/**模板类型**/
	private String cnslmodType;
	
	/**住院科室代码**/
	private String deptCode;
	
	/**护理站代码**/
	private String nurseCellCode;
	
	/**医嘱医师代码**/
	private String docCode;
	
	/**医嘱医师姓名**/
	private String docName;
	
	/**会诊类型**/
	private String cnslKind;
	
	/**加急会诊，1是/0否**/
	private String urgentFlag;
	
	/**会诊科室**/
	private String cnslDeptcd;
	
	/**会诊医师**/
	private String cnslDoccd;
	
	/**会诊说明**/
	private String cnslNote;
	
	/**确认医生代码**/
	private String confirmDoccd;
	
	/**医院名称**/
	private String hosName;
	
	/**紧急说明**/
	private String urgentMemo;
	
	/**简要病历**/
	private String cnslNote1;
	
	/**检查结果**/
	private String cnslNote2;
	
	/**初步诊断意见**/
	private String cnslNote3;
	
	/**会诊地点**/
	private String location;
	
	/**会诊纪录**/
	private String cnslRecord;
	
	/**会诊意见及建议**/
	private String cnslSuggestion;
	
	/**能开立医嘱,1是/0否**/
	private String createOrderFlag;
	
	/**目前诊断(申请)**/
	private String zd1;
	
	/**治疗措施(申请)**/
	private String zl1;
	
	/**会诊目的(申请)**/
	private String hzmd1;
	
	/**诊断(会诊者填写)**/
	private String zd2;
	
	/**处理(会诊者填写)**/
	private String cl2;
	
	public String getCnslmodName() {
		return cnslmodName;
	}
	public void setCnslmodName(String cnslmodName) {
		this.cnslmodName = cnslmodName;
	}
	public String getCnslmodType() {
		return cnslmodType;
	}
	public void setCnslmodType(String cnslmodType) {
		this.cnslmodType = cnslmodType;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
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
	public String getConfirmDoccd() {
		return confirmDoccd;
	}
	public void setConfirmDoccd(String confirmDoccd) {
		this.confirmDoccd = confirmDoccd;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	
}
