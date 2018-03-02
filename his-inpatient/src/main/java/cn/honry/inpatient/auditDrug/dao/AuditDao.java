package cn.honry.inpatient.auditDrug.dao;

import java.util.List;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugInfo;
import cn.honry.base.bean.model.DrugOutstoreNow;
import cn.honry.base.bean.model.DrugStorage;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientMedicineListNow;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.InpatientStoMsgNow;
import cn.honry.base.bean.model.OutpatientDrugcontrol;
import cn.honry.base.dao.EntityDao;
/***
 * 
 * @ClassName: AuditDao 
 * @Description: 
 * @author wfj
 * @date 2016年4月11日 下午8:42:21 
 *
 */
@SuppressWarnings({"all"})
public interface AuditDao extends EntityDao<DrugApplyoutNow>{
	/**  
	 * @Description：医嘱类型渲染
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InpatientKind> queryKindFunction();
	/**  
	 * @Description：  摆药台下拉
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: id 当前登陆药房
	 * @version 1.0
	 */
	List<OutpatientDrugcontrol> queryDrugHouse(String id);
	/**  
	 * @Description：  根据当期前登陆药房选择一个出室化的摆药台
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: id 当前登陆药房
	 * @version 1.0
	 */
	OutpatientDrugcontrol queryDrugcontril(String id);
	/**  
	 * @Description： 根据摆药台ID查询摆药单分类
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: id 当前登陆摆药台
	 * @version 1.0
	 */
	List<DrugBillclass> findDillClassById(String controlId);
	/**  
	 * @Description： 根据当前登陆药房等于摆药科室查询出库申请单 
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: deptId 取药科室
	 * @param: classIds 摆药单分类Ids
	 * @param: sendType 摆药状态
	 * @param: opType 摆药类别
	 * @version 1.0
	 * @param medicalrecordId 住院号
	 * @param sendFlags 通知状态
	 */
	List<DrugApplyoutNow> findApplyoutByDept(String deptId, List classIds,Integer sendType, Integer opType,String[] applyStates, String medicalrecordId, String[] sendFlags);
	/**  
	 * @Description： 根据当前登陆药房等于摆药科室查询出库申请单 
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: deptId 取药科室
	 * @param: deptCode 开立科室
	 * @param ： sendType 摆药状态
	 * @param: opType 必要分类
	 * @version 1.0
	 * @param medicalrecordId 住院号
	 * @param sendFlags 通知状态
	 */
	List<DrugApplyoutNow> findApplyoutByClass(String deptId, String deptCode,Integer sendType, Integer opType,String[] applyStates, String medicalrecordId, String[] sendFlags);
	/**  
	 * @Description： 根据当前登陆药房等于摆药科室查询出库申请单 
	 * @Author：ldl
	 * @CreateDate：2016-04-29
	 * @ModifyRmk：  
	 * @param: deptId 取药科室
	 * @param: billclassCode 摆药单分类Ids
	 * @param: deptCode 开立科室
	 * @param: sendType 摆药状态
	 * @param: opType 摆药类别
	 * @version 1.0
	 * @param medicalrecordId 住院号
	 * @param sendFlags 通知状态
	 */
	List<DrugApplyoutNow> findApplyoutBybill(String deptId, String billclassCode,String deptCode, Integer sendType, Integer opType,String[] applyStates, String medicalrecordId, String[] sendFlags);
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
	 */
	List<DrugApplyoutNow> findDeptSummary(String drugedBill, Integer opType,Integer sendType, String sendFlag, String applyState);
	/**  
	 * @Description：摆药明细记录
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: deptId 摆药单对应的科室
	 * @param: opType 摆药类型
	 * @param: applyState 申请类型
	 * @version 1.0
	 */
	List<DrugApplyoutNow> findDrugApplyoutByDeptId(String deptId,String applyState, Integer opType);
	/**  
	 * @Description：根据申请单号集合查询申请记录
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: applyNumberCode 申请单号集合
	 * @version 1.0
	 */
	List<DrugApplyoutNow> findApplyoutByApplyNumber(String applyNumberCode);
	/**  
	 * @Description：根据住院流水号查询患者住院记录
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: patientId 患者编码（住院流水号）
	 * @version 1.0
	 */
	InpatientInfoNow queryInpatientInfo(String patientId);
	/**  
	 * @Description：根据药品Id查询该药品是否存在
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: patientId 患者编码（住院流水号）
	 * @version 1.0
	 */
	DrugInfo queryDrugInfo(String itemCode);
	/**  
	 * @Description： 根据处方号与处方内流水号查询住院药品明细表
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param： recipeNo 处方号
	 * @param : sequenceNo  处方内流水号
	 * @version 1.0
	 */
	InpatientMedicineListNow queryInpatientMedicineList(String recipeNo,Integer sequenceNo);
	/**  
	 * @Description： 根据处方号与处方内流水号查询药嘱执行档
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param： recipeNo 处方号
	 * @param : sequenceNo  处方内流水号
	 * @version 1.0
	 */
	InpatientExecdrugNow queryInpatientExecdrug(String recipeNo, Integer sequenceNo);
	/**  
	 * @Description： 根据医嘱流水号查询主档医嘱
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param： moOrder 医嘱流水号
	 * @version 1.0
	 */
	InpatientOrderNow queryInpatientOrder(String moOrder);
	/**  
	 * @Description： 根据摆药单分类查询摆药通知单
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param： billclassCode 摆药单分类
	 * @version 1.0
	 */
	InpatientStoMsgNow queryInpatientStoMsg(String billclassCode);
	/**  
	 * @Description： 科室汇总记录显示列表
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param： drugedBill 摆药单号
	 * @param： opType 摆药台类型
	 * @param： sendType 发送类型
	 * @param： sendFlag 摆药状态
	 * @param： applyState 申请类型
	 * @param： deptId 摆药单号对应科室
	 * @version 1.0
	 */
	List<DrugApplyoutNow> findDetailed(String drugedBill, Integer opType,Integer sendType, String sendFlag, String[] applyStates, String deptId);
	/**  
	 * @Description：根据病历号查询住院流水号
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: medicalrecordId 住院号/病历号
	 * @version 1.0
	 */
	InpatientInfoNow queryMedicalrecordId(String medicalrecordId);
	/**  
	 * @Description： 根据药品查询库存中批次
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param： drugCode 药品ID
	 * @param : drugDeptCode 药房
	 * @version 1.0
	 */
	List<DrugStorage> findDrugStorageByDrugId(String drugDeptCode,String drugCode);
	/**  
	 * @Description：根据出库单号查询出库记录表
	 * @Author：ldl
	 * @CreateDate：2016-05-04
	 * @ModifyRmk：  
	 * @param: outstore 摆药单号
	 * @version 1.0
	 */
	List<DrugOutstoreNow> findExDrugOutstore(List<String> outstore);
	/**  
	 * @Description：退药过程
	 * @Author：ldl
	 * @CreateDate：2016-05-06
	 * @ModifyRmk：  
	 * @param: applyNumberCode 申请单号集合
	 * @version 1.0
	 */
	List<DrugApplyoutNow> findDrugApplyoutBywith(String applyNumberCode);
	/**  
	 * @Description：更新住院费用汇总表
	 * @Author：ldl
	 * @CreateDate：2016-06-16
	 * @ModifyRmk：  
	 * @param: recipeNo 处方号
	 * @version 1.0
	 */
	InpatientFeeInfoNow queryInpatientFeeInfo(String recipeNo);
	
	/**  
	 * @Description：根据处方号、处方内流水号查询药品明细
	 * @Author：zxl
	 * @CreateDate：2016-06-17
	 * @ModifyRmk：  
	 * @param: 
	 * @version 1.0
	 */
	InpatientMedicineListNow getMed(String recipeNo, Integer sequenceNo);
	
}
