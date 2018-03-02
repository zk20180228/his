package cn.honry.oa.activiti.delegateHistory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaDelegateHistory;
import cn.honry.oa.activiti.delegateHistory.dao.OaDelegateHistoryDao;
import cn.honry.oa.activiti.delegateHistory.service.OaDelegateHistoryService;

/**
 * 代理记录service实现类
 * @author luyanshou
 *
 */
@Service("oaDelegateHistoryService")
@Transactional
@SuppressWarnings({ "all" })
public class OaDelegateHistoryServiceImpl implements OaDelegateHistoryService {

	@Autowired
	@Qualifier(value = "oaDelegateHistoryDao")
	private OaDelegateHistoryDao oaDelegateHistoryDao;
	
	public void setOaDelegateHistoryDao(OaDelegateHistoryDao oaDelegateHistoryDao) {
		this.oaDelegateHistoryDao = oaDelegateHistoryDao;
	}

	@Override
	public OaDelegateHistory get(String arg0) {
		return oaDelegateHistoryDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {

	}

	@Override
	public void saveOrUpdate(OaDelegateHistory arg0) {
		oaDelegateHistoryDao.save(arg0);
	}

	public void saveOrUpdateList(List<OaDelegateHistory> list){
		oaDelegateHistoryDao.saveOrUpdateList(list);
	}
}
