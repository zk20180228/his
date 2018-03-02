package cn.honry.inner.statistics.handing.service;

public interface InnerHandingService {

	/**
	 * 执行预处理方法
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 开始日期
	 */
	public void handing(String menuAlias,String type,String date);
}
