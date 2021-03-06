package cn.honry.inner.patient.account.service;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.service.BaseService;
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
public interface AccountInInterService extends BaseService<PatientAccount>{
	
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
	PatientAccount getAccountByMedicalrecord(String caseNo);

}
