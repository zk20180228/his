package cn.honry.inner.statistics.inneroperationProportion.dao;

/**
 * 
 * 
 * <p>手术占比定时更新 </p>
 * @Author: XCL
 * @CreateDate: 2017年9月18日 下午4:46:00 
 * @Modifier: XCL
 * @ModifyDate: 2017年9月18日 下午4:46:00 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface InnerOperaPropDao {
	
	/**
	 * 
	 * 
	 * <p>手术占比定时更新 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月18日 下午4:46:55 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月18日 下午4:46:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void init_OperaProption(String menuAlias, String type, String date);
}
