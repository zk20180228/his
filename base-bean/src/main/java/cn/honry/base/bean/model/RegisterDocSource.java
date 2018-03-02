package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 医生号源表
 * @author GH
 */
@SuppressWarnings("serial")
public class RegisterDocSource extends Entity {
		// Fields
		/**医生code**/
		private String employeeCode;
		/**医生姓名**/
		private String employeeName;
		/**挂号级别CODE**/
		private String gradeCode;
		/**挂号名**/
		private String gradeName;
		/**科室code**/
		private String deptCode;
		/**科室名称**/
		private String deptName;
		/**午别(编码)**/
		private String middayCode;
		/**午别名称**/
		private String middayName;
		/**诊室编号**/
		private String clinicCode;
		/**诊室名称**/
		private String clinicName;
		/**挂号限额**/
		private Integer limitSum;
		/**特诊限额**/
		private Integer peciallimitSum;
		/**已挂人数**/
		private Integer clinicSum;
		/**是否加号**/
		private Integer appFlag;
		/**是否停诊**/
		private Integer isStop;
		/**停诊原因**/
		private String stopReason;
		/**排班Id**/
		private String scheduleId;
		/**排班日期**/
		private Date regDate;
		/** 
		* @Fields version : 版本号 
		*/ 
		private int version;
		/**预约人数**/
		private Integer preregisterSum;
		/**预约已取号人数**/
		private Integer preclinicSum;
		
		public String getEmployeeCode() {
			return employeeCode;
		}
		public void setEmployeeCode(String employeeCode) {
			this.employeeCode = employeeCode;
		}
		public String getEmployeeName() {
			return employeeName;
		}
		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}
		public String getGradeCode() {
			return gradeCode;
		}
		public void setGradeCode(String gradeCode) {
			this.gradeCode = gradeCode;
		}
		public String getGradeName() {
			return gradeName;
		}
		public void setGradeName(String gradeName) {
			this.gradeName = gradeName;
		}
		public String getDeptCode() {
			return deptCode;
		}
		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}
		public String getDeptName() {
			return deptName;
		}
		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}
		public String getMiddayCode() {
			return middayCode;
		}
		public void setMiddayCode(String middayCode) {
			this.middayCode = middayCode;
		}
		public String getMiddayName() {
			return middayName;
		}
		public void setMiddayName(String middayName) {
			this.middayName = middayName;
		}
		public String getClinicCode() {
			return clinicCode;
		}
		public void setClinicCode(String clinicCode) {
			this.clinicCode = clinicCode;
		}
		public String getClinicName() {
			return clinicName;
		}
		public void setClinicName(String clinicName) {
			this.clinicName = clinicName;
		}
		public Integer getLimitSum() {
			return limitSum;
		}
		public void setLimitSum(Integer limitSum) {
			this.limitSum = limitSum;
		}
		public Integer getPeciallimitSum() {
			return peciallimitSum;
		}
		public void setPeciallimitSum(Integer peciallimitSum) {
			this.peciallimitSum = peciallimitSum;
		}
		public Integer getClinicSum() {
			return clinicSum;
		}
		public void setClinicSum(Integer clinicSum) {
			this.clinicSum = clinicSum;
		}
		public Integer getAppFlag() {
			return appFlag;
		}
		public void setAppFlag(Integer appFlag) {
			this.appFlag = appFlag;
		}
		public Integer getIsStop() {
			return isStop;
		}
		public void setIsStop(Integer isStop) {
			this.isStop = isStop;
		}
		public String getStopReason() {
			return stopReason;
		}
		public void setStopReason(String stopReason) {
			this.stopReason = stopReason;
		}
		public String getScheduleId() {
			return scheduleId;
		}
		public void setScheduleId(String scheduleId) {
			this.scheduleId = scheduleId;
		}
		public Date getRegDate() {
			return regDate;
		}
		public void setRegDate(Date regDate) {
			this.regDate = regDate;
		}
		public int getVersion() {
			return version;
		}
		public void setVersion(int version) {
			this.version = version;
		}
		public Integer getPreregisterSum() {
			return preregisterSum;
		}
		public void setPreregisterSum(Integer preregisterSum) {
			this.preregisterSum = preregisterSum;
		}
		public Integer getPreclinicSum() {
			return preclinicSum;
		}
		public void setPreclinicSum(Integer preclinicSum) {
			this.preclinicSum = preclinicSum;
		}
		
}
