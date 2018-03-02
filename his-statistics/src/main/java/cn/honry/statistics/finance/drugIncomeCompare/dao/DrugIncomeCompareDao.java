package cn.honry.statistics.finance.drugIncomeCompare.dao;

import java.util.List;

import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;

public interface DrugIncomeCompareDao {
	/**
	 * 
	 * <p>从数据库查询药品收入</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> queryFee(String startTime, String endTime, List<String> tnL,String type) throws Exception;
	/**
	 * 
	 * <p>从数据库查询药品收入科室前五</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> queryDeptTop5(String startTime, String endTime,
			List<String> tnL,String type) throws Exception;
	/**
	 * 
	 * <p>从数据库查询药品收入医生前五</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> queryDocTop5(String startTime, String endTime,
			List<String> tnL,String type) throws Exception;
	/**
	 * 
	 * <p>从数据库查询药品收入环比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> compareFeeByYear(String startTime, String endTime,
			List<String> tnL, String type) throws Exception;
	/**
	 * 
	 * <p>从数据库查询药品收入同比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	Dashboard compareFeeByMonthsInYears(String startTime, String endTime,
			List<String> tnL, String type) throws Exception;
	/**
	 * <p>查询mongo中是否用该数据库</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:23:38 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:23:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: boolean
	 *
	 */
	boolean isCollection(String name) throws Exception;
	/**
	 * 
	 * <p>从mongo查询药品收入医生前五</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> queryDocTop5ByMongo(String searchTime, String type,String queryMongo) throws Exception;
	/**
	 * 
	 * <p>从mongo查询药品收入科室前五</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> queryDeptTop5ByMongo(String searchTime, String type,String queryMongo) throws Exception;
	/**
	 * 
	 * <p>从mongo查询药品收入</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> queryFeeByMongo(String searchTime, String type,String queryMongo) throws Exception;
	/**
	 * 
	 * <p>从mongo查询药品收入环比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> querysequentialDrugByMongo(String searchTime, String type,String queryMongo) throws Exception;
	/**
	 * 
	 * <p>从mongo查询药品收入同比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午5:21:57 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午5:21:57 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<Dashboard>
	 *
	 */
	List<Dashboard> queryAllSameFeeByMongo(String searchTime, String type,String queryMongo) throws Exception;
}
