package cn.honry.inner.statistics.operationDeptLevel.vo;

/**
 * 
 * 
 * <p>手术科室手术分级预处理VO </p>
 * @Author: XCL
 * @CreateDate: 2017年7月25日 下午5:51:56 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月25日 下午5:51:56 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class OperationDeptLevel {
	private String deptCode;//科室code
	private String deptName;//科室名称
	private String levelA;//一级
	private String levelB;//二级
	private String levelC;//三级
	private String levelD;//四级
	private String sumLevel;//手术合计人数
	private String transOut;//转出
	private String transOut2;//转出2
	private String sumAndTransOut;//合计含转出
	private String deptDate;//时间
	public String getDeptDate() {
		return deptDate;
	}
	public void setDeptDate(String deptDate) {
		this.deptDate = deptDate;
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
	public String getSumLevel() {
		return sumLevel;
	}
	public void setSumLevel(String sumLevel) {
		this.sumLevel = sumLevel;
	}
	public String getTransOut() {
		return transOut;
	}
	public void setTransOut(String transOut) {
		this.transOut = transOut;
	}
	public String getTransOut2() {
		return transOut2;
	}
	public void setTransOut2(String transOut2) {
		this.transOut2 = transOut2;
	}
	public String getSumAndTransOut() {
		return sumAndTransOut;
	}
	public void setSumAndTransOut(String sumAndTransOut) {
		this.sumAndTransOut = sumAndTransOut;
	}
	
}
