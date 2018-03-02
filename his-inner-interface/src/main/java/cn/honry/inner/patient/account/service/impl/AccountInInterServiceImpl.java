package cn.honry.inner.patient.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.inner.patient.account.dao.AccountInInterDAO;
import cn.honry.inner.patient.account.service.AccountInInterService;

/**  
 *  
 * 内部接口：账户
 * @Author：tangfeishuai
 * @CreateDate：2016-7-07 上午11:56:31  
 * @Modifier：tangfeishuai
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("accountInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class AccountInInterServiceImpl implements AccountInInterService{

	/** 账户数据库操作类 **/
	@Autowired
	@Qualifier(value = "accountInInterDAO")
	private AccountInInterDAO accountInInterDAO;
	
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public PatientAccount get(String id) {
		return accountInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(PatientAccount entity) {
		
	}
	
	/**  
	 *  
	 * @Description：   根据病历号获得患者账户
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-5 下午02:37:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-5 下午02:37:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public PatientAccount getAccountByMedicalrecord(String caseNo) {
		return accountInInterDAO.getAccountByMedicalrecord(caseNo);
	}

}
