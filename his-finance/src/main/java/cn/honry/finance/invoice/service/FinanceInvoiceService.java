package cn.honry.finance.invoice.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.SysEmployee;
@SuppressWarnings({"all"})
public interface FinanceInvoiceService {
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
	 * @param id 
	 * @return
	 * @throws Exception 
	 */
	void saveFinanceInvoice(FinanceInvoice financeInvoice, String num) throws Exception;
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
	 * @return
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
	 * 查询条数
	 * @param num 
	 * @param invoiceType 
	 * @return
	 */
	String sumfinance(String num, String invoiceType);
	/**
	 * 查询领取发票号是否超过发票总数量
	 * @param num
	 * @param financeInvoice
	 * @return
	 */
	int sumfinace(String num, FinanceInvoice financeInvoice);
	/**
	 * @Description 根据结束号获取发票数量
	 * @author  marongbin
	 * @createDate： 2017年4月6日 下午4:51:39 
	 * @modifier 
	 * @modifyDate：
	 * @param invoiceType
	 * @param endNum
	 * @return: String
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String getNum(String invoiceType,String endNum);
}
