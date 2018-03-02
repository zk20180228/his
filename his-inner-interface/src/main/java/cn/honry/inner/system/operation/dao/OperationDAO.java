package cn.honry.inner.system.operation.dao;

import java.util.List;

import cn.honry.base.bean.model.SysUseroperation;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface OperationDAO extends EntityDao<SysUseroperation>{

	/**  
	 *  
	 * @Description：  分页查询-获得总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-17 上午11:33:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-17 上午11:33:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getTotal(SysUseroperation operation);

	/**  
	 *  
	 * @Description：  分页查询-获得列表信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-8-17 上午11:33:44  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-8-17 上午11:33:44  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<SysUseroperation> getPage(String page, String rows,SysUseroperation operation);
}
