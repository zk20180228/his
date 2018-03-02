package cn.honry.inner.inpatient.inpatient.dao.impl;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;



import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.inner.inpatient.inpatient.dao.InpatientDao;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;

/**
 * 
 * @author hzr 住院数据迁移接口实现
 *
 */
@Repository("inpatientDao")
@SuppressWarnings({ "all" })
public class InpatientDaoImpl implements InpatientDao {

	// 扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private Logger logger = Logger.getLogger(InpatientDaoImpl.class);

	public int getParameterByCode() {
		String hospitalCode = HisParameters.CURRENTHOSPITALCODE;//获得当前医院系统编号 
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append(" select a.parameter_value from t_hospital_parameter a ");
		sqlBuffer.append(" left join t_hospital_parameter_ref b on a.parameter_id = b.parameter_id ");
		sqlBuffer.append(" left join t_hospital c on b.hospital_id = c.hospital_id");
		sqlBuffer.append(" where a.parameter_code = 'saveTime' ");
		sqlBuffer.append(" and c.hospital_code = '"+hospitalCode+"' ");
		String value = namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sqlBuffer.toString(), java.lang.String.class);
		if (StringUtils.isNotBlank(value)) {
			return Integer.valueOf(value);
		}
		return 1;
	}

	@Override
	public Map<String, Object> getInpatentNos(String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pMap = new HashMap<String, Object>();
		if (StringUtils.isBlank(date)) {
			Integer vailueData = this.getParameterByCode() - 1;
			Date nowDate = new Date();
			String dateString = DateUtils.formatDateY_M_SLASH(DateUtils.addDay(
					nowDate, vailueData * -1));
			pMap.put("dateString", dateString);
			StringBuffer sql = new StringBuffer(
					"select to_char(max(t.out_date),'yyyy/MM/dd') as Max,to_char(min(t.out_date),'yyyy/MM/dd') as Min "
					+ "from t_inpatient_info_now t "
					+ "where t.in_state in ('O','N') and t.out_date < to_date(:dateString,'yyyy/MM/dd') "
					+ "and t.out_date >= to_date('2016/01/01','yyyy/MM/dd') ");
			map = namedParameterJdbcTemplate.queryForMap(sql.toString(), pMap);
			//将map中的key由大写转为小写
			map.put("minDate", map.get("Min"));
			map.put("maxDate", map.get("Max"));
			logger.info("[sql: " + sql.toString() + "]");
			logger.info("[result: " + map.toString() + "]");
			logger.info("[parem: " + pMap.toString() + "]");
		} else {
			pMap.put("vailueData", date);
			Date endDate = DateUtils.addDay(
					DateUtils.parseDateY_M_D_SLASH(date), 1);
			String endString = DateUtils.formatDateY_M_SLASH(endDate);
			pMap.put("endDate", endString);
			StringBuffer sql = new StringBuffer("select t.inpatient_no from t_inpatient_info_now t "
					+ "where t.in_state in ('O','N') and t.out_date >= to_date(:vailueData,'yyyy/MM/dd') "
					+ "and t.out_date < to_date(:endDate,'yyyy/MM/dd')");
			List<String> inpatentNos = namedParameterJdbcTemplate.queryForList(
					sql.toString(), pMap, String.class);
			if (inpatentNos != null && inpatentNos.size() > 0) {
				map.put("inpatient", inpatentNos);
			}
			logger.info("[sql: " + sql.toString() + "]");
			logger.info("[result: " + map.toString() + "]");
			logger.info("[parem: " + pMap.toString() + "]");
		}
		return map;
	}

	@Override
	public List<String> getRecipes(String date) {
		Map<String, Object> map = this.getInpatentNos(date);
		Map<String, Object> pMap = new HashMap<>();
		List<String> inpatients = (List<String>) map.get("inpatient");
		if (inpatients == null || inpatients.size() == 0) {
			return null;
		}
		pMap.put("list", inpatients);
		StringBuffer sql = new StringBuffer("select distinct t.out_bill_code from T_DRUG_APPLYOUT_NOW t "
				+ "where t.PATIENT_ID in (:list) and t.OP_TYPE in ('4','5')");
		List<String> list = namedParameterJdbcTemplate.queryForList(
				sql.toString(), pMap, String.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + list.toString() + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	public List<String> getInvoices(String date) {
		Map<String, Object> map = this.getInpatentNos(date);
		Map<String, Object> pMap = new HashMap<>();
		List<String> inpatients = (List<String>) map.get("inpatient");
		if (inpatients == null || inpatients.size() == 0) {
			return null;
		}
		pMap.put("list", inpatients);
		StringBuffer sql = new StringBuffer("select distinct t.invoice_no from T_INPATIENT_BALANCEHEAD_NOW t "
				+ "where t.inpatient_no in(:list)");
		List<String> list = namedParameterJdbcTemplate.queryForList(
				sql.toString(), pMap, String.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + list.toString() + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	public long countInpatientInfoNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		String inWhere;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "t_inpatient_info_now t";
			inWhere = " and t.inpatient_no in(:list)";
		} else {
			tableName = "t_inpatient_info_mid t";
			inWhere = "";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		sql.append(" where t.in_state in('O','N')");
		sql.append(inWhere);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientApplyNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_APPLY_NOW t where t.patient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_APPLY_MID t";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientShiftapplyNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_SHIFTAPPLY_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_SHIFTAPPLY_MID t";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientOrderNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_ORDER_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_ORDER_MID t";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientExecdrugNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_EXECDRUG_NOW  t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_EXECDRUG_MID t";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientExecundrugNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_EXECUNDRUG_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_EXECUNDRUG_MID t";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientFeeinfoNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_FEEINFO_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_FEEINFO_MID t";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientInprepayNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_INPREPAY_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_INPREPAY_MID";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientBalanceheadNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_BALANCEHEAD_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_BALANCEHEAD_MID";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientBalancepayNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			List<String> invoices = this.getInvoices(date);
			if (invoices == null || invoices.size() == 0) {
				return 0;
			}
			pMap.put("list", invoices);
			tableName = "T_INPATIENT_BALANCEPAY_NOW t where t.invoice_no in (:list)";
		} else {
			tableName = "T_INPATIENT_BALANCEPAY_MID";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientCancelitemNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_CANCELITEM_NOW t where t.APPLY_FLAG = 2 and t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_CANCELITEM_MID t where t.APPLY_FLAG = 2";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientItemlistNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_ITEMLIST_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_ITEMLIST_MID";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientMedicinelistNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_MEDICINELIST_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_MEDICINELIST_MID";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countDrugApplyoutNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		String inWhere;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_DRUG_APPLYOUT_NOW";
			inWhere = " and t.PATIENT_ID in (:list)";
		} else {
			tableName = "T_DRUG_APPLYOUT_MID";
			inWhere = "";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		sql.append(" t where t.op_type in ('4','5')");
		sql.append(inWhere);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countDrugOutstoreNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		String inWhere;
		if ("1".equals(state)) {
			List<String> recipes = this.getRecipes(date);
			if (recipes == null || recipes.size() == 0) {
				return 0;
			}
			pMap.put("list", recipes);
			tableName = "T_DRUG_OUTSTORE_NOW";
			inWhere = " and t.recipe_no in (:list)";
		} else {
			tableName = "T_DRUG_OUTSTORE_MID";
			inWhere = "";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		sql.append(" t where t.op_type in ('4','5')");
		sql.append(inWhere);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public long countInpatientBalancelistNow(String date, String state) {
		Map<String, Object> pMap = new HashMap<>();
		String tableName;
		if ("1".equals(state)) {
			Map<String, Object> map = this.getInpatentNos(date);
			List<String> inpatients = (List<String>) map.get("inpatient");
			if (inpatients == null || inpatients.size() == 0) {
				return 0;
			}
			pMap.put("list", inpatients);
			tableName = "T_INPATIENT_BALANCELIST_NOW t where t.inpatient_no in (:list)";
		} else {
			tableName = "T_INPATIENT_BALANCELIST_MID";
		}
		StringBuffer sql = new StringBuffer("select count(1) from ");
		sql.append(tableName);
		long result = namedParameterJdbcTemplate.queryForObject(sql.toString(),
				pMap, long.class);
		logger.info("[sql: " + sql.toString() + "]");
		logger.info("[result: " + result + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		return result;
	}

	public void moveInpatientBalancelistNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_BALANCELIST_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_BALANCELIST_MID";
		} else {
			fromTable = "T_INPATIENT_BALANCELIST_MID t";
			toTable = "T_INPATIENT_BALANCELIST";
		}
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,INVOICE_NO,TRANS_TYPE,INPATIENT_NO,NAME,PAYKIND_CODE,PACT_CODE,DEPT_CODE,STAT_CODE,"
				+ "STAT_NAME,SORT_ID,TOT_COST,OWN_COST,PAY_COST,PUB_COST,ECO_COST,BALANCE_OPERCODE,BALANCE_DATE,"
				+ "BALANCE_TYPE,BALANCE_NO,BABY_FLAG,CHECK_NO,EXT_FLAG,EXT1_FLAG,EXT2_FLAG,EXT_CODE,EXT_DATE,EXT_OPERCODE,"
				+ "BALANCEOPER_DEPTCODE,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,"
				+ "STOP_FLG,DEL_FLG,DEPT_NAME,BALANCE_OPERNAME,BALANCEOPER_DEPTNAME,HOSPITAL_ID,AREA_CODE "
				+ "from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveDrugOutstoreNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			List<String> recipes = this.getRecipes((String) map.get("date"));
			pMap.put("list", recipes);
			fromTable = "T_DRUG_OUTSTORE_NOW";
			toTable = "T_DRUG_OUTSTORE_MID";
			inWhere = " and t.recipe_no in (:list)";
		} else {
			fromTable = "T_DRUG_OUTSTORE_MID";
			toTable = "T_DRUG_OUTSTORE";
			inWhere = "";
		}
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,DRUG_DEPT_CODE,OUT_BILL_CODE,SERIAL_CODE,GROUP_CODE,OUT_LIST_CODE,OUT_TYPE,op_type,IN_BILL_CODE,IN_SERIAL_CODE,IN_LIST_CODE,"
				+ "DRUG_CODE,TRADE_NAME,DRUG_TYPE,DRUG_QUALITY,SPECS,PACK_UNIT,PACK_QTY,MIN_UNIT,SHOW_FLAG,SHOW_UNIT,BATCH_NO,VALID_DATE,PRODUCER_CODE,COMPANY_CODE,"
				+ "RETAIL_PRICE,WHOLESALE_PRICE,PURCHASE_PRICE,OUT_NUM,RETAIL_COST,WHOLESALE_COST,PURCHASE_COST,STORE_NUM,STORE_COST,SPECIAL_FLAG,OUT_STATE,"
				+ "APPLY_NUM,APPLY_OPERCODE,APPLY_DATE,EXAM_NUM,EXAM_OPERCODE,EXAM_DATE,APPROVE_OPERCODE,APPROVE_DATE,PLACE_CODE,RETURN_NUM,DRUGED_BILL,MED_ID,"
				+ "DRUG_STORAGE_CODE,RECIPE_NO,SEQUENCE_NO,SIGN_PERSON,GET_PERSON,STRIKE_FLAG,ARK_FLAG,ARK_BILL_CODE,OUT_DATE,REMARK,CREATEUSER,CREATEDEPT,CREATETIME,"
				+ "UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,DRUGED_CODE,DRUGED_DATE,DRUG_DEPT_NAME,EXAM_OPERNAME,APPROVE_OPERNAME,DRUG_STORAGE_NAME,"
				+ "SIGN_PERSON_NAME,GET_PERSON_NAME,DRUGED_NAME,PRODUCER_NAME,COMPANY_NAME,HOSPITAL_ID,AREA_CODE from "
				+ " (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(" t where t.op_type in('4','5')");
		sql.append(inWhere);
		sql.append(") t1 "
				+ " where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveDrugApplyoutNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String inWhere;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_DRUG_APPLYOUT_NOW";
			toTable = "T_DRUG_APPLYOUT_MID";
			inWhere = " and t.patient_id in (:list)";
		} else {
			fromTable = "T_DRUG_APPLYOUT_MID";
			toTable = "T_DRUG_APPLYOUT";
			inWhere = "";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select APPLY_NUMBER,DEPT_CODE,DRUG_DEPT_CODE,OP_TYPE,GROUP_CODE,DRUG_CODE,TRADE_NAME,BATCH_NO,"
				+ "DRUG_TYPE,DRUG_QUALITY,SPECS,PACK_UNIT,PACK_QTY,MIN_UNIT,SHOW_FLAG,SHOW_UNIT,RETAIL_PRICE,WHOLESALE_PRICE,PURCHASE_PRICE,"
				+ "APPLY_BILLCODE,APPLY_OPERCODE,APPLY_DATE,APPLY_STATE,APPLY_NUM,DAYS,PREOUT_FLAG,CHARGE_FLAG,PATIENT_ID,PATIENT_DEPT,DRUGED_BILL,"
				+ "DRUGED_DEPT,DRUGED_EMPL,DRUGED_DATE,DRUGED_NUM,DOSE_ONCE,DOSE_UNIT,USAGE_CODE,USE_NAME,DFQ_FREQ,DFQ_CEXP,DOSE_MODEL_CODE,"
				+ "ORDER_TYPE,MO_ORDER,COMB_NO,EXEC_SQN,RECIPE_NO,SEQUENCE_NO,SEND_TYPE,BILLCLASS_CODE,PRINT_STATE,RELIEVE_FLAG,RELIEVE_CODE,"
				+ "PRINT_EMPL,PRINT_DATE,OUT_BILL_CODE,VALID_STATE,MARK,CANCEL_EMPL,CANCEL_DATE,PLACE_CODE,RECIPE_DEPT,RECIPE_OPER,BABY_FLAG,"
				+ "EXT_FLAG,EXT_FLAG1,COMPOUND_GROUP,COMPOUND_FLAG,COMPOUND_EXEC,COMPOUND_OPER,COMPOUND_DATE,CREATEUSER,CREATEDEPT,CREATETIME,"
				+ "UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,ID,DEPT_NAME,DRUG_DEPT_NAME,DRUG_TYPE_NAME,DRUG_QUALITY_NAME,"
				+ "APPLY_OPERNAME,PATIENT_DEPT_NAME,DRUGED_DEPT_NAME,DRUGED_EMPL_NAME,DOSE_MODEL_NAME,PRINT_EMPL_NAME,CANCEL_EMPL_NAME,RECIPE_DEPT_NAME,"
				+ "RECIPE_OPER_NAME,COMPOUND_OPER_NAME,HOSPITAL_ID,AREA_CODE from "
				+ " (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(" t where t.op_type in('4','5')");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientMedicinelistNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_MEDICINELIST_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_MEDICINELIST_MID";
		} else {
			fromTable = "T_INPATIENT_MEDICINELIST_MID t";
			toTable = "T_INPATIENT_MEDICINELIST";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,RECIPE_NO,SEQUENCE_NO,TRANS_TYPE,INPATIENT_NO,NAME,PAYKIND_CODE,PACT_CODE,INHOS_DEPTCODE,NURSE_CELL_CODE,"
				+ "RECIPE_DEPTCODE,EXECUTE_DEPTCODE,MEDICINE_DEPTCODE,RECIPE_DOCCODE,DRUG_CODE,FEE_CODE,CENTER_CODE,DRUG_NAME,SPECS,"
				+ "DRUG_TYPE,DRUG_QUALITY,HOME_MADE_FLAG,UNIT_PRICE,CURRENT_UNIT,PACK_QTY,QTY,DAYS,TOT_COST,OWN_COST,PAY_COST,PUB_COST,"
				+ "ECO_COST,UPDATE_SEQUENCENO,SENDDRUG_SEQUENCE,SENDDRUG_FLAG,BABY_FLAG,JZQJ_FLAG,BROUGHT_FLAG,EXT_FLAG,INVOICE_NO,"
				+ "BALANCE_NO,BALANCE_STATE,NOBACK_NUM,EXT_CODE,EXT_OPERCODE,EXT_DATE,APPRNO,CHARGE_OPERCODE,CHARGE_DATE,FEE_OPERCODE,"
				+ "FEE_DATE,EXEC_OPERCODE,EXEC_DATE,SENDDRUG_OPERCODE,SENDDRUG_DATE,CHECK_OPERCODE,CHECK_NO,MO_ORDER,MO_EXEC_SQN,FEE_RATE,"
				+ "FEEOPER_DEPTCODE,UPLOAD_FLAG,EXT_FLAG2,EXT_FLAG1,EXT_FLAG3,MEDICALTEAM_CODE,OPERATIONNO,TRANSACTION_SEQUENCE_NUMBER,"
				+ "SI_TRANSACTION_DATETIME,HIS_RECIPE_NO,SI_RECIPE_NO,HIS_CANCEL_RECIPE_NO,SI_CANCEL_RECIPE_NO,CREATEUSER,CREATEDEPT,CREATETIME,"
				+ "UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,OPERATION_ID,INHOS_DEPTNAME,NURSE_CELL_NAME,RECIPE_DEPTNAME,"
				+ "EXECUTE_DEPTNAME,MEDICINE_DEPTNAME,RECIPE_DOCNAME,CURRENT_UNIT_NAME,HOSPITAL_ID,AREA_CODE from "
				+ " (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientItemlistNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_ITEMLIST_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_ITEMLIST_MID";
		} else {
			fromTable = "T_INPATIENT_ITEMLIST_MID t";
			toTable = "T_INPATIENT_ITEMLIST";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,RECIPE_NO,SEQUENCE_NO,TRANS_TYPE,INPATIENT_NO,NAME,PAYKIND_CODE,PACT_CODE,UPDATE_SEQUENCENO,INHOS_DEPTCODE,NURSE_CELL_CODE,"
				+ "RECIPE_DEPTCODE,EXECUTE_DEPTCODE,STOCK_DEPTCODE,RECIPE_DOCCODE,ITEM_CODE,FEE_CODE,CENTER_CODE,ITEM_NAME,UNIT_PRICE,QTY,CURRENT_UNIT,"
				+ "PACKAGE_CODE,PACKAGE_NAME,TOT_COST,OWN_COST,PAY_COST,PUB_COST,ECO_COST,SENDMAT_SEQUENCE,SEND_FLAG,BABY_FLAG,JZQJ_FLAG,BROUGHT_FLAG,"
				+ "EXT_FLAG,INVOICE_NO,BALANCE_NO,BALANCE_STATE,NOBACK_NUM,EXT_CODE,EXT_OPERCODE,EXT_DATE,APPRNO,CHARGE_OPERCODE,CHARGE_DATE,CONFIRM_NUM,"
				+ "MACHINE_NO,EXEC_OPERCODE,EXEC_DATE,SEND_OPERCODE,FEE_OPERCODE,FEE_DATE,SEND_DATE,CHECK_OPERCODE,CHECK_NO,MO_ORDER,MO_EXEC_SQN,FEE_RATE,"
				+ "FEEOPER_DEPTCODE,UPLOAD_FLAG,EXT_FLAG1,EXT_FLAG2,EXT_FLAG3,ITEM_FLAG,MEDICALTEAM_CODE,OPERATIONNO,TRANSACTION_SEQUENCE_NUMBER,SI_TRANSACTION_DATETIME,"
				+ "HIS_RECIPE_NO,SI_RECIPE_NO,HIS_CANCEL_RECIPE_NO,SI_CANCEL_RECIPE_NO,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,"
				+ "STOP_FLG,DEL_FLG,OPERATION_ID,INHOS_DEPTNAME,NURSE_CELL_NAME,RECIPE_DEPTNAME,EXECUTE_DEPTNAME,STOCK_DEPTNAME,RECIPE_DOCNAME,FEE_NAME,"
				+ "CENTER_NAME,CURRENT_UNIT_NAME,HOSPITAL_ID,AREA_CODE from " + " (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientCancelitemNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_CANCELITEM_NOW t where t.inpatient_no in (:list) and t.APPLY_FLAG = 2";
			toTable = "T_INPATIENT_CANCELITEM_MID";
		} else {
			fromTable = "T_INPATIENT_CANCELITEM_MID t where t.APPLY_FLAG = 2";
			toTable = "T_INPATIENT_CANCELITEM";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select APPLY_NO,BILL_CODE,INPATIENT_NO,NAME,BABY_FLAG,DEPT_CODE,NURSE_CELL_CODE,DRUG_FLAG,ITEM_CODE,ITEM_NAME,SPECS,SALE_PRICE,"
				+ "QUANTITY,DAYS,PRICE_UNIT,EXEC_DPCD,OPER_CODE,OPER_DATE,OPER_DPCD,RECIPE_NO,SEQUENCE_NO,BILL_NO,CONFIRM_FLAG,CONFIRM_DPCD,CONFIRM_CODE,"
				+ "CONFIRM_DATE,CHARGE_FLAG,CHARGE_CODE,CHARGE_DATE,EXT_FLAG3,QTY,PACKAGE_CODE,PACKAGE_NAME,CARD_NO,RETURN_REASON,CREATEUSER,CREATEDEPT,"
				+ "CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,APPLY_FLAG,DEPT_NAME,NURSE_CELL_NAME,EXEC_DPCD_NAME,OPER_NAME,"
				+ "OPER_DPCD_NAME,CONFIRM_DPCD_NAME,CONFIRM_NAME,CHARGE_NAME,INVO_CODE,HOSPITAL_ID,AREA_CODE from "
				+ " (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 "
				+ " where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientBalancepayNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			List<String> invoices = this.getInvoices((String) map.get("date"));
			pMap.put("list", invoices);
			fromTable = "T_INPATIENT_BALANCEPAY_NOW t where t.invoice_no in (:list)";
			toTable = "T_INPATIENT_BALANCEPAY_MID";
		} else {
			fromTable = "T_INPATIENT_BALANCEPAY_MID t";
			toTable = "T_INPATIENT_BALANCEPAY";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,INVOICE_NO,TRANS_TYPE,TRANS_KIND,PAY_WAY,BALANCE_NO,COST,COUNT_NUM,CHANGE_PREPAYCOST,BANK_CODE,BANK_NAME,"
				+ "BANK_ACCOUNT,BANK_ACCOUTNAME,POSTRANS_NO,REUTRNORSUPPLY_FLAG,BALANCE_OPERCODE,BALANCE_DATE,CREATEUSER,CREATEDEPT,CREATETIME,"
				+ "UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,BALANCE_OPERNAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientBalanceheadNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_BALANCEHEAD_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_BALANCEHEAD_MID";
		} else {
			fromTable = "T_INPATIENT_BALANCEHEAD_MID t";
			toTable = "T_INPATIENT_BALANCEHEAD";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,INVOICE_NO,TRANS_TYPE,INPATIENT_NO,BALANCE_NO,PAYKIND_CODE,PACT_CODE,PREPAY_COST,CHANGE_PREPAYCOST,TOT_COST,"
				+ "OWN_COST,PAY_COST,PUB_COST,ECO_COST,DER_COST,CHANGE_TOTCOST,SUPPLY_COST,RETURN_COST,FOREGIFT_COST,BEGIN_DATE,END_DATE,"
				+ "BALANCE_TYPE,BALANCE_OPERCODE,BALANCE_DATE,ACCOUNT_PAY,OFFICE_PAY,LARGE_PAY,MILTARY_PAY,CASH_PAY,FINGRP_CODE,PRINT_TIMES,"
				+ "CHECK_NO,MAININVOICE_FLAG,WASTE_FLAG,EXT1_FLAG,EXT_CODE,LAST_FLAG,NAME,BALANCEOPER_DEPTCODE,BURSARY_ADJUSTOVERTOP,WASTE_OPERCODE,"
				+ "WASTE_DATE,CHECK_FLAG,CHECK_OPCD,CHECK_DATE,DAYBALANCE_FLAG,DAYBALANCE_NO,DAYBALANCE_OPCD,DAYBALANCE_DATE,CREATEUSER,CREATEDEPT,"
				+ "CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,BALANCE_OPERNAME,BALANCEOPER_DEPTNAME,WASTE_OPERNAME,"
				+ "CHECK_OPCD_NAME,DAYBALANCE_OPCD_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientInprepayNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_INPREPAY_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_INPREPAY_MID";
		} else {
			fromTable = "T_INPATIENT_INPREPAY_MID t";
			toTable = "T_INPATIENT_INPREPAY";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,TRANS_TYPE,INPATIENT_NO,HAPPEN_NO,NAME,PREPAY_COST,PAY_WAY,DEPT_CODE,RECEIPT_NO,STAT_DATE,BALANCE_DATE,"
				+ "BALANCE_STATE,PREPAY_STATE,OLD_RECIPENO,OPEN_BANK,OPEN_ACCOUNTS,INVOICE_NO,BALANCE_NO,BALANCE_OPERCODE,REPORT_FLAG,"
				+ "CHECK_NO,FINGRP_CODE,WORK_NAME,TRANS_FLAG,CHANGE_BALANCE_NO,TRANS_CODE,TRANS_DATE,PRINT_FLAG,EXT_FLAG,EXT1_FLAG,POSTRANS_NO,"
				+ "DAYBALANCE_NO,DAYBALANCE_OPCD,DAYBALANCE_DATE,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,"
				+ "STOP_FLG,DEL_FLG,DEPT_NAME,BALANCE_OPERNAME,FINGRP_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientFeeinfoNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_FEEINFO_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_FEEINFO_MID";
		} else {
			fromTable = "T_INPATIENT_FEEINFO_MID t";
			toTable = "T_INPATIENT_FEEINFO";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,RECIPE_NO,FEE_CODE,TRANS_TYPE,INPATIENT_NO,NAME,PAYKIND_CODE,PACT_CODE,INHOS_DEPTCODE,NURSE_CELL_CODE,RECIPE_DEPTCODE,"
				+ "EXECUTE_DEPTCODE,STOCK_DEPTCODE,RECIPE_DOCCODE,TOT_COST,OWN_COST,PAY_COST,PUB_COST,ECO_COST,CHARGE_OPERCODE,CHARGE_DATE,FEE_OPERCODE,"
				+ "FEE_DATE,BALANCE_OPERCODE,BALANCE_DATE,INVOICE_NO,BALANCE_NO,BALANCE_STATE,CHECK_NO,BABY_FLAG,EXT_FLAG,EXT_CODE,EXT_DATE,EXT_OPERCODE,"
				+ "FEEOPER_DEPTCODE,EXT_FLAG1,EXT_FLAG2,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,"
				+ "OPERATION_ID,INHOS_DEPTNAME,NURSE_CELL_NAME,RECIPE_DEPTNAME,EXECUTE_DEPTNAME,STOCK_DEPTNAME,RECIPE_DOCNAME,HOUSE_DOC_CODE,CHARGE_DOC_CODE,"
				+ "CHIEF_DOC_CODE,DUTY_NURSE_CODE,HOUSE_DOC_NAME,CHARGE_DOC_NAME,CHIEF_DOC_NAME,DUTY_NURSE_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientExecundrugNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_EXECUNDRUG_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_EXECUNDRUG_MID";
		} else {
			fromTable = "T_INPATIENT_EXECUNDRUG_MID t";
			toTable = "T_INPATIENT_EXECUNDRUG";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,EXEC_ID,DEPT_CODE,NURSE_CELL_CODE,LIST_DPCD,INPATIENT_NO,PATIENT_NO,MO_ORDER,DOC_CODE,DOC_NAME,MO_DATE,BABY_FLAG,"
				+ "HAPPEN_NO,SET_ITMATTR,SET_SUBTBL,TYPE_CODE,DECMPS_STATE,CHARGE_STATE,PRN_EXELIST,PRN_MORLIST,NEED_CONFIRM,UNDRUG_CODE,UNDRUG_NAME,"
				+ "CLASS_CODE,CLASS_NAME,EXEC_DPCD,EXEC_DPNM,COMB_NO,MAIN_DRUG,DFQ_FREQ,DFQ_CEXP,QTY_TOT,STOCK_UNIT,UNIT_PRICE,USE_TIME,EMC_FLAG,"
				+ "VALID_FLAG,VALID_DATE,VALID_USERCD,CONFIRM_FLAG,CONFIRM_DATE,CONFIRM_USERCD,CONFIRM_DEPTCD,EXEC_FLAG,EXEC_DATE,EXEC_USERCD,EXEC_DEPTCD,"
				+ "CHARGE_FLAG,CHARGE_DATE,CHARGE_USERCD,CHARGE_DEPTCD,ITEM_NOTE,APPLY_NO,MO_NOTE1,MO_NOTE2,DECO_DATE,FIRST_DAY,EXEC_PRNFLAG,"
				+ "EXEC_PRNDATE,EXEC_PRNUSERCD,RECIPE_NO,SEQUENCE_NO,SUBTBL_FLAG,LAB_CODE,LAB_BARCODE,CHARGE_PRNFLAG,CHARGE_PRNDATE,CHARGE_PRNUSERCD,"
				+ "CIRCULT_PRNFLAG,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,DEPT_NAME,NURSE_CELL_NAME,"
				+ "LIST_DPCD_NAME,VALID_USERCD_NAME,CONFIRM_USERCD_NAME,CONFIRM_DEPTCD_NAME,EXEC_USERCD_NAME,EXEC_DEPTCD_NAME,CHARGE_USERCD_NAME,CHARGE_DEPTCD_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientExecdrugNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_EXECDRUG_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_EXECDRUG_MID";
		} else {
			fromTable = "T_INPATIENT_EXECDRUG_MID t";
			toTable = "T_INPATIENT_EXECDRUG";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,EXEC_ID,DEPT_CODE,NURSE_CELL_CODE,LIST_DPCD,INPATIENT_NO,PATIENT_NO,MO_ORDER,DOC_CODE,DOC_NAME,MO_DATE,BABY_FLAG,"
				+ "HAPPEN_NO,SET_ITMATTR,SET_SUBTBL,TYPE_CODE,DECMPS_STATE,CHARGE_STATE,NEED_DRUG,PRN_EXELIST,PRN_MORLIST,NEED_CONFIRM,DRUG_CODE,"
				+ "DRUG_NAME,BASE_DOSE,DOSE_UNIT,MIN_UNIT,PRICE_UNIT,PACK_QTY,SPECS,DOSE_MODEL_CODE,DRUG_TYPE,DRUG_QUALITY,ITEM_PRICE,STOCK_MIN,"
				+ "COMB_NO,MAIN_DRUG,USAGE_CODE,USE_NAME,ENGLISH_AB,FREQUENCY_CODE,FREQUENCY_NAME,DOSE_ONCE,USE_DAYS,QTY_TOT,USE_TIME,PHARMACY_CODE,"
				+ "EXEC_DPCD,VALID_FLAG,VALID_DATE,VALID_USERCD,DRUGED_FLAG,DRUGED_DATE,DRUGED_USERCD,DRUGED_DEPTCD,PRN_FLAG,PRN_DATE,PRN_USERCD,"
				+ "PRN_DEPTCD,SET_CODE,SET_SEQN,EXEC_FLAG,EXEC_DATE,EXEC_USERCD,EXEC_DEPTCD,EXEC_PRNFLAG,EXEC_PRNDATE,EXEC_PRNUSERCD,CHARGE_FLAG,"
				+ "CHARGE_DATE,CHARGE_USERCD,CHARGE_DEPTCD,RECIPE_NO,SEQUENCE_NO, MO_NOTE1,MO_NOTE2,DECO_DATE,CHARGE_PRNFLAG,CHARGE_PRNDATE,CHARGE_PRNUSERCD,"
				+ "CIRCULT_PRNFLAG,COMPOUND_FLAG,COMPOUND_EXEC,COMPOUND_OPER,COMPOUND_DEPT,COMPOUND_DATE,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,"
				+ "UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,DEPT_NAME,NURSE_CELL_NAME,LIST_DPCD_NAME,PHARMACY_NAME,EXEC_DPCD_NAME,"
				+ "VALID_USERCD_NAME,DRUGED_USERCD_NAME,DRUGED_DEPTCD_NAME,EXEC_USERCD_NAME,EXEC_DEPTCD_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientOrderNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_ORDER_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_ORDER_MID";
		} else {
			fromTable = "T_INPATIENT_ORDER_MID t";
			toTable = "T_INPATIENT_ORDER";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select EXEC_ID,INPATIENT_NO,PATIENT_NO,DEPT_CODE,NURSE_CELL_CODE,LIST_DPCD,MO_ORDER,DOC_CODE,DOC_NAME,MO_DATE,BABY_FLAG,"
				+ "HAPPEN_NO,SET_ITMATTR,SET_SUBTBL,TYPE_CODE,TYPE_NAME,DECMPS_STATE,CHARGE_STATE,NEED_DRUG,PRN_EXELIST,PRM_MORLIST,NEED_CONFIRM,"
				+ "ITEM_TYPE,ITEM_CODE,ITEM_NAME,CLASS_CODE,CLASS_NAME,PHARMACY_CODE,EXEC_DPCD,EXEC_DPNM,BASE_DOSE,DOSE_UNIT,MIN_UNIT,PRICE_UNIT,"
				+ "PACK_QTY,SPECS,DOSE_MODEL_CODE,DRUG_TYPE,DRUG_QUALITY,ITEM_PRICE,COMB_NO,MAIN_DRUG,MO_STAT,USAGE_CODE,USE_NAME,ENGLISH_AB,"
				+ "FREQUENCY_CODE,FREQUENCY_NAME,DOSE_ONCE,STOCK_MIN,QTY_TOT,USE_DAYS,DATE_BGN,DATE_END,REC_USERCD,REC_USERNM,CONFIRM_FLAG,"
				+ "CONFIRM_DATE,CONFIRM_USERCD,DC_FLAG,DC_DATE,DC_CODE,DC_NAME,DC_DOCCD,DC_DOCNM,DC_USERCD,DC_USERNM,EXECUTE_FLAG,EXECUTE_DATE,"
				+ "EXECUTE_USERCD,DECO_FLAG,DATE_CURMODC,DATE_NXTMODC,MO_NOTE1,MO_NOTE2,HYPOTEST,ITEM_NOTE,APPLY_NO,EMC_FLAG,GET_FLAG,SUBTBL_FLAG,"
				+ "SORT_ID,DC_CONFIRM_DATE,DC_CONFIRM_OPER,DC_CONFIRM_FLAG,LAB_CODE,PERMISSION,PACKAGE_CODE,PACKAGE_NAME,MARK1,MARK2,MARK3,EXEC_TIMES,"
				+ "EXEC_DOSE,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,DRUGPACKAGING_UNIT,DEPT_NAME,"
				+ "NURSE_CELL_NAME,LIST_DPCD_NAME,PATIENT_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientShiftapplyNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_SHIFTAPPLY_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_SHIFTAPPLY_MID";
		} else {
			fromTable = "T_INPATIENT_SHIFTAPPLY_MID t";
			toTable = "T_INPATIENT_SHIFTAPPLY";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,INPATIENT_NO,HAPPEN_NO,NEW_DEPT_CODE,OLD_DEPT_CODE,NEW_NURSE_CELL_CODE,NURSE_CELL_CODE,SHIFT_STATE,"
				+ "CONFIRM_OPERCODE,CONFIRM_DATE,CANCEL_CODE,CANCEL_DATE,MARK,OLD_BED_CODE,NEW_BED_CODE,CREATEUSER,CREATETIME,UPDATEUSER,"
				+ "UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,NEW_DEPT_NAME,OLD_DEPT_NAME,NEW_NURSE_CELL_NAME, NURSE_CELL_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientApplyNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_APPLY_NOW t where t.patient_no in (:list)";
			toTable = "T_INPATIENT_APPLY_MID";
		} else {
			fromTable = "T_INPATIENT_APPLY_MID t";
			toTable = "T_INPATIENT_APPLY";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select ID,APPLY_NUM,APPLY_STATE,PATIENT_TYPE,PATIENT_NO,CLINIC_NO,NAME,SEX_CODE,AGE,DEPT_CODE,NURSE_CELL_CODE,BED_NO,DIAGNOSE,"
				+ "DIAGNOSE_NAME,BLOOD_AIM,QUALITY,INSOURCE,ORDER_TIME,BLOOD_KIND,BLOOD_TYPE_CODE,QUANTITY,STOCK_UNIT,RH,PREGNANT,HEMATIN,HCT,"
				+ "PLATELET,ALT,ANTI_HCV,ANTI_HIV,LUES,HBSAG,BLOODHISTORY,PATIENT_BLOODKIND,IS_CHARGE,APPLY_DOC_CODE,APPLY_TIME,CHARGE_DOC_CODE,"
				+ "REMARKS,MATCH_RESULT,CANCEL_CODE,CANCEL_DATE,ANTI_HBS,HBEAG,ANTI_HBE,ANTI_HBC,APPROVAL_OPER,APPROVAL_DATE,APPLY_BARCODE,GETBLOOD_FLAG,"
				+ "GETBLOOD_OPER,GETBLOOD_DATE,EXAM_RESULT,EXAM_OPER_CODE,EXAM_DATE,SAMPLE_REG_QTY,SAMPLE_REG_OPER,SAMPLE_DATE,BLOOD_TYPE_CODE2,"
				+ "QUANTITY2,STOCK_UNIT2,BLOOD_TYPE_CODE3,QUANTITY3,STOCK_UNIT3,WBC,UNCHECK_REASON,BACKDEAL,BACKDEAL_MEMO,ANTIFILTER_FLAG,BLOODMETHOD,"
				+ "IS_URGENT,PT,APTT,FIB,INFUSIONDAY,FEETYPE,UPDATEUSER,UPDATETIME,DEL_FLG,CREATEUSER,CREATETIME,NURSE_CELL_NAME,APPLY_DOC_NAME,CHARGE_DOC_NAME,"
				+ "SEX_NAME,CANCEL_NAME,APPROVAL_OPER_NAME,DEPT_NAME,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	public void moveInpatientInfoNow(Map<String, Object> map) {
		Map<String, Object> pMap = new HashMap<>();
		String fromTable;
		String toTable;
		String state = (String) map.get("state");
		if ("1".equals(state)) {
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
			fromTable = "T_INPATIENT_INFO_NOW t where t.inpatient_no in (:list)";
			toTable = "T_INPATIENT_INFO_MID";
		} else {
			fromTable = "T_INPATIENT_INFO_MID t";
			toTable = "T_INPATIENT_INFO";
		}
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(toTable);
		sql.append(" select INPATIENT_ID,INPATIENT_NO,MEDICAL_TYPE,MEDICALRECORD_ID,IDCARD_NO,MCARD_NO,PATIENT_NAME,CERTIFICATES_TYPE,CERTIFICATES_NO,"
				+ "REPORT_SEX,REPORT_BIRTHDAY,REPORT_AGE,REPORT_AGEUNIT,PROF_CODE,WORK_NAME,WORK_TEL,WORK_ZIP,HOME,HOME_TEL,HOME_ZIP,DIST,BIRTH_AREA,"
				+ "NATION_CODE,LINKMAN_NAME,LINKMAN_TEL,LINKMAN_ADDRESS,RELA_CODE,MARI,COUN_CODE,HEIGHT,WEIGHT,BLOOD_DRESS,BLOOD_CODE,HEPATITIS_FLAG,"
				+ "ANAPHY_FLAG,IN_DATE,DEPT_CODE,PAYKIND_CODE,PACT_CODE,BEDINFO_ID,IN_CIRCS,DIAG_NAME,IN_AVENUE,IN_SOURCE,IN_TIMES,PREPAY_COST,CHANGE_PREPAYCOST,"
				+ "ALTER_TYPE,ALTER_BEGIN,ALTER_END,MONEY_ALERT,TOT_COST,OWN_COST,PAY_COST,PUB_COST,ECO_COST,FREE_COST,CHANGE_TOTCOST,UPPER_LIMIT,FEE_INTERVAL,"
				+ "BALANCE_NO,BALANCE_COST,BALANCE_PREPAY,BALANCE_DATE,STOP_ACOUNT,BABY_FLAG,CASE_FLAG,IN_STATE,LEAVE_FLAG,PREPAY_OUTDATE,OUT_DATE,ZG,"
				+ "EMPL_CODE,IN_ICU,CASESEND_FLAG,TEND,CRITICAL_FLAG,PREFIXFEE_DATE,BLOOD_LATEFEE,DAY_LIMIT,LIMIT_TOT,LIMIT_OVERTOP,PROCREATE_PCNO,BURSARY_TOTMEDFEE,"
				+ "REMARK,BED_LIMIT,AIR_LIMIT,BEDOVERDEAL,EXT_FLAG,EXT_FLAG1,EXT_FLAG2,BOARD_COST,BOARD_PREPAY,BOARD_STATE,OWN_RATE,PAY_RATE,EXT_NUMBER,"
				+ "EXT_CODE,CREATEUSER,CREATEDEPT,CREATETIME,UPDATEUSER,UPDATETIME,DELETEUSER,DELETETIME,STOP_FLG,DEL_FLG,IDCARD_ID,DIAG_OUTSTATE,HAVE_BABY_FLAG,"
				+ "PATIENT_STATUS,AGE,REPORT_SEX_NAME,DEPT_NAME,EMPL_NAME,BEDWARD_ID,BEDWARD_NAME,BED_ID,BED_NAME,NURSE_CELL_CODE,HOUSE_DOC_CODE,"
				+ "CHARGE_DOC_CODE,CHIEF_DOC_CODE,DUTY_NURSE_CODE,NURSE_CELL_NAME,HOUSE_DOC_NAME,CHARGE_DOC_NAME,CHIEF_DOC_NAME,DUTY_NURSE_NAME,VERSION,HOSPITAL_ID,AREA_CODE "
				+ " from (select t.*,rownum as n from ");
		sql.append(fromTable);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row");
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		int i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteInpatientBalancelistNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_BALANCELIST_NOW";
			inWhere = "T_INPATIENT_BALANCELIST_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> mapbalance = this.getInpatentNos((String) map.get("date"));
			List<String> inpatients = (List<String>) mapbalance.get("inpatient");
			pMap.put("list", inpatients);
		} else {
			tableName = "T_INPATIENT_BALANCELIST_MID";
			inWhere = "T_INPATIENT_BALANCELIST_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteDrugOutstoreNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_BALANCELIST_NOW";
			inWhere = "T_DRUG_OUTSTORE_NOW t where t.op_type in('4','5') and t.recipe_no in (:list)";
			List<String> recipes = this.getRecipes((String) map.get("date"));
			pMap.put("list", recipes);
		} else {
			tableName = "T_DRUG_OUTSTORE_MID";
			inWhere = "T_DRUG_OUTSTORE_MID t where t.op_type in('4','5')";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteDrugApplyoutNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_DRUG_APPLYOUT_NOW";
			inWhere = "T_DRUG_APPLYOUT_NOW t where t.patient_id in (:list) and t.op_type in('4','5')";
			Map<String, Object> mapbalance = this.getInpatentNos((String) map.get("date"));
			List<String> inpatients = (List<String>) mapbalance.get("inpatient");
			pMap.put("list", inpatients);
		} else {
			tableName = "T_DRUG_APPLYOUT_MID";
			inWhere = "T_DRUG_APPLYOUT_MID t where t.op_type in('4','5')";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");

	}

	@Override
	public void deleteInpatientMedicinelistNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_MEDICINELIST_NOW";
			inWhere = "T_INPATIENT_MEDICINELIST_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> mapbalance = this.getInpatentNos((String) map.get("date"));
			List<String> inpatients = (List<String>) mapbalance.get("inpatient");
			pMap.put("list", inpatients);
		} else {
			tableName = "T_INPATIENT_MEDICINELIST_MID";
			inWhere = "T_INPATIENT_MEDICINELIST_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");

	}

	@Override
	public void deleteInpatientItemlistNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_ITEMLIST_NOW";
			inWhere = "T_INPATIENT_ITEMLIST_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> mapbalance = this.getInpatentNos((String) map.get("date"));
			List<String> inpatients = (List<String>) mapbalance.get("inpatient");
			pMap.put("list", inpatients);
		} else {
			tableName = "T_INPATIENT_ITEMLIST_MID";
			inWhere = "T_INPATIENT_ITEMLIST_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteInpatientCancelitemNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_CANCELITEM_NOW";
			inWhere = "T_INPATIENT_CANCELITEM_NOW t where t.inpatient_no in (:list) and t.APPLY_FLAG = 2";
			Map<String, Object> mapbalance = this.getInpatentNos((String) map.get("date"));
			List<String> inpatients = (List<String>) mapbalance.get("inpatient");
			pMap.put("list", inpatients);
		} else {
			tableName = "T_INPATIENT_CANCELITEM_MID";
			inWhere = "T_INPATIENT_CANCELITEM_MID t where t.APPLY_FLAG = 2";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.apply_no in (select t1.apply_no from (select t.apply_no, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteInpatientBalancepayNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_BALANCEPAY_NOW";
			inWhere = "T_INPATIENT_BALANCEPAY_NOW t where t.invoice_no in (:list)";
			List<String> invoices = this.getInvoices((String) map.get("date"));
			pMap.put("list", invoices);
		} else {
			tableName = "T_INPATIENT_BALANCEPAY_MID";
			inWhere = "T_INPATIENT_BALANCEPAY_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteInpatientBalanceheadNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_BALANCEHEAD_NOW";
			inWhere = "T_INPATIENT_BALANCEHEAD_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_BALANCEHEAD_MID";
			inWhere = "T_INPATIENT_BALANCEHEAD_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");

	}

	@Override
	public void deleteInpatientInprepayNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_INPREPAY_NOW";
			inWhere = "T_INPATIENT_INPREPAY_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_INPREPAY_MID";
			inWhere = "T_INPATIENT_INPREPAY_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");

	}

	@Override
	public void deleteInpatientFeeinfoNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_FEEINFO_NOW";
			inWhere = "T_INPATIENT_FEEINFO_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_FEEINFO_MID";
			inWhere = "T_INPATIENT_FEEINFO_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");

	}

	@Override
	public void deleteInpatientExecundrugNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_EXECUNDRUG_NOW";
			inWhere = "T_INPATIENT_EXECUNDRUG_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_EXECUNDRUG_MID";
			inWhere = "T_INPATIENT_EXECUNDRUG_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");

	}

	@Override
	public void deleteInpatientExecdrugNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_EXECDRUG_NOW";
			inWhere = "T_INPATIENT_EXECDRUG_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_EXECDRUG_MID";
			inWhere = "T_INPATIENT_EXECDRUG_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");

	}

	@Override
	public void deleteInpatientOrderNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_ORDER_NOW";
			inWhere = "T_INPATIENT_ORDER_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_ORDER_MID";
			inWhere = "T_INPATIENT_ORDER_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 where t2.EXEC_ID in (select t1.EXEC_ID from (select t.EXEC_ID, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteInpatientShiftapplyNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_SHIFTAPPLY_NOW";
			inWhere = "T_INPATIENT_SHIFTAPPLY_NOW t where t.inpatient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_SHIFTAPPLY_MID";
			inWhere = "T_INPATIENT_SHIFTAPPLY_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteInpatientApplyNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_APPLY_NOW";
			inWhere = "T_INPATIENT_APPLY_NOW t where t.patient_no in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_APPLY_MID";
			inWhere = "T_INPATIENT_APPLY_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 "
				+ " where t2.id in (select t1.id from (select t.id, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
	}

	@Override
	public void deleteInpatientInfoNow(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		String state = (String) map.get("state");
		String tableName;
		String inWhere;
		pMap.put("page", map.get("page"));
		pMap.put("row", map.get("row"));
		int i;
		if ("1".equals(state)) {
			tableName = "T_INPATIENT_INFO_NOW";
			inWhere = "T_INPATIENT_INFO_NOW t where t.INPATIENT_NO in (:list)";
			Map<String, Object> inpatient = this.getInpatentNos((String) map.get("date"));
			List<String> inpatiens = (List<String>) inpatient.get("inpatient");
			pMap.put("list", inpatiens);
		} else {
			tableName = "T_INPATIENT_INFO_MID";
			inWhere = "T_INPATIENT_INFO_MID t";
		}
		sql.append("delete from ");
		sql.append(tableName);
		sql.append(" t2 where t2.INPATIENT_ID in (select t1.INPATIENT_ID from (select t.INPATIENT_ID, rownum as n from ");
		sql.append(inWhere);
		sql.append(") t1 where t1.n > (:page -1) * :row and t1.n <=:page * :row)");
		i = namedParameterJdbcTemplate.update(sql.toString(), pMap);
		logger.info("[sql: " + sql + "]");
		logger.info("[parem: " + pMap.toString() + "]");
		logger.info("[result: " + i + "]");
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
				+ "FROM T_SYS_MOVEDATA_LOG t WHERE t.OPT_TYPE = :optType AND t.DATA_TYPE = :dateType "
				+ "AND t.ISSUCCESS = 2 order by t.start_date");
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
