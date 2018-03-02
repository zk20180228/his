package cn.honry.assets.deviceContract.dao;

import java.util.List;

import cn.honry.base.bean.model.AssetsDeviceContract;
import cn.honry.base.dao.EntityDao;

public interface DeviceContractDao extends EntityDao<AssetsDeviceContract>{
	/**  
	 * 设备合同管理列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<AssetsDeviceContract> queryDeviceContract(AssetsDeviceContract contract, String page,String rows, String menuAlias);
	/**  
	 * 设备合同管理列表查询(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	int queryTotal(AssetsDeviceContract contract);
	
}
