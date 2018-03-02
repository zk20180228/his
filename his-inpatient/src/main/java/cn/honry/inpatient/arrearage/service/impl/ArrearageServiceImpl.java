package cn.honry.inpatient.arrearage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.inpatient.arrearage.dao.ArrearageDAO;
import cn.honry.inpatient.arrearage.service.ArrearageService;

@Service("arrearageService")
@Transactional
public class ArrearageServiceImpl implements ArrearageService{
	
	@Autowired
	private ArrearageDAO arrearageDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientInPrepay get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientInPrepay entity) {
		
	}

	@Override
	public List<InpatientInPrepay> QueryInpatientInPrepay(String inpatientNo,
			String outDate,String inDate) throws Exception {
		List<InpatientInPrepay> list=arrearageDAO.QueryInpatientInPrepay(inpatientNo, outDate,inDate);
		return list;
	}

	@Override
	public List<InpatientFeeInfo> inpatientNo(String inpatientNo,
			String outDate, String inDate) throws Exception {
		List<InpatientFeeInfo> list=arrearageDAO.QueryInpatientFeeInfo(inpatientNo, outDate, inDate);
		return list;
	}

	@Override
	public List<InpatientInPrepay> QueryprepayCost(String inpatientNo,
			String outDate, String inDate) throws Exception {
		List<InpatientInPrepay> list=arrearageDAO.QueryprepayCost(inpatientNo, outDate,inDate);
		return list;
	}

	@Override
	public List<InpatientFeeInfo> QuerytotCost(String inpatientNo,
			String outDate, String inDate) throws Exception {
		List<InpatientFeeInfo> list=arrearageDAO.QuerytotCost(inpatientNo, outDate,inDate);
		return list;
	}

	@Override
	public List<InpatientInPrepay> QueryInpatientInPrepayID(String id) throws Exception {
		List<InpatientInPrepay> list=arrearageDAO.QueryInpatientInPrepayID(id);
		return list;
	}

	@Override
	public void SaveInpatientBalancePay(InpatientBalancePay inpatientBalancePay) {
		
	}

	@Override
	public List<InpatientFeeInfo> QueryID(String id) throws Exception {
		List<InpatientFeeInfo> list=arrearageDAO.QueryID(id);
		return list;
	}

}
