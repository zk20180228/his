package cn.honry.oa.userPortal.service;

import java.util.List;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.MApkVersion;
import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.base.bean.model.OaTaskInfo;
import cn.honry.base.bean.model.OaUserPortal;
import cn.honry.base.bean.model.Schedule;
import cn.honry.base.bean.model.SysInfo;
import cn.honry.base.service.BaseService;

/**
 * OA系统首页个人首页维护
 * @author  zpty
 * @date 2017-7-18 15：40
 * @version 1.0
 */
public interface UserPortalService extends BaseService<OaUserPortal>{
	/**  
	 * 
	 * 移除首页组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月17日 上午11:04:24 
	 * @version: V1.0
	 * @param id 待删除组件主键
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void del(String id);
	/**  
	 * 
	 * 查询所有首页组件的数据
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
	 * @CreateDate: 2017年7月19日 上午9:23:50 
	 * @version: V1.0:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void moveWidget(String dataJson);
	
	/**  
	 * 
	 * 初始化首页组件
	 * @Author: zpty
	 * @CreateDate: 2017年7月21日 下午6:40:20 
	 * @Modifier: zpty
	 * @ModifyDate: 2017年7月21日 下午6:40:20 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param butName:
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void initialization(String[] butName);
	/**  
	 * 
	 * 启用组件方法
	 * @Author: zpty
	 * @CreateDate: 2017年7月24日 上午11:54:04 
	 * @version: V1.0
	 * @param userPortal: 个人组件实体
	 * @throws:
	 * @return: void 返回值类型
	 *
	 */
	void enableUserWidget(OaUserPortal userPortal);
	/**  
	 * 
	 * 查询信息管理数据
	 * @Author: zpty
	 * @CreateDate: 2017年7月26日 下午7:05:36 
	 * @version: V1.0
	 * @param type 信息类型
	 * @return:
	 * @throws:
	 * @return: List<Information> 返回值类型
	 *
	 */
	List<Information> getInformationList(String type);
	/**  
	 * 
	 * 查询所有的需要审批的信息管理的数据
	 * @Author: zpty
	 * @CreateDate: 2017年7月28日 下午3:11:01 
	 * @version: V1.0
	 * @return:
	 * @throws:
	 * @return: List<Information> 返回值类型
	 *
	 */
	List<Information> getInformationCheck();
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
