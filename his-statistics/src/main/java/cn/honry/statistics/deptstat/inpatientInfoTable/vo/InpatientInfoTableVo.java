package cn.honry.statistics.deptstat.inpatientInfoTable.vo;


/**
 * 住院病人动态报表VO
 * 
 */
public class InpatientInfoTableVo {
	
	private String deptCode;//科室code
	private String deptName;//科室名称
	private String firstBeds;//期初床位数
	private String finalBeds;//期末床位数
	private String firstStayHos;//期初留院
	private String withinInHos;//期内入院
	private String otherTransIn;//他科转入
	private String total;//总数
	private String subTotal;//小计
	private String cure;//治愈
	private String better;//好转
	private String notCure;//未治愈
	private String death;//死亡
	private String others;//其他
	private String childBirth;//正常分娩
	private String familyPlan;//计划生育
	private String transOther;//转往他科
	private String finalStayHos;//期末留院
	private String actOpenBedDays;//实际开放总床日数
	private String avgOpenBeds;//平均开放病床数
	private String actUseBedDays;//实际占用总床日数
	private String outPatUseBedDays;//出院者占用总床日
	private String outAvgInDay;//出平均住天数
	private String cureRate;//治愈率
	private String betterRate;//好转率
	private String deathRate;//死亡率
	private String bedTurnNum;//病床周转次数
	private String avgBedWorkDay;//平均病床工作日
	private String bedUseRate;//床位使用率
	private String avgBeforeOperDay;//术前平均住院天数
	private String criPatient;//危重病人数
	private String addBedDays;//加床总日数
	private String freeBedDays;//空床总日数
	private String hangBedDays;//挂床日数
	private String levelA ;//一级护理
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
	public String getFirstBeds() {
		return firstBeds;
	}
	public void setFirstBeds(String firstBeds) {
		this.firstBeds = firstBeds;
	}
	public String getFinalBeds() {
		return finalBeds;
	}
	public void setFinalBeds(String finalBeds) {
		this.finalBeds = finalBeds;
	}
	public String getFirstStayHos() {
		return firstStayHos;
	}
	public void setFirstStayHos(String firstStayHos) {
		this.firstStayHos = firstStayHos;
	}
	public String getWithinInHos() {
		return withinInHos;
	}
	public void setWithinInHos(String withinInHos) {
		this.withinInHos = withinInHos;
	}
	public String getOtherTransIn() {
		return otherTransIn;
	}
	public void setOtherTransIn(String otherTransIn) {
		this.otherTransIn = otherTransIn;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getCure() {
		return cure;
	}
	public void setCure(String cure) {
		this.cure = cure;
	}
	public String getBetter() {
		return better;
	}
	public void setBetter(String better) {
		this.better = better;
	}
	public String getNotCure() {
		return notCure;
	}
	public void setNotCure(String notCure) {
		this.notCure = notCure;
	}
	public String getDeath() {
		return death;
	}
	public void setDeath(String death) {
		this.death = death;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public String getChildBirth() {
		return childBirth;
	}
	public void setChildBirth(String childBirth) {
		this.childBirth = childBirth;
	}
	public String getFamilyPlan() {
		return familyPlan;
	}
	public void setFamilyPlan(String familyPlan) {
		this.familyPlan = familyPlan;
	}
	public String getTransOther() {
		return transOther;
	}
	public void setTransOther(String transOther) {
		this.transOther = transOther;
	}
	public String getFinalStayHos() {
		return finalStayHos;
	}
	public void setFinalStayHos(String finalStayHos) {
		this.finalStayHos = finalStayHos;
	}
	public String getActOpenBedDays() {
		return actOpenBedDays;
	}
	public void setActOpenBedDays(String actOpenBedDays) {
		this.actOpenBedDays = actOpenBedDays;
	}
	public String getAvgOpenBeds() {
		return avgOpenBeds;
	}
	public void setAvgOpenBeds(String avgOpenBeds) {
		this.avgOpenBeds = avgOpenBeds;
	}
	public String getActUseBedDays() {
		return actUseBedDays;
	}
	public void setActUseBedDays(String actUseBedDays) {
		this.actUseBedDays = actUseBedDays;
	}
	public String getOutPatUseBedDays() {
		return outPatUseBedDays;
	}
	public void setOutPatUseBedDays(String outPatUseBedDays) {
		this.outPatUseBedDays = outPatUseBedDays;
	}
	public String getOutAvgInDay() {
		return outAvgInDay;
	}
	public void setOutAvgInDay(String outAvgInDay) {
		this.outAvgInDay = outAvgInDay;
	}
	public String getCureRate() {
		return cureRate;
	}
	public void setCureRate(String cureRate) {
		this.cureRate = cureRate;
	}
	public String getBetterRate() {
		return betterRate;
	}
	public void setBetterRate(String betterRate) {
		this.betterRate = betterRate;
	}
	public String getDeathRate() {
		return deathRate;
	}
	public void setDeathRate(String deathRate) {
		this.deathRate = deathRate;
	}
	public String getBedTurnNum() {
		return bedTurnNum;
	}
	public void setBedTurnNum(String bedTurnNum) {
		this.bedTurnNum = bedTurnNum;
	}
	public String getAvgBedWorkDay() {
		return avgBedWorkDay;
	}
	public void setAvgBedWorkDay(String avgBedWorkDay) {
		this.avgBedWorkDay = avgBedWorkDay;
	}
	public String getBedUseRate() {
		return bedUseRate;
	}
	public void setBedUseRate(String bedUseRate) {
		this.bedUseRate = bedUseRate;
	}
	public String getAvgBeforeOperDay() {
		return avgBeforeOperDay;
	}
	public void setAvgBeforeOperDay(String avgBeforeOperDay) {
		this.avgBeforeOperDay = avgBeforeOperDay;
	}
	public String getCriPatient() {
		return criPatient;
	}
	public void setCriPatient(String criPatient) {
		this.criPatient = criPatient;
	}
	public String getAddBedDays() {
		return addBedDays;
	}
	public void setAddBedDays(String addBedDays) {
		this.addBedDays = addBedDays;
	}
	public String getFreeBedDays() {
		return freeBedDays;
	}
	public void setFreeBedDays(String freeBedDays) {
		this.freeBedDays = freeBedDays;
	}
	public String getHangBedDays() {
		return hangBedDays;
	}
	public void setHangBedDays(String hangBedDays) {
		this.hangBedDays = hangBedDays;
	}
	public String getLevelA() {
		return levelA;
	}
	public void setLevelA(String levelA) {
		this.levelA = levelA;
	}
	
}



