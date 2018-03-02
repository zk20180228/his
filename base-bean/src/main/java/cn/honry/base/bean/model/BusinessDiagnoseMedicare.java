package cn.honry.base.bean.model;


import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 
 * @author zhangjin
 *
 */
public class BusinessDiagnoseMedicare extends Entity implements java.io.Serializable {
	    //住院流水号
		private String inpatientNo;
		//发生序号
		private Integer happenNo;
		//就诊卡号
		private String cardNo;
		//诊断类别
		private String diagKind;
		//诊断代码
		private String icdCode;
		//诊断名称
		private String diagName;
		//诊断拼音
		private String diagSpell; 
		//是否主诊断1是0否
		private Integer mainFlay;
		//患者科室
		private String deptCode;
		//诊断时间
		private Date diagDate;
		//诊断医生
		private String doctName;
		
		
		
		
		public Integer getMainFlay() {
			return mainFlay;
		}
		public void setMainFlay(Integer mainFlay) {
			this.mainFlay = mainFlay;
		}
		public String getIcdCode() {
			return icdCode;
		}
		public void setIcdCode(String icdCode) {
			this.icdCode = icdCode;
		}
		public String getDoctName() {
			return doctName;
		}
		public void setDoctName(String doctName) {
			this.doctName = doctName;
		}
		public String getInpatientNo() {
			return inpatientNo;
		}
		public void setInpatientNo(String inpatientNo) {
			this.inpatientNo = inpatientNo;
		}
		public Integer getHappenNo() {
			return happenNo;
		}
		public void setHappenNo(Integer happenNo) {
			this.happenNo = happenNo;
		}
		public String getCardNo() {
			return cardNo;
		}
		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}
		public String getDiagKind() {
			return diagKind;
		}
		public void setDiagKind(String diagKind) {
			this.diagKind = diagKind;
		}
		public String getDiagName() {
			return diagName;
		}
		public void setDiagName(String diagName) {
			this.diagName = diagName;
		}
		public String getDiagSpell() {
			return diagSpell;
		}
		public void setDiagSpell(String diagSpell) {
			this.diagSpell = diagSpell;
		}
		public String getDeptCode() {
			return deptCode;
		}
		public void setDeptCode(String deptCode) {
			this.deptCode = deptCode;
		}
		public Date getDiagDate() {
			return diagDate;
		}
		public void setDiagDate(Date diagDate) {
			this.diagDate = diagDate;
		}
		
		

}
