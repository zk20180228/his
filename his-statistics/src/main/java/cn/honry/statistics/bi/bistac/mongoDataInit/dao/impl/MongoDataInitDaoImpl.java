package cn.honry.statistics.bi.bistac.mongoDataInit.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import org.bson.BSONObject;
import org.bson.Document;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.MongoCount;
import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.statistics.outpatientIndicatorsPrestreatment.vo.OutpatientIndicatorsVO;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.inner.statistics.totalIncomeCount.vo.TotalIncomeCountVo;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.mongoDataInit.dao.MongoDataInitDao;
import cn.honry.statistics.bi.bistac.mongoDataInit.vo.DoctorWorkCountVo;
import cn.honry.statistics.bi.bistac.mongoDataInit.vo.RegisterInfoMongoVo;
import cn.honry.statistics.doctor.registerInfoGzltj.service.RegisterInfoGzltjService;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;

@Repository("mongoDataInitDao")
@SuppressWarnings({"all"})
public class MongoDataInitDaoImpl extends HibernateDaoSupport implements MongoDataInitDao {
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource(name = "client")
	private Client client;
	
	@Value("${es.register_main.index}")
	private String register_main_index;
	
	@Value("${es.register_main.type}")
	private String register_main_type;
	@Autowired
	@Qualifier(value = "registerInfoGzltjService")
	private RegisterInfoGzltjService registerInfoGzltjService;
	
	private MongoBasicDao mbDao = null;
	
