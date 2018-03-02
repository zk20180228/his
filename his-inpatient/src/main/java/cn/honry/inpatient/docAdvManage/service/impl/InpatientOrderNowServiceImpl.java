package cn.honry.inpatient.docAdvManage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.service.BaseService;
import cn.honry.inpatient.docAdvManage.dao.DocAdvManageDAO;
import cn.honry.inpatient.docAdvManage.dao.InpatientOrderNowDAO;
import cn.honry.inpatient.docAdvManage.service.DocAdvManageService;
import cn.honry.inpatient.docAdvManage.service.InpatientOrderNowService;
@Service("inpatientOrderNowService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientOrderNowServiceImpl implements InpatientOrderNowService{
	@Autowired
	@Qualifier(value = "inpatientOrderNowDAO")
	private InpatientOrderNowDAO inpatientOrderNowDAO;

	@Override
	public InpatientOrderNow get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientOrderNow arg0) {
		
	}
}
