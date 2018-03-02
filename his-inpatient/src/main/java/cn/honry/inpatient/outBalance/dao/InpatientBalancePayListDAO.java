package cn.honry.inpatient.outBalance.dao;


import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.bean.model.InpatientBalancePayNow;
import cn.honry.base.dao.EntityDao;

public interface InpatientBalancePayListDAO extends EntityDao<InpatientBalancePay>{

	/**
	 * @Description： 生成相应支付方式的结算记录
	 * @author  wsj
	 * @createDate： 2017年1月20日 下午14:28:53 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int updateInpatientBalancePayNow(InpatientBalancePayNow entity);

}
