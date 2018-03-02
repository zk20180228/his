package cn.honry.statistics.bi.bistac.mongoDataInit.vo;

/**
 * 
 * <p>医生工作量统计vo </p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月17日 下午3:36:22 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月17日 下午3:36:22 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */

public class DoctorWorkCountVo {
	
	private String regDate;//挂号日期
	private String doctorName;//医生的名字
	private String doctorCode;//医生的编号
	private int doctorGzlNum;//挂号的数量
	private Double doctorCost;//医生费用
	
	
	public Double getDoctorCost() {
		return doctorCost;
	}
	public void setDoctorCost(Double doctorCost) {
		this.doctorCost = doctorCost;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public int getDoctorGzlNum() {
		return doctorGzlNum;
	}
	public void setDoctorGzlNum(int doctorGzlNum) {
		this.doctorGzlNum = doctorGzlNum;
	}

	

}
