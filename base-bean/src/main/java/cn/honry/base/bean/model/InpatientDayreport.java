package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpatientDayreport extends Entity{
	//统计日期
	private Date dateStat;
	//病房代码
	private String deptCode;
	//护士站代码
	private String nurseCellCode;
	//编制内病床数
	private Integer bedStand;
	//加床数
	private Integer bedAdd;
	//空床数
	private Integer bedFree;
	//期初病人数
	private Integer beginningNun;
	//常规入院数
	private Integer inNormal;
	//急诊入院数
	private Integer inEmergency;
	//其他科转入数
	private Integer intransfer;
	//招回入院人数
	private Integer inRerurn;
	//常规出院数
	private Integer outNormal;
	//转出其他科数
	private Integer outTransfer;
	//退院人数
	private Integer outWithdrawal;
	//期末病人数
	private Integer endNum;
	//24小时内死亡数
	private Integer deadIn24;
	//24小时外死亡数
	private Integer deadOut24;
	//床位使用率
	private Double bedRate;
	//其他1数量
	private Integer other1Num;
	//其他2数量
	private Integer other2Num;
	//操作人
	private String operCode;
	//整理日期
	private Date operDate;
	//备注
	private String mark;
	//其他科转入数(内部转入,中山一需求)
	private Integer inTransferInner;
	//转出其他科数(内部转入,中山一需求)
	private Integer outTransferInner;
	//出院治愈人数
	private Integer outCure;
	//出院未愈人数
	private Integer outUncure;
	//出院好转人数
	private Integer outBetter;
	//出院死亡人数
	private Integer outDeath;
	//出院其他人数 
	private Integer outOther;
	//出院患者占用总床日数
	private Integer outBeduseDay;
	//实际占用总床日数(就等于每天的期末病人数)
	private Integer allBeduseDay;
	//状态 0 未提交 1提交未审核 2 打回 3已审核
	private String state;
	//病危患者数
	private Integer dangerNum;
	//病重患者数
	private Integer heavyNum;
	//抢救人次数
	private Integer salveNum;
	//陪护人数
	private Integer attentNum;
	//输液人次
	private Integer transfusion;
	//输液反应人次
	private Integer transfusionActive;
	//输血人次
	private Integer bloodNum;
	//输血反应人次
	private Integer bloodActive;
	//成功次数
	private Integer succNum;
	//死亡人数
	private Integer deadNum;
	//是否抢救提交
	private String salveState;
	//I级护理次数
	private Integer onelvcareNum;
	//气管切开次数
	private Integer qiguanqiekaiNum;
	//气管插管次数
	private Integer qiguanchaguanNum;
	//呼吸机辅助呼吸次数
	private Integer huxifuzhuNum;
	//二级护理人次数
	private Integer twolvlcareNum;
	public Date getDateStat() {
		return dateStat;
	}
	public void setDateStat(Date dateStat) {
		this.dateStat = dateStat;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getNurseCellCode() {
		return nurseCellCode;
	}
	public void setNurseCellCode(String nurseCellCode) {
		this.nurseCellCode = nurseCellCode;
	}
	public Integer getBedStand() {
		return bedStand;
	}
	public void setBedStand(Integer bedStand) {
		this.bedStand = bedStand;
	}
	public Integer getBedAdd() {
		return bedAdd;
	}
	public void setBedAdd(Integer bedAdd) {
		this.bedAdd = bedAdd;
	}
	public Integer getBedFree() {
		return bedFree;
	}
	public void setBedFree(Integer bedFree) {
		this.bedFree = bedFree;
	}
	public Integer getBeginningNun() {
		return beginningNun;
	}
	public void setBeginningNun(Integer beginningNun) {
		this.beginningNun = beginningNun;
	}
	public Integer getInNormal() {
		return inNormal;
	}
	public void setInNormal(Integer inNormal) {
		this.inNormal = inNormal;
	}
	public Integer getInEmergency() {
		return inEmergency;
	}
	public void setInEmergency(Integer inEmergency) {
		this.inEmergency = inEmergency;
	}
	public Integer getIntransfer() {
		return intransfer;
	}
	public void setIntransfer(Integer intransfer) {
		this.intransfer = intransfer;
	}
	public Integer getInRerurn() {
		return inRerurn;
	}
	public void setInRerurn(Integer inRerurn) {
		this.inRerurn = inRerurn;
	}
	public Integer getOutNormal() {
		return outNormal;
	}
	public void setOutNormal(Integer outNormal) {
		this.outNormal = outNormal;
	}
	public Integer getOutTransfer() {
		return outTransfer;
	}
	public void setOutTransfer(Integer outTransfer) {
		this.outTransfer = outTransfer;
	}
	public Integer getOutWithdrawal() {
		return outWithdrawal;
	}
	public void setOutWithdrawal(Integer outWithdrawal) {
		this.outWithdrawal = outWithdrawal;
	}
	public Integer getEndNum() {
		return endNum;
	}
	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}
	public Integer getDeadIn24() {
		return deadIn24;
	}
	public void setDeadIn24(Integer deadIn24) {
		this.deadIn24 = deadIn24;
	}
	public Integer getDeadOut24() {
		return deadOut24;
	}
	public void setDeadOut24(Integer deadOut24) {
		this.deadOut24 = deadOut24;
	}
	public Double getBedRate() {
		return bedRate;
	}
	public void setBedRate(Double bedRate) {
		this.bedRate = bedRate;
	}
	public Integer getOther1Num() {
		return other1Num;
	}
	public void setOther1Num(Integer other1Num) {
		this.other1Num = other1Num;
	}
	public Integer getOther2Num() {
		return other2Num;
	}
	public void setOther2Num(Integer other2Num) {
		this.other2Num = other2Num;
	}
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Integer getInTransferInner() {
		return inTransferInner;
	}
	public void setInTransferInner(Integer inTransferInner) {
		this.inTransferInner = inTransferInner;
	}
	public Integer getOutTransferInner() {
		return outTransferInner;
	}
	public void setOutTransferInner(Integer outTransferInner) {
		this.outTransferInner = outTransferInner;
	}
	public Integer getOutCure() {
		return outCure;
	}
	public void setOutCure(Integer outCure) {
		this.outCure = outCure;
	}
	public Integer getOutUncure() {
		return outUncure;
	}
	public void setOutUncure(Integer outUncure) {
		this.outUncure = outUncure;
	}
	public Integer getOutBetter() {
		return outBetter;
	}
	public void setOutBetter(Integer outBetter) {
		this.outBetter = outBetter;
	}
	public Integer getOutDeath() {
		return outDeath;
	}
	public void setOutDeath(Integer outDeath) {
		this.outDeath = outDeath;
	}
	public Integer getOutOther() {
		return outOther;
	}
	public void setOutOther(Integer outOther) {
		this.outOther = outOther;
	}
	public Integer getOutBeduseDay() {
		return outBeduseDay;
	}
	public void setOutBeduseDay(Integer outBeduseDay) {
		this.outBeduseDay = outBeduseDay;
	}
	public Integer getAllBeduseDay() {
		return allBeduseDay;
	}
	public void setAllBeduseDay(Integer allBeduseDay) {
		this.allBeduseDay = allBeduseDay;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getDangerNum() {
		return dangerNum;
	}
	public void setDangerNum(Integer dangerNum) {
		this.dangerNum = dangerNum;
	}
	public Integer getHeavyNum() {
		return heavyNum;
	}
	public void setHeavyNum(Integer heavyNum) {
		this.heavyNum = heavyNum;
	}
	public Integer getSalveNum() {
		return salveNum;
	}
	public void setSalveNum(Integer salveNum) {
		this.salveNum = salveNum;
	}
	public Integer getAttentNum() {
		return attentNum;
	}
	public void setAttentNum(Integer attentNum) {
		this.attentNum = attentNum;
	}
	public Integer getTransfusion() {
		return transfusion;
	}
	public void setTransfusion(Integer transfusion) {
		this.transfusion = transfusion;
	}
	public Integer getTransfusionActive() {
		return transfusionActive;
	}
	public void setTransfusionActive(Integer transfusionActive) {
		this.transfusionActive = transfusionActive;
	}
	public Integer getBloodNum() {
		return bloodNum;
	}
	public void setBloodNum(Integer bloodNum) {
		this.bloodNum = bloodNum;
	}
	public Integer getBloodActive() {
		return bloodActive;
	}
	public void setBloodActive(Integer bloodActive) {
		this.bloodActive = bloodActive;
	}
	public Integer getSuccNum() {
		return succNum;
	}
	public void setSuccNum(Integer succNum) {
		this.succNum = succNum;
	}
	public Integer getDeadNum() {
		return deadNum;
	}
	public void setDeadNum(Integer deadNum) {
		this.deadNum = deadNum;
	}
	public String getSalveState() {
		return salveState;
	}
	public void setSalveState(String salveState) {
		this.salveState = salveState;
	}
	public Integer getOnelvcareNum() {
		return onelvcareNum;
	}
	public void setOnelvcareNum(Integer onelvcareNum) {
		this.onelvcareNum = onelvcareNum;
	}
	public Integer getQiguanqiekaiNum() {
		return qiguanqiekaiNum;
	}
	public void setQiguanqiekaiNum(Integer qiguanqiekaiNum) {
		this.qiguanqiekaiNum = qiguanqiekaiNum;
	}
	public Integer getQiguanchaguanNum() {
		return qiguanchaguanNum;
	}
	public void setQiguanchaguanNum(Integer qiguanchaguanNum) {
		this.qiguanchaguanNum = qiguanchaguanNum;
	}
	public Integer getHuxifuzhuNum() {
		return huxifuzhuNum;
	}
	public void setHuxifuzhuNum(Integer huxifuzhuNum) {
		this.huxifuzhuNum = huxifuzhuNum;
	}
	public Integer getTwolvlcareNum() {
		return twolvlcareNum;
	}
	public void setTwolvlcareNum(Integer twolvlcareNum) {
		this.twolvlcareNum = twolvlcareNum;
	}
	
	
}
