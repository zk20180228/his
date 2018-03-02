package cn.honry.inner.outpatient.outpatientDataMigration.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.MoveDataLog;


/** 门诊迁移Service
* @ClassName: OutpatientDataMigrationDAO 
* @Description: 门诊迁移dao 
* @author dtl
* @date 2016年12月3日
*  
*/
public interface OutpatientDataMigrationService{
	/** 得到超过挂号有效期的挂号主表中所有门诊号（若无记录条数分页信息则返回null）
	* @Title: queryClincCodeNows 
	* @Description: 得到超过挂号有效期的挂号主表中门诊号（若无记录条数分页信息则返回null）
	* @param date 迁移数据所在时间
	* @author dtl 
	* @date 2016年12月2日
	*/
	Map<String, Object> queryClincCodeNows(String date);

	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的发票号（若无记录条数分页信息则返回null）
	* @Title: pageInvoiceNoNows 
	* @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的发票号（若无记录条数分页信息则返回null）
	* * @param date 迁移数据所在时间
	* @author dtl 
	* @date 2016年12月2日
	*/
	List<String> queryInvoiceNoNows(String date);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的处方号（若无记录条数分页信息则返回null）
	* @Title: pageRecipeNos 
	* @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的处方号（若无记录条数分页信息则返回null）
	* * @param date 迁移数据所在时间
	* @author dtl 
	* @date 2016年12月2日
	*/
	List<String> queryRecipeNoNows(String date);
	
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

	/** 迁移符合条件的药品出库记录（门诊号--》处方号&&OP_TYPE1 门诊发药3 门诊退药）
	* @Title: moveOutStoreNows 
	* @Description: 迁移符合条件的药品出库记录（门诊号--》处方号&&OP_TYPE 1 门诊发药3 门诊退药）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveOutStoreNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的药品出库申请记录（门诊号--》处方号&&OP_TYPE1 门诊发药3 门诊退药）
	* @Title: moveApplyOutNows 
	* @Description: 迁移符合条件的药品出库申请记录（门诊号--》处方号&&OP_TYPE1 门诊发药3 门诊退药）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveApplyOutNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊病区退费记录（门诊号--》发票号&&申请归属标识 1门诊）
	* @Title: moveInpatientCancelitemNows 
	* @Description: 迁移符合条件的门诊病区退费记录（门诊号--》发票号&&申请归属标识 1门诊）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveInpatientCancelitemNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊发票明细记录（门诊号--》发票号）
	* @Title: moveFinanceInvoicedetailNows 
	* @Description: 迁移符合条件的门诊发票明细记录（门诊号--》发票号）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveFinanceInvoicedetailNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊支付记录（门诊号--》发票号）
	* @Title: moveBusinessPayModeNows 
	* @Description: 迁移符合条件的门诊支付记录（门诊号--》发票号）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveBusinessPayModeNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊结算信息记录（门诊号）
	* @Title: moveFinanceInvoiceInfoNows 
	* @Description: 迁移符合条件的门诊结算信息记录（门诊号）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveFinanceInvoiceInfoNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊处方调剂头信息记录（门诊号）
	* @Title: moveStoRecipeNows 
	* @Description: 迁移符合条件的门诊处方调剂头信息记录（门诊号）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveStoRecipeNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊处方明细信息记录（门诊号）
	* @Title: moveOutpatientFeedetailNows 
	* @Description: 迁移符合条件的门诊处方明细信息记录（门诊号）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveOutpatientFeedetailNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊处方信息记录（门诊号）
	* @Title: moveOutpatientRecipedetailNows 
	* @Description: 迁移符合条件的门诊处方信息记录（门诊号）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveOutpatientRecipedetailNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊挂号信息记录（挂号有效期--》门诊号）
	* @Title: moveRegistrationNows 
	* @Description: 迁移符合条件的门诊挂号信息记录（挂号有效期--》门诊号）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveRegistrationNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊预约挂号信息记录（预约时间早于今天）
	* @Title: moveRegisterPreregisterNows 
	* @Description: 迁移符合条件的门诊预约挂号信息记录（预约时间早于今天）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveRegisterPreregisterNows(Map<String, Object> map) throws Exception;
	
	/** 迁移符合条件的门诊挂号排班信息记录（排班时间早于今天）
	* @Title: moveRegisterScheduleNows 
	* @Description: 迁移符合条件的门诊挂号排班信息记录（排班时间早于今天）
	* @param page  页数
	* @param row  每页条数
	* @author dtl 
	* @date 2016年12月5日
	*/
	void moveRegisterScheduleNows(Map<String, Object> map) throws Exception;
	
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的药品出库记录条数分页信息（若无记录条数分页信息则返回null）
	* @Title: pageOutStoreNows 
	* @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的药品出库记录条数分页信息（若无记录条数分页信息则返回null）
	* @param date 迁移数据所在时间
	* @param state 迁移步骤 1：对now表操作 2：对mid表操作
	* @author dtl 
	* @date 2016年12月3日
	*/
	Map<String, Object> pageOutStoreNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的药品出库申请记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageApplyOutNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的药品出库申请记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageApplyOutNows(String date, String state);

	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊病区退费记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageInpatientCancelitemNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊病区退费记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageInpatientCancelitemNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊发票明细记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageFinanceInvoicedetailNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊发票明细记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageFinanceInvoicedetailNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊支付记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageBusinessPayModeNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊支付记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageBusinessPayModeNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊结算信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageFinanceInvoiceInfoNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊结算信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageFinanceInvoiceInfoNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊处方调剂头信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageStoRecipeNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊处方调剂头信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageStoRecipeNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊处方明细信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageOutpatientFeedetailNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊处方明细信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageOutpatientFeedetailNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊处方信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageOutpatientRecipedetailNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊处方信息记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageOutpatientRecipedetailNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊挂号记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageRegistrationNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊挂号记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageRegistrationNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊预约挂号记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageRegisterPreregisterNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊预约挂号记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageRegisterPreregisterNows(String date, String state);
	
