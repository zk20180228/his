package cn.honry.inner.inpatient.execUndrug.service;


import java.util.List;

import cn.honry.base.bean.model.InpatientExecundrug;
import cn.honry.base.service.BaseService;

public interface UnDrugOrderExecInInterService extends BaseService<InpatientExecundrug> {

	List<InpatientExecundrug> getListNoExec(String patientIds,String pid,String did,String btime,String etime);
}
