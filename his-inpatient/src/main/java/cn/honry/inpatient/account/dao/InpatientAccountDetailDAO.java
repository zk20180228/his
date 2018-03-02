package cn.honry.inpatient.account.dao;

import cn.honry.base.bean.model.InpatientAccountdetail;
import cn.honry.base.dao.EntityDao;

public interface InpatientAccountDetailDAO extends EntityDao<InpatientAccountdetail>{
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
