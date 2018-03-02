package cn.honry.inner.statistics.totalUndrugIncome.dao;

public interface InnerTotalUnDrugDao {
	/**
	 * 带统计大类对的非药品汇总
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 */
	public void init_FYPSRFXHZ_Total(String menuAlias,String type,String date);
	/**
	 * 带统计大类对的非药品汇总年月
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_FYPSRFXHZ_Total_MoreDay(String menuAlias, String type, String date);
	/**
	 * 年月
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_FYPSRFXHZ_Dept_MoreDay(String menuAlias, String type, String date);
	/**
	 * 带科室的非药品汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_FYPSRFXHZ_Dept(String menuAlias,String type,String date);
	/**
	 * 带医生的非药品汇总 年月
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_FYPSRFXHZ_Doc_MoreDay(String menuAlias, String type, String date);
	/**
	 * 带医生的非药品汇总
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_FYPSRFXHZ_Doc(String menuAlias,String type,String date);
}
