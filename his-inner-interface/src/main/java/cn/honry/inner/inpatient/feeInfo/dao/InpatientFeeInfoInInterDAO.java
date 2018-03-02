package cn.honry.inner.inpatient.feeInfo.dao;

import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.base.dao.EntityDao;

public interface InpatientFeeInfoInInterDAO extends EntityDao<InpatientFeeInfoNow> {
	/**
	 * 修改费用汇总表
	 */
	void updateInpatientFeeInfoNow(InpatientFeeInfoNow fee);

}
