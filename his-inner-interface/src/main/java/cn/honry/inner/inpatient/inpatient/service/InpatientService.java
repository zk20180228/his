package cn.honry.inner.inpatient.inpatient.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.MoveDataLog;
/**
 * 
 * @author hzr
 *	住院数据迁移接口
 *
 */
public interface InpatientService {
	
	
	/**
	 * 获取最大时间最小时间
	 * @param  date 迁移数据所在时间
	 * @return
	 */
	public Map<String, Object> getInpatentNos(String date);
	/**
	 * 根据住院流水号，获取住院药品明细表中的处方号
	 * @param date 迁移数据所在时间
	 * @return
	 */
	public List<String> getRecipes(String date);
	
	/**
	 * 根据住院流水号，获取住院结算头细表中的发票号
	 * @param date 迁移数据所在时间
	 * @return
	 */
	public List<String> getInvoices(String date);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院结算明细表分页情况
	 */
	Map<String, Object> countInpatientBalancelistNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 出库记录表分页情况
	 */
	Map<String, Object> countDrugOutstoreNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 出库申请表分页情况
	 */
	Map<String, Object> countDrugApplyoutNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院药品明细表分页情况
	 */
	Map<String, Object> countInpatientMedicinelistNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院非药品明细表分页情况
	 */
	Map<String, Object> countInpatientItemlistNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 病区退费申请表分页情况
	 */
	Map<String, Object> countInpatientCancelitemNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院结算实付表分页情况
	 */
	Map<String, Object> countInpatientBalancepayNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院结算头表分页情况
	 */
	Map<String, Object> countInpatientBalanceheadNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 预交金表分页情况
	 */
	Map<String, Object> countInpatientInprepayNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 费用汇总表分页情况
	 */
	Map<String, Object> countInpatientFeeinfoNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 非药嘱执行档表分页情况
	 */
	Map<String, Object> countInpatientExecundrugNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 药嘱执行档表分页情况
	 */
	Map<String, Object> countInpatientExecdrugNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 医嘱表分页情况
	 */
	Map<String, Object> countInpatientOrderNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 转科申请表分页情况
	 */
	Map<String, Object> countInpatientShiftapplyNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 血液申请表分页情况
	 */
	Map<String, Object> countInpatientApplyNow(String date, String state);
	/**
	 * 根据出院时间
	 * @param date 迁移数据所在时间
	 * @param state 迁移步骤 1：对now表操作 2：对mid表操作
	 * @return 住院主表分页情况
	 */
	Map<String,Object> countInpatientInfoNow(String date, String state);
	/**
	 * 迁移住院明细表中的数据
	 */
	void moveInpatientBalancelistNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移出库记录表中的数据
	 */
	void moveDrugOutstoreNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移出库申请表中的数据
	 */
	void moveDrugApplyoutNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移住院药品明细表中的数据
	 */
	void moveInpatientMedicinelistNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移住院非药品明细表中的数据
	 */
	void moveInpatientItemlistNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移病区退费申请表中的数据
	 */
	void moveInpatientCancelitemNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移住院结算实付表中的数据
	 */
	void moveInpatientBalancepayNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移住院结算头表中的数据
	 */
	void moveInpatientBalanceheadNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移预交金表中的数据
	 */
	void moveInpatientInprepayNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移费用汇总表中的数据
	 */
	void moveInpatientFeeinfoNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移非药嘱执行档表中的数据
	 */
	void moveInpatientExecundrugNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移药嘱执行档表中的数据
	 */
	void moveInpatientExecdrugNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移医嘱表中的数据
	 */
	void moveInpatientOrderNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移转科申请表中的数据
	 */
	void moveInpatientShiftapplyNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移血液申请表中的数据
	 */
	void moveInpatientApplyNow(Map<String,Object> map)throws Exception;
	/**
	 * 迁移住院住表中的数据
	 */
	void moveInpatientInfoNow(Map<String,Object> map)throws Exception;
	
	/**********************************删除***************************************************/
	/**
	 * 删除住院结算明细表中迁移成功的数据
	 */
	public void deleteInpatientBalancelistNow(Map<String,Object> map) ;
	/**
	 * 删除出库记录表中迁移成功的数据
	 */
	public void deleteDrugOutstoreNow(Map<String,Object> map);
	/**
	 * 删除出库申请表中迁移成功的数据
	 */
	public void deleteDrugApplyoutNow(Map<String,Object> map);
	/**
	 * 删除住院药品明细表中迁移成功的数据
	 * @param date
	 */
	public void deleteInpatientMedicinelistNow(Map<String,Object> map);
	/**
	 * 删除住院非药品明细表中迁移成功的数据
	 */
	public void deleteInpatientItemlistNow(Map<String,Object> map);
	/**
	 * 删除病区退费申请表中迁移成功的数据
	 * @param date
	 */
	public void deleteInpatientCancelitemNow(Map<String,Object> map);
	/**
	 * 删除住院结算实付表中迁移成功的数据
	 */
	public void deleteInpatientBalancepayNow(Map<String,Object> map);
	/**
	 * 删除住院结算头表中迁移成功的数据
	 * @param date
	 */
	public void deleteInpatientBalanceheadNow(Map<String,Object> map);
	/**
	 * 删除预交金表中迁移成功的数据
	 */
	public void deleteInpatientInprepayNow(Map<String,Object> map);
	/**
	 * 删除费用汇总表中迁移成功的数据
	 * @param date
	 */
	public void deleteInpatientFeeinfoNow(Map<String,Object> map);
	/**
	 * 删除非药嘱执行档表中迁移成功的数据
	 */
	public void deleteInpatientExecundrugNow(Map<String,Object> map);
	/**
	 * 删除药嘱执行档表中迁移成功的数据
	 */
	public void deleteInpatientExecdrugNow(Map<String,Object> map);
	/**
	 * 删除医嘱表中迁移成功的数据
	 */
	public void deleteInpatientOrderNow(Map<String,Object> map);
	/**
	 * 删除转科申请表中迁移成功的数据
	 */
	public void deleteInpatientShiftapplyNow(Map<String,Object> map);
	/**
	 * 删除血液申请表中迁移成功的数据
	 */
	public void deleteInpatientApplyNow(Map<String,Object> map);
	/**
	 * 删除住院主表中迁移成功的数据
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
