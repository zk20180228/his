package cn.honry.statistics.bi.operation.operationDoctor.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.operationNum.vo.pcOperationWork;
import cn.honry.statistics.bi.operation.operationDoctor.dao.OperationDoctorDao;
@Repository("operationDoctorDao")
public class OperationDoctorDaoImpl implements OperationDoctorDao {

	@Override
	public Map<String, Object> queryoperationPatientDoctor(String[] depts,
			String[] doctors, String begin, String end, Integer rows,
			Integer page) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList mongoDocList = new BasicDBList();
		BasicDBList condList = new BasicDBList(); 
		
		String mongoCollection="OPERATION_PC_DOC_DAY"+begin+"|"+end;
		if(!new MongoBasicDao().isCollection(mongoCollection)){
			bdObjectTimeS.put("date",new BasicDBObject("$gte",begin));
			condList.add(bdObjectTimeS);
			bdObjectTimeE.put("date",new BasicDBObject("$lte",end));
			condList.add(bdObjectTimeE);
			bdbObject.put("$and", condList);
			
			DBObject _group = new BasicDBObject("deptCode", "$deptCode").append("deptName", "$deptName").append("docCode", "$docCode").append("docName", "$docName").append("ownDeptName", "$ownDeptName").append("ownDeptCode", "$ownDeptCode");  
			DBObject groupFields = new BasicDBObject("_id", _group);
			groupFields.put("opskindzq", new BasicDBObject("$sum","$opskindzq")); 
			groupFields.put("opskingpt", new BasicDBObject("$sum","$opskingpt")); 
			groupFields.put("opskingjz", new BasicDBObject("$sum","$opskingjz")); 
			groupFields.put("operationNums", new BasicDBObject("$sum","$operationNums")); 
			groupFields.put("opskinggr", new BasicDBObject("$sum","$opskinggr")); 
			groupFields.put("anesmz", new BasicDBObject("$sum","$anesmz")); 
			groupFields.put("anesbmz", new BasicDBObject("$sum","$anesbmz")); 
			groupFields.put("bigoperation", new BasicDBObject("$sum","$bigoperation")); 
			groupFields.put("middleoperation", new BasicDBObject("$sum","$middleoperation")); 
			groupFields.put("smalloperation", new BasicDBObject("$sum","$smalloperation")); 
			groupFields.put("consolept", new BasicDBObject("$sum","$consolept")); 
			groupFields.put("consolejt", new BasicDBObject("$sum","$consolejt")); 
			groupFields.put("consoledt", new BasicDBObject("$sum","$consoledt")); 
			groupFields.put("consolejjt", new BasicDBObject("$sum","$consolejjt")); 
			
			DBObject match = new BasicDBObject("$match", bdbObject);
			DBObject group = new BasicDBObject("$group", groupFields);
			AggregationOutput output = new MongoBasicDao().findGroupBy("OPERATION_PC_DETAIL_DAY", match, group);
			Iterator<DBObject> it = output.results().iterator();
			List<pcOperationWork> list=new ArrayList<pcOperationWork>();
			while(it.hasNext()){
				pcOperationWork vo=new pcOperationWork();
				BasicDBObject dbo = ( BasicDBObject ) it.next();
				BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
				vo.setDeptName((String)keyValus.get("deptName"));
				vo.setDeptCode((String)keyValus.get("deptCode"));
				vo.setOwnDeptName((String)keyValus.get("ownDeptName"));
				vo.setOwnDeptCode((String)keyValus.get("ownDeptCode"));
				vo.setDocCode((String)keyValus.get("docCode"));
				vo.setDocName((String)keyValus.get("docName"));
				vo.setOpskindzq(dbo.getInt("opskindzq"));
				vo.setOpskingpt(dbo.getInt("opskingpt"));
				vo.setOpskingjz(dbo.getInt("opskingjz"));
				vo.setOpskinggr(dbo.getInt("opskinggr"));
				vo.setOperationNums(dbo.getInt("operationNums"));
				vo.setOpskinggr(dbo.getInt("opskinggr"));
				vo.setAnesmz(dbo.getInt("anesmz"));
				
				vo.setAnesbmz(dbo.getInt("anesbmz"));
				vo.setBigoperation(dbo.getInt("bigoperation"));
				vo.setMiddleoperation(dbo.getInt("middleoperation"));
				vo.setSmalloperation(dbo.getInt("smalloperation"));
				vo.setConsolept(dbo.getInt("consolept"));
				vo.setConsolejt(dbo.getInt("consolejt"));
				vo.setConsoledt(dbo.getInt("consoledt"));
				vo.setConsolejjt(dbo.getInt("consolejjt"));
				list.add(vo);
			}
			List<DBObject> voList = new ArrayList<DBObject>();
			if(list.size()>0){
				for(pcOperationWork vo:list){
					BasicDBObject obj = new BasicDBObject();
		    		obj.append("deptCode", vo.getDeptCode());
		    		obj.append("deptName", vo.getDeptName());
		    		obj.append("ownDeptCode", vo.getOwnDeptCode());
		    		obj.append("ownDeptName", vo.getOwnDeptName());
		    		obj.append("docCode", vo.getDocCode());
		    		obj.append("docName", vo.getDocName());
		    		obj.append("opskindzq", vo.getOpskindzq());
		    		obj.append("opskingpt", vo.getOpskingpt());
		    		obj.append("opskingjz", vo.getOpskingjz());
		    		obj.append("opskinggr", vo.getOpskinggr());
		    		obj.append("anesmz", vo.getAnesmz());
		    		
		    		obj.append("anesbmz", vo.getAnesbmz());
		    		obj.append("bigoperation", vo.getBigoperation());
		    		obj.append("middleoperation", vo.getMiddleoperation());
		    		obj.append("smalloperation", vo.getSmalloperation());
		    		obj.append("consolept", vo.getConsolept());
		    		obj.append("consolejt", vo.getConsolejt());
		    		obj.append("consoledt", vo.getConsoledt());
		    		obj.append("consolejjt", vo.getConsolejjt());
		    		obj.append("operationNums", vo.getOperationNums());
		    		voList.add(obj);
				}
//				DB DBDatabase = MongoManager.getDBDatabase();
//				DBCollection dbCollection=DBDatabase.getCollection(mongoCollection);
//				DBObject indexFields = new BasicDBObject("createdAt", 1);
//				DBObject indexOptions = new BasicDBObject("expireAfterSeconds", 60);//过期时间，单位秒
//				dbCollection.createIndex(indexFields, indexOptions);
				new MongoBasicDao().insertDataByList(mongoCollection, voList);//添加新数据
				
			}
		}
		
