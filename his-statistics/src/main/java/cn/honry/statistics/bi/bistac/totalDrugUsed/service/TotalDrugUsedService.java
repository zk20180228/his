package cn.honry.statistics.bi.bistac.totalDrugUsed.service;



public interface TotalDrugUsedService {
	/**
	 * @see 初始化药品数据
	 * @return
	 */
	public boolean initDrugDB();
	/**
	 * @see 初始化药品数据
	 * @return
	 */
	public void initDrugTopFiveDeptDB();
	/**
	 * @see 初始化药品数据
	 * @return
	 */
	public void initDrugTopFiveDocDB();
/***************************************************************************************************************/
	/**
	 * @see 药品日统计分类
	 * @return
	 */
	public void initGroupDrugByOneDay(String begin,String end);
	/**
	 * @see 药品科室年统计分类TOP5
	 * @return
	 */
	public void InitDeptDrugByOneDay(String begin,String end);
	/**
	 * @see 药品医生年统计分类TOP5
	 * @return
	 */
	public void InitDocDrugByOneDay(String begin,String end);
/*******************************************************************************************************************/
  /**
	 * 月初始化
	 * @param begin
	 * @param end
	 */
	public void initMonth(String begin, String end);
/*************************************************************************************************************************/
	/**
	 * 年初始化
	 * @param begin
	 * @param end
	 */
	public void initYear(String begin, String end);
	/**
	 * 非药品数据预处理
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void init_YPSRFXHZ(String begin,String end,Integer type);
	
}
