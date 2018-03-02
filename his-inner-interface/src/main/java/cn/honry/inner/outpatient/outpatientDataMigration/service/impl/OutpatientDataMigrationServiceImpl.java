package cn.honry.inner.outpatient.outpatientDataMigration.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.visitor.functions.If;

import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.inner.outpatient.outpatientDataMigration.dao.OutpatientDataMigrationDAO;
import cn.honry.inner.outpatient.outpatientDataMigration.service.OutpatientDataMigrationService;

@Service("outpatientDataMigrationService")
@Transactional
@SuppressWarnings({"all"})
public class OutpatientDataMigrationServiceImpl implements OutpatientDataMigrationService{

	public static final long rows = 100;
	
	@Autowired
	@Qualifier(value = "outpatientDataMigrationDAO")
	private OutpatientDataMigrationDAO outpatientDataMigrationDAO;


	@Override
	public void saveOrUpdate(MoveDataLog moveDataLog) {
		outpatientDataMigrationDAO.saveOrUpdate(moveDataLog);
	}

	@Override
	public Map<String, Object> queryClincCodeNows(String date) {
		return outpatientDataMigrationDAO.queryClincCodeNows(date);
	}

	@Override
	public List<String> queryInvoiceNoNows(String date) {
		return outpatientDataMigrationDAO.queryInvoiceNoNows(date);
	}

	@Override
	public List<String> queryRecipeNoNows(String date) {
		return outpatientDataMigrationDAO.queryRecipeNoNows(date);
	}

	@Override
	public Map<String, Object> pageOutStoreNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryOutStoreNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageApplyOutNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryApplyOutNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageInpatientCancelitemNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryInpatientCancelitemNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageFinanceInvoicedetailNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryFinanceInvoicedetailNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageBusinessPayModeNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryBusinessPayModeNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageFinanceInvoiceInfoNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryFinanceInvoicedetailNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageStoRecipeNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryStoRecipeNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageOutpatientFeedetailNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryOutpatientFeedetailNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageOutpatientRecipedetailNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryOutpatientRecipedetailNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageRegistrationNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		long total = outpatientDataMigrationDAO.queryRegistrationNows(date,state);
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageRegisterPreregisterNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = outpatientDataMigrationDAO.queryRegisterPreregisterNows(date,state);
		if (StringUtils.isBlank(date)) {
			return map;
		}
		long total = (long) map.get("total");
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public Map<String, Object> pageRegisterScheduleNows(String date, String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = outpatientDataMigrationDAO.queryRegisterScheduleNows(date,state);
		if (StringUtils.isBlank(date)) {
			return map;
		}
		long total = (long) map.get("total");
		if(total == 0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}

	@Override
	public void moveOutStoreNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveOutStoreNows(map);
	}

	@Override
	public void moveApplyOutNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveApplyOutNows(map);
	}

	@Override
	public void moveInpatientCancelitemNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveInpatientCancelitemNows(map);
	}

	@Override
	public void moveFinanceInvoicedetailNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveFinanceInvoicedetailNows(map);
	}

	@Override
	public void moveBusinessPayModeNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveBusinessPayModeNows(map);
	}

	@Override
	public void moveFinanceInvoiceInfoNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveFinanceInvoiceInfoNows(map);		
	}

	@Override
	public void moveStoRecipeNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveStoRecipeNows(map);		
	}

	@Override
	public void moveOutpatientFeedetailNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveOutpatientFeedetailNows(map);		
	}

	@Override
	public void moveOutpatientRecipedetailNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveOutpatientRecipedetailNows(map);		
	}

	@Override
	public void moveRegistrationNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveRegistrationNows(map);		
	}

	@Override
	public void moveRegisterPreregisterNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveRegisterPreregisterNows(map);		
	}

	@Override
	public void moveRegisterScheduleNows(Map<String, Object> map) throws Exception {
		outpatientDataMigrationDAO.moveRegisterScheduleNows(map);		
	}

	@Override
	public MoveDataLog queryErrorMoveDataLog(Integer optType, Integer dateType,
			String tableName, String dataDate) {
		return outpatientDataMigrationDAO.queryErrorMoveDataLog(optType, dateType, tableName, dataDate);
	}

	@Override
	public List<MoveDataLog> queryMoveDataLogs(Integer optType, Integer dateType) {
		return outpatientDataMigrationDAO.queryMoveDataLogs(optType, dateType);
	}

	@Override
	public MoveDataLog querySuccessMoveDataLog(Integer optType,
			Integer dateType, String tableName, String dataDate) {
		return outpatientDataMigrationDAO.querySuccessMoveDataLog(optType, dateType, tableName, dataDate);
	}

	@Override
	public Map<String, Object> queryRegisterPreregisterNows(String date,
			String state) {
		return outpatientDataMigrationDAO.queryRegisterPreregisterNows(date, state);
	}

	@Override
	public Map<String, Object> queryRegisterScheduleNows(String date,
			String state) {
		return outpatientDataMigrationDAO.queryRegisterScheduleNows(date, state);
	}
	
	
	
}