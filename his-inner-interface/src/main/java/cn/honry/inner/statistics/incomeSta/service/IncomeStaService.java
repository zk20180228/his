package cn.honry.inner.statistics.incomeSta.service;

public interface IncomeStaService {
	/**
	 * 将门诊各项收入统计数据存入mongodb中
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	void init(String menuAlias,String type,String date);
}
