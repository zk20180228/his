package cn.honry.inner.system.user.dao;

import java.util.List;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * 内部接口：用户
 * @Author：aizhonghua
 * @CreateDate：2016-7-05 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-7-05 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface UserInInterDAO extends EntityDao<User>{

	List<User> findUserByDept(String id);
	
	/**  
	 *  
	 * @Description：   用户 分页查询 - 获得列表
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:52:23  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:52:23  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<User> getPage(String page, String rows,User user);
	
	/**  
	 *  
	 * @Description：   用户 分页查询 - 统计总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:52:23  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:52:23  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(User user);
	
	/**  
	 *  
	 * @Description：穿入写好的hql分页获得数据列表
	 * @Author：kjh
	 * @CreateDate：2015-11-4 上午11:24:46  
	 * @version 1.0
	 *
	 */
	List<User> getPage(String page, String rows,String hql);
	
	/**  
	 *  
	 * @Description： 穿入写好的hql分页获得数据的总数
	 * @Author：kjh
	 * @CreateDate：2015-11-4上午11:24:46  
	 * @version 1.0
	 *
	 */
	int getTotalList(String hql);
	
	/**
	 * 根据编码查询用户信息
	 * @param code 系统编码
	 * @return
	 */
	User getByCode(String code);
	/**
	 * @Description:获取所有员工信息
	 * @Author: zhangjin
	 * @CreateDate: 2016年11月8日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	List<SysEmployee> getuserMap();

	/**
	 * 
	 * @Description 根据用户account 去查用户
	 * @author  zpty
	 * @createDate： 2017年3月28日 上午10:57:32 
	 * @param：  account 用户编码
	 * @modifyRmk：  
	 * @version 1.0
	 */
	User getByAccount(String account);
	
	/**
	 * 
	 * @Description 根据用户account 去查关联科室
	 * @author  zpty
	 * @createDate： 2017年3月28日 上午10:57:32 
	 * @param：  account 用户编码
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<SysDepartment> getRelatedDeptAccount(String account);

	/**  
	 * 
	 * <p>获得未被删除，停用的用户集合</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年9月14日 上午9:58:49 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年9月14日 上午9:58:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<User> getAllUser();

}
