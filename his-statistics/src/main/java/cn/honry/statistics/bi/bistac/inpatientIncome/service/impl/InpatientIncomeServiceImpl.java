package cn.honry.statistics.bi.bistac.inpatientIncome.service.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.inpatientIncome.dao.InpatientIncomeDao;
import cn.honry.statistics.bi.bistac.inpatientIncome.service.InpatientIncomeService;
import cn.honry.statistics.bi.bistac.inpatientIncome.vo.InpatientIncomeVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.RedisUtil;

@Service("inpatientIncomeService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientIncomeServiceImpl  implements InpatientIncomeService{
	@Resource
	private RedisUtil redis;
	@Autowired
	@Qualifier(value = "inpatientIncomeDao")
	private InpatientIncomeDao inpatientIncomeDao;
	public void setInpatientIncomeDao(InpatientIncomeDao inpatientIncomeDao) {
		this.inpatientIncomeDao = inpatientIncomeDao;
	}
	public static final String TABLENAME7 = "ZYSRTJ";//住院收入统计表
	public static final String TABLENAME = "T_ZYSRTJ";//住院收入统计表

	private MongoBasicDao mbDao = null;
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Resource(name = "deptInInterDAO")
	private DeptInInterDAO deptInInterDAO;
	
	@Resource(name = "client")
	private Client client;

	@Value("${es.balancehead.index}")
	private String balancehead_index;

	@Value("${es.balancehead.type}")
	private String balancehead_type;
	
	/**
	 * 
	 * 住院收入分析 elasticsearch实现
	 * @Author: 朱振坤
	 * @throws Exception 
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InpatientIncomeVo> queryInpatientIncomeVoFromES(String time1,
			String time2) throws Exception {
		if (StringUtils.isBlank(time1) || StringUtils.isBlank(time2)) {
			throw new IllegalArgumentException("您没有输入年月！");
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		List<InpatientIncomeVo> list = new ArrayList<InpatientIncomeVo>();
		Map<String,InpatientIncomeVo> map = new HashedMap();
		String lastM1 = this.getLastYear(time1);
		String lastM2 = this.getLastYear(time2);
		Date sdate1 = format.parse(time1);
		Date lastsdate1 = format.parse(lastM1);
		Date sdate2 = format.parse(time2);
		Date lastsdate2 = format.parse(lastM2);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdate1);
		calendar.add(Calendar.MONTH, 1);
		Date edate1 = calendar.getTime();
		calendar.setTime(lastsdate1);
		calendar.add(Calendar.MONTH, 1);
		Date lastedate1 = calendar.getTime();
		calendar.setTime(sdate2);
		calendar.add(Calendar.MONTH, 1);
		Date edate2 = calendar.getTime();
		calendar.setTime(lastsdate2);
		calendar.add(Calendar.MONTH, 1);
		Date lastedate2 = calendar.getTime();
		BoolQueryBuilder boolQuery1 = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("stop_flg", 0))
				.filter(QueryBuilders.termQuery("del_flg", 0))
				.filter(QueryBuilders.boolQuery()
					.should(QueryBuilders.rangeQuery("balance_date").gte(sdate1).lt(edate1))
					.should(QueryBuilders.rangeQuery("balance_date").gte(sdate2).lt(edate2)));
		SearchResponse searchResponse1 = this.client.prepareSearch(balancehead_index)
				.setTypes(balancehead_type).setQuery(boolQuery1)
				.execute().actionGet();
		Set<String> deptCodeSet = new HashSet<String>();
		for (SearchHit hit : searchResponse1.getHits()) {
			Map<String,Object> jsonMap = (Map<String,Object>)JSONUtils.fromJson(hit.getSourceAsString(), Object.class);
			if (jsonMap.get("balanceoper_deptcode") != null) {
				deptCodeSet.add((String) jsonMap.get("balanceoper_deptcode"));// 放在set中去重
			}
		}
		for (String deptCode : deptCodeSet) {
			InpatientIncomeVo inpatientIncomeVo = new InpatientIncomeVo();
			inpatientIncomeVo.setDeptName(this.getDeptName(deptCode));
			map.put(deptCode, inpatientIncomeVo);
		}
		BoolQueryBuilder boolQuery2 = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("stop_flg", 0))
				.filter(QueryBuilders.termQuery("del_flg", 0))
				.filter(QueryBuilders.rangeQuery("balance_date").gte(sdate1).lt(edate1))
				.filter(QueryBuilders.termsQuery("balanceoper_deptcode",deptCodeSet));
		SearchResponse searchResponse2 = this.client.prepareSearch(balancehead_index)
				.setTypes(balancehead_type).setQuery(boolQuery2).addAggregation(
						AggregationBuilders.terms("balanceoperDeptCode").field("balanceoper_deptcode").size(0).order(Terms.Order.term(true)).subAggregation(
								AggregationBuilders.sum("totCost").field("tot_cost")).size(0).subAggregation(
										AggregationBuilders.sum("supplyCost").field("supply_cost")).size(0))
										.execute().actionGet();
		Terms balanceoperDeptCodes = searchResponse2.getAggregations().get("balanceoperDeptCode");
		for (Terms.Bucket dept : balanceoperDeptCodes.getBuckets()) {
			String deptCode = (String)dept.getKey();
			Sum totCostSum = dept.getAggregations().get("totCost");
			map.get(deptCode).setTotCost1(totCostSum.getValue());
			Sum supplyCostSum = dept.getAggregations().get("supplyCost");
			map.get(deptCode).setSupplyCost1(supplyCostSum.getValue());
		}
		BoolQueryBuilder boolQuery3 = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("stop_flg", 0))
				.filter(QueryBuilders.termQuery("del_flg", 0))
				.filter(QueryBuilders.rangeQuery("balance_date").gte(sdate2).lt(edate2))
				.filter(QueryBuilders.termsQuery("balanceoper_deptcode",deptCodeSet));
		SearchResponse searchResponse3 = this.client.prepareSearch(balancehead_index)
				.setTypes(balancehead_type).setQuery(boolQuery3).addAggregation(
						AggregationBuilders.terms("balanceoperDeptCode").field("balanceoper_deptcode").size(0).order(Terms.Order.term(true)).subAggregation(
								AggregationBuilders.sum("totCost").field("tot_cost")).size(0).subAggregation(
										AggregationBuilders.sum("supplyCost").field("supply_cost")).size(0))
										.execute().actionGet();
		balanceoperDeptCodes = searchResponse3.getAggregations().get("balanceoperDeptCode");
		for (Terms.Bucket dept : balanceoperDeptCodes.getBuckets()) {
			String deptCode = (String)dept.getKey();
			Sum totCostSum = dept.getAggregations().get("totCost");
			map.get(deptCode).setTotCost2(totCostSum.getValue());
			Sum supplyCostSum = dept.getAggregations().get("supplyCost");
			map.get(deptCode).setSupplyCost2(supplyCostSum.getValue());
		}
		
		BoolQueryBuilder boolQuery4 = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("stop_flg", 0))
				.filter(QueryBuilders.termQuery("del_flg", 0))
				.filter(QueryBuilders.rangeQuery("balance_date").gte(lastsdate1).lt(lastedate1))
				.filter(QueryBuilders.termsQuery("balanceoper_deptcode",deptCodeSet));
		SearchResponse searchResponse4 = this.client.prepareSearch(balancehead_index)
				.setTypes(balancehead_type).setQuery(boolQuery4).addAggregation(
						AggregationBuilders.terms("balanceoperDeptCode").field("balanceoper_deptcode").size(0).order(Terms.Order.term(true)).subAggregation(
								AggregationBuilders.sum("supplyCost").field("supply_cost")).size(0))
								.execute().actionGet();
		balanceoperDeptCodes = searchResponse4.getAggregations().get("balanceoperDeptCode");
		for (Terms.Bucket dept : balanceoperDeptCodes.getBuckets()) {
			String deptCode = (String)dept.getKey();
			Sum supplyCostSum = dept.getAggregations().get("supplyCost");
			map.get(deptCode).setLastSupplyCost1(supplyCostSum.getValue());
		}
		
		BoolQueryBuilder boolQuery5 = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery("stop_flg", 0))
				.filter(QueryBuilders.termQuery("del_flg", 0))
				.filter(QueryBuilders.rangeQuery("balance_date").gte(lastsdate2).lt(lastedate2))
				.filter(QueryBuilders.termsQuery("balanceoper_deptcode",deptCodeSet));
		SearchResponse searchResponse5 = this.client.prepareSearch(balancehead_index)
				.setTypes(balancehead_type).setQuery(boolQuery5).addAggregation(
						AggregationBuilders.terms("balanceoperDeptCode").field("balanceoper_deptcode").size(0).order(Terms.Order.term(true)).subAggregation(
								AggregationBuilders.sum("supplyCost").field("supply_cost")).size(0))
								.execute().actionGet();
		balanceoperDeptCodes = searchResponse5.getAggregations().get("balanceoperDeptCode");
		for (Terms.Bucket dept : balanceoperDeptCodes.getBuckets()) {
			String deptCode = (String)dept.getKey();
			Sum supplyCostSum = dept.getAggregations().get("supplyCost");
			map.get(deptCode).setLastSupplyCost2(supplyCostSum.getValue());
		}
		for(Map.Entry<String, InpatientIncomeVo> entry : map.entrySet()){
			list.add(entry.getValue());
		}
		System.out.println("住院收入分析用时："+(searchResponse1.getTookInMillis()+searchResponse2.getTookInMillis()+searchResponse3.getTookInMillis()
				+searchResponse4.getTookInMillis()+searchResponse5.getTookInMillis())+"ms");
		return list;
	}
	
	/**  
	 * 
	 * 住院收入分析
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月20日 上午11:10:02 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月20日 上午11:10:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<InpatientIncomeVo> queryInpatientIncomeVo(String time1,
			String time2) throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		List<String> list = null;
		String lastM1=this.getLastYear(time1);
		String lastM2=this.getLastYear(time2);
		try {
			//获取当前表最大时间及最小时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dTime = df.parse(df.format(new Date()));
			Date cTime = DateUtils.addDay(dTime,-(Integer.parseInt(dateNum)*30)+1);
			Date st1 = format.parse(time1);
			Date lastSt1 = format.parse(lastM1);
			Date st2 = format.parse(time1);
			Date lastSt2 = format.parse(lastM2);
			Calendar a1=Calendar.getInstance();
			Calendar LastA1=Calendar.getInstance();
			Calendar a2=Calendar.getInstance();
			Calendar LastA2=Calendar.getInstance();
		    a1.setTime(st1); 
		    LastA1.setTime(lastSt1); 
		    a2.setTime(st2);
		    LastA2.setTime(lastSt2); 
		    a1.set(Calendar.DATE, 1); 
		    a2.set(Calendar.DATE, 1);
		    LastA1.set(Calendar.DATE, 1);
		    LastA2.set(Calendar.DATE, 1);//把日期设置为当月第一天
		    a1.roll(Calendar.DATE, -1);  
		    a2.roll(Calendar.DATE, -1);
		    LastA1.roll(Calendar.DATE, -1);
		    LastA2.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		    int MaxDate1=a1.get(Calendar.DATE);
		    int MaxDate2=a2.get(Calendar.DATE);
		    int MaxLastDate1=LastA1.get(Calendar.DATE);
		    int MaxLastDate2=LastA2.get(Calendar.DATE);
		    String stime1=time1+"-01";
		    String etime1=time1+"-"+MaxDate1;
		    String stime2=time2+"-01";
		    String etime2=time2+"-"+MaxDate2;
		    String lastSTime1=lastM1+"-01";
		    String lastETime1=lastM1+"-"+MaxLastDate1;
		    String lastSTime2=lastM2+"-01";
		    String lastETime2=lastM2+"-"+MaxLastDate2;
			Date sTime = DateUtils.parseDateY_M_D(stime1);
			Date eTime = DateUtils.parseDateY_M_D(etime1);
			Date sTime2 = DateUtils.parseDateY_M_D(stime2);
			Date eTime2= DateUtils.parseDateY_M_D(etime2);
			Date lastsTime1 = DateUtils.parseDateY_M_D(lastSTime1);
			Date lasteTime1 = DateUtils.parseDateY_M_D(lastETime1);
			Date lastsTime2 = DateUtils.parseDateY_M_D(lastSTime2);
			Date lasteTime2 = DateUtils.parseDateY_M_D(lastETime2);
			List<String> tnL1 = new ArrayList<String>();
			List<String> tnL2 = new ArrayList<String>();
			List<String> tnL3 = new ArrayList<String>();
			List<String> tnL4 = new ArrayList<String>();
			list = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",stime1,etime1);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime1);
					//获取相差年分的分区集合，默认加1
					tnL1 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",yNum+1);
					tnL1.add(0,"T_INPATIENT_BALANCEHEAD_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL1.add("T_INPATIENT_BALANCEHEAD_NOW");
			}
			
			if(DateUtils.compareDate(sTime2, cTime)==-1){
				if(DateUtils.compareDate(eTime2, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",stime2,etime2);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),stime2);
					//获取相差年分的分区集合，默认加1
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",yNum+1);
					tnL2.add(0,"T_INPATIENT_BALANCEHEAD_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL2.add("T_INPATIENT_BALANCEHEAD_NOW");
			}
			
			if(DateUtils.compareDate(lastsTime1, cTime)==-1){
				if(DateUtils.compareDate(lasteTime1, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL3 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",lastSTime1,lastETime1);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),lastSTime1);
					//获取相差年分的分区集合，默认加1
					tnL3 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",yNum+1);
					tnL3.add(0,"T_INPATIENT_BALANCEHEAD_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL3.add("T_INPATIENT_BALANCEHEAD_NOW");
			}
			
			if(DateUtils.compareDate(lastsTime2, cTime)==-1){
				if(DateUtils.compareDate(lasteTime2, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL4 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",lastSTime2,lastETime2);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),lastSTime2);
					//获取相差年分的分区集合，默认加1
					tnL4 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_BALANCEHEAD",yNum+1);
					tnL4.add(0,"T_INPATIENT_BALANCEHEAD_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL4.add("T_INPATIENT_BALANCEHEAD_NOW");
			}
			String str1=tnL1.get(0);
			String str2=tnL2.get(0);
			String str3=tnL3.get(0);
			String str4=tnL4.get(0);
			list.add(str1+"/"+time1);
			list.add(str2+"/"+time2);
			list.add(str3+"/"+lastM1);
			list.add(str4+"/"+lastM2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return inpatientIncomeDao.queryInpatientIncomeVo(list);
	}
	/**  
	 * 
	 * 封装求上一年同月
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午9:40:13 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午9:40:13 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	private String getLastYear(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		Date stime = null;
		try {
			stime = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        calendar.setTime(stime);
        calendar.add(Calendar.YEAR, -1);
        Date d = calendar.getTime();
        //上年
        String lastDate = format.format(d);
		return lastDate;
	}
	/**
	 * 根据科室id查询科室名称
	 */
	public String getDeptName(String deptCode){
		SysDepartment dept = deptInInterDAO.get(deptCode);
		if(StringUtils.isNotBlank(dept.getDeptName())){
			return dept.getDeptName();
		}
		return null;
	}
	/*******************************************************mongodb*************************************************/
	/**  
	 * 
	 *  从mongodb中查询住院收入分析表
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月18日 下午3:28:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月18日 下午3:28:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryVoByMongo(String time) {
		mbDao = new MongoBasicDao();
		List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", time);
		DBCursor cursor = mbDao.findAlldata(TABLENAME, bdObject);
		while(cursor.hasNext()){
			vo = new OutpatientUseMedicVo();
			DBObject dbCursor = cursor.next();
			String regDpcdName =(String) dbCursor.get("dept");
			Double cost1 =(Double) dbCursor.get("drugCost");
			Double cost2 =(Double) dbCursor.get("noDrugCost");
			vo.setRegDpcdName(regDpcdName);
			vo.setCost1(cost1);
			vo.setCost2(cost2);
			vos.add(vo);
		}
		if(vos!=null&&vos.size()>0){
			return vos;
		}else{
			return new ArrayList<OutpatientUseMedicVo>();
		}
	}
	
	/**  
	 * 
	 * 通过时间和科室从mongodb中查询住院收入分析表
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月18日 下午3:28:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月18日 下午3:28:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo queryOneVoByMongo(String time, String deptName) {
		mbDao = new MongoBasicDao();
		List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
		OutpatientUseMedicVo vo=null;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", time);
		bdObject.append("dept", deptName);
		DBCursor cursor = mbDao.findAlldata(TABLENAME, bdObject);
		while(cursor.hasNext()){
			vo = new OutpatientUseMedicVo();
			DBObject dbCursor = cursor.next();
			String regDpcdName =(String) dbCursor.get("dept");
			Double cost1 =(Double) dbCursor.get("drugCost");
			Double cost2 =(Double) dbCursor.get("noDrugCost");
			vo.setRegDpcdName(regDpcdName);
			vo.setCost1(cost1);
			vo.setCost2(cost2);
			vos.add(vo);
		}
		if(vos!=null&&vos.size()>0){
			return vos.get(0);
		}else{
			return new OutpatientUseMedicVo();
		}
	}
	
	/**  
	 * 
	 * 住院收入分析（new）
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:45:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:45:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public List<OutpatientUseMedicVo> queryOutpatientUseMedicVo(String time) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		List<String> tnL = null;
		List<String> tnL2 = null;
		String begin=null;
		String end=null;
		try {
			Date stime1 = format.parse(time);
			Calendar a=Calendar.getInstance();
			Calendar a2=Calendar.getInstance();
		    a.setTime(stime1); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=time+"-01";
			end=time+"-"+MaxDate;
			
			Date sTime = format2.parse(begin);
			Date eTime = format2.parse(end);
			
			//获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//获得当前时间
			Date dTime = format2.parse(format2.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			tnL = new ArrayList<String>();
			tnL2 = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",yNum+1);
					tnL.add(0,"T_INPATIENT_MEDICINELIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_MEDICINELIST_NOW");
			}
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL2.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL2.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return inpatientIncomeDao.queryOutpatientUseMedicVo(tnL,tnL2,begin,end);
	}
	/**  
	 * 
	 *  住院收入分析（new）,根据时间和科室查
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午10:49:54 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午10:49:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @throws Exception 
	 *
	 */
	@Override
	public OutpatientUseMedicVo queryOneOutpatientUseMedicVo(String time,
			String deptName) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		List<String> tnL = null;
		List<String> tnL2 = null;
		String begin=null;
		String end=null;
		try {
			Date stime1 = format.parse(time);
			Calendar a=Calendar.getInstance();
			Calendar a2=Calendar.getInstance();
		    a.setTime(stime1); 
		    a.set(Calendar.DATE, 1);     //把日期设置为当月第一天
		    a.roll(Calendar.DATE, -1);   //日期回滚一天，也就是最后一天
		    int MaxDate=a.get(Calendar.DATE);
		    begin=time+"-01";
			end=time+"-"+MaxDate;
			
			Date sTime = format2.parse(begin);
			Date eTime = format2.parse(end);
			
			//获取住院数据保留时间
			String dateNum = parameterInnerDAO.getParameterByCode("saveTime");
			if("1".equals(dateNum)){
				dateNum="30";
			}
			//获得当前时间
			Date dTime = format2.parse(format2.format(new Date()));
			//获得在线库数据应保留最小时间
			Date cTime = DateUtils.addDay(dTime, -Integer.parseInt(dateNum)+1);
			
			tnL = new ArrayList<String>();
			tnL2 = new ArrayList<String>();
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_MEDICINELIST",yNum+1);
					tnL.add(0,"T_INPATIENT_MEDICINELIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL.add("T_INPATIENT_MEDICINELIST_NOW");
			}
			
			//判断查询类型
			if(DateUtils.compareDate(sTime, cTime)==-1){
				if(DateUtils.compareDate(eTime, cTime)==-1){//1.只查询分区（查询的开始时间小于表中的最小时间&&查询的结束时间小于表中的最小时间）
					//获取需要查询的全部分区
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",begin,end);
				}else{//2.查询主表加分区表（查询的开始时间小于表中的最小时间&&查询的结束时间大于或等于表中的最小时间）
					//获得时间差(年)
					int yNum = DateUtils.yearDateDiff(DateUtils.formatDateY_M_D(cTime),begin);
					//获取相差年分的分区集合，默认加1
					tnL2 = ZoneManageUtil.getInstance().getZoneNameListForNum(HisParameters.HISONLINEDB,"T_INPATIENT_ITEMLIST",yNum+1);
					tnL2.add(0,"T_INPATIENT_ITEMLIST_NOW");
				}
			}else{//3.只查询主表（查询的开始时间大或等于表中的最小时间）
				tnL2.add("T_INPATIENT_ITEMLIST_NOW");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return inpatientIncomeDao.queryOneOutpatientUseMedicVo(tnL,tnL2,begin,end,deptName);
	}
	/**  
	 * 
	 * 将科室的name渲染成code
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 上午11:58:33 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 上午11:58:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public Map<String, String> querydeptCodeAndNameMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<SysDepartment> list=null;
			list = deptInInterDAO.getAll();
		if(list!=null&&list.size()>0){
			for(SysDepartment dept : list){
				map.put( dept.getDeptName(),dept.getDeptCode());
			}
		}
		return map;
	}
}
