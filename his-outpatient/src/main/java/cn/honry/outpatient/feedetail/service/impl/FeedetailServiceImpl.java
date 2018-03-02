package cn.honry.outpatient.feedetail.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OutpatientFeedetailNow;
import cn.honry.outpatient.feedetail.dao.FeedetailDAO;
import cn.honry.outpatient.feedetail.service.FeedetailService;

@Service("feedetailService")
@Transactional
@SuppressWarnings({ "all" })
public class FeedetailServiceImpl implements FeedetailService{

	@Resource
	private FeedetailDAO feedetailDAO;

	@Override
	public void removeUnused(String id) {
	}
	
	@Override
	public void saveOrUpdate(OutpatientFeedetailNow entity) {
	}

	@Override
	public OutpatientFeedetailNow get(String id) {
		return feedetailDAO.get(id);
	}

}
