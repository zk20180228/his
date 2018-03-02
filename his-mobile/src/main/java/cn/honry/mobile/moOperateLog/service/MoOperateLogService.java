package cn.honry.mobile.moOperateLog.service;

import java.util.Map;

import net.sf.json.JSONArray;
import cn.honry.base.bean.model.MSysOperateLog;

public interface MoOperateLogService {
	
	/** 查询列表信息
	* @param pageUtil 查询列表信息
	* @return List<MSysOperateLog> 
	* @author zxl
	 * @throws Exception 
	 * @date 2017年6月20日
	*/
	JSONArray  getOperateLogByPage(String page, String rows, String queryName) throws Exception;
	
	/** 查询总条数
	* @return Long
	* @author zxl
	 * @throws Exception 
	* @date 2017年6月20日
	*/
	Long getTotalByPage(String operateName) throws Exception;
	
	
	/**
	 * 保存操作信息
	 * @Author：zxl
	 * @CreateDate：2017-03-23
     * @version 1.0
	 * @param MSysOperateLog 操作日志实体
	 * @param e  异常对象
	 * @throws Exception 
	 */
    void saveSysOperateLogToMongo(MSysOperateLog mSysOperateLog) throws Exception;

    /**
	 * 根据id获取信息
	 * @Author：zxl
	 * @CreateDate：2017-03-23
     * @version 1.0
	 * @param MSysOperateLog 操作日志实体
	 * @param e  异常对象
     * @throws Exception 
	 */
	MSysOperateLog getOperateLogById(String id) throws Exception;

	/**  
	 *  
	 * @Description：  更新用户信息
	 * @param map
	 * @Author：zxl
	 * @CreateDate：2015-6-3 下午05:11:17  
	 * @version 1.0
	 * @throws Exception 
	 *
	 */
	void updateUser(Map<String, String> map) throws Exception;
}
