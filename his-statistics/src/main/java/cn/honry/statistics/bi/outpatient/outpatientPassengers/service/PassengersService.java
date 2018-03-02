package cn.honry.statistics.bi.outpatient.outpatientPassengers.service;

import java.util.List;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.util.dateVo.DateVo;

public interface PassengersService {
	/**  
	 * @Description： 门诊人次统计列表
	 * @Author：liudelin
	 * @CreateDate：2016-07-27
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param dimensionVO 维度实体
	 */
	List<DimensionVO> findDimensionList(DimensionVO dimensionVO);
	/**  
	 * @Description： 查询地区（省级）
	 * @Author：liudelin
	 * @CreateDate：2016-07-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<District> findDistrict();
	/**  
	 * @Description： 科室
	 * @Author：liudelin
	 * @CreateDate：2016-07-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> findAllDept();
	/**  
	 * @Description： BI门诊人次---填充json---横版
	 * @Author：ldl
	 * @CreateDate：2016-08-11
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String queryPassengersoadDatagrid(DateVo datevo, String dimensionString,int dateType, String dimensionValue);
	/**  
	 * @Description： 渲染城市
	 * @Author：ldl
	 * @CreateDate：2016-08-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BIBaseDistrict> queryCity();

}
