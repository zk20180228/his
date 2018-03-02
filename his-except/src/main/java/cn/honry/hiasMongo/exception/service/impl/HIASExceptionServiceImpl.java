package cn.honry.hiasMongo.exception.service.impl;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.RecordToHIASException;
import cn.honry.hiasMongo.exception.dao.HIASExceptionDao;
import cn.honry.hiasMongo.exception.service.HIASExceptionService;
/**  
 * @Author：hedong
 * @CreateDate：2017-03-23
 * @version 1.0
 * @remark:平台异常信息记录
 */
@Service("hiasExceptionService")
@Transactional
@SuppressWarnings({ "all" })
public class HIASExceptionServiceImpl implements HIASExceptionService{
	@Autowired
	@Qualifier(value = "hiasExceptionDao")
	private HIASExceptionDao hiasExceptionDao;
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
	public void saveExceptionInfoToMongo(RecordToHIASException hiasException,
			Exception e) {
		hiasExceptionDao.saveExceptionInfoToMongo(hiasException,e);
	}
	@Override
	public JSONArray getHIASExceptionByPage(String page, String rows, RecordToHIASException hiasException){
		return hiasExceptionDao.getHIASExceptionByPage(page, rows, hiasException);
	}
	@Override
	public Long getTotalByPage(RecordToHIASException hiasException){
		return hiasExceptionDao.getTotalByPage(hiasException);
	}
	public void startDeal(String id){
		hiasExceptionDao.startDeal(id);
	}
	public void endDeal(String id){
		hiasExceptionDao.endDeal(id);
	}
}