		BasicDBList mongoDeptList = new BasicDBList();
		if(depts.length>0){
			for(String dept:depts){
				mongoDeptList.add(new BasicDBObject("ownDeptCode",dept));
			}
			bdbObject.put("$or", mongoDeptList);
		}
		if(doctors.length>0){
			for(String doctor:doctors){
				mongoDocList.add(new BasicDBObject("docCode",doctor));
			}
			bdbObject.put("$or", mongoDocList);
		}
		bdbObject.remove("$and");
		Long total=new MongoBasicDao().findAllCountBy(mongoCollection, bdbObject);
		DBCursor cursor=new MongoBasicDao().findAllDataSortBy(mongoCollection, "operationNums", bdbObject, rows, page);
		DBObject dbCursor;
		List<pcOperationWork> list1=new ArrayList<pcOperationWork>();
		while(cursor.hasNext()){
			dbCursor=cursor.next();
			pcOperationWork vo=new pcOperationWork();
			vo.setDeptName((String)dbCursor.get("deptName"));
			vo.setDeptCode((String)dbCursor.get("deptCode"));
			vo.setDocCode((String)dbCursor.get("docCode"));
			vo.setDocName((String)dbCursor.get("docName"));
			vo.setOwnDeptCode((String)dbCursor.get("ownDeptCode"));
			vo.setOwnDeptName((String)dbCursor.get("ownDeptName"));
			vo.setOpskindzq((Integer)dbCursor.get("opskindzq"));
			vo.setOpskingpt((Integer)dbCursor.get("opskingpt"));
			vo.setOpskingjz((Integer)dbCursor.get("opskingjz"));
			vo.setOpskinggr((Integer)dbCursor.get("opskinggr"));
			vo.setOperationNums((Integer)dbCursor.get("operationNums"));
			vo.setOpskinggr((Integer)dbCursor.get("opskinggr"));
			vo.setAnesmz((Integer)dbCursor.get("anesmz"));
			
			vo.setAnesbmz((Integer)dbCursor.get("anesbmz"));
			vo.setBigoperation((Integer)dbCursor.get("bigoperation"));
			vo.setMiddleoperation((Integer)dbCursor.get("middleoperation"));
			vo.setSmalloperation((Integer)dbCursor.get("smalloperation"));
			vo.setConsolept((Integer)dbCursor.get("consolept"));
			vo.setConsolejt((Integer)dbCursor.get("consolejt"));
			vo.setConsoledt((Integer)dbCursor.get("consoledt"));
			vo.setConsolejjt((Integer)dbCursor.get("consolejjt"));
			list1.add(vo);
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", list1);
		new MongoBasicDao().deleteData(mongoCollection);
		return map;
	}

}
