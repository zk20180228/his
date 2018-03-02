package cn.honry.inner.inpatient.inpatientOrder.service;

import java.util.Date;

import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.service.BaseService;

public interface InpatientOrderInInterService extends BaseService<InpatientOrder> {
	/**
	 *  停止小时计费的医嘱
	 *  @author  liujl 2016-4-27
	 *  @createDate： 2016年4月21日 上午9:33:38 
	 *  @param： 医嘱对象
	 *  @param： 医嘱停止时间
	 *  @version 1.0
	 */
	public void StopHourFrequenceOrder(InpatientOrder order,Date stopTime) throws Exception;

}
