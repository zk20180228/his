package cn.honry.statistics.bi.bistac.listTotalIncomeStatic.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;

public interface ListTotalIncomeStaticService {
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
	public ListTotalIncomeStaticVo queryVo(String date);
	/**  
	 * 
	 * 总收入情况统计（年）
	 * @Author: donghe
	 * @CreateDate: 2017年5月9日 下午4:09:43 
	 * @Modifier: donghe
	 * @ModifyDate: 2017年5月9日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public ListTotalIncomeStaticVo queryVoYear(String startTime,String endTime);
	/**  
	 * 
	 * 获取所有的统计大类名称和code
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
	 * 获取所有的院区名称和code
	 * 
	 */
	public List<BusinessDictionary> queryAreaName();
	/**  
	 * 
	 * 总收入情况统计
	 * @author conglin
	 * @CreateDate: 
	 * @Modifier: 
	 * @ModifyDate: 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	public List<ListTotalIncomeStaticVo> queryTotalCount(String startTime,String endTime,String dateSign);
	
	/**  
	 * 
	 * 总收入情况统计 elasticsearch
	 * @throws Exception 
	 *
	 */
	Map<String, Object> queryTotalCountByES(String date, String dateSign) throws Exception;
	/**
	 * @see 初始化收入统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYear();
	/**
	 * @see 初始化收入统计日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYearOneDay();
	/**
	 * @see 初始化收入统计移动端
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongTotalWithDept();
	/**
	 * @see 初始化收入统计移动端
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongTotalWithDeptTotal();
	/**
	 * @see 初始化收入统计日统计日统计
	 * @param tnL
	 * @param maintnl
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean initTotalMongYearOneDayWithDept();
	/**
	 * @see 初始化住院医生科室收入
	 */
	public void initTotalDeptDoctor();
/*******************************************************************************************************************/
	/**
	 * @see  天更新 总收入 带明细
	 * @param beginTime
	 * @param endTime
	 * @param hours 按几个小时更新 为空默认为天
	 */
	public void initTotalForOracle(String beginTime,String endTime);
	
	/**
	 * 住院收入科室 统计 天更新
	 */
	void initDeptTotalByDayOrHours(String beginTime,String endTime);
	/**
	 * 住院收入医生 统计 天更新
	 */
	void initDoctTotalByDayOrHours(String beginTime,String endTi);
	/**
	 * 总收入情况同环比数据
	 * @param tnL
	 * @param mainL
	 * @param begin
	 * @param end
	 * @param hours
	 */
	void initTotalWithDateToAPP(String beginTime,String endTi);
	/**
	 * 门诊住院收入
	 * @param beginTime
	 * @param endTi
	 */
	public void initMZZYTotalByDayOrHours(String beginTime,String endTi);
/*****************************月***********************************************************************************/
	public void initMonth(String begin,String end);
/*****************************年*****************************************************************************************/
	public void initYear(String begin,String end);
	/*********************总收入明细******************************************************************************************************/
	/**
	 * 
	 * 总收入环图
	 * @param begin
	 * @param dateSign
	 * @return
	 */
	List<Dashboard> queryTotalCountMZZY(String begin, String dateSign);
	/**
	 * 总收入 柱状图 同比
	 * @param begin
	 * @param dateSign
	 * @return
	 */
	List<Dashboard> queryTotalCount(String begin, String dateSign);
	/**
	 * 总收入 柱状图 环比
	 * @param begin
	 * @param dateSign
	 * @return
	 */
	List<Dashboard> queryTotalCountHB(String begin, String dateSign);
	/**
	 * 总收入情况统计--从mongodb中获取数据
	 * @param date 日期
	 * @param dateSign 日期类型
	 * @return
	 */
    String queryTotalCountByMongo(String date,String dateSign);
    
}
