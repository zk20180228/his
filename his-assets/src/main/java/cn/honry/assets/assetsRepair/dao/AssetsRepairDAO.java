package cn.honry.assets.assetsRepair.dao;

import java.util.List;

import cn.honry.assets.assetsPurchase.vo.AssetsPurchplanVo;
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.FinanceUsergroup;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.EntityDao;


@SuppressWarnings({"all"})
public interface AssetsRepairDAO extends EntityDao<AssetsDeviceMaintain>{

	List<AssetsDeviceMaintain> queryRepairRecode(String page, String rows, String state, String deviceNo) throws Exception;

	int getTotalList(String page, String rows, String state, String deviceNo)throws Exception;

	List<AssetsDeviceMaintain> queryAssetsRepair(String page, String rows, AssetsDeviceMaintain assets)throws Exception;

	int queryTotalRepair(String page, String rows, AssetsDeviceMaintain assets)throws Exception;


}
