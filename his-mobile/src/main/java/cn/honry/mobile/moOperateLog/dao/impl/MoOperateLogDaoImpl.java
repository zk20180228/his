package cn.honry.mobile.moOperateLog.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MSysOperateLog;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.mobile.moOperateLog.dao.MoOperateLogDao;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("moOperateLogDao")
public class MoOperateLogDaoImpl implements MoOperateLogDao{
	 public static final String LOGACTIONINSERT="添加";//操作日志-操作行为-添加
	 public static final String LOGACTIONUPDATE="修改";//操作日志-操作行为-修改
	 public static final String LOGACTIONDELETE="删除";//操作日志-操作行为-删除	
	
	private MongoBasicDao mbDao = new MongoBasicDao();

	@Override
	public JSONArray  getOperateLogByPage(String page, String rows, String queryName) throws Exception{
		Integer r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):20;
		Integer p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		JSONArray jsonArray = new JSONArray();
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject bdbObject = new BasicDBObject();
		if(StringUtils.isNotBlank(queryName)){//模糊查询
			BasicDBObject userDBObject = new BasicDBObject();
			Pattern pattern = Pattern.compile("^.*"+queryName+".*$", Pattern.CASE_INSENSITIVE);
			userDBObject.put("userName",pattern);
			DBCursor userIdPattern = mbDao.findAlldata("M_OPERATELOG_USER", userDBObject);
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userAccount = dbCursor.get("userAccount").toString();
				userIdObject.put("log_account", userAccount);
				condList.add(userIdObject);
			}
			BasicDBObject userIdObject1 = new BasicDBObject();
			userIdObject1.put("log_account", pattern);
			condList.add(userIdObject1);
			bdbObject.put("$or", condList);
		}
		
		DBObject dbCursor;
		DBCursor cursor = mbDao.findAllDataSortBy("M_SYS_OPERATELOG", "LOG_TIME", bdbObject, r, p);
		while(cursor.hasNext()){
			dbCursor = cursor.next(); 
			ObjectId id = (ObjectId) dbCursor.get("_id");
			JSONObject operateObject = JSONObject.fromObject(dbCursor);
			operateObject.remove("_id");
			operateObject.put("log_id", id.toString());
			jsonArray.add(operateObject);
		}
		return jsonArray;
	}

	@Override
	public Long getTotalByPage(String queryName) throws Exception{
		BasicDBList condList = new BasicDBList(); 
		BasicDBObject bdbObject = new BasicDBObject();
		if(StringUtils.isNotBlank(queryName)){//模糊查询
			BasicDBObject userDBObject = new BasicDBObject();
			Pattern pattern = Pattern.compile("^.*"+queryName+".*$", Pattern.CASE_INSENSITIVE);
			userDBObject.put("userName",pattern);
			DBCursor userIdPattern = mbDao.findAlldata("OPERATRE_USER", userDBObject);
			while(userIdPattern.hasNext()){
				DBObject dbCursor = userIdPattern.next();
				BasicDBObject userIdObject = new BasicDBObject();
				String userId = dbCursor.get("userId").toString();
				userIdObject.put("log_account", userId);
				condList.add(userIdObject);
			}
			BasicDBObject userIdObject1 = new BasicDBObject();
			userIdObject1.put("log_account", pattern);
			condList.add(userIdObject1);
			bdbObject.put("$or", condList);
		}
		return mbDao.findAllCountBy("M_SYS_OPERATELOG", bdbObject);
	}

	@Override
	public void saveSysOperateLogToMongo(MSysOperateLog mSysOperateLog) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.put("log_action",mSysOperateLog.getLog_action());
		bdObject.put("log_account",account);
		bdObject.put("log_menu_id",mSysOperateLog.getLog_menu_id());
		bdObject.put("log_sql",mSysOperateLog.getLog_sql());
		bdObject.put("log_table",mSysOperateLog.getLog_table());
		bdObject.put("log_target_id",mSysOperateLog.getLog_target_id());
		bdObject.put("log_time",sf.format(new Date()));
		bdObject.put("log_description",mSysOperateLog.getLog_description());
		mbDao.addData("M_SYS_OPERATELOG", bdObject);
	}

	@Override
	public MSysOperateLog getOperateLogById(String id) throws Exception{
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("_id", new ObjectId(id));
		DBCursor cursor = mbDao.findAlldata("M_SYS_OPERATELOG", bdObject);
		DBObject dbCursor;
		List<MSysOperateLog> list=new ArrayList<MSysOperateLog>();
		
		while(cursor.hasNext()){
			 MSysOperateLog voOne=new MSysOperateLog();
			 dbCursor = cursor.next();
			 voOne.setLog_action((String)dbCursor.get("log_action"));
			 voOne.setLog_account((String)dbCursor.get("log_account"));
			 voOne.setLog_menu_id((String)dbCursor.get("log_menu_id"));
			 voOne.setLog_table((String)dbCursor.get("log_table"));
			 voOne.setLog_sql((String)dbCursor.get("log_sql"));
			 voOne.setLog_target_id((String)dbCursor.get("log_target_id"));
			 voOne.setLog_time((String)dbCursor.get("log_time"));
			 voOne.setLog_description((String)dbCursor.get("log_description"));
			list.add(voOne);
		}
		if(list.size()>0){
			return list.get(0);
		}
		return new MSysOperateLog();
	}

	@Override
	public void updateUser(Map<String, String> userMap) throws Exception{
		//更新用户数据
		if(userMap.size()>0){
			List<DBObject> userList = new ArrayList<DBObject>();
			for(String key : userMap.keySet()){
				BasicDBObject oneObject = new BasicDBObject();
				String userAccount = key;
				String userName = userMap.get(key);
				oneObject.append("userAccount", userAccount);
				oneObject.append("userName", userName);
				userList.add(oneObject);
			}
			mbDao.deleteData("M_OPERATELOG_USER");
			mbDao.insertDataByList("M_OPERATELOG_USER", userList);
		}
		
	}
}
