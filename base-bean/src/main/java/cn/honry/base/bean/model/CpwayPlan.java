package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class CpwayPlan extends Entity {

	/**  
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月14日 下午8:14:49 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月14日 下午8:14:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;

	
	private String cpId	        ;//		临床路径ID
	private String versionNo	;//	临床路径版号
	private String stageId	    ;//	阶段ID
	private String chooseFlag	;//	是否为必选项目
	private String planCode	    ;//	计划类别代
	private String modelCode	;//		模板代码
	private String modelVersion	;//	模板版本号(用来存放模板名称)
	private String itemNo	    ;//	项目序号
	private String hospitalId	;//		所属医院
	private String areaCode	;//		所属院区
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getStageId() {
		return stageId;
	}
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
	public String getChooseFlag() {
		return chooseFlag;
	}
	public void setChooseFlag(String chooseFlag) {
		this.chooseFlag = chooseFlag;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelVersion() {
		return modelVersion;
	}
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
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
}
