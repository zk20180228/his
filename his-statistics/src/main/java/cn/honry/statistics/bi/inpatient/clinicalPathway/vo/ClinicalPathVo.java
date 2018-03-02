package cn.honry.statistics.bi.inpatient.clinicalPathway.vo;

import java.math.BigDecimal;

/**
 * 临床路径统计使用实体
 * 
 * <p> </p>
 * @Author: zouxianhao
 * @CreateDate: 2017年11月28日 下午4:14:10 
 * @Modifier: zouxianhao
 * @ModifyDate: 2017年11月28日 下午4:14:10 
 * @ModifyRmk:  
 * @version: V1.0:
 * @throws:
 * @return: 
 *
 */
public class ClinicalPathVo {
	private String deptCode;
	private String nameCode;
	private BigDecimal num;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getNameCode() {
		return nameCode;
	}
	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}
	public BigDecimal getNum() {
		return num;
	}
	public void setNum(BigDecimal num) {
		this.num = num;
	}
}
