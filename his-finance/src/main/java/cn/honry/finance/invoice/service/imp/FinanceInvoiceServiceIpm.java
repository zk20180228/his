package cn.honry.finance.invoice.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.FinanceInvoice;
import cn.honry.base.bean.model.FinanceInvoiceStorage;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.finance.invoice.dao.FinanceInvoiceDao;
import cn.honry.finance.invoice.service.FinanceInvoiceService;
import cn.honry.inner.system.utli.OperationUtils;

@Service("financeInvoiceService")
@Transactional
@SuppressWarnings({ "all" })
public class FinanceInvoiceServiceIpm implements FinanceInvoiceService {
	@Autowired
	private FinanceInvoiceDao financeInvoiceDao;

	@Override
	public List<FinanceInvoice> queryFinanceInvoice(FinanceInvoice financeInvoice) {
		return financeInvoiceDao.queryFinanceInvoice(financeInvoice);
	}

	@Override
	public int getFinanceInvoiceCount(FinanceInvoice financeInvoice) {

		return financeInvoiceDao.getFinanceInvoiceCount(financeInvoice);
	}

	@Override
	public void saveFinanceInvoice(FinanceInvoice financeInvoice, String num) throws Exception{
		financeInvoiceDao.saveFinanceInvoice(financeInvoice,num);
		financeInvoiceDao.updateFinanceInvoiceStorage(financeInvoice,num);
	}
	public FinanceInvoiceDao getFinanceInvoiceDao() {
		return financeInvoiceDao;
	}

	public void setFinanceInvoiceDao(FinanceInvoiceDao financeInvoiceDao) {
		this.financeInvoiceDao = financeInvoiceDao;
	}

	@Override
	public List<Map<String, Object>> employeeTree() {
		return financeInvoiceDao.employeeTree();
	}

	@Override
	public List<FinanceInvoice> findByGetPerson(String invoiceGetperson, String invoiceType) {
		return financeInvoiceDao.findByGetPerson(invoiceGetperson, invoiceType);
	}

	@Override
	public List<FinanceInvoiceStorage> findMaxStartNo(String invoiceType) {
		return financeInvoiceDao.findMaxStartNo(invoiceType);
	}

	/**
	 * 人员为挂号员的数据
	 * 
	 * @author wj
	 * @date 2015-12-03
	 * @version 1.0
	 * @param Manufacturer
	 * @return
	 */
	@Override
	public List<SysEmployee> getAllEmp(SysEmployee sysEmployee) {
		return financeInvoiceDao.getAllEmp(sysEmployee);
	}

	@Override
	public String sumfinance(String num, String invoiceType) {
		List<FinanceInvoiceStorage> fislist = financeInvoiceDao.findMaxStartNo(invoiceType);
		int parseInt=Integer.parseInt(num);
		if(parseInt<=0){
			return null;
		}
		for (FinanceInvoiceStorage fis : fislist) {
			// 获取入库表中发票开始号
			String startNo = fis.getInvoiceStartno();
			Integer len = startNo.length();
			String startNoStren = startNo.toString().substring(0, 1);
			String startNoStr = startNo.toString().substring(1, startNo.toString().length());
			int useNo = Integer.parseInt(startNoStr);
			// 获取入库表中发票终止号
			String endNo = fis.getInvoiceEndno();
			Integer lene = endNo.length();
			String startNoStrene = endNo.toString().substring(0, 1);
			String startNoStre = endNo.toString().substring(1, endNo.toString().length());
			int useNoe = Integer.parseInt(startNoStre);

			// 获取入库表中发票已用号
			String invoiceUsedno = fis.getInvoiceUsedno();
			Integer lenu = invoiceUsedno.length();
			String startNoStrenu = invoiceUsedno.toString().substring(0, 1);
			String startNoStru = invoiceUsedno.toString().substring(1, invoiceUsedno.toString().length());
			int useNoue = Integer.parseInt(startNoStru);
					if(invoiceUsedno!=null){
						if(parseInt<=(useNoe-useNoue)){
							int i=useNoue+parseInt;
							String substr=i+"";
							int lengths=substr.toString().length();
							for (int j = 0; j < lenu-lengths-1; j++) {
								substr="0"+substr;
							}
							String strAll=startNoStrenu+substr;
							return strAll;
						}else if(parseInt>(useNoe-useNoue)){
							parseInt=parseInt-(useNoe-useNoue);
						}
					}else{
						if(parseInt<=(useNoe-useNo)){
							int i=useNo+parseInt;
							String substr=i+"";
							int lengths=substr.toString().length();
							for (int j = 0; j < lenu-lengths-1; j++) {
								substr="0"+substr;
							}
							String strAll=startNoStrenu+substr;
							return strAll;
						}else if(parseInt>(useNoe-useNo)){
							parseInt=parseInt-(useNoe-useNo);
						}
					}
				}
		return null;
	}

