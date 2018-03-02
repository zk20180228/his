package cn.honry.mobile.exceptionLog.dao.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.RecordToMobileException;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.mobile.exceptionLog.dao.ExceptionLogDao;
import cn.honry.utils.DateUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
@Repository("exceptionLogDao")
public class ExceptionLogDaoImpl implements ExceptionLogDao{
	private MongoBasicDao mbDao = new MongoBasicDao();
	@Override
	public JSONArray getHIASExceptionByPage(String page, String rows,
			RecordToMobileException hiasException) {
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		if(StringUtils.isNotBlank(hiasException.getCodeName())){//模糊查询
			Pattern pattern = Pattern.compile("^.*"+hiasException.getCodeName()+".*$", Pattern.CASE_INSENSITIVE);
			bdObject.put("CODE_NAME",pattern);
		}
		if(StringUtils.isNotBlank(hiasException.getWarnLevel())){
			bdObject.put("WARN_LEVEL",hiasException.getWarnLevel());
		}
		if(StringUtils.isNotBlank(hiasException.getWarnType())){
			bdObject.put("WARN_TYPE",hiasException.getWarnType());
		}
		if(hiasException.getWarnDate()!=null){//开始时间,"$gt"大于
			bdObjectTimeS.put("WARN_DATE",new BasicDBObject("$gte",DateUtils.formatDateY_M_D(hiasException.getWarnDate())));
			condList.add(bdObjectTimeS);
		}
		if(hiasException.getProcessTime()!=null){//截止时间,"$lte"小于
			bdObjectTimeE.put("WARN_DATE",new BasicDBObject("$lte",DateUtils.formatDateY_M_D(hiasException.getProcessTime())));
			condList.add(bdObjectTimeE);
		}
		if(StringUtils.isNotBlank(hiasException.getProcessStatus())){
			bdObject.put("PROCESS_STATUS",hiasException.getProcessStatus());
		}
		if(hiasException.getWarnDate()!=null||hiasException.getProcessTime()!=null){
			bdObject.put("$and", condList);
		}
		Integer r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):20;
		Integer p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
		DBCursor cursor = mbDao.findAllDataSortBy("M_SYS_EXCEPTION", "WARN_DATE", bdObject, r, p);
		JSONArray JsonArray = new JSONArray(); 
		while(cursor.hasNext()){
			DBObject dbCursor = cursor.next();
			ObjectId id = (ObjectId) dbCursor.get("_id");
			String recodeException = dbCursor.toString();
			JSONObject exceptionObject = JSONObject.fromObject(recodeException);
			exceptionObject.remove("_id");
			exceptionObject.put("id", id.toString());
			JsonArray.add(exceptionObject);
		}
		return JsonArray;
	}
	@Override
	public Long getTotalByPage(RecordToMobileException hiasException) {
		BasicDBObject bdObject = new BasicDBObject();
		if(StringUtils.isNotBlank(hiasException.getCodeName())){
			bdObject.put("CODE_NAME",hiasException.getCodeName());
		}
		if(StringUtils.isNotBlank(hiasException.getWarnLevel())){
			bdObject.put("WARN_LEVEL",hiasException.getWarnLevel());
		}
		if(StringUtils.isNotBlank(hiasException.getWarnType())){
			bdObject.put("WARN_TYPE",hiasException.getWarnType());
		}
		if(hiasException.getWarnDate()!=null){
			bdObject.put("WARN_DATE",DateUtils.formatDateY_M_D(hiasException.getWarnDate()));
		}
		if(StringUtils.isNotBlank(hiasException.getProcessStatus())){
			bdObject.put("PROCESS_STATUS",hiasException.getProcessStatus());
		}
		return mbDao.findAllCountBy("M_SYS_EXCEPTION", bdObject);
	}
	@Override
	public void saveExceptionInfoToMongo(RecordToMobileException hiasException,
			Exception e) {
		        //获取异常信息
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				String strException = sw.toString(); 
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String warnDate = sf.format(new Date());
				BasicDBObject bdObject = new BasicDBObject();
				bdObject.put("CODE",hiasException.getCode());
				bdObject.put("CODE_NAME",hiasException.getCodeName());
				bdObject.put("WARN_LEVEL",hiasException.getWarnLevel());
				bdObject.put("WARN_TYPE",hiasException.getWarnType());
				bdObject.put("WARN_DATE",warnDate);
				bdObject.put("PROCESS_TIME","");
				bdObject.put("PROCESS_STATUS","0");
				bdObject.put("FINISHED_TIME","");
				bdObject.put("MSG",e.getMessage());
				bdObject.put("MSG_DETAIL",strException);
				mbDao.addData("M_SYS_EXCEPTION", bdObject);
	}
	@Override
	public void startDeal(String id) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String processTime = sf.format(new Date());
		Document whereNew = new Document();
		whereNew.append("PROCESS_STATUS", "1").append("PROCESS_TIME", processTime);
		if(StringUtils.isNotBlank(id)){
			if(id.contains(",")){
				while(id.contains(",")){
					String stringId = id.split(",")[0];
					mbDao.updateById("M_SYS_EXCEPTION", stringId, whereNew);
					id = id.substring(id.indexOf(",")+1,id.length());
				}
				mbDao.updateById("M_SYS_EXCEPTION", id, whereNew);
			}else{
				mbDao.updateById("M_SYS_EXCEPTION", id, whereNew);
			}
		}
	}
	@Override
	public void endDeal(String id) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String finishedTime = sf.format(new Date());
		Document whereNew = new Document();
		whereNew.append("PROCESS_STATUS", "2").append("FINISHED_TIME", finishedTime);
		if(StringUtils.isNotBlank(id)){
			if(id.contains(",")){
				while(id.contains(",")){
					String stringId = id.split(",")[0];
					Document doc = mbDao.findDataById("M_SYS_EXCEPTION", stringId,"PROCESS_TIME");
					String havValue = doc.get("PROCESS_TIME").toString();
					if(StringUtils.isBlank(havValue)){
						whereNew.append("PROCESS_TIME", finishedTime);
					}
					mbDao.updateById("M_SYS_EXCEPTION", stringId, whereNew);
					id = id.substring(id.indexOf(",")+1,id.length());
				}
			}
			Document doc = mbDao.findDataById("M_SYS_EXCEPTION", id,"PROCESS_TIME");
			String havValue = doc.get("PROCESS_TIME").toString();
			if(StringUtils.isBlank(havValue)){
				whereNew.append("PROCESS_TIME", finishedTime);
			}
			mbDao.updateById("M_SYS_EXCEPTION", id, whereNew);
		}		
	}
	
}
