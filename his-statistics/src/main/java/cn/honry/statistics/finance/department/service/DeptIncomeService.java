package cn.honry.statistics.finance.department.service;

import java.util.Map;

public interface DeptIncomeService {

	/**  
	 * @Description：  科室收入统计  elasticsearch实现
	 * @Author：朱振坤
	 * @param  date 日期 dateSign为1时格式为"yyyy-MM-dd"，为2时格式为"yyyy-MM"，为3时格式为"yyyy"，为4时格式为"yyyy-MM-dd,yyyy-MM-dd",且第一个日期小于第二个日期，
	 * @param  dateSign 按日月年查询的标记,1、按日查询；2、按月查询；3、按年查询；4、自定义日期查询，查询结果包括结束日期当天
	 *
	 */
	Map<String, Object> deptIncomeChartsByES(String date, String dateSign, String deptCodes) throws Exception;
	
}
