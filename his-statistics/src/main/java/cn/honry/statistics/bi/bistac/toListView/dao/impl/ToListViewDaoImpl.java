package cn.honry.statistics.bi.bistac.toListView.dao.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.statistics.wordLoadDoctorTotal.dao.WordLoadDocDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.bi.bistac.toListView.dao.ToListViewDao;
import cn.honry.statistics.bi.bistac.toListView.vo.RegisterWorkVo;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListView;
import cn.honry.statistics.bi.bistac.toListView.vo.ToListViewVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.statistics.util.echartsVo.PieVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.NumberUtil;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("toListViewDao")
@SuppressWarnings({ "all" })
public class ToListViewDaoImpl extends HibernateEntityDao<ToListViewVo> implements ToListViewDao{
	private DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	private final String[] registerMain={"T_REGISTER_MAIN_NOW","T_REGISTER_MAIN"};//挂号主表
	private final String[] outFee={"T_OUTPATIENT_FEEDETAIL_NOW","T_OUTPATIENT_FEEDETAIL"};//挂号主表
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource(name = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;
	
	@Resource(name = "client")
	private Client client;

	@Value("${es.register_main.index}")
	private String register_main_index;

	@Value("${es.register_main.type}")
	private String register_main_type;
	
	
	@Autowired
	@Qualifier(value="wordLoadDocDao")
	private WordLoadDocDao wordLoadDocDao;
	
	@Value("${es.outpatient_feedetail.index}")
	private String outpatient_feedetail_index;

	@Value("${es.outpatient_feedetail.type}")
	private String outpatient_feedetail_type;
	public void setWordLoadDocDao(WordLoadDocDao wordLoadDocDao) {
		this.wordLoadDocDao = wordLoadDocDao;
	}
	/**  
	 * 
	 * 门急诊人次统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 下午2:38:18 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 下午2:38:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ToListViewVo queryVo(List<String> tnL,final String date, final String lastM, final String lastY) {
		
		final StringBuffer sb = new StringBuffer();
		sb.append("select (select count(t.id) from ").append(tnL.get(0)).append(" t ").append(" where ");
		if(StringUtils.isNotBlank(date)){
			sb.append("trunc(t.REG_DATE, 'dd')=to_date(:date,'yyyy-MM-dd') and ");
		}
		sb.append(" t.valid_flag=1 and t.in_state=0 and t.trans_type=1 and t.del_flg=0 and t.stop_flg=0) as outpatientD,");
		sb.append("(select count(t.id) from ").append(tnL.get(0)).append(" t ").append(" where ");
		if(StringUtils.isNotBlank(lastM)){
			sb.append("trunc(t.REG_DATE, 'dd')=to_date(:lastM,'yyyy-MM-dd') and ");
		}
		sb.append(" t.valid_flag=1 and t.in_state=0 and t.trans_type=1 and t.del_flg=0 and t.stop_flg=0) as outpatientLastM,");
		sb.append("(select count(t.id) from ").append(tnL.get(0)).append(" t ").append(" where ");
		if(StringUtils.isNotBlank(lastY)){
			sb.append("trunc(t.REG_DATE, 'dd')=to_date(:lastY,'yyyy-MM-dd') and ");
		}
		sb.append(" t.valid_flag=1 and t.in_state=0 and t.trans_type=1 and t.del_flg=0 and t.stop_flg=0) as outpatientLastY,");
		sb.append(" (select count(t.id) from ").append(tnL.get(0)).append(" t ").append(" where ");
		if(StringUtils.isNotBlank(date)){
			sb.append("trunc(t.REG_DATE, 'dd')=to_date(:date,'yyyy-MM-dd') and ");
		}
		sb.append(" t.dept_code in (select dept_code from t_department where dept_name like '%急诊%' and stop_flg = 0 and del_flg = 0) ");
		sb.append(" and t.valid_flag=1 and t.in_state=0 and t.trans_type=1 and t.del_flg=0 and t.stop_flg=0) as emergencyD,");
		sb.append(" (select count(t.id) from ").append(tnL.get(0)).append(" t ").append(" where ");
		if(StringUtils.isNotBlank(lastM)){
			sb.append("trunc(t.REG_DATE, 'dd')=to_date(:lastM,'yyyy-MM-dd') and ");
		}
		sb.append(" t.dept_code in (select dept_code from t_department where dept_name like '%急诊%' and stop_flg = 0 and del_flg = 0) ");
		sb.append(" and t.valid_flag=1 and t.in_state=0 and t.trans_type=1 and t.del_flg=0 and t.stop_flg=0) as emergencyLastM,");
		sb.append(" (select count(t.id) from ").append(tnL.get(0)).append(" t ").append(" where ");
		if(StringUtils.isNotBlank(lastY)){
			sb.append("trunc(t.REG_DATE, 'dd')=to_date(:lastY,'yyyy-MM-dd') and ");
		}
		sb.append(" t.dept_code in (select dept_code from t_department where dept_name like '%急诊%' and stop_flg = 0 and del_flg = 0) ");
		sb.append(" and t.valid_flag=1 and t.in_state=0 and t.trans_type=1 and t.del_flg=0 and t.stop_flg=0) as emergencyLastY ");
		sb.append(" from dual");
		ToListViewVo vo = (ToListViewVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public ToListViewVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
					queryObject.addScalar("outpatientD",Hibernate.INTEGER);
					queryObject.addScalar("outpatientLastM",Hibernate.INTEGER);
					queryObject.addScalar("outpatientLastY",Hibernate.INTEGER);
					queryObject.addScalar("emergencyD",Hibernate.INTEGER);
					queryObject.addScalar("emergencyLastM",Hibernate.INTEGER);
					queryObject.addScalar("emergencyLastY",Hibernate.INTEGER);
					queryObject.setParameter("date", date);
					queryObject.setParameter("lastM", lastM);
					queryObject.setParameter("lastY", lastY);
				return (ToListViewVo) queryObject.setResultTransformer(Transformers.aliasToBean(ToListViewVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	@Override
	public ToListViewVo findMaxMin() {
		final String sql = "SELECT MAX(mn.REG_DATE) AS eTime ,MIN(mn.REG_DATE) AS sTime FROM T_REGISTER_MAIN_NOW mn";
		ToListViewVo vo = (ToListViewVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public ToListViewVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime",Hibernate.DATE).addScalar("sTime",Hibernate.DATE);
				return (ToListViewVo) queryObject.setResultTransformer(Transformers.aliasToBean(ToListViewVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	@Override
	public ToListViewVo queryVoByMongo(String date, String lastM, String lastY) {
		ToListViewVo vo = new ToListViewVo();
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("regdate", date);
		DBCursor cursor = new MongoBasicDao().findAlldata("T_TJ_MZJZRCTJ", bdObject);
		while(cursor.hasNext()){
			DBObject dbCursor = cursor.next();
			Integer outpatientD = (Integer) dbCursor.get("outpatientD");
			Integer emergencyD = (Integer) dbCursor.get("emergencyD");
			vo.setOutpatientD(outpatientD);
			vo.setEmergencyD(emergencyD);
		}
		bdObject.append("regdate", lastM);
		DBCursor cursor1 = new MongoBasicDao().findAlldata("T_TJ_MZJZRCTJ", bdObject);
		while(cursor1.hasNext()){
			DBObject dbCursor = cursor1.next();
			Integer outpatientD = (Integer) dbCursor.get("outpatientD");
			Integer emergencyD = (Integer) dbCursor.get("emergencyD");
			vo.setOutpatientLastM(outpatientD);
			vo.setEmergencyLastM(emergencyD);
		}
		bdObject.append("regdate", lastY);
		DBCursor cursor2 = new MongoBasicDao().findAlldata("T_TJ_MZJZRCTJ", bdObject);
		while(cursor2.hasNext()){
			DBObject dbCursor = cursor2.next();
			Integer outpatientD = (Integer) dbCursor.get("outpatientD");
			Integer emergencyD = (Integer) dbCursor.get("emergencyD");
			vo.setOutpatientLastY(outpatientD);
			vo.setEmergencyLastY(emergencyD);
		}
		return vo;
	}
	
	@Override
	public List<PieVo> queryAreaByES(Date sTime, Date eTime){
		try{
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//			boolQuery.filter(QueryBuilders.termQuery("trans_type", 1));//交易类型,1正交易，2反交易
//			boolQuery.filter(QueryBuilders.termQuery("del_flg", 0));//删除标志 0-正常 1-删除 默认0
//			boolQuery.filter(QueryBuilders.termQuery("stop_flg", 0));//停用标志 0-启用 1-停用 默认0
			boolQuery.filter(QueryBuilders.termQuery("in_state", 0));//状态[0正常，1换科，2退号，3退费]
			boolQuery.filter(QueryBuilders.termQuery("valid_flag", 1));//0退费,1有效,2作废
			boolQuery.filter(QueryBuilders.rangeQuery("reg_date").gte(sTime).lt(eTime));//查询时间
			
			SearchResponse searchResponse = client.prepareSearch(register_main_index)
					.setTypes(register_main_type).setQuery(boolQuery)
					.addAggregation(AggregationBuilders.terms("areaCode").field("area_code").size(0).order(Terms.Order.term(true)))
					.setSize(0).execute().actionGet();
			List<PieVo> areaList = new ArrayList<PieVo>();
			Terms areaCodes = searchResponse.getAggregations().get("areaCode");
			long totalNum = searchResponse.getHits().getTotalHits();
			for(Terms.Bucket areaCode : areaCodes.getBuckets()){
				PieVo areaVo = new PieVo();
				if("2".equals(areaCode.getKeyAsString())||"3".equals(areaCode.getKeyAsString())){
					String areaName = innerCodeDao.getDictionaryByCode("hospitalArea", areaCode.getKeyAsString()).getName();
					long areaCount = areaCode.getDocCount();
					totalNum -= areaCount;
					areaVo.setName(areaName);
					areaVo.setValue(Double.valueOf(areaCount));
					areaList.add(areaVo);
				}
			}
			PieVo pieVo = new PieVo();
			if(totalNum>0){
				pieVo.setName(innerCodeDao.getDictionaryByCode("hospitalArea", "1").getName());
				pieVo.setValue(Double.valueOf(totalNum));
				areaList.add(pieVo);
			}
			return areaList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<PieVo>();
	}
	@Override
	public ToListViewVo queryVoByES(Date thisDay, Date nextDay, String staType) {
		ToListViewVo vo = new ToListViewVo();
		try{
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
			boolQuery
//			.filter(QueryBuilders.termQuery("ynregchrg", 1))//挂号收费标志 1是/0否
//			.filter(QueryBuilders.termQuery("valid_flag", 1))//0退费,1有效,2作废
			.filter(QueryBuilders.termQuery("trans_type", 1));//交易类型,1正交易，2反交易
			
			Date thisDayOfLastMonth = null;
			Date nextDayOfLastMonth = null;
			Date thisDayOfLastYear  = null;
			Date nextDayOfLastYear  = null;

		switch (Integer.parseInt(staType)) {
		case 1://按天
			thisDayOfLastMonth = DateUtils.addMonth(thisDay, -1);
			nextDayOfLastMonth = DateUtils.addDay(thisDayOfLastMonth, 1);
			thisDayOfLastYear = DateUtils.addMonth(thisDay, -12);
			nextDayOfLastYear = DateUtils.addDay(thisDayOfLastYear, 1);
			break;
		case 2://按月
			thisDay = DateUtils.parseDateY_M_D(DateUtils.formatDateYM(thisDay)+"-01");//本期月初
			nextDay = DateUtils.addMonth(thisDay, 1);             //本期月末
			thisDayOfLastMonth = DateUtils.addMonth(thisDay, -1);                      //环比上期月初
			nextDayOfLastMonth = thisDay;                        //环比上期月末
			thisDayOfLastYear = DateUtils.addMonth(thisDay, -12);                      //同比上期月初
			nextDayOfLastYear = DateUtils.addMonth(nextDay, -12);                      //同比上去月末
			break;
		case 3://按年
			thisDay = DateUtils.parseDateY_M_D(DateUtils.formatDateY(thisDay)+"-01-01");//本期年初
			nextDay = DateUtils.addMonth(thisDay, 12);                             //本期年末
			thisDayOfLastMonth = DateUtils.addMonth(thisDay, -12);                                      //环比上期年初
			nextDayOfLastMonth = thisDay;                                         //环比上期年末
			break;
		case 4://自定义
			int dayNum = 1 - DateUtils.subDateGetDay(thisDay, nextDay);
			thisDayOfLastMonth = DateUtils.addDay(thisDay, dayNum);                  //环比上期年初
			nextDayOfLastMonth = DateUtils.addMonth(nextDay, dayNum);                    //环比上期年末
			break;
		case 5:
			thisDay = DateUtils.addMonth(DateUtils.parseDateY_M_D(DateUtils.formatDateY_M(thisDay).split("-")[0]+"-"+DateUtils.formatDateY_M(thisDay).split("-")[1]+"-01"),-2);//本期月初
			nextDay = DateUtils.addDay(DateUtils.addMonth(thisDay, 1),-1);             //本期月末
			thisDayOfLastMonth = DateUtils.addMonth(thisDay, -1);                      //环比上期月初
			nextDayOfLastMonth = DateUtils.addDay(thisDay, -1);                        //环比上期月末
			thisDayOfLastYear = DateUtils.addMonth(thisDay, -12);                      //同比上期月初
			nextDayOfLastYear = DateUtils.addMonth(nextDay, -12);                      //同比上去月末
			break;
		case 6:
			thisDay = DateUtils.addMonth(DateUtils.parseDateY_M_D(DateUtils.formatDateY_M(thisDay).split("-")[0]+"-"+DateUtils.formatDateY_M(thisDay).split("-")[1]+"-01"),-4);//本期月初
			nextDay = DateUtils.addDay(DateUtils.addMonth(thisDay, 1),-1);             //本期月末
			thisDayOfLastMonth = DateUtils.addMonth(thisDay, -1);                      //环比上期月初
			nextDayOfLastMonth = DateUtils.addDay(thisDay, -1);                        //环比上期月末
			thisDayOfLastYear = DateUtils.addMonth(thisDay, -12);                      //同比上期月初
			nextDayOfLastYear = DateUtils.addMonth(nextDay, -12);                      //同比上去月末
			break;

			default:
				break;
			}
			
			SearchResponse searchResponse = client.prepareSearch(register_main_index)
					.setTypes(register_main_type).setQuery(boolQuery)
							.addAggregation(AggregationBuilders.terms("emergencyFlag").field("emergency_flag").size(0)
									.subAggregation(AggregationBuilders.dateRange("regDate").field("reg_date")
											.addRange(thisDayOfLastYear, nextDayOfLastYear)
											.addRange(thisDayOfLastMonth, nextDayOfLastMonth)
											.addRange(thisDay, nextDay)))
											.setSize(0).execute().actionGet();
			    Terms emergencys = searchResponse.getAggregations().get("emergencyFlag");
			    for(Terms.Bucket emergency : emergencys.getBuckets()){
			    	Range agg = emergency.getAggregations().get("regDate");
				if ((double) emergency.getKey() == 0) {
					vo.setOutpatientLastY((int) agg.getBuckets().get(0).getDocCount());
					vo.setOutpatientLastM((int) agg.getBuckets().get(1).getDocCount());
					vo.setOutpatientD((int) agg.getBuckets().get(2).getDocCount());
				} else if ((double) emergency.getKey() == 1) {
					vo.setEmergencyLastY((int) agg.getBuckets().get(0).getDocCount());
					vo.setEmergencyLastM((int) agg.getBuckets().get(1).getDocCount());
					vo.setEmergencyD((int) agg.getBuckets().get(2).getDocCount());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return vo;
		}
		    
		return vo;
	}
	@Override
	public ToListView querySixMomYoyByES(Date thisDay, Date nextDay,
			String staType) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.filter(QueryBuilders.termQuery("trans_type", 1));//交易类型,1正交易，2反交易
		
		Date lastTimeOne1 = null;
		Date lastTimeOne2 = null;
		Date lastTimeTwo1 = null;
		Date lastTimeTwo2 = null;
		Date lastTimeThr1 = null;
		Date lastTimeThr2 = null;
		Date lastTimeFou1 = null;
		Date lastTimeFou2 = null;
		Date lastTimeFiv1 = null;
		Date lastTimeFiv2 = null;
		Date lastTimeSix1 = null;
		Date lastTimeSix2 = null;
		
		ToListView vo = new ToListView();

		switch (Integer.parseInt(staType)) {
		case 1://按天---环比
			lastTimeOne1 = thisDay;
			lastTimeOne2 = nextDay;
			lastTimeTwo1 = DateUtils.addDay(lastTimeOne1, -1);            
			lastTimeTwo2 = DateUtils.addDay(lastTimeTwo1, 1);
			lastTimeThr1 = DateUtils.addDay(lastTimeTwo1, -1);            
			lastTimeThr2 = DateUtils.addDay(lastTimeThr1, 1);
			lastTimeFou1 = DateUtils.addDay(lastTimeThr1, -1);            
			lastTimeFou2 = DateUtils.addDay(lastTimeFou1, 1);
			lastTimeFiv1 = DateUtils.addDay(lastTimeFou1, -1);            
			lastTimeFiv2 = DateUtils.addDay(lastTimeFiv1, 1);
			lastTimeSix1 = DateUtils.addDay(lastTimeFiv1, -1);            
			lastTimeSix2 = DateUtils.addDay(lastTimeSix1, 1);
			break;
		case 2://按天----同比
			lastTimeOne1 = thisDay;
			lastTimeOne2 = nextDay;
			lastTimeTwo1 = DateUtils.addYear(lastTimeOne1, -1);            
			lastTimeTwo2 = DateUtils.addDay(lastTimeTwo1, 1);
			lastTimeThr1 = DateUtils.addYear(lastTimeTwo1, -1);            
			lastTimeThr2 = DateUtils.addDay(lastTimeThr1, 1);
			lastTimeFou1 = DateUtils.addYear(lastTimeThr1, -1);            
			lastTimeFou2 = DateUtils.addDay(lastTimeFou1, 1);
			lastTimeFiv1 = DateUtils.addYear(lastTimeFou1, -1);            
			lastTimeFiv2 = DateUtils.addDay(lastTimeFiv1, 1);
			lastTimeSix1 = DateUtils.addYear(lastTimeFiv1, -1);            
			lastTimeSix2 = DateUtils.addDay(lastTimeSix1, 1);
			break;
		case 3://按月---环比
			lastTimeOne1 = thisDay;
			lastTimeOne2 = nextDay;
			lastTimeTwo1 = DateUtils.addMonth(lastTimeOne1, -1);            
			lastTimeTwo2 = DateUtils.addMonth(lastTimeTwo1, 1);
			lastTimeThr1 = DateUtils.addMonth(lastTimeTwo1, -1);                                  
			lastTimeThr2 = DateUtils.addMonth(lastTimeThr1, 1);  
			lastTimeFou1 = DateUtils.addMonth(lastTimeThr1, -1);                                 
			lastTimeFou2 = DateUtils.addMonth(lastTimeFou1, 1);  
			lastTimeFiv1 = DateUtils.addMonth(lastTimeFou1, -1);                               
			lastTimeFiv2 = DateUtils.addMonth(lastTimeFiv1, 1);  
			lastTimeSix1 = DateUtils.addMonth(lastTimeFiv1, -1);                           
			lastTimeSix2 = DateUtils.addMonth(lastTimeSix1, 1);  
			break;
		case 4://按月---同比
			lastTimeOne1 = thisDay;
			lastTimeOne2 = nextDay;
			lastTimeTwo1 = DateUtils.addYear(lastTimeOne1, -1);            
			lastTimeTwo2 = DateUtils.addYear(lastTimeOne2, -1);    
			lastTimeThr1 = DateUtils.addYear(lastTimeTwo1, -1);             
			lastTimeThr2 = DateUtils.addYear(lastTimeTwo2, -1);      
			lastTimeFou1 = DateUtils.addYear(lastTimeThr1, -1);             
			lastTimeFou2 = DateUtils.addYear(lastTimeThr2, -1);       
			lastTimeFiv1 = DateUtils.addYear(lastTimeFou1, -1);           
			lastTimeFiv2 = DateUtils.addYear(lastTimeFou2, -1);       
			lastTimeSix1 = DateUtils.addYear(lastTimeFiv1, -1);      
			lastTimeSix2 = DateUtils.addYear(lastTimeFiv1, -1);      
			break;

		default:
			break;
		}
		
		try{
			SearchResponse searchResponse = client.prepareSearch(register_main_index)
					.setTypes(register_main_type).setQuery(boolQuery)
							.addAggregation(AggregationBuilders.terms("emergencyFlag").field("emergency_flag").size(0)
									.subAggregation(AggregationBuilders.dateRange("regDate").field("reg_date")
											.addRange(lastTimeSix1, lastTimeSix2)
											.addRange(lastTimeFiv1, lastTimeFiv2)
											.addRange(lastTimeFou1, lastTimeFou2)
											.addRange(lastTimeThr1, lastTimeThr2)
											.addRange(lastTimeTwo1, lastTimeTwo2)
											.addRange(lastTimeOne1, lastTimeOne2)))
											.setSize(0).execute().actionGet();
			    Terms emergencys = searchResponse.getAggregations().get("emergencyFlag");
			    for(Terms.Bucket emergency : emergencys.getBuckets()){
			    	Range agg = emergency.getAggregations().get("regDate");
				if ((double) emergency.getKey() == 0) {
					vo.setNowMJNum(String.valueOf(agg.getBuckets().get(0).getDocCount()));
					vo.setNowMJNumB1(String.valueOf(agg.getBuckets().get(1).getDocCount()));
					vo.setNowMJNumB2(String.valueOf(agg.getBuckets().get(2).getDocCount()));
					vo.setNowMJNumB3(String.valueOf(agg.getBuckets().get(3).getDocCount()));
					vo.setNowMJNumB4(String.valueOf(agg.getBuckets().get(4).getDocCount()));
					vo.setNowMJNumB5(String.valueOf(agg.getBuckets().get(5).getDocCount()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return vo;
		}
		    
		return vo;
	}
	@Override
	public void initPcRegisterDoctorWorkTotal(String date, String dateSign) {
		Date thisDay=null;
		Date nextDay=null;
		
		
		thisDay=DateUtils.parseDateY_M_D(date);
		nextDay=DateUtils.addDay(thisDay, 1);
		String eTime=DateUtils.formatDateY_M_D(nextDay);
        //查询PC端门诊医生工作量门急诊、处方量 日统计  
		List<String> registerList=wordLoadDocDao.returnInTables(date, eTime, registerMain, "MZ");//获取分区
		List<String> outFeeList=wordLoadDocDao.returnInTables(date, eTime, outFee, "MZ");
		StringBuffer buffer=new StringBuffer();
		if(registerList!=null&&registerList.size()>0){
			buffer.append("select f.doct_code docCode,f.dept_code deptCode,f.ghzj ghzj,f.jzgh jzgh,f.mzgh mzgh,f.feeCost feeCost, ");
			buffer.append("(select dept_name from t_department where dept_code = f.dept_code) deptName, ");
			buffer.append("(select employee_name from t_employee where employee_jobNo = f.doct_code) docName,");
			buffer.append(" nvl(d.cfs, 0) cfs ");
			
			buffer.append("from ( ");
			
			buffer.append("select distinct count(f.emergency_flag) ghzj,sum(f.emergency_flag) jzgh,(count(f.emergency_flag)-sum(f.emergency_flag)) mzgh,f.dept_code,f.doct_code,sum(f.feeCost) feeCost ");
			buffer.append(" from ( ");
			for(int i=0,len=registerList.size();i<len;i++){
				if(i>0){
					buffer.append(" union all ");
				}
				buffer.append(" select decode(substr(t"+i+".dept_name, 0, 1), '急', 1, 0) as emergency_flag, t"+i+".dept_code,t"+i+".doct_code,(t"+i+".OTH_FEE + t"+i+".diag_fee + t"+i+".chck_fee + t"+i+".reg_fee) feeCost ");
				buffer.append(" from  "+registerList.get(i)+" t"+i+" ");
				buffer.append(" where t"+i+".valid_flag = 1 and t"+i+".in_state = 0 and t"+i+".trans_type = 1 and t"+i+".del_flg = 0 and t"+i+".stop_flg = 0  and t"+i+".reg_date >= to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append(" and t"+i+".reg_date <= to_date('"+date+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			}
			buffer.append(" ) f  ");
			buffer.append("  group by f.dept_code, f.doct_code) f left join ( select t.doct_code, t.doct_dept, count(t.cfs) cfs from( ");
			for(int i=0,len=outFeeList.size();i<len;i++){
				if(i>0){
					buffer.append(" union all  ");
				}
				buffer.append(" select t"+i+".doct_code, t"+i+".doct_dept,count(t"+i+".recipe_no) cfs ");
				buffer.append(" from "+outFeeList.get(i)+" t"+i+" ");
				buffer.append(" where t"+i+".reg_date >= to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append(" and t"+i+".reg_date <= to_date('"+date+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
				buffer.append("  group by t"+i+".recipe_no, t"+i+".doct_code, t"+i+".doct_dept ");
			}
			buffer.append(" ) t group by t.doct_code, t.doct_dept) d on d.doct_code = f.doct_code and d.doct_dept = f.dept_code ");
			List<RegisterWorkVo> list=this.getSession().createSQLQuery(buffer.toString()).addScalar("deptCode")
							.addScalar("deptName").addScalar("docCode").addScalar("docName").addScalar("ghzj",Hibernate.INTEGER)
							.addScalar("jzgh",Hibernate.INTEGER).addScalar("mzgh",Hibernate.INTEGER).addScalar("feeCost",Hibernate.DOUBLE)
							.addScalar("cfs",Hibernate.INTEGER)
							.setResultTransformer(Transformers.aliasToBean(RegisterWorkVo.class)).list();
			Map<String,RegisterWorkVo> map=new HashMap<String,RegisterWorkVo>();
			if(list.size()>0){
				for(int i=0,len=list.size();i<len;i++){
					RegisterWorkVo vo=list.get(i);
					String key=vo.getDeptCode()+"-"+vo.getDocCode();
					map.put(key, vo);
				}
				BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("fee_date")
	                    .gte(thisDay)
	                    .lt(nextDay));
				boolQuery.filter(QueryBuilders.termQuery("pay_flag", 1));
				//查询当天挂号医生
				 SearchRequestBuilder searchBuilder =client.prepareSearch(outpatient_feedetail_index)
		                .setTypes(outpatient_feedetail_type).setQuery(boolQuery)
		                .addAggregation(AggregationBuilders.terms("regDpcd").field("reg_dpcd").size(0).missing("未知科室")
		                        .subAggregation(AggregationBuilders.terms("deptName").field("dept_name").size(0).missing("未知科室"))
		                        .subAggregation(AggregationBuilders.terms("doctCode").field("doct_code").size(0).missing("未知医生")
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
		            	RegisterWorkVo vo = map.get(key);
		            	if(vo==null){
		            		Terms deptNames = regDpcd.getAggregations().get("deptName");
			            	String deptName=(String) deptNames.getBuckets().get(0).getKey();
			            	Terms docNames = doct.getAggregations().get("doctName");
			            	String docName=null;
			            	if(docNames.getBuckets().size()>0){
			            		docName=(String)docNames.getBuckets().get(0).getKey();
		 		            }
		            		 vo=new RegisterWorkVo();
		 		             vo.setDeptName(deptName);
		 		             vo.setDocName(docName);
			                 vo.setDeptCode(regDpcd.getKeyAsString());
		            		 vo.setDocCode(doct.getKeyAsString());
		            	 }
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
				new MongoBasicDao().remove("MZYS_DETAIL_DAY", query);//删除原来的数据
		        if(map.size()>0){
		        	List<DBObject> voList = new ArrayList<DBObject>();
		        	for(String key :map.keySet()){
		        		RegisterWorkVo vo = map.get(key);
		        		BasicDBObject obj = new BasicDBObject();
		        		obj.append("date", date);
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
		        	new MongoBasicDao().insertDataByList("MZYS_DETAIL_DAY", voList);//添加新数据
		        }
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
    private RegisterWorkVo setCost(RegisterWorkVo vo, double cost, String code) {

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
            case "18"://疫苗费
                vo.setYimiaoCost(cost);
                break;
        }

        return vo;
    }
	@Override
	public void initPcRegisterDoctorWorkTotalMonthAndYear(String date,
			String dateSign) {
		String begin=null;
		String end=null;
		String savemongoCollection=null;
		String querymongoCollection=null;
		if("2".equals(dateSign)){
			begin=date+"-01";
			end=returnEndTime(begin);
			querymongoCollection="MZYS_DETAIL_DAY";
			savemongoCollection="MZYS_DETAIL_MONTH";
		}else{
			begin=date.substring(0,4)+"-01";
			end=date.substring(0,4)+"-12";
			querymongoCollection="MZYS_DETAIL_MONTH";
			savemongoCollection="MZYS_DETAIL_YEAR";
		}
		
		
		BasicDBObject bdbObject = new BasicDBObject();
		BasicDBObject bdObjectTimeS = new BasicDBObject();
		BasicDBObject bdObjectTimeE = new BasicDBObject();
		BasicDBList condList = new BasicDBList();
		
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
		AggregationOutput output = new MongoBasicDao().findGroupBy(querymongoCollection, match, group);
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
		BasicDBObject removeDate=new BasicDBObject();
		removeDate.append("date", date);//删除数据
		new MongoBasicDao().remove(savemongoCollection, removeDate);
		
		List<DBObject> voList = new ArrayList<DBObject>();
		if(list.size()>0){
			for(RegisterWorkVo vo:list){
				BasicDBObject obj = new BasicDBObject();
				obj.append("date", date);
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
			new MongoBasicDao().insertDataByList(savemongoCollection, voList);//添加新数据
		
		}
	}
		public String returnEndTime(String date){
			String end=null;
			date=date.substring(0,7)+"-01";
			Calendar ca=Calendar.getInstance(Locale.CHINESE);
			try {
				ca.setTime(df.parse(date));
				ca.add(Calendar.MONTH, 1);
				ca.add(Calendar.DATE, -1);
				end=df.format(ca.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return end;
		}
}
