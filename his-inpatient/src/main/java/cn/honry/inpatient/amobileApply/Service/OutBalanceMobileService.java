package cn.honry.inpatient.amobileApply.Service;

import java.util.Map;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.service.BaseService;

public interface OutBalanceMobileService extends BaseService<InpatientInfo> {
	/**
	 * 出院结算保存
	 * @throws Exception 
	 */
	String saveBalance(Map<String, String> parameterMap,String zfJson,String costJson,String empJobNo,String deptCode,String empJobName,String deptName) throws Exception;
	/**
	 * 中途和欠费结算保存
	 * @throws Exception 
	 */
	String saveBalanceZhongtu(Map<String, String> parameterMap,String zfJson,String costJson,String empJobNo,String deptCode,String empJobName,String deptName) throws Exception;
	
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
	public Map<String, String> approvalDrugSave(String applyNumberCode,String parameterHz,String empJobNo,String deptCode) throws Exception ;
	
	public Map<String, String> examineDrugSaveAndUpdate(String applyNumber,String ids,String empJobNo,String deptCode) throws Exception;
}
