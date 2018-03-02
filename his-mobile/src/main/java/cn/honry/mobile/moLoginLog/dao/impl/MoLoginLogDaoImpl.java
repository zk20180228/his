package cn.honry.mobile.moLoginLog.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.LoginLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.mobile.moLoginLog.dao.MoLoginLogDao;
import cn.honry.utils.DateUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("moLoginLogDao")
public class MoLoginLogDaoImpl implements MoLoginLogDao{
	/** 
	* mongodb基础类用于dao的创建
	*/ 
	private MongoBasicDao mbDao = new MongoBasicDao();
	
	/** 
	* @Description: 分页查询登录日志 
	* @param page
	* @param rows
	* @param userLogin
	* @param userMap
	* @throws Exception
	* @return JSONArray    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@Override
	public JSONArray getLoginLogByPage(String page, String rows,
			LoginLog userLogin,String userMap) throws Exception{
		if(StringUtils.isNotBlank(userMap)){//初次模糊查询初始化用户
			userMap = userMap.replaceAll("\"", "").replace("{", "").replace("}", "");
			List<String> uList = Arrays.asList(userMap.split(","));
			List<DBObject> userList = new ArrayList<DBObject>();
			for(String s : uList){
				String uId = s.split(":")[0];
				String uName = s.split(":")[1];
				BasicDBObject oObject = new BasicDBObject();
				oObject.append("userId", uId);
				oObject.append("userName", uName);
				userList.add(oObject);
			}
			// 删除mongdb中的登陆用户表
			mbDao.deleteData("M_LOGIN_USER");
			//将所有的用户信息初始化保存到mongdb表中
			mbDao.insertDataByList("M_LOGIN_USER", userList);
		}
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject userDBObject = new BasicDBObject();
		BasicDBList userNameList = new BasicDBList(); 
		if(StringUtils.isNotBlank(userLogin.getUserId())){//模糊查询 正则表达式
			Pattern pattern = Pattern.compile("^.*"+userLogin.getUserId()+".*$", Pattern.CASE_INSENSITIVE);
			userNameList.add(new BasicDBObject("userName",pattern));
			userNameList.add(new BasicDBObject("userId",pattern));
			userDBObject.put("$or", userNameList);
			//根据用户名或者用户账号查询用户信息
			DBCursor userIdPattern = mbDao.findAlldata("M_LOGIN_USER", userDBObject);
			//由于mongdb的M_SYS_USERLOGIN表中没有用户name字段，
			//所以根据用户name查询时首先根据姓名在M_LOGIN_USER表中查询出人员信息后，获取该人员的account再与M_SYS_USERLOGIN连表查询得到最终的查询结果
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userId = dbCursor.get("userId").toString();
				//获得用户的账号信息
				userIdObject.put("LOGIN_USERID", userId);
				condList.add(userIdObject);
			}
			userIdPattern.close();
			if(condList.size()<=0||condList==null){
				condList.add(new BasicDBObject("LOGIN_USERID", ""));
			}
			//根据人员的account
			bdbObject.put("$or", condList);
		}
		Integer r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):20;
		Integer p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		if(userLogin.getLoginTime()!=null){
			Pattern pattern = Pattern.compile("^.*"+DateUtils.formatDateY_M_D(userLogin.getLoginTime())+".*$", Pattern.CASE_INSENSITIVE);
			//根据人员的登录时间
			bdbObject.put("LOGIN_TIME", pattern);
		}
		//M_SYS_USERLOGIN与M_LOGIN_USER连表按条件查询人员信息
		DBCursor cursor = mbDao.findAllDataSortBy("M_SYS_USERLOGIN", "LOGIN_TIME", bdbObject, r, p);
		JSONArray jsonArray = new JSONArray();
		while(cursor.hasNext()){
			DBObject dbCursor = cursor.next();
			ObjectId id = (ObjectId) dbCursor.get("_id");
			String loginLog = dbCursor.toString();
			JSONObject loginLogObject = JSONObject.fromObject(loginLog);
			loginLogObject.remove("_id");
			loginLogObject.put("id", id.toString());
			jsonArray.add(loginLogObject);
		}
		cursor.close();
		return jsonArray;
	}
	/** 
	* @Description: 登录日志总条数
	* @param userLogin
	* @throws Exception
	* @return Long    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@Override
	public Long getTotalByPage(LoginLog userLogin) throws Exception {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject userDBObject = new BasicDBObject();
		BasicDBList userNameList = new BasicDBList(); 
		if(StringUtils.isNotBlank(userLogin.getUserId())){//模糊查询
			Pattern pattern = Pattern.compile("^.*"+userLogin.getUserId()+".*$", Pattern.CASE_INSENSITIVE);
			userNameList.add(new BasicDBObject("userName",pattern));
			userNameList.add(new BasicDBObject("userId",pattern));
			userDBObject.put("$or", userNameList);
			//根据用户名或者用户账号查询用户信息
			DBCursor userIdPattern = mbDao.findAlldata("M_LOGIN_USER", userDBObject);
			//由于mongdb的M_SYS_USERLOGIN表中没有用户name字段，
			//所以根据用户name查询时首先根据姓名在M_LOGIN_USER表中查询出人员信息后，获取该人员的account再与M_SYS_USERLOGIN连表查询得到最终的查询结果
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userId = dbCursor.get("userId").toString();
				//获得用户的账号信息
				userIdObject.put("LOGIN_USERID", userId);
				condList.add(userIdObject);
			}
			userIdPattern.close();
			if(condList.size()<=0||condList==null){
				condList.add(new BasicDBObject("LOGIN_USERID", ""));
			}
			//根据人员的account
			bdbObject.put("$or", condList);
		}
		if(userLogin.getLoginTime()!=null){
			Pattern pattern = Pattern.compile("^.*"+DateUtils.formatDateY_M_D(userLogin.getLoginTime())+".*$", Pattern.CASE_INSENSITIVE);
			//根据人员的登录时间
			bdbObject.put("LOGIN_TIME", pattern);
		}
		//M_SYS_USERLOGIN与M_LOGIN_USER连表按条件查询人员信息
		Long pageNumber = mbDao.findAllCountBy("M_SYS_USERLOGIN", bdbObject);
		return pageNumber;
	}
	/** 
	* @Description:  保存用户登录信息到mongdb中
	* @param document
	* @throws Exception
	* @return void    返回类型 
	* @author zx 
	* @date 2017年7月3日
	*/
	@Override
	public void insertLoginByMongo(Document document) throws Exception{
		mbDao.insertData("M_SYS_USERLOGIN", document);
	}

}
