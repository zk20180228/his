package cn.honry.inner.inpatient.shiftApply.dao;


import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.base.dao.EntityDao;

public interface ShiftApplyInInterDao extends EntityDao<InpatientShiftApply> {
	/**
	 * 根据患者住院流水号，患者现住院科室获取转科申请
	 */	
	InpatientShiftApply getApply(String inpatientno, String oldDeptCode);
	/**
	 * 获取患者转科申请次数
	 */	
	Integer getHappenNo(String inpatientNo);
}
