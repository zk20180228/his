package cn.honry.statistics.bi.inpatient.averageStayDay.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.inpatient.averageStayDay.vo.AverageStayDayVo;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;

/**
 * 平均住院日统计分析
 * @author zhuxiaolu
 * @createDate：2016/7/27
 * @version 1.0
 */

@SuppressWarnings({"all"})
public interface AverageStayDayDao extends EntityDao<AverageStayDayVo>{

	
	/**
	 * 查询所有住院科室
	 * @author zhuxiaolu
	 * @createDate：2016/7/27
	 * @version 1.0
	 */
	List<SysDepartment> queryAllDept();
	
	/**
	 * 加载列表数据
	 * @author zhuxiaolu
	 * @createDate：2016/7/27
	 * @version 1.0
	 * @return 
	 */
	List<AverageStayDayVo> queryAverageStayDay(String deptCode, String years);
}
