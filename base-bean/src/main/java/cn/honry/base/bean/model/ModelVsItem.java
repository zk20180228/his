package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class ModelVsItem extends Entity {
	
	private static final long serialVersionUID = 1L;
	
	private String modelCode;//模板代码
	private String modelVersion;//模板版本号
	private String drugRoom;//药房code
	private String drugRoomName;//药房名称
	private String itemCode;//项目代码
	private String itemName;//模板代码
	private String flag;//医嘱类型
	private Integer itemNo;//组内序号
	private String chooseFlag;//是否为必选项目
	private String unit;//单位
	private Double num;//数量
	private String frequencyCode;//频次代码
	private String directionCode;//用法代码
	private String hospitalId;
	private String areaCode;
	
	private String modelId;//模板ID
	private String modelClass;//模板类别
	
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getModelVersion() {
		return modelVersion;
	}
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}
	public String getDrugRoom() {
		return drugRoom;
	}
	public void setDrugRoom(String drugRoom) {
		this.drugRoom = drugRoom;
	}
	public String getDrugRoomName() {
		return drugRoomName;
	}
	public void setDrugRoomName(String drugRoomName) {
		this.drugRoomName = drugRoomName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Integer getItemNo() {
		return itemNo;
	}
	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}
	public String getChooseFlag() {
		return chooseFlag;
	}
	public void setChooseFlag(String chooseFlag) {
		this.chooseFlag = chooseFlag;
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
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getModelClass() {
		return modelClass;
	}
	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}
	
}
