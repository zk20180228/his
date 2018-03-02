package cn.honry.statistics.bi.bistac.operationNum.dao;

import java.text.ParseException;
import java.util.List;

import cn.honry.statistics.bi.bistac.deptAndFeeData.vo.StatisticsVo;
import cn.honry.statistics.bi.bistac.operationNum.vo.OperationNumsVo;

public interface OperationNumsDao {

	/**
	 *  初始化手术例数（门诊、住院、介入）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperationNumsToDB(String startDate,String endDate);
	
	/**
	 *  初始化手术例数（科室TOP5）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperationNumsTopDeptToDB(String startDate,String endDate);
	
	/**
	 *  初始化手术例数（医生TOP5）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperationNumsTopDocToDB(String startDate,String endDate);
	
	/**
	 *  初始化手术例数（同环比）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsYoyRatioToDB(String startDate,String endDate);

	/**
	 *  初始化手术例数（门诊、住院、介入）按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperationNumsMonthToDB(String startDate, String endDate);
	/**
	 *  初始化手术例数（门诊、住院、介入）按年
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperationNumsYearToDB(String startDate, String endDate);
	
	/**
	 *  初始化手术例数科室前五按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsTopDeptMonthToDB(String startDate, String endDate);
	
	/**
	 *  初始化手术例数科室前五年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsTopDeptYearToDB(String startDate, String endDate);

	/**
	 *  初始化手术例数医生前五按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsTopDocMonthToDB(String startDate, String endDate);

	/**
	 *  初始化手术例数医生前五按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsTopDocYearToDB(String startDate, String endDate);
	/**
	 * 初始化手术例数同环比按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsYoyRatioMonthToDB(String startDate, String endDate);

	/**
	 *初始化手术例数同环比按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsYoyRatioYearToDB(String startDate, String endDate);
	
	/**
	 *初始化手术例数同环比按天
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsOpTypeToDB(String startDate, String endDate);
	
	/**
	 *初始化手术例数同环比按月份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsOpTypeMonthToDB(String startDate, String endDate);

	/**
	 *初始化手术例数同环比按年份
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	void saveOperNumsOpTypeYearToDB(String startDate, String endDate);
	
	 /**
	 *查询在做或已完成（当天）
	 *  @author zhuxiaolu 
	 *  @param endDate 结束时间
	 *  @param startDate 开始时间
	 *  @CreateDate: 2017-7-3 下午15:30:31
	 *  @return: void
	 */
	List<OperationNumsVo> getDoingOrFinish(String startDate,String endDate);
	
	/**
	 * 手术例数统计（住院门诊）mongodb
	 *  @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryTotalCount(String searchTime,String dateSign );
	
	/**
	 * 手术例数统计（科室top5）mongodb
	 *  @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryOperationNumsTopDept(String searchTime,String dateSign );
	
	/**
	 * 手术例数统计（医生top5）mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryOperationNumsTopDoc(String searchTime,String dateSign );
	
	/**
	 * 手术例数统计（手术类别）mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryNumsOpType(String searchTime, String dateSign);

	/**
	 * 手术例数统计（手术类别）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param tnL 手术表分区
	 * @param startDate 开始时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryNumsOpTypeToDB(List<String> tnL,
			String startDate, String endDate, String dateSign);

	/**
	 * 手术例数统计（医生前五）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @param tnL 手术表分区
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryNumsTopDocToDB(List<String> tnL,
			String startDate, String endDate, String dateSign);

	/**
	 * 手术例数统计（科室前五）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @param tnL 手术表分区
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryNumsTopDeptToDB(List<String> tnL,
			String startDate, String endDate, String dateSign);

	/**
	 * 手术例数统计（门诊住院）数据库
	 * @author zhuxiaolu 
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @param tnL 手术表分区
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryTotalCountToDB(List<String> tnL,
			String startDate, String endDate, String dateSign);

	/**
	 * 查询已完成手术例数（昨天）
	 * @author zhuxiaolu 
	 * @param endDate 结束时间
	 * @param startDate 开始时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> getYesterDayFinish(String startDate, String endDate);
	

	/**
	 * 手术例数统计环比mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryRatioCount(String searchTime, String dateSign);

	/**
	 * 手术例数统计同比mongodb
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryYoyCount(String searchTime, String dateSign);

	/**
	 * 手术例数统计同比数据库
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	OperationNumsVo queryYoyCountToDB(String begin, String endTime,
			String dateSign);
	/**
	 * 手术例数统计(普通或门诊)数据库
	 * @author zhuxiaolu 
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return: List<OperationNumsVo>
	 */
	List<OperationNumsVo> queryJzOrPtCountToDB(List<String> tnL,
			String startDate, String endDate, String dateSign);
	
	/**
	 * 查询手术例数
	 * @param startDate
	 * @param timeType 查询时间格式
	 * 			1：YYYY-dd-mm 按天
	 * 			2：YYYY-dd	    按月
	 * 			3：YYYY		    按年
	 * @return
	 */
	List<OperationNumsVo> query(String startDate,Integer timeType);
	
	/**
	 * 
	 * 
	 * <p>查询手术例数从mongodb手术例数科室表中</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月24日 下午3:24:35 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月24日 下午3:24:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate
	 * @param timeType
	 *          1：YYYY-dd-mm 按天
	 * 			2：YYYY-dd	    按月
	 * 			3：YYYY		    按年
	 * @return:
	 *
	 */
	List<OperationNumsVo> queryForMongoDB(String startDate,Integer timeType);
	
	/**
	 * 环比
	 * @param startDate
	 * @param timeType  
	 * @return
	 */
	List<StatisticsVo> queryMom(String startDate,Integer timeType);
	/**
	 * 同比
	 * @param startDate
	 * @param timeType  
	 * @return
	 */
	List<StatisticsVo> queryYoy(String startDate, String endDate);
	
	/**
	 * 
	 * 
	 * <p>获取同环比数据</p>
	 * @Author: XCL
	 * @CreateDate: 2018年1月24日 下午6:41:17 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年1月24日 下午6:41:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param mongoCollection mongo表名
	 * @param date 查询日期
	 * @return:
	 * @throws ParseException 
	 *
	 */
	List<StatisticsVo> queryMoy(String mongoCollection, String begin,String end,Integer timeType) throws ParseException;
}
