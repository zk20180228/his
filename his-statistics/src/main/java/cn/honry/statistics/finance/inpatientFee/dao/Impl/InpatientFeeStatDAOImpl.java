package cn.honry.statistics.finance.inpatientFee.dao.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientFeeInfo;
import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientItemList;
import cn.honry.base.bean.model.InpatientMedicineList;
import cn.honry.base.bean.model.MinfeeStatCode;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.finance.inpatientFee.dao.InpatientFeeStatDAO;
import cn.honry.statistics.finance.inpatientFee.vo.FeeInfosVo;
import cn.honry.statistics.finance.inpatientFee.vo.InpatientInfosVo;
@Repository("inpatientFeeStatDAO")
@SuppressWarnings({ "all" })
public class InpatientFeeStatDAOImpl extends HibernateEntityDao<InpatientInfo> implements InpatientFeeStatDAO{
	
	private static final int List = 0;
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public List<InpatientInfosVo> queryInpatientInfo(InpatientInfo entity, String a) throws Exception{		
			StringBuffer sql=new StringBuffer();
			sql.append("select t.INPATIENT_ID   as id, t.INPATIENT_NO  as inpatientNo, t.MEDICALRECORD_ID as medicalrecordId, t.PATIENT_NAME   as patientName,t.IN_DATE  as inDate, t.PACT_CODE  as pactCode, t.DEPT_CODE  as deptCode, t.BEDINFO_ID  as bedId, t.nurse_cell_code as nurseCellCode,t.PREPAY_COST   as prepayCost, t.PAY_COST  as payCost, t.OWN_COST  as ownCost,");
			sql.append(" t.TOT_COST  as totCost, t.PUB_COST    as pubCost, t.FREE_COST   as freeCost,t.IN_STATE as inState,  t.bed_name as bedName,t.dept_name  as deptName, t.nurse_cell_name  as nurseCellName, t.REPORT_SEX_NAME  as reportSexName, t.REPORT_AGE   as reportAge,t.REPORT_AGEUNIT  as reportAgeUnit,p.UNIT_NAME as pactName, t.OUT_DATE  as outDate, t.BALANCE_COST as balanceCost,t.PAYKIND_CODE  as paykindCode,");
			sql.append("  t.BALANCE_DATE  as balanceDate, t.BALANCE_PREPAY   as balancePrepay, t.PATIENT_STATUS   as patientStatus,t.DIAG_NAME  as diagName ,t.REPORT_BIRTHDAY as reportBirthday from HONRYHIS.T_INPATIENT_INFO_NOW t ");
			sql.append(" left join HONRYHIS.T_BUSINESS_CONTRACTUNIT p on p.unit_code = t.PACT_CODE where 1=1 ");
			if(entity!=null){
				if(StringUtils.isNotBlank(entity.getInpatientNo())){
					sql.append(" AND t.INPATIENT_NO =:inpatientNo ");
				}
				if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
					sql.append(" AND t.MEDICALRECORD_ID =:medicalrecordId ");
				}
			}
			if("O".equals(a)){
				String sqls="select count(1) from ( "+sql.toString()+" )";
				Map<String, Object> paraMap = new HashMap<String, Object>();
				if(entity!=null){
					if(StringUtils.isNotBlank(entity.getInpatientNo())){
						paraMap.put("inpatientNo", entity.getInpatientNo());
					}
					if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
						paraMap.put("medicalrecordId", entity.getMedicalrecordId());
					}
				}
				int total=namedParameterJdbcTemplate.queryForObject(sqls, paraMap, java.lang.Integer.class);
				if(total==0){
					sql.delete(0, sql.length());
					sql.append(" select t.INPATIENT_ID   as id, t.INPATIENT_NO  as inpatientNo, t.MEDICALRECORD_ID as medicalrecordId, t.PATIENT_NAME   as patientName,t.IN_DATE  as inDate, t.PACT_CODE  as pactCode, t.DEPT_CODE  as deptCode, t.BEDINFO_ID  as bedId, t.nurse_cell_code as nurseCellCode,t.PREPAY_COST   as prepayCost, t.PAY_COST  as payCost, t.OWN_COST  as ownCost,");
					sql.append(" t.TOT_COST  as totCost, t.PUB_COST    as pubCost, t.FREE_COST   as freeCost,t.IN_STATE as inState,  t.bed_name as bedName,t.dept_name  as deptName, t.nurse_cell_name  as nurseCellName,  t.REPORT_SEX_NAME  as reportSexName,  t.REPORT_AGE   as reportAge,t.REPORT_AGEUNIT  as reportAgeUnit ,p.UNIT_NAME as pactName, t.OUT_DATE  as outDate, t.BALANCE_COST as balanceCost,t.PAYKIND_CODE  as paykindCode,");
					sql.append("  t.BALANCE_DATE  as balanceDate, t.BALANCE_PREPAY   as balancePrepay, t.PATIENT_STATUS   as patientStatus,t.DIAG_NAME as diagName,t.REPORT_BIRTHDAY as reportBirthday from HONRYHIS.T_INPATIENT_INFO t ");
					sql.append(" left join HONRYHIS.T_BUSINESS_CONTRACTUNIT p on p.unit_code = t.PACT_CODE where 1=1 ");
					if(entity!=null){
						if(StringUtils.isNotBlank(entity.getInpatientNo())){
							sql.append("AND t.INPATIENT_NO =:inpatientNo ");
						}
						if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
							sql.append("AND t.MEDICALRECORD_ID =:medicalrecordId ");
						}
					}
				}
			}
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			if(entity!=null){
				if(StringUtils.isNotBlank(entity.getInpatientNo())){
					paraMap.put("inpatientNo", entity.getInpatientNo());
				}
				if(StringUtils.isNotBlank(entity.getMedicalrecordId())){
					paraMap.put("medicalrecordId", entity.getMedicalrecordId());
				}
			}
			List<InpatientInfosVo> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientInfosVo>() {
				@Override
				public InpatientInfosVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					InpatientInfosVo vo = new InpatientInfosVo();
					vo.setId(rs.getString("id"));
					vo.setInpatientNo(rs.getString("inpatientNo"));
					vo.setMedicalrecordId(rs.getString("medicalrecordId"));
					vo.setPatientName(rs.getString("patientName"));
					vo.setInDate(rs.getTimestamp("inDate"));
					vo.setPactCode(rs.getString("pactCode"));
					vo.setDeptCode(rs.getString("deptCode"));
					vo.setBedId(rs.getString("bedId"));
					vo.setNurseCellCode(rs.getString("nurseCellCode"));
					vo.setPrepayCost(rs.getDouble("prepayCost"));
					vo.setPayCost(rs.getDouble("payCost"));
					vo.setOwnCost(rs.getDouble("ownCost"));
					vo.setTotCost(rs.getDouble("totCost"));
					vo.setPubCost(rs.getDouble("pubCost"));
					vo.setFreeCost(rs.getDouble("freeCost"));
					vo.setInState(rs.getString("inState"));
					vo.setBedName(rs.getString("bedName"));
					vo.setDeptName(rs.getString("deptName"));
					vo.setNurseCellName(rs.getString("nurseCellName"));
					vo.setReportSexName(rs.getString("reportSexName"));
					vo.setReportBirthday(rs.getTimestamp("reportBirthday"));
					vo.setReportAge(rs.getInt("reportAge")+" "+rs.getString("reportAgeUnit"));
					vo.setPactName(StringUtils.isNotBlank(rs.getString("pactName"))?rs.getString("pactName"):"自费");
					vo.setOutDate(rs.getTimestamp("outDate"));
					vo.setBalanceCost(rs.getDouble("balanceCost"));
					vo.setPaykindCode(rs.getString("paykindCode"));
					vo.setBalanceDate(rs.getTimestamp("balanceDate"));
					vo.setBalancePrepay(rs.getDouble("balancePrepay"));
					vo.setPatientStatus(rs.getString("patientStatus"));
					vo.setDiagName(rs.getString("diagName"));
					return vo;
			}});
			return voList;	
	}
	@Override
	public List<InpatientMedicineList> queryMedicineList(String inpatientNo, String a) throws Exception{
		StringBuffer sql=new StringBuffer("select t.drug_name,t.specs,t.unit_price, t.qty, t.days, t.current_unit,t.tot_cost, t.own_cost,");
		sql.append("t.pub_cost,t.pay_cost, t.eco_cost,t.execute_deptname, t.inhos_deptname, t.fee_date,t.fee_opercode,t.senddrug_date,t.senddrug_opercode ");
		sql.append(" from t_inpatient_medicinelist_now t  where stop_flg = 0 and del_flg = 0 and INPATIENT_NO=:inpatientNo");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		if("O".equals(a)){
			String sqls="select count(1) from t_inpatient_medicinelist_now where  stop_flg = 0 and del_flg = 0 and INPATIENT_NO=:inpatientNo";
			int total=namedParameterJdbcTemplate.queryForObject(sqls, paraMap, java.lang.Integer.class);
			if(total==0){
				sql.delete(0, sql.length());
				sql.append(" select t.drug_name,t.specs,t.unit_price, t.qty, t.days, t.current_unit,t.tot_cost, t.own_cost,");
				sql.append(" t.pub_cost,t.pay_cost, t.eco_cost,t.execute_deptname, t.inhos_deptname, t.fee_date,t.fee_opercode,t.senddrug_date,t.senddrug_opercode  ");
				sql.append(" from t_inpatient_medicinelist t  where stop_flg = 0 and del_flg = 0 and INPATIENT_NO=:inpatientNo");
			}
		}
		List<InpatientMedicineList> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientMedicineList>() {
			@Override
			public InpatientMedicineList mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientMedicineList vo = new InpatientMedicineList();
				vo.setDrug_name(rs.getString("drug_name"));
				vo.setSpecs(rs.getString("specs"));
				vo.setUnitPrice(rs.getDouble("unit_price"));
				vo.setQty(rs.getDouble("qty"));
				vo.setDays(rs.getInt("days"));
				vo.setCurrentUnit(rs.getString("current_unit"));
				vo.setTotCost(rs.getDouble("tot_cost"));
				vo.setOwnCost(rs.getDouble("own_cost"));
				vo.setPubCost(rs.getDouble("pub_cost"));
				vo.setPayCost(rs.getDouble("pay_cost"));
				vo.setEcoCost(rs.getDouble("eco_cost"));
				vo.setExecuteDeptname(rs.getString("execute_deptname"));
				vo.setInhosDeptname(rs.getString("inhos_deptname"));
				vo.setFeeDate(rs.getTimestamp("fee_date"));
				vo.setFeeOpercode(rs.getString("fee_opercode"));
				vo.setSenddrugDate(rs.getTimestamp("senddrug_date"));
				vo.setSenddrugOpercode(rs.getString("senddrug_opercode"));
				return vo;
		}});
		return voList;	
	}
	@Override
	public List<InpatientItemList> queryItemList(String inpatientNo, String a) throws Exception{
		StringBuffer sql=new StringBuffer("select ITEM_NAME  as itemName ,UNIT_PRICE as unitPrice,QTY as qty,CURRENT_UNIT as currentUnit,");
		sql.append(" TOT_COST as totCost,OWN_COST  as ownCost,PUB_COST as pubCost,PAY_COST as payCost,ECO_COST as ecoCost,");
		sql.append(" EXECUTE_DEPTNAME as executeDeptname,INHOS_DEPTNAME as inhosDeptname,FEE_DATE as feeDate,FEE_OPERCODE as feeOpercode  from T_INPATIENT_ITEMLIST_NOW");
		sql.append(" where stop_flg = 0 and del_flg = 0 and INPATIENT_NO=:inpatientNo");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		if("O".equals(a)){
			String sqls="select count(1) from T_INPATIENT_ITEMLIST_NOW where INPATIENT_NO=:inpatientNo";
			int total=namedParameterJdbcTemplate.queryForObject(sqls, paraMap, java.lang.Integer.class);
			if(total==0){
				sql.delete(0, sql.length());
				sql.append(" select ITEM_NAME  as itemName ,UNIT_PRICE as unitPrice,QTY as qty,CURRENT_UNIT as currentUnit,");
				sql.append(" TOT_COST as totCost,OWN_COST  as ownCost,PUB_COST as pubCost,PAY_COST as payCost,ECO_COST as ecoCost,");
				sql.append(" EXECUTE_DEPTNAME as executeDeptname,INHOS_DEPTNAME as inhosDeptname,FEE_DATE as feeDate,FEE_OPERCODE as feeOpercode  from T_INPATIENT_ITEMLIST");
				sql.append(" where stop_flg = 0 and del_flg = 0 and INPATIENT_NO=:inpatientNo");
			}
		}
		List<InpatientItemList> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientItemList>() {
			@Override
			public InpatientItemList mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientItemList vo = new InpatientItemList();
				vo.setItemName(rs.getString("itemName"));
				vo.setUnitPrice(rs.getDouble("unitPrice"));
				vo.setQty(rs.getDouble("qty"));
				vo.setCurrentUnit(rs.getString("currentUnit"));
				vo.setTotCost(rs.getDouble("totCost"));
				vo.setOwnCost(rs.getDouble("ownCost"));
				vo.setPubCost(rs.getDouble("pubCost"));
				vo.setPayCost(rs.getDouble("payCost"));
				vo.setEcoCost(rs.getDouble("ecoCost"));
				vo.setExecuteDeptname(rs.getString("executeDeptname"));
				vo.setInhosDeptname(rs.getString("inhosDeptname"));
				vo.setFeeDate(rs.getTimestamp("feeDate"));
				vo.setFeeOpercode(rs.getString("feeOpercode"));
				return vo;
		}});
		return voList;	
	}
	@Override
	public List<InpatientInPrepay> queryInPrepay(String inpatientNo, String a) throws Exception{
		StringBuffer sql=new StringBuffer("select receipt_no  as receiptNo ,prepay_cost as prepayCost,pay_way as payWay,CREATEUSER as createUser,");
		sql.append(" CREATETIME as createTime,DEPT_NAME  as deptName,balance_state as balanceState,trans_flag as transFlag ");
		sql.append(" FROM T_INPATIENT_INPREPAY_NOW");
		sql.append(" where stop_flg = 0 and del_flg = 0 and inpatient_no=:inpatientNo");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		if("O".equals(a)){
			String sqls="select count(1) from T_INPATIENT_INPREPAY_NOW where  stop_flg = 0 and del_flg = 0 and INPATIENT_NO=:inpatientNo";
			int total=namedParameterJdbcTemplate.queryForObject(sqls, paraMap, java.lang.Integer.class);
			if(total==0){
				sql.delete(0, sql.length());
				sql.append(" select receipt_no  as receiptNo ,prepay_cost as prepayCost,pay_way as payWay,CREATEUSER as createUser,");
				sql.append(" CREATETIME as createTime,DEPT_NAME  as deptName,balance_state as balanceState,trans_flag as transFlag ");
				sql.append(" FROM T_INPATIENT_INPREPAY ");
				sql.append(" where stop_flg = 0 and del_flg = 0 and inpatient_no=:inpatientNo");
			}
		}
		List<InpatientInPrepay> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientInPrepay>() {
			@Override
			public InpatientInPrepay mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientInPrepay vo = new InpatientInPrepay();
				vo.setReceiptNo(rs.getString("receiptNo"));
				vo.setPrepayCost(rs.getDouble("prepayCost"));
				vo.setPayWay(rs.getString("payWay"));
				vo.setCreateUser(rs.getString("createUser"));
				vo.setCreateTime(rs.getTimestamp("createTime"));
				vo.setDeptName(rs.getString("deptName"));
				vo.setBalanceState(rs.getInt("balanceState"));
				vo.setTransFlag(rs.getInt("transFlag"));
				return vo;
		}});
		return voList;	
	}
	@Override
	public List<FeeInfosVo> queryFeeInfo(String inpatientNo, String a) throws Exception{
		String sql ="select a.FEE_CODE as feeCode, sum(a.tot_cost) as tot,sum(a.own_cost) as own,sum(a.pub_cost) as pub,sum(a.pay_cost) as pay,"
				+ "sum(a.eco_cost) as eco from T_INPATIENT_FEEINFO_NOW a where a.BALANCE_STATE = 0 and a.inpatient_No = :inpatientNo "
						+ "GROUP BY a.FEE_CODE ";
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		if("O".equals(a)){
			String sqls="select count(1) from T_INPATIENT_INPREPAY_NOW where stop_flg = 0 and del_flg = 0 and INPATIENT_NO=:inpatientNo";
			int total=namedParameterJdbcTemplate.queryForObject(sqls, paraMap, java.lang.Integer.class);
			if(total==0){
				sql="";
				sql ="select a.FEE_CODE as feeCode, sum(a.tot_cost) as tot,sum(a.own_cost) as own,sum(a.pub_cost) as pub,sum(a.pay_cost) as pay,"
						+ "sum(a.eco_cost) as eco from T_INPATIENT_FEEINFO a where a.BALANCE_STATE = 0 and a.inpatient_No =:inpatientNo"
								+ "GROUP BY a.FEE_CODE ";
			}
		}	
		List<FeeInfosVo> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<FeeInfosVo>() {
			@Override
			public FeeInfosVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FeeInfosVo vo = new FeeInfosVo();
			    vo.setFeeCode(rs.getString("feeCode"));
			    vo.setTot(rs.getDouble("tot"));
			    vo.setOwn(rs.getDouble("own"));
			    vo.setPub(rs.getDouble("pub"));
			    vo.setPay(rs.getDouble("pay"));
			    vo.setEco(rs.getDouble("eco"));
				return vo;
		}});
		return voList;	
	}
	@Override
	public List<InpatientBalanceHead> queryBalanceInfo(String inpatientNo, String a) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select t.invoice_no,t.tot_cost,t.supply_cost,t.return_cost,t.balance_opername,t.balance_date,t.balanceoper_deptname  ");
		sql.append("from t_inpatient_balancehead_now t where  t.stop_flg = 0 and t.del_flg = 0 and t.inpatient_No =:inpatientNo ");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("inpatientNo", inpatientNo);
		if("O".equals(a)){
			String sqls="select count(1) from t_inpatient_balancehead_now where stop_flg = 0 and del_flg = 0 and  INPATIENT_NO=:inpatientNo";
			int total=namedParameterJdbcTemplate.queryForObject(sqls, paraMap, java.lang.Integer.class);
			if(total==0){
				sql.delete(0, sql.length());
				sql.append(" select t.invoice_no,t.tot_cost,t.supply_cost,t.return_cost,t.balance_opername,t.balance_date,t.balanceoper_deptname ");
				sql.append(" from t_inpatient_balancehead t where t.stop_flg = 0 and t.del_flg = 0 and t.inpatient_No =:inpatientNo");
			}
		}	
		List<InpatientBalanceHead> voList =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<InpatientBalanceHead>() {
			@Override
			public InpatientBalanceHead mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientBalanceHead vo = new InpatientBalanceHead();
				vo.setInvoiceNo(rs.getString("invoice_no"));
				vo.setTotCost(rs.getDouble("tot_cost"));
				vo.setSupplyCost(rs.getDouble("supply_cost"));
				vo.setReturnCost(rs.getDouble("return_cost"));
				vo.setBalanceOpername(rs.getString("balance_opername"));
				vo.setBalanceDate(rs.getTimestamp("balance_date"));
				vo.setBalanceoperDeptname(rs.getString("balanceoper_deptname"));
				return vo;
		}});
		return voList;	
	}
	@Override
	public Map<String, String> queryEmployeeMap() throws Exception {
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from SysEmployee t where t.stop_flg = 0 and t.del_flg = 0";
		List<SysEmployee> sysEmployee=this.getSession().createQuery(hql).list();
		if(sysEmployee!=null&&sysEmployee.size()>0){
			for(SysEmployee cs : sysEmployee){
				dsMap.put(cs.getJobNo(), cs.getName());
			}
		}
		return dsMap;
	}
	@Override
	public Map<String, String> queryFeeNameMap() throws Exception{
		Map<String, String> dsMap = new HashMap<String, String>();
		String hql = "from MinfeeStatCode t where t.stop_flg = 0 and t.del_flg = 0";
		List<MinfeeStatCode> fee=this.getSession().createQuery(hql).list();
		if(fee!=null&&fee.size()>0){
			for(MinfeeStatCode cs : fee){
				dsMap.put(cs.getFeeStatCode(), cs.getFeeStatName());
			}
		}
		return dsMap;
	}
	@Override
	public List<InpatientBalanceList> queryBalanceList(
			String inpatientNo, String a) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append(" select paykind_code  as paykindCode ,invoice_no as invoiceNo,TOT_COST as totCost,own_cost as ownCost,");
		sql.append(" balance_opercode as balanceOpercode,balance_date  as balanceDate,STAT_NAME as  statName ");
		sql.append(" FROM T_INPATIENT_BALANCELIST_NOW");
		sql.append(" where stop_flg = 0 and del_flg = 0 and inpatient_no='"+inpatientNo+"'");
		if("O".equals(a)){
			int total = super.getSqlTotal(sql.toString());
			if(total==0){
				sql.delete(0, sql.length());
				sql.append(" select paykind_code  as paykindCode ,invoice_no as invoiceNo,TOT_COST as totCost,own_cost as ownCost,");
				sql.append(" balance_opercode as balanceOpercode,balance_date  as balanceDate,STAT_NAME as  statName ");
				sql.append(" FROM T_INPATIENT_BALANCELIST ");
				sql.append(" where stop_flg = 0 and del_flg = 0 and inpatient_no='"+inpatientNo+"'");
			}
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("paykindCode").addScalar("invoiceNo").addScalar("totCost",Hibernate.DOUBLE)
		.addScalar("ownCost",Hibernate.DOUBLE).addScalar("balanceOpercode")
		.addScalar("balanceDate",Hibernate.TIMESTAMP).addScalar("statName");
		List<InpatientBalanceList> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientBalanceList.class)).list();;
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<InpatientBalanceList>();
	}
	@Override
	public List<InpatientFeeInfo> queryFeeInfo1(String inpatientNo,
			String a) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append(" select paykind_code  as paykindCode ,invoice_no as invoiceNo,TOT_COST as totCost,own_cost as ownCost,");
		sql.append(" balance_opercode as balanceOpercode,balance_date  as balanceDate ");
		sql.append(" FROM T_INPATIENT_FEEINFO_NOW");
		sql.append(" where stop_flg = 0 and del_flg = 0 and inpatient_no='"+inpatientNo+"'");
		if("O".equals(a)){
			int total = super.getSqlTotal(sql.toString());
			if(total==0){
				sql.delete(0, sql.length());
				sql.append(" select paykind_code  as paykindCode ,invoice_no as invoiceNo,TOT_COST as totCost,own_cost as ownCost,");
				sql.append(" balance_opercode as balanceOpercode,balance_date  as balanceDate ");
				sql.append(" FROM T_INPATIENT_FEEINFO ");
				sql.append(" where stop_flg = 0 and del_flg = 0 and inpatient_no='"+inpatientNo+"'");
			}
		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql.toString());
		queryObject.addScalar("paykindCode").addScalar("invoiceNo").addScalar("totCost",Hibernate.DOUBLE)
		.addScalar("ownCost",Hibernate.DOUBLE).addScalar("balanceOpercode")
		.addScalar("balanceDate",Hibernate.TIMESTAMP);
		List<InpatientFeeInfo> list = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientFeeInfo.class)).list();;
		if(list!=null && list.size()>0){
			return list;
		}
		return new ArrayList<InpatientFeeInfo>();
	}
}
