package cn.honry.statistics.bi.outpatient.outpatientTotCost.service;

import java.util.List;

import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.util.dateVo.DateVo;

public interface TotCostService {
	/**  
	 * @Description：  BI门诊收费统计列表
	 * @Author：ldl
	 * @CreateDate：2016-08-10
	 * @ModifyRmk：  
	 * @param:dimensionVO 维度实体
	 * @version 1.0
	 */
	List<DimensionVO> findDimensionList(DimensionVO dimensionVO);
	/**  
	 * @Description： BI门诊收费统计---填充json---横版
	 * @Author：ldl
	 * @CreateDate：2016-08-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String querytWordloadDatagrid(DateVo datevo, String dimensionString,int dateType, String dimensionValue);

}
