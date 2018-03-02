package cn.honry.statistics.deptstat.operationDeptLevel.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.deptstat.operationDeptLevel.vo.OperationDeptLevelVo;

public interface OperationDeptLevelDao extends EntityDao<OperationDeptLevelVo>{
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
	 List<OperationDeptLevelVo> queryOperationDeptLevel(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
	 
	 /**
	  * 
	  * 
	  * <p>手术科室手术分级统计信息查询从预处理中获取</p>
	  * @Author: XCL
	  * @CreateDate: 2017年7月25日 下午6:48:56 
	  * @Modifier: XCL
	  * @ModifyDate: 2017年7月25日 下午6:48:56 
	  * @ModifyRmk:  
	  * @version: V1.0
	  * @param startTime
	  * @param endTime
	  * @param deptCode
	  * @param menuAlias
	  * @param page
	  * @param rows
	  * @return:
	  *
	  */
	Map<String,Object> queryOperationDeptLevelMong(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
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
	 * 手术科室手术分级统计信息从mongoDB查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年6月2日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年6月2日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	 List<OperationDeptLevelVo> queryOperationDeptLevelForDB(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows);
}