	/**
	 * 初始化门诊各项收入统计中的数据
	 * @param list 初始化数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * 
	 */
	public void init_MZGXSRTJ(List<StatisticsVo> list,String date,Integer type){
		mbDao = new MongoBasicDao();
		MongoLog mong = new MongoLog();//日志
		Date d = null;
		String key="MZGXSRTJ_";//mongodb表前缀
		if(type==null||type==1){
			key+="DAY";
			d=DateUtils.parseDateY_M_D(date);
		}else if(type==2){
			key+="MONTH";
			d=DateUtils.parseDateY_M(date);
		}else{
			key+="YEAR";
			d=DateUtils.parseDateY(date);
		}
		mong.setMenuType(key);//表名称(栏目名)
		mong.setStartTime(d);//开始时间
		mong.setEndTime(d);//结束时间
		mong.setState(1);//状态
		mong.setCreateTime(new Date());
		if (list != null && list.size() > 0) {
			mong.setCountStartTime(new Date());//计算开始时间
			mong.setTotalNum(list.size());//总条数
			List<DBObject> userList = new ArrayList<DBObject>();
			for (StatisticsVo r : list) {
				BasicDBObject document = new BasicDBObject();
				document.append("regDate", date);
				document.append("deptCode", r.getDeptCode());
				document.append("docterCode", r.getDocterCode());
				document.append("name", r.getName());
				document.append("dept", r.getDept());
				document.append("inspectnum", r.getInspectNum().toString());
				document.append("inspectcost", r.getInspectCost().toString());
				document.append("treatmentnum", r.getTreatmentNum().toString());
				document.append("treatmentcost", r.getTreatmentCost().toString());
				document.append("radiationnum", r.getRadiationNum().toString());
				document.append("radiationcost", r.getRadiationCost().toString());
				document.append("bloodnum", r.getBloodNum().toString());
				document.append("bloodcost", r.getBloodCost().toString());
				document.append("testnum", r.getTestNum().toString());
				document.append("testcost", r.getTestCost().toString());
				document.append("othernum", r.getOtherNum().toString());
				document.append("othercost", r.getOtherCost().toString());
				document.append("medicalnum", r.getMedicalNum().toString());
				document.append("medicalcost", r.getMedicalCost().toString());
				document.append("westernnum", r.getWesternNum().toString());
				document.append("westerncost", r.getWesternCost().toString());
				document.append("chinesenum", r.getChineseNum().toString());
				document.append("chinesecost", r.getChineseCost().toString());
				document.append("herbalnum", r.getHerbalNum().toString());
				document.append("herbalcost", r.getHerbalCost().toString());
				document.append("allnum", r.getAllNum().toString());
				document.append("allcost", r.getAllCost().toString());
				document.append("totle", r.getTotle().toString());
				userList.add(document);
			}
			DBObject query = new BasicDBObject();
			query.put( "regDate",date);
			try {
				mbDao.remove(key, query);//删除原来的数据
				mbDao.insertDataByList(key, userList);//将数据插入到mongodb中
			} catch (Exception ex) {
				mong.setState(0);
				ex.printStackTrace();
			}
		}
		mong.setCountEndTime(new Date());//计算结束时间
		this.getHibernateTemplate().save(mong);//保存日志
	}
	
	
	/**
	 * 初始化总收入情况统计中的数据
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 * 
	 */
	public void init_ZSRQKTJ(String json,String date,Integer type){
		mbDao = new MongoBasicDao();
		MongoLog mong = new MongoLog();
		Date d = null;
		String key="ZSRQKTJ_";
		if(type==null||type==1){
			key+="DAY";
			d=DateUtils.parseDateY_M_D(date);
		}else if(type==2){
			key+="MONTH";
			d=DateUtils.parseDateY_M(date);
		}else{
			key+="YEAR";
			d=DateUtils.parseDateY(date);
		}
		mong.setMenuType(key);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());
		Document document = new Document();
		document.append("date", date);
		document.append("value", json);
		DBObject query = new BasicDBObject();
		query.put( "date",date);
		try {
			mbDao.remove(key, query);//删除原来的数据
			mbDao.insertData(key, document);
		} catch (Exception e) {
			mong.setState(0);
			e.printStackTrace();
		}
		mong.setCountEndTime(new Date());//计算结束时间
		this.getHibernateTemplate().save(mong);//保存日志
	}
	
	/**
	 * 初始化门急诊人次统计中的数据
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	public void init_MJZRCTJ(String json,String date,Integer type){
		MongoLog mong = new MongoLog();
		mbDao = new MongoBasicDao();
		Date d = null;
		String key="MJZRCTJ_";
		if(type==null||type==1){
			key+="DAY";
			d=DateUtils.parseDateY_M_D(date);
		}else if(type==2){
			key+="MONTH";
			d=DateUtils.parseDateY_M(date);
		}else{
			key+="YEAR";
			d=DateUtils.parseDateY(date);
		}
		mong.setMenuType(key);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setTotalNum(1);
		mong.setCreateUser(null);
		mong.setCreateDept(null);
		mong.setCreateTime(new Date());
		Document document = new Document();
		document.append("date", date);
		document.append("value", json);
		DBObject query = new BasicDBObject();
		query.put( "date",date);
		try {
			mbDao.remove(key, query);//删除原来的数据
			mbDao.insertData(key, document);
		} catch (Exception e) {
			mong.setState(0);
			e.printStackTrace();
		}
		mong.setCountEndTime(new Date());//计算结束时间
		this.getHibernateTemplate().save(mong);//保存日志
	}
	
	/**
	 * 初始化门诊收入统计中的数据
	 * 栏目别名：SRTJB（原名为：收入统计表）
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	public void init_SRTJB(String json,String date,Integer type){
		mbDao = new MongoBasicDao();
		MongoLog mong = new MongoLog();
		Date d = null;
		String key="SRTJB_";
		if(type==null||type==1){
			key+="DAY";
			d=DateUtils.parseDateY_M_D(date);
		}else if(type==2){
			key+="MONTH";
			d=DateUtils.parseDateY_M(date);
		}else{
			key+="YEAR";
			d=DateUtils.parseDateY(date);
		}
		mong.setMenuType(key);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());
		Document document = new Document();
		document.append("date", date);
		document.append("value", json);
		DBObject query = new BasicDBObject();
		query.put( "date",date);

		try {
			mbDao.remove(key, query);
			mbDao.insertData(key, document);
		} catch (Exception e) {
			mong.setState(0);
			e.printStackTrace();
		}
		mong.setCountEndTime(new Date());//计算结束时间
		this.getHibernateTemplate().save(mong);//保存日志
	}

	/**
	 * 初始化住院收入统计中的数据
	 * 栏目别名：ZYSRTJ
	 * @param json 数据
	 * @param date 日期
	 * @param type 统计类型(1-按日统计；2-按月统计；3-按年统计)
	 */
	@Override
	public void init_ZYSRTJ(String deptAndFeeData, String tonghuanbiData,String pcIncome,String date, Integer type) {
		MongoLog mong = new MongoLog();
		mbDao = new MongoBasicDao();
		Date d = null;
		String key="ZYSRTJ_";
		if(type==null||type==1){
			key+="DAY";
			d=DateUtils.parseDateY_M_D(date);
		}else if(type==2){
			key+="MONTH";
			d=DateUtils.parseDateY_M(date);
		}else{
			key+="YEAR";
			d=DateUtils.parseDateY(date);
		}
		mong.setMenuType(key);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());
		Document document = new Document();
		document.append("date", date);
		//住院收入前十名科室和收费项目饼状图统计
		document.append("deptAndFee", deptAndFeeData);
		//住院收入同环比统计
		document.append("tonghuanbi", tonghuanbiData);
		document.append("pcIncome", pcIncome);
		DBObject query = new BasicDBObject();
		query.put( "date",date);
		try {
			mbDao.remove(key, query);//删除原来的数据
			mbDao.insertData(key, document);
		} catch (Exception e) {
			mong.setState(0);
			e.printStackTrace();
		}
		mong.setCountEndTime(new Date());//计算结束时间
		this.getHibernateTemplate().save(mong);//保存日志
	}
	
	/**
	 * 
	 * <p>医生工作量统计 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年7月17日 下午3:55:10 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月17日 下午3:55:10 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param type 类型，根据他分别将数据存放入对应的集合中，集合分年月日集合
	 * @throws:
	 *
	 */
	public void init_DocterWorkCount(DoctorWorkCountVo vo, Integer type){
		mbDao = new MongoBasicDao();
		MongoLog mong = new MongoLog();
		Date d = null;
		String key="GHYSGZLTJ_";
		String regDate="";
		if(type==null||type==1){
			key+="DAY";
			d=DateUtils.parseDateY_M_D(vo.getRegDate());
			regDate=DateUtils.formatDateY_M_D(d);
		}else if(type==2){
			key+="MONTH";
			d=DateUtils.parseDateY_M(vo.getRegDate());
			regDate=DateUtils.formatDateY_M(d);
		}else{
			key+="YEAR";
			d=DateUtils.parseDateY(vo.getRegDate());
			regDate=DateUtils.formatDateY(d);
		}
		mong.setMenuType(key);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setTotalNum(1);
		mong.setCreateTime(new Date());
		Document document = new Document();
		document.append("regDate",regDate);
		document.append("doctorName", vo.getDoctorName());
		document.append("doctorCode", vo.getDoctorCode());
		document.append("doctorGzlNum", vo.getDoctorGzlNum());
		
		DBObject query = new BasicDBObject();
		query.put("regDate",regDate);
		query.put("doctorCode", vo.getDoctorCode());
		try {
			mbDao.remove(key, query);
			mbDao.insertData(key, document);
		} catch (Exception e) {
			mong.setState(0);
			e.printStackTrace();
		}
		mong.setCountEndTime(new Date());//计算结束时间
		this.getHibernateTemplate().save(mong);//保存日志
		
	}
	
	
	

	@Override
	public MongoCount getMongoCount(final String menuType) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" from MongoCount where stop_flg=0 and del_flg=0 and munyType = :menuType ");
		List<MongoCount> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MongoCount>>() {

			@Override
			public List<MongoCount> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(sb.toString());
				query.setParameter("menuType", menuType);
				return query.list();
			}
		});
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return new MongoCount();
	}
	public void init_GHKSGZLTJ_D_D(String stime,String etime,String type,Map<String, String> deptmap,String date){
		mbDao = new MongoBasicDao();
		String collectionName = "";
		MongoLog log = new MongoLog();
		log.setCountStartTime(new Date());
		log.setCreateTime(new Date());
		log.setUpdateTime(new Date());
		if("1".equals(type)){//日
			log.setEndTime(DateUtils.parseDateY_M_D(stime));
			log.setStartTime(DateUtils.parseDateY_M_D(stime));
			log.setMenuType("GHKSGZLTJ_DAY");
			collectionName = "GHKSGZLTJ_DAY";
		}else if("2".equals(type)){//月
			log.setEndTime(DateUtils.parseDateY_M(stime));
			log.setStartTime(DateUtils.parseDateY_M(stime));
			log.setMenuType("GHKSGZLTJ_MONTH");
			collectionName = "GHKSGZLTJ_MONTH";
		}else if("3".equals(type)){//年
			log.setEndTime(DateUtils.parseDateY(stime));
			log.setStartTime(DateUtils.parseDateY(stime));
			log.setMenuType("GHKSGZLTJ_YEAR");
			collectionName = "GHKSGZLTJ_YEAR";
		}
		try{
			Map<String, Object> byES = registerInfoGzltjService.statRegDorWorkByES(stime, etime, "all", "all", null, null, "GHKSGZLTJ");
			List<RegisterInfoGzltjVo> list = (List<RegisterInfoGzltjVo>)byES.get("rows");
			Map<String,RegisterInfoVo> infoMap = new HashMap<String, RegisterInfoVo>();
			int a = 0;
			for (RegisterInfoGzltjVo esvo : list) {
				if("0620".equals(esvo.getDept())){
					a++;
				}
				if(infoMap.containsKey(esvo.getDept())){
					RegisterInfoVo infoVO = infoMap.get(esvo.getDept());
					infoVO.setDeptCode(esvo.getDept());
					infoVO.setDeptName(deptmap.get(esvo.getDept()));
					infoVO.setCost(infoVO.getCost()+esvo.getCost());
					infoVO.setFriCost(infoVO.getFriCost()+esvo.getFriCost());
					infoVO.setFriNum(infoVO.getFriNum()+esvo.getFriNum());
					infoVO.setMonCost(infoVO.getMonCost()+esvo.getMonCost());
					infoVO.setMonNum(infoVO.getMonNum()+esvo.getMonNum());
					infoVO.setNum(infoVO.getNum()+esvo.getNum());
					infoVO.setSatCost(infoVO.getSatCost()+esvo.getSatCost());
					infoVO.setSatNum(infoVO.getSatNum()+esvo.getSatNum());
					infoVO.setSunCost(infoVO.getSunCost()+esvo.getSunCost());
					infoVO.setSunNum(infoVO.getSunNum()+esvo.getSunNum());
					infoVO.setThuCost(infoVO.getThuCost()+esvo.getThuCost());
					infoVO.setThuNum(infoVO.getThuNum()+esvo.getThuNum());
					infoVO.setTueCost(infoVO.getTueCost()+esvo.getTueCost());
					infoVO.setTueNum(infoVO.getTueNum()+esvo.getTueNum());
					infoVO.setWedCost(infoVO.getWedCost()+esvo.getWedCost());
					infoVO.setWedNum(infoVO.getWedNum()+esvo.getWedNum());
					infoMap.put(esvo.getDept(), infoVO);
				}else{
					RegisterInfoVo infoVO = new RegisterInfoVo();
					infoVO.setDeptCode(esvo.getDept());
					infoVO.setDeptName(deptmap.get(esvo.getDept()));
					infoVO.setCost(esvo.getCost());
					infoVO.setFriCost(esvo.getFriCost());
					infoVO.setFriNum(esvo.getFriNum());
					infoVO.setMonCost(esvo.getMonCost());
					infoVO.setMonNum(esvo.getMonNum());
					infoVO.setNum(esvo.getNum());
					infoVO.setSatCost(esvo.getSatCost());
					infoVO.setSatNum(esvo.getSatNum());
					infoVO.setSunCost(esvo.getSunCost());
					infoVO.setSunNum(esvo.getSunNum());
					infoVO.setThuCost(esvo.getThuCost());
					infoVO.setThuNum(esvo.getThuNum());
					infoVO.setTueCost(esvo.getTueCost());
					infoVO.setTueNum(esvo.getTueNum());
					infoVO.setWedCost(esvo.getWedCost());
					infoVO.setWedNum(esvo.getWedNum());
					infoMap.put(esvo.getDept(), infoVO);
				}
			}
			DBObject obj = new BasicDBObject();
			obj.put("date", date);
			mbDao.remove(collectionName,obj);
			int b = 0;
			for (Entry<String, RegisterInfoVo> vo : infoMap.entrySet()) {
				if("0620".equals(vo.getKey())){
					b++;
				}
				Document doc = new Document();
				doc.append("date", date).append("deptCode", vo.getKey()).append("value",JSONUtils.toJson(vo.getValue()));
				mbDao.insertData(collectionName, doc);
			}
			log.setCountEndTime(new Date());
			log.setState(1);
			log.setTotalNum(infoMap.size());
			this.saveMongoLog(log);
		}catch(Exception e){
			log.setState(0);
			log.setTotalNum(0);
			this.saveMongoLog(log);
		}
	}

	@Override
	public void saveMongoLog(MongoLog log) {
		this.getHibernateTemplate().save(log);
	}


	@Override
	public void init_GHKSGZLTJ_D_S(String stime, String etime, String type,Map<String, String> map) {
		mbDao = new MongoBasicDao();
		String collectionName = null;
		MongoLog mong = new MongoLog();
		mong.setId(null);
		mong.setCountStartTime(new Date());
		mong.setCreateTime(new Date());
		String[] st = stime.split("-");
		String[] et = etime.split("-");
		final String sTime = st.length==2?stime+"-01":st.length==1?stime+"-01-01":stime;
		final String eTime = et.length==2?etime+"-01":et.length==1?etime+"-01-01":etime;
		mong.setEndTime(DateUtils.parseDateY_M_D(sTime));
		mong.setStartTime(DateUtils.parseDateY_M_D(eTime));
		if("1".equals(type)){//日
			mong.setMenuType("GHKSGZLTJ_DAY");
			collectionName = "GHKSGZLTJ_DAY";
		}else if("2".equals(type)){//月
			mong.setMenuType("GHKSGZLTJ_MONTH");
			collectionName = "GHKSGZLTJ_MONTH";
		}else if("3".equals(type)){//年
			mong.setMenuType("GHKSGZLTJ_YEAR");
			collectionName = "GHKSGZLTJ_YEAR";
		}
		try{
			final StringBuffer sb = new StringBuffer();
			sb.append(" Select tab.DEPT_CODE deptCode,NVL(sum(tab.r7),0) sunNum,NVL(sum(tab.r1),0) monNum,NVL(sum(tab.r2),0) tueNum,NVL(sum(tab.r3),0) wedNum,NVL(sum(tab.r4),0) thuNum,NVL(sum(tab.r5),0) friNum,NVL(sum(tab.r6),0) satNum,NVL(sum(tab.r8),0) num,"
					+ "NVL(sum(tab.w7),0.0) sunCost,NVL(sum(tab.w1),0.0) monCost,NVL(sum(tab.w2),0.0) tueCost,NVL(sum(tab.w3),0.0) wedCost,NVL(sum(tab.w4),0.0) thuCost,NVL(sum(tab.w5),0.0) friCost,NVL(sum(tab.w6),0.0) satCost,NVL(sum(tab.w8),0.0) cost From( ");
			sb.append(" Select n.dept_code,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 1, count(1)) as r7 ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 2, count(1)) as r1 ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 3, count(1)) as r2  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 4, count(1)) as r3  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 5, count(1)) as r4  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 6, count(1)) as r5  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 7, count(1)) as r6  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 1, SUM(n.sum_cost)) as w7  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 2, SUM(n.sum_cost)) as w1 ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 3, SUM(n.sum_cost)) as w2 ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 4, SUM(n.sum_cost)) as w3 ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 5, SUM(n.sum_cost)) as w4  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 6, SUM(n.sum_cost)) as w5  ");
			sb.append(" ,DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 7, SUM(n.sum_cost)) as w6  ");
			sb.append(" ,count(1) AS r8 ,SUM(n.sum_cost) as w8 ");
			sb.append(" From t_register_main n Where n.stop_flg = 0 And n.del_flg = 0 and n.YNREGCHRG = 1 and n.IN_STATE = 0 and n.VALID_FLAG = 1 ");
			sb.append(" And n.reg_date >= to_date(:time,'yyyy-MM-dd') and n.reg_date < to_date(:etime,'yyyy-MM-dd') ");
			sb.append("  Group By n.reg_date ,n.DEPT_CODE ) tab Group By DEPT_CODE ");
			List<RegisterInfoVo> list = this.getHibernateTemplate().execute(new HibernateCallback<List<RegisterInfoVo>>() {
				
				@Override
				public List<RegisterInfoVo> doInHibernate(Session session)
						throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(sb.toString());
					query.setParameter("time", sTime);
					query.setParameter("etime", eTime);
					query.addScalar("deptCode").addScalar("monNum",Hibernate.INTEGER).addScalar("monCost",Hibernate.DOUBLE)
					.addScalar("tueNum",Hibernate.INTEGER).addScalar("tueCost",Hibernate.DOUBLE)
					.addScalar("wedNum",Hibernate.INTEGER).addScalar("wedCost",Hibernate.DOUBLE)
					.addScalar("thuNum",Hibernate.INTEGER).addScalar("thuCost",Hibernate.DOUBLE)
					.addScalar("friNum",Hibernate.INTEGER).addScalar("friCost",Hibernate.DOUBLE)
					.addScalar("satNum",Hibernate.INTEGER).addScalar("satCost",Hibernate.DOUBLE)
					.addScalar("sunNum",Hibernate.INTEGER).addScalar("sunCost",Hibernate.DOUBLE)
					.addScalar("num",Hibernate.INTEGER).addScalar("cost",Hibernate.DOUBLE);
					
					return query.setResultTransformer(Transformers.aliasToBean(RegisterInfoVo.class)).list();
				}
			});
			DBObject obj = new BasicDBObject();
			obj.put("date", stime);
			mbDao.remove(collectionName,obj);
			if(list!=null&&list.size()>0){
				for (RegisterInfoVo vo : list) {
					vo.setDeptName(map.get(vo.getDeptCode()));
					//插入新的文档
					Document doc = new Document();
					doc.append("date", stime).append("deptCode", vo.getDeptCode()).append("value",JSONUtils.toJson(vo));
					mbDao.insertData(collectionName, doc);
				}
				mong.setCountEndTime(new Date());
				mong.setTotalNum(list.size());
				mong.setState(1);
				this.saveMongoLog(mong);
			}
			mong.setCountEndTime(new Date());
			mong.setTotalNum(0);
			mong.setState(1);
			this.saveMongoLog(mong);
		}catch(Exception e){
			mong.setCountEndTime(new Date());
			mong.setCountStartTime(new Date());
			mong.setState(0);
			this.saveMongoLog(mong);
		}
		
	}


	@Override
	public List<MongoLog> findMongoLog(final String menuType) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" from MongoLog where stop_flg=0 and del_flg=0 and menuType like :menuType and state = 0 ");
		List<MongoLog> list = this.getHibernateTemplate().execute(new HibernateCallback<List<MongoLog>>() {

			@Override
			public List<MongoLog> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(sb.toString());
				query.setParameter("menuType", menuType+"%");
				return query.list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<MongoLog>();
	}


	@Override
	public void inserDoc(String sTime, String eTime, String date,String collectionName,String inserCollName,Map<String,String> deptmap) {
		mbDao = new MongoBasicDao();
		RegisterInfoVo infoVO = null;
		Map<String,List<RegisterInfoVo>> map = new HashMap<String, List<RegisterInfoVo>>();
		String userCode = "";
		String deptCdoe = "";
		Date startTime = null;
		MongoLog log = new MongoLog();
		log.setCountStartTime(new Date());
		log.setCreateDept(deptCdoe);
		log.setCreateTime(new Date());
		log.setCreateUser(userCode);
		log.setDel_flg(0);
		log.setId(null);
		log.setMenuType(inserCollName);
		log.setState(1);
		log.setStop_flg(0);
		log.setUpdateTime(new Date());
		log.setUpdateUser(userCode);
		try{
			BasicDBList dblist = new BasicDBList();
			BasicDBObject sdb = new BasicDBObject();
			sdb.put("date", new BasicDBObject("$gte", sTime));
			BasicDBObject edb = new BasicDBObject();
			edb.put("date", new BasicDBObject("$lt", eTime));
			dblist.add(sdb);
			dblist.add(edb);
			BasicDBObject where = new BasicDBObject();
			where.put("$and", dblist);
			DBCursor cursor = mbDao.findAlldata(collectionName, where);
			while(cursor.hasNext()){
				Gson gson = new Gson();
				DBObject next = cursor.next();
				Object object = next.get("value");
				RegisterInfoVo vo = gson.fromJson(object.toString(), RegisterInfoVo.class);
				if(map.containsKey(vo.getDeptCode())){
					List<RegisterInfoVo> list2 = map.get(vo.getDeptCode());
					list2.add(vo);
					map.put(vo.getDeptCode(), list2);
				}else{
					List<RegisterInfoVo> list3 = new ArrayList<RegisterInfoVo>();
					list3.add(vo);
					map.put(vo.getDeptCode(), list3);
				}
			}
			List<RegisterInfoVo> infoVOList = new ArrayList<RegisterInfoVo>();
			for (Entry<String, List<RegisterInfoVo>> m : map.entrySet()) {
				infoVO = new RegisterInfoVo();
				infoVO.setDeptCode(m.getKey());
				infoVO.setDeptName(deptmap.get(m.getKey()));
				for (RegisterInfoVo esvo : m.getValue()) {
					infoVO.setCost(infoVO.getCost()+esvo.getCost());
					infoVO.setFriCost(infoVO.getFriCost()+esvo.getFriCost());
					infoVO.setFriNum(infoVO.getFriNum()+esvo.getFriNum());
					infoVO.setMonCost(infoVO.getMonCost()+esvo.getMonCost());
					infoVO.setMonNum(infoVO.getMonNum()+esvo.getMonNum());
					infoVO.setNum(infoVO.getNum()+esvo.getNum());
					infoVO.setSatCost(infoVO.getSatCost()+esvo.getSatCost());
					infoVO.setSatNum(infoVO.getSatNum()+esvo.getSatNum());
					infoVO.setSunCost(infoVO.getSunCost()+esvo.getSunCost());
					infoVO.setSunNum(infoVO.getSunNum()+esvo.getSunNum());
					infoVO.setThuCost(infoVO.getThuCost()+esvo.getThuCost());
					infoVO.setThuNum(infoVO.getThuNum()+esvo.getThuNum());
					infoVO.setTueCost(infoVO.getTueCost()+esvo.getTueCost());
					infoVO.setTueNum(infoVO.getTueNum()+esvo.getTueNum());
					infoVO.setWedCost(infoVO.getWedCost()+esvo.getWedCost());
					infoVO.setWedNum(infoVO.getWedNum()+esvo.getWedNum());
				}
				infoVOList.add(infoVO);
			}
			DBObject obj = new BasicDBObject();
			obj.put("date", date);
			mbDao.remove(inserCollName,obj);
			for (RegisterInfoVo vo : infoVOList) {
				Document doc = new Document();
				doc.append("date", date).append("deptCode", vo.getDeptCode()).append("value",JSONUtils.toJson(vo));
				mbDao.insertData(inserCollName, doc);
			}
			log.setTotalNum(infoVOList.size());
			log.setCountEndTime(new Date());
			log.setUpdateTime(new Date());
			log.setUpdateUser(userCode);
			String[] split = eTime.split("-");
			String[] split1 = sTime.split("-");
			eTime = split.length==2?eTime+"-01":split.length==1?eTime+"-01-01":eTime;
			sTime = split1.length==2?sTime+"-01":split1.length==1?sTime+"-01-01":sTime;
			log.setEndTime(DateUtils.parseDateY_M_D(eTime));
			log.setStartTime(DateUtils.parseDateY_M_D(sTime));
			this.saveMongoLog(log);
		}catch(Exception e){
			e.printStackTrace();
			log.setTotalNum(0);
			log.setState(0);
			log.setCountEndTime(new Date());
			this.saveMongoLog(log);
		}
		
	}


	@Override
	public void insertDocMZGXZBTJ(String date, String insertCollectionName,List<OutpatientIndicatorsVO> list) {
		mbDao = new MongoBasicDao();
		DBObject obj = new BasicDBObject();
		obj.put("date", date);
		mbDao.remove(insertCollectionName,obj);
		Document doc = null;
		for (OutpatientIndicatorsVO vo : list) {
			doc = new Document();
			doc.append("date", date).append("deptCode", vo.getDeptCode()).append("value",JSONUtils.toJson(vo));
			mbDao.insertData(insertCollectionName, doc);
		}
		
	}


	@Override
	public List<OutpatientIndicatorsVO> queryFromMongo(String sDate, String eDate,String collectionName) {
		
		mbDao = new MongoBasicDao();
		List<OutpatientIndicatorsVO> list = new ArrayList<OutpatientIndicatorsVO>();
		try{
			BasicDBList dblist = new BasicDBList();
			BasicDBObject sdb = new BasicDBObject();
			sdb.put("date", new BasicDBObject("$gte", sDate));
			BasicDBObject edb = new BasicDBObject();
			edb.put("date", new BasicDBObject("$lt", eDate));
			dblist.add(sdb);
			dblist.add(edb);
			BasicDBObject where = new BasicDBObject();
			where.put("$and", dblist);
			DBCursor cursor = mbDao.findAlldata(collectionName, where);
			while(cursor.hasNext()){
				DBObject next = cursor.next();
				Object object = next.get("value");
				OutpatientIndicatorsVO vo = JSONUtils.fromJson(object.toString(), OutpatientIndicatorsVO.class);
				list.add(vo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 门诊医生、科室工作量(移动端)初始化预处理
	 */
	@Override
	public void init_MZGZL(String beginDate, String endDate) {
		mbDao = new MongoBasicDao();
		Date sTime = DateUtils.parseDateY_M_D(beginDate);
		Date eTime = DateUtils.parseDateY_M_D(endDate);
		String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
		Date cTime = DateUtils.addDay(new Date(),-Integer.parseInt(dateNum)+1);
		String sTableTime = beginDate + " 00:00:00";
		String eTableTime = beginDate + " 23:59:59";
		
		try{
			while(DateUtils.compareDate(sTime, eTime)==-1){
				List<String> tableList = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_OUTPATIENT_FEEDETAIL",sTableTime,eTableTime);
				String tableName = tableList.get(0);
				//String tableName = "t_outpatient_feedetail";
				if(DateUtils.compareDate(sTime, cTime)!=-1){
					tableName = "t_outpatient_feedetail_now";
				}
				
				 StringBuffer sql1 = new StringBuffer();
				 sql1.append("select a.regDate,a.doctCode,a.doctName,a.deptCode,a.deptName,count(1) cfNum,sum(totCost) totCost from ( ");
				 sql1.append("select trunc(t.reg_date,'dd') as regDate,t.doct_code as doctCode,e.employee_name as doctName,t.doct_dept as deptCode,d.dept_name as deptName,count(t.RECIPE_NO) as cfNum,sum(t.tot_cost) as totCost ");
				 sql1.append("from " +tableName+ " t ");
				 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
				 sql1.append(" inner join t_department d on t.DOCT_DEPT = d.dept_code ");
				 sql1.append("where t.del_flg = 0 and t.stop_flg = 0 and  ");
				 sql1.append("t.reg_date >= to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
				 sql1.append("and t.reg_date <= to_date(:noDay,'yyyy-MM-dd HH24:MI:SS') and t.tot_cost!=0");
				 sql1.append("group by t.doct_code,t.doct_dept,e.employee_name,d.dept_name,trunc(t.reg_date,'dd'),t.RECIPE_NO ");
				 sql1.append("  ) a group by  a.regDate,a.doctCode,a.doctName,a.deptCode,a.deptName ");
				 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
						 .addScalar("regdate",Hibernate.DATE)//开方时间
						 .addScalar("doctCode",Hibernate.STRING)//医生code
						 .addScalar("doctName",Hibernate.STRING)//医生姓名
						 .addScalar("deptCode",Hibernate.STRING)//科室code
						 .addScalar("deptName",Hibernate.STRING)//科室姓名
						 .addScalar("cfNum",Hibernate.INTEGER)//处方数
						 .addScalar("totCost",Hibernate.DOUBLE)//处方金额
						 .setParameter("seDay", sTableTime)
						 .setParameter("noDay", eTableTime)
						 .list();

				 DBObject query = new BasicDBObject();
				 query.put("workdate", beginDate);//移除数据条件
				 new MongoBasicDao().remove("T_TJ_MZCFDAY", query);//删除原来的数据
				 List<DBObject> userList1 = new ArrayList<DBObject>();
				 for(Object[] l : list1){
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("regdate", l[0].toString());
					 bdObject.append("doctCode", l[1]);
					 bdObject.append("doctName", l[2]);
					 bdObject.append("deptCode", l[3]);
					 bdObject.append("deptName", l[4]);
					 bdObject.append("cfNum", l[5]);
					 bdObject.append("totCost", l[6]);
					 
					 userList1.add(bdObject);
				 }
				 mbDao.insertDataByList("T_TJ_MZCFDAY", userList1);
				 sTime = DateUtils.addDay(DateUtils.parseDateY_M_D(sTableTime),1);
				 String nextDay = DateUtils.formatDateY_M_D(sTime);
				 sTableTime = nextDay + " 00:00:00";
				 eTableTime = nextDay + " 23:59:59";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
