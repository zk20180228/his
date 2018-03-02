package cn.honry.outpatient.scheduleModel.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;

/**  
 *  
 * @className：ScheduleModelService 
 * @Description：  挂号排班模板Service 
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午01:38:22  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午01:38:22  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface ScheduleModelService  extends BaseService<RegisterSchedulemodel>{
	
	/**  
	 *  
	 * @Description：  挂号排班模板分页查询 - 获得列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:37:29  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:37:29  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterSchedulemodel> getPage(String page, String rows,RegisterSchedulemodel schedulemodel);

	/**  
	 *  
	 * @Description：  挂号排班模板分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:37:29  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:37:29  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterSchedulemodel schedulemodel);

	/**  
	 *  
	 * @Description：  挂号排班模板删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:38:28  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:38:28  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void del(String id);

	/**  
	 *  
	 * @Description：  保存或修改排班模板
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-17 下午04:25:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-17 下午04:25:24  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	boolean saveOrUpdateModel(RegisterSchedulemodel model);

	/**  
	 *  
	 * @Description：  查询某科室下某星期的全部排班模板
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterSchedulemodel> getSchedulemodel(String deptId, int week,String search);

	/**  
	 *  
	 * @Description：  查询挂号部门树
	 * @Author：aizhonghua
	 * @CreateDate：2015年12月23日 上午9:15:18  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015年12月23日 上午9:15:18  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<TreeJson> treeDeptSchedule();

	/**  
	 *  
	 * @Description：  查询科室下的员工
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午09:05:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午09:05:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysEmployee> getEmployeeByDeptId(RegisterSchedulemodel model);
	
	/**
	 * @Description 挂号排版模板导入
	 * @Author huangbiao
	 * @CreateDate 2016年3月26日
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk
	 * @version 1.0
	 */
	Map<String,Object> schedulModelAddTemp(String ids,String deptId,int nowWeek,int week);
}
