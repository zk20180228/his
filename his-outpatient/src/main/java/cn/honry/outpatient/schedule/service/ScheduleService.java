package cn.honry.outpatient.schedule.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.service.BaseService;


/**  
 *  
 * @className：ScheduleService 
 * @Description：  挂号排班Service 
 * @Author：aizhonghua
 * @CreateDate：2015-6-18 下午01:39:35  
 * @Modifier：aizhonghua
 * @ModifyDate：2015-6-18 下午01:39:35  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface ScheduleService  extends BaseService<RegisterScheduleNow>{
	
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
	List<RegisterScheduleNow> getPage(String page, String rows,RegisterScheduleNow schedule);

	/**  
	 *  
	 * @Description：  挂号排班分页查询 - 获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:39:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:39:24  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(RegisterScheduleNow schedule);

	/**  
	 *  
	 * @Description：  挂号排班删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午01:39:24  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午01:39:24  
	 * @ModifyRmk：  
	 * @Modifier：GH
	 * @ModifyDate：2016年12月29日20:48:13  
	 * @ModifyRmk：将返回值由void改为String
	 * @version 1.0
	 *
	 */
	String del(String id);
	
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
	RegisterScheduleNow getzj(String id,String time);
	
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
	 * @Description：  保存排班信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午04:01:01  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午04:01:01  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	boolean saveOrUpdateSchedule(RegisterScheduleNow schedule)throws Exception;

	/**  
	 *  
	 * @Description：  排班同步
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午05:37:13  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午05:37:13  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int schedulSynch(String deptId);

	/**  
	 *  
	 * @Description：  排班导入
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-18 下午05:37:13  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-18 下午05:37:13  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String,Object> schedulAddTemp(String ids,String deptId,String dateTime);

	/**  
	 *  
	 * @Description：  排班导入
	 * @Author：tangfeishuai
	 * @CreateDate：2016-3-28 下午02:12:16  
	 * @Modifier：tangfeishuai
	 * @ModifyDate：2016-3-28 下午02:12:16  
	 * @ModifyRmk：  
	 * @version 2.0
	 *
	 */
	Map<String,Object> schedulAddTTemp(String ids,String deptId,String dateTime,String mdateTime);

}
