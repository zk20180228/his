package cn.honry.inpatient.outBalance.dao;

import cn.honry.base.bean.model.InpatientBalanceList;
import cn.honry.base.bean.model.InpatientBalanceListNow;
import cn.honry.base.dao.EntityDao;

public interface InpatientBalanceListDAO extends EntityDao<InpatientBalanceList>{

	void updateInpatientFeeInfoNow(InpatientBalanceListNow inpatientBalanceList);

}
