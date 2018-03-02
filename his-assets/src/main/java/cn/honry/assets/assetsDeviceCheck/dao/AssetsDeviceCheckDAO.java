package cn.honry.assets.assetsDeviceCheck.dao;

import java.util.List;

import cn.honry.base.bean.model.AssetsDeviceCheck;
import cn.honry.base.dao.EntityDao;

public interface AssetsDeviceCheckDAO extends EntityDao<AssetsDeviceCheck>{
	/**  
	 * 
	 * 设备盘点管理list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public List<AssetsDeviceCheck> queryAssetsDeviceCheck(AssetsDeviceCheck deviceDossier) throws Exception;
	/**  
	 * 
	 * 设备盘点管理Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午9:26:53 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午9:26:53 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public Integer queryTotal(AssetsDeviceCheck deviceDossier) throws Exception;
	/**  
	 * 
	 * 根据设备代码查询
	 * @Author: huzhenguo
	 * @CreateDate: 2017年12月4日 下午4:18:28 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年12月4日 下午4:18:28 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public AssetsDeviceCheck queryByDeviceCode(String deviceCode);
}
