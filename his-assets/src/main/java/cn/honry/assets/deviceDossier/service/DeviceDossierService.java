package cn.honry.assets.deviceDossier.service;

import java.util.List;

import cn.honry.assets.deviceDossier.vo.DeviceDossierVo;
import cn.honry.base.bean.model.AssetsDepot;
import cn.honry.base.bean.model.AssetsDevice;
import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.service.BaseService;

@SuppressWarnings({"all"})
public interface DeviceDossierService  extends BaseService<AssetsDeviceDossier> {
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	List<AssetsDeviceDossier> queryDeviceDossier(AssetsDeviceDossier DeviceDossier);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	int getDeviceDossierCount(AssetsDeviceDossier DeviceDossier);
	/**
	 *  增加数据/修改数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	void saveOrupdata(AssetsDeviceDossier deviceDossier);
	/**
	 *  删除数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	void delete(AssetsDeviceDossier deviceDossier);
	/**
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceDossier> findAll();
	
	/***
	 * 数据唯一验证
	 * @Title: verification 
	 * @author zpty
	 * @date 2017-11-14
	 * @param deviceDossier
	 * @return String 标识，T & F
	 * @version 1.0
	 */
	String verification(AssetsDeviceDossier deviceDossier);
	/**
	 *  查询所有符合条件的数据,渲染用
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	List<AssetsDeviceDossier> queryDeviceDossierforXR();
	/**
	 *  停用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	void disableDeviceDossier(AssetsDeviceDossier deviceDossier);
	/**
	 *  启用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	void enableDeviceDossier(AssetsDeviceDossier deviceDossier);

	List<AssetsDeviceDossier> queryAllAssetsByData(String page, String rows, AssetsDeviceDossier assets, String state)throws Exception;
	int getTotalList(String page, String rows, AssetsDeviceDossier assets, String state)throws Exception;
	void saveRepairReson(AssetsDeviceDossier deviceDossier, String repairReson)throws Exception;
	void saveRepairScrap(AssetsDeviceDossier deviceDossier, String assetScrap)throws Exception;

	/**  
	 * 
	 * 设备资产list
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:09:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:09:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	List<DeviceDossierVo> queryDeviceDossierValue(AssetsDeviceDossier DeviceDossier) throws Exception;
	/**  
	 * 
	 * 设备资产Total
	 * @Author: huzhenguo
	 * @CreateDate: 2017年11月16日 上午11:10:42 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年11月16日 上午11:10:42 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	public Integer queryDeviceDossierTotal(AssetsDeviceDossier DeviceDossier) throws Exception; 

	
	//***************************************************以下是设备领用管理******************************************************//
	
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	List<AssetsDeviceDossier> queryDeviceDossierMyUse(AssetsDeviceDossier DeviceDossier);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	int getDeviceDossierCountMyUse(AssetsDeviceDossier DeviceDossier);
	
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	List<AssetsDevice> queryDeviceDossierReceive(AssetsDeviceDossier DeviceDossier);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	int getDeviceDossierCountReceive(AssetsDeviceDossier DeviceDossier);
	/**
	 *  保存领用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	void saveDeviceUse(AssetsDeviceDossier deviceDossier);
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	List<AssetsDeviceDossier> queryDeviceUseView(String deviceCode,AssetsDeviceDossier deviceDossier);
	/**
	 *  查询所有符合条件的数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	int getDeviceCountUseView(String deviceCode,AssetsDeviceDossier deviceDossier);
	/**
	 *  维修数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	void repairMyUse(AssetsDeviceDossier deviceDossier);
}
