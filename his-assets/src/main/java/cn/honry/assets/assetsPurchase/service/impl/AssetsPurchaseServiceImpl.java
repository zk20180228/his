package cn.honry.assets.assetsPurchase.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.assets.assetsPurchase.dao.AssetsPurchaseDAO;
import cn.honry.assets.assetsPurchase.service.AssetsPurchaseService;
import cn.honry.assets.assetsPurchase.vo.AssetsPurchplanVo;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("assetsPurchaseService")
@Transactional
@SuppressWarnings({ "all" })
public class AssetsPurchaseServiceImpl implements AssetsPurchaseService{
	@Autowired
	@Qualifier(value = "assetsPurchaseDAO")
	private AssetsPurchaseDAO assetsPurchaseDAO;
	@Override
	public AssetsPurchplan get(String id) {
		AssetsPurchplan assets = assetsPurchaseDAO.get(id);
		return assets;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(AssetsPurchplan arg0) {
		
	}

	@Override
	public void saveOrUpdateeditInfo(AssetsPurchplan assetsPurchplan) throws Exception{
		if(StringUtils.isBlank(assetsPurchplan.getId())){
			assetsPurchplan.setId(null);
			assetsPurchplan.setApplDate(new Date());
			assetsPurchplan.setApplAcc(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assetsPurchplan.setApplName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			assetsPurchplan.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assetsPurchplan.setCreateTime(DateUtils.getCurrentTime());
			assetsPurchplan.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
		}else{
			assetsPurchplan.setApplDate(new Date());
			assetsPurchplan.setApplAcc(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assetsPurchplan.setApplName(ShiroSessionUtils.getCurrentUserFromShiroSession().getName());
			assetsPurchplan.setUpdateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
			assetsPurchplan.setUpdateTime(DateUtils.getCurrentTime());
		}
		assetsPurchaseDAO.save(assetsPurchplan);
	}

	@Override
	public List<AssetsPurchplan> queryAllAssetsByData(String page, String rows, AssetsPurchplan assets,String state)
			throws Exception {
		List<AssetsPurchplan> assetsList=assetsPurchaseDAO.queryAllAssetsByData(page,rows,assets,state);
		return assetsList;
	}

	@Override
	public int getTotalList(String page, String rows, AssetsPurchplan assets,String state) throws Exception {
		return assetsPurchaseDAO.getTotal(page,rows,assets,state);
	}

	@Override
	public void stopAssets(String id, String flag) {
		assetsPurchaseDAO.stopAssets(id,flag);
	}

	@Override
	public void del(String id) {
		assetsPurchaseDAO.del(id,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
	}

	@Override
	public void passAssets(String id) throws Exception {
		assetsPurchaseDAO.passAssets(id);
	}

	@Override
	public void noPassAssets(String id, String reason) throws Exception {
		assetsPurchaseDAO.noPassAssets(id,reason);
	}

	@Override
	public AssetsPurchplan seeReason(String id) {
		return assetsPurchaseDAO.seeReason(id);
	}

	@Override
	public Map<String, Object> querySPAssetsByData(String page, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd){
		Map<String, Object> map = new HashMap<String, Object>();
		map=assetsPurchaseDAO.querySPAssetsByData(page,rows,assets,state,timeBegin,timeEnd);
		return map;
	}

	@Override
	public int getSPTotalList(String page, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd){
		return assetsPurchaseDAO.getSPTotalList(page,rows,assets,state,timeBegin,timeEnd);
	}

	@Override
	public List<AssetsPurchplan> queryAllAssetsStat(String page, String rows, AssetsPurchplan assets) {
		return assetsPurchaseDAO.queryAllAssetsStat(page,rows,assets);
	}

	@Override
	public int getTotalListStat(AssetsPurchplan assets) {
		return assetsPurchaseDAO.queryAllAssetsStat(assets);
	}

	@Override
	public int queryPlan(AssetsPurch assets) {
		return assetsPurchaseDAO.queryPlan(assets);
	}

}
