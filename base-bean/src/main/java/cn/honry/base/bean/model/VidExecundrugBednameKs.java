package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**  
 *  
 * @Description：  非药品医嘱执行档和床位信息的视图
 * @Author：donghe
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public class VidExecundrugBednameKs extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String deptId;
	private String nurseCellCode1;
	/** 患者床号姓名*/
	private String bedName;
	/** 患者姓名*/
	private String patientName;
	//医嘱执行单编号
		private String execId;
		//住院科室代码
		private String deptCode;
		//住院护理站代码
		private String  nurseCellCode;
		//开立科室代码
		private String listDpcd;
		//住院流水号
		private String inpatientNo;
		//住院病历号
		private String patientNo;
		//医嘱流水号
		private  String moOrder;
		//医嘱医师代号
		private  String docCode;
		//医嘱医师姓名
		private String docName;
		//医嘱日期
		private Date  moDate;
		//是否婴儿医嘱
		private Integer babyFlag;
		//婴儿序号
		private Integer happenNo;
		//项目属性
		private Integer setItmattr;	               
		//是否包含附材
		private Integer setSubtbl;
		//医嘱类别代码
		private String typeCode;
		//医嘱是否分解
		private Integer decmpsState;
		//是否计费
		private Integer chargeState;
		//打印执行单
		private Integer prnExelist;
		//是否打印医嘱单
		private Integer prnMorlist;
		//是否需要确认
		private Integer needConfirm;
		//项目代码
		private String undrugCode;
		//项目名称
		private String undrugName;
		//项目类别代码
		private String classCode;
		//项目类别名称
		private String className;
	    //项目执行科室代码
		private String execDpcd;
	    //项目执行科室名称
		private String execDpnm;
		//组合序号
		private String combNo;
		//主项标记
		private Integer mainDrug;
		//频次代码
		private String  dfqFreq;
		//频次名称
		private String  dfqCexp;
		//项目数量
	 	private Integer  qtyTot;
	 	//项目单位
	 	private String stockUnit;
	 	//项目单价
	    private Double unitPrice;
	    //要求执行时间
	    private Date useTime;
	    //加急标记0普通/1加急
	    private Integer emcFlag;
	    //1有效/0作废
	    private Integer validFlag;
	    //作废时间
	    private Date validDate;
	    //作废人代码
	    private String validUsercd;
	    //确认标记 0未确认/1已确认
	    private Integer confirmFlag;
	    //确认时间
	    private Date confirmDate;
	    //确认人员代码
	    private String confirmUsercd;
	    //确认科室代码
	    private String confirmDeptcd;
	    //执行标记 0未执行/1已执行
	    private Integer execFlag;
	    //执行时间
	    private Date execDate;
	    //执行人员代码
	    private String execUsercd;
	    //执行科室代码
	    private String execDeptcd;
	    //记账标记0待记账/1已
	    private Integer chargeFlag;
	    //记账时间
	    private Date chargeDate;
	    //记账人代码
	    private String chargeUsercd;
	    //记账科室代码
	    private String chargeDeptcd;
	    //检查部位检体
	    private String itemNote;
	    //申请单号
	    private String applyNo;
	    //医嘱说明
	    private String moNote1;
	    //备注
	    private String moNote2;
	    //分解时间
	    private Date decoDate; 
	    //首日量标记1是/0否/2节假日
	    private Integer firstDay;
	    //执行单打印标记 0未打印/1已
	    private Integer execPrnflag;
	    //执行单打印时间
	    private Date execPrndate;
	    //执行单打印人员
	    private String execPrnusercd;
	    //处方流水号
	    private String recipeNo;
	    //处方内流水号
	    private Integer sequenceNo;
	    //是否附材''1''是
	    private Integer subtblFlag;
	    //样本类型
	    private String labCode;
	    //检验条码号
	    private String labBarcode;
	    //收费发送单打印标记
	    private Integer chargePrnflag;
	    //收费发送单打印时间，借用转数据，存储使用时间
	    private Date chargePrndate;
	    //收费发送单打印人
	    private String chargePrnusercd;
	    //巡回卡打印标记
	    private Integer circultPrnflag;
		public String getDeptId() {
			return deptId;
		}
		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}
		public String getNurseCellCode1() {
			return nurseCellCode1;
		}
		public void setNurseCellCode1(String nurseCellCode1) {
			this.nurseCellCode1 = nurseCellCode1;
		}
		public String getBedName() {
			return bedName;
		}
		public void setBedName(String bedName) {
			this.bedName = bedName;
		}
		public String getPatientName() {
			return patientName;
		}
		public void setPatientName(String patientName) {
			this.patientName = patientName;
		}
		public String getExecId() {
			return execId;
		}
		public void setExecId(String execId) {
			this.execId = execId;
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
		public String getListDpcd() {
			return listDpcd;
		}
		public void setListDpcd(String listDpcd) {
			this.listDpcd = listDpcd;
		}
		public String getInpatientNo() {
			return inpatientNo;
		}
		public void setInpatientNo(String inpatientNo) {
			this.inpatientNo = inpatientNo;
		}
		public String getPatientNo() {
			return patientNo;
		}
		public void setPatientNo(String patientNo) {
			this.patientNo = patientNo;
		}
		public String getMoOrder() {
			return moOrder;
		}
		public void setMoOrder(String moOrder) {
			this.moOrder = moOrder;
		}
		public String getDocCode() {
			return docCode;
		}
		public void setDocCode(String docCode) {
			this.docCode = docCode;
		}
		public String getDocName() {
			return docName;
		}
		public void setDocName(String docName) {
			this.docName = docName;
		}
		public Date getMoDate() {
			return moDate;
		}
		public void setMoDate(Date moDate) {
			this.moDate = moDate;
		}
		public Integer getBabyFlag() {
			return babyFlag;
		}
		public void setBabyFlag(Integer babyFlag) {
			this.babyFlag = babyFlag;
		}
		public Integer getHappenNo() {
			return happenNo;
		}
		public void setHappenNo(Integer happenNo) {
			this.happenNo = happenNo;
		}
		public Integer getSetItmattr() {
			return setItmattr;
		}
		public void setSetItmattr(Integer setItmattr) {
			this.setItmattr = setItmattr;
		}
		public Integer getSetSubtbl() {
			return setSubtbl;
		}
		public void setSetSubtbl(Integer setSubtbl) {
			this.setSubtbl = setSubtbl;
		}
		public String getTypeCode() {
			return typeCode;
		}
		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}
		public Integer getDecmpsState() {
			return decmpsState;
		}
		public void setDecmpsState(Integer decmpsState) {
			this.decmpsState = decmpsState;
		}
		public Integer getChargeState() {
			return chargeState;
		}
		public void setChargeState(Integer chargeState) {
			this.chargeState = chargeState;
		}
		public Integer getPrnExelist() {
			return prnExelist;
		}
		public void setPrnExelist(Integer prnExelist) {
			this.prnExelist = prnExelist;
		}
		public Integer getPrnMorlist() {
			return prnMorlist;
		}
		public void setPrnMorlist(Integer prnMorlist) {
			this.prnMorlist = prnMorlist;
		}
		public Integer getNeedConfirm() {
			return needConfirm;
		}
		public void setNeedConfirm(Integer needConfirm) {
			this.needConfirm = needConfirm;
		}
		public String getUndrugCode() {
			return undrugCode;
		}
		public void setUndrugCode(String undrugCode) {
			this.undrugCode = undrugCode;
		}
		public String getUndrugName() {
			return undrugName;
		}
		public void setUndrugName(String undrugName) {
			this.undrugName = undrugName;
		}
		public String getClassCode() {
			return classCode;
		}
		public void setClassCode(String classCode) {
			this.classCode = classCode;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public String getExecDpcd() {
			return execDpcd;
		}
		public void setExecDpcd(String execDpcd) {
			this.execDpcd = execDpcd;
		}
		public String getExecDpnm() {
			return execDpnm;
		}
		public void setExecDpnm(String execDpnm) {
			this.execDpnm = execDpnm;
		}
		public String getCombNo() {
			return combNo;
		}
		public void setCombNo(String combNo) {
			this.combNo = combNo;
		}
		public Integer getMainDrug() {
			return mainDrug;
		}
		public void setMainDrug(Integer mainDrug) {
			this.mainDrug = mainDrug;
		}
		public String getDfqFreq() {
			return dfqFreq;
		}
		public void setDfqFreq(String dfqFreq) {
			this.dfqFreq = dfqFreq;
		}
		public String getDfqCexp() {
			return dfqCexp;
		}
		public void setDfqCexp(String dfqCexp) {
			this.dfqCexp = dfqCexp;
		}
		public Integer getQtyTot() {
			return qtyTot;
		}
		public void setQtyTot(Integer qtyTot) {
			this.qtyTot = qtyTot;
		}
		public String getStockUnit() {
			return stockUnit;
		}
		public void setStockUnit(String stockUnit) {
			this.stockUnit = stockUnit;
		}
		public Double getUnitPrice() {
			return unitPrice;
		}
		public void setUnitPrice(Double unitPrice) {
			this.unitPrice = unitPrice;
		}
		public Date getUseTime() {
			return useTime;
		}
		public void setUseTime(Date useTime) {
			this.useTime = useTime;
		}
		public Integer getEmcFlag() {
			return emcFlag;
		}
		public void setEmcFlag(Integer emcFlag) {
			this.emcFlag = emcFlag;
		}
		public Integer getValidFlag() {
			return validFlag;
		}
		public void setValidFlag(Integer validFlag) {
			this.validFlag = validFlag;
		}
		public Date getValidDate() {
			return validDate;
		}
		public void setValidDate(Date validDate) {
			this.validDate = validDate;
		}
		public String getValidUsercd() {
			return validUsercd;
		}
		public void setValidUsercd(String validUsercd) {
			this.validUsercd = validUsercd;
		}
		public Integer getConfirmFlag() {
			return confirmFlag;
		}
		public void setConfirmFlag(Integer confirmFlag) {
			this.confirmFlag = confirmFlag;
		}
		public Date getConfirmDate() {
			return confirmDate;
		}
		public void setConfirmDate(Date confirmDate) {
			this.confirmDate = confirmDate;
		}
		public String getConfirmUsercd() {
			return confirmUsercd;
		}
		public void setConfirmUsercd(String confirmUsercd) {
			this.confirmUsercd = confirmUsercd;
		}
		public String getConfirmDeptcd() {
			return confirmDeptcd;
		}
		public void setConfirmDeptcd(String confirmDeptcd) {
			this.confirmDeptcd = confirmDeptcd;
		}
		public Integer getExecFlag() {
			return execFlag;
		}
		public void setExecFlag(Integer execFlag) {
			this.execFlag = execFlag;
		}
		public Date getExecDate() {
			return execDate;
		}
		public void setExecDate(Date execDate) {
			this.execDate = execDate;
		}
		public String getExecUsercd() {
			return execUsercd;
		}
		public void setExecUsercd(String execUsercd) {
			this.execUsercd = execUsercd;
		}
		public String getExecDeptcd() {
			return execDeptcd;
		}
		public void setExecDeptcd(String execDeptcd) {
			this.execDeptcd = execDeptcd;
		}
		public Integer getChargeFlag() {
			return chargeFlag;
		}
		public void setChargeFlag(Integer chargeFlag) {
			this.chargeFlag = chargeFlag;
		}
		public Date getChargeDate() {
			return chargeDate;
		}
		public void setChargeDate(Date chargeDate) {
			this.chargeDate = chargeDate;
		}
		public String getChargeUsercd() {
			return chargeUsercd;
		}
		public void setChargeUsercd(String chargeUsercd) {
			this.chargeUsercd = chargeUsercd;
		}
		public String getChargeDeptcd() {
			return chargeDeptcd;
		}
		public void setChargeDeptcd(String chargeDeptcd) {
			this.chargeDeptcd = chargeDeptcd;
		}
		public String getItemNote() {
			return itemNote;
		}
		public void setItemNote(String itemNote) {
			this.itemNote = itemNote;
		}
		public String getApplyNo() {
			return applyNo;
		}
		public void setApplyNo(String applyNo) {
			this.applyNo = applyNo;
		}
		public String getMoNote1() {
			return moNote1;
		}
		public void setMoNote1(String moNote1) {
			this.moNote1 = moNote1;
		}
		public String getMoNote2() {
			return moNote2;
		}
		public void setMoNote2(String moNote2) {
			this.moNote2 = moNote2;
		}
		public Date getDecoDate() {
			return decoDate;
		}
		public void setDecoDate(Date decoDate) {
			this.decoDate = decoDate;
		}
		public Integer getFirstDay() {
			return firstDay;
		}
		public void setFirstDay(Integer firstDay) {
			this.firstDay = firstDay;
		}
		public Integer getExecPrnflag() {
			return execPrnflag;
		}
		public void setExecPrnflag(Integer execPrnflag) {
			this.execPrnflag = execPrnflag;
		}
		public Date getExecPrndate() {
			return execPrndate;
		}
		public void setExecPrndate(Date execPrndate) {
			this.execPrndate = execPrndate;
		}
		public String getExecPrnusercd() {
			return execPrnusercd;
		}
		public void setExecPrnusercd(String execPrnusercd) {
			this.execPrnusercd = execPrnusercd;
		}
		public String getRecipeNo() {
			return recipeNo;
		}
		public void setRecipeNo(String recipeNo) {
			this.recipeNo = recipeNo;
		}
		public Integer getSequenceNo() {
			return sequenceNo;
		}
		public void setSequenceNo(Integer sequenceNo) {
			this.sequenceNo = sequenceNo;
		}
		public Integer getSubtblFlag() {
			return subtblFlag;
		}
		public void setSubtblFlag(Integer subtblFlag) {
			this.subtblFlag = subtblFlag;
		}
		public String getLabCode() {
			return labCode;
		}
		public void setLabCode(String labCode) {
			this.labCode = labCode;
		}
		public String getLabBarcode() {
			return labBarcode;
		}
		public void setLabBarcode(String labBarcode) {
			this.labBarcode = labBarcode;
		}
		public Integer getChargePrnflag() {
			return chargePrnflag;
		}
		public void setChargePrnflag(Integer chargePrnflag) {
			this.chargePrnflag = chargePrnflag;
		}
		public Date getChargePrndate() {
			return chargePrndate;
		}
		public void setChargePrndate(Date chargePrndate) {
			this.chargePrndate = chargePrndate;
		}
		public String getChargePrnusercd() {
			return chargePrnusercd;
		}
		public void setChargePrnusercd(String chargePrnusercd) {
			this.chargePrnusercd = chargePrnusercd;
		}
		public Integer getCircultPrnflag() {
			return circultPrnflag;
		}
		public void setCircultPrnflag(Integer circultPrnflag) {
			this.circultPrnflag = circultPrnflag;
		}
	
}
