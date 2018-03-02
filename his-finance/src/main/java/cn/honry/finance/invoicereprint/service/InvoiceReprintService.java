package cn.honry.finance.invoicereprint.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.finance.invoicereprint.vo.InvoiceReprintVO;

public interface InvoiceReprintService {
	/** 
	* @Title: getInvoiceBycode 
	* @Description: 根据发票号和门诊号获取发票信息
	* @param clinicCode
	* @param invoiceNo
	* @return
	* @return List<InvoiceReprintVO>    返回类型 
	 * @throws Exception 
	* @throws 
	* @author mrb
	* @date 2017年5月9日
	*/
	List<InvoiceReprintVO> getInvoiceBycode(String clinicCode,String invoiceNo) throws Exception;
	/** 
	* @Title: reprint 
	* @Description: 重打/补打操作
	* @param invoiceNo
	* @return
	* @return Map<String,String>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月9日
	*/
	Map<String,String> reprint(String invoiceNo);
	/** 
	* @Title: getfee 
	* @Description: 根据发票号查询处方明细
	* @param invoiceNo
	* @return
	* @return List<OutpatientFeedetailNow>    返回类型 
	 * @throws Exception 
	* @throws 
	* @author mrb
	* @date 2017年5月9日
	*/
	List<OutpatientFeedetailNow> getfee(String invoiceNo) throws Exception;
}
