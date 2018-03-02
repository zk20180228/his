package cn.honry.inner.inpatient.feeInfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientFeeInfoNow;
import cn.honry.inner.inpatient.feeInfo.dao.InpatientFeeInfoInInterDAO;
import cn.honry.inner.inpatient.feeInfo.service.InpatientFeeInfoInInterService;

@Service("inpatientFeeInfoInInterService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientFeeInfoInInterServiceImpl implements InpatientFeeInfoInInterService{
	
	@Autowired
	private InpatientFeeInfoInInterDAO inpatientFeeInfoInInterDAO;

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
	@Override
	public void save(InpatientFeeInfoNow infee) {
		inpatientFeeInfoInInterDAO.save(infee);
		
	}

}
