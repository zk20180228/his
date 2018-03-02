package cn.honry.migrate.logManage.service;

import java.util.List;

import cn.honry.base.bean.model.LogManage;
public interface LogManageService {
	/**  
	 * 日志列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<LogManage> queryLogManage(String code, String page, String rows,String param);

	int queryTotal(String code,String param);
}
