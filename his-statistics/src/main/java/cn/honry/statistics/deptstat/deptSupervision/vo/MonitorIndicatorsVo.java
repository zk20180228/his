package cn.honry.statistics.deptstat.deptSupervision.vo;

/**
 * 
 * 
 * <p>指标监控Vo </p>
 * @Author: XCL
 * @CreateDate: 2017年7月18日 下午6:31:03 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月18日 下午6:31:03 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public class MonitorIndicatorsVo {
	
	/**科室名称**/
	private String deptCode;
	
	/**本期门诊例数**/
	private Integer nowOutCases;
	
	/**上期门诊例数**/
	private Integer beforOutCases;
	
	/**增减**/
	private Double outDecrease;
	
	
	/**本期人均诊察例数**/
	private Integer nowDiagnosCases;
	
	/**上期人均诊察例数**/
	private Integer beforDiagnosCases;
	
	/**增减**/
	private Double diagnosDecrease;
	
	/**本期住院例数**/
	private Integer nowInhCases;
	
	/**上期住院例数**/
	private Integer beforInhCases;
	
	/**增减**/
	private Double inhDecrease;
	
	/**本期出院例数**/
	private Integer nowOutHostCases;
	
	/**上期出院例数**/
	private Integer beforOutHostCases;
	
	/**增减**/
	private Double outHostDecrease;
	
	/**本期急危重例数**/
	private Integer nowCriticalCases;
	
	/**上期急危重例数**/
	private Integer beforCriticalCases;
	
	/**增减**/
	private Double criticalDecrease;
	
	/**本期抢救成功例数**/
	private Integer nowRescueCases;
	
	/**上期抢救成功例数**/
	private Integer beforRescueCases;
	
	/**增减**/
	private Double rescueDecrease;
	
	/**本期手术例数例数**/
	private Integer nowSurgicalCases;
	
	/**上期手术例数**/
	private Integer beforSurgicalCases;
	
	/**增减**/
	private Double surgicalDecrease;
	
	/**本期平均住院天数**/
	private Integer nowDaysCases;
	
	/**上期平均住院天数**/
	private Integer beforDaysCases;
	
	/**增减**/
	private Double daysDecrease;
	
	/**本期治愈例数**/
	private Integer nowCureCases;
	
	/**上期治愈例数**/
	private Integer beforCureCases;
	
	/**增减**/
	private Double cureDecrease;
	
	/**本期未治愈例数**/
	private Integer nowUnCureCases;
	
	/**上期未治愈例数**/
	private Integer beforUnCureCases;
	
	/**增减**/
	private Double unCureDecrease;
	
	/**本期好转例数**/
	private Integer nowBetterCases;
	
	/**上期好转例数**/
	private Integer beforBetterCases;
	
	/**增减**/
	private Double betterDecrease;
	
	/**本期床位适用率**/
	private Integer nowBedUsedCases;
	
	/**上期床位使用率**/
	private Integer beforBedUsedCases;
	
	/**增减**/
	private Double bedUsedDecrease;
	
	/**本期周转次数**/
	private Integer nowTurnsCases;
	
	/**上期周转次数**/
	private Integer beforTurnsCases;
	
	/**增减**/
	private Double turnsDecrease;
	
	/**本期平均床位工作日**/
	private Integer nowBedWorkCases;
	
	/**上期平均床位工作日**/
	private Integer beforBedWorkCases;
	
	/**增减**/
	private Double bedWorkDecrease;
	
	/**本期平均住院费用**/
	private Double nowExpensesCases;
	
	/**上期平均住院费用**/
	private Double beforExpensesCases;
	
	/**增减**/
	private Double expensesDecrease;
	
	/**本期死亡例数**/
	private Integer nowDeathCases;
	
	/**上期死亡例数**/
	private Integer beforDeathCases;
	
	/**增减**/
	private Double deathDecrease;
	
	/**重返率**/
	private Integer returnRate;

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Integer getNowOutCases() {
		return nowOutCases;
	}

	public void setNowOutCases(Integer nowOutCases) {
		this.nowOutCases = nowOutCases;
	}

	public Integer getBeforOutCases() {
		return beforOutCases;
	}

	public void setBeforOutCases(Integer beforOutCases) {
		this.beforOutCases = beforOutCases;
	}

	public Double getOutDecrease() {
		return outDecrease;
	}

	public void setOutDecrease(Double outDecrease) {
		this.outDecrease = outDecrease;
	}

	public Integer getNowDiagnosCases() {
		return nowDiagnosCases;
	}

	public void setNowDiagnosCases(Integer nowDiagnosCases) {
		this.nowDiagnosCases = nowDiagnosCases;
	}

	public Integer getBeforDiagnosCases() {
		return beforDiagnosCases;
	}

	public void setBeforDiagnosCases(Integer beforDiagnosCases) {
		this.beforDiagnosCases = beforDiagnosCases;
	}

	public Double getDiagnosDecrease() {
		return diagnosDecrease;
	}

	public void setDiagnosDecrease(Double diagnosDecrease) {
		this.diagnosDecrease = diagnosDecrease;
	}

	public Integer getNowInhCases() {
		return nowInhCases;
	}

	public void setNowInhCases(Integer nowInhCases) {
		this.nowInhCases = nowInhCases;
	}

	public Integer getBeforInhCases() {
		return beforInhCases;
	}

	public void setBeforInhCases(Integer beforInhCases) {
		this.beforInhCases = beforInhCases;
	}

	public Double getInhDecrease() {
		return inhDecrease;
	}

	public void setInhDecrease(Double inhDecrease) {
		this.inhDecrease = inhDecrease;
	}

	public Integer getNowOutHostCases() {
		return nowOutHostCases;
	}

	public void setNowOutHostCases(Integer nowOutHostCases) {
		this.nowOutHostCases = nowOutHostCases;
	}

	public Integer getBeforOutHostCases() {
		return beforOutHostCases;
	}

	public void setBeforOutHostCases(Integer beforOutHostCases) {
		this.beforOutHostCases = beforOutHostCases;
	}

	public Double getOutHostDecrease() {
		return outHostDecrease;
	}

	public void setOutHostDecrease(Double outHostDecrease) {
		this.outHostDecrease = outHostDecrease;
	}

	public Integer getNowCriticalCases() {
		return nowCriticalCases;
	}

	public void setNowCriticalCases(Integer nowCriticalCases) {
		this.nowCriticalCases = nowCriticalCases;
	}

	public Integer getBeforCriticalCases() {
		return beforCriticalCases;
	}

	public void setBeforCriticalCases(Integer beforCriticalCases) {
		this.beforCriticalCases = beforCriticalCases;
	}

	public Double getCriticalDecrease() {
		return criticalDecrease;
	}

	public void setCriticalDecrease(Double criticalDecrease) {
		this.criticalDecrease = criticalDecrease;
	}

	public Integer getNowRescueCases() {
		return nowRescueCases;
	}

	public void setNowRescueCases(Integer nowRescueCases) {
		this.nowRescueCases = nowRescueCases;
	}

	public Integer getBeforRescueCases() {
		return beforRescueCases;
	}

	public void setBeforRescueCases(Integer beforRescueCases) {
		this.beforRescueCases = beforRescueCases;
	}

	public Double getRescueDecrease() {
		return rescueDecrease;
	}

	public void setRescueDecrease(Double rescueDecrease) {
		this.rescueDecrease = rescueDecrease;
	}

	public Integer getNowSurgicalCases() {
		return nowSurgicalCases;
	}

	public void setNowSurgicalCases(Integer nowSurgicalCases) {
		this.nowSurgicalCases = nowSurgicalCases;
	}

	public Integer getBeforSurgicalCases() {
		return beforSurgicalCases;
	}

	public void setBeforSurgicalCases(Integer beforSurgicalCases) {
		this.beforSurgicalCases = beforSurgicalCases;
	}

	public Double getSurgicalDecrease() {
		return surgicalDecrease;
	}

	public void setSurgicalDecrease(Double surgicalDecrease) {
		this.surgicalDecrease = surgicalDecrease;
	}

	public Integer getNowDaysCases() {
		return nowDaysCases;
	}

	public void setNowDaysCases(Integer nowDaysCases) {
		this.nowDaysCases = nowDaysCases;
	}

	public Integer getBeforDaysCases() {
		return beforDaysCases;
	}

	public void setBeforDaysCases(Integer beforDaysCases) {
		this.beforDaysCases = beforDaysCases;
	}

	public Double getDaysDecrease() {
		return daysDecrease;
	}

	public void setDaysDecrease(Double daysDecrease) {
		this.daysDecrease = daysDecrease;
	}

	public Integer getNowCureCases() {
		return nowCureCases;
	}

	public void setNowCureCases(Integer nowCureCases) {
		this.nowCureCases = nowCureCases;
	}

	public Integer getBeforCureCases() {
		return beforCureCases;
	}

	public void setBeforCureCases(Integer beforCureCases) {
		this.beforCureCases = beforCureCases;
	}

	public Double getCureDecrease() {
		return cureDecrease;
	}

	public void setCureDecrease(Double cureDecrease) {
		this.cureDecrease = cureDecrease;
	}

	public Integer getNowUnCureCases() {
		return nowUnCureCases;
	}

	public void setNowUnCureCases(Integer nowUnCureCases) {
		this.nowUnCureCases = nowUnCureCases;
	}

	public Integer getBeforUnCureCases() {
		return beforUnCureCases;
	}

	public void setBeforUnCureCases(Integer beforUnCureCases) {
		this.beforUnCureCases = beforUnCureCases;
	}

	public Double getUnCureDecrease() {
		return unCureDecrease;
	}

	public void setUnCureDecrease(Double unCureDecrease) {
		this.unCureDecrease = unCureDecrease;
	}

	public Integer getNowBetterCases() {
		return nowBetterCases;
	}

	public void setNowBetterCases(Integer nowBetterCases) {
		this.nowBetterCases = nowBetterCases;
	}

	public Integer getBeforBetterCases() {
		return beforBetterCases;
	}

	public void setBeforBetterCases(Integer beforBetterCases) {
		this.beforBetterCases = beforBetterCases;
	}

	public Double getBetterDecrease() {
		return betterDecrease;
	}

	public void setBetterDecrease(Double betterDecrease) {
		this.betterDecrease = betterDecrease;
	}

	public Integer getNowBedUsedCases() {
		return nowBedUsedCases;
	}

	public void setNowBedUsedCases(Integer nowBedUsedCases) {
		this.nowBedUsedCases = nowBedUsedCases;
	}

	public Integer getBeforBedUsedCases() {
		return beforBedUsedCases;
	}

	public void setBeforBedUsedCases(Integer beforBedUsedCases) {
		this.beforBedUsedCases = beforBedUsedCases;
	}

	public Double getBedUsedDecrease() {
		return bedUsedDecrease;
	}

	public void setBedUsedDecrease(Double bedUsedDecrease) {
		this.bedUsedDecrease = bedUsedDecrease;
	}

	public Integer getNowTurnsCases() {
		return nowTurnsCases;
	}

	public void setNowTurnsCases(Integer nowTurnsCases) {
		this.nowTurnsCases = nowTurnsCases;
	}

	public Integer getBeforTurnsCases() {
		return beforTurnsCases;
	}

	public void setBeforTurnsCases(Integer beforTurnsCases) {
		this.beforTurnsCases = beforTurnsCases;
	}

	public Double getTurnsDecrease() {
		return turnsDecrease;
	}

	public void setTurnsDecrease(Double turnsDecrease) {
		this.turnsDecrease = turnsDecrease;
	}

	public Integer getNowBedWorkCases() {
		return nowBedWorkCases;
	}

	public void setNowBedWorkCases(Integer nowBedWorkCases) {
		this.nowBedWorkCases = nowBedWorkCases;
	}

	public Integer getBeforBedWorkCases() {
		return beforBedWorkCases;
	}

	public void setBeforBedWorkCases(Integer beforBedWorkCases) {
		this.beforBedWorkCases = beforBedWorkCases;
	}

	public Double getBedWorkDecrease() {
		return bedWorkDecrease;
	}

	public void setBedWorkDecrease(Double bedWorkDecrease) {
		this.bedWorkDecrease = bedWorkDecrease;
	}


	public Double getNowExpensesCases() {
		return nowExpensesCases;
	}

	public void setNowExpensesCases(Double nowExpensesCases) {
		this.nowExpensesCases = nowExpensesCases;
	}

	public Double getBeforExpensesCases() {
		return beforExpensesCases;
	}

	public void setBeforExpensesCases(Double beforExpensesCases) {
		this.beforExpensesCases = beforExpensesCases;
	}

	public Double getExpensesDecrease() {
		return expensesDecrease;
	}

	public void setExpensesDecrease(Double expensesDecrease) {
		this.expensesDecrease = expensesDecrease;
	}

	public Integer getNowDeathCases() {
		return nowDeathCases;
	}

	public void setNowDeathCases(Integer nowDeathCases) {
		this.nowDeathCases = nowDeathCases;
	}

	public Integer getBeforDeathCases() {
		return beforDeathCases;
	}

	public void setBeforDeathCases(Integer beforDeathCases) {
		this.beforDeathCases = beforDeathCases;
	}

	public Double getDeathDecrease() {
		return deathDecrease;
	}

	public void setDeathDecrease(Double deathDecrease) {
		this.deathDecrease = deathDecrease;
	}

	public Integer getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(Integer returnRate) {
		this.returnRate = returnRate;
	}

	@Override
	public String toString() {
		return "MonitorIndicatorsVo [deptCode=" + deptCode + ", nowOutCases="
				+ nowOutCases + ", beforOutCases=" + beforOutCases
				+ ", outDecrease=" + outDecrease + ", nowDiagnosCases="
				+ nowDiagnosCases + ", beforDiagnosCases=" + beforDiagnosCases
				+ ", diagnosDecrease=" + diagnosDecrease + ", nowInhCases="
				+ nowInhCases + ", beforInhCases=" + beforInhCases
				+ ", inhDecrease=" + inhDecrease + ", nowOutHostCases="
				+ nowOutHostCases + ", beforOutHostCases=" + beforOutHostCases
				+ ", outHostDecrease=" + outHostDecrease
				+ ", nowCriticalCases=" + nowCriticalCases
				+ ", beforCriticalCases=" + beforCriticalCases
				+ ", criticalDecrease=" + criticalDecrease
				+ ", nowRescueCases=" + nowRescueCases + ", beforRescueCases="
				+ beforRescueCases + ", rescueDecrease=" + rescueDecrease
				+ ", nowSurgicalCases=" + nowSurgicalCases
				+ ", beforSurgicalCases=" + beforSurgicalCases
				+ ", surgicalDecrease=" + surgicalDecrease + ", nowDaysCases="
				+ nowDaysCases + ", beforDaysCases=" + beforDaysCases
				+ ", daysDecrease=" + daysDecrease + ", nowCureCases="
				+ nowCureCases + ", beforCureCases=" + beforCureCases
				+ ", cureDecrease=" + cureDecrease + ", nowUnCureCases="
				+ nowUnCureCases + ", beforUnCureCases=" + beforUnCureCases
				+ ", unCureDecrease=" + unCureDecrease + ", nowBetterCases="
				+ nowBetterCases + ", beforBetterCases=" + beforBetterCases
				+ ", betterDecrease=" + betterDecrease + ", nowBedUsedCases="
				+ nowBedUsedCases + ", beforBedUsedCases=" + beforBedUsedCases
				+ ", bedUsedDecrease=" + bedUsedDecrease + ", nowTurnsCases="
				+ nowTurnsCases + ", beforTurnsCases=" + beforTurnsCases
				+ ", turnsDecrease=" + turnsDecrease + ", nowBedWorkCases="
				+ nowBedWorkCases + ", beforBedWorkCases=" + beforBedWorkCases
				+ ", bedWorkDecrease=" + bedWorkDecrease
				+ ", nowExpensesCases=" + nowExpensesCases
				+ ", beforExpensesCases=" + beforExpensesCases
				+ ", expensesDecrease=" + expensesDecrease + ", nowDeathCases="
				+ nowDeathCases + ", beforDeathCases=" + beforDeathCases
				+ ", deathDecrease=" + deathDecrease + ", returnRate="
				+ returnRate + "]";
	}
	
	
}
