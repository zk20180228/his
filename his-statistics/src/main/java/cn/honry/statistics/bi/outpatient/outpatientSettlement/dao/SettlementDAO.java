package cn.honry.statistics.bi.outpatient.outpatientSettlement.dao;

import java.util.List;
import java.util.Map;

import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.bi.outpatient.outpatientTotCost.vo.TotCostDimension;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface SettlementDAO {
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
	List<TotCostDimension> querytWordloadDatagrid(String[] diArrayKey,List<Map<String, List<String>>> list, int dateType, DateVo datevo);

}
