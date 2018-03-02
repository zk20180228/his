package cn.honry.mobile.moLoginLog.dao;

import net.sf.json.JSONArray;

import org.bson.Document;

import cn.honry.base.bean.model.LoginLog;
@SuppressWarnings({"all"})
public interface MoLoginLogDao{
	/** 
	* @Description: 分页查询登录日志 
	* @param page 当前页
	* @param rows 每页显示的记录数
	* @param userLogin 登录日志对象
	* @param userMap 用户账号和姓名Map 用于初始化到mongdb
	* @throws Exception
	* @return JSONArray    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	JSONArray getLoginLogByPage(String page, String rows, LoginLog userLogin, String userMap) throws Exception;
	/** 
	* @Description: 登录日志总条数
	* @param userLogin 登录日志对象
	* @throws Exception
	* @return Long    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	Long getTotalByPage(LoginLog userLogin) throws Exception;
	/** 
	* @Description:  保存用户登录信息到mongdb中
	* @param document 
	* @throws Exception
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	void insertLoginByMongo(Document document) throws Exception;
}
