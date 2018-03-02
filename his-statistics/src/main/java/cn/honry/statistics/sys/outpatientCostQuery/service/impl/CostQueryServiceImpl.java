package cn.honry.statistics.sys.outpatientCostQuery.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessContractunit;
import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.FinanceInvoiceInfo;
import cn.honry.base.bean.model.FinanceInvoicedetail;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.OutpatientFeedetail;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.statistics.sys.outpatientCostQuery.dao.CostQueryDao;
import cn.honry.statistics.sys.outpatientCostQuery.service.CostQueryService;

@Service("costQueryService")
@Transactional
@SuppressWarnings({ "all" })
public class CostQueryServiceImpl implements CostQueryService{

	@Autowired
	@Qualifier(value = "costQueryDAO")
	private CostQueryDao costQueryDAO;
	
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
	public Map<String, Object> findInvoiceNoSummary(String invoiceNo,String beginTime, String endTime,String page,String rows) throws Exception {
		
		return costQueryDAO.findInvoiceNoSummary(invoiceNo,beginTime,endTime,page,rows);
	}

	@Override
	public Map<String,Object> findInvoiceDetailed(String invoiceNo,String beginTime, String endTime,String page,String rows) throws Exception {
		
		return costQueryDAO.findInvoiceDetailed(invoiceNo,beginTime,endTime,page,rows);
	}

	@Override
	public Map<String,Object> findCostDetailed(String invoiceNo,String beginTime, String endTime,String page,String rows) throws Exception {
		
		return costQueryDAO.findCostDetailed(invoiceNo,beginTime,endTime,page,rows);
	}

	@Override
	public List<MinfeeStatCode> itemFunction() {
		
		return costQueryDAO.itemFunction();
	}



}
