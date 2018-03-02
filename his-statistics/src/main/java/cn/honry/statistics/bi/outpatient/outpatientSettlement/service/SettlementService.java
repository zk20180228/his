package cn.honry.statistics.bi.outpatient.outpatientSettlement.service;

import java.util.List;

import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.util.dateVo.DateVo;

public interface SettlementService {
	/**  
	 * @Description： 门诊结算费用统计列表
	 * @Author：ldl
	 * @CreateDate：2016-08-10
	 * @ModifyRmk：  
	 * @param:dimensionVO 维度实体
	 * @version 1.0
	 */
	List<DimensionVO> findDimensionList(DimensionVO dimensionVO);
	/**  
	 * @Description： BI门诊结算费用统计---填充json---横版
	 * @Author：ldl
	 * @CreateDate：2016-08-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String querytSettlementloadDatagrid(DateVo datevo, String dimensionString,int dateType, String dimensionValue);

}
