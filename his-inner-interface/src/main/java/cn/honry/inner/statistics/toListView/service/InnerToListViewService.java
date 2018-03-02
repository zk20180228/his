package cn.honry.inner.statistics.toListView.service;

public interface InnerToListViewService {
	/**
	 * 将门急诊人数据存入mongodb中
	 * @param menuAlias
	 * @param type
	 * @param date
	 */
	void init(String menuAlias,String type,String date);
	
	/**
	 * 
	 * 
	 * <p> 将月和年门急诊人数据存入mongodb中 </p>
	 * @Author: yuke
	 * @CreateDate: 2017年11月7日 下午2:53:01 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年11月7日 下午2:53:01 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	public void init_MonthOrYear(String menuAlias,String type,String date);
}
