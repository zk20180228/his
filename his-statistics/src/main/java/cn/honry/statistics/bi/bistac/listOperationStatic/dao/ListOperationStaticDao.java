package cn.honry.statistics.bi.bistac.listOperationStatic.dao;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.listOperationStatic.vo.ListOperationStaticVo;

public interface ListOperationStaticDao extends EntityDao<ListOperationStaticVo>{
	/**  
	 * 
	 * 手术情况统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 上午10:30:21 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 上午10:30:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ListOperationStaticVo queryVo(String startTime, String endTime);
}
