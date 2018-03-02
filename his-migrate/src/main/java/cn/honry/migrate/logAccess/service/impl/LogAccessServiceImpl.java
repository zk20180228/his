package cn.honry.migrate.logAccess.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.LogAccess;
import cn.honry.migrate.logAccess.dao.LogAccessDao;
import cn.honry.migrate.logAccess.service.LogAccessService;
@Service("logAccessService")
public class LogAccessServiceImpl implements LogAccessService {
	
	@Autowired
	@Qualifier(value = "logAccessDao")
	private LogAccessDao logAccessDao;
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
	public List<LogAccess> queryLogAccess(String code,String page,String rows,String param) {
		
		return logAccessDao.queryLogAccess(code, page, rows, param);
	}
	@Override
	public int queryTotal(String code,String param) {
		return logAccessDao.queryTotal(code, param);
	}
	
}
