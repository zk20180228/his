package cn.honry.base.bean.model;

import java.util.Date;
import java.util.List;
/**
 * 报表以javaBean的形式作为数据源,门诊挂号员日结单
 * @author  zpty
 * @date 创建时间：2017年3月7日
 * @version 1.0
 * @parameter 
 * @since 
 * @return  
 */
public class RegisterBalancedetailVo {
	/**日结序号**/
	private String balanceNo;
	/**开始时间**/
	private Date startTime;
	/**结束时间**/
	private Date endTime;
	/**处方数量**/
	private Integer totQty;
	/**财务审核1未审核/0已审核**/
	private Integer checkFlag;
	/**审核人**/
	private String checkUser;
	/**审核时间**/
	private Date checkDate;
	/**结算类型 0-个人结算，1-财务组结算**/
	private Integer balanceType;
	/**财务组,在财务组日结功能中使用**/
	private String groupCode;
	
	/**添加字段（收入）**/
	private Double income;
	/**添加字段（退费）**/
	private Double refund;
	/**添加字段（实际收入）**/
	private Double actualIncome;
	/**添加字段（总挂号人数）**/
	private Integer inNum;
	/**添加字段（退号人数）**/
	private Integer outNum;
	/**添加字段（实际看诊人数）**/
	private Integer actualNum;
	/**添加字段（开始时间）**/
	private String startTimeStr;
	/**添加字段（结束时间）**/
	private String endTimeStr;
	
    private List<RegisterBalancedetail> regBaldetList;//子表数据

	public String getBalanceNo() {
		return balanceNo;
	}

	public void setBalanceNo(String balanceNo) {
		this.balanceNo = balanceNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getTotQty() {
		return totQty;
	}

	public void setTotQty(Integer totQty) {
		this.totQty = totQty;
	}

	public Integer getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Integer getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}

	public Double getRefund() {
		return refund;
	}

	public void setRefund(Double refund) {
		this.refund = refund;
	}

	public Double getActualIncome() {
		return actualIncome;
	}

	public void setActualIncome(Double actualIncome) {
		this.actualIncome = actualIncome;
	}

	public Integer getInNum() {
		return inNum;
	}

	public void setInNum(Integer inNum) {
		this.inNum = inNum;
	}

	public Integer getOutNum() {
		return outNum;
	}

	public void setOutNum(Integer outNum) {
		this.outNum = outNum;
	}

	public Integer getActualNum() {
		return actualNum;
	}

	public void setActualNum(Integer actualNum) {
		this.actualNum = actualNum;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public List<RegisterBalancedetail> getRegBaldetList() {
		return regBaldetList;
	}

	public void setRegBaldetList(List<RegisterBalancedetail> regBaldetList) {
		this.regBaldetList = regBaldetList;
	}

}
