package cn.honry.base.bean.model;

import cn.honry.base.bean.business.Entity;


/**
 * 
 * 
 * @author wujiao
 * 2015-06-03
 *黑名单
 */
public class EmployeeBlacklist extends Entity{
	
	/**唯一编号(主键)**/
	private SysEmployee dmployeeId;

	public SysEmployee getDmployeeId() {
		return dmployeeId;
	}

	public void setDmployeeId(SysEmployee dmployeeId) {
		this.dmployeeId = dmployeeId;
	}
	
	private String resaon;

	public String getResaon() {
		return resaon;
	}

	public void setResaon(String resaon) {
		this.resaon = resaon;
	}
	
}
