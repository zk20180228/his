package cn.honry.inner.outpatient.outpatientDataMigration.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.inner.outpatient.outpatientDataMigration.dao.OutpatientDataMigrationDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

@Repository("outpatientDataMigrationDAO")
@SuppressWarnings({"all"})
public class OutpatientDataMigrationDAOImpl implements OutpatientDataMigrationDAO{
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private Logger logger=Logger.getLogger(OutpatientDataMigrationDAOImpl.class);
	@Override
	public Map<String, Object> queryClincCodeNows(String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pMap = new HashMap<String, Object>();
		StringBuffer sBuffer;
		Query queryObject;
		if(StringUtils.isBlank(date)){//传入时间为空则查询最大与最小时间
			Integer vailueData = this.queryParameter() - 1;
			Date nowDate = new Date(); 
			String dateString = DateUtils.formatDateY_M_SLASH(DateUtils.addDay(nowDate, vailueData * -1));
			pMap.put("dateString", dateString);
			sBuffer = new StringBuffer("SELECT TO_CHAR(MAX(REG_DATE),'yyyy/mm/dd') AS MAXDATE, TO_CHAR(MIN(REG_DATE),'yyyy/mm/dd') AS MINDATE"
					+ " FROM T_REGISTER_MAIN_NOW T WHERE REG_DATE < TO_DATE(:dateString,'yyyy/mm/dd')"
					+ " AND REG_DATE >= TO_DATE('2016/01/01', 'yyyy/mm/dd')");
			map = namedParameterJdbcTemplate.queryForMap(sBuffer.toString(), pMap);
			//将map中的key由大写转为小写
			map.put("minDate", map.get("MINDATE"));
			map.put("maxDate", map.get("MAXDATE"));
			logger.info("[sql: " + sBuffer.toString() +"]");
			logger.info("[result: " + map.toString() +"]");
			logger.info("[parem: " + pMap.toString() +"]");
		}else{//传入时间不为空时查询改天内所有的挂号信息
			pMap.put("vailueData", date);
			Date endDate = DateUtils.addDay(DateUtils.parseDateY_M_D_SLASH(date), 1);
			String endString = DateUtils.formatDateY_M_SLASH(endDate);
			pMap.put("endDate", endString);
			sBuffer = new StringBuffer("SELECT DISTINCT T.CLINIC_CODE FROM T_REGISTER_MAIN_NOW T"
					+ " WHERE REG_DATE >= TO_DATE(:vailueData, 'yyyy/mm/dd') AND REG_DATE < TO_DATE(:endDate,'yyyy/mm/dd')");
			List<String> clincCodes = namedParameterJdbcTemplate.queryForList(sBuffer.toString(), pMap, String.class);
			if(clincCodes != null && clincCodes.size() > 0){
				map.put("clincCodes", clincCodes);
			}
			logger.info("[sql: " + sBuffer.toString() +"]");
			logger.info("[result: " + map.toString() +"]");
			logger.info("[parem: " + pMap.toString() +"]");
		}
		return map;
	}

