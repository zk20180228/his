package cn.honry.patinent.account.dao;

import java.util.List;

import cn.honry.base.bean.model.PatientAccount;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface AccountDAO extends EntityDao<PatientAccount>{
	
	
	/**
	 * @Description:获取患者账户信息列表带分页
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @version 1.0
	 * @param entity 实体类
	 * @param page 当前第几页
	 * @param rows 当前页显示条数
	 * @return list
	**/
	List<PatientAccount> getPage(PatientAccount entity, String page, String rows);

	/**
	 * @Description:获取患者账户信息列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @version 1.0
	 * @param entity 实体类
	 * @return 
	**/
	int getTotal(PatientAccount entity);

	/**
	 * @Description:根据IDcard ID 取患者账户
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
	 * @Description:根据患者账户id 去患者账户
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
	 * @Description：  根据病历号获得患者账户
	 * @Author：aizhonghua
	 * @CreateDate：2015-11-3 上午10:42:23  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-11-3 上午10:42:23  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	PatientAccount getAccountByMedicalrecord(String medicalrecordId);
}
