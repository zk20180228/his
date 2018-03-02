package cn.honry.statistics.bi.bistac.imStacData.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MongoLog;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.imStacData.dao.ImStacDataDao;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.statistics.bi.bistac.treatmentEffect.vo.TreatmentEffectVo;
import cn.honry.statistics.deptstat.kidneyDiseaseWithDept.vo.ItemVo;
import cn.honry.statistics.deptstat.out_inpatient_work.vo.OutInpatientWorkVo;
import cn.honry.statistics.finance.pharmacyRefund.vo.RefundVo;
import cn.honry.statistics.sys.reportForms.vo.ReportVo;
import cn.honry.statistics.sys.reportForms.vo.StatisticsVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.model.Filters;
@Repository("imStacDataDao")
@SuppressWarnings({"all"})
public class ImStacDataDaoImpl extends HibernateDaoSupport implements ImStacDataDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private MongoBasicDao mbDao = null;
	public static final String TABLENAME_MZJZRCTJ = "T_TJ_MZJZRCTJ";//门诊急诊人次统计
	public static final String TABLENAME = "MZJZRCTJ";//门诊急诊人次统计
	public static final String TABLENAME_ZLXGSJFX = "ZLXGSJFX";//治疗效果数据分析
	public static final String TABLENAME1 = "YZB";//药占比
	public static final String TABLENAME1_1 = "YZB_1";//药占比
	public static final String TABLENAME2 = "YYTS";//门诊用药天数
	public static final String TABLENAME2_1 = "YYTS_1";//门诊用药天数
	public static final String TABLENAME3 = "YSYYJE";//门诊医生用药金额表
	public static final String TABLENAME3_1 = "YSYYJE_1";//门诊医生用药金额表
	public static final String TABLENAME4 = "KSYYJE";//门诊科室用药金额表
	public static final String TABLENAME4_1 = "KSYYJE_1";//门诊科室用药金额表
	public static final String TABLENAME5 = "YPJE";//门诊月药品金额，用药数量，人次表
	public static final String TABLENAME6 = "YPJE_1";//门诊月药品金额，用药数量，人次表（修改）
	public static final String TABLENAME6_1 = "YPJE_11";//门诊月药品金额，用药数量，人次表（修改）
	public static final String TABLENAME7 = "ZYSRTJ";//住院收入统计表
	public static final String TABLENAME_GZL = "T_TJ_GZL";//医生工作量
	public static final String TABLENAME_MZYFTFTJ = "MZYFTFTJ";//门诊药房退费统计
	public static final String TABLENAME_GZLDAY = "T_TJ_GZLDAY";//门诊工作量-----按天
	public static final String TABLENAME_GZLMONTH = "T_TJ_GZLMONTH";//门诊工作量--------按月
	public static final String TABLENAME_GZLYEAR = "T_TJ_GZLYEAR";//门诊工作量--------按年
	public static final String TABLENAME_MZCFDAY = "T_TJ_MZCFDAY";//门诊处方量--------按天
	public static final String TABLENAME_MZCFMONTH = "T_TJ_MZCFMONTH";//门诊处方量--------按月
	public static final String TABLENAME_MZCFYEAR = "T_TJ_MZCFYEAR";//门诊处方量--------按年
	public static final String TABLENAME_MZGXSRTJ = "MZGXSRTJ";//门诊各项收入统计
	public static final String TABLENAME_KSDBB = "KSDBB";//科室对比表(天)
	public static final String TABLENAME_KSTJ_MZZYGZTQDBB = "KSTJ_MZZYGZTQDBB";//门诊住院工作同期对比表
	
	
	
	

	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	
	public void imMZJZRCTJ(String startTime ,String endTime){
		try{
				String reg = "[0-9]{4}-[0-9]{2}-[0-9]{2}.{0,10}";
				Pattern pattern = Pattern.compile(reg);
				Matcher matcher = pattern.matcher(startTime);
				Matcher matcher1 = pattern.matcher(endTime);
				if(matcher.matches()&&matcher1.matches()){
					Date sTime = DateUtils.parseDateY_M_D(startTime);
					Date eTime = DateUtils.parseDateY_M_D(endTime);
					
					String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dTime = df.parse(df.format(new Date()));
					Date cTime = DateUtils.addDay(new Date(),-Integer.parseInt(dateNum)+1);
					String sTableTime = DateUtils.formatDateY_M_D(sTime) + " 00:00:00";
					String eTableTime = DateUtils.formatDateY_M_D(sTime) + " 23:59:59";
					
					while(DateUtils.compareDate(sTime, eTime)==-1){
						String partitionYear = sTableTime.split("-")[0];
						List<String> tableList = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",sTableTime,eTableTime);
//						String tableName = "T_REGISTER_MAIN partition (PART_"+partitionYear+")";
						String tableName = tableList.get(0);
						if(DateUtils.compareDate(sTime, cTime)!=-1){
							tableName = "T_REGISTER_MAIN_NOW";
						}
						
						 StringBuffer sql = new StringBuffer();
						 sql.append("select t1.regdate as regdate,nvl(t1.outpatientD,0) as outpatientD,nvl(t2.emergencyD,0) as emergencyD ");
						 sql.append("from (select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) outpatientD ");
						 sql.append(" from "+tableName+" t ");
						 sql.append("where t.REG_DATE >= to_date(:sTableTime, 'yyyy-MM-dd HH24:MI:SS') ");
						 sql.append("and  t.REG_DATE <= to_date(:eTableTime, 'yyyy-MM-dd HH24:MI:SS') ");
						 sql.append("and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
						 sql.append("group by trunc(t.REG_DATE, 'dd'))t1 full join ");
						 sql.append("(select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) emergencyD ");
						 sql.append("from "+tableName+" t ");
						 sql.append("where t.REG_DATE >= to_date(:sTableTime, 'yyyy-MM-dd HH24:MI:SS') ");
						 sql.append("and  t.REG_DATE <= to_date(:eTableTime, 'yyyy-MM-dd HH24:MI:SS') ");
						 sql.append("and  t.dept_code in (select dept_code ");
						 sql.append(" from t_department where dept_name like '%急诊%' and stop_flg = 0 and del_flg = 0) ");
						 sql.append(" and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
						 sql.append("group by trunc(t.REG_DATE, 'dd'))t2 on t1.regDate = t2.regDate order by t1.regDate");
						 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
								 .addScalar("regdate",Hibernate.DATE)
								 .addScalar("outpatientD",Hibernate.INTEGER)
								 .addScalar("emergencyD",Hibernate.INTEGER)
								 .setParameter("sTableTime", sTableTime)
								 .setParameter("eTableTime", eTableTime)
								 .list();
						 List<DBObject> userList = new ArrayList<DBObject>();
						 for(Object[] l : list){
							 	Bson whileFilter = Filters.eq("regdate", l[0].toString());
								Document document = new Document();
								document.append("regdate", l[0].toString());
								document.append("outpatientD", l[1]);
								document.append("emergencyD", l[2]);
								mbDao = new MongoBasicDao();
								mbDao.update(TABLENAME_MZJZRCTJ, whileFilter, document, true);
						 }
						 sTime = DateUtils.addDay(DateUtils.parseDateY_M_D(sTableTime),1);
						 String nextDay = DateUtils.formatDateY_M_D(sTime);
						 sTableTime = nextDay + " 00:00:00";
						 eTableTime = nextDay + " 23:59:59";
					}
				}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("============导入失败============");
		}
		System.out.println("============导入成功============");
	}
	/**
	 * 导入每天数据
	 * @Author zxh
	 * @time 2017年5月9日
	 */
	public void imEachDay(){
		/******************************************开始更新挂号主表数据**************************************************************/
		Date now = new Date();
		String today = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String yesterday = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -1))+" 23:59:59";
		StringBuffer sql = new StringBuffer();
		 sql.append("select t1.regdate as regdate,nvl(t1.outpatientD,0) as outpatientD,nvl(t2.emergencyD,0) as emergencyD ");
		 sql.append("from (select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) outpatientD ");
		 sql.append(" from T_REGISTER_MAIN_NOW t ");
		 sql.append(" where t.REG_DATE <= to_date(:today, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("and t.REG_DATE > to_date(:yesterday, 'yyyy-MM-dd HH24:MI:SS')");
		 sql.append("and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
		 sql.append("group by trunc(t.REG_DATE, 'dd'))t1 full join ");
		 sql.append("(select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) emergencyD ");
		 sql.append("from T_REGISTER_MAIN_NOW t ");
		 sql.append("where t.REG_DATE <= to_date(:today, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("and t.REG_DATE > to_date(:yesterday, 'yyyy-MM-dd HH24:MI:SS')");
		 sql.append("and  t.dept_code in (select dept_code ");
		 sql.append(" from t_department where dept_name like '%急诊%' and stop_flg = 0 and del_flg = 0) ");
		 sql.append(" and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
		 sql.append("group by trunc(t.REG_DATE, 'dd'))t2 on t1.regDate = t2.regDate order by t1.regDate");
		List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regdate",Hibernate.DATE)
				 .addScalar("outpatientD",Hibernate.INTEGER)
				 .addScalar("emergencyD",Hibernate.INTEGER)
				 .setParameter("today", today)
				 .setParameter("yesterday", yesterday)
				 .list();
		if(list.size()>0){
			for(Object[] l : list){
//				Document document1 = new Document();
//				document1.append("regdate", l[0].toString());
//				document1.append("outpatientD", l[1]);
				Bson whileFilter = Filters.eq("regdate", l[0].toString());
				Document document = new Document();
				document.append("regdate", l[0].toString());
				document.append("outpatientD", l[1]);
				document.append("emergencyD", l[2]);
				mbDao = new MongoBasicDao();
				mbDao.update(TABLENAME, whileFilter, document, true);
			}
		}
		/******************************************结束更新挂号主表数据**************************************************************/
	}
	
	/**
	 * 导历史数据
	 */
	public void imTableData(){
		/******************************************开始导入挂号主表数据**************************************************************/
		 mbDao = new MongoBasicDao();
		 mbDao.deleteData(TABLENAME);
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		 
		 StringBuffer sql = new StringBuffer();
		 sql.append("select t1.regdate as regdate,nvl(t1.outpatientD,0) as outpatientD,nvl(t2.emergencyD,0) as emergencyD ");
		 sql.append("from (select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) outpatientD ");
		 sql.append(" from T_REGISTER_MAIN t ");
		 sql.append(" where t.REG_DATE <= to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
		 sql.append("group by trunc(t.REG_DATE, 'dd'))t1 full join ");
		 sql.append("(select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) emergencyD ");
		 sql.append("from T_REGISTER_MAIN t ");
		 sql.append("where t.REG_DATE <= to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("and  t.dept_code in (select dept_code ");
		 sql.append(" from t_department where dept_name like '%急诊%' and stop_flg = 0 and del_flg = 0) ");
		 sql.append(" and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
		 sql.append("group by trunc(t.REG_DATE, 'dd'))t2 on t1.regDate = t2.regDate order by t1.regDate");
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regdate",Hibernate.DATE)
				 .addScalar("outpatientD",Hibernate.INTEGER)
				 .addScalar("emergencyD",Hibernate.INTEGER)
				 .setParameter("seDay", sevenDay)
				 .list();
		 List<DBObject> userList = new ArrayList<DBObject>();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regdate", l[0].toString());
			 bdObject.append("outpatientD", l[1]);
			 bdObject.append("emergencyD", l[2]);
			 userList.add(bdObject);
		 }
		 mbDao = new MongoBasicDao();
		 mbDao.insertDataByList(TABLENAME, userList);
		 
		 StringBuffer sql1 = new StringBuffer();
		 sql1.append("select t1.regdate as regdate,nvl(t1.outpatientD,0) as outpatientD,nvl(t2.emergencyD,0) as emergencyD ");
		 sql1.append("from (select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) outpatientD ");
		 sql1.append(" from T_REGISTER_MAIN_NOW t ");
		 sql1.append(" where t.REG_DATE <= to_date(:noDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append(" and tt.REG_DATE > to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
		 sql1.append("group by trunc(t.REG_DATE, 'dd'))t1 full join ");
		 sql1.append("(select trunc(t.REG_DATE, 'dd')as regDate, count(t.id) emergencyD ");
		 sql1.append("from T_REGISTER_MAIN_NOW t ");
		 sql1.append("where t.REG_DATE <= to_date(:noDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append(" and  t.REG_DATE > to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("and  t.dept_code in (select dept_code ");
		 sql1.append(" from t_department where dept_name like '%急诊%' and stop_flg = 0 and del_flg = 0) ");
		 sql1.append(" and t.valid_flag = 1 and t.in_state = 0 and t.trans_type = 1 and t.del_flg = 0 and t.stop_flg = 0 ");
		 sql1.append("group by trunc(t.REG_DATE, 'dd'))t2 on t1.regDate = t2.regDate order by t1.regDate");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regdate",Hibernate.DATE)
				 .addScalar("outpatientD",Hibernate.INTEGER)
				 .addScalar("emergencyD",Hibernate.INTEGER)
				 .setParameter("noDay", nowDay)
				 .setParameter("seDay", sevenDay)
				 .list();
		 List<DBObject> userList1 = new ArrayList<DBObject>();
		 for(Object[] l : list1){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regdate", l[0].toString());
			 bdObject.append("outpatientD", l[1]);
			 bdObject.append("emergencyD", l[2]);
			 userList1.add(bdObject);
		 }
		 mbDao = new MongoBasicDao();
		 mbDao.insertDataByList(TABLENAME, userList1);
		 
		/******************************************开始导入挂号主表数据**************************************************************/ 
		 
	}
	
	/**
	 * 向mongodb中更新当年的在线表中的数据
	 * @Author zhangkui
	 * @time 2017年5月10日
	 */
	public void imEachDay_T_INPATIENT_INFO(){
		// 第一步：把当年所有住院的数据查出来，放入list
		/************************************************ 开始更新在线住院表出院数据 ******************************************/
		Date nowDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		String endTime = dateFormat.format(nowDate)+ "-01-01 00:00:00";
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT TO_CHAR(OUT_DATE) as OUT_DATE ,NVL(OUTSTATE, 1) AS OUTSTATE,DEPTNAME,COUNT (OUTSTATETOTAL) AS OUTSTATETOTAL FROM( ");
		sb.append(" SELECT TO_CHAR(T.OUT_DATE,'YYYY') AS OUT_DATE,NVL(T.DIAG_OUTSTATE, 1) AS OUTSTATE,T.DEPT_CODE AS DEPTNAME,COUNT(T.INPATIENT_ID) AS OUTSTATETOTAL FROM ");
		sb.append(" T_INPATIENT_INFO_NOW T ");
		sb.append(" WHERE T.OUT_DATE < (SYSDATE+1) AND T.OUT_DATE>=TO_DATE(:endTime,'YYYY-MM-DD HH24:MI:SS') AND T.DEL_FLG = 0 AND T.STOP_FLG = 0 AND (T.IN_STATE = 'O' OR T.IN_STATE = 'N') GROUP BY T.DIAG_OUTSTATE,T.DEPT_CODE,T.OUT_DATE ");
		sb.append(" UNION ALL ");
		
		sb.append(" SELECT TO_CHAR(T.OUT_DATE,'YYYY') AS OUT_DATE,NVL(T.DIAG_OUTSTATE, 1) AS OUTSTATE,T.DEPT_CODE AS DEPTNAME,COUNT(T.INPATIENT_ID) AS OUTSTATETOTAL FROM ");
		sb.append(" T_INPATIENT_INFO T ");
		sb.append(" WHERE T.OUT_DATE < (SYSDATE+1) AND T.OUT_DATE>=TO_DATE(:endTimeOld,'YYYY-MM-DD HH24:MI:SS') AND T.DEL_FLG = 0 AND T.STOP_FLG = 0 AND (T.IN_STATE = 'O' OR T.IN_STATE = 'N') GROUP BY T.DIAG_OUTSTATE,T.DEPT_CODE,T.OUT_DATE ");
		
		sb.append(" ) GROUP BY OUT_DATE,OUTSTATE,DEPTNAME ");
		// 设置sql
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("out_Date",Hibernate.STRING)
				.addScalar("outStateTotal", Hibernate.INTEGER)
				.addScalar("deptName", Hibernate.STRING)
				.addScalar("outState", Hibernate.INTEGER);
		// 设置参数
		queryObject.setParameter("endTime", endTime);
		queryObject.setParameter("endTimeOld", endTime);
		// 进行查询
		List<TreatmentEffectVo> list_now = queryObject.setResultTransformer(
				Transformers.aliasToBean(TreatmentEffectVo.class)).list();
		// 将需要的数据插入mongodb中
		List<DBObject> userList_now = new ArrayList<DBObject>();
		if (list_now != null && list_now.size() > 0) {
		 for (TreatmentEffectVo t : list_now) {
				//String yearSelect = dateFormat.format(t.getOut_Date());
			 	String yearSelect = t.getOut_Date();
				//更新条件
				Document document1 = new Document();
				document1.append("yearSelect", yearSelect);
				document1.append("deptName", t.getDeptName());
				
				Document document = new Document();
				document.append("yearSelect", yearSelect);
				document.append("deptName", t.getDeptName());
				document.append("outState", t.getOutState());
				document.append("outStateTotal", t.getOutStateTotal());
				mbDao = new MongoBasicDao();
				mbDao.update(TABLENAME_ZLXGSJFX, document1, document, true);
			}
		}
	}
	
	/**
	 * 向mongodb中导入历史数据和在线库数据
	 * @throws ParseException 
	 *  @Author zhangkui
	 * @time 2017年5月10日
	 */
	@Deprecated
	public void imTableData_T_INPATIENT_INFO(){
		mbDao = new MongoBasicDao();
		mbDao.deleteData(TABLENAME_ZLXGSJFX);
		//历史住院表：T_INPATIENT_INFO 
		//住院在线表：T_INPATIENT_INFO_NOW
		/************************************************开始导入历史住院表数据******************************************/
		//导入历史数据
		//Date nowDate = new Date();
		StringBuffer sb  = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		sb.append(" SELECT TO_CHAR(OUT_DATE) as OUT_DATE,NVL(OUTSTATE, 1) AS OUTSTATE,DEPTNAME,COUNT (OUTSTATETOTAL) AS OUTSTATETOTAL ");
		sb.append(" FROM ( ");
		sb.append(" SELECT TO_CHAR(T.OUT_DATE,'YYYY') AS OUT_DATE,NVL(T.DIAG_OUTSTATE,1) AS OUTSTATE,T.DEPT_CODE AS DEPTNAME,COUNT(T.INPATIENT_ID) AS OUTSTATETOTAL FROM T_INPATIENT_INFO T WHERE T.OUT_DATE < (SYSDATE+1) AND T.DEL_FLG = 0 AND T.STOP_FLG = 0 AND (T.IN_STATE = 'O' OR T.IN_STATE = 'N') GROUP BY T.DIAG_OUTSTATE,T.DEPT_CODE,T.OUT_DATE ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT TO_CHAR(T.OUT_DATE,'YYYY') AS OUT_DATE,NVL(T.DIAG_OUTSTATE,1) AS OUTSTATE,T.DEPT_CODE AS DEPTNAME,COUNT(T.INPATIENT_ID) AS OUTSTATETOTAL FROM T_INPATIENT_INFO_NOW T WHERE T.OUT_DATE <(SYSDATE+1)	AND T.DEL_FLG = 0 AND T.STOP_FLG = 0 AND (T.IN_STATE = 'O' OR T.IN_STATE = 'N')	GROUP BY T.DIAG_OUTSTATE,T.DEPT_CODE,T.OUT_DATE ");
		sb.append(" ) ");
		sb.append(" GROUP BY OUT_DATE,OUTSTATE,DEPTNAME ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
	    queryObject.addScalar("out_Date",Hibernate.STRING).addScalar("outStateTotal",Hibernate.INTEGER).addScalar("deptName",Hibernate.STRING).addScalar("outState",Hibernate.INTEGER);
		//进行查询
		List<TreatmentEffectVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(TreatmentEffectVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>();
		if(list!=null&&list.size()>0){
			 for(TreatmentEffectVo t : list){
				 	//String yearSelect= dateFormat.format(t.geteTime());
				 	String yearSelect = t.getOut_Date();
				 	BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("yearSelect", yearSelect);
					bdObject.append("deptName", t.getDeptName());
					bdObject.append("outState", t.getOutState());
					bdObject.append("outStateTotal", t.getOutStateTotal());
					userList.add(bdObject);
			 }
			mbDao = new MongoBasicDao();
			mbDao.insertDataByList(TABLENAME_ZLXGSJFX, userList);
		}
		/************************************************结束导入历史住院表数据******************************************/
	}
	
	
	

	/**
	 * @Description:门诊药房退费统计：把now表中的数据更新到mongodb中
	 * 结算信息表：T_INPATIENT_CANCELITEM_NOW
	 * 病区退费申请表：T_FINANCE_INVOICEINFO_NOW,T_FINANCE_INVOICEINFO 
	 * @author: zhangkui
	 * @time:2017年5月12日 下午8:04:54
	 */
	public void imEachDay_T_INPATIENT_CANCELITEM(){
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(" SELECT INVOICENO,PATIENTNAME,FEESTATCODE,CONFIRMDATE,SUM(REFUNDMONEY) AS REFUNDMONEY,INVO_CODE AS INVOCODE,SENDWIN ");
		sb.append(" FROM ( ");
		sb.append(" SELECT T.BILL_NO AS INVOICENO,T.NAME AS PATIENTNAME,T.INVO_CODE AS FEESTATCODE,T.CONFIRM_DATE AS CONFIRMDATE,SUM (T.SALE_PRICE * T.QUANTITY) AS REFUNDMONEY,T.INVO_CODE,I.DRUG_WINDOW AS SENDWIN ");
		sb.append(" FROM T_INPATIENT_CANCELITEM_NOW T,T_FINANCE_INVOICEINFO_NOW I ");
		sb.append(" WHERE T.APPLY_FLAG = 1	AND T.DRUG_FLAG = 1 AND T.BILL_NO = I.INVOICE_NO AND T.INVO_CODE IN (01, 02, 03) AND T.CONFIRM_DATE < (SYSDATE + 1) ");
		sb.append(" GROUP BY T.BILL_NO,T.INVO_CODE,T.NAME,T.CONFIRM_DATE,I.DRUG_WINDOW,T.INVO_CODE ");
		sb.append(" UNION ALL ");
		
		sb.append(" SELECT T.BILL_NO AS INVOICENO,T.NAME AS PATIENTNAME,T.INVO_CODE AS FEESTATCODE,T.CONFIRM_DATE AS CONFIRMDATE,SUM (T.SALE_PRICE * T.QUANTITY) AS REFUNDMONEY,T.INVO_CODE,I.DRUG_WINDOW AS SENDWIN ");
		sb.append(" FROM T_INPATIENT_CANCELITEM_NOW T,T_FINANCE_INVOICEINFO I ");
		sb.append(" WHERE T.APPLY_FLAG = 1	AND T.DRUG_FLAG = 1 AND T.BILL_NO = I.INVOICE_NO AND T.INVO_CODE IN (01, 02, 03) AND T.CONFIRM_DATE < (SYSDATE + 1) ");
		sb.append(" GROUP BY T.BILL_NO,T.INVO_CODE,T.NAME,T.CONFIRM_DATE,I.DRUG_WINDOW,T.INVO_CODE ");
		
		sb.append("	)GROUP BY INVOICENO,PATIENTNAME,FEESTATCODE,CONFIRMDATE,INVO_CODE,SENDWIN ");
		
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
	    queryObject.addScalar("invoiceNo",Hibernate.STRING).addScalar("patientName",Hibernate.STRING)
	    			.addScalar("feeStatCode",Hibernate.STRING).addScalar("confirmDate",Hibernate.DATE)
	    			.addScalar("refundMoney",Hibernate.DOUBLE).addScalar("sendWin",Hibernate.STRING)
	    			.addScalar("invocode",Hibernate.STRING);
	    //进行查询
	  	List<RefundVo> list_now = queryObject.setResultTransformer(Transformers.aliasToBean(RefundVo.class)).list();
		//更新
	  	List<DBObject> userList_now = new ArrayList<DBObject>();
		if (list_now != null && list_now.size() > 0) {
		 for (RefundVo r : list_now) {
				//String yearSelect = dateFormat.format(t.getOut_Date());
			 	String confirmDate= dateFormat.format(r.getConfirmDate());
				//更新条件
				Document document1 = new Document();
				document1.append("invoiceNo", r.getInvoiceNo());
				document1.append("patientName", r.getPatientName());
				document1.append("feeStatCode", r.getFeeStatCode());
				document1.append("sendWin", r.getSendWin());
				document1.append("invocode", r.getInvocode());
				document1.append("confirmDate", confirmDate);
				
				Document document = new Document();
				document.append("invoiceNo", r.getInvoiceNo());
				document.append("patientName", r.getPatientName());
				document.append("feeStatCode", r.getFeeStatCode());
				document.append("refundMoney", r.getRefundMoney().toString());
				document.append("sendWin", r.getSendWin());
				document.append("invocode", r.getInvocode());
				document.append("confirmDate", confirmDate);
				mbDao = new MongoBasicDao();
				mbDao.update(TABLENAME_ZLXGSJFX, document1, document, true);
			}
		}
		
		
	}
	/**  
	 * 
	 * 将处方明细表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月11日 下午3:48:22 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月11日 下午3:48:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData(String tnL,String begin,String end,boolean timeType) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" select nvl(sum(decode(t.Drug_Flag, '1', t.tot_cost, 0)),0) as drugFee,nvl(sum(t.tot_cost),0) as totCost, count(distinct t.CLINIC_CODE) as total ");
		sb.append(" from "+tnL+" t ");
		sb.append(" where t.fee_date > to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.fee_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.PAY_FLAG != 0  and t.TRANS_TYPE=1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("drugFee",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("total",Hibernate.INTEGER);
		queryObject.setParameter("begin", begin);
		queryObject.setParameter("end", end);
		OutpatientUseMedicVo vo =(OutpatientUseMedicVo)queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).uniqueResult();
		//将需要的数据插入mongodb中
		Document document1 = new Document();
		document1.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
		Document document =null;
		if (timeType==false) {
			List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
			BasicDBObject bdObject = new BasicDBObject();
			bdObject.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
			mbDao = new MongoBasicDao();
			DBCursor cursor = mbDao.findAlldata(TABLENAME1_1, bdObject);
			while(cursor.hasNext()){
				OutpatientUseMedicVo vo2 = new OutpatientUseMedicVo();
				DBObject dbCursor = cursor.next();
				Double  drugFee =(Double) dbCursor.get("drugFee");
				Double  totCost =(Double) dbCursor.get("totCost");
				int  total =(int) dbCursor.get("total");
				vo2.setDrugFee(drugFee);
				vo2.setTotCost(totCost);
				vo2.setTotal(total);
				vos.add(vo2);
			}
			document = new Document();
			document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
			document.append("totCost", vo.getTotCost()+vos.get(0).getTotCost());
			document.append("drugFee", vo.getDrugFee()+vos.get(0).getDrugFee());
			document.append("total", vo.getTotal()+vos.get(0).getTotal());
			mbDao.update(TABLENAME1_1, document1,document,true);
			
		}else{
			document = new Document();
			document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
			document.append("totCost", vo.getTotCost());
			document.append("drugFee", vo.getDrugFee());
			document.append("total", vo.getTotal());
			mbDao.update(TABLENAME1_1, document1,document,true);
		}
		
	}
	/**  
	 * 
	 * 药占比（YZB）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午2:11:48 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午2:11:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_YZB() {
		mbDao = new MongoBasicDao();
		try {
			Date nowDate = new Date();
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			String beginTime = dateFormat.format(nowDate) + "-01 00:00:00";
			String ThirtyBf = DateUtils.formatDateY_M_D(DateUtils.addDay(nowDate,-7)) + " 23:59:59";
			String endTime = DateUtils.formatDateY_M_D(nowDate) + " 23:59:59";
			sb.append("select nvl(sum(drugFee),0) as drugFee,nvl(sum(totCost),0) as totCost,nvl(sum(total),0) as total, mon as selectTime from ( ");
			//判断是否在同一个月
			if (dateFormat.parse(ThirtyBf).equals(dateFormat.parse(endTime))) {
				sb.append(" select distinct sum(decode(t.Drug_Flag, '1', t.tot_cost, 0)) as drugFee,sum(t.tot_cost) as totCost, count(t.CLINIC_CODE) as total ,to_char(t.fee_date, 'yyyy-mm') as mon from T_OUTPATIENT_FEEDETAIL t ");
				sb.append(" where t.fee_date >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.fee_date <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.PAY_FLAG != 0  and t.TRANS_TYPE=1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 ");
				sb.append(" group by t.fee_date ");
				sb.append(" union all ");
				sb.append(" select distinct sum(decode(t.Drug_Flag, '1', t.tot_cost, 0)) as drugFee,sum(t.tot_cost) as totCost, count(t.CLINIC_CODE) as total ,to_char(t.fee_date, 'yyyy-mm') as mon from T_OUTPATIENT_FEEDETAIL_NOW t ");
				sb.append(" where t.fee_date >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.fee_date <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.PAY_FLAG != 0  and t.TRANS_TYPE=1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 ");
				sb.append(" group by t.fee_date ");
			}else{
				sb.append(" select distinct sum(decode(t.Drug_Flag, '1', t.tot_cost, 0)) as drugFee,sum(t.tot_cost) as totCost, count(t.CLINIC_CODE) as total ,to_char(t.fee_date, 'yyyy-mm') as mon from T_OUTPATIENT_FEEDETAIL_NOW t ");
				sb.append(" where t.fee_date >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.fee_date <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.PAY_FLAG != 0  and t.TRANS_TYPE=1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0 ");
				sb.append(" group by t.fee_date ");
			}
			sb.append(" )  group by mon");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("drugFee",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("total",Hibernate.INTEGER).addScalar("selectTime",Hibernate.STRING);
			queryObject.setParameter("ThirtyBf", ThirtyBf);
			queryObject.setParameter("endTime", endTime);
			queryObject.setParameter("beginTime", beginTime);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			List<DBObject> userList = new ArrayList<DBObject>();
			if(list!=null&&list.size()>0){
				for(OutpatientUseMedicVo t : list){
					Document document1 = new Document();
					document1.append("selectTime", t.getSelectTime());
					
					Document document = new Document();
					document.append("selectTime", t.getSelectTime());
					document.append("totCost", t.getTotCost());
					document.append("drugFee", t.getDrugFee());
					document.append("total", t.getTotal());
					mbDao.update(TABLENAME1, document1,document,true);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 将门诊用药天数表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午3:28:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午3:28:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	/*@Override
	public void inTableData_YYTS(String tnL,String begin,String end,boolean typeTime) {
		mbDao.deleteData(TABLENAME2);
		String startStr = "2014-11-01 00:00:00";
		//得到7天前的当前时间
		String ThirtyBf=DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(), -7))+" 23:59:59";
		String endTime = DateUtils.formatDateY_M_D(new Date())+" 23:59:59";
		final StringBuffer sb = new StringBuffer();
		sb.append("select selectTime,avg(avgDays) as avgDays from (");
		//时间大于2004-11-01 00:00:00 （T_OUTPATIENT_FEEDETAIL）（判断 是否为 pack_qty 包装单位）
		sb.append(" select o.CLINIC_CODE,  to_char(o.REG_DATE, 'yyyy-mm') as selectTime, avg(nvl(case  when o.ext_flag3 = 1 then (case ");
		sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
		sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
		sb.append(" when t.FREQUENCY_UNIT = 'M' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
		sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
		sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
		sb.append(" end) else(case");
		sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
		sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
		sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
		sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
		sb.append(" end) end, 1)) as avgDays ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL o");
		sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
		sb.append(" where o.REG_DATE >= to_date(:startStr,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
		sb.append(" group by o.CLINIC_CODE,o.REG_DATE");
		sb.append(" union all ");
		//时间小于2004-11-01 00:00:00 （T_OUTPATIENT_FEEDETAIL）（qty 数量 就是最小单位的总数量）
		sb.append(" select o.CLINIC_CODE, to_char(o.REG_DATE, 'yyyy-mm') as selectTime, avg(nvl(  case ");
		sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose)))");
		sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
		sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24) ");
		sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
		sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
		sb.append(" end,  1)) as avgDays");
		sb.append(" from T_OUTPATIENT_FEEDETAIL o");
		sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
		sb.append(" where o.REG_DATE < to_date(:startStr,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
		sb.append(" group by o.CLINIC_CODE,o.REG_DATE");
		sb.append(" union all ");
		//时间大于 当前时间7天前的时间 小于 当前时间  （T_OUTPATIENT_FEEDETAIL_NOW）（判断 是否为 pack_qty 包装单位）
		sb.append(" select o.CLINIC_CODE,  to_char(o.REG_DATE, 'yyyy-mm') as selectTime, avg(nvl(case  when o.ext_flag3 = 1 then (case ");
		sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
		sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
		sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
		sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
		sb.append(" end) else(case");
		sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
		sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
		sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
		sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
		sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
		sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
		sb.append(" end) end, 1)) as avgDays ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW o");
		sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
		sb.append(" where o.REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and o.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
		sb.append(" group by o.CLINIC_CODE,o.REG_DATE");
		
		sb.append(" ) group by selectTime");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("avgDays",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING);
		queryObject.setParameter("ThirtyBf", ThirtyBf);
		queryObject.setParameter("endTime", endTime);
		queryObject.setParameter("startStr", startStr);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>();
		if(list!=null&&list.size()>0){
			 for(OutpatientUseMedicVo t : list){
				 	BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("selectTime", t.getSelectTime());
					bdObject.append("avgDays", t.getAvgDays());
					userList.add(bdObject);
			 }
			mbDao.insertDataByList(TABLENAME2, userList);
		}
	}*/
	@Override
	public void inTableData_YYTS(String tnL,String begin,String end,boolean timeType) {
		mbDao = new MongoBasicDao();
		final StringBuffer sb = new StringBuffer();
		sb.append(" select nvl(count(CLINIC_CODE),0) as total ,nvl(sum(days),0) as days from( ");
		sb.append(" select o.CLINIC_CODE as CLINIC_CODE, ");
		if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
			sb.append(" nvl(avg(nvl(case  when o.ext_flag3 = 0 then (case ");
			sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
			sb.append("  when t.FREQUENCY_UNIT = 'T' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
			sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
			sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
			sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
			sb.append(" end) else(case");
			sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
			sb.append(" when t.FREQUENCY_UNIT = 'T' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
			sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
			sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
			sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
			sb.append(" end) end, 1)),0) as days ");
		}else{
			//时间小于2014-11-01 00:00:00 （T_OUTPATIENT_FEEDETAIL）（qty 数量 就是最小单位的总数量）
			sb.append(" nvl(avg(nvl(  case ");
			sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose)))");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
			sb.append(" when t.FREQUENCY_UNIT = 'T' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose)))");
			sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
			sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
			sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24) ");
			sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
			sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
			sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
			sb.append(" end,  1)),0) as days");
		}
		sb.append(" from "+tnL+" o");
		sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
		sb.append(" where o.REG_DATE > to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and o.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
		sb.append(" group by o.CLINIC_CODE)");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("days",Hibernate.DOUBLE).addScalar("total",Hibernate.INTEGER);
		queryObject.setParameter("begin", begin);
		queryObject.setParameter("end", end);
		OutpatientUseMedicVo vo =(OutpatientUseMedicVo)queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).uniqueResult();
		//将需要的数据插入mongodb中
		Document document1 = new Document();
		Document document = null;
		document1.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
		if (timeType==false) {
			List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
			BasicDBObject bdObject = new BasicDBObject();
			bdObject.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
			DBCursor cursor = mbDao.findAlldata(TABLENAME2_1, bdObject);
			while(cursor.hasNext()){
				OutpatientUseMedicVo vo2 = new OutpatientUseMedicVo();
				DBObject dbCursor = cursor.next();
				Double  days =(Double) dbCursor.get("days");
				int  total =(int) dbCursor.get("total");
				vo2.setDays(days);
				vo2.setTotal(total);
				vos.add(vo2);
			}
			document = new Document();
			document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
			document.append("days", vo.getDays()+vos.get(0).getDays());
			document.append("total", vo.getTotal()+vos.get(0).getTotal());
			mbDao.update(TABLENAME2_1, document1,document,true);
		}else{
			document = new Document();
			document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
			document.append("days", vo.getDays());
			document.append("total", vo.getTotal());
			mbDao.update(TABLENAME2_1, document1,document,true);
		}
		
	}
	/**  
	 * 
	 * 门诊用药天数（YYTS）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月12日 下午3:29:00 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月12日 下午3:29:00 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_YYTS() {
		mbDao = new MongoBasicDao();
		try {
			Date nowDate = new Date();
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			String beginTime = dateFormat.format(nowDate) + "-01 00:00:00";
			String ThirtyBf = DateUtils.formatDateY_M_D(DateUtils.addDay(nowDate,-7)) + " 23:59:59";
			String endTime = DateUtils.formatDateY_M_D(nowDate) + " 23:59:59";
			sb.append("select selectTime,avg(avgDays) as avgDays from (");
			//判断是否在同一个月
			if (dateFormat.parse(ThirtyBf).equals(dateFormat.parse(endTime))) {
				sb.append(" select o.CLINIC_CODE,  to_char(o.REG_DATE, 'yyyy-mm') as selectTime, avg(nvl(case  when o.ext_flag3 = 1 then (case ");
				sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" end) else(case");
				sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
				sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
				sb.append(" end) end, 1)) as avgDays ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL o");
				sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
				sb.append(" where o.REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and o.REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
				sb.append(" group by o.CLINIC_CODE,o.REG_DATE");
				
				sb.append(" union all ");
				
				sb.append(" select o.CLINIC_CODE,  to_char(o.REG_DATE, 'yyyy-mm') as selectTime, avg(nvl(case  when o.ext_flag3 = 1 then (case ");
				sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" end) else(case");
				sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
				sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
				sb.append(" end) end, 1)) as avgDays ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW o");
				sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
				sb.append(" where o.REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and o.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
				sb.append(" group by o.CLINIC_CODE,o.REG_DATE");
			}else{
				sb.append(" select o.CLINIC_CODE,  to_char(o.REG_DATE, 'yyyy-mm') as selectTime, avg(nvl(case  when o.ext_flag3 = 1 then (case ");
				sb.append("  when t.FREQUENCY_UNIT = 'D' then (decode(o.pack_qty,NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/ decode(o.base_dose,NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.pack_qty, NULL,1,DECODE(o.pack_qty, 0, 1, o.pack_qty)) *decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" /(decode(o.dose_once,NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))/decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" /(decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.pack_qty,NULL,1, DECODE(o.pack_qty, 0, 1, o.pack_qty)) * decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7  ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM, 0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" end) else(case");
				sb.append(" when t.FREQUENCY_UNIT = 'D' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once,NULL,1, DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL, 1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM, NULL,1,DECODE(t.FREQUENCY_NUM,0,1, t.FREQUENCY_NUM)))) ");
				sb.append(" when t.FREQUENCY_UNIT = 'H' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) ");
				sb.append(" / (decode(o.dose_once, NULL,1,DECODE(o.dose_once, 0, 1, o.dose_once))  / decode(o.base_dose, NULL,1,DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1, DECODE(t.FREQUENCY_NUM,0, 1, t.FREQUENCY_NUM))) / 24)");
				sb.append(" when t.FREQUENCY_UNIT = 'W' then (decode(o.qty, NULL, 1, DECODE(o.qty, 0, 1, o.qty)) * 7 ");
				sb.append(" / (decode(o.dose_once, NULL, 1,DECODE(o.dose_once, 0, 1, o.dose_once)) / decode(o.base_dose, NULL,1, DECODE(o.base_dose, 0, 1, o.base_dose))) ");
				sb.append(" / (decode(t.FREQUENCY_NUM,NULL,1,DECODE(t.FREQUENCY_NUM,0,1,t.FREQUENCY_NUM))))");
				sb.append(" end) end, 1)) as avgDays ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW o");
				sb.append(" left join T_BUSINESS_FREQUENCY t on o.FREQUENCY_CODE = t.FREQUENCY_ENCODE ");
				sb.append(" where o.REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and o.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and o.PAY_FLAG != 0 and o.DRUG_FLAG = '1' and o.TRANS_TYPE = 1  and o.CANCEL_FLAG = 1 and o.DAYS = 1 and o.stop_flg = 0 and o.del_flg = 0 and t.stop_flg = 0 and t.stop_flg = 0 and t.del_flg = 0  and (o.reg_dpcd = t.createdept or t.createdept = 'ROOT') ");
				sb.append(" group by o.CLINIC_CODE,o.REG_DATE");
			}
			sb.append(" ) group by selectTime");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("avgDays",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING);
			queryObject.setParameter("ThirtyBf", ThirtyBf);
			queryObject.setParameter("endTime", endTime);
			queryObject.setParameter("beginTime", beginTime);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			//将需要的数据插入mongodb中
			List<DBObject> userList = new ArrayList<DBObject>();
			if(list!=null&&list.size()>0){
				for(OutpatientUseMedicVo t : list){
					Document document1 = new Document();
					document1.append("selectTime", t.getSelectTime());
					
					Document document = new Document();
					document.append("selectTime", t.getSelectTime());
					document.append("avgDays", t.getAvgDays());
					mbDao.update(TABLENAME2, document1,document,true);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 将医生用药金额表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	/*@Override
	public void inTableData_YSYYJE() {
		mbDao.deleteData(TABLENAME3);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		//得到7天前的当前时间
		String ThirtyBf=DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(), -7))+" 23:59:59";
		String endTime = DateUtils.formatDateY_M_D(new Date())+" 23:59:59";
		final StringBuffer sb = new StringBuffer();
		sb.append("select a.totCost as totCost, a.num as num, a.mon as selectTime, o.employee_name as doctCodeName  from ( ");
		sb.append(" select doct_code as doct_code,sum(totCost) as totCost,sum(num) as num,mon from ( ");
		sb.append(" select t.tot_cost as totCost,t.num as num,t.doct_code,to_char(t.REG_DATE, 'yyyy-mm') as mon from ( ");
		sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num  ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW t where ");
		sb.append(" t.REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" union all ");
		sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num  ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t where ");
		sb.append(" t.REG_DATE >= to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" union all ");
		sb.append(" select t.*,(decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty))) as num  ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t where ");
		sb.append(" t.REG_DATE < to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" ) t ");
		sb.append(" where t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0)");
		sb.append(" group by doct_code, mon");
		sb.append(" order by mon desc, totCost desc) a");
		sb.append(" left join T_EMPLOYEE o on a.doct_code = o.employee_jobno ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("doctCodeName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
		queryObject.setParameter("ThirtyBf", ThirtyBf);
		queryObject.setParameter("endTime", endTime);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>();
		String t=dateFormat.format(new Date());
		int j=0;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", list.get(0).getSelectTime());
		bdObject.append("doctCodeName", list.get(0).getDoctCodeName());
		bdObject.append("num", list.get(0).getNum());
		bdObject.append("totCost", list.get(0).getTotCost());
		userList.add(bdObject);
		if(list!=null&&list.size()>0){
			for(int i=1;i<list.size();i++){
				if(list.get(i).getSelectTime().equals(t)){
					if(j<4){
						bdObject = new BasicDBObject();
						bdObject.append("selectTime", list.get(i).getSelectTime());
						bdObject.append("doctCodeName", list.get(i).getDoctCodeName());
						bdObject.append("num", list.get(i).getNum());
						bdObject.append("totCost", list.get(i).getTotCost());
						userList.add(bdObject);
					}
					j++;
				}else{
					j=0;
					t=list.get(i).getSelectTime();
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", list.get(i).getSelectTime());
					bdObject.append("doctCodeName", list.get(i).getDoctCodeName());
					bdObject.append("num", list.get(i).getNum());
					bdObject.append("totCost", list.get(i).getTotCost());
					userList.add(bdObject);
				}
			}
			mbDao.insertDataByList(TABLENAME3, userList);
		}
		
	}*/
	@Override
	public void inTableData_YSYYJE(String tnL,String begin,String end,boolean timeType) {
		mbDao = new MongoBasicDao();
		final StringBuffer sb = new StringBuffer();
		sb.append("select a.totCost as totCost, a.num as num, o.employee_name as doctCodeName  from ( ");
		sb.append(" select nvl(sum(t.tot_cost),0) as totCost, t.doct_code, ");
		if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
			sb.append(" nvl(sum((case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end)),0) as num  ");
		}else{
			sb.append(" nvl(sum((decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)))),0) as num  ");
		}
		sb.append(" from "+tnL+" t where ");
		sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0");
		sb.append(" group by doct_code");
		sb.append(" order by totCost desc) a");
		sb.append(" left join T_EMPLOYEE o on a.doct_code = o.employee_jobno ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("doctCodeName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
		queryObject.setParameter("begin", begin);
		queryObject.setParameter("end", end);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		Document document =null;
		if(list!=null&&list.size()>0){
			for(OutpatientUseMedicVo t : list){
				Document document1 = new Document();
				document1.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
				document1.append("doctCodeName", t.getDoctCodeName());
				if (timeType==false) {
					List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
					BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					bdObject.append("doctCodeName", t.getDoctCodeName());
					DBCursor cursor = mbDao.findAlldata(TABLENAME3_1, bdObject);
					if (cursor!=null) {
						while(cursor.hasNext()){
							OutpatientUseMedicVo vo2 = new OutpatientUseMedicVo();
							DBObject dbCursor = cursor.next();
							Double  totCost =(Double) dbCursor.get("totCost");
							Double  num =(Double) dbCursor.get("num");
							vo2.setTotCost(totCost);
							vo2.setNum(num);
							vos.add(vo2);
						}
						document = new Document();
						document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
						document.append("doctCodeName", t.getDoctCodeName());
						document.append("totCost", t.getTotCost()+vos.get(0).getTotCost());
						document.append("num", t.getNum()+vos.get(0).getNum());
						mbDao.update(TABLENAME3_1, document1,document,true);
					}else{
						document = new Document();
						document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
						document.append("doctCodeName", t.getDoctCodeName());
						document.append("totCost", t.getTotCost());
						document.append("num", t.getNum());
						mbDao.update(TABLENAME3_1, document1,document,true);
					}
				}else{
					document = new Document();
					document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					document.append("doctCodeName", t.getDoctCodeName());
					document.append("totCost", t.getTotCost());
					document.append("num", t.getNum());
					mbDao.update(TABLENAME3_1, document1,document,true);
				}
			}
		}
	}
	/**  
	 * 
	 * 医生用药金额表（YSYYJE）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_YSYYJE() {
		try {
			Date nowDate = new Date();
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			String beginTime = dateFormat.format(nowDate) + "-01 00:00:00";
			String ThirtyBf = DateUtils.formatDateY_M_D(DateUtils.addDay(nowDate,-7)) + " 23:59:59";
			String endTime = DateUtils.formatDateY_M_D(nowDate) + " 23:59:59";
			sb.append("select a.totCost as totCost, a.num as num, a.mon as selectTime, o.employee_name as doctCodeName  from ( ");
			sb.append(" select doct_code as doct_code,sum(totCost) as totCost,sum(num) as num,mon from ( ");
			sb.append(" select t.tot_cost as totCost,t.num as num,t.doct_code,to_char(t.REG_DATE, 'yyyy-mm') as mon from ( ");
			//判断是否在同一个月
			if (dateFormat.parse(ThirtyBf).equals(dateFormat.parse(endTime))) {
				sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW t where ");
				sb.append(" t.REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and t.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" union all ");
				sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num  ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL t where ");
				sb.append(" t.REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and t.REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
			}else{
				sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW where ");
				sb.append(" REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
			}
			sb.append(" ) t");
			sb.append(" where t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0)");
			sb.append(" group by doct_code, mon");
			sb.append(" order by mon desc, totCost desc) a");
			sb.append(" left join T_EMPLOYEE o on a.doct_code = o.employee_jobno ");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("doctCodeName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
			queryObject.setParameter("ThirtyBf", ThirtyBf);
			queryObject.setParameter("endTime", endTime);
			queryObject.setParameter("beginTime", beginTime);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			List<DBObject> userList = new ArrayList<DBObject>();
			if(list!=null&&list.size()>0){
				for(OutpatientUseMedicVo t : list){
					Document document1 = new Document();
					document1.append("selectTime", t.getSelectTime());
					
					Document document = new Document();
					document.append("selectTime", t.getSelectTime());
					document.append("totCost", t.getTotCost());
					document.append("doctCodeName", t.getDoctCodeName());
					document.append("num", t.getNum());
					mbDao.update(TABLENAME3, document1,document,true);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**  
	 * 
	 * 将科室用药金额表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	/*@Override
	public void inTableData_KSYYJE() {
		mbDao.deleteData(TABLENAME4);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		//得到7天前的当前时间
		String ThirtyBf=DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(), -7))+" 23:59:59";
		String endTime = DateUtils.formatDateY_M_D(new Date())+" 23:59:59";
		final StringBuffer sb = new StringBuffer();
		sb.append("select a.totCost as totCost, a.num as num, a.mon as selectTime, o.DEPT_NAME as regDpcdName  from ( ");
		sb.append(" select DOCT_DEPT as DOCT_DEPT,sum(totCost) as totCost,sum(num) as num,mon from ( ");
		sb.append(" select t.tot_cost as totCost,t.num as num,t.DOCT_DEPT,to_char(t.REG_DATE, 'yyyy-mm') as mon from ( ");
		sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW t where ");
		sb.append(" t.REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" union all ");
		sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t where ");
		sb.append(" t.REG_DATE >= to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" union all ");
		sb.append(" select t.*,(decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty))) as num ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t where ");
		sb.append(" t.REG_DATE < to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" ) t ");
		sb.append(" where t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0)");
		sb.append(" group by DOCT_DEPT, mon");
		sb.append(" order by mon desc, totCost desc) a");
		sb.append(" left join T_DEPARTMENT o on a.DOCT_DEPT = o.DEPT_CODE ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("regDpcdName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
		queryObject.setParameter("ThirtyBf", ThirtyBf);
		queryObject.setParameter("endTime", endTime);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>();
		String t=dateFormat.format(new Date());
		int j=0;
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("selectTime", list.get(0).getSelectTime());
		bdObject.append("regDpcdName", list.get(0).getRegDpcdName());
		bdObject.append("num", list.get(0).getNum());
		bdObject.append("totCost", list.get(0).getTotCost());
		userList.add(bdObject);
		if(list!=null&&list.size()>0){
			for(int i=1;i<list.size();i++){
				if(list.get(i).getSelectTime().equals(t)){
					if(j<4){
						bdObject = new BasicDBObject();
						bdObject.append("selectTime", list.get(i).getSelectTime());
						bdObject.append("regDpcdName", list.get(i).getRegDpcdName());
						bdObject.append("num", list.get(i).getNum());
						bdObject.append("totCost", list.get(i).getTotCost());
						userList.add(bdObject);
					}
					j++;
				}else{
					j=0;
					t=list.get(i).getSelectTime();
					bdObject = new BasicDBObject();
					bdObject.append("selectTime", list.get(i).getSelectTime());
					bdObject.append("regDpcdName", list.get(i).getRegDpcdName());
					bdObject.append("num", list.get(i).getNum());
					bdObject.append("totCost", list.get(i).getTotCost());
					userList.add(bdObject);
				}
			}
			mbDao.insertDataByList(TABLENAME4, userList);
		}
		
	}*/
	@Override
	public void inTableData_KSYYJE(String tnL,String begin,String end,boolean timeType) {
		mbDao = new MongoBasicDao();
		final StringBuffer sb = new StringBuffer();
		sb.append("select a.totCost as totCost, a.num as num,o.DEPT_NAME as regDpcdName  from ( ");
		sb.append(" select nvl(sum(t.tot_cost),0) as totCost,t.DOCT_DEPT ,");
		if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
			sb.append(" nvl(sum((case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end)),0) as num  ");
		}else{
			sb.append(" nvl(sum((decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)))),0) as num  ");
		}
		sb.append(" from "+tnL+" t where ");
		sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0");
		sb.append(" group by DOCT_DEPT ");
		sb.append(" order by totCost desc) a");
		sb.append(" left join T_DEPARTMENT o on a.DOCT_DEPT = o.DEPT_CODE ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("regDpcdName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
		queryObject.setParameter("begin", begin);
		queryObject.setParameter("end", end);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		Document document =null;
		if(list!=null&&list.size()>0){
			for(OutpatientUseMedicVo t : list){
				Document document1 = new Document();
				document1.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
				document1.append("regDpcdName", t.getDoctCodeName());
				if (timeType==false) {
					List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
					BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					bdObject.append("regDpcdName", t.getRegDpcdName());
					DBCursor cursor = mbDao.findAlldata(TABLENAME4_1, bdObject);
					if (cursor!=null) {
						while(cursor.hasNext()){
							OutpatientUseMedicVo vo2 = new OutpatientUseMedicVo();
							DBObject dbCursor = cursor.next();
							Double  totCost =(Double) dbCursor.get("totCost");
							Double  num =(Double) dbCursor.get("num");
							vo2.setTotCost(totCost);
							vo2.setNum(num);
							vos.add(vo2);
						}
						document = new Document();
						document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
						document.append("regDpcdName", t.getRegDpcdName());
						document.append("totCost", t.getTotCost()+vos.get(0).getTotCost());
						document.append("num", t.getNum()+vos.get(0).getNum());
						mbDao.update(TABLENAME4_1, document1,document,true);
					}else{
						document = new Document();
						document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
						document.append("regDpcdName", t.getRegDpcdName());
						document.append("totCost", t.getTotCost());
						document.append("num", t.getNum());
						mbDao.update(TABLENAME4_1, document1,document,true);
					}
				}else{
					document = new Document();
					document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					document.append("regDpcdName", t.getRegDpcdName());
					document.append("totCost", t.getTotCost());
					document.append("num", t.getNum());
					mbDao.update(TABLENAME4_1, document1,document,true);
				}
			}
		}
	}
	/**  
	 * 
	 * 科室用药金额表（KSYYJE）向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月13日 上午10:10:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月13日 上午10:10:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_KSYYJE() {
		try {
			mbDao = new MongoBasicDao();
			Date nowDate = new Date();
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			String beginTime = dateFormat.format(nowDate) + "-01 00:00:00";
			String ThirtyBf = DateUtils.formatDateY_M_D(DateUtils.addDay(nowDate,-7)) + " 23:59:59";
			String endTime = DateUtils.formatDateY_M_D(nowDate) + " 23:59:59";
			sb.append("select a.totCost as totCost, a.num as num, a.mon as selectTime, o.DEPT_NAME as regDpcdName  from ( ");
			sb.append(" select DOCT_DEPT as DOCT_DEPT,sum(totCost) as totCost,sum(num) as num,mon from ( ");
			sb.append(" select t.tot_cost as totCost,t.QTY as num,t.DOCT_DEPT,to_char(t.REG_DATE, 'yyyy-mm') as mon from ( ");
			//判断是否在同一个月
			if (dateFormat.parse(ThirtyBf).equals(dateFormat.parse(endTime))) {
				sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW t where ");
				sb.append(" t.REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and t.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" union all ");
				sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL t where ");
				sb.append(" t.REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and t.REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
			}else{
				sb.append(" select t.* ,(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL_NOW where ");
				sb.append(" t.REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss')");
				sb.append(" and t.REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss')");
			}
			sb.append(" ) t");
			sb.append(" where t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1  and t.stop_flg = 0 and t.del_flg = 0)");
			sb.append(" group by DOCT_DEPT, mon");
			sb.append(" order by mon desc, totCost desc) a");
			sb.append(" left join T_DEPARTMENT o on a.DOCT_DEPT = o.DEPT_CODE ");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("totCost",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("regDpcdName",Hibernate.STRING).addScalar("num",Hibernate.DOUBLE);
			queryObject.setParameter("ThirtyBf", ThirtyBf);
			queryObject.setParameter("endTime", endTime);
			queryObject.setParameter("beginTime", beginTime);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			List<DBObject> userList = new ArrayList<DBObject>();
			if(list!=null&&list.size()>0){
				for(OutpatientUseMedicVo t : list){
					Document document1 = new Document();
					document1.append("selectTime", t.getSelectTime());
					
					Document document = new Document();
					document.append("selectTime", t.getSelectTime());
					document.append("totCost", t.getTotCost());
					document.append("regDpcdName", t.getRegDpcdName());
					document.append("num", t.getNum());
					mbDao.update(TABLENAME4, document1,document,true);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description:门诊药房退费统计：把数据导入mongodb中
	 *病区退费申请表：T_INPATIENT_CANCELITEM_NOW,lT_INPATIENT_CANCELITEM
	 *结算信息表:T_FINANCE_INVOICEINFO_NOW,T_FINANCE_INVOICEINFO 
	 * @author: zhangkui
	 * @time:2017年5月12日 下午8:04:54
	 */
	public void imTableData_T_INPATIENT_CANCELITEM(){
		mbDao = new MongoBasicDao();
		mbDao.deleteData(TABLENAME_MZYFTFTJ);
		/************************************************开始导入数据******************************************/
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(" SELECT INVOICENO,PATIENTNAME,FEESTATCODE,CONFIRMDATE,SUM(REFUNDMONEY) AS REFUNDMONEY,INVO_CODE AS INVOCODE,SENDWIN ");
		sb.append(" FROM ( ");
		sb.append(" SELECT T.BILL_NO AS INVOICENO,T.NAME AS PATIENTNAME,T.INVO_CODE AS FEESTATCODE,T.CONFIRM_DATE AS CONFIRMDATE,SUM (T.SALE_PRICE * T.QUANTITY) AS REFUNDMONEY,T.INVO_CODE,I.DRUG_WINDOW AS SENDWIN ");
		sb.append(" FROM T_INPATIENT_CANCELITEM_NOW T,T_FINANCE_INVOICEINFO_NOW I ");
		sb.append(" WHERE T.APPLY_FLAG = 1	AND T.DRUG_FLAG = 1 AND T.BILL_NO = I.INVOICE_NO AND T.INVO_CODE IN (01, 02, 03) AND T.CONFIRM_DATE < (SYSDATE + 1) ");
		sb.append(" GROUP BY T.BILL_NO,T.INVO_CODE,T.NAME,T.CONFIRM_DATE,I.DRUG_WINDOW,T.INVO_CODE ");
		sb.append(" UNION ALL ");
		
		sb.append(" SELECT T.BILL_NO AS INVOICENO,T.NAME AS PATIENTNAME,T.INVO_CODE AS FEESTATCODE,T.CONFIRM_DATE AS CONFIRMDATE,SUM (T.SALE_PRICE * T.QUANTITY) AS REFUNDMONEY,T.INVO_CODE,I.DRUG_WINDOW AS SENDWIN ");
		sb.append(" FROM T_INPATIENT_CANCELITEM T,T_FINANCE_INVOICEINFO_NOW I ");
		sb.append(" WHERE T.APPLY_FLAG = 1	AND T.DRUG_FLAG = 1 AND T.BILL_NO = I.INVOICE_NO AND T.INVO_CODE IN (01, 02, 03) AND T.CONFIRM_DATE < (SYSDATE + 1) ");
		sb.append(" GROUP BY T.BILL_NO,T.INVO_CODE,T.NAME,T.CONFIRM_DATE,I.DRUG_WINDOW,T.INVO_CODE ");
		sb.append(" UNION ALL ");
		
		sb.append(" SELECT T.BILL_NO AS INVOICENO,T.NAME AS PATIENTNAME,T.INVO_CODE AS FEESTATCODE,T.CONFIRM_DATE AS CONFIRMDATE,SUM (T.SALE_PRICE * T.QUANTITY) AS REFUNDMONEY,T.INVO_CODE,I.DRUG_WINDOW AS SENDWIN ");
		sb.append(" FROM T_INPATIENT_CANCELITEM T,T_FINANCE_INVOICEINFO I ");
		sb.append(" WHERE T.APPLY_FLAG = 1	AND T.DRUG_FLAG = 1 AND T.BILL_NO = I.INVOICE_NO AND T.INVO_CODE IN (01, 02, 03) AND T.CONFIRM_DATE < (SYSDATE + 1) ");
		sb.append(" GROUP BY T.BILL_NO,T.INVO_CODE,T.NAME,T.CONFIRM_DATE,I.DRUG_WINDOW,T.INVO_CODE ");
		sb.append(" UNION ALL ");
		
		sb.append(" SELECT T.BILL_NO AS INVOICENO,T.NAME AS PATIENTNAME,T.INVO_CODE AS FEESTATCODE,T.CONFIRM_DATE AS CONFIRMDATE,SUM (T.SALE_PRICE * T.QUANTITY) AS REFUNDMONEY,T.INVO_CODE,I.DRUG_WINDOW AS SENDWIN ");
		sb.append(" FROM T_INPATIENT_CANCELITEM_NOW T,T_FINANCE_INVOICEINFO I ");
		sb.append(" WHERE T.APPLY_FLAG = 1	AND T.DRUG_FLAG = 1 AND T.BILL_NO = I.INVOICE_NO AND T.INVO_CODE IN (01, 02, 03) AND T.CONFIRM_DATE < (SYSDATE + 1) ");
		sb.append(" GROUP BY T.BILL_NO,T.INVO_CODE,T.NAME,T.CONFIRM_DATE,I.DRUG_WINDOW,T.INVO_CODE ");
		sb.append("	)GROUP BY INVOICENO,PATIENTNAME,FEESTATCODE,CONFIRMDATE,INVO_CODE,SENDWIN ");
		
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
	    queryObject.addScalar("invoiceNo",Hibernate.STRING).addScalar("patientName",Hibernate.STRING)
	    			.addScalar("feeStatCode",Hibernate.STRING).addScalar("confirmDate",Hibernate.DATE)
	    			.addScalar("refundMoney",Hibernate.DOUBLE).addScalar("sendWin",Hibernate.STRING)
	    			.addScalar("invocode",Hibernate.STRING);
	    //进行查询
	  	List<RefundVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(RefundVo.class)).list();
	  	//将需要的数据插入mongodb中
	  	List<DBObject> userList = new ArrayList<DBObject>();
	  	if(list!=null&&list.size()>0){
	  			 for(RefundVo r : list){
	  				 	String confirmDate= dateFormat.format(r.getConfirmDate());
	  				 	BasicDBObject bdObject = new BasicDBObject();
	  					bdObject.append("invoiceNo", r.getInvoiceNo());
	  					bdObject.append("patientName", r.getPatientName());
	  					bdObject.append("feeStatCode", r.getFeeStatCode());
	  					bdObject.append("refundMoney", r.getRefundMoney().toString());
	  					bdObject.append("sendWin", r.getSendWin());
	  					bdObject.append("invocode", r.getInvocode());
	  					bdObject.append("confirmDate", confirmDate);
	  					userList.add(bdObject);
	  			 }
	  			mbDao.insertDataByList(TABLENAME_MZYFTFTJ, userList);
	  	}
	  	/************************************************结束导入数据******************************************/
		
		
	}
	

	@Override
	public void imTableData_GZL() {
//		 mbDao.deleteData(TABLENAME_GZL);
		mbDao = new MongoBasicDao();
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		 /******************************************开始导入挂号主表数据----------老数据**************************************************************/
		 for(int j = 0;j<7;j++){
			 StringBuffer sql = new StringBuffer();
			 sql.append("(select t.reg_date as regDate,t.dept_code as deptCode,t.doct_code as doctCode,t.reglevl_name as reglevlName, ");
			 sql.append("t.REGLEVL_CODE as reglevlCode,nvl(t.REG_TRIAGETYPE,4) as regTriageType, ");
			 sql.append("count(1) as regNum,sum(decode(t.ynbook,2,1,0)) as preRegNum, ");
			 sql.append("SUM(t.sum_cost) as totalFee, ");
			 sql.append("decode(T O_NUMBER(to_char(t.REG_DATE, 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday ");
			 sql.append(" from T_REGISTER_MAIN   partition (PART_2017) t ");
			 sql.append("  inner join t_department d on t.dept_code = d.dept_code ");
			 sql.append("where t.del_flg = 0 and d.dept_type = 'C' ");
			 switch (j) {
			case 0:
				sql.append("AND t.REG_DATE <= to_date('2017-01-31 23:59:59', 'yyyy-MM-dd HH24:MI:SS') ");
				sql.append("AND t.REG_DATE >= to_date('2017-01-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
				break;
			case 1:
				sql.append("AND t.REG_DATE <= to_date('2017-03-31 23:59:59', 'yyyy-MM-dd HH24:MI:SS') ");
				sql.append("AND t.REG_DATE >= to_date('2017-02-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
				break;
			case 2:
				 sql.append("AND t.REG_DATE <= to_date('2017-05-31 23:59:59', 'yyyy-MM-dd HH24:MI:SS') ");
				 sql.append("AND t.REG_DATE >= to_date('2017-04-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
				break;
			case 3:
				 sql.append("AND t.REG_DATE <= to_date('2016-07-31 23:59:59', 'yyyy-MM-dd HH24:MI:SS') ");
				 sql.append("AND t.REG_DATE >= to_date('2016-06-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
				break;
			case 4:
				 sql.append("AND t.REG_DATE <= to_date('2016-08-31 23:59:59', 'yyyy-MM-dd HH24:MI:SS') ");
				 sql.append("AND t.REG_DATE >= to_date('2016-08-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
				break;
			case 5:
				sql.append("AND t.REG_DATE <= to_date('2016-10-31 23:59:59', 'yyyy-MM-dd HH24:MI:SS') ");
				sql.append("AND t.REG_DATE >= to_date('2016-09-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
				break;
			case 6:
				 sql.append("AND t.REG_DATE <= to_date('2016-12-31 23:59:59', 'yyyy-MM-dd HH24:MI:SS') ");
				 sql.append("AND t.REG_DATE >= to_date('2016-11-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
				break;
			default:
				break;
			}
			 sql.append("group by t.reg_date,t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,t.REG_TRIAGETYPE) ");
//			 sql.append("order by t.dept_code");
			 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
					 .addScalar("regDate",Hibernate.DATE)             //日期
					 .addScalar("deptCode",Hibernate.STRING)         //科室code
					 .addScalar("doctCode",Hibernate.STRING)         //医生code
					 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
					 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
					 .addScalar("regTriageType",Hibernate.INTEGER)    //平诊、优诊、急诊、预约、过号、复诊
					 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
					 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
					 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
					 .addScalar("dateToday",Hibernate.INTEGER)        //周几
//					 .setParameter("seDay", sevenDay)
					 .list();
			 List<DBObject> userList = new ArrayList<DBObject>();
			 for(Object[] l : list){
				 BasicDBObject bdObject = new BasicDBObject();
				 bdObject.append("regDate",l[0].toString());
				 bdObject.append("deptCode", l[1]);
				 bdObject.append("doctCode", l[2]);
				 bdObject.append("reglevlName", l[3]);
				 bdObject.append("reglevlCode", l[4]);
				 bdObject.append("regTriageType", l[5]);
				 bdObject.append("regNum", l[6]);
				 bdObject.append("preRegNum", l[7]);
				 bdObject.append("totalFee", l[8]);
				 bdObject.append("dateToday", l[9]);
				 userList.add(bdObject);
			 }
			 if(list.size()>0){
				 mbDao.insertDataByList(TABLENAME_GZL, userList);
			 }
		 }
			 
		 /******************************************开始导入挂号主表数据----------新数据**************************************************************/
		 StringBuffer sql1 = new StringBuffer();
		 sql1.append("select t.reg_date as regDate,t.dept_code as deptCode,t.doct_code as doctCode,t.reglevl_name as reglevlName, ");
		 sql1.append("t.REGLEVL_CODE as reglevlCode,nvl(t.REG_TRIAGETYPE,4) as regTriageType, ");
		 sql1.append("count(1) as regNum,sum(decode(t.ynbook,2,1,0)) as preRegNum, ");
		 sql1.append("SUM(t.sum_cost) as totalFee, ");
		 sql1.append("decode(TO_NUMBER(to_char(t.REG_DATE, 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday ");
		 sql1.append(" from T_REGISTER_MAIN_NOW t ");
		 sql1.append("where t.del_flg = 0 ");
		 sql1.append("AND t.REG_DATE >= to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("AND t.REG_DATE < to_date(:noDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by t.reg_date,t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,t.REG_TRIAGETYPE ");
		 sql1.append("order by t.dept_code");
		 
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regDate",Hibernate.DATE)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("regTriageType",Hibernate.INTEGER)    //平诊、优诊、急诊、预约、过号、复诊
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("dateToday",Hibernate.INTEGER)        //周几
				 .setParameter("noDay", nowDay)
				 .setParameter("seDay", sevenDay)
				 .list();
		 List<DBObject> userList1 = new ArrayList<DBObject>();
		 for(Object[] l : list1){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("doctCode", l[2]);
			 bdObject.append("reglevlName", l[3]);
			 bdObject.append("reglevlCode", l[4]);
			 bdObject.append("regTriageType", l[5]);
			 bdObject.append("regNum", l[6]);
			 bdObject.append("preRegNum", l[7]);
			 bdObject.append("totalFee", l[8]);
			 bdObject.append("dateToday", l[9]);
			 userList1.add(bdObject);
		 }
		 mbDao.insertDataByList(TABLENAME_GZL, userList1);
	}

	@Override
	public void imEachDay_GZL() {
		mbDao = new MongoBasicDao();
		Date now = new Date();
		String today = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		String yesterday = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -1))+" 23:59:59";
		
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select t.reg_date as regDate,t.dept_code as deptCode,t.doct_code as doctCode,t.reglevl_name as reglevlName, ");
		sql1.append("t.REGLEVL_CODE as reglevlCode,nvl(t.REG_TRIAGETYPE,4) as regTriageType, ");
		sql1.append("count(1) as regNum,sum(decode(t.ynbook,2,1,0)) as preRegNum, ");
		sql1.append("SUM(t.sum_cost) as totalFee, ");
		sql1.append("decode(TO_NUMBER(to_char(t.REG_DATE, 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday ");
		sql1.append(" from T_REGISTER_MAIN_NOW t ");
		sql1.append("where t.del_flg = 0 ");
		sql1.append("AND t.REG_DATE >= to_date(:yesterday, 'yyyy-MM-dd HH24:MI:SS') ");
		sql1.append("AND t.REG_DATE < to_date(:today, 'yyyy-MM-dd HH24:MI:SS') ");
		sql1.append("group by t.reg_date,t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,t.REG_TRIAGETYPE ");
		sql1.append("order by t.dept_code");
		
		List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				.addScalar("regDate",Hibernate.DATE)             //日期
				.addScalar("deptCode",Hibernate.STRING)         //科室code
				.addScalar("doctCode",Hibernate.STRING)         //医生code
				.addScalar("reglevlName",Hibernate.STRING)       //医生职称
				.addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				.addScalar("regTriageType",Hibernate.INTEGER)    //平诊、优诊、急诊、预约、过号、复诊
				.addScalar("regNum",Hibernate.INTEGER)           //挂号数
				.addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				.addScalar("totalFee",Hibernate.DOUBLE)         //金额
				.addScalar("dateToday",Hibernate.INTEGER)        //周几
				.setParameter("yesterday", yesterday)
				.setParameter("today", today)
				.list();
		if(list1.size()>0){
			for(Object[] l : list1){
				Document whileFilter = new Document();
				whileFilter.append("regDate",l[0].toString());
				whileFilter.append("doctCode", l[2]);
				
				Document bdObject = new Document();
				bdObject.append("regDate",l[0].toString());
				bdObject.append("deptCode", l[1]);
				bdObject.append("doctCode", l[2]);
				bdObject.append("reglevlName", l[3]);
				bdObject.append("reglevlCode", l[4]);
				bdObject.append("regTriageType", l[5]);
				bdObject.append("regNum", l[6]);
				bdObject.append("preRegNum", l[7]);
				bdObject.append("totalFee", l[8]);
				bdObject.append("dateToday", l[9]);
				mbDao.update(TABLENAME_GZL, whileFilter, bdObject, true);
			}
		}
	}

	/**
	 * 根据时间段，导入每天的医生工作量
	 * @Author zxh
	 * @time 2017年5月23日
	 * @param startTime
	 * @param endTime
	 */
	@Override
	public void imTableData_GHYSGZLDAY(String startTime ,String endTime){
//		mbDao.deleteData(TABLENAME_GZLDAY);
		mbDao = new MongoBasicDao();
		try{
			String reg = "[0-9]{4}-[0-9]{2}-[0-9]{2}.{0,10}";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(startTime);
			Matcher matcher1 = pattern.matcher(endTime);
			if(matcher.matches()&&matcher1.matches()){
				Date sTime = DateUtils.parseDateY_M_D(startTime);
				Date eTime = DateUtils.parseDateY_M_D(endTime);
				
				String dateNum = parameterInnerDAO.getParameterByCode(HisParameters.PARINFOTIME);
//				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//				Date dTime = df.parse(df.format(new Date()));//当天时间
//				boolean isInOneDay = (DateUtils.compareDate(DateUtils.addHour(sTime, 2), eTime)!=-1)&&
//						DateUtils.formatDateY_M_D(sTime).equals(DateUtils.formatDateY_M_D(dTime));
				Date cTime = DateUtils.addDay(new Date(),-Integer.parseInt(dateNum)+1);
//				String sTableTime = DateUtils.formatDateY_M_D_H_M_S(sTime);
//				String eTableTime = DateUtils.formatDateY_M_D_H_M_S(eTime);
//				
				String sTableTime = startTime + " 00:00:00";
				String eTableTime = startTime + " 23:59:59";
				
				while(DateUtils.compareDate(sTime, eTime)==-1){
					List<String> tableList = ZoneManageUtil.getInstance().getZoneNameListForDate(HisParameters.HISONLINEDB,"T_REGISTER_MAIN",sTableTime,eTableTime);
					String tableName = tableList.get(0);
					if(DateUtils.compareDate(sTime, cTime)!=-1){
						tableName = "T_REGISTER_MAIN_NOW";
					}
					System.out.println("=============="+sTime);
					StringBuffer sql1 = new StringBuffer();
					sql1.append("select to_char(t.reg_date, 'yyyy-mm-dd') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
					sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
					sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
					sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum, ");
					sql1.append("decode(TO_NUMBER(to_char(t.REG_DATE, 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday ");
					sql1.append("from "+tableName+" t inner join t_department d on t.dept_code = d.dept_code ");
					sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
					sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
					sql1.append(" and t.REG_DATE <= to_date(:eTableTime, 'yyyy-MM-dd HH24:MI:SS') ");
					sql1.append("and t.REG_DATE >= to_date(:sTableTime, 'yyyy-MM-dd HH24:MI:SS')");
					sql1.append("group by t.REG_DATE,to_char(t.reg_date, 'yyyy-mm-dd'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
					List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
							.addScalar("regDate",Hibernate.STRING)             //日期
							.addScalar("deptCode",Hibernate.STRING)         //科室code
							.addScalar("deptName",Hibernate.STRING)         //科室code
							.addScalar("doctCode",Hibernate.STRING)         //医生code
							.addScalar("doctName",Hibernate.STRING)         //医生code
							.addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
							.addScalar("reglevlName",Hibernate.STRING)       //医生职称
							.addScalar("regNum",Hibernate.INTEGER)           //挂号数
							.addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
							.addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
							.addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
							.addScalar("totalFee",Hibernate.DOUBLE)         //金额
							.addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
							.addScalar("dateToday",Hibernate.INTEGER)          //周几
							.setParameter("sTableTime", sTableTime)
							.setParameter("eTableTime", eTableTime)
							.list();
					for(Object[] l : list1){
						Document whileFilter = new Document();
						whileFilter.append("regDate",l[0].toString());
						whileFilter.append("deptCode", l[1]);
						whileFilter.append("deptName", l[2]);
						whileFilter.append("doctCode", l[3]);
						whileFilter.append("doctName", l[4]);
						whileFilter.append("dateToday", l[13]);
						Document bdObject = new Document();
//						if(isInOneDay){
						if(false){
							BasicDBObject bdb = new BasicDBObject();
							bdb.append("regDate",l[0].toString());
							bdb.append("deptCode", l[1]);
							bdb.append("deptName", l[2]);
							bdb.append("doctCode", l[3]);
							bdb.append("doctName", l[4]);
							bdb.append("dateToday", l[13]);
							DBCursor cursor = mbDao.findAlldata(TABLENAME_GZLDAY, bdb);
							bdObject.append("regDate",l[0].toString());
							bdObject.append("deptCode", l[1]);
							bdObject.append("deptName", l[2]);
							bdObject.append("doctCode", l[3]);
							bdObject.append("doctName", l[4]);
							bdObject.append("reglevlCode", l[5]);
							bdObject.append("reglevlName", l[6]);
							int finRegNum = (int) l[7] + Integer.parseInt(cursor.next().get("regNum").toString());
							int finPreRegNum = (int) l[8] + Integer.parseInt(cursor.next().get("preRegNum").toString());
							int finXcRegNum = (int) l[9] + Integer.parseInt(cursor.next().get("xcRegNum").toString());
							int finMzRegNum = (int) l[10] + Integer.parseInt(cursor.next().get("mzRegNum").toString());
							double fintotalFee = (double) l[11] + Double.parseDouble(cursor.next().get("totalFee").toString());
							int finYnsee = (int) l[12] + Integer.parseInt(cursor.next().get("ynsee").toString());
							bdObject.append("regNum", finRegNum);
							bdObject.append("preRegNum", finPreRegNum);
							bdObject.append("xcRegNum", finXcRegNum);
							bdObject.append("mzRegNum", finMzRegNum);
							bdObject.append("totalFee", fintotalFee);
							bdObject.append("ynsee", finYnsee);
							bdObject.append("dateToday", l[13]);
//							 mbDao.update(TABLENAME_GZLDAY, whileFilter, bdObject, true);
						}else{
							bdObject.append("regDate",l[0].toString());
							bdObject.append("deptCode", l[1]);
							bdObject.append("deptName", l[2]);
							bdObject.append("doctCode", l[3]);
							bdObject.append("doctName", l[4]);
							bdObject.append("reglevlCode", l[5]);
							bdObject.append("reglevlName", l[6]);
							bdObject.append("regNum", l[7]);
							bdObject.append("preRegNum", l[8]);
							bdObject.append("xcRegNum", l[9]);
							bdObject.append("mzRegNum", l[10]);
							bdObject.append("totalFee", l[11]);
							bdObject.append("ynsee", l[12]);
							bdObject.append("dateToday", l[13]);
						}
						mbDao.update(TABLENAME_GZLDAY, whileFilter, bdObject, true);
					}
					sTime = DateUtils.addDay(DateUtils.parseDateY_M_D(sTableTime),1);
					String nextDay = DateUtils.formatDateY_M_D(sTime);
					sTableTime = nextDay + " 00:00:00";
					eTableTime = nextDay + " 23:59:59";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("============导入失败============");
		}
		System.out.println("============导入成功============");
	}
	/**
	 * 导入工作量。按天导入
	 */
	@Override
	public void imTableData_GHYSGZL() {
		 mbDao = new MongoBasicDao();
		 mbDao.deleteData("T_TEST_GZL");
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		 
		 StringBuffer sql = new StringBuffer();
		 sql.append("select regDate,deptCode,deptName,doctCode,doctName,reglevlName,reglevlCode,regNum,preRegNum,totalFee,ynsee,xcRegNum,mzRegNum,dateToday from (");
		 sql.append("select to_char(t.reg_date, 'yyyy-mm-dd') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum, ");
		 sql.append("decode(TO_NUMBER(to_char(t.REG_DATE, 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday ");
		 sql.append("from T_REGISTER_MAIN partition (PART_2017) t inner join t_department d on t.dept_code = d.dept_code ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql.append("AND t.REG_DATE < to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by  t.REG_DATE,to_char(t.reg_date, 'yyyy-mm-dd'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 for(int partitionYear = 2009;partitionYear<2017;partitionYear++){
			 if(partitionYear==2015){
				 continue;
			 }
			 sql.append("union all ");
			 sql.append("select to_char(t.reg_date, 'yyyy-mm-dd') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
			 sql.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
			 sql.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
			 sql.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum, ");
			 sql.append("decode(TO_NUMBER(to_char(t.REG_DATE, 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday ");
			 sql.append("from T_REGISTER_MAIN    partition (PART_"+partitionYear+") t inner join t_department d on t.dept_code = d.dept_code ");
//			 sql.append("from T_REGISTER_MAIN    partition (PART_2011) t inner join t_department d on t.dept_code = d.dept_code ");
			 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
			 sql.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
//			 sql.append("AND t.REG_DATE < to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
			 sql.append("group by  t.REG_DATE,to_char(t.reg_date, 'yyyy-mm-dd'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 }
		 sql.append(")");
		 
		 List<DBObject> userList = new ArrayList<DBObject>();
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
				 .addScalar("dateToday",Hibernate.INTEGER)          //周几
				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 bdObject.append("dateToday", l[13]);
			 userList.add(bdObject);
		 }
		 if(userList.size()>0){
			 mbDao.insertDataByList("T_TEST_GZL", userList);
		 }
		 
		 
		 
		 
		 
		 
		 
		 
		 StringBuffer sql1 = new StringBuffer();
		 sql1.append("select to_char(t.reg_date, 'yyyy-mm-dd') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum, ");
		 sql1.append("decode(TO_NUMBER(to_char(t.REG_DATE, 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday ");
		 sql1.append("from T_REGISTER_MAIN_NOW t inner join t_department d on t.dept_code = d.dept_code ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql1.append("AND t.REG_DATE >= to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by t.REG_DATE,to_char(t.reg_date, 'yyyy-mm-dd'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
				 .addScalar("dateToday",Hibernate.INTEGER)          //周几
				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list1){
			 Document whileFilter = new Document();
			 whileFilter.append("regDate",l[0].toString());
			 whileFilter.append("deptCode", l[1]);
			 whileFilter.append("deptName", l[2]);
			 whileFilter.append("doctCode", l[3]);
			 whileFilter.append("doctName", l[4]);
			 whileFilter.append("dateToday", l[13]);
			 Document bdObject = new Document();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 bdObject.append("dateToday", l[13]);
			 mbDao.update("T_TEST_GZL", whileFilter, bdObject, true);
		 }
	}
	@Override
	public void imEachDay_GHYSGZL() {
		mbDao = new MongoBasicDao();
		Date now = new Date();
//		String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		String nowDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -1))+" 23:59:59";
		String yesterday = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -2))+" 23:59:59";
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select trunc(t.reg_date,'dd') as regDate,t.dept_code as deptCode,d.DEPT_NAME as deptName,t.doct_code as doctCode,t.DOCT_NAME as doctName,t.reglevl_name as reglevlName, ");
		sql1.append("t.REGLEVL_CODE as reglevlCode,");
//		sql1.append("nvl(t.REG_TRIAGETYPE,4) as regTriageType,");
		sql1.append("count(1) as regNum,sum(decode(t.ynbook,2,1,0)) as preRegNum, ");
		sql1.append("SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE,'1',1,0)) as ynsee, ");
		sql1.append("decode(TO_NUMBER(to_char(trunc(t.reg_date,'dd'), 'D')),1,7,2,1,3,2,4,3,5,4,6,5,7,6) as dateToday  ");
		sql1.append(" from T_REGISTER_MAIN_NOW t ");
		sql1.append("  inner join t_department d on t.dept_code = d.dept_code ");
	    sql1.append("where t.del_flg = 0 and d.dept_type = 'C' ");
		sql1.append("AND t.REG_DATE >= to_date(:yesterday, 'yyyy-MM-dd HH24:MI:SS') ");
		sql1.append("AND t.REG_DATE < to_date(:noDay, 'yyyy-MM-dd HH24:MI:SS') ");
		sql1.append("group by trunc(t.reg_date,'dd'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,t.DOCT_NAME ");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
			 .addScalar("regDate",Hibernate.DATE)             //日期
			 .addScalar("deptCode",Hibernate.STRING)         //科室code
			 .addScalar("deptName",Hibernate.STRING)         //科室code
			 .addScalar("doctCode",Hibernate.STRING)         //医生code
			 .addScalar("doctName",Hibernate.STRING)         //医生code
			 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
			 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
//			 .addScalar("regTriageType",Hibernate.INTEGER)    //平诊、优诊、急诊、预约、过号、复诊
			 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
			 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
			 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
			 .addScalar("dateToday",Hibernate.INTEGER)        //周几
			 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
			 .setParameter("noDay", nowDay)
			 .setParameter("yesterday", yesterday)
			 .list();
		 for(Object[] l : list1){
			 Document whileFilter = new Document();
			 whileFilter.append("regDate",l[0].toString());
			 whileFilter.append("deptCode", l[1]);
			 whileFilter.append("doctCode", l[2]);
			 whileFilter.append("regTriageType", l[5]);
			 Document bdObject = new Document();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
//			 bdObject.append("regTriageType", l[7]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("totalFee", l[9]);
			 bdObject.append("dateToday", l[10]);
			 bdObject.append("ynsee", l[11]);
			 mbDao.update(TABLENAME_GZLDAY, whileFilter, bdObject, true);
		 }
	}
	/**  
	 * 
	 * 将门诊月药品金额，用药数量，人次表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午3:37:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午3:37:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inTableData_YPJE() {
		mbDao = new MongoBasicDao();
		mbDao.deleteData(TABLENAME5);
		//得到7天前的当前时间
		String ThirtyBf=DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(), -7))+" 23:59:59";
		String endTime = DateUtils.formatDateY_M_D(new Date())+" 23:59:59";
		final StringBuffer sb = new StringBuffer();
		sb.append("select nvl(sum(clinic_code),0) as total,nvl(sum(tot_cost),0) as totCost, nvl(sum(QTY),0) as num, mon as selectTime from ( ");
		sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost,");
		sb.append(" sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t  where ");
		sb.append(" t.REG_DATE >= to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date");
		sb.append(" union all ");
		sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost,");
		sb.append(" sum(decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty))) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t  where ");
		sb.append(" t.REG_DATE < to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date");
		sb.append(" union all ");
		sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost, sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon from T_OUTPATIENT_FEEDETAIL_NOW t");
		sb.append(" where REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date ");
		sb.append(" )  group by mon");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("num",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("total",Hibernate.INTEGER);
		queryObject.setParameter("ThirtyBf", ThirtyBf);
		queryObject.setParameter("endTime", endTime);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>();
		if(list!=null&&list.size()>0){
			 for(OutpatientUseMedicVo t : list){
				 	BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("selectTime", t.getSelectTime());
					bdObject.append("totCost", t.getTotCost());
					bdObject.append("num", t.getNum());
					bdObject.append("total", t.getTotal());
					userList.add(bdObject);
			 }
			mbDao.insertDataByList(TABLENAME5, userList);
		}
	}
	/**  
	 * ( 修改 )
	 * 将门诊月药品金额，用药数量，人次表导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午3:37:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午3:37:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	/*@Override
	public void inTableData_YPJE2() {
		mbDao.deleteData(TABLENAME6);
		//得到7天前的当前时间
		String ThirtyBf=DateUtils.formatDateY_M_D(DateUtils.addDay(new Date(), -7))+" 23:59:59";
		String endTime = DateUtils.formatDateY_M_D(new Date())+" 23:59:59";
		final StringBuffer sb = new StringBuffer();
		sb.append("select a.total as total,a.totCost as totCost,a.num as num,a.selectTime as selectTime,b.code_name as type from (");
		sb.append("select nvl(sum(clinic_code),0) as total,nvl(sum(tot_cost),0) as totCost, nvl(sum(QTY),0) as num, mon as selectTime, DRUG_TYPE from ( ");
		sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost,");
		sb.append(" sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon, d.DRUG_TYPE");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE where ");
		sb.append(" t.REG_DATE >= to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date, d.DRUG_TYPE");
		sb.append(" union all ");
		sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost,");
		sb.append(" sum(decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty))) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon, d.DRUG_TYPE ");
		sb.append(" from T_OUTPATIENT_FEEDETAIL t  left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE where ");
		sb.append(" t.REG_DATE < to_date('2014-11-01 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date, d.DRUG_TYPE");
		sb.append(" union all ");
		sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost, sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon, d.DRUG_TYPE from T_OUTPATIENT_FEEDETAIL_NOW t");
		sb.append(" left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE ");
		sb.append(" where REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date , d.DRUG_TYPE");
		sb.append(" )  group by mon,DRUG_TYPE )a");
		sb.append("  left join t_business_dictionary b on  b.CODE_ENCODE = a.DRUG_TYPE where b.code_type='drugType'");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("num",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("total",Hibernate.INTEGER).addScalar("type",Hibernate.STRING);
		queryObject.setParameter("ThirtyBf", ThirtyBf);
		queryObject.setParameter("endTime", endTime);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>();
		if(list!=null&&list.size()>0){
			 for(OutpatientUseMedicVo t : list){
				 if (t.getType()!=null) {
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("selectTime", t.getSelectTime());
					 bdObject.append("totCost", t.getTotCost());
					 bdObject.append("num", t.getNum());
					 bdObject.append("total", t.getTotal());
					 bdObject.append("type", t.getType());
					 userList.add(bdObject);
				}
			 }
			mbDao.insertDataByList(TABLENAME6, userList);
		}
	}*/
	@Override
	public void inTableData_YPJE2(String tnL,String begin,String end,boolean timeType) {
		mbDao = new MongoBasicDao();
		final StringBuffer sb = new StringBuffer();
		sb.append("select a.total as total,a.totCost as totCost,a.num as num,b.code_name as type from (");
		sb.append(" select nvl(sum(total),0) as total,nvl(sum(totCost),0) as totCost, nvl(sum(num),0) as num, DRUG_TYPE from ( ");
		sb.append(" select count(distinct t.clinic_code) as total, sum(t.tot_cost) as totCost,d.DRUG_TYPE,");
		if (DateUtils.parseDateY_M_D_H_M(begin).getTime()>DateUtils.parseDateY_M_D_H_M("2014-11-01 00:00:00").getTime()) {
			sb.append(" sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as num  ");
		}else{
			sb.append(" sum(decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty))) as num  ");
		}
		sb.append(" from "+tnL+" t left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE where ");
		sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by d.DRUG_TYPE)");
		sb.append(" group by DRUG_TYPE )a");
		sb.append(" left join t_business_dictionary b on  b.CODE_ENCODE = a.DRUG_TYPE where b.code_type='drugType'");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("num",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("total",Hibernate.INTEGER).addScalar("type",Hibernate.STRING);
		queryObject.setParameter("begin", begin);
		queryObject.setParameter("end", end);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		Document document = null;
		if(list!=null&&list.size()>0){
			for(OutpatientUseMedicVo vo : list){
				//将需要的数据插入mongodb中
				Document document1 = new Document();
				document1.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
				document1.append("type", vo.getType());
				if (timeType==false) {
					List<OutpatientUseMedicVo> vos=new ArrayList<OutpatientUseMedicVo>();
					BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					bdObject.append("type", vo.getType());
					DBCursor cursor = mbDao.findAlldata(TABLENAME6_1, bdObject);
					if (cursor!=null) {
						while(cursor.hasNext()){
							OutpatientUseMedicVo vo2 = new OutpatientUseMedicVo();
							DBObject dbCursor = cursor.next();
							Double  num =(Double) dbCursor.get("num");
							Double  totCost =(Double) dbCursor.get("totCost");
							int  total =(int) dbCursor.get("total");
							String  type =(String) dbCursor.get("type");
							vo2.setNum(num);
							vo2.setTotCost(totCost);
							vo2.setTotal(total);
							vo2.setType(type);
							vos.add(vo2);
						}
						document = new Document();
						document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
						document.append("totCost", vo.getTotCost()+vos.get(0).getTotCost());
						document.append("total", vo.getTotal()+vos.get(0).getTotal());
						document.append("num", vo.getNum()+vos.get(0).getNum());
						document.append("type", vo.getType());
						mbDao.update(TABLENAME6_1, document1,document,true);
					}else{
						document = new Document();
						document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
						document.append("totCost", vo.getTotCost());
						document.append("total", vo.getTotal());
						document.append("num", vo.getNum());
						document.append("type", vo.getType());
						mbDao.update(TABLENAME6_1, document1,document,true);
					}
				}else{
					document = new Document();
					document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					document.append("totCost", vo.getTotCost());
					document.append("total", vo.getTotal());
					document.append("num", vo.getNum());
					document.append("type", vo.getType());
					mbDao.update(TABLENAME6_1, document1,document,true);
				}
			}
		}
	}
	/**  
	 * 
	 * 门诊月药品金额，用药数量，人次表(YPJE)向mongodb更新每天的数据
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月15日 下午5:45:34 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月15日 下午5:45:34 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public void inEachDay_YPJE2() {
		try {
			mbDao = new MongoBasicDao();
			Date nowDate = new Date();
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			String beginTime = dateFormat.format(nowDate) + "-01 00:00:00";
			String ThirtyBf = DateUtils.formatDateY_M_D(DateUtils.addDay(nowDate,-7)) + " 23:59:59";
			String endTime = DateUtils.formatDateY_M_D(nowDate) + " 23:59:59";
			sb.append("select a.total as total,a.totCost as totCost,a.num as num,a.selectTime as selectTime,b.code_name as type from (");
			sb.append("select nvl(sum(clinic_code),0) as total,nvl(sum(tot_cost),0) as totCost, nvl(sum(QTY),0) as num, mon as selectTime, DRUG_TYPE from ( ");
			//判断是否在同一个月
			if (dateFormat.parse(ThirtyBf).equals(dateFormat.parse(endTime))) {
				sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost, sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon, d.DRUG_TYPE ");
				sb.append(" from T_OUTPATIENT_FEEDETAIL t  left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE where ");
				sb.append(" REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and REG_DATE <= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.PAY_FLAG != 0  and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date, d.DRUG_TYPE");
				sb.append(" union all ");
				sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost, sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon, d.DRUG_TYPE from T_OUTPATIENT_FEEDETAIL_NOW t");
				sb.append(" left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE ");
				sb.append(" where REG_DATE >= to_date(:ThirtyBf,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date, d.DRUG_TYPE ");
			}else{
				sb.append(" select count(distinct t.clinic_code) as clinic_code, sum(t.tot_cost) as tot_cost, sum(case  when t.ext_flag3 =1 then decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY)) else decode(t.QTY, NULL,1,DECODE(t.QTY, 0, 1, t.QTY))/decode(t.pack_qty, NULL,1,DECODE(t.pack_qty, 0, 1, t.pack_qty)) end) as QTY, to_char(t.reg_date, 'yyyy-mm') as mon, d.DRUG_TYPE from T_OUTPATIENT_FEEDETAIL_NOW t");
				sb.append(" left join t_drug_info d on t.ITEM_CODE = d.DRUG_CODE ");
				sb.append(" where REG_DATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and REG_DATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ");
				sb.append(" and t.PAY_FLAG != 0 and t.DRUG_FLAG = '1' and t.TRANS_TYPE = 1 and t.CANCEL_FLAG = 1 and t.stop_flg = 0 and t.del_flg = 0 group by t.reg_date, d.DRUG_TYPE ");
			}
			sb.append(" )  group by mon,DRUG_TYPE )a");
			sb.append("  left join t_business_dictionary b on  b.CODE_ENCODE = a.DRUG_TYPE where b.code_type='drugType'");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("num",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("total",Hibernate.INTEGER).addScalar("type",Hibernate.STRING);
			queryObject.setParameter("ThirtyBf", ThirtyBf);
			queryObject.setParameter("endTime", endTime);
			queryObject.setParameter("beginTime", beginTime);
			List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
			List<DBObject> userList = new ArrayList<DBObject>();
			if(list!=null&&list.size()>0){
				for(OutpatientUseMedicVo t : list){
					Document document1 = new Document();
					document1.append("selectTime", t.getSelectTime());
					document1.append("type", t.getType());
					
					Document document = new Document();
					document.append("selectTime", t.getSelectTime());
					document.append("totCost", t.getTotCost());
					document.append("total", t.getTotal());
					document.append("num", t.getNum());
					document.append("type", t.getType());
					mbDao.update(TABLENAME6, document1,document,true);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 有开始时间，结束时间，按月导入工作量
	 * @Author zxh
	 * @time 2017年5月23日
	 * @param startTime
	 * @param endTime
	 * @param tableList
	 */
	public void imTableData_GZLMonth1(String startTime ,String endTime, List<String> tableList){
		 mbDao = new MongoBasicDao();
		 mbDao.deleteData("T_TEST_GZL");
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M(now).substring(0, 7)+"-01 00:00:00";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7)).substring(0, 7)+"-01 00:00:00";
		 
		 try{
				String reg = "[0-9]{4}-[0-9]{2}-[0-9]{2}.{0,10}";
				Pattern pattern = Pattern.compile(reg);
				Matcher matcher = pattern.matcher(startTime);
				Matcher matcher1 = pattern.matcher(endTime);
				if(matcher.matches()&&matcher1.matches()){
					Date sTime = DateUtils.parseDateY_M_D_H_M_S(startTime);
					Date eTime = DateUtils.parseDateY_M_D_H_M_S(endTime);
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dTime = df.parse(df.format(new Date()));//当天时间
					boolean isInOneDay = (DateUtils.compareDate(DateUtils.addHour(sTime, 2), eTime)!=-1)&&
							DateUtils.formatDateY_M_D(sTime).equals(DateUtils.formatDateY_M_D(dTime));
					String sTableTime = DateUtils.formatDateY_M_D_H_M_S(sTime);
					String eTableTime = DateUtils.formatDateY_M_D_H_M_S(eTime);
						StringBuffer sql1 = new StringBuffer();
				}
				
				StringBuffer sql = new StringBuffer();
				sql.append("select regDate,deptCode,deptName,doctCode,doctName,reglevlCode,reglevlName,regNum,preRegNum,xcRegNum,mzRegNum,totalFee,ynsee from ( ");
				for(int i=0;i<tableList.size();i++){
					if(i>0){
						sql.append(" UNION ALL ");
					}
					sql.append("select to_char(t.reg_date, 'yyyy-mm') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
					sql.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
					sql.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
					sql.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
					sql.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
					sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
					sql.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
					sql.append("AND t.REG_DATE < to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
					sql.append("group by to_char(t.reg_date, 'yyyy-mm'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME )");
				}
				List<DBObject> userList = new ArrayList<DBObject>();
				List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
						.addScalar("regDate",Hibernate.STRING)             //日期
						.addScalar("deptCode",Hibernate.STRING)         //科室code
						.addScalar("deptName",Hibernate.STRING)         //科室code
						.addScalar("doctCode",Hibernate.STRING)         //医生code
						.addScalar("doctName",Hibernate.STRING)         //医生code
						.addScalar("reglevlCode",Hibernate.STRING)      //挂号级别
						.addScalar("reglevlName",Hibernate.STRING)       //医生职称
						.addScalar("regNum",Hibernate.INTEGER)           //挂号数
						.addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
						.addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
						.addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
						.addScalar("totalFee",Hibernate.DOUBLE)         //金额
						.addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
						.setParameter("seDay", sevenDay)
						.list();
				for(Object[] l : list){
					BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("regDate",l[0].toString());
					bdObject.append("deptCode", l[1]);
					bdObject.append("deptName", l[2]);
					bdObject.append("doctCode", l[3]);
					bdObject.append("doctName", l[4]);
					bdObject.append("reglevlCode", l[5]);
					bdObject.append("reglevlName", l[6]);
					bdObject.append("regNum", l[7]);
					bdObject.append("preRegNum", l[8]);
					bdObject.append("xcRegNum", l[9]);
					bdObject.append("mzRegNum", l[10]);
					bdObject.append("totalFee", l[11]);
					bdObject.append("ynsee", l[12]);
					userList.add(bdObject);
				}
				if(userList.size()>0){
					mbDao.insertDataByList("T_TEST_GZL", userList);
				}
		 }catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void imTableData_GZLMonth() {
		 mbDao = new MongoBasicDao();
		 mbDao.deleteData(TABLENAME_GZLMONTH);
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M(now).substring(0, 7)+"-01 00:00:00";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7)).substring(0, 7)+"-01 00:00:00";
		 
		 StringBuffer sql = new StringBuffer();
		 sql.append("select to_char(t.reg_date, 'yyyy-mm') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql.append("AND t.REG_DATE < to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by to_char(t.reg_date, 'yyyy-mm'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 List<DBObject> userList = new ArrayList<DBObject>();
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 userList.add(bdObject);
		 }
		 if(userList.size()>0){
			 mbDao.insertDataByList(TABLENAME_GZLMONTH, userList);
		 }
		 
		 
		 
		 
		 
		 
		 
		 
		 StringBuffer sql1 = new StringBuffer();
		 sql1.append("select regDate,deptCode,deptName,doctCode,doctName,reglevlCode,reglevlName,regNum,preRegNum,xcRegNum,mzRegNum,totalFee,ynsee from (");
		 sql1.append("select to_char(t.reg_date, 'yyyy-mm') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql1.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql1.append("AND t.REG_DATE > to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by to_char(t.reg_date, 'yyyy-mm'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 sql1.append("union all ");
		 sql1.append("select to_char(t.reg_date, 'yyyy-mm') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql1.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql1.append("AND t.REG_DATE > to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by to_char(t.reg_date, 'yyyy-mm'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME) ");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list1){
			 Document whileFilter = new Document();
			 whileFilter.append("regDate",l[0].toString());
			 whileFilter.append("deptCode", l[1]);
			 whileFilter.append("doctCode", l[2]);
			 Document bdObject = new Document();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 mbDao.update(TABLENAME_GZLMONTH, whileFilter, bdObject, true);
		 }
	}

	@Override
	public void imEachDay_GZLMonth() {
		 mbDao = new MongoBasicDao();
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M(now).substring(0, 7)+"-01 00:00:00";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7)).substring(0, 7)+"-01 00:00:00";
		 
		 StringBuffer sql = new StringBuffer();
		 sql.append("select to_char(t.reg_date, 'yyyy-mm') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql.append("AND t.REG_DATE < to_date(:seDay, 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by to_char(t.reg_date, 'yyyy-m'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 List<DBObject> userList = new ArrayList<DBObject>();
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 userList.add(bdObject);
		 }
		 if(userList.size()>0){
			 mbDao.insertDataByList(TABLENAME_GZLMONTH, userList);
		 }
		 
		 
		 
		 
		 
		 
		 
		 
		 StringBuffer sql1 = new StringBuffer();
		 sql1.append("select regDate,deptCode,deptName,doctCode,doctName,reglevlCode,reglevlName,regNum,preRegNum,xcRegNum,mzRegNum,totalFee,ynsee from (");
		 sql1.append("select to_char(t.reg_date, 'yyyy') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql1.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql1.append("AND t.REG_DATE > to_date('2017-01-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by to_char(t.reg_date, 'yyyy'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 sql1.append("union all ");
		 sql1.append("select to_char(t.reg_date, 'yyyy') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql1.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql1.append("AND t.REG_DATE > to_date('2017-01-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by to_char(t.reg_date, 'yyyy'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME) ");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
//				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list1){
			 Document whileFilter = new Document();
			 whileFilter.append("regDate",l[0].toString());
			 whileFilter.append("deptCode", l[1]);
			 whileFilter.append("doctCode", l[2]);
			 Document bdObject = new Document();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 mbDao.update(TABLENAME_GZLMONTH, whileFilter, bdObject, true);
		 }
	}

	@Override
	public void imTableData_GZLYear() {
		 mbDao = new MongoBasicDao();
		 mbDao.deleteData(TABLENAME_GZLYEAR);
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		 
		 StringBuffer sql = new StringBuffer();
		 sql.append("select to_char(t.reg_date, 'yyyy') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql.append("AND t.REG_DATE < to_date('2017-01-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by to_char(t.reg_date, 'yyyy'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 List<DBObject> userList = new ArrayList<DBObject>();
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
//				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 userList.add(bdObject);
		 }
		 if(userList.size()>0){
			 mbDao.insertDataByList(TABLENAME_GZLYEAR, userList);
		 }
		 
		 
		 
		 
		 StringBuffer sql1 = new StringBuffer();
		 sql1.append("select regDate,deptCode,deptName,doctCode,doctName,reglevlCode,reglevlName,regNum,preRegNum,xcRegNum,mzRegNum,totalFee,ynsee from (");
		 sql1.append("select to_char(t.reg_date, 'yyyy') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql1.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql1.append("AND t.REG_DATE > to_date('2017-01-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by to_char(t.reg_date, 'yyyy'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 sql1.append("union all ");
		 sql1.append("select to_char(t.reg_date, 'yyyy') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql1.append(" t.doct_code as doctCode, E.EMPLOYEE_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql1.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql1.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql1.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql1.append("AND t.REG_DATE > to_date('2017-01-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by to_char(t.reg_date, 'yyyy'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME) ");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
//				 .setParameter("seDay", sevenDay)
				 .list();
		 for(Object[] l : list1){
			 Document whileFilter = new Document();
			 whileFilter.append("regDate",l[0].toString());
			 whileFilter.append("deptCode", l[1]);
			 whileFilter.append("doctCode", l[2]);
			 Document bdObject = new Document();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 mbDao.update(TABLENAME_GZLYEAR, whileFilter, bdObject, true);
		 }
		
	}

	@Override
	public void imEachDay_GZLYear() {
		mbDao = new MongoBasicDao();
		Date now = new Date();
		String nowDay = DateUtils.formatDateY_M(now).substring(0, 5)+"01-01 00:00:00";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		 
		 StringBuffer sql = new StringBuffer();
		 sql.append("select to_char(t.reg_date, 'yyyy') as regDate, t.dept_code as deptCode, d.DEPT_NAME as deptName, ");
		 sql.append(" e.employee_name as doctCode, t.DOCT_NAME as doctName,  t.reglevl_name as reglevlName, t.REGLEVL_CODE as reglevlCode, count(1) as regNum, ");
		 sql.append("sum(decode(t.ynbook, 2, 1, 0)) as preRegNum, SUM(t.sum_cost) as totalFee, sum(decode(t.YNSEE, '1', 1, 0)) as ynsee, ");
		 sql.append("sum(decode(t.ynbook, 1, 1, 0)) as xcRegNum,sum(decode(t.ynbook, 3, 1, 0)) as mzRegNum ");
		 sql.append("from T_REGISTER_MAIN t inner join t_department d on t.dept_code = d.dept_code ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" where t.del_flg = 0 and d.dept_type = 'C' ");
		 sql.append("AND t.REG_DATE < to_date('2017-01-01 00:00:00', 'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by to_char(t.reg_date, 'yyyy'),t.dept_code,t.doct_code,t.reglevl_name,t.REGLEVL_CODE,d.DEPT_NAME,E.EMPLOYEE_NAME ");
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regDate",Hibernate.STRING)             //日期
				 .addScalar("deptCode",Hibernate.STRING)         //科室code
				 .addScalar("deptName",Hibernate.STRING)         //科室code
				 .addScalar("doctCode",Hibernate.STRING)         //医生code
				 .addScalar("doctName",Hibernate.STRING)         //医生code
				 .addScalar("reglevlCode",Hibernate.INTEGER)      //挂号级别
				 .addScalar("reglevlName",Hibernate.STRING)       //医生职称
				 .addScalar("regNum",Hibernate.INTEGER)           //挂号数
				 .addScalar("preRegNum",Hibernate.INTEGER)        //预约挂号数
				 .addScalar("xcRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("mzRegNum",Hibernate.INTEGER)          //看诊人数
				 .addScalar("totalFee",Hibernate.DOUBLE)         //金额
				 .addScalar("ynsee",Hibernate.INTEGER)          //看诊人数
//				 .setParameter("seDay", sevenDay)
				 .list();
		 List<DBObject> userList = new ArrayList<DBObject>();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regDate",l[0].toString());
			 bdObject.append("deptCode", l[1]);
			 bdObject.append("deptName", l[2]);
			 bdObject.append("doctCode", l[3]);
			 bdObject.append("doctName", l[4]);
			 bdObject.append("reglevlCode", l[5]);
			 bdObject.append("reglevlName", l[6]);
			 bdObject.append("regNum", l[7]);
			 bdObject.append("preRegNum", l[8]);
			 bdObject.append("xcRegNum", l[9]);
			 bdObject.append("mzRegNum", l[10]);
			 bdObject.append("totalFee", l[11]);
			 bdObject.append("ynsee", l[12]);
			 userList.add(bdObject);
		 }
		 if(userList.size()>0){
			 mbDao.insertDataByList(TABLENAME_GZLYEAR, userList);
		 }
	}

	@Override
	public void imTableData_MZCFGZL() {
		 mbDao = new MongoBasicDao();
		 mbDao.deleteData(TABLENAME_MZCFDAY);
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		 
		 StringBuffer sql = new StringBuffer();
		 sql.append("select trunc(t.reg_date,'dd') as regDate,t.doct_code as doctCode,e.employee_name as doctName,t.doct_dept as deptCode,d.dept_name as deptName,count(1) as cfNum,sum(t.tot_cost) as totCost ");
		 sql.append("from t_outpatient_feedetail t ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" inner join t_department d on t.DOCT_DEPT = d.dept_code ");
		 sql.append("where t.del_flg = 0 and t.stop_flg = 0 and  ");
		 sql.append("t.reg_date < to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by t.doct_code,t.doct_dept,e.employee_name,d.dept_name,trunc(t.reg_date,'dd') ");
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regdate",Hibernate.DATE)//开方时间
				 .addScalar("doctCode",Hibernate.STRING)//医生code
				 .addScalar("doctName",Hibernate.STRING)//医生姓名
				 .addScalar("deptCode",Hibernate.STRING)//科室code
				 .addScalar("deptName",Hibernate.STRING)//科室姓名
				 .addScalar("cfNum",Hibernate.INTEGER)//处方数
				 .addScalar("totCost",Hibernate.DOUBLE)//处方金额
				 .setParameter("seDay", sevenDay)
				 .list();
		 List<DBObject> userList = new ArrayList<DBObject>();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regdate", l[0].toString());
			 bdObject.append("doctCode", l[1]);
			 bdObject.append("doctName", l[2]);
			 bdObject.append("deptCode", l[3]);
			 bdObject.append("deptName", l[4]);
			 bdObject.append("cfNum", l[5]);
			 bdObject.append("totCost", l[6]);
			 userList.add(bdObject);
		 }
		 mbDao.insertDataByList(TABLENAME_MZCFDAY, userList);
		 
		 StringBuffer sql1 = new StringBuffer();
		 sql1.append("select trunc(t.reg_date,'dd') as regDate,t.doct_code as doctCode,e.employee_name as doctName,t.doct_dept as deptCode,d.dept_name as deptName,count(1) as cfNum,sum(t.tot_cost) as totCost ");
		 sql1.append("from t_outpatient_feedetail_now t ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" inner join t_department d on t.DOCT_DEPT = d.dept_code ");
		 sql1.append("where t.del_flg = 0 and t.stop_flg = 0 and  ");
		 sql1.append("t.reg_date >= to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("and t.reg_date < to_date(:noDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by t.doct_code,t.doct_dept,e.employee_name,d.dept_name,trunc(t.reg_date,'dd') ");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regdate",Hibernate.DATE)//开方时间
				 .addScalar("doctCode",Hibernate.STRING)//医生code
				 .addScalar("doctName",Hibernate.STRING)//医生姓名
				 .addScalar("deptCode",Hibernate.STRING)//科室code
				 .addScalar("deptName",Hibernate.STRING)//科室姓名
				 .addScalar("cfNum",Hibernate.INTEGER)//处方数
				 .addScalar("totCost",Hibernate.DOUBLE)//处方金额
				 .setParameter("seDay", sevenDay)
				 .setParameter("noDay", nowDay)
				 .list();
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
		 mbDao.insertDataByList(TABLENAME_MZCFDAY, userList1);		
	}

	@Override
	public void imTableData_MZCFGZLMonth() {
		mbDao = new MongoBasicDao();
		mbDao.deleteData(TABLENAME_MZCFMONTH);
		
		Date now = new Date();
		String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		
		StringBuffer sql = new StringBuffer();
		 sql.append("select to_char(t.reg_date, 'yyyy-mm') as regDate,t.doct_code as doctCode,e.employee_name as doctName,t.doct_dept as deptCode,d.dept_name as deptName,count(1) as cfNum,sum(t.tot_cost) as totCost ");
		 sql.append("from t_outpatient_feedetail_now t ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" inner join t_department d on t.DOCT_DEPT = d.dept_code ");
		 sql.append("where t.del_flg = 0 and t.stop_flg = 0 and  ");
//		 sql.append("t.reg_date >= to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("t.reg_date < to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by t.doct_code,t.doct_dept,e.employee_name,d.dept_name, to_char(t.reg_date, 'yyyy-mm') ");
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regdate",Hibernate.STRING)//开方时间
				 .addScalar("doctCode",Hibernate.STRING)//医生code
				 .addScalar("doctName",Hibernate.STRING)//医生姓名
				 .addScalar("deptCode",Hibernate.STRING)//科室code
				 .addScalar("deptName",Hibernate.STRING)//科室姓名
				 .addScalar("cfNum",Hibernate.INTEGER)//处方数
				 .addScalar("totCost",Hibernate.DOUBLE)//处方金额
				 .setParameter("seDay", sevenDay)
//				 .setParameter("noDay", nowDay)
				 .list();
		 List<DBObject> userList = new ArrayList<DBObject>();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regdate", l[0].toString());
			 bdObject.append("doctCode", l[1]);
			 bdObject.append("doctName", l[2]);
			 bdObject.append("deptCode", l[3]);
			 bdObject.append("deptName", l[4]);
			 bdObject.append("cfNum", l[5]);
			 bdObject.append("totCost", l[6]);
			 userList.add(bdObject);
		 }
		 mbDao.insertDataByList(TABLENAME_MZCFMONTH, userList);
		 
		 /*StringBuffer sql1 = new StringBuffer();
		 sql1.append("select regDate,doctCode,doctName,deptCode,deptName,cfNum,totCost from (");
		 sql1.append("select to_char(t.reg_date, 'yyyy-mm') as regDate,t.doct_code as doctCode,e.employee_name as doctName,t.doct_dept as deptCode,d.dept_name as deptName,count(1) as cfNum,sum(t.tot_cost) as totCost ");
		 sql1.append("from t_outpatient_feedetail t ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" inner join t_department d on t.DOCT_DEPT = d.dept_code ");
		 sql1.append("where t.del_flg = 0 and t.stop_flg = 0 and  ");
		 sql1.append("t.reg_date >= to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("t.reg_date < to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by t.doct_code,t.doct_dept,e.employee_name,d.dept_name, to_char(t.reg_date, 'yyyy-mm') ");
		 sql.append("union all ");
		 sql1.append("select to_char(t.reg_date, 'yyyy-mm') as regDate,t.doct_code as doctCode,e.employee_name as doctName,t.doct_dept as deptCode,d.dept_name as deptName,count(1) as cfNum,sum(t.tot_cost) as totCost ");
		 sql1.append("from t_outpatient_feedetail_now t ");
		 sql1.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql1.append(" inner join t_department d on t.DOCT_DEPT = d.dept_code ");
		 sql1.append("where t.del_flg = 0 and t.stop_flg = 0 and  ");
		 sql1.append("t.reg_date >= to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("t.reg_date < to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql1.append("group by t.doct_code,t.doct_dept,e.employee_name,d.dept_name, to_char(t.reg_date, 'yyyy-mm') )");
		 List<Object[]> list1 = this.getSession().createSQLQuery(sql1.toString())
				 .addScalar("regdate",Hibernate.STRING)//开方时间
				 .addScalar("doctCode",Hibernate.STRING)//医生code
				 .addScalar("doctName",Hibernate.STRING)//医生姓名
				 .addScalar("deptCode",Hibernate.STRING)//科室code
				 .addScalar("deptName",Hibernate.STRING)//科室姓名
				 .addScalar("cfNum",Hibernate.INTEGER)//处方数
				 .addScalar("totCost",Hibernate.DOUBLE)//处方金额
				 .setParameter("seDay", sevenDay)
//				 .setParameter("noDay", nowDay)
				 .list();
		 List<DBObject> userList1 = new ArrayList<DBObject>();
		 for(Object[] l : list){
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
		 mbDao.insertDataByList(TABLENAME_MZCFMONTH, userList1);*/
	}

	@Override
	public void imTableData_MZCFGZLYear() {
//		mbDao.deleteData(TABLENAME_MZCFYEAR);
		
		Date now = new Date();
		String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		String sevenDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -7))+" 23:59:59";
		 
		StringBuffer sql = new StringBuffer();
		 sql.append("select to_char(t.reg_date, 'yyyy') as regDate,t.doct_code as doctCode,e.employee_name as doctName,t.doct_dept as deptCode,d.dept_name as deptName,count(1) as cfNum,sum(t.tot_cost) as totCost ");
		 sql.append("from t_outpatient_feedetail t ");
		 sql.append("inner join t_employee e on t.doct_code = e.employee_jobno ");
		 sql.append(" inner join t_department d on t.DOCT_DEPT = d.dept_code ");
		 sql.append("where t.del_flg = 0 and t.stop_flg = 0 and  ");
//		 sql.append("t.reg_date >= to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("t.reg_date < to_date(:seDay,'yyyy-MM-dd HH24:MI:SS') ");
		 sql.append("group by t.doct_code,t.doct_dept,e.employee_name,d.dept_name,to_char(t.reg_date, 'yyyy') ");
		 List<Object[]> list = this.getSession().createSQLQuery(sql.toString())
				 .addScalar("regdate",Hibernate.STRING)//开方时间
				 .addScalar("doctCode",Hibernate.STRING)//医生code
				 .addScalar("doctName",Hibernate.STRING)//医生姓名
				 .addScalar("deptCode",Hibernate.STRING)//科室code
				 .addScalar("deptName",Hibernate.STRING)//科室姓名
				 .addScalar("cfNum",Hibernate.INTEGER)//处方数
				 .addScalar("totCost",Hibernate.DOUBLE)//处方金额
				 .setParameter("seDay", sevenDay)
//				 .setParameter("noDay", nowDay)
				 .list();
		 List<DBObject> userList = new ArrayList<DBObject>();
		 for(Object[] l : list){
			 BasicDBObject bdObject = new BasicDBObject();
			 bdObject.append("regdate", l[0].toString());
			 bdObject.append("doctCode", l[1]);
			 bdObject.append("doctName", l[2]);
			 bdObject.append("deptCode", l[3]);
			 bdObject.append("deptName", l[4]);
			 bdObject.append("cfNum", l[5]);
			 bdObject.append("totCost", l[6]);
			 userList.add(bdObject);
		 }
		 mbDao.insertDataByList(TABLENAME_MZCFYEAR, userList);
	}
	
	/**  
	 * 
	 * 住院收入统计表（ZYSRTJ）导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月17日 下午7:07:52 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月17日 下午7:07:52 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	/*@Override
	public void inTableData_ZYSRTJ() {
		mbDao.deleteData(TABLENAME7);
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String monthDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -30))+" 23:59:59";
		 StringBuffer sb = new StringBuffer();
		 sb.append(" select b.cost1 as cost1,b.cost2 as cost2, o.DEPT_NAME as regDpcdName,b.mon as selectTime from ( ");
		 sb.append(" select sum(a.cost1) as cost1,sum(a.cost2) as cost2, a.deptCode ,a.mon  from ( ");
		 sb.append(" select sum(t.tot_cost) as cost1,0 as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_MEDICINELIST_NOW t  where ");
		 sb.append(" t.FEE_DATE >= to_date(:monthDay,'yyyy-mm-dd hh24:mi:ss') ");
		 sb.append(" and t.FEE_DATE <= to_date(:nowDay,'yyyy-mm-dd hh24:mi:ss') ");
		 sb.append(" and t.senddrug_flag = 1  and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" union all ");
		 sb.append(" select 0 as cost1,sum(t.tot_cost) as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_ITEMLIST_NOW t where ");
		 sb.append(" t.FEE_DATE >= to_date(:monthDay,'yyyy-mm-dd hh24:mi:ss')");
		 sb.append(" and t.FEE_DATE <= to_date(:nowDay,'yyyy-mm-dd hh24:mi:ss')");
		 sb.append(" and t.send_flag = 1 and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" union all ");
		 sb.append(" select sum(t.tot_cost) as cost1,0 as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_MEDICINELIST t  where ");
		 sb.append(" t.senddrug_flag = 1  and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" union all ");
		 sb.append(" select 0 as cost1,sum(t.tot_cost) as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_ITEMLIST t where ");
		 sb.append(" t.send_flag = 1 and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" ) a group by a.deptCode,a.mon ) b");
		 sb.append(" left join T_DEPARTMENT o on o.DEPT_CODE=b.deptCode ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("cost1",Hibernate.DOUBLE).addScalar("cost2",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("regDpcdName",Hibernate.STRING);
		queryObject.setParameter("nowDay", nowDay); 
		queryObject.setParameter("monthDay", monthDay);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>(); 
		if(list!=null&&list.size()>0){
			 for(OutpatientUseMedicVo t : list){
				 if (t.getRegDpcdName()!=null) {
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("selectTime", t.getSelectTime());
					 bdObject.append("cost1", t.getCost1());
					 bdObject.append("cost2", t.getCost2());
					 bdObject.append("regDpcdName", t.getRegDpcdName());
					 userList.add(bdObject);
				}
			 }
			mbDao.insertDataByList(TABLENAME7, userList);
		}
		
	}*/
	@Override
	public void inTableData_ZYSRTJ() {
		mbDao = new MongoBasicDao();
		mbDao.deleteData(TABLENAME7);
		 Date now = new Date();
		 String nowDay = DateUtils.formatDateY_M_D(now)+" 23:59:59";
		 String monthDay = DateUtils.formatDateY_M_D(DateUtils.addDay(now, -30))+" 23:59:59";
		 StringBuffer sb = new StringBuffer();
		 sb.append(" select b.cost1 as cost1,b.cost2 as cost2, o.DEPT_NAME as regDpcdName,b.mon as selectTime from ( ");
		 sb.append(" select sum(a.cost1) as cost1,sum(a.cost2) as cost2, a.deptCode ,a.mon  from ( ");
		 sb.append(" select sum(t.tot_cost) as cost1,0 as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_MEDICINELIST_NOW t  where ");
		 sb.append(" t.FEE_DATE >= to_date(:monthDay,'yyyy-mm-dd hh24:mi:ss') ");
		 sb.append(" and t.FEE_DATE <= to_date(:nowDay,'yyyy-mm-dd hh24:mi:ss') ");
		 sb.append(" and t.senddrug_flag = 1  and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" union all ");
		 sb.append(" select 0 as cost1,sum(t.tot_cost) as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_ITEMLIST_NOW t where ");
		 sb.append(" t.FEE_DATE >= to_date(:monthDay,'yyyy-mm-dd hh24:mi:ss')");
		 sb.append(" and t.FEE_DATE <= to_date(:nowDay,'yyyy-mm-dd hh24:mi:ss')");
		 sb.append(" and t.send_flag = 1 and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" union all ");
		 sb.append(" select sum(t.tot_cost) as cost1,0 as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_MEDICINELIST t  where ");
		 sb.append(" t.senddrug_flag = 1  and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" union all ");
		 sb.append(" select 0 as cost1,sum(t.tot_cost) as cost2, t.recipe_deptcode as deptCode,to_char(t.FEE_DATE, 'yyyy-mm') as mon ");
		 sb.append(" from T_INPATIENT_ITEMLIST t where ");
		 sb.append(" t.send_flag = 1 and t.trans_type = 1 and t.stop_flg = 0 and t.del_flg = 0 ");
		 sb.append(" group by t.recipe_deptcode,t.FEE_DATE ");
		 sb.append(" ) a group by a.deptCode,a.mon ) b");
		 sb.append(" left join T_DEPARTMENT o on o.DEPT_CODE=b.deptCode ");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("cost1",Hibernate.DOUBLE).addScalar("cost2",Hibernate.DOUBLE).addScalar("selectTime",Hibernate.STRING).addScalar("regDpcdName",Hibernate.STRING);
		queryObject.setParameter("nowDay", nowDay); 
		queryObject.setParameter("monthDay", monthDay);
		List<OutpatientUseMedicVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).list();
		//将需要的数据插入mongodb中
		List<DBObject> userList = new ArrayList<DBObject>(); 
		if(list!=null&&list.size()>0){
			 for(OutpatientUseMedicVo t : list){
				 if (t.getRegDpcdName()!=null) {
					 BasicDBObject bdObject = new BasicDBObject();
					 bdObject.append("selectTime", t.getSelectTime());
					 bdObject.append("cost1", t.getCost1());
					 bdObject.append("cost2", t.getCost2());
					 bdObject.append("regDpcdName", t.getRegDpcdName());
					 userList.add(bdObject);
				}
			 }
			mbDao.insertDataByList(TABLENAME7, userList);
		}
		
	}
	/**
	 *@Description:门诊各项收入统计：把数据导入mongodb中
	 *处方明细表:T_OUTPATIENT_FEEDETAIL  T_OUTPATIENT_FEEDETAIL_NOW 
	 * @author: zhangkui
	 * @time:2017年5月18日 
	 */
	public void imTableData_T_OUTPATIENT_FEEDETAIL(List<ReportVo> gcode){
		mbDao = new MongoBasicDao();
		mbDao.deleteData(TABLENAME_MZGXSRTJ);
		/************************************************开始导入数据******************************************/
//		List<ReportVO> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code
	//	int count=0;//记录导入数据的总数
	//for(int j=1;j<=9;j++){//---->批量导入
		List<String> table= new ArrayList<String>();
		table.add("T_OUTPATIENT_FEEDETAIL_NOW");
		table.add("T_OUTPATIENT_FEEDETAIL");
		//检查费
		StringBuffer jcfcode = new StringBuffer();
		//治疗费
		StringBuffer zlfcode = new StringBuffer();
		//放射费
		StringBuffer fsfcode = new StringBuffer();
		//化验费
		StringBuffer hyfcode = new StringBuffer();
		//输血费
		StringBuffer sxfcode = new StringBuffer();
		//西药费
		StringBuffer xyfcode = new StringBuffer();
		//中成药费
		StringBuffer zcyfcode = new StringBuffer();
		//中草药费
		StringBuffer zcysfcode = new StringBuffer();
		for (ReportVo vo : gcode) {
			if(("07").equals(vo.getFeestatcode())){
				jcfcode.append(vo.getMinfeecode()+",");
			}
			if(("05").equals(vo.getFeestatcode())){
				zlfcode.append(vo.getMinfeecode()+",");
			}
			if(("08").equals(vo.getFeestatcode())){
				fsfcode.append(vo.getMinfeecode()+",");
			}
			if(("09").equals(vo.getFeestatcode())){
				hyfcode.append(vo.getMinfeecode()+",");
			}
			if(("01").equals(vo.getFeestatcode())){
				xyfcode.append(vo.getMinfeecode()+",");
			}
			if(("02").equals(vo.getFeestatcode())){
				zcyfcode.append(vo.getMinfeecode()+",");
			}
			if(("03").equals(vo.getFeestatcode())){
				zcysfcode.append(vo.getMinfeecode()+",");
			}
			if(("11").equals(vo.getFeestatcode())){
				sxfcode.append(vo.getMinfeecode()+",");
			}
			
		}
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT min(doct) doct,min(dept) dept,regDate, deptCode, docterCode,SUM(jcfkds) jcfkds,SUM(jcfje) jcfje,SUM(zlfkds) zlfkds,");
		sb.append("SUM(zlfje) zlfje,SUM(fsfkds) fsfkds,SUM(fsfje) fsfje,SUM(hyfkds) hyfkds,SUM(hyfje) hyfje,SUM(sxfkds) sxfkds,SUM(sxfje) sxfje,");
		sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0)-NVL(SUM(sxfkds),0)-NVL(SUM(hyfkds),0)-NVL(SUM(fsfkds),0)-NVL(SUM(zlfkds),0)-NVL(SUM(jcfkds),0) qtsrkds,");
		sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0)-NVL(SUM(sxfje),0)-NVL(SUM(hyfje),0)-NVL(SUM(fsfje),0)-NVL(SUM(zlfje),0)-NVL(SUM(jcfje),0) ylsfje,");
		sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0) ylsrkds,");
		sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0) MedicalCost,SUM(xyfkds) xyfkds,");
		sb.append("SUM(xyfje) xyfje,SUM(zcyfkds) zcyfkds,SUM(zcyfje) zcyfje,SUM(zcykds) zcykds,SUM(zcyje) zcyje,");
		sb.append("NVL(SUM(xyfkds),0)+NVL(SUM(zcyfkds),0)+NVL(SUM(zcykds),0) ypsrkds,");
		sb.append("NVL(SUM(xyfje),0)+NVL(SUM(zcyfje),0)+NVL(SUM(zcyje),0) ypsrkje,SUM(zkds) zkds,SUM(zje) zje");
		sb.append(" from (");
	    for (int i = 0; i < table.size(); i++) {
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			//检查费开单数
			sb.append("SELECT min(e.EMPLOYEE_NAME) doct,min(p.DEPT_NAME) dept,TO_CHAR(f.REG_DATE,'yyyy-mm-dd') regDate,f.REG_DPCD deptCode,f.DOCT_CODE docterCode,DECODE(f.FEE_CODE, ");
			String[] splitkds = jcfcode.toString().split(",");
			for (String string : splitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0 ) jcfkds,");
			//检查费金额
			sb.append("DECODE(f.FEE_CODE, ");
			String[] splitje = jcfcode.toString().split(",");
			for (String string : splitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) jcfje,");
			//治疗费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zlfsplitkds = zlfcode.toString().split(",");
			for (String string : zlfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) zlfkds,");
			//治疗费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zlfsplitje = zlfcode.toString().split(",");
			for (String string : zlfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zlfje,");
			//放射费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] fsfsplitkds = fsfcode.toString().split(",");
			for (String string : fsfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) fsfkds,");
			//放射费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] fsfsplitjs = fsfcode.toString().split(",");
			for (String string : fsfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) fsfje,");
			//化验费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] hyfsplitkds = hyfcode.toString().split(",");
			for (String string : hyfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) hyfkds,");
			//化验费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] hyfsplitjs = hyfcode.toString().split(",");
			for (String string : hyfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) hyfje,");
			//输血费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] sxfsplitkds = sxfcode.toString().split(",");
			for (String string : sxfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) sxfkds,");
			//输血费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] sxfsplitjs = sxfcode.toString().split(",");
			for (String string : sxfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) sxfje,");
			//西药费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] xyfsplitkds = xyfcode.toString().split(",");
			for (String string : xyfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) xyfkds,");
			//西药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] xyfsplitje = xyfcode.toString().split(",");
			for (String string : xyfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) xyfje,");
			//中成药开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcyfsplitkds = zcyfcode.toString().split(",");
			for (String string : zcyfsplitkds) {
				sb.append("'"+string).append("',1,");
			}
			sb.append("0) zcyfkds,");
			//中成药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcyfsplitje = zcyfcode.toString().split(",");
			for (String string : zcyfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zcyfje,");
			//中草药费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcysfsplitkds = zcysfcode.toString().split(",");
			for (String string : zcysfsplitkds) {
				sb.append("'"+string).append("',1,");
			}
			sb.append("0) zcykds,");
			//中草药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcysfsplitje = zcysfcode.toString().split(",");
			for (String string : zcysfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zcyje,");
			
			sb.append("COUNT(f.FEE_CODE) zkds,f.TOT_COST zje FROM  ");
			sb.append(table.get(i)).append(" f ");
			sb.append(" ,T_DEPARTMENT p,T_EMPLOYEE e ");//AND f.REG_DATE >= (SYSDATE-365)----->批量导入时使用AND f.REG_DATE >= (SYSDATE-"+365*j+") ,f.REG_DATE < (SYSDATE -"+365*(j-1)+")
			sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.REG_DATE < (SYSDATE +1) and p.DEPT_CODE=f.REG_DPCD and e.EMPLOYEE_JOBNO=f.DOCT_CODE ");
//			if(j==1){
//				sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.REG_DATE >= (SYSDATE-"+365*j+") AND f.REG_DATE < (SYSDATE +1) and p.DEPT_CODE=f.REG_DPCD and e.EMPLOYEE_JOBNO=f.DOCT_CODE ");
//			}else{
//				sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.REG_DATE >= (SYSDATE-"+365*j+") AND f.REG_DATE < (SYSDATE -"+365*(j-1)+") and p.DEPT_CODE=f.REG_DPCD and e.EMPLOYEE_JOBNO=f.DOCT_CODE ");
//			}
			sb.append(" GROUP BY f.REG_DPCD,f.DOCT_CODE,f.FEE_CODE,f.TOT_COST,f.REG_DATE ");
		}
		sb.append(" ) GROUP BY deptCode,docterCode,REGDATE ");
		//进行查询
		List<StatisticsVo> list =namedParameterJdbcTemplate.query(sb.toString(),new RowMapper<StatisticsVo>() {
			@Override
			public StatisticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StatisticsVo vo = new StatisticsVo();
				vo.setRegDate(rs.getString("regDate"));
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDocterCode(rs.getString("docterCode"));
				
				vo.setName(rs.getString("doct"));
				vo.setDept(rs.getString("dept"));
				vo.setInspectNum(rs.getInt("jcfkds"));
				vo.setInspectCost(rs.getDouble("jcfje"));
				vo.setTreatmentNum(rs.getInt("zlfkds"));
				vo.setTreatmentCost(rs.getDouble("zlfje"));
				vo.setRadiationNum(rs.getInt("fsfkds"));
				vo.setRadiationCost(rs.getDouble("fsfje"));
				vo.setBloodNum(rs.getInt("sxfkds"));
				vo.setBloodCost(rs.getDouble("sxfje"));
				vo.setTestNum(rs.getInt("hyfkds"));
				vo.setTestCost(rs.getDouble("hyfje"));
				vo.setOtherNum(rs.getInt("qtsrkds"));
				vo.setOtherCost(rs.getDouble("ylsfje"));
				vo.setMedicalNum(rs.getInt("ylsrkds"));
				vo.setMedicalCost(rs.getDouble("MedicalCost"));
				vo.setWesternNum(rs.getInt("xyfkds"));
				vo.setWesternCost(rs.getDouble("xyfje"));
				vo.setChineseNum(rs.getInt("zcyfkds"));
				vo.setChineseCost(rs.getDouble("zcyfje"));
				vo.setHerbalNum(rs.getInt("zcykds"));
				vo.setHerbalCost(rs.getDouble("zcyje"));
				vo.setAllNum(rs.getInt("ypsrkds"));
				vo.setAllCost(rs.getDouble("ypsrkje"));
				vo.setTotle(rs.getDouble("zje"));
				return vo;
			}
		});
		
	  	//将需要的数据插入mongodb中
	  	List<DBObject> userList = new ArrayList<DBObject>();
	  	if(list!=null&&list.size()>0){
	  			 for(StatisticsVo r : list){
	  				 	//String confirmDate= dateFormat.format(r.getConfirmDate());
	  				 	BasicDBObject bdObject = new BasicDBObject();
	  					bdObject.append("regDate", r.getRegDate());
	  					bdObject.append("deptCode", r.getDeptCode());
	  					bdObject.append("docterCode", r.getDocterCode());
	  					
	  					bdObject.append("name", r.getName());
	  					bdObject.append("dept", r.getDept());
	  					bdObject.append("inspectnum", r.getInspectNum().toString());
	  					bdObject.append("inspectcost", r.getInspectCost().toString());
	  					bdObject.append("treatmentnum", r.getTreatmentNum().toString());
	  					bdObject.append("treatmentcost", r.getTreatmentCost().toString());
	  					bdObject.append("radiationnum", r.getRadiationNum().toString());
	  					bdObject.append("radiationcost", r.getRadiationCost().toString());
	  					bdObject.append("bloodnum", r.getBloodNum().toString());
	  					bdObject.append("bloodcost", r.getBloodCost().toString());
	  					bdObject.append("testnum", r.getTestNum().toString());
	  					bdObject.append("testcost", r.getTestCost().toString());
	  					bdObject.append("othernum", r.getOtherNum().toString());
	  					bdObject.append("othercost", r.getOtherCost().toString());
	  					bdObject.append("medicalnum", r.getMedicalNum().toString());
	  					bdObject.append("medicalcost", r.getMedicalCost().toString());
	  					bdObject.append("westernnum", r.getWesternNum().toString());
	  					bdObject.append("westerncost", r.getWesternCost().toString());
	  					bdObject.append("chinesenum", r.getChineseNum().toString());
	  					bdObject.append("chinesecost", r.getChineseCost().toString());
	  					bdObject.append("herbalnum", r.getHerbalNum().toString());
	  					bdObject.append("herbalcost", r.getHerbalCost().toString());
	  					bdObject.append("allnum", r.getAllNum().toString());
	  					bdObject.append("allcost", r.getAllCost().toString());
	  					bdObject.append("totle", r.getTotle().toString());
	  					userList.add(bdObject);
	  			 }
	  			mbDao.insertDataByList(TABLENAME_MZGXSRTJ, userList);
	  			//count+=userList.size();
	  	}
	  	
	//}
	//System.out.println("插入成功！本次插入:"+count+" 条数据！");
	  	/************************************************结束导入数据******************************************/
		
		
	}
	

	
	
	
	
	/**
	 *@Description:把门诊各项收入统计：把每天now表数据更新到mongodb中
	 *处方明细表: T_OUTPATIENT_FEEDETAIL_NOW 
	 * @author: zhangkui
	 * @time:2017年5月20日 
	 */
	public void imEachDay_T_OUTPATIENT_FEEDETAIL(List<ReportVo> gcode){
		mbDao = new MongoBasicDao();
		/************************************************开始更新当天数据******************************************/
//		List<ReportVO> encode = reportFormsjdbcDao.getEncode();//获取费用类别fee_code
		//for(int j=1;j<=9;j++){---->批量导入
		List<String> table= new ArrayList<String>();
		table.add("T_OUTPATIENT_FEEDETAIL_NOW");
		//table.add("T_OUTPATIENT_FEEDETAIL");
		//检查费
		StringBuffer jcfcode = new StringBuffer();
		//治疗费
		StringBuffer zlfcode = new StringBuffer();
		//放射费
		StringBuffer fsfcode = new StringBuffer();
		//化验费
		StringBuffer hyfcode = new StringBuffer();
		//输血费
		StringBuffer sxfcode = new StringBuffer();
		//西药费
		StringBuffer xyfcode = new StringBuffer();
		//中成药费
		StringBuffer zcyfcode = new StringBuffer();
		//中草药费
		StringBuffer zcysfcode = new StringBuffer();
		for (ReportVo vo : gcode) {
			if(("07").equals(vo.getFeestatcode())){
				jcfcode.append(vo.getMinfeecode()+",");
			}
			if(("05").equals(vo.getFeestatcode())){
				zlfcode.append(vo.getMinfeecode()+",");
			}
			if(("08").equals(vo.getFeestatcode())){
				fsfcode.append(vo.getMinfeecode()+",");
			}
			if(("09").equals(vo.getFeestatcode())){
				hyfcode.append(vo.getMinfeecode()+",");
			}
			if(("01").equals(vo.getFeestatcode())){
				xyfcode.append(vo.getMinfeecode()+",");
			}
			if(("02").equals(vo.getFeestatcode())){
				zcyfcode.append(vo.getMinfeecode()+",");
			}
			if(("03").equals(vo.getFeestatcode())){
				zcysfcode.append(vo.getMinfeecode()+",");
			}
			if(("11").equals(vo.getFeestatcode())){
				sxfcode.append(vo.getMinfeecode()+",");
			}
			
		}
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT min(doct) doct,min(dept) dept,regDate, deptCode, docterCode,SUM(jcfkds) jcfkds,SUM(jcfje) jcfje,SUM(zlfkds) zlfkds,");
		sb.append("SUM(zlfje) zlfje,SUM(fsfkds) fsfkds,SUM(fsfje) fsfje,SUM(hyfkds) hyfkds,SUM(hyfje) hyfje,SUM(sxfkds) sxfkds,SUM(sxfje) sxfje,");
		sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0)-NVL(SUM(sxfkds),0)-NVL(SUM(hyfkds),0)-NVL(SUM(fsfkds),0)-NVL(SUM(zlfkds),0)-NVL(SUM(jcfkds),0) qtsrkds,");
		sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0)-NVL(SUM(sxfje),0)-NVL(SUM(hyfje),0)-NVL(SUM(fsfje),0)-NVL(SUM(zlfje),0)-NVL(SUM(jcfje),0) ylsfje,");
		sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0) ylsrkds,");
		sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0) MedicalCost,SUM(xyfkds) xyfkds,");
		sb.append("SUM(xyfje) xyfje,SUM(zcyfkds) zcyfkds,SUM(zcyfje) zcyfje,SUM(zcykds) zcykds,SUM(zcyje) zcyje,");
		sb.append("NVL(SUM(xyfkds),0)+NVL(SUM(zcyfkds),0)+NVL(SUM(zcykds),0) ypsrkds,");
		sb.append("NVL(SUM(xyfje),0)+NVL(SUM(zcyfje),0)+NVL(SUM(zcyje),0) ypsrkje,SUM(zkds) zkds,SUM(zje) zje");
		sb.append(" from (");
	    for (int i = 0; i < table.size(); i++) {
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			//检查费开单数
			sb.append("SELECT min(e.EMPLOYEE_NAME) doct,min(p.DEPT_NAME) dept,TO_CHAR(f.REG_DATE,'yyyy-mm-dd') regDate,f.REG_DPCD deptCode,f.DOCT_CODE docterCode,DECODE(f.FEE_CODE, ");
			String[] splitkds = jcfcode.toString().split(",");
			for (String string : splitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0 ) jcfkds,");
			//检查费金额
			sb.append("DECODE(f.FEE_CODE, ");
			String[] splitje = jcfcode.toString().split(",");
			for (String string : splitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) jcfje,");
			//治疗费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zlfsplitkds = zlfcode.toString().split(",");
			for (String string : zlfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) zlfkds,");
			//治疗费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zlfsplitje = zlfcode.toString().split(",");
			for (String string : zlfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zlfje,");
			//放射费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] fsfsplitkds = fsfcode.toString().split(",");
			for (String string : fsfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) fsfkds,");
			//放射费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] fsfsplitjs = fsfcode.toString().split(",");
			for (String string : fsfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) fsfje,");
			//化验费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] hyfsplitkds = hyfcode.toString().split(",");
			for (String string : hyfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) hyfkds,");
			//化验费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] hyfsplitjs = hyfcode.toString().split(",");
			for (String string : hyfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) hyfje,");
			//输血费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] sxfsplitkds = sxfcode.toString().split(",");
			for (String string : sxfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) sxfkds,");
			//输血费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] sxfsplitjs = sxfcode.toString().split(",");
			for (String string : sxfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) sxfje,");
			//西药费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] xyfsplitkds = xyfcode.toString().split(",");
			for (String string : xyfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) xyfkds,");
			//西药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] xyfsplitje = xyfcode.toString().split(",");
			for (String string : xyfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) xyfje,");
			//中成药开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcyfsplitkds = zcyfcode.toString().split(",");
			for (String string : zcyfsplitkds) {
				sb.append("'"+string).append("',1,");
			}
			sb.append("0) zcyfkds,");
			//中成药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcyfsplitje = zcyfcode.toString().split(",");
			for (String string : zcyfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zcyfje,");
			//中草药费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcysfsplitkds = zcysfcode.toString().split(",");
			for (String string : zcysfsplitkds) {
				sb.append("'"+string).append("',1,");
			}
			sb.append("0) zcykds,");
			//中草药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcysfsplitje = zcysfcode.toString().split(",");
			for (String string : zcysfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zcyje,");
			
			sb.append("COUNT(f.FEE_CODE) zkds,f.TOT_COST zje FROM  ");
			sb.append(table.get(i)).append(" f ");
			sb.append(" ,T_DEPARTMENT p,T_EMPLOYEE e ");//AND f.REG_DATE >= (SYSDATE-365)----->批量导入时使用AND f.REG_DATE >= (SYSDATE-"+365*j+") ,f.REG_DATE < (SYSDATE -"+365*(j-1)+")
			sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.REG_DATE >= (SYSDATE)  and p.DEPT_CODE=f.REG_DPCD and e.EMPLOYEE_JOBNO=f.DOCT_CODE ");
			
			sb.append(" GROUP BY f.REG_DPCD,f.DOCT_CODE,f.FEE_CODE,f.TOT_COST,f.REG_DATE ");
		}
		sb.append(" ) GROUP BY deptCode,docterCode,REGDATE ");
		//进行查询
		List<StatisticsVo> list =namedParameterJdbcTemplate.query(sb.toString(),new RowMapper<StatisticsVo>() {
			@Override
			public StatisticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StatisticsVo vo = new StatisticsVo();
				vo.setRegDate(rs.getString("regDate"));
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setDocterCode(rs.getString("docterCode"));
				
				vo.setName(rs.getString("doct"));
				vo.setDept(rs.getString("dept"));
				vo.setInspectNum(rs.getInt("jcfkds"));
				vo.setInspectCost(rs.getDouble("jcfje"));
				vo.setTreatmentNum(rs.getInt("zlfkds"));
				vo.setTreatmentCost(rs.getDouble("zlfje"));
				vo.setRadiationNum(rs.getInt("fsfkds"));
				vo.setRadiationCost(rs.getDouble("fsfje"));
				vo.setBloodNum(rs.getInt("sxfkds"));
				vo.setBloodCost(rs.getDouble("sxfje"));
				vo.setTestNum(rs.getInt("hyfkds"));
				vo.setTestCost(rs.getDouble("hyfje"));
				vo.setOtherNum(rs.getInt("qtsrkds"));
				vo.setOtherCost(rs.getDouble("ylsfje"));
				vo.setMedicalNum(rs.getInt("ylsrkds"));
				vo.setMedicalCost(rs.getDouble("MedicalCost"));
				vo.setWesternNum(rs.getInt("xyfkds"));
				vo.setWesternCost(rs.getDouble("xyfje"));
				vo.setChineseNum(rs.getInt("zcyfkds"));
				vo.setChineseCost(rs.getDouble("zcyfje"));
				vo.setHerbalNum(rs.getInt("zcykds"));
				vo.setHerbalCost(rs.getDouble("zcyje"));
				vo.setAllNum(rs.getInt("ypsrkds"));
				vo.setAllCost(rs.getDouble("ypsrkje"));
				vo.setTotle(rs.getDouble("zje"));
				return vo;
			}
		});
		
	  	//将需要的数据插入mongodb中
	  	List<DBObject> userList = new ArrayList<DBObject>();
	  	if(list!=null&&list.size()>0){
	  			 for(StatisticsVo r : list){
	  				 	//String confirmDate= dateFormat.format(r.getConfirmDate());
	  				 	Document document = new Document();
	  				
	  					//更新条件
	  					Document document1 = new Document();
	  					document1.append("regDate", r.getRegDate());
	  					document1.append("deptCode", r.getDeptCode());
	  					document1.append("docterCode", r.getDocterCode());
	  					
	  					document.append("regDate", r.getRegDate());
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
	  					mbDao.update(TABLENAME_MZGXSRTJ, document1, document, true);
	  			 }
	  			 
	  	}
		//}
	  	/************************************************结束更新数据******************************************/
	}
	
	
	/**
	 * 向mongodb中导入历史数据和在线库数据：治疗效果数据分析
	 * @param startTime 开始时间,格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间,格式：yyyy-MM-dd HH:mm:ss
	  * @param l  分区表集合
	 * @throws ParseException 
	 *  @Author zhangkui
	 * @time 2017年5月22日
	 */
	public void imTableData_T_INPATIENT_INFO(String startTime, String endTime,List l)throws ParseException {
		
		mbDao = new MongoBasicDao();
		//mbDao.deleteData(TABLENAME_ZLXGSJFX);//第一次要先把数据清空再导
		// 计算开始时间，结束时间区间天数
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// SimpleDateFormat formatEndDate = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 循环导入数据
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT OUT_DATE as OUT_DATE,NVL(OUTSTATE, 1) AS OUTSTATE,DEPTNAME,COUNT (OUTSTATETOTAL) AS OUTSTATETOTAL ");
		sb.append(" FROM ( ");
		if(l!=null&&l.size()>0){
			for(int i=0;i<l.size();i++){
				if(i!=0){
					sb.append(" UNION ALL ");
				}
				sb.append(" SELECT TO_CHAR(T.OUT_DATE,'YYYY-MM-DD') AS OUT_DATE,NVL(T.DIAG_OUTSTATE,1) AS OUTSTATE,T.DEPT_CODE AS DEPTNAME,COUNT(T.INPATIENT_ID) AS OUTSTATETOTAL FROM "+l.get(i)+" T WHERE T .OUT_DATE < TO_DATE(:endTime,'yyyy-mm-dd hh24:mi:ss') AND T .OUT_DATE >= TO_DATE(:startTime,'yyyy-mm-dd hh24:mi:ss') AND T.DEL_FLG = 0 AND T.STOP_FLG = 0 AND (T.IN_STATE = 'O' OR T.IN_STATE = 'N') GROUP BY T.DIAG_OUTSTATE,T.DEPT_CODE,T.OUT_DATE ");
			}
		}
		sb.append(" ) ");
		sb.append(" GROUP BY OUT_DATE,OUTSTATE,DEPTNAME ");
		if (startTime != null && endTime != null) {
			int count=0;//记录更新次数
			Date et = simpleDateFormat.parse(endTime);
			Date s_t = simpleDateFormat.parse(startTime);
			Long es=et.getTime();
			Long ss= s_t.getTime();
			if (es > ss) {
				if ((es - ss) < (86400000)) {// 查询的区间小于一天，精确更新24 * 60 * 60 * 1000=86400000
					//可以把结束时间加1秒，因为sql是<
					Long e_s=es+1000;
					endTime =simpleDateFormat.format(new Date(e_s));
					SQLQuery queryObject = getSession().createSQLQuery(
							sb.toString());
					
					
					queryObject.addScalar("out_Date", Hibernate.STRING)
					.addScalar("outStateTotal", Hibernate.INTEGER)
					.addScalar("deptName", Hibernate.STRING)
					.addScalar("outState", Hibernate.INTEGER);
					
					queryObject.setParameter("endTime", endTime);
					queryObject.setParameter("startTime", startTime);
				
					System.out.println("本次更新区间小于一天，执行的sql是:"+queryObject.getQueryString());
					System.out.println("时间区间为："+startTime+"-"+endTime);
					// 执行sql，查询结果
					List<TreatmentEffectVo> list = queryObject
							.setResultTransformer(
									Transformers
											.aliasToBean(TreatmentEffectVo.class))
							.list();
					// 遍历list，将数据插入mongodb
					if (list != null && list.size() > 0) {
						for (TreatmentEffectVo t : list) {
							// String yearSelect =
							// dateFormat.format(t.getOut_Date());
							String yearSelect = t.getOut_Date();
							// 更新条件
							Document document1 = new Document();
							document1.append("yearSelect", yearSelect);
							document1.append("deptName", t.getDeptName());
							document1.append("outState", t.getOutState());
							//读出开始时间以前是否存在同样的数据(因为是按天存的，当前按小时存，如果之前的小时有相同的记录，会覆盖掉，而不是某些变量求和)
							//先读出当前天数据
							BasicDBObject bdObject = new BasicDBObject();
							if(t.getDeptName()!=null){
								//科室名
								bdObject.append("deptName",t.getDeptName());
							}
							
							if(StringUtils.isNotBlank(startTime)){
								//yyyy-mm-dd
								String beginDate=startTime.substring(0, 10);
								System.out.println("正在检查"+beginDate+"日,是否已存在相同的数据！");
								bdObject.append("yearSelect",beginDate);
							}
								bdObject.put("outState", t.getOutState());
							
							DBCursor cursor=mbDao.findAlldata(TABLENAME_ZLXGSJFX, bdObject);
							Integer  outStateTotal=0;
							while(cursor.hasNext()){
								System.out.println("发现当天已有相同的数据。。。。！");
								DBObject dbCursor = cursor.next();
								//相同条件的叠加
								System.out.println("上次结果是:"+dbCursor.get("outStateTotal"));
								outStateTotal+=Integer.parseInt(dbCursor.get("outStateTotal").toString());
							}
							Document document = new Document();
							document.append("yearSelect", yearSelect);
							document.append("deptName", t.getDeptName());
							document.append("outState", t.getOutState());
							document.append("outStateTotal",
									t.getOutStateTotal()+outStateTotal);
							System.out.println("本次结果是："+(t.getOutStateTotal()+outStateTotal));
							try {
								count++;
								mbDao.update(TABLENAME_ZLXGSJFX, document1,
										document, true);
								System.out.println("更新成功,时间区间为："+startTime+"-"+endTime+"更新第"+count+"条数据！");
							} catch (Exception e) {
								System.out.println("更新失败,时间区间为："+startTime+"-"+endTime+"更新第"+count+"条数据失败！");
								e.printStackTrace();
							}
							
						}
						

					}

				} else {// 查询的区间大于一天，按天
					Date beginDate = dateFormat.parse(startTime);
					long beginSS = beginDate.getTime();
					Date endDate = dateFormat.parse(endTime);
					long endSS = endDate.getTime();
					int days = 0;
					days = (int) ((endSS - beginSS) / (86400000))+1;// 区间天数//24 * 60 * 60 * 1000=86400000
					for (int i = 1; i <= days; i++) {
						// 每次执行时间格式如>=2017-05-22 00:00:00 <2015-05-23 00:00:00
						// 设置参数
						// 结束日期是开始日期的下一天
						Date st = dateFormat.parse(startTime);
						Date ed = DateUtils.addDay(st, 1);
//						for(int j=0;j<=23;j++){
//							
//							
//						}
						
						String s = dateFormat.format(st)+ " 00:00:00";
						String e = dateFormat.format(ed)+ " 00:00:00";
						SQLQuery queryObject = getSession().createSQLQuery(
								sb.toString());

						queryObject.addScalar("out_Date", Hibernate.STRING)
						.addScalar("outStateTotal", Hibernate.INTEGER)
						.addScalar("deptName", Hibernate.STRING)
						.addScalar("outState", Hibernate.INTEGER);
						
						queryObject.setParameter("endTime", e);
						queryObject.setParameter("startTime", s);

						System.out.println("本次更新区间大于一天,按天执行,第"+i+"天,执行的sql是:"+queryObject.getQueryString());
						System.out.println("时间区间为："+s+"-"+e);
						// 执行sql，查询结果
						List<TreatmentEffectVo> list = queryObject
								.setResultTransformer(
										Transformers
												.aliasToBean(TreatmentEffectVo.class))
								.list();
						// 遍历list，将数据插入mongodb
						if (list != null && list.size() > 0) {
							for (TreatmentEffectVo t : list) {
								// String yearSelect =
								// dateFormat.format(t.getOut_Date());
								String yearSelect = t.getOut_Date();
								// 更新条件
								Document document1 = new Document();
								document1.append("yearSelect", yearSelect);
								document1.append("deptName", t.getDeptName());
								document1.append("outState", t.getOutState());

								Document document = new Document();
								document.append("yearSelect", yearSelect);
								document.append("deptName", t.getDeptName());
								document.append("outState", t.getOutState());
								document.append("outStateTotal",
										t.getOutStateTotal());
								try {
									count++;
									mbDao.update(TABLENAME_ZLXGSJFX, document1,
											document, true);
									System.out.println("更新成功,时间区间为："+s+"-"+e+"更新第"+count+"条数据！");
								} catch (Exception ex) {
									System.out.println("更新失败,时间区间为："+s+"-"+e+"更新第"+count+"条数据失败！");
									ex.printStackTrace();
								}

							}

						}
						startTime= e;
					}

				}
			}
		}
		/************************************************ 结束导入历史住院表数据 ******************************************/
	}

	
	/**
	 *@Description:门诊各项收入统计：把数据导入mongodb中
	 *处方明细表:T_OUTPATIENT_FEEDETAIL  T_OUTPATIENT_FEEDETAIL_NOW 
	 *@param startTime：开始时间
	 *@param endTime：结束时间
	 *@param table:T_OUTPATIENT_FEEDETAIL,处方明细表
	 *@param gcode:费用类别
	 * @author: zhangkui
	 * @throws ParseException 
	 * @time:2017年5月22日 
	 */
	public void imTableData_T_OUTPATIENT_FEEDETAIL(String startTime ,String endTime, List table, List<ReportVo> gcode) throws ParseException{
		mbDao = new MongoBasicDao();
		//mbDao.deleteData(TABLENAME_MZGXSRTJ);
		/************************************************开始导入数据******************************************/
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//检查费
		StringBuffer jcfcode = new StringBuffer();
		//治疗费
		StringBuffer zlfcode = new StringBuffer();
		//放射费
		StringBuffer fsfcode = new StringBuffer();
		//化验费
		StringBuffer hyfcode = new StringBuffer();
		//输血费
		StringBuffer sxfcode = new StringBuffer();
		//西药费
		StringBuffer xyfcode = new StringBuffer();
		//中成药费
		StringBuffer zcyfcode = new StringBuffer();
		//中草药费
		StringBuffer zcysfcode = new StringBuffer();
		for (ReportVo vo : gcode) {
			if(("07").equals(vo.getFeestatcode())){
				jcfcode.append(vo.getMinfeecode()+",");
			}
			if(("05").equals(vo.getFeestatcode())){
				zlfcode.append(vo.getMinfeecode()+",");
			}
			if(("08").equals(vo.getFeestatcode())){
				fsfcode.append(vo.getMinfeecode()+",");
			}
			if(("09").equals(vo.getFeestatcode())){
				hyfcode.append(vo.getMinfeecode()+",");
			}
			if(("01").equals(vo.getFeestatcode())){
				xyfcode.append(vo.getMinfeecode()+",");
			}
			if(("02").equals(vo.getFeestatcode())){
				zcyfcode.append(vo.getMinfeecode()+",");
			}
			if(("03").equals(vo.getFeestatcode())){
				zcysfcode.append(vo.getMinfeecode()+",");
			}
			if(("11").equals(vo.getFeestatcode())){
				sxfcode.append(vo.getMinfeecode()+",");
			}
			
		}
		StringBuffer sb = new StringBuffer();
		sb.append("	SELECT min(doct) doct,min(dept) dept,regDate, deptCode, docterCode,SUM(jcfkds) jcfkds,SUM(jcfje) jcfje,SUM(zlfkds) zlfkds,");
		sb.append("SUM(zlfje) zlfje,SUM(fsfkds) fsfkds,SUM(fsfje) fsfje,SUM(hyfkds) hyfkds,SUM(hyfje) hyfje,SUM(sxfkds) sxfkds,SUM(sxfje) sxfje,");
		sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0)-NVL(SUM(sxfkds),0)-NVL(SUM(hyfkds),0)-NVL(SUM(fsfkds),0)-NVL(SUM(zlfkds),0)-NVL(SUM(jcfkds),0) qtsrkds,");
		sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0)-NVL(SUM(sxfje),0)-NVL(SUM(hyfje),0)-NVL(SUM(fsfje),0)-NVL(SUM(zlfje),0)-NVL(SUM(jcfje),0) ylsfje,");
		sb.append("NVL(SUM(zkds),0)-NVL(SUM(xyfkds),0)-NVL(SUM(zcyfkds),0)-NVL(SUM(zcykds),0) ylsrkds,");
		sb.append("NVL(SUM(zje),0)-NVL(SUM(xyfje),0)-NVL(SUM(zcyfje),0)-NVL(SUM(zcyje),0) MedicalCost,SUM(xyfkds) xyfkds,");
		sb.append("SUM(xyfje) xyfje,SUM(zcyfkds) zcyfkds,SUM(zcyfje) zcyfje,SUM(zcykds) zcykds,SUM(zcyje) zcyje,");
		sb.append("NVL(SUM(xyfkds),0)+NVL(SUM(zcyfkds),0)+NVL(SUM(zcykds),0) ypsrkds,");
		sb.append("NVL(SUM(xyfje),0)+NVL(SUM(zcyfje),0)+NVL(SUM(zcyje),0) ypsrkje,SUM(zkds) zkds,SUM(zje) zje");
		sb.append(" from (");
	    for (int i = 0; i < table.size(); i++) {
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			//检查费开单数
			sb.append("SELECT min(e.EMPLOYEE_NAME) doct,min(p.DEPT_NAME) dept,TO_CHAR(f.REG_DATE,'yyyy-mm-dd') regDate,f.REG_DPCD deptCode,f.DOCT_CODE docterCode,DECODE(f.FEE_CODE, ");
			String[] splitkds = jcfcode.toString().split(",");
			for (String string : splitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0 ) jcfkds,");
			//检查费金额
			sb.append("DECODE(f.FEE_CODE, ");
			String[] splitje = jcfcode.toString().split(",");
			for (String string : splitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) jcfje,");
			//治疗费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zlfsplitkds = zlfcode.toString().split(",");
			for (String string : zlfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) zlfkds,");
			//治疗费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zlfsplitje = zlfcode.toString().split(",");
			for (String string : zlfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zlfje,");
			//放射费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] fsfsplitkds = fsfcode.toString().split(",");
			for (String string : fsfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) fsfkds,");
			//放射费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] fsfsplitjs = fsfcode.toString().split(",");
			for (String string : fsfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) fsfje,");
			//化验费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] hyfsplitkds = hyfcode.toString().split(",");
			for (String string : hyfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) hyfkds,");
			//化验费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] hyfsplitjs = hyfcode.toString().split(",");
			for (String string : hyfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) hyfje,");
			//输血费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] sxfsplitkds = sxfcode.toString().split(",");
			for (String string : sxfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) sxfkds,");
			//输血费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] sxfsplitjs = sxfcode.toString().split(",");
			for (String string : sxfsplitjs) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) sxfje,");
			//西药费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] xyfsplitkds = xyfcode.toString().split(",");
			for (String string : xyfsplitkds) {
				sb.append("'"+string).append("',1 ,");
			}
			sb.append("0) xyfkds,");
			//西药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] xyfsplitje = xyfcode.toString().split(",");
			for (String string : xyfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) xyfje,");
			//中成药开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcyfsplitkds = zcyfcode.toString().split(",");
			for (String string : zcyfsplitkds) {
				sb.append("'"+string).append("',1,");
			}
			sb.append("0) zcyfkds,");
			//中成药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcyfsplitje = zcyfcode.toString().split(",");
			for (String string : zcyfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zcyfje,");
			//中草药费开单数
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcysfsplitkds = zcysfcode.toString().split(",");
			for (String string : zcysfsplitkds) {
				sb.append("'"+string).append("',1,");
			}
			sb.append("0) zcykds,");
			//中草药费金额
			sb.append("DECODE(f.FEE_CODE,");
			String[] zcysfsplitje = zcysfcode.toString().split(",");
			for (String string : zcysfsplitje) {
				sb.append("'"+string+"'").append(",f.TOT_COST,");
			}
			sb.append("0) zcyje,");
			
			sb.append("COUNT(f.FEE_CODE) zkds,f.TOT_COST zje FROM  ");
			sb.append(table.get(i)).append(" f ");
			sb.append(" ,T_DEPARTMENT p,T_EMPLOYEE e ");//AND f.REG_DATE >= (SYSDATE-365)----->批量导入时使用AND f.REG_DATE >= (SYSDATE-"+365*j+") ,f.REG_DATE < (SYSDATE -"+365*(j-1)+")
			sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.REG_DATE < TO_DATE(:endTime,'yyyy-mm-dd hh24:mi:ss') AND f.REG_DATE >= TO_DATE(:startTime,'yyyy-mm-dd hh24:mi:ss') and p.DEPT_CODE=f.REG_DPCD and e.EMPLOYEE_JOBNO=f.DOCT_CODE ");
//			if(j==1){
//				sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.REG_DATE >= (SYSDATE-"+365*j+") AND f.REG_DATE < (SYSDATE +1) and p.DEPT_CODE=f.REG_DPCD and e.EMPLOYEE_JOBNO=f.DOCT_CODE ");
//			}else{
//				sb.append(" WHERE f.STOP_FLG = 0 AND f.DEL_FLG = 0 AND f.REG_DATE >= (SYSDATE-"+365*j+") AND f.REG_DATE < (SYSDATE -"+365*(j-1)+") and p.DEPT_CODE=f.REG_DPCD and e.EMPLOYEE_JOBNO=f.DOCT_CODE ");
//			}
			sb.append(" GROUP BY f.REG_DPCD,f.DOCT_CODE,f.FEE_CODE,f.TOT_COST,f.REG_DATE ");
		}
		sb.append(" ) GROUP BY deptCode,docterCode,REGDATE ");
		
		if(startTime!=null&&endTime!=null){
			int count=0;//记录更新次数
			Date et = simpleDateFormat.parse(endTime);
			Date s_t = simpleDateFormat.parse(startTime);
			Long es=et.getTime();
			Long ss= s_t.getTime();
			if (es > ss) {
				if ((es - ss) < (86400000)) {// 查询的区间小于一天，精确更新24 * 60 * 60 * 1000=86400000
					//可以把结束时间加1秒，因为sql是<
					Long e_s=es+1000;
					endTime =simpleDateFormat.format(new Date(e_s));
					System.out.println("本次更新区间小于一天，执行的sql是:"+sb.toString());
					System.out.println("时间区间为："+startTime+"-"+endTime);
					Map map= new HashMap();
					map.put("startTime", startTime);
					map.put("endTime", endTime);
					//进行查询
					List<StatisticsVo> list =namedParameterJdbcTemplate.query(sb.toString(),map,new RowMapper<StatisticsVo>() {
						@Override
						public StatisticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
							StatisticsVo vo = new StatisticsVo();
							vo.setRegDate(rs.getString("regDate"));
							vo.setDeptCode(rs.getString("deptCode"));
							vo.setDocterCode(rs.getString("docterCode"));
							
							vo.setName(rs.getString("doct"));
							vo.setDept(rs.getString("dept"));
							vo.setInspectNum(rs.getInt("jcfkds"));
							vo.setInspectCost(rs.getDouble("jcfje"));
							vo.setTreatmentNum(rs.getInt("zlfkds"));
							vo.setTreatmentCost(rs.getDouble("zlfje"));
							vo.setRadiationNum(rs.getInt("fsfkds"));
							vo.setRadiationCost(rs.getDouble("fsfje"));
							vo.setBloodNum(rs.getInt("sxfkds"));
							vo.setBloodCost(rs.getDouble("sxfje"));
							vo.setTestNum(rs.getInt("hyfkds"));
							vo.setTestCost(rs.getDouble("hyfje"));
							vo.setOtherNum(rs.getInt("qtsrkds"));
							vo.setOtherCost(rs.getDouble("ylsfje"));
							vo.setMedicalNum(rs.getInt("ylsrkds"));
							vo.setMedicalCost(rs.getDouble("MedicalCost"));
							vo.setWesternNum(rs.getInt("xyfkds"));
							vo.setWesternCost(rs.getDouble("xyfje"));
							vo.setChineseNum(rs.getInt("zcyfkds"));
							vo.setChineseCost(rs.getDouble("zcyfje"));
							vo.setHerbalNum(rs.getInt("zcykds"));
							vo.setHerbalCost(rs.getDouble("zcyje"));
							vo.setAllNum(rs.getInt("ypsrkds"));
							vo.setAllCost(rs.getDouble("ypsrkje"));
							vo.setTotle(rs.getDouble("zje"));
							return vo;
						}
					});
					// 遍历list，将数据插入mongodb
					if (list != null && list.size() > 0) {
						for (StatisticsVo r : list) {
							Document document1 = new Document();
							document1.append("regDate", r.getRegDate());
							document1.append("deptCode", r.getDeptCode());
							document1.append("docterCode", r.getDocterCode());
							BasicDBObject bdObject = new BasicDBObject();
							if(r.getRegDate()!=null){
								
								System.out.println("正在检查"+r.getRegDate()+"日,是否已存在相同的数据！");
								bdObject.append("regDate",r.getRegDate());
							}
							
							if(StringUtils.isNotBlank(r.getDeptCode())){
								bdObject.append("deptCode",r.getDeptCode());
							}
							if(StringUtils.isNotBlank(r.getDocterCode())){
								bdObject.append("docterCode",r.getDocterCode());
							}
							
							DBCursor cursor=mbDao.findAlldata(TABLENAME_MZGXSRTJ, bdObject);
							
							Integer inspectnum=0;
							Double  inspectcost=0.0;
							Integer treatmentnum=0;
							Double  treatmentcost=0.0;
							Integer radiationnum=0;
							Double  radiationcost=0.0;
							Integer bloodnum=0;
							Double  bloodcost=0.0;
							Integer	testnum=0;
							Double  testcost=0.0;
							Integer othernum=0;
							Double  othercost=0.0;
							Integer medicalnum=0;
							Double  medicalcost=0.0;
							Integer westernnum=0;
							Double  westerncost=0.0;
							Integer chinesenum=0;
							Double  chinesecost=0.0;
							Integer herbalnum=0;
							Double  herbalcost=0.0;
							Integer allnum=0;
							Double  allcost=0.0;
							Double  totle=0.0;
							while(cursor.hasNext()){
								System.out.println("发现当天已有相同的数据。。。。！");
								DBObject dbCursor = cursor.next();
								//相同条件的叠加
								inspectnum+=Integer.parseInt((String) dbCursor.get("inspectnum"));
								inspectcost+=Double.parseDouble((String) dbCursor.get("inspectcost"));
								treatmentnum+=Integer.parseInt((String) dbCursor.get("treatmentnum"));
								treatmentcost+=Double.parseDouble((String) dbCursor.get("treatmentcost"));
								radiationnum+=Integer.parseInt((String) dbCursor.get("radiationnum"));
								radiationcost+=Double.parseDouble((String) dbCursor.get("radiationcost"));
								bloodnum+=Integer.parseInt((String) dbCursor.get("bloodnum"));
								bloodcost+=Double.parseDouble((String) dbCursor.get("bloodcost"));
								testnum+=Integer.parseInt((String) dbCursor.get("testnum"));
								testcost+=Double.parseDouble((String) dbCursor.get("testcost"));
								othernum+=Integer.parseInt((String) dbCursor.get("othernum"));
								othercost+=Double.parseDouble((String) dbCursor.get("othercost"));
								medicalnum+=Integer.parseInt((String) dbCursor.get("medicalnum"));
								medicalcost+=Double.parseDouble((String) dbCursor.get("medicalcost"));
								westernnum+=Integer.parseInt((String) dbCursor.get("westernnum"));
								westerncost+=Double.parseDouble((String) dbCursor.get("westerncost"));
								chinesenum+=Integer.parseInt((String) dbCursor.get("chinesenum"));
								chinesecost+=Double.parseDouble((String) dbCursor.get("chinesecost"));
								herbalnum+=Integer.parseInt((String) dbCursor.get("herbalnum"));
								herbalcost+=Double.parseDouble((String) dbCursor.get("herbalcost"));
								allnum+=Integer.parseInt((String) dbCursor.get("allnum"));
								allcost+=Double.parseDouble((String) dbCursor.get("allcost"));
								totle+=Double.parseDouble((String) dbCursor.get("totle"));
								
							}
							 inspectnum+=r.getInspectNum();
							 inspectcost+=r.getInspectCost();
							 treatmentnum+=r.getTreatmentNum();
							 treatmentcost+=r.getTreatmentCost();
							 radiationnum+=r.getRadiationNum();
							 radiationcost+=r.getRadiationCost();
							 bloodnum+=r.getBloodNum();
							 bloodcost+=r.getBloodCost();
							 testnum+=r.getTestNum();
							 testcost+=r.getTestCost();
							 othernum+=r.getOtherNum();
							 othercost+=r.getOtherCost();
							 
							 medicalnum+=r.getMedicalNum();
							 medicalcost+=r.getMedicalCost();
							 westernnum+=r.getWesternNum();
							 westerncost+=r.getWesternCost();
							 chinesenum+=r.getChineseNum();
							 chinesecost+=r.getChineseCost();
							 herbalnum+=r.getHerbalNum();
							 herbalcost+=r.getHerbalCost();
							 allnum+=r.getAllNum();
							 allcost+=r.getAllCost();
							 totle+=r.getTotle();
							
							Document document = new Document();
		  					document.append("regDate", r.getRegDate());
		  					document.append("deptCode", r.getDeptCode());
		  					document.append("docterCode", r.getDocterCode());
		  					
		  					document.append("name", r.getName());
		  					document.append("dept", r.getDept());
		  					
		  					document.append("inspectnum", inspectnum);
		  					document.append("inspectcost", inspectcost);
		  					document.append("treatmentnum", treatmentnum);
		  					document.append("treatmentcost", treatmentcost);
		  					document.append("radiationnum", radiationnum);
		  					document.append("radiationcost", radiationcost);
		  					document.append("bloodnum", bloodnum);
		  					document.append("bloodcost", bloodcost);
		  					document.append("testnum", testnum);
		  					document.append("testcost", testcost);
		  					document.append("othernum", othernum);
		  					document.append("othercost", othercost);
		  					document.append("medicalnum", medicalnum);
		  					document.append("medicalcost", medicalcost);
		  					document.append("westernnum", westernnum);
		  					document.append("westerncost", westerncost);
		  					document.append("chinesenum", chinesenum);
		  					document.append("chinesecost", chinesecost);
		  					document.append("herbalnum", herbalnum);
		  					document.append("herbalcost", herbalcost);
		  					document.append("allnum", allnum);
		  					document.append("allcost", allcost);
		  					document.append("totle", totle);
		  					try {
								count++;
								mbDao.update(TABLENAME_MZGXSRTJ, document1,document, true);
								System.out.println("更新成功,时间区间为："+startTime+"-"+endTime+"更新第"+count+"条数据！");
							} catch (Exception e) {
								System.out.println("更新失败,时间区间为："+startTime+"-"+endTime+"更新第"+count+"条数据失败！");
								e.printStackTrace();
							}
						}
					}
					//更新区间大于一天
				}else{
					Date beginDate = dateFormat.parse(startTime);
					long beginSS = beginDate.getTime();
					Date endDate = dateFormat.parse(endTime);
					long endSS = endDate.getTime();
					int days = 0;
					days = (int) ((endSS - beginSS) / (86400000))+1;// 区间天数24 * 60 * 60 * 1000=86400000
					
					for (int i = 1; i <= days; i++) {
						// 结束日期是开始日期的下一天
						Date st = dateFormat.parse(startTime);
						Date ed = DateUtils.addDay(st, 1);
						String s = dateFormat.format(st)+ " 00:00:00";
						String e = dateFormat.format(ed)+ " 00:00:00";
						Map map= new HashMap();
						map.put("startTime", s);
						map.put("endTime", e);
						
						//进行查询
						List<StatisticsVo> list =namedParameterJdbcTemplate.query(sb.toString(),map,new RowMapper<StatisticsVo>() {
							@Override
							public StatisticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
								StatisticsVo vo = new StatisticsVo();
								vo.setRegDate(rs.getString("regDate"));
								vo.setDeptCode(rs.getString("deptCode"));
								vo.setDocterCode(rs.getString("docterCode"));
								
								vo.setName(rs.getString("doct"));
								vo.setDept(rs.getString("dept"));
								vo.setInspectNum(rs.getInt("jcfkds"));
								vo.setInspectCost(rs.getDouble("jcfje"));
								vo.setTreatmentNum(rs.getInt("zlfkds"));
								vo.setTreatmentCost(rs.getDouble("zlfje"));
								vo.setRadiationNum(rs.getInt("fsfkds"));
								vo.setRadiationCost(rs.getDouble("fsfje"));
								vo.setBloodNum(rs.getInt("sxfkds"));
								vo.setBloodCost(rs.getDouble("sxfje"));
								vo.setTestNum(rs.getInt("hyfkds"));
								vo.setTestCost(rs.getDouble("hyfje"));
								vo.setOtherNum(rs.getInt("qtsrkds"));
								vo.setOtherCost(rs.getDouble("ylsfje"));
								vo.setMedicalNum(rs.getInt("ylsrkds"));
								vo.setMedicalCost(rs.getDouble("MedicalCost"));
								vo.setWesternNum(rs.getInt("xyfkds"));
								vo.setWesternCost(rs.getDouble("xyfje"));
								vo.setChineseNum(rs.getInt("zcyfkds"));
								vo.setChineseCost(rs.getDouble("zcyfje"));
								vo.setHerbalNum(rs.getInt("zcykds"));
								vo.setHerbalCost(rs.getDouble("zcyje"));
								vo.setAllNum(rs.getInt("ypsrkds"));
								vo.setAllCost(rs.getDouble("ypsrkje"));
								vo.setTotle(rs.getDouble("zje"));
								return vo;
							}
						});
						// 遍历list，将数据插入mongodb
						if (list != null && list.size() > 0) {
							for (StatisticsVo r : list) {
								Document document1 = new Document();
								document1.append("regDate", r.getRegDate());
								document1.append("deptCode", r.getDeptCode());
								document1.append("docterCode", r.getDocterCode());
							
								Document document = new Document();
								document.append("regDate", r.getRegDate());
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
			  					try {
									count++;
									mbDao.update(TABLENAME_MZGXSRTJ, document1,document, true);
									System.out.println("更新成功,时间区间为："+s+"-"+e+"更新第"+count+"条数据！");
								} catch (Exception ex) {
									System.out.println("更新失败,时间区间为："+s+"-"+e+"更新第"+count+"条数据失败！");
									ex.printStackTrace();
								}

							}
						}
						startTime= e;
					}
				}
				/************************************************ 结束导入数据 ******************************************/
			}
		}
	}	
	
	/**  
	 * 
	 * 处方明细表的最小时间
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月22日 下午3:57:21 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月22日 下午3:57:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public OutpatientUseMedicVo findMin() {
		final String sql = "SELECT MIN(mn.REG_DATE) AS sTime FROM T_OUTPATIENT_FEEDETAIL mn";
		OutpatientUseMedicVo vo = (OutpatientUseMedicVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public OutpatientUseMedicVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("sTime",Hibernate.DATE);
				return (OutpatientUseMedicVo) queryObject.setResultTransformer(Transformers.aliasToBean(OutpatientUseMedicVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	
	
	/**
	 * @Description:门诊药房退费统计：把数据导入mongodb中
	 *病区退费申请表：T_INPATIENT_CANCELITEM_NOW,lT_INPATIENT_CANCELITEM
	 *结算信息表:T_FINANCE_INVOICEINFO_NOW,T_FINANCE_INVOICEINFO 
	 *@param startTime:开始时间 
	 *@param endTime :结束时间
	 *@param invoiceInfoPartName 结算信息表分区表
	 *@param cancelPartName 病区退费申请表分区表
	 * @author: zhangkui
	 * @throws Exception 
	 * @time:2017年5月22日 下午8:04:54
	 */
	public void imTableData_T_INPATIENT_CANCELITEM(String startTime,String endTime,List invoiceInfoPartName,List cancelPartName) throws Exception{
		mbDao = new MongoBasicDao();
		//mbDao.deleteData(TABLENAME_MZYFTFTJ);第一次执行时，把数据先删了
		/************************************************开始导入数据******************************************/
		
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		sb.append(" SELECT INVOICENO,PATIENTNAME,FEESTATCODE,CONFIRMDATE,SUM(REFUNDMONEY) AS REFUNDMONEY,INVO_CODE AS INVOCODE,SENDWIN ");
		sb.append(" FROM ( ");
		if(invoiceInfoPartName!=null&&invoiceInfoPartName.size()>0){
			for(int j=0;j<invoiceInfoPartName.size();j++){
				if(j!=0){
					sb.append(" UNION ALL ");
				}
				for(int i=0;i<cancelPartName.size();i++){
					if(i!=0){
						sb.append(" UNION ALL ");
					}
					sb.append(" SELECT T.BILL_NO AS INVOICENO,T.NAME AS PATIENTNAME,T.INVO_CODE AS FEESTATCODE,T.CONFIRM_DATE AS CONFIRMDATE,SUM (T.SALE_PRICE * T.QUANTITY) AS REFUNDMONEY,T.INVO_CODE,I.DRUG_WINDOW AS SENDWIN ");
					sb.append(" FROM "+cancelPartName.get(i)+" T,"+invoiceInfoPartName.get(j)+" I ");
					sb.append(" WHERE T.APPLY_FLAG = 1	AND T.DRUG_FLAG = 1 AND T.BILL_NO = I.INVOICE_NO AND T.INVO_CODE IN (01, 02, 03) AND T.CONFIRM_DATE < TO_DATE(:endTime,'yyyy-mm-dd hh24:mi:ss') AND T.CONFIRM_DATE >= TO_DATE(:startTime,'yyyy-mm-dd hh24:mi:ss') ");
					sb.append(" GROUP BY T.BILL_NO,T.INVO_CODE,T.NAME,T.CONFIRM_DATE,I.DRUG_WINDOW,T.INVO_CODE ");
				}
				
			}
		}
		sb.append("	)GROUP BY INVOICENO,PATIENTNAME,FEESTATCODE,CONFIRMDATE,INVO_CODE,SENDWIN ");
		if (startTime != null && endTime != null) {
			int count=0;//记录更新次数
			Date et = simpleDateFormat.parse(endTime);
			Date s_t = simpleDateFormat.parse(startTime);
			Long es=et.getTime();
			Long ss= s_t.getTime();
			if (es > ss) {
				if ((es - ss) < (86400000)) {// 查询的区间小于一天，精确更新24 * 60 * 60 * 1000=86400000
					//可以把结束时间加1秒，因为sql是<
					Long e_s=es+1000;
					endTime =simpleDateFormat.format(new Date(e_s));//yyyy-MM-dd HH:mm:ss
					SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
				    queryObject.addScalar("invoiceNo",Hibernate.STRING).addScalar("patientName",Hibernate.STRING)
				    			.addScalar("feeStatCode",Hibernate.STRING).addScalar("confirmDate",Hibernate.DATE)
				    			.addScalar("refundMoney",Hibernate.DOUBLE).addScalar("sendWin",Hibernate.STRING)
				    			.addScalar("invocode",Hibernate.STRING);
					
					queryObject.setParameter("endTime", endTime);
					queryObject.setParameter("startTime", startTime);
				
					System.out.println("本次更新区间小于一天，执行的sql是:"+queryObject.getQueryString());
					System.out.println("时间区间为："+startTime+"-"+endTime);
					// 执行sql，查询结果
					List<RefundVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(RefundVo.class)).list();
				  	if(list!=null&&list.size()>0){
			  			 for(RefundVo r : list){
			  				 	String confirmDate= dateFormat.format(r.getConfirmDate());
			  				 	
			  				 	Document document1 = new Document();
								document1.append("invoiceNo", r.getInvoiceNo());
								document1.append("patientName", r.getPatientName());
								document1.append("feeStatCode", r.getFeeStatCode());
								document1.append("confirmDate", confirmDate);
								document1.append("invocode", r.getInvocode());
								document1.append("sendWin", r.getSendWin());
								
								BasicDBObject bdObject = new BasicDBObject();
								if(r.getInvoiceNo()!=null){
									bdObject.append("invoiceNo",r.getInvoiceNo());
								}
								if(r.getPatientName()!=null){
									bdObject.append("patientName",r.getPatientName());
								}
								if(r.getFeeStatCode()!=null){
									bdObject.append("feeStatCode",r.getFeeStatCode());
								}
								if(confirmDate!=null){
									System.out.println("正在检查"+confirmDate+"日,是否已存在相同的数据！");
									bdObject.append("confirmDate",confirmDate);
								}
								if(r.getInvocode()!=null){
									bdObject.append("invocode",r.getInvocode());
								}
								if(r.getSendWin()!=null){
									bdObject.append("sendWin",r.getSendWin());
								}
								
								DBCursor cursor=mbDao.findAlldata(TABLENAME_MZYFTFTJ, bdObject);
								Double  refundMoney=0.0;
								while(cursor.hasNext()){
									System.out.println("发现当天已有相同的数据。。。。！");
									DBObject dbCursor = cursor.next();
									//相同条件的叠加
									System.out.println("上次结果是:"+dbCursor.get("refundMoney"));
									refundMoney+=Double.parseDouble(dbCursor.get("refundMoney").toString());
								}
								
								refundMoney+=r.getRefundMoney();
								Document document = new Document();
								document.append("invoiceNo", r.getInvoiceNo());
								document.append("patientName", r.getPatientName());
								document.append("feeStatCode", r.getFeeStatCode());
								document.append("refundMoney",refundMoney.toString());
								document.append("sendWin", r.getSendWin());
								document.append("invocode", r.getInvocode());
			  					document.append("confirmDate", confirmDate);
								//更新数据
								System.out.println("本次结果是："+refundMoney);
								try {
									count++;
									mbDao.update(TABLENAME_MZYFTFTJ, document1,document, true);
									System.out.println("更新成功,时间区间为："+startTime+"-"+endTime+"更新第"+count+"条数据！");
								} catch (Exception e) {
									System.out.println("更新失败,时间区间为："+startTime+"-"+endTime+"更新第"+count+"条数据失败！");
									e.printStackTrace();
								}
			  					
			  			 }
				  	}
				} else {// 查询的区间大于一天，按天更新
					Date beginDate = dateFormat.parse(startTime);
					long beginSS = beginDate.getTime();
					Date endDate = dateFormat.parse(endTime);
					long endSS = endDate.getTime();
					int days = 0;
					days = (int) ((endSS - beginSS) / (86400000))+1;// 区间天数24 * 60 * 60 * 1000=86400000
					for (int i = 1; i <= days; i++) {
						// 每次执行时间格式如>=2017-05-22 00:00:00 <2015-05-23 00:00:00
						// 设置参数
						// 结束日期是开始日期的下一天
						Date st = dateFormat.parse(startTime);
						Date ed = DateUtils.addDay(st, 1);
//						for(int j=0;j<=23;j++){
//							
//							
//						}
						String s = dateFormat.format(st)+ " 00:00:00";
						String e = dateFormat.format(ed)+ " 00:00:00";
						SQLQuery queryObject = getSession().createSQLQuery(
								sb.toString());
					    queryObject.addScalar("invoiceNo",Hibernate.STRING).addScalar("patientName",Hibernate.STRING)
		    			.addScalar("feeStatCode",Hibernate.STRING).addScalar("confirmDate",Hibernate.DATE)
		    			.addScalar("refundMoney",Hibernate.DOUBLE).addScalar("sendWin",Hibernate.STRING)
		    			.addScalar("invocode",Hibernate.STRING);
						queryObject.setParameter("endTime", e);
						queryObject.setParameter("startTime", s);

						System.out.println("本次更新区间大于一天,按天执行,第"+i+"天,执行的sql是:"+queryObject.getQueryString());
						System.out.println("时间区间为："+s+"-"+e);
						// 执行sql，查询结果
						List<RefundVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(RefundVo.class)).list();
						// 遍历list，将数据插入mongodb
					  	if(list!=null&&list.size()>0){
				  			 for(RefundVo r : list){
				  				 	String confirmDate= dateFormat.format(r.getConfirmDate());
				  				 	
				  				 	Document document1 = new Document();
									document1.append("invoiceNo", r.getInvoiceNo());
									document1.append("patientName", r.getPatientName());
									document1.append("feeStatCode", r.getFeeStatCode());
									document1.append("confirmDate", confirmDate);
									document1.append("invocode", r.getInvocode());
									document1.append("sendWin", r.getSendWin());
									
									Document document = new Document();
									document.append("invoiceNo", r.getInvoiceNo());
									document.append("patientName", r.getPatientName());
									document.append("feeStatCode", r.getFeeStatCode());
									document.append("refundMoney",r.getRefundMoney().toString());
									document.append("sendWin", r.getSendWin());
									document.append("invocode", r.getInvocode());
				  					document.append("confirmDate", confirmDate);
									try {
										count++;
										mbDao.update(TABLENAME_MZYFTFTJ, document1,document, true);
										System.out.println("更新成功,时间区间为："+s+"-"+e+"更新第"+count+"条数据！");
									} catch (Exception ex) {
										System.out.println("更新失败,时间区间为："+s+"-"+e+"更新第"+count+"条数据失败！");
										ex.printStackTrace();
									}
				  					
				  			 }
					  	}
						startTime= e;
					}

				}
			}
		}
		/************************************************ 结束导入数据 ******************************************/

	}
	
	/**
	 *@Description:科室统计--门诊住院工作同期对比表   病床信息表:T_BUSINESS_HOSPITALBED---->没有分区表，没有在线表  
	 * 挂号主表:T_REGISTER_MAIN----->有分区表 ,有在线表 ,走分区
	 * 住院表：T_INPATIENT_INFO----->没有分区表，有在线表 ，但是还要走分区
	 *@param:Btime 开始时间:注意按月更新,如果时间为2017-01-01 00:00:00-2017-06-07 00:00:00 更新数据为为1-6月的所有数据
	 *@param:Etime 结束时间:注意按月更新,如果时间为2017-01-01 00:00:00-2017-06-07 00:00:00 更新数据为为1-6月的所有数据
	 *@param:areaCode 院区编号，默认全部
	 *@param:ghList 挂号分表集合
	 *@param:zyList 住院分表集合
	 *@author zhangkui
	 * @throws Exception 
	 */
	public void imTableData_KSTJ_MZZYGZTQDBB(String Btime,String Etime,String areaCode,List ghList,List zyList) throws Exception{
		 
		 mbDao = new MongoBasicDao();
		 final String FALG_KSTJ_MZRC = "KSTJ_MZRC";//门诊人次：科室统计--门诊住院工作同期对比表    按月存：门诊人次，挂号时间，院区，标记
		 final String FALG_KSTJ_JZRC = "KSTJ_JZRC";//急诊人次：科室统计--门诊住院工作同期对比表    按月存：急诊人次，时间，院区，标记
		 final String FALG_KSTJ_RYRC = "KSTJ_RYRC";//入院人次：科室统计--门诊住院工作同期对比表    按月存：入院人次，时间，院区，标记
		 final String FALG_KSTJ_CYRC = "KSTJ_CYRC";//出院人次：科室统计--门诊住院工作同期对比表     按月存：出院人次，时间(出院时间-入院时间)，院区，标记
		 final String FALG_KSTJ_BCSYL = "KSTJ_BCSYL";//病床使用率：科室统计--门诊住院工作同期对比表    按天：当天是占有状态的床位数 ，当天总的床位数，院区，标记
		//final String FALG_KSTJ_PJSYTS="KSTJ_PJSYTS";//平均使用天数：科室统计--门诊住院工作同期对比表
		 List<String> areaCodeList=null;
			if(StringUtils.isNotBlank(areaCode)){
				String[] areaCodes=areaCode.split(",");
				areaCodeList= Arrays.asList(areaCodes);
			}
		//1.门诊人次yyyy-MM//门诊人次：科室统计--门诊住院工作同期对比表    按月存：门诊人次，时间，院区，标记
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 String beginTime = Btime.substring(0,7)+"-01"+" 00:00:00";//如2017-01-01 00:00:00
		 String lastDay=getLastDay(Etime);//查询年的结束时间的月份的天数
		 String endTime=dateFormat.format(DateUtils.addDay(dateFormat.parse(Etime.substring(0, 7)+"-"+lastDay), 1))+" 00:00:00";//如2017-07-01 00:00:00
		 /*******************************************************开始导入门诊人次******************************************************************/
			StringBuffer sb01 = new StringBuffer();
			// R.EMERGENCY_FLAG=0--->
			//这条sql查询普通门诊的人次
			sb01.append(" SELECT T.DEPTNAME AS DEPTNAME ,SUM(T.NUM) AS NUM ,T.DATETIME AS DATETIME FROM ( ");
			for(int i=0;i<ghList.size();i++){
				if(i!=0){
					sb01.append(" UNION ALL ");
				}
				sb01.append(" SELECT ");
				sb01.append(" DECODE(R.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME,COUNT (1) AS NUM ");
				sb01.append(" ,TO_CHAR(R.REG_DATE,'YYYY-MM') AS DATETIME ");
				sb01.append(" FROM ");
				sb01.append(ghList.get(i)).append(" R ");
				sb01.append(" WHERE ");
				sb01.append(" R.DEL_FLG = 0 AND R.STOP_FLG = 0 ");
				if(StringUtils.isNotBlank(Btime)){
					sb01.append(" AND R.REG_DATE >= TO_DATE (:beginTime, 'YYYY-MM-DD HH24:MI:SS') ");
				}
				if(StringUtils.isNotBlank(Etime)){
					sb01.append(" AND R.REG_DATE < TO_DATE (:endTime, 'YYYY-MM-DD HH24:MI:SS') ");
				}
				sb01.append(" AND R.EMERGENCY_FLAG=:flag ");
				if(StringUtils.isNotBlank(areaCode)){
					sb01.append(" AND R.AREA_CODE IN (:areaCode) ");
				}
				sb01.append(" GROUP BY ");
				sb01.append(" R.AREA_CODE,R.REG_DATE");
			}
			sb01.append(" ) T GROUP BY ");
			sb01.append(" T.DEPTNAME,T.DATETIME ");
			HashMap map01 = new HashMap();
			if(StringUtils.isNotBlank(beginTime)){
				map01.put("beginTime", beginTime);
			}
			if(StringUtils.isNotBlank(endTime)){
				map01.put("endTime", endTime);
			}
			if(StringUtils.isNotBlank(areaCode)){
				map01.put("areaCode", areaCodeList);
			}
			map01.put("flag", 0);
			
			//门诊人次(院区，人次)
			List<OutInpatientWorkVo> outInpatientWorkVoList01= namedParameterJdbcTemplate.query(sb01.toString(),map01,new RowMapper<OutInpatientWorkVo>() {
				@Override
				public OutInpatientWorkVo mapRow(ResultSet rs, int r)
						throws SQLException {
					OutInpatientWorkVo vo=new OutInpatientWorkVo();
					vo.setProjectName(rs.getString("DEPTNAME"));
					vo.setDateTime(rs.getString("DATETIME"));
					vo.setNum(rs.getString("NUM"));
					vo.setFlag(FALG_KSTJ_MZRC);
					return vo;
				}
			});
			//数据量不大，因此直接存应该没问题,一年12条
		 	if(outInpatientWorkVoList01!=null&&outInpatientWorkVoList01.size()>0){
	  			 for(OutInpatientWorkVo r : outInpatientWorkVoList01){
	  				 	Document document1 = new Document();
						document1.append("projectName", r.getProjectName());
						document1.append("dateTime", r.getDateTime());
						document1.append("flag",FALG_KSTJ_MZRC);
						
						Document document = new Document();
						document.append("projectName", r.getProjectName());
						document.append("dateTime", r.getDateTime());
						document.append("num", r.getNum());
						document.append("flag",r.getFlag());
						try {
							mbDao.update(TABLENAME_KSTJ_MZZYGZTQDBB, document1,document, true);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
	  					
	  			 }
		  	}
			
	     /*******************************************************结束导入门诊人次******************************************************************/

		 /*******************************************************开始导入急诊人次******************************************************************/
		 	//急诊人次：科室统计--门诊住院工作同期对比表    按月存：急诊人次，时间，院区，标记
		 	HashMap map02 = new HashMap();
			if(StringUtils.isNotBlank(beginTime)){
				map02.put("beginTime", beginTime);
			}
			if(StringUtils.isNotBlank(endTime)){
				map02.put("endTime", endTime);
			}
			if(StringUtils.isNotBlank(areaCode)){
				map02.put("areaCode", areaCodeList);
			}
			map02.put("flag", 1);
			
			//门诊人次(院区，人次)
			List<OutInpatientWorkVo> outInpatientWorkVoList02= namedParameterJdbcTemplate.query(sb01.toString(),map02,new RowMapper<OutInpatientWorkVo>() {
				@Override
				public OutInpatientWorkVo mapRow(ResultSet rs, int r)
						throws SQLException {
					OutInpatientWorkVo vo=new OutInpatientWorkVo();
					vo.setProjectName(rs.getString("DEPTNAME"));
					vo.setDateTime(rs.getString("DATETIME"));
					vo.setNum(rs.getString("NUM"));
					vo.setFlag(FALG_KSTJ_JZRC);//急诊人次
					return vo;
				}
			});
			//数据量不大，因此直接存应该没问题,一年12条
		 	if(outInpatientWorkVoList02!=null&&outInpatientWorkVoList02.size()>0){
	  			 for(OutInpatientWorkVo r : outInpatientWorkVoList02){
	  				 	Document document1 = new Document();
						document1.append("projectName", r.getProjectName());
						document1.append("dateTime", r.getDateTime());
						document1.append("flag",FALG_KSTJ_JZRC);
						
						Document document = new Document();
						document.append("projectName", r.getProjectName());
						document.append("dateTime", r.getDateTime());
						document.append("num", r.getNum());
						document.append("flag",r.getFlag());
						try {
							mbDao.update(TABLENAME_KSTJ_MZZYGZTQDBB, document1,document, true);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
	  					
	  			 }
		  	}
		 /*******************************************************结束导入急诊人次******************************************************************/
		 	
		 /*******************************************************开始导入入院人次******************************************************************/
		 	
		 	//入院人次：科室统计--门诊住院工作同期对比表    按月存：入院人次，时间，院区，标记
		 	//拼接sql
			StringBuffer sb02 = new StringBuffer();
			sb02.append(" SELECT T.DEPTNAME AS DEPTNAME, SUM(T.NUM) AS NUM,T.DATETIME AS DATETIME FROM ( ");
			for(int i=0;i<zyList.size();i++){
				if(i!=0){
					sb02.append(" UNION ALL ");
				}
				sb02.append(" SELECT ");
				sb02.append(" DECODE(P.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME,COUNT (1) AS NUM ");
				sb02.append(" ,TO_CHAR(P.IN_DATE,'YYYY-MM') AS DATETIME ");
				sb02.append(" FROM ");
				sb02.append(zyList.get(i)).append(" P ");
				sb02.append(" WHERE ");
				sb02.append(" P.DEL_FLG = 0 ");
				sb02.append(" AND P.STOP_FLG = 0 ");
				if(StringUtils.isNotBlank(Btime)){
					sb02.append(" AND P.IN_DATE >= TO_DATE (:beginTime, 'YYYY-MM-DD HH24:MI:SS') ");
				}
				if(StringUtils.isNotBlank(Etime)){
					sb02.append(" AND P.IN_DATE < TO_DATE (:endTime, 'YYYY-MM-DD HH24:MI:SS') ");
				}
				if(StringUtils.isNotBlank(areaCode)){
					sb02.append(" AND P.AREA_CODE IN (:areaCode) ");
				}
				sb02.append(" GROUP BY ");
				sb02.append(" P.AREA_CODE ,P.IN_DATE ");
			}
			sb02.append(" ) T GROUP BY ");
			sb02.append(" T.DEPTNAME,T.DATETIME ");
			
			HashMap map03 = new HashMap();
			if (StringUtils.isNotBlank(beginTime)) {
				map03.put("beginTime", beginTime);
			}
			if (StringUtils.isNotBlank(endTime)) {
				map03.put("endTime", endTime);
			}
			if (StringUtils.isNotBlank(areaCode)) {
				map03.put("areaCode", areaCodeList);
			}
		 	
			List<OutInpatientWorkVo> outInpatientWorkVoList03 = namedParameterJdbcTemplate.query(sb02.toString(), map03,new RowMapper<OutInpatientWorkVo>() {
				@Override
				public OutInpatientWorkVo mapRow(ResultSet rs, int r)
						throws SQLException {
					OutInpatientWorkVo vo = new OutInpatientWorkVo();
					vo.setProjectName(rs.getString("DEPTNAME"));
					vo.setDateTime(rs.getString("DATETIME"));
					vo.setNum(rs.getString("NUM"));
					vo.setFlag(FALG_KSTJ_RYRC);
					return vo;
				}

			});
		 	
			//数据量不大，因此直接存应该没问题,一年12条
		 	if(outInpatientWorkVoList03!=null&&outInpatientWorkVoList03.size()>0){
	  			 for(OutInpatientWorkVo r : outInpatientWorkVoList03){
	  				 	Document document1 = new Document();
						document1.append("projectName", r.getProjectName());
						document1.append("dateTime", r.getDateTime());
						document1.append("flag",FALG_KSTJ_RYRC);
						
						Document document = new Document();
						document.append("projectName", r.getProjectName());
						document.append("dateTime", r.getDateTime());
						document.append("num", r.getNum());
						document.append("flag",r.getFlag());
						try {
							mbDao.update(TABLENAME_KSTJ_MZZYGZTQDBB, document1,document, true);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
	  					
	  			 }
		  	}
		 	
		 /*******************************************************结束导入入院人次******************************************************************/
		 
		 /*******************************************************开始导入出院人次******************************************************************/
		 	//出院人次：科室统计--门诊住院工作同期对比表     按月存：出院人次,时间(月),时间和(出院时间-入院时间),院区，标记
		 	//拼接sql
			StringBuffer sb03 = new StringBuffer();
			sb03.append(" SELECT T.DEPTNAME AS DEPTNAME, SUM(T.NUM) AS NUM,T.DATETIME ,SUM(T.VSUM) AS VSUM FROM ( ");
			for(int i=0;i<zyList.size();i++){
				if(i!=0){
					sb03.append(" UNION ALL ");
				}
				sb03.append(" SELECT ");
				sb03.append(" DECODE(P.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME,COUNT (1) AS NUM ");
				
				sb03.append(" ,TO_CHAR(P.OUT_DATE,'YYYY-MM') AS DATETIME ");
				sb03.append(" ,SUM(ROUND(P.OUT_DATE-P.IN_DATE,0)) AS VSUM ");
				
				sb03.append(" FROM ");
				sb03.append(zyList.get(i)).append(" P ");
				sb03.append(" WHERE ");
				sb03.append(" P.DEL_FLG = 0 ");
				sb03.append(" AND P.STOP_FLG = 0 ");
				if(StringUtils.isNotBlank(Btime)){
					sb03.append(" AND P.OUT_DATE >= TO_DATE (:beginTime, 'YYYY-MM-DD HH24:MI:SS') ");
				}
				if(StringUtils.isNotBlank(Etime)){
					sb03.append(" AND P.OUT_DATE < TO_DATE (:endTime, 'YYYY-MM-DD HH24:MI:SS') ");
				}
				if(StringUtils.isNotBlank(areaCode)){
					sb03.append(" AND P.AREA_CODE IN (:areaCode) ");
				}
				sb03.append(" GROUP BY ");
				sb03.append(" P.AREA_CODE,P.OUT_DATE");
			}
			sb03.append(" ) T GROUP BY ");
			sb03.append(" T.DEPTNAME,T.DATETIME ");
			HashMap map04 = new HashMap();// 查询所在年的map
			if (StringUtils.isNotBlank(beginTime)) {
				map04.put("beginTime", beginTime);
			}
			if (StringUtils.isNotBlank(endTime)) {
				map04.put("endTime", endTime);
			}
			if (StringUtils.isNotBlank(areaCode)) {
				map04.put("areaCode", areaCodeList);
			}
			//出院人次
			List<OutInpatientWorkVo> outInpatientWorkVoList04 = namedParameterJdbcTemplate.query(sb03.toString(), map04,new RowMapper<OutInpatientWorkVo>() {
				@Override
				public OutInpatientWorkVo mapRow(ResultSet rs, int r)
						throws SQLException {
					OutInpatientWorkVo vo = new OutInpatientWorkVo();
					vo.setProjectName(rs.getString("DEPTNAME"));
					vo.setDateTime(rs.getString("DATETIME"));
					vo.setNum(rs.getString("NUM"));
					vo.setFlag(FALG_KSTJ_CYRC);
					vo.setvSum(rs.getString("VSUM"));//在这里作为每月的住院时间，用于求平均住院时间
					return vo;
				}

			});
			
			//数据量不大，因此直接存应该没问题,一年12条
		 	if(outInpatientWorkVoList04!=null&&outInpatientWorkVoList04.size()>0){
	  			 for(OutInpatientWorkVo r : outInpatientWorkVoList04){
	  				 	Document document1 = new Document();
						document1.append("projectName", r.getProjectName());
						document1.append("dateTime", r.getDateTime());
						document1.append("flag",FALG_KSTJ_CYRC);
						
						Document document = new Document();
						document.append("projectName", r.getProjectName());
						document.append("dateTime", r.getDateTime());
						document.append("num", r.getNum());
						document.append("flag",r.getFlag());
						document.append("vSum", r.getvSum());//在这里作为每月的住院时间，用于求平均住院时间
						try {
							mbDao.update(TABLENAME_KSTJ_MZZYGZTQDBB, document1,document, true);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
	  					
	  			 }
		  	}
		 /*******************************************************结束导入出院人次******************************************************************/
		 
		/*******************************************************开始导入病床使用率******************************************************************/
		 // final String FALG_KSTJ_BCSYCL = 病床信息表只保存当天的数据，所以没保存信息更新的时间
		 	StringBuffer sb04 = new StringBuffer();
			sb04.append(" SELECT ");
			sb04.append(" DECODE(T.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME ,COUNT (1) AS NUM");//,TO_CHAR(SYSDATE,'YYYY-MM-dd') AS DATETIME
			sb04.append(" FROM ");
			sb04.append(" T_BUSINESS_HOSPITALBED T");
			sb04.append(" WHERE ");
			sb04.append(" T.BED_STATE = 4 ");
			if(StringUtils.isNotBlank(areaCode)){
				sb04.append(" AND T.AREA_CODE IN (:areaCode) ");
			}
			sb04.append(" GROUP BY ");
			sb04.append(" T.AREA_CODE");
			
			HashMap map05 = new HashMap();// 前一年的map
			if (StringUtils.isNotBlank(areaCode)) {
				map05.put("areaCode", areaCodeList);
			}
	
			//所有床位
			StringBuffer sb05 = new StringBuffer();
			sb05.append(" SELECT ");
			sb05.append(" DECODE(T.AREA_CODE,1,'河医院区',2,'郑东院区',3,'惠济院区','河医院区') AS DEPTNAME ,COUNT (1) AS NUM");
			sb05.append(" FROM ");
			sb05.append(" T_BUSINESS_HOSPITALBED T");
			if(StringUtils.isNotBlank(areaCode)){
				sb05.append(" WHERE ");
				sb05.append(" T.AREA_CODE IN (:areaCode) ");
			}
			sb05.append(" GROUP BY ");
			sb05.append(" T.AREA_CODE");
			HashMap map06 = new HashMap();// 前一年的map
			if (StringUtils.isNotBlank(areaCode)) {
				map06.put("areaCode", areaCodeList);
			}
			
			//得到当天的每个院区的所有床位数量
			final List<OutInpatientWorkVo> outInpatientWorkVoList06 = namedParameterJdbcTemplate.query(sb05.toString(), map06,new RowMapper<OutInpatientWorkVo>() {
								@Override
								public OutInpatientWorkVo mapRow(ResultSet rs, int r)
										throws SQLException {
									OutInpatientWorkVo vo = new OutInpatientWorkVo();
									vo.setProjectName(rs.getString("DEPTNAME"));
									vo.setNum(rs.getString("NUM"));
									return vo;
								}

							});
		 	
			//得到当天的每个院区的床位是占有状态的list按天：当天是占有状态的床位数 ，当天总的床位数，当天时间，院区，标记
			List<OutInpatientWorkVo> outInpatientWorkVoList05 = namedParameterJdbcTemplate.query(sb04.toString(), map05,new RowMapper<OutInpatientWorkVo>() {
								@Override
								public OutInpatientWorkVo mapRow(ResultSet rs, int r)
										throws SQLException {
									OutInpatientWorkVo vo = new OutInpatientWorkVo();
									vo.setProjectName(rs.getString("DEPTNAME"));
									//vo.setDateTime(rs.getString("DATETIME"));
									vo.setNum(rs.getString("NUM"));
									for(OutInpatientWorkVo v:outInpatientWorkVoList06){
											if(vo.getProjectName().equals(v.getProjectName())){
												vo.setvSum(v.getNum());//vSum,在这里作为当天的病床的总数
											}
									}
									vo.setFlag(FALG_KSTJ_BCSYL);//病床使用率
									return vo;
								}
							});
			
			//数据量不大，因此直接存应该没问题,一年12条
		 	if(outInpatientWorkVoList05!=null&&outInpatientWorkVoList05.size()>0){
	  			 for(OutInpatientWorkVo r : outInpatientWorkVoList05){
	  				 	Document document1 = new Document();
						document1.append("projectName", r.getProjectName());
						//document1.append("dateTime", r.getDateTime());当天时间不存了，每次更新都是当天数据
						document1.append("flag",FALG_KSTJ_BCSYL);
						
						Document document = new Document();
						document.append("projectName", r.getProjectName());
						//document.append("dateTime", r.getDateTime());
						document.append("num", r.getNum());//占有状态的床位数
						document.append("flag",r.getFlag());
						document.append("vSum", r.getvSum());//vSum,在这里作为当天的病床的总数
						try {
							mbDao.update(TABLENAME_KSTJ_MZZYGZTQDBB, document1,document, true);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
	  					
	  			 }
		  	}
		 	
		/*******************************************************结束导入病床使用率******************************************************************/
		 
		
	}
	
	
	
	
	public void inindata(List<StatisticsVo> list,String date){
		MongoLog mong = new MongoLog();
		mong.setMenuType("MZGXSRTJ");
		Date d = DateUtils.parseDateY_M_D(date);
		mong.setStartTime(d);
		mong.setEndTime(d);
		mong.setState(1);
		mong.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		if(department!=null){
			mong.setCreateDept(department.getDeptCode());
		}
		mong.setCreateTime(new Date());
		if (list != null && list.size() > 0) {
			mong.setCountStartTime(new Date());
			mong.setTotalNum(list.size());
			List<DBObject> userList = new ArrayList<DBObject>();
			for (StatisticsVo r : list) {
//				Document document1 = new Document();
//				document1.append("regDate", date);
//				document1.append("deptCode", r.getDeptCode());
//				document1.append("docterCode", r.getDocterCode());
			
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
			try {
				mbDao.insertDataByList("MZGXSRTJ", userList);
			} catch (Exception ex) {
				mong.setState(0);
				ex.printStackTrace();
			}
		}
		mong.setCountEndTime(new Date());
		this.getHibernateTemplate().save(mong);
	}
	/**  
	 * 
	 * 科室对比表（KSDBB）导入mongodb
	 * @Author: huzhenguo
	 * @CreateDate: 2017年6月5日 下午4:42:03 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年6月5日 下午4:42:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	/*@Override
	public void inTableData_KSDBB(List<FicDeptVO> depts,List<String> tnLs,String begin,String end,boolean timeType) {
		StringBuffer sb = null;
		for (FicDeptVO vo : depts) {
			sb = new StringBuffer();
			sb.append(" select ");
			sb.append(" (select count(1) from "+tnLs.get(0)+" t where");
			sb.append(" t.out_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.out_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 and t.dept_code ='"+vo.getDeptCode()+"') as chuYUNum,");
			sb.append(" (select count(1) from "+tnLs.get(0)+" t where");
			sb.append(" t.in_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.in_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 and t.dept_code ='"+vo.getDeptCode()+"') as ruYuNum,");
			sb.append(" (select nvl(avg(trunc(t.out_date) - trunc(t.in_date)),0) from "+tnLs.get(0)+" t where");
			sb.append(" t.out_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.out_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 and t.dept_code ='"+vo.getDeptCode()+"') as avgInYuDays,");
			sb.append(" (select count(1) from t_business_hospitalbed t where t.stop_flg = 0 and t.del_flg = 0 ) as beds,");
			sb.append(" (select count(1) from t_business_hospitalbed t where t.bed_state = '4' and t.stop_flg = 0 and t.del_flg = 0 ) as bedUsed,");
			sb.append(" (select count(1) from "+tnLs.get(1)+" t where");
			sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 and t.dept_code ='"+vo.getDeptCode()+"') as workNum,");
			sb.append(" (select nvl(sum(t.tot_cost),0) from "+tnLs.get(2)+" t where");
			sb.append(" t.BALANCE_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.BALANCE_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 and t.INHOS_DEPTCODE ='"+vo.getDeptCode()+"') as zhuCost,");
			sb.append(" (select nvl(sum(t.tot_cost),0) from "+tnLs.get(3)+" t where");
			sb.append(" t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
			sb.append(" and t.stop_flg = 0 and t.del_flg = 0 and t.DOCT_DEPT ='"+vo.getDeptCode()+"') as menCost from dual");
			SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
			queryObject.addScalar("ruYuNum",Hibernate.INTEGER)
			   .addScalar("chuYUNum",Hibernate.INTEGER)
			   .addScalar("beds",Hibernate.INTEGER) 
			   .addScalar("bedUsed",Hibernate.INTEGER)
			   .addScalar("avgInYuDays",Hibernate.DOUBLE)
			   .addScalar("workNum",Hibernate.INTEGER)
			   .addScalar("menCost",Hibernate.DOUBLE)
			   .addScalar("zhuCost",Hibernate.DOUBLE);
			queryObject.setParameter("begin", begin);
			queryObject.setParameter("end", end);
			ItemVo itemVo =(ItemVo)queryObject.setResultTransformer(Transformers.aliasToBean(ItemVo.class)).uniqueResult();
			//将需要的数据插入mongodb中
			Document document1 = new Document();
			document1.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
			document1.append("deptCode", vo.getDeptCode());
			document1.append("deptName", vo.getDeptName());
			Document document =null;
			if (timeType==false) {
				List<ItemVo> vos=new ArrayList<ItemVo>();
				BasicDBObject bdObject = new BasicDBObject();
				bdObject.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
				bdObject.append("deptCode", vo.getDeptCode());
				bdObject.append("deptName", vo.getDeptName());
				DBCursor cursor = mbDao.findAlldata(TABLENAME_KSDBB, bdObject);
				while(cursor.hasNext()){
					ItemVo vo2 = new ItemVo();
					DBObject dbCursor = cursor.next();
					int  ruYuNum =(int) dbCursor.get("ruYuNum");
					int  chuYUNum =(int) dbCursor.get("chuYUNum");
					int  beds =(int) dbCursor.get("beds");
					int  bedUsed =(int) dbCursor.get("bedUsed");
					Double  avgInYuDays =(Double) dbCursor.get("avgInYuDays");
					int  workNum =(int) dbCursor.get("workNum");
					Double  menCost =(Double) dbCursor.get("menCost");
					Double  zhuCost =(Double) dbCursor.get("zhuCost");
					vo2.setRuYuNum(ruYuNum);
					vo2.setChuYUNum(chuYUNum);
					vo2.setBeds(beds);
					vo2.setBedUsed(bedUsed);
					vo2.setAvgInYuDays(avgInYuDays);
					vo2.setWorkNum(workNum);
					vo2.setMenCost(menCost);
					vo2.setZhuCost(zhuCost);
					vos.add(vo2);
				}
				document = new Document();
				document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
				document.append("deptCode", vo.getDeptCode());
				document.append("deptName", vo.getDeptName());
				document.append("ruYuNum", itemVo.getRuYuNum()+vos.get(0).getRuYuNum());
				document.append("chuYUNum", itemVo.getChuYUNum()+vos.get(0).getChuYUNum());
				document.append("beds", itemVo.getBeds()+vos.get(0).getBeds());
				document.append("bedUsed", itemVo.getBedUsed()+vos.get(0).getBedUsed());
				document.append("avgInYuDays", itemVo.getAvgInYuDays()+vos.get(0).getAvgInYuDays());
				document.append("workNum", itemVo.getWorkNum()+vos.get(0).getWorkNum());
				document.append("menCost", itemVo.getMenCost()+vos.get(0).getMenCost());
				document.append("zhuCost", itemVo.getZhuCost()+vos.get(0).getZhuCost());
				mbDao.update(TABLENAME_KSDBB, document1,document,true);
			}else{
				document = new Document();
				document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
				document.append("deptCode", vo.getDeptCode());
				document.append("deptName", vo.getDeptName());
				document.append("ruYuNum", itemVo.getRuYuNum());
				document.append("chuYUNum", itemVo.getChuYUNum());
				document.append("beds", itemVo.getBeds());
				document.append("bedUsed", itemVo.getBedUsed());
				document.append("avgInYuDays", itemVo.getAvgInYuDays());
				document.append("workNum", itemVo.getWorkNum());
				document.append("menCost", itemVo.getMenCost());
				document.append("zhuCost", itemVo.getZhuCost());
				mbDao.update(TABLENAME_KSDBB, document1,document,true);
			}
		}
	}*/
	@Override
	public void inTableData_KSDBB(List<String> tnLs,String begin,String end,boolean timeType) {
		mbDao = new MongoBasicDao();
		StringBuffer sb = null;
		sb = new StringBuffer();
		sb.append("select f.dept_name,f.dept_code as dept_code, nvl(sum(tt.chuYUNum), 0) as chuYUNum, nvl(sum(tt.avgInYuDays), 0) as avgInYuDays,nvl(sum(tt.ruYuNum), 0) as ruYuNum,");
		sb.append(" nvl(sum(tt.workNum), 0) as workNum, nvl(sum(tt.zhuCost), 0) as zhuCost, nvl(sum(tt.menCost),0) as menCost,");
		sb.append(" (select count(1) as beds from t_business_hospitalbed t where t.stop_flg = 0 and t.del_flg = 0) as beds,");
		sb.append(" (select count(1) from t_business_hospitalbed t where t.bed_state = '4' and t.stop_flg = 0 and t.del_flg = 0) as bedUsed ");
		sb.append(" from (select t.dept_code as dept_code,count(1) as chuYUNum,nvl(avg(trunc(t.out_date) - trunc(t.in_date)), 0) as avgInYuDays,");
		sb.append(" 0 as ruYuNum, 0 as workNum,0 as zhuCost,0 as menCost from "+tnLs.get(0)+" t");
		sb.append(" where t.out_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.out_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.dept_code");
		sb.append(" union all ");
		sb.append(" select t.dept_code as dept_code, 0 as chuYUNum,0 as avgInYuDays, count(1) as ruYuNum, 0 as workNum,0 as zhuCost,0 as menCost  from "+tnLs.get(0)+" t");
		sb.append(" where t.in_date >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.in_date <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.dept_code");
		sb.append(" union all ");
		sb.append(" select t.DEPT_CODE as dept_code, 0 as chuYUNum, 0 as avgInYuDays, 0 as ruYuNum,count(1) as workNum,0 as zhuCost,0 as menCost from "+tnLs.get(1)+" t");
		sb.append(" where t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.dept_code");
		sb.append(" union all ");
		sb.append(" select t.INHOS_DEPTCODE as dept_code,0 as chuYUNum, 0 as avgInYuDays,0 as ruYuNum, 0 as workNum,nvl(sum(t.tot_cost), 0) as zhuCost,0 as menCost from "+tnLs.get(2)+" t");
		sb.append(" where t.BALANCE_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.BALANCE_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.INHOS_DEPTCODE");
		sb.append(" union all ");
		sb.append(" select t.DOCT_DEPT as dept_code,0 as chuYUNum,0 as avgInYuDays,0 as ruYuNum, 0 as workNum,0 as zhuCost,nvl(sum(t.tot_cost), 0) as menCost from "+tnLs.get(3)+" t");
		sb.append(" where t.REG_DATE >= to_date(:begin,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.REG_DATE <= to_date(:end,'yyyy-mm-dd hh24:mi:ss') ");
		sb.append(" and t.stop_flg = 0 and t.del_flg = 0 group by t.DOCT_DEPT");
		sb.append("  ) tt ");
		sb.append(" right join T_FICTITIOUS_CONTACT f on f.DEPT_CODE = tt.dept_code");
		sb.append(" where f.STOP_FLG = 0 and f.DEL_FLG = 0 and f.type in ('C', 'I', 'N')");
		sb.append(" group by f.dept_code,f.dept_name");
		SQLQuery queryObject = getSession().createSQLQuery(sb.toString());
		queryObject.addScalar("dept_name",Hibernate.STRING)
		   .addScalar("dept_code",Hibernate.STRING)
		   .addScalar("ruYuNum",Hibernate.INTEGER)
		   .addScalar("chuYUNum",Hibernate.INTEGER)
		   .addScalar("beds",Hibernate.INTEGER) 
		   .addScalar("bedUsed",Hibernate.INTEGER)
		   .addScalar("avgInYuDays",Hibernate.DOUBLE)
		   .addScalar("workNum",Hibernate.INTEGER)
		   .addScalar("menCost",Hibernate.DOUBLE)
		   .addScalar("zhuCost",Hibernate.DOUBLE);
		queryObject.setParameter("begin", begin);
		queryObject.setParameter("end", end);
		List<ItemVo> list = queryObject.setResultTransformer(Transformers.aliasToBean(ItemVo.class)).list();
		Document document = null;
		if(list!=null&&list.size()>0){
			for(ItemVo itemVo : list){
				//将需要的数据插入mongodb中
				Document document1 = new Document();
				document1.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
				document1.append("deptCode", itemVo.getDept_code());
				document1.append("deptName", itemVo.getDept_name());
				if (timeType==false) {
					List<ItemVo> vos=new ArrayList<ItemVo>();
					BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					bdObject.append("deptCode", itemVo.getDept_code());
					bdObject.append("deptName", itemVo.getDept_name());
					DBCursor cursor = mbDao.findAlldata(TABLENAME_KSDBB, bdObject);
					while(cursor.hasNext()){
						ItemVo vo2 = new ItemVo();
						DBObject dbCursor = cursor.next();
						int  ruYuNum =(int) dbCursor.get("ruYuNum");
						int  chuYUNum =(int) dbCursor.get("chuYUNum");
						int  beds =(int) dbCursor.get("beds");
						int  bedUsed =(int) dbCursor.get("bedUsed");
						Double  avgInYuDays =(Double) dbCursor.get("avgInYuDays");
						int  workNum =(int) dbCursor.get("workNum");
						Double  menCost =(Double) dbCursor.get("menCost");
						Double  zhuCost =(Double) dbCursor.get("zhuCost");
						vo2.setRuYuNum(ruYuNum);
						vo2.setChuYUNum(chuYUNum);
						vo2.setBeds(beds);
						vo2.setBedUsed(bedUsed);
						vo2.setAvgInYuDays(avgInYuDays);
						vo2.setWorkNum(workNum);
						vo2.setMenCost(menCost);
						vo2.setZhuCost(zhuCost);
						vos.add(vo2);
					}
					document = new Document();
					document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					document.append("deptCode", itemVo.getDept_code());
					document.append("deptName", itemVo.getDept_name());
					document.append("ruYuNum", itemVo.getRuYuNum()+vos.get(0).getRuYuNum());
					document.append("chuYUNum", itemVo.getChuYUNum()+vos.get(0).getChuYUNum());
					document.append("beds", itemVo.getBeds()+vos.get(0).getBeds());
					document.append("bedUsed", itemVo.getBedUsed()+vos.get(0).getBedUsed());
					document.append("avgInYuDays", itemVo.getAvgInYuDays()+vos.get(0).getAvgInYuDays());
					document.append("workNum", itemVo.getWorkNum()+vos.get(0).getWorkNum());
					document.append("menCost", itemVo.getMenCost()+vos.get(0).getMenCost());
					document.append("zhuCost", itemVo.getZhuCost()+vos.get(0).getZhuCost());
					mbDao.update(TABLENAME_KSDBB, document1,document,true);
				}else{
					document = new Document();
					document.append("selectTime", DateUtils.formatDateY_M_D(DateUtils.parseDateY_M_D(end)));
					document.append("deptCode", itemVo.getDept_code());
					document.append("deptName", itemVo.getDept_name());
					document.append("ruYuNum", itemVo.getRuYuNum());
					document.append("chuYUNum", itemVo.getChuYUNum());
					document.append("beds", itemVo.getBeds());
					document.append("bedUsed", itemVo.getBedUsed());
					document.append("avgInYuDays", itemVo.getAvgInYuDays());
					document.append("workNum", itemVo.getWorkNum());
					document.append("menCost", itemVo.getMenCost());
					document.append("zhuCost", itemVo.getZhuCost());
					mbDao.update(TABLENAME_KSDBB, document1,document,true);
				}
			}
		}
	}
	
	 //获取某月的最后一天
	public String getLastDay(String date){
		date= date.substring(0, 7);
		System.out.println(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date time =null;
		try {
			 time = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 Calendar  calendar =  Calendar.getInstance(); 
		 calendar.setTime(time);
		 final  int  lastDay  =   calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		 Date   lastDate   =   calendar.getTime();  
	     lastDate.setDate(lastDay);  
	     SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat1.format(lastDate).substring(8, 10);
	}

	
	
	
	
}
