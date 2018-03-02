package cn.honry.inner.inpatient.inpatient.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.MoveDataLog;
import cn.honry.inner.inpatient.inpatient.dao.InpatientDao;
import cn.honry.inner.inpatient.inpatient.service.InpatientService;
/**
 * 住院迁移数据service实现
 * @author hzr
 *
 */
@Service("inpatientService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientServiceImpl implements InpatientService {

	@Autowired
	@Qualifier(value = "inpatientDao")
	private InpatientDao inpatientDao;
	
	public static final long rows = 100;
	
	@Override
	public void saveOrUpdate(MoveDataLog moveDataLog) {
		inpatientDao.saveOrUpdate(moveDataLog);
	}
	
	public Map<String, Object> getInpatentNos(String date){
		return inpatientDao. getInpatentNos(date);
	}
	
	public List<String> getRecipes(String date){
		return inpatientDao.getRecipes(date);
	}
	
	public List<String> getInvoices(String date){
		return inpatientDao.getInvoices(date);
	}
	
	public Map<String, Object> countInpatientBalancelistNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientBalancelistNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	
	
	
	
	public Map<String, Object> countDrugOutstoreNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countDrugOutstoreNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countDrugApplyoutNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countDrugApplyoutNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientMedicinelistNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientMedicinelistNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientItemlistNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientItemlistNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientCancelitemNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientCancelitemNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientBalancepayNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientBalancepayNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientBalanceheadNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientBalanceheadNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientInprepayNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientInprepayNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientFeeinfoNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientFeeinfoNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientExecundrugNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientExecundrugNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientExecdrugNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientExecdrugNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientOrderNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientOrderNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientShiftapplyNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientShiftapplyNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String, Object> countInpatientApplyNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientApplyNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public Map<String,Object> countInpatientInfoNow(String date, String state){
		Map<String, Object> map = new HashMap<String, Object>();
		long total = inpatientDao.countInpatientInfoNow(date,state);
		if(total==0){
			return null;
		}
		map.put("total", total);
		long page = total % rows == 0 ? (total / rows) : (total / rows) + 1;
		map.put("page", page);
		map.put("row", rows);
		return map;
	}
	public void moveInpatientBalancelistNow(Map<String, Object> map) throws Exception{
		inpatientDao.moveInpatientBalancelistNow(map);
	}
	public void moveDrugOutstoreNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveDrugOutstoreNow(map);
	}
	public void moveDrugApplyoutNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveDrugApplyoutNow(map);
	}
	public void moveInpatientMedicinelistNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientMedicinelistNow(map);
	}
	public void moveInpatientItemlistNow(Map<String, Object> map) throws Exception{
		inpatientDao.moveInpatientItemlistNow(map);
	}
	public void moveInpatientCancelitemNow(Map<String, Object> map) throws Exception{
		inpatientDao.moveInpatientCancelitemNow(map);
	}
	public void moveInpatientBalancepayNow(Map<String, Object> map) throws Exception{
		inpatientDao.moveInpatientBalancepayNow(map);
	}
	public void moveInpatientBalanceheadNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientBalanceheadNow(map);
	}
	public void moveInpatientInprepayNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientInprepayNow(map);
	}
	public void moveInpatientFeeinfoNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientFeeinfoNow(map);
	}
	public void moveInpatientExecundrugNow(Map<String, Object> map) throws Exception{
		inpatientDao.moveInpatientExecundrugNow(map);
	}
	public void moveInpatientExecdrugNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientExecdrugNow(map);
	}
	public void moveInpatientOrderNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientOrderNow(map);
	}
	public void moveInpatientShiftapplyNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientShiftapplyNow(map);
	}
	public void moveInpatientApplyNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientApplyNow(map);
	}
	public void moveInpatientInfoNow(Map<String, Object> map)throws Exception {
		inpatientDao.moveInpatientInfoNow(map);
	}


	@Override
	public void deleteInpatientBalancelistNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientBalancelistNow(map);
	}

	@Override
	public void deleteDrugOutstoreNow(Map<String,Object> map) {
		inpatientDao.deleteDrugOutstoreNow(map);
		
	}
	@Override
	public void deleteDrugApplyoutNow(Map<String,Object> map) {
		inpatientDao.deleteDrugApplyoutNow(map);
		
	}

	@Override
	public void deleteInpatientMedicinelistNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientMedicinelistNow(map);
		
	}

	@Override
	public void deleteInpatientItemlistNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientItemlistNow(map);
		
	}

	@Override
	public void deleteInpatientCancelitemNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientCancelitemNow(map);
		
	}

	@Override
	public void deleteInpatientBalancepayNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientBalancepayNow(map);
		
	}

	@Override
	public void deleteInpatientBalanceheadNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientBalanceheadNow(map);
		
	}

	@Override
	public void deleteInpatientInprepayNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientInprepayNow(map);
		
	}

	@Override
	public void deleteInpatientFeeinfoNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientFeeinfoNow(map);
		
	}

	@Override
	public void deleteInpatientExecundrugNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientExecundrugNow(map);
		
	}

	@Override
	public void deleteInpatientExecdrugNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientExecdrugNow(map);
		
	}

	@Override
	public void deleteInpatientOrderNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientOrderNow(map);
		
	}

	@Override
	public void deleteInpatientShiftapplyNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientShiftapplyNow(map);
		
	}

	@Override
	public void deleteInpatientApplyNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientApplyNow(map);
		
	}

	@Override
	public void deleteInpatientInfoNow(Map<String,Object> map) {
		inpatientDao.deleteInpatientInfoNow(map);
		
	}

	@Override
	public MoveDataLog queryErrorMoveDataLog(Integer optType, Integer dateType,
			String tableName, String dataDate) {
		return inpatientDao.queryErrorMoveDataLog(optType, dateType, tableName, dataDate);
	}

	@Override
	public MoveDataLog querySuccessMoveDataLog(Integer optType,
			Integer dateType, String tableName, String dataDate) {
		return inpatientDao.querySuccessMoveDataLog(optType, dateType, tableName, dataDate);
	}

	@Override
	public List<MoveDataLog> queryMoveDataLogs(Integer optType, Integer dateType) {
		return inpatientDao.queryMoveDataLogs(optType, dateType);
	}

}
