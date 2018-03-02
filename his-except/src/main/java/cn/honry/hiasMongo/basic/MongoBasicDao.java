package cn.honry.hiasMongo.basic;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

/**
 * mongodb基础类用于dao的创建(获取mongodb数据库连接,提供增删改查....)
 * hedong 20170325
 */
public class MongoBasicDao {
	private Logger logger=Logger.getLogger(MongoBasicDao.class);
	private static MongoManager mongoManager = MongoManager.getInstance();
//	private static MongoDatabase mongoDatabase = MongoManager.getMongoDatabase(); 
//	private static DB DBDatabase = MongoManager.getDBDatabase(); 
	
	private DBCursor cursor = null;
	private Long count = null;
	
	/***************************增*****************************************/
	/**
	 * 插入一条数据，参数为Document类型
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName  mongodb集合名（表名）
	 * @param document
	 */
	public void insertData(String collectionName, Document document){
		try{
			MongoDatabase mongoDatabase = MongoManager.getMongoDatabase();
			mongoDatabase.getCollection(collectionName).insertOne(document);
			mongoDatabase=null;
			logger.info(collectionName+" insert "+document);
		}catch(Exception e){
			logger.error("mongodb:insertData()", e);
		}
	}
	/**
	 * 插入一个list，参数为DBObject类型
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName  mongodb集合名（表名）
	 * @param userList
	 */
	public void insertDataByList(String collectionName,List<DBObject> userList){
		try{
			DB DBDatabase = MongoManager.getDBDatabase();
			DBDatabase.getCollection(collectionName).insert(userList);
			DBDatabase = null;
			logger.info(collectionName+" insert "+userList);
		}catch(Exception e){
			logger.error("mongodb:insertDataByList()", e);
		}
	}
	/**
	 * 插入一条数据，参数为BasicDBObject类型
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName
	 * @param where
	 */
	public void addData(String collectionName, BasicDBObject where){
		try{
			DB DBDatabase = MongoManager.getDBDatabase();
			DBDatabase.getCollection(collectionName).save(where);
			DBDatabase=null;
			logger.info(collectionName+" insert "+where);
		}catch(Exception e){
			logger.error("mongodb:addData()", e);
		}
	}
	/***************************删*****************************************/
	/**
	 * 清空一个集合，删除集合中的所有数据
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName  mongodb集合名（表名）
	 */
	public void deleteData(String collectionName){
		try{
			MongoDatabase mongoDatabase = MongoManager.getMongoDatabase();
			mongoDatabase.getCollection(collectionName).drop();
			mongoDatabase=null;
			logger.info(collectionName+" drop ");
		}catch(Exception e){
			logger.error("mongodb:deleteData()", e);
		}
	}
	
	/**
	 * 删除集合中符合查询条件的数据
	 * @param collectionName 集合名(表名)
	 * @param query 查询条件
	 */
	public void remove(String collectionName,DBObject query){
		try {
			DB dataBase = MongoManager.getDBDatabase();
			dataBase.getCollection(collectionName).remove(query);
			dataBase=null;
			logger.info(collectionName+" remove "+query);
		} catch (Exception e) {
			logger.error("mongodb:remove()", e);
			e.printStackTrace();
		}
	}
	
