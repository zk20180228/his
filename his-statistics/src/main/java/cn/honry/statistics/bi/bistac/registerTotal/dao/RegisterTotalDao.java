package cn.honry.statistics.bi.bistac.registerTotal.dao;

import java.util.List;

public interface RegisterTotalDao {
	/**
	 * 
	 * 
	 * <p>移动端挂号量统计预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月27日 下午8:19:52 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月27日 下午8:19:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param stime
	 * @param etime:
	 *
	 */
	public void initRegisterTotal(String stime);
	/**
	 * 
	 * 
	 * <p>移动端挂号量统计预处理 </p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月27日 下午8:19:52 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月27日 下午8:19:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param stime
	 * @param etime:
	 *
	 */
	public void initRegisterTotalMonthAndYear(String stime,String type);
	/**
	 * 
	 * 
	 * <p>移动端医生挂号量</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月27日 下午8:32:53 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月27日 下午8:32:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param stime
	 * @param etime
	 * @param ghList:
	 *
	 */
	public void initRegisterDoctorTotal(String stime);
	/**
	 * 
	 * 
	 * <p>移动端医生年月挂号量预处理</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月27日 下午8:32:53 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月27日 下午8:32:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param stime
	 * @param etime
	 * @param ghList:
	 *
	 */
	public void initRegisterDoctorTotalMonthAndYear(String stime,String type);
	
	/**
	 * 
	 * 
	 * <p>从38数据库定时执行预处理</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月28日 下午2:51:48 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月28日 下午2:51:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param stime:
	 *
	 */
	public void tenTimingPerformRegisterTotal(String stime);
	
	/**
	 * 
	 * 
	 * <p>从38数据库定时执行预处理</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月28日 下午2:52:33 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月28日 下午2:52:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param stime:
	 *
	 */
	public void tenTimingPerformRegisterDoctorTotal(String stime);
	
}
