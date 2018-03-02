package cn.honry.statistics.bi.bistac.monthlyDashboard.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.model.Filters;

import cn.honry.base.bean.model.DrugOutstore;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.vo.StatVo;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.dao.MonthlyDashboardDao;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.MonthlyDashboardVo;
import cn.honry.statistics.bi.bistac.outpatientStac.vo.OutpatientStacVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.statistics.drug.nurseDrugDispens.vo.DrugDispensSumVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("monthlyDashboardDao")
@SuppressWarnings({ "all" })
public class MonthlyDashboardDaoImpl extends
		HibernateEntityDao<MonthlyDashboardVo> implements MonthlyDashboardDao {
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	// 基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	protected ResultSet rs;

	/**
	 * 
	 * 月出院人数
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryInpatientInfoNowGo(String date,
			String deptName) throws Exception{

		BasicDBObject bdObject = new BasicDBObject();
		String[] dateArr = this.returnCountMonth(date);
		List<MonthlyDashboardVo> list1 = new ArrayList<MonthlyDashboardVo>();
		for (String vo : dateArr) {
			bdObject.append("yearAndMonth", vo);
			DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBP_INPATIENT_MONTH", bdObject);
			DBObject dbCursor;
			while (cursor.hasNext()) {
				MonthlyDashboardVo voOne = new MonthlyDashboardVo();
				dbCursor = cursor.next();
				String value = (String) dbCursor.get("countLeave");
				String name = (String) dbCursor.get("yearAndMonth");
				String average = (String) dbCursor.get("average");
				voOne.setCountLeave(value);
				voOne.setYearAndMonth(name);
				voOne.setAverage(average);
				list1.add(voOne);
			}
		}

		if (list1!=null && list1.size() > 0) {
			return list1;
		}
		return new ArrayList<MonthlyDashboardVo>();
	}

	/**
	 * 
	 * 月手术例数
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月31日 上午10:35:12
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月31日 上午10:35:12
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public MonthlyDashboardVo queryOperationApply(final String date,
			final String deptCode)  throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append("select (select nvl(count(t.op_id),0) from T_OPERATION_APPLY t where ");
		if (StringUtils.isNotBlank(date)) {
			sb.append("to_char(t.pre_date,'yyyy-MM')=:date ");
		}
		if (StringUtils.isNotBlank(deptCode)) {
			sb.append("and t.IN_DEPT= :deptCode ");
		}
		sb.append("and t.stop_flg=0 and t.del_flg=0 and t.pasource=2 and t.status=4) as mOperationApply, "
				+ "(select nvl(count(t.op_id),0) from T_OPERATION_APPLY t where to_char(t.pre_date,'yyyy')=to_char(sysdate,'yyyy')  and  t.pasource=2 and t.status=4 and t.stop_flg=0 and t.del_flg=0) as operationApplys from dual");
		MonthlyDashboardVo vo = (MonthlyDashboardVo) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public MonthlyDashboardVo doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery queryObject = session.createSQLQuery(sb
								.toString());
						queryObject.addScalar("mOperationApply",
								Hibernate.INTEGER).addScalar("operationApplys",
								Hibernate.INTEGER);
						if (StringUtils.isNotBlank(date)) {
							queryObject.setParameter("date", date);
						}
						if (StringUtils.isNotBlank(deptCode)) {
							queryObject.setParameter("deptCode", deptCode);
						}
						return (MonthlyDashboardVo) queryObject
								.setResultTransformer(
										Transformers
												.aliasToBean(MonthlyDashboardVo.class))
								.uniqueResult();
					}
				});
		return vo;
	}

	/**
	 * 
	 * 平均住院日
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午10:39:22
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午10:39:22
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryInpatientInfo(final String deptName) throws Exception {
		final StringBuffer sb = new StringBuffer();
		sb.append("select t.in_date as inDate,t.OUT_DATE as outDate from T_INPATIENT_INFO_NOW t where ");
		if (StringUtils.isNotBlank(deptName)) {
			sb.append("t.dept_name=:deptName and ");
		}
		sb.append(" t.in_state = 'R' and t.stop_flg = 0 and t.del_flg = 0");

		List<MonthlyDashboardVo> voList = (List<MonthlyDashboardVo>) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public List<MonthlyDashboardVo> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery queryObject = session
								.createSQLQuery(sb.toString())
								.addScalar("inDate", Hibernate.DATE)
								.addScalar("outDate", Hibernate.DATE);
						if (StringUtils.isNotBlank(deptName)) {
							queryObject.setParameter("deptName", deptName);
						}
						return queryObject.setResultTransformer(
								Transformers
										.aliasToBean(MonthlyDashboardVo.class))
								.list();
					}
				});
		if (voList != null && voList.size() > 0) {
			return voList;
		}
		return new ArrayList<MonthlyDashboardVo>();
	}

	/**
	 * 
	 * 做手术人均住院日
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月1日 上午10:39:22
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月1日 上午10:39:22
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryOperationApplyInfo(
			final String deptName) throws Exception {
		final StringBuffer sb = new StringBuffer();
		sb.append("select t.in_date as inDate,t.OUT_DATE as outDate from T_INPATIENT_INFO_NOW t where ");
		if (StringUtils.isNotBlank(deptName)) {
			sb.append("t.dept_name=:deptName and ");
		}
		sb.append(" t.PATIENT_STATUS='3' and t.in_state = 'R' and t.stop_flg = 0 and t.del_flg = 0");

		List<MonthlyDashboardVo> voList = (List<MonthlyDashboardVo>) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public List<MonthlyDashboardVo> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery queryObject = session
								.createSQLQuery(sb.toString())
								.addScalar("inDate", Hibernate.DATE)
								.addScalar("outDate", Hibernate.DATE);
						if (StringUtils.isNotBlank(deptName)) {
							queryObject.setParameter("deptName", deptName);
						}
						return queryObject.setResultTransformer(
								Transformers
										.aliasToBean(MonthlyDashboardVo.class))
								.list();
					}
				});
		if (voList != null && voList.size() > 0) {
			return voList;
		}
		return new ArrayList<MonthlyDashboardVo>();
	}

	/**
	 * 
	 * 使用中床位、总床位
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 上午11:42:19
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 上午11:42:19
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public MonthlyDashboardVo queryBed(String date, String deptCode)  throws Exception{
		final StringBuffer sb = new StringBuffer();
		sb.append("select (select nvl(count(t.bed_id),0) from T_BUSINESS_HOSPITALBED t where ");
		if (StringUtils.isNotBlank(deptCode)) {
			sb.append("t.dept_name=:deptCode and ");
		}
		sb.append("t.BED_STATE='4' and t.hospital_id=1 and t.stop_flg=0 and t.del_flg=0 ) as useBed, "
				+ "(select nvl(count(t.bed_id),0) from T_BUSINESS_HOSPITALBED t where t.hospital_id=1 and t.stop_flg=0 and t.del_flg=0) as totalBed from dual");
		MonthlyDashboardVo vo = (MonthlyDashboardVo) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public MonthlyDashboardVo doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery queryObject = session.createSQLQuery(sb
								.toString());
						queryObject.addScalar("freeBed", Hibernate.INTEGER)
								.addScalar("totalBed", Hibernate.INTEGER);
						return (MonthlyDashboardVo) queryObject
								.setResultTransformer(
										Transformers
												.aliasToBean(MonthlyDashboardVo.class))
								.uniqueResult();
					}
				});
		return vo;
	}

	/**
	 * 
	 * 治疗数量
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryTreatment(List<String> tnL,
			final String date, final String deptName, final String begin,
			final String end)  throws Exception{
		if (tnL == null || tnL.size() < 0) {
			return new ArrayList<MonthlyDashboardVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select outState,sum(outStateTotal) as outStateTotal from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				sb.append(" union all ");
			}
			sb.append(
					" select t.diag_outstate as outState,nvl(count(t.inpatient_id),0) as outStateTotal from ")
					.append(tnL.get(i)).append(" t ").append(" where  ");
			if (StringUtils.isNotBlank(deptName)) {
				sb.append(" t.DEPT_NAME = :deptName and ");
			}
			if (StringUtils.isNotBlank(begin)) {
				sb.append(" trunc(t.OUT_DATE,'dd') >= to_date(:begin,'yyyy-MM-dd') and ");
			}
			if (StringUtils.isNotBlank(end)) {
				sb.append(" trunc(t.OUT_DATE,'dd') <= to_date(:end,'yyyy-MM-dd') and ");
			}
			sb.append(" t.stop_flg=0 and t.del_flg=0 group by t.diag_outstate");
		}
		sb.append(" ) group by outState ");
		List<MonthlyDashboardVo> voList = (List<MonthlyDashboardVo>) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public List<MonthlyDashboardVo> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery queryObject = session.createSQLQuery(sb
								.toString());
						queryObject.addScalar("outStateTotal",
								Hibernate.INTEGER).addScalar("outState",
								Hibernate.INTEGER);
						if (StringUtils.isNotBlank(deptName)) {
							queryObject.setParameter("deptName", deptName);
						}
						if (StringUtils.isNotBlank(begin)) {
							queryObject.setParameter("begin", begin);
						}
						if (StringUtils.isNotBlank(end)) {
							queryObject.setParameter("end", end);
						}
						return queryObject.setResultTransformer(
								Transformers
										.aliasToBean(MonthlyDashboardVo.class))
								.list();
					}
				});
		if (voList != null && voList.size() > 0) {
			return voList;
		}
		return new ArrayList<MonthlyDashboardVo>();
	}

	/**
	 * 
	 * 获取住院表的最大和最小时间
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月29日 上午10:51:26
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月29日 上午10:51:26
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public MonthlyDashboardVo findMaxMin()  throws Exception{
		final String sql = "SELECT MAX(mn.IN_DATE) AS eTime ,MIN(mn.IN_DATE) AS sTime FROM T_INPATIENT_INFO_NOW mn";
		MonthlyDashboardVo vo = (MonthlyDashboardVo) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public MonthlyDashboardVo doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery queryObject = session.createSQLQuery(sql
								.toString());
						queryObject.addScalar("eTime", Hibernate.DATE)
								.addScalar("sTime", Hibernate.DATE);
						return (MonthlyDashboardVo) queryObject
								.setResultTransformer(
										Transformers
												.aliasToBean(MonthlyDashboardVo.class))
								.uniqueResult();
					}
				});
		return vo;
	}

	/**
	 * 
	 * 获取费用汇总表的最大和最小时间
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年3月29日 上午10:51:26
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年3月29日 上午10:51:26
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public MonthlyDashboardVo findFeeMaxMin()  throws Exception{
		final String sql = "SELECT MAX(mn.CREATETIME) AS eTime ,MIN(mn.CREATETIME) AS sTime FROM t_inpatient_feeinfo_now mn";
		MonthlyDashboardVo vo = (MonthlyDashboardVo) this
				.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public MonthlyDashboardVo doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery queryObject = session.createSQLQuery(sql
								.toString());
						queryObject.addScalar("eTime", Hibernate.DATE)
								.addScalar("sTime", Hibernate.DATE);
						return (MonthlyDashboardVo) queryObject
								.setResultTransformer(
										Transformers
												.aliasToBean(MonthlyDashboardVo.class))
								.uniqueResult();
					}
				});
		return vo;
	}

	/**
	 * 
	 * 住院费用
	 * 
	 * @Author: huzhenguo
	 * @CreateDate: 2017年4月5日 下午2:26:04
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年4月5日 下午2:26:04
	 * @ModifyRmk:
	 * @version: V1.0
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryHospExpenses(String deptName,
			String end)  throws Exception{
		String[] dateArr = end.split("-");
		String[] date1Arr = new String[12];
		String[] monArr = { "01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "11", "12" };
		int mon = Integer.parseInt(dateArr[1]);
		int yea = Integer.parseInt(dateArr[0]) - 1;
		for (int i = 0; i < 12; i++) {
			if (i < mon) {
				date1Arr[i] = dateArr[0] + "-" + monArr[i];
			} else {
				date1Arr[i] = yea + "-" + monArr[i];
			}
		}
		BasicDBObject bdObject = new BasicDBObject();
		List<MonthlyDashboardVo> list1 = new ArrayList<MonthlyDashboardVo>();
		for (int i = 0, len = date1Arr.length; i < len; i++) {
			
			bdObject.append("yearAndMonth", date1Arr[i]);
			DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBP_HOSPEXPENSES_MONTH", bdObject);
			if(!cursor.hasNext()){
				MonthlyDashboardVo voOne = new MonthlyDashboardVo();
				voOne.setTotCost0(0.0);
				voOne.setYearAndMonth(date1Arr[i]);
				list1.add(voOne);
				continue;
			}
			DBObject dbCursor;
			while (cursor.hasNext()) {
				MonthlyDashboardVo voOne = new MonthlyDashboardVo();
				dbCursor = cursor.next();
				Double value = (Double) dbCursor.get("totCost0");
				String name = (String) dbCursor.get("yearAndMonth");
				voOne.setTotCost0(value);
				voOne.setYearAndMonth(name);
				list1.add(voOne);
			}
		}
		return list1;

	}

	@Override
	public boolean saveHospExpensesToDBMonth() throws Exception {
		boolean sign = false;
		final StringBuffer buffer = new StringBuffer();
		List<String> tnL = new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		buffer.append("select Sum(b.totCost) AS totCost0, b.mon AS yearAndMonth from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select sum(t" + i + ".TOT_COST) as totCost,");
			buffer.append("to_char(t" + i + ".fee_date, 'yyyy-mm') as mon ");
			buffer.append("from " + tnL.get(i) + " t" + i);
			buffer.append(" group by t" + i + ".fee_date ");
		}
		buffer.append(" ) b group by b.mon");
		List<MonthlyDashboardVo> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("yearAndMonth")
				.addScalar("totCost0", Hibernate.DOUBLE)
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();

		if (list!=null && list.size() > 0) {
			for (MonthlyDashboardVo vo : list) {
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());// 时间
				Document document = new Document();
				document.append("totCost0", vo.getTotCost0());
				document.append("yearAndMonth", vo.getYearAndMonth());
				new MongoBasicDao().update("MYZHYBPZYFY", document1, document, true);
			}
			sign = true;
		}
		return sign;
	}

	@Override
	public boolean saveHospExpensesToDBYear()  throws Exception{
		boolean sign = false;
		final StringBuffer buffer = new StringBuffer();
		List<String> tnL = new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		buffer.append("select Sum(b.totCost) AS totCost0, b.mon AS yearAndMonth from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select sum(t" + i + ".TOT_COST) as totCost,");
			buffer.append("to_char(t" + i + ".fee_date, 'yyyy') as mon ");
			buffer.append("from " + tnL.get(i) + " t" + i);
			buffer.append(" group by t" + i + ".fee_date ");
		}
		buffer.append(" ) b group by b.mon");
		List<MonthlyDashboardVo> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("yearAndMonth")
				.addScalar("totCost0", Hibernate.DOUBLE)
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();

		if (list!=null && list.size() > 0) {
			List<DBObject> userList = new ArrayList<DBObject>();
			for (MonthlyDashboardVo vo : list) {
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());// 时间
				Document document = new Document();
				document.append("totCost0", vo.getTotCost0());
				document.append("yearAndMonth", vo.getYearAndMonth());
				new MongoBasicDao().update("MYZHYBPZYFY", document1, document, true);
			}
			sign = true;
		}
		return sign;
	}

	@Override
	public boolean saveHospExpensesToDBDay() throws Exception {
		boolean sign = false;
		final StringBuffer buffer = new StringBuffer();
		List<String> tnL = new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		buffer.append("select Sum(b.totCost) AS totCost0, b.mon AS yearAndMonth from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select sum(t" + i + ".TOT_COST) as totCost,");
			buffer.append("to_char(t" + i + ".fee_date, 'yyyy-mm-dd') as mon ");
			buffer.append("from " + tnL.get(i) + " t" + i);
			buffer.append(" group by t" + i + ".fee_date ");
		}
		buffer.append(" ) b group by b.mon");
		List<MonthlyDashboardVo> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("yearAndMonth")
				.addScalar("totCost0", Hibernate.DOUBLE)
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();

		if (list!=null && list.size() > 0) {
			List<DBObject> userList = new ArrayList<DBObject>();
			for (MonthlyDashboardVo vo : list) {
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());// 时间
				Document document = new Document();
				document.append("totCost0", vo.getTotCost0());
				document.append("yearAndMonth", vo.getYearAndMonth());
				new MongoBasicDao().update("MYZHYBPZYFY", document1, document, true);
			}
			sign = true;
		}
		return sign;
	}

	@Override
	public boolean saveSurgeryToDB() throws Exception {
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(310);
		buffer.append("select TO_Char(sum(b.num)) AS value,b.dat AS name,b.dept AS dept, to_char(trunc(sum(b.days)/sum(b.num), 2)) AS inhost from ( ");
		buffer.append("select nvl(count(t.op_id) , 0) as num,to_char(t.pre_date,'yyyy-mm') as dat,t.op_doctordept as dept, ");
		buffer.append("sysdate - decode(sign(nvl(nvl(i.in_date,ii.in_date),sysdate-2)-to_date('1970-01-01','yyyy-mm-dd')),-1,sysdate-2,nvl(nvl(i.in_date,ii.in_date),sysdate-2)) as days ");
		buffer.append("from T_OPERATION_APPLY t  ");
		buffer.append("left join t_inpatient_info_now i on t.clinic_code = i.inpatient_no ");
		buffer.append("left join t_inpatient_info ii on t.clinic_code = ii.inpatient_no ");
		buffer.append("where t.stop_flg = 0 and t.del_flg = 0 and t.status = 4 ");
		buffer.append("group by t.pre_date,t.op_doctordept,i.in_date,ii.in_date ");
		buffer.append(" ) b group by b.dat,b.dept");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("inhost")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (Dashboard vo : list) {
				Document document1 = new Document();
				document1.append("name", vo.getName());// 时间
				document1.append("dept", vo.getDept());// 科室
				Document document = new Document();
				document.append("dept", vo.getDept());// 科室
				document.append("value", vo.getValue());// 手术例数统计
				document.append("name", vo.getName());// 时间
				document.append("inHost", vo.getInhost());// 住院时间
				new MongoBasicDao().update("MYZHYBPSSLS", document1, document, true);
			}
			/** 手术例数 **/
			sign = true;
		}
		return sign;
	}

	@Override
	public List<Dashboard> querySurgeryForDB(String date, String dept) throws Exception {
		BasicDBObject bdObject = new BasicDBObject();
		List<Dashboard> list = new ArrayList<Dashboard>();
		String[] dateArr = this.returnSameSe(date);
		
		for (String vo : dateArr) {
			bdObject.append("name", vo);
			DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBP_SURGERY_MONTH", bdObject);
			DBObject dbCursor;
			while (cursor.hasNext()) {
				Dashboard voOne = new Dashboard();
				dbCursor = cursor.next();
				String value = (String) dbCursor.get("value");
				String name = (String) dbCursor.get("name");
				String dept1 = (String) dbCursor.get("dept");
				String inHost = (String) dbCursor.get("inHost");
				voOne.setValue(value);
				voOne.setName(name);
				voOne.setDept(dept1);
				voOne.setInhost(inHost);
				list.add(voOne);
			}
		}
		return list;
	}

	@Override
	public boolean svaeTreatmentToDB() throws Exception {
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(770);
		buffer.append("select b.stat AS stat,TO_Char(sum(b.con)) AS value,b.dat AS name,b.dept AS dept from ( ");
		buffer.append("select  decode(nvl(t.diag_outstate,0),0,'治愈',1,'好转',2,'未愈',3,'死亡',4,'其他') as stat,count(nvl(t.diag_outstate,0)) as con,to_char(t.out_date,'yyyy-mm') as dat,t.dept_name as dept from t_inpatient_info_now t where t.in_state in('O','B') and t.stop_flg=0 and t.del_flg=0 group by t.out_date,t.diag_outstate,t.dept_name ");
		buffer.append("union all ");
		buffer.append("select  decode(nvl(t.diag_outstate,0),0,'治愈',1,'好转',2,'未愈',3,'死亡',4,'其他') as stat,count(nvl(t.diag_outstate,0)) as con,to_char(t.out_date,'yyyy-mm') as dat,t.dept_name as dept from t_inpatient_info t where t.in_state in('O','B') and t.stop_flg=0 and t.del_flg=0 group by t.out_date,t.diag_outstate,t.dept_name ");
		buffer.append(") b group by b.dat,b.con,b.stat,b.dept ");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("stat")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			List<DBObject> userList = new ArrayList<DBObject>();
			for (Dashboard vo : list) {
				Document document1 = new Document();
				document1.append("name", vo.getName());// 时间
				document1.append("dept", vo.getDept());// 科室
				Document document = new Document();
				document.append("stat", vo.getStat());// 状态
				document.append("dept", vo.getDept());// 科室
				document.append("value", vo.getValue());// 治疗数量
				document.append("name", vo.getName());// 时间
				new MongoBasicDao().update("MYZHYBPZLSL", document1, document, true);
			}
			/** 治疗数量 **/
			sign = true;
		}
		return sign;
	}
	/**
	 * mongdb中没有数据的时，分区查询治疗数量
	 * 
	 * */
	@Override
	public List<Dashboard> svaeTreatment(List<String> tnL, String startData,String endData,String deptName) throws Exception {
		if(tnL==null||tnL.size()==0){
			return new ArrayList<Dashboard>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select b.stat AS stat,TO_Char(sum(b.con)) AS value,b.dat AS name,b.dept AS dept from ( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("select  decode(nvl(t"+i+".diag_outstate,0),0,'治愈',1,'好转',2,'未愈',3,'死亡',4,'其他') as stat,t"+i+".diag_outstate as con,to_char(t"+i+".out_date,'yyyy-mm') as dat,t"+i+".dept_name as dept ");
			sb.append("from "+tnL.get(i)+" t"+i+" ");
			sb.append("where t"+i+".in_state in('O','B') and t"+i+".stop_flg=0 and t"+i+".del_flg=0 ");
			sb.append("and t"+i+".out_date>=to_date('"+startData+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".out_date<to_date('"+endData+"','yyyy-mm-dd HH24:mi:ss') ");
			if(StringUtils.isNotBlank(deptName)){
				sb.append("and t"+i+".dept_name='"+deptName+"' ");
			}
		}
			sb.append(") b group by b.dat,b.con,b.stat,b.dept ");
			List<Dashboard> voList =namedParameterJdbcTemplate.query(sb.toString(),new HashMap<String, Object>(0),new RowMapper<Dashboard>(){
				@Override
				public Dashboard mapRow(ResultSet rs, int rowNum) throws SQLException {
					Dashboard vo = new Dashboard();
					vo.setName(rs.getString("name"));
					vo.setDept(rs.getString("dept"));
					vo.setStat(rs.getString("stat"));
					vo.setValue(rs.getString("value"));
					return vo;
				}});
			if(voList.size()>0){
				return voList ;
			}
		return new ArrayList<Dashboard>();	
	}

	@Override
	public List<Dashboard> queryTreatmentFromDB(String date, String dept) throws Exception {
		BasicDBObject bdObject = new BasicDBObject();
		List<Dashboard> list = new ArrayList<Dashboard>();
		bdObject.append("name", date);
		if (StringUtils.isNotBlank(dept)) {
			bdObject.append("dept", dept);
		}
		DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBP_TREATMENT_MONTH", bdObject);
		DBObject dbCursor;
		while (cursor.hasNext()) {
			Dashboard voOne = new Dashboard();
			dbCursor = cursor.next();
			String value = (String) dbCursor.get("value");
			String name = (String) dbCursor.get("name");
			String dept1 = (String) dbCursor.get("dept");
			String stat = (String) dbCursor.get("stat");
			voOne.setValue(value);
			voOne.setName(name);
			voOne.setDept(dept1);
			voOne.setStat(stat);
			list.add(voOne);
		}
		return list;
	}

	@Override
	public boolean svaeWardApplyToDB()  throws Exception{
		boolean sign = false;
		StringBuffer buffer = new StringBuffer();
		buffer.append("select TO_CHAR(trunc( sum(c.con)/(select distinct count(1) from t_business_hospitalbed),4)) AS value,c.dat AS name,c.dept AS dept,to_char(trunc(sum(c.days)/sum(c.con),2)) as inhost from( ");
		buffer.append("select count(b.dat) as con,b.dat as dat,b.dept as dept,b.days as days from( ");
		buffer.append("select  TO_CHAR(t.in_date,'yyyy-mm') as dat,t.dept_code as dept,sysdate-t.in_date as days from t_inpatient_info_now t where t.in_state='I' group by t.in_date,t.dept_code ");
		buffer.append("union all ");
		buffer.append("select  TO_CHAR(t.in_date,'yyyy-mm') as dat,t.dept_code as dept,sysdate-t.in_date as days from t_inpatient_info t where t.in_state='I' group by t.in_date,t.dept_code ");
		buffer.append(") b group by b.dat,b.dept,b.days ");
		buffer.append(") c group by c.dat ,c.dept ");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("inhost")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (Dashboard vo : list) {
				Document document1 = new Document();
				document1.append("name", vo.getName());// 时间
				document1.append("dept", vo.getDept());// 科室
				Document document = new Document();
				document.append("dept", vo.getDept());// 科室
				document.append("value", vo.getValue());// 病床使用率
				document.append("name", vo.getName());// 时间
				document.append("inhost", vo.getInhost());// 在院时间
				new MongoBasicDao().update("MYZHYBPBCSYL", document1, document, true);
			}
			sign = true;
		}
		return sign;
	}

	@Override
	public List<Dashboard> queryWardApplyFromDB(String date, String dept) throws Exception {
		String[] dateArr = this.returnSameSe(date);
		BasicDBObject bdObject = new BasicDBObject();
		List<Dashboard> list = new ArrayList<Dashboard>();
		if (StringUtils.isNotBlank(dept)) {
			bdObject.append("dept", dept);
		}
		for (String vo : dateArr) {
			bdObject.append("name", vo);
			DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBP_WARDAPPLY_MONTH", bdObject);
			DBObject dbCursor;
			while (cursor.hasNext()) {
				Dashboard voOne = new Dashboard();
				dbCursor = cursor.next();
				String value = (String) dbCursor.get("value");
				String name = (String) dbCursor.get("name");
				String dept1 = (String) dbCursor.get("dept");
				String inhost = (String) dbCursor.get("inhost");
				voOne.setValue(value);
				voOne.setName(name);
				voOne.setDept(dept1);
				voOne.setInhost(inhost);
				list.add(voOne);
			}
		}
		return list;
	}

	@Override
	public boolean saveInpatientInfoToDB() throws Exception {
		boolean sign = false;
		final StringBuffer buffer = new StringBuffer(370);
		buffer.append("select TO_Char(Sum(b.totCost)) AS countLeave, b.mon AS yearAndMonth,to_char(floor(b.days)) AS average  from ( ");
		buffer.append("select count(1) as totCost, to_char(t.in_date, 'yyyy-mm') as mon,trunc(decode(sign(t.out_date-t.in_date),-1,t.in_date + 2,t.out_date) - t.in_date,2) as days ");
		buffer.append("from t_inpatient_info t ");
		buffer.append("where t.in_state = 'O' group by t.in_date,t.out_date ");
		buffer.append(" union all ");
		buffer.append("select count(1) as totCost, to_char(t1.in_date, 'yyyy-mm') as mon,trunc(decode(sign(t1.out_date-t1.in_date),-1,t1.in_date + 2,t1.out_date) - t1.in_date,2) as days ");
		buffer.append("from t_inpatient_info_now t1 ");
		buffer.append("where t1.in_state = 'O' group by t1.in_date,t1.out_date ");
		buffer.append(") b group by b.mon,b.days");
		List<MonthlyDashboardVo> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("countLeave")
				.addScalar("yearAndMonth")
				.addScalar("average")
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (MonthlyDashboardVo vo : list) {
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());
				Document document = new Document();
				document.append("countLeave", vo.getCountLeave());
				document.append("yearAndMonth", vo.getYearAndMonth());
				document.append("average", vo.getAverage());
				new MongoBasicDao().update("MYZHYBPCYRS", document1, document, true);
			}
			sign = true;
		}
		return false;
	}

	@Override
	public boolean saveHospExpensesToDBOneDay()  throws Exception{
		//住院费月更新
		boolean sign=false;
		String[] month=this.returnNowMonth();
		final StringBuffer buffer = new StringBuffer();
		List<String> tnL = new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		buffer.append("select Sum(b.totCost) AS totCost0, b.mon AS yearAndMonth from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select sum(t" + i + ".TOT_COST) as totCost,");
			buffer.append("to_char(t" + i + ".fee_date, 'yyyy-mm') as mon ");
			buffer.append("from " + tnL.get(i) + " t" + i);
			buffer.append(" where t" + i + ".fee_date between to_date('"
					+ month[0]
					+ " 00:00:00','yyyy-mm-dd HH24:mi:ss') and to_date('"
					+ month[1] + "','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append(" group by t" + i + ".fee_date ");
		}
		buffer.append(" ) b group by b.mon");
		List<MonthlyDashboardVo> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("yearAndMonth")
				.addScalar("totCost0", Hibernate.DOUBLE)
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();

		if (list!=null && list.size() > 0) {
			for (MonthlyDashboardVo vo : list) {
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());// 时间
				Document document = new Document();
				document.append("totCost0", vo.getTotCost0());
				document.append("yearAndMonth", vo.getYearAndMonth());
				new MongoBasicDao().update("MYZHYBPZYFY", document1, document, true);
			}
			sign = true;
		}
		return sign;
	}

	@Override
	public boolean saveSurgeryToDBOneDay() throws Exception {
		// 手术例数保存到DB中 每天
		boolean sign=false;
		String[] month=this.returnNowMonth();
		StringBuffer buffer=new StringBuffer(310);
		buffer.append("select TO_Char(sum(b.num)) AS value,b.dat AS name,b.dept AS dept,TO_CHAR(trunc(sum(b.days)/sum(b.num),2)) AS inhost from ( ");
		buffer.append("select nvl(count(t.op_id) , 0) as num,to_char(t.pre_date,'yyyy-mm') as dat,t.op_doctordept as dept,sysdate-nvl(i.in_date,sysdate-2) as days ");
		buffer.append("from T_OPERATION_APPLY t left join t_inpatient_info_now i on t.clinic_code = i.inpatient_no ");
		buffer.append("where t.stop_flg = 0 and t.del_flg = 0 and t.status = 4 ");
		buffer.append(" and t.pre_date between to_date('" + month[0]
				+ " 00:00:00','yyyy-mm-dd HH24:mi:ss') and to_date('"
				+ month[1] + " 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by t.pre_date,t.op_doctordept,i.in_date ");
		buffer.append(" ) b group by b.dat,b.dept ");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("inhost")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			List<DBObject> userList = new ArrayList<DBObject>();
			for (Dashboard vo : list) {
				Document document1 = new Document();
				document1.append("name", vo.getName());// 时间
				document1.append("dept", vo.getDept());// 科室
				Document document = new Document();
				document.append("dept", vo.getDept());// 科室
				document.append("value", vo.getValue());// 手术例数统计
				document.append("name", vo.getName());// 时间
				document.append("inHost", vo.getInhost());// 住院时间
				new MongoBasicDao().update("MYZHYBPSSLS", document1, document, true);
			}
			/** 手术例数 **/
			sign = true;
		}
		return sign;
	}

	@Override
	public boolean svaeTreatmentToDBOneDay() throws Exception {
		// 治疗数量每天
		boolean sign=false;
		String[] month=this.returnNowMonth();
		 StringBuffer buffer=new StringBuffer(770);
			buffer.append("select b.stat AS stat,TO_Char(sum(b.con)) AS value,b.dat AS name,b.dept AS dept from ( ");
			buffer.append("select  decode(nvl(t.diag_outstate,0),0,'治愈',1,'好转',2,'未愈',3,'死亡',4,'其他') as stat,count(nvl(t.diag_outstate,0)) as con,to_char(t.out_date,'yyyy-mm') as dat,t.dept_name as dept from t_inpatient_info_now t where t.in_state in('O','B') and t.stop_flg=0 and t.del_flg=0 and t.out_date between TO_DATE('"+month[0]+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and TO_DATE('"+month[1]+" 23:59:59','yyyy-mm-dd HH24:mi:ss') group by t.out_date,t.diag_outstate,t.dept_name ");
			buffer.append(") b group by b.dat,b.con,b.stat,b.dept ");
			List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("value").addScalar("name")
					.addScalar("dept").addScalar("stat")
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
			if(list!=null && list.size()>0){
				for(Dashboard vo:list){
//					Bson whileFilter =Filters.and( Filters.eq("name", vo.getName()),Filters.ne("dept", vo.getDept()));
					Document document1 = new Document();
					document1.append("name", vo.getName());//时间
					document1.append("dept", vo.getDept());//科室
					Document document = new Document();
					document.append("stat", vo.getStat());//状态
					document.append("dept", vo.getDept());//科室
					document.append("value", vo.getValue());//治疗数量
					document.append("name", vo.getName());//时间
					new MongoBasicDao().update("MYZHYBPZLSL", document1, document, true);
				}
				/**治疗数量**/
				sign=true;

		}

		return sign;
	}

	@Override
	public boolean svaeWardApplyToDBOneDay() throws Exception {
		// 病床适用率每天
		boolean sign=false;
		String[] month=this.returnNowMonth();
		StringBuffer buffer=new StringBuffer();
		buffer.append("select  TO_CHAR(trunc( sum(c.con)/(select distinct count(1) from t_business_hospitalbed),4)) AS value,c.dat AS name,c.dept AS dept,to_CHar(trunc(sum(c.days)/sum(c.con),2)) as inhost from( ");
		buffer.append("select count(b.dat) as con,b.dat as dat,b.dept as dept,b.days as days from( ");
		buffer.append("select  TO_CHAR(t.in_date,'yyyy-mm') as dat,t.dept_code as dept,sysdate-t.in_date as days from t_inpatient_info_now t where t.in_state='I' and t.in_date between to_date('"
				+ month[0]
				+ " 00:00:00','yyyy-mm-dd HH24:mi:ss') and to_date('"
				+ month[1]
				+ " 23:59:59','yyyy-mm-dd HH24:mi:ss')  group by t.in_date,t.dept_code ");
		buffer.append(") b group by b.dat,b.dept,b.days ");
		buffer.append(") c group by c.dat ,c.dept ");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("inhost")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (Dashboard vo : list) {
				Document document1 = new Document();
				document1.append("name", vo.getName());// 时间
				document1.append("dept", vo.getDept());// 科室
				Document document = new Document();
				document.append("dept", vo.getDept());// 科室
				document.append("value", vo.getValue());// 病床使用率
				document.append("name", vo.getName());// 时间
				document.append("inhost", vo.getInhost());// 在院时间
				new MongoBasicDao().update("MYZHYBPBCSYL", document1, document, true);
			}
			/** 病床适用lv **/
			sign = true;
		}
		return sign;
	}

	@Override
	public boolean saveInpatientInfoToDBOneDay()  throws Exception{
		// 月出院人数每月更新
		boolean sign=false;
		String[] month=this.returnNowMonth();
		final StringBuffer buffer = new StringBuffer(370);
		buffer.append("select TO_Char(Sum(b.totCost)) AS countLeave, b.mon AS yearAndMonth,to_char(floor(sum(b.days)/sum(b.totcost))) as average  from ( ");
		buffer.append("select count(1) as totCost, to_char(t1.in_date, 'yyyy-mm') as mon,trunc(decode(sign(t1.out_date-t1.in_date),-1,t1.in_date + 2,t1.out_date) - t1.in_date,2) as days ");
		buffer.append("from t_inpatient_info_now t1 ");
		buffer.append("where t1.in_state = 'O' and t1.in_date between to_date('"
				+ month[0]
				+ " 00:00:00','yyyy-mm-dd HH24:mi:ss') and to_date('"
				+ month[1]
				+ " 23:59:59','yyyy-mm-dd HH24:mi:ss') group by t1.in_date,t1.out_date ");
		buffer.append(") b group by b.mon");
		List<MonthlyDashboardVo> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("countLeave")
				.addScalar("yearAndMonth")
				.addScalar("average")
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (MonthlyDashboardVo vo : list) {
				// Bson whileFilter =Filters.eq("yearAndMonth",
				// vo.getYearAndMonth());
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());
				Document document = new Document();
				document.append("countLeave", vo.getCountLeave());
				document.append("yearAndMonth", vo.getYearAndMonth());
				document.append("average", vo.getAverage());
				new MongoBasicDao().update("MYZHYBPCYRS", document1, document, true);
			}
			sign = true;
		}
		return sign;
	}

	public String[] returnNowMonth()  throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DATE, 1);
		ca.roll(Calendar.DATE, -1);
		Date date = ca.getTime();
		String lastMonth = sdf.format(date);
		ca.set(GregorianCalendar.DAY_OF_MONTH, 1);
		date = ca.getTime();
		String fristMonth = sdf.format(date);
		String[] month = { fristMonth, lastMonth };
		return month;
	}

	public String[] returnLastMonth(String date)  throws Exception{
		String[] dateArr = date.split("-");
		int lastYear = Integer.parseInt(dateArr[0]) - 1;
		String shMonth;// 上月
		String smMonth = lastYear + "-" + dateArr[1];// 上年
		if ("1".equals(dateArr[1])) {
			shMonth = lastYear + "-12";
		} else {
			int mon = (Integer.parseInt(dateArr[1]) - 1);
			if (mon >= 10) {

				shMonth = dateArr[0] + "-" + mon;
			} else {
				shMonth = dateArr[0] + "-0" + mon;
			}

		}
		String[] monthArr = { date, shMonth, smMonth };

		return monthArr;

	}

	public String[] returnCountMonth(String date)  throws Exception{
		String[] dateArr = { "01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "11", "12" };
		String[] dateArr1 = date.split("-");
		int lastYear = Integer.parseInt(dateArr1[0]) - 1;
		String shMonth;// 上月
		String[] countMonth = new String[13];
		int mon = (Integer.parseInt(dateArr1[1]));
		for (int i = 0; i < 12; i++) {
			if (i < mon) {
				countMonth[i] = dateArr1[0] + "-" + dateArr[i];
			} else {
				countMonth[i] = lastYear + "-" + dateArr[i];
			}
		}
		countMonth[12] = lastYear + "-" + dateArr1[1];

		return countMonth;
	}
	public String[] returnSameSe(String date)  throws Exception{
		String[] same=new String[3];
		same[0]=date.substring(0, 7);
		same[1]=DateUtils.formatDateY_M(DateUtils.addMonth(DateUtils.parseDateY_M(same[0]), -1));
		same[2]=DateUtils.formatDateY_M(DateUtils.addYear(DateUtils.parseDateY_M(same[0]), -1));
		return same;
	}
	@Override
	public boolean svaeWardToDBOneDay()  throws Exception{
		StringBuffer buffer = new StringBuffer();
		buffer.append("");
		return false;
	}

	@Override
	public Boolean isCollection(String name)  throws Exception{
		return new MongoBasicDao().isCollection(name);
	}
	/**  
	 * 
	 * 住院费用mongdb没有数据时查分区
	 * @Author: wangshujuan
	 * @version: V1.0
	 *
	 */
	@Override
	public List<MonthlyDashboardVo> queryHospExpenses2(List<String> tnL,String deptName, String endDate) throws Exception {
		if(tnL==null||tnL.size()==0){
			return new ArrayList<MonthlyDashboardVo>();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append("select sum(b.totCost) totCost0,b.yearAndMonth from ( ");
		
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append("UNION ");
			}
			sb.append("select sum(t"+i+".TOT_COST) as totCost,");
			sb.append("to_char(t"+i+".fee_date, 'yyyy-mm') as yearAndMonth ");
			sb.append("from "+tnL.get(i)+" t"+i);
			sb.append(" where t"+i+".fee_date>=to_date('"+deptName+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+endDate+"','yyyy-mm-dd HH24:mi:ss') ");
			sb.append(" group by t"+i+".fee_date ");
		}
			sb.append(" ) b group by b.yearAndMonth");
			List<MonthlyDashboardVo> voList =namedParameterJdbcTemplate.query(sb.toString(),new HashMap<String, Object>(0),new RowMapper<MonthlyDashboardVo>(){
				@Override
				public MonthlyDashboardVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					MonthlyDashboardVo vo = new MonthlyDashboardVo();
					vo.setTotCost0(rs.getDouble("totCost0"));
					vo.setYearAndMonth(rs.getString("yearAndMonth"));
					return vo;
				}});
			return voList ;
	}
	/**  
	 * 
	 * <p> 获取业务表中最大及最小时间 </p>
	 * @Author:zhuxiaolu
	 * @CreateDate: 2016年11月29日 下午03:58:02 
	 * @Modifier: zhuxiaolu
	 * @ModifyDate: 2016年11月29日 下午03:58:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public StatVo findMaxMin1()  throws Exception{
		final String sql = "SELECT MAX(mn.APPROVE_DATE) AS eTime ,MIN(mn.APPROVE_DATE) AS sTime FROM T_DRUG_OUTSTORE_NOW mn";
	     List<StatVo> list =namedParameterJdbcTemplate.query(sql, new RowMapper<StatVo>() {
				@Override
				public StatVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					StatVo vo  = new StatVo();
					vo.setsTime(rs.getDate("sTime"));
					vo.seteTime(rs.getDate("eTime"));
					return vo;
				}
			});
		return list.get(0); 
	}

	@Override
	public List<Dashboard> queryWardApplyFromOracle(List<String> tnL,
			String deptName, String startTime, String endTime)  throws Exception{
		if (tnL == null || tnL.size() < 1) {
			return new ArrayList<Dashboard>();
		}
		final StringBuffer sb = new StringBuffer(1400);
		sb.append(" select TO_CHAR(trunc(sum(c.con) /(select distinct count(1) from t_business_hospitalbed),4)) AS value,c.dat AS name,c.dept AS dept,"
				+ "to_char(trunc(sum(c.days) / sum(c.con), 2)) as inhost from (select count(b.dat) as con,b.dat as dat,b.dept as dept,b.days as days from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				sb.append(" union all ");
			}
			sb.append(" select TO_CHAR(t").append(i)
					.append(".in_date, 'yyyy-mm') as dat,t").append(i)
					.append(".dept_code as dept,sysdate - t").append(i)
					.append(".in_date as days ").append(" from ")
					.append(HisParameters.HISPARSCHEMAHISUSER)
					.append(tnL.get(i)).append("  t").append(i)
					.append(" where t").append(i).append(".in_state = 'I' ");
			if (StringUtils.isNotBlank(deptName)) {
				sb.append(" and t").append(i)
						.append(".dept_name='" + deptName + "'");
			}
			if (StringUtils.isNotBlank(startTime)) {
				sb.append(" and t")
						.append(i)
						.append(".in_date>to_date('" + startTime
								+ "','yyyy-mm-dd')");
			}
			if (StringUtils.isNotBlank(endTime)) {
				sb.append(" and t")
						.append(i)
						.append(".in_date<to_date('" + endTime
								+ "','yyyy-mm-dd')");
			}
			sb.append(" group by t").append(i).append(".in_date, t").append(i)
					.append(".dept_code");
		}
		sb.append(") b group by b.dat, b.dept, b.days) c group by c.dat, c.dept");
		return super
				.getSession()
				.createSQLQuery(sb.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("inhost")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
	}

	@Override
	public List<MonthlyDashboardVo> queryInpatientInfoFromOracle(
			List<String> tnL, String deptName, String startTime, String endTime)  throws Exception{
		if (tnL == null || tnL.size() < 1) {
			return new ArrayList<MonthlyDashboardVo>();
		}
		final StringBuffer buffer = new StringBuffer(370);
		buffer.append("select TO_Char(Sum(b.totCost)) AS countLeave, b.mon AS yearAndMonth,to_char(floor(b.days)) AS average  from ( ");
		
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
		buffer.append("select count(1) as totCost, to_char(t").append(i)
		.append(".in_date, 'yyyy-mm' ) as mon,trunc(decode(sign(t").append(i)
		.append(".out_date-t").append(i).append(".in_date),-1,t").append(i)
		.append(".in_date + 2,t").append(i).append(".out_date) - t")
		.append(i).append(".in_date,2) as days ").append(" from ")
		.append(HisParameters.HISPARSCHEMAHISUSER)
		.append(tnL.get(i)).append(" t"+i).append(" where  t").append(i).append(".in_state = 'O'");
		
		if (StringUtils.isNotBlank(deptName)) {
			buffer.append(" and t").append(i)
					.append(".dept_name='" + deptName + "'");
		}
		if (StringUtils.isNotBlank(startTime)) {
			buffer.append(" and t")
					.append(i)
					.append(".in_date>to_date('" + startTime
							+ "','yyyy-mm-dd')");
		}
		if (StringUtils.isNotBlank(endTime)) {
			buffer.append(" and t")
					.append(i)
					.append(".in_date<to_date('" + endTime
							+ "','yyyy-mm-dd')");
		}
		buffer.append(" group by t").append(i).append(".in_date, t").append(i)
		.append(".out_date");
	}
		buffer.append(") b group by b.mon,b.days");
		return super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("countLeave")
				.addScalar("yearAndMonth")
				.addScalar("average")
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();
	}
/*************************************************************************************************************************************/
	/**
	 * @see 新住院费用
	 * @param tnL
	 * @param begin
	 * @param end
	 * @param hours
	 */
	@Override
	public void initHospExpensesToDBDay(List<String> tnL, String begin,
			String end, Integer hours)  throws Exception{
		StringBuffer buffer=new StringBuffer(200);
		buffer.append("select Sum(b.totCost) AS totCost0,to_char(b.mon, 'yyyy-mm-dd') AS yearAndMonth from ( ");
		for (int i = 0; i < tnL.size(); i++) {
			if (i != 0) {
				buffer.append(" union all ");
			}
			buffer.append("select t"+ i +".TOT_COST as totCost,");
			buffer.append("t" + i + ".fee_date as mon ");
			buffer.append("from " + tnL.get(i) + " t" + i);
			buffer.append(" where t" + i+ ".fee_date >=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t" + i+ ".fee_date <to_date('"+end+"','yyyy-mm-dd HH24:mi:ss')");
		}
		buffer.append(" ) b group by b.mon");
		List<MonthlyDashboardVo> list = super.getSession().createSQLQuery(buffer.toString())
				.addScalar("yearAndMonth").addScalar("totCost0", Hibernate.DOUBLE)
				.setResultTransformer(Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (MonthlyDashboardVo vo : list) {
				if(vo.getTotCost0()!=null){
					    BasicDBObject bdObject = new BasicDBObject();
						bdObject.append("yearAndMonth", vo.getYearAndMonth());
						DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBPZYFY", bdObject);
						DBObject dbCursor;
						Double dou=vo.getTotCost0();
						while (cursor.hasNext()) {
							dbCursor = cursor.next();
							dou+= (Double) dbCursor.get("totCost0");
						}
						vo.setTotCost0(dou);
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());// 时间
				Document document = new Document();
				document.append("totCost0", vo.getTotCost0());
				document.append("yearAndMonth", vo.getYearAndMonth());
				new MongoBasicDao().update("MYZHYBPZYFY", document1, document, true);
				}
				}
			}
		
	}
	/**
	 * @see 月分析仪表盘手术例数
	 */
	@Override
	public void initSurgeryToDBDay(List<String> tnL, String begin,
			String end, Integer hours)  throws Exception{
		StringBuffer buffer=new StringBuffer(400);
		buffer.append("select TO_Char(sum(b.num)) AS value,b.dat AS name,b.dept AS dept,TO_CHAR(trunc(sum(b.days)/sum(b.num),2)) AS inhost from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			buffer.append("select nvl(count(t"+i+".op_id) , 0) as num,to_char(t"+i+".pre_date,'yyyy-mm') as dat,t"+i+".op_doctordept as dept,sysdate-nvl(i.in_date,sysdate-2) as days ");
			buffer.append("from "+tnL.get(i)+" t"+i+" left join t_inpatient_info_now i on t"+i+".clinic_code = i.inpatient_no ");
			buffer.append(" where t"+i+".stop_flg = 0 and t"+i+".del_flg = 0 and t"+i+".status = 4 ");
			buffer.append(" and t"+i+".pre_date between to_date('" + begin
					+ "','yyyy-mm-dd HH24:mi:ss') and to_date('"
					+ end + "','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by t"+i+".pre_date,t"+i+".op_doctordept,i.in_date ");
		}
		buffer.append(" ) b group by b.dat,b.dept ");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("inhost")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			List<DBObject> userList = new ArrayList<DBObject>();
			for (Dashboard vo : list) {
				if(vo.getValue()!=null&&vo.getInhost()!=null){
						BasicDBObject bdObject = new BasicDBObject();
						bdObject.append("name", vo);
						DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBPSSLS", bdObject);
						DBObject dbCursor;
						Integer iValue=Integer.parseInt(vo.getValue());
						Double dInhost=Double.parseDouble(vo.getInhost());
							while (cursor.hasNext()) {
								Dashboard voOne = new Dashboard();
								dbCursor = cursor.next();
								iValue+=Integer.parseInt((String) dbCursor.get("value"));
								dInhost+=Double.parseDouble((String) dbCursor.get("inhost"));
							}
						vo.setValue(iValue.toString());
						vo.setInhost(dInhost.toString());
						Document document1 = new Document();
						document1.append("name", vo.getName());// 时间
						document1.append("dept", vo.getDept());// 科室
						Document document = new Document();
						document.append("dept", vo.getDept());// 科室
						document.append("value", vo.getValue());// 手术例数统计
						document.append("name", vo.getName());// 时间
						document.append("inHost", vo.getInhost());// 住院时间
						new MongoBasicDao().update("MYZHYBPSSLS", document1, document, true);
			 }
			}
		}
	}

	@Override
	public void initTreatmentToDBDay(List<String> tnL, String begin,
			String end, Integer hours)  throws Exception{
		StringBuffer buffer=new StringBuffer(770);
		buffer.append("select b.stat AS stat,TO_Char(sum(b.con)) AS value,b.dat AS name,b.dept AS dept from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			buffer.append("select  decode(nvl(t"+i+".diag_outstate,0),0,'治愈',1,'好转',2,'未愈',3,'死亡',4,'其他') as stat,");
			buffer.append("count(nvl(t"+i+".diag_outstate,0)) as con,to_char(t"+i+".out_date,'yyyy-mm') as dat,t"+i+".dept_name as dept ");
			buffer.append("from "+tnL.get(i)+" t"+i+" where t"+i+".in_state in('O','B') and t"+i+".stop_flg=0 and t"+i+".del_flg=0 ");
			buffer.append("and t"+i+".out_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".out_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by t"+i+".out_date,t"+i+".diag_outstate,t"+i+".dept_name ");
		}
		buffer.append(") b group by b.dat,b.con,b.stat,b.dept ");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("stat")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			List<DBObject> userList = new ArrayList<DBObject>();
			for (Dashboard vo : list) {
				if(vo.getValue()!=null){
					BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("name", vo.getName());
					bdObject.append("dept", vo.getDept());
					DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBPZLSL", bdObject);
					DBObject dbCursor;
					Integer value=Integer.parseInt(vo.getValue());
					while (cursor.hasNext()) {
						dbCursor = cursor.next();
						value+=Integer.parseInt((String) dbCursor.get("value"));
					}
					vo.setValue(value.toString());
				Document document1 = new Document();
				document1.append("name", vo.getName());// 时间
				document1.append("dept", vo.getDept());// 科室
				Document document = new Document();
				document.append("stat", vo.getStat());// 状态
				document.append("dept", vo.getDept());// 科室
				document.append("value", vo.getValue());// 治疗数量
				document.append("name", vo.getName());// 时间
				new MongoBasicDao().update("MYZHYBPZLSL", document1, document, true);
				}
			}
		}
	}

	@Override
	public void initWardApplyToDBDay(List<String> tnL, String begin,
			String end, Integer hours)  throws Exception{
		StringBuffer buffer=new StringBuffer();
		buffer.append("select TO_CHAR(trunc(sum(c.con)/(select distinct count(1) from t_business_hospitalbed),2)) AS value,c.dat AS name,c.dept AS dept,to_CHar(trunc(sum(c.days)/sum(c.con),2)) as inhost from( ");
		buffer.append("select count(b.dat) as con,b.dat as dat,b.dept as dept,b.days as days from( ");
		 for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
			 buffer.append("select  TO_CHAR(t"+i+".in_date,'yyyy-mm') as dat,t"+i+".dept_code as dept,sysdate-t"+i+".in_date as days from "+tnL.get(i)+" t"+i+" where t"+i+".in_state='I' and t"+i+".in_date between to_date('"
					+ begin
					+ "','yyyy-mm-dd HH24:mi:ss') and to_date('"
					+ end
					+ "','yyyy-mm-dd HH24:mi:ss')  group by t"+i+".in_date,t"+i+".dept_code ");
		 }
		buffer.append(") b group by b.dat,b.dept,b.days ");
		buffer.append(") c group by c.dat ,c.dept ");
		List<Dashboard> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("value")
				.addScalar("name")
				.addScalar("dept")
				.addScalar("inhost")
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (Dashboard vo : list) {
				if(vo.getValue()!=null&&vo.getInhost()!=null){
				  BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("dept", vo.getDept());
					bdObject.append("name", vo);
					DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBPBCSYL", bdObject);
					DBObject dbCursor;
					Double dou=Double.parseDouble(vo.getValue());
					Double inhost=Double.parseDouble(vo.getInhost());
					while (cursor.hasNext()) {
						Dashboard voOne = new Dashboard();
						dbCursor = cursor.next();
						dou+=Double.parseDouble((String) dbCursor.get("value"));
						inhost+=(Double.parseDouble((String) dbCursor.get("inhost")));
					   }
					vo.setValue(dou.toString());
					vo.setInhost(inhost.toString());
				Document document1 = new Document();
				document1.append("name", vo.getName());// 时间
				document1.append("dept", vo.getDept());// 科室
				Document document = new Document();
				document.append("dept", vo.getDept());// 科室
				document.append("value", vo.getValue());// 病床使用率
				document.append("name", vo.getName());// 时间
				document.append("inhost", vo.getInhost());// 在院时间
				new MongoBasicDao().update("MYZHYBPBCSYL", document1, document, true);
				}
			 }
			}
		}

	@Override
	public void InitInpatientInfoToDBOneDay(List<String> tnL, String begin,
			String end, Integer hours)  throws Exception{
		final StringBuffer buffer = new StringBuffer(370);
		buffer.append("select TO_Char(Sum(b.totCost)) AS countLeave, b.mon AS yearAndMonth,to_char(floor(sum(b.days)/sum(b.totcost))) as average  from ( ");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" union all ");
			}
		buffer.append("select count(1) as totCost, to_char(t"+i+".in_date, 'yyyy-mm') as mon,trunc(decode(sign(t"+i+".out_date-t"+i+".in_date),-1,t"+i+".in_date + 2,t"+i+".out_date) - t"+i+".in_date,2) as days ");
		buffer.append("from "+tnL.get(i)+" t"+i+" ");
		buffer.append("where t"+i+".in_state = 'O' and t"+i+".in_date between to_date('"
				+ begin
				+ "','yyyy-mm-dd HH24:mi:ss') and to_date('"
				+ end
				+ "','yyyy-mm-dd HH24:mi:ss') group by t"+i+".in_date,t"+i+".out_date ");
		}
		buffer.append(") b group by b.mon");
		
		List<MonthlyDashboardVo> list = super
				.getSession()
				.createSQLQuery(buffer.toString())
				.addScalar("countLeave")
				.addScalar("yearAndMonth")
				.addScalar("average")
				.setResultTransformer(
						Transformers.aliasToBean(MonthlyDashboardVo.class))
				.list();
		if (list!=null && list.size() > 0) {
			for (MonthlyDashboardVo vo : list) {
				if(vo.getCountLeave()!=null&&vo.getAverage()!=null){
				BasicDBObject bdObject = new BasicDBObject();
				List<MonthlyDashboardVo> list1 = new ArrayList<MonthlyDashboardVo>();
					bdObject.append("yearAndMonth", vo.getYearAndMonth());
					DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBPCYRS", bdObject);
					DBObject dbCursor;
					Integer coun=Integer.parseInt(vo.getCountLeave());
					Double aver=Double.parseDouble(vo.getAverage());
					while (cursor.hasNext()) {
						dbCursor = cursor.next();
						coun+=Integer.parseInt((String) dbCursor.get("countLeave"));
						aver+=Double.parseDouble((String) dbCursor.get("average"));
					}
					vo.setCountLeave(coun.toString());
					vo.setAverage(aver.toString());
				Document document1 = new Document();
				document1.append("yearAndMonth", vo.getYearAndMonth());
				Document document = new Document();
				document.append("countLeave", vo.getCountLeave());
				document.append("yearAndMonth", vo.getYearAndMonth());
				document.append("average", vo.getAverage());
				new MongoBasicDao().update("MYZHYBPCYRS", document1, document, true);
				}
			}
		}
	}
