package cn.honry.inner.statistics.totalDrugIncome.dao;

public interface InnerTotalDrugUsedDao {
	/**
	 * 带统计大类对的药品汇总
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 */
	public void init_YPSRFXHZ_Total(String menuAlias,String type,String date);
	/**
	 * 带统计大类对的药品汇总 年月
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_YPSRFXHZ_Total_MoreDay(String menuAlias, String type, String date);
	/**
	 * 带科室的药品汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_YPSRFXHZ_Dept(String menuAlias,String type,String date);
	/**
	 * 带科室的药品汇总 年月
	 * @param menuAlias
	 * @param type 
	 * @param date
	 */
	public void init_YPSRFXHZ_Dept_MoreDay(String menuAlias, String type, String date);
	/**
	 * 带医生的药品汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_YPSRFXHZ_Doc(String menuAlias,String type,String date);
	/**
	 * 带医生的药品汇总 年月
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_YPSRFXHZ_Doc_MoreDay(String menuAlias, String type, String date);
}
