package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;

/**
 * @className：BusinessHlDrugGradeRef
 * @Description:药品等级表和医院表中间表对应的实体
 * @Author: huangbiao
 * @CreateDate: 2016年4月8日
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
public class BusinessHlDrugGradeRef extends Entity implements java.io.Serializable{
	
	/**
	 * @Description:药品ID
	 */
	private String contrastId;
	/**
	 * @Description:医院ID
	 */
	private Integer hospitalId;
	/**
	 * @Description:适用标识
	 */
	private Integer useFlag;
	
	public String getContrastId() {
		return contrastId;
	}
	public void setContrastId(String contrastId) {
		this.contrastId = contrastId;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(Integer useFlag) {
		this.useFlag = useFlag;
	}
	
}
