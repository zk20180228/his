package cn.honry.statistics.deptstat.internalCompare1.vo;

public class InternalCompare1Vo {
	
	public InternalCompare1Vo() {
		super();
		this.registerCountPrev = 0;
		this.registerCount = 0;
		this.registerRise = 0;
		this.inHospitalCountPrev = 0;
		this.inHospitalCount = 0;
		this.outHospitalCountPrev = 0;
		this.outHospitalCount = 0;
		this.avgInpatientCountPrev = 0d;
		this.avgInpatientCount = 0d;
		this.bedTurnoverCountPrev = 0;
		this.bedTurnoverCount = 0;
		this.bedTurnoverRise = 0;
		this.bedUseRateRise = 0;
		this.rescueSuccessRateRise = 0;
		this.realBed = 0;
		this.realUsedBed = 0;
		this.prevRealBed = 0;
		this.prevRealUsedBed = 0;
		this.prevNodeath = 0;
		this.prevDeath = 0;
		this.nodeath = 0;
		this.death = 0;
	}
	
	public InternalCompare1Vo(String deptCode, String deptName) {
		super();
		this.deptCode = deptCode;
		this.deptName = deptName;
	}

	//部门id
	private String deptCode;
	//部门名称
	private String deptName;
	//时间参数
	private String searchTime;
	//院区
	private String district;
	//病区负责人
	private String disLeader;
	
	//门诊量人次（上一年）
	private Integer registerCountPrev;
	//门诊量人次（当年）
	private Integer registerCount;
	//增长数(当年-上一年)
	private Integer registerRise;
	//增长率（增长数/上一年）
	private Double registerRisePercent;
	
	//入院人数（上一年）
	private Integer inHospitalCountPrev;
	//入院人数（当年）
	private Integer inHospitalCount;
	//增长数(当年-上一年)
	private Integer inHospitalRise;
	//增长率（增长数/上一年）
	private Double inHospitalPercent;
	
	//出院人数（上一年）
	private Integer outHospitalCountPrev;
	//出院人数（当年）
	private Integer outHospitalCount;
	//增长数(当年-上一年)
	private Integer outHospitalRise;
	//增长率（增长数/上一年）
	private Double outHospitalPercent;
	
	//平均住院天数（上一年）
	private Double avgInpatientCountPrev;
	//平均住院天数（当年）
	private Double avgInpatientCount;
	//增长数(当年-上一年)
	private Double avgInpatientRise;
	//增长率（增长数/上一年）
	private Double avgInpatientPercent;
	
	//病床周转次数（上一年）
	private Integer bedTurnoverCountPrev;
	//病床周转次数（当年）
	private Integer bedTurnoverCount;
	//增长数(当年-上一年)
	private Integer bedTurnoverRise;
	//增长率（增长数/上一年）
	private Double bedTurnoverPercent;
	
	//床位使用率（上一年）
	private Double bedUseRatePrev;
	//床位使用率（当年）
	private Double bedUseRate;
	//增长数(当年-上一年)
	private Integer bedUseRateRise;
	//增长率（增长数/上一年）
	private Double bedUseRatePercent;
		
	//急危重抢救成功率（上一年）
	private Double rescueSuccessRatePrev;
	//急危重抢救成功率（当年）
	private Double rescueSuccessRate;
	//增长数(当年-上一年)
	private Integer rescueSuccessRateRise;
	//增长率（增长数/上一年）
	private Double rescueSuccessRatePercent;
	
	//病床(当年)
	private Integer realBed;
	//使用病床(当年)
	private Integer realUsedBed;
	//病床(上年)
	private Integer prevRealBed;
	//使用病床(上年)
	private Integer prevRealUsedBed;
	
	
	//危重未死（上年）
	private Integer prevNodeath;
	//危重死亡（上年）
	private Integer prevDeath;
	//危重未死(当年)
	private Integer nodeath;
	//危重死亡(当年)
	private Integer death;
	
	public Integer getRealBed() {
		return realBed;
	}
	public void setRealBed(Integer realBed) {
		this.realBed = realBed;
	}
	public Integer getRealUsedBed() {
		return realUsedBed;
	}
	public void setRealUsedBed(Integer realUsedBed) {
		this.realUsedBed = realUsedBed;
	}
	public Integer getPrevRealBed() {
		return prevRealBed;
	}
	public void setPrevRealBed(Integer prevRealBed) {
		this.prevRealBed = prevRealBed;
	}
	public Integer getPrevRealUsedBed() {
		return prevRealUsedBed;
	}
	public void setPrevRealUsedBed(Integer prevRealUsedBed) {
		this.prevRealUsedBed = prevRealUsedBed;
	}
	
