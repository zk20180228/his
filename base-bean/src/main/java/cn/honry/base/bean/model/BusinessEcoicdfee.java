package cn.honry.base.bean.model;

import java.util.Date;

import cn.honry.base.bean.business.Entity;

/**
 * 单病种定额优惠表
 * @author donghe
 * Date:2016/01/15 
 */
public class BusinessEcoicdfee extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**疾病编码 **/
	private String icdCode;
	/**疾病名称**/
	private String icdName;
	/**最小费用代码 000 全部**/
	private String FeeCode;
	/**上封顶线**/
	private Double Cost;
	/**开始日期**/
	private Date BeginDate;
	/**结束日期**/
	private Date EndDate;
	/**有效性 0 有效 1 无效 2 废弃**/
	private String ValidState;
	/**顺序号**/
	private Integer SortId;
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public String getIcdName() {
		return icdName;
	}
	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}
	public String getFeeCode() {
		return FeeCode;
	}
	public void setFeeCode(String feeCode) {
		FeeCode = feeCode;
	}
	public Double getCost() {
		return Cost;
	}
	public void setCost(Double cost) {
		Cost = cost;
	}
	public Date getBeginDate() {
		return BeginDate;
	}
	public void setBeginDate(Date beginDate) {
		BeginDate = beginDate;
	}
	public Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	public String getValidState() {
		return ValidState;
	}
	public void setValidState(String validState) {
		ValidState = validState;
	}
	public Integer getSortId() {
		return SortId;
	}
	public void setSortId(Integer sortId) {
		SortId = sortId;
	}
	
}
