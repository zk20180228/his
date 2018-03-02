package cn.honry.statistics.bi.bistac.totalUnDrugUsed.service;

import java.util.List;



public interface TotalUnDrugUsedService {
	/**
	 * @see 初始化非药品数据
	 * @return
	 */
	public void initUnDrugDB();
	/**
	 * @see 初始化非药品数据
	 * @return
	 */
	public void initTopFiveDeptUnDrugDB();
	/**
	 * @see 初始化非药品数据
	 * @return
	 */
	public void initTopFiveDocUnDrugDB();
/***************************************************************************************************/
	/**
	 * @see 非药品年统计分类
	 * @return
	 */
	public void initUnDrugTotalForOracleByOneDay(String begin,String end);
	/**
	 * @see 非药品科室年统计分类TOP5
	 * @return
	 */
	public void initUnDrugForOracleWithDept(String begin,String end);
	/**
	 * @see 非药品医生月统计分类TOP5
	 * @return
	 */
	public void initUnDrugForOracleWithDoc(String begin,String end);
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
		 *  预处理
		 * @param begin
		 * @param end
		 * @param type
		 */
		public void init_FYPSRFXHZ(String begin,String end,Integer type);
}
