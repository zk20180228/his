package cn.honry.inner.inpatient.execdrug.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientExecbill;
import cn.honry.base.bean.model.InpatientExecdrug;
import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.inner.inpatient.execdrug.dao.DrugExecOrderInInterDao;
import cn.honry.inner.inpatient.execdrug.service.DrugExecOrderInInterService;


@Service("drugExecOrderInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class DrugExecOrderInInterServiceImpl implements DrugExecOrderInInterService {

	@Autowired
	@Qualifier(value = "drugExecOrderInInterDao")
	private DrugExecOrderInInterDao drugExecOrderInInterDao;
	
	public void removeUnused(String id) {

	}

	public InpatientExecdrug get(String id) {
		InpatientExecdrug execdrug=drugExecOrderInInterDao.get(id);
		return execdrug;
	}

	public void saveOrUpdate(InpatientExecdrug entity) {

		entity.setId(null);
		entity.setCreateUser("");
		entity.setCreateDept("");
		entity.setCreateTime(new Date());
		drugExecOrderInInterDao.save(entity);
	}

	@Override
	public List<InpatientExecdrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime) {
		return drugExecOrderInInterDao.getListNoExec( patientIds, pid, did, btime, etime);
	}

	@Override
	public List<InpatientExecdrug> getListExecdrug(String userCode,
			String typtCode, String drugType) {
		return drugExecOrderInInterDao.getListExecdrug(userCode, typtCode, drugType);
	}

	@Override
	public List<InpatientExecundrug> getListExecundrug(String userCode,
			String typtCode, String drugType) {
		return drugExecOrderInInterDao.getListExecundrug(userCode, typtCode, drugType);
	}
	@Override
	public cn.honry.base.bean.model.SysDepartment SysDepartment(String id) {
		return drugExecOrderInInterDao.SysDepartment(id);
	}

	@Override
	public List<InpatientExecdrug> queryExecdrugpage(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,
			String page, String rows,String inpatientNo) {
		return drugExecOrderInInterDao.queryExecdrugpage(billNo,validFlag,drugedFlag,beginDate,endDate, page, rows,inpatientNo);
	}

	@Override
	public int queryExecdrugToatl(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo) {
		return drugExecOrderInInterDao.queryExecdrugToatl(billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
	}

	@Override
	public List<InpatientExecundrug> queryExecundrugpage(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,
			String page, String rows,String inpatientNo) {
		return drugExecOrderInInterDao.queryExecundrugpage(billNo,validFlag,drugedFlag,beginDate,endDate, page, rows,inpatientNo);
	}

	@Override
	public int queryExecundrugToatl(String billNo,String validFlag,String drugedFlag,String beginDate,String endDate,String inpatientNo) {
		return drugExecOrderInInterDao.queryExecundrugToatl(billNo,validFlag,drugedFlag,beginDate,endDate,inpatientNo);
	}
	@Override
	public InpatientExecbill queryBillName(String billNo) {
		return drugExecOrderInInterDao.queryBillName(billNo);
	}
	
}
