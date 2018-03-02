package cn.honry.inner.inpatient.inpatientBedInfoNow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientBedinfo;
import cn.honry.base.bean.model.InpatientBedinfoNow;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.inner.inpatient.inpatientBedInfo.dao.InpatientBedInfoInInterDAO;
import cn.honry.inner.inpatient.inpatientBedInfo.service.InpatientBedInfoInInterService;
import cn.honry.inner.inpatient.inpatientBedInfoNow.dao.InpatientBedInfoNowInInterDAO;
import cn.honry.inner.inpatient.inpatientBedInfoNow.service.InpatientBedInfoNowInInterService;

@Service("inpatientBedInfoNowInInterService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientBedInfoNowInInterServiceImpl implements InpatientBedInfoNowInInterService{
	@Autowired
	private InpatientBedInfoNowInInterDAO inpatientBedInfoNowDAO;
	

	@Override
	public InpatientBedinfoNow get(String id) {
		return inpatientBedInfoNowDAO.get(id);
	}
	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientBedinfoNow arg0) {
		
	}
	
	@Override
	public InpatientInfoNow queryByMedical(String medicalNo) {
		return inpatientBedInfoNowDAO.queryByMedical(medicalNo);
	}

}
