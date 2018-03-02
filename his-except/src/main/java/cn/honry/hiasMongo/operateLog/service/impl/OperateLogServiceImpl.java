package cn.honry.hiasMongo.operateLog.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import cn.honry.hiasMongo.operateLog.dao.OperateLogDao;
import cn.honry.hiasMongo.operateLog.service.OperateLogService;
@Service("operateLogService")
@Transactional
@SuppressWarnings({ "all" })
public class OperateLogServiceImpl implements OperateLogService{
	@Autowired
	@Qualifier(value = "operateLogDao")
	private OperateLogDao operateLogDao;
	@Override
	public JSONArray getOperateLogByPage(String page, String rows, String operateName, String ud) {
		return operateLogDao.getOperateLogByPage(page, rows, operateName, ud);
	}
	
	@Override
	public Long getTotalByPage(String operateName) {
		return operateLogDao.getTotalByPage(operateName);
	}

}
