package cn.honry.finance.invoiceStorage.dao;

import java.util.List;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface InvoiceStorageDao extends EntityDao<FinanceInvoiceStorage>{
	/**  
	 *   查询发票类型list
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
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
	FinanceInvoiceStorage queryFISById(String id) throws Exception;
	/**  
	 *   根据发票类型擦滑坠发票
	 * @Author：tcj
	 * @CreateDate：2016-6-28
	 * @version 1.0
	 * @param fis 
	 */
	List<FinanceInvoiceStorage> queryFISByType(String type) throws Exception;
}