	@Override
	public List<String> queryInvoiceNoNows(String date) {
		Map<String, Object> map = this.queryClincCodeNows(date);
		Map<String, Object> pMap = new HashMap<>();
		List<String> clinics = (List<String>) map.get("clincCodes");
		if(clinics == null || clinics.size() == 0){
			return null;
		}
		pMap.put("list", clinics);
		StringBuffer sBuffer = new StringBuffer("SELECT DISTINCT T.INVOICE_NO FROM T_REGISTER_MAIN_NOW T"
				+ " WHERE T.CLINIC_CODE IN (:list) AND T.INVOICE_NO IS NOT NULL");
		List<String> invoiceNos = namedParameterJdbcTemplate.queryForList(sBuffer.toString(), pMap, String.class);
		logger.info("[sql: " + sBuffer.toString() +"]");
		logger.info("[result: " + invoiceNos.toString() +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		if(invoiceNos != null && invoiceNos.size() > 0){
			return invoiceNos; 
		}
		return null;
	}

	@Override
	public List<String> queryRecipeNoNows(String date) {
		Map<String, Object> map = this.queryClincCodeNows(date);
		Map<String, Object> pMap = new HashMap<>();
		List<String> clinics = (List<String>) map.get("clincCodes");
		if(clinics == null || clinics.size() == 0){
			return null;
		}
		pMap.put("list", clinics);
		StringBuffer sBuffer = new StringBuffer("SELECT DISTINCT T.RECIPE_NO FROM T_STO_RECIPE_NOW T"
				+ " WHERE T.CLINIC_CODE IN (:list)");
		List<String> recipeNos = namedParameterJdbcTemplate.queryForList(sBuffer.toString(), pMap, String.class);
		logger.info("[sql: " + sBuffer.toString() +"]");
		logger.info("[result: " + recipeNos.toString() +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		if(recipeNos != null && recipeNos.size() > 0){
			return recipeNos; 
		}
		return null;
	}

	@Override
	public long queryOutStoreNows(String date,String state) {
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			List<String> recipeNos = this.queryRecipeNoNows(date);
			if(recipeNos == null || recipeNos.size() == 0){
				return 0;
			}
			pMap.put("list", recipeNos);
			sql = "SELECT COUNT(1) FROM T_DRUG_OUTSTORE_NOW WHERE RECIPE_NO in (:list) AND OP_TYPE IN (1,3)";
		}else {
			sql = "SELECT COUNT(1) FROM T_DRUG_OUTSTORE_MID WHERE OP_TYPE IN (1,3)";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryApplyOutNows(String date,String state) {
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			List<String> clincCodes = (List<String>) this.queryClincCodeNows(date).get("clincCodes");
			if(clincCodes == null || clincCodes.size() == 0){
				return 0;
			}
			pMap.put("list", clincCodes);
			sql = "SELECT COUNT(1) FROM T_DRUG_APPLYOUT_NOW WHERE PATIENT_ID in (:list) AND OP_TYPE IN (1,3)";
		}else {
			sql = "SELECT COUNT(1) FROM T_DRUG_APPLYOUT_MID WHERE OP_TYPE IN (1,3)";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryInpatientCancelitemNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			List<String> recipeNos = this.queryRecipeNoNows(date);
			if(recipeNos == null || recipeNos.size() == 0){
				return 0;
			}
			pMap.put("list", recipeNos);
			sql = "SELECT COUNT(1) FROM T_INPATIENT_CANCELITEM_NOW WHERE RECIPE_NO in (:list) AND APPLY_FLAG = 1";
		}else {
			sql = "SELECT COUNT(1) FROM T_INPATIENT_CANCELITEM_MID WHERE APPLY_FLAG = 1";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
		
	}

	@Override
	public long queryFinanceInvoicedetailNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			List<String> invoiceNos = this.queryInvoiceNoNows(date);
			if(invoiceNos == null || invoiceNos.size() == 0){
				return 0;
			}
			pMap.put("list", invoiceNos);
			sql = "SELECT COUNT(1) FROM T_FINANCE_INVOICEDETAIL_NOW WHERE INVOICE_NO in (:list)";
		}else {
			sql = "SELECT COUNT(1) FROM T_FINANCE_INVOICEDETAIL_MID";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryBusinessPayModeNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			List<String> invoiceNos = this.queryInvoiceNoNows(date);
			if(invoiceNos == null || invoiceNos.size() == 0){
				return 0;
			}
			pMap.put("list", invoiceNos);
			sql = "SELECT COUNT(1) FROM T_BUSINESS_PAYMODE_NOW WHERE INVOICE_NO in (:list)";
		}else {
			sql = "SELECT COUNT(1) FROM T_BUSINESS_PAYMODE_MID";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryFinanceInvoiceInfoNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			List<String> invoiceNos = this.queryInvoiceNoNows(date);
			if(invoiceNos == null || invoiceNos.size() == 0){
				return 0;
			}
			pMap.put("list", invoiceNos);
			sql = "SELECT COUNT(1) FROM T_FINANCE_INVOICEINFO_NOW WHERE INVOICE_NO in (:list)";
		}else {
			sql = "SELECT COUNT(1) FROM T_FINANCE_INVOICEINFO_MID";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryStoRecipeNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			Map<String, Object> map = this.queryClincCodeNows(date);
			List<String> clinics = (List<String>) map.get("clincCodes");
			if(clinics == null || clinics.size() == 0){
				return 0;
			}
			pMap.put("list", clinics);
			sql = "SELECT COUNT(1) FROM T_STO_RECIPE_NOW WHERE CLINIC_CODE in (:list)";
		}else {
			sql = "SELECT COUNT(1) FROM T_STO_RECIPE_MID";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryOutpatientFeedetailNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			Map<String, Object> map = this.queryClincCodeNows(date);
			List<String> clinics = (List<String>) map.get("clincCodes");
			if(clinics == null || clinics.size() == 0){
				return 0;
			}
			pMap.put("list", clinics);
			sql = "SELECT COUNT(1) FROM T_OUTPATIENT_FEEDETAIL_NOW WHERE CLINIC_CODE in (:list)";
		}else {
			sql = "SELECT COUNT(1) FROM T_OUTPATIENT_FEEDETAIL_MID";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryOutpatientRecipedetailNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			Map<String, Object> map = this.queryClincCodeNows(date);
			List<String> clinics = (List<String>) map.get("clincCodes");
			if(clinics == null || clinics.size() == 0){
				return 0;
			}
			pMap.put("list", clinics);
			sql = "SELECT COUNT(1) FROM T_OUTPATIENT_RECIPEDETAIL_NOW WHERE CLINIC_CODE in (:list)";
		}else {
			sql = "SELECT COUNT(1) FROM T_OUTPATIENT_RECIPEDETAIL_MID";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public long queryRegistrationNows(String date,String state) {
		
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if ("1".equals(state)) {
			pMap.put("date", date);
			Date endDate = DateUtils.addDay(DateUtils.parseDateY_M_D_SLASH(date), 1);
			String endString = DateUtils.formatDateY_M_SLASH(endDate);
			pMap.put("endDate", endString);
			sql = "SELECT COUNT(1) FROM T_REGISTER_MAIN_NOW WHERE REG_DATE >= TO_DATE(:date,'yyyy/mm/dd') AND REG_DATE < TO_DATE(:endDate,'yyyy/mm/dd')";
		}else {
			sql = "SELECT COUNT(1) FROM T_REGISTER_MAIN_MID";
		}
		long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + result +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return result;
	}

	@Override
	public Map<String, Object> queryRegisterPreregisterNows(String date,String state) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if(StringUtils.isBlank(date)){//时间为空则查询今天以前的最大最小时间
			String endString = DateUtils.formatDateY_M_SLASH(new Date());
			pMap.put("endDate", endString);
			sql = "SELECT TO_CHAR(MAX(PREREGISTER_DATE),'yyyy/mm/dd') AS MAXDATE, TO_CHAR(MIN(PREREGISTER_DATE),'yyyy/mm/dd') AS MINDATE "
					+ "FROM T_REGISTER_PREREGISTER_NOW WHERE PREREGISTER_DATE >= TO_DATE('2017/6/5','yyyy/mm/dd') "
						+ "AND PREREGISTER_DATE < TO_DATE('2017/6/6','yyyy/mm/dd')";
			map = namedParameterJdbcTemplate.queryForMap(sql, pMap);
			if (StringUtils.isBlank((String) map.get("MINDATE")) && StringUtils.isBlank((String) map.get("MAXDATE"))) {
				return null;
			}
			//将map中的key由大写转为小写
			map.put("minDate", map.get("MINDATE"));
			map.put("maxDate", map.get("MAXDATE"));
		}else {//时间不为空则查询改天记录数
			if ("1".equals(state)) {
				pMap.put("date", date);
				Date enDate = DateUtils.addDay(DateUtils.parseDateY_M_D_SLASH(date), 1);
				String endDate = DateUtils.formatDateY_M_SLASH(enDate);
				pMap.put("endDate", endDate);
				sql = "SELECT COUNT(1) FROM T_REGISTER_PREREGISTER_NOW WHERE PREREGISTER_DATE >= TO_DATE(:date,'yyyy/mm/dd') AND PREREGISTER_DATE < TO_DATE(:endDate,'yyyy/mm/dd')";
			}else {
				sql = "SELECT COUNT(1) FROM T_REGISTER_PREREGISTER_MID";
			}
			long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
			map.put("total", result);
		}
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + map.toString() +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return map;
	}

	@Override
	public Map<String, Object> queryRegisterScheduleNows(String date,String state) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> pMap = new HashMap<>();
		String sql;
		if(StringUtils.isBlank(date)){//时间为空则查询今天以前的最大最小时间
			String endString = DateUtils.formatDateY_M_SLASH(new Date());
			pMap.put("endDate", endString);
			sql = "SELECT TO_CHAR(MAX(SCHEDULE_DATE),'yyyy/mm/dd') AS MAXDATE, TO_CHAR(MIN(SCHEDULE_DATE),'yyyy/mm/dd') AS MINDATE "
					+ "FROM T_REGISTER_SCHEDULE_NOW WHERE SCHEDULE_DATE >= TO_DATE('2017/6/5','yyyy/mm/dd') "
					+ "AND SCHEDULE_DATE < TO_DATE('2017/6/6','yyyy/mm/dd')";
			map = namedParameterJdbcTemplate.queryForMap(sql, pMap);
			if (StringUtils.isBlank((String) map.get("MINDATE")) && StringUtils.isBlank((String) map.get("MAXDATE"))) {
				return null;
			}
			//将map中的key由大写转为小写
			map.put("minDate", map.get("MINDATE"));
			map.put("maxDate", map.get("MAXDATE"));
		}else {//时间不为空则查询改天记录数
			if ("1".equals(state)) {
				pMap.put("date", date);
				Date enDate = DateUtils.addDay(DateUtils.parseDateY_M_D_SLASH(date), 1);
				String endDateString = DateUtils.formatDateY_M_SLASH(enDate);
				pMap.put("endDate", endDateString);
				sql = "SELECT COUNT(1) FROM T_REGISTER_SCHEDULE_NOW WHERE SCHEDULE_DATE >= TO_DATE(:date,'yyyy/mm/dd') AND SCHEDULE_DATE < TO_DATE(:endDate,'yyyy/mm/dd')";
			}else {
				sql = "SELECT COUNT(1) FROM T_REGISTER_SCHEDULE_MID";
			}
			long result =  namedParameterJdbcTemplate.queryForObject(sql, pMap, long.class);
			map.put("total", result);
		}
		logger.info("[sql: " + sql +"]");
		logger.info("[result: " + map.toString() +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		return map;
	}

	@Override
	public void moveOutStoreNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			List<String> recipeNos = this.queryRecipeNoNows((String) map.get("date"));
			pMap.put("list", recipeNos);
			fromTable = "T_DRUG_OUTSTORE_NOW";
			toTable = "T_DRUG_OUTSTORE_MID";
			inWhere = " t.RECIPE_NO IN(:list) AND";
		}else {
			fromTable = "T_DRUG_OUTSTORE_MID";
			toTable = "T_DRUG_OUTSTORE";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT ID,DRUG_DEPT_CODE,OUT_BILL_CODE,SERIAL_CODE,GROUP_CODE,OUT_LIST_CODE,"
					+ "OUT_TYPE,IN_BILL_CODE,IN_SERIAL_CODE,IN_LIST_CODE,DRUG_CODE,TRADE_NAME,"
					+ "DRUG_TYPE,DRUG_QUALITY,SPECS,PACK_UNIT,PACK_QTY,MIN_UNIT,SHOW_FLAG,SHOW_UNIT,"
					+ "BATCH_NO,VALID_DATE,PRODUCER_CODE,COMPANY_CODE,RETAIL_PRICE,WHOLESALE_PRICE,"
					+ "PURCHASE_PRICE,OUT_NUM,RETAIL_COST,WHOLESALE_COST,PURCHASE_COST,STORE_NUM,"
					+ "STORE_COST,SPECIAL_FLAG,OUT_STATE,APPLY_NUM,APPLY_OPERCODE,APPLY_DATE,EXAM_NUM,"
					+ "EXAM_OPERCODE,EXAM_DATE,APPROVE_OPERCODE,APPROVE_DATE,PLACE_CODE,RETURN_NUM,"
					+ "DRUGED_BILL,MED_ID,DRUG_STORAGE_CODE,RECIPE_NO,SEQUENCE_NO,SIGN_PERSON,GET_PERSON,"
					+ "STRIKE_FLAG,ARK_FLAG,ARK_BILL_CODE,OUT_DATE,REMARK,CREATEUSER,CREATEDEPT,CREATETIME,"
					+ "UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,DRUGED_CODE,DRUGED_DATE,"
					+ "DRUG_DEPT_NAME,EXAM_OPERNAME,APPROVE_OPERNAME,DRUG_STORAGE_NAME,SIGN_PERSON_NAME,"
					+ "GET_PERSON_NAME,DRUGED_NAME,PRODUCER_NAME,COMPANY_NAME,OP_TYPE,HOSPITAL_ID,AREA_CODE "
				+ "FROM (SELECT t.*,ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t WHERE");
			sql.append(inWhere);
			sql.append(" t.OP_TYPE IN(1,3)) t1"
				+ " WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" tdo WHERE tdo.ID IN ( "
					+ "SELECT t1.ID FROM (SELECT t.ID,ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t WHERE");
			sql.append(inWhere);
			sql.append(" t.OP_TYPE IN(1,3)) t1"
					+ " WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
		
	}

	@Override
	public void moveApplyOutNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			List<String> recipeNos = this.queryRecipeNoNows((String) map.get("date"));
			pMap.put("list", recipeNos);
			fromTable = "T_DRUG_APPLYOUT_NOW";
			toTable = "T_DRUG_APPLYOUT_MID";
			inWhere = " t.PATIENT_ID IN(:list) AND";
		}else {
			fromTable = "T_DRUG_APPLYOUT_MID";
			toTable = "T_DRUG_APPLYOUT";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT APPLY_NUMBER,DEPT_CODE,DRUG_DEPT_CODE,OP_TYPE,GROUP_CODE,"
						+ "DRUG_CODE,TRADE_NAME,BATCH_NO,DRUG_TYPE,DRUG_QUALITY,SPECS,"
						+ "PACK_UNIT,PACK_QTY,MIN_UNIT,SHOW_FLAG,SHOW_UNIT,RETAIL_PRICE,"
						+ "WHOLESALE_PRICE,PURCHASE_PRICE,APPLY_BILLCODE,APPLY_OPERCODE,"
						+ "APPLY_DATE,APPLY_STATE,APPLY_NUM,DAYS,PREOUT_FLAG,CHARGE_FLAG,"
						+ "PATIENT_ID,PATIENT_DEPT,DRUGED_BILL,DRUGED_DEPT,"
						+ "DRUGED_EMPL,DRUGED_DATE,DRUGED_NUM,DOSE_ONCE,DOSE_UNIT,"
						+ "USAGE_CODE,USE_NAME,DFQ_FREQ,DFQ_CEXP,DOSE_MODEL_CODE,ORDER_TYPE,"
						+ "MO_ORDER,COMB_NO,EXEC_SQN,RECIPE_NO,SEQUENCE_NO,SEND_TYPE,BILLCLASS_CODE,"
						+ "PRINT_STATE,RELIEVE_FLAG,RELIEVE_CODE,PRINT_EMPL,PRINT_DATE,OUT_BILL_CODE,"
						+ "VALID_STATE,MARK,CANCEL_EMPL,CANCEL_DATE,PLACE_CODE,RECIPE_DEPT,RECIPE_OPER,"
						+ "BABY_FLAG,EXT_FLAG,EXT_FLAG1,COMPOUND_GROUP,COMPOUND_FLAG,COMPOUND_EXEC,"
						+ "COMPOUND_OPER,COMPOUND_DATE,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,"
						+ "UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,ID,DEPT_NAME,DRUG_DEPT_NAME,"
						+ "DRUG_TYPE_NAME,DRUG_QUALITY_NAME,APPLY_OPERNAME,PATIENT_DEPT_NAME,"
						+ "DRUGED_DEPT_NAME,DRUGED_EMPL_NAME,DOSE_MODEL_NAME,PRINT_EMPL_NAME,CANCEL_EMPL_NAME,"
						+ "RECIPE_DEPT_NAME,RECIPE_OPER_NAME,COMPOUND_OPER_NAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t WHERE");
			sql.append(inWhere);
			sql.append(" t.OP_TYPE IN(1,3)) t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" tda WHERE tda.ID IN( "
					+ "SELECT t1.ID FROM (SELECT t.ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t WHERE");
			sql.append(inWhere);
			sql.append(" t.OP_TYPE IN(1,3)) t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveInpatientCancelitemNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			List<String> recipeNos = this.queryRecipeNoNows((String) map.get("date"));
			pMap.put("list", recipeNos);
			fromTable = "T_INPATIENT_CANCELITEM_NOW";
			toTable = "T_INPATIENT_CANCELITEM_MID";
			inWhere = " T.RECIPE_NO IN (:list) AND";
		}else {
			fromTable = "T_INPATIENT_CANCELITEM_MID";
			toTable = "T_INPATIENT_CANCELITEM";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT APPLY_NO,BILL_CODE,INPATIENT_NO,NAME,BABY_FLAG,"
						+ "DEPT_CODE,NURSE_CELL_CODE,DRUG_FLAG,ITEM_CODE,"
						+ "ITEM_NAME,SPECS,SALE_PRICE,QUANTITY,DAYS,PRICE_UNIT,"
						+ "EXEC_DPCD,OPER_CODE,OPER_DATE,OPER_DPCD,RECIPE_NO,"
						+ "SEQUENCE_NO,BILL_NO,CONFIRM_FLAG,CONFIRM_DPCD,CONFIRM_CODE,"
						+ "CONFIRM_DATE,CHARGE_FLAG,CHARGE_CODE,CHARGE_DATE,EXT_FLAG3,"
						+ "QTY,PACKAGE_CODE,PACKAGE_NAME,CARD_NO,RETURN_REASON,"
						+ "CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,"
						+ "DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,APPLY_FLAG,"
						+ "DEPT_NAME,NURSE_CELL_NAME,EXEC_DPCD_NAME,OPER_NAME,OPER_DPCD_NAME,"
						+ "CONFIRM_DPCD_NAME,CONFIRM_NAME,CHARGE_NAME,INVO_CODE,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t WHERE");
			sql.append(inWhere);
			sql.append(" T.APPLY_FLAG = 1) t1 WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" tic WHERE tic.APPLY_NO IN ("
				+ "SELECT t1.APPLY_NO FROM (SELECT t.APPLY_NO, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t WHERE");
			sql.append(inWhere);
			sql.append(" T.APPLY_FLAG = 1) t1 WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
		
	}

	@Override
	public void moveFinanceInvoicedetailNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			List<String> invoiceNos = this.queryInvoiceNoNows((String) map.get("date"));
			pMap.put("list", invoiceNos);
			fromTable = "T_FINANCE_INVOICEDETAIL_NOW";
			toTable = "T_FINANCE_INVOICEDETAIL_MID";
			inWhere = " WHERE T.INVOICE_NO IN(:list)";
		}else {
			fromTable = "T_FINANCE_INVOICEDETAIL_MID";
			toTable = "T_FINANCE_INVOICEDETAIL";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT INVOICE_ID,INVOICE_NO,TRANS_TYPE,INVO_SEQUENCE,INVO_CODE,"
						+ "INVO_NAME,PUB_COST,OWN_COST,PAY_COST,DEPT_CODE,DEPT_NAME,"
						+ "OPER_DATE,OPER_CODE,BALANCE_FLAG,BALANCE_NO,BALANCE_OPCD,"
						+ "BALANCE_DATE,CANCEL_FLAG,INVOICE_SEQ,CREATEUSER,CREATEDEPT,"
						+ "CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,"
						+ "STOP_FLG,DEL_FLG,ZD_PUB_COST,ZD_OWN_COST,ZD_PAY_COST,"
						+ "OPER_NAME,BALANCE_OPCD_NAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			namedParameterJdbcTemplate.update(sql.toString(), pMap);
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE INVOICE_ID IN ("
				+ "SELECT t1.INVOICE_ID FROM (SELECT t.INVOICE_ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveBusinessPayModeNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			List<String> invoiceNos = this.queryInvoiceNoNows((String) map.get("date"));
			pMap.put("list", invoiceNos);
			fromTable = "T_BUSINESS_PAYMODE_NOW";
			toTable = "T_BUSINESS_PAYMODE_MID";
			inWhere = " WHERE t.INVOICE_NO IN(:list)";
		}else {
			fromTable = "T_BUSINESS_PAYMODE_MID";
			toTable = "T_BUSINESS_PAYMODE";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT INVOICE_ID,INVOICE_NO,TRANS_TYPE,SEQUENCE_NO,"
						+ "MODE_CODE,TOT_COST,REAL_COST,BANK_CODE,BANK_NAME,"
						+ "ACCOUNT,POS_NO,CHECK_NO,OPER_CODE,OPER_DATE,CHECK_FLAG,"
						+ "CHECK_OPCD,CHECK_DATE,BALANCE_FLAG,BALANCE_NO,BALANCE_OPCD,"
						+ "CORRECT_FLAG,CORRECT_OPCD,CORRECT_DATE,BALANCE_DATE,INVOICE_SEQ,"
						+ "CANCEL_FLAG,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,"
						+ "DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,INVOICE_COMB,MODE_NAME,"
						+ "OPER_NAME,CHECK_OPCD_NAME,BALANCE_OPCD_NAME,CORRECT_OPCD_NAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE INVOICE_ID IN("
				+ "SELECT t1.INVOICE_ID FROM (SELECT t.INVOICE_ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
				+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveFinanceInvoiceInfoNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			Map<String, Object> dataMap = this.queryClincCodeNows((String) map.get("date"));
			List<String> clinics = (List<String>) dataMap.get("clincCodes");
			pMap.put("list", clinics);
			fromTable = "T_FINANCE_INVOICEINFO_NOW";
			toTable = "T_FINANCE_INVOICEINFO_MID";
			inWhere =" WHERE T.CLINIC_CODE IN(:list)";
		}else {
			fromTable = "T_FINANCE_INVOICEINFO_MID";
			toTable = "T_FINANCE_INVOICEINFO";
			inWhere ="";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT INVOICE_ID,INVOICE_NO,TRANS_TYPE,CARD_NO,CLINIC_CODE,"
						+ "REG_DATE,NAME,PAYKIND_CODE,PACT_CODE,PACT_NAME,MCARD_NO,"
						+ "MEDICAL_TYPE,TOT_COST,PUB_COST,OWN_COST,PAY_COST,BACK1,"
						+ "BACK2,BACK3,REAL_COST,OPER_CODE,OPER_DATE,EXAMINE_FLAG,"
						+ "CANCEL_FLAG,CANCEL_INVOICE,CANCEL_CODE,CANCEL_DATE,"
						+ "CHECK_FLAG,CHECK_OPCD,CHECK_DATE,BALANCE_FLAG,BALANCE_NO,"
						+ "BALANCE_OPCD,BALANCE_DATE,INVOICE_SEQ,EXT_FLAG,PRINT_INVOICENO,"
						+ "DRUG_WINDOW,ACCOUNT_FLAG,INVOICE_COMB,CREATEUSER,CREATEDEPT,"
						+ "CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,"
						+ "STOP_FLG,DEL_FLG,PAYKIND_NAME,OPER_NAME,CANCEL_NAME,"
						+ "CHECK_OPCD_NAME,BALANCE_OPCD_NAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELECT FROM ");
			sql.append(fromTable);
			sql.append(" WHERE INVOICE_ID IN("
				+ "SELECT t1.INVOICE_ID FROM (SELECT t.INVOICE_ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
				+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveStoRecipeNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			Map<String, Object> dataMap = this.queryClincCodeNows((String) map.get("date"));
			List<String> clinics = (List<String>) dataMap.get("clincCodes");
			pMap.put("list", clinics);
			fromTable = "T_STO_RECIPE_NOW";
			toTable = "T_STO_RECIPE_MID";
			inWhere = " WHERE T.CLINIC_CODE IN(:list)";
		}else {
			fromTable = "T_STO_RECIPE_MID";
			toTable = "T_STO_RECIPE";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT ID,DRUG_DEPT_CODE,RECIPE_NO,CLASS_MEANING_CODE,TRANS_TYPE,"
						+ "RECIPE_STATE,CLINIC_CODE,CARD_NO,PATIENT_NAME,SEX_CODE,BIRTHDAY,"
						+ "PAYKIND_CODE,DEPT_CODE,REG_DATE,DOCT_CODE,DOCT_DEPT,DRUGED_TERMINAL,"
						+ "SEND_TERMINAL,FEE_OPER,FEE_DATE,INVOICE_NO,RECIPE_COST,RECIPE_QTY,"
						+ "DRUGED_QTY,DRUGED_OPER,DRUGED_DEPT,DRUGED_DATE,SEND_OPER,SEND_DEPT,"
						+ "SEND_DATE,VALID_STATE,MODIFY_FLAG,BACK_OPER,BACK_DATE,CANCEL_OPER,"
						+ "CANCEL_DATE,MARK,SUM_DAYS,EXT_FLAG,EXT_FLAG1,CREATEUSER,CREATEDEPT,"
						+ "CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,"
						+ "DEL_FLG,DRUGED_TERMINAL_CODE,DRUGED_TERMINAL_NAME,SEND_TERMINAL_CODE,"
						+ "SEND_TERMINAL_NAME,DRUG_DEPT_NAME,PAYKIND_NAME,DEPT_NAME,DOCT_NAME,"
						+ "DOCT_DEPT_NAME,FEE_OPER_NAME,DRUGED_OPER_NAME,DRUGED_DEPT_NAME,"
						+ "SEND_OPER_NAME,SEND_DEPT_NAME,BACK_OPER_NAME,CANCEL_OPER_NAME,SEX_NAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE ID IN("
				+ "SELECT t1.ID FROM (SELECT t.ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
				+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveOutpatientFeedetailNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			Map<String, Object> dataMap = this.queryClincCodeNows((String) map.get("date"));
			List<String> clinics = (List<String>) dataMap.get("clincCodes");
			pMap.put("list", clinics);
			fromTable = "T_OUTPATIENT_FEEDETAIL_NOW";
			toTable = "T_OUTPATIENT_FEEDETAIL_MID";
			inWhere = " WHERE T.CLINIC_CODE IN(:list)";
		}else {
			fromTable = "T_OUTPATIENT_FEEDETAIL_MID";
			toTable = "T_OUTPATIENT_FEEDETAIL";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT ID,RECIPE_NO,SEQUENCE_NO,TRANS_TYPE,CARD_NO,REG_DATE,REG_DPCD,"
						+ "DOCT_CODE,DOCT_DEPT,ITEM_CODE,ITEM_NAME,DRUG_FLAG,SPECS,SELF_MADE,"
						+ "DRUG_QUALITY,DOSE_MODEL_CODE,FEE_CODE,CLASS_CODE,UNIT_PRICE,QTY,"
						+ "DAYS,FREQUENCY_CODE,USAGE_CODE,USE_NAME,INJECT_NUMBER,EMC_FLAG,"
						+ "LAB_TYPE,CHECK_BODY,DOSE_ONCE,DOSE_UNIT,BASE_DOSE,PACK_QTY,PRICE_UNIT,"
						+ "PUB_COST,PAY_COST,OWN_COST,EXEC_DPCD,EXEC_DPNM,CENTER_CODE,ITEM_GRADE,"
						+ "MAIN_DRUG,COMB_NO,OPER_CODE,OPER_DATE,PAY_FLAG,CANCEL_FLAG,FEE_CPCD,"
						+ "FEE_DATE,INVOICE_NO,INVO_CODE,INVO_SEQUENCE,CONFIRM_FLAG,CONFIRM_CODE,"
						+ "CONFIRM_DEPT,CONFIRM_DATE,ECO_COST,INVOICE_SEQ,NEW_ITEMRATE,OLD_ITEMRATE,"
						+ "EXT_FLAG,EXT_FLAG1,EXT_FLAG2,EXT_FLAG3,PACKAGE_CODE,PACKAGE_NAME,NOBACK_NUM,"
						+ "CONFIRM_NUM,CONFIRM_INJECT,MO_ORDER,SAMPLE_ID,RECIPE_SEQ,OVER_COST,EXCESS_COST,"
						+ "DRUG_OWNCOST,COST_SOURCE,SUBJOB_FLAG,ACCOUNT_FLAG,UPDATE_SEQUENCENO,ACCOUNT_NO,"
						+ "USE_FLAG,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,"
						+ "DELETETIME,STOP_FLG,DEL_FLG,CLINIC_CODE,PATIENT_NO,EXTEND_ONE,EXTEND_TWO,"
						+ "TOT_COST,AGE,AGE_UNIT,REG_DPCDNAME,DOCT_CODENAME,DOCT_DEPTNAME,FREQUENCY_NAME,"
						+ "OPER_NAME,FEE_CPCDNAME,CONFIRM_NAME,CONFIRM_DEPTNAME,VERSION,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE ID IN( "
				+ "SELECT t1.ID FROM (SELECT t.ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
				+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveOutpatientRecipedetailNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		if ("1".equals(state)) {
			Map<String, Object> dataMap = this.queryClincCodeNows((String) map.get("date"));
			List<String> clinics = (List<String>) dataMap.get("clincCodes");
			pMap.put("list", clinics);
			fromTable = "T_OUTPATIENT_RECIPEDETAIL_NOW";
			toTable = "T_OUTPATIENT_RECIPEDETAIL_MID";
			inWhere = " WHERE T.CLINIC_CODE IN(:list)";
		}else {
			fromTable = "T_OUTPATIENT_RECIPEDETAIL_MID";
			toTable = "T_OUTPATIENT_RECIPEDETAIL";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT ID,SEE_NO,CLINIC_CODE,PATIENT_NO,REG_DATE,REG_DEPT,ITEM_CODE,"
						+ "ITEM_NAME,SPECS,DRUG_FLAG,CLASS_CODE,FEE_CODE,UNIT_PRICE,QTY,"
						+ "DAYS,PACK_QTY,ITEM_UNIT,OWN_COST,PAY_COST,PUB_COST,BASE_DOSE,"
						+ "SELF_MADE,DRUG_QUANLITY,ONCE_DOSE,ONCE_UNIT,DOSE_MODEL_CODE,"
						+ "FREQUENCY_CODE,USAGE_CODE,EXEC_DPCD,MAIN_DRUG,COMB_NO,HYPOTEST,"
						+ "INJECT_NUMBER,REMARK,DOCT_CODE,DOCT_DPCD,OPER_DATE,STATUS,CANCEL_USERID,"
						+ "CANCEL_DATE,EMC_FLAG,LAB_TYPE,CHECK_BODY,APPLY_NO,SUBTBL_FLAG,"
						+ "NEED_CONFIRM,CONFIRM_CODE,CONFIRM_DEPT,CONFIRM_DATE,CHARGE_FLAG,"
						+ "CHARGE_CODE,CHARGE_DATE,RECIPE_NO,PHAMARCY_CODE,MINUNIT_FLAG,"
						+ "DATAORDER,PRINT_FLAG,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,"
						+ "UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,SEQUENCE_NO,"
						+ "RECIPE_FEESEQ,RECIPE_SEQ,AUDIT_FLG,AUDIT_REMARK,REG_DEPT_NAME,"
						+ "CLASS_NAME,FEE_NAME,ONCE_UNIT_NAME,DOSE_MODEL_NAME,FREQUENCY_NAME,"
						+ "USAGE_NAME,EXEC_DPCD_NAME,DOCT_NAME,DOCT_DPCD_NAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE ID IN("
				+ "SELECT t1.ID FROM (SELECT t.ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
				+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveRegistrationNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		String fromTable;
		String toTable;
		String inWhere;
		if ("1".equals(state)) {
			if (StringUtils.isNotBlank((String) map.get("date"))) {
				pMap.put("date", map.get("date"));
				Date enDate = DateUtils.addDay(DateUtils.parseDateY_M_D_SLASH((String) map.get("date")), 1);
				String endDate = DateUtils.formatDateY_M_SLASH(enDate);
				pMap.put("endDate", endDate);
			}
			fromTable = "T_REGISTER_MAIN_NOW";
			toTable = "T_REGISTER_MAIN_MID";
			inWhere = " WHERE t.REG_DATE >= TO_DATE(:date, 'yyyy/mm/dd') AND t.REG_DATE < TO_DATE(:endDate, 'yyyy/mm/dd')";
		}else {
			fromTable = "T_REGISTER_MAIN_MID";
			toTable = "T_REGISTER_MAIN";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT ID,CLINIC_CODE,TRANS_TYPE,CARD_ID,CARD_NO,REG_DATE,NOON_CODE,"
						+ "PATIENT_NAME,PATIENT_IDENNO,PATIENT_SEX,PATIENT_BIRTHDAY,PATIENT_AGE,"
						+ "PATIENT_AGEUNIT,RELA_PHONE,ADDRESS,CARD_TYPE,PAYKIND_CODE,PAYKIND_NAME,"
						+ "PACT_CODE,PACT_NAME,MEDICAL_TYPE,MCARD_NO,REGLEVL_CODE,REGLEVL_NAME,"
						+ "DEPT_CODE,DEPT_NAME,SCHEMA_NO,ORDER_NO,SEENO,BEGIN_TIME,END_TIME,"
						+ "DOCT_CODE,DOCT_NAME,YNREGCHRG,INVOICE_NO,INVOICE_PRINT_FLAG,RECIPE_NO,"
						+ "YNFR,APPEND_FLAG,REG_FEE_CODE,REG_FEE,CHCK_FEE_CODE,CHCK_FEE,DIAG_FEE_CODE,"
						+ "DIAG_FEE,OTH_FEE_CODE,OTH_FEE,BOOK_FEE_CODE,BOOK_FEE,BOOK_FLAG,ECO_COST,"
						+ "OWN_COST,PUB_COST,PAY_COST,SUM_COST,VALID_FLAG,OPER_CODE,OPER_DATE,"
						+ "CANCEL_OPCD,CANCEL_DATE,ICD_CODE,ICD_NAME,EXAM_CODE,EXAM_DATE,CHECK_FLAG,"
						+ "CHECK_OPCD,CHECK_DATE,BALANCE_FLAG,BALANCE_NO,BALANCE_OPCD,BALANCE_DATE,"
						+ "YNSEE,SEE_DATE,TRIAGE_FLAG,TRIAGE_OPCD,TRIAGE_DATE,PRINT_INVOICECNT,SEE_DPCD,"
						+ "SEE_DOCD,IN_SOURCE,IN_STATE,IS_ACCOUNT,OPERSEQ,SI_CARD,SI_NO,BACKNUMBER_REASON,"
						+ "ACCOUNT_NO,UP_FLAG,CREATEUSER,CREATEDEPT,CREATEHOS,CREATETIME,UPDATEUSER,"
						+ "UPDATETIME,DELETEUSER,DELETETIME,DEL_FLG,STOP_FLG,MEDICAL_TYPE_CODE,MEDICALRECORDID,"
						+ "EGISTER_SEEOPTIMIZE,YNBOOK,EMERGENCY_FLAG,REG_TRIAGETYPE,NOON_CODENMAE,PATIENT_SEXNAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE ID IN( "
				+ "SELECT t1.ID FROM (SELECT t.ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
				+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveRegisterPreregisterNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		String fromTable;
		String toTable;
		String inWhere;
		if ("1".equals(state)) {
			if (StringUtils.isNotBlank((String) map.get("date"))) {
				pMap.put("date", map.get("date"));
				Date enDate = DateUtils.addDay(DateUtils.parseDateY_M_D_SLASH((String) map.get("date")), 1);
				String endDate = DateUtils.formatDateY_M_SLASH(enDate);
				pMap.put("endDate", endDate);
			}
			fromTable = "T_REGISTER_PREREGISTER_NOW";
			toTable = "T_REGISTER_PREREGISTER_MID";
			inWhere = " WHERE t.PREREGISTER_DATE >= TO_DATE(:date, 'yyyy/mm/dd') AND t.PREREGISTER_DATE < TO_DATE(:endDate, 'yyyy/mm/dd')";
		}else {
			fromTable = "T_REGISTER_PREREGISTER_MID";
			toTable = "T_REGISTER_PREREGISTER";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT SCHEDULE_ID,PREREGISTER_ID,PREREGISTER_NO,PREREGISTER_ISNET,"
						+ "PREREGISTER_ISPHONE,PREREGISTER_DEPT,PREREGISTER_EXPERT,PREREGISTER_GRADE,"
						+ "PREREGISTER_DATE,PREREGISTER_STARTTIME,PREREGISTER_ENDTIME,MEDICALRECORD_ID,"
						+ "IDCARD_ID,PREREGISTER_CERTIFICATESTYPE,PREREGISTER_CERTIFICATESNO,PREREGISTER_NAME,"
						+ "PREREGISTER_SEX,PREREGISTER_AGE,PREREGISTER_AGEUNIT,PREREGISTER_PHONE,PREREGISTER_ADDRESS,"
						+ "CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,"
						+ "DEL_FLG,PREREGISTER_SEXS,PREREGISTER_MIDDAY,PREREGISTER_SEEFLAG,PREREGISTER_APPFLAG,"
						+ "PREREGISTER_ORDERNO,PREREGISTER_SOURCETYPE,PREREGISTER_ISFIRST,PREREGISTER_STATUS,"
						+ "MISS_NUMBER,PREREGISTER_DEPTNAME,PREREGISTER_EXPERTNAME,PREREGISTER_GRADENAME,"
						+ "PREREGISTER_CERTIFICATESNAME,PREREGISTER_MIDDAYNAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE PREREGISTER_ID IN( "
				+ "SELECT t1.PREREGISTER_ID FROM (SELECT t.PREREGISTER_ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
				+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}
	}

	@Override
	public void moveRegisterScheduleNows(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		String flag = (String) map.get("flag");//迁移或删除标记  1：迁移  2：删除
		String state = (String) map.get("state");//步骤标记 1：now——mid  2：mid——总表
		String fromTable;
		String toTable;
		String inWhere;
		if ("1".equals(state)) {
			if (StringUtils.isNotBlank((String) map.get("date"))) {
				pMap.put("date", map.get("date"));
				Date enDate = DateUtils.addDay(DateUtils.parseDateY_M_D_SLASH((String) map.get("date")), 1);
				String endDate = DateUtils.formatDateY_M_SLASH(enDate);
				pMap.put("endDate", endDate);
			}
			fromTable = "T_REGISTER_SCHEDULE_NOW";
			toTable = "T_REGISTER_SCHEDULE_MID";
			inWhere = " WHERE t.SCHEDULE_DATE >= TO_DATE(:date, 'yyyy/mm/dd') AND t.SCHEDULE_DATE < TO_DATE(:endDate, 'yyyy/mm/dd')";
		}else {
			fromTable = "T_REGISTER_SCHEDULE_MID";
			toTable = "T_REGISTER_SCHEDULE";
			inWhere = "";
		}
		if("1".equals(flag)){//迁移数据
			sql.append("INSERT INTO ");
			sql.append(toTable);
			sql.append(" SELECT SCHEDULE_ID,SCHEDULE_MODELID,SCHEDULE_TYPE,SCHEDULE_DEPTID,"
						+ "SCHEDULE_CLINICID,SCHEDULE_DOCTOR,SCHEDULE_WEEK,SCHEDULE_DATE,"
						+ "SCHEDULE_MIDDAY,SCHEDULE_LIMIT,SCHEDULE_PRELIMIT,SCHEDULE_PHONELIMIT,"
						+ "SCHEDULE_NETLIMIT,SCHEDULE_PECIALLIMIT,SCHEDULE_STARTTIME,SCHEDULE_ENDTIME,"
						+ "SCHEDULE_APPFLAG,SCHEDULE_ISSTOP,SCHEDULE_STOPDOCTOR,SCHEDULE_STOPTIME,"
						+ "SCHEDULE_STOPREASON,SCHEDULE_REGGRADE,SCHEDULE_REMARK,CREATEUSER,CREATEDEPT,"
						+ "CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,"
						+ "SCHEDULE_CLASS,SCHEDULE_WORKDEPT,SCHEDULE_DEPTNAME,SCHEDULE_CLINICNAME,"
						+ "SCHEDULE_DOCTORNAME,SCHEDULE_MIDDAYNAME,HOSPITAL_ID,AREA_CODE "
					+ "FROM (SELECT t.*, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row");
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[result: " + i +"]");
		}else {//删除数据
			sql.append("DELETE FROM ");
			sql.append(fromTable);
			sql.append(" WHERE SCHEDULE_ID IN( "
					+ "SELECT T1.SCHEDULE_ID FROM (SELECT t.SCHEDULE_ID, ROWNUM AS n FROM ");
			sql.append(fromTable);
			sql.append(" t");
			sql.append(inWhere);
			sql.append(") t1 "
					+ "WHERE t1.n > (:page - 1) * :row AND t1.n <= :page * :row)");
			int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
			logger.info("[sql: " + sql +"]");
			logger.info("[parem: " + pMap.toString() +"]");
			logger.info("[result: " + i +"]");
		}
	}
	
	public int queryParameter() {
		String hospitalCode = HisParameters.CURRENTHOSPITALCODE;//获得当前医院系统编号 
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append(" select a.parameter_value from t_hospital_parameter a ");
		sqlBuffer.append(" left join t_hospital_parameter_ref b on a.parameter_id = b.parameter_id ");
		sqlBuffer.append(" left join t_hospital c on b.hospital_id = c.hospital_id");
		sqlBuffer.append(" where a.parameter_code = 'infoTime' ");
		sqlBuffer.append(" and c.hospital_code = '"+hospitalCode+"' ");
		String value = namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sqlBuffer.toString(), java.lang.String.class);
		if (StringUtils.isNotBlank(value)) {
			return Integer.valueOf(value);
		}
		return 1;
	}

	@Override
	public MoveDataLog queryErrorMoveDataLog(Integer optType, Integer dateType,
			String tableName, String dataDate) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("optType", optType);
		pMap.put("dateType", dateType);
		pMap.put("tableName", tableName);
		if (StringUtils.isNotBlank(dataDate)) {
			pMap.put("dataDate", dataDate);
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ID AS ID,t.OPT_TYPE AS OPTTYPE,t.DATA_TYPE AS DATATYPE,t.TABLENAME AS TABLENAME,"
				+ "t.TABLEZHNAME AS TABLEZHNAME,t.DATA_DATE AS DATADATE,t.TOTAL AS TOTAL,t.PAGECOUNT AS PAGECOUNT,"
				+ "t.PAGESIZE AS PAGESIZE,t.ISSUCCESS AS ISSUCCESS,t.FAILPAGE AS FAILPAGE,t.MOVE_DATE AS MOVEDATE,"
				+ "t.START_DATE AS STARTDATE,t.END_DATE AS ENDDATE "
				+ "FROM T_SYS_MOVEDATA_LOG t WHERE t.OPT_TYPE = :optType AND t.DATA_TYPE = :dateType "
				+ "AND t.TABLENAME = :tableName AND t.ISSUCCESS = 2");
		sql.append(StringUtils.isNotBlank(dataDate) ? " AND t.DATA_DATE = :dataDate " : "");
		sql.append(" order by t.start_date");
		List<MoveDataLog> dataLogs = namedParameterJdbcTemplate.query(sql.toString(), pMap, new RowMapper<MoveDataLog>() {
			@Override
			public MoveDataLog mapRow(ResultSet rs, int rowNum) throws SQLException {
				MoveDataLog moveDataLog = new MoveDataLog();
				if (rs == null) {
					return null;
				}
				moveDataLog.setId(rs.getString("ID"));
				moveDataLog.setOptType(rs.getInt("OPTTYPE"));
				moveDataLog.setDateType(rs.getInt("DATATYPE"));
				moveDataLog.setTableName(rs.getString("TABLENAME"));
				moveDataLog.setTableZhName(rs.getString("TABLEZHNAME"));
				moveDataLog.setDataDate(rs.getString("DATADATE"));
				moveDataLog.setTotal(rs.getLong("TOTAL"));
				moveDataLog.setPageCount(rs.getLong("PAGECOUNT"));
				moveDataLog.setPageSize(rs.getLong("PAGESIZE"));
				moveDataLog.setIsSuccess(rs.getInt("ISSUCCESS"));
				moveDataLog.setFailPage(rs.getLong("FAILPAGE"));
				moveDataLog.setMoveDate(rs.getDate("MOVEDATE"));
				moveDataLog.setStartDate(rs.getDate("STARTDATE"));
				moveDataLog.setEndDate(rs.getDate("ENDDATE"));
				return moveDataLog;
			}
		});
		logger.info("[sql: " + sql +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		if(dataLogs != null && dataLogs.size() > 0){
			logger.info("[result: " + dataLogs.get(0) +"]");
			return dataLogs.get(0);
		}
		return null;
	}

	@Override
	public void saveOrUpdate(MoveDataLog moveDataLog) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("id",moveDataLog.getId());
		if (StringUtils.isBlank(moveDataLog.getId())) {
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			pMap.put("id",id);
		}
		pMap.put("optType", moveDataLog.getOptType());
		pMap.put("dateType", moveDataLog.getDateType());
		pMap.put("tableName", moveDataLog.getTableName());
		pMap.put("tableZhName", moveDataLog.getTableZhName());
		pMap.put("dataDate", moveDataLog.getDataDate());
		pMap.put("total", moveDataLog.getTotal());
		pMap.put("pageCount", moveDataLog.getPageCount());
		pMap.put("pageSize", moveDataLog.getPageSize());
		pMap.put("isSuccess", moveDataLog.getIsSuccess());
		pMap.put("failPage", moveDataLog.getFailPage());
		pMap.put("moveDate", moveDataLog.getMoveDate());
		pMap.put("startDate", moveDataLog.getStartDate());
		pMap.put("endDate", moveDataLog.getEndDate());
		String sql;
		if (StringUtils.isNotBlank(moveDataLog.getId())) {
			sql = "UPDATE T_SYS_MOVEDATA_LOG SET OPT_TYPE = :optType, DATA_TYPE = :dateType, TABLENAME = :tableName, TABLEZHNAME = :tableZhName,"
					+ " DATA_DATE = :dataDate, TOTAL = :total, PAGECOUNT = :pageCount, PAGESIZE = :pageSize, ISSUCCESS = :isSuccess, FAILPAGE = :failPage,"
					+ " MOVE_DATE = :moveDate, START_DATE = :startDate, END_DATE = :endDate WHERE ID = :id";
		}else {
			sql = "INSERT INTO T_SYS_MOVEDATA_LOG VALUES(:id,:optType,:dateType,"
					+ ":tableName,:tableZhName,:dataDate,:total,:pageCount,"
					+ ":pageSize,:isSuccess,:failPage,:moveDate,:startDate,:endDate)";
		}
		logger.info("[sql: " + sql +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		int i =namedParameterJdbcTemplate.update(sql, pMap);
		logger.info("[result: " + i +"]");
	}

	@Override
	public List<MoveDataLog> queryMoveDataLogs(Integer optType, Integer dateType) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("optType", optType);
		pMap.put("dateType", dateType);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ID AS ID,t.OPT_TYPE AS OPTTYPE,t.DATA_TYPE AS DATATYPE,t.TABLENAME AS TABLENAME,"
				+ "t.TABLEZHNAME AS TABLEZHNAME,t.DATA_DATE AS DATADATE,t.TOTAL AS TOTAL,t.PAGECOUNT AS PAGECOUNT,"
				+ "t.PAGESIZE AS PAGESIZE,t.ISSUCCESS AS ISSUCCESS,t.FAILPAGE AS FAILPAGE,t.MOVE_DATE AS MOVEDATE,"
				+ "t.START_DATE AS STARTDATE,t.END_DATE AS ENDDATE "
				+ "FROM T_SYS_MOVEDATA_LOG t  WHERE t.OPT_TYPE = :optType AND t.DATA_TYPE = :dateType "
				+ "AND t.ISSUCCESS = 2  order by t.start_date");
		List<MoveDataLog> dataLogs = namedParameterJdbcTemplate.query(sql.toString(), pMap, new RowMapper<MoveDataLog>() {
			@Override
			public MoveDataLog mapRow(ResultSet rs, int rowNum) throws SQLException {
				MoveDataLog moveDataLog = new MoveDataLog();
				if (rs == null) {
					return null;
				}
				moveDataLog.setId(rs.getString("ID"));
				moveDataLog.setOptType(rs.getInt("OPTTYPE"));
				moveDataLog.setDateType(rs.getInt("DATATYPE"));
				moveDataLog.setTableName(rs.getString("TABLENAME"));
				moveDataLog.setTableZhName(rs.getString("TABLEZHNAME"));
				moveDataLog.setDataDate(rs.getString("DATADATE"));
				moveDataLog.setTotal(rs.getLong("TOTAL"));
				moveDataLog.setPageCount(rs.getLong("PAGECOUNT"));
				moveDataLog.setPageSize(rs.getLong("PAGESIZE"));
				moveDataLog.setIsSuccess(rs.getInt("ISSUCCESS"));
				moveDataLog.setFailPage(rs.getLong("FAILPAGE"));
				moveDataLog.setMoveDate(rs.getDate("MOVEDATE"));
				moveDataLog.setStartDate(rs.getDate("STARTDATE"));
				moveDataLog.setEndDate(rs.getDate("ENDDATE"));
				return moveDataLog;
			}
		});
		logger.info("[sql: " + sql +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		if(dataLogs != null && dataLogs.size() > 0){
			logger.info("[result: " + dataLogs.get(0) +"]");
			return dataLogs;
		}
		return null;
	}

	@Override
	public MoveDataLog querySuccessMoveDataLog(Integer optType,
			Integer dateType, String tableName, String dataDate) {
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("optType", optType);
		pMap.put("dateType", dateType);
		pMap.put("tableName", tableName);
		if (StringUtils.isNotBlank(dataDate)) {
			pMap.put("dataDate", dataDate);
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t.ID AS ID,t.OPT_TYPE AS OPTTYPE,t.DATA_TYPE AS DATATYPE,t.TABLENAME AS TABLENAME,"
				+ "t.TABLEZHNAME AS TABLEZHNAME,t.DATA_DATE AS DATADATE,t.TOTAL AS TOTAL,t.PAGECOUNT AS PAGECOUNT,"
				+ "t.PAGESIZE AS PAGESIZE,t.ISSUCCESS AS ISSUCCESS,t.FAILPAGE AS FAILPAGE,t.MOVE_DATE AS MOVEDATE,"
				+ "t.START_DATE AS STARTDATE,t.END_DATE AS ENDDATE "
				+ "FROM T_SYS_MOVEDATA_LOG t WHERE t.OPT_TYPE = :optType AND t.DATA_TYPE = :dateType "
				+ "AND t.TABLENAME = :tableName AND t.ISSUCCESS = 1");
		sql.append(StringUtils.isNotBlank(dataDate) ? " AND t.DATA_DATE = :dataDate " : "");
		List<MoveDataLog> dataLogs = namedParameterJdbcTemplate.query(sql.toString(), pMap, new RowMapper<MoveDataLog>() {
			@Override
			public MoveDataLog mapRow(ResultSet rs, int rowNum) throws SQLException {
				MoveDataLog moveDataLog = new MoveDataLog();
				if (rs == null) {
					return null;
				}
				moveDataLog.setId(rs.getString("ID"));
				moveDataLog.setOptType(rs.getInt("OPTTYPE"));
				moveDataLog.setDateType(rs.getInt("DATATYPE"));
				moveDataLog.setTableName(rs.getString("TABLENAME"));
				moveDataLog.setTableZhName(rs.getString("TABLEZHNAME"));
				moveDataLog.setDataDate(rs.getString("DATADATE"));
				moveDataLog.setTotal(rs.getLong("TOTAL"));
				moveDataLog.setPageCount(rs.getLong("PAGECOUNT"));
				moveDataLog.setPageSize(rs.getLong("PAGESIZE"));
				moveDataLog.setIsSuccess(rs.getInt("ISSUCCESS"));
				moveDataLog.setFailPage(rs.getLong("FAILPAGE"));
				moveDataLog.setMoveDate(rs.getDate("MOVEDATE"));
				moveDataLog.setStartDate(rs.getDate("STARTDATE"));
				moveDataLog.setEndDate(rs.getDate("ENDDATE"));
				return moveDataLog;
			}
		});
		logger.info("[sql: " + sql +"]");
		logger.info("[parem: " + pMap.toString() +"]");
		if(dataLogs != null && dataLogs.size() > 0){
			logger.info("[result: " + dataLogs.get(0) +"]");
			return dataLogs.get(0);
		}
		return null;
	}


}
