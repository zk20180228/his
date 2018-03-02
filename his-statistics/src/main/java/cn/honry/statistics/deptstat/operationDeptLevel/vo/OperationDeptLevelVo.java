package cn.honry.statistics.deptstat.operationDeptLevel.vo;

import java.util.List;

/**
 * 手术科室手术分级统计信息VO
 * 
 */
public class OperationDeptLevelVo {
	
	private String deptCode;//科室code
	private String deptName;//科室名称
	private String levelA;//一级
	private String levelB;//二级
	private String levelC;//三级
	private String levelD;//四级
	private String percentA;//一级百分比
	private String percentB;//二级百分比
	private String percentC;//三级百分比
	private String percentD;//四级百分比
	private String sum;//手术合计人数
	private String transOut;//转出
	private String transOut2;//转出2
	private String sumAndTransOut;//合计含转出
	
	
	public String getTransOut2() {
		return transOut2;
	}
	public void setTransOut2(String transOut2) {
		this.transOut2 = transOut2;
	}
	public String getTransOut() {
		return transOut;
	}
	public void setTransOut(String transOut) {
		this.transOut = transOut;
	}
	public String getSumAndTransOut() {
		return sumAndTransOut;
	}
	public void setSumAndTransOut(String sumAndTransOut) {
		this.sumAndTransOut = sumAndTransOut;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getLevelA() {
		return levelA;
	}
	public void setLevelA(String levelA) {
		this.levelA = levelA;
	}
	public String getLevelB() {
		return levelB;
	}
	public void setLevelB(String levelB) {
		this.levelB = levelB;
	}
	public String getLevelC() {
		return levelC;
	}
	public void setLevelC(String levelC) {
		this.levelC = levelC;
	}
	public String getLevelD() {
		return levelD;
	}
	public void setLevelD(String levelD) {
		this.levelD = levelD;
	}
	public String getPercentA() {
		return percentA;
	}
	public void setPercentA(String percentA) {
		this.percentA = percentA;
	}
	public String getPercentB() {
		return percentB;
	}
	public void setPercentB(String percentB) {
		this.percentB = percentB;
	}
	public String getPercentC() {
		return percentC;
	}
	public void setPercentC(String percentC) {
		this.percentC = percentC;
	}
	public String getPercentD() {
		return percentD;
	}
	public void setPercentD(String percentD) {
		this.percentD = percentD;
	}
	private List<OperationDeptLevelVo> list;//报表打印
	
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public List<OperationDeptLevelVo> getList() {
		return list;
	}
	public void setList(List<OperationDeptLevelVo> list) {
		this.list = list;
	}
	
}