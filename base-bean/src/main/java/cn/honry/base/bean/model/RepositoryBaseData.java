package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**  
 * <p>知识库基础数据（术语注释） </p>
 * @Author: mrb
 * @CreateDate: 2017年11月24日 上午10:26:35 
 * @Modifier: mrb
 * @ModifyDate: 2017年11月24日 上午10:26:35 
 * @ModifyRmk:  
 * @version: V1.0 
 *RepositoryBaseData
 */
public class RepositoryBaseData extends Entity {
	/**
	 * 术语缩写
	 */
	private String term;
	/**
	 * 术语解释
	 */
	private String interpretation;
	/**
	 * 备注
	 */
	private String remark;
	
	
	
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getInterpretation() {
		return interpretation;
	}
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
