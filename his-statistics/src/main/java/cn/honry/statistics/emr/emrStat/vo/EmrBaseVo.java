package cn.honry.statistics.emr.emrStat.vo;

import java.sql.Date;

/**  
 * 
 * <p>电子病历首页基本信息档Vo</p>
 * @Author dutianliang
 * @CreateDate 2017年11月25日 上午10:36:43
 * @Modifier dutianliang
 * @ModifyDate 2017年11月25日 上午10:36:43
 * @ModifyRmk 
 * @version V1.0
 *
 */
public class EmrBaseVo {
	/**
	 * 患者姓名
	 */
	private String name;
	
	/**
	 * 患者性别
	 */
	private String sex;
	
	/**  
	 * 
	 * @Fields inpatientNo  住院号
	 *
	 */
	private String inpatientNo;
	
	/**
	 * 入院科室
	 */
	private String deptInnm;
	
	/**
	 * 病历提交人
	 */
	private String firstSubmitOper;
	
	/**
	 * 病历提交时间
	 */
	private Date firstSubmitDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDeptInnm() {
		return deptInnm;
	}

	public void setDeptInnm(String deptInnm) {
		this.deptInnm = deptInnm;
	}

	public String getFirstSubmitOper() {
		return firstSubmitOper;
	}

	public void setFirstSubmitOper(String firstSubmitOper) {
		this.firstSubmitOper = firstSubmitOper;
	}

	public Date getFirstSubmitDate() {
		return firstSubmitDate;
	}

	public void setFirstSubmitDate(Date firstSubmitDate) {
		this.firstSubmitDate = firstSubmitDate;
	}

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	
	
	
}
