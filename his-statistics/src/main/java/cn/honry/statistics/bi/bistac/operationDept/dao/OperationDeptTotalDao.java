package cn.honry.statistics.bi.bistac.operationDept.dao;


public interface OperationDeptTotalDao {
	/**手术科室按天统计*/
	public void initOperationDeptTotalByDay(String startTime,String endTime );
	/**手术科室按月统计*/
	public void initOperationDeptTotalByMonth(String startTime, String endTime);
	/**手术科室按年统计*/
	void initOperationDeptTotalByYear(String startTime, String endTime);
	/**手术医生按天统计*/
	public void initOperationDocTotalByDay(String startTime,String endTime );
	/**手术医生按月统计*/
	void initOperationDocTotalByMonth(String startTime, String endTime);
	/**手术医生按年统计*/
	void initOperationDocTotalByYear(String startTime, String endTime);

}
