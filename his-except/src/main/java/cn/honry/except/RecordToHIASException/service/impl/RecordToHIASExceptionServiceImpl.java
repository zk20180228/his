package cn.honry.except.RecordToHIASException.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.except.RecordToHIASException.dao.RecordToHIASExceptionDao;
import cn.honry.except.RecordToHIASException.service.RecordToHIASExceptionService;
/**  
 * @Author：hedong
 * @CreateDate：2017-03-14
 * @version 1.0
 * @remark:平台异常信息记录
 */
@Service("recordToHIASExceptionService")
@Transactional
@SuppressWarnings({ "all" })
public class RecordToHIASExceptionServiceImpl implements RecordToHIASExceptionService{
	
	@Autowired
	@Qualifier(value = "recordToHIASExceptionDao")
	private RecordToHIASExceptionDao recordToHIASExceptionDao;
	@Override
	public RecordToHIASException get(String arg0) {
		return null;
	}
	@Override
	public void removeUnused(String arg0) {
	}
	@Override
	public void saveOrUpdate(RecordToHIASException arg0) {
	}
	@Override
	public void saveExceptionInfo(RecordToHIASException hiasException, Exception e) {
		recordToHIASExceptionDao.saveExceptionInfo(hiasException,e);
	}
	

	

}
