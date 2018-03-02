package cn.honry.mobile.clearData.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.MBlackList;
import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.mobile.clearData.dao.ClearDataDao;
import cn.honry.mobile.clearData.service.ClearDataService;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.utils.ShiroSessionUtils;
@Service("clearDataService")
public class ClearDataServiceImpl implements ClearDataService{
	@Autowired
	private ClearDataDao clearDataDao;

	@Resource
	private MoOperateLogDao moOperateLogDao;
	
	@Override
	public void clear(String flag) throws Exception {
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if("1".equals(flag)){
			clearDataDao.clearSchedule();
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","业务数据初始化",account,"DELETE","M_SCHEDULE","",""));
		}else if("2".equals(flag)){
			clearDataDao.clearNotepad();
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","业务数据初始化",account,"DELETE","M_NOTEPAD","",""));
		}else if("3".equals(flag)){
			clearDataDao.clearAdvice();
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","业务数据初始化",account,"DELETE","M_ADVICE","",""));
		}else if("4".equals(flag)){
			clearDataDao.clearTodo();
			moOperateLogDao.saveSysOperateLogToMongo(new MSysOperateLog("删除","业务数据初始化",account,"DELETE","M_TODO","",""));
		}
	}

	@Override
	public MBlackList get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(MBlackList arg0) {
	}

	@Override
	public void sendMes()  throws Exception {
		clearDataDao.sendMes();
		
	}
	
}
