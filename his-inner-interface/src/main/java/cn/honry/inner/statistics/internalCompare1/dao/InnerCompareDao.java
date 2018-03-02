package cn.honry.inner.statistics.internalCompare1.dao;

/**
 * 
 * 
 * <p>内科医学部和内二医学部对比表1 </p>
 * @Author: XCL
 * @CreateDate: 2017年8月10日 下午3:50:02 
 * @Modifier: XCL
 * @ModifyDate: 2017年8月10日 下午3:50:02 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface InnerCompareDao {
	/**
	 * 
	 * 
	 * <p>内科医学部和内二医学部对比表1预处理，在线更新 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月10日 下午3:53:10 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月10日 下午3:53:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void initCompare(String menuAlias, String type, String date);
	
}
