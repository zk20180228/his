package cn.honry.statistics.sys.invoiceChecks.dao.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.DrugInStore;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.sys.invoiceChecks.dao.InvoiceChecksDAO;
import cn.honry.statistics.sys.invoiceChecks.vo.VinpatirntInfoBalance;

@Repository("invoiceChecksDAO")
@SuppressWarnings({ "all" })
public class InvoiceChecksDAOImpl extends HibernateEntityDao<VinpatirntInfoBalance> implements InvoiceChecksDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<VinpatirntInfoBalance> queryVinpatirntInfoBalance(
			String medicalrecordId,String invoiceNo) throws Exception{
		String sql="select t.patient_name as patientName,t.medicalrecord_id as medicalrecordId,(select aa.UNIT_NAME from  T_BUSINESS_CONTRACTUNIT aa "
				+ "where aa.unit_code=t.pact_code) as pactCode,t.in_date as inDate,tt.invoice_no as invoiceNo,tt.balance_date as balanceDate,"
				+ "tt.prepay_cost as prepayCost,tt.return_cost as returnCost,tt.tot_cost as totCost from t_inpatient_info_now t "
				+ "join T_INPATIENT_BALANCEHEAD_NOW tt  on tt.inpatient_no=t.inpatient_no "
				+ "where  tt.del_flg=0 and tt.stop_flg=0 "
				+ "and t.INPATIENT_NO = '"+medicalrecordId+"' and tt.invoice_no='"+invoiceNo+"'";
		int total = super.getSqlTotal(sql);
		if(total==0){
			sql="";
			sql="select t.patient_name as patientName,t.medicalrecord_id as medicalrecordId,(select aa.UNIT_NAME from  T_BUSINESS_CONTRACTUNIT aa "
					+ "where aa.unit_code=t.pact_code) as pactCode,t.in_date as inDate,tt.invoice_no as invoiceNo,tt.balance_date as balanceDate,"
					+ "tt.prepay_cost as prepayCost,tt.return_cost as returnCost,tt.tot_cost as totCost from t_inpatient_info t "
					+ "join T_INPATIENT_BALANCEHEAD tt  on tt.inpatient_no=t.inpatient_no "
					+ "where tt.del_flg=0 and tt.stop_flg=0 "
					+ "and t.INPATIENT_NO = '"+medicalrecordId+"' and tt.invoice_no='"+invoiceNo+"'";
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString())
				.addScalar("patientName").addScalar("medicalrecordId").addScalar("pactCode").addScalar("inDate",Hibernate.TIMESTAMP)
				.addScalar("invoiceNo").addScalar("balanceDate",Hibernate.TIMESTAMP).addScalar("prepayCost",Hibernate.DOUBLE)
				.addScalar("returnCost",Hibernate.DOUBLE).addScalar("totCost",Hibernate.DOUBLE);
		List<VinpatirntInfoBalance> VinpatirntInfoBalanceList = queryObject.setResultTransformer(Transformers.aliasToBean(VinpatirntInfoBalance.class)).list();
		if(VinpatirntInfoBalanceList!=null&&VinpatirntInfoBalanceList.size()>0){
			return VinpatirntInfoBalanceList;
		}
		return new ArrayList<VinpatirntInfoBalance>();
	}

	@Override
	public List<InpatientInfoNow> queryInfolist(String medicalrecordId,String idCard) throws Exception{
		Query qureySql=null;
		List<InpatientInfoNow> InpatientInfoist=null;
		String hql=null;
		String hql1=null;
		if(StringUtils.isNotBlank(medicalrecordId)){
			hql="from InpatientInfoNow e where  e.medicalrecordId = :medicalrecordId ";   
			qureySql=this.getSession().createQuery(hql).setParameter("medicalrecordId", medicalrecordId);
			InpatientInfoist=qureySql.list();
			hql1="from InpatientInfo e where  e.medicalrecordId = :medicalrecordId ";   
			qureySql=this.getSession().createQuery(hql1).setParameter("medicalrecordId", medicalrecordId);
		}else{
			hql="from InpatientInfoNow e where  e.certificatesNo = :idCard ";
			qureySql=this.getSession().createQuery(hql).setParameter("idCard", idCard);
			InpatientInfoist=qureySql.list();
			hql1="from InpatientInfo e where  e.certificatesNo = :idCard ";   
			qureySql=this.getSession().createQuery(hql1).setParameter("idCard", idCard);
		}
		if(InpatientInfoist!=null && InpatientInfoist.size()>0){
			InpatientInfoist.addAll(qureySql.list());
		}else{
			InpatientInfoist = new ArrayList<InpatientInfoNow>();
			InpatientInfoist.addAll(qureySql.list());
		}
		return InpatientInfoist;
	} 
	
	@Override
	public List<InpatientBalanceHeadNow> queryBalanceHead(String inpatientNo) throws Exception{
		String hql="FROM InpatientBalanceHeadNow i WHERE  i.del_flg = 0 and i.wasteFlag=1 ";
			   hql = hql +" and i.inpatientNo = :inpatientNo order by i.balanceDate";
	    Query qureySql=this.getSession().createQuery(hql).setParameter("inpatientNo", inpatientNo);
	    List<InpatientBalanceHeadNow> InpatientBalanceHeadlist=qureySql.list();
	    String hql1="FROM InpatientBalanceHead i WHERE  i.del_flg = 0 and i.wasteFlag=1 ";
		   hql1 = hql1 +" and i.inpatientNo = :inpatientNo order by i.balanceDate";
        Query qureySql1=this.getSession().createQuery(hql1).setParameter("inpatientNo", inpatientNo);
		if(InpatientBalanceHeadlist!=null&&InpatientBalanceHeadlist.size()>0){
			InpatientBalanceHeadlist.addAll(qureySql1.list());
		}else{
			InpatientBalanceHeadlist = new ArrayList<InpatientBalanceHeadNow>();
			InpatientBalanceHeadlist.addAll(qureySql1.list());
		}
		return InpatientBalanceHeadlist;
	}

	@Override
	public List<VinpatirntInfoBalance> queryVinpatirntInfoBalancepages(
			String medicalrecordId, String invoiceNo, String page, String rows) throws Exception{
		String sql;
		if(StringUtils.isNotBlank(page)){
			sql=querySql(medicalrecordId,invoiceNo,page,rows);
		}else{
			sql=querySqlAll(medicalrecordId,invoiceNo,page,rows);
		}
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<VinpatirntInfoBalance> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<VinpatirntInfoBalance>() {
			@Override
			public VinpatirntInfoBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
				VinpatirntInfoBalance vo = new VinpatirntInfoBalance();
				vo.setPatientName(rs.getString("patientName"));
				vo.setStatName(rs.getString("statName"));
				vo.setSmallTot(rs.getDouble("smallTot"));
				vo.setInvoiceNo(rs.getString("invoiceNo"));
				return vo;
		}});
		return voList;
	}

	@Override
	public int queryVinpatirntInfoBalanceTotal(String medicalrecordId,
			String invoiceNo) throws Exception{
		String sql="select count(1)  from T_INPATIENT_BALANCELIST_NOW b "
				+ " where b.trans_type = 1 "
				+ "and b.del_flg=0 and b.stop_flg=0  "
				+ "and b.inpatient_no='"+medicalrecordId+"' and b.invoice_no='"+invoiceNo+"' ";
		int total = jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
		if(total==0){
			sql="";
			sql="select count(1)  from T_INPATIENT_BALANCELIST b "
					+ " where b.trans_type = 1 "
					+ "and b.del_flg=0 and b.stop_flg=0  "
					+ "and b.inpatient_no='"+medicalrecordId+"' and b.invoice_no='"+invoiceNo+"'";
		}
		return jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
	}
	private String querySql(String medicalrecordId,String invoiceNo,String page, String rows){
		String sql="select * from (select b.name as patientName,b.stat_name as statName,b.tot_cost as smallTot,"
				+ " b.invoice_no as invoiceNo,rownum as rn  from T_INPATIENT_BALANCELIST_NOW b "
				+ " where b.trans_type = 1 "
				+ "and b.del_flg=0 and b.stop_flg=0  "
				+ "and b.inpatient_no='"+medicalrecordId+"' and b.invoice_no='"+invoiceNo+"')tt where  rn > ('"+page+"' -1) * '"+rows+"' and rn <=('"+page+"') * '"+rows+"'  ";
		int total = super.getSqlTotal(sql);
		if(total==0){
			sql="";
			sql="select * from (select b.name as patientName,b.stat_name as statName,b.tot_cost as smallTot,"
					+ " b.invoice_no as invoiceNo,rownum as rn  from T_INPATIENT_BALANCELIST b "
					+ " where b.trans_type = 1 "
					+ "and b.del_flg=0 and b.stop_flg=0  "
					+ "and b.inpatient_no='"+medicalrecordId+"' and b.invoice_no='"+invoiceNo+"')tt where  rn > ('"+page+"' -1) * '"+rows+"' and rn <=('"+page+"') * '"+rows+"' ";
		}
		return sql;
	}
	
	/**报表打印  查询全部结算患者发票对账单**/
	private String querySqlAll(String medicalrecordId,String invoiceNo,String page, String rows){
		String sql="select * from (select b.name as patientName,b.stat_name as statName,b.tot_cost as smallTot,"
				+ " b.invoice_no as invoiceNo  from T_INPATIENT_BALANCELIST_NOW b "
				+ " where b.trans_type = 1 "
				+ "and b.del_flg=0 and b.stop_flg=0  "
				+ "and b.inpatient_no='"+medicalrecordId+"' and b.invoice_no='"+invoiceNo+"')tt ";
		int total = super.getSqlTotal(sql);
		if(total==0){
			sql="";
			sql="select * from (select b.name as patientName,b.stat_name as statName,b.tot_cost as smallTot,"
					+ " b.invoice_no as invoiceNo  from T_INPATIENT_BALANCELIST b "
					+ " where b.trans_type = 1 "
					+ "and b.del_flg=0 and b.stop_flg=0  "
					+ "and b.inpatient_no='"+medicalrecordId+"' and b.invoice_no='"+invoiceNo+"')tt  ";
		}
		return sql;
	}
}
