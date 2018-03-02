package cn.honry.migrate.serviceManagement.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.LogServiceVo;
import cn.honry.base.bean.model.ServiceManagement;


public interface ServiceManagementService{
	/**  
	 * 服务管理列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<ServiceManagement> queryServiceManagement(String code,String page, String rows,String menuAlias,String serviceType,String serviceState);
	/**  
	 * 服务管理列表查询(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	int queryTotal(String code);
	
	/**  
	 * 服务管理  添加/修改
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return 
	 * @throws Exception 
	 */
	Map<String, String> saveServiceManagement(ServiceManagement serviceManagement) throws Exception;
	/**  
	 * 服务管理  删除
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code 
	 */
	void delServiceManagement(String id,String state);
	/**  
	 * 服务管理  得到要修改的数据
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param code 
	 */
	ServiceManagement getOnedata(String code);
	/**
	 * 
	 * 
	 * <p>查询主备服务管理 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月26日 下午8:55:15 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月26日 下午8:55:15 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return:
	 *
	 */
	List<ServiceManagement> queryServiceManagement(String queryCode);
	
	/**
	 * 
	 * 
	 * <p> </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月25日 上午10:30:17 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月25日 上午10:30:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id:
	 * @throws Exception 
	 *
	 */
	void delService(String id) throws Exception;
	/**
	 * 
	 * 
	 * <p>发送请求 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月26日 上午10:21:12 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月26日 上午10:21:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param ip
	 * @param port
	 * @return:
	 *
	 */
	String sendRequest(String ip,String port,String serviceName) throws Exception;
	
	/** 
	 * 
	 * <p>服务管理日志 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月28日 上午10:03:09 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月28日 上午10:03:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param serviceCode 服务编号
	 * @param newTime 最新心跳时间
	 * @param page
	 * @param rows
	 * @param menuAlias
	 * @return:
	 *
	 */
	List<LogServiceVo> queryLogService(String serviceCode,String STime,String Etime,String page,String rows, String menuAlias);
	/**
	 * 
	 * 
	 * <p>服务管理日志总条数 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月28日 上午10:04:17 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月28日 上午10:04:17 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param serviceCode
	 * @param newTime
	 * @return:
	 *
	 */
	int totalService(String serviceCode,String STime,String Etime);
	
	
}
