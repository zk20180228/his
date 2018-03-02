package cn.honry.statistics.deptstat.criticallyIllAnalysis.service;

import java.util.List;

import cn.honry.statistics.deptstat.criticallyIllAnalysis.vo.CriticallyIllAnalysisVo;


public interface CriticallyIllAnalysisService {

	/**  
	 * 重症患者占比分析
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */

	List<CriticallyIllAnalysisVo> queryCriticallyIllAnalysis(String startTime,String endTime,String deptCode,String menuAlias);
}
