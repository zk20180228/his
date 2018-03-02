package cn.honry.statistics.bi.bistac.totalDrugUsed.dao;

import java.util.List;

public interface TotalDrugUsedDao {
	/**
	 * @see 药品年统计分类
	 * @return
	 */
	public boolean saveDrugTotalToDBYear();
	/**
	 * @see 药品科室年统计分类TOP5
	 * @return
	 */
	public void saveDrugTotalTopFiveDeptToDBYear();
	/**
	 * @see 药品医生年统计分类TOP5
	 * @return
	 */
	public void saveDrugTotalTopFiveDocToDBYear();
	
	
	
	
/**************************日*********************************************************************************/
	/**
	 * @see 药品 日统计分类
	 * @return
	 */
	public void initGroupDrugByOneDay(List<String> tnL,String begin,String end,Integer hours);
	/**
	 * @see 药品科室日统计分类TOP5
	 * @return
	 */
	public void InitDeptDrugByOneDay(List<String> tnL,String begin,String end,Integer hours);
	/**
	 * @see 药品医生年统计分类TOP5
	 * @return
	 */
	public void InitDocDrugByOneDay(List<String> tnL,String begin,String end,Integer hours);
/*************月****************************************************************************************************/
	/**
	 * 药品月统计
	 * @param begin
	 * @param end
	 */
	public void initGroupDrugByOneDay( String begin,String end);
	/**
	 * 药品科室月统计分类TOP5
	 * @param begin
	 * @param end
	 */
	public void InitDeptDrugByOneDay( String begin,String end);
	/**
	 * 科室医生月统计
	 * @param begin
	 * @param end
	 */
	public void InitDocDrugByOneDay(String begin, String end);
/*************年*****************************************************************************************************/
	/**
	 * 药品年统计
	 * @param begin
	 * @param end
	 */
	public void initGroupDrugYear( String begin,String end);
	/**
	 * 药品科室年统计
	 * @param begin
	 * @param end
	 */
	public void InitDeptDrugYear( String begin,String end);
	/**
	 * 药品医生年统计
	 * @param begin
	 * @param end
	 */
	public void InitDocDrugYear(String begin, String end);
}
