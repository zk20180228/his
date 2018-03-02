package cn.honry.inner.inpatient.feeInfo.service;

import cn.honry.base.bean.model.InpatientFeeInfoNow;

/**
 * ClassName: InpatientFeeInfoInInterService 
 * @Description: 住院费用业务逻辑接口
 * @author zxl
 * @date 2015-6-24
 */
public interface InpatientFeeInfoInInterService {

	/**  
	 * 
	 * 住院费用汇总实体
	 * @Author: zxl
	 * @CreateDate: 2017年7月4日 下午6:02:55 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年7月4日 下午6:02:55 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	void save(InpatientFeeInfoNow infee);

}
