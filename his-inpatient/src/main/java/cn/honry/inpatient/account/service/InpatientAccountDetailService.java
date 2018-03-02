package cn.honry.inpatient.account.service;

import cn.honry.base.bean.model.InpatientAccountdetail;
import cn.honry.base.service.BaseService;

public interface InpatientAccountDetailService extends BaseService<InpatientAccountdetail>{
	/**
	 * @Description:通过父id 物理删除子表记录
	 * @Author：  lt
	 * @CreateDate： 2015-11-16
	 * @param @throws Exception   
	 * @return void  
	 * @version 1.0
	**/
	void delByParentId(String id);

}
