package cn.honry.patinent.account.service;

import java.util.List;

import cn.honry.base.bean.model.PatientAccountdetail;
import cn.honry.base.service.BaseService;

/**
 * 用户账户信息
 * @author  lt
 * @date 2015-6-3
 * @version 1.0
 */
public interface AccountDetailService extends BaseService<PatientAccountdetail>{
	
	/**  
	 *  
	 * @Description：保存或修改
	 * @Author：lt
	 * @CreateDate：2015-6-18  
	 * @version 1.0
	 *
	 */
	void saveOrUpdate(PatientAccountdetail entity);

	/**
	 * @Description:获取患者账户操作记录表信息列表
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @return 
	**/
	List<PatientAccountdetail> getPage(PatientAccountdetail entity, String page, String rows,String accountId);
	/**
	 * @Description:获取患者账户操作记录表信息列表总条数
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param entity 实体类
	 * @return 
	**/
	int getTotal(PatientAccountdetail entity,String accountId);

	/**
	 * @Description:按id 逻辑删除
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void del(String id);
	/**
	 * @Description:按ParentId 物理删除
	 * @Author：  lt
	 * @CreateDate： 2015-6-18
	 * @Modifier：lt
	 * @ModifyDate：2015-6-18
	 * @ModifyRmk：  
	 * @version 1.0
	**/
	void delByParentId(String id);
}
