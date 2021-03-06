package cn.honry.mobile.openFireInit.dao;

import java.util.List;

import cn.honry.base.bean.model.Ofuser;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;

public interface OpenFireInitDao extends EntityDao<Ofuser>{

	void delOfUserAccount(String account1, String account2) throws Exception;
	/**  
	 * 
	 * 查询所有用户
	 * @Author:zxl
	 * @CreateDate: 2017年9月18日 下午12:00:31 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月18日 下午12:00:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: List<User> 
	 *
	 */
	List<User> findUsers();


}
