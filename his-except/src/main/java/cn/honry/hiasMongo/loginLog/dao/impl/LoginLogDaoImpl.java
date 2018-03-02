package cn.honry.hiasMongo.loginLog.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.UserLogin;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.loginLog.dao.LoginLogDao;
import cn.honry.utils.DateUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("loginLogDao")
@SuppressWarnings({ "all" })
public class LoginLogDaoImpl extends HibernateEntityDao<UserLogin> implements LoginLogDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	private MongoBasicDao mbDao = new MongoBasicDao();
	@Override
	public JSONArray getLoginLogByPage(String page, String rows,
			UserLogin userLogin,String ud) {
		if(StringUtils.isNotBlank(ud)){
			ud = ud.replaceAll("\"", "").replace("{", "").replace("}", "");
			List<String> uList = Arrays.asList(ud.split(","));
			List<DBObject> userList = new ArrayList<DBObject>();
			for(String s : uList){
				String uId = s.split(":")[0];
				String uName = s.split(":")[1];
				BasicDBObject oObject = new BasicDBObject();
				oObject.append("userId", uId);
				oObject.append("userName", uName);
				userList.add(oObject);
			}
			mbDao.deleteData("LOGIN_USER");
			mbDao.insertDataByList("LOGIN_USER", userList);
		}
		
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject userDBObject = new BasicDBObject();
		if(StringUtils.isNotBlank(userLogin.getUserId())){//模糊查询
			Pattern pattern = Pattern.compile("^.*"+userLogin.getUserId()+".*$", Pattern.CASE_INSENSITIVE);
			userDBObject.put("userName",pattern);
			DBCursor userIdPattern = mbDao.findAlldata("LOGIN_USER", userDBObject);
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userId = dbCursor.get("userId").toString();
				userIdObject.put("LOGIN_USERID", userId);
				condList.add(userIdObject);
			}
			userIdPattern.close();
			if(condList.size()<=0||condList==null){
				condList.add(new BasicDBObject("LOGIN_USERID", ""));
			}
			bdbObject.put("$or", condList);
		}
		Integer r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):20;
		Integer p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		if(userLogin.getLoginTime()!=null){
			Pattern pattern = Pattern.compile("^.*"+DateUtils.formatDateY_M_D(userLogin.getLoginTime())+".*$", Pattern.CASE_INSENSITIVE);
			bdbObject.put("LOGIN_TIME", pattern);
		}
		DBCursor cursor = mbDao.findAllDataSortBy("T_SYS_USERLOGIN", "LOGIN_TIME", bdbObject, r, p);
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
	@Override
	public Long getTotalByPage(UserLogin userLogin) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject userDBObject = new BasicDBObject();
		if(StringUtils.isNotBlank(userLogin.getUserId())){//模糊查询
			Pattern pattern = Pattern.compile("^.*"+userLogin.getUserId()+".*$", Pattern.CASE_INSENSITIVE);
			userDBObject.put("userName",pattern);
			DBCursor userIdPattern = mbDao.findAlldata("LOGIN_USER", userDBObject);
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userId = dbCursor.get("userId").toString();
				userIdObject.put("LOGIN_USERID", userId);
				condList.add(userIdObject);
			}
			userIdPattern.close();
			if(condList.size()<=0||condList==null){
				condList.add(new BasicDBObject("LOGIN_USERID", ""));
			}
			bdbObject.put("$or", condList);
		}
		Long pageNumber = mbDao.findAllCountBy("T_SYS_USERLOGIN", bdbObject);
		return pageNumber;
	}
	@Override
	public void insertLoginByMongo(Document document) {
		mbDao.insertData("T_SYS_USERLOGIN", document);
	}

}
