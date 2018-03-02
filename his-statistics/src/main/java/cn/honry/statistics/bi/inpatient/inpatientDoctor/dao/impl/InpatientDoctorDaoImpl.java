package cn.honry.statistics.bi.inpatient.inpatientDoctor.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.inner.statistics.wordLoadDoctorTotal.vo.PcWorkTotal;
import cn.honry.statistics.bi.inpatient.inpatientDoctor.dao.InpatientDoctorDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.NumberUtil;

@Repository("inpatientDoctorDao")
public class InpatientDoctorDaoImpl extends HibernateDaoSupport implements InpatientDoctorDao {
	private final String[] inpatientMedi={"T_INPATIENT_MEDICINELIST_NOW","T_INPATIENT_MEDICINELIST"};
	private final String[] inpatientItem={"T_INPATIENT_ITEMLIST_NOW","T_INPATIENT_ITEMLIST"};
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	 @Autowired
	 @Qualifier("client")
	 private Client client;

	 @Value("${es.inpatient_list.index}")
	 private String inpatient_list_index;

	 @Value("${es.inpatient_list.type}")
	 private String inpatient_list_type;

	@Override
	public void pcDoctorWorkTotal(String date) {
		String begin=date+" 00:00:00";
		String end=date+" 23:59:59";
		String mongodbCollection="ZYYSGZL_PC_DOCTOR_DAY";
		List<String> tnL=wordLoadDocDao.returnInTables(begin, end, inpatientMedi, "ZY");
		List<String> mainL=wordLoadDocDao.returnInTables(begin, end, inpatientItem, "ZY");
		
		StringBuffer buffer=new StringBuffer();
		buffer.append("select r.deptCode deptCode,");
		buffer.append("(select t.dept_name from t_department t where t.dept_code=r.deptCode and t.del_flg=0 and t.stop_flg=0) deptName,");
		buffer.append("r.doctorCode doctorCode,");
		buffer.append("(select t.employee_name from t_employee t where t.employee_code=r.doctorCode and t.del_flg=0 and t.stop_flg=0) doctorName,");
		buffer.append("nvl(r.cfs, 0) cfs,nvl(r.ryrs, 0) ryrs,nvl(r.cyrs, 0) cyrs,");
		buffer.append("nvl(r.avgindate , 0) avgindate,");
		buffer.append("nvl(r.operationNum, 0) operationNum,nvl(r.criticallyNum, 0) criticallyNum,nvl(r.deadNum, 0) deadNum ");
		
		buffer.append("from (select r.deptCode deptCode,r.doctorCode doctorCode, sum(nvl(r.cfs, 0)) cfs,sum(nvl(r.ryrs, 0)) ryrs,sum(nvl(r.cyrs, 0)) cyrs,");
		buffer.append("sum(nvl(r.indate, 0)) avgindate,sum(nvl(r.operationNum, 0)) operationNum,sum(nvl(r.criticallyNum, 0)) criticallyNum,");
		buffer.append("sum(nvl(r.deadNum, 0)) deadNum ");
		
		buffer.append("from (select count(1) ryrs,null as cfs,null as cyrs,null as indate,null as operationNum,null as criticallyNum,null as deadNum,t.dept_code deptCode,t.HOUSE_DOC_CODE doctorCode ");
		buffer.append("from t_inpatient_info  t ");
		buffer.append("where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.in_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by t.dept_code,t.HOUSE_DOC_CODE ");
		buffer.append("union all ");
		buffer.append("select  count(1) ryrs,null as cfs,null as cyrs,null as indate,null as operationNum,null as criticallyNum,null as deadNum,t.dept_code deptCode,t.HOUSE_DOC_CODE doctorCode ");
		buffer.append("from t_inpatient_info_now  t ");
		buffer.append("where t.in_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.in_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by t.dept_code,t.HOUSE_DOC_CODE  ");
		
		buffer.append("union all ");
		
		buffer.append(" select null as ryrs, null as cfs,sum(c.cyrs) cyrs,sum(c.indate) indate,null as operationNum,null as criticallyNum,null as deadNum,c.deptCode,c.doctorCode doctorCode from ( ");
		buffer.append("select count(1) cyrs,t.dept_code deptCode,(t.out_date - t.in_date) indate,t.HOUSE_DOC_CODE doctorCode ");
		buffer.append(" from t_inpatient_info  t ");
		buffer.append("where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date is not null ");
		buffer.append("group by t.dept_code, (t.out_date - t.in_date),t.HOUSE_DOC_CODE ");
		buffer.append("union all ");
		buffer.append("select count(1) cyrs,t.dept_code deptCode,(t.out_date - t.in_date) indate,t.HOUSE_DOC_CODE doctorCode ");
		buffer.append(" from t_inpatient_info_now  t ");
		buffer.append("where t.out_date <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.out_date is not null ");
		buffer.append("and t.in_state='O' ");
		buffer.append("group by t.dept_code, (t.out_date - t.in_date),t.HOUSE_DOC_CODE )c group by c.deptCode,c.doctorCode ");
		
		buffer.append("union all ");
		buffer.append("select null as ryrs,count(1) as cfs,null cyrs,null indate,null as operationNum,null as criticallyNum,null as deadNum,c.dept as deptCode,c.doctorCode doctorCode ");
		 buffer.append("from( ");
		 buffer.append("select ti.dept as dept,ti.recipeNo,ti.doctorCode doctorCode from ( ");
		 for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			 buffer.append("select t"+i+".recipe_deptcode as dept,t"+i+".RECIPE_NO recipeNo,t"+i+".RECIPE_DOCCODE  doctorCode "); 
			 buffer.append("from "+tnL.get(i)+" t"+i+" ");
			 buffer.append(" where t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		 }
		 if(mainL!=null){
			 buffer.append("union all "); 
		 }
		 for(int i=0,len=mainL.size();i<len;i++){
			 if(i>0){
				 buffer.append("union all ");
			 }
			 buffer.append("select  n"+i+".recipe_deptcode as dept,n"+i+".RECIPE_NO recipeNo,n"+i+".RECIPE_DOCCODE  doctorCode "); 
			 buffer.append("from "+mainL.get(i)+" n"+i+" where n"+i+".send_flag = 1 ");
			 buffer.append(" and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		 }
		 buffer.append(") ti  ");
		 buffer.append("group by ti.dept,ti.recipeNo,ti.doctorCode ) c ");
		 buffer.append(" group by c.dept,c.doctorCode ");
		 
		buffer.append("union all ");
		buffer.append("select null as ryrs, null as cfs,null cyrs,null indate,count(1) operationNum,null as criticallyNum,null as deadNum,t.DEPT_CODE deptCode,t.OPS_DOCD doctorCode ");
		buffer.append("from t_operation_record t ");
		buffer.append("where t.CREATETIME <= to_date('"+end+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.CREATETIME >= to_date('"+begin+"', 'yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("and t.YNVALID = 1 and t.del_flg=0 and t.stop_flg=0 group by t.DEPT_CODE,t.OPS_DOCD ");
		buffer.append(") r group by r.deptCode,r.doctorCode ) r ");
		
		List<PcWorkTotal> list=this.getSession().createSQLQuery(buffer.toString()).addScalar("deptCode")
				.addScalar("deptName").addScalar("doctorCode")
				.addScalar("cfs",Hibernate.INTEGER)
				.addScalar("doctorName").addScalar("ryrs",Hibernate.INTEGER).addScalar("cyrs",Hibernate.INTEGER).addScalar("operationNum",Hibernate.INTEGER)
				.addScalar("avgindate",Hibernate.DOUBLE).setResultTransformer(Transformers.aliasToBean(PcWorkTotal.class)).list();
		
	
	
		if(list.size()>0){
			Map<String,PcWorkTotal> map=new HashMap<String,PcWorkTotal>();
			for(int i=0,len=list.size();i<len;i++){
				PcWorkTotal vo=list.get(i);
				String key=vo.getDeptCode()+"-"+vo.getDoctorCode();
				map.put(key, vo);
			}
			Date thisDay=DateUtils.parseDateY_M_D(date);
			Date nextDay=DateUtils.addDay(thisDay, 1);
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("fee_date")
                    .gte(thisDay)
                    .lt(nextDay));
			
			
			//查询当天挂号医生
			 SearchRequestBuilder searchBuilder =client.prepareSearch(inpatient_list_index)
	                .setTypes(inpatient_list_type).setQuery(boolQuery)
	                .addAggregation(AggregationBuilders.terms("regDpcd").field("inhos_deptcode").size(0).missing("未知科室")
	                        .subAggregation(AggregationBuilders.terms("deptName").field("inhos_name").size(0).missing("未知科室"))
	                        .subAggregation(AggregationBuilders.terms("doctCode").field("recipe_doccode").size(0).missing("未知医生")
	                                .subAggregation(AggregationBuilders.terms("doctName").field("doct_name").size(0).missing("未知医生"))
	                                .subAggregation(AggregationBuilders.sum("totCost").field("tot_cost"))
	                                .subAggregation(AggregationBuilders.terms("feeStatCode").field("fee_stat_code").size(0)
	                                        .subAggregation(AggregationBuilders.sum("totCostByfee").field("tot_cost")))));
	        SearchResponse searchResponse = searchBuilder.setSize(0).execute().actionGet();
	        Terms regDpcds = searchResponse.getAggregations().get("regDpcd");
	        for (Terms.Bucket regDpcd : regDpcds.getBuckets()) {
	        	
	            Terms doctCodes = regDpcd.getAggregations().get("doctCode");
	            for (Terms.Bucket doct : doctCodes.getBuckets()) {
	            	String key=regDpcd.getKeyAsString();
		        	key+="-";
	            	key+=doct.getKeyAsString();
	            	PcWorkTotal vo = map.get(key);
	            	if(vo==null){
	            		vo=new PcWorkTotal();
	            		Terms deptNames = regDpcd.getAggregations().get("deptName");
		            	String deptName=(String) deptNames.getBuckets().get(0).getKey();
		            	Terms docNames = doct.getAggregations().get("doctName");
		            	String docName=null;
		            	if(docNames.getBuckets().size()>0){
		            		docName=(String)docNames.getBuckets().get(0).getKey();
	 		            }
	            		vo.setDeptCode(regDpcd.getKeyAsString());
	            		vo.setDeptName(deptName);
	            		vo.setDoctorCode(doct.getKeyAsString());
	            		vo.setDoctorName(docName);
	            	}
            		 vo.setDeptCode(regDpcd.getKeyAsString());
//			                Terms deptNames = regDpcd.getAggregations().get("deptName");
		                Sum totCost = doct.getAggregations().get("totCost");
		                vo.setTotle(Double.valueOf(NumberUtil.init().format(totCost.getValue(), 2)));
		                Terms feeStatCodes = doct.getAggregations().get("feeStatCode");
		                double drugCost = 0;
		                int drugNum = 0;
		                for (Terms.Bucket feeStat : feeStatCodes.getBuckets()) {
		                    Sum totCostByfee = feeStat.getAggregations().get("totCostByfee");
		                    if ("01".equals(feeStat.getKeyAsString()) || "02".equals(feeStat.getKeyAsString()) || "03".equals(feeStat.getKeyAsString())) {
		                        drugCost += totCostByfee.getValue();
		                        drugNum += feeStat.getDocCount();
		                    }
		                    this.setCost(vo, Double.valueOf(NumberUtil.init().format(totCostByfee.getValue(), 2)), feeStat.getKeyAsString());
		                }
		                vo.setAllCost(Double.valueOf(NumberUtil.init().format(drugCost, 2)));
		                vo.setMedicalCost(Double.valueOf(NumberUtil.init().format(totCost.value() - drugCost, 2)));
		                map.put(key, vo);
	            }
	        }
	        DBObject query = new BasicDBObject();
			query.put("date", date);//移除数据条件
			new MongoBasicDao().remove(mongodbCollection, query);//删除原来的数据
			if(map.size()>0){
				List<DBObject> voList = new ArrayList<DBObject>();
	        	for(String key :map.keySet()){
	        		PcWorkTotal vo = map.get(key);
	        		BasicDBObject obj = new BasicDBObject();
	        		obj.append("date", date);
	        		obj.append("deptCode", vo.getDeptCode());
	        		obj.append("deptName", vo.getDeptName());
	        		obj.append("docCode", vo.getDoctorCode());
	        		obj.append("docName", vo.getDoctorName());
	        		obj.append("cfs", vo.getCfs());
	        		obj.append("ryrs", vo.getRyrs());
	        		obj.append("cyrs", vo.getCyrs());
	        		obj.append("operationNum", vo.getOperationNum());
	        		obj.append("avgindate", vo.getAvgindate());
	        		
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
	        		obj.append("huliCost", vo.getHuliCost());
	        		obj.append("zhenchaCost", vo.getZhenchaCost());
	        		obj.append("medicalCost", vo.getMedicalCost());
	        		obj.append("totle", vo.getTotle());
	        		voList.add(obj);
	        	}
	        	new MongoBasicDao().insertDataByList(mongodbCollection, voList);//添加新数据
			}
	
	
	
		}
	}
		/**
	     * @param vo
	     * @param count
	     * @param cost
	     * @param code  统计费用代码
	     * @return
	     */
	    private PcWorkTotal setCost(PcWorkTotal vo, double cost, String code) {

	        if (StringUtils.isBlank(code) || cost == 0) {
	            return vo;
	        }
	        switch (code.trim()) {
	            case "01"://西药费
	                vo.setWesternCost(cost);
	                break;
	            case "02"://中成药费
	                vo.setChineseCost(cost);
	                break;
	            case "03"://中草药费
	                vo.setHerbalCost(cost);
	                break;
	            case "04"://床位费
	                vo.setChuangweiCost(cost);
	                break;
	            case "05"://治疗费
	                vo.setTreatmentCost(cost);
	                break;
	            case "07"://检查费
	                vo.setInspectCost(cost);
	                break;
	            case "08"://放射费
	                vo.setRadiationCost(cost);
	                break;
	            case "09"://化验费
	                vo.setTestCost(cost);
	                break;
	            case "10"://手术费
	                vo.setShoushuCost(cost);
	                break;
	            case "11"://输血费
	                vo.setBloodCost(cost);
	                break;
	            case "12"://输氧费
	                vo.setO2Cost(cost);
	                break;
	            case "13"://材料费
	                vo.setCailiaoCost(cost);
	                break;
	            case "14"://其他
	                vo.setOtherCost(cost);
	                break;
	            case "15"://护理费
	                vo.setHuliCost(cost);
	                break;
	            case "16"://诊察费
	            	vo.setZhenchaCost(cost);
	            	break;
	            	
	        }
	        return vo;
	    }

		@Override
		public Map<String, Object> queryInPatientDoc(String[] depts,
				String[] doctors, String begin, String end, Integer rows,
				Integer page) {
			BasicDBObject bdbObject = new BasicDBObject();
			BasicDBObject bdObjectTimeS = new BasicDBObject();
			BasicDBObject bdObjectTimeE = new BasicDBObject();
			BasicDBList condList = new BasicDBList(); 
			

			
			
			String mongoCollection="ZYYS_DETAIL_DAY"+begin+"|"+end;
			if(!new MongoBasicDao().isCollection(mongoCollection)){
				bdObjectTimeS.put("date",new BasicDBObject("$gte",begin));
				condList.add(bdObjectTimeS);
				bdObjectTimeE.put("date",new BasicDBObject("$lte",end));
				condList.add(bdObjectTimeE);
				bdbObject.put("$and", condList);
				
				DBObject _group = new BasicDBObject("deptCode", "$deptCode").append("docCode", "$docCode").append("docName", "$docName").append("deptName", "$deptName");  
				DBObject groupFields = new BasicDBObject("_id", _group);
				groupFields.put("cfs", new BasicDBObject("$sum","$cfs")); 
				groupFields.put("ryrs", new BasicDBObject("$sum","$ryrs")); 
				groupFields.put("cyrs", new BasicDBObject("$sum","$cyrs")); 
				groupFields.put("operationNum", new BasicDBObject("$sum","$operationNum")); 
				groupFields.put("avgindate", new BasicDBObject("$sum","$avgindate")); 
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
				groupFields.put("huliCost", new BasicDBObject("$sum","$huliCost"));
				groupFields.put("zhenchaCost", new BasicDBObject("$sum","$zhenchaCost"));
				
				DBObject match = new BasicDBObject("$match", bdbObject);
				DBObject group = new BasicDBObject("$group", groupFields);
				AggregationOutput output = new MongoBasicDao().findGroupBy("ZYYSGZL_PC_DOCTOR_DAY", match, group);
				Iterator<DBObject> it = output.results().iterator();
				List<PcWorkTotal> list=new ArrayList<PcWorkTotal>();
				while(it.hasNext()){
					PcWorkTotal vo=new PcWorkTotal();
					BasicDBObject dbo = ( BasicDBObject ) it.next();
					BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
					vo.setDeptName((String)keyValus.get("deptName"));
					vo.setDoctorName((String)keyValus.get("docName"));
					vo.setDoctorCode((String)keyValus.get("docCode"));
					vo.setDeptCode((String)keyValus.get("deptCode"));
					
					vo.setAllCost(dbo.getDouble("allCost"));
					vo.setRyrs(dbo.getInt("ryrs"));
					vo.setCyrs(dbo.getInt("cyrs"));
					vo.setOperationNum(dbo.getInt("operationNum"));
					vo.setAvgindate(dbo.getDouble("avgindate"));
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
					vo.setHuliCost(dbo.getDouble("huliCost"));
					vo.setZhenchaCost(dbo.getDouble("zhenchaCost"));
					list.add(vo);
				}
				List<DBObject> voList = new ArrayList<DBObject>();
				if(list.size()>0){
					for(PcWorkTotal vo:list){
						BasicDBObject obj = new BasicDBObject();
			    		obj.append("deptCode", vo.getDeptCode());
			    		obj.append("deptName", vo.getDeptName());
			    		obj.append("docCode", vo.getDoctorCode());
			    		obj.append("docName", vo.getDoctorName());
			    		obj.append("ryrs", vo.getRyrs());
			    		obj.append("cyrs", vo.getCyrs());
			    		obj.append("operationNum", vo.getOperationNum());
			    		obj.append("avgindate", vo.getAvgindate());
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
			    		obj.append("huliCost", vo.getHuliCost());
			    		obj.append("zhenchaCost", vo.getZhenchaCost());
			    		voList.add(obj);
					}
//					DB DBDatabase = MongoManager.getDBDatabase();
//					DBCollection dbCollection=DBDatabase.getCollection(mongoCollection);
//					DBObject indexFields = new BasicDBObject("createdAt", 1);
//					DBObject indexOptions = new BasicDBObject("expireAfterSeconds", 60);//过期时间，单位秒
//					dbCollection.createIndex(indexFields, indexOptions);
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
			List<PcWorkTotal> list1=new ArrayList<PcWorkTotal>();
			while(cursor.hasNext()){
				dbCursor=cursor.next();
				PcWorkTotal vo=new PcWorkTotal();
				vo.setDeptName((String)dbCursor.get("deptName"));
				vo.setDoctorName((String)dbCursor.get("docName"));
				vo.setDoctorCode((String)dbCursor.get("docCode"));
				vo.setAllCost((Double)dbCursor.get("allCost"));
				vo.setRyrs((Integer)dbCursor.get("ryrs"));
				vo.setCyrs((Integer)dbCursor.get("cyrs"));
				vo.setOperationNum((Integer)dbCursor.get("operationNum"));
				vo.setAvgindate((Double)dbCursor.get("feeCost"));
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
				vo.setHuliCost((Double)dbCursor.get("huliCost"));
				vo.setZhenchaCost((Double)dbCursor.get("zhenchaCost"));
				list1.add(vo);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("total", total);
			map.put("rows", list1);
			new MongoBasicDao().deleteData(mongoCollection);
			return map;
		}
	
}
