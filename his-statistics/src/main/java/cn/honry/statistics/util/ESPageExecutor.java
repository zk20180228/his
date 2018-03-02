package cn.honry.statistics.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.honry.hiasMongo.basic.MongoManager;
import cn.honry.utils.JSONUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class ESPageExecutor<T> implements Runnable {
	
	private static final Integer EXPIREAFTERSECONDS = 60*5;
	private String collection;
	private List<T> totalList = new ArrayList<T>();
	private BasicDBObject bdbObject = new BasicDBObject();
	private String rows;
	
	public ESPageExecutor(String collection, List<T> totalList, BasicDBObject bdbObject, String rows) {
		this.collection = collection;
		this.totalList = totalList;
		this.bdbObject = bdbObject;
		this.rows = rows;
	}

	@Override
	public void run() {
		DB DBDatabase = MongoManager.getDBDatabase();
		DBCollection dbCollection;
		if (!DBDatabase.getCollectionNames().contains(collection)) {
			dbCollection = DBDatabase.getCollection(collection);
			DBObject indexFields = new BasicDBObject("createTime", 1);
			DBObject indexOptions = new BasicDBObject("expireAfterSeconds", EXPIREAFTERSECONDS);//过期时间，单位秒
			dbCollection.createIndex(indexFields, indexOptions);
		} else {
			dbCollection = DBDatabase.getCollection(collection);
		}
		//缓存分页
		for (int i = 1; i <= totalList.size()/Integer.valueOf(rows)+1; i++) {
			BasicDBObject object = (BasicDBObject)bdbObject.clone();
			List<T> pageList = new ArrayList<>();
			for (int j = (i-1) * Integer.valueOf(rows); j < totalList.size() && j < i * Integer.valueOf(rows); j++) {
				pageList.add(totalList.get(j));
			}
			object.put("page", String.valueOf(i));
			if (!dbCollection.find(object).hasNext()) {
				object.put("total", totalList.size());
				object.put("jsonList", JSONUtils.toJson(pageList));
				object.put("createTime", new Date());
				dbCollection.insert(object);
			}
		}
	}
	
}
