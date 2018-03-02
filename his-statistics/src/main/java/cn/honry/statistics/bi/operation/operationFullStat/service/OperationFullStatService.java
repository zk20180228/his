package cn.honry.statistics.bi.operation.operationFullStat.service;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.operation.operationFullStat.vo.OperationFullStatVo;
import cn.honry.statistics.util.dateVo.DateVo;

public interface OperationFullStatService extends BaseService<OperationFullStatVo>{
	/**	
	 * 将得到结果写成json数据格式
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年8月11日 
	 * @version 1.0
	 * @param datevo
	 * @param dimensionString
	 * @param dateType
	 * @param dimensionValue
	 * @return String
	 */
	String querytWordloadDatagrid(DateVo datevo,String[] dimStringArray,int dateType,String dimensionValue);
}
