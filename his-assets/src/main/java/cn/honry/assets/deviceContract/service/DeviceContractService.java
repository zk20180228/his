package cn.honry.assets.deviceContract.service;

import java.util.List;

import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.bean.model.AssetsDeviceContract;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.service.BaseService;


public interface DeviceContractService extends BaseService<AssetsDeviceContract> {
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
	
	/**
	 * 
	 * 
	 * <p>文件上传保存到数据库</p>
	 * @Author: XCL
	 * @CreateDate: 2017年11月18日 下午3:05:19 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年11月18日 下午3:05:19 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param entity:
	 *
	 */
	void save(AssetsDeviceContract entity);
	/**  
	 * 设备合同管理列表查询
	 * @Author: zpty
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	List<AssetsPurch> queryDevicePurch(String officeCode, String page,String rows, String menuAlias);
	
	/**  
	 * 设备合同管理列表查询(总条数)
	 * @Author: zpty
	 * @CreateDate: 2017年11月15日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	int queryTotalPurch(String officeCode);

}
