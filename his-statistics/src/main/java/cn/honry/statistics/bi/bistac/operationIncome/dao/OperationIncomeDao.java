package cn.honry.statistics.bi.bistac.operationIncome.dao;

import java.util.List;

import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.bi.bistac.operationIncome.vo.OperationIncomeVo;

public interface OperationIncomeDao  extends EntityDao<OperationIncomeVo>{


	/**  
	 * 
	 * 初始化手术收入统计（门诊+住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOperationIncomeToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg);

	/**  
	 * 
	 * 初始化手术收入统计（手术类别）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOperationOpTypeToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg);

	/**  
	 * 
	 * 初始化手术类别
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOperOpTypeToDB();
	
	 /**  
	 * 
	 * 初始化科室前五
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOperTopFiveDeptToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg);
	
	 /**  
	 * 
	 * 初始化医生前五
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOperTopFiveDocToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg);
	
	 /**  
	 * 
	 * 初始化同环比
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOperYoyRatioToDB(String startDate,String endDate, List<String> itemtnL, List<String> feetnL,int flg);
	
	
	/**
	 * 初始化手术收入统计（门诊+住院）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperationIncomeMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（门诊+住院）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperationIncomeYear(String startDate,String endDate);
	
	/**
	 * 初始化手术收入统计（手术类别）按月份份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperIncomeTypeMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（手术类别）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperIncomeTypeYear(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（科室前五）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperTopFiveDeptMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（科室前五）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperTopFiveDeptYear(String startDate,String endDate);
	
	/**
	 * 初始化手术收入统计（医生前五）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperTopFiveDocMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（医生前五）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperTopFiveDocYear(String startDate,String endDate);
	
	/**
	 * 初始化手术收入统计（同环比）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperIncomYoyRatioMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（同环比）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @CreateDate: 2017-7-3 下午15:30:31
	 */
	void initOperIncomYoyRatioYear(String startDate,String endDate);

	/**  
	 * 
	 * 手术收入统计（住院门诊）mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryOperationNums(String searchTime,
			String dateSign);
	
	/**  
	 * 
	 * 手术收入统计（手术类别）mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryOperationOpType(String searchTime,
			String dateSign);

	/**  
	 * 
	 * 手术收入统计（手术类别）从数据库
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryOperationOpTypeToDB(List<String> itemList, List<String> feeList, String startDate,String endDate, String dateSign);

	 /**  
	 * 
	 * 查询手术收入统计（门诊住院）从数据库
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryOperationNumsToDB(List<String> itemList,
			List<String> feeList, String startDate, String endDate,
			String dateSign);
	
	
	/**  
	 * 
	 * 查询科室前五从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryOperationTOPFiveDept(String searchTime,String dateSign );
	
	/**  
	 * 
	 * 查询医生前五从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryOperationTOPFiveDoc(String searchTime,String dateSign );

	 /**  
	 * 
	 * 数据库查询科室前五
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryTOPFiveDeptToDB(List<String> itemList,
			List<String> feeList, String startDate, String endDate,
			String dateSign);

	 /**  
	 * 
	 * 数据库查询医生前五
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryTOPFiveDocDB(List<String> itemList,
			List<String> feeList, String startDate, String endDate,
			String dateSign);
	
	/**  
	 * 
	 * 查询环环比从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryRatioCount(String searchTime, String dateSign);

	 /**  
	 * 
	 * 查询同环比从mongodb
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	List<OperationIncomeVo> queryYoyCount(String searchTime, String dateSign);

	/**  
	 * 
	 * 数据库查询同环比
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:
	 * @param startDate开始时间
	 * @param endDate 结束时间
	 * @param dateSign 区分标记 年、月、日
	 * @param itemtnL 非药品明细分区
	 * @param feetnL 费用明细分区
	 * @param flg  区分按小时还是按天更新  
	 * @version: V1.0
	 * @throws:
	 * @return: OperationIncomeVo
	 *
	 */
	OperationIncomeVo queryYoyCountToDB(List<String> itemList,
			List<String> feeList, String startDate, String endDate, String dateSign);
}
