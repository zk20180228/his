package cn.honry.statistics.deptstat.diagnosticRate.service;

import java.util.List;

import cn.honry.statistics.deptstat.diagnosticRate.vo.DiagnosticRateVo;

/**
 * 
 * 
 * <p>诊断符合率service </p>
 * @Author: XCL
 * @CreateDate: 2017年7月12日 上午11:22:21 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月12日 上午11:22:21 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface DiagnosticRateService {
	/**
	 * 
	 * 
	 * <p>查询门诊  住院 手术诊断符合率 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月12日 上午11:53:32 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月12日 上午11:53:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param rows 行数
	 * @param page 页数
	 * @param menuAlias 栏目别名
	 * @param depts 科室code
	 * @param  campus 院区
	 * @return:
	 *
	 */
	public List<DiagnosticRateVo> queryDiagnoSticRate(String beginTime,String endTime,String rows,
			String page,String menuAlias,String depts,String campus);
}
