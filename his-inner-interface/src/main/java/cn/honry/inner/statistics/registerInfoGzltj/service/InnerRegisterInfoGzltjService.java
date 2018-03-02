package cn.honry.inner.statistics.registerInfoGzltj.service;

public interface InnerRegisterInfoGzltjService {

	/**
	 * 将挂号工作量统计数据存入mongodb中
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	void init(String menuAlias,String type,String date);
}
