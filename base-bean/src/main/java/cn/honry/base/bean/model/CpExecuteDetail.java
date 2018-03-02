package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class CpExecuteDetail  extends Entity implements Serializable {
	/**  
	 * 临床信息执行明细
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 下午1:37:06 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 下午1:37:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Date    startDate	;//	开始时间
	private Date    endDate	    ;//	结束时间
	private String itemCode	;//			项目代码
	private String itemName	;//			项目名称
	private Integer itemNo	    ;//组内序号
	private String orderType	;//	医嘱类型
	private String unit	    ;//	单位
	private Double num	        ;//		数量
	private String frequencyCode	;//频次代码
	private String directionCode	;//用法代码
	private Double useDays	    ;//			付数
	private String specimen	;//			标本
	private String executeDeptCode	;//		执行科室代码
	private String stopFlag	;//	停止标志
	private String hospitalId	;//		所属医院
	private String areaCode	;//		所属院区
	private String executeFlag;// 执行标志
	
	
	private String inpatientNo	   ;//	住院流水号
	private String modelCode	 ;//			模板代码
	private String versionCode	    ;//	临床路径版本号
	private String cpId	        ;//		临床路径ID
	
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getDirectionCode() {
		return directionCode;
	}
	public void setDirectionCode(String directionCode) {
		this.directionCode = directionCode;
	}
	public Double getUseDays() {
		return useDays;
	}
	public void setUseDays(Double useDays) {
		this.useDays = useDays;
	}
	public String getSpecimen() {
		return specimen;
	}
	public void setSpecimen(String specimen) {
		this.specimen = specimen;
	}
	public String getExecuteDeptCode() {
		return executeDeptCode;
	}
	public void setExecuteDeptCode(String executeDeptCode) {
		this.executeDeptCode = executeDeptCode;
	}
	public String getStopFlag() {
		return stopFlag;
	}
	public void setStopFlag(String stopFlag) {
		this.stopFlag = stopFlag;
	}
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getExecuteFlag() {
		return executeFlag;
	}
	public void setExecuteFlag(String executeFlag) {
		this.executeFlag = executeFlag;
	}
	
}
