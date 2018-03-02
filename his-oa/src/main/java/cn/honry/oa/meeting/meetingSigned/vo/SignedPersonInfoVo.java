package cn.honry.oa.meeting.meetingSigned.vo;

import java.io.Serializable;

/**
 * 
 * <p>签到人员信息vo </p>
 * @Author: zhangkui
 * @CreateDate: 2017年8月30日 上午11:03:13 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年8月30日 上午11:03:13 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */
public class SignedPersonInfoVo implements Serializable {

	
	private static final long serialVersionUID = 2738564525678970208L;
	private String rid;//分页用的rownum，没意义
	
	
	private String pName;//姓名
	private String pAccount;//员工号
	private String deptName;//科室名字
	private String signedTime;//签到时间
	private Integer isLateNum;//迟到次数
	private Integer noComeNum;//未到次数
	
	
	
	
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpAccount() {
		return pAccount;
	}
	public void setpAccount(String pAccount) {
		this.pAccount = pAccount;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getSignedTime() {
		return signedTime;
	}
	public void setSignedTime(String signedTime) {
		this.signedTime = signedTime;
	}
	public Integer getIsLateNum() {
		return isLateNum;
	}
	public void setIsLateNum(Integer isLateNum) {
		this.isLateNum = isLateNum;
	}
	public Integer getNoComeNum() {
		return noComeNum;
	}
	public void setNoComeNum(Integer noComeNum) {
		this.noComeNum = noComeNum;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	
	
	
	

}
