package cn.honry.inner.statistics.operationNum.dao;

import java.util.Date;
import java.util.List;

public interface InnerOperationNumDao {
	/**  
	 * 
	 * 获取手术登记表的区表
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param beginTime  开始时间
	 * @param endTime 结束时间
	 * @throws:
	 * @return: void
	 *
	 */
	public List<String> getOperationRecordTnl(String beginTime,String endTime);
	/**  
	 * 
	 * 保存日志
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param date 开始时间
	 * @param menuAlias 模块名
	 * @param list 大小
	 * @param queryDate 在线更新时间
	 * @throws:
	 * @return: void
	 *
	 */
	public void saveMongoLog(Date date,String menuAlias,List list,String queryDate);
	

	/**  
	 * 
	 * 手术例数（门诊、住院、介入）
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
	public void initOperNumsMZJ(String menuAlias,String type,String date);
	/**  
	 * 
	 * 手术例数（门诊、住院、介入） 年月
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
	public void initOperNumsMZJYearOrMonth(String menuAlias, String type, String date);
	
	/**  
	 * 
	 * 手术例数（手术类别）
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
	public void initOperNumsType(String menuAlias, String type, String date);
	
	
	
	/**  
	 * 
	 * 手术例数（手术类别）年月
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
	public void initOperNumsTypeYearOrMonth(String menuAlias, String type, String date);
	
	/**  
	 * 
	 * 手术例数（科室）
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
	public void initOperNumsDept(String menuAlias, String type, String date);
	
	/**  
	 * 
	 * 手术例数（科室）年月
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
	public void initOperNumsDeptYearOrMonth(String menuAlias, String type, String date);
	
	/**  
	 * 
	 * 手术例数（医生）
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
	public void initOperNumsDoc(String menuAlias, String type, String date);
	
	/**  
	 * 
	 * 手术例数（医生）年月
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
	public void initOperNumsDocYearOrMonth(String menuAlias, String type, String date);
	
	/**  
	 * 
	 * 手术例数（同环比）
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
	public void initOperNumsYR(String menuAlias, String type, String date);
	
	/**  
	 * 
	 * 手术例数（同环比）年月
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
	public void initOperNumsYRYearOrMonth(String menuAlias, String type, String date);
	
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
	
	/**
	 * 
	 * 
	 * <p>pc端手术科室、手术医生工作量统计 预处理</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月2日 下午6:58:05 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月2日 下午6:58:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void initPcOperationNumbers(String menuAlias, String type, String date);
	
	/**
	 * 
	 * 
	 * <p>pc端手术科室、手术医生工作量统计 预处理</p>
	 * @Author: XCL
	 * @CreateDate: 2018年2月2日 下午6:58:05 
	 * @Modifier: XCL
	 * @ModifyDate: 2018年2月2日 下午6:58:05 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuAlias
	 * @param type
	 * @param date:
	 *
	 */
	public void initPcOperationNumbersMonthAndYear(String menuAlias, String type, String date);
	

}
