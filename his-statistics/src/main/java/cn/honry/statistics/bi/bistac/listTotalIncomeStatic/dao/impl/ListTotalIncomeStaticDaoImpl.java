package cn.honry.statistics.bi.bistac.listTotalIncomeStatic.dao.impl;

import java.math.BigDecimal;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.model.Filters;

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.utli.ZoneManageUtil;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.dao.ListTotalIncomeStaticDao;
import cn.honry.statistics.bi.bistac.listTotalIncomeStatic.vo.ListTotalIncomeStaticVo;
import cn.honry.statistics.bi.bistac.monthlyDashboard.vo.Dashboard;
import cn.honry.statistics.bi.bistac.outpatientUseMedic.vo.OutpatientUseMedicVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("listTotalIncomeStaticDao")
@SuppressWarnings({ "all" })
public class ListTotalIncomeStaticDaoImpl extends HibernateEntityDao<ListTotalIncomeStaticVo> implements ListTotalIncomeStaticDao{
	SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfMonth=new SimpleDateFormat("yyyy-MM");
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	private MongoBasicDao mbDao =null;
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**  
	 * 
	 * 总收入情况统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月4日 下午4:09:43 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月4日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ListTotalIncomeStaticVo queryVo(List<String> tnLs,final String date) {
		if(tnLs==null||tnLs.size()<0){
			return new ListTotalIncomeStaticVo();
		}
		final StringBuffer sb = new StringBuffer();
		sb.append(" select ");
		sb.append(" (select nvl(sum(t.tot_cost),0) from ").append(tnLs.get(0)).append(" t ").append(" where ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" trunc(t.fee_date,'dd') = to_date(:date,'yyyy-MM-dd') and ");
		}
		sb.append(" t.drug_flag = '1' and t.trans_type = 1 and t.pay_flag = 1 and t.cancel_flag = 1 and t.stop_flg = 0 and t.del_flg = 0) as cost1,");
		
		sb.append(" (select sum(cost) from ( ");
		sb.append(" select nvl(sum(t.tot_cost),0) as cost from ").append(tnLs.get(0)).append(" t ").append(" where t.fee_code in (select t.code_encode from t_business_dictionary t where t.code_name like '%治疗费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(t.fee_date,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and t.trans_type=1 and t.pay_flag=1 and t.cancel_flag=1 and t.stop_flg=0 and t.del_flg=0 ");
		sb.append(" union all ");
		sb.append(" select nvl(sum(tt.tot_cost),0) as cost from ").append(tnLs.get(1)).append(" tt ").append(" where tt.fee_code in(select t.code_encode from t_business_dictionary t where t.code_name like '%治疗费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(tt.BALANCE_DATE,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and tt.trans_type=1  and tt.stop_flg=0 and tt.del_flg=0)) as cost2,");
		
		sb.append(" (select sum(cost) from ( ");
		sb.append(" select nvl(sum(t.tot_cost),0) as cost from ").append(tnLs.get(0)).append(" t ").append(" where t.fee_code in (select t.code_encode from t_business_dictionary t where t.code_name like '%检查费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(t.fee_date,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and t.trans_type=1 and t.pay_flag=1 and t.cancel_flag=1 and t.stop_flg=0 and t.del_flg=0 ");
		sb.append(" union all ");
		sb.append(" select nvl(sum(tt.tot_cost),0) as cost from ").append(tnLs.get(1)).append(" tt ").append(" where tt.fee_code in(select t.code_encode from t_business_dictionary t where t.code_name like '%检查费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(tt.BALANCE_DATE,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and tt.trans_type=1  and tt.stop_flg=0 and tt.del_flg=0)) as cost3,");
		
		sb.append(" (select sum(cost) from ( ");
		sb.append(" select nvl(sum(t.tot_cost),0) as cost from ").append(tnLs.get(0)).append(" t ").append(" where t.fee_code in (select t.code_encode from t_business_dictionary t where t.code_name like '%床位费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(t.fee_date,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and t.trans_type=1 and t.pay_flag=1 and t.cancel_flag=1 and t.stop_flg=0 and t.del_flg=0 ");
		sb.append(" union all ");
		sb.append(" select nvl(sum(tt.tot_cost),0) as cost from ").append(tnLs.get(1)).append(" tt ").append(" where tt.fee_code in(select t.code_encode from t_business_dictionary t where t.code_name like '%床位费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(tt.BALANCE_DATE,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and tt.trans_type=1  and tt.stop_flg=0 and tt.del_flg=0)) as cost4,");
		
		sb.append(" (select sum(cost) from ( ");
		sb.append(" select nvl(sum(t.tot_cost),0) as cost from ").append(tnLs.get(0)).append(" t ").append(" where t.fee_code in (select t.code_encode from t_business_dictionary t where t.code_name like '%化验费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(t.fee_date,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and t.trans_type=1 and t.pay_flag=1 and t.cancel_flag=1 and t.stop_flg=0 and t.del_flg=0 ");
		sb.append(" union all ");
		sb.append(" select nvl(sum(tt.tot_cost),0) as cost from ").append(tnLs.get(1)).append(" tt ").append(" where tt.fee_code in(select t.code_encode from t_business_dictionary t where t.code_name like '%化验费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(tt.BALANCE_DATE,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and tt.trans_type=1  and tt.stop_flg=0 and tt.del_flg=0)) as cost5,");
		
		sb.append(" (select sum(cost) from ( ");
		sb.append(" select nvl(sum(t.tot_cost),0) as cost from ").append(tnLs.get(0)).append(" t ").append(" where t.fee_code in (select t.code_encode from t_business_dictionary t where t.code_name like '%手术费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(t.fee_date,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and t.trans_type=1 and t.pay_flag=1 and t.cancel_flag=1 and t.stop_flg=0 and t.del_flg=0 ");
		sb.append(" union all ");
		sb.append(" select nvl(sum(tt.tot_cost),0) as cost from ").append(tnLs.get(1)).append(" tt ").append(" where tt.fee_code in(select t.code_encode from t_business_dictionary t where t.code_name like '%手术费%') ");
		if(StringUtils.isNotBlank(date)){
			sb.append(" and trunc(tt.BALANCE_DATE,'dd') = to_date(:date,'yyyy-MM-dd') ");
		}
		sb.append(" and tt.trans_type=1  and tt.stop_flg=0 and tt.del_flg=0)) as cost6");
		
		sb.append(" from dual");
		ListTotalIncomeStaticVo vo = (ListTotalIncomeStaticVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public ListTotalIncomeStaticVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sb.toString());
					queryObject.addScalar("cost1",Hibernate.DOUBLE);
					queryObject.addScalar("cost2",Hibernate.DOUBLE);
					queryObject.addScalar("cost3",Hibernate.DOUBLE);
					queryObject.addScalar("cost4",Hibernate.DOUBLE);
					queryObject.addScalar("cost5",Hibernate.DOUBLE);
					queryObject.addScalar("cost6",Hibernate.DOUBLE);
					queryObject.setParameter("date", date);
				return (ListTotalIncomeStaticVo) queryObject.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).uniqueResult();
			}
		});
		return vo;
		
		
	}
	@Override
	public ListTotalIncomeStaticVo findFeeMaxMin1() {
		final String sql = "SELECT MAX(mn.REG_DATE) AS eTime1 ,MIN(mn.REG_DATE) AS sTime1 FROM T_OUTPATIENT_FEEDETAIL_NOW mn";
		ListTotalIncomeStaticVo vo = (ListTotalIncomeStaticVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public ListTotalIncomeStaticVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime1",Hibernate.DATE).addScalar("sTime1",Hibernate.DATE);
				return (ListTotalIncomeStaticVo) queryObject.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).uniqueResult();
			}
		});
		return vo;
	}
	@Override
	public ListTotalIncomeStaticVo findFeeMaxMin2() {
		final String sql = "SELECT MAX(mn.CREATETIME) AS eTime2 ,MIN(mn.CREATETIME) AS sTime2 FROM T_INPATIENT_FEEINFO_NOW mn";
		ListTotalIncomeStaticVo vo = (ListTotalIncomeStaticVo) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public ListTotalIncomeStaticVo doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql.toString());
				queryObject.addScalar("eTime2",Hibernate.DATE).addScalar("sTime2",Hibernate.DATE);
				return (ListTotalIncomeStaticVo) queryObject.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).uniqueResult();
			}
		});
		return vo;
	}

	@Override
	public List<MinfeeStatCode> queryFeeName() {
		final String sql = "select distinct t.fee_stat_code as feeStatCode,t.fee_stat_name as feeStatName from t_charge_minfeetostat t where t.report_code = 'MZ01' or t.report_code = 'ZY01'";
		List<MinfeeStatCode> VinpatientApplyoutList =  namedParameterJdbcTemplate.query(sql.toString(),new RowMapper<MinfeeStatCode>() {
			@Override
			public MinfeeStatCode mapRow(ResultSet rs, int rowNum) throws SQLException {
				MinfeeStatCode vo = new MinfeeStatCode();
				vo.setFeeStatName(rs.getString("feeStatName"));
				vo.setFeeStatCode(rs.getString("feeStatCode"));
				return vo;
			}
		});
		return VinpatientApplyoutList;
	}
	@Override
	public List<ListTotalIncomeStaticVo> queryTotalCount(String begin, String end,String dateSign) {
			mbDao = new MongoBasicDao();
			String tableName;//查询表
			if(begin==null){
				return new ArrayList<ListTotalIncomeStaticVo>();
			}
			String[] date=begin.split("-");
			String date1;//条件
			
			if("1".equals(dateSign)){
				tableName="YZJEZSRQKTJYEAR";
				date1=date[0];
			}else if("2".equals(dateSign)){
				tableName="YZJEZSRQKTJMONTH";
				date1=date[0]+"-"+date[1];
			}else{
				tableName="YZJEZSRQKTJDAY";
				date1=begin;
			}
			BasicDBObject bdObject = new BasicDBObject();
			bdObject.append("date1", date1);
			DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
			DBObject dbCursor;
			List<ListTotalIncomeStaticVo> list1=new ArrayList<ListTotalIncomeStaticVo>();
			
			while(cursor.hasNext()){
				ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("value");
				 String name = (String) dbCursor.get("name");
				voOne.setValue(value);
				voOne.setName(name);
				list1.add(voOne);
			}
				return list1;
	}
	@Override
	public boolean initTotalMongYear() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,");
			//年统计
			buffer.append("to_char(n"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 List<DBObject> userList = new ArrayList<DBObject>();
		 
		for(ListTotalIncomeStaticVo vo:list){
			Document doucment1=new Document();
			doucment1.append("date1", vo.getDate1());
			doucment1.append("name", vo.getName());
			Document document = new Document();
			document.append("value", vo.getValue().toString());
			document.append("name", vo.getName());
			document.append("date1", vo.getDate1());
			mbDao.update("YZJEZSRQKTJYEAR", doucment1, document, true);
		}
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongMonth() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
//		mbDao.deleteData("YZJEZSRQKTJMONTH");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 List<DBObject> userList = new ArrayList<DBObject>();
		 
		for(ListTotalIncomeStaticVo vo:list){
			Document doucment1=new Document();
			doucment1.append("date1", vo.getDate1());
			doucment1.append("name", vo.getName());
			Document document = new Document();
			document.append("value", vo.getValue().toString());
			document.append("name", vo.getName());
			document.append("date1", vo.getDate1());
			mbDao.update("YZJEZSRQKTJMONTH", doucment1, document, true);
		}
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongDate() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 List<DBObject> userList = new ArrayList<DBObject>();
		 
		for(ListTotalIncomeStaticVo vo:list){
			Document doucment1=new Document();
			doucment1.append("date1", vo.getDate1());
			doucment1.append("name", vo.getName());
			Document document = new Document();
			document.append("value", vo.getValue());
			document.append("name", vo.getName());
			document.append("date1", vo.getDate1());
			mbDao.update("YZJEZSRQKTJDAY", doucment1, document, true);
			
		}
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongYearOneDay() {
		mbDao = new MongoBasicDao();
		String year=new SimpleDateFormat("yyyy").format(System.currentTimeMillis());//获取当前年
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
			buffer.append("and t"+i+".fee_date >= to_date('"+year+"-01-01 00:00:00','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<= to_date('"+year+"-12-31 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,");
			//年统计
			buffer.append("to_char(n"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date >= to_date('"+year+"-01-01 00:00:00','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date <=to_date('"+year+"-12-31 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")//
											.addScalar("code")//费用汇总
											.addScalar("date1")//日期
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 
		for(ListTotalIncomeStaticVo vo:list){
			Document doucment1=new Document();
			doucment1.append("date1", vo.getDate1());
			doucment1.append("name", vo.getName());
			Document document = new Document();
			document.append("value", vo.getValue().toString());
			document.append("name", vo.getName());
			document.append("date1", vo.getDate1());
			mbDao.update("YZJEZSRQKTJYEAR", doucment1, document, true);
		}
		
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongMonthOneDay() {
		mbDao = new MongoBasicDao();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca=Calendar.getInstance();
		ca.set(Calendar.DATE, 1);
		ca.roll(Calendar.DATE, -1);
		Date date=ca.getTime();
		String lastMonth=sdf.format(date);
		 ca.set(GregorianCalendar.DAY_OF_MONTH, 1); 
		 date=ca.getTime();
		 String fristMonth=sdf.format(date);
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("and t"+i+".fee_date >= to_date('"+fristMonth+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <= to_date('"+lastMonth+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date >= to_date('"+fristMonth+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date <= to_date('"+lastMonth+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 
			for(ListTotalIncomeStaticVo vo:list){
				Document doucment1=new Document();
				doucment1.append("date1", vo.getDate1());
				doucment1.append("name", vo.getName());
				Document document = new Document();
				document.append("value", vo.getValue().toString());
				document.append("name", vo.getName());
				document.append("date1", vo.getDate1());
				mbDao.update("YZJEZSRQKTJMONTH", doucment1, document, true);
			}
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongDateOneDay() {
		mbDao = new MongoBasicDao();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("and t"+i+".fee_date >= to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and  t"+i+".fee_date<= to_date('"+date+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date >= to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date to_date('"+date+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 
			for(ListTotalIncomeStaticVo vo:list){
				Document doucment1=new Document();
				doucment1.append("date1", vo.getDate1());
				doucment1.append("name", vo.getName());
				Document document = new Document();
				document.append("value", vo.getValue());
				document.append("name", vo.getName());
				document.append("date1", vo.getDate1());
				mbDao.update("YZJEZSRQKTJDAY", doucment1, document, true);
			}
			
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongYearWithDept() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from (");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" left join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(n"+i+".tot_cost) AS value,");
			//年统计
			buffer.append("to_char(n"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" left join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by n"+i+".fee_date ");
		}
		buffer.append(") b group by b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
//		 List<DBObject> userList = new ArrayList<DBObject>();
		 
		for(ListTotalIncomeStaticVo vo:list){
			Document doucment1=new Document();
			doucment1.append("date1", vo.getDate1());
			Document document = new Document();
			document.append("value", vo.getValue().toString());
			document.append("date1", vo.getDate1());
			mbDao.update("YZJEZSRQKTJYEARFORAPP", doucment1, document, true);
		}
		
//		mbDao.insertDataByList("YZJEZSRQKTJYEARFORAPP", userList);
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongMonthWithDept() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" left join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by t"+i+".fee_date");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" left join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by n"+i+".fee_date ");
		}
		buffer.append(") b group by b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
//		 List<DBObject> userList = new ArrayList<DBObject>();
		 
		for(ListTotalIncomeStaticVo vo:list){
//			BasicDBObject bdObject1 = new BasicDBObject();
//			Document document = new Document();
//			bdObject1.append("value", vo.getValue().toString());
//			bdObject1.append("date1", vo.getDate1());
//			userList.add(bdObject1);
			Document doucment1=new Document();
			doucment1.append("date1", vo.getDate1());
			Document document = new Document();
			document.append("value", vo.getValue().toString());
			document.append("date1", vo.getDate1());
			mbDao.update("YZJEZSRQKTJMONTHFORAPP", doucment1, document, true);
		}
//		mbDao.insertDataByList("YZJEZSRQKTJMONTHFORAPP", userList);
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongDateWithDept() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from (");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" left join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" left join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by n"+i+".fee_date ");
		}
		buffer.append(") b group by b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 List<DBObject> userList = new ArrayList<DBObject>();
		 
		for(ListTotalIncomeStaticVo vo:list){
			BasicDBObject bdObject1 = new BasicDBObject();
			Document document = new Document();
			bdObject1.append("value", vo.getValue());
			bdObject1.append("date1", vo.getDate1());
			userList.add(bdObject1);
			
		}
		mbDao.insertDataByList("YZJEZSRQKTJDAYFORAPP", userList);
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongYearOneDayToApp() {
		mbDao = new MongoBasicDao();
		String year=new SimpleDateFormat("yyyy").format(System.currentTimeMillis());//获取当前年
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" left join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
			buffer.append("and t"+i+".fee_date >= to_date('"+year+"-01-01 00:00:00','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<= to_date('"+year+"-12-31 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" left join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date >= to_date('"+year+"-01-01 00:00:00','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date <=to_date('"+year+"-12-31 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by n"+i+".fee_date ");
		}
		buffer.append(") b group by b.sTime1 ");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("date1")//日期
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 
		for(ListTotalIncomeStaticVo vo:list){
			Document doucment1=new Document();
			doucment1.append("date1", vo.getDate1());
			Document document = new Document();
			document.append("value", vo.getValue().toString());
			document.append("date1", vo.getDate1());
			mbDao.update("YZJEZSRQKTJYEARFORAPP", doucment1, document, true);
		}
		sign=true;
		}
	return sign;
	}
	@Override
	public boolean initTotalMongMonthOneDayToAPP() {
				mbDao = new MongoBasicDao();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Calendar ca=Calendar.getInstance();
				ca.set(Calendar.DATE, 1);
				ca.roll(Calendar.DATE, -1);
				Date date=ca.getTime();
				String lastMonth=sdf.format(date);
				 ca.set(GregorianCalendar.DAY_OF_MONTH, 1); 
				 date=ca.getTime();
				 String fristMonth=sdf.format(date);
				List<String> tnL=new ArrayList<String>();
				List<String> maintnl=new ArrayList<String>();
				tnL.add("T_INPATIENT_FEEINFO");
				tnL.add("T_INPATIENT_FEEINFO_NOW");
				maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
				maintnl.add("T_OUTPATIENT_FEEDETAIL");
				boolean sign=false;
				StringBuffer buffer=new StringBuffer(2000);
				buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from(");
				for(int i=0,len=maintnl.size();i<len;i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append("select sum(t"+i+".tot_cost) AS value,");
					buffer.append("to_char(t"+i+".fee_date,'yyyy-MM') AS sTime1 ");
					buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
					buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
					buffer.append("and t"+i+".fee_date >= to_date('"+fristMonth+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <= to_date('"+lastMonth+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
					buffer.append("group by t"+i+".fee_date ");
				}
				if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
					buffer.append(" union All ");
				}
				for(int i=0,len=tnL.size();i<len;i++){
					if(i>0){
						buffer.append(" Union All ");
					}
					buffer.append("select sum(n"+i+".tot_cost) AS value,");
					buffer.append("to_char(n"+i+".fee_date,'yyyy-MM') AS sTime1 ");
					buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
					buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
					buffer.append("and n"+i+".fee_date >= to_date('"+fristMonth+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date <= to_date('"+lastMonth+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
					buffer.append("group by n"+i+".fee_date ");
				}
				buffer.append(") b group by b.sTime1");
				List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
													.addScalar("value",Hibernate.DOUBLE)
													.addScalar("date1")
													.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
			if(list!=null && list.size()>0){
				 
					for(ListTotalIncomeStaticVo vo:list){
						Document doucment1=new Document();
						doucment1.append("date1", vo.getDate1());
						Document document = new Document();
						document.append("value", vo.getValue().toString());
						document.append("date1", vo.getDate1());
						mbDao.update("YZJEZSRQKTJMONTHFORAPP", doucment1, document, true);
					}
				sign=true;
			}
				return sign;
	}
	@Override
	public boolean initTotalMongDateOneDayTOAPP() {
		mbDao = new MongoBasicDao();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("and t"+i+".fee_date >= to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and  t"+i+".fee_date<= to_date('"+date+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date >= to_date('"+date+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date to_date('"+date+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by n"+i+".fee_date ");
		}
		buffer.append(") b group by b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 
			for(ListTotalIncomeStaticVo vo:list){
				Document doucment1=new Document();
				doucment1.append("date1", vo.getDate1());
				Document document = new Document();
				document.append("value", vo.getValue());
				document.append("date1", vo.getDate1());
				mbDao.update("YZJEZSRQKTJDAYFORAPP", doucment1, document, true);
			}
			
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongYearWithDeptTotal() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
//		mbDao.deleteData("YZJEZSRQKTJYEARFORAPPMZZY");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1,b.classType AS classType from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,cast('MZ' as varchar2(2)) AS classType,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,cast('ZY' as varchar2(2)) as classType,");
			//年统计
			buffer.append("to_char(n"+i+".fee_date,'yyyy') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1,b.classType");
 		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.addScalar("classType")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 
		for(ListTotalIncomeStaticVo vo:list){
			BasicDBObject bdObject1 = new BasicDBObject();
			Document document1 = new Document();
			document1.append("date1", vo.getDate1());
			document1.append("name", vo.getName());
			document1.append("classType", vo.getClassType());
			Document document = new Document();
			document.append("value", vo.getValue().toString());//金额
			document.append("name", vo.getName());//月份
			document.append("date1", vo.getDate1());//日期
			document.append("classType", vo.getClassType());//分类MZ门诊 ZY住院
			mbDao.update("YZJEZSRQKTJYEARFORAPPMZZY", document1, document, true);
		}
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongMonthWithDeptTotal() {
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1,b.classType AS classType from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,cast('MZ' as varchar2(2)) AS classType,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,cast('ZY' as varchar(2)) as classType,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1,b.classType");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.addScalar("classType")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list.size()>0){
		 
		for(ListTotalIncomeStaticVo vo:list){
			BasicDBObject bdObject1 = new BasicDBObject();
			Document document1 = new Document();
			document1.append("date1", vo.getDate1());
			document1.append("name", vo.getName());
			document1.append("classType", vo.getClassType());
			Document document = new Document();
			document.append("value", vo.getValue().toString());//金额
			document.append("name", vo.getName());//月份
			document.append("date1", vo.getDate1());//日期
			document.append("classType", vo.getClassType());//分类MZ门诊 ZY住院
			mbDao.update("YZJEZSRQKTJMONTHFORAPPMZZY", document1, document, true);
		}
		sign=true;
	}
		return sign;
	}
	@Override
	public boolean initTotalMongDateWithDeptTotal() {
		
		mbDao = new MongoBasicDao();
		List<String> tnL=new ArrayList<String>();
		List<String> maintnl=new ArrayList<String>();
		tnL.add("T_INPATIENT_FEEINFO");
		tnL.add("T_INPATIENT_FEEINFO_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL_NOW");
		maintnl.add("T_OUTPATIENT_FEEDETAIL");
		boolean sign=false;
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1,b.classType AS classType from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,cast('MZ' as varchar(2)) AS classType,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,cast('ZY' as varchar2(2)) as classType,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1,b.classType");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.addScalar("classType")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list!=null && list.size()>0){
		 List<DBObject> userList = new ArrayList<DBObject>();
		 
		for(ListTotalIncomeStaticVo vo:list){
			BasicDBObject bdObject1 = new BasicDBObject();
			Document document1 = new Document();
			document1.append("date1", vo.getDate1());
			document1.append("name", vo.getName());
			document1.append("classType", vo.getClassType());
			Document document = new Document();
			document.append("value", vo.getValue());//金额
			document.append("name", vo.getName());//月份
			document.append("date1", vo.getDate1());//日期
			document.append("classType", vo.getClassType());//分类MZ门诊 ZY住院
			mbDao.update("YZJEZSRQKTJDAYFORAPPMZZY", document1, document, true);
			
		}
		sign=true;
	}
		return sign;
	}
	
	@Override
	public List<ListTotalIncomeStaticVo> queryTotalIncom(List<String> tnL,List<String> mainL, String begin, String end) {
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value from(");
		for(int i=0,len=mainL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value ");
			buffer.append("from "+mainL.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
			buffer.append("and t"+i+".fee_date >= to_date('"+begin+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date <= to_date('"+end+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name ");
		}
		if(tnL!=null&&mainL!=null&&tnL.size()>0&&mainL.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date >= to_date('"+begin+" 00:00:00','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date <= to_date('"+end+" 23:59:59','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name ");
		}
		buffer.append(") b group by b.cost1,b.name ");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
				.addScalar("value",Hibernate.DOUBLE)
				.addScalar("name")
				.addScalar("code")
				.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<ListTotalIncomeStaticVo>();
	}
	@Override
	public List<Dashboard> queryTotalCount(String endDate, String dateSign) {
		String tableName;//查询表
		String[] startDate;//开始时间
		if("1".equals(dateSign)){
			tableName="YZJEZSRQKTJYEARFORAPP";
			startDate=this.conYear(endDate, dateSign);
		}else if("2".equals(dateSign)){//月
			tableName="YZJEZSRQKTJMONTHFORAPP";
			startDate=this.conYear(endDate, dateSign);
		}else{//日
			tableName="YZJEZSRQKTJDAYFORAPP";
			startDate=this.conYear(endDate, dateSign);
		}
		BasicDBObject bdObject = new BasicDBObject();
		List<Dashboard> list1=new ArrayList<Dashboard>();
		for(String vo:startDate){
		bdObject.append("date1", vo);
		mbDao = new MongoBasicDao();
		DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
		DBObject dbCursor;
		if(!cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			voOne.setName(vo);
			voOne.setValue("0");
			list1.add(voOne);
		}
		while(cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("value");//费用
			 String date2 =(String)dbCursor.get("date1");//科室
			voOne.setDouValue(value);
			voOne.setName(date2);
			list1.add(voOne);
			}
		}
			return list1;
	}
	@Override
	public List<Dashboard> queryTotalsequential(String endDate, String dateSign) {
		String tableName;//查询表
		String[] startDate=this.conMonth(endDate, dateSign);//6月内时间数组
		
		if("2".equals(dateSign)){//月
			tableName="YZJEZSRQKTJMONTHFORAPP";
		}else{//日
			tableName="YZJEZSRQKTJDAYFORAPP";
		}
		List<Dashboard> list1=new ArrayList<Dashboard>();
		BasicDBObject bdObject = new BasicDBObject();
		for(String dateVo:startDate){
		bdObject.append("date1", dateVo);
		mbDao = new MongoBasicDao();
		DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
		DBObject dbCursor;
		if(!cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			voOne.setValue("0");
			voOne.setName(dateVo);
			list1.add(voOne);
		}
		while(cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			 dbCursor = cursor.next();
			 String value = (String) dbCursor.get("value");//费用
			 String date2 =(String)dbCursor.get("date1");//科室
			voOne.setValue(value);
			voOne.setName(date2);
			list1.add(voOne);
		}
		}
			return list1;
	}
	@Override
	public List<Dashboard> queryTotalCountMZZY(String begin, String dateSign){ 
		if(begin==null||dateSign==null){
			return new  ArrayList<Dashboard>();
		}
	    String tableName;//查询表
		String date1;//条件
		int len=begin.length();
		if("1".equals(dateSign)){//年
			if(len!=4){
				return new  ArrayList<Dashboard>();
			}
			tableName="YZJEZSRQKTJYEARFORAPPMZZY";
		}else if("2".equals(dateSign)){//月
			if(len!=7){
				return new  ArrayList<Dashboard>();
			}
			tableName="YZJEZSRQKTJMONTHFORAPPMZZY";
		}else{//日
			if(len!=10){
				return new  ArrayList<Dashboard>();
			}
			tableName="YZJEZSRQKTJDAYFORAPPMZZY";
		}
		BasicDBObject bdObject = new BasicDBObject();
		bdObject.append("date1", begin);
		mbDao = new MongoBasicDao();
		DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
		DBObject dbCursor;
		List<Dashboard> list1=new ArrayList<Dashboard>();
		
		while(cursor.hasNext()){
			Dashboard voOne=new  Dashboard();
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("value") ;//费用
			 String name =(String) dbCursor.get("name");//费用类别
			 String classType=(String)dbCursor.get("classType");//门诊住院
			voOne.setDouValue(value);
			voOne.setName(name);
			voOne.setClassType(classType);
			list1.add(voOne);
		}
			return list1;
	}
	/**
	 * 环比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conYearOne(String date,String dateSing){
		 Calendar ca = Calendar .getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		 SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
		 try {
			 if("1".equals(dateSing)){
			
				date=sdf.format(sdf2.parse(date));
			
			 }else if("2".equals(dateSing)){
				 date=sdf.format(sdf1.parse(date)); 
			 }
		 } catch (ParseException e) {
			} 
		 String[] dateOne =date.split("-");
		 String[] strArr=new String[6];
		 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
		for(int i=0;i<6;i++){
			if("1".equals(dateSing)){
				strArr[i]=sdf2.format(ca.getTime());
				ca.add(Calendar.YEAR, -1);
			}else if("2".equals(dateSing)){
				strArr[i]=sdf1.format(ca.getTime());
				ca.add(Calendar.MONTH, -1);
			}else{
				strArr[i]=sdf.format(ca.getTime());
				ca.add(Calendar.DATE, -1);
			}
		}
		 return strArr;
	}
	/**
	 * 同比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conMonthOne(String date,String dateSing){
		String [] strArr=new String[6];
		String[] dateArr=date.split("-");
		int dateTemp=Integer.parseInt(dateArr[0]);
		for(int i=0;i<6;i++){
			if("2".equals(dateSing)){//月同比
				strArr[i]=(dateTemp-i)+"-"+dateArr[1];
			}else{
				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
			}
		}
		
		return strArr;
	}
	/**
	 * 环比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conYear(String date,String dateSing){
		 Calendar ca = Calendar .getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
		 SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
		 String[] dateOne =date.split("-");
		 String[] strArr=new String[6];
		 if(dateOne.length!=3){
			 dateOne=sdf.format(new Date(System.currentTimeMillis())).split("-"); 
		 }
		 ca.set(Integer.parseInt(dateOne[0]), Integer.parseInt(dateOne[1])-1, Integer.parseInt(dateOne[2]));
		for(int i=0;i<6;i++){
			if("1".equals(dateSing)){
				strArr[i]=sdf2.format(ca.getTime());
				ca.add(Calendar.YEAR, -1);
			}else if("2".equals(dateSing)){
				strArr[i]=sdf1.format(ca.getTime());
				ca.add(Calendar.MONTH, -1);
			}else{
				strArr[i]=sdf.format(ca.getTime());
				ca.add(Calendar.DATE, -1);
			}
		}
		 return strArr;
	}
	/**
	 * 同比
	 * @param date
	 * @param dateSing
	 * @return
	 */
	public String[] conMonth(String date,String dateSing){
		String [] strArr=new String[6];
		String[] dateArr=date.split("-");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(dateArr.length!=3){
			dateArr=sdf.format(new Date(System.currentTimeMillis())).split("-");
		}
		int dateTemp=Integer.parseInt(dateArr[0]);
		for(int i=0;i<6;i++){
			if("2".equals(dateSing)){//月同比
				strArr[i]=(dateTemp-i)+"-"+dateArr[1];
			}else{
				strArr[i]=(dateTemp-i)+"-"+dateArr[1]+"-"+dateArr[2];
			}
		}
		
		return strArr;
	}
	@Override
	public List<Dashboard> queryTotalCountHb(List<String> tnL,
			List<String> maintnl, String begin, String dateSign) {
		String dateFormate;
		if("1".equals(dateSign)){
			dateFormate="yyyy";
		}else if("2".equals(dateSign)){
			dateFormate="yyyy-mm";
		}else{
			dateFormate="yyyy-mm-dd";
		}
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from(");
		buffer.append("select sum(ti.value) as value,ti.  from ");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select t"+i+".tot_cost AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" left join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01'");
		}
		buffer.append(") ti where ti. ");
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" left join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("group by n"+i+".fee_date ");
		}
		buffer.append(") b group by b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
		return null;
	}
	@Override
	public void initDeptTotal() {
		List<String> dateArr=new ArrayList<String>();
		dateArr.add("yyyy");
		dateArr.add("yyyy-mm");
		dateArr.add("yyyy-mm-dd");
		for(String date:dateArr){
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select f.num as num, f.name as name,f.douValue as douValue,(select e.dept_name from t_department e where e.dept_code=f.dept ) as dept from( ");
		 buffer.append("select sum(c.con) as num,c.name as name,sum(c.douValue) as douValue,c.dept as dept  from( ");
		 buffer.append("select count(ti.value) as con,to_char(ti.name,'"+date+"') AS name,sum(ti.value) as douValue,ti.dept as dept from ");
		 buffer.append("(select t.fee_date as name,t.tot_cost as value,t.recipe_deptcode as dept ");
		 buffer.append("from t_inpatient_medicinelist_now t ");
		 buffer.append("union all ");
		 buffer.append("select t1.fee_date as name,t1.tot_cost as value,t1.recipe_deptcode as dept ");
		 buffer.append("from t_inpatient_medicinelist t1 ");
		 buffer.append("union all ");
		 buffer.append("select t3.fee_date as name,t3.tot_cost as value,t3.recipe_deptcode as dept ");
		 buffer.append("from t_inpatient_itemlist_now t3 where t3.send_flag = 1 ");
		 buffer.append("union all ");
		 buffer.append("select t2.fee_date as name,t2.tot_cost as value,t2.recipe_deptcode as dept ");
		 buffer.append("from t_inpatient_itemlist t2 where t2.send_flag = 1 ) ti ");
		 buffer.append("group by ti.name,ti.dept ");
		 buffer.append(") c group by c.name ,c.dept ");
		 buffer.append(") f ");
		 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("dept").addScalar("num",Hibernate.INTEGER)
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
	if(list!=null && list.size()>0){
		for(Dashboard vo:list){
			Document doucment1=new Document();
			doucment1.append("name", vo.getName());
			doucment1.append("dept", vo.getDept());
			Document document = new Document();
				document.append("value", vo.getDouValue());
				document.append("name", vo.getName());
				document.append("dept", vo.getDept());
				document.append("num", vo.getNum());
				mbDao = new MongoBasicDao();
				mbDao.update("ZYSRDEPT", doucment1, document, true);
			}
			}
		}
	}
	@Override
	public List<Dashboard> queryTotalCountTb(List<String> tnL,
			List<String> mainL, String begin, String dateSign) {
		return null;
	}
	@Override
	public void initDoctTotal() {
		List<String> dateArr=new ArrayList<String>();
		dateArr.add("yyyy");
		dateArr.add("yyyy-mm");
		dateArr.add("yyyy-mm-dd");
		for(String date:dateArr){
		 StringBuffer buffer=new StringBuffer();
		 buffer.append("select f.con as num, f.name as name,f.douValue as douValue,(select e.employee_name from t_employee e where e.employee_code=f.doctor) as doctor from( ");
		 buffer.append("select sum(c.con) as con, c.name as name,sum(c.douValue) as douValue,c.doctor as doctor  from( ");
		 buffer.append("select count(ti.value) as con,to_char(ti.name,'"+date+"') AS name,sum(ti.value) as douValue,ti.doc as doctor from ");
		 buffer.append("(select t.fee_date as name,t.tot_cost as value,t.RECIPE_DOCCODE as doc ");
		 buffer.append("from t_inpatient_medicinelist_now t ");
		 buffer.append("union all ");
		 buffer.append("select t1.fee_date as name,t1.tot_cost as value,t1.RECIPE_DOCCODE as doc ");
		 buffer.append("from t_inpatient_medicinelist t1 ");
		 buffer.append("union all ");
		 buffer.append("select t3.fee_date as name,t3.tot_cost as value,t3.RECIPE_DOCCODE as doc ");
		 buffer.append("from t_inpatient_itemlist_now t3 where t3.send_flag = 1 ");
		 buffer.append("union all ");
		 buffer.append("select t2.fee_date as name,t2.tot_cost as value,t2.RECIPE_DOCCODE as doc ");
		 buffer.append("from t_inpatient_itemlist t2 where t2.send_flag = 1 ) ti ");
		 buffer.append("group by ti.name,ti.doc ");
		 buffer.append(") c group by c.name ,c.doctor ");
		 buffer.append(") f ");
		 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
					.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("doctor").addScalar("num",Hibernate.INTEGER)
					.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
	if(list!=null && list.size()>0){
		for(Dashboard vo:list){
			Document doucment1=new Document();
			doucment1.append("name", vo.getName());
			doucment1.append("doctor", vo.getDept());
			Document document = new Document();
				document.append("value", vo.getDouValue());
				document.append("name", vo.getName());
				document.append("doctor", vo.getDoctor());
				document.append("num", vo.getNum());
				mbDao = new MongoBasicDao();
				mbDao.update("ZYSRDOCTOR", doucment1, document, true);
			}
			}
		}
		
	}
/*************************按日初始化YZJEZSRQKTJDAY************************************************************************************************/
	@Override
	public void initToDBByDayOrHours(List<String> tnL, List<String> maintnl,
			String begin, String end, Integer hours) {
		
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1 from(");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
			buffer.append("and t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1");
		List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("value",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
	if(list.size()>0){
		 List<DBObject> userList = new ArrayList<DBObject>();
		 boolean sign=true;
		 if(hours==null){//如果小时不为空  先查mongoDB在更新
			 sign=false;
		 }
		for(ListTotalIncomeStaticVo vo:list){
			if(vo.getValue()!=null){
				if(sign){
					BasicDBObject bdObject = new BasicDBObject();
					bdObject.append("date1", vo.getDate1());
					bdObject.append("name", vo.getName());
					mbDao = new MongoBasicDao();
					DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJDAY", bdObject);
					DBObject dbCursor;
					Double dou=vo.getValue();
					while(cursor.hasNext()){
						dbCursor = cursor.next();
						 Double value = (Double) dbCursor.get("value");//费用
						 if(value!=null){
							dou+= value;
						 }
					}
					vo.setValue(dou);
				}
				Document doucment1=new Document();
				doucment1.append("date1", vo.getDate1());
				doucment1.append("name", vo.getName());
				Document document = new Document();
				document.append("value", vo.getValue());
				document.append("name", vo.getName());
				document.append("date1", vo.getDate1());
				mbDao = new MongoBasicDao();
				mbDao.update("YZJEZSRQKTJDAY", doucment1, document, true);
			}
		}
	 }
	
	}
@Override
public void initDeptTotalByDayOrHours(List<String> tnL,List<String> mainL,String begin,String end,Integer hours) {
	if(tnL!=null||mainL!=null){
	
	 StringBuffer buffer=new StringBuffer(1500);
	 buffer.append("select f.num as num, f.name as name,f.douValue as douValue,(select e.dept_name from t_department e where e.dept_code=f.dept ) as dept from( ");
	 buffer.append("select sum(c.con) as num,c.name as name,sum(c.douValue) as douValue,c.dept as dept  from( ");
	 buffer.append("select count(ti.value) as con,to_char(ti.name,'yyyy-mm-dd') AS name,sum(ti.value) as douValue,ti.dept as dept from ( ");
	 for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" union all ");
		}
		 buffer.append("select t"+i+".fee_date as name,t"+i+".tot_cost as value,t"+i+".recipe_deptcode as dept "); 
		 buffer.append("from "+tnL.get(i)+" t"+i+" ");
	 }
	 if(mainL!=null){
		 buffer.append("union all "); 
	 }
	 for(int i=0,len=mainL.size();i<len;i++){
		 if(i>0){
			 buffer.append("union all ");
		 }
		 buffer.append("select  n"+i+".fee_date as name, n"+i+".tot_cost as value, n"+i+".recipe_deptcode as dept "); 
		 buffer.append("from "+mainL.get(i)+" n"+i+" where n"+i+".send_flag = 1 ");
	 }
	 buffer.append(") ti where ti.name>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and ti.name<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss')  ");
	 buffer.append("group by ti.name,ti.dept ");
	 buffer.append(") c group by c.name ,c.dept ");
	 buffer.append(") f ");
	 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
				.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("dept").addScalar("num",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
			if(list!=null && list.size()>0){
				for(Dashboard vo:list){
					if(vo.getNum()!=null&&vo.getDouValue()!=null){
					if(hours!=null){
						BasicDBObject bdObject = new BasicDBObject();
						bdObject.append("name", vo.getName());
						bdObject.append("dept", vo.getDept());
						mbDao = new MongoBasicDao();
						DBCursor cursor=mbDao.findAlldata("ZYSRDEPT",  bdObject);
						DBObject dbCursor;
						Integer num=vo.getNum();
						Double  value=vo.getDouValue();
						while(cursor.hasNext()){
							 dbCursor = cursor.next();
							 value+=(Double) dbCursor.get("value") ;//金额
							 num+=(Integer)dbCursor.get("num");//数量
							}
						vo.setDouValue(value);
						vo.setNum(num);
					}
					Document doucment1=new Document();
					doucment1.append("name", vo.getName());
					doucment1.append("dept", vo.getDept());
					Document document = new Document();
						document.append("value", vo.getDouValue());
						document.append("name", vo.getName());
						document.append("dept", vo.getDept());
						document.append("num", vo.getNum());
						mbDao = new MongoBasicDao();
						mbDao.update("ZYSRDEPT", doucment1, document, true);
					}
					}
			}
				}
}
@Override
public void initDoctTotalByDayOrHours(List<String> tnL,List<String> mainL,String begin,String end,Integer hours) {
	if(tnL!=null||mainL!=null){
	StringBuffer buffer=new StringBuffer(1500);
	 buffer.append("select f.con as num, f.name as name,f.douValue as douValue,(select e.employee_name from t_employee e where e.employee_code=f.doctor) as doctor from( ");
	 buffer.append("select sum(c.con) as con, c.name as name,sum(c.douValue) as douValue,c.doctor as doctor  from( ");
	 buffer.append("select count(ti.value) as con,to_char(ti.name,'yyyy-mm-dd') AS name,sum(ti.value) as douValue,ti.doc as doctor from ( ");
	 for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" union all ");
		}
		 buffer.append("select t"+i+".fee_date as name,t"+i+".tot_cost as value,t"+i+".RECIPE_DOCCODE as doc "); 
		 buffer.append("from "+tnL.get(i)+" t"+i+" ");
	 }
	 if(mainL!=null){
		 buffer.append("union all "); 
	 }
	 for(int i=0,len=mainL.size();i<len;i++){
		 if(i>0){
			 buffer.append("union all ");
		 }
		 buffer.append("select  n"+i+".fee_date as name, n"+i+".tot_cost as value, n"+i+".RECIPE_DOCCODE as doc "); 
		 buffer.append("from "+mainL.get(i)+" n"+i+" where n"+i+".send_flag = 1 ");
	 }
	 buffer.append(") ti where ti.name>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and ti.name<to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
	 buffer.append("group by ti.name,ti.doc ");
	 buffer.append(") c group by c.name ,c.doctor ");
	 buffer.append(") f ");
	 List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
				.addScalar("name").addScalar("douValue",Hibernate.DOUBLE).addScalar("doctor").addScalar("num",Hibernate.INTEGER)
				.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
if(list!=null && list.size()>0){
	for(Dashboard vo:list){
		if(vo.getValue()!=null&&vo.getName()==null){
		if(hours!=null){
			BasicDBObject bdObject = new BasicDBObject();
			bdObject.append("name", vo.getName());
			bdObject.append("doctor", vo.getDoctor());
			mbDao = new MongoBasicDao();
			DBCursor cursor=mbDao.findAlldata("ZYSRDOCTOR", bdObject);
			DBObject dbCursor;
			double dou=vo.getDouValue();
			int numSum=vo.getNum();
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 dou+= (Double) dbCursor.get("value") ;//金额
				 numSum+=(Integer)dbCursor.get("num");//数量
				}
			vo.setDouValue(dou);
			vo.setNum(numSum);
		 }
			Document doucment1=new Document();
			doucment1.append("name", vo.getName());
			doucment1.append("doctor", vo.getDept());
			Document document = new Document();
			document.append("value", vo.getDouValue());
			document.append("name", vo.getName());
			document.append("doctor", vo.getDoctor());
			document.append("num", vo.getNum());
			mbDao = new MongoBasicDao();
			mbDao.update("ZYSRDOCTOR", doucment1, document, true);
		    }
		  }
		}
	}
  }
@Override
public void initMZZYTotalByDayOrHours(List<String> maintnl, List<String> tnL ,
		String begin, String end, Integer hours) {
	if(tnL!=null||maintnl!=null){
	StringBuffer buffer=new StringBuffer(2000);
	buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS value,b.sTime1 AS date1,b.classType AS classType from(");
	for(int i=0,len=maintnl.size();i<len;i++){
		if(i>0){
			buffer.append(" Union All ");
		}
		buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,cast('MZ' as varchar(2)) AS classType,");
		buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
		buffer.append("from "+maintnl.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
		buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
		buffer.append("and t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
	}
	if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
		buffer.append(" union All ");
	}
	for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" Union All ");
		}
		buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,cast('ZY' as varchar2(2)) as classType,");
		buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
		buffer.append("from "+tnL.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
		buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
		buffer.append("and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
	}
	buffer.append(") b group by b.cost1,b.name,b.sTime1,b.classType");
	List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
										.addScalar("value",Hibernate.DOUBLE)
										.addScalar("name")
										.addScalar("code")
										.addScalar("date1")
										.addScalar("classType")
										.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
if(list!=null && list.size()>0){
	 List<DBObject> userList = new ArrayList<DBObject>();
	for(ListTotalIncomeStaticVo vo:list){
		if(null!=vo.getValue()){
			if(hours!=null){
				BasicDBObject bdObject = new BasicDBObject();
				bdObject.append("date1", vo.getDate1());
				bdObject.append("name", vo.getName());
				bdObject.append("classType", vo.getClassType());
				mbDao = new MongoBasicDao();
				DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJDAYFORAPPMZZY", bdObject);
				DBObject dbCursor;
				Double value1=vo.getValue();
				while(cursor.hasNext()){
					 dbCursor = cursor.next();
					 value1+=(Double) dbCursor.get("value") ;//费用
				}
				vo.setValue(value1);
			}
			BasicDBObject bdObject1 = new BasicDBObject();
			Document document1 = new Document();
			document1.append("date1", vo.getDate1());
			document1.append("name", vo.getName());
			document1.append("classType", vo.getClassType());
			Document document = new Document();
			document.append("value", vo.getValue());//金额
			document.append("name", vo.getName());//月份
			document.append("date1", vo.getDate1());//日期
			document.append("classType", vo.getClassType());//分类MZ门诊 ZY住院
			mbDao = new MongoBasicDao();
			mbDao.update("YZJEZSRQKTJDAYFORAPPMZZY", document1, document, true);
		}
		
				}
			}
	}
}
@Override
public void initTotalWithDateToAPP(List<String> maintnl, List<String> tnL,
		String begin, String end, Integer hours) {
	if(tnL!=null||maintnl!=null){
		
	StringBuffer buffer=new StringBuffer(2000);
	buffer.append("select sum(b.value) AS value,b.sTime1 AS date1 from (");
	for(int i=0,len=maintnl.size();i<len;i++){
		if(i>0){
			buffer.append(" Union All ");
		}
		buffer.append("select sum(t"+i+".tot_cost) AS value,");
		buffer.append("to_char(t"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
		buffer.append("from "+maintnl.get(i)+" t"+i+" left join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
		buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
		buffer.append("and t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by t"+i+".fee_date ");
	}
	if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
		buffer.append(" union All ");
	}
	for(int i=0,len=tnL.size();i<len;i++){
		if(i>0){
			buffer.append(" Union All ");
		}
		buffer.append("select sum(n"+i+".tot_cost) AS value,");
		buffer.append("to_char(n"+i+".fee_date,'yyyy-MM-dd') AS sTime1 ");
		buffer.append("from "+tnL.get(i)+" n"+i+" left join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
		buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
		buffer.append("and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
		buffer.append("group by n"+i+".fee_date ");
	}
	buffer.append(") b group by b.sTime1");
	List<ListTotalIncomeStaticVo> list=super.getSession().createSQLQuery(buffer.toString())
										.addScalar("value",Hibernate.DOUBLE)
										.addScalar("date1")
										.setResultTransformer(Transformers.aliasToBean(ListTotalIncomeStaticVo.class)).list();
if(list!=null && list.size()>0){
	for(ListTotalIncomeStaticVo vo:list){
		if(vo.getValue()!=null){
		if(hours!=null){
			BasicDBObject bdObject = new BasicDBObject();
			List<Dashboard> list1=new ArrayList<Dashboard>();
			bdObject.append("date1", vo.getDate1());
			mbDao = new MongoBasicDao();
			DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJDAYFORAPP", bdObject);
			DBObject dbCursor;
			Double dou=vo.getValue();
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				  dou+=(Double) dbCursor.get("value");//金额
				}
			vo.setValue(dou);
			}
		}
		Document document1 = new Document();
		document1.append("date1", vo.getDate1());
		Document document = new Document();
		document.append("value", vo.getValue());
		document.append("date1", vo.getDate1());
		mbDao = new MongoBasicDao();
		mbDao.update("YZJEZSRQKTJDAYFORAPP", document1, document, true);
				}
			}
		}
	
	}
/***********************月初始化************************************************************************************************/
@Override
public void initToDBByMonth(String begin, String end) {
	List<String> list=reMonthDay(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
	String temp;//月数据
	Double dou;
	String temp1;//key
	for(String st:list){//获取时间段天数
		bdObject.append("date1", st);
		temp=st.substring(0,7);
		mbDao = new MongoBasicDao();
		DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJDAY", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("value") ;//金额
			 String name = (String) dbCursor.get("name");//统计大类名称
			 temp1=temp+"&"+name;
			 if(map.containsKey(temp1)){//如果key存在 比较name
					 dou=map.get(temp1);
					 dou+=value;
					 map.put(temp1,dou);
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, value);
			 }
		}
		
	}	
		String[] stArr=new String[2];
		for(String key:map.keySet()){
			stArr=key.split("&");
			Document doucment1=new Document();
			doucment1.append("date1",stArr[0]);
			doucment1.append("name",stArr[1] );
			Document document = new Document();
			document.append("value",map.get(key));
			document.append("name", stArr[1]);
			document.append("date1", stArr[0]);
			mbDao = new MongoBasicDao();
			mbDao.update("YZJEZSRQKTJMONTH", doucment1, document, true);
			
		}
	}
}


@Override
public void initDeptTotalByDayOrHours(String begin, String end){
	List<String> list=reMonthDay(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,String> map=new HashMap<String,String>();//日期+科室   科室引用
	Map<String,Integer> map1=new HashMap<String,Integer>();//科室  数量
	Map<String,Double> map2=new HashMap<String,Double>();//科室  金额
	String temp;//月数据
	Double dou;
	Integer nu;
	String temp1;//key
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,7);
			bdObject.append("name", begin);
			mbDao = new MongoBasicDao();
			DBCursor cursor=mbDao.findAlldata("ZYSRDEPT", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("value") ;//金额
				 Integer num =(Integer)dbCursor.get("num");//数量
				 String  dept=(String)dbCursor.get("dept");//科室
		    temp1=temp+"&"+dept; 
		    if(map.containsKey(temp1)){//如果key存在 比较name
					nu=map1.get(dept);
					dou=map2.get(dept);
		    	    nu+=num;
					dou+=value;
					 map1.put(dept,nu);
					 map2.put(dept, dou);
					 
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, dept);
				 map1.put(dept, num);
				 map2.put(dept, value);
			 }
			   }
			 }
	String[] strKey=new String[2];
	for(String key:map.keySet()){
		  strKey=key.split("&");
		   Document doucment1=new Document();
		   doucment1.append("name", strKey[0]);
		   doucment1.append("dept", strKey[1]);
		    Document document = new Document();
			document.append("value",map2.get(strKey[1]));
			document.append("name", strKey[0]);
			document.append("dept", strKey[1]);
			document.append("num", map1.get(strKey[1]));
			mbDao = new MongoBasicDao();
			mbDao.update("ZYSRDEPT", doucment1, document, true);
		}
	}
}
@Override
public void initDoctTotalByDayOrHours(String begin,String end){
	List<String> list=reMonthDay(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,String> map=new HashMap<String,String>();//日期+科室   科室引用
	Map<String,Integer> map1=new HashMap<String,Integer>();//科室  数量
	Map<String,Double> map2=new HashMap<String,Double>();//科室  金额
	String temp;//月数据
	Double dou;
	Integer nu;
	String temp1;//key
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,7);
			bdObject.append("name", begin);
			mbDao = new MongoBasicDao();
			DBCursor cursor=mbDao.findAlldata("ZYSRDOCTOR", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("value") ;//金额
				 Integer num =(Integer)dbCursor.get("num");//数量
				 String  doctor=(String)dbCursor.get("doctor");//科室
		    temp1=temp+"&"+doctor; 
		    if(map.containsKey(temp1)){//如果key存在 比较name
					nu=map1.get(doctor);
					dou=map2.get(doctor);
		    	    nu+=num;
					dou+=value;
					 map1.put(doctor,nu);
					 map2.put(doctor, dou);
					 
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, doctor);
				 map1.put(doctor, num);
				 map2.put(doctor, value);
			    }
			   }
			 }
	String[] strKey=new String[2];
	for(String key:map.keySet()){
		  strKey=key.split("&");
		   Document doucment1=new Document();
		   doucment1.append("name", strKey[0]);
		   doucment1.append("dept", strKey[1]);
		    Document document = new Document();
			document.append("value",map2.get(strKey[1]));
			document.append("name", strKey[0]);
			document.append("doctor", strKey[1]);
			document.append("num", map1.get(strKey[1]));
			mbDao = new MongoBasicDao();
			mbDao.update("ZYSRDOCTOR", doucment1, document, true);
		}
	}
}

@Override
public void initMZZYTotalByDayOrHours(String begin, String end){
	List<String> list=reMonthDay(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,Double> map=new HashMap<String,Double>();
	String temp;//月数据
	Double dou;
	String temp1;//key
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,7);
			bdObject.append("date1", st);
			mbDao = new MongoBasicDao();
			DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJDAYFORAPPMZZY", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				Dashboard voOne=new  Dashboard();
				 dbCursor = cursor.next();
				 Double  value = (Double) dbCursor.get("value") ;//费用
				 String name =(String) dbCursor.get("name");//费用类别
				 String classType=(String)dbCursor.get("classType");//门诊住院
		        temp1=temp+"&"+name+"&"+classType;
				 if(map.containsKey(temp1)){//如果key存在 比较name
					dou=map.get(temp1);
					dou+=value;
					map.put(temp1, dou);
			    
				 }else{//如果key不存在   添加到map1中
			    	map.put(temp1, value);
			    }
			  }
			}
	String[] strKey=null;
	for(String key:map.keySet()){
		  strKey=key.split("&");
		   Document doucment1=new Document();
		   doucment1.append("date1", strKey[0]);
		   doucment1.append("name", strKey[1]);
		   doucment1.append("classType", strKey[2]);
		    Document document = new Document();
			document.append("value",map.get(key));
			document.append("name", strKey[1]);
			document.append("classType", strKey[2]);
			document.append("date1", strKey[0]);
			mbDao = new MongoBasicDao();
			mbDao.update("YZJEZSRQKTJMONTHFORAPPMZZY", doucment1, document, true);
		}
	
	
	
	}
}

@Override
public void initTotalWithDateToAPP(String begin, String end){
	List<String> list=reMonthDay(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,Double> map=new HashMap<String,Double>();
	String temp;//月数据
	Double dou;
	String temp1;//key
	mbDao = new MongoBasicDao();
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,7);
			bdObject.append("date1", st);
			DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJDAYFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				Dashboard voOne=new  Dashboard();
				 dbCursor = cursor.next();
				 Double  value = (Double) dbCursor.get("value") ;//费用
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
		mbDao = new MongoBasicDao();
		for(String key:map.keySet()){
			   Document doucment1=new Document();
			   doucment1.append("date1", key);
			    Document document = new Document();
				document.append("value",map.get(key));
				document.append("date1", key);
				
				mbDao.update("YZJEZSRQKTJMONTHFORAPP", doucment1, document, true);
			}
	
	}
	
}
/***************************年统计************************************************************************************************/
@Override
public void initToDBByYear(String begin, String end) {
	List<String> list=reYearMonth(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	List<ListTotalIncomeStaticVo> list1=new ArrayList<ListTotalIncomeStaticVo>();
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,Double> map=new HashMap<String,Double>();//保存日期时间费用名称
	String temp;//月数据
	Double dou;
	String temp1;//key
	mbDao = new MongoBasicDao();
	for(String st:list){//获取时间段天数
		bdObject.append("date1", st);
		temp=st.substring(0,4);
		DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJMONTH", bdObject);
		DBObject dbCursor;
		while(cursor.hasNext()){
			ListTotalIncomeStaticVo voOne=new  ListTotalIncomeStaticVo();
			 dbCursor = cursor.next();
			 Double value = (Double) dbCursor.get("value") ;//金额
			 String name = (String) dbCursor.get("name");//统计大类名称
			 temp1=temp+"&"+name;
			 if(map.containsKey(temp1)){//如果key存在 比较name
					 dou=map.get(temp1);
					 dou+=value;
					 map.put(temp1,dou);
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, value);
			 }
		}
		
	}	
		String[] stArr=new String[2];
		mbDao = new MongoBasicDao();
		for(String key:map.keySet()){
			stArr=key.split("&");
			if(stArr.length==2){
			Document doucment1=new Document();
			doucment1.append("date1",stArr[0]);
			doucment1.append("name",stArr[1] );
			Document document = new Document();
			document.append("value",map.get(key));
			document.append("name", stArr[1]);
			document.append("date1", stArr[0]);
			mbDao.update("YZJEZSRQKTJYEAR", doucment1, document, true);
			}
			
		}
	}
}

@Override
public void initDeptTotalByDB(String begin, String end){
	List<String> list=reYearMonth(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	List<ListTotalIncomeStaticVo> list1=new ArrayList<ListTotalIncomeStaticVo>();
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,String> map=new HashMap<String,String>();//日期+科室   科室引用
	Map<String,Integer> map1=new HashMap<String,Integer>();//科室  数量
	Map<String,Double> map2=new HashMap<String,Double>();//科室  金额
	String temp;//月数据
	Double dou;
	Integer nu;
	String temp1;//key
	mbDao = new MongoBasicDao();
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,4);
			bdObject.append("name", begin);
			DBCursor cursor=mbDao.findAlldata("ZYSRDEPT", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("value") ;//金额
				 Integer num =(Integer)dbCursor.get("num");//数量
				 String  dept=(String)dbCursor.get("dept");//科室
		    temp1=temp+"&"+dept; 
		    if(map.containsKey(temp1)){//如果key存在 
					nu=map1.get(dept);
					dou=map2.get(dept);
		    	    nu+=num;
					dou+=value;
					 map1.put(dept,nu);
					 map2.put(dept, dou);
					 
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, dept);
				 map1.put(dept, num);
				 map2.put(dept, value);
			  }
			   }
			 }
	String[] strKey=new String[2];
	mbDao = new MongoBasicDao();
	for(String key:map.keySet()){
		  strKey=key.split("&");
		  if(strKey.length==2){
		   Document doucment1=new Document();
		   doucment1.append("name", strKey[0]);
		   doucment1.append("dept", strKey[1]);
		    Document document = new Document();
			document.append("value",map2.get(strKey[1]));
			document.append("name", strKey[0]);
			document.append("dept", strKey[1]);
			document.append("num", map1.get(strKey[1]));
			mbDao.update("ZYSRDEPT", doucment1, document, true);
		  }
		}
	}
}

@Override
public void initDoctTotalByDB(String begin,String end){
	List<String> list=reYearMonth(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	List<ListTotalIncomeStaticVo> list1=new ArrayList<ListTotalIncomeStaticVo>();
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,String> map=new HashMap<String,String>();//日期+科室   科室引用
	Map<String,Integer> map1=new HashMap<String,Integer>();//科室  数量
	Map<String,Double> map2=new HashMap<String,Double>();//科室  金额
	String temp;//月数据
	Double dou;
	Integer nu;
	String temp1;//key
	mbDao = new MongoBasicDao();
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,4);
			bdObject.append("name", begin);
			DBCursor cursor=mbDao.findAlldata("ZYSRDOCTOR", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				 dbCursor = cursor.next();
				 Double value = (Double) dbCursor.get("value") ;//金额
				 Integer num =(Integer)dbCursor.get("num");//数量
				 String  doctor=(String)dbCursor.get("doctor");//科室
		    temp1=temp+"&"+doctor; 
		    if(map.containsKey(temp1)){//如果key存在 比较name
					nu=map1.get(doctor);
					dou=map2.get(doctor);
		    	    nu+=num;
					dou+=value;
					 map1.put(doctor,nu);
					 map2.put(doctor, dou);
					 
			 }else{//如果key不存在   添加到map1中
				 map.put(temp1, doctor);
				 map1.put(doctor, num);
				 map2.put(doctor, value);
			    }
			   }
			 }
	String[] strKey=new String[2];
	mbDao = new MongoBasicDao();
	for(String key:map.keySet()){
		  strKey=key.split("&");
		  if(strKey.length==2){
			  Document doucment1=new Document();
			   doucment1.append("name", strKey[0]);
			   doucment1.append("dept", strKey[1]);
			    Document document = new Document();
				document.append("value",map2.get(strKey[1]));
				document.append("name", strKey[0]);
				document.append("doctor", strKey[1]);
				document.append("num", map1.get(strKey[1]));
				mbDao.update("ZYSRDOCTOR", doucment1, document, true);  
		  }
		   
		}
	}
}

