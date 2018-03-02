package cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.service;

import java.util.List;
import java.util.Map;

import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo.OutpatientIndicatorsVO;

public interface OutpatientIndicatorsService {
	/**  
	 * 
	 * <p> 门诊各项收入指标统计</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月7日 下午6:24:23 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月7日 下午6:24:23 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @param flag 是否是老表
	 * @return
	 * @throws:
	 * @return: Map<String,List<OutpatientIndicatorsVO>>
	 *
	 */
	Map<String,List<OutpatientIndicatorsVO>> findOutpatientIndicators(String sTime,String eTime,boolean flag);
}
