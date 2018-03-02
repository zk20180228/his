package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InoroutStandard extends Entity implements Serializable{

	/**  
	 * 出入径标准表
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 上午10:40:37 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 上午10:40:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private String standCode	;//			标准代码
	private String standVersionNo	;//			标准版本
	private String standName	;//			标准名称
	private String inputCode	;//		输入码
	private String inputCodeWb	;//			五笔码
	private String customCode	;//			自定义码
	private Integer hospitalId	;//			所属医院
	private String areaCode	    ;//			所属院区
	
	/**
	 *渲染字段
	 */
	private String createUserName;
	
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
	public String getStandName() {
		return standName;
	}
	public void setStandName(String standName) {
		this.standName = standName;
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
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

}
