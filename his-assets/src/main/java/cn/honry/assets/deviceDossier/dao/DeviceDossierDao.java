package cn.honry.assets.deviceDossier.dao;

import java.util.List;

import cn.honry.assets.deviceDossier.vo.DeviceDossierVo;
import cn.honry.base.bean.model.DrugAdjustPriceInfo;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.dao.EntityDao;

@SuppressWarnings({"all"})
public interface DeviceDossierDao extends EntityDao<AssetsDeviceDossier>{
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
	 * 查出所有数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceDossier> findAll();
	/**
	 * 根据厂商名称查询模糊
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @return
	 */
	List<AssetsDeviceDossier> findbyName(String name);
	
	/***
	 * name 验证
	 * @Title: queryListByName 
	 * @author zpty
	 * @date 2017-11-14
	 * @param name
	 * @return List<AssetsDeviceDossier>
	 * @version 1.0
	 */
	List<AssetsDeviceDossier> queryListByName(String name);
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
	void disableDeviceDossier(String id);
	/**
	 *  启用数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	void enableDeviceDossier(String id);
	int getTotal(String page, String rows, AssetsDeviceDossier assets, String state)throws Exception;
	List<AssetsDeviceDossier> queryAllAssetsByData(String page, String rows, AssetsDeviceDossier assets, String state)throws Exception;
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
	List<AssetsDeviceDossier> queryDeviceUseView(String deviceCode,AssetsDeviceDossier deviceDossier);
	/**
	 *  获取所有符合条件的数据的总数
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param DeviceDossier
	 * @return
	 */
	int getDeviceDossierCount(String deviceCode,AssetsDeviceDossier deviceDossier);
	
	/**
	 *  维修数据
	 * @author zpty
	 * @date 2017-11-14
	 * @version 1.0
	 * @param Depot
	 * @return
	 */
	void repairMyUse(String id);
}
