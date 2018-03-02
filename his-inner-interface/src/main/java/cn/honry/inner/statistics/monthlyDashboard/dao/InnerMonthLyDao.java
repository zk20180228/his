package cn.honry.inner.statistics.monthlyDashboard.dao;

public interface InnerMonthLyDao {
	/**
	 * 月分析仪表盘 住院费用
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MYZHYBP_HospExpenses(String menuAlias,String type,String date);
	/**
	 * 月分析仪表盘 住院费用 月年
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MYZHYBP_HospExpenses_MoreDay(String menuAlias, String type, String date);
	/**
	 * 月分析仪表盘 手术例数
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MYZHYBP_Surgery(String menuAlias,String type,String date);
	/**
	 * 月分析仪表盘 治疗数量
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MYZHYBP_Treatment(String menuAlias,String type,String date);
	/**
	 * 月分析仪表盘 病床使用率
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MYZHYBP_WardApply(String menuAlias,String type,String date);
	/**
	 * 月分析仪表盘 月出院人数
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_MYZHYBP_Inpatient(String menuAlias,String type,String date);
	
}
