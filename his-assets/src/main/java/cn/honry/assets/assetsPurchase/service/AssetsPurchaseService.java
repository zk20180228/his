package cn.honry.assets.assetsPurchase.service;

import java.util.List;
import java.util.Map;

import cn.honry.assets.assetsPurchase.vo.AssetsPurchplanVo;
import cn.honry.base.bean.model.AssetsPurch;
import cn.honry.base.bean.model.AssetsPurchplan;
import cn.honry.base.service.BaseService;
/**
 *  
 * @Description：  业务变更Service层
 * @Author：lyy
 * @CreateDate：2015-10-23 下午05:27:48  
 * @Modifier：lyy
 * @ModifyDate：2015-10-23 下午05:27:48  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface AssetsPurchaseService extends BaseService<AssetsPurchplan>{

	void saveOrUpdateeditInfo(AssetsPurchplan assetsPurchplan) throws Exception;

	List<AssetsPurchplan> queryAllAssetsByData(String page, String rows, AssetsPurchplan assets,String state)throws Exception;

	int getTotalList(String page, String rows, AssetsPurchplan assets,String state)throws Exception;
	void stopAssets(String id, String flag);
	void del(String id);
	void passAssets(String id)throws Exception;
	void noPassAssets(String id, String reason)throws Exception;

	AssetsPurchplan seeReason(String id);

	Map<String, Object> querySPAssetsByData(String page, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd);

	int getSPTotalList(String page, String rows, AssetsPurchplan assets, String state, String timeBegin, String timeEnd);

	List<AssetsPurchplan> queryAllAssetsStat(String page, String rows, AssetsPurchplan assets);

	int getTotalListStat(AssetsPurchplan assets);
	/**  
	 * 
	 * 通过设备代码查询设备采购计划
	 * @Author: huzhenguo
	 * @CreateDate: 2017年12月7日 下午2:47:24 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年12月7日 下午2:47:24 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	int queryPlan(AssetsPurch assets);
}
