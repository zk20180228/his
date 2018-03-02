package cn.honry.inpatient.settlementRecall.vo;

public class VSettlementRecall {
	    private String inPatientInfoId;//住院信息主表id
	    private String medicalrecordId;//病历号
        private String patientName;//姓名
        /**合同单位代码 (从合同单位编码表里读取)*/
		private String pactCode;
		/**科室代码*/
		private String deptCode;
		//所属病区 
		private String nurseCellCode;
		/**入院日期*/
		private String inDate;
		/**医师代码(住院)*/
		private String houseDocCode;
		/**床号  (从病床维护表里读取)*/
		private String bedId;
		/**出生日期*/
		private String reportBirthday;//Date-String
		public String getNurseCellCode() {
			return nurseCellCode;
		}
		public void setNurseCellCode(String nurseCellCode) {
			this.nurseCellCode = nurseCellCode;
		}
		public String getMedicalrecordId() {
			return medicalrecordId;
		}
		public void setMedicalrecordId(String medicalrecordId) {
			this.medicalrecordId = medicalrecordId;
		}
		public String getInPatientInfoId() {
			return inPatientInfoId;
		}
		public void setInPatientInfoId(String inPatientInfoId) {
			this.inPatientInfoId = inPatientInfoId;
		}
		public String getPatientName() {
			return patientName;
		}
		public void setPatientName(String patientName) {
			this.patientName = patientName;
		}
		public String getPactCode() {
			return pactCode;
		}
		public void setPactCode(String pactCode) {
			this.pactCode = pactCode;
		}
		public String getDeptCode() {
			return deptCode;
		}
		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}
		public String getInDate() {
			return inDate;
		}
		public void setInDate(String inDate) {
			this.inDate = inDate;
		}
		public String getHouseDocCode() {
			return houseDocCode;
		}
		public void setHouseDocCode(String houseDocCode) {
			this.houseDocCode = houseDocCode;
		}
		public String getBedId() {
			return bedId;
		}
		public void setBedId(String bedId) {
			this.bedId = bedId;
		}
		public String getReportBirthday() {
			return reportBirthday;
		}
		public void setReportBirthday(String reportBirthday) {
			this.reportBirthday = reportBirthday;
		}
		
		
		
}
