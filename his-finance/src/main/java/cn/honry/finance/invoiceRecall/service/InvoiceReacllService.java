package cn.honry.finance.invoiceRecall.service;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface InvoiceReacllService extends BaseService<FinanceInvoice>{
	/**  
	 *   查询发票领取记录
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<FinanceInvoice> queryInvoiceRecall(String page,String rows,String name) throws Exception;
	/**  
	 *   查询发票领取记录总条数
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	int getTotal(String name);
	/**  
	 *   查询员工Map
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<SysEmployee> queryEmpMap() throws Exception;
	/**  
	 *   发票召回
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String recallInvoice(String date,String num);

}
