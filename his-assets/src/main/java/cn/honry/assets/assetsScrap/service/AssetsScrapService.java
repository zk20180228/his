package cn.honry.assets.assetsScrap.service;

import java.util.List;

import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsDeviceScrap;
import cn.honry.base.service.BaseService;
public interface AssetsScrapService extends BaseService<AssetsDeviceScrap>{

	List<AssetsDeviceScrap> queryRepairRecode(String page, String rows, AssetsDeviceScrap assets)throws Exception;

	int getTotalList(String page, String rows, AssetsDeviceScrap assets)throws Exception;

}
