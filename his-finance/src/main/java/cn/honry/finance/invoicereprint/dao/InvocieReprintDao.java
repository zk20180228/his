package cn.honry.finance.invoicereprint.dao;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.base.dao.EntityDao;
import cn.honry.finance.invoicereprint.vo.InvoiceReprintVO;

public interface InvocieReprintDao extends EntityDao<OutpatientFeedetailNow> {
	
	/** 
	* @Title: getInvoiceVO 
	* @Description: 根据门诊号或发票号获取发票信息
	* @param clinicCode
	* @param invoiceNo
	* @return
	* @return List<InvoiceReprintVO>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月9日
	*/
	List<InvoiceReprintVO> getInvoiceVO(String clinicCode,String invoiceNo) throws Exception;
	
	/** 
	* @Title: getfee 
	* @Description: 根据发票号查询处方明细
	* @param invoiceNo
	* @return
	* @return List<OutpatientFeedetailNow>    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月9日
	*/
	List<OutpatientFeedetailNow> getfee(String invoiceNo) throws Exception;
	
	/** 
	* @Title: updateInvoice 
	* @Description: 更新相关表的发票号
	* @param oldInvoiceNo
	* @param newInvoiceNo
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月10日
	*/
	int updateInvoiceInfo(String oldInvoiceNo,String newInvoiceNo) throws Exception;
	
	/** 
	* @Title: updatePayMode 
	* @Description: 更新相关表的发票号
	* @param oldInvoiceNo
	* @param newInvoiceNo
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月10日
	*/
	int updatePayMode(String oldInvoiceNo,String newInvoiceNo) throws Exception;
	
	/** 
	* @Title: updateInvoiceDetial 
	* @Description: 更新相关表的发票号
	* @param oldInvoiceNo
	* @param newInvoiceNo
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月10日
	*/
	int updateInvoiceDetial(String oldInvoiceNo,String newInvoiceNo) throws Exception;
	
	/** 
	* @Title: updateFeeDetail 
	* @Description: 更新相关表的发票号
	* @param oldInvoiceNo
	* @param newInvoiceNo
	* @return void    返回类型 
	* @throws 
	* @author mrb
	* @date 2017年5月10日
	*/
	int updateFeeDetail(String oldInvoiceNo,String newInvoiceNo) throws Exception;
}
