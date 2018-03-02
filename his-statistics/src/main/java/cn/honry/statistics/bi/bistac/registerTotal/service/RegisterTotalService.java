package cn.honry.statistics.bi.bistac.registerTotal.service;

public interface RegisterTotalService {
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
	public void initRegisterTotal(String begin,String end,Integer type);
	
	/**
	 * 
	 * 
	 * <p>定时查询38数据库数据</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月28日 下午4:30:31 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月28日 下午4:30:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin:
	 *
	 */
	public void tenTimingPerformRegister(String begin);
}
