package cn.honry.base.bean.model;

import java.io.Serializable;

import cn.honry.base.bean.business.Entity;

public class InoroutStandardDetail extends Entity implements Serializable {

	/**  
	 * 出入径标准明细
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 上午10:43:40 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 上午10:43:40 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String standCode	    ;//	标准代码
	private String standVersionNo	;//标准版本号
	private String flag	            ;//出入径标志
	private String assessName	    ;//	评估项
	private String assessValue	    ;//	评估项值
	private Integer hospitalId;//所属医院
	private String areaCode;//所属院区
	/**
	 * 渲染字段
	 */
	private String standName;
	public String getStandCode() {
		return standCode;
	}
	public void setStandCode(String standCode) {
		this.standCode = standCode;
	}
	public String getStandVersionNo() {
		return standVersionNo;
	}
	public void setStandVersionNo(String standVersionNo) {
		this.standVersionNo = standVersionNo;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAssessName() {
		return assessName;
	}
	public void setAssessName(String assessName) {
		this.assessName = assessName;
	}
	public String getAssessValue() {
		return assessValue;
	}
	public void setAssessValue(String assessValue) {
		this.assessValue = assessValue;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getStandName() {
		return standName;
	}
	public void setStandName(String standName) {
		this.standName = standName;
	}
	
}
