package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;
/**
 * 医院与参数的中间关系表
 * @author    tangfeishuai
 * @version   1.0, 2016-3-31
 */

public class HospitalParameterRef extends Entity implements java.io.Serializable{
    /*
     * 医院编码	
     */
    private Integer hospitalId;
    
    /*
     * 参数编码
     */
    private String parameterId;
    
    /*
     * 扩展字段一
     */
    private String extField1;
    
    /*
     * 扩展字段二
     */
    private String extField2;

	

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getParameterId() {
		return parameterId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	public String getExtField1() {
		return extField1;
	}

	public void setExtField1(String extField1) {
		this.extField1 = extField1;
	}

	public String getExtField2() {
		return extField2;
	}

	public void setExtField2(String extField2) {
		this.extField2 = extField2;
	}
    
    
}
