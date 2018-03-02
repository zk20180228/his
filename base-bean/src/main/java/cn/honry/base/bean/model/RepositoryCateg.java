package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**  
 * <p>知识库分类维护 </p>
 * @Author: mrb
 * @CreateDate: 2017年11月14日 下午4:55:15 
 * @Modifier: mrb
 * @ModifyDate: 2017年11月14日 下午4:55:15 
 * @ModifyRmk:  
 * @version: V1.0 
 *RepositoryCateg
 */
public class RepositoryCateg extends Entity{
	/**
	 * 科室编码
	 */
	private String diseaseCode;
	/**
	 * 科室名称
	 */
	private String diseaseName;
	/**
	 * 类别编码
	 */
	private String code;
	/**
	 * 类别名称
	 */
	private String name;
	
	
	
	
	public String getDiseaseCode() {
		return diseaseCode;
	}
	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
