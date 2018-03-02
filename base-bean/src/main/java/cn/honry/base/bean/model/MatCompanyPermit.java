package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class MatCompanyPermit extends Entity implements java.io.Serializable {
        private String companyId;//公司主键编码
        private String companyCode;//公司编码
        private Integer companytype;//证件类别(0营业执照,1生产许可证,2经营许可证)
        private String permitFilename;//证件文件名称
        private String permitFilepath;//证件文件保存路径【相对路径】
        private String registerCode;//注册号
        private Date registerDate;//注册时间
        private Date overDate;//到期时间
        private Integer validFlag;//有效标记(0－停用,1－有效)
        private String mark;//备注
        private String operCode;//操作员
        private Double operDate;//操作日期
        
        
        
        
		public String getCompanyId() {
			return companyId;
		}
		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}
		public String getCompanyCode() {
			return companyCode;
		}
		public void setCompanyCode(String companyCode) {
			this.companyCode = companyCode;
		}
		public Integer getCompanytype() {
			return companytype;
		}
		public void setCompanytype(Integer companytype) {
			this.companytype = companytype;
		}
		public String getPermitFilename() {
			return permitFilename;
		}
		public void setPermitFilename(String permitFilename) {
			this.permitFilename = permitFilename;
		}
		public String getPermitFilepath() {
			return permitFilepath;
		}
		public void setPermitFilepath(String permitFilepath) {
			this.permitFilepath = permitFilepath;
		}
		public String getRegisterCode() {
			return registerCode;
		}
		public void setRegisterCode(String registerCode) {
			this.registerCode = registerCode;
		}
		public Date getRegisterDate() {
			return registerDate;
		}
		public void setRegisterDate(Date registerDate) {
			this.registerDate = registerDate;
		}
		public Date getOverDate() {
			return overDate;
		}
		public void setOverDate(Date overDate) {
			this.overDate = overDate;
		}
		public Integer getValidFlag() {
			return validFlag;
		}
		public void setValidFlag(Integer validFlag) {
			this.validFlag = validFlag;
		}
		public String getMark() {
			return mark;
		}
		public void setMark(String mark) {
			this.mark = mark;
		}
		public String getOperCode() {
			return operCode;
		}
		public void setOperCode(String operCode) {
			this.operCode = operCode;
		}
		public Double getOperDate() {
			return operDate;
		}
		public void setOperDate(Double operDate) {
			this.operDate = operDate;
		}
        
}
