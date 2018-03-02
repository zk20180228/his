package cn.honry.inner.outpatient.outpatientDataMigration.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.MoveDataLog;


/** 门诊迁移dao 
* @ClassName: OutpatientDataMigrationDAO 
* @Description: 门诊迁移dao 
* @author dtl
* @date 2016年12月3日
*  
*/
public interface OutpatientDataMigrationDAO{

	/** 迁移数据所在时间为null时查表中需要迁移的最大最小时间，有值时查当天所有门诊号
	* @Title: queryClincCodeNows 
	* @Description: 迁移数据所在时间为null时查表中需要迁移的最大最小时间，有值时查当天所有门诊号
	* @param date 迁移数据所在时间(为null时查表中需要迁移的最大最小时间，有值时查当天所有门诊号)
	* @author dtl
	* @date 2016年12月2日
	*/
	Map<String, Object> queryClincCodeNows(String date);

	/** 迁移数据所在时间为null时查表中需要迁移的最大最小时间，有值时查当天所有发票号
	* @Title: queryInvoiceNoNows 
	* @Description: 迁移数据所在时间为null时查表中需要迁移的最大最小时间，有值时查当天所有发票号
	* @param date 迁移数据所在时间(为null时查表中需要迁移的最大最小时间，有值时查当天所有发票号)
	* @author dtl 
	* @date 2016年12月2日
	*/
	List<String> queryInvoiceNoNows(String date);
	
	/** 迁移数据所在时间为null时查表中需要迁移的最大最小时间，有值时查当天所有处方号
	* @Title: queryRecipeNoNows 
	* @Description: 迁移数据所在时间为null时查表中需要迁移的最大最小时间，有值时查当天所有处方号
	* @param date 迁移数据所在时间(为null时查表中需要迁移的最大最小时间，有值时查当天所有处方号)
	* @author dtl
	* @date 2016年12月2日
	*/
	List<String> queryRecipeNoNows(String date);
	
	/** 查询药品出库
	* @Title: queryOutStoreNows 
	* @Description: 查询药品出库
	* @param date 迁移数据所在时间
	* @param state 迁移步骤 1：对now表操作 2：对mid表操作
	* @author dtl 
	* @date 2016年12月3日
	*/
	long queryOutStoreNows(String date,String state);
	
	/** 查询药品出库申请
	 * @Title: queryApplyOutNows 
	 * @Description: 查询药品出库申请
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	long queryApplyOutNows(String date,String state);

	/** 查询门诊病区退费记录
	 * @Title: queryInpatientCancelitemNows 
	 * @Description: 查询门诊病区退费记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	long queryInpatientCancelitemNows(String date,String state);
	
	/** 查询门诊发票明细记录
	 * @Title: queryFinanceInvoicedetailNows 
	 * @Description: 查询门诊发票明细记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @date 2016年12月3日
	 */
	long queryFinanceInvoicedetailNows(String date,String state);
	
	/** 查询门诊支付记录
	 * @Title: queryBusinessPayModeNows 
	 * @Description: 查询门诊支付记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @date 2016年12月3日
	 */
	long queryBusinessPayModeNows(String date,String state);
	
	/** 查询门诊结算信息
	 * @Title: queryFinanceInvoiceInfoNows 
	 * @Description:  查询门诊支付记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @date 2016年12月3日
	 */
	long queryFinanceInvoiceInfoNows(String date,String state);
	
	/** 查询门诊处方调剂头信息记录
	 * @Title: queryStoRecipeNows 
	 * @Description: 查询门诊处方调剂头信息记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @date 2016年12月3日
	 */
	long queryStoRecipeNows(String date,String state);
	
	/** 查询门诊处方明细信息记录
	 * @Title: queryOutpatientFeedetailNows 
	 * @Description: 查询门诊处方明细信息记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @date 2016年12月3日
	 */
	long queryOutpatientFeedetailNows(String date,String state);
	
	/** 查询门诊处方信息记录
	 * @Title: queryOutpatientRecipedetailNows 
	 * @Description: 查询门诊处方信息记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @date 2016年12月3日
	 */
	long queryOutpatientRecipedetailNows(String date,String state);
	
	/** 查询门诊挂号记录
	 * @Title: queryRegistrationNows 
	 * @Description: 查询门诊挂号记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @date 2016年12月3日
	 */
	long queryRegistrationNows(String date,String state);
	
	/** 查询门诊预约挂号记录
	 * @Title: queryRegisterPreregisterNows 
	 * @Description: 查询门诊预约挂号记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @param date 
	 * @date 2016年12月3日
	 */
	Map<String, Object> queryRegisterPreregisterNows(String date,String state);
	
	/** 查询门诊挂号排班记录
	 * @Title: queryRegisterScheduleNows 
	 * @Description: 查询门诊挂号排班记录
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl
	 * @param date 
	 * @date 2016年12月3日
	 */
	Map<String, Object> queryRegisterScheduleNows(String date,String state);

