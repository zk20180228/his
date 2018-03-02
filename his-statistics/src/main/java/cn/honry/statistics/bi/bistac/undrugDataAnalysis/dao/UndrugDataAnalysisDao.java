package cn.honry.statistics.bi.bistac.undrugDataAnalysis.dao;

import java.util.List;

import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.undrugDataAnalysis.vo.UndrugDataVo;

/**  
 * 
 * <p>住院非药品收入统计分析dao </p>
 * @Author: yuke
 * @CreateDate: 2017年7月3日 下午3:54:25 
 * @Modifier: yuke
 * @ModifyDate: 2017年7月3日 下午3:54:25 
 * @ModifyRmk:  
 * @version: V1.0
 * @param:
 * @throws:
 * @return: 
 *
 */
public interface UndrugDataAnalysisDao {

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
	List<UndrugDataVo> queryUndrugDataFromOracle(List<String> tnL,
			String firstData, String endData) throws Exception;
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
	List<UndrugDataVo> queryUndrugDeptFeeTop5(List<String> tnL,
			String firstData, String endData) throws Exception;
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
	List<UndrugDataVo> queryUndrugDocFeeTop5(List<String> tnL,
			String firstData, String endData) throws Exception;
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
	List<UndrugDataVo> queryUndrugFeeMoM(List<String> tnL, String firstData,
			String endData, String type) throws Exception;
	/**
	 * 
	 * <p>从数据查询非药品收入同比</p>
	 * @Author: yuke
	 * @CreateDate: 2017年7月3日 下午3:55:42 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年7月3日 下午3:55:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: UndrugDataVo
	 *
	 */
	UndrugDataVo queryUndrugFeeSameDOM(List<String> tnL, String firstData,
			String endData, String type) throws Exception;
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
