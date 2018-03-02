package cn.honry.statistics.deptstat.operaCondtion.vo;
/**
 * 
 * 
 * <p>手术情况查新Vo </p>
 * @Author: XCL
 * @CreateDate: 2017年7月15日 下午4:49:42 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月15日 下午4:49:42 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class OperaCondVo {
	/**住院科室**/
	private String deptCode;
	
	/**姓名**/
	private String name;
	
	/**病历号**/
	private String patientNo;
	
	/**手术名称**/
	private String itemName;
	
	/**手术时间**/
	private Double realDuation;
	
	/**执行科室**/
	private String execDept;

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getRealDuation() {
		return realDuation;
	}

	public void setRealDuation(Double realDuation) {
		this.realDuation = realDuation;
	}

	public String getExecDept() {
		return execDept;
	}

	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}

	@Override
	public String toString() {
		return "OperaCondVo [deptCode=" + deptCode + ", name=" + name
				+ ", patientNo=" + patientNo + ", itemName=" + itemName
				+ ", realDuation=" + realDuation + ", execDept=" + execDept
				+ "]";
	}
	
}
