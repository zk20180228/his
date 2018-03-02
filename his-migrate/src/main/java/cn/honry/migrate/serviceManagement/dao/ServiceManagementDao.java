package cn.honry.migrate.serviceManagement.dao;

import java.util.List;

import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.base.dao.EntityDao;

public interface ServiceManagementDao extends EntityDao<ServiceManagement>{
	/**  
	 * 服务管理列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<ServiceManagement> queryServiceManagement(String code, String page,String rows, String menuAlias,String serviceType,String serviceState);
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
	 * 服务管理 删除
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	void delServiceManagement(String id);
	/**  
	 * 服务管理 得到要修改的数据
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	public ServiceManagement getOnedata(String id);
	
	List<ServiceManagement> queryServiceManagement(String queryCode);
	/**
	 * 
	 * 
	 * <p>一个服务只能存在 主服务和备用服务两种状态   </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月26日 下午4:11:37 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月26日 下午4:11:37 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param serviceName
	 * @param masterprePare
	 * @return:
	 *
	 */
	Boolean queryUnionList(String serviceName,Integer masterprePare ,String id);

	
	
}
