package cn.honry.finance.refundConfirm.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.InpatientCancelitem;
import cn.honry.base.bean.model.InpatientCancelitemNow;
import cn.honry.base.service.BaseService;

public interface RefundConfirmService extends BaseService<InpatientCancelitem>{
	/**  
	 * @Description：  查询患者退费申请
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @param：billNo 发票号
	 * @version 1.0
	 */
	List<InpatientCancelitemNow> query(String billNo);
	/**  
	 * @Description：  退费确认
	 * @Author：ldl
	 * @CreateDate：2016-06-28
	 * @ModifyRmk：  
	 * @param：drugIds 退费药品ID集合
	 * @param applyCost 退费金额
	 * @param payType 退费路径
	 * @param invoiceNo 原发票号
	 * @param newInvoiceNo 现发票号
	 * @param medicalRecord 病历号
	 * @version 1.0
	 */
	Map<String, String> refundSave(String drugIds, String newInvoiceNo, String invoiceNo, String payType, String applyCost, String medicalRecord);
	/**
	 * @Description 退费确认(现在使用)
	 * @author  marongbin
	 * @createDate： 2016年12月19日 下午5:16:49 
	 * @modifier 
	 * @modifyDate：
	 * @param drugApplyIds
	 * @param newInvoiceNo
	 * @param invoiceNo
	 * @param payType
	 * @param applyCost
	 * @param medicalRecord
	 * @return: Map<String,String>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	Map<String, String> refundSaveNow(String drugApplyIds, String newInvoiceNo,String invoiceNo, String payType, String applyCost,String medicalRecord);

}
