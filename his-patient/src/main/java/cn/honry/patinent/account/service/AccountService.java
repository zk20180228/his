package cn.honry.patinent.account.service;

import java.util.List;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.service.BaseService;

/**
 * 用户账户信息
 * @author  lt
 * @date 2015-6-3
 * @version 1.0
 */
public interface AccountService extends BaseService<PatientAccount>{
	void saveOrUpdate(PatientAccount entity);
	/**
	 * @Description:获取患者账户信息列表带分页
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @param page 当前第几页
	 * @param rows 当前页显示条数
	 * @return list
	**/
	List<PatientAccount> getPage(String page, String rows,PatientAccount account);
	/**
	 * @Description:获取患者账户信息列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @return 
	**/
	int getTotal(PatientAccount account);

	void del(String id);
	/**
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param idcardId 就诊卡ID
	 * @return PatientAccount 实体
	**/
	PatientAccount queryByIdcardId(String idcardId);
	/**
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param id 患者账户ID
	 * @return PatientAccount 实体
	**/
	PatientAccount queryById(String id);
	
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
