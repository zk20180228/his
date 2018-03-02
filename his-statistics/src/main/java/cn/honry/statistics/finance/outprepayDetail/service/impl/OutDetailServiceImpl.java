package cn.honry.statistics.finance.outprepayDetail.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OutpatientAccountrecord;
import cn.honry.base.bean.model.OutpatientOutprepay;
import cn.honry.base.bean.model.Patient;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.statistics.finance.outprepayDetail.dao.OutDetailDao;
import cn.honry.statistics.finance.outprepayDetail.service.OutDetailService;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;


@Service("outDetailService")
@Transactional(rollbackFor={Throwable.class})
@SuppressWarnings({"all"})
public class OutDetailServiceImpl implements OutDetailService {
	
	/***
	 * 注入本类Dao
	 */
	@Autowired
	@Qualifier(value = "outDetailDao")
	private OutDetailDao outDetailDao;
	/***
	 * 注入innerCodeService
	 */
	@Autowired
	@Qualifier(value = "innerCodeService")
	private CodeInInterService innerCodeService;
	/***
	 * 注入employeeInInterService
	 */
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	/***
	 * 注入deptInInterService
	 */
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;

	@Override
	public OutpatientOutprepay get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OutpatientOutprepay arg0) {
		
	}

	@Override
	public List<OutpatientOutprepay> queryOutprepay(String page, String rows, String medicalrecordId, String beginDate,
			String endDate) {
		return outDetailDao.queryOutprepay(page, rows, medicalrecordId, beginDate, endDate);
	}

	@Override
	public int queryOutprepayTotal(String page, String rows, String medicalrecordId, String beginDate, String endDate) {
		return outDetailDao.queryOutprepayTotal(page, rows, medicalrecordId, beginDate, endDate);
	}

	@Override
	public List<OutpatientAccountrecord> queryRecord(String page, String rows, String medicalrecordId, String beginDate,
			String endDate) {
		return outDetailDao.queryRecord(page, rows, medicalrecordId, beginDate, endDate);
	}

	@Override
	public int queryRecordTotal(String page, String rows, String medicalrecordId, String beginDate, String endDate) {
		return outDetailDao.queryRecordTotal(page, rows, medicalrecordId, beginDate, endDate);
	}

	@Override
	public List<Patient> queryPatient() {
		return outDetailDao.queryPatient();
	}

	@Override
	public List<Patient> querymedicalrecord(String medicalrecord) {
		return outDetailDao.querymedicalrecord(medicalrecord);
	}

	@Override
	public List<Patient> cardQueryMedicalrecord(String ic, String idCard,String codeCertificate) {
		return outDetailDao.cardQueryMedicalrecord(ic, idCard,codeCertificate);
	}

	@Override
	public List<OutpatientOutprepay> queryOutprepayList(String medicalrecordId,
			String beginDate, String endDate) {
		Map<String, String> paywayMap = innerCodeService.getBusDictionaryMap("payway");
		Map<String, String> empMap = employeeInInterService.queryEmpCodeAndNameMap();
		List<OutpatientOutprepay> list = outDetailDao.queryOutprepayList(medicalrecordId, beginDate, endDate);
		outDetailDao.clear();
		for (OutpatientOutprepay outpatientOutprepay : list) {
			if(outpatientOutprepay.getPrepayCost() != null){//充值金额
				BigDecimal b = new BigDecimal(Double.toString(outpatientOutprepay.getPrepayCost()));
				BigDecimal one = new BigDecimal("1");
				Double coste = b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();//充值金额
				outpatientOutprepay.setPrepayCost(coste);
			}
			if (StringUtils.isNotBlank(outpatientOutprepay.getPrepayType())) {//充值方式
				if (paywayMap.containsKey(outpatientOutprepay.getPrepayType())) {
					outpatientOutprepay.setPrepayType(paywayMap.get(outpatientOutprepay.getPrepayType()));
				}
			}
			if (StringUtils.isNotBlank(outpatientOutprepay.getCreateUser())) {//操作员
				if (empMap.containsKey(outpatientOutprepay.getCreateUser())) {
					outpatientOutprepay.setCreateUser(empMap.get(outpatientOutprepay.getCreateUser()));
				}
			}
		}
		return list;
	}

	@Override
	public List<OutpatientAccountrecord> queryRecordList(
			String medicalrecordId, String beginDate, String endDate) {
		List<OutpatientAccountrecord> list = outDetailDao.queryRecordList(medicalrecordId, beginDate, endDate);
		outDetailDao.clear();
		for (OutpatientAccountrecord outpatientAccountrecord : list) {
			if(outpatientAccountrecord.getMoney() != null){//金额
				BigDecimal b = new BigDecimal(Double.toString(outpatientAccountrecord.getMoney()));
				BigDecimal one = new BigDecimal("1");
				Double money = b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
				outpatientAccountrecord.setMoney(money);
			}
			if(outpatientAccountrecord.getAccountBalance() != null){//余额
				BigDecimal b = new BigDecimal(Double.toString(outpatientAccountrecord.getAccountBalance()));
				BigDecimal one = new BigDecimal("1");
				Double balance = b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
				outpatientAccountrecord.setAccountBalance(balance);
			}
		}
		return list;
	}

	@Override
	public FileUtil exportOutperpay(List<OutpatientOutprepay> list,
			FileUtil fUtil) {
		Map<String, String> paywayMap = innerCodeService.getBusDictionaryMap("payway");
		Map<String, String> empMap = employeeInInterService.queryEmpCodeAndNameMap();
		for (OutpatientOutprepay model : list) {
			String record="";
			if(model.getPrepayCost() != null){//充值金额
				BigDecimal b = new BigDecimal(Double.toString(model.getPrepayCost()));
				BigDecimal one = new BigDecimal("1");
				record = b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue() + ",";//充值金额
			}else {
				record = "" + ",";//充值金额
			}
			if (StringUtils.isNotBlank(model.getPrepayType())) {//充值方式
				if (paywayMap.containsValue(model.getPrepayType())) {
					record += CommonStringUtils.trimToEmpty(model.getPrepayType()) + ",";
				}
			}else {
				record += CommonStringUtils.trimToEmpty("") + ",";
			}
			record += (model.getCreateTime() != null ? DateUtils.formatDateY_M_D_H_M_S(model.getCreateTime()) : "") + ",";//充值时间
			if (StringUtils.isNotBlank(model.getCreateUser())) {//操作员
				if (empMap.containsKey(model.getCreateUser())) {
					record += CommonStringUtils.trimToEmpty(empMap.get(model.getCreateUser()));
				}
			}else {
				record += CommonStringUtils.trimToEmpty("");
			}
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

	@Override
	public FileUtil exportRecord(List<OutpatientAccountrecord> list,
			FileUtil fUtil) {
		Map<String, String> deptMap = deptInInterService.querydeptCodeAndNameMap();
		Map<String, String> empMap = employeeInInterService.queryEmpCodeAndNameMap();
		for (OutpatientAccountrecord model : list) {
			String record="";
			if(model.getMoney() != null){
				BigDecimal b = new BigDecimal(Double.toString(model.getMoney()));
				BigDecimal one = new BigDecimal("1");
				record = b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue() + ",";//金额
			}else {
				record = "" + ",";//金额
			}
			if(model.getAccountBalance() != null){//余额
				BigDecimal b = new BigDecimal(Double.toString(model.getAccountBalance()));
				BigDecimal one = new BigDecimal("1");
				record += b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue() + ",";//余额
			}else {
				record += "" + ",";//余额
			}
			if (StringUtils.isNotBlank(model.getDeptCode())) {//科室
				if (deptMap.containsKey(model.getDeptCode())) {
					record += CommonStringUtils.trimToEmpty(deptMap.get(model.getDeptCode())) + ",";
				}else{
					record += CommonStringUtils.trimToEmpty("") + ",";
				}
			}else {
				record += CommonStringUtils.trimToEmpty("") + ",";
			}
			record += (model.getOperDate() != null ? DateUtils.formatDateY_M_D_H_M_S(model.getOperDate()) : "") + ",";//充值时间
			if (StringUtils.isNotBlank(model.getOperCode())) {//操作员
				if (empMap.containsKey(model.getOperCode())) {
					record += CommonStringUtils.trimToEmpty(empMap.get(model.getOperCode()));
				}else{
					record += CommonStringUtils.trimToEmpty("");
				}
			}else {
				record += CommonStringUtils.trimToEmpty("");
			}
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}

}
