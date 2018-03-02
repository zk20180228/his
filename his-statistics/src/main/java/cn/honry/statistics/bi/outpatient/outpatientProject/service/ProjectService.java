package cn.honry.statistics.bi.outpatient.outpatientProject.service;

import java.util.List;

import cn.honry.base.bean.model.BiBaseDictionary;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.util.dateVo.DateVo;

public interface ProjectService {
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
	 * @CreateDate：2016-08-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String queryProjectoadDatagrid(DateVo datevo, String dimensionString,int dateType, String dimensionValue);
	/**  
	 * @Description： 编码下拉框
	 * @Author：ldl
	 * @CreateDate：2016-08-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BiBaseDictionary> getDictionary(String type);

}
