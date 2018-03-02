package cn.honry.inpatient.outBalance.service;

import cn.honry.base.bean.model.InpatientBalancePay;
import cn.honry.base.service.BaseService;

public interface InpatientBalancePayListService extends BaseService<InpatientBalancePay>{
	/**
	 * 
	 * @Description：保存住院实付信息
	 * @author  dh
	 * @createDate： 2016年4月1日 下午4:11:46 
	 * @modifier dh
	 * @modifyDate：2016年4月1日 下午4:11:46
	 * @param：  
	 * @return：
	 * @modifyRmk：  
	 * @version 1.0
	 */
	void saveInpatientBalancePay(InpatientBalancePay inpatientBalancePay);
}
