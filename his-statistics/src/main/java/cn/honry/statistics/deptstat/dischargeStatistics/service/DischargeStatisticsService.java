package cn.honry.statistics.deptstat.dischargeStatistics.service;

import java.util.List;

import cn.honry.statistics.deptstat.dischargeStatistics.vo.DischargeStatisticsVo;

public interface DischargeStatisticsService {

	List<DischargeStatisticsVo> queryIllness();

	/**  
	 * 出院病种统计
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	List<DischargeStatisticsVo> queryDischargeStat(String startTime,String endTime,String deptCode,String page, String rows, String menuAlias);

	/**  
	 * 出院病种统计 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月18日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月18日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	int getTotalDischargeStat(String startTime,String endTime,String deptCode,String page, String rows, String menuAlias);

}
