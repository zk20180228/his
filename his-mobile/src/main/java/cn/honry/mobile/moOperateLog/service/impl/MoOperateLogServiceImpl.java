package cn.honry.mobile.moOperateLog.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.mobile.moOperateLog.service.MoOperateLogService;

@Service("moOperateLogService")
public class MoOperateLogServiceImpl implements MoOperateLogService{
	
	@Resource
	private MoOperateLogDao moOperateLogDao;
	
	@Override
	public Long getTotalByPage(String queryName) throws Exception {
		return moOperateLogDao.getTotalByPage(queryName);
	}

	@Override
	public JSONArray getOperateLogByPage(String page, String rows,
			String queryName) throws Exception {
		return moOperateLogDao.getOperateLogByPage(page, rows, queryName);
	}

	@Override
	public void saveSysOperateLogToMongo(MSysOperateLog mSysOperateLog) throws Exception {
		moOperateLogDao.saveSysOperateLogToMongo(mSysOperateLog);
		
	}

	@Override
	public MSysOperateLog getOperateLogById(String id) throws Exception {
		return moOperateLogDao.getOperateLogById(id);
	}

	@Override
	public void updateUser(Map<String, String> map) throws Exception {
		moOperateLogDao.updateUser(map);
		
	}

}
