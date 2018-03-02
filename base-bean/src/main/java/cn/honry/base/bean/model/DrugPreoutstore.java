package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * ClassName: DrugPreoutstore 
 * @Description: 药品预扣库存信息表
 * @author dh
 * @date 2015-2-22
 */
@SuppressWarnings("serial")
public class DrugPreoutstore extends Entity implements java.io.Serializable {
		//申请流水号
		private Integer applyNumber;
		//发药部门编码
		private String drugDeptCode;
		//出库申请分类
		private String class3MeaningCode;
		//药品编码
		private String drugCode;
		//药品商品名
		private String tradeName;
		//规格
		private String specs;
		//申请出库量(每付的总数量)
		private Double applyNum;
		//付数（草药）
		private Integer days;
		//申请人编码
		private String applyOpercode;
		//申请日期
		private Date applyDate;
		//患者编号
		private String patientId;
		
		/** 
		* @Fields hospitalId : 所属医院 
		*/ 
		private Integer hospitalId;
		/** 
		* @Fields areaCode : 所属院区
		*/ 
		private String areaCode;

		public Integer getApplyNumber() {
			return applyNumber;
		}
		public void setApplyNumber(Integer applyNumber) {
			this.applyNumber = applyNumber;
		}
		public String getDrugDeptCode() {
			return drugDeptCode;
		}
		public void setDrugDeptCode(String drugDeptCode) {
			this.drugDeptCode = drugDeptCode;
		}
		public String getClass3MeaningCode() {
			return class3MeaningCode;
		}
		public void setClass3MeaningCode(String class3MeaningCode) {
			this.class3MeaningCode = class3MeaningCode;
		}
		public String getDrugCode() {
			return drugCode;
		}
		public void setDrugCode(String drugCode) {
			this.drugCode = drugCode;
		}
		public String getTradeName() {
			return tradeName;
		}
		public void setTradeName(String tradeName) {
			this.tradeName = tradeName;
		}
		public String getSpecs() {
			return specs;
		}
		public void setSpecs(String specs) {
			this.specs = specs;
		}
		public Double getApplyNum() {
			return applyNum;
		}
		public void setApplyNum(Double applyNum) {
			this.applyNum = applyNum;
		}
		public Integer getDays() {
			return days;
		}
		public void setDays(Integer days) {
			this.days = days;
		}
		public String getApplyOpercode() {
			return applyOpercode;
		}
		public void setApplyOpercode(String applyOpercode) {
			this.applyOpercode = applyOpercode;
		}
		public Date getApplyDate() {
			return applyDate;
		}
		public void setApplyDate(Date applyDate) {
			this.applyDate = applyDate;
		}
		public String getPatientId() {
			return patientId;
		}
		public void setPatientId(String patientId) {
			this.patientId = patientId;
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
