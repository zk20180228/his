package cn.honry.statistics.bi.bistac.operationDept.service;

public interface OperationDeptTotalService {
	/**手术科室按天统计*/
	public void initOperationDeptTotalByDay(String startTime,String endTime);
	
	
	/**手术科室按月统计*/
	public void initOperationDocTotalByDay(String startTime,String endTime);


	/**手术科室按年统计*/
	public void initoperationDeptTotalByMonth(String startTime, String endTime);

	
	/**手术医生按天统计*/
	public void initoperationDeptTotalByYear(String startTime, String endTime);


	/**手术医生按月统计*/
	public void initOperationDocTotalByMouth(String startTime, String endTime);


	/**手术医生按年统计*/
	public void initOperationDocTotalByYear(String startTime, String endTime);
	/**
	 * 
	 * 
	 * <p> 预处理手术数据</p>
	 * @Author: XCL
	 * @CreateDate: 2017年8月3日 下午7:53:09 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年8月3日 下午7:53:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param begin
	 * @param end
	 * @param type:
	 *
	 */
	public void initOperation(String begin, String end, Integer type);
}
