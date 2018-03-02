package cn.honry.assets.assetsScrap.dao;

import java.util.List;

import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsDeviceScrap;
import cn.honry.base.dao.EntityDao;


@SuppressWarnings({"all"})
public interface AssetsScrapDAO extends EntityDao<AssetsDeviceScrap>{

	List<AssetsDeviceScrap> queryRepairRecode(String page, String rows, AssetsDeviceScrap assets)throws Exception;

	int getTotalList(String page, String rows, AssetsDeviceScrap assets)throws Exception;

}
