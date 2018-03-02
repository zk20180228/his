package cn.honry.statistics.bi.bistac.temporary.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.temporary.vo.HistoryRecordsInfoVo;

/**  
 *  
 * @className：HistoryRecordsDAO
 * @Description： 历史病历
 * @Author：gaotiantian
 * @CreateDate：2017-4-5 下午2:51:16 
 * @Modifier：gaotiantian
 * @ModifyDate：2017-4-5 下午2:51:16
 * @ModifyRmk：  
 * @version 1.0
 *
 */

@SuppressWarnings({"all"})
public interface HistoryRecordsDAO extends EntityDao<HistoryRecordsInfoVo> {
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

