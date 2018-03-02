package cn.honry.outpatient.newInfo.vo;

import java.io.Serializable;

/**
 * 
 * <p>通过科室的编号查询科室 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年9月20日 下午6:55:36 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年9月20日 下午6:55:36 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class HospitalVo implements Serializable{


	private static final long serialVersionUID = 8508190236334918337L;
	
	private Integer hospital_id;//所属医院
	private String dept_area_code;//所属院区
	

	public Integer getHospital_id() {
		return hospital_id;
	}
	public void setHospital_id(Integer hospital_id) {
		this.hospital_id = hospital_id;
	}
	public String getDept_area_code() {
		return dept_area_code;
	}
	public void setDept_area_code(String dept_area_code) {
		this.dept_area_code = dept_area_code;
	}

	
	
	
	
	

}