@Override
public void initMZZYTotalYear(String begin, String end){
	List<String> list=reYearMonth(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	List<ListTotalIncomeStaticVo> list1=new ArrayList<ListTotalIncomeStaticVo>();
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,Double> map=new HashMap<String,Double>();
	String temp;//月数据
	Double dou;
	String temp1;//key
	mbDao = new MongoBasicDao();
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,4);
			bdObject.append("date1", st);
			DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJMONTHFORAPPMZZY", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				Dashboard voOne=new  Dashboard();
				 dbCursor = cursor.next();
				 Double  value = (Double) dbCursor.get("value") ;//费用
				 String name =(String) dbCursor.get("name");//费用类别
				 String classType=(String)dbCursor.get("classType");//门诊住院
		        temp1=temp+"&"+name+"&"+classType;
				 if(map.containsKey(temp1)){//如果key存在 比较name
					dou=map.get(temp1);
					dou+=value;
					map.put(temp, dou);
			    
				 }else{//如果key不存在   添加到map1中
			    	map.put(temp1, value);
			    }
			  }
			}
	String[] strKey=new String[3];
	mbDao = new MongoBasicDao();
	for(String key:map.keySet()){
		  strKey=key.split("&");
		  if(strKey.length==3){
		   Document doucment1=new Document();
		   doucment1.append("date1", strKey[0]);
		   doucment1.append("name", strKey[1]);
		   doucment1.append("classType", strKey[2]);
		    Document document = new Document();
			document.append("value",map.get(key));
			document.append("name", strKey[1]);
			document.append("classType", strKey[2]);
			document.append("date1", strKey[0]);
			mbDao.update("YZJEZSRQKTJYEARFORAPPMZZY", doucment1, document, true);
		  }
		}
	
	}
}
@Override
public void initTotalYear(String begin, String end){
	List<String> list=reYearMonth(begin,end,new ArrayList<String>());
	if(list!=null && list.size()>0){
	BasicDBObject bdObject = new BasicDBObject();
	Map<String,Double> map=new HashMap<String,Double>();
	String temp;//月数据
	Double dou;
	String temp1;//key
	mbDao = new MongoBasicDao();
	for(String st:list){//获取时间段天数
		    temp=st.substring(0,4);
			bdObject.append("date1", st);
			DBCursor cursor = mbDao.findAlldata("YZJEZSRQKTJMONTHFORAPP", bdObject);
			DBObject dbCursor;
			while(cursor.hasNext()){
				Dashboard voOne=new  Dashboard();
				 dbCursor = cursor.next();
				 Double  value = (Double) dbCursor.get("value") ;//费用
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
		mbDao = new MongoBasicDao();
		for(String key:map.keySet()){
			   Document doucment1=new Document();
			   doucment1.append("date1", key);
			    Document document = new Document();
				document.append("value",map.get(key));
				document.append("date1", key);
			mbDao.update("YZJEZSRQKTJYEARFORAPP", doucment1, document, true);
			}
	
	}
}
/**************************************************************************************************************************************/
//获取日期每个月的每天
public List<String> reMonthDay(String begin,String end,List<String> list){
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
public List<String> reYearMonth(String begin,String end,List<String> list){
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
/**************************初始化失败分区查数据库***************************************************************************************/
@Override
public List<Dashboard> queryForOracleMZZY(List<String> tnL, List<String> maintnl,
		String begin, String end, String dateSign) {
	String 	dateFormate;
	if("1".equals(dateSign)){
			dateFormate="yyyy";
	} else if("2".equals(dateSign)){
			dateFormate="yyyy-mm";
	} else {
		dateFormate="yyyy-mm-dd";
	}
	StringBuffer buffer=new StringBuffer(2000);
	if(tnL!=null||maintnl!=null){
		buffer.append("select to_char(b.cost1) AS code,b.name AS name,sum(b.value) AS douValue,b.sTime1 AS date1,b.classType AS classType from(");
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(t"+i+".tot_cost) AS value,cast('MZ' as varchar(2)) AS classType,");
			buffer.append("to_char(t"+i+".fee_date,'"+dateFormate+"') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" t"+i+" join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
			buffer.append("and t"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select c.fee_stat_code AS cost1,c.fee_stat_name AS name,sum(n"+i+".tot_cost) AS value,cast('ZY' as varchar2(2)) as classType,");
			buffer.append("to_char(n"+i+".fee_date,'"+dateFormate+"') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" n"+i+" join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date>=to_date('"+begin+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+end+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by c.fee_stat_code,c.fee_stat_name,n"+i+".fee_date ");
		}
		buffer.append(") b group by b.cost1,b.name,b.sTime1,b.classType");
		List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("douValue",Hibernate.DOUBLE)
											.addScalar("name")
											.addScalar("code")
											.addScalar("date1")
											.addScalar("classType")
											.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
		if(list.size()>0){
			return list;
			}
		}
	return new ArrayList<Dashboard>();
	}
@Override
public List<Dashboard> queryTotalCountSame(String endDate, String dateSign) {
	
	String tableName;//查询表
	if("2".equals(dateSign)){//月
		tableName="YZJEZSRQKTJMONTHFORAPP";
	}else{//日
		tableName="YZJEZSRQKTJDAYFORAPP";
	}
	String[] startDate;//开始时间
	startDate=this.conMonthOne(endDate, dateSign);
	BasicDBObject bdObject = new BasicDBObject();
	List<Dashboard> list1=new ArrayList<Dashboard>();
	for(String vo:startDate){
	bdObject.append("date1", vo);
	mbDao = new MongoBasicDao();
	DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
	DBObject dbCursor;
	if(!cursor.hasNext()){
		Dashboard voOne=new  Dashboard();
		voOne.setName(vo);
		voOne.setDouValue(0.00);
		list1.add(voOne);
	}
	while(cursor.hasNext()){
		Dashboard voOne=new  Dashboard();
		 dbCursor = cursor.next();
		 Double value = (Double) dbCursor.get("value");//费用
		 String date2 =(String)dbCursor.get("date1");//科室
		voOne.setDouValue(value);
		voOne.setName(date2);
		list1.add(voOne);
		}
	}
		return list1;
}
@Override
public Dashboard queryTotalForOracle(List<String> tnL,
		List<String> maintnl, String startTime, String endTime, String dateSign) {
	if(tnL!=null||maintnl!=null){
		String formateDate;
		if("1".equals(dateSign)){
			formateDate="yyyy";
		}else if("2".equals(dateSign)){
			formateDate="yyyy-mm";
		}else{
			formateDate="yyyy-mm-dd";
		}
		StringBuffer buffer=new StringBuffer(2000);
		buffer.append("select sum(b.value) AS douValue,b.sTime1 AS date1 from (");
		for(int i=0,len=maintnl.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(t"+i+".tot_cost) AS value,");
			buffer.append("to_char(t"+i+".fee_date,'"+formateDate+"') AS sTime1 ");
			buffer.append("from "+maintnl.get(i)+" t"+i+" left join t_charge_minfeetostat c on c.minfee_code=t"+i+".fee_code ");
			buffer.append("where t"+i+".pay_flag = 1 and t"+i+".cancel_flag = 1 and t"+i+".sequence_no = 1 and c.report_code='MZ01' ");
			buffer.append("and t"+i+".fee_date>=to_date('"+startTime+"','yyyy-mm-dd HH24:mi:ss') and t"+i+".fee_date<=to_date('"+endTime+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by t"+i+".fee_date ");
		}
		if(tnL!=null&&maintnl!=null&&tnL.size()>0&&maintnl.size()>0){
			buffer.append(" union All ");
		}
		for(int i=0,len=tnL.size();i<len;i++){
			if(i>0){
				buffer.append(" Union All ");
			}
			buffer.append("select sum(n"+i+".tot_cost) AS value,");
			buffer.append("to_char(n"+i+".fee_date,'"+formateDate+"') AS sTime1 ");
			buffer.append("from "+tnL.get(i)+" n"+i+" left join t_charge_minfeetostat c on c.minfee_code=n"+i+".fee_code ");
			buffer.append("where n"+i+".TRANS_TYPE = 1 and c.report_code='ZY01' ");
			buffer.append("and n"+i+".fee_date>=to_date('"+startTime+"','yyyy-mm-dd HH24:mi:ss') and n"+i+".fee_date<=to_date('"+endTime+"','yyyy-mm-dd HH24:mi:ss') ");
			buffer.append("group by n"+i+".fee_date ");
		}
		buffer.append(") b group by b.sTime1");
		List<Dashboard> list=super.getSession().createSQLQuery(buffer.toString())
											.addScalar("douValue",Hibernate.DOUBLE)
											.addScalar("date1")
											.setResultTransformer(Transformers.aliasToBean(Dashboard.class)).list();
	if(list.size()>0){
		return list.get(0);
		}
	}
	Dashboard vo=new Dashboard();
	vo.setDouValue(0.0);
	if("1".equals(dateSign)){
		vo.setName(startTime.substring(0,4));
	}else if("2".equals(dateSign)){
		vo.setName(startTime.substring(0,7));
	}else{
		vo.setName(startTime.substring(0,10));
	}
		return vo;
}
@Override
public List<Dashboard> queryTotalCountSque(String begin, String dateSign) {
	mbDao = new MongoBasicDao();
	String tableName;//查询表
	if("1".equals(dateSign)){//年
		tableName="YZJEZSRQKTJYEARFORAPP";
	}else if("2".equals(dateSign)){//月
		tableName="YZJEZSRQKTJMONTHFORAPP";
	}else{//日
		tableName="YZJEZSRQKTJDAYFORAPP";
	}
	String[] startDate;//开始时间
	startDate=this.conYearOne(begin, dateSign);
	BasicDBObject bdObject = new BasicDBObject();
	List<Dashboard> list1=new ArrayList<Dashboard>();
	for(String vo:startDate){
	bdObject.append("date1", vo);
	DBCursor cursor = mbDao.findAlldata(tableName, bdObject);
	DBObject dbCursor;
	if(!cursor.hasNext()){
		Dashboard voOne=new  Dashboard();
		voOne.setName(vo);
		voOne.setDouValue(0.00);
		list1.add(voOne);
	}
	while(cursor.hasNext()){
		Dashboard voOne=new  Dashboard();
		 dbCursor = cursor.next();
		 Double value = (Double) dbCursor.get("value");//费用
		 String date2 =(String)dbCursor.get("date1");//科室
		voOne.setDouValue(value);
		voOne.setName(date2);
		list1.add(voOne);
		}
	}
		return list1;
}
}