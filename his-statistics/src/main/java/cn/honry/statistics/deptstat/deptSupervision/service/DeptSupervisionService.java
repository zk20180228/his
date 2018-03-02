package cn.honry.statistics.deptstat.deptSupervision.service;

import java.util.List;

import cn.honry.statistics.deptstat.deptSupervision.vo.MonitorIndicatorsVo;

/**
 * 
 * 
 * <p>科室监督指标统计 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月22日 上午11:38:42 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月22日 上午11:38:42 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface DeptSupervisionService {
	/**
	 * 
	 * 
	 * <p>科室监督指标统计 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月12日 下午4:13:23 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月12日 下午4:13:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param depts 科室
	 * @param menuAlias 栏目别名
	 * @param campus 院区
	 * @return: List<JournalVo>
	 *
	 */
	List<MonitorIndicatorsVo> queryDayReport(String begin,String end,String depts,String menuAlias,String campus) throws Exception;
}
