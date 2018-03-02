package cn.honry.inner.inpatient.shiftApply.service;


import cn.honry.base.bean.model.InpatientShiftApply;
import cn.honry.base.service.BaseService;

public interface ShiftApplyInInterService extends BaseService<InpatientShiftApply> {
	/**
	 * 根据患者住院流水号，患者现住院科室获取转科申请
	 */
	InpatientShiftApply getApply(String inpatientno, String oldDeptCode);
	/**
	 * 生成转科申请
	 */
	void save(InpatientShiftApply inpatientShiftApply);
	/**
	 * 获取转科申请发生次数
	 */
	Integer getHappenNo(String inpatientNo);

}
