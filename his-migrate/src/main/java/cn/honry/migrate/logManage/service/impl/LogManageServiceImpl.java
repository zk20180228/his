package cn.honry.migrate.logManage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.LogManage;
import cn.honry.migrate.logManage.dao.LogManageDao;
import cn.honry.migrate.logManage.service.LogManageService;
@Service("logManageService")
public class LogManageServiceImpl implements LogManageService {
	
	@Autowired
	@Qualifier(value = "logManageDao")
	private LogManageDao logManageDao;
	/**  
	 * 查询日志
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<LogManage> queryLogManage(String code, String page, String rows,String param) {
		return logManageDao.queryLogManage(code, page, rows,param);
	}
	/**  
	 * 查询日志 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(String code,String param) {
		return logManageDao.queryTotal(code,param);
	}
}
