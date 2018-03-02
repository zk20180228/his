package cn.honry.oa.userPortal.dao;

import java.util.List;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaUserPortal;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.SysInfo;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;
/**
 * OA系统首页个人首页维护
 * @author  zpty
 * @date 2017-7-18 15：40
 * @version 1.0
 */
@SuppressWarnings({"all"})
public interface UserPortalDAO extends EntityDao<OaUserPortal>{

	/**  
	 * 
	 * 通过ID查询实体
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:45:23 
	 * @version: V1.0
	 * @param id
	 * @return:
	 * @throws:
	 * @return: OaUserPortal 返回值类型
	 *
	 */
	OaUserPortal  getById(String id);

	/**  
	 * 
	 * 查询所有首页组件的数据(去掉停用的)
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 上午11:04:05 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: List<OaUserPortal> 返回值类型
	 *
	 */
	List<OaPortalWidget> queryPortalWidget();
	/**  
	 * 
	 * 查询所有个人首页组件的数据
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午1:57:39 
	 * @version: V1.0
	 * @param longinUserAccount 登录人的account
	 * @return:
	 * @throws:
	 * @return: List<OaUserPortal> 返回值类型
	 *
	 */
	List<OaUserPortal> queryUserPortal(String longinUserAccount);
	/**  
	 * 
	 * 查询所有个人首页组件的数据(包括停用的)
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 下午1:57:39 
	 * @version: V1.0
	 * @param longinUserAccount 登录人的account
	 * @return:
	 * @throws:
	 * @return: List<OaUserPortal> 返回值类型
	 *
	 */
	List<OaUserPortal> queryUserPortalAll(String longinUserAccount);
	
	/***
	 * 显示通知公告集合
	 * @Title: showList 
	 * @author  zpty
	 * @createDate ：2017年7月18日
	 * @return List<SysInfo> ： 通知公告集合
	 * @version 1.0
	 */
	List<SysInfo> getList(SysInfo info);

	/**  
	 * 
	 * 移动首页组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月19日 上午11:02:09 
	 * @version: V1.0
	 * @param userPortal:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void updateWidget(OaUserPortal userPortal);

	/**  
	 * 
	 * 查询所有首页组件的数据(包括停用的)
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 上午11:04:05 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: List<OaUserPortal> 返回值类型
	 *
	 */
	List<OaPortalWidget> queryPortalWidgetAll();
	/**  
	 * 
	 * 通过组件ID查询出相应组件内容
	 * @Author: zpty
	 * @CreateDate: 2017年7月18日 上午11:04:05 
	 * @version: V1.0
	 * @param id 组件ID
	 * @return:
	 * @throws:
	 * @return: List<OaUserPortal> 返回值类型
	 *
	 */
	OaPortalWidget queryPortalWidgetById(String id);

	/**  
	 * 
	 * 查询出所有的用户Account
	 * @Author: zpty
	 * @CreateDate: 2017年7月21日 下午7:03:52 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: List<User> 返回值类型
	 *
	 */
	List<User> queryAllUser();

	/**  
	 * 
	 * 查询出当前首页组件下的所有的用户
	 * @Author: zpty
	 * @CreateDate: 2017年7月21日 下午7:10:51 
	 * @version: V1.0
	 * @param string
	 * @return:
	 * @throws:
	 * @return: List<OaUserPortal> 返回值类型
	 *
	 */
	List<OaUserPortal> queryPortalUser(String widgetId);

	/**  
	 * 
	 * 启用组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月24日 上午11:57:49 
	 * @version: V1.0
	 * @param oaUserPortal:个人组件实体
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void enableUserWidget(OaUserPortal oaUserPortal);
	/**  
	 * 
	 * 查询信息管理数据
	 * @Author: zpty
	 * @CreateDate: 2017年7月26日 下午7:05:36 
	 * @version: V1.0
	 * @param type 信息类型
	 * @param longinUserAccount 当前登录人员
	 * @return:
	 * @throws:
	 * @return: List<Information> 返回值类型
	 *
	 */
	List<Information> getInformationList(String type, String longinUserAccount);

	/**  
	 * 
	 * 查询所有的需要审批的信息管理的数据
	 * @Author: zpty
	 * @CreateDate: 2017年7月28日 下午3:13:01 
	 * @version: V1.0
	 * @param employee 当前登录人
	 * @param user 当前登录人
	 * @return:
	 * @throws:
	 * @return: List<Information> 返回值类型
	 *
	 */
	List<Information> getInformationCheck(SysEmployee employee,User user);

	/**  
	 * 
	 * 根据科室的ID来查询科室
	 * @Author: zpty
	 * @CreateDate: 2017年7月28日 下午4:12:37 
	 * @version: V1.0
	 * @param deptId 科室ID
	 * @return:
	 * @throws:
	 * @return: SysDepartment 返回值类型
	 *
	 */
	SysDepartment getDeptForId(String deptId);
	/**  
	 * 
	 * 查询当前登录人是否已经有此组件了
	 * @Author: zpty
	 * @CreateDate: 2017年8月4日 下午7:35:26 
	 * @version: V1.0
	 * @param longinUserAccount 当前登录人
	 * @param moudelId: 组件ID
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	String queryWidgetForUser(String longinUserAccount, String moudelId);
	
	/**  
	 * 
	 * 查询日程安排
	 * @Author: zpty
	 * @CreateDate: 2017年8月4日 下午7:35:26 
	 * @version: V1.0
	 * @param userAccount 当前登录人
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	List<Schedule> qeryScheduleList(String userAccount);
	/**  
	 * 
	 * 查询apkVersion
	 * @Author: zpty
	 * @CreateDate: 2017年8月11日 下午7:35:26 
	 * @version: V1.0
	 * @throws:
	 * @return: MApkVersion 返回值类型
	 *
	 */
	MApkVersion getVersion();
	/**
	 * 查询待办数据
	 * @Author: zpty
	 * @CreateDate: 2017年8月12日 下午7:35:26 
	 * @param account 当前登录人
	 * @param tenantId 租用人
	 * @version: V1.0
	 * @throws:
	 * @return: List<OaTaskInfo> 返回值类型
	 */
	List<OaTaskInfo> getListForTask(String account, String tenantId);
}
