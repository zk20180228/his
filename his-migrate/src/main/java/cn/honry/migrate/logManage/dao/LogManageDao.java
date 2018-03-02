package cn.honry.migrate.logManage.dao;

import java.util.List;

import cn.honry.base.bean.model.LogManage;
import cn.honry.base.dao.EntityDao;

	/**  
	 * 查询日志DAO
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
public interface LogManageDao extends EntityDao<LogManage> {
	/**  
	 * 查询日志
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<LogManage> queryLogManage(String code, String page, String rows,String param);
	
	/**  
	 * 查询日志 总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	int queryTotal(String code,String param);

}
