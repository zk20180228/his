package cn.honry.statistics.deptstat.internalCompare2.vo;

import java.math.BigDecimal;
import java.util.List;

public class InternalCompare2Vo {
	private String dept;//部门名字
	private String deptCode;//部门code
	private String bingqu;//病区负责人
	private Double yearedTol=0d;//总收入
	private Double yearTol=0d;//总收入
	private Double increaseTol=0d;//总收入增长数
	private Double rateTol=0d;//总收入增长率
	private Double yearedMZ=0d;//门诊收入
	private Double yearMZ=0d;//门诊收入
	private Double increaseMZ=0d;//门诊增长数
	private Double rateMZ=0d;//门诊增长率
	private Double yearedZY=0d;//住院收入
	private Double yearZY=0d;//住院收入
	private Double increaseZY=0d;//住院增长数
	private Double rateZY=0d;//住院增长率
	private String classType;
	private String feeDate;
	private BigDecimal totalMZ;
	private BigDecimal totalZY;
	private List<InternalCompare2Vo> list;//Javabean报表
	
	
	public BigDecimal getTotalMZ() {
		return totalMZ;
	}
	public void setTotalMZ(BigDecimal totalMZ) {
		this.totalMZ = totalMZ;
	}
	public BigDecimal getTotalZY() {
		return totalZY;
	}
	public void setTotalZY(BigDecimal totalZY) {
		this.totalZY = totalZY;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(String feeDate) {
		this.feeDate = feeDate;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getBingqu() {
		return bingqu;
	}
	public void setBingqu(String bingqu) {
		this.bingqu = bingqu;
	}
	public Double getYearedTol() {
		return yearedTol;
	}
	public void setYearedTol(Double yearedTol) {
		this.yearedTol = yearedTol;
	}
	public Double getYearTol() {
		return yearTol;
	}
	public void setYearTol(Double yearTol) {
		this.yearTol = yearTol;
	}
	public Double getIncreaseTol() {
		return increaseTol;
	}
	public void setIncreaseTol(Double increaseTol) {
		this.increaseTol = increaseTol;
	}
	public Double getRateTol() {
		return rateTol;
	}
	public void setRateTol(Double rateTol) {
		this.rateTol = rateTol;
	}
	public Double getYearedMZ() {
		return yearedMZ;
	}
	public void setYearedMZ(Double yearedMZ) {
		this.yearedMZ = yearedMZ;
	}
	public Double getYearMZ() {
		return yearMZ;
	}
	public void setYearMZ(Double yearMZ) {
		this.yearMZ = yearMZ;
	}
	public Double getIncreaseMZ() {
		return increaseMZ;
	}
	public void setIncreaseMZ(Double increaseMZ) {
		this.increaseMZ = increaseMZ;
	}
	public Double getRateMZ() {
		return rateMZ;
	}
	public void setRateMZ(Double rateMZ) {
		this.rateMZ = rateMZ;
	}
	public Double getYearedZY() {
		return yearedZY;
	}
	public void setYearedZY(Double yearedZY) {
		this.yearedZY = yearedZY;
	}
	public Double getYearZY() {
		return yearZY;
	}
	public void setYearZY(Double yearZY) {
		this.yearZY = yearZY;
	}
	public Double getIncreaseZY() {
		return increaseZY;
	}
	public void setIncreaseZY(Double increaseZY) {
		this.increaseZY = increaseZY;
	}
	public Double getRateZY() {
		return rateZY;
	}
	public void setRateZY(Double rateZY) {
		this.rateZY = rateZY;
	}
	/*@Override
	public String toString() {
		return "InternalCompare2Vo [dept=" + dept + ", deptCode=" + deptCode + ", bingqu=" + bingqu + ", yearedTol="
				+ yearedTol + ", yearTol=" + yearTol + ", increaseTol=" + increaseTol + ", rateTol=" + rateTol
				+ ", yearedMZ=" + yearedMZ + ", yearMZ=" + yearMZ + ", increaseMZ=" + increaseMZ + ", rateMZ=" + rateMZ
				+ ", yearedZY=" + yearedZY + ", yearZY=" + yearZY + ", increaseZY=" + increaseZY + ", rateZY=" + rateZY
				+ ", classType=" + classType + ", feeDate=" + feeDate + "]";
	}*/
	public List<InternalCompare2Vo> getList() {
		return list;
	}
	public void setList(List<InternalCompare2Vo> list) {
		this.list = list;
	}
	

	
	
}
