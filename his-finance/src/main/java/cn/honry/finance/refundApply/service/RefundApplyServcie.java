package cn.honry.finance.refundApply.service;

import java.util.Map;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.service.BaseService;

public interface RefundApplyServcie extends BaseService<InpatientCancelitem>{
	/**  
	 * @Description：  生成退费申请记录
	 * @Author：ldl
	 * @CreateDate：2016-06-27
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param clinicCode 门诊号
	 * @param invoiceNo 发票号
	 * @param drugList 退费药品集合
	 * @param unDrugList 退费非药品集合
	 */
	Map<String, String> saveRefund(String invoiceNo, String drugList,String unDrugList, String clinicCode);

}
