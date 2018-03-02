package cn.honry.inner.inpatient.inpatient.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.MoveDataLog;

/**
 * 
 * @author hzr
 *	住院数据迁移接口
 *
 */
public interface InpatientDao {

	/**
	 *	根据出院状态、出院天数求出出院患者人数
	 * @param date 迁移数据的日期
	 * @return 迁移患者数据
	 * @author hzr
	 * 2016/12/3
	 */
	public Map<String, Object> getInpatentNos(String date);
	/**
	 * 根据住院流水号，获取出库申请表中的处方号
	 * @param date 	日期
	 * @return 出院患者处方号
	 *  @author hzr
	 * 2016/12/3
	 */
	List<String> getRecipes(String date);
	/**
	 * 根据住院流水号，获取住院结算头细表中的发票号
	 * @param date 	日期
	 * @return 出院患者发票号
	 *  @author hzr
	 * 2016/12/3
	 */
	List<String> getInvoices(String date);
	
	/**
	 * 根据日期
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return住院主表患者条数
	 */
	long countInpatientInfoNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 血液申请患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientApplyNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 转科申请患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientShiftapplyNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 医嘱表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientOrderNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 药嘱执行档表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientExecdrugNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 非药嘱执行档表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientExecundrugNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 费用汇总表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientFeeinfoNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 预交金表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientInprepayNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院结算头表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientBalanceheadNow(String date, String state);
	/**
	 * 根据发票号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院结算实付表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientBalancepayNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 病区退费申请表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientCancelitemNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院非药品明细表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientItemlistNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院药品明细表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientMedicinelistNow(String date, String state);
	/**
	 * 根据处方号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 出库申请表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countDrugApplyoutNow(String date, String state);
	/**
	 * 根据处方号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 出库记录表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countDrugOutstoreNow(String date, String state);
	/**
	 * 根据住院流水号
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院结算明细表患者条数
	 *  @author hzr
	 * 2016/12/3
	 */
	long countInpatientBalancelistNow(String date, String state);
	/**
	 * 迁移住院明细表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientBalancelistNow(Map<String,Object> map);
	/**
	 * 迁移出库记录表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveDrugOutstoreNow(Map<String,Object> map);
	/**
	 * 迁移出库申请表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveDrugApplyoutNow(Map<String,Object> map);
	/**
	 * 迁移住院药品明细表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientMedicinelistNow(Map<String,Object> map);
	/**
	 * 迁移住院非药品明细表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientItemlistNow(Map<String,Object> map);
	/**
	 * 迁移病区退费申请表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientCancelitemNow(Map<String,Object> map);
	/**
	 * 迁移住院结算实付表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientBalancepayNow(Map<String,Object> map);
	/**
	 * 迁移住院结算头表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientBalanceheadNow(Map<String,Object> map);
	/**
	 * 迁移预交金表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientInprepayNow(Map<String,Object> map);
	/**
	 * 迁移费用汇总表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientFeeinfoNow(Map<String,Object> map);
	/**
	 * 迁移非药嘱执行档表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientExecundrugNow(Map<String,Object> map);
	/**
	 * 迁移药嘱执行档表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientExecdrugNow(Map<String,Object> map);
	/**
	 * 迁移医嘱表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientOrderNow(Map<String,Object> map);
	/**
	 * 迁移转科申请表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientShiftapplyNow(Map<String,Object> map);
	/**
	 * 迁移血液申请表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientApplyNow(Map<String,Object> map);
	/**
	 * 迁移住院住表中的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	void moveInpatientInfoNow(Map<String,Object> map);
	/**********************************删除数据*********************************************/
	/**
	 * 删除住院结算明细表中迁移成功的
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientBalancelistNow(Map<String, Object> map);
	/**
	 * 删除出库记录表中迁移成功的
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteDrugOutstoreNow(Map<String,Object> map);
	/**
	 * 删除出库申请表中迁移成功的
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteDrugApplyoutNow(Map<String,Object> map);
	/**
	 * 删除住院药品明细表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientMedicinelistNow(Map<String,Object> map);
	/**
	 * 删除住院非药品明细表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientItemlistNow(Map<String,Object> map);
	/**
	 * 删除病区退费申请表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientCancelitemNow(Map<String,Object> map);
	/**
	 * 删除住院结算实付表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientBalancepayNow(Map<String,Object> map);
	/**
	 * 删除住院结算头表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientBalanceheadNow(Map<String,Object> map);
	/**
	 * 删除预交金表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientInprepayNow(Map<String,Object> map);
	/**
	 * 删除费用汇总表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientFeeinfoNow(Map<String,Object> map);
	/**
	 * 删除非药嘱执行档表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientExecundrugNow(Map<String,Object> map);
	/**
	 * 删除药嘱执行档表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientExecdrugNow(Map<String,Object> map);
	/**
	 * 删除医嘱表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientOrderNow(Map<String,Object> map);
	/**
	 * 删除转科申请表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientShiftapplyNow(Map<String,Object> map);
	/**
	 * 删除血液申请表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientApplyNow(Map<String,Object> map);
	/**
	 * 删除住院主表中迁移成功的数据
	 * @param map page：当前页，row：每页行数，state 迁移步骤 1：对now表操作 2：对mid表操作， date：数据所在时间，当state为2时date为空
	 */
	public void deleteInpatientInfoNow(Map<String,Object> map);

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
