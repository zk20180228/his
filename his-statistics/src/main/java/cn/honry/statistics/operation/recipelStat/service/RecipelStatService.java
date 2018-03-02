package cn.honry.statistics.operation.recipelStat.service;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.operation.recipelStat.vo.RecipelInfoVo;
import cn.honry.statistics.operation.recipelStat.vo.RecipelStatVo;


/**  
 *  
 * @className：AddrateService 
 * @Description： 门诊处方查询
 * @Author：aizhonghua
 * @CreateDate：2016-6-23 下午04:41:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-6-23 下午04:41:31 
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface RecipelStatService{

	/**  
	 *  
	 * 获得处方患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-23 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-23 下午04:41:31 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String,Object> getRows(String page,String rows,String startTime,String endTime,String type,String para,String vague);

	/**  
	 *  
	 * 根据处方号查询处方信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RecipelInfoVo> getRecipelInfoRows(String startTime, String endTime,String recipeNo);
	
	/**  
	 * 
	 * 打印处方患者信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月16日 上午11:06:31 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月16日 上午11:06:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<RecipelStatVo> getRecipelStatVos(String recipeNos);
	
	/**  
	 * 
	 * 打印根据处方号查询处方信息
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月16日 上午11:06:31 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月16日 上午11:06:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<RecipelInfoVo> getRecipelInfos(String startTime,String endTime,String recipeNos);
}
