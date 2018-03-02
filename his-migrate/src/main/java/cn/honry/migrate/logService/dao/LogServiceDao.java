package cn.honry.migrate.logService.dao;

import java.util.List;

import cn.honry.base.bean.model.LogServiceVo;
import cn.honry.base.dao.EntityDao;

public interface LogServiceDao extends EntityDao<LogServiceVo> {
	/**
	 * 
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
