package cn.honry.inpatient.outBalance.dao;

import cn.honry.base.bean.model.InpatientShiftData;
import cn.honry.base.dao.EntityDao;

public interface InpatientShiftDAO extends EntityDao<InpatientShiftData>{

	void updateInpatientShiftData(InpatientShiftData inpatientShiftData);

}
