package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

public class PathVsIcd extends Entity {

	/**  
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月14日 下午8:21:15 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月14日 下午8:21:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;

	
	private String cpId	        ;//临床路径ID
	private String icdName	    ;//ICD名称
	private String icdCode	    ;//ICD编码
	private String hospitalId	;//所属医院
	private String areaCode	    ;//所属院区
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getIcdName() {
		return icdName;
	}
	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
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