	/** 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊挂号排班记录条数分页信息（若无记录条数分页信息则返回null）
	 * @Title: pageRegisterScheduleNows 
	 * @Description: 得到超过挂号有效期的挂号记录条数分页信息产生的所有的门诊挂号排班记录条数分页信息（若无记录条数分页信息则返回null）
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @author dtl 
	 * @date 2016年12月3日
	 */
	Map<String, Object> pageRegisterScheduleNows(String date, String state);
	
	/** 查询失败的迁移记录(单表单天查询)
	* @Title: queryMoveDataLog 
	* @Description: 查询迁移记录(单表单天查询)
	* @param optType 1-迁移N2M，2-删除N2M，3迁移M2H，4删除M2H
	* @param dateType 1-门诊，2-住院
	* @param tableName 表名
	* @param dataDate 数据所在日期
	* @author dtl 
	* @date 2016年12月9日
	*/
	MoveDataLog queryErrorMoveDataLog(Integer optType, Integer dateType, String tableName, String dataDate);
	
	/** 查询成功的迁移记录(单表单天查询)
	 * @Title: querySuccessMoveDataLog 
	 * @Description: 查询迁移记录(单表单天查询)
	 * @param optType 1-迁移N2M，2-删除N2M，3迁移M2H，4删除M2H
	 * @param dateType 1-门诊，2-住院
	 * @param tableName 表名
	 * @param dataDate 数据所在日期
	 * @author dtl 
	 * @date 2016年12月9日
	 */
	MoveDataLog querySuccessMoveDataLog(Integer optType, Integer dateType, String tableName, String dataDate);

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
	* @param optType 1-迁移N2M，2-删除N2M，3迁移M2H，4删除M2H
	* @param dateType 1-门诊，2-住院
	* @author dtl 
	* @date 2016年12月29日
	*/
	List<MoveDataLog> queryMoveDataLogs(Integer optType, Integer dateType);


}
