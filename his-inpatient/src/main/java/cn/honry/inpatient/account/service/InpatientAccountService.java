package cn.honry.inpatient.account.service;

import cn.honry.base.bean.model.InpatientAccount;
import cn.honry.base.service.BaseService;

public interface InpatientAccountService extends BaseService<InpatientAccount>{

	/**
	 * @Description:根据病历号获取住院账户信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return   
	 * @return InpatientAccount  
	 * @version 1.0
	**/
	InpatientAccount queryByMedical(String string);
	/**
	 * @Description:物理删除账号信息
	 * @Author：  lt
	 * @CreateDate： 2015-7-1
	 * @param @param string
	 * @param @return   
	 * @version 1.0
	**/
	void del(String id);
	/**
	 * @Description:执行挂失操作
	 * @Author：  lt
	 * @CreateDate： 2015-11-18
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	InpatientAccount queryByIdcardId(String idcardId)throws Exception;

}
