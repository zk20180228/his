package cn.honry.statistics.finance.statistic.vo;

import java.util.Date;

/**  
 *  收入统计汇总 封装查询条件VO
 * @Author:luyanshou
 * @version 1.0
 */
public class QueryVo {

	private Date startDate;//开始时间
	private Date endDate;//结束时间
	private String id;//科室id
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
