package cn.honry.finance.invoiceRecall.dao;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface InvoiceRecallDao extends EntityDao<FinanceInvoice> {
	/**  
	 *   查询发票领取记录
	 * @Author：tcj
	 * @CreateDate：2016-06-30
	 * @ModifyRmk：  
	 * @version 1.0
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
	 */
	List<SysEmployee> queryEmpMap() throws Exception;
}
