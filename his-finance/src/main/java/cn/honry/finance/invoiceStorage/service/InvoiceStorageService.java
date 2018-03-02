package cn.honry.finance.invoiceStorage.service;

import java.util.List;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.service.BaseService;

public interface InvoiceStorageService extends BaseService<FinanceInvoiceStorage>{
	/**  
	 *   查询发票类型list
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 * @param encode 
	 * @param type 
	 */
	List<BusinessDictionary> queryInvoiceType(String type, String encode) throws Exception;
	/**  
	 *   查询发票入库记录
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 */
	List<FinanceInvoiceStorage> queryInvoiceStorage(String page, String rows,
			FinanceInvoiceStorage fis) throws Exception;
	/**  
	 *   查询发票入库记录总条数
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 * @param fis 
	 */
	int getTotal(FinanceInvoiceStorage fis) throws Exception;
	/**  
	 *   保存（修改）发票入库记录
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 */
	String saveform(FinanceInvoiceStorage financeInvoiceStorage) throws Exception;

}
