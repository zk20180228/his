package cn.honry.hiasMongo.loginLog.service.impl;

import java.util.Map;

import net.sf.json.JSONArray;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.UserLogin;
import cn.honry.hiasMongo.loginLog.dao.LoginLogDao;
import cn.honry.hiasMongo.loginLog.service.LoginLogService;

import com.mongodb.BasicDBObject;
@Service("loginLogService")
@Transactional
@SuppressWarnings({ "all" })
public class LoginLogServiceImpl implements LoginLogService{
	@Autowired
	@Qualifier(value = "loginLogDao")
	private LoginLogDao loginLogDao;
	@Override
	public UserLogin get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
	}

	@Override
	public void saveOrUpdate(UserLogin arg0) {
	}

	@Override
	public JSONArray getLoginLogByPage(String page, String rows,
			UserLogin userLogin,String ud) {
		return loginLogDao.getLoginLogByPage(page, rows, userLogin, ud);
	}

	@Override
	public Long getTotalByPage(UserLogin userLogin) {
		return loginLogDao.getTotalByPage(userLogin);
	}
	
	public void insertLoginByMongo(Document document) {
		loginLogDao.insertLoginByMongo(document);
	}

	
}
