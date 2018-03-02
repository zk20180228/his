package cn.honry.inner.statistics.peopleNumOfOperation.dao;

public interface InnerPeopleNumOfOperationDao {
	/**
	 * 手术科室手术人数在线更新
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	public void init_SSKSSSRSTJ(String menuAlias,String type,String date);
	
	/**
	 * 
	 * 
	 * <p>手术科室手术人数在线更新  </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月25日 上午10:54:10 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月25日 上午10:54:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目名
	 * @param type 时间类型 
	 * @param date: 时间
	 *
	 */
	public void init_SSKSSSRSTJ_Day(String menuAlias, String type, String date);
}
