package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class ModelDict extends Entity {

	/**  
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月14日 下午8:06:52 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月14日 下午8:06:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private String modelCode	;//			模板代码
	private String modelVersion	;//		模板版本号
	private String modelName	;//			模板名称
	private String inputCode	;//		输入码
	private String inputCodeWb	;//			五笔码
	private String customCode	;//			自定义码
	private String modelClass	;//		模板类别
	private String modelNature	;//		模板类别
	private String deptCode	    ;//			执行科室
	private String hospitalId	;//			所属医院
	private String areaCode	    ;//			所属院区
	
	
	/**上级**/
	private String deptParent;
	/**是否有下级**/
	private int deptHasson;
	/**层级**/
	private Integer deptLevel;
	
	
	
	public String getDeptParent() {
		return deptParent;
	}
	public void setDeptParent(String deptParent) {
		this.deptParent = deptParent;
	}
	public int getDeptHasson() {
		return deptHasson;
	}
	public void setDeptHasson(int deptHasson) {
		this.deptHasson = deptHasson;
	}
	public Integer getDeptLevel() {
		return deptLevel;
	}
	public void setDeptLevel(Integer deptLevel) {
		this.deptLevel = deptLevel;
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
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	public String getInputCodeWb() {
		return inputCodeWb;
	}
	public void setInputCodeWb(String inputCodeWb) {
		this.inputCodeWb = inputCodeWb;
	}
	public String getCustomCode() {
		return customCode;
	}
	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}
	public String getModelClass() {
		return modelClass;
	}
	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}
	public String getModelNature() {
		return modelNature;
	}
	public void setModelNature(String modelNature) {
		this.modelNature = modelNature;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
