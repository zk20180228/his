package cn.honry.migrate.outInterfaceManager.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.ExterInter;
import cn.honry.base.service.BaseService;

public interface OutInterfaceManagerService  extends BaseService<ExterInter> {

	ExterInter get(Integer id);

	/**
	 * <p>保存方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月22日 下午2:19:18 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月22日 下午2:19:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	void saveOrUpdateExterInter(ExterInter exterInter) throws Exception;
	/**
	 * <p>查找方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月22日 下午2:19:18 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月22日 下午2:19:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:queryCode：接口code或者名字，page，rows分页信息
	 * @throws:
	 * @return: void
	 *
	 */
	List<ExterInter> findAll(String queryCode, String menuAlias, String page, String rows ,String serviceState) throws Exception;

	/**
	 * <p>查询全部数量</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月22日 下午2:19:18 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月22日 下午2:19:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: void
	 *
	 */
	int getTotal(String queryCode, String menuAlias,String serviceState) throws Exception;

	/**
	 * <p>根据id查找方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月22日 下午2:19:18 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月22日 下午2:19:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:id
	 * @throws:
	 * @return: void
	 *
	 */
	ExterInter findById(String id) throws Exception;

	/**
	 * <p>删除方法</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月22日 下午2:19:18 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月22日 下午2:19:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:ids 要删除的id
	 * @throws:
	 * @return: void
	 *
	 */
	void delInter(String ids) throws Exception;

	/**
	 * <p>根据code查找方法，如果查不到返回null而不是new个新的对象</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月22日 下午2:19:18 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月22日 下午2:19:18 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:code
	 * @throws:
	 * @return: void
	 *
	 */
	ExterInter findByCode(String code) throws Exception;
	/**
	 * 
	 * 
	 * <p>停用启用 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月23日 下午5:27:22 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月23日 下午5:27:22 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @param state:
	 * @throws Exception 
	 *
	 */
	public void updateState(String id,String state) throws Exception;
	
	/**
	 * 
	 * 
	 * <p>获取厂商下拉列表 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月27日 上午10:05:30 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月27日 上午10:05:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param fireCode
	 * @return:
	 *
	 */
	public List<ExterInter> getfireCode(String fireCode);
	/**
	 * 
	 * 
	 * <p>获取厂商渲染 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年9月27日 上午10:06:33 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年9月27日 上午10:06:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param fireCode
	 * @return:
	 *
	 */
	public Map<String,String> getfireCodeRender(String fireCode);

}
