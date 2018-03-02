package cn.honry.inner.statistics.deptWorkCount.service;



public interface UpdateKSGZLTJService {
	
	/**
	 * @Description:定时更新'科室工作量统计'在线表数据，也可以更新历史数据，级联更新当月，当年
	 * @param date yyyy-MM-dd 更新时间
	 * @param type 类型1
	 * void
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月30日 上午11:22:19
	 */
	public void init_KSGZLTJ_ByDay(String menuAlias,String type,String date);
	
	/**
	 * @Description:根据type的类型，初始化'挂号科室工作量'数据,这个接口是为外边的包提供初始化日，月，年的数据的
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param type "1","2","3"
	 * int 返回往mongo中插入(包含更新)数据的条数
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月30日 上午10:16:52
	 */
	public void init_KSGZLTJ(String startTime,String endTime,Integer ty);
	
	/**
	 * 
	 * 
	 * <p>pc端门诊统计分析下医生工作量统计 </p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月17日 下午8:14:42 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月17日 下午8:14:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void initPCdoctorWorkTotal(String menuAlias,String type,String date);
	
	
	public void initPCdoctorWork(String startTime,String endTime,Integer type);
}
