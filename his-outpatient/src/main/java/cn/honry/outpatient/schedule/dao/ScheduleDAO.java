package cn.honry.outpatient.schedule.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.RegisterSchedule;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @className：ScheduleDAO 
 * @Description：  挂号排班Dao
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午01:54:09  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午01:54:09  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface ScheduleDAO extends EntityDao<RegisterScheduleNow>{

	/**  
	 *  
	 * @Description：  挂号排班分页查询 - 获得列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:39:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:39:24  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterScheduleNow> getPage(RegisterScheduleNow entity, String page, String rows);

	/**  
	 *  
	 * @Description：  挂号排班分页查询 - 统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:39:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:39:24  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterScheduleNow entity);

	/**  
	 *  
	 * @Description：  挂号排班获得专家
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:39:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:39:24  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterScheduleNow> getzj(String id,String time);

	/**  
	 *  
	 * @Description：  挂号排班获得专家 参数 部门id 级别id 人员id
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:39:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:39:24  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<RegisterScheduleNow> findSchedule(String dept, String grade, String emplo);

	/**  
	 *  
	 * @Description：  根据科室,日期,医生,午别查询该记录是否存在 如果id为空则查询全部符合条件的信息 如果id不为空查询除此id外的全部信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-17 下午04:30:05  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-17 下午04:30:05  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	boolean findModelByDateAndDoctor(String id, String deptId, Date date,String doctorId, Integer midday);
	
	/**
	 * 根据排班id获取网络限额
	 * @param id
	 * @return
	 */
	public Integer getNetLimit(String id);
	
	/**
	 * 根据排班id获取挂号限额
	 * @param id
	 * @return
	 */
	public Integer getLimit(String id);
	
	/**
	 * 根据id获取创建日期
	 * @Author zxh
	 * @time 2017年4月12日
	 * @param id
	 * @return
	 */
	List<RegisterScheduleNow> findOldSchedule(String id);

}
