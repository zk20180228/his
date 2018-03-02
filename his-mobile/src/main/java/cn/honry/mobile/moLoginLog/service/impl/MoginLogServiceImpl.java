package cn.honry.mobile.moLoginLog.service.impl;

import net.sf.json.JSONArray;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.LoginLog;
import cn.honry.mobile.moLoginLog.dao.MoLoginLogDao;
import cn.honry.mobile.moLoginLog.service.MoLoginLogService;
@Service("moLoginLogService")
public class MoginLogServiceImpl implements MoLoginLogService{
	/** 
	* 登录日志dao 
	*/ 
	@Autowired
	@Qualifier(value = "moLoginLogDao")
	private MoLoginLogDao moLoginLogDao;

	/** 
	* @Description:  保存用户登录信息到mongdb中
	* @param document
	* @throws Exception
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@Override
	public void insertLoginByMongo(Document document) throws Exception {
		moLoginLogDao.insertLoginByMongo(document);
	}
	/** 
	* @Description: 分页查询登录日志
	* @param request
	* @param response
	* @param page 当前页
	* @param rows 每页显示的记录数
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@Override
	public JSONArray getLoginLogByPage(String page, String rows, LoginLog userLogin, String userMap) throws Exception {
		return moLoginLogDao.getLoginLogByPage(page, rows, userLogin, userMap);
	}

	/** 
	* @Description: 登录日志总条数
	* @param userLogin 登录日志对象
	* @throws Exception
	* @return Long    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@Override
	public Long getTotalByPage(LoginLog userLogin) throws Exception {
		return moLoginLogDao.getTotalByPage(userLogin);
	}
	
}
