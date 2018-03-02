package cn.honry.statistics.bi.operation.operationFullStat.vo;

public class OperationFullStatVo {
	/**
	 * 人次
	 */
	private Double opNum;
	
	/**
	 * 比例
	 */
	private Double scale;
	
	/**
	 * 病人科室 维度一
	 */
	private String deptCode;
	
	/**
	 * 手术医生 维度二
	 */
	private String opsDocd;
	
	/**
	 * 患者病种  维度三
	 */
	private String itemName;
	/**
	 * 手术种类 维度四
	 */
	private String opsKind;
	
	/**时间维度 **/
	private String timeChose;

	private String code;
	

	

	public Double getOpNum() {
		return opNum;
	}

	public void setOpNum(Double opNum) {
		this.opNum = opNum;
	}

	

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getOpsDocd() {
		return opsDocd;
	}

	public void setOpsDocd(String opsDocd) {
		this.opsDocd = opsDocd;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getOpsKind() {
		return opsKind;
	}

	public void setOpsKind(String opsKind) {
		this.opsKind = opsKind;
	}

	public String getTimeChose() {
		return timeChose;
	}

	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
