package cn.honry.assets.assetsRepair.service.impl;

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
import cn.honry.base.bean.model.AssetsDeviceDossier;
import cn.honry.base.bean.model.AssetsDeviceMaintain;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("assetsRepairService")
@Transactional
@SuppressWarnings({ "all" })
public class AssetsRepairServiceImpl implements AssetsRepairService{
	@Autowired
	@Qualifier(value = "assetsRepairDAO")
	private AssetsRepairDAO assetsRepairDAO;

	@Override
	public AssetsDeviceMaintain get(String id) {
		AssetsDeviceMaintain assetsDeviceMaintain =assetsRepairDAO.get(id);
		return assetsDeviceMaintain;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(AssetsDeviceMaintain arg0) {
	}

	@Override
	public void save(AssetsDeviceMaintain assets) {
		assetsRepairDAO.save(assets);
	}

	@Override
	public List<AssetsDeviceMaintain> queryRepairRecode(String page, String rows, String state, String deviceNo)
			throws Exception {
		return assetsRepairDAO.queryRepairRecode(page,rows,state,deviceNo);
	}

	@Override
	public int getTotalList(String page, String rows, String state, String deviceNo) throws Exception {
		return assetsRepairDAO.getTotalList(page,rows,state,deviceNo);
	}

	@Override
	public List<AssetsDeviceMaintain> queryAssetsRepair(String page, String rows, AssetsDeviceMaintain assets)
			throws Exception {
		return assetsRepairDAO.queryAssetsRepair(page,rows,assets);
	}

	@Override
	public int queryTotalRepair(String page, String rows, AssetsDeviceMaintain assets) throws Exception {
		return assetsRepairDAO.queryTotalRepair(page,rows,assets);
	}

}
