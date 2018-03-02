package cn.honry.statistics.finance.inpatientFeeDetail.dao.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.finance.inpatientFeeDetail.dao.InpatientFeeDetailDAO;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.CostDetailsVo;
import cn.honry.statistics.finance.inpatientFeeDetail.vo.FeeDetailVo;
@Repository("inpatientFeeDetailDAO")
@SuppressWarnings({ "all" })
public class InpatientFeeDetailDAOImpl extends HibernateEntityDao<InpatientInfo>  implements InpatientFeeDetailDAO{
	private static final int List = 0;
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
	public List<FeeDetailVo> queryFeeInfo(InpatientInfo inpatientInfo) throws Exception{
		String sql = "select feeCode, feeName, totcost from (SELECT '00' as feeCode,'预交金' as feeName,sum(a.prepay_cost) as totcost FROM T_INPATIENT_INPREPAY_NOW a ";
			sql +="where (a.inpatient_no = :inpatientNo) having sum(a.prepay_cost) <> 0 union all select a.FEE_STAT_CODE as feeCode,a.FEE_STAT_NAME as feeName, ";
			sql +="sum(b.tot_cost) as totcost from T_INPATIENT_FEEINFO_NOW b, T_CHARGE_MINFEETOSTAT a where b.fee_code = a.FEE_STAT_CODE and a.report_code = 'ZY01' ";
			sql +="and (b.inpatient_no = :inpatientNo) group by a.FEE_STAT_CODE, a.FEE_STAT_NAME)";			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("inpatientNo", inpatientInfo.getInpatientNo());
			List<FeeDetailVo> feeDetail =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<FeeDetailVo>() {
				@Override
				public FeeDetailVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					FeeDetailVo vo = new FeeDetailVo();
					vo.setFeeCode(rs.getString("feeCode"));
					vo.setFeeName(rs.getString("feeName"));
					vo.setTotcost(rs.getDouble("totcost"));
					return vo;
			}});
		if(feeDetail!=null && feeDetail.size()>0){
			double totCost = 0;
			for(int i = 0;i<feeDetail.size();i++){
				if("预交金".equals(feeDetail.get(i).getFeeName())){
					continue;
				}
				totCost = totCost+feeDetail.get(i).getTotcost();
			}
			FeeDetailVo feeDetailVo = new FeeDetailVo();
			feeDetailVo.setFeeCode("费用合计：");
			
			DecimalFormat df = new DecimalFormat("#.00");   
			feeDetailVo.setTotcost(Double.parseDouble(df.format(totCost)));
			feeDetail.add(feeDetailVo);
			return feeDetail;
		}  
		return new ArrayList<FeeDetailVo>();
	}

	@Override
	public List<CostDetailsVo> queryCostDetails(InpatientInfo inpatientInfo) throws Exception{
		String sql = "select t.inpatientNo,t.recipeNo,t.itemCode,t.itemName,t.unitPrice,t.qty,t.currentUnit,t.totcost,t.feeOpercode,t.feeDate,t.itemType from ";
			sql +=" (select a.INPATIENT_NO as inpatientNo,a.RECIPE_NO as recipeNo,a.DRUG_CODE as itemCode,a.DRUG_NAME as itemName,a.UNIT_PRICE as unitPrice,a.QTY as qty,a.CURRENT_UNIT as currentUnit,a.TOT_COST as totcost,a.FEE_OPERCODE as feeOpercode,a.FEE_DATE as feeDate,'1' as itemType";
			sql +=" from T_INPATIENT_MEDICINELIST_NOW a union all";
			sql +=" select b.INPATIENT_NO as inpatientNo,b.RECIPE_NO as recipeNo,b.ITEM_CODE as itemCode,b.ITEM_NAME as itemName,b.UNIT_PRICE as unitPrice,b.QTY as qty,b.CURRENT_UNIT as currentUnit,b.TOT_COST as totcost,b.FEE_OPERCODE as feeOpercode,b.FEE_DATE as feeDate,'2' as itemType";
			sql +=" from T_INPATIENT_ITEMLIST_NOW b) t where t.inpatientNo = :inpatientNo";		
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("inpatientNo", inpatientInfo.getInpatientNo());
			List<CostDetailsVo> feeDetail =  namedParameterJdbcTemplate.query(sql.toString(),paraMap,new RowMapper<CostDetailsVo>() {
				@Override
				public CostDetailsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CostDetailsVo vo = new CostDetailsVo();
					vo.setInpatientNo(rs.getString("inpatientNo"));
					vo.setRecipeNo(rs.getString("recipeNo"));
					vo.setItemCode(rs.getString("itemCode"));
					vo.setItemName(rs.getString("itemName"));
					vo.setUnitPrice(rs.getDouble("unitPrice"));
					vo.setQty(rs.getDouble("qty"));
					vo.setCurrentUnit(rs.getString("currentUnit"));
					vo.setTotcost(rs.getDouble("totcost"));
					vo.setFeeOpercode(rs.getString("feeOpercode"));
					vo.setFeeDate(rs.getTimestamp("feeDate"));
					vo.setItemType(rs.getInt("itemType"));
					return vo;
			}});
		return feeDetail;
	}

	@Override
	public List<InpatientInfoNow> queryPatient(String deptId,
			String type, String flag) throws Exception{
		String sql = "select i.INPATIENT_NO as inpatientNo,i.PATIENT_NAME as patientName,i.REPORT_SEX as reportSex,i.BED_NAME as bedName,i.IN_STATE as inState,"
				+ "i.MEDICALRECORD_ID as medicalrecordId,i.PACT_CODE as pactCode from T_INPATIENT_INFO_NOW i where 1=1 ";
		if("1".equals(flag)){//1在院   2出院未结算
			if("N".equals(type)){
				sql += " and i.IN_STATE='I' and i.NURSE_CELL_CODE=:deptId";
			}else{
				sql += " and i.IN_STATE='I' and i.NURSE_CELL_CODE=(select tdc.DEPT_ID "
						+ "from T_DEPARTMENT_CONTACT tdc "
						+ "where tdc.id in (select dc.PARDEPT_ID "
						+ "from T_DEPARTMENT_CONTACT dc "
						+ "where dc.DEPT_ID =:deptId "
						+ "and dc.REFERENCE_TYPE = '03'))";
			}
		}else if("2".equals(flag)){
			if("N".equals(type)){
				sql += " and i.IN_STATE='B' and i.NURSE_CELL_CODE=:deptId ";
			}else{
				sql += " and i.IN_STATE='B' and i.NURSE_CELL_CODE=(select tdc.DEPT_ID "
						+ " from T_DEPARTMENT_CONTACT tdc "
						+ " where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID =:deptId "
						+ " and dc.REFERENCE_TYPE = '03')) ";
			}
		}else  if("12".equals(flag)){
			if("N".equals(type)){
				sql += " and i.NURSE_CELL_CODE=:deptId and ( i.IN_STATE='B' or  i.IN_STATE='I')";
			}else{
				sql += " and ( i.IN_STATE='B' or  i.IN_STATE='I') and i.NURSE_CELL_CODE=(select tdc.DEPT_ID "
						+ " from T_DEPARTMENT_CONTACT tdc "
						+ " where tdc.id in (select dc.PARDEPT_ID "
						+ " from T_DEPARTMENT_CONTACT dc "
						+ " where dc.DEPT_ID =:deptId "
						+ " and dc.REFERENCE_TYPE = '03')) ";
			}
		}else{
			return new ArrayList<InpatientInfoNow>();
		}
		SQLQuery queryObject = (SQLQuery) super.getSession().createSQLQuery(sql).addScalar("inpatientNo").addScalar("medicalrecordId").addScalar("pactCode")
				.addScalar("patientName").addScalar("reportSex").addScalar("bedName").addScalar("inState").setParameter("deptId", deptId);
		List<InpatientInfoNow> iList = queryObject.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class)).list();
		if(iList!=null&&iList.size()>0){
			return iList;
		}
		return new ArrayList<InpatientInfoNow>();
	}

	@Override
	public InpatientInfoNow queryFeeInpatientInfo(String inpatientNo) throws Exception{
        DetachedCriteria createCriteria =DetachedCriteria.forClass(InpatientInfoNow.class);
        createCriteria.add(Restrictions.eq("inpatientNo",inpatientNo));
       List<InpatientInfoNow> list = (List<InpatientInfoNow>) this.getHibernateTemplate().findByCriteria(createCriteria);
       if(list!=null&&list.size()>0){
    	   return list.get(0);
       }
		return new InpatientInfoNow();
	}

}
