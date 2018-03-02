package cn.honry.inner.inpatient.inpatientBedInfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.inner.inpatient.inpatientBedInfo.dao.InpatientBedInfoInInterDAO;
import cn.honry.inner.inpatient.inpatientBedInfo.service.InpatientBedInfoInInterService;

@Service("inpatientBedInfoInInterService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientBedInfoInInterServiceImpl implements InpatientBedInfoInInterService{
	@Autowired
	private InpatientBedInfoInInterDAO inpatientBedInfoDAO;
	

	@Override
	public InpatientBedinfo get(String id) {
		return inpatientBedInfoDAO.get(id);
	}
	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientBedinfo arg0) {
		
	}
	
	@Override
	public InpatientInfo queryByMedical(String medicalNo) {
		return inpatientBedInfoDAO.queryByMedical(medicalNo);
	}

}
