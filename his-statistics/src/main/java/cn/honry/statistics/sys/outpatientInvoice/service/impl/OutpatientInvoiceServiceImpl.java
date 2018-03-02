package cn.honry.statistics.sys.outpatientInvoice.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.statistics.sys.outpatientInvoice.dao.OutpatientInvoiceDao;
import cn.honry.statistics.sys.outpatientInvoice.service.OutpatientInvoiceService;
import cn.honry.statistics.sys.outpatientInvoice.vo.InvoiceInfoVo;
import cn.honry.statistics.sys.outpatientInvoice.vo.OutpatientStaVo;

@Service("invoiceService")
@Transactional
@SuppressWarnings({ "all" })
public class OutpatientInvoiceServiceImpl implements OutpatientInvoiceService{

	@Autowired
	@Qualifier(value = "invoiceDAO")
	private OutpatientInvoiceDao invoiceDAO;

	@Override
	public OutpatientFeedetail get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(OutpatientFeedetail arg0) {
	}

	@Override
	public InvoiceInfoVo queryInvoiceInfoVo(String invoiceNo) {
		
		InvoiceInfoVo infoVoNew = invoiceDAO.queryInvoiceInfoVoNew(invoiceNo);
		if(infoVoNew == null){
			InvoiceInfoVo infoVoOld = invoiceDAO.queryInvoiceInfoVoOld(invoiceNo);
			
			return infoVoOld;
		}
		
		return infoVoNew;
	}

	@Override
	public List<OutpatientStaVo> findOutpatient(String invoiceNo) {
		
		List<OutpatientStaVo> outpatientNew = invoiceDAO.findOutpatientNew(invoiceNo);
		if(outpatientNew==null||outpatientNew.size()<=0){
			List<OutpatientStaVo> outpatientOld = invoiceDAO.findOutpatientOld(invoiceNo);
			
			return outpatientOld;
		}
	
		return outpatientNew;
	}

	

}
