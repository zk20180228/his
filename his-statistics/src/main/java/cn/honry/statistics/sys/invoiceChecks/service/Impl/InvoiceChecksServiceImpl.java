package cn.honry.statistics.sys.invoiceChecks.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.statistics.sys.invoiceChecks.dao.InvoiceChecksDAO;
import cn.honry.statistics.sys.invoiceChecks.service.InvoiceChecksService;
import cn.honry.statistics.sys.invoiceChecks.vo.VinpatirntInfoBalance;

@Service("invoiceChecksService")
@Transactional
@SuppressWarnings({ "all" })
public class InvoiceChecksServiceImpl implements InvoiceChecksService{
	@Autowired
	@Qualifier(value = "invoiceChecksDAO")
	private InvoiceChecksDAO invoiceChecksDAO;

	@Override
	public VinpatirntInfoBalance get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(VinpatirntInfoBalance arg0) {
		
	}

	@Override
	public List<VinpatirntInfoBalance> queryVinpatirntInfoBalance(
			String medicalrecordId,String invoiceNo) throws Exception {
		return invoiceChecksDAO.queryVinpatirntInfoBalance(medicalrecordId,invoiceNo);
	}

	@Override
	public List<InpatientInfoNow> queryInfolist(String medicalrecordId,String idCard) throws Exception {
		return invoiceChecksDAO.queryInfolist(medicalrecordId,idCard);
	}

	@Override
	public List<InpatientBalanceHeadNow> queryBalanceHead(String inpatientNo) throws Exception {
		return invoiceChecksDAO.queryBalanceHead(inpatientNo);
	}

	@Override
	public List<VinpatirntInfoBalance> queryVinpatirntInfoBalancepages(
			String medicalrecordId, String invoiceNo, String page, String rows) throws Exception {
		return invoiceChecksDAO.queryVinpatirntInfoBalancepages(medicalrecordId, invoiceNo, page, rows);
	}

	@Override
	public int queryVinpatirntInfoBalanceTotal(String medicalrecordId,
			String invoiceNo) throws Exception {
		return invoiceChecksDAO.queryVinpatirntInfoBalanceTotal(medicalrecordId, invoiceNo);
	}

}
