package cn.honry.statistics.bi.outpatient.outpatientDoctor.dao.impl;

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
import cn.honry.statistics.bi.bistac.toListView.vo.RegisterWorkVo;
import cn.honry.statistics.bi.outpatient.outpatientDoctor.dao.OutPatientDoctorDao;
@Repository("outPatientDoctorDao")
public class OutPatientDoctorDaoImpl implements OutPatientDoctorDao {

	@Override
	public Map<String,Object> queryOutPatientDoc(String[] depts,
			String[] doctors,String begin,String end, Integer rows, Integer page) {
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList(); 
		

		
		
		String mongoCollection="MZYS_DETAIL_DAY"+begin+"|"+end;
		if(!new MongoBasicDao().isCollection(mongoCollection)){
			bdObjectTimeS.put("date",new BasicDBObject("$gte",begin));
			condList.add(bdObjectTimeS);
			bdObjectTimeE.put("date",new BasicDBObject("$lte",end));
			condList.add(bdObjectTimeE);
			bdbObject.put("$and", condList);
			
			DBObject _group = new BasicDBObject("deptCode", "$deptCode").append("docCode", "$docCode").append("docName", "$docName").append("deptName", "$deptName");  
			DBObject groupFields = new BasicDBObject("_id", _group);
			groupFields.put("ghzj", new BasicDBObject("$sum","$ghzj")); 
			groupFields.put("jzgh", new BasicDBObject("$sum","$jzgh")); 
			groupFields.put("mzgh", new BasicDBObject("$sum","$mzgh")); 
			groupFields.put("feeCost", new BasicDBObject("$sum","$feeCost")); 
			groupFields.put("cfs", new BasicDBObject("$sum","$cfs")); 
			groupFields.put("westernCost", new BasicDBObject("$sum","$westernCost")); 
			groupFields.put("chineseCost", new BasicDBObject("$sum","$chineseCost")); 
			groupFields.put("herbalCost", new BasicDBObject("$sum","$herbalCost")); 
			groupFields.put("allCost", new BasicDBObject("$sum","$allCost")); 
			groupFields.put("chuangweiCost", new BasicDBObject("$sum","$chuangweiCost")); 
			groupFields.put("treatmentCost", new BasicDBObject("$sum","$treatmentCost")); 
			groupFields.put("inspectCost", new BasicDBObject("$sum","$inspectCost")); 
			groupFields.put("radiationCost", new BasicDBObject("$sum","$radiationCost")); 
			groupFields.put("testCost", new BasicDBObject("$sum","$testCost")); 
			groupFields.put("shoushuCost", new BasicDBObject("$sum","$shoushuCost")); 
			groupFields.put("bloodCost", new BasicDBObject("$sum","$bloodCost")); 
			groupFields.put("o2Cost", new BasicDBObject("$sum","$o2Cost")); 
			groupFields.put("cailiaoCost", new BasicDBObject("$sum","$cailiaoCost")); 
			groupFields.put("yimiaoCost", new BasicDBObject("$sum","$yimiaoCost")); 
			groupFields.put("otherCost", new BasicDBObject("$sum","$otherCost")); 
			groupFields.put("medicalCost", new BasicDBObject("$sum","$medicalCost")); 
			groupFields.put("totle", new BasicDBObject("$sum","$totle"));
			
			DBObject match = new BasicDBObject("$match", bdbObject);
			DBObject group = new BasicDBObject("$group", groupFields);
			AggregationOutput output = new MongoBasicDao().findGroupBy("MZYS_DETAIL_DAY", match, group);
			Iterator<DBObject> it = output.results().iterator();
			List<RegisterWorkVo> list=new ArrayList<RegisterWorkVo>();
			while(it.hasNext()){
				RegisterWorkVo vo=new RegisterWorkVo();
				BasicDBObject dbo = ( BasicDBObject ) it.next();
				BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
				vo.setDeptName((String)keyValus.get("deptName"));
				vo.setDocName((String)keyValus.get("docName"));
				vo.setDocCode((String)keyValus.get("docCode"));
				vo.setDeptCode((String)keyValus.get("deptCode"));
				
				vo.setAllCost(dbo.getDouble("allCost"));
				vo.setGhzj(dbo.getInt("ghzj"));
				vo.setJzgh(dbo.getInt("jzgh"));
				vo.setMzgh(dbo.getInt("mzgh"));
				vo.setFeeCost(dbo.getDouble("feeCost"));
				vo.setCfs(dbo.getInt("cfs"));
				
				vo.setWesternCost(dbo.getDouble("westernCost"));
				vo.setChineseCost(dbo.getDouble("chineseCost"));
				vo.setHerbalCost(dbo.getDouble("herbalCost"));
				vo.setChuangweiCost(dbo.getDouble("chuangweiCost"));
				vo.setTreatmentCost(dbo.getDouble("treatmentCost"));
				vo.setInspectCost(dbo.getDouble("inspectCost"));
				vo.setRadiationCost(dbo.getDouble("radiationCost"));
				vo.setTestCost(dbo.getDouble("testCost"));
				vo.setShoushuCost(dbo.getDouble("shoushuCost"));
				vo.setBloodCost(dbo.getDouble("bloodCost"));
				vo.setO2Cost(dbo.getDouble("o2Cost"));
				vo.setCailiaoCost(dbo.getDouble("cailiaoCost"));
				vo.setYimiaoCost(dbo.getDouble("yimiaoCost"));
				vo.setOtherCost(dbo.getDouble("otherCost"));
				vo.setMedicalCost(dbo.getDouble("medicalCost"));
				vo.setTotle(dbo.getDouble("totle"));
				list.add(vo);
			}
			List<DBObject> voList = new ArrayList<DBObject>();
			if(list.size()>0){
				for(RegisterWorkVo vo:list){
					BasicDBObject obj = new BasicDBObject();
		    		obj.append("deptCode", vo.getDeptCode());
		    		obj.append("deptName", vo.getDeptName());
		    		obj.append("docCode", vo.getDocCode());
		    		obj.append("docName", vo.getDocName());
		    		obj.append("ghzj", vo.getGhzj());
		    		obj.append("jzgh", vo.getJzgh());
		    		obj.append("mzgh", vo.getMzgh());
		    		obj.append("feeCost", vo.getFeeCost());
		    		obj.append("cfs", vo.getCfs());
		    		
		    		obj.append("westernCost", vo.getWesternCost());
		    		obj.append("chineseCost", vo.getChineseCost());
		    		obj.append("herbalCost", vo.getHerbalCost());
		    		obj.append("allCost", vo.getAllCost());
		    		obj.append("chuangweiCost", vo.getChuangweiCost());
		    		obj.append("treatmentCost", vo.getTreatmentCost());
		    		obj.append("inspectCost", vo.getInspectCost());
		    		obj.append("radiationCost", vo.getRadiationCost());
		    		obj.append("testCost", vo.getTestCost());
		    		obj.append("shoushuCost", vo.getShoushuCost());
		    		obj.append("bloodCost", vo.getBloodCost());
		    		obj.append("o2Cost", vo.getO2Cost());
		    		obj.append("cailiaoCost", vo.getCailiaoCost());
		    		obj.append("yimiaoCost", vo.getYimiaoCost());
		    		obj.append("otherCost", vo.getOtherCost());
		    		obj.append("medicalCost", vo.getMedicalCost());
		    		obj.append("totle", vo.getTotle());
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
		BasicDBList mongoDocList = new BasicDBList();
		if(depts.length>0){
			for(String dept:depts){
				mongoDeptList.add(new BasicDBObject("deptCode",dept));
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
		DBCursor cursor=new MongoBasicDao().findAllDataSortBy(mongoCollection, "cfs", bdbObject, rows, page);
		DBObject dbCursor;
		List<RegisterWorkVo> list1=new ArrayList<RegisterWorkVo>();
		while(cursor.hasNext()){
			dbCursor=cursor.next();
			RegisterWorkVo vo=new RegisterWorkVo();
			vo.setDeptName((String)dbCursor.get("deptName"));
			vo.setDocName((String)dbCursor.get("docName"));
			vo.setDocCode((String)dbCursor.get("docCode"));
			vo.setAllCost((Double)dbCursor.get("allCost"));
			vo.setGhzj((Integer)dbCursor.get("ghzj"));
			vo.setJzgh((Integer)dbCursor.get("jzgh"));
			vo.setMzgh((Integer)dbCursor.get("mzgh"));
			vo.setFeeCost((Double)dbCursor.get("feeCost"));
			vo.setCfs((Integer)dbCursor.get("cfs"));
			
			vo.setWesternCost((Double)dbCursor.get("westernCost"));
			vo.setChineseCost((Double)dbCursor.get("chineseCost"));
			vo.setHerbalCost((Double)dbCursor.get("herbalCost"));
			vo.setChuangweiCost((Double)dbCursor.get("chuangweiCost"));
			vo.setTreatmentCost((Double)dbCursor.get("treatmentCost"));
			vo.setInspectCost((Double)dbCursor.get("inspectCost"));
			vo.setRadiationCost((Double)dbCursor.get("radiationCost"));
			vo.setTestCost((Double)dbCursor.get("testCost"));
			vo.setShoushuCost((Double)dbCursor.get("shoushuCost"));
			vo.setBloodCost((Double)dbCursor.get("bloodCost"));
			vo.setO2Cost((Double)dbCursor.get("o2Cost"));
			vo.setCailiaoCost((Double)dbCursor.get("cailiaoCost"));
			vo.setYimiaoCost((Double)dbCursor.get("yimiaoCost"));
			vo.setOtherCost((Double)dbCursor.get("otherCost"));
			vo.setMedicalCost((Double)dbCursor.get("medicalCost"));
			vo.setTotle((Double)dbCursor.get("totle"));
			list1.add(vo);
		}
		new MongoBasicDao().deleteData(mongoCollection);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", list1);
		return map;
	}

}
