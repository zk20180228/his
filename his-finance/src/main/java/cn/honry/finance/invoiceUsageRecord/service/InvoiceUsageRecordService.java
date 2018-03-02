package cn.honry.finance.invoiceUsageRecord.service;

import java.util.List;

import cn.honry.base.bean.model.InvoiceUsageRecord;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;

public interface InvoiceUsageRecordService extends BaseService<InvoiceUsageRecord>{
	/**
	 * 查询发票使用记录
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<InvoiceUsageRecord> queryDatagrid(String page, String rows, String code,
			String type, String num) throws Exception;
	/**
	 * 查询发票使用记录总条数
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	int getTotal(String code,String type, String num) throws Exception;
	/**
	 * 将选中的未使用发票召回
	 * @Author：tcj
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	String saveData(String rowss) throws Exception;
	/**
	 * 查询用户Map
	 * @author tuchuanjiang
	 * @CreateDate：2016-06-29
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	List<User> queryUserRecord() throws Exception;

}
