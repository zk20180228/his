package cn.honry.statistics.bi.bistac.listOperationStatic.service;

import cn.honry.statistics.bi.bistac.listOperationStatic.vo.ListOperationStaticVo;

public interface ListOperationStaticService {
	/**  
	 * 
	 * 手术情况统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 上午10:30:21 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 上午10:30:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param endTime 
	 *
	 */
	public ListOperationStaticVo queryVo(String startTime, String endTime);
}
