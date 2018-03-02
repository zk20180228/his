package cn.honry.statistics.bi.bistac.operationIncome.service;

import java.util.List;

import cn.honry.statistics.bi.bistac.operationIncome.vo.OperationIncomeVo;


public interface OperationIncomeService {

	
	/**  
	 * 
	 * 获得总手术收入
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	void getIncomeSum(String startDate,String endDate);

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
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperationIncome(String startDate,String endDate);

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
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperationIncomeOpType(String startDate,String endDate);

	/**  
	 * 
	 * 初始化手术收入统计（科室前5）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperationDeptTopFive(String startDate,String endDate);
	/**  
	 * 
	 * 初始化手术收入统计（医生前5）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperationDocTopFive(String startDate,String endDate);

	/**  
	 * 
	 * 初始化同环比
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOperYoyRatioToDB(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（门诊+住院）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperationIncomeMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（门诊+住院）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperationIncomeYear(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（手术类别）按月份份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperIncomeTypeMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（手术类别）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperIncomeTypeYear(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（科室前五）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperTopFiveDeptMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（科室前五）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperTopFiveDeptYear(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（医生前五）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperTopFiveDocMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（医生前五）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperTopFiveDocYear(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（同环比）按月份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperIncomYoyRatioMonth(String startDate,String endDate);

	/**
	 * 初始化手术收入统计（同环比）按年份
	 * @author zhuxiaolu 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	void initOperIncomYoyRatioYear(String startDate,String endDate);

	/**  
	 * 
	 * 手术收入统计（门诊住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param response
	 * @param searchTime yyyy-MM-dd月份加为两位日为两位 
	 * @param dateSign 1查询年收入表 2查询月收入 
	 * @throws:
	 * @return: void
	 *
	 */
	List<OperationIncomeVo> queryOperationNums(String searchTime,
			String dateSign);
	
	/**
	 * 查询手术收入统计（手术类别）
	 * @author zhuxiaolu 
	 * @param searchTime 时间
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	List<OperationIncomeVo> queryOperationOpType(String searchTime,
			String dateSign);
	
	/**
	 * 查询手术收入统计（科室TOP5）mongodb
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	List<OperationIncomeVo> queryOperationTOPFiveDept(String searchTime,String dateSign );
	
	/**
	 * 查询手术收入统计（医生TOP5）mongodb
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	List<OperationIncomeVo> queryOperationTOPFiveDoc(String searchTime,String dateSign );

	/**
	 * 查询手术收入统计（同比）mongodb
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	List<OperationIncomeVo> queryYoyCount(String monthStart, String dateSign);

	/**
	 * 查询手术收入统计（环比）
	 * @param searchTime 查询时间 :如：2017、2017-03、2017-05-11
	 * @param dateSign 区分时间标记  1：年  2： 月  3：日
	 * @author zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @return  List<OperationIncomeVo>
	 */
	List<OperationIncomeVo> queryRatioCount(String monthStart, String dateSign);
	 /**  
	 * 
	 * 预处理
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:begin 开始时间
	 * @param:end 结束时间
	 * @param:type 类型
	 * @throws:
	 * @return: void
	 *
	 */
		 
	public void initDate(String beginDate,String endDate,Integer type);
}
