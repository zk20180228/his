package cn.honry.assets.assetsScrap.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.assetsPurchase.dao.AssetsPurchaseDAO;
import cn.honry.assets.assetsPurchase.service.AssetsPurchaseService;
import cn.honry.assets.assetsPurchase.vo.AssetsPurchplanVo;
import cn.honry.assets.assetsRepair.dao.AssetsRepairDAO;
import cn.honry.assets.assetsRepair.service.AssetsRepairService;
import cn.honry.assets.assetsScrap.dao.AssetsScrapDAO;
import cn.honry.assets.assetsScrap.service.AssetsScrapService;
import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsDeviceScrap;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("assetsScrapService")
@Transactional
@SuppressWarnings({ "all" })
public class AssetsScrapServiceImpl implements AssetsScrapService{
	@Autowired
	@Qualifier(value = "assetsScrapDAO")
	private AssetsScrapDAO assetsScrapDAO;

	@Override
	public void removeUnused(String arg0) {
		
	}


	@Override
	public AssetsDeviceScrap get(String id) {
		AssetsDeviceScrap assetsDeviceScrap =assetsScrapDAO.get(id);
		return assetsDeviceScrap;
	}

	@Override
	public void saveOrUpdate(AssetsDeviceScrap arg0) {
		
	}


	@Override
	public List<AssetsDeviceScrap> queryRepairRecode(String page, String rows, AssetsDeviceScrap assets)
			throws Exception {
		return assetsScrapDAO.queryRepairRecode(page,rows,assets);
	}


	@Override
	public int getTotalList(String page, String rows, AssetsDeviceScrap assets) throws Exception {
		return assetsScrapDAO.getTotalList(page,rows,assets);

	}

}
