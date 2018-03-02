package cn.honry.statistics.bi.bistac.monthlyDashboard.service;

import java.util.List;

import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;

public interface MonthlyDashboardService {
	/**  
	 * 
	 * 平均住院日
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午10:39:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午10:39:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public List<MonthlyDashboardVo> queryInpatientInfo(String deptName) throws Exception;
	/**  
	 * 
	 * 做手术人均住院日
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午10:39:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午10:39:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */	
	public List<MonthlyDashboardVo> queryOperationApplyInfo(String deptName) throws Exception;
	/**  
	 * 
	 * 月出院人数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<MonthlyDashboardVo> queryInpatientInfoNowGo(String date,String deptName) throws Exception;
	/**  
	 * 
	 * 去年出院人数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public MonthlyDashboardVo queryLastYearGo(String date,String deptName) throws Exception;
	/**  
	 * 
	 * 月手术例数
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public MonthlyDashboardVo queryOperationApply(String date,String deptCode) throws Exception;
	/**  
	 * 
	 * 病床使用率
	 * @Author: 
	 * @CreateDate: 2017年4月5日 上午11:42:19 
	 * @Modifier: 
	 * @ModifyDate: 2017年4月5日 上午11:42:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<Dashboard> queryWardApply(String date,String deptCode) throws Exception;
	/**  
	 * 
	 * 使用中床位、总床位
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午11:42:19 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午11:42:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public MonthlyDashboardVo queryBed(String date,String deptCode) throws Exception;
	/**  
	 * 
	 * 治疗数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<MonthlyDashboardVo> queryTreatment(String date,String deptName) throws Exception;
	/**  
	 * 
	 * 住院费用
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<MonthlyDashboardVo> queryHospExpenses(String startDate,String endDate,String deptName) throws Exception;
	public List<Dashboard> querySurgeryForDB(String date,String dept) throws Exception;
	/**
	 * @see 治疗数量查询
	 * @return
	 */
	public List<Dashboard> queryTreatmentFormDB(String date, String dept) throws Exception;
	/**
	 * @see 治疗数量查询(分区查询)
	 * @return
	 */
	public List<Dashboard> queryTreatment2(String dateS,String dateE, String dept) throws Exception;
	/**
	 * 导入数据到DB
	 * @return
	 */
	public boolean initDB() throws Exception;
	/**
	 * 导入数据到DB
	 * @return
	 */
	public boolean initDBTwo() throws Exception;
	/**
	 * @see 日更新
	 * @return
	 */
	public boolean initDBOneDay() throws Exception;
	
	public Boolean isCollection(String string) throws Exception;
	/**
	 * @see mongdb中没有数据时，分区查询数据
	 * @return
	 */
	public List<MonthlyDashboardVo> queryHospExpenses2(String string,
			String string2, String deptName) throws Exception;
	
	public List<Dashboard> queryWardApplyFromOracle(String startTime,
			String endTime, String deptName) throws Exception;
	public List<MonthlyDashboardVo> queryInpatientInfoFromOracle(String beginDate, String date,
			String deptName) throws Exception;
/**************************************************************************************************************************************/
	/**
	 * 住院费用日统计
	 * @param begin
	 * @param end
	 */
	public void initHospExpensesToDBMonth(String begin,String end) throws Exception;
	/**
	 * 手术例数
	 * @param begin
	 * @param end
	 */
	public void initSurgeryToDBDay(String begin,String end) throws Exception;
	/**
	 * @see 治疗数量存储每天
	 * @param date
	 * @param dept
	 * @return
	 */
	public void initTreatmentToDBDay(String begin,String end) throws Exception;
	/**
	 * 病床使用率
	 * @param tnL
	 * @param begin
	 * @param end
	 * @param hours
	 * @return
	 */
	public void initWardApplyToDBDay(String begin,String end) throws Exception;
	/**
	 * 月出院人数
	 * @param tnL
	 * @param begin
	 * @param end
	 * @param hours
	 */
	public void InitInpatientInfoToDBOneDay(String begin,String end) throws Exception;
	
	/*******************************************************************************************************************/
	  /**
		 * 月初始化
		 * @param begin
		 * @param end
		 */
		public void initMonth(String begin, String end) throws Exception;
	/*************************************************************************************************************************/
		/**
		 * 年初始化
		 * @param begin
		 * @param end
		 */
	public void initYear(String begin, String end) throws Exception;
	/**
	 * 
	 * @param begin 
	 * @param end
	 * @param type 1 日 2月 3年 
	 */
	public void init_MYZHYBP(String begin,String end,Integer type) throws Exception;
}
