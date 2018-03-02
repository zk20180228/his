package cn.honry.inner.inpatient.execUndrug.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.inner.inpatient.execUndrug.dao.UnDrugOrderExecInInterDao;
import cn.honry.inner.inpatient.execUndrug.service.UnDrugOrderExecInInterService;
import cn.honry.inner.system.utli.OperationUtils;


@Service("unDrugOrderExecInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class UnDrugOrderExecInInterServiceImpl implements UnDrugOrderExecInInterService {

	@Autowired
	@Qualifier(value = "unDrugOrderExecInInterDao")
	private UnDrugOrderExecInInterDao unDrugOrderExecInInterDao;
	
	public void removeUnused(String id) {

	}

	public InpatientExecundrug get(String id) {
		InpatientExecundrug execdrug=unDrugOrderExecInInterDao.get(id);
		return execdrug;
	}

	public void saveOrUpdate(InpatientExecundrug entity) {

		entity.setId(null);
		entity.setCreateUser("");
		entity.setCreateDept("");
		entity.setCreateTime(new Date());
		unDrugOrderExecInInterDao.save(entity);
		OperationUtils.getInstance().conserve(null,"非药嘱执行档","INSERT INTO","T_INPATIENT_EXECUNDRUG",OperationUtils.LOGACTIONINSERT);
	}

	@Override
	public List<InpatientExecundrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime) {
		return unDrugOrderExecInInterDao.getListNoExec( patientIds, pid, did, btime, etime);
	}

}
