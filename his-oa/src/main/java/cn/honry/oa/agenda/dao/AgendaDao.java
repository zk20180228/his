package cn.honry.oa.agenda.dao;

import java.util.List;

import cn.honry.base.bean.model.Schedule;

@SuppressWarnings({"all"})
public interface AgendaDao {
	/** 
	* @Description: 根据用户id查询所有日程安排
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	List<Schedule> qeryScheduleList(String id);
	/** 
	* @Description: 保存
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	void save(Schedule schedule) throws Exception;
	/** 
	* @Description: 根据id查询日程
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	Schedule queryById(String id);
	/** 
	* @Description: 删除
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	void update(Schedule schedule) throws Exception;
	/** 
	* @Description: 分页根据用户id查询所有日程安排
	* @author qh
	 * @param rows 
	 * @param page 
	* @date 2017年7月21日
	*  
	*/
	List<Schedule> queryScheduleList(String id, String page, String rows);
	/** 
	* @Description: 根据用户id查询所有日程安排总条数
	* @author qh
	 * @param rows 
	 * @param page 
	* @date 2017年7月21日
	*  
	*/
	int queryScheduleListTotal(String id);

}
