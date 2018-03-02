package cn.honry.assets.assetsDeviceCheck.service;

import java.util.List;

import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.AssetsDeviceCheck;
import cn.honry.base.service.BaseService;

public interface AssetsDeviceCheckService extends BaseService<AssetsDeviceCheck>{
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
	public List<AssetsDeviceCheck> queryAssetsDeviceCheck(AssetsDeviceCheck assetsDeviceCheck) throws Exception;
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
	public Integer queryTotal(AssetsDeviceCheck assetsDeviceCheck) throws Exception;
	/**  
	 * 
	 * 校正
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月18日 上午11:07:31 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月18日 上午11:07:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public void Correcting(AssetsDeviceCheck assetsDeviceCheck) throws Exception;
	
	/**
	 *  从入库审批的时候插入新数据到盘点管理
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceCode
	 * @return
	 */
	void newDeviceCode(AssetsDevice device);
}
