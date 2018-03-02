package cn.honry.inner.outpatient.schedule.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.HospitalParameter;
import cn.honry.base.bean.model.RegisterScheduleNow;
import cn.honry.base.dao.EntityDao;

public interface ScheduleInInterDAO extends EntityDao<RegisterScheduleNow>{

	/**  
	 * @Description：  判断数据是否已经在排班表
	 * @Author：zhangjin
	 * @CreateDate：2016-11-21
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 * @param day 
	 */
	boolean getboolean(Object object, String department, Integer week,
			String doctor, Integer midday, Date day);

	/**  
	 * @Description：  查询排班表中当天的挂号信息
	 * @Author：zhangjin
	 * @CreateDate：2016-12-3
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	List<RegisterScheduleNow> getDaydate(String day);
	/**  
	 * @Description：  获取挂号级别
	 * @Author：zhangjin
	 * @CreateDate：2016-12-3
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	Map<String,String> getRegisterGrade();
	/**  
	 * @Description：  获取预约数
	 * @Author：zhangjin
	 * @CreateDate：2016-12-3
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	int getNNt(String department, String doctor, Integer midday, Date date);
	/**  
	 * @Description：  获取参数
	 * @Author：zhangjin
	 * @CreateDate：2016-12-15
	 * @ModifyRmk：
	 * @param:   
	 * @version 1.0
	 */
	List<HospitalParameter> getParameter(String string);

}
