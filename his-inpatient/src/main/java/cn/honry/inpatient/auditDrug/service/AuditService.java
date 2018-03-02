package cn.honry.inpatient.auditDrug.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.service.BaseService;
import cn.honry.utils.TreeJson;
/***
 * 
 * @ClassName: AuditService 
 * @Description: 
 * @author wfj
 * @date 2016年4月11日 下午8:40:54 
 *
 */
@SuppressWarnings({"all"})
public interface AuditService extends BaseService<DrugApplyoutNow>{
	/**  
	 * @Description：医嘱类型渲染
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<InpatientKind> queryKindFunction() throws Exception;
	/**  
	 * @Description：  摆药台下拉
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: id 当前登陆药房
	 * @version 1.0
	 * @throws Exception 
	 */
	List<OutpatientDrugcontrol> queryDrugHouse(String id) throws Exception;
	/**  
	 * @Description：  根据当期前登陆药房选择一个出室化的摆药台
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: id 当前登陆药房
	 * @version 1.0
	 * @throws Exception 
	 */
	OutpatientDrugcontrol queryDrugcontril(String id) throws Exception;
	/**  
	 * @Description： 加载摆药树
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: controlId 摆药台ID
	 * @param: sendType 发送类型
	 * @param: opType 摆药类型
	 * @param: applyState 申请状态
	 * @param: sendFlag 通知状态
	 * @param: medicalrecordId病历号
	 * @version 1.0
	 * @throws Exception 
	 */
	List<TreeJson> approveNoDrugTree(String controlId, Integer sendType,Integer opType, String applyState, String sendFlag, String medicalrecordId) throws Exception;
	/**  
	 * @Description：摆药明细记录
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: drugedBill 摆药单号
	 * @param: opType 摆药台类型
	 * @param: sendType 发送类型
	 * @param: sendFlag 摆药通知类型
	 * @param: applyState 申请类型
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DrugApplyoutNow> findDeptSummary(String drugedBill, Integer opType, Integer sendType, String sendFlag, String applyState) throws Exception;
	/**  
	 * @Description：科室汇总记录
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: drugedBill 摆药单号
	 * @param: opType 摆药台类型
	 * @param: sendType 发送类型
	 * @param: sendFlag 摆药通知类型
	 * @param: applyState 申请类型
	 * @param: deptId 摆药单对应的科室
	 * @version 1.0
	 * @throws Exception 
	 */
	List<DrugApplyoutNow> findDetailed(String drugedBill, Integer opType,Integer sendType, String sendFlag, String applyState, String deptId) throws Exception;
	/**  
	 * @Description：摆药单审批过程 根据参数来确定 是直接核准 还是先审批
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: applyNumberCode 申请单号集合
	 * @param: parameterHz 是否直接核准参数
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String, String> approvalDrugSave(String applyNumberCode,String parameterHz) throws Exception;
	/**  
	 * @Description：根据病历号查询住院流水号
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: medicalrecordId 住院号/病历号
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String,String> queryMedicalrecordId(String medicalrecordId) throws Exception;
	/**  
	 * @Description：药品出库核准
	 * @Author：ldl
	 * @CreateDate：2016-05-05
	 * @ModifyRmk：  
	 * @param: applyNumber 申请单号集合
	 * @param： ids 出库记录号集合
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String,String> examineDrugSaveAndUpdate(String applyNumber, String ids) throws Exception;
	/**  
	 * @Description：退药过程
	 * @Author：ldl
	 * @CreateDate：2016-05-06
	 * @ModifyRmk：  
	 * @param: applyNumberCode 申请单号集合
	 * @param： parameterTf 退费参数
	 * @version 1.0
	 * @throws Exception 
	 */
	Map<String, String> withdrawalDrug(String applyNumberCode,String parameterTf) throws Exception;
}
