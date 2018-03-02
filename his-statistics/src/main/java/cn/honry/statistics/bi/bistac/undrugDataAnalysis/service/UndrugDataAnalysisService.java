package cn.honry.statistics.bi.bistac.undrugDataAnalysis.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.vo.UndrugDataVo;

public interface UndrugDataAnalysisService {

	/**
	 * 
	 * <p> 从数据库查询非药品收入 </p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:54:55 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:54:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugTypeFee(String startTime, String endTime) throws Exception;
	
	/**
	 * 
	 * <p>从数据库查询非药品科室收入前五 </p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugDeptFeeTop5(String startTime, String endTime) throws Exception;

	/**
	 * 
	 * <p>渲染科室</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午4:04:37 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午4:04:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: Map<String,String>
	 *
	 */
	Map<String, String> querydeptCodeAndNameMap() throws Exception;

	/**
	 * 
	 * <p>从数据库查询非药品医生收入前五 </p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugDocFeeTop5(String startTime, String endTime) throws Exception;

	/**
	 * 
	 * <p>从数据查询非药品收入环比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugFeeMoM(String stime, String etime, String type) throws Exception;

	/**
	 * 
	 * <p>从数据查询非药品收入同比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	UndrugDataVo queryUndrugFeeSameDOM(String stime, String etime, String type) throws Exception;

	/**
	 * 
	 * <p>从mongo中按类型查询非药品收入</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugTypeFeeFromDB(String searchTime, String t,String queryMongo) throws Exception;

	/**
	 * 
	 * <p>从mongo中查询非药品科室收入前五</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugDeptFeeTop5FromDB(String searchTime, String t,String queryMongo) throws Exception;

	/**
	 * 
	 * <p>从mongo中查询非药品医生收入前五</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugDocFeeTop5FromDB(String searchTime, String t,String queryMongo) throws Exception;

	/**
	 * 
	 * <p>从mongo中查询非药品收入环比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugFeeSameDOMFromDB(String searchTime, String t,String queryMongo) throws Exception;

	/**
	 * 
	 * <p>从mongo中查询非药品收入同比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:22 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<UndrugDataVo>
	 *
	 */
	List<UndrugDataVo> queryUndrugFeeMoMFromDB(String searchTime, String t,String queryMongo) throws Exception;

}
