package cn.honry.statistics.bi.operation.operationCostList.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.operation.operationCostList.vo.OperationCostListvo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;
@SuppressWarnings({"all"})
public interface OperationCostListDao extends EntityDao<OperationCostListvo> {

	/**
	 * 加载统计
	 * @author zhangjin
	 * @createDate：2016/8/16
	 * @version 1.0
	 */
	List<OperationCostListvo> queryOperationCost(String[] dimStringArray,
			List<Map<String, List<String>>> list, int dateType, DateVo datevo);
	
}
