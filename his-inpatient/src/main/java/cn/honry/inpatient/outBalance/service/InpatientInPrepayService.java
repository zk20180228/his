package cn.honry.inpatient.outBalance.service;

import java.util.List;

import cn.honry.base.bean.model.InpatientInPrepay;
import cn.honry.base.service.BaseService;

public interface InpatientInPrepayService extends BaseService<InpatientInPrepay> {
	/**
	 * 根据住院流水号和时间查询预交金总额
	 */
	List<InpatientInPrepay> queryprepayCost(String inpatientNo,String outDate,String inDate);
}
