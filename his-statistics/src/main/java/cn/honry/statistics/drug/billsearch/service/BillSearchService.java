package cn.honry.statistics.drug.billsearch.service;

import java.util.List;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.drug.billsearch.vo.BillClassHzVo;
import cn.honry.statistics.drug.billsearch.vo.BillClassMxVo;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;
import cn.honry.utils.FileUtil;
import cn.honry.utils.TreeJson;
/***
 * 摆药单分类service层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface BillSearchService  extends BaseService<OperationBillingInfoVo>{
	
	/***
	 * 登录病区下的各科室摆药单信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月12日 
	 * @version 1.0
	 * @parameter beganTime 开始时间,endTime 结束时间, billCode 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<TreeJson>
	 * @throws Exception 
	 */
	List<TreeJson> treeBillSearch(String beginTime,String endTime,String drugedBill,String applyState) throws Exception;
	
	
	/***
	 * 摆药单汇总信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 * @throws Exception 
	 */
	List<BillClassHzVo> getBillClassHzVo(String drugedBill,String applyState,String bname,String page,String rows) throws Exception;
	/***
	 * 摆药单汇总信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassMxVo>
	 * @throws Exception 
	 */
	List<BillClassMxVo> getBillClassMxVo(String drugedBill,String applyState,String bname,String page,String rows) throws Exception;
	/**
	 * @Description:根据条件查询摆药单汇总记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @throws Exception 
	 */
	public int getBillHzTotal(String drugedBill,String bname,String applyState) throws Exception; 
	/**
	 * @Description:根据条件查询摆药单明细记录数
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年6月8日 上午9:47:41 
	 * @param:beganTime 开始时间； endTime 结束时间； patientNo 住院流水号； execDept 执行科室
	 * @return List<OperationCostVo>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 * @throws Exception 
	 */
	public int getBillMxTotal(String drugedBill,String bname,String applyState) throws Exception; 
	
	/**
	 * @Description:导出摆药单汇总列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月29日 
	 * @version 1.0
	**/
	FileUtil exportBillClassHzVo(List<BillClassHzVo> list, FileUtil fUtil);
	
	/**
	 * @Description:导出摆药单明细列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月29日 
	 * @version 1.0
	 **/
	FileUtil exportBillClassMxVo(List<BillClassMxVo> list, FileUtil fUtil);
	
	/***
	 * 所有摆药单汇总信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月29日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 * @throws Exception 
	 */
	List<BillClassHzVo> getAllBillClassHzVo(String drugedBill,String applyState,String bname) throws Exception;
	
	/***
	 * 所有摆药单明细信息
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月29日 
	 * @version 1.0
	 * @parameter drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassMxVo>
	 * @throws Exception 
	 */
	List<BillClassMxVo> getAllBillClassMxVo(String drugedBill,String applyState,String bname) throws Exception;

}
