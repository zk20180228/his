package cn.honry.inner.statistics.inOutPatient.dao;

/**
 * 
 * 
 * <p>住出院人次统计预处理、在线更新 </p>
 * @Author: XCL
 * @CreateDate: 2017年8月3日 下午4:00:26 
 * @Modifier: XCL
 * @ModifyDate: 2017年8月3日 下午4:00:26 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface InOutPatientDao {
		/**
		 * 
		 * 
		 * <p>住出院人次统计 天预处理</p>
		 * @Author: XCL
		 * @CreateDate: 2017年8月3日 下午4:02:06 
		 * @Modifier: XCL
		 * @ModifyDate: 2017年8月3日 下午4:02:06 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param menuAlias
		 * @param type
		 * @param date:
		 *
		 */
		public void initZCYRCTJ(String menuAlias, String type, String date);
		/**
		 * 
		 * 
		 * <p>住出院人次统计月年 </p>
		 * @Author: XCL
		 * @CreateDate: 2017年8月3日 下午5:09:17 
		 * @Modifier: XCL
		 * @ModifyDate: 2017年8月3日 下午5:09:17 
		 * @ModifyRmk:  
		 * @version: V1.0
		 * @param menuAlias
		 * @param type
		 * @param date:
		 *
		 */
		public void initZCYRCTJMoreDay(String menuAlias, String type, String date);
}
