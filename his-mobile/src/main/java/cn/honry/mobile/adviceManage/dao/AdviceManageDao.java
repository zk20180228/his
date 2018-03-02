package cn.honry.mobile.adviceManage.dao;

import java.util.List;

import cn.honry.base.bean.model.MAdviceManage;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;

public interface AdviceManageDao extends EntityDao<MAdviceManage>{


	/**  
	 * 
	 * 查询列表数据
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:48:39 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月21日 上午10:48:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:queryName 用户账户、姓名
	 * @param:rows 行数
	 * @param:page 页数
	 * @throws:
	 * @return: List<MAdviceManage>
	 *
	 */
	List<MAdviceManage> getMAdviceManageList(String rows, String page,String queryName) throws Exception;


	/**  
	 * 
	 * 查询列表数据总条数
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:48:39 
	 * @Modifier:  zxl
	 * @ModifyDate: 2017年9月21日 上午10:48:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:queryName 用户账户、姓名
	 * @throws:
	 * @return: 	Integer
	 *
	 */
	Integer getMAdviceManageCount(String queryName) throws Exception;

	
	/**  
	 * 
	 * 根据id删除意见箱管理员数据
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:48:39 
	 * @Modifier:  zxl
	 * @ModifyDate: 2017年9月21日 上午10:48:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:ids
	 * @throws:
	 * @return: 	
	 *
	 */
	void delAdviceManage(String ids)  throws Exception;
	
	/**  
	 * 
	 * 查询用户列表数据
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:48:39 
	 * @Modifier: zxl
	 * @ModifyDate: 2017年9月21日 上午10:48:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:queryUser 用户账户、姓名
	 * @param:rows 行数
	 * @param:page 页数
	 * @throws:
	 * @return: List<MAdviceManage>
	 *
	 */
	List<User> getUserManageList(String rows, String page, String queryUser) throws Exception;

	/**  
	 * 
	 * 查询用户列表数据总条数
	 * @Author: zxl
	 * @CreateDate: 2017年9月21日 上午10:48:39 
	 * @Modifier:  zxl
	 * @ModifyDate: 2017年9月21日 上午10:48:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:queryUser 用户账户、姓名
	 * @throws:
	 * @return: 	Integer
	 *
	 */
	Integer getUserManageCount(String queryUser) throws Exception;
}
