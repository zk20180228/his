package cn.honry.outpatient.observation.vo;

import java.io.Serializable;
/**
 * 
 * <p>门诊留观vo </p>
 * @Author: zhangkui
 * @CreateDate: 2017年11月16日 上午11:20:27 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年11月16日 上午11:20:27 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class ObservationVo implements Serializable{

	private static final long serialVersionUID = -8497733487464802996L;
	
	private String cardNo="";//就诊卡号
	private String patientNo="";//病历号
	private String observationIntime;//留观开始时间
	private String observationOuttime;//留观结束时间
	private Integer observationDays;//留观天数，单位：天
	private String diagnosis="";//诊断
	private String lapseTo="";//转归
	private String doctorName="";//责任医师名字
	private String doctorCode="";//责任医师code
	private String nurseName="";//责任护士名字
	private String nurseCode="";//责任护士code
	
	/**唯一编号(主键)**/
	private String id;
	/**建立人员**/
	private String createUser="";
	/**建立部门**/
	private String createDept ="";
	/**建立时间**/
	private String createTime;
	/**修改人员**/
	private String updateUser="";
	/**修改时间**/
	private String updateTime;
	/**删除人员**/
	private String deleteUser="";
	/**删除时间**/
	private String deleteTime;	
	/**停用标志**/
	private Integer stop_flg=0;
	/**删除标志**/
	private Integer del_flg=0;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getObservationIntime() {
		return observationIntime;
	}
	public void setObservationIntime(String observationIntime) {
		this.observationIntime = observationIntime;
	}
	public String getObservationOuttime() {
		return observationOuttime;
	}
	public void setObservationOuttime(String observationOuttime) {
		this.observationOuttime = observationOuttime;
	}
	public Integer getObservationDays() {
		return observationDays;
	}
	public void setObservationDays(Integer observationDays) {
		this.observationDays = observationDays;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getLapseTo() {
		return lapseTo;
	}
	public void setLapseTo(String lapseTo) {
		this.lapseTo = lapseTo;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getNurseName() {
		return nurseName;
	}
	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}
	public String getNurseCode() {
		return nurseCode;
	}
	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public String getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(String deleteTime) {
		this.deleteTime = deleteTime;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
	}
	public Integer getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}
	
	

}
