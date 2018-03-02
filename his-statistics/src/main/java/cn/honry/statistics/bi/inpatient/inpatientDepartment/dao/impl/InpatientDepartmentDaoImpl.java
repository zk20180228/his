package cn.honry.statistics.bi.inpatient.inpatientDepartment.dao.impl;

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
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.PcWorkTotal;
import cn.honry.statistics.bi.inpatient.inpatientDepartment.dao.InpatientDepartmentDao;
@Repository("inpatientDepartmentDao")
public class InpatientDepartmentDaoImpl implements InpatientDepartmentDao {

	@Override
	public Map<String,Object> queryInpatientDept(String[] depts, String begin,
			String end, Integer rows, Integer page) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		
		String mongoCollection="ZYKS_DETAIL_DEPT_DAY"+begin+"|"+end;
		
		
		if(!new MongoBasicDao().isCollection(mongoCollection)){
			bdObjectTimeS.put("date",new BasicDBObject("$gte",begin));
			condList.add(bdObjectTimeS);
			bdObjectTimeE.put("date",new BasicDBObject("$lte",end));
			condList.add(bdObjectTimeE);
			bdbObject.put("$and", condList);
			DBObject _group = new BasicDBObject("deptCode", "$deptCode").append("deptName", "$deptName");  
			DBObject groupFields = new BasicDBObject("_id", _group);
			groupFields.put("cfs", new BasicDBObject("$sum","$cfs")); 
			groupFields.put("ryrs", new BasicDBObject("$sum","$ryrs")); 
			groupFields.put("cyrs", new BasicDBObject("$sum","$cyrs")); 
			groupFields.put("avgindate", new BasicDBObject("$sum","$avgindate")); 
			groupFields.put("operationNum", new BasicDBObject("$sum","$operationNum")); 
			groupFields.put("criticallyNum", new BasicDBObject("$sum","$criticallyNum")); 
			groupFields.put("deadNum", new BasicDBObject("$sum","$deadNum")); 
			
			DBObject match = new BasicDBObject("$match", bdbObject);
			DBObject group = new BasicDBObject("$group", groupFields);
			AggregationOutput output = new MongoBasicDao().findGroupBy("ZYYSGZL_PC_DEPT_DAY", match, group);
			Iterator<DBObject> it = output.results().iterator();
			List<PcWorkTotal> list=new ArrayList<PcWorkTotal>();
			while(it.hasNext()){
				PcWorkTotal vo=new PcWorkTotal();
				BasicDBObject dbo = ( BasicDBObject ) it.next();
				BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
				vo.setDeptName((String)keyValus.get("deptName"));
				vo.setDeptCode((String)keyValus.get("deptCode"));
				
				vo.setCfs(dbo.getInt("cfs"));
				vo.setRyrs(dbo.getInt("ryrs"));
				vo.setCyrs(dbo.getInt("cyrs"));
				vo.setAvgindate(dbo.getDouble("avgindate"));
				vo.setOperationNum(dbo.getInt("operationNum"));
				
				vo.setCriticallyNum(dbo.getInt("criticallyNum"));
				vo.setDeadNum(dbo.getInt("deadNum"));
				list.add(vo);
			}
			List<DBObject> voList = new ArrayList<DBObject>();
			if(list.size()>0){
				for(PcWorkTotal vo:list){
					BasicDBObject obj = new BasicDBObject();
					obj.append("deptName", vo.getDeptName());
					obj.append("deptCode", vo.getDeptCode());
					obj.append("cfs", vo.getCfs());
					obj.append("ryrs", vo.getRyrs());
					obj.append("cyrs", vo.getCyrs());
					obj.append("avgindate", vo.getAvgindate());
					obj.append("operationNum", vo.getOperationNum());
					obj.append("criticallyNum", vo.getCriticallyNum());
					obj.append("deadNum", vo.getDeadNum());
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
				mongoDeptList.add(new BasicDBObject("deptCode",dept));
			}
			bdbObject.put("$or", mongoDeptList);
		}
		bdbObject.remove("$and");
		Long total=new MongoBasicDao().findAllCountBy(mongoCollection, bdbObject);
		DBCursor cursor=new MongoBasicDao().findAllDataSortBy(mongoCollection, "cfs", bdbObject, rows, page);
		DBObject dbCursor;
		List<PcWorkTotal> list1=new ArrayList<PcWorkTotal>();
		while(cursor.hasNext()){
			dbCursor=cursor.next();
			PcWorkTotal vo=new PcWorkTotal();
			vo.setDeptName((String)dbCursor.get("deptName"));
			vo.setDeptCode((String)dbCursor.get("deptCode"));
			
			vo.setCfs((Integer)dbCursor.get("cfs"));
			vo.setRyrs((Integer)dbCursor.get("ryrs"));
			vo.setCyrs((Integer)dbCursor.get("cyrs"));
			vo.setAvgindate((Double)dbCursor.get("avgindate"));
			vo.setOperationNum((Integer)dbCursor.get("operationNum"));
			vo.setCriticallyNum((Integer)dbCursor.get("criticallyNum"));
			vo.setDeadNum((Integer)dbCursor.get("deadNum"));
			list1.add(vo);
		}
		new MongoBasicDao().deleteData(mongoCollection);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", list1);
		return map;
	}

}