/*********************月*********************************************************************************************/
	@Override
	public void initHospExpensesToDBDay(String begin, String end)  throws Exception{
		List<String> list=reMonthDay(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			    temp=st.substring(0,7);
				bdObject.append("yearAndMonth", st);
				DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBPZYFY", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					Dashboard voOne=new  Dashboard();
					 dbCursor = cursor.next();
					 Double  value = (Double) dbCursor.get("totCost0") ;//费用
			        temp1=temp;
					 if(map.containsKey(temp1)){//如果key存在 比较name
						dou=map.get(temp1);
						dou+=value;
						map.put(temp1, dou);
					 }else{//如果key不存在   添加到map1中
				    	map.put(temp1, value);
				    }
				  }
				}
			for(String key:map.keySet()){
				   Document doucment1=new Document();
				   doucment1.append("yearAndMonth", key);
				    Document document = new Document();
					document.append("totCost0",map.get(key));
					document.append("yearAndMonth", key);
				new MongoBasicDao().update("MYZHYBPZYFY", doucment1, document, true);
				}
		
		}
	
	}
/**********************年***********************************************************************************************/
	@Override
	public void initHospExpensesYear(String begin, String end) throws Exception {
		List<String> list=reYearMonth(begin,end,new ArrayList<String>());
		if(list!=null && list.size()>0){
		BasicDBObject bdObject = new BasicDBObject();
		Map<String,Double> map=new HashMap<String,Double>();
		String temp;//月数据
		Double dou;
		String temp1;//key
		for(String st:list){//获取时间段天数
			    temp=st.substring(0,4);
				bdObject.append("yearAndMonth", st);
				DBCursor cursor = new MongoBasicDao().findAlldata("MYZHYBPZYFY", bdObject);
				DBObject dbCursor;
				while(cursor.hasNext()){
					Dashboard voOne=new  Dashboard();
					 dbCursor = cursor.next();
					 Double  value = (Double) dbCursor.get("totCost0") ;//费用
			        temp1=temp;
					 if(map.containsKey(temp1)){//如果key存在 比较name
						dou=map.get(temp1);
						dou+=value;
						map.put(temp1, dou);
					 }else{//如果key不存在   添加到map1中
				    	map.put(temp1, value);
				    }
				  }
				}
			for(String key:map.keySet()){
				   Document doucment1=new Document();
				   doucment1.append("yearAndMonth", key);
				    Document document = new Document();
					document.append("totCost0",map.get(key));
					document.append("yearAndMonth", key);
				new MongoBasicDao().update("MYZHYBPZYFY", doucment1, document, true);
				}
		
		}
	
		
	}
