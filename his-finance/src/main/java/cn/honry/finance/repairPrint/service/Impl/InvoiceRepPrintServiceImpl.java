package cn.honry.finance.repairPrint.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.FinanceInvoiceInfoNow;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.finance.repairPrint.dao.InvoiceRepPrintDao;
import cn.honry.finance.repairPrint.service.InvoiceRepPrintService;
import cn.honry.finance.repairPrint.vo.InvoiceRepPrintVo;
@Service("invoiceRepPrintService")
@Transactional
@SuppressWarnings({ "all" })
public class InvoiceRepPrintServiceImpl implements InvoiceRepPrintService {
	
	@Autowired
	@Qualifier(value = "invoiceRepPrintDao")
	private InvoiceRepPrintDao invoiceRepPrintDao;
	
	@Override
	public FinanceInvoiceInfoNow get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(FinanceInvoiceInfoNow arg0) {
		
	}

	@Override
	public List<InvoiceRepPrintVo> queryInvoiceInfo(InvoiceRepPrintVo invoiceRepPrintVo) {
		return invoiceRepPrintDao.queryInvoiceInfo(invoiceRepPrintVo);
	}

	@Override
	public List<InpatientBalanceListNow> queryBalanceList(InpatientBalanceList inpatientBalanceList) {
		return invoiceRepPrintDao.queryBalanceList(inpatientBalanceList);
	}

}
