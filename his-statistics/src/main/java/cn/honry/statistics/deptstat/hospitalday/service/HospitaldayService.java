package cn.honry.statistics.deptstat.hospitalday.service;

import java.util.Map;


public interface HospitaldayService {
	/**  
	 * 
	 * 预处理在院人数
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	void queryHospitaldayList(String startTime,String endTime,Integer type);
	/**  
	 * 
	 * 查询医院每日的汇总
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	Map<String, Object> init_YYMRHZ(String beginDate, String endDate,Integer type) ;
	/**  
	 * 
	 * 预处理查询医院每日的汇总
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	void init_DRJYSJ(String startTime,String endTime,Integer type) ;
	

	/**
	 * 
	 * 
	 * <p>查询每日运营情况 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月4日 下午5:04:08 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月4日 下午5:04:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return:
	 *
	 */
	public Map<String,Object> queryDate(String begin,String end,String rows,String page);
}
