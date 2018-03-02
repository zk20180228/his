package cn.honry.base.bean.model;

import java.io.Serializable;
import java.util.Date;

import cn.honry.base.bean.business.Entity;

public class InpAccess  extends Entity  implements Serializable {

	/**  
	 * 患者阶段性评估
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年11月15日 下午1:52:09 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年11月15日 下午1:52:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String stageId	    ;//	阶段ID
	private String inpatientNo	;//		住院流水号
	private Date accessDate	    ;//评估时间
	private String accessUser	;//	评估人
	private String accessResult	;//		评估结果
	private String roleFlag	;//		角色标志
	private String accessCheckUser	;//		评估审核人
	private Date accessCheckDate	;//	评估审核时
	private String accessCheckInfo	;//			评估审核信息
	private String days	;//		天数
	private String hospitalId	;//		所属医院
	private String areaCode	;//		所属院区
	public String getStageId() {
		return stageId;
	}
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public Date getAccessDate() {
		return accessDate;
	}
	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}
	public String getAccessUser() {
		return accessUser;
	}
	public void setAccessUser(String accessUser) {
		this.accessUser = accessUser;
	}
	public String getAccessResult() {
		return accessResult;
	}
	public void setAccessResult(String accessResult) {
		this.accessResult = accessResult;
	}
	public String getRoleFlag() {
		return roleFlag;
	}
	public void setRoleFlag(String roleFlag) {
		this.roleFlag = roleFlag;
	}
	public String getAccessCheckUser() {
		return accessCheckUser;
	}
	public void setAccessCheckUser(String accessCheckUser) {
		this.accessCheckUser = accessCheckUser;
	}
	public Date getAccessCheckDate() {
		return accessCheckDate;
	}
	public void setAccessCheckDate(Date accessCheckDate) {
		this.accessCheckDate = accessCheckDate;
	}
	public String getAccessCheckInfo() {
		return accessCheckInfo;
	}
	public void setAccessCheckInfo(String accessCheckInfo) {
		this.accessCheckInfo = accessCheckInfo;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
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
