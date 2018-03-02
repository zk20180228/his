package cn.honry.statistics.bi.bistac.deptAndFeeData.service;

@SuppressWarnings({"all"})
public interface DeptAndFeeDataService {
	/**
	 * 住院收入前十名科室和收费项目饼状图统计
	 * @throws Exception 
	 */
	public String deptAndFeeData(String date) throws Exception;
	/**
	 * 住院收入同环比统计
	 * @throws Exception 
	 */
	public String tonghuanbiDataByES(String date, Integer num) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>pc端住院收入统计饼状图</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月25日 下午8:41:50 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月25日 下午8:41:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date
	 * @param num
	 * @return
	 * @throws Exception:
	 *
	 */
	public String queryInpatientChartsByES(String date, String dateSign) throws Exception;

	/**
	 *
	 *
	 * <p>查询住院收入</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月25日 下午7:57:17
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月25日 下午7:57:17
	 * @ModifyRmk:
	 * @version: V1.0
	 * @param date
	 * @param dateSign
	 * @return:
	 *
	 */
	String queryInpatientChartsByMongo(String date,String dateSign);
}
