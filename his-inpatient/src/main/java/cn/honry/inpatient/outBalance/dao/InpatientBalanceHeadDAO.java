package cn.honry.inpatient.outBalance.dao;

import cn.honry.base.bean.model.InpatientBalanceHead;
import cn.honry.base.bean.model.InpatientBalanceHeadNow;
import cn.honry.base.dao.EntityDao;

public interface InpatientBalanceHeadDAO  extends EntityDao<InpatientBalanceHead>{

	void updateHead(InpatientBalanceHeadNow head);

}
