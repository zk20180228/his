package cn.honry.statistics.leaveOrder.dao.impl;

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

import cn.honry.base.bean.model.DrugApplyoutNow;
import cn.honry.base.bean.model.InpatientExecdrugNow;
import cn.honry.base.bean.model.InpatientExecundrugNow;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.leaveOrder.dao.LeaveOrderDao;
import cn.honry.statistics.leaveOrder.vo.InpatientExecDrugVo;
import cn.honry.statistics.leaveOrder.vo.InpatientExecUnDrugVo;
import cn.honry.utils.HisParameters;

@Repository("leaveOrderDao")
@SuppressWarnings({ "all" })
public class LeaveOrderDaoImpl extends HibernateEntityDao<InpatientExecDrugVo> implements LeaveOrderDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**查询医嘱列表  药品 总条数
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public int queryDrugTotal(List<String> tnL, String queryName, String startTime, String endTime) {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sb = new StringBuffer(1500);
		sb.append("SELECT  ROWNUM AS n, prnFlag,docName,typeCode,validFlag,drugedFlag,drugName,specs,qtyTot,priceUnit,useTime,decoDate,"
				+ "chargeDate,drugedDate,moDate,frequencyName,doseOnce,packQty,useDays,useName,pharmacyCode,moNote1,moNote2,moOrder,"
				+ "combNo,execId,chargeFlag,chargeUsercd,drugedDeptcd,drugedUsercd,validUsercd,recipeNo,sequenceNo,chargePrnflag,chargePrndate,"
				+ "doseModelCode,drugCode,deptCode,nurseCellCode,listDpcd,inpatientNo FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT  ").append(" rm").append(i).append(".PRN_FLAG AS prnFlag,rm").append(i)
			.append(".DOC_NAME AS docName,rm").append(i).append(".TYPE_CODE AS typeCode,rm").append(i).append(".VALID_FLAG AS validFlag,rm").append(i)
			.append(".DRUGED_FLAG as drugedFlag, rm").append(i).append(".DRUG_NAME AS drugName,rm").append(i).append(".SPECS AS specs,rm").append(i)
			.append(".QTY_TOT AS qtyTot,rm").append(i).append(".PRICE_UNIT AS priceUnit,rm").append(i).append(".USE_TIME AS useTime,rm").append(i)
			.append(".DECO_DATE AS decoDate,rm").append(i).append(".CHARGE_DATE AS chargeDate,rm").append(i).append(".DRUGED_DATE AS drugedDate,rm").append(i)
			.append(".MO_DATE AS moDate,rm").append(i).append(".FREQUENCY_NAME AS frequencyName,rm").append(i).append(".DOSE_ONCE AS doseOnce,rm").append(i)
			.append(".PACK_QTY AS packQty,rm").append(i).append(".USE_DAYS AS useDays,rm").append(i).append(".USE_NAME AS useName,rm").append(i)
			.append(".PHARMACY_NAME AS pharmacyCode,rm").append(i).append(".MO_NOTE1 AS moNote1,rm").append(i).append(".MO_NOTE2 AS moNote2,rm").append(i)
			.append(".MO_ORDER AS moOrder,rm").append(i).append(".COMB_NO AS combNo,rm").append(i).append(".EXEC_ID AS execId,rm").append(i)
			.append(".CHARGE_FLAG AS chargeFlag,rm").append(i).append(".CHARGE_USERCD AS chargeUsercd,rm").append(i).append(".DRUGED_DEPTCD_NAME AS drugedDeptcd,rm").append(i)
			.append(".DRUGED_USERCD_NAME AS drugedUsercd,rm").append(i).append(".VALID_USERCD_NAME AS validUsercd,rm").append(i).append(".RECIPE_NO AS recipeNo,rm").append(i)
			.append(".SEQUENCE_NO AS sequenceNo,rm").append(i).append(".CHARGE_PRNFLAG AS chargePrnflag,rm").append(i).append(".CHARGE_PRNDATE AS chargePrndate,rm").append(i)
			.append(".DOSE_MODEL_CODE AS doseModelCode,rm").append(i).append(".DRUG_CODE AS drugCode,rm").append(i).append(".DEPT_NAME AS deptCode,rm").append(i)
			.append(".NURSE_CELL_NAME AS nurseCellCode,rm").append(i).append(".LIST_DPCD_NAME AS listDpcd,rm").append(i).append(".inpatient_no as inpatientNo ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append("  WHERE rm").append(i).append(".STOP_FLG = 0 and rm").append(i).append(".DEL_FLG = 0 ");
			if(StringUtils.isNotBlank(queryName)){
				sb.append(" and rm").append(i).append(".INPATIENT_NO =:queryName ");
			} 
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" and rm").append(i).append(".mo_Date >=  to_date(:startTime ,'yyyy-MM-dd') ");
				sb.append(" and rm").append(i).append(".mo_Date <=  to_date(:endTime ,'yyyy-MM-dd') ");
			}
		}
		sb.append(")" );
		Map<String, Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(queryName)&&!"1".equals(queryName)){
			paraMap.put("queryName", queryName);
		}
		if(StringUtils.isNotBlank(startTime)){
			paraMap.put("startTime", startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", endTime);
		}
		sb.insert(0, "SELECT COUNT(1) FROM (" );
		sb.append(" )");
		int total = namedParameterJdbcTemplate.queryForObject(sb.toString(), paraMap, java.lang.Integer.class);
		return total;
	}
	/**查询医嘱列表
	 * GH
	 * @param queryName  住院流水号
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 * @param page
	 * @param rows
	 */
	@Override
	public List<InpatientExecdrugNow> queryDrugList(List<String> tnL,String queryName, String startTime, String endTime,String page, String rows) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<InpatientExecdrugNow>();
		}
		final StringBuffer sb = new StringBuffer(1500);
		sb.append("SELECT * from (");
		sb.append("SELECT  ROWNUM AS n, prnFlag,docName,typeCode,validFlag,drugedFlag,drugName,specs,qtyTot,priceUnit,useTime,decoDate,"
				+ "chargeDate,drugedDate,moDate,frequencyName,doseOnce,packQty,useDays,useName,pharmacyCode,moNote1,moNote2,moOrder,"
				+ "combNo,execId,chargeFlag,chargeUsercd,drugedDeptcd,drugedUsercd,validUsercd,recipeNo,sequenceNo,chargePrnflag,chargePrndate,"
				+ "doseModelCode,drugCode,deptCode,nurseCellCode,listDpcd,inpatientNo FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT  ").append(" rm").append(i).append(".PRN_FLAG AS prnFlag,rm").append(i)
			.append(".DOC_NAME AS docName,rm").append(i).append(".TYPE_CODE AS typeCode,rm").append(i).append(".VALID_FLAG AS validFlag,rm").append(i)
			.append(".DRUGED_FLAG as drugedFlag, rm").append(i).append(".DRUG_NAME AS drugName,rm").append(i).append(".SPECS AS specs,rm").append(i)
			.append(".QTY_TOT AS qtyTot,rm").append(i).append(".PRICE_UNIT AS priceUnit,rm").append(i).append(".USE_TIME AS useTime,rm").append(i)
			.append(".DECO_DATE AS decoDate,rm").append(i).append(".CHARGE_DATE AS chargeDate,rm").append(i).append(".DRUGED_DATE AS drugedDate,rm").append(i)
			.append(".MO_DATE AS moDate,rm").append(i).append(".FREQUENCY_NAME AS frequencyName,rm").append(i).append(".DOSE_ONCE AS doseOnce,rm").append(i)
			.append(".PACK_QTY AS packQty,rm").append(i).append(".USE_DAYS AS useDays,rm").append(i).append(".USE_NAME AS useName,rm").append(i)
			.append(".PHARMACY_NAME AS pharmacyCode,rm").append(i).append(".MO_NOTE1 AS moNote1,rm").append(i).append(".MO_NOTE2 AS moNote2,rm").append(i)
			.append(".MO_ORDER AS moOrder,rm").append(i).append(".COMB_NO AS combNo,rm").append(i).append(".EXEC_ID AS execId,rm").append(i)
			.append(".CHARGE_FLAG AS chargeFlag,rm").append(i).append(".CHARGE_USERCD AS chargeUsercd,rm").append(i).append(".DRUGED_DEPTCD_NAME AS drugedDeptcd,rm").append(i)
			.append(".DRUGED_USERCD_NAME AS drugedUsercd,rm").append(i).append(".VALID_USERCD_NAME AS validUsercd,rm").append(i).append(".RECIPE_NO AS recipeNo,rm").append(i)
			.append(".SEQUENCE_NO AS sequenceNo,rm").append(i).append(".CHARGE_PRNFLAG AS chargePrnflag,rm").append(i).append(".CHARGE_PRNDATE AS chargePrndate,rm").append(i)
			.append(".DOSE_MODEL_CODE AS doseModelCode,rm").append(i).append(".DRUG_CODE AS drugCode,rm").append(i).append(".DEPT_NAME AS deptCode,rm").append(i)
			.append(".NURSE_CELL_NAME AS nurseCellCode,rm").append(i).append(".LIST_DPCD_NAME AS listDpcd,rm").append(i).append(".inpatient_no as inpatientNo ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append("  WHERE rm").append(i).append(".STOP_FLG = 0 and rm").append(i).append(".DEL_FLG = 0 ");
			if(StringUtils.isNotBlank(queryName)){
				sb.append(" and rm").append(i).append(".INPATIENT_NO =:queryName ");
			} 
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" and rm").append(i).append(".mo_Date >=  to_date(:startTime ,'yyyy-MM-dd') ");
				sb.append(" and rm").append(i).append(".mo_Date <=  to_date(:endTime ,'yyyy-MM-dd') ");
			}
		}
		sb.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		if(StringUtils.isNotBlank(queryName)&&!"1".equals(queryName)){
			paraMap.put("queryName", queryName);
		}
		if(StringUtils.isNotBlank(startTime)){
			paraMap.put("startTime", startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", endTime);
		}
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<InpatientExecdrugNow> list =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<InpatientExecdrugNow>() {
			@Override
			public InpatientExecdrugNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientExecdrugNow vo = new InpatientExecdrugNow();
				vo.setPrnFlag(rs.getObject("prnFlag")==null?null:rs.getInt("prnFlag"));
				vo.setDocName(rs.getString("docName"));
				vo.setTypeCode(rs.getString("typeCode"));
				vo.setValidFlag(rs.getObject("validFlag")==null?null:rs.getInt("validFlag"));
				vo.setDrugedFlag(rs.getObject("drugedFlag")==null?null:rs.getInt("drugedFlag"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setSpecs(rs.getString("specs"));
				vo.setQtyTot(rs.getObject("qtyTot")==null?null:rs.getDouble("qtyTot"));
				vo.setPriceUnit(rs.getString("priceUnit"));
				vo.setUseTime(rs.getTimestamp("useTime"));
				vo.setDecoDate(rs.getTimestamp("decoDate"));
				vo.setChargeDate(rs.getTimestamp("chargeDate"));
				vo.setDrugedDate(rs.getTimestamp("drugedDate"));
				vo.setMoDate(rs.getTimestamp("moDate"));
				vo.setFrequencyName(rs.getString("frequencyName"));
				vo.setDoseOnce(rs.getObject("doseOnce")==null?null:rs.getDouble("doseOnce"));
				vo.setPackQty(rs.getObject("packQty")==null?null:rs.getInt("packQty"));
				vo.setUseDays(rs.getObject("useDays")==null?null:rs.getInt("useDays"));
				vo.setUseName(rs.getString("useName"));
				vo.setPharmacyCode(rs.getString("pharmacyCode"));
				vo.setMoNote1(rs.getString("moNote1"));
				vo.setMoNote2(rs.getString("moNote2"));
				vo.setMoOrder(rs.getString("moOrder"));
				vo.setCombNo(rs.getString("combNo"));
				vo.setExecId(rs.getString("execId"));
				vo.setChargeFlag(rs.getObject("chargeFlag")==null?null:rs.getInt("chargeFlag"));
				vo.setChargeUsercd(rs.getString("chargeUsercd"));
				vo.setDrugedDeptcd(rs.getString("drugedDeptcd"));
				vo.setDrugedUsercd(rs.getString("drugedUsercd"));
				vo.setValidUsercd(rs.getString("validUsercd"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setSequenceNo(rs.getObject("sequenceNo")==null?null:rs.getInt("sequenceNo"));
				vo.setChargePrnflag(rs.getObject("chargePrnflag")==null?null:rs.getInt("chargePrnflag"));
				vo.setChargePrndate(rs.getTimestamp("chargePrndate"));
				vo.setDoseModelCode(rs.getString("doseModelCode"));
				vo.setDrugCode(rs.getString("drugCode"));
				vo.setDeptCode(rs.getString("deptCode"));
				vo.setNurseCellCode(rs.getString("nurseCellCode"));
				vo.setListDpcd(rs.getString("listDpcd"));
				return vo;
		}});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientExecdrugNow>();
	}
	
	
	
	/**查询医嘱列表  非药品 总条数
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public int queryUnDrugTotal(List<String> tnL,String queryName, String startTime,String endTime) {
		if(tnL==null||tnL.size()<0){
			return 0;
		}
		final StringBuffer sb = new StringBuffer(1500);
		sb.append("SELECT  ROWNUM AS n,docName,typeCode,validFlag,undrugName,stockUnit,useTime,decoDate,chargeDate,moDate,validDate,dfqCexp,moNote1,"
				+ "moNote2,moOrder,combNo,execId,subtblFlag,execDpnm,chargeFlag,chargeUsercd,validUsercd,sequenceNo,recipeNo,inpatientNo FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT  ").append(" rm").append(i)
			.append(".DOC_NAME AS docName,rm").append(i).append(".TYPE_CODE AS typeCode,rm").append(i).append(".VALID_FLAG AS validFlag,rm").append(i)
			.append(".UNDRUG_NAME AS undrugName,rm").append(i).append(".STOCK_UNIT AS stockUnit,rm").append(i)
			.append(".USE_TIME AS useTime,rm").append(i).append(".DECO_DATE AS decoDate,rm").append(i)
			.append(".CHARGE_DATE AS chargeDate,rm").append(i).append(".MO_DATE AS moDate,rm").append(i)
			.append(".VALID_DATE AS validDate,rm").append(i).append(".DFQ_CEXP AS dfqCexp,rm").append(i)
			.append(".MO_NOTE1 AS moNote1,rm").append(i).append(".MO_NOTE2 AS moNote2,rm").append(i)
			.append(".MO_ORDER AS moOrder,rm").append(i).append(".COMB_NO AS combNo,rm").append(i).append(".EXEC_ID AS execId,rm").append(i)
			.append(".SUBTBL_FLAG AS subtblFlag,rm").append(i).append(".EXEC_DPNM AS execDpnm,rm").append(i)
			.append(".CHARGE_FLAG AS chargeFlag,rm").append(i).append(".CHARGE_USERCD_NAME AS chargeUsercd,rm").append(i)
			.append(".VALID_USERCD_NAME AS validUsercd,rm").append(i).append(".SEQUENCE_NO AS sequenceNo,rm").append(i)
			.append(".RECIPE_NO AS recipeNo,rm").append(i).append(".inpatient_no as inpatientNo ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append("  WHERE rm").append(i).append(".STOP_FLG = 0 and rm").append(i).append(".DEL_FLG = 0 ");
			if(StringUtils.isNotBlank(queryName)){
				sb.append(" and rm").append(i).append(".INPATIENT_NO =:queryName ");
			} 
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" and rm").append(i).append(".mo_Date >=  to_date(:startTime ,'yyyy-MM-dd') ");
				sb.append(" and rm").append(i).append(".mo_Date <=  to_date(:endTime ,'yyyy-MM-dd') ");
			}
		}
		sb.append(")" );
		Map<String,Object> paraMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(queryName)&&!"1".equals(queryName)){
			paraMap.put("queryName", queryName);
		}
		if(StringUtils.isNotBlank(startTime)){
			paraMap.put("startTime", startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", endTime);
		}
	
		sb.insert(0, "SELECT COUNT(1) FROM (" );
		sb.append(" )");
		int total = namedParameterJdbcTemplate.queryForObject(sb.toString(), paraMap, java.lang.Integer.class);
		return total;
	}
	
	/**查询医嘱列表 非  药品 总数据
	 * GH
	 * @param queryName  病历号或住院流水
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public List<InpatientExecundrugNow> queryUnDrugList(List<String> tnL, String queryName,String startTime, String endTime, String page, String rows) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<InpatientExecundrugNow>();
		}
		final StringBuffer sb = new StringBuffer(1500);
		sb.append("SELECT * from (");
		sb.append("SELECT  ROWNUM AS n,docName,typeCode,validFlag,undrugName,stockUnit,useTime,decoDate,chargeDate,moDate,validDate,dfqCexp,moNote1,"
				+ "moNote2,moOrder,combNo,execId,subtblFlag,execDpnm,chargeFlag,chargeUsercd,validUsercd,sequenceNo,recipeNo,inpatientNo FROM( ");
		for(int i=0;i<tnL.size();i++){
			if(i!=0){
				sb.append(" UNION ALL ");
			}
			sb.append("SELECT  ").append(" rm").append(i)
			.append(".DOC_NAME AS docName,rm").append(i).append(".TYPE_CODE AS typeCode,rm").append(i).append(".VALID_FLAG AS validFlag,rm").append(i)
			.append(".UNDRUG_NAME AS undrugName,rm").append(i).append(".STOCK_UNIT AS stockUnit,rm").append(i)
			.append(".USE_TIME AS useTime,rm").append(i).append(".DECO_DATE AS decoDate,rm").append(i)
			.append(".CHARGE_DATE AS chargeDate,rm").append(i).append(".MO_DATE AS moDate,rm").append(i)
			.append(".VALID_DATE AS validDate,rm").append(i).append(".DFQ_CEXP AS dfqCexp,rm").append(i)
			.append(".MO_NOTE1 AS moNote1,rm").append(i).append(".MO_NOTE2 AS moNote2,rm").append(i)
			.append(".MO_ORDER AS moOrder,rm").append(i).append(".COMB_NO AS combNo,rm").append(i).append(".EXEC_ID AS execId,rm").append(i)
			.append(".SUBTBL_FLAG AS subtblFlag,rm").append(i).append(".EXEC_DPNM AS execDpnm,rm").append(i)
			.append(".CHARGE_FLAG AS chargeFlag,rm").append(i).append(".CHARGE_USERCD_NAME AS chargeUsercd,rm").append(i)
			.append(".VALID_USERCD_NAME AS validUsercd,rm").append(i).append(".SEQUENCE_NO AS sequenceNo,rm").append(i)
			.append(".RECIPE_NO AS recipeNo,rm").append(i).append(".inpatient_no as inpatientNo ");
			sb.append("FROM ").append(HisParameters.HISPARSCHEMAHISUSER).append(tnL.get(i)).append(" rm").append(i)
			.append("  WHERE rm").append(i).append(".STOP_FLG = 0 and rm").append(i).append(".DEL_FLG = 0 ");
			if(StringUtils.isNotBlank(queryName)){
				sb.append(" and rm").append(i).append(".INPATIENT_NO =:queryName ");
			} 
			if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
				sb.append(" and rm").append(i).append(".mo_Date >=  to_date(:startTime ,'yyyy-MM-dd') ");
				sb.append(" and rm").append(i).append(".mo_Date <=  to_date(:endTime ,'yyyy-MM-dd') ");
			}
		}
		sb.append(") where rownum <= :page * :rows )   where n > (:page - 1) * :rows");
		Map<String,Object> paraMap = new HashMap<String, Object>();
		int start = Integer.parseInt(page == null ? "1" : page);
		int count = Integer.parseInt(rows == null ? "20" : rows);
		if(StringUtils.isNotBlank(queryName)&&!"1".equals(queryName)){
			paraMap.put("queryName", queryName);
		}
		if(StringUtils.isNotBlank(startTime)){
			paraMap.put("startTime", startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			paraMap.put("endTime", endTime);
		}
		paraMap.put("page", start);
		paraMap.put("rows", count);
		List<InpatientExecundrugNow> list =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<InpatientExecundrugNow>() {
			@Override
			public InpatientExecundrugNow mapRow(ResultSet rs, int rowNum) throws SQLException {
				InpatientExecundrugNow vo = new InpatientExecundrugNow();
				vo.setDocName(rs.getString("docName"));
				vo.setTypeCode(rs.getString("typeCode"));
				vo.setValidFlag(rs.getObject("validFlag")==null?null:rs.getInt("validFlag"));
				vo.setUndrugName(rs.getString("undrugName"));
				vo.setStockUnit(rs.getString("stockUnit"));
				vo.setUseTime(rs.getTimestamp("useTime"));
				vo.setDecoDate(rs.getTimestamp("decoDate"));
				vo.setChargeDate(rs.getTimestamp("chargeDate"));
				vo.setMoDate(rs.getTimestamp("moDate"));
				vo.setValidDate(rs.getTimestamp("validDate"));
				vo.setDfqCexp(rs.getString("dfqCexp"));
				vo.setMoNote1(rs.getString("moNote1"));
				vo.setMoNote2(rs.getString("moNote2"));
				vo.setMoOrder(rs.getString("moOrder"));
				vo.setCombNo(rs.getString("combNo"));
				vo.setExecId(rs.getString("execId"));
				vo.setSubtblFlag(rs.getObject("subtblFlag")==null?null:rs.getInt("subtblFlag"));
				vo.setExecDpnm(rs.getString("execDpnm"));
				vo.setChargeFlag(rs.getObject("chargeFlag")==null?null:rs.getInt("chargeFlag"));
				vo.setChargeUsercd(rs.getString("chargeUsercd"));
				vo.setValidUsercd(rs.getString("validUsercd"));
				vo.setRecipeNo(rs.getString("recipeNo"));
				vo.setSequenceNo(rs.getObject("sequenceNo")==null?null:rs.getInt("sequenceNo"));
				return vo;
		}});
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<InpatientExecundrugNow>();
	}
	
	/**查询住院信息   标识 已出院    
	 * GH
	 * @param queryLsh  住院流水
	 * @param queryBlh  病历号或
	 * @param startTime  开始时间
	 * @param endTime	 结束时间
	 */
	@Override
	public List<InpatientInfoNow> queryInfoNows(String queryBlh,String queryLsh,String startTime, String endTime) {
		StringBuffer sqlnew=new StringBuffer();
		StringBuffer sqlold=new StringBuffer();
		sqlnew.append("select t.INPATIENT_NO as inpatientNo,t.OUT_DATE as outDate,t.IN_DATE as inDate from T_INPATIENT_INFO_NOW t where t.IN_STATE ='O'");
		sqlold.append("select t1.INPATIENT_NO as inpatientNo,t1.OUT_DATE as outDate,t1.IN_DATE as inDate  from T_INPATIENT_INFO t1 where t1.IN_STATE ='O'");
		
		if(StringUtils.isNotBlank(queryBlh)){
			sqlnew.append(" and  t.MEDICALRECORD_ID='"+queryBlh+"' ");
			sqlold.append(" and  t1.MEDICALRECORD_ID='"+queryBlh+"' ");
		}
		sqlnew.append(" union all ");
		sqlnew.append(sqlold.toString());
		SQLQuery query=this.getSession().createSQLQuery(sqlnew.toString());
		query.addScalar("inpatientNo").addScalar("outDate",Hibernate.DATE).addScalar("inDate",Hibernate.DATE);
		query.setResultTransformer(Transformers.aliasToBean(InpatientInfoNow.class));
		return query.list();
	}
}
