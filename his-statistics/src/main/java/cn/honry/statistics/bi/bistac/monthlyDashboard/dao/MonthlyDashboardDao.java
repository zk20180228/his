package cn.honry.statistics.bi.bistac.monthlyDashboard.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;


public interface MonthlyDashboardDao extends EntityDao<MonthlyDashboardVo>{
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
public List<MonthlyDashboardVo> queryTreatment(List<String> tnL,String date,String deptName,String begin,String end) throws Exception;
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
 * @see 月出院人数存储到DB
 * @return
 */
public boolean saveInpatientInfoToDB() throws Exception;
/**
 * @see 月出院人数每天存储到DB
 * @return
 */
public boolean saveInpatientInfoToDBOneDay() throws Exception;

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
public List<MonthlyDashboardVo> queryHospExpenses(String deptName,String end) throws Exception;
/**
 * @see 住院费用保存到mongDB中按月
 * @return
 */
public boolean saveHospExpensesToDBMonth() throws Exception;
/**
 * @see 住院费用保存到mongDB中按年
 * @return
 */
public boolean saveHospExpensesToDBYear() throws Exception;
/**
 * @see 住院费用保存到mongDB中按日
 * @return
 */
public boolean saveHospExpensesToDBDay() throws Exception;
/**
 * @see 住院费用保存到mongDB中每天
 * @return
 */
public boolean saveHospExpensesToDBOneDay() throws Exception;
/**
 * @see 手术例数保存到mongDB中
 * @return
 */
public boolean saveSurgeryToDB() throws Exception;
/**
 * @see 手术例数保存到mongDB中每天
 * @return
 */
public boolean saveSurgeryToDBOneDay() throws Exception;
/**
 * @see 手术隶属查询
 * @param date
 * @param dept
 * @return
 */
public List<Dashboard> querySurgeryForDB(String date,String dept) throws Exception;
/**
 * @see 治疗数量存储
 * @param date
 * @param dept
 * @return
 */
public boolean svaeTreatmentToDB() throws Exception;
/**
 * @see 治疗数量存储（分区查询）
 * @param date
 * @param dept
 * @return
 */
public List<Dashboard> svaeTreatment(List<String> tnL, String startData,String endData,String deptName) throws Exception;
/**
 * @see 治疗数量存储每天
 * @param date
 * @param dept
 * @return
 */
public boolean svaeTreatmentToDBOneDay() throws Exception;
/**
 * @see 病床使用率
 * @return
 */
public boolean svaeWardApplyToDB() throws Exception;
/**
 * @see 病床使用率每天
 * @return
 */
public boolean svaeWardApplyToDBOneDay() throws Exception;
/**
 * @see 病床使用每天
 * @return
 */
public boolean svaeWardToDBOneDay() throws Exception;

public List<Dashboard> queryWardApplyFromDB(String date,String dept) throws Exception;

public List<Dashboard> queryTreatmentFromDB(String date,String dept) throws Exception;

/**  
 * 
 * 获取住院表的最大和最小时间
 * @Author: huzhenguo
 * @CreateDate: 2017年3月29日 上午10:51:26 
 * @Modifier: huzhenguo
 * @ModifyDate: 2017年3月29日 上午10:51:26 
 * @ModifyRmk:  
 * @version: V1.0
 *
 */
public MonthlyDashboardVo findMaxMin() throws Exception;
/**  
 * 
 * 获取费用汇总表的最大和最小时间
 * @Author: huzhenguo
 * @CreateDate: 2017年3月29日 上午10:51:26 
 * @Modifier: huzhenguo
 * @ModifyDate: 2017年3月29日 上午10:51:26 
 * @ModifyRmk:  
 * @version: V1.0
 *
 */
public MonthlyDashboardVo findFeeMaxMin() throws Exception;

public Boolean isCollection(String name) throws Exception;
/**  
 * 
 * 住院费用查询mongdb中是否有数据，没有数据分区查询数据
 * @Author: wangshujuan
 * @version: V1.0
 * @param tnL 
 *
 */
public List<MonthlyDashboardVo> queryHospExpenses2(List<String> tnL, String deptName, String endDate) throws Exception;

public List<Dashboard> queryWardApplyFromOracle(List<String> tnL,String deptName, String firstData, String endData) throws Exception;
StatVo findMaxMin1() throws Exception;
public List<MonthlyDashboardVo> queryInpatientInfoFromOracle(List<String> tnL,String deptName, String startTime, String endTime) throws Exception;



/************************新初始化数据***************************************************************************************************************************************************/
/**
 * @see 住院费用保存到mongDB 按YYYY-MM-dd存储 
 * @param tnL
 * @param begin
 * @param end
 * @param hours
 */
public  void initHospExpensesToDBDay(List<String> tnL,String begin,String end,Integer hours) throws Exception;
/**
 * @see 手术例数保存到mongDB中按天导数据
 * @return
 */
public void initSurgeryToDBDay(List<String> tnL,String begin,String end,Integer hours) throws Exception;
/**
 * @see 治疗数量存储每天
 * @param date
 * @param dept
 * @return
 */
public void initTreatmentToDBDay(List<String> tnL,String begin,String end,Integer hours) throws Exception;
/**
 * 病床使用率
 * @param tnL
 * @param begin
 * @param end
 * @param hours
 * @return
 */
public void initWardApplyToDBDay(List<String> tnL,String begin,String end,Integer hours) throws Exception;
/**
 * @see 月出院人数每天存储到DB
 * @return
 */
public void InitInpatientInfoToDBOneDay(List<String> tnL,String begin,String end,Integer hours) throws Exception;
/*********************月*******************************************************************************************/
/**
 * @see 住院费用保存到mongDB 按YYYY-MM存储  进行同环比
 * @param tnL
 * @param begin
 * @param end
 * @param hours
 */
public  void initHospExpensesToDBDay(String begin,String end) throws Exception;
/***********************************************************************************************************************/
/**
 * @see 住院费用保存到mongDB 按YYYY存储  进行同环比
 * @param tnL
 * @param begin
 * @param end
 * @param hours
 */
public  void initHospExpensesYear(String begin,String end) throws Exception;
}
