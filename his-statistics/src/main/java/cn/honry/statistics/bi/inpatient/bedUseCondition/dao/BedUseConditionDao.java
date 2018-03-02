package cn.honry.statistics.bi.inpatient.bedUseCondition.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;

/**
 * 病床使用情况统计分析
 * @author zhuxiaolu
 * @createDate：2016/7/27
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface BedUseConditionDao  extends EntityDao<BedUseConditionVo> {

	/**
	 * 查询所有病区
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
	List<BedUseConditionVo> queryBedUseCondition(String deptCode, String years);

	

}
