package cn.honry.assets.assetsRepair.service;

import java.util.List;

import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.service.BaseService;
public interface AssetsRepairService extends BaseService<AssetsDeviceMaintain>{

	void save(AssetsDeviceMaintain assets);

	List<AssetsDeviceMaintain> queryRepairRecode(String page, String rows, String state, String deviceNo) throws Exception;

	int getTotalList(String page, String rows, String state, String deviceNo)throws Exception;

	List<AssetsDeviceMaintain> queryAssetsRepair(String page, String rows, AssetsDeviceMaintain assets)throws Exception;

	int queryTotalRepair(String page, String rows, AssetsDeviceMaintain assets)throws Exception;


}
