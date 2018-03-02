package cn.honry.inner.statistics.operationDeptLevel.dao;

/**
 * 
 * 
 * <p>手术科室手术分级预处理 </p>
 * @Author: XCL
 * @CreateDate: 2017年7月25日 下午5:56:00 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月25日 下午5:56:00 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface InnerOperationDeptLevelDao {
	
	/**
	 * 
	 * 
	 * <p>手术科室手术分级预处理+在线更新 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月25日 下午5:57:01 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月25日 下午5:57:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void initOperationDeptLeve(String menuAlias,String type,String date);
}
