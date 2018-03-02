package cn.honry.statistics.bi.bistac.listTotalIncomeStatic.dao;

import java.util.Date;
import java.util.List;

import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;

public interface ListTotalIncomeStaticDao extends EntityDao<ListTotalIncomeStaticVo>{
	/**  
	 * 
	 * 总收入情况统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月4日 下午4:09:43 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月4日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ListTotalIncomeStaticVo queryVo(List<String> tnLs,String date);
	/**  
	 * 
	 * 获取处方明细表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月4日 下午4:09:43 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月4日 下午4:09:43  
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ListTotalIncomeStaticVo findFeeMaxMin1();
	/**  
	 * 
	 * 获取费用汇总表的最大和最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月4日 下午4:09:43  
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月4日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ListTotalIncomeStaticVo findFeeMaxMin2();
	/**  
	 * 
	 * 获取所有的统计大类名称
	 * @Author: donghe
	 * @CreateDate: 2017年5月9日 下午4:09:43  
	 * @Modifier: donghe
	 * @ModifyDate: 2017年5月9日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<MinfeeStatCode> queryFeeName();
	/**
	 * @see 收入情况统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return
	 * @author conglin
	 */
	public List<ListTotalIncomeStaticVo> queryTotalCount(String begin,String end,String dateSign);
	/**
	 * @see 初始化统计年
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYear();
	/**
	 * @see 初始化统计年带科室
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYearWithDept();
	/**
	 * @see 初始化统计年门诊/科室
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYearWithDeptTotal();
	/**
	 * @see 初始化统计年日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYearOneDay();
	/**
	 * @see 初始化统计年日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYearOneDayToApp();
	/**
	 * @see 初始化统计月
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongMonth();
	/**
	 * @see 初始化统计月移动端数据
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongMonthWithDept();
	/**
	 * @see 初始化统计月移动端数据门诊/住院
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongMonthWithDeptTotal();
	/**
	 * @see 初始化统计月日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongMonthOneDay();
	/**
	 * @see 初始化统计月日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongMonthOneDayToAPP();
	/**
	 * @see 初始化统计日
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongDate();
	/**
	 * @see 初始化统计日移动端
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongDateWithDept();
	/**
	 * @see 初始化统计日移动端门诊 住院
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongDateWithDeptTotal();
	/**
	 * @see 初始化统计日日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongDateOneDay();
	/**
	 * @see 初始化统计日日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongDateOneDayTOAPP();
	/**
	 * 分区查询医院总收入情况
	 * @param tnL
	 * @param mainL
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<ListTotalIncomeStaticVo> queryTotalIncom(List<String> tnL,List<String> mainL,String begin,String end);
	/**
	 * 医院收入统计 环比 6年 6月6日 月份两位 dateSign1 年 dateSign2 月 dateSign3日
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return 
	 */
	List<Dashboard> queryTotalCount(String begin, String dateSign);
	/**
	 * 医院收入统计 环比 6年 6月6日 月份两位 dateSign1 年 dateSign2 月 dateSign3日 ----->分区查
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return 
	 */
	List<Dashboard> queryTotalCountHb(List<String> tnL,List<String> mainL,String begin, String dateSign);
	/**
	 * 医院收入统计 同比6年内同月 或同日  月份两位  dateSign2 月 dateSign3日 比
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return 
	 */
	List<Dashboard> queryTotalsequential(String begin, String dateSign);
	/**
	 * 医院收入统计 同比 6月6日 月份两位 dateSign1 年 dateSign2 月 dateSign3日 ----->分区查
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return 
	 */
	List<Dashboard> queryTotalCountTb(List<String> tnL,List<String> mainL,String begin, String dateSign);
	/**
	 * 医院收入统计 环装图门诊/住院 月份两位 dateSign1 年 dateSign2 月 dateSign3日 门诊住院统计
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return 
	 */
	List<Dashboard> queryTotalCountMZZY(String begin, String dateSign);
	/**
	 * 住院收入科室
	 */
	void initDeptTotal();
	/**
	 * 住院收入科室
	 */
	void initDoctTotal();
	
	
	
/*********************************按日统计*********************************************************************************************/
	/**
	 * 带明细的统计
	 * @param tnL
	 * @param mainL
	 * @param begin
	 * @param end
	 * @param hours
	 */
	public void initToDBByDayOrHours(List<String> tnL,List<String> mainL,String begin,String end,Integer hours);
	/**
	 * 住院收入科室
	 */
	void initDeptTotalByDayOrHours(List<String> tnL,List<String> mainL,String begin,String end,Integer hours);
	/**
	 * 总收入医生
	 */
	void initDoctTotalByDayOrHours(List<String> tnL,List<String> mainL,String begin,String end,Integer hours);
	/**
	 * 门诊住院统计
	 * @param tnL
	 * @param mainL
	 * @param begin
	 * @param end
	 * @param hours
	 */
	void initMZZYTotalByDayOrHours(List<String> tnL,List<String> mainL,String begin,String end,Integer hours);
	/**
	 * 总收入情况同环比数据
	 * @param tnL
	 * @param mainL
	 * @param begin
	 * @param end
	 * @param hours
	 */
	void initTotalWithDateToAPP(List<String> tnL,List<String> mainL,String begin,String end,Integer hours);
/*******************************月初始化**************************************************************************************/
	/**
	 * 带明细的统计 月初始化
	 * @param tnL
	 * @param mainL
	 * @param begin
	 * @param end
	 * @param hours
	 */
	public void initToDBByMonth(String begin,String end); 
	/**
	 * 住院收入科室
	 */
	public void initDeptTotalByDayOrHours(String begin, String end);
	/**
	 * 住院收入医生
	 * @param begin
	 * @param end
	 */
	public void initDoctTotalByDayOrHours(String begin,String end);
	/**
	 * 门诊住院统计
	 * @param begin
	 * @param end
	 */
	public void initMZZYTotalByDayOrHours(String begin, String end);
	
