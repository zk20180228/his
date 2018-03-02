package cn.honry.statistics.deptstat.outpatientAntPresDetail.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.outpatientAntPresDetail.vo.OutpatientAntVo;
import cn.honry.statistics.deptstat.outpatientAntPresDetail.dao.OutpatientAntPresDao;
import cn.honry.utils.NumberUtil;
import cn.honry.utils.ShiroSessionUtils;

@Repository("outpatientAntPresDao")
@SuppressWarnings("all")
public class OutpatientAntDaoImpl implements OutpatientAntPresDao {
	private static final String queryMongo="MZKJYWCFBL_TOTAL_DAY";//查询的表
	private static final String bySort="docName";//排序字段
	
	private static final  String sign="&";
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Override
	public Map<String,Object> queryList(String searchBegin,String searchEnd, String deptCodes,String menuAlias,String rows,String page) {
		Map<String,Object> returnMap=new HashMap<String,Object>();
		BasicDBObject bdObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		
		BasicDBList mongoDeptList = new BasicDBList();
		
//		DBObject _group = new BasicDBObject("docName", "$docName");
//		_group.put("dept", "$dept");
//		DBObject groupFields = new BasicDBObject("_id",_group);
//		groupFields.put("drugCfs", new BasicDBObject("$sum","$drugCfs"));
//		groupFields.put("drugKjcfs", new BasicDBObject("$sum","$drugKjcfs"));
//		groupFields.put("drugBl", new BasicDBObject("$sum","$drugBl"));
//		groupFields.put("ygbl", new BasicDBObject("$sum","$ygbl"));
//		groupFields.put("equel", new BasicDBObject("$sum","$equel"));
		
		
		
		bdObjectTimeS.put("name",new BasicDBObject("$gte",searchBegin));
		condList.add(bdObjectTimeS);
		bdObjectTimeE.put("name",new BasicDBObject("$lte",searchEnd));
		condList.add(bdObjectTimeE);
		bdObject.put("$and", condList);
		
		if(StringUtils.isBlank(deptCodes)){//如果科室为空 查询授权科室
			String jobNo=ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
			//查询授权科室
			List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,jobNo);
			
				for(int i = 0;i<deptList.size();i++){
					mongoDeptList.add(new BasicDBObject("dept",deptList.get(i).getDeptCode()));
					bdObject.put("$or", mongoDeptList);
				}
		}else{//查询前台传过来的科室
			String[] dept= deptCodes.split(",");
			for(String dep:dept){
				mongoDeptList.add(new BasicDBObject("dept",dep));
			}
			bdObject.put("$or", mongoDeptList);
		}
//		
//		DBObject match = new BasicDBObject("$match", bdObject); 
//		DBObject group = new BasicDBObject("$group", groupFields);
//		
//		AggregationOutput output=new MongoBasicDao().findGroupBySort(queryMongo, match, group, bySort, Integer.parseInt(rows), Integer.parseInt(page));
//		Iterator<DBObject> it = output.results().iterator();
//		
//		while(it.hasNext()){
//			BasicDBObject dbo = ( BasicDBObject ) it.next();
//			OutpatientAntVo vo=new OutpatientAntVo();
//			vo.setDrugBl(dbo.getString("drugBl"));
//			vo.setDrugCfs(dbo.getString("drugCfs"));
//			
//		}
		
		DBCursor cursor = new MongoBasicDao().findAlldataBySort(queryMongo, bdObject,bySort);
		
		DBObject dbCursor;
		
		Integer drugCfs1;//药物处方数
		Integer drugKjcfs1;//抗菌药物处方数
//		Double drugBl1;//抗菌药物处方比例
		Double ygbl1;//抗菌药物处方比例
		Double equel1;//对比
		
		Integer drugCfs;//药物处方数
		Integer drugKjcfs;//抗菌药物处方数
//		Double drugBl;//抗菌药物处方比例
		Double ygbl;//抗菌药物处方比例
		Double equel;//对比
		
		Map<String,Integer> drugCfsMap=new LinkedHashMap<String,Integer>();
		Map<String,Integer> drugKjcfsMap=new HashMap<String,Integer>();
//		Map<String,Double> drugBlMap=new HashMap<String,Double>();
		Map<String,Double> ygblMap=new HashMap<String,Double>();
		Map<String,Double> equelMap=new HashMap<String,Double>();
		
