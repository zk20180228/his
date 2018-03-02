package cn.honry.inner.statistics.internalCompare1.vo;

public class InternalCompare1Vo {
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
		
		//入院人数（上一年）
		private Integer inHospitalCountPrev;
		//入院人数（当年）
		private Integer inHospitalCount;
		
		//出院人数（上一年）
		private Integer outHospitalCountPrev;
		//出院人数（当年）
		private Integer outHospitalCount;
		
		//平均住院天数（上一年）
		private Double avgInpatientCountPrev;
		//平均住院天数（当年）
		private Double avgInpatientCount;
		
		//病床周转次数（上一年）
		private Integer bedTurnoverCountPrev;
		//病床周转次数（当年）
		private Integer bedTurnoverCount;
		
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
		@Override
		public String toString() {
			return "InternalCompare1Vo [deptCode=" + deptCode + ", deptName="
					+ deptName + ", searchTime=" + searchTime + ", district="
					+ district + ", disLeader=" + disLeader
					+ ", registerCountPrev=" + registerCountPrev
					+ ", registerCount=" + registerCount
					+ ", inHospitalCountPrev=" + inHospitalCountPrev
					+ ", inHospitalCount=" + inHospitalCount
					+ ", outHospitalCountPrev=" + outHospitalCountPrev
					+ ", outHospitalCount=" + outHospitalCount
					+ ", avgInpatientCountPrev=" + avgInpatientCountPrev
					+ ", avgInpatientCount=" + avgInpatientCount
					+ ", bedTurnoverCountPrev=" + bedTurnoverCountPrev
					+ ", bedTurnoverCount=" + bedTurnoverCount + ", realBed="
					+ realBed + ", realUsedBed=" + realUsedBed
					+ ", prevRealBed=" + prevRealBed + ", prevRealUsedBed="
					+ prevRealUsedBed + ", prevNodeath=" + prevNodeath
					+ ", prevDeath=" + prevDeath + ", nodeath=" + nodeath
					+ ", death=" + death + ", getDeptCode()=" + getDeptCode()
					+ ", getDeptName()=" + getDeptName() + ", getSearchTime()="
					+ getSearchTime() + ", getDistrict()=" + getDistrict()
					+ ", getDisLeader()=" + getDisLeader()
					+ ", getRegisterCountPrev()=" + getRegisterCountPrev()
					+ ", getRegisterCount()=" + getRegisterCount()
					+ ", getInHospitalCountPrev()=" + getInHospitalCountPrev()
					+ ", getInHospitalCount()=" + getInHospitalCount()
					+ ", getOutHospitalCountPrev()="
					+ getOutHospitalCountPrev() + ", getOutHospitalCount()="
					+ getOutHospitalCount() + ", getAvgInpatientCountPrev()="
					+ getAvgInpatientCountPrev() + ", getAvgInpatientCount()="
					+ getAvgInpatientCount() + ", getBedTurnoverCountPrev()="
					+ getBedTurnoverCountPrev() + ", getBedTurnoverCount()="
					+ getBedTurnoverCount() + ", getRealBed()=" + getRealBed()
					+ ", getRealUsedBed()=" + getRealUsedBed()
					+ ", getPrevRealBed()=" + getPrevRealBed()
					+ ", getPrevRealUsedBed()=" + getPrevRealUsedBed()
					+ ", getPrevNodeath()=" + getPrevNodeath()
					+ ", getPrevDeath()=" + getPrevDeath() + ", getNodeath()="
					+ getNodeath() + ", getDeath()=" + getDeath()
					+ ", getClass()=" + getClass() + ", hashCode()="
					+ hashCode() + ", toString()=" + super.toString() + "]";
		}
		
		
		
}
