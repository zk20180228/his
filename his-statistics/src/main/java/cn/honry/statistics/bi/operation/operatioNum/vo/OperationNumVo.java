package cn.honry.statistics.bi.operation.operatioNum.vo;


public class OperationNumVo {
	//查询指标
	
	/**
	 * 手术执行科室
	 */
	private String opExecdept;
	
	/**
	 * 手术分类
	 */
	private String opKind;
	/**
	 * 手术分类
	 */
	private String timeChose;
	//查询变量
	
	/**
	 * 人数
	 */
	private Integer opNum;
	public String getOpExecdept() {
		return opExecdept;
	}

	public void setOpExecdept(String opExecdept) {
		this.opExecdept = opExecdept;
	}

	public String getOpKind() {
		return opKind;
	}

	public void setOpKind(String opKind) {
		this.opKind = opKind;
	}

	
	public Integer getOpNum() {
		return opNum;
	}

	public void setOpNum(Integer opNum) {
		this.opNum = opNum;
	}

	public String getTimeChose() {
		return timeChose;
	}

	public void setTimeChose(String timeChose) {
		this.timeChose = timeChose;
	}
	
	
}