		String key;//主键
		String temp;
		while(cursor.hasNext()){
			 	dbCursor = cursor.next();
				 String doctorName=(String)dbCursor.get("docName") ;//医生姓名
				 String deptName=(String)dbCursor.get("dept");//科室
				 drugCfs=Integer.parseInt((String)dbCursor.get("drugCfs"));//药物处方数
				 drugKjcfs=Integer.parseInt((String)dbCursor.get("drugKjcfs")) ;//抗菌药物处方数
				 
//				 temp=(String)dbCursor.get("drugBl");
//				 drugBl=temp==null?0.0:Double.parseDouble(temp) ;//抗菌药物处方比例
				 temp=(String)dbCursor.get("ygbl");
				 ygbl=temp==null?0.0:Double.parseDouble(temp) ;//抗菌药物处方比例
				 temp=(String)dbCursor.get("equel");
				 equel=temp==null?0.0:Double.parseDouble(temp);//对比
			key=deptName+sign+doctorName;
			if(drugCfsMap.containsKey(key)){//进行累加
				drugCfs1=drugCfsMap.get(key);
				drugKjcfs1=drugKjcfsMap.get(key);
//				drugBl1=drugBlMap.get(key);
				ygbl1=ygblMap.get(key);
				equel1=equelMap.get(key);
				
				drugCfs1+=drugCfs;
				drugCfsMap.put(key, drugCfs1);
				
				drugKjcfs1+=drugKjcfs;
				drugKjcfsMap.put(key, drugKjcfs1);
				
				ygbl1+=ygbl;
				ygblMap.put(key, ygbl1);
				
				equel1+=equel;
				equelMap.put(key, equel1);
			}else{//进行添加
				drugCfsMap.put(key, drugCfs);
				drugKjcfsMap.put(key, drugKjcfs);
//				drugBlMap.put(key, drugBl);
				ygblMap.put(key, ygbl);
				equelMap.put(key, equel);
			}
		  }
		
//		drugCfs1=0;//药物处方数
//		drugKjcfs1=0;//抗菌药物处方数
//		drugBl1=0.0;//抗菌药物处方比例
//		ygbl1=0.0;//抗菌药物处方比例
//		equel1=0.0;//对比
		
//
//		 drugCfs=0;//药物处方数
//		drugKjcfs=0;//抗菌药物处方数
//		drugBl=0.0;//抗菌药物处方比例
//		ygbl=0.0;//抗菌药物处方比例
//		equel=0.0;//对比
		Integer end=Integer.parseInt(rows)*Integer.parseInt(page);
		Integer start=Integer.parseInt(rows)*(Integer.parseInt(page)-1);
		String [] keysArr;
		List<OutpatientAntVo> list=new ArrayList<OutpatientAntVo>();
		int total=0;
		for(String keys:drugCfsMap.keySet()){//循环计算
			
			if(total>=start&&total<=end){
				OutpatientAntVo vo=new OutpatientAntVo();
				keysArr=keys.split(sign);
				vo.setDept(keysArr[0]);
				vo.setDocName(keysArr[1]);
				
				drugCfs=drugCfsMap.get(keys);
//				drugCfs1+=drugCfs;
				vo.setDrugCfs(drugCfs.toString());
				
				
				drugKjcfs=drugKjcfsMap.get(keys);
//				drugKjcfs1+=drugKjcfs;
				vo.setDrugKjcfs(drugKjcfs.toString());
				
				if(drugCfs==0){
					drugCfs=1;
				}
				
				vo.setDrugBl(NumberUtil.init().format(drugKjcfs*100/drugCfs,2));
	//			drugBl=drugBlMap.get(keys);
	//			drugBl1+=drugBl;
	//			vo.setDrugBl(drugBl.toString());
				
				ygbl=ygblMap.get(keys);
//				ygbl1+=ygbl;
				vo.setYgbl(ygbl.toString());
				
				equel=equelMap.get(keys);
//				equel1+=equel;
				vo.setEquel(equel.toString());
				list.add(vo);
				if(total+1==end){
					break;
				}
			}
			total++;
		}
		returnMap.put("total", drugCfsMap.size());
		drugCfsMap=null;
		drugKjcfsMap=null;
//		drugBlMap=null;
		ygblMap=null;
		equelMap=null;
//		if(list.size()>0){
//			OutpatientAntVo vo=new OutpatientAntVo();
//			vo.setDept("病区合计");
//			vo.setDrugCfs(drugCfs1.toString());
//			vo.setDrugKjcfs(drugKjcfs1.toString());
//			vo.setDrugBl(drugBl1.toString());
//			vo.setYgbl(ygbl1.toString());
//			vo.setEquel(equel1.toString());
//			list.add(vo);
//		}
		
		returnMap.put("rows", list);
		return returnMap;
	}
	@Override
	public List<OutpatientAntVo> queryList(String tnL, String searchBegin,
			String searchEnd, String deptCodes, String menuAlias) {
		// TODO Auto-generated method stub
		return null;
	}

}
