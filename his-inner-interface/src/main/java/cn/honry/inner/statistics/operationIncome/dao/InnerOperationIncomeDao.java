package cn.honry.inner.statistics.operationIncome.dao;

import java.util.List;


public interface InnerOperationIncomeDao {

	/**  
	 * 
	 * 初始化手术收入统计（门诊+住院）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeMZ(String menuAlias,String type,String date);

	/**  
	 * 
	 * 在线更新返回分区表(住院非药品明细表或门诊费用明细）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginTime  开始时间
	 * @param endTime 结束时间
	 * @param tables String[]表名
	 * @param type MZ ZY 门诊表或住院表
	 * @throws:
	 * @return: void
	 *
	 */
	List<String> getTableTnl(String begin, String end, String[] tables,String flg);
	
	/**  
	 * 
	 * 初始化手术收入统计（门诊+住院）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeMZYearOrMonth(String menuAlias,String type,String date);
	
	/**  
	 * 
	 * 初始化手术收入统计（手术类别）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeType(String menuAlias,String type,String date);
	
	/**  
	 * 
	 * 初始化手术收入统计（手术类别）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeTypeYearOrMonth(String menuAlias,String type,String date);
	
	/**  
	 * 
	 * 初始化手术收入统计（科室）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeDept(String menuAlias,String type,String date);
	
	/**  
	 * 
	 * 初始化手术收入统计（科室）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeDeptYearOrMonth(String menuAlias,String type,String date);
	
	/**  
	 * 
	 * 初始化手术收入统计（医生）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeDoc(String menuAlias,String type,String date);
	
	/**  
	 * 
	 * 初始化手术收入统计（医生）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeDocYearOrMonth(String menuAlias,String type,String date);
	/**  
	 * 
	 * 初始化手术收入统计（同环比）
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeYoyRatio(String menuAlias,String type,String date);
	
	/**  
	 * 
	 * 初始化手术收入统计（同环比）年月
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias 栏目别名
	 * @param type 类型
	 * @param date 时间
	 * @throws:
	 * @return: void
	 *
	 */
	void initOperIncomeYoyRatioYearOrMonth(String menuAlias,String type,String date);
	
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
	void initOperOpTypeToDB();
}
