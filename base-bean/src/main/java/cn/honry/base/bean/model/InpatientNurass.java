package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientNurass extends Entity{
	private static final long serialVersionUID = 1L;
	/**患者姓名**/
	private String pname;
	/**患者性别**/
	private String psex;
	/**患者年龄**/
	private String page;
	/**科室编码**/
	private String deptCode;
	/**科室名称**/
	private String deptName;
	/**床号**/
	private String bedNo;
	/**住院号(病历号)**/
	private String medicalrecodeId;
	/**住院流水号**/
	private String inpatientNo;
	/**职业**/
	private String occupationa;
	/**文化程度**/
	private String culture;
	/**婚姻状况(未婚/已婚)**/
	private String marriage;
	/**本人电话**/
	private String pphone;
	/**联系人**/
	private String cperson;
	/**联系人电话**/
	private String cphone;
	/**入院日期**/
	private Date bhDate;
	/**入院方式(步行/轮椅/平车/其他)**/
	private String bhWay;
	/**入院方式其它**/
	private String bhWayOth;
	/**入院诊断**/
	private String bhDiag;
	/**神志(清楚/嗜睡/意识模糊/昏睡/浅昏迷/深昏迷/痴呆)**/
	private String mind;
	/**表情(正常/淡漠/痛苦/紧张)**/
	private String expression;
	/**情绪状态(稳定/易激动/焦虑/恐惧/抑郁)**/
	private String emotion;
	/**视力(正常/异常)**/
	private String vision;
	/**视力备注**/
	private String visionRem;
	/**听力(正常/异常)**/
	private String hearing;
	/**听力备注**/
	private String hearingRem;
	/**沟通方式(语言/文字/手势)**/
	private String commMode;
	/**理解能力(良好/一般/差)**/
	private String compAbility;
	/**口腔粘膜(正常/充血/破损/霉菌感染/溃疡)**/
	private String oralMucosa;
	/**义齿(无/有)**/
	private String falseTooth;
	/**皮肤(正常/水肿/黄疸/苍白/紫绀/皮疹/瘀斑/搔痒/破损/其他)**/
	private String skin;
	/**皮肤其它状态**/
	private String skinOth;
	/**压疮(无/有)**/
	private String sore;
	/**压疮部位**/
	private String sorePosi;
	/**压疮范围**/
	private String soreRange;
	/**小便(正常/失禁/尿频/尿少/尿急/尿痛/尿潴留/尿管/造口/其他)**/
	private String urine;
	/**小便其它状态**/
	private String urineOth;
	/**大便(正常/失禁/便秘/黑便/造口/腹泻/其他)**/
	private String shit;
	/**腹泻次数/每日**/
	private Integer shitDiarr;
	/**大便其它状态**/
	private String shitOth;
	/**自理能力(无需依赖(100分)/轻度依赖(75-95分)/中度依赖(50-70分)/重度依赖(25-45分)/完全依赖(0-20分))**/
	private String scAbility;
	/**Braden评分**/
	private Integer braden;
	/**Morse评分**/
	private Integer morse;
	/**体型(正常/肥胖/消瘦/恶液质)**/
	private String shape;
	/**饮食习惯(正常/咸/甜/辛辣/油腻/清淡/其他/忌食)**/
	private String eating;
	/**饮食习惯其它状态**/
	private String eatingOth;
	/**忌食**/
	private String eatingDiet;
	/**饮食习惯异常(食欲不振/吞咽困难/咀嚼困难/恶心/呕吐)**/
	private String eatingAbn;
	/**吸烟(是/否)**/
	private String smoke;
	/**吸烟备注**/
	private String smokeRem;
	/**饮酒(是/否)**/
	private String wine;
	/**饮酒备注**/
	private String wineRemark;
	/**睡眠(正常/多梦/易醒)**/
	private String sleep;
	/**小时/每日睡眠**/
	private Integer sleepDay;
	/**药物辅助睡眠(有/无)**/
	private String sleepMedi;
	/**药物辅助睡眠备注**/
	private String sleepMediRem;
	/**家属态度(关心/不关心/过于关心/无人照顾)**/
	private String fstate;
	/**宗教信仰(有/无)**/
	private String religion;
	/**宗教信仰备注**/
	private String religionRem;
	/**既往史(高血压/心脏病/糖尿病/脑血管病/手术史/精神病/其他)**/
	private String pastHis;
	/**既往史其它状态**/
	private String pastHisOther;
	/**过敏史(有/无)**/
	private String allerHis;
	/**过敏史药物**/
	private String allerHisMedi;
	/**过敏史食物**/
	private String allerHisFood;
	/**过敏史其它**/
	private String allerHisOther;
	/**护士姓名**/
	private String nurseName;
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPsex() {
		return psex;
	}
	public void setPsex(String psex) {
		this.psex = psex;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
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
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getMedicalrecodeId() {
		return medicalrecodeId;
	}
	public void setMedicalrecodeId(String medicalrecodeId) {
		this.medicalrecodeId = medicalrecodeId;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getOccupationa() {
		return occupationa;
	}
	public void setOccupationa(String occupationa) {
		this.occupationa = occupationa;
	}
	public String getCulture() {
		return culture;
	}
	public void setCulture(String culture) {
		this.culture = culture;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getPphone() {
		return pphone;
	}
	public void setPphone(String pphone) {
		this.pphone = pphone;
	}
	public String getCperson() {
		return cperson;
	}
	public void setCperson(String cperson) {
		this.cperson = cperson;
	}
	public String getCphone() {
		return cphone;
	}
	public void setCphone(String cphone) {
		this.cphone = cphone;
	}
	public Date getBhDate() {
		return bhDate;
	}
	public void setBhDate(Date bhDate) {
		this.bhDate = bhDate;
	}
	public String getBhWay() {
		return bhWay;
	}
	public void setBhWay(String bhWay) {
		this.bhWay = bhWay;
	}
	public String getBhWayOth() {
		return bhWayOth;
	}
	public void setBhWayOth(String bhWayOth) {
		this.bhWayOth = bhWayOth;
	}
	public String getBhDiag() {
		return bhDiag;
	}
	public void setBhDiag(String bhDiag) {
		this.bhDiag = bhDiag;
	}
	public String getMind() {
		return mind;
	}
	public void setMind(String mind) {
		this.mind = mind;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getEmotion() {
		return emotion;
	}
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	public String getVision() {
		return vision;
	}
	public void setVision(String vision) {
		this.vision = vision;
	}
	public String getVisionRem() {
		return visionRem;
	}
	public void setVisionRem(String visionRem) {
		this.visionRem = visionRem;
	}
	public String getHearing() {
		return hearing;
	}
	public void setHearing(String hearing) {
		this.hearing = hearing;
	}
	public String getHearingRem() {
		return hearingRem;
	}
	public void setHearingRem(String hearingRem) {
		this.hearingRem = hearingRem;
	}
	public String getCommMode() {
		return commMode;
	}
	public void setCommMode(String commMode) {
		this.commMode = commMode;
	}
	public String getCompAbility() {
		return compAbility;
	}
	public void setCompAbility(String compAbility) {
		this.compAbility = compAbility;
	}
	public String getOralMucosa() {
		return oralMucosa;
	}
	public void setOralMucosa(String oralMucosa) {
		this.oralMucosa = oralMucosa;
	}
	public String getFalseTooth() {
		return falseTooth;
	}
	public void setFalseTooth(String falseTooth) {
		this.falseTooth = falseTooth;
	}
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
	}
	public String getSkinOth() {
		return skinOth;
	}
	public void setSkinOth(String skinOth) {
		this.skinOth = skinOth;
	}
	public String getSore() {
		return sore;
	}
	public void setSore(String sore) {
		this.sore = sore;
	}
	public String getSorePosi() {
		return sorePosi;
	}
	public void setSorePosi(String sorePosi) {
		this.sorePosi = sorePosi;
	}
	public String getSoreRange() {
		return soreRange;
	}
	public void setSoreRange(String soreRange) {
		this.soreRange = soreRange;
	}
	public String getUrine() {
		return urine;
	}
	public void setUrine(String urine) {
		this.urine = urine;
	}
	public String getUrineOth() {
		return urineOth;
	}
	public void setUrineOth(String urineOth) {
		this.urineOth = urineOth;
	}
	public String getShit() {
		return shit;
	}
	public void setShit(String shit) {
		this.shit = shit;
	}
	public Integer getShitDiarr() {
		return shitDiarr;
	}
	public void setShitDiarr(Integer shitDiarr) {
		this.shitDiarr = shitDiarr;
	}
	public String getShitOth() {
		return shitOth;
	}
	public void setShitOth(String shitOth) {
		this.shitOth = shitOth;
	}
	public String getScAbility() {
		return scAbility;
	}
	public void setScAbility(String scAbility) {
		this.scAbility = scAbility;
	}
	public Integer getBraden() {
		return braden;
	}
	public void setBraden(Integer braden) {
		this.braden = braden;
	}
	public Integer getMorse() {
		return morse;
	}
	public void setMorse(Integer morse) {
		this.morse = morse;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public String getEating() {
		return eating;
	}
	public void setEating(String eating) {
		this.eating = eating;
	}
	public String getEatingOth() {
		return eatingOth;
	}
	public void setEatingOth(String eatingOth) {
		this.eatingOth = eatingOth;
	}
	public String getEatingDiet() {
		return eatingDiet;
	}
	public void setEatingDiet(String eatingDiet) {
		this.eatingDiet = eatingDiet;
	}
	public String getEatingAbn() {
		return eatingAbn;
	}
	public void setEatingAbn(String eatingAbn) {
		this.eatingAbn = eatingAbn;
	}
	public String getSmoke() {
		return smoke;
	}
	public void setSmoke(String smoke) {
		this.smoke = smoke;
	}
	public String getSmokeRem() {
		return smokeRem;
	}
	public void setSmokeRem(String smokeRem) {
		this.smokeRem = smokeRem;
	}
	public String getWine() {
		return wine;
	}
	public void setWine(String wine) {
		this.wine = wine;
	}
	public String getWineRemark() {
		return wineRemark;
	}
	public void setWineRemark(String wineRemark) {
		this.wineRemark = wineRemark;
	}
	public String getSleep() {
		return sleep;
	}
	public void setSleep(String sleep) {
		this.sleep = sleep;
	}
	public Integer getSleepDay() {
		return sleepDay;
	}
	public void setSleepDay(Integer sleepDay) {
		this.sleepDay = sleepDay;
	}
	public String getSleepMedi() {
		return sleepMedi;
	}
	public void setSleepMedi(String sleepMedi) {
		this.sleepMedi = sleepMedi;
	}
	public String getSleepMediRem() {
		return sleepMediRem;
	}
	public void setSleepMediRem(String sleepMediRem) {
		this.sleepMediRem = sleepMediRem;
	}
	public String getFstate() {
		return fstate;
	}
	public void setFstate(String fstate) {
		this.fstate = fstate;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getReligionRem() {
		return religionRem;
	}
	public void setReligionRem(String religionRem) {
		this.religionRem = religionRem;
	}
	public String getPastHis() {
		return pastHis;
	}
	public void setPastHis(String pastHis) {
		this.pastHis = pastHis;
	}
	public String getPastHisOther() {
		return pastHisOther;
	}
	public void setPastHisOther(String pastHisOther) {
		this.pastHisOther = pastHisOther;
	}
	public String getAllerHis() {
		return allerHis;
	}
	public void setAllerHis(String allerHis) {
		this.allerHis = allerHis;
	}
	public String getAllerHisMedi() {
		return allerHisMedi;
	}
	public void setAllerHisMedi(String allerHisMedi) {
		this.allerHisMedi = allerHisMedi;
	}
	public String getAllerHisFood() {
		return allerHisFood;
	}
	public void setAllerHisFood(String allerHisFood) {
		this.allerHisFood = allerHisFood;
	}
	public String getAllerHisOther() {
		return allerHisOther;
	}
	public void setAllerHisOther(String allerHisOther) {
		this.allerHisOther = allerHisOther;
	}
	public String getNurseName() {
		return nurseName;
	}
	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}
}