	/** 迁移符合条件的药品出库记录（门诊号--》处方号&&OP_TYPE 门诊发药3 门诊退药）
	* @Title: moveOutStoreNows 
	* @Description: 迁移符合条件的药品出库记录（门诊号--》处方号&&OP_TYPE 门诊发药3 门诊退药）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveOutStoreNows(Map<String, Object> map);
	
	/** 迁移符合条件的药品出库申请记录（门诊号--》处方号&&OP_TYPE1 门诊发药3 门诊退药）
	* @Title: moveApplyOutNows 
	* @Description: 迁移符合条件的药品出库申请记录（门诊号--》处方号&&OP_TYPE1 门诊发药3 门诊退药）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveApplyOutNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊病区退费记录（门诊号--》发票号&&申请归属标识 1门诊）
	* @Title: moveInpatientCancelitemNows 
	* @Description: 迁移符合条件的门诊病区退费记录（门诊号--》发票号&&申请归属标识 1门诊）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveInpatientCancelitemNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊发票明细记录（门诊号--》发票号）
	* @Title: moveFinanceInvoicedetailNows 
	* @Description: 迁移符合条件的门诊发票明细记录（门诊号--》发票号）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveFinanceInvoicedetailNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊支付记录（门诊号--》发票号）
	* @Title: moveBusinessPayModeNows 
	* @Description: 迁移符合条件的门诊支付记录（门诊号--》发票号）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveBusinessPayModeNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊结算信息记录（门诊号）
	* @Title: moveFinanceInvoiceInfoNows 
	* @Description: 迁移符合条件的门诊结算信息记录（门诊号）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveFinanceInvoiceInfoNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊处方调剂头信息记录（门诊号）
	* @Title: moveStoRecipeNows 
	* @Description: 迁移符合条件的门诊处方调剂头信息记录（门诊号）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveStoRecipeNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊处方明细信息记录（门诊号）
	* @Title: moveOutpatientFeedetailNows 
	* @Description: 迁移符合条件的门诊处方明细信息记录（门诊号）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveOutpatientFeedetailNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊处方信息记录（门诊号）
	* @Title: moveOutpatientRecipedetailNows 
	* @Description: 迁移符合条件的门诊处方信息记录（门诊号）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveOutpatientRecipedetailNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊挂号信息记录（挂号有效期--》门诊号）
	* @Title: moveRegistrationNows 
	* @Description: 迁移符合条件的门诊挂号信息记录（挂号有效期--》门诊号）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveRegistrationNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊预约挂号信息记录（预约时间早于今天）
	* @Title: moveRegisterPreregisterNows 
	* @Description: 迁移符合条件的门诊预约挂号信息记录（预约时间早于今天）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveRegisterPreregisterNows(Map<String, Object> map);
	
	/** 迁移符合条件的门诊挂号排班信息记录（排班时间早于今天）
	* @Title: moveRegisterScheduleNows 
	* @Description: 迁移符合条件的门诊挂号排班信息记录（排班时间早于今天）
	* @param map page：当前页；row：每页行数；state 迁移步骤 1：对now表操作 2：对mid表操作； date：数据所在时间，当state为2时date为空；flag：1迁移，2删除
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveRegisterScheduleNows(Map<String, Object> map);

	/** 查询失败的迁移记录(单表单天查询)
	* @Title: queryMoveDataLog 
	* @Description: 查询迁移记录(单表单天查询)
	* @param optType 1-迁移，2-删除
	* @param dateType 1-门诊，2-住院
	* @param tableName 表名
	* @param dataDate 数据所在日期
	* @author dtl 
	* @date 2016年12月9日
	*/
	MoveDataLog queryErrorMoveDataLog(Integer optType, Integer dateType, String tableName, String dataDate);
	
	/** 日志保存
	* @Title: saveOrUpdate 日志保存
	* @Description: 日志保存
	* @param moveDataLog 日志实体
	* @author dtl 
	* @date 2016年12月14日
	*/
	void saveOrUpdate(MoveDataLog moveDataLog);
	
	/** 查询失败的迁移记录（可查多表的迁移记录，查询不到返回null）
	* @Title: queryMoveDataLogs 
	* @Description: 查询失败的迁移记录（可查多表的迁移记录，查询不到返回null）
	* @param optType 1-迁移，2-删除
	* @param dateType 1-门诊，2-住院
	* @author dtl 
	* @date 2016年12月29日
	*/
	List<MoveDataLog> queryMoveDataLogs(Integer optType, Integer dateType);
	/** 查询成功的迁移记录(单表单天查询)
	 * @Title: querySuccessMoveDataLog 
	 * @Description: 查询迁移记录(单表单天查询)
	 * @param optType 1-迁移，2-删除
	 * @param dateType 1-门诊，2-住院
	 * @param tableName 表名
	 * @param dataDate 数据所在日期
	 * @author dtl 
	 * @date 2016年12月9日
	 */
	MoveDataLog querySuccessMoveDataLog(Integer optType, Integer dateType,
			String tableName, String dataDate);
}
