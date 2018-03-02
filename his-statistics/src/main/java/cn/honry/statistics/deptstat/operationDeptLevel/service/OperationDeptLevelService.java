package cn.honry.statistics.deptstat.operationDeptLevel.service;

import java.util.Map;



public interface OperationDeptLevelService {

   /**  
	 * 手术科室手术分级统计信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月17日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月17日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */	
	Map<String,Object> queryOperationDeptLevel(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
   /**  
	 * 手术科室手术分级统计信息查询总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月17日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月17日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	int getTotalOperationDeptLevel(String startTime,String endTime,String deptCode,String menuAlias);
	/**
	 * 
	 * 
	 * <p>手术科室手术分级统计 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月25日 下午6:23:00 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月25日 下午6:23:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param type: 查询类型
	 *
	 */
	public void init_SSKSSSFJTJ(String begin,String end,Integer type);
}