	public Integer getPrevNodeath() {
		return prevNodeath;
	}
	public void setPrevNodeath(Integer prevNodeath) {
		this.prevNodeath = prevNodeath;
	}
	public Integer getPrevDeath() {
		return prevDeath;
	}
	public void setPrevDeath(Integer prevDeath) {
		this.prevDeath = prevDeath;
	}
	public Integer getNodeath() {
		return nodeath;
	}
	public void setNodeath(Integer nodeath) {
		this.nodeath = nodeath;
	}
	public Integer getDeath() {
		return death;
	}
	public void setDeath(Integer death) {
		this.death = death;
	}
	//getters and setters
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getRegisterCountPrev() {
		return registerCountPrev;
	}
	public void setRegisterCountPrev(Integer registerCountPrev) {
		this.registerCountPrev = registerCountPrev;
	}
	public Integer getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}
	public Integer getRegisterRise() {
		return registerRise;
	}
	public void setRegisterRise(Integer registerRise) {
		this.registerRise = registerRise;
	}
	public Double getRegisterRisePercent() {
		return registerRisePercent;
	}
	public void setRegisterRisePercent(Double registerRisePercent) {
		this.registerRisePercent = registerRisePercent;
	}
	public Integer getInHospitalCountPrev() {
		return inHospitalCountPrev;
	}
	public void setInHospitalCountPrev(Integer inHospitalCountPrev) {
		this.inHospitalCountPrev = inHospitalCountPrev;
	}
	public Integer getInHospitalCount() {
		return inHospitalCount;
	}
	public void setInHospitalCount(Integer inHospitalCount) {
		this.inHospitalCount = inHospitalCount;
	}
	public Integer getInHospitalRise() {
		return inHospitalRise;
	}
	public void setInHospitalRise(Integer inHospitalRise) {
		this.inHospitalRise = inHospitalRise;
	}
	public Double getInHospitalPercent() {
		return inHospitalPercent;
	}
	public void setInHospitalPercent(Double inHospitalPercent) {
		this.inHospitalPercent = inHospitalPercent;
	}
	public Integer getOutHospitalCountPrev() {
		return outHospitalCountPrev;
	}
	public void setOutHospitalCountPrev(Integer outHospitalCountPrev) {
		this.outHospitalCountPrev = outHospitalCountPrev;
	}
	public Integer getOutHospitalCount() {
		return outHospitalCount;
	}
	public void setOutHospitalCount(Integer outHospitalCount) {
		this.outHospitalCount = outHospitalCount;
	}
	public Integer getOutHospitalRise() {
		return outHospitalRise;
	}
	public void setOutHospitalRise(Integer outHospitalRise) {
		this.outHospitalRise = outHospitalRise;
	}
	public Double getOutHospitalPercent() {
		return outHospitalPercent;
	}
	public void setOutHospitalPercent(Double outHospitalPercent) {
		this.outHospitalPercent = outHospitalPercent;
	}
	public Double getAvgInpatientCountPrev() {
		return avgInpatientCountPrev;
	}
	public void setAvgInpatientCountPrev(Double avgInpatientCountPrev) {
		this.avgInpatientCountPrev = avgInpatientCountPrev;
	}
	public Double getAvgInpatientCount() {
		return avgInpatientCount;
	}
	public void setAvgInpatientCount(Double avgInpatientCount) {
		this.avgInpatientCount = avgInpatientCount;
	}
	public Double getAvgInpatientRise() {
		return avgInpatientRise;
	}
	public void setAvgInpatientRise(Double avgInpatientRise) {
		this.avgInpatientRise = avgInpatientRise;
	}
	public Double getAvgInpatientPercent() {
		return avgInpatientPercent;
	}
	public void setAvgInpatientPercent(Double avgInpatientPercent) {
		this.avgInpatientPercent = avgInpatientPercent;
	}
	
	public Integer getBedTurnoverCountPrev() {
		return bedTurnoverCountPrev;
	}
	public void setBedTurnoverCountPrev(Integer bedTurnoverCountPrev) {
		this.bedTurnoverCountPrev = bedTurnoverCountPrev;
	}
	public Integer getBedTurnoverCount() {
		return bedTurnoverCount;
	}
	public void setBedTurnoverCount(Integer bedTurnoverCount) {
		this.bedTurnoverCount = bedTurnoverCount;
	}
	public Integer getBedTurnoverRise() {
		return bedTurnoverRise;
	}
	public void setBedTurnoverRise(Integer bedTurnoverRise) {
		this.bedTurnoverRise = bedTurnoverRise;
	}
	public Double getBedTurnoverPercent() {
		return bedTurnoverPercent;
	}
	public void setBedTurnoverPercent(Double bedTurnoverPercent) {
		this.bedTurnoverPercent = bedTurnoverPercent;
	}
	public Double getBedUseRatePrev() {
		return bedUseRatePrev;
	}
	public void setBedUseRatePrev(Double bedUseRatePrev) {
		this.bedUseRatePrev = bedUseRatePrev;
	}
	public Double getBedUseRate() {
		return bedUseRate;
	}
	public void setBedUseRate(Double bedUseRate) {
		this.bedUseRate = bedUseRate;
	}
	
	public Integer getBedUseRateRise() {
		return bedUseRateRise;
	}
	public void setBedUseRateRise(Integer bedUseRateRise) {
		this.bedUseRateRise = bedUseRateRise;
	}
	public Double getBedUseRatePercent() {
		return bedUseRatePercent;
	}
	public void setBedUseRatePercent(Double bedUseRatePercent) {
		this.bedUseRatePercent = bedUseRatePercent;
	}
	public Double getRescueSuccessRatePrev() {
		return rescueSuccessRatePrev;
	}
	public void setRescueSuccessRatePrev(Double rescueSuccessRatePrev) {
		this.rescueSuccessRatePrev = rescueSuccessRatePrev;
	}
	public Double getRescueSuccessRate() {
		return rescueSuccessRate;
	}
	public void setRescueSuccessRate(Double rescueSuccessRate) {
		this.rescueSuccessRate = rescueSuccessRate;
	}
	
	public Integer getRescueSuccessRateRise() {
		return rescueSuccessRateRise;
	}
	public void setRescueSuccessRateRise(Integer rescueSuccessRateRise) {
		this.rescueSuccessRateRise = rescueSuccessRateRise;
	}
	public Double getRescueSuccessRatePercent() {
		return rescueSuccessRatePercent;
	}
	public void setRescueSuccessRatePercent(Double rescueSuccessRatePercent) {
		this.rescueSuccessRatePercent = rescueSuccessRatePercent;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}
	
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getDisLeader() {
		return disLeader;
	}
	public void setDisLeader(String disLeader) {
		this.disLeader = disLeader;
	}
	@Override
	public String toString() {
		return "InternalCompare1Vo [deptCode=" + deptCode + ", deptName="
				+ deptName + ", searchTime=" + searchTime + ", district="
				+ district + ", disLeader=" + disLeader
				+ ", registerCountPrev=" + registerCountPrev
				+ ", registerCount=" + registerCount + ", registerRise="
				+ registerRise + ", registerRisePercent=" + registerRisePercent
				+ ", inHospitalCountPrev=" + inHospitalCountPrev
				+ ", inHospitalCount=" + inHospitalCount + ", inHospitalRise="
				+ inHospitalRise + ", inHospitalPercent=" + inHospitalPercent
				+ ", outHospitalCountPrev=" + outHospitalCountPrev
				+ ", outHospitalCount=" + outHospitalCount
				+ ", outHospitalRise=" + outHospitalRise
				+ ", outHospitalPercent=" + outHospitalPercent
				+ ", avgInpatientCountPrev=" + avgInpatientCountPrev
				+ ", avgInpatientCount=" + avgInpatientCount
				+ ", avgInpatientRise=" + avgInpatientRise
				+ ", avgInpatientPercent=" + avgInpatientPercent
				+ ", bedTurnoverCountPrev=" + bedTurnoverCountPrev
				+ ", bedTurnoverCount=" + bedTurnoverCount
				+ ", bedTurnoverRise=" + bedTurnoverRise
				+ ", bedTurnoverPercent=" + bedTurnoverPercent
				+ ", bedUseRatePrev=" + bedUseRatePrev + ", bedUseRate="
				+ bedUseRate + ", bedUseRateRise=" + bedUseRateRise
				+ ", bedUseRatePercent=" + bedUseRatePercent
				+ ", rescueSuccessRatePrev=" + rescueSuccessRatePrev
				+ ", rescueSuccessRate=" + rescueSuccessRate
				+ ", rescueSuccessRateRise=" + rescueSuccessRateRise
				+ ", rescueSuccessRatePercent=" + rescueSuccessRatePercent
				+ ", realBed=" + realBed + ", realUsedBed=" + realUsedBed
				+ ", prevRealBed=" + prevRealBed + ", prevRealUsedBed="
				+ prevRealUsedBed + ", PrevNodeath=" + prevNodeath
				+ ", prevDeath=" + prevDeath + ", nodeath=" + nodeath
				+ ", death=" + death + "]";
	}
	
	
}

