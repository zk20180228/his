package cn.honry.finance.repairPrint.service;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.service.BaseService;
import cn.honry.finance.repairPrint.vo.InvoiceRepPrintVo;

public interface InvoiceRepPrintService extends BaseService<FinanceInvoiceInfoNow>{
	/**  
	 *  
	 * @Description：  返查询所有符合条件的数据
	 * @Author：kjh
	 * @CreateDate：2015-6-12   
	 * @version 1.0
	 *
	 */
	List<InvoiceRepPrintVo> queryInvoiceInfo(InvoiceRepPrintVo invoiceRepPrintVo);
	/**  
	 *  
	 * @Description：  返查询住院结算明细表的数据
	 * @Author：yeguanqun
	 * @CreateDate：2016-4-22   
	 * @version 1.0
	 *
	 */
	List<InpatientBalanceListNow> queryBalanceList(InpatientBalanceList inpatientBalanceList);
}
