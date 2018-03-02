package cn.honry.statistics.deptstat.inpatientStatistics.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.deptstat.inpatientStatistics.vo.InpatientStatisticsVo;

@SuppressWarnings({"all"})
public interface InpatientStatisticsService {
	/** 
	* @Description: 获取环比时间轴数据
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<String> getHTimeList(String time);
	/** 
	* @Description: 获取同比时间轴数据
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<String> getTTimeList(String time);
	/** 
	* @Description: 科室下拉
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	Map<String, String> queryDeptCodeName();
	/** 
	* @Description: 查询在院人数数据
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<List<InpatientStatisticsVo>> queryDataList(String code,
			List<String> htimeList, String string) throws Exception;
	/** 
	* @Description: 获取院区下拉
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	Map<String, String> queryAreaCodeName();
	/** 
	* @Description: 根据院区code获取科室
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	String queryDeptByAreaCode(String code);
	/** 
	* @Description:获取院区下拉
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	List<SysDepartment> queryArea();

	/** 
	* @Description: 初始化数据（非实时）；
	* @author qh
	* @date 2017年7月15日
	*  
	*/
	void init_ZYRSTJ(String begin, String end, Integer type) throws Exception;
	/**
	 * 
	 * 
	 * <p>根据院区查询科室 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月2日 下午3:19:47 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月2日 下午3:19:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code 院区code
	 * @return:
	 *
	 */
	String queryDeptByAreaCodes(String code);
}
