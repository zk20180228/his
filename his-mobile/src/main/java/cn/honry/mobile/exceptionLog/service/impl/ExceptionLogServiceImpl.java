package cn.honry.mobile.exceptionLog.service.impl;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.mobile.exceptionLog.dao.ExceptionLogDao;
import cn.honry.mobile.exceptionLog.service.ExceptionLogService;
@Service("exceptionLogService")
public class ExceptionLogServiceImpl implements ExceptionLogService{
	@Autowired
	@Qualifier(value = "exceptionLogDao")
	private ExceptionLogDao exceptionLogDao;
	@Override
	public JSONArray getHIASExceptionByPage(String page, String rows,
			RecordToMobileException hiasException) {
		return exceptionLogDao.getHIASExceptionByPage(page, rows, hiasException);
	}
	@Override
	public Long getTotalByPage(RecordToMobileException hiasException) {
		return exceptionLogDao.getTotalByPage(hiasException);
	}
	@Override
	public void saveExceptionInfoToMongo(RecordToMobileException hiasException,
			Exception e) {
		exceptionLogDao.saveExceptionInfoToMongo(hiasException,e);
		
	}
	@Override
	public void startDeal(String id) {
		exceptionLogDao.startDeal(id);
	}
	@Override
	public void endDeal(String id) {
		exceptionLogDao.endDeal(id);
	}

}
