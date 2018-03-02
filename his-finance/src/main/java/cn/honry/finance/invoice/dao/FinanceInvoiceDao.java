package cn.honry.finance.invoice.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.StaleObjectStateException;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface FinanceInvoiceDao extends EntityDao<FinanceInvoice>{
	/**
	 *  查询所有符合条件的数据
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param financeInvoice 实体
	 * @return
	 */
	List<FinanceInvoice> queryFinanceInvoice(FinanceInvoice financeInvoice);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param financeInvoice 实体
	 * @return
	 */
	int getFinanceInvoiceCount(FinanceInvoice financeInvoice);
	/**
	 *  增加数据
	 * @author sgt
	 * @date 2015-06-15
	 * @version 1.0
	 * @param financeInvoice 实体
	 * @param num 
	 * @param id 
	 * @return
	 * @throws Exception 
	 */
	void saveFinanceInvoice(FinanceInvoice financeInvoice, String num);
	/**
	 *  人员树
	 * @author sgt
	 * @date 2015-06-16
	 * @version 1.0
	 * @return
	 */
	List<Map<String, Object>>  employeeTree();
	/**
	 *  根据领取人和发票种类，查出领取的所有发票号
	 * @author kjh
	 * @date 2015-06-24
	 * @version 1.0
	 * @param invoiceGetperson ：领取人；invoiceType：发票种类
	 * @return 返回list
	 */
	List<FinanceInvoice> findByGetPerson(String invoiceGetperson,String invoiceType);
	/**
	 * 查询数据库中同类型的发票的最大号+1
	 * @author kjh
	 * @date 2015-06-24
	 * @version 1.0
	 * @param 领取人；invoiceType：发票种类
	 * @return 最大发票号+1
	 */
	List<FinanceInvoiceStorage> findMaxStartNo(String invoiceType);

	/**
	 *  人员为挂号员的数据
	 * @author wj
	 * @date 2015-12-03
	 * @version 1.0
	 * @param Manufacturer
	 * @return
	 */
	List<SysEmployee> getAllEmp(SysEmployee sysEmployee);
	/**
	 * @Description:一次性领取多张发票
	 * @author: lt
	 * @CreateDate:2016-2-2
	 * @version:1.0
	 */
	Map<String,Object> findFinanceInvoice(String id, String invoiceType, int size);
	/**
	 * 查询条数
	 * @param num 
	 * @param invoiceType 
	 * @return
	 */
	String sumfinance(String num, String invoiceType);
	
	/** 发票领取更新入库表
	* @Title: updateFinanceInvoiceStorage 
	* @Description: 发票领取更新入库表
	* @param financeInvoice 前台传来发票领取表的实体
	* @param num 前台传来的申请数量
	* @author dtl 
	* @date 2016年11月28日
	*/
	void updateFinanceInvoiceStorage(FinanceInvoice financeInvoice, String num) throws Exception;
	/**
	 * @Description 根据类型查询库
	 * @author  marongbin
	 * @createDate： 2017年4月6日 下午5:44:35 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceType
	 * @return: List<FinanceInvoiceStorage>
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<FinanceInvoiceStorage> findFinanceInvoiceStorageBytype(String invoiceType);
}
