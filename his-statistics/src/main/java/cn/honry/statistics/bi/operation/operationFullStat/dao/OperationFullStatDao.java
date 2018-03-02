package cn.honry.statistics.bi.operation.operationFullStat.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.operation.operationFullStat.vo.OperationFullStatVo;
import cn.honry.statistics.util.dateVo.DateVo;

public interface OperationFullStatDao extends EntityDao<OperationFullStatVo>{


	/***
	 * 根据查询条件查询全院手术量和涨跌比
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年8月8日 
	 * @version 1.0
	 */
	List<OperationFullStatVo> queryOperationFullStatVo(String[] diArrayKey, List<Map<String, List<String>>> list,
			int dateType, DateVo datevo);
}