	@Override
	public int sumfinace(String num, FinanceInvoice financeInvoice) {
		String invoiceType = financeInvoice.getInvoiceType();
		List<FinanceInvoiceStorage> fislist = financeInvoiceDao.findMaxStartNo(invoiceType);
		int parseInt=Integer.parseInt(num);
		for (FinanceInvoiceStorage fis : fislist) {
			// 获取入库表中发票开始号
			String startNo = fis.getInvoiceStartno();
			Integer len = startNo.length();
			String startNoStren = startNo.toString().substring(0, 1);
			String startNoStr = startNo.toString().substring(1, startNo.toString().length());
			int useNo = Integer.parseInt(startNoStr);
			// 获取入库表中发票终止号
			String endNo = fis.getInvoiceEndno();
			Integer lene = endNo.length();
			String startNoStrene = endNo.toString().substring(0, 1);
			String startNoStre = endNo.toString().substring(1, endNo.toString().length());
			int useNoe = Integer.parseInt(startNoStre);

			// 获取入库表中发票已用号
			String invoiceUsedno = fis.getInvoiceUsedno();
			Integer lenu = invoiceUsedno.length();
			String startNoStrenu = invoiceUsedno.toString().substring(0, 1);
			String startNoStru = invoiceUsedno.toString().substring(1, invoiceUsedno.toString().length());
			int useNoue = Integer.parseInt(startNoStru);
					if(invoiceUsedno!=null){
						if(parseInt<=(useNoe-useNoue)){
							parseInt=parseInt-(useNoe-useNoue);
							
							return parseInt;
						}else if(parseInt>(useNoe-useNoue)){
							parseInt=parseInt-(useNoe-useNoue);
						}
					}else{
						if(parseInt<=(useNoe-useNo)){
							parseInt=parseInt-(useNoe-useNo);
							return parseInt;
						}else if(parseInt>(useNoe-useNo)){
							parseInt=parseInt-(useNoe-useNo);
						}
					}
				}
		return parseInt;
	}

	@Override
	public String getNum(String invoiceType, String endNum) {
		String prefix = endNum.substring(0, 1);//前缀
		int invoice = Integer.parseInt(endNum.substring(1));
		List<FinanceInvoiceStorage> fislist = financeInvoiceDao.findMaxStartNo(invoiceType);
		List<FinanceInvoiceStorage> list = new ArrayList<FinanceInvoiceStorage>();
		int endNo;
		int num ;
		int a = 0;//发票号所在组中需要获取的数量
		int statNo;
		int useNo;
		for (FinanceInvoiceStorage sto : fislist) {
			endNo = Integer.parseInt(sto.getInvoiceEndno().substring(1));
			if(Math.max(invoice, endNo)==Math.min(invoice, endNo)){
				statNo = Integer.parseInt(sto.getInvoiceUsedno().substring(1))+1;
				a = invoice - statNo;
			}else{
				list.add(sto);
			}
		}
		if(list!=null&&list.size()>0){
			for (FinanceInvoiceStorage fi : list) {
				useNo = Integer.parseInt(fi.getInvoiceUsedno().substring(1))+1;
				a = a + invoice - useNo;
			}
		}
		return String.valueOf(a);
	}
	
	
	
}