	/***************************改*****************************************/
	/**
	 * 根据ID。更新数据
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName
	 * @param id 数据ID
	 * @param newdoc 新条目
	 */
	public void updateById(String collectionName, String id, Document newdoc) {
		ObjectId _idobj = null;
		try {
			_idobj = new ObjectId(id);
		} catch (Exception e) {
			logger.error("mongodb:updateById()", e);
		}
		Bson filter = Filters.eq("_id", _idobj);
		MongoDatabase mongoDatabase = MongoManager.getMongoDatabase();
		mongoDatabase.getCollection(collectionName).updateOne(filter, new Document("$set", newdoc));
		mongoDatabase=null;
		logger.info(collectionName+" update old"+filter+" new "+newdoc);
	}
	/**
	 * 
	 * @Author zxh
	 * @time 2017年5月10日
	 * @param collectionName
	 * @param whileFilter 更新条件
	 * @param setFilter 更新数据
	 * @param isUpsert  
	 * 			true:匹配不到查询条件时会插入文档
	 */
	public void update(String collectionName, Bson whileFilter,  Document newdoc, boolean isUpsert) {
			UpdateOptions upOp = new UpdateOptions();
			upOp.upsert(isUpsert);
			MongoDatabase mongoDatabase = MongoManager.getMongoDatabase();
			mongoDatabase.getCollection(collectionName).updateOne(whileFilter, new Document("$set", newdoc), upOp);
			logger.info(collectionName+" update old"+whileFilter+" new "+newdoc+" upon "+upOp);
	}
	/***************************查*****************************************/
	/**
	 * 判断mongodb中是否存在该表
	 * @Author zxh
	 * @time 2017年5月20日
	 * @param collectionName
	 * @return
	 */
	public boolean isCollection(String collectionName){
	    DB DBDatabase = MongoManager.getDBDatabase();
	    Iterable<String> collectionNames = DBDatabase.getCollectionNames();
	    Iterator<String> iterator = collectionNames.iterator();
	    while(iterator.hasNext()){
	      String name = iterator.next();
	      if(name.equals(collectionName)){
	        return true;
	      }
	    }
	      return false;
	  }
	/**
	 * 查询所有
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName
	 * @param document
	 * @return
	 */
	public DBCursor findAlldata(String collectionName, BasicDBObject where){
		try{
			DB DBDatabase = MongoManager.getDBDatabase();
			cursor = DBDatabase.getCollection(collectionName).find(where);
			DBDatabase=null;
			logger.info(collectionName+" find "+where);
		}catch(Exception e){
			logger.error("mongodb:findAlldata()", e);
		}
		return cursor;
	}
	/**
	 * 排序查询
	 * @Author zxh
	 * @time 2017年5月17日
	 * @param collectionName
	 * @param where
	 * @return
	 */
	public DBCursor findAlldataBySort(String collectionName, BasicDBObject where, String sortName){
		DBObject orderBy = new BasicDBObject(sortName, -1);
		try{
			DB DBDatabase = MongoManager.getDBDatabase();
			cursor = DBDatabase.getCollection(collectionName).find(where).sort(orderBy);
			DBDatabase=null;
			logger.info(collectionName+" find "+where+" orderBy "+orderBy);
		}catch(Exception e){
			logger.error("mongodb:findAlldata()", e);
		}
		return cursor;
	}
	/**
	 * sortType：-1降序
	 * 			1升序
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年8月2日 上午9:29:48 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年8月2日 上午9:29:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param collectionName
	 * @param where
	 * @param sortName
	 * @param sortType
	 * @return:
	 * @throws:
	 * @return: DBCursor
	 *
	 */
	public DBCursor findAlldataBySort(String collectionName, BasicDBObject where, String sortName, Integer sortType){
		if(sortType!=1&&sortType!=-1){
			return cursor;
		}
		DBObject orderBy = new BasicDBObject(sortName, sortType);
		try{
			DB DBDatabase = MongoManager.getDBDatabase();
			cursor = DBDatabase.getCollection(collectionName).find(where).sort(orderBy);
			DBDatabase=null;
			logger.info(collectionName+" find "+where+" orderBy "+orderBy);
		}catch(Exception e){
			logger.error("mongodb:findAlldata()", e);
		}
		return cursor;
	}
	/**
	 * 根据ID，查询指定列
	 * @Author zxh
	 * @time 2017年4月7日
	 * @param collectionName
	 * @param id
	 * @param column
	 * @return
	 */
	public Document findDataById(String collectionName, String id, String column){
		ObjectId _idobj = null;
		try {
			_idobj = new ObjectId(id);
		} catch (Exception e) {
			return null;
		}
		MongoDatabase mongoDatabase = MongoManager.getMongoDatabase();
		Document myDoc = mongoDatabase.getCollection(collectionName).find(Filters.eq("_id", _idobj)).first();
		logger.info(collectionName+" find "+_idobj);
		return myDoc;
	}
	/**
	 * 条件查询,逆序,分页
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName
	 * @param sortName 根据它进行排序
	 * @param document
	 * @param rows
	 * @param page
	 * @return
	 */
	public DBCursor findAllDataSortBy(String collectionName, String sortName, BasicDBObject where, Integer rows, Integer page){
		DBObject orderBy = new BasicDBObject(sortName, -1);
		try{
			DB DBDatabase = MongoManager.getDBDatabase();
			cursor = DBDatabase.getCollection(collectionName).find(where).sort(orderBy).skip((page - 1) * rows).limit(rows);
			DBDatabase=null;
			logger.info(collectionName+" find "+where+" orderBy "+orderBy+" skip "+(page - 1) * rows+" limit "+rows);
		}catch(Exception e){
			logger.error("mongodb:findAllDataSortBy()", e);
		}
		return cursor;
	}
	/**
	 * 分组分页查询
	 * 
	 * <p> </p>
	 * @Author: zouxianhao
	 * @CreateDate: 2017年7月29日 下午4:47:22 
	 * @Modifier: zouxianhao
	 * @ModifyDate: 2017年7月29日 下午4:47:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param collectionName
	 * @param match
	 * @param group
	 * @param sortName
	 * @return:
	 * @throws:
	 * @return: AggregationOutput
	 *
	 */
	public AggregationOutput findGroupBySort(String collectionName,DBObject match,DBObject group,String sortName, Integer rows, Integer page){
		DB DBDatabase = MongoManager.getDBDatabase();
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject(sortName, -1)); 
		DBObject skip = new BasicDBObject("$skip", (page - 1) * rows);
		DBObject limit = new BasicDBObject("$limit", rows);
		AggregationOutput output = DBDatabase.getCollection(collectionName).aggregate(match, group, sort, skip, limit);
		return output;
	}
	/**
	 * 查询符合条件的数据量
	 * @Author zxh
	 * @time 2017年4月6日
	 * @param collectionName
	 * @param where
	 * @return
	 */
	public Long findAllCountBy(String collectionName, BasicDBObject where){
		try{
			DB DBDatabase = MongoManager.getDBDatabase();
			count = DBDatabase.getCollection(collectionName).count(where);
			DBDatabase = null;
			logger.info(collectionName+" count "+where);
		}catch(Exception e){
			logger.error("mongodb:findAllCountBy()", e);
		}
		return count;
	}
	public AggregationOutput findGroupBy(String collectionName,DBObject match,DBObject group){
		DB DBDatabase = MongoManager.getDBDatabase();
		AggregationOutput output = DBDatabase.getCollection(collectionName).aggregate(match, group);
		return output;
	}
	
	/***************************关*****************************************/
//	public void closeBase(){
//		if(this.DBDatabase!=null){
//			this.DBDatabase=null;
//		}
//		if(this.mongoDatabase!=null){
//			this.mongoDatabase=null;
//		}
//	}
}
