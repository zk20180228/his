package cn.honry.outpatient.scheduleModel.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.RegisterSchedulemodel;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;
import cn.honry.utils.TreeJson;

/**  
 *  
 * @className：ScheduleModelDAO 
 * @Description：  排班模板Dao
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午01:55:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午01:55:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface ScheduleModelDAO extends EntityDao<RegisterSchedulemodel>{

	/**  
	 *  
	 * @Description：    排班模板分页查询 - 获得列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:55:44  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:55:44  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterSchedulemodel> getPage(RegisterSchedulemodel entity, String page, String rows);

	/**  
	 *  
	 * @Description：  排班模板分页查询 - 统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:56:02  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:56:02  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterSchedulemodel entity);

	/**  
	 *  
	 * @Description：  根据科室,星期,医生,午别查询该记录是否存在 如果id为空则查询全部符合条件的信息 如果id不为空查询除此id外的全部信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-17 下午04:30:05  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-17 下午04:30:05  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	boolean findModelByWeekAndDoctor(String id,String deptId,Integer modelWeek, String doctorId,Integer midday);

	/**  
	 *  
	 * @Description：  根据部门和星期查询
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午05:57:43  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午05:57:43  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterSchedulemodel> getScheduleByDeptAndWeek(String deptId, int week,String search);
	/**
	 * 根据id获取创建日期
	 * @Author zxh
	 * @time 2017年4月12日
	 * @param id
	 * @return
	 */
	List<RegisterSchedulemodel> findOldScheduleModel(String id);

	/**  
	 *  
	 * @Description：  根据ids获得模板信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-19 上午10:35:47  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-19 上午10:35:47  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterSchedulemodel> getScheduleByidsAndDeptIdAndRq(String ids,String deptId,int week);

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

}
