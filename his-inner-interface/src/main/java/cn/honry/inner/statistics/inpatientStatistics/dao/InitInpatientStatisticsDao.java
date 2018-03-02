package cn.honry.inner.statistics.inpatientStatistics.dao;

@SuppressWarnings({"all"})
public interface InitInpatientStatisticsDao {
    /**
     * 
     * 
     * <p>在院人数定时更新  年、月、日</p>
     * @Author: XCL
     * @CreateDate: 2017年9月18日 上午10:29:44 
     * @Modifier: XCL
     * @ModifyDate: 2017年9月18日 上午10:29:44 
     * @ModifyRmk:  
     * @version: V1.0
     * @param menuAlias
     * @param type
     * @param date
     * @throws Exception:
     *
     */
	void init_ZYRSTJ(String menuAlias, String type, String date) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>移动端在院人数更新</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月3日 下午7:05:47 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月3日 下午7:05:47 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date
	 * @throws Exception:
	 *
	 */
	void inHostNumber(String menuAlias, String type, String date) throws Exception;
}