	public void initTotalWithDateToAPP(String begin, String end);
	
	/*********************年统计************************************************************************************************/
	/**
	 * 带明细的总收入  年初始化
	 * @param begin
	 * @param end
	 */
	public void initToDBByYear(String begin, String end);
	/**
	 * 住院收入科室 年初始化
	 * @param begin
	 * @param end
	 */
	public void initDeptTotalByDB(String begin, String end);
	/**
	 * 住院收入医生 年统计
	 * @param begin
	 * @param end
	 */
	public void initDoctTotalByDB(String begin,String end);
	/**
	 * MZZY年统计
	 * @param begin
	 * @param end
	 */
	public void initMZZYTotalYear(String begin, String end);
	/**
	 * 不分MZZY
	 * @param begin
	 * @param end
	 */
	public void initTotalYear(String begin, String end);
/*********************mongoDB初始化失败的分区查***********************************************************************************************/
	/**
	 * 门诊住院分区查总收入
	 * @param tnL
	 * @param mainL
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return
	 */
	public List<Dashboard> queryForOracleMZZY(List<String> tnL,List<String> mainL,String begin,String end,String dateSign);

	/**
	 * 医院收入统计 同比比 6年 6月6日 月份两位 dateSign1 年 dateSign2 月 dateSign3日
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return 
	 */
	List<Dashboard> queryTotalCountSame(String begin, String dateSign);
	/**
	 * 医院收入统计 环比 6年 6月6日 月份两位 dateSign1 年 dateSign2 月 dateSign3日
	 * @param begin
	 * @param end
	 * @param dateSign
	 * @return 
	 */
	List<Dashboard> queryTotalCountSque(String begin, String dateSign);
	/**
	 * 总收入同环比
	 * @param tnL
	 * @param maintnl
	 * @param startTime
	 * @param endTime
	 * @param dateSign
	 * @return
	 */
	Dashboard queryTotalForOracle(List<String> tnL,List<String> maintnl,String startTime, String endTime,String dateSign);
}