/**********************时间计算**********************************************************************************************/
	//获取日期每个月的每天
	public List<String> reMonthDay(String begin,String end,List<String> list) throws Exception{
		 if(begin!=null){
			 Date date;
			 Date endTime;
			try {
				 date = sd.parse(begin);
				 endTime=sd.parse(end);
				 begin=sdf.format(date);
				 String[] dateArr=begin.split("-");
				 Calendar ca=Calendar.getInstance();
				 ca.set(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1])-1,Integer.parseInt(dateArr[2]));
				if(date.getTime()>=endTime.getTime()){
					begin=sdf.format(ca.getTime());
					list.add(begin);
					return list;
				}else{
					begin=sdf.format(ca.getTime());
					ca.add(Calendar.DATE, 1);
					list.add(begin);
					begin=sd.format(ca.getTime());
					return reMonthDay(begin,end,list);
				}
				
			} catch (ParseException e) {
				return list;
			}
		 }else{
			 return new ArrayList<String>();
		 }
	}
	//获取每年的每月
	public List<String> reYearMonth(String begin,String end,List<String> list) throws Exception{
		 if(begin!=null){
			 Date date;
			 Date endTime;
			try {
				 date = sd.parse(begin);
				 endTime=sd.parse(end);
				 begin=sdfMonth.format(date);//
				 String[] dateArr=begin.split("-");
				 Calendar ca=Calendar.getInstance();
				 ca.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
				 ca.set(Calendar.MONTH,Integer.parseInt(dateArr[1])-1);
				if(date.getTime()>=endTime.getTime()){
					begin=sdfMonth.format(ca.getTime());
					list.add(begin);
					return list;
				}else{
					begin=sdfMonth.format(ca.getTime());
					ca.add(Calendar.MONTH, 1);
					list.add(begin);
					begin=sd.format(ca.getTime());
					return reYearMonth(begin,end,list);
				}
			}catch (ParseException e) {
				return list;
			}
		 }else{
			 return new ArrayList<String>();
		 }
	}

}
