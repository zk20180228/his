package cn.honry.inpatient.bill.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inpatient.bill.dao.OutpatientDrugcontrolDrugbillclassDAO;
import cn.honry.inpatient.bill.service.OutpatientDrugcontrolDrugbillclassService;
import cn.honry.inpatient.bill.vo.OutpatientDrugcontrolDrugbillclass;

@Service("outpatientDrugcontrolDrugbillclassService")
@Transactional
@SuppressWarnings({"all"})
public class OutpatientDrugcontrolDrugbillclassServiceImpl implements OutpatientDrugcontrolDrugbillclassService{
	
	@Autowired
	@Qualifier(value = "outpatientDrugcontrolDrugbillclassDAO")
	private OutpatientDrugcontrolDrugbillclassDAO outpatientDrugcontrolDrugbillclassDAO;
	public void setOutpatientDrugcontrolDrugbillclassDAO(
			OutpatientDrugcontrolDrugbillclassDAO outpatientDrugcontrolDrugbillclassDAO) {
		this.outpatientDrugcontrolDrugbillclassDAO = outpatientDrugcontrolDrugbillclassDAO;
	}

	@Override
	public List<OutpatientDrugcontrolDrugbillclass> getWarnLine(
			OutpatientDrugcontrolDrugbillclass vo, String page, String rows) {
		return outpatientDrugcontrolDrugbillclassDAO.getPage(page, rows, vo);
	}

	@Override
	public int getTotalCount(OutpatientDrugcontrolDrugbillclass vo) {
		return  outpatientDrugcontrolDrugbillclassDAO.getTotal(vo);
	}
			
}
