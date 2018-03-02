package cn.honry.migrate.logAccess.service;

import java.util.List;

import cn.honry.base.bean.model.LogAccess;
/**
 * 
 * 
 * <p>同步数据service </p>
 * @Author: XCL
 * @CreateDate: 2017年9月20日 下午2:13:57 
 * @Modifier: XCL
 * @ModifyDate: 2017年9月20日 下午2:13:57 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface LogAccessService {
	/**  
	 * 日志列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<LogAccess> queryLogAccess(String code,String page,String rows,String param);
	/**  
	 * 查询日志总记录数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	int queryTotal(String code,String param);
}
