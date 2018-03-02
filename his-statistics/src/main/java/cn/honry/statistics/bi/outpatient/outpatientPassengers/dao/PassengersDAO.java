package cn.honry.statistics.bi.outpatient.outpatientPassengers.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BIBaseDistrict;
import cn.honry.base.bean.model.District;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.statistics.bi.outpatient.outpatientPassengers.vo.DimensionVO;
import cn.honry.statistics.util.dateVo.DateVo;

@SuppressWarnings({"all"})
public interface PassengersDAO {
	/**  
	 * @Description： 门诊人次统计列表
	 * @Author：liudelin
	 * @CreateDate：2016-07-27
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param dimensionVO 
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
	List<DimensionVO> queryPassengersoadDatagrid(String[] diArrayKey,List<Map<String, List<String>>> list, int dateType, DateVo datevo);
	/**  
	 * @Description： 渲染城市
	 * @Author：ldl
	 * @CreateDate：2016-08-18
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<BIBaseDistrict> queryCity();

}
