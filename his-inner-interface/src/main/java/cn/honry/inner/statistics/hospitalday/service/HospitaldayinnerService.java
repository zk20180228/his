package cn.honry.inner.statistics.hospitalday.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessYzcxzhcx;




public interface HospitaldayinnerService {
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
	void init_YYMRHZ(String beginDate, String endDate,String type) ;
	/**  
	 * 
	 * 从郑东库导每日运营数据导28库
	 * @Author: donghe
	 * @CreateDate: 2017年7月25日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年7月25日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return map
	 *
	 */
	Map<String, String> saveBusinessYzcxzhcx(String date);
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
	void init_DRJYSJinner(String startTime,String endTime,Integer type) ;
	/**
	 * 查询经营日报最大的时间
	 */
	List<BusinessYzcxzhcx> queryBusinessYzcxzhcxMaxdate();
}
