package cn.honry.statistics.deptstat.journal.vo;



public class JournalVo{

	/** 
	* @Fields deptName : 科室名称 
	*/ 
	private String deptName;
	/** 
	* @Fields oldNum : 原有人数 
	*/ 
	private Integer oldNum;
	/** 
	* @Fields inNum : 入院人数 
	*/ 
	private Integer inNum;
	/** 
	* @Fields outNum : 出院人数
	*/ 
	private Integer outNum;
	/** 
	* @Fields exInNum : 转入人数 
	*/ 
	private Integer exInNum;
	/** 
	* @Fields exOutNum : 转出人数
	*/ 
	private Integer exOutNum;
	/** 
	* @Fields nowNum : 现有人数
	*/ 
	private Integer nowNum;
	/** 
	* @Fields realBedNum : 实占床位
	*/ 
	private Integer realBedNum;
	/** 
	* @Fields hangBedDays : 挂床日期 
	*/ 
	private Integer hangBedDays;
	/** 
	* @Fields openBedNum : 开放床位
	*/ 
	private Integer openBedNum;
	/** 
	* @Fields rateOfBed : 病床使用率
	*/ 
	private Double rateOfBed;
	/** 
	* @Fields criticallyNum : 危重病人 
	*/ 
	private Integer criticallyNum;
	/** 
	* @Fields grateOneNum : 一级护理
	*/ 
	private Integer grateOneNum;
	/** 
	* @Fields extraBedNum : 加床
	*/ 
	private Integer extraBedNum;
	/** 
	* @Fields emptyBedNum : 空床
	*/ 
	private Integer emptyBedNum;
	
	/**
	 * 总条数
	 */
	private Integer total;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getOldNum() {
		return oldNum;
	}
	public void setOldNum(Integer oldNum) {
		this.oldNum = oldNum;
	}
	public Integer getInNum() {
		return inNum;
	}
	public void setInNum(Integer inNum) {
		this.inNum = inNum;
	}
	public Integer getOutNum() {
		return outNum;
	}
	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
	}
	public Integer getExInNum() {
		return exInNum;
	}
	public void setExInNum(Integer exInNum) {
		this.exInNum = exInNum;
	}
	public Integer getExOutNum() {
		return exOutNum;
	}
	public void setExOutNum(Integer exOutNum) {
		this.exOutNum = exOutNum;
	}
	public Integer getNowNum() {
		return nowNum;
	}
	public void setNowNum(Integer nowNum) {
		this.nowNum = nowNum;
	}
	public Integer getRealBedNum() {
		return realBedNum;
	}
	public void setRealBedNum(Integer realBedNum) {
		this.realBedNum = realBedNum;
	}
	public Integer getHangBedDays() {
		return hangBedDays;
	}
	public void setHangBedDays(Integer hangBedDays) {
		this.hangBedDays = hangBedDays;
	}
	public Integer getOpenBedNum() {
		return openBedNum;
	}
	public void setOpenBedNum(Integer openBedNum) {
		this.openBedNum = openBedNum;
	}
	public Double getRateOfBed() {
		return rateOfBed;
	}
	public void setRateOfBed(Double rateOfBed) {
		this.rateOfBed = rateOfBed;
	}
	public Integer getCriticallyNum() {
		return criticallyNum;
	}
	public void setCriticallyNum(Integer criticallyNum) {
		this.criticallyNum = criticallyNum;
	}
	public Integer getGrateOneNum() {
		return grateOneNum;
	}
	public void setGrateOneNum(Integer grateOneNum) {
		this.grateOneNum = grateOneNum;
	}
	public Integer getExtraBedNum() {
		return extraBedNum;
	}
	public void setExtraBedNum(Integer extraBedNum) {
		this.extraBedNum = extraBedNum;
	}
	public Integer getEmptyBedNum() {
		return emptyBedNum;
	}
	public void setEmptyBedNum(Integer emptyBedNum) {
		this.emptyBedNum = emptyBedNum;
	}
	public JournalVo(String deptName, Integer oldNum, Integer inNum,
			Integer outNum, Integer exInNum, Integer exOutNum, Integer nowNum,
			Integer realBedNum, Integer hangBedDays, Integer openBedNum,
			Double rateOfBed, Integer criticallyNum, Integer grateOneNum,
			Integer extraBedNum, Integer emptyBedNum) {
		super();
		this.deptName = deptName;
		this.oldNum = oldNum;
		this.inNum = inNum;
		this.outNum = outNum;
		this.exInNum = exInNum;
		this.exOutNum = exOutNum;
		this.nowNum = nowNum;
		this.realBedNum = realBedNum;
		this.hangBedDays = hangBedDays;
		this.openBedNum = openBedNum;
		this.rateOfBed = rateOfBed;
		this.criticallyNum = criticallyNum;
		this.grateOneNum = grateOneNum;
		this.extraBedNum = extraBedNum;
		this.emptyBedNum = emptyBedNum;
	}
	public JournalVo() {
	}

}
