package cn.honry.statistics.bi.bistac.totalUnDrugUsed.dao;

import java.util.List;



public interface TotalUnDrugUsedDao {
	/**
	 * @see 非药品年统计分类
	 * @return
	 */
	public void saveUnDrugTotalToDBYear();
	/**
	 * @see 非药品科室年统计分类TOP5
	 * @return
	 */
	public void saveUnDrugTotalTopFiveDeptToDBYear();
	/**
	 * @see 非药品医生月统计分类TOP5
	 * @return
	 */
	public void saveUnDrugTotalTopFiveDocToDBMonth();
/*********************日******************************************************************************/
	/**
	 * @see 非药品年统计分类
	 * @return
	 */
	public void initUnDrugTotalForOracleByOneDay(List<String> tnL,String begin,String end,Integer hours);
	/**
	 * @see 非药品科室年统计分类TOP5
	 * @return
	 */
	public void initUnDrugForOracleWithDept(List<String> tnL,String begin,String end,Integer hours);
	/**
	 * @see 非药品医生月统计分类TOP5
	 * @return
	 */
	public void initUnDrugForOracleWithDoc(List<String> tnL,String begin,String end,Integer hours);
/********************月**********************************************************************************/
	/**
	 * 药品月统计
	 * @param begin
	 * @param end
	 */
	public void initGroupUnDrugByOneDay( String begin,String end);
	/**
	 * 药品科室月统计分类TOP5
	 * @param begin
	 * @param end
	 */
	public void InitDeptUnDrugByOneDay( String begin,String end);
	/**
	 * 科室医生月统计
	 * @param begin
	 * @param end
	 */
	public void InitDocUnDrugByOneDay(String begin, String end);
/*************年*****************************************************************************************************/
	/**
	 * 药品年统计
	 * @param begin
	 * @param end
	 */
	public void initGroupUnDrugYear( String begin,String end);
	/**
	 * 药品科室年统计
	 * @param begin
	 * @param end
	 */
	public void InitDeptUnDrugYear( String begin,String end);
	/**
	 * 药品医生年统计
	 * @param begin
	 * @param end
	 */
	public void InitDocUnDrugYear(String begin, String end);
}
