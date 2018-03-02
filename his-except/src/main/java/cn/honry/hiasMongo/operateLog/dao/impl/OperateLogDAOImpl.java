package cn.honry.hiasMongo.operateLog.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.hiasMongo.operateLog.dao.OperateLogDao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("operateLogDao")
@SuppressWarnings({"all"})
public class OperateLogDAOImpl implements OperateLogDao{
	private MongoBasicDao mbDao = new MongoBasicDao();
	
	@Override
	public JSONArray getOperateLogByPage(String page, String rows, String operateName, String ud) {
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
			mbDao.deleteData("OPERATRE_USER");
			mbDao.insertDataByList("OPERATRE_USER", userList);
		}
		
		
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject userDBObject = new BasicDBObject();
		if(StringUtils.isNotBlank(operateName)){//模糊查询
			Pattern pattern = Pattern.compile("^.*"+operateName+".*$", Pattern.CASE_INSENSITIVE);
			userDBObject.put("userName",pattern);
			DBCursor userIdPattern = mbDao.findAlldata("OPERATRE_USER", userDBObject);
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userId = dbCursor.get("userId").toString();
				userIdObject.put("LOG_USERID", userId);
				condList.add(userIdObject);
			}
			if(condList.size()<=0||condList==null){
				condList.add(new BasicDBObject("LOG_USERID", ""));
			}
			bdbObject.put("$or", condList);
		}
		Integer r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):20;
		Integer p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		JSONArray jsonArray = new JSONArray();
		DBCursor cursor = mbDao.findAllDataSortBy("T_SYS_USEROPERATION", "LOG_TIME", bdbObject, r, p);
		while(cursor.hasNext()){
			DBObject dbCursor = cursor.next(); 
			ObjectId id = (ObjectId) dbCursor.get("_id");
			JSONObject operateObject = JSONObject.fromObject(dbCursor);
			operateObject.remove("_id");
			operateObject.put("id", id.toString());
			jsonArray.add(operateObject);
		}
		return jsonArray;
	}

	@Override
	public Long getTotalByPage(String operateName) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject userDBObject = new BasicDBObject();
		if(StringUtils.isNotBlank(operateName)){//模糊查询
			Pattern pattern = Pattern.compile("^.*"+operateName+".*$", Pattern.CASE_INSENSITIVE);
			userDBObject.put("userName",pattern);
			DBCursor userIdPattern = mbDao.findAlldata("OPERATRE_USER", userDBObject);
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userId = dbCursor.get("userId").toString();
				userIdObject.put("LOG_USERID", userId);
				condList.add(userIdObject);
			}
			if(condList.size()<=0||condList==null){
				condList.add(new BasicDBObject("LOG_USERID", ""));
			}
			bdbObject.put("$or", condList);
		}
		return mbDao.findAllCountBy("T_SYS_USEROPERATION", bdbObject);
	}

}
