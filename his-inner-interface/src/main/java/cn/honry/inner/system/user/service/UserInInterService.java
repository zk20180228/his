package cn.honry.inner.system.user.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.service.BaseService;

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
public interface UserInInterService  extends BaseService<User>{
	
	/**  
	 *  
	 * @Description：  获得userMap
	 * @Author：aizhonghua
	 * @CreateDate：2015-10-27 上午11:24:46  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-10-27 上午11:24:46  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public Map<String, String> getUserMap();
	
	/**  
	 *  
	 * @Description：   用户 添加&修改
	 * @Author：aizhonghua
	 * @CreateDate：2015-6-18 下午04:52:23  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-6-18 下午04:52:23  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	void saveOrUpdateeditInfo(User entity);

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
	 * @Description 根据用户编码code 去查用户
	 * @author  lyy
	 * @createDate： 2016年7月22日 上午10:57:32 
	 * @modifier lyy
	 * @modifyDate：2016年7月22日 上午10:57:32
	 * @param：  code 用户编码
	 * @modifyRmk：  
	 * @version 1.0
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
	public List<SysEmployee> getuserMap();
	
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

}
