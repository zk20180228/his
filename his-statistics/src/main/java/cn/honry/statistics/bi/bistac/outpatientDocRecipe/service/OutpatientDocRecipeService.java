package cn.honry.statistics.bi.bistac.outpatientDocRecipe.service;

import java.util.Map;

public interface OutpatientDocRecipeService {
	/** 
	* @Description: 初始化门诊医生开单工作量
	* @param beginDate
	* @param endDate
	* @param type
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月6日
	*/
	void init_MZYSKDGZL(String beginDate,String endDate,Integer type)throws Exception;

	/** 
	* @Description:门诊医生开单工作量统计 
	* @param dept 科室code
	* @param expxrt 医生code
	* @param sTime 开始时间
	* @param eTime 结束时间
	* @param page 页数
	* @param rows 行数
	* @throws Exception
	* @return Map<String,Object>    返回类型 
	* @author zx 
	* @date 2017年7月26日
	*/
	Map<String, Object> listStatisticsQueryByES(String dept, String expxrt, String sTime, String eTime, String page,
			String rows) throws Exception;
}
