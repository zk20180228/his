package cn.honry.statistics.sys.empWorkStats.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

public interface EmpWorkStatsService extends BaseService<RegisterInfo>{
	
	/**  
	 * @Description:门诊科室下拉框
	 * @Author：ldl
	 * @CreateDate：2016-06-23
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> deptCombobox();
	
	/**  
	 * @Description:坐诊医生下拉框
	 * @Author：ldl
	 * @CreateDate：2016-06-23
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param ids：挂号科室
	 */
	List<SysEmployee> empCombobox(String ids);
	
	/**  
	 * @Description:查询列表
	 * @Author：ldl
	 * @CreateDate：2016-06-23
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param:beginTime 开始时间
	 * @param:endTime 结束时间
	 * @param:dept 科室
	 * @param:emp 人员
	 */
	Map<String,Object> queryList(String beginTime, String endTime,String dept, String emp,String menuAlias,String rows,String page);
	
}
