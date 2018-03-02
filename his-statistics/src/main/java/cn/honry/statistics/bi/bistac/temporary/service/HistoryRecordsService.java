package cn.honry.statistics.bi.bistac.temporary.service;

import java.util.List;

import cn.honry.statistics.bi.bistac.temporary.vo.HistoryRecordsInfoVo;

/**  
 *  
 * @className：HistoryRecordsService
 * @Description： 历史病历
 * @Author：gaotiantian
 * @CreateDate：2017-4-5 下午2:51:16 
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-5 下午2:51:16
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface HistoryRecordsService {
	/**  
	 *  
	 * 根据门诊号或住院号查询病历信息
	 * @Author：gaotiantian
	 * @CreateDate：2017-4-5 下午2:51:16  
	 * @Modifier：gaotiantian
	 * @ModifyDate：2017-4-5 下午2:51:16   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<HistoryRecordsInfoVo> getHistoryRecordsInfo(String clinicNo);
}

