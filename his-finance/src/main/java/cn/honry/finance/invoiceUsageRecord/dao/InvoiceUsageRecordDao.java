package cn.honry.finance.invoiceUsageRecord.dao;

import java.util.List;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface InvoiceUsageRecordDao extends EntityDao<InvoiceUsageRecord>{

	List<InvoiceUsageRecord> queryDatagrid(String page, String rows, String code,
			String type, String num) throws Exception;

	int getTotal(String code,String type, String num) throws Exception;
	/**
	 * 根据Id查询发票使用表中的对象
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	InvoiceUsageRecord getRecordModel(String id) throws Exception;
	/**
	 * 查询用户Map
	 * @author tuchuanjiang
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<User> queryUserRecord() throws Exception;
	/**
	 * 查询发票领取表中的记录
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 * @throws Exception 
	 */
	List<FinanceInvoice> queryFinInvoice() throws Exception;

}
