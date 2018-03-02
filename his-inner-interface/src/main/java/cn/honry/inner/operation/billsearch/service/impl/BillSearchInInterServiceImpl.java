package cn.honry.inner.operation.billsearch.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.operation.billsearch.dao.BillSearchInInterDAO;
import cn.honry.inner.operation.billsearch.service.BillSearchInInterService;
import cn.honry.inner.operation.billsearch.vo.OperationBillingInfoVo;

/**   
* 摆药单分类service实现类
* @className：OperationBillingInfoService
* @description：  手术计费信息Service实现层
* @author：tangfeishuai
* @createDate：2016-6-12 上午10:52:19  
* @modifier：tangfeishuai
* @modifyRmk：  
* @version 1.0
 */
@Service("billSearchInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class BillSearchInInterServiceImpl implements BillSearchInInterService{
	/**      
	 *摆药单dao
	 */
	@Autowired
	@Qualifier(value = "billSearchInInterDAO")
	private BillSearchInInterDAO billSearchInInterDAO;

	@Override
	public OperationBillingInfoVo get(String id) {
		return billSearchInInterDAO.get(id);
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationBillingInfoVo arg0) {
		
	}
	

}
