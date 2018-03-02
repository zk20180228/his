package cn.honry.inpatient.outBalance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.inpatient.outBalance.dao.InpatientInPrepayDAO;
import cn.honry.inpatient.outBalance.service.InpatientInPrepayService;

@Service("inpatientInPrepayService")
@Transactional
public class InpatientInPrepayServiceImpl implements InpatientInPrepayService{
	
	@Autowired
	private InpatientInPrepayDAO inpatientInPrepayDAO;
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
	public List<InpatientInPrepay> queryprepayCost(String inpatientNo,
			String outDate, String inDate) {
		return inpatientInPrepayDAO.queryprepayCost(inpatientNo, outDate, inDate);
	}
}
