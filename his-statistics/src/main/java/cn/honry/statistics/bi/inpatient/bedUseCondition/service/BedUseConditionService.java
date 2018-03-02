package cn.honry.statistics.bi.inpatient.bedUseCondition.service;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.service.BaseService;
import cn.honry.statistics.bi.inpatient.bedUseCondition.vo.BedUseConditionVo;

/**
 * 病床使用情况统计分析
 * @author zhuxiaolu
 * @createDate：2016/7/27
 * @version 1.0
 */
public interface BedUseConditionService extends BaseService<BedUseConditionVo>{

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
	 */
	List<BedUseConditionVo> queryBedUseCondition(String deptCode, String years);

}
