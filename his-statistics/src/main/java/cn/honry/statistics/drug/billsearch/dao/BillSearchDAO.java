package cn.honry.statistics.drug.billsearch.dao;

import java.util.List;

import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.base.bean.model.DrugApplyout;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.dao.EntityDao;
import cn.honry.statistics.drug.billsearch.vo.BillClassHzVo;
import cn.honry.statistics.drug.billsearch.vo.BillClassMxVo;
import cn.honry.statistics.operation.operationBillingInfo.vo.OperationBillingInfoVo;

/**   
* @className：摆药单查询
* @description：  摆药单查询 dao
* @author：tangfeishuai
* @createDate：2016-5-30 上午10:52:19  
* @modifier：tangfeishuai
* @modifyDate：2016-5-30 上午10:52:19  
* @modifyRmk：  
* @version 1.0
*/
@SuppressWarnings({"all"}) 
public interface BillSearchDAO extends EntityDao<OperationBillingInfoVo>{
	
	/**   
	     根据id 查询登录病区关联的科室
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	 * @throws Exception 
	*/
	public List<DepartmentContact> getDepConByPid(String pid) throws Exception;
	
	/**   
	    查询医院维护的所有摆药单分类
	* @author：tangfeishuai
	* @createDate：2016-6-12 上午10:52:19  
	* @modifier：tangfeishuai
	* @modifyDate：2016-6-12 上午10:52:19  
	* @modifyRmk：  
	* @version 1.0
	 * @throws Exception 
	*/
	public List<DrugBillclass> getDrugBillclass() throws Exception; 
	
	/**   
	    查询医院维护的所有摆药单分类
	 * @author：tangfeishuai
	 * @createDate：2016-6-12 上午10:52:19  
	 * @modifier：tangfeishuai
	 * @modifyDate：2016-6-12 上午10:52:19  
	 * @modifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	public List<DrugApplyout> getDrugOutstore(String deptCode,String billClassCode,String beginTime,String endTime,String drugedBill,String applyState) throws Exception; 
	
	/***
	 * 得到摆药单汇总hql
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 * @throws Exception 
	 */
	String getBillClassHzHql(String drugedBill,String name,String applyState) throws Exception;
	/***
	 * 摆药单汇总明细hql
	 * @Description:
	 * @author:  tangfeishuai
	 * @CreateDate: 2016年6月13日 
	 * @version 1.0
	 * @parameter  drugedBill 摆药单号，applyState 申请状态
	 * @since 
	 * @return List<BillClassHzVo>
	 * @throws Exception 
	 */
	String getBillClassMxHql(String drugedBill,String bname,String applyState) throws Exception;
	
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
	 * 摆药单明细信息
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
