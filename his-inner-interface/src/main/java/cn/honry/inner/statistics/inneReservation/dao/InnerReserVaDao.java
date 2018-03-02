package cn.honry.inner.statistics.inneReservation.dao;

/**
 * 
 * 
 * <p>预约统计预处理 在线更新 </p>
 * @Author: XCL
 * @CreateDate: 2017年8月12日 上午10:19:16 
 * @Modifier: XCL
 * @ModifyDate: 2017年8月12日 上午10:19:16 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface InnerReserVaDao {
	/**
	 * 
	 * 
	 * <p>门诊预约统计预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月12日 上午10:20:37 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月12日 上午10:20:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void initReserVation(String menuAlias,String type,String date);
}
