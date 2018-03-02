package cn.honry.inner.inpatient.nurseApply.service;

import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.base.service.BaseService;
	
public interface NurseApplyInInterService extends BaseService<InpatientShiftApply> {
	 //患者树
    String treeInpatient(String id,String deptId);
}
