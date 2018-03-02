package cn.honry.statistics.bi.outpatient.outpatientProject.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BiBaseDictionary;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.bi.outpatient.outpatientTotCost.vo.TotCostDimension;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface ProjectDAO {
	/**  
	 * @Description：  门诊项目收费均次费用分析列表
	 * @Author：liudelin
	 * @CreateDate：2016-08-10
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<DimensionVO> findDimensionList(DimensionVO dimensionVO);
	/**  
	 * @Description： BI门诊结算费用统计---填充json---横版
	 * @Author：ldl
	 * @CreateDate：2016-08-12
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<TotCostDimension> queryProjectoadDatagrid(String[] diArrayKey,List<Map<String, List<String>>> list, int dateType, DateVo datevo);
	/**  
	 * @Description： 编码下拉框
	 * @Author：ldl
	 * @CreateDate：2016-08-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BiBaseDictionary> getDictionary(String type);

}
