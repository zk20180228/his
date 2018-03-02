package cn.honry.statistics.bi.operation.operationCostList.service;


import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.operation.operationCostList.vo.OperationCostListvo;
import cn.honry.statistics.util.dateVo.DateVo;
@SuppressWarnings({"all"})
public interface OperationCostListService extends BaseService<OperationCostListvo>{

	/**
	 * 加载统计
	 * @author zhangjin
	 * @createDate：2016/8/16
	 * @version 1.0
	 */
	String queryOperationCost(DateVo datevo, String[] dimStringArray,
			int dateType, String dimensionValue);


}
