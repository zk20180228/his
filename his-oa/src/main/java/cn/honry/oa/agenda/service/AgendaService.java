package cn.honry.oa.agenda.service;

import java.util.List;

import cn.honry.base.bean.model.Schedule;

@SuppressWarnings({"all"})
public interface AgendaService {
	/** 
	* @Description: 根据用户id查查询所有的日程
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
	* @Description: 删除
	* @author qh
	 * @throws Exception 
	* @date 2017年7月19日
	*  
	*/
	void del(String t) throws Exception;
	/** 
	* @Description:根据低查询日程安排
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	Schedule queryById(String t);
	/** 
	* @Description: 更新日程
	* @author qh
	* @date 2017年7月19日
	*  
	*/
	void update(Schedule schedule) throws Exception;
	/** 
	* @Description: 分页根据用户id查查询所有的日程
	* @author qh
	 * @param rows 
	 * @param page 
	* @date 2017年7月21日
	*  
	*/
	List<Schedule> queryScheduleList(String id, String page, String rows);
	/** 
	* @Description: 根据用户id查查询所有的日程总数量
	* @author qh
	 * @param rows 
	 * @param page 
	* @date 2017年7月21日
	*  
	*/
	int queryScheduleListTotal(String id);

}
